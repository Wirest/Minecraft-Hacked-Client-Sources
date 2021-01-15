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
/*     */ import io.netty.handler.codec.http.HttpClientCodec;
/*     */ import io.netty.handler.codec.http.HttpContentDecompressor;
/*     */ import io.netty.handler.codec.http.HttpHeaderNames;
/*     */ import io.netty.handler.codec.http.HttpHeaders;
/*     */ import io.netty.handler.codec.http.HttpObjectAggregator;
/*     */ import io.netty.handler.codec.http.HttpRequestEncoder;
/*     */ import io.netty.handler.codec.http.HttpResponse;
/*     */ import io.netty.handler.codec.http.HttpResponseDecoder;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.internal.EmptyArrays;
/*     */ import io.netty.util.internal.StringUtil;
/*     */ import java.net.URI;
/*     */ import java.nio.channels.ClosedChannelException;
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
/*     */ public abstract class WebSocketClientHandshaker
/*     */ {
/*  46 */   private static final ClosedChannelException CLOSED_CHANNEL_EXCEPTION = new ClosedChannelException();
/*     */   private final URI uri;
/*     */   
/*  49 */   static { CLOSED_CHANNEL_EXCEPTION.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final WebSocketVersion version;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private volatile boolean handshakeComplete;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final String expectedSubprotocol;
/*     */   
/*     */ 
/*     */ 
/*     */   private volatile String actualSubprotocol;
/*     */   
/*     */ 
/*     */ 
/*     */   protected final HttpHeaders customHeaders;
/*     */   
/*     */ 
/*     */ 
/*     */   private final int maxFramePayloadLength;
/*     */   
/*     */ 
/*     */ 
/*     */   protected WebSocketClientHandshaker(URI uri, WebSocketVersion version, String subprotocol, HttpHeaders customHeaders, int maxFramePayloadLength)
/*     */   {
/*  83 */     this.uri = uri;
/*  84 */     this.version = version;
/*  85 */     this.expectedSubprotocol = subprotocol;
/*  86 */     this.customHeaders = customHeaders;
/*  87 */     this.maxFramePayloadLength = maxFramePayloadLength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public URI uri()
/*     */   {
/*  94 */     return this.uri;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public WebSocketVersion version()
/*     */   {
/* 101 */     return this.version;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int maxFramePayloadLength()
/*     */   {
/* 108 */     return this.maxFramePayloadLength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isHandshakeComplete()
/*     */   {
/* 115 */     return this.handshakeComplete;
/*     */   }
/*     */   
/*     */   private void setHandshakeComplete() {
/* 119 */     this.handshakeComplete = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String expectedSubprotocol()
/*     */   {
/* 126 */     return this.expectedSubprotocol;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String actualSubprotocol()
/*     */   {
/* 134 */     return this.actualSubprotocol;
/*     */   }
/*     */   
/*     */   private void setActualSubprotocol(String actualSubprotocol) {
/* 138 */     this.actualSubprotocol = actualSubprotocol;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChannelFuture handshake(Channel channel)
/*     */   {
/* 148 */     if (channel == null) {
/* 149 */       throw new NullPointerException("channel");
/*     */     }
/* 151 */     return handshake(channel, channel.newPromise());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final ChannelFuture handshake(Channel channel, final ChannelPromise promise)
/*     */   {
/* 163 */     FullHttpRequest request = newHandshakeRequest();
/*     */     
/* 165 */     HttpResponseDecoder decoder = (HttpResponseDecoder)channel.pipeline().get(HttpResponseDecoder.class);
/* 166 */     if (decoder == null) {
/* 167 */       HttpClientCodec codec = (HttpClientCodec)channel.pipeline().get(HttpClientCodec.class);
/* 168 */       if (codec == null) {
/* 169 */         promise.setFailure(new IllegalStateException("ChannelPipeline does not contain a HttpResponseDecoder or HttpClientCodec"));
/*     */         
/* 171 */         return promise;
/*     */       }
/*     */     }
/*     */     
/* 175 */     channel.writeAndFlush(request).addListener(new ChannelFutureListener()
/*     */     {
/*     */       public void operationComplete(ChannelFuture future) {
/* 178 */         if (future.isSuccess()) {
/* 179 */           ChannelPipeline p = future.channel().pipeline();
/* 180 */           ChannelHandlerContext ctx = p.context(HttpRequestEncoder.class);
/* 181 */           if (ctx == null) {
/* 182 */             ctx = p.context(HttpClientCodec.class);
/*     */           }
/* 184 */           if (ctx == null) {
/* 185 */             promise.setFailure(new IllegalStateException("ChannelPipeline does not contain a HttpRequestEncoder or HttpClientCodec"));
/*     */             
/* 187 */             return;
/*     */           }
/* 189 */           p.addAfter(ctx.name(), "ws-encoder", WebSocketClientHandshaker.this.newWebSocketEncoder());
/*     */           
/* 191 */           promise.setSuccess();
/*     */         } else {
/* 193 */           promise.setFailure(future.cause());
/*     */         }
/*     */       }
/* 196 */     });
/* 197 */     return promise;
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
/*     */   public final void finishHandshake(Channel channel, FullHttpResponse response)
/*     */   {
/* 214 */     verify(response);
/*     */     
/*     */ 
/*     */ 
/* 218 */     String receivedProtocol = (String)response.headers().getAndConvert(HttpHeaderNames.SEC_WEBSOCKET_PROTOCOL);
/* 219 */     receivedProtocol = receivedProtocol != null ? receivedProtocol.trim() : null;
/* 220 */     String expectedProtocol = this.expectedSubprotocol != null ? this.expectedSubprotocol : "";
/* 221 */     boolean protocolValid = false;
/*     */     
/* 223 */     if ((expectedProtocol.isEmpty()) && (receivedProtocol == null))
/*     */     {
/* 225 */       protocolValid = true;
/* 226 */       setActualSubprotocol(this.expectedSubprotocol);
/* 227 */     } else if ((!expectedProtocol.isEmpty()) && (receivedProtocol != null) && (!receivedProtocol.isEmpty()))
/*     */     {
/* 229 */       for (String protocol : StringUtil.split(this.expectedSubprotocol, ',')) {
/* 230 */         if (protocol.trim().equals(receivedProtocol)) {
/* 231 */           protocolValid = true;
/* 232 */           setActualSubprotocol(receivedProtocol);
/* 233 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 238 */     if (!protocolValid) {
/* 239 */       throw new WebSocketHandshakeException(String.format("Invalid subprotocol. Actual: %s. Expected one of: %s", new Object[] { receivedProtocol, this.expectedSubprotocol }));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 244 */     setHandshakeComplete();
/*     */     
/* 246 */     ChannelPipeline p = channel.pipeline();
/*     */     
/* 248 */     HttpContentDecompressor decompressor = (HttpContentDecompressor)p.get(HttpContentDecompressor.class);
/* 249 */     if (decompressor != null) {
/* 250 */       p.remove(decompressor);
/*     */     }
/*     */     
/*     */ 
/* 254 */     HttpObjectAggregator aggregator = (HttpObjectAggregator)p.get(HttpObjectAggregator.class);
/* 255 */     if (aggregator != null) {
/* 256 */       p.remove(aggregator);
/*     */     }
/*     */     
/* 259 */     ChannelHandlerContext ctx = p.context(HttpResponseDecoder.class);
/* 260 */     if (ctx == null) {
/* 261 */       ctx = p.context(HttpClientCodec.class);
/* 262 */       if (ctx == null) {
/* 263 */         throw new IllegalStateException("ChannelPipeline does not contain a HttpRequestEncoder or HttpClientCodec");
/*     */       }
/*     */       
/* 266 */       p.replace(ctx.name(), "ws-decoder", newWebsocketDecoder());
/*     */     } else {
/* 268 */       if (p.get(HttpRequestEncoder.class) != null) {
/* 269 */         p.remove(HttpRequestEncoder.class);
/*     */       }
/* 271 */       p.replace(ctx.name(), "ws-decoder", newWebsocketDecoder());
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
/*     */   public final ChannelFuture processHandshake(Channel channel, HttpResponse response)
/*     */   {
/* 287 */     return processHandshake(channel, response, channel.newPromise());
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
/*     */   public final ChannelFuture processHandshake(final Channel channel, HttpResponse response, final ChannelPromise promise)
/*     */   {
/* 304 */     if ((response instanceof FullHttpResponse)) {
/*     */       try {
/* 306 */         finishHandshake(channel, (FullHttpResponse)response);
/* 307 */         promise.setSuccess();
/*     */       } catch (Throwable cause) {
/* 309 */         promise.setFailure(cause);
/*     */       }
/*     */     } else {
/* 312 */       ChannelPipeline p = channel.pipeline();
/* 313 */       ChannelHandlerContext ctx = p.context(HttpResponseDecoder.class);
/* 314 */       if (ctx == null) {
/* 315 */         ctx = p.context(HttpClientCodec.class);
/* 316 */         if (ctx == null) {
/* 317 */           return promise.setFailure(new IllegalStateException("ChannelPipeline does not contain a HttpResponseDecoder or HttpClientCodec"));
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 325 */       String aggregatorName = "httpAggregator";
/* 326 */       p.addAfter(ctx.name(), aggregatorName, new HttpObjectAggregator(8192));
/* 327 */       p.addAfter(aggregatorName, "handshaker", new SimpleChannelInboundHandler()
/*     */       {
/*     */         protected void messageReceived(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception
/*     */         {
/* 331 */           ctx.pipeline().remove(this);
/*     */           try {
/* 333 */             WebSocketClientHandshaker.this.finishHandshake(channel, msg);
/* 334 */             promise.setSuccess();
/*     */           } catch (Throwable cause) {
/* 336 */             promise.setFailure(cause);
/*     */           }
/*     */         }
/*     */         
/*     */         public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
/*     */           throws Exception
/*     */         {
/* 343 */           ctx.pipeline().remove(this);
/* 344 */           promise.setFailure(cause);
/*     */         }
/*     */         
/*     */         public void channelInactive(ChannelHandlerContext ctx)
/*     */           throws Exception
/*     */         {
/* 350 */           promise.tryFailure(WebSocketClientHandshaker.CLOSED_CHANNEL_EXCEPTION);
/* 351 */           ctx.fireChannelInactive();
/*     */         }
/*     */       });
/*     */       try {
/* 355 */         ctx.fireChannelRead(ReferenceCountUtil.retain(response));
/*     */       } catch (Throwable cause) {
/* 357 */         promise.setFailure(cause);
/*     */       }
/*     */     }
/* 360 */     return promise;
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
/*     */   public ChannelFuture close(Channel channel, CloseWebSocketFrame frame)
/*     */   {
/* 387 */     if (channel == null) {
/* 388 */       throw new NullPointerException("channel");
/*     */     }
/* 390 */     return close(channel, frame, channel.newPromise());
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
/* 404 */     if (channel == null) {
/* 405 */       throw new NullPointerException("channel");
/*     */     }
/* 407 */     return channel.writeAndFlush(frame, promise);
/*     */   }
/*     */   
/*     */   protected abstract FullHttpRequest newHandshakeRequest();
/*     */   
/*     */   protected abstract void verify(FullHttpResponse paramFullHttpResponse);
/*     */   
/*     */   protected abstract WebSocketFrameDecoder newWebsocketDecoder();
/*     */   
/*     */   protected abstract WebSocketFrameEncoder newWebSocketEncoder();
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\WebSocketClientHandshaker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */