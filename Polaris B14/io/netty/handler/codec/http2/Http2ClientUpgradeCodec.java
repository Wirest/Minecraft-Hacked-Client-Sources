/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.handler.codec.base64.Base64;
/*     */ import io.netty.handler.codec.base64.Base64Dialect;
/*     */ import io.netty.handler.codec.http.FullHttpResponse;
/*     */ import io.netty.handler.codec.http.HttpClientUpgradeHandler.UpgradeCodec;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpRequest;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.collection.IntObjectMap.Entry;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
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
/*     */ public class Http2ClientUpgradeCodec
/*     */   implements HttpClientUpgradeHandler.UpgradeCodec
/*     */ {
/*  43 */   private static final List<String> UPGRADE_HEADERS = Collections.singletonList("HTTP2-Settings");
/*     */   
/*     */ 
/*     */   private final String handlerName;
/*     */   
/*     */ 
/*     */   private final Http2ConnectionHandler connectionHandler;
/*     */   
/*     */ 
/*     */ 
/*     */   public Http2ClientUpgradeCodec(Http2ConnectionHandler connectionHandler)
/*     */   {
/*  55 */     this("http2ConnectionHandler", connectionHandler);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Http2ClientUpgradeCodec(String handlerName, Http2ConnectionHandler connectionHandler)
/*     */   {
/*  66 */     this.handlerName = ((String)ObjectUtil.checkNotNull(handlerName, "handlerName"));
/*  67 */     this.connectionHandler = ((Http2ConnectionHandler)ObjectUtil.checkNotNull(connectionHandler, "connectionHandler"));
/*     */   }
/*     */   
/*     */   public String protocol()
/*     */   {
/*  72 */     return "h2c-16";
/*     */   }
/*     */   
/*     */ 
/*     */   public Collection<String> setUpgradeHeaders(ChannelHandlerContext ctx, HttpRequest upgradeRequest)
/*     */   {
/*  78 */     String settingsValue = getSettingsHeaderValue(ctx);
/*  79 */     upgradeRequest.headers().set("HTTP2-Settings", settingsValue);
/*  80 */     return UPGRADE_HEADERS;
/*     */   }
/*     */   
/*     */ 
/*     */   public void upgradeTo(ChannelHandlerContext ctx, FullHttpResponse upgradeResponse)
/*     */     throws Exception
/*     */   {
/*  87 */     this.connectionHandler.onHttpClientUpgrade();
/*     */     
/*     */ 
/*  90 */     ctx.pipeline().addAfter(ctx.name(), this.handlerName, this.connectionHandler);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String getSettingsHeaderValue(ChannelHandlerContext ctx)
/*     */   {
/*  98 */     ByteBuf buf = null;
/*  99 */     ByteBuf encodedBuf = null;
/*     */     try
/*     */     {
/* 102 */       Http2Settings settings = this.connectionHandler.decoder().localSettings();
/*     */       
/*     */ 
/* 105 */       int payloadLength = 6 * settings.size();
/* 106 */       buf = ctx.alloc().buffer(payloadLength);
/* 107 */       for (IntObjectMap.Entry<Long> entry : settings.entries()) {
/* 108 */         Http2CodecUtil.writeUnsignedShort(entry.key(), buf);
/* 109 */         Http2CodecUtil.writeUnsignedInt(((Long)entry.value()).longValue(), buf);
/*     */       }
/*     */       
/*     */ 
/* 113 */       encodedBuf = Base64.encode(buf, Base64Dialect.URL_SAFE);
/* 114 */       return encodedBuf.toString(CharsetUtil.UTF_8);
/*     */     } finally {
/* 116 */       ReferenceCountUtil.release(buf);
/* 117 */       ReferenceCountUtil.release(encodedBuf);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\Http2ClientUpgradeCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */