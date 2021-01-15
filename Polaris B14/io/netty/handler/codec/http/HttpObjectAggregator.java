/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.DefaultByteBufHolder;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.handler.codec.DecoderResult;
/*     */ import io.netty.handler.codec.MessageAggregator;
/*     */ import io.netty.handler.codec.TooLongFrameException;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
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
/*     */ public class HttpObjectAggregator
/*     */   extends MessageAggregator<HttpObject, HttpMessage, HttpContent, FullHttpMessage>
/*     */ {
/*     */   private static final InternalLogger logger;
/*     */   private static final FullHttpResponse CONTINUE;
/*     */   private static final FullHttpResponse TOO_LARGE;
/*     */   
/*     */   static
/*     */   {
/*  54 */     logger = InternalLoggerFactory.getInstance(HttpObjectAggregator.class);
/*  55 */     CONTINUE = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE, Unpooled.EMPTY_BUFFER);
/*     */     
/*  57 */     TOO_LARGE = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE, Unpooled.EMPTY_BUFFER);
/*     */     
/*     */ 
/*     */ 
/*  61 */     TOO_LARGE.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, 0);
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
/*     */   public HttpObjectAggregator(int maxContentLength)
/*     */   {
/*  74 */     super(maxContentLength);
/*     */   }
/*     */   
/*     */   protected boolean isStartMessage(HttpObject msg) throws Exception
/*     */   {
/*  79 */     return msg instanceof HttpMessage;
/*     */   }
/*     */   
/*     */   protected boolean isContentMessage(HttpObject msg) throws Exception
/*     */   {
/*  84 */     return msg instanceof HttpContent;
/*     */   }
/*     */   
/*     */   protected boolean isLastContentMessage(HttpContent msg) throws Exception
/*     */   {
/*  89 */     return msg instanceof LastHttpContent;
/*     */   }
/*     */   
/*     */   protected boolean isAggregated(HttpObject msg) throws Exception
/*     */   {
/*  94 */     return msg instanceof FullHttpMessage;
/*     */   }
/*     */   
/*     */   protected boolean hasContentLength(HttpMessage start) throws Exception
/*     */   {
/*  99 */     return HttpHeaderUtil.isContentLengthSet(start);
/*     */   }
/*     */   
/*     */   protected long contentLength(HttpMessage start) throws Exception
/*     */   {
/* 104 */     return HttpHeaderUtil.getContentLength(start);
/*     */   }
/*     */   
/*     */   protected Object newContinueResponse(HttpMessage start) throws Exception
/*     */   {
/* 109 */     if (HttpHeaderUtil.is100ContinueExpected(start)) {
/* 110 */       return CONTINUE;
/*     */     }
/* 112 */     return null;
/*     */   }
/*     */   
/*     */   protected FullHttpMessage beginAggregation(HttpMessage start, ByteBuf content)
/*     */     throws Exception
/*     */   {
/* 118 */     assert (!(start instanceof FullHttpMessage));
/*     */     
/* 120 */     HttpHeaderUtil.setTransferEncodingChunked(start, false);
/*     */     
/*     */     AggregatedFullHttpMessage ret;
/* 123 */     if ((start instanceof HttpRequest)) {
/* 124 */       ret = new AggregatedFullHttpRequest((HttpRequest)start, content, null); } else { AggregatedFullHttpMessage ret;
/* 125 */       if ((start instanceof HttpResponse)) {
/* 126 */         ret = new AggregatedFullHttpResponse((HttpResponse)start, content, null);
/*     */       } else
/* 128 */         throw new Error(); }
/*     */     AggregatedFullHttpMessage ret;
/* 130 */     return ret;
/*     */   }
/*     */   
/*     */   protected void aggregate(FullHttpMessage aggregated, HttpContent content) throws Exception
/*     */   {
/* 135 */     if ((content instanceof LastHttpContent))
/*     */     {
/* 137 */       ((AggregatedFullHttpMessage)aggregated).setTrailingHeaders(((LastHttpContent)content).trailingHeaders());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void finishAggregation(FullHttpMessage aggregated)
/*     */     throws Exception
/*     */   {
/* 149 */     if (!HttpHeaderUtil.isContentLengthSet(aggregated)) {
/* 150 */       aggregated.headers().set(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(aggregated.content().readableBytes()));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void handleOversizedMessage(final ChannelHandlerContext ctx, HttpMessage oversized)
/*     */     throws Exception
/*     */   {
/* 158 */     if ((oversized instanceof HttpRequest))
/*     */     {
/* 160 */       ChannelFuture future = ctx.writeAndFlush(TOO_LARGE).addListener(new ChannelFutureListener()
/*     */       {
/*     */         public void operationComplete(ChannelFuture future) throws Exception {
/* 163 */           if (!future.isSuccess()) {
/* 164 */             HttpObjectAggregator.logger.debug("Failed to send a 413 Request Entity Too Large.", future.cause());
/* 165 */             ctx.close();
/*     */           }
/*     */         }
/*     */       });
/*     */       
/*     */ 
/*     */ 
/* 172 */       if (((oversized instanceof FullHttpMessage)) || ((!HttpHeaderUtil.is100ContinueExpected(oversized)) && (!HttpHeaderUtil.isKeepAlive(oversized))))
/*     */       {
/* 174 */         future.addListener(ChannelFutureListener.CLOSE);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 179 */       HttpObjectDecoder decoder = (HttpObjectDecoder)ctx.pipeline().get(HttpObjectDecoder.class);
/* 180 */       if (decoder != null)
/* 181 */         decoder.reset();
/*     */     } else {
/* 183 */       if ((oversized instanceof HttpResponse)) {
/* 184 */         ctx.close();
/* 185 */         throw new TooLongFrameException("Response entity too large: " + oversized);
/*     */       }
/* 187 */       throw new IllegalStateException();
/*     */     }
/*     */   }
/*     */   
/*     */   private static abstract class AggregatedFullHttpMessage extends DefaultByteBufHolder implements FullHttpMessage {
/*     */     protected final HttpMessage message;
/*     */     private HttpHeaders trailingHeaders;
/*     */     
/*     */     AggregatedFullHttpMessage(HttpMessage message, ByteBuf content, HttpHeaders trailingHeaders) {
/* 196 */       super();
/* 197 */       this.message = message;
/* 198 */       this.trailingHeaders = trailingHeaders;
/*     */     }
/*     */     
/*     */     public HttpHeaders trailingHeaders()
/*     */     {
/* 203 */       HttpHeaders trailingHeaders = this.trailingHeaders;
/* 204 */       if (trailingHeaders == null) {
/* 205 */         return EmptyHttpHeaders.INSTANCE;
/*     */       }
/* 207 */       return trailingHeaders;
/*     */     }
/*     */     
/*     */     void setTrailingHeaders(HttpHeaders trailingHeaders)
/*     */     {
/* 212 */       this.trailingHeaders = trailingHeaders;
/*     */     }
/*     */     
/*     */     public HttpVersion protocolVersion()
/*     */     {
/* 217 */       return this.message.protocolVersion();
/*     */     }
/*     */     
/*     */     public FullHttpMessage setProtocolVersion(HttpVersion version)
/*     */     {
/* 222 */       this.message.setProtocolVersion(version);
/* 223 */       return this;
/*     */     }
/*     */     
/*     */     public HttpHeaders headers()
/*     */     {
/* 228 */       return this.message.headers();
/*     */     }
/*     */     
/*     */     public DecoderResult decoderResult()
/*     */     {
/* 233 */       return this.message.decoderResult();
/*     */     }
/*     */     
/*     */     public void setDecoderResult(DecoderResult result)
/*     */     {
/* 238 */       this.message.setDecoderResult(result);
/*     */     }
/*     */     
/*     */     public FullHttpMessage retain(int increment)
/*     */     {
/* 243 */       super.retain(increment);
/* 244 */       return this;
/*     */     }
/*     */     
/*     */     public FullHttpMessage retain()
/*     */     {
/* 249 */       super.retain();
/* 250 */       return this;
/*     */     }
/*     */     
/*     */     public FullHttpMessage touch(Object hint)
/*     */     {
/* 255 */       super.touch(hint);
/* 256 */       return this;
/*     */     }
/*     */     
/*     */     public FullHttpMessage touch()
/*     */     {
/* 261 */       super.touch();
/* 262 */       return this;
/*     */     }
/*     */     
/*     */     public abstract FullHttpMessage copy();
/*     */     
/*     */     public abstract FullHttpMessage duplicate();
/*     */   }
/*     */   
/*     */   private static final class AggregatedFullHttpRequest
/*     */     extends HttpObjectAggregator.AggregatedFullHttpMessage implements FullHttpRequest
/*     */   {
/*     */     AggregatedFullHttpRequest(HttpRequest request, ByteBuf content, HttpHeaders trailingHeaders)
/*     */     {
/* 275 */       super(content, trailingHeaders);
/*     */     }
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
/*     */     private FullHttpRequest copy(boolean copyContent, ByteBuf newContent)
/*     */     {
/* 294 */       DefaultFullHttpRequest copy = new DefaultFullHttpRequest(protocolVersion(), method(), uri(), newContent == null ? Unpooled.buffer(0) : copyContent ? content().copy() : newContent);
/*     */       
/*     */ 
/*     */ 
/* 298 */       copy.headers().set(headers());
/* 299 */       copy.trailingHeaders().set(trailingHeaders());
/* 300 */       return copy;
/*     */     }
/*     */     
/*     */     public FullHttpRequest copy(ByteBuf newContent)
/*     */     {
/* 305 */       return copy(false, newContent);
/*     */     }
/*     */     
/*     */     public FullHttpRequest copy()
/*     */     {
/* 310 */       return copy(true, null);
/*     */     }
/*     */     
/*     */     public FullHttpRequest duplicate()
/*     */     {
/* 315 */       DefaultFullHttpRequest duplicate = new DefaultFullHttpRequest(protocolVersion(), method(), uri(), content().duplicate());
/*     */       
/* 317 */       duplicate.headers().set(headers());
/* 318 */       duplicate.trailingHeaders().set(trailingHeaders());
/* 319 */       return duplicate;
/*     */     }
/*     */     
/*     */     public FullHttpRequest retain(int increment)
/*     */     {
/* 324 */       super.retain(increment);
/* 325 */       return this;
/*     */     }
/*     */     
/*     */     public FullHttpRequest retain()
/*     */     {
/* 330 */       super.retain();
/* 331 */       return this;
/*     */     }
/*     */     
/*     */     public FullHttpRequest touch()
/*     */     {
/* 336 */       super.touch();
/* 337 */       return this;
/*     */     }
/*     */     
/*     */     public FullHttpRequest touch(Object hint)
/*     */     {
/* 342 */       super.touch(hint);
/* 343 */       return this;
/*     */     }
/*     */     
/*     */     public FullHttpRequest setMethod(HttpMethod method)
/*     */     {
/* 348 */       ((HttpRequest)this.message).setMethod(method);
/* 349 */       return this;
/*     */     }
/*     */     
/*     */     public FullHttpRequest setUri(String uri)
/*     */     {
/* 354 */       ((HttpRequest)this.message).setUri(uri);
/* 355 */       return this;
/*     */     }
/*     */     
/*     */     public HttpMethod method()
/*     */     {
/* 360 */       return ((HttpRequest)this.message).method();
/*     */     }
/*     */     
/*     */     public String uri()
/*     */     {
/* 365 */       return ((HttpRequest)this.message).uri();
/*     */     }
/*     */     
/*     */     public FullHttpRequest setProtocolVersion(HttpVersion version)
/*     */     {
/* 370 */       super.setProtocolVersion(version);
/* 371 */       return this;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 376 */       return HttpMessageUtil.appendFullRequest(new StringBuilder(256), this).toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class AggregatedFullHttpResponse extends HttpObjectAggregator.AggregatedFullHttpMessage implements FullHttpResponse
/*     */   {
/*     */     AggregatedFullHttpResponse(HttpResponse message, ByteBuf content, HttpHeaders trailingHeaders)
/*     */     {
/* 384 */       super(content, trailingHeaders);
/*     */     }
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
/*     */     private FullHttpResponse copy(boolean copyContent, ByteBuf newContent)
/*     */     {
/* 403 */       DefaultFullHttpResponse copy = new DefaultFullHttpResponse(protocolVersion(), status(), newContent == null ? Unpooled.buffer(0) : copyContent ? content().copy() : newContent);
/*     */       
/*     */ 
/*     */ 
/* 407 */       copy.headers().set(headers());
/* 408 */       copy.trailingHeaders().set(trailingHeaders());
/* 409 */       return copy;
/*     */     }
/*     */     
/*     */     public FullHttpResponse copy(ByteBuf newContent)
/*     */     {
/* 414 */       return copy(false, newContent);
/*     */     }
/*     */     
/*     */     public FullHttpResponse copy()
/*     */     {
/* 419 */       return copy(true, null);
/*     */     }
/*     */     
/*     */     public FullHttpResponse duplicate()
/*     */     {
/* 424 */       DefaultFullHttpResponse duplicate = new DefaultFullHttpResponse(protocolVersion(), status(), content().duplicate());
/*     */       
/* 426 */       duplicate.headers().set(headers());
/* 427 */       duplicate.trailingHeaders().set(trailingHeaders());
/* 428 */       return duplicate;
/*     */     }
/*     */     
/*     */     public FullHttpResponse setStatus(HttpResponseStatus status)
/*     */     {
/* 433 */       ((HttpResponse)this.message).setStatus(status);
/* 434 */       return this;
/*     */     }
/*     */     
/*     */     public HttpResponseStatus status()
/*     */     {
/* 439 */       return ((HttpResponse)this.message).status();
/*     */     }
/*     */     
/*     */     public FullHttpResponse setProtocolVersion(HttpVersion version)
/*     */     {
/* 444 */       super.setProtocolVersion(version);
/* 445 */       return this;
/*     */     }
/*     */     
/*     */     public FullHttpResponse retain(int increment)
/*     */     {
/* 450 */       super.retain(increment);
/* 451 */       return this;
/*     */     }
/*     */     
/*     */     public FullHttpResponse retain()
/*     */     {
/* 456 */       super.retain();
/* 457 */       return this;
/*     */     }
/*     */     
/*     */     public FullHttpResponse touch(Object hint)
/*     */     {
/* 462 */       super.touch(hint);
/* 463 */       return this;
/*     */     }
/*     */     
/*     */     public FullHttpResponse touch()
/*     */     {
/* 468 */       super.touch();
/* 469 */       return this;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 474 */       return HttpMessageUtil.appendFullResponse(new StringBuilder(256), this).toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\HttpObjectAggregator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */