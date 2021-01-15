/*     */ package io.netty.handler.codec.http.websocketx;
/*     */ 
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.channel.SimpleChannelInboundHandler;
/*     */ import io.netty.handler.codec.http.FullHttpRequest;
/*     */ import io.netty.handler.codec.http.FullHttpResponse;
/*     */ import io.netty.handler.codec.http.HttpContentCompressor;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpObjectAggregator;
/*     */ import io.netty.handler.codec.http.HttpRequest;
/*     */ import io.netty.handler.codec.http.HttpRequestDecoder;
/*     */ import io.netty.handler.codec.http.HttpResponseEncoder;
/*     */ import io.netty.handler.codec.http.HttpServerCodec;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.internal.EmptyArrays;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.nio.channels.ClosedChannelException;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
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
/*     */ public abstract class WebSocketServerHandshaker
/*     */ {
/*  49 */   protected static final InternalLogger logger = InternalLoggerFactory.getInstance(WebSocketServerHandshaker.class);
/*  50 */   private static final ClosedChannelException CLOSED_CHANNEL_EXCEPTION = new ClosedChannelException();
/*     */   private final String uri;
/*     */   
/*  53 */   static { CLOSED_CHANNEL_EXCEPTION.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final String[] subprotocols;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final WebSocketVersion version;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int maxFramePayloadLength;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String selectedSubprotocol;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final String SUB_PROTOCOL_WILDCARD = "*";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected WebSocketServerHandshaker(WebSocketVersion version, String uri, String subprotocols, int maxFramePayloadLength)
/*     */   {
/*  87 */     this.version = version;
/*  88 */     this.uri = uri;
/*  89 */     if (subprotocols != null) {
/*  90 */       String[] subprotocolArray = StringUtil.split(subprotocols, ',');
/*  91 */       for (int i = 0; i < subprotocolArray.length; i++) {
/*  92 */         subprotocolArray[i] = subprotocolArray[i].trim();
/*     */       }
/*  94 */       this.subprotocols = subprotocolArray;
/*     */     } else {
/*  96 */       this.subprotocols = EmptyArrays.EMPTY_STRINGS;
/*     */     }
/*  98 */     this.maxFramePayloadLength = maxFramePayloadLength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String uri()
/*     */   {
/* 105 */     return this.uri;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Set<String> subprotocols()
/*     */   {
/* 112 */     Set<String> ret = new LinkedHashSet();
/* 113 */     Collections.addAll(ret, this.subprotocols);
/* 114 */     return ret;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public WebSocketVersion version()
/*     */   {
/* 121 */     return this.version;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int maxFramePayloadLength()
/*     */   {
/* 130 */     return this.maxFramePayloadLength;
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
/*     */   public ChannelFuture handshake(Channel channel, FullHttpRequest req)
/*     */   {
/* 145 */     return handshake(channel, req, null, channel.newPromise());
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
/*     */   public final ChannelFuture handshake(Channel channel, FullHttpRequest req, HttpHeaders responseHeaders, final ChannelPromise promise)
/*     */   {
/* 167 */     if (logger.isDebugEnabled()) {
/* 168 */       logger.debug("{} WebSocket version {} server handshake", channel, version());
/*     */     }
/* 170 */     FullHttpResponse response = newHandshakeResponse(req, responseHeaders);
/* 171 */     ChannelPipeline p = channel.pipeline();
/* 172 */     if (p.get(HttpObjectAggregator.class) != null) {
/* 173 */       p.remove(HttpObjectAggregator.class);
/*     */     }
/* 175 */     if (p.get(HttpContentCompressor.class) != null) {
/* 176 */       p.remove(HttpContentCompressor.class);
/*     */     }
/* 178 */     ChannelHandlerContext ctx = p.context(HttpRequestDecoder.class);
/*     */     String encoderName;
/* 180 */     final String encoderName; if (ctx == null)
/*     */     {
/* 182 */       ctx = p.context(HttpServerCodec.class);
/* 183 */       if (ctx == null) {
/* 184 */         promise.setFailure(new IllegalStateException("No HttpDecoder and no HttpServerCodec in the pipeline"));
/*     */         
/* 186 */         return promise;
/*     */       }
/* 188 */       p.addBefore(ctx.name(), "wsdecoder", newWebsocketDecoder());
/* 189 */       p.addBefore(ctx.name(), "wsencoder", newWebSocketEncoder());
/* 190 */       encoderName = ctx.name();
/*     */     } else {
/* 192 */       p.replace(ctx.name(), "wsdecoder", newWebsocketDecoder());
/*     */       
/* 194 */       encoderName = p.context(HttpResponseEncoder.class).name();
/* 195 */       p.addBefore(encoderName, "wsencoder", newWebSocketEncoder());
/*     */     }
/* 197 */     channel.writeAndFlush(response).addListener(new ChannelFutureListener()
/*     */     {
/*     */       public void operationComplete(ChannelFuture future) throws Exception {
/* 200 */         if (future.isSuccess()) {
/* 201 */           ChannelPipeline p = future.channel().pipeline();
/* 202 */           p.remove(encoderName);
/* 203 */           promise.setSuccess();
/*     */         } else {
/* 205 */           promise.setFailure(future.cause());
/*     */         }
/*     */       }
/* 208 */     });
/* 209 */     return promise;
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
/*     */   public ChannelFuture handshake(Channel channel, HttpRequest req)
/*     */   {
/* 224 */     return handshake(channel, req, null, channel.newPromise());
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
/*     */   public final ChannelFuture handshake(final Channel channel, HttpRequest req, final HttpHeaders responseHeaders, final ChannelPromise promise)
/*     */   {
/* 246 */     if ((req instanceof FullHttpRequest)) {
/* 247 */       return handshake(channel, (FullHttpRequest)req, responseHeaders, promise);
/*     */     }
/* 249 */     if (logger.isDebugEnabled()) {
/* 250 */       logger.debug("{} WebSocket version {} server handshake", channel, version());
/*     */     }
/* 252 */     ChannelPipeline p = channel.pipeline();
/* 253 */     ChannelHandlerContext ctx = p.context(HttpRequestDecoder.class);
/* 254 */     if (ctx == null)
/*     */     {
/* 256 */       ctx = p.context(HttpServerCodec.class);
/* 257 */       if (ctx == null) {
/* 258 */         promise.setFailure(new IllegalStateException("No HttpDecoder and no HttpServerCodec in the pipeline"));
/*     */         
/* 260 */         return promise;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 267 */     String aggregatorName = "httpAggregator";
/* 268 */     p.addAfter(ctx.name(), aggregatorName, new HttpObjectAggregator(8192));
/* 269 */     p.addAfter(aggregatorName, "handshaker", new SimpleChannelInboundHandler()
/*     */     {
/*     */       protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception
/*     */       {
/* 273 */         ctx.pipeline().remove(this);
/* 274 */         WebSocketServerHandshaker.this.handshake(channel, msg, responseHeaders, promise);
/*     */       }
/*     */       
/*     */       public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
/*     */         throws Exception
/*     */       {
/* 280 */         ctx.pipeline().remove(this);
/* 281 */         promise.tryFailure(cause);
/* 282 */         ctx.fireExceptionCaught(cause);
/*     */       }
/*     */       
/*     */       public void channelInactive(ChannelHandlerContext ctx)
/*     */         throws Exception
/*     */       {
/* 288 */         promise.tryFailure(WebSocketServerHandshaker.CLOSED_CHANNEL_EXCEPTION);
/* 289 */         ctx.fireChannelInactive();
/*     */       }
/*     */     });
/*     */     try {
/* 293 */       ctx.fireChannelRead(ReferenceCountUtil.retain(req));
/*     */     } catch (Throwable cause) {
/* 295 */       promise.setFailure(cause);
/*     */     }
/* 297 */     return promise;
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
/*     */   public ChannelFuture close(Channel channel, CloseWebSocketFrame frame)
/*     */   {
/* 314 */     if (channel == null) {
/* 315 */       throw new NullPointerException("channel");
/*     */     }
/* 317 */     return close(channel, frame, channel.newPromise());
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
/*     */   public ChannelFuture close(Channel channel, CloseWebSocketFrame frame, ChannelPromise promise)
/*     */   {
/* 331 */     if (channel == null) {
/* 332 */       throw new NullPointerException("channel");
/*     */     }
/* 334 */     return channel.writeAndFlush(frame, promise).addListener(ChannelFutureListener.CLOSE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String selectSubprotocol(String requestedSubprotocols)
/*     */   {
/* 345 */     if ((requestedSubprotocols == null) || (this.subprotocols.length == 0)) {
/* 346 */       return null;
/*     */     }
/*     */     
/* 349 */     String[] requestedSubprotocolArray = StringUtil.split(requestedSubprotocols, ',');
/* 350 */     for (String p : requestedSubprotocolArray) {
/* 351 */       String requestedSubprotocol = p.trim();
/*     */       
/* 353 */       for (String supportedSubprotocol : this.subprotocols) {
/* 354 */         if (("*".equals(supportedSubprotocol)) || (requestedSubprotocol.equals(supportedSubprotocol)))
/*     */         {
/* 356 */           this.selectedSubprotocol = requestedSubprotocol;
/* 357 */           return requestedSubprotocol;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 363 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String selectedSubprotocol()
/*     */   {
/* 373 */     return this.selectedSubprotocol;
/*     */   }
/*     */   
/*     */   protected abstract FullHttpResponse newHandshakeResponse(FullHttpRequest paramFullHttpRequest, HttpHeaders paramHttpHeaders);
/*     */   
/*     */   protected abstract WebSocketFrameDecoder newWebsocketDecoder();
/*     */   
/*     */   protected abstract WebSocketFrameEncoder newWebSocketEncoder();
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\WebSocketServerHandshaker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */