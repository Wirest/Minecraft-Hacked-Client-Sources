/*     */ package io.netty.resolver.dns;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.socket.InternetProtocolFamily;
/*     */ import io.netty.handler.codec.dns.DnsClass;
/*     */ import io.netty.handler.codec.dns.DnsQuestion;
/*     */ import io.netty.handler.codec.dns.DnsResource;
/*     */ import io.netty.handler.codec.dns.DnsResponse;
/*     */ import io.netty.handler.codec.dns.DnsType;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.concurrent.Future;
/*     */ import io.netty.util.concurrent.FutureListener;
/*     */ import io.netty.util.concurrent.Promise;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.net.Inet4Address;
/*     */ import java.net.Inet6Address;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class DnsNameResolverContext
/*     */ {
/*     */   private static final int INADDRSZ4 = 4;
/*     */   private static final int INADDRSZ6 = 16;
/*  54 */   private static final FutureListener<DnsResponse> RELEASE_RESPONSE = new FutureListener()
/*     */   {
/*     */     public void operationComplete(Future<DnsResponse> future) {
/*  57 */       if (future.isSuccess()) {
/*  58 */         ((DnsResponse)future.getNow()).release();
/*     */       }
/*     */     }
/*     */   };
/*     */   
/*     */   private final DnsNameResolver parent;
/*     */   
/*     */   private final Promise<InetSocketAddress> promise;
/*     */   private final String hostname;
/*     */   private final int port;
/*     */   private final int maxAllowedQueries;
/*     */   private final InternetProtocolFamily[] resolveAddressTypes;
/*  70 */   private final Set<Future<DnsResponse>> queriesInProgress = Collections.newSetFromMap(new IdentityHashMap());
/*     */   private List<InetAddress> resolvedAddresses;
/*     */   private StringBuilder trace;
/*     */   private int allowedQueries;
/*     */   private boolean triedCNAME;
/*     */   
/*     */   DnsNameResolverContext(DnsNameResolver parent, String hostname, int port, Promise<InetSocketAddress> promise)
/*     */   {
/*  78 */     this.parent = parent;
/*  79 */     this.promise = promise;
/*  80 */     this.hostname = hostname;
/*  81 */     this.port = port;
/*     */     
/*  83 */     this.maxAllowedQueries = parent.maxQueriesPerResolve();
/*  84 */     this.resolveAddressTypes = parent.resolveAddressTypesUnsafe();
/*  85 */     this.allowedQueries = this.maxAllowedQueries;
/*     */   }
/*     */   
/*     */   void resolve() {
/*  89 */     for (InternetProtocolFamily f : this.resolveAddressTypes) {
/*     */       DnsType type;
/*  91 */       switch (f) {
/*     */       case IPv4: 
/*  93 */         type = DnsType.A;
/*  94 */         break;
/*     */       case IPv6: 
/*  96 */         type = DnsType.AAAA;
/*  97 */         break;
/*     */       default: 
/*  99 */         throw new Error();
/*     */       }
/*     */       
/* 102 */       query(this.parent.nameServerAddresses, new DnsQuestion(this.hostname, type));
/*     */     }
/*     */   }
/*     */   
/*     */   private void query(Iterable<InetSocketAddress> nameServerAddresses, final DnsQuestion question) {
/* 107 */     if ((this.allowedQueries == 0) || (this.promise.isCancelled())) {
/* 108 */       return;
/*     */     }
/*     */     
/* 111 */     this.allowedQueries -= 1;
/*     */     
/* 113 */     Future<DnsResponse> f = this.parent.query(nameServerAddresses, question);
/* 114 */     this.queriesInProgress.add(f);
/*     */     
/* 116 */     f.addListener(new FutureListener()
/*     */     {
/*     */       public void operationComplete(Future<DnsResponse> future) throws Exception {
/* 119 */         DnsNameResolverContext.this.queriesInProgress.remove(future);
/*     */         
/* 121 */         if (DnsNameResolverContext.this.promise.isDone()) {
/* 122 */           return;
/*     */         }
/*     */         try
/*     */         {
/* 126 */           if (future.isSuccess()) {
/* 127 */             DnsNameResolverContext.this.onResponse(question, (DnsResponse)future.getNow());
/*     */           } else {
/* 129 */             DnsNameResolverContext.this.addTrace(future.cause());
/*     */           }
/*     */         } finally {
/* 132 */           DnsNameResolverContext.this.tryToFinishResolve();
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   void onResponse(DnsQuestion question, DnsResponse response) {
/* 139 */     DnsType type = question.type();
/*     */     try {
/* 141 */       if ((type == DnsType.A) || (type == DnsType.AAAA)) {
/* 142 */         onResponseAorAAAA(type, question, response);
/* 143 */       } else if (type == DnsType.CNAME) {
/* 144 */         onResponseCNAME(question, response);
/*     */       }
/*     */     } finally {
/* 147 */       ReferenceCountUtil.safeRelease(response);
/*     */     }
/*     */   }
/*     */   
/*     */   private void onResponseAorAAAA(DnsType qType, DnsQuestion question, DnsResponse response)
/*     */   {
/* 153 */     Map<String, String> cnames = buildAliasMap(response);
/*     */     
/* 155 */     boolean found = false;
/* 156 */     for (DnsResource r : response.answers()) {
/* 157 */       DnsType type = r.type();
/* 158 */       if ((type == DnsType.A) || (type == DnsType.AAAA))
/*     */       {
/*     */ 
/*     */ 
/* 162 */         String qName = question.name().toLowerCase(Locale.US);
/* 163 */         String rName = r.name().toLowerCase(Locale.US);
/*     */         
/*     */ 
/* 166 */         if (!rName.equals(qName))
/*     */         {
/* 168 */           String resolved = qName;
/*     */           do {
/* 170 */             resolved = (String)cnames.get(resolved);
/* 171 */           } while ((!rName.equals(resolved)) && 
/*     */           
/*     */ 
/* 174 */             (resolved != null));
/*     */           
/* 176 */           if (resolved == null) {}
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 181 */           ByteBuf content = r.content();
/* 182 */           int contentLen = content.readableBytes();
/* 183 */           if ((contentLen == 4) || (contentLen == 16))
/*     */           {
/*     */ 
/*     */ 
/* 187 */             byte[] addrBytes = new byte[contentLen];
/* 188 */             content.getBytes(content.readerIndex(), addrBytes);
/*     */             try
/*     */             {
/* 191 */               InetAddress resolved = InetAddress.getByAddress(this.hostname, addrBytes);
/* 192 */               if (this.resolvedAddresses == null) {
/* 193 */                 this.resolvedAddresses = new ArrayList();
/*     */               }
/* 195 */               this.resolvedAddresses.add(resolved);
/* 196 */               found = true;
/*     */             }
/*     */             catch (UnknownHostException e) {
/* 199 */               throw new Error(e);
/*     */             }
/*     */           }
/*     */         } } }
/* 203 */     if (found) {
/* 204 */       return;
/*     */     }
/*     */     
/* 207 */     addTrace(response.sender(), "no matching " + qType + " record found");
/*     */     
/*     */ 
/* 210 */     if (!cnames.isEmpty()) {
/* 211 */       onResponseCNAME(question, response, cnames, false);
/*     */     }
/*     */   }
/*     */   
/*     */   private void onResponseCNAME(DnsQuestion question, DnsResponse response) {
/* 216 */     onResponseCNAME(question, response, buildAliasMap(response), true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void onResponseCNAME(DnsQuestion question, DnsResponse response, Map<String, String> cnames, boolean trace)
/*     */   {
/* 223 */     String name = question.name().toLowerCase(Locale.US);
/* 224 */     String resolved = name;
/* 225 */     boolean found = false;
/*     */     for (;;) {
/* 227 */       String next = (String)cnames.get(resolved);
/* 228 */       if (next == null) break;
/* 229 */       found = true;
/* 230 */       resolved = next;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 236 */     if (found) {
/* 237 */       followCname(response.sender(), name, resolved);
/* 238 */     } else if (trace) {
/* 239 */       addTrace(response.sender(), "no matching CNAME record found");
/*     */     }
/*     */   }
/*     */   
/*     */   private static Map<String, String> buildAliasMap(DnsResponse response) {
/* 244 */     Map<String, String> cnames = null;
/* 245 */     for (DnsResource r : response.answers()) {
/* 246 */       DnsType type = r.type();
/* 247 */       if (type == DnsType.CNAME)
/*     */       {
/*     */ 
/*     */ 
/* 251 */         String content = decodeDomainName(r.content());
/* 252 */         if (content != null)
/*     */         {
/*     */ 
/*     */ 
/* 256 */           if (cnames == null) {
/* 257 */             cnames = new HashMap();
/*     */           }
/*     */           
/* 260 */           cnames.put(r.name().toLowerCase(Locale.US), content.toLowerCase(Locale.US));
/*     */         }
/*     */       } }
/* 263 */     return cnames != null ? cnames : Collections.emptyMap();
/*     */   }
/*     */   
/*     */   void tryToFinishResolve() {
/* 267 */     if (!this.queriesInProgress.isEmpty())
/*     */     {
/* 269 */       if (gotPreferredAddress())
/*     */       {
/* 271 */         finishResolve();
/*     */       }
/*     */       
/*     */ 
/* 275 */       return;
/*     */     }
/*     */     
/*     */ 
/* 279 */     if (this.resolvedAddresses == null)
/*     */     {
/* 281 */       if (!this.triedCNAME)
/*     */       {
/* 283 */         this.triedCNAME = true;
/* 284 */         query(this.parent.nameServerAddresses, new DnsQuestion(this.hostname, DnsType.CNAME, DnsClass.IN));
/* 285 */         return;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 290 */     finishResolve();
/*     */   }
/*     */   
/*     */   private boolean gotPreferredAddress() {
/* 294 */     if (this.resolvedAddresses == null) {
/* 295 */       return false;
/*     */     }
/*     */     
/* 298 */     int size = this.resolvedAddresses.size();
/* 299 */     switch (this.resolveAddressTypes[0]) {
/*     */     case IPv4: 
/* 301 */       for (int i = 0; i < size; i++) {
/* 302 */         if ((this.resolvedAddresses.get(i) instanceof Inet4Address)) {
/* 303 */           return true;
/*     */         }
/*     */       }
/* 306 */       break;
/*     */     case IPv6: 
/* 308 */       for (int i = 0; i < size; i++) {
/* 309 */         if ((this.resolvedAddresses.get(i) instanceof Inet6Address)) {
/* 310 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     
/* 316 */     return false;
/*     */   }
/*     */   
/*     */   private void finishResolve() { Iterator<Future<DnsResponse>> i;
/* 320 */     if (!this.queriesInProgress.isEmpty())
/*     */     {
/* 322 */       for (i = this.queriesInProgress.iterator(); i.hasNext();) {
/* 323 */         Future<DnsResponse> f = (Future)i.next();
/* 324 */         i.remove();
/*     */         
/* 326 */         if (!f.cancel(false)) {
/* 327 */           f.addListener(RELEASE_RESPONSE);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 332 */     if (this.resolvedAddresses != null)
/*     */     {
/* 334 */       for (InternetProtocolFamily f : this.resolveAddressTypes) {
/* 335 */         switch (f) {
/*     */         case IPv4: 
/* 337 */           if (finishResolveWithIPv4()) {
/*     */             return;
/*     */           }
/*     */           break;
/*     */         case IPv6: 
/* 342 */           if (finishResolveWithIPv6()) {
/*     */             return;
/*     */           }
/*     */           
/*     */           break;
/*     */         }
/*     */         
/*     */       }
/*     */     }
/* 351 */     int tries = this.maxAllowedQueries - this.allowedQueries;
/*     */     UnknownHostException cause;
/* 353 */     UnknownHostException cause; if (tries > 1) {
/* 354 */       cause = new UnknownHostException("failed to resolve " + this.hostname + " after " + tries + " queries:" + this.trace);
/*     */     }
/*     */     else
/*     */     {
/* 358 */       cause = new UnknownHostException("failed to resolve " + this.hostname + ':' + this.trace);
/*     */     }
/*     */     
/* 361 */     this.promise.tryFailure(cause);
/*     */   }
/*     */   
/*     */   private boolean finishResolveWithIPv4() {
/* 365 */     List<InetAddress> resolvedAddresses = this.resolvedAddresses;
/* 366 */     int size = resolvedAddresses.size();
/*     */     
/* 368 */     for (int i = 0; i < size; i++) {
/* 369 */       InetAddress a = (InetAddress)resolvedAddresses.get(i);
/* 370 */       if ((a instanceof Inet4Address)) {
/* 371 */         this.promise.trySuccess(new InetSocketAddress(a, this.port));
/* 372 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 376 */     return false;
/*     */   }
/*     */   
/*     */   private boolean finishResolveWithIPv6() {
/* 380 */     List<InetAddress> resolvedAddresses = this.resolvedAddresses;
/* 381 */     int size = resolvedAddresses.size();
/*     */     
/* 383 */     for (int i = 0; i < size; i++) {
/* 384 */       InetAddress a = (InetAddress)resolvedAddresses.get(i);
/* 385 */       if ((a instanceof Inet6Address)) {
/* 386 */         this.promise.trySuccess(new InetSocketAddress(a, this.port));
/* 387 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 391 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static String decodeDomainName(ByteBuf buf)
/*     */   {
/* 398 */     buf.markReaderIndex();
/*     */     try {
/* 400 */       int position = -1;
/* 401 */       int checked = 0;
/* 402 */       int length = buf.writerIndex();
/* 403 */       StringBuilder name = new StringBuilder(64);
/* 404 */       for (int len = buf.readUnsignedByte(); (buf.isReadable()) && (len != 0); len = buf.readUnsignedByte()) {
/* 405 */         boolean pointer = (len & 0xC0) == 192;
/* 406 */         if (pointer) {
/* 407 */           if (position == -1) {
/* 408 */             position = buf.readerIndex() + 1;
/*     */           }
/* 410 */           buf.readerIndex((len & 0x3F) << 8 | buf.readUnsignedByte());
/*     */           
/* 412 */           checked += 2;
/* 413 */           if (checked >= length)
/*     */           {
/* 415 */             return null;
/*     */           }
/*     */         } else {
/* 418 */           name.append(buf.toString(buf.readerIndex(), len, CharsetUtil.UTF_8)).append('.');
/* 419 */           buf.skipBytes(len);
/*     */         }
/*     */       }
/*     */       
/* 423 */       if (position != -1) {
/* 424 */         buf.readerIndex(position);
/*     */       }
/*     */       
/* 427 */       if (name.length() == 0) {
/* 428 */         return null;
/*     */       }
/*     */       
/* 431 */       return name.substring(0, name.length() - 1);
/*     */     } finally {
/* 433 */       buf.resetReaderIndex();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void followCname(InetSocketAddress nameServerAddr, String name, String cname)
/*     */   {
/* 440 */     if (this.trace == null) {
/* 441 */       this.trace = new StringBuilder(128);
/*     */     }
/*     */     
/* 444 */     this.trace.append(StringUtil.NEWLINE);
/* 445 */     this.trace.append("\tfrom ");
/* 446 */     this.trace.append(nameServerAddr);
/* 447 */     this.trace.append(": ");
/* 448 */     this.trace.append(name);
/* 449 */     this.trace.append(" CNAME ");
/* 450 */     this.trace.append(cname);
/*     */     
/* 452 */     query(this.parent.nameServerAddresses, new DnsQuestion(cname, DnsType.A, DnsClass.IN));
/* 453 */     query(this.parent.nameServerAddresses, new DnsQuestion(cname, DnsType.AAAA, DnsClass.IN));
/*     */   }
/*     */   
/*     */   private void addTrace(InetSocketAddress nameServerAddr, String msg) {
/* 457 */     if (this.trace == null) {
/* 458 */       this.trace = new StringBuilder(128);
/*     */     }
/*     */     
/* 461 */     this.trace.append(StringUtil.NEWLINE);
/* 462 */     this.trace.append("\tfrom ");
/* 463 */     this.trace.append(nameServerAddr);
/* 464 */     this.trace.append(": ");
/* 465 */     this.trace.append(msg);
/*     */   }
/*     */   
/*     */   private void addTrace(Throwable cause) {
/* 469 */     if (this.trace == null) {
/* 470 */       this.trace = new StringBuilder(128);
/*     */     }
/*     */     
/* 473 */     this.trace.append(StringUtil.NEWLINE);
/* 474 */     this.trace.append("Caused by: ");
/* 475 */     this.trace.append(cause);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\resolver\dns\DnsNameResolverContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */