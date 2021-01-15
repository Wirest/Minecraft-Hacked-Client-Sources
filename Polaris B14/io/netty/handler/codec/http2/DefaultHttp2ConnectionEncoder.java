/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.util.ReferenceCountUtil;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.util.ArrayDeque;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultHttp2ConnectionEncoder
/*     */   implements Http2ConnectionEncoder
/*     */ {
/*     */   private final Http2FrameWriter frameWriter;
/*     */   private final Http2Connection connection;
/*     */   private final Http2LifecycleManager lifecycleManager;
/*  40 */   private final ArrayDeque<Http2Settings> outstandingLocalSettingsQueue = new ArrayDeque(4);
/*     */   
/*     */ 
/*     */   public static class Builder
/*     */     implements Http2ConnectionEncoder.Builder
/*     */   {
/*     */     protected Http2FrameWriter frameWriter;
/*     */     
/*     */     protected Http2Connection connection;
/*     */     protected Http2LifecycleManager lifecycleManager;
/*     */     
/*     */     public Builder connection(Http2Connection connection)
/*     */     {
/*  53 */       this.connection = connection;
/*  54 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */     public Builder lifecycleManager(Http2LifecycleManager lifecycleManager)
/*     */     {
/*  60 */       this.lifecycleManager = lifecycleManager;
/*  61 */       return this;
/*     */     }
/*     */     
/*     */     public Http2LifecycleManager lifecycleManager()
/*     */     {
/*  66 */       return this.lifecycleManager;
/*     */     }
/*     */     
/*     */     public Builder frameWriter(Http2FrameWriter frameWriter)
/*     */     {
/*  71 */       this.frameWriter = frameWriter;
/*  72 */       return this;
/*     */     }
/*     */     
/*     */     public Http2ConnectionEncoder build()
/*     */     {
/*  77 */       return new DefaultHttp2ConnectionEncoder(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder newBuilder() {
/*  82 */     return new Builder();
/*     */   }
/*     */   
/*     */   protected DefaultHttp2ConnectionEncoder(Builder builder) {
/*  86 */     this.connection = ((Http2Connection)ObjectUtil.checkNotNull(builder.connection, "connection"));
/*  87 */     this.frameWriter = ((Http2FrameWriter)ObjectUtil.checkNotNull(builder.frameWriter, "frameWriter"));
/*  88 */     this.lifecycleManager = ((Http2LifecycleManager)ObjectUtil.checkNotNull(builder.lifecycleManager, "lifecycleManager"));
/*  89 */     if (this.connection.remote().flowController() == null) {
/*  90 */       this.connection.remote().flowController(new DefaultHttp2RemoteFlowController(this.connection));
/*     */     }
/*     */   }
/*     */   
/*     */   public Http2FrameWriter frameWriter()
/*     */   {
/*  96 */     return this.frameWriter;
/*     */   }
/*     */   
/*     */   public Http2Connection connection()
/*     */   {
/* 101 */     return this.connection;
/*     */   }
/*     */   
/*     */   public final Http2RemoteFlowController flowController()
/*     */   {
/* 106 */     return (Http2RemoteFlowController)connection().remote().flowController();
/*     */   }
/*     */   
/*     */   public void remoteSettings(Http2Settings settings) throws Http2Exception
/*     */   {
/* 111 */     Boolean pushEnabled = settings.pushEnabled();
/* 112 */     Http2FrameWriter.Configuration config = configuration();
/* 113 */     Http2HeaderTable outboundHeaderTable = config.headerTable();
/* 114 */     Http2FrameSizePolicy outboundFrameSizePolicy = config.frameSizePolicy();
/* 115 */     if (pushEnabled != null) {
/* 116 */       if (!this.connection.isServer()) {
/* 117 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Client received SETTINGS frame with ENABLE_PUSH specified", new Object[0]);
/*     */       }
/* 119 */       this.connection.remote().allowPushTo(pushEnabled.booleanValue());
/*     */     }
/*     */     
/* 122 */     Long maxConcurrentStreams = settings.maxConcurrentStreams();
/* 123 */     if (maxConcurrentStreams != null) {
/* 124 */       this.connection.local().maxStreams((int)Math.min(maxConcurrentStreams.longValue(), 2147483647L));
/*     */     }
/*     */     
/* 127 */     Long headerTableSize = settings.headerTableSize();
/* 128 */     if (headerTableSize != null) {
/* 129 */       outboundHeaderTable.maxHeaderTableSize((int)Math.min(headerTableSize.longValue(), 2147483647L));
/*     */     }
/*     */     
/* 132 */     Integer maxHeaderListSize = settings.maxHeaderListSize();
/* 133 */     if (maxHeaderListSize != null) {
/* 134 */       outboundHeaderTable.maxHeaderListSize(maxHeaderListSize.intValue());
/*     */     }
/*     */     
/* 137 */     Integer maxFrameSize = settings.maxFrameSize();
/* 138 */     if (maxFrameSize != null) {
/* 139 */       outboundFrameSizePolicy.maxFrameSize(maxFrameSize.intValue());
/*     */     }
/*     */     
/* 142 */     Integer initialWindowSize = settings.initialWindowSize();
/* 143 */     if (initialWindowSize != null) {
/* 144 */       flowController().initialWindowSize(initialWindowSize.intValue());
/*     */     }
/*     */   }
/*     */   
/*     */   public ChannelFuture writeData(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endOfStream, ChannelPromise promise)
/*     */   {
/*     */     Http2Stream stream;
/*     */     try
/*     */     {
/* 153 */       if (this.connection.isGoAway()) {
/* 154 */         throw new IllegalStateException("Sending data after connection going away.");
/*     */       }
/*     */       
/* 157 */       stream = this.connection.requireStream(streamId);
/*     */       
/*     */ 
/* 160 */       switch (stream.state())
/*     */       {
/*     */       case OPEN: 
/*     */       case HALF_CLOSED_REMOTE: 
/*     */         break;
/*     */       default: 
/* 166 */         throw new IllegalStateException(String.format("Stream %d in unexpected state: %s", new Object[] { Integer.valueOf(stream.id()), stream.state() }));
/*     */       }
/*     */       
/*     */       
/* 170 */       if (endOfStream) {
/* 171 */         this.lifecycleManager.closeLocalSide(stream, promise);
/*     */       }
/*     */     } catch (Throwable e) {
/* 174 */       data.release();
/* 175 */       return promise.setFailure(e);
/*     */     }
/*     */     
/*     */ 
/* 179 */     flowController().sendFlowControlled(ctx, stream, new FlowControlledData(ctx, stream, data, padding, endOfStream, promise, null));
/*     */     
/* 181 */     return promise;
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture writeHeaders(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endStream, ChannelPromise promise)
/*     */   {
/* 187 */     return writeHeaders(ctx, streamId, headers, 0, (short)16, false, padding, endStream, promise);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture writeHeaders(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endOfStream, ChannelPromise promise)
/*     */   {
/*     */     try
/*     */     {
/* 196 */       if (this.connection.isGoAway()) {
/* 197 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Sending headers after connection going away.", new Object[0]);
/*     */       }
/* 199 */       Http2Stream stream = this.connection.stream(streamId);
/* 200 */       if (stream == null) {
/* 201 */         stream = this.connection.createLocalStream(streamId);
/*     */       }
/*     */       
/* 204 */       switch (stream.state()) {
/*     */       case RESERVED_LOCAL: 
/*     */       case IDLE: 
/* 207 */         stream.open(endOfStream);
/* 208 */         break;
/*     */       case OPEN: 
/*     */       case HALF_CLOSED_REMOTE: 
/*     */         break;
/*     */       
/*     */       default: 
/* 214 */         throw new IllegalStateException(String.format("Stream %d in unexpected state: %s", new Object[] { Integer.valueOf(stream.id()), stream.state() }));
/*     */       }
/*     */       
/*     */       
/*     */ 
/* 219 */       flowController().sendFlowControlled(ctx, stream, new FlowControlledHeaders(ctx, stream, headers, streamDependency, weight, exclusive, padding, endOfStream, promise, null));
/*     */       
/*     */ 
/* 222 */       if (endOfStream) {
/* 223 */         this.lifecycleManager.closeLocalSide(stream, promise);
/*     */       }
/* 225 */       return promise;
/*     */     } catch (Http2NoMoreStreamIdsException e) {
/* 227 */       this.lifecycleManager.onException(ctx, e);
/* 228 */       return promise.setFailure(e);
/*     */     } catch (Throwable e) {
/* 230 */       return promise.setFailure(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public ChannelFuture writePriority(ChannelHandlerContext ctx, int streamId, int streamDependency, short weight, boolean exclusive, ChannelPromise promise)
/*     */   {
/*     */     try
/*     */     {
/* 238 */       if (this.connection.isGoAway()) {
/* 239 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Sending priority after connection going away.", new Object[0]);
/*     */       }
/*     */       
/*     */ 
/* 243 */       Http2Stream stream = this.connection.stream(streamId);
/* 244 */       if (stream == null) {
/* 245 */         stream = this.connection.createLocalStream(streamId);
/*     */       }
/*     */       
/* 248 */       stream.setPriority(streamDependency, weight, exclusive);
/*     */     } catch (Throwable e) {
/* 250 */       return promise.setFailure(e);
/*     */     }
/*     */     
/* 253 */     ChannelFuture future = this.frameWriter.writePriority(ctx, streamId, streamDependency, weight, exclusive, promise);
/* 254 */     ctx.flush();
/* 255 */     return future;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture writeRstStream(ChannelHandlerContext ctx, int streamId, long errorCode, ChannelPromise promise)
/*     */   {
/* 262 */     return this.lifecycleManager.writeRstStream(ctx, streamId, errorCode, promise);
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
/*     */   public ChannelFuture writeRstStream(ChannelHandlerContext ctx, int streamId, long errorCode, ChannelPromise promise, boolean writeIfNoStream)
/*     */   {
/* 280 */     Http2Stream stream = this.connection.stream(streamId);
/* 281 */     if ((stream == null) && (!writeIfNoStream))
/*     */     {
/* 283 */       promise.setSuccess();
/* 284 */       return promise;
/*     */     }
/*     */     
/* 287 */     ChannelFuture future = this.frameWriter.writeRstStream(ctx, streamId, errorCode, promise);
/* 288 */     ctx.flush();
/*     */     
/* 290 */     if (stream != null) {
/* 291 */       stream.resetSent();
/* 292 */       this.lifecycleManager.closeStream(stream, promise);
/*     */     }
/*     */     
/* 295 */     return future;
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture writeSettings(ChannelHandlerContext ctx, Http2Settings settings, ChannelPromise promise)
/*     */   {
/* 301 */     this.outstandingLocalSettingsQueue.add(settings);
/*     */     try {
/* 303 */       if (this.connection.isGoAway()) {
/* 304 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Sending settings after connection going away.", new Object[0]);
/*     */       }
/*     */       
/* 307 */       Boolean pushEnabled = settings.pushEnabled();
/* 308 */       if ((pushEnabled != null) && (this.connection.isServer())) {
/* 309 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Server sending SETTINGS frame with ENABLE_PUSH specified", new Object[0]);
/*     */       }
/*     */     } catch (Throwable e) {
/* 312 */       return promise.setFailure(e);
/*     */     }
/*     */     
/* 315 */     ChannelFuture future = this.frameWriter.writeSettings(ctx, settings, promise);
/* 316 */     ctx.flush();
/* 317 */     return future;
/*     */   }
/*     */   
/*     */   public ChannelFuture writeSettingsAck(ChannelHandlerContext ctx, ChannelPromise promise)
/*     */   {
/* 322 */     ChannelFuture future = this.frameWriter.writeSettingsAck(ctx, promise);
/* 323 */     ctx.flush();
/* 324 */     return future;
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture writePing(ChannelHandlerContext ctx, boolean ack, ByteBuf data, ChannelPromise promise)
/*     */   {
/* 330 */     if (this.connection.isGoAway()) {
/* 331 */       data.release();
/* 332 */       return promise.setFailure(Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Sending ping after connection going away.", new Object[0]));
/*     */     }
/*     */     
/* 335 */     ChannelFuture future = this.frameWriter.writePing(ctx, ack, data, promise);
/* 336 */     ctx.flush();
/* 337 */     return future;
/*     */   }
/*     */   
/*     */   public ChannelFuture writePushPromise(ChannelHandlerContext ctx, int streamId, int promisedStreamId, Http2Headers headers, int padding, ChannelPromise promise)
/*     */   {
/*     */     try
/*     */     {
/* 344 */       if (this.connection.isGoAway()) {
/* 345 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Sending push promise after connection going away.", new Object[0]);
/*     */       }
/*     */       
/*     */ 
/* 349 */       Http2Stream stream = this.connection.requireStream(streamId);
/* 350 */       this.connection.local().reservePushStream(promisedStreamId, stream);
/*     */     } catch (Throwable e) {
/* 352 */       return promise.setFailure(e);
/*     */     }
/*     */     
/* 355 */     ChannelFuture future = this.frameWriter.writePushPromise(ctx, streamId, promisedStreamId, headers, padding, promise);
/* 356 */     ctx.flush();
/* 357 */     return future;
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture writeGoAway(ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData, ChannelPromise promise)
/*     */   {
/* 363 */     return this.lifecycleManager.writeGoAway(ctx, lastStreamId, errorCode, debugData, promise);
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture writeWindowUpdate(ChannelHandlerContext ctx, int streamId, int windowSizeIncrement, ChannelPromise promise)
/*     */   {
/* 369 */     return promise.setFailure(new UnsupportedOperationException("Use the Http2[Inbound|Outbound]FlowController objects to control window sizes"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture writeFrame(ChannelHandlerContext ctx, byte frameType, int streamId, Http2Flags flags, ByteBuf payload, ChannelPromise promise)
/*     */   {
/* 376 */     return this.frameWriter.writeFrame(ctx, frameType, streamId, flags, payload, promise);
/*     */   }
/*     */   
/*     */   public void close()
/*     */   {
/* 381 */     this.frameWriter.close();
/*     */   }
/*     */   
/*     */   public Http2Settings pollSentSettings()
/*     */   {
/* 386 */     return (Http2Settings)this.outstandingLocalSettingsQueue.poll();
/*     */   }
/*     */   
/*     */   public Http2FrameWriter.Configuration configuration()
/*     */   {
/* 391 */     return this.frameWriter.configuration();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final class FlowControlledData
/*     */     extends DefaultHttp2ConnectionEncoder.FlowControlledBase
/*     */   {
/*     */     private ByteBuf data;
/*     */     
/*     */ 
/*     */     private int size;
/*     */     
/*     */ 
/*     */ 
/*     */     private FlowControlledData(ChannelHandlerContext ctx, Http2Stream stream, ByteBuf data, int padding, boolean endOfStream, ChannelPromise promise)
/*     */     {
/* 409 */       super(ctx, stream, padding, endOfStream, promise);
/* 410 */       this.data = data;
/* 411 */       this.size = (data.readableBytes() + padding);
/*     */     }
/*     */     
/*     */     public int size()
/*     */     {
/* 416 */       return this.size;
/*     */     }
/*     */     
/*     */     public void error(Throwable cause)
/*     */     {
/* 421 */       ReferenceCountUtil.safeRelease(this.data);
/* 422 */       DefaultHttp2ConnectionEncoder.this.lifecycleManager.onException(this.ctx, cause);
/* 423 */       this.data = null;
/* 424 */       this.size = 0;
/* 425 */       this.promise.tryFailure(cause);
/*     */     }
/*     */     
/*     */     public boolean write(int allowedBytes)
/*     */     {
/* 430 */       if (this.data == null) {
/* 431 */         return false;
/*     */       }
/* 433 */       if ((allowedBytes == 0) && (size() != 0))
/*     */       {
/* 435 */         return false;
/*     */       }
/* 437 */       int maxFrameSize = DefaultHttp2ConnectionEncoder.this.frameWriter().configuration().frameSizePolicy().maxFrameSize();
/*     */       try {
/* 439 */         int bytesWritten = 0;
/*     */         do {
/* 441 */           int allowedFrameSize = Math.min(maxFrameSize, allowedBytes - bytesWritten);
/*     */           
/*     */ 
/* 444 */           int writeableData = this.data.readableBytes();
/* 445 */           ByteBuf toWrite; ByteBuf toWrite; if (writeableData > allowedFrameSize) {
/* 446 */             writeableData = allowedFrameSize;
/* 447 */             toWrite = this.data.readSlice(writeableData).retain();
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/* 452 */             toWrite = this.data;
/* 453 */             this.data = Unpooled.EMPTY_BUFFER;
/*     */           }
/* 455 */           int writeablePadding = Math.min(allowedFrameSize - writeableData, this.padding);
/* 456 */           this.padding -= writeablePadding;
/* 457 */           bytesWritten += writeableData + writeablePadding;
/*     */           ChannelPromise writePromise;
/* 459 */           ChannelPromise writePromise; if (this.size == bytesWritten)
/*     */           {
/* 461 */             writePromise = this.promise;
/*     */           }
/*     */           else {
/* 464 */             writePromise = this.ctx.newPromise();
/* 465 */             writePromise.addListener(this);
/*     */           }
/* 467 */           DefaultHttp2ConnectionEncoder.this.frameWriter().writeData(this.ctx, this.stream.id(), toWrite, writeablePadding, (this.size == bytesWritten) && (this.endOfStream), writePromise);
/*     */         }
/* 469 */         while ((this.size != bytesWritten) && (allowedBytes > bytesWritten));
/* 470 */         this.size -= bytesWritten;
/* 471 */         return true;
/*     */       } catch (Throwable e) {
/* 473 */         error(e); }
/* 474 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private final class FlowControlledHeaders
/*     */     extends DefaultHttp2ConnectionEncoder.FlowControlledBase
/*     */   {
/*     */     private final Http2Headers headers;
/*     */     
/*     */     private final int streamDependency;
/*     */     
/*     */     private final short weight;
/*     */     
/*     */     private final boolean exclusive;
/*     */     
/*     */ 
/*     */     private FlowControlledHeaders(ChannelHandlerContext ctx, Http2Stream stream, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endOfStream, ChannelPromise promise)
/*     */     {
/* 494 */       super(ctx, stream, padding, endOfStream, promise);
/* 495 */       this.headers = headers;
/* 496 */       this.streamDependency = streamDependency;
/* 497 */       this.weight = weight;
/* 498 */       this.exclusive = exclusive;
/*     */     }
/*     */     
/*     */     public int size()
/*     */     {
/* 503 */       return 0;
/*     */     }
/*     */     
/*     */     public void error(Throwable cause)
/*     */     {
/* 508 */       DefaultHttp2ConnectionEncoder.this.lifecycleManager.onException(this.ctx, cause);
/* 509 */       this.promise.tryFailure(cause);
/*     */     }
/*     */     
/*     */     public boolean write(int allowedBytes)
/*     */     {
/* 514 */       DefaultHttp2ConnectionEncoder.this.frameWriter().writeHeaders(this.ctx, this.stream.id(), this.headers, this.streamDependency, this.weight, this.exclusive, this.padding, this.endOfStream, this.promise);
/*     */       
/* 516 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract class FlowControlledBase
/*     */     implements Http2RemoteFlowController.FlowControlled, ChannelFutureListener
/*     */   {
/*     */     protected final ChannelHandlerContext ctx;
/*     */     
/*     */     protected final Http2Stream stream;
/*     */     protected final ChannelPromise promise;
/*     */     protected final boolean endOfStream;
/*     */     protected int padding;
/*     */     
/*     */     public FlowControlledBase(ChannelHandlerContext ctx, Http2Stream stream, int padding, boolean endOfStream, ChannelPromise promise)
/*     */     {
/* 533 */       this.ctx = ctx;
/* 534 */       if (padding < 0) {
/* 535 */         throw new IllegalArgumentException("padding must be >= 0");
/*     */       }
/* 537 */       this.padding = padding;
/* 538 */       this.endOfStream = endOfStream;
/* 539 */       this.stream = stream;
/* 540 */       this.promise = promise;
/* 541 */       promise.addListener(this);
/*     */     }
/*     */     
/*     */     public void operationComplete(ChannelFuture future) throws Exception
/*     */     {
/* 546 */       if (!future.isSuccess()) {
/* 547 */         error(future.cause());
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\DefaultHttp2ConnectionEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */