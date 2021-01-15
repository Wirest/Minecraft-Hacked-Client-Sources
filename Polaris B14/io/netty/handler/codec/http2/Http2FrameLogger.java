/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.channel.ChannelHandlerAdapter;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.logging.InternalLogLevel;
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
/*     */ public class Http2FrameLogger
/*     */   extends ChannelHandlerAdapter
/*     */ {
/*     */   private final InternalLogger logger;
/*     */   private final InternalLogLevel level;
/*     */   
/*     */   public static enum Direction
/*     */   {
/*  32 */     INBOUND, 
/*  33 */     OUTBOUND;
/*     */     
/*     */     private Direction() {}
/*     */   }
/*     */   
/*     */   public Http2FrameLogger(InternalLogLevel level)
/*     */   {
/*  40 */     this(level, InternalLoggerFactory.getInstance(Http2FrameLogger.class));
/*     */   }
/*     */   
/*     */   public Http2FrameLogger(InternalLogLevel level, InternalLogger logger) {
/*  44 */     this.level = ((InternalLogLevel)ObjectUtil.checkNotNull(level, "level"));
/*  45 */     this.logger = ((InternalLogger)ObjectUtil.checkNotNull(logger, "logger"));
/*     */   }
/*     */   
/*     */   public void logData(Direction direction, int streamId, ByteBuf data, int padding, boolean endStream)
/*     */   {
/*  50 */     log(direction, "DATA: streamId=%d, padding=%d, endStream=%b, length=%d, bytes=%s", new Object[] { Integer.valueOf(streamId), Integer.valueOf(padding), Boolean.valueOf(endStream), Integer.valueOf(data.readableBytes()), ByteBufUtil.hexDump(data) });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void logHeaders(Direction direction, int streamId, Http2Headers headers, int padding, boolean endStream)
/*     */   {
/*  57 */     log(direction, "HEADERS: streamId:%d, headers=%s, padding=%d, endStream=%b", new Object[] { Integer.valueOf(streamId), headers, Integer.valueOf(padding), Boolean.valueOf(endStream) });
/*     */   }
/*     */   
/*     */ 
/*     */   public void logHeaders(Direction direction, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endStream)
/*     */   {
/*  63 */     log(direction, "HEADERS: streamId:%d, headers=%s, streamDependency=%d, weight=%d, exclusive=%b, padding=%d, endStream=%b", new Object[] { Integer.valueOf(streamId), headers, Integer.valueOf(streamDependency), Short.valueOf(weight), Boolean.valueOf(exclusive), Integer.valueOf(padding), Boolean.valueOf(endStream) });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void logPriority(Direction direction, int streamId, int streamDependency, short weight, boolean exclusive)
/*     */   {
/*  71 */     log(direction, "PRIORITY: streamId=%d, streamDependency=%d, weight=%d, exclusive=%b", new Object[] { Integer.valueOf(streamId), Integer.valueOf(streamDependency), Short.valueOf(weight), Boolean.valueOf(exclusive) });
/*     */   }
/*     */   
/*     */   public void logRstStream(Direction direction, int streamId, long errorCode)
/*     */   {
/*  76 */     log(direction, "RST_STREAM: streamId=%d, errorCode=%d", new Object[] { Integer.valueOf(streamId), Long.valueOf(errorCode) });
/*     */   }
/*     */   
/*     */   public void logSettingsAck(Direction direction) {
/*  80 */     log(direction, "SETTINGS ack=true", new Object[0]);
/*     */   }
/*     */   
/*     */   public void logSettings(Direction direction, Http2Settings settings) {
/*  84 */     log(direction, "SETTINGS: ack=false, settings=%s", new Object[] { settings });
/*     */   }
/*     */   
/*     */   public void logPing(Direction direction, ByteBuf data) {
/*  88 */     log(direction, "PING: ack=false, length=%d, bytes=%s", new Object[] { Integer.valueOf(data.readableBytes()), ByteBufUtil.hexDump(data) });
/*     */   }
/*     */   
/*     */   public void logPingAck(Direction direction, ByteBuf data) {
/*  92 */     log(direction, "PING: ack=true, length=%d, bytes=%s", new Object[] { Integer.valueOf(data.readableBytes()), ByteBufUtil.hexDump(data) });
/*     */   }
/*     */   
/*     */   public void logPushPromise(Direction direction, int streamId, int promisedStreamId, Http2Headers headers, int padding)
/*     */   {
/*  97 */     log(direction, "PUSH_PROMISE: streamId=%d, promisedStreamId=%d, headers=%s, padding=%d", new Object[] { Integer.valueOf(streamId), Integer.valueOf(promisedStreamId), headers, Integer.valueOf(padding) });
/*     */   }
/*     */   
/*     */   public void logGoAway(Direction direction, int lastStreamId, long errorCode, ByteBuf debugData)
/*     */   {
/* 102 */     log(direction, "GO_AWAY: lastStreamId=%d, errorCode=%d, length=%d, bytes=%s", new Object[] { Integer.valueOf(lastStreamId), Long.valueOf(errorCode), Integer.valueOf(debugData.readableBytes()), ByteBufUtil.hexDump(debugData) });
/*     */   }
/*     */   
/*     */   public void logWindowsUpdate(Direction direction, int streamId, int windowSizeIncrement)
/*     */   {
/* 107 */     log(direction, "WINDOW_UPDATE: streamId=%d, windowSizeIncrement=%d", new Object[] { Integer.valueOf(streamId), Integer.valueOf(windowSizeIncrement) });
/*     */   }
/*     */   
/*     */   public void logUnknownFrame(Direction direction, byte frameType, int streamId, Http2Flags flags, ByteBuf data)
/*     */   {
/* 112 */     log(direction, "UNKNOWN: frameType=%d, streamId=%d, flags=%d, length=%d, bytes=%s", new Object[] { Integer.valueOf(frameType & 0xFF), Integer.valueOf(streamId), Short.valueOf(flags.value()), Integer.valueOf(data.readableBytes()), ByteBufUtil.hexDump(data) });
/*     */   }
/*     */   
/*     */   private void log(Direction direction, String format, Object... args)
/*     */   {
/* 117 */     if (this.logger.isEnabled(this.level)) {
/* 118 */       StringBuilder b = new StringBuilder(200);
/* 119 */       b.append("\n----------------").append(direction.name()).append("--------------------\n").append(String.format(format, args)).append("\n------------------------------------");
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 124 */       this.logger.log(this.level, b.toString());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\Http2FrameLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */