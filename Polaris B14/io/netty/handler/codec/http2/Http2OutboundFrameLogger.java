/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.util.internal.ObjectUtil;
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
/*     */ public class Http2OutboundFrameLogger
/*     */   implements Http2FrameWriter
/*     */ {
/*     */   private final Http2FrameWriter writer;
/*     */   private final Http2FrameLogger logger;
/*     */   
/*     */   public Http2OutboundFrameLogger(Http2FrameWriter writer, Http2FrameLogger logger)
/*     */   {
/*  34 */     this.writer = ((Http2FrameWriter)ObjectUtil.checkNotNull(writer, "writer"));
/*  35 */     this.logger = ((Http2FrameLogger)ObjectUtil.checkNotNull(logger, "logger"));
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture writeData(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endStream, ChannelPromise promise)
/*     */   {
/*  41 */     this.logger.logData(Http2FrameLogger.Direction.OUTBOUND, streamId, data, padding, endStream);
/*  42 */     return this.writer.writeData(ctx, streamId, data, padding, endStream, promise);
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture writeHeaders(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endStream, ChannelPromise promise)
/*     */   {
/*  48 */     this.logger.logHeaders(Http2FrameLogger.Direction.OUTBOUND, streamId, headers, padding, endStream);
/*  49 */     return this.writer.writeHeaders(ctx, streamId, headers, padding, endStream, promise);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture writeHeaders(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endStream, ChannelPromise promise)
/*     */   {
/*  56 */     this.logger.logHeaders(Http2FrameLogger.Direction.OUTBOUND, streamId, headers, streamDependency, weight, exclusive, padding, endStream);
/*     */     
/*  58 */     return this.writer.writeHeaders(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endStream, promise);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ChannelFuture writePriority(ChannelHandlerContext ctx, int streamId, int streamDependency, short weight, boolean exclusive, ChannelPromise promise)
/*     */   {
/*  65 */     this.logger.logPriority(Http2FrameLogger.Direction.OUTBOUND, streamId, streamDependency, weight, exclusive);
/*  66 */     return this.writer.writePriority(ctx, streamId, streamDependency, weight, exclusive, promise);
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture writeRstStream(ChannelHandlerContext ctx, int streamId, long errorCode, ChannelPromise promise)
/*     */   {
/*  72 */     this.logger.logRstStream(Http2FrameLogger.Direction.OUTBOUND, streamId, errorCode);
/*  73 */     return this.writer.writeRstStream(ctx, streamId, errorCode, promise);
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture writeSettings(ChannelHandlerContext ctx, Http2Settings settings, ChannelPromise promise)
/*     */   {
/*  79 */     this.logger.logSettings(Http2FrameLogger.Direction.OUTBOUND, settings);
/*  80 */     return this.writer.writeSettings(ctx, settings, promise);
/*     */   }
/*     */   
/*     */   public ChannelFuture writeSettingsAck(ChannelHandlerContext ctx, ChannelPromise promise)
/*     */   {
/*  85 */     this.logger.logSettingsAck(Http2FrameLogger.Direction.OUTBOUND);
/*  86 */     return this.writer.writeSettingsAck(ctx, promise);
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture writePing(ChannelHandlerContext ctx, boolean ack, ByteBuf data, ChannelPromise promise)
/*     */   {
/*  92 */     this.logger.logPing(Http2FrameLogger.Direction.OUTBOUND, data);
/*  93 */     return this.writer.writePing(ctx, ack, data, promise);
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture writePushPromise(ChannelHandlerContext ctx, int streamId, int promisedStreamId, Http2Headers headers, int padding, ChannelPromise promise)
/*     */   {
/*  99 */     this.logger.logPushPromise(Http2FrameLogger.Direction.OUTBOUND, streamId, promisedStreamId, headers, padding);
/* 100 */     return this.writer.writePushPromise(ctx, streamId, promisedStreamId, headers, padding, promise);
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture writeGoAway(ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData, ChannelPromise promise)
/*     */   {
/* 106 */     this.logger.logGoAway(Http2FrameLogger.Direction.OUTBOUND, lastStreamId, errorCode, debugData);
/* 107 */     return this.writer.writeGoAway(ctx, lastStreamId, errorCode, debugData, promise);
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture writeWindowUpdate(ChannelHandlerContext ctx, int streamId, int windowSizeIncrement, ChannelPromise promise)
/*     */   {
/* 113 */     this.logger.logWindowsUpdate(Http2FrameLogger.Direction.OUTBOUND, streamId, windowSizeIncrement);
/* 114 */     return this.writer.writeWindowUpdate(ctx, streamId, windowSizeIncrement, promise);
/*     */   }
/*     */   
/*     */ 
/*     */   public ChannelFuture writeFrame(ChannelHandlerContext ctx, byte frameType, int streamId, Http2Flags flags, ByteBuf payload, ChannelPromise promise)
/*     */   {
/* 120 */     this.logger.logUnknownFrame(Http2FrameLogger.Direction.OUTBOUND, frameType, streamId, flags, payload);
/* 121 */     return this.writer.writeFrame(ctx, frameType, streamId, flags, payload, promise);
/*     */   }
/*     */   
/*     */   public void close()
/*     */   {
/* 126 */     this.writer.close();
/*     */   }
/*     */   
/*     */   public Http2FrameWriter.Configuration configuration()
/*     */   {
/* 131 */     return this.writer.configuration();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\Http2OutboundFrameLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */