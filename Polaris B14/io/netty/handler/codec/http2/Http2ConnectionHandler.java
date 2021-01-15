/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.handler.codec.ByteToMessageDecoder;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.util.Collection;
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
/*     */ public class Http2ConnectionHandler
/*     */   extends ByteToMessageDecoder
/*     */   implements Http2LifecycleManager
/*     */ {
/*     */   private final Http2ConnectionDecoder decoder;
/*     */   private final Http2ConnectionEncoder encoder;
/*     */   private ByteBuf clientPrefaceString;
/*     */   private boolean prefaceSent;
/*     */   private ChannelFutureListener closeListener;
/*     */   
/*     */   public Http2ConnectionHandler(boolean server, Http2FrameListener listener)
/*     */   {
/*  56 */     this(new DefaultHttp2Connection(server), listener);
/*     */   }
/*     */   
/*     */   public Http2ConnectionHandler(Http2Connection connection, Http2FrameListener listener) {
/*  60 */     this(connection, new DefaultHttp2FrameReader(), new DefaultHttp2FrameWriter(), listener);
/*     */   }
/*     */   
/*     */   public Http2ConnectionHandler(Http2Connection connection, Http2FrameReader frameReader, Http2FrameWriter frameWriter, Http2FrameListener listener)
/*     */   {
/*  65 */     this(DefaultHttp2ConnectionDecoder.newBuilder().connection(connection).frameReader(frameReader).listener(listener), DefaultHttp2ConnectionEncoder.newBuilder().connection(connection).frameWriter(frameWriter));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Http2ConnectionHandler(Http2ConnectionDecoder.Builder decoderBuilder, Http2ConnectionEncoder.Builder encoderBuilder)
/*     */   {
/*  77 */     ObjectUtil.checkNotNull(decoderBuilder, "decoderBuilder");
/*  78 */     ObjectUtil.checkNotNull(encoderBuilder, "encoderBuilder");
/*     */     
/*  80 */     if (encoderBuilder.lifecycleManager() != decoderBuilder.lifecycleManager())
/*  81 */       throw new IllegalArgumentException("Encoder and Decoder must share a lifecycle manager");
/*  82 */     if (encoderBuilder.lifecycleManager() == null) {
/*  83 */       encoderBuilder.lifecycleManager(this);
/*  84 */       decoderBuilder.lifecycleManager(this);
/*     */     }
/*     */     
/*     */ 
/*  88 */     this.encoder = ((Http2ConnectionEncoder)ObjectUtil.checkNotNull(encoderBuilder.build(), "encoder"));
/*     */     
/*     */ 
/*  91 */     decoderBuilder.encoder(this.encoder);
/*  92 */     this.decoder = ((Http2ConnectionDecoder)ObjectUtil.checkNotNull(decoderBuilder.build(), "decoder"));
/*     */     
/*     */ 
/*  95 */     ObjectUtil.checkNotNull(this.encoder.connection(), "encoder.connection");
/*  96 */     if (this.encoder.connection() != this.decoder.connection()) {
/*  97 */       throw new IllegalArgumentException("Encoder and Decoder do not share the same connection object");
/*     */     }
/*     */     
/* 100 */     this.clientPrefaceString = clientPrefaceString(this.encoder.connection());
/*     */   }
/*     */   
/*     */   public Http2Connection connection() {
/* 104 */     return this.encoder.connection();
/*     */   }
/*     */   
/*     */   public Http2ConnectionDecoder decoder() {
/* 108 */     return this.decoder;
/*     */   }
/*     */   
/*     */   public Http2ConnectionEncoder encoder() {
/* 112 */     return this.encoder;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void onHttpClientUpgrade()
/*     */     throws Http2Exception
/*     */   {
/* 120 */     if (connection().isServer()) {
/* 121 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Client-side HTTP upgrade requested for a server", new Object[0]);
/*     */     }
/* 123 */     if ((this.prefaceSent) || (this.decoder.prefaceReceived())) {
/* 124 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "HTTP upgrade must occur before HTTP/2 preface is sent or received", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/* 128 */     connection().createLocalStream(1).open(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void onHttpServerUpgrade(Http2Settings settings)
/*     */     throws Http2Exception
/*     */   {
/* 136 */     if (!connection().isServer()) {
/* 137 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Server-side HTTP upgrade requested for a client", new Object[0]);
/*     */     }
/* 139 */     if ((this.prefaceSent) || (this.decoder.prefaceReceived())) {
/* 140 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "HTTP upgrade must occur before HTTP/2 preface is sent or received", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/* 144 */     this.encoder.remoteSettings(settings);
/*     */     
/*     */ 
/* 147 */     connection().createRemoteStream(1).open(true);
/*     */   }
/*     */   
/*     */   public void channelActive(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 153 */     sendPreface(ctx);
/* 154 */     super.channelActive(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */   public void handlerAdded(ChannelHandlerContext ctx)
/*     */     throws Exception
/*     */   {
/* 161 */     sendPreface(ctx);
/*     */   }
/*     */   
/*     */   protected void handlerRemoved0(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 166 */     dispose();
/*     */   }
/*     */   
/*     */   public void close(ChannelHandlerContext ctx, ChannelPromise promise)
/*     */     throws Exception
/*     */   {
/* 172 */     if (!ctx.channel().isActive()) {
/* 173 */       ctx.close(promise);
/* 174 */       return;
/*     */     }
/*     */     
/* 177 */     ChannelFuture future = writeGoAway(ctx, null);
/*     */     
/*     */ 
/*     */ 
/* 181 */     if (connection().numActiveStreams() == 0) {
/* 182 */       future.addListener(new ClosingChannelFutureListener(ctx, promise));
/*     */     } else {
/* 184 */       this.closeListener = new ClosingChannelFutureListener(ctx, promise);
/*     */     }
/*     */   }
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 190 */     ChannelFuture future = ctx.newSucceededFuture();
/* 191 */     Collection<Http2Stream> streams = connection().activeStreams();
/* 192 */     for (Http2Stream s : (Http2Stream[])streams.toArray(new Http2Stream[streams.size()])) {
/* 193 */       closeStream(s, future);
/*     */     }
/* 195 */     super.channelInactive(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
/*     */     throws Exception
/*     */   {
/* 203 */     if (Http2CodecUtil.getEmbeddedHttp2Exception(cause) != null)
/*     */     {
/* 205 */       onException(ctx, cause);
/*     */     } else {
/* 207 */       super.exceptionCaught(ctx, cause);
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
/*     */   public void closeLocalSide(Http2Stream stream, ChannelFuture future)
/*     */   {
/* 220 */     switch (stream.state()) {
/*     */     case HALF_CLOSED_LOCAL: 
/*     */     case OPEN: 
/* 223 */       stream.closeLocalSide();
/* 224 */       break;
/*     */     default: 
/* 226 */       closeStream(stream, future);
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void closeRemoteSide(Http2Stream stream, ChannelFuture future)
/*     */   {
/* 240 */     switch (stream.state()) {
/*     */     case OPEN: 
/*     */     case HALF_CLOSED_REMOTE: 
/* 243 */       stream.closeRemoteSide();
/* 244 */       break;
/*     */     default: 
/* 246 */       closeStream(stream, future);
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void closeStream(final Http2Stream stream, ChannelFuture future)
/*     */   {
/* 260 */     stream.close();
/*     */     
/* 262 */     future.addListener(new ChannelFutureListener()
/*     */     {
/*     */       public void operationComplete(ChannelFuture future) throws Exception
/*     */       {
/* 266 */         Http2ConnectionHandler.this.connection().deactivate(stream);
/*     */         
/*     */ 
/*     */ 
/* 270 */         if ((Http2ConnectionHandler.this.closeListener != null) && (Http2ConnectionHandler.this.connection().numActiveStreams() == 0)) {
/* 271 */           Http2ConnectionHandler.this.closeListener.operationComplete(future);
/*     */         }
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onException(ChannelHandlerContext ctx, Throwable cause)
/*     */   {
/* 282 */     Http2Exception embedded = Http2CodecUtil.getEmbeddedHttp2Exception(cause);
/* 283 */     if (Http2Exception.isStreamError(embedded)) {
/* 284 */       onStreamError(ctx, cause, (Http2Exception.StreamException)embedded);
/* 285 */     } else if ((embedded instanceof Http2Exception.CompositeStreamException)) {
/* 286 */       Http2Exception.CompositeStreamException compositException = (Http2Exception.CompositeStreamException)embedded;
/* 287 */       for (Http2Exception.StreamException streamException : compositException) {
/* 288 */         onStreamError(ctx, cause, streamException);
/*     */       }
/*     */     } else {
/* 291 */       onConnectionError(ctx, cause, embedded);
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
/*     */   protected void onConnectionError(ChannelHandlerContext ctx, Throwable cause, Http2Exception http2Ex)
/*     */   {
/* 305 */     if (http2Ex == null) {
/* 306 */       http2Ex = new Http2Exception(Http2Error.INTERNAL_ERROR, cause.getMessage(), cause);
/*     */     }
/* 308 */     writeGoAway(ctx, http2Ex).addListener(new ClosingChannelFutureListener(ctx, ctx.newPromise()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void onStreamError(ChannelHandlerContext ctx, Throwable cause, Http2Exception.StreamException http2Ex)
/*     */   {
/* 320 */     writeRstStream(ctx, http2Ex.streamId(), http2Ex.error().code(), ctx.newPromise());
/*     */   }
/*     */   
/*     */   protected Http2FrameWriter frameWriter() {
/* 324 */     return encoder().frameWriter();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChannelFuture writeRstStream(ChannelHandlerContext ctx, int streamId, long errorCode, ChannelPromise promise)
/*     */   {
/* 333 */     Http2Stream stream = connection().stream(streamId);
/* 334 */     ChannelFuture future = frameWriter().writeRstStream(ctx, streamId, errorCode, promise);
/* 335 */     ctx.flush();
/*     */     
/* 337 */     if (stream != null) {
/* 338 */       stream.resetSent();
/* 339 */       closeStream(stream, promise);
/*     */     }
/*     */     
/* 342 */     return future;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChannelFuture writeGoAway(ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData, ChannelPromise promise)
/*     */   {
/* 351 */     Http2Connection connection = connection();
/* 352 */     if (connection.isGoAway()) {
/* 353 */       debugData.release();
/* 354 */       return ctx.newSucceededFuture();
/*     */     }
/*     */     
/* 357 */     ChannelFuture future = frameWriter().writeGoAway(ctx, lastStreamId, errorCode, debugData, promise);
/* 358 */     ctx.flush();
/*     */     
/* 360 */     connection.goAwaySent(lastStreamId);
/* 361 */     return future;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private ChannelFuture writeGoAway(ChannelHandlerContext ctx, Http2Exception cause)
/*     */   {
/* 368 */     Http2Connection connection = connection();
/* 369 */     if (connection.isGoAway()) {
/* 370 */       return ctx.newSucceededFuture();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 375 */     long errorCode = cause != null ? cause.error().code() : Http2Error.NO_ERROR.code();
/* 376 */     ByteBuf debugData = Http2CodecUtil.toByteBuf(ctx, cause);
/* 377 */     int lastKnownStream = connection.remote().lastStreamCreated();
/* 378 */     return writeGoAway(ctx, lastKnownStream, errorCode, debugData, ctx.newPromise());
/*     */   }
/*     */   
/*     */   protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out)
/*     */     throws Exception
/*     */   {
/*     */     try
/*     */     {
/* 386 */       if (!readClientPrefaceString(in))
/*     */       {
/* 388 */         return;
/*     */       }
/*     */       
/* 391 */       this.decoder.decodeFrame(ctx, in, out);
/*     */     } catch (Throwable e) {
/* 393 */       onException(ctx, e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void sendPreface(ChannelHandlerContext ctx)
/*     */   {
/* 401 */     if ((this.prefaceSent) || (!ctx.channel().isActive())) {
/* 402 */       return;
/*     */     }
/*     */     
/* 405 */     this.prefaceSent = true;
/*     */     
/* 407 */     if (!connection().isServer())
/*     */     {
/* 409 */       ctx.write(Http2CodecUtil.connectionPrefaceBuf()).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
/*     */     }
/*     */     
/*     */ 
/* 413 */     this.encoder.writeSettings(ctx, this.decoder.localSettings(), ctx.newPromise()).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void dispose()
/*     */   {
/* 421 */     this.encoder.close();
/* 422 */     this.decoder.close();
/* 423 */     if (this.clientPrefaceString != null) {
/* 424 */       this.clientPrefaceString.release();
/* 425 */       this.clientPrefaceString = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean readClientPrefaceString(ByteBuf in)
/*     */     throws Http2Exception
/*     */   {
/* 436 */     if (this.clientPrefaceString == null) {
/* 437 */       return true;
/*     */     }
/*     */     
/* 440 */     int prefaceRemaining = this.clientPrefaceString.readableBytes();
/* 441 */     int bytesRead = Math.min(in.readableBytes(), prefaceRemaining);
/*     */     
/*     */ 
/* 444 */     ByteBuf sourceSlice = in.readSlice(bytesRead);
/*     */     
/*     */ 
/* 447 */     ByteBuf prefaceSlice = this.clientPrefaceString.readSlice(bytesRead);
/*     */     
/*     */ 
/* 450 */     if ((bytesRead == 0) || (!prefaceSlice.equals(sourceSlice))) {
/* 451 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "HTTP/2 client preface string missing or corrupt.", new Object[0]);
/*     */     }
/*     */     
/* 454 */     if (!this.clientPrefaceString.isReadable())
/*     */     {
/* 456 */       this.clientPrefaceString.release();
/* 457 */       this.clientPrefaceString = null;
/* 458 */       return true;
/*     */     }
/* 460 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static ByteBuf clientPrefaceString(Http2Connection connection)
/*     */   {
/* 467 */     return connection.isServer() ? Http2CodecUtil.connectionPrefaceBuf() : null;
/*     */   }
/*     */   
/*     */   private static final class ClosingChannelFutureListener
/*     */     implements ChannelFutureListener
/*     */   {
/*     */     private final ChannelHandlerContext ctx;
/*     */     private final ChannelPromise promise;
/*     */     
/*     */     ClosingChannelFutureListener(ChannelHandlerContext ctx, ChannelPromise promise)
/*     */     {
/* 478 */       this.ctx = ctx;
/* 479 */       this.promise = promise;
/*     */     }
/*     */     
/*     */     public void operationComplete(ChannelFuture sentGoAwayFuture) throws Exception
/*     */     {
/* 484 */       this.ctx.close(this.promise);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\Http2ConnectionHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */