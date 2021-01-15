/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.handler.codec.AsciiString;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.ReferenceCounted;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpServerUpgradeHandler
/*     */   extends HttpObjectAggregator
/*     */ {
/*     */   private final Map<String, UpgradeCodec> upgradeCodecMap;
/*     */   private final SourceCodec sourceCodec;
/*     */   private boolean handlingUpgrade;
/*     */   
/*     */   public static final class UpgradeEvent
/*     */     implements ReferenceCounted
/*     */   {
/*     */     private final String protocol;
/*     */     private final FullHttpRequest upgradeRequest;
/*     */     
/*     */     private UpgradeEvent(String protocol, FullHttpRequest upgradeRequest)
/*     */     {
/* 100 */       this.protocol = protocol;
/* 101 */       this.upgradeRequest = upgradeRequest;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public String protocol()
/*     */     {
/* 108 */       return this.protocol;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public FullHttpRequest upgradeRequest()
/*     */     {
/* 115 */       return this.upgradeRequest;
/*     */     }
/*     */     
/*     */     public int refCnt()
/*     */     {
/* 120 */       return this.upgradeRequest.refCnt();
/*     */     }
/*     */     
/*     */     public UpgradeEvent retain()
/*     */     {
/* 125 */       this.upgradeRequest.retain();
/* 126 */       return this;
/*     */     }
/*     */     
/*     */     public UpgradeEvent retain(int increment)
/*     */     {
/* 131 */       this.upgradeRequest.retain(increment);
/* 132 */       return this;
/*     */     }
/*     */     
/*     */     public UpgradeEvent touch()
/*     */     {
/* 137 */       this.upgradeRequest.touch();
/* 138 */       return this;
/*     */     }
/*     */     
/*     */     public UpgradeEvent touch(Object hint)
/*     */     {
/* 143 */       this.upgradeRequest.touch(hint);
/* 144 */       return this;
/*     */     }
/*     */     
/*     */     public boolean release()
/*     */     {
/* 149 */       return this.upgradeRequest.release();
/*     */     }
/*     */     
/*     */     public boolean release(int decrement)
/*     */     {
/* 154 */       return this.upgradeRequest.release();
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 159 */       return "UpgradeEvent [protocol=" + this.protocol + ", upgradeRequest=" + this.upgradeRequest + ']';
/*     */     }
/*     */   }
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
/*     */   public HttpServerUpgradeHandler(SourceCodec sourceCodec, Collection<UpgradeCodec> upgradeCodecs, int maxContentLength)
/*     */   {
/* 177 */     super(maxContentLength);
/* 178 */     if (sourceCodec == null) {
/* 179 */       throw new NullPointerException("sourceCodec");
/*     */     }
/* 181 */     if (upgradeCodecs == null) {
/* 182 */       throw new NullPointerException("upgradeCodecs");
/*     */     }
/* 184 */     this.sourceCodec = sourceCodec;
/* 185 */     this.upgradeCodecMap = new LinkedHashMap(upgradeCodecs.size());
/* 186 */     for (UpgradeCodec upgradeCodec : upgradeCodecs) {
/* 187 */       String name = upgradeCodec.protocol().toUpperCase(Locale.US);
/* 188 */       this.upgradeCodecMap.put(name, upgradeCodec);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void decode(ChannelHandlerContext ctx, HttpObject msg, List<Object> out)
/*     */     throws Exception
/*     */   {
/* 196 */     this.handlingUpgrade |= isUpgradeRequest(msg);
/* 197 */     if (!this.handlingUpgrade)
/*     */     {
/* 199 */       ReferenceCountUtil.retain(msg);
/* 200 */       out.add(msg); return;
/*     */     }
/*     */     
/*     */     FullHttpRequest fullRequest;
/*     */     
/* 205 */     if ((msg instanceof FullHttpRequest)) {
/* 206 */       FullHttpRequest fullRequest = (FullHttpRequest)msg;
/* 207 */       ReferenceCountUtil.retain(msg);
/* 208 */       out.add(msg);
/*     */     }
/*     */     else {
/* 211 */       super.decode(ctx, msg, out);
/* 212 */       if (out.isEmpty())
/*     */       {
/* 214 */         return;
/*     */       }
/*     */       
/*     */ 
/* 218 */       assert (out.size() == 1);
/* 219 */       this.handlingUpgrade = false;
/* 220 */       fullRequest = (FullHttpRequest)out.get(0);
/*     */     }
/*     */     
/* 223 */     if (upgrade(ctx, fullRequest))
/*     */     {
/*     */ 
/*     */ 
/* 227 */       out.clear();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean isUpgradeRequest(HttpObject msg)
/*     */   {
/* 238 */     return ((msg instanceof HttpRequest)) && (((HttpRequest)msg).headers().get(HttpHeaderNames.UPGRADE) != null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean upgrade(final ChannelHandlerContext ctx, final FullHttpRequest request)
/*     */   {
/* 251 */     CharSequence upgradeHeader = (CharSequence)request.headers().get(HttpHeaderNames.UPGRADE);
/* 252 */     final UpgradeCodec upgradeCodec = selectUpgradeCodec(upgradeHeader);
/* 253 */     if (upgradeCodec == null)
/*     */     {
/* 255 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 259 */     CharSequence connectionHeader = (CharSequence)request.headers().get(HttpHeaderNames.CONNECTION);
/* 260 */     if (connectionHeader == null) {
/* 261 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 265 */     Collection<String> requiredHeaders = upgradeCodec.requiredUpgradeHeaders();
/* 266 */     Set<CharSequence> values = splitHeader(connectionHeader);
/* 267 */     if ((!values.contains(HttpHeaderNames.UPGRADE)) || (!values.containsAll(requiredHeaders))) {
/* 268 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 272 */     for (String requiredHeader : requiredHeaders) {
/* 273 */       if (!request.headers().contains(requiredHeader)) {
/* 274 */         return false;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 279 */     final UpgradeEvent event = new UpgradeEvent(upgradeCodec.protocol(), request, null);
/*     */     
/*     */ 
/*     */ 
/* 283 */     final FullHttpResponse upgradeResponse = createUpgradeResponse(upgradeCodec);
/* 284 */     upgradeCodec.prepareUpgradeResponse(ctx, request, upgradeResponse);
/* 285 */     ctx.writeAndFlush(upgradeResponse).addListener(new ChannelFutureListener()
/*     */     {
/*     */       public void operationComplete(ChannelFuture future) throws Exception {
/*     */         try {
/* 289 */           if (future.isSuccess())
/*     */           {
/* 291 */             HttpServerUpgradeHandler.this.sourceCodec.upgradeFrom(ctx);
/* 292 */             upgradeCodec.upgradeTo(ctx, request, upgradeResponse);
/*     */             
/*     */ 
/*     */ 
/* 296 */             ctx.fireUserEventTriggered(event.retain());
/*     */             
/*     */ 
/* 299 */             ctx.pipeline().remove(HttpServerUpgradeHandler.this);
/*     */           } else {
/* 301 */             future.channel().close();
/*     */           }
/*     */         }
/*     */         finally {
/* 305 */           event.release();
/*     */         }
/*     */       }
/* 308 */     });
/* 309 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private UpgradeCodec selectUpgradeCodec(CharSequence upgradeHeader)
/*     */   {
/* 317 */     Set<CharSequence> requestedProtocols = splitHeader(upgradeHeader);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 322 */     Set<String> supportedProtocols = new LinkedHashSet(this.upgradeCodecMap.keySet());
/* 323 */     supportedProtocols.retainAll(requestedProtocols);
/*     */     
/* 325 */     if (!supportedProtocols.isEmpty()) {
/* 326 */       String protocol = ((String)supportedProtocols.iterator().next()).toUpperCase(Locale.US);
/* 327 */       return (UpgradeCodec)this.upgradeCodecMap.get(protocol);
/*     */     }
/* 329 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static FullHttpResponse createUpgradeResponse(UpgradeCodec upgradeCodec)
/*     */   {
/* 336 */     DefaultFullHttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.SWITCHING_PROTOCOLS);
/* 337 */     res.headers().add(HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE);
/* 338 */     res.headers().add(HttpHeaderNames.UPGRADE, upgradeCodec.protocol());
/* 339 */     res.headers().add(HttpHeaderNames.CONTENT_LENGTH, "0");
/* 340 */     return res;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Set<CharSequence> splitHeader(CharSequence header)
/*     */   {
/* 348 */     StringBuilder builder = new StringBuilder(header.length());
/* 349 */     Set<CharSequence> protocols = new TreeSet(AsciiString.CHARSEQUENCE_CASE_INSENSITIVE_ORDER);
/* 350 */     for (int i = 0; i < header.length(); i++) {
/* 351 */       char c = header.charAt(i);
/* 352 */       if (!Character.isWhitespace(c))
/*     */       {
/*     */ 
/*     */ 
/* 356 */         if (c == ',')
/*     */         {
/* 358 */           protocols.add(builder.toString());
/* 359 */           builder.setLength(0);
/*     */         } else {
/* 361 */           builder.append(c);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 366 */     if (builder.length() > 0) {
/* 367 */       protocols.add(builder.toString());
/*     */     }
/*     */     
/* 370 */     return protocols;
/*     */   }
/*     */   
/*     */   public static abstract interface UpgradeCodec
/*     */   {
/*     */     public abstract String protocol();
/*     */     
/*     */     public abstract Collection<String> requiredUpgradeHeaders();
/*     */     
/*     */     public abstract void prepareUpgradeResponse(ChannelHandlerContext paramChannelHandlerContext, FullHttpRequest paramFullHttpRequest, FullHttpResponse paramFullHttpResponse);
/*     */     
/*     */     public abstract void upgradeTo(ChannelHandlerContext paramChannelHandlerContext, FullHttpRequest paramFullHttpRequest, FullHttpResponse paramFullHttpResponse);
/*     */   }
/*     */   
/*     */   public static abstract interface SourceCodec
/*     */   {
/*     */     public abstract void upgradeFrom(ChannelHandlerContext paramChannelHandlerContext);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\HttpServerUpgradeHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */