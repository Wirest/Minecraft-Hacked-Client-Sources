/*     */ package io.netty.handler.codec.http.websocketx;
/*     */ 
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerAdapter;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.handler.codec.http.DefaultFullHttpResponse;
/*     */ import io.netty.handler.codec.http.FullHttpRequest;
/*     */ import io.netty.handler.codec.http.FullHttpResponse;
/*     */ import io.netty.handler.codec.http.HttpResponseStatus;
/*     */ import io.netty.handler.codec.http.HttpVersion;
/*     */ import io.netty.util.Attribute;
/*     */ import io.netty.util.AttributeKey;
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
/*     */ public class WebSocketServerProtocolHandler
/*     */   extends WebSocketProtocolHandler
/*     */ {
/*     */   public static enum ServerHandshakeStateEvent
/*     */   {
/*  59 */     HANDSHAKE_COMPLETE;
/*     */     
/*     */     private ServerHandshakeStateEvent() {} }
/*  62 */   private static final AttributeKey<WebSocketServerHandshaker> HANDSHAKER_ATTR_KEY = AttributeKey.valueOf(WebSocketServerHandshaker.class, "HANDSHAKER");
/*     */   
/*     */   private final String websocketPath;
/*     */   private final String subprotocols;
/*     */   private final boolean allowExtensions;
/*     */   private final int maxFramePayloadLength;
/*     */   private final boolean allowMaskMismatch;
/*     */   
/*     */   public WebSocketServerProtocolHandler(String websocketPath)
/*     */   {
/*  72 */     this(websocketPath, null, false);
/*     */   }
/*     */   
/*     */   public WebSocketServerProtocolHandler(String websocketPath, String subprotocols) {
/*  76 */     this(websocketPath, subprotocols, false);
/*     */   }
/*     */   
/*     */   public WebSocketServerProtocolHandler(String websocketPath, String subprotocols, boolean allowExtensions) {
/*  80 */     this(websocketPath, subprotocols, allowExtensions, 65536);
/*     */   }
/*     */   
/*     */   public WebSocketServerProtocolHandler(String websocketPath, String subprotocols, boolean allowExtensions, int maxFrameSize)
/*     */   {
/*  85 */     this(websocketPath, subprotocols, allowExtensions, maxFrameSize, false);
/*     */   }
/*     */   
/*     */   public WebSocketServerProtocolHandler(String websocketPath, String subprotocols, boolean allowExtensions, int maxFrameSize, boolean allowMaskMismatch)
/*     */   {
/*  90 */     this.websocketPath = websocketPath;
/*  91 */     this.subprotocols = subprotocols;
/*  92 */     this.allowExtensions = allowExtensions;
/*  93 */     this.maxFramePayloadLength = maxFrameSize;
/*  94 */     this.allowMaskMismatch = allowMaskMismatch;
/*     */   }
/*     */   
/*     */   public void handlerAdded(ChannelHandlerContext ctx)
/*     */   {
/*  99 */     ChannelPipeline cp = ctx.pipeline();
/* 100 */     if (cp.get(WebSocketServerProtocolHandshakeHandler.class) == null)
/*     */     {
/* 102 */       ctx.pipeline().addBefore(ctx.name(), WebSocketServerProtocolHandshakeHandler.class.getName(), new WebSocketServerProtocolHandshakeHandler(this.websocketPath, this.subprotocols, this.allowExtensions, this.maxFramePayloadLength, this.allowMaskMismatch));
/*     */     }
/*     */     
/*     */ 
/* 106 */     if (cp.get(Utf8FrameValidator.class) == null)
/*     */     {
/* 108 */       ctx.pipeline().addBefore(ctx.name(), Utf8FrameValidator.class.getName(), new Utf8FrameValidator());
/*     */     }
/*     */   }
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> out)
/*     */     throws Exception
/*     */   {
/* 115 */     if ((frame instanceof CloseWebSocketFrame)) {
/* 116 */       WebSocketServerHandshaker handshaker = getHandshaker(ctx);
/* 117 */       if (handshaker != null) {
/* 118 */         frame.retain();
/* 119 */         handshaker.close(ctx.channel(), (CloseWebSocketFrame)frame);
/*     */       } else {
/* 121 */         ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
/*     */       }
/* 123 */       return;
/*     */     }
/* 125 */     super.decode(ctx, frame, out);
/*     */   }
/*     */   
/*     */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
/*     */   {
/* 130 */     if ((cause instanceof WebSocketHandshakeException)) {
/* 131 */       FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST, Unpooled.wrappedBuffer(cause.getMessage().getBytes()));
/*     */       
/* 133 */       ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
/*     */     } else {
/* 135 */       ctx.close();
/*     */     }
/*     */   }
/*     */   
/*     */   static WebSocketServerHandshaker getHandshaker(ChannelHandlerContext ctx) {
/* 140 */     return (WebSocketServerHandshaker)ctx.attr(HANDSHAKER_ATTR_KEY).get();
/*     */   }
/*     */   
/*     */   static void setHandshaker(ChannelHandlerContext ctx, WebSocketServerHandshaker handshaker) {
/* 144 */     ctx.attr(HANDSHAKER_ATTR_KEY).set(handshaker);
/*     */   }
/*     */   
/*     */   static ChannelHandler forbiddenHttpRequestResponder() {
/* 148 */     new ChannelHandlerAdapter()
/*     */     {
/*     */       public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
/* 151 */         if ((msg instanceof FullHttpRequest)) {
/* 152 */           FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN);
/*     */           
/* 154 */           ctx.channel().writeAndFlush(response);
/*     */         } else {
/* 156 */           ctx.fireChannelRead(msg);
/*     */         }
/*     */       }
/*     */     };
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\WebSocketServerProtocolHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */