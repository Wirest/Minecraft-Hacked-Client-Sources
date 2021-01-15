/*     */ package io.netty.resolver.dns;
/*     */ 
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.EventLoop;
/*     */ import io.netty.channel.socket.DatagramChannel;
/*     */ import io.netty.handler.codec.dns.DnsQuery;
/*     */ import io.netty.handler.codec.dns.DnsQueryHeader;
/*     */ import io.netty.handler.codec.dns.DnsQuestion;
/*     */ import io.netty.handler.codec.dns.DnsResource;
/*     */ import io.netty.handler.codec.dns.DnsResponse;
/*     */ import io.netty.handler.codec.dns.DnsType;
/*     */ import io.netty.util.concurrent.Promise;
/*     */ import io.netty.util.concurrent.ScheduledFuture;
/*     */ import io.netty.util.internal.OneTimeTask;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import io.netty.util.internal.ThreadLocalRandom;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.Iterator;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.atomic.AtomicReferenceArray;
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
/*     */ final class DnsQueryContext
/*     */ {
/*  43 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(DnsQueryContext.class);
/*     */   
/*     */   private final DnsNameResolver parent;
/*     */   
/*     */   private final Promise<DnsResponse> promise;
/*     */   
/*     */   private final int id;
/*     */   
/*     */   private final DnsQuestion question;
/*     */   private final DnsResource optResource;
/*     */   private final Iterator<InetSocketAddress> nameServerAddresses;
/*     */   private final boolean recursionDesired;
/*     */   private final int maxTries;
/*     */   private int remainingTries;
/*     */   private volatile ScheduledFuture<?> timeoutFuture;
/*     */   private StringBuilder trace;
/*     */   
/*     */   DnsQueryContext(DnsNameResolver parent, Iterable<InetSocketAddress> nameServerAddresses, DnsQuestion question, Promise<DnsResponse> promise)
/*     */   {
/*  62 */     this.parent = parent;
/*  63 */     this.promise = promise;
/*  64 */     this.question = question;
/*     */     
/*  66 */     this.id = allocateId();
/*  67 */     this.recursionDesired = parent.isRecursionDesired();
/*  68 */     this.maxTries = parent.maxTriesPerQuery();
/*  69 */     this.remainingTries = this.maxTries;
/*  70 */     this.optResource = new DnsResource("", DnsType.OPT, parent.maxPayloadSizeClass(), 0L, Unpooled.EMPTY_BUFFER);
/*     */     
/*  72 */     this.nameServerAddresses = nameServerAddresses.iterator();
/*     */   }
/*     */   
/*     */   private int allocateId() {
/*  76 */     int id = ThreadLocalRandom.current().nextInt(this.parent.promises.length());
/*  77 */     int maxTries = this.parent.promises.length() << 1;
/*  78 */     int tries = 0;
/*     */     do {
/*  80 */       if (this.parent.promises.compareAndSet(id, null, this)) {
/*  81 */         return id;
/*     */       }
/*     */       
/*  84 */       id = id + 1 & 0xFFFF;
/*     */       
/*  86 */       tries++; } while (tries < maxTries);
/*  87 */     throw new IllegalStateException("query ID space exhausted: " + this.question);
/*     */   }
/*     */   
/*     */ 
/*     */   Promise<DnsResponse> promise()
/*     */   {
/*  93 */     return this.promise;
/*     */   }
/*     */   
/*     */   DnsQuestion question() {
/*  97 */     return this.question;
/*     */   }
/*     */   
/*     */   ScheduledFuture<?> timeoutFuture() {
/* 101 */     return this.timeoutFuture;
/*     */   }
/*     */   
/*     */   void query() {
/* 105 */     DnsQuestion question = this.question;
/*     */     
/* 107 */     if ((this.remainingTries <= 0) || (!this.nameServerAddresses.hasNext())) {
/* 108 */       this.parent.promises.set(this.id, null);
/*     */       
/* 110 */       int tries = this.maxTries - this.remainingTries;
/*     */       UnknownHostException cause;
/* 112 */       UnknownHostException cause; if (tries > 1) {
/* 113 */         cause = new UnknownHostException("failed to resolve " + question + " after " + tries + " attempts:" + this.trace);
/*     */       }
/*     */       else
/*     */       {
/* 117 */         cause = new UnknownHostException("failed to resolve " + question + ':' + this.trace);
/*     */       }
/*     */       
/* 120 */       cache(question, cause);
/* 121 */       this.promise.tryFailure(cause);
/* 122 */       return;
/*     */     }
/*     */     
/* 125 */     this.remainingTries -= 1;
/*     */     
/* 127 */     InetSocketAddress nameServerAddr = (InetSocketAddress)this.nameServerAddresses.next();
/* 128 */     DnsQuery query = new DnsQuery(this.id, nameServerAddr);
/* 129 */     query.addQuestion(question);
/* 130 */     query.header().setRecursionDesired(this.recursionDesired);
/* 131 */     query.addAdditionalResource(this.optResource);
/*     */     
/* 133 */     if (logger.isDebugEnabled()) {
/* 134 */       logger.debug("{} WRITE: [{}: {}], {}", new Object[] { this.parent.ch, Integer.valueOf(this.id), nameServerAddr, question });
/*     */     }
/*     */     
/* 137 */     sendQuery(query, nameServerAddr);
/*     */   }
/*     */   
/*     */   private void sendQuery(final DnsQuery query, final InetSocketAddress nameServerAddr) {
/* 141 */     if (this.parent.bindFuture.isDone()) {
/* 142 */       writeQuery(query, nameServerAddr);
/*     */     } else {
/* 144 */       this.parent.bindFuture.addListener(new ChannelFutureListener()
/*     */       {
/*     */         public void operationComplete(ChannelFuture future) throws Exception {
/* 147 */           if (future.isSuccess()) {
/* 148 */             DnsQueryContext.this.writeQuery(query, nameServerAddr);
/*     */           } else {
/* 150 */             DnsQueryContext.this.promise.tryFailure(future.cause());
/*     */           }
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */   private void writeQuery(DnsQuery query, final InetSocketAddress nameServerAddr) {
/* 158 */     final ChannelFuture writeFuture = this.parent.ch.writeAndFlush(query);
/* 159 */     if (writeFuture.isDone()) {
/* 160 */       onQueryWriteCompletion(writeFuture, nameServerAddr);
/*     */     } else {
/* 162 */       writeFuture.addListener(new ChannelFutureListener()
/*     */       {
/*     */         public void operationComplete(ChannelFuture future) throws Exception {
/* 165 */           DnsQueryContext.this.onQueryWriteCompletion(writeFuture, nameServerAddr);
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */   private void onQueryWriteCompletion(ChannelFuture writeFuture, final InetSocketAddress nameServerAddr) {
/* 172 */     if (!writeFuture.isSuccess()) {
/* 173 */       retry(nameServerAddr, "failed to send a query: " + writeFuture.cause());
/* 174 */       return;
/*     */     }
/*     */     
/*     */ 
/* 178 */     final long queryTimeoutMillis = this.parent.queryTimeoutMillis();
/* 179 */     if (queryTimeoutMillis > 0L) {
/* 180 */       this.timeoutFuture = this.parent.ch.eventLoop().schedule(new OneTimeTask()
/*     */       {
/*     */         public void run() {
/* 183 */           if (DnsQueryContext.this.promise.isDone())
/*     */           {
/* 185 */             return;
/*     */           }
/*     */           
/* 188 */           DnsQueryContext.this.retry(nameServerAddr, "query timed out after " + queryTimeoutMillis + " milliseconds"); } }, queryTimeoutMillis, TimeUnit.MILLISECONDS);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   void retry(InetSocketAddress nameServerAddr, String message)
/*     */   {
/* 195 */     if (this.promise.isCancelled()) {
/* 196 */       return;
/*     */     }
/*     */     
/* 199 */     if (this.trace == null) {
/* 200 */       this.trace = new StringBuilder(128);
/*     */     }
/*     */     
/* 203 */     this.trace.append(StringUtil.NEWLINE);
/* 204 */     this.trace.append("\tfrom ");
/* 205 */     this.trace.append(nameServerAddr);
/* 206 */     this.trace.append(": ");
/* 207 */     this.trace.append(message);
/* 208 */     query();
/*     */   }
/*     */   
/*     */   private void cache(DnsQuestion question, Throwable cause) {
/* 212 */     int negativeTtl = this.parent.negativeTtl();
/* 213 */     if (negativeTtl == 0) {
/* 214 */       return;
/*     */     }
/*     */     
/* 217 */     this.parent.cache(question, new DnsNameResolver.DnsCacheEntry(cause), negativeTtl);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\resolver\dns\DnsQueryContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */