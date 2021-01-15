/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.handler.codec.AsciiString;
/*     */ import io.netty.handler.codec.base64.Base64;
/*     */ import io.netty.handler.codec.base64.Base64Dialect;
/*     */ import io.netty.handler.codec.http.FullHttpRequest;
/*     */ import io.netty.handler.codec.http.FullHttpResponse;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpResponseStatus;
/*     */ import io.netty.handler.codec.http.HttpServerUpgradeHandler.UpgradeCodec;
/*     */ import io.netty.util.CharsetUtil;
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
/*     */ public class Http2ServerUpgradeCodec
/*     */   implements HttpServerUpgradeHandler.UpgradeCodec
/*     */ {
/*  44 */   private static final List<String> REQUIRED_UPGRADE_HEADERS = Collections.singletonList("HTTP2-Settings");
/*     */   
/*     */ 
/*     */   private final String handlerName;
/*     */   
/*     */ 
/*     */   private final Http2ConnectionHandler connectionHandler;
/*     */   
/*     */ 
/*     */   private final Http2FrameReader frameReader;
/*     */   
/*     */ 
/*     */   public Http2ServerUpgradeCodec(Http2ConnectionHandler connectionHandler)
/*     */   {
/*  58 */     this("http2ConnectionHandler", connectionHandler);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Http2ServerUpgradeCodec(String handlerName, Http2ConnectionHandler connectionHandler)
/*     */   {
/*  68 */     this.handlerName = ((String)ObjectUtil.checkNotNull(handlerName, "handlerName"));
/*  69 */     this.connectionHandler = ((Http2ConnectionHandler)ObjectUtil.checkNotNull(connectionHandler, "connectionHandler"));
/*  70 */     this.frameReader = new DefaultHttp2FrameReader();
/*     */   }
/*     */   
/*     */   public String protocol()
/*     */   {
/*  75 */     return "h2c-16";
/*     */   }
/*     */   
/*     */   public Collection<String> requiredUpgradeHeaders()
/*     */   {
/*  80 */     return REQUIRED_UPGRADE_HEADERS;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void prepareUpgradeResponse(ChannelHandlerContext ctx, FullHttpRequest upgradeRequest, FullHttpResponse upgradeResponse)
/*     */   {
/*     */     try
/*     */     {
/*  89 */       List<CharSequence> upgradeHeaders = upgradeRequest.headers().getAll("HTTP2-Settings");
/*  90 */       if ((upgradeHeaders.isEmpty()) || (upgradeHeaders.size() > 1)) {
/*  91 */         throw new IllegalArgumentException("There must be 1 and only 1 HTTP2-Settings header.");
/*     */       }
/*     */       
/*  94 */       Http2Settings settings = decodeSettingsHeader(ctx, (CharSequence)upgradeHeaders.get(0));
/*  95 */       this.connectionHandler.onHttpServerUpgrade(settings);
/*     */     }
/*     */     catch (Throwable e)
/*     */     {
/*  99 */       upgradeResponse.setStatus(HttpResponseStatus.BAD_REQUEST);
/* 100 */       upgradeResponse.headers().clear();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void upgradeTo(ChannelHandlerContext ctx, FullHttpRequest upgradeRequest, FullHttpResponse upgradeResponse)
/*     */   {
/* 109 */     ctx.pipeline().addAfter(ctx.name(), this.handlerName, this.connectionHandler);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Http2Settings decodeSettingsHeader(ChannelHandlerContext ctx, CharSequence settingsHeader)
/*     */     throws Http2Exception
/*     */   {
/* 117 */     ByteBuf header = Unpooled.wrappedBuffer(AsciiString.getBytes(settingsHeader, CharsetUtil.UTF_8));
/*     */     try
/*     */     {
/* 120 */       ByteBuf payload = Base64.decode(header, Base64Dialect.URL_SAFE);
/*     */       
/*     */ 
/* 123 */       ByteBuf frame = createSettingsFrame(ctx, payload);
/*     */       
/*     */ 
/* 126 */       return decodeSettings(ctx, frame);
/*     */     } finally {
/* 128 */       header.release();
/*     */     }
/*     */   }
/*     */   
/*     */   private Http2Settings decodeSettings(ChannelHandlerContext ctx, ByteBuf frame)
/*     */     throws Http2Exception
/*     */   {
/*     */     try
/*     */     {
/* 137 */       final Http2Settings decodedSettings = new Http2Settings();
/* 138 */       this.frameReader.readFrame(ctx, frame, new Http2FrameAdapter()
/*     */       {
/*     */         public void onSettingsRead(ChannelHandlerContext ctx, Http2Settings settings) {
/* 141 */           decodedSettings.copyFrom(settings);
/*     */         }
/* 143 */       });
/* 144 */       return decodedSettings;
/*     */     } finally {
/* 146 */       frame.release();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static ByteBuf createSettingsFrame(ChannelHandlerContext ctx, ByteBuf payload)
/*     */   {
/* 154 */     ByteBuf frame = ctx.alloc().buffer(9 + payload.readableBytes());
/* 155 */     Http2CodecUtil.writeFrameHeader(frame, payload.readableBytes(), (byte)4, new Http2Flags(), 0);
/* 156 */     frame.writeBytes(payload);
/* 157 */     payload.release();
/* 158 */     return frame;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\Http2ServerUpgradeCodec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */