/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.handler.codec.AsciiString;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
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
/*     */ public class HttpClientUpgradeHandler
/*     */   extends HttpObjectAggregator
/*     */ {
/*     */   private final SourceCodec sourceCodec;
/*     */   private final UpgradeCodec upgradeCodec;
/*     */   private boolean upgradeRequested;
/*     */   
/*     */   public static abstract interface UpgradeCodec
/*     */   {
/*     */     public abstract String protocol();
/*     */     
/*     */     public abstract Collection<String> setUpgradeHeaders(ChannelHandlerContext paramChannelHandlerContext, HttpRequest paramHttpRequest);
/*     */     
/*     */     public abstract void upgradeTo(ChannelHandlerContext paramChannelHandlerContext, FullHttpResponse paramFullHttpResponse)
/*     */       throws Exception;
/*     */   }
/*     */   
/*     */   public static abstract interface SourceCodec
/*     */   {
/*     */     public abstract void upgradeFrom(ChannelHandlerContext paramChannelHandlerContext);
/*     */   }
/*     */   
/*     */   public static enum UpgradeEvent
/*     */   {
/*  45 */     UPGRADE_ISSUED, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  50 */     UPGRADE_SUCCESSFUL, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  56 */     UPGRADE_REJECTED;
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
/*     */     private UpgradeEvent() {}
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
/*     */   public HttpClientUpgradeHandler(SourceCodec sourceCodec, UpgradeCodec upgradeCodec, int maxContentLength)
/*     */   {
/* 108 */     super(maxContentLength);
/* 109 */     if (sourceCodec == null) {
/* 110 */       throw new NullPointerException("sourceCodec");
/*     */     }
/* 112 */     if (upgradeCodec == null) {
/* 113 */       throw new NullPointerException("upgradeCodec");
/*     */     }
/* 115 */     this.sourceCodec = sourceCodec;
/* 116 */     this.upgradeCodec = upgradeCodec;
/*     */   }
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
/*     */     throws Exception
/*     */   {
/* 122 */     if (!(msg instanceof HttpRequest)) {
/* 123 */       super.write(ctx, msg, promise);
/* 124 */       return;
/*     */     }
/*     */     
/* 127 */     if (this.upgradeRequested) {
/* 128 */       promise.setFailure(new IllegalStateException("Attempting to write HTTP request with upgrade in progress"));
/*     */       
/* 130 */       return;
/*     */     }
/*     */     
/* 133 */     this.upgradeRequested = true;
/* 134 */     setUpgradeRequestHeaders(ctx, (HttpRequest)msg);
/*     */     
/*     */ 
/* 137 */     super.write(ctx, msg, promise);
/*     */     
/*     */ 
/* 140 */     ctx.fireUserEventTriggered(UpgradeEvent.UPGRADE_ISSUED);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void decode(ChannelHandlerContext ctx, HttpObject msg, List<Object> out)
/*     */     throws Exception
/*     */   {
/* 147 */     FullHttpResponse response = null;
/*     */     try {
/* 149 */       if (!this.upgradeRequested) {
/* 150 */         throw new IllegalStateException("Read HTTP response without requesting protocol switch");
/*     */       }
/*     */       
/* 153 */       if ((msg instanceof FullHttpResponse)) {
/* 154 */         response = (FullHttpResponse)msg;
/*     */         
/* 156 */         response.retain();
/* 157 */         out.add(response);
/*     */       }
/*     */       else {
/* 160 */         super.decode(ctx, msg, out);
/* 161 */         if (out.isEmpty())
/*     */         {
/* 163 */           return;
/*     */         }
/*     */         
/* 166 */         assert (out.size() == 1);
/* 167 */         response = (FullHttpResponse)out.get(0);
/*     */       }
/*     */       
/* 170 */       if (!HttpResponseStatus.SWITCHING_PROTOCOLS.equals(response.status()))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 175 */         ctx.fireUserEventTriggered(UpgradeEvent.UPGRADE_REJECTED);
/* 176 */         removeThisHandler(ctx);
/* 177 */         return;
/*     */       }
/*     */       
/* 180 */       CharSequence upgradeHeader = (CharSequence)response.headers().get(HttpHeaderNames.UPGRADE);
/* 181 */       if (upgradeHeader == null) {
/* 182 */         throw new IllegalStateException("Switching Protocols response missing UPGRADE header");
/*     */       }
/*     */       
/* 185 */       if (!AsciiString.equalsIgnoreCase(this.upgradeCodec.protocol(), upgradeHeader)) {
/* 186 */         throw new IllegalStateException("Switching Protocols response with unexpected UPGRADE protocol: " + upgradeHeader);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 192 */       this.sourceCodec.upgradeFrom(ctx);
/* 193 */       this.upgradeCodec.upgradeTo(ctx, response);
/*     */       
/*     */ 
/* 196 */       ctx.fireUserEventTriggered(UpgradeEvent.UPGRADE_SUCCESSFUL);
/*     */       
/*     */ 
/*     */ 
/* 200 */       response.release();
/* 201 */       out.clear();
/* 202 */       removeThisHandler(ctx);
/*     */     } catch (Throwable t) {
/* 204 */       ReferenceCountUtil.release(response);
/* 205 */       ctx.fireExceptionCaught(t);
/* 206 */       removeThisHandler(ctx);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void removeThisHandler(ChannelHandlerContext ctx) {
/* 211 */     ctx.pipeline().remove(ctx.name());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void setUpgradeRequestHeaders(ChannelHandlerContext ctx, HttpRequest request)
/*     */   {
/* 219 */     request.headers().set(HttpHeaderNames.UPGRADE, this.upgradeCodec.protocol());
/*     */     
/*     */ 
/* 222 */     Set<String> connectionParts = new LinkedHashSet(2);
/* 223 */     connectionParts.addAll(this.upgradeCodec.setUpgradeHeaders(ctx, request));
/*     */     
/*     */ 
/* 226 */     StringBuilder builder = new StringBuilder();
/* 227 */     for (String part : connectionParts) {
/* 228 */       builder.append(part);
/* 229 */       builder.append(',');
/*     */     }
/* 231 */     builder.append(HttpHeaderNames.UPGRADE);
/* 232 */     request.headers().set(HttpHeaderNames.CONNECTION, builder.toString());
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\HttpClientUpgradeHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */