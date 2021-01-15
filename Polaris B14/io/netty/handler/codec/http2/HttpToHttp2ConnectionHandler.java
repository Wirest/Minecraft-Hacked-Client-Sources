/*    */ package io.netty.handler.codec.http2;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.channel.ChannelPromise;
/*    */ import io.netty.handler.codec.http.FullHttpMessage;
/*    */ import io.netty.handler.codec.http.HttpHeaders;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HttpToHttp2ConnectionHandler
/*    */   extends Http2ConnectionHandler
/*    */ {
/*    */   public HttpToHttp2ConnectionHandler(boolean server, Http2FrameListener listener)
/*    */   {
/* 31 */     super(server, listener);
/*    */   }
/*    */   
/*    */   public HttpToHttp2ConnectionHandler(Http2Connection connection, Http2FrameListener listener) {
/* 35 */     super(connection, listener);
/*    */   }
/*    */   
/*    */   public HttpToHttp2ConnectionHandler(Http2Connection connection, Http2FrameReader frameReader, Http2FrameWriter frameWriter, Http2FrameListener listener)
/*    */   {
/* 40 */     super(connection, frameReader, frameWriter, listener);
/*    */   }
/*    */   
/*    */   public HttpToHttp2ConnectionHandler(Http2ConnectionDecoder.Builder decoderBuilder, Http2ConnectionEncoder.Builder encoderBuilder)
/*    */   {
/* 45 */     super(decoderBuilder, encoderBuilder);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private int getStreamId(HttpHeaders httpHeaders)
/*    */     throws Exception
/*    */   {
/* 56 */     return httpHeaders.getInt(HttpUtil.ExtensionHeaderNames.STREAM_ID.text(), connection().local().nextStreamId());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
/*    */   {
/* 64 */     if ((msg instanceof FullHttpMessage)) {
/* 65 */       FullHttpMessage httpMsg = (FullHttpMessage)msg;
/* 66 */       boolean hasData = httpMsg.content().isReadable();
/* 67 */       boolean httpMsgNeedRelease = true;
/* 68 */       Http2CodecUtil.SimpleChannelPromiseAggregator promiseAggregator = null;
/*    */       try
/*    */       {
/* 71 */         int streamId = getStreamId(httpMsg.headers());
/*    */         
/*    */ 
/* 74 */         Http2Headers http2Headers = HttpUtil.toHttp2Headers(httpMsg);
/* 75 */         Http2ConnectionEncoder encoder = encoder();
/*    */         
/* 77 */         if (hasData) {
/* 78 */           promiseAggregator = new Http2CodecUtil.SimpleChannelPromiseAggregator(promise, ctx.channel(), ctx.executor());
/* 79 */           encoder.writeHeaders(ctx, streamId, http2Headers, 0, false, promiseAggregator.newPromise());
/* 80 */           httpMsgNeedRelease = false;
/* 81 */           encoder.writeData(ctx, streamId, httpMsg.content(), 0, true, promiseAggregator.newPromise());
/* 82 */           promiseAggregator.doneAllocatingPromises();
/*    */         } else {
/* 84 */           encoder.writeHeaders(ctx, streamId, http2Headers, 0, true, promise);
/*    */         }
/*    */       } catch (Throwable t) {
/* 87 */         if (promiseAggregator == null) {
/* 88 */           promise.tryFailure(t);
/*    */         } else {
/* 90 */           promiseAggregator.setFailure(t);
/*    */         }
/*    */       } finally {
/* 93 */         if (httpMsgNeedRelease) {
/* 94 */           httpMsg.release();
/*    */         }
/*    */       }
/*    */     } else {
/* 98 */       ctx.write(msg, promise);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\HttpToHttp2ConnectionHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */