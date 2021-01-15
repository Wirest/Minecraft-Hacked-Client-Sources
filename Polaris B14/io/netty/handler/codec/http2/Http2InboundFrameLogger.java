/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
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
/*     */ public class Http2InboundFrameLogger
/*     */   implements Http2FrameReader
/*     */ {
/*     */   private final Http2FrameReader reader;
/*     */   private final Http2FrameLogger logger;
/*     */   
/*     */   public Http2InboundFrameLogger(Http2FrameReader reader, Http2FrameLogger logger)
/*     */   {
/*  32 */     this.reader = ((Http2FrameReader)ObjectUtil.checkNotNull(reader, "reader"));
/*  33 */     this.logger = ((Http2FrameLogger)ObjectUtil.checkNotNull(logger, "logger"));
/*     */   }
/*     */   
/*     */   public void readFrame(ChannelHandlerContext ctx, ByteBuf input, final Http2FrameListener listener)
/*     */     throws Http2Exception
/*     */   {
/*  39 */     this.reader.readFrame(ctx, input, new Http2FrameListener()
/*     */     {
/*     */ 
/*     */       public int onDataRead(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endOfStream)
/*     */         throws Http2Exception
/*     */       {
/*  45 */         Http2InboundFrameLogger.this.logger.logData(Http2FrameLogger.Direction.INBOUND, streamId, data, padding, endOfStream);
/*  46 */         return listener.onDataRead(ctx, streamId, data, padding, endOfStream);
/*     */       }
/*     */       
/*     */ 
/*     */       public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endStream)
/*     */         throws Http2Exception
/*     */       {
/*  53 */         Http2InboundFrameLogger.this.logger.logHeaders(Http2FrameLogger.Direction.INBOUND, streamId, headers, padding, endStream);
/*  54 */         listener.onHeadersRead(ctx, streamId, headers, padding, endStream);
/*     */       }
/*     */       
/*     */ 
/*     */       public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endStream)
/*     */         throws Http2Exception
/*     */       {
/*  61 */         Http2InboundFrameLogger.this.logger.logHeaders(Http2FrameLogger.Direction.INBOUND, streamId, headers, streamDependency, weight, exclusive, padding, endStream);
/*     */         
/*  63 */         listener.onHeadersRead(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endStream);
/*     */       }
/*     */       
/*     */ 
/*     */       public void onPriorityRead(ChannelHandlerContext ctx, int streamId, int streamDependency, short weight, boolean exclusive)
/*     */         throws Http2Exception
/*     */       {
/*  70 */         Http2InboundFrameLogger.this.logger.logPriority(Http2FrameLogger.Direction.INBOUND, streamId, streamDependency, weight, exclusive);
/*  71 */         listener.onPriorityRead(ctx, streamId, streamDependency, weight, exclusive);
/*     */       }
/*     */       
/*     */       public void onRstStreamRead(ChannelHandlerContext ctx, int streamId, long errorCode)
/*     */         throws Http2Exception
/*     */       {
/*  77 */         Http2InboundFrameLogger.this.logger.logRstStream(Http2FrameLogger.Direction.INBOUND, streamId, errorCode);
/*  78 */         listener.onRstStreamRead(ctx, streamId, errorCode);
/*     */       }
/*     */       
/*     */       public void onSettingsAckRead(ChannelHandlerContext ctx) throws Http2Exception
/*     */       {
/*  83 */         Http2InboundFrameLogger.this.logger.logSettingsAck(Http2FrameLogger.Direction.INBOUND);
/*  84 */         listener.onSettingsAckRead(ctx);
/*     */       }
/*     */       
/*     */       public void onSettingsRead(ChannelHandlerContext ctx, Http2Settings settings)
/*     */         throws Http2Exception
/*     */       {
/*  90 */         Http2InboundFrameLogger.this.logger.logSettings(Http2FrameLogger.Direction.INBOUND, settings);
/*  91 */         listener.onSettingsRead(ctx, settings);
/*     */       }
/*     */       
/*     */       public void onPingRead(ChannelHandlerContext ctx, ByteBuf data) throws Http2Exception
/*     */       {
/*  96 */         Http2InboundFrameLogger.this.logger.logPing(Http2FrameLogger.Direction.INBOUND, data);
/*  97 */         listener.onPingRead(ctx, data);
/*     */       }
/*     */       
/*     */       public void onPingAckRead(ChannelHandlerContext ctx, ByteBuf data) throws Http2Exception
/*     */       {
/* 102 */         Http2InboundFrameLogger.this.logger.logPingAck(Http2FrameLogger.Direction.INBOUND, data);
/* 103 */         listener.onPingAckRead(ctx, data);
/*     */       }
/*     */       
/*     */       public void onPushPromiseRead(ChannelHandlerContext ctx, int streamId, int promisedStreamId, Http2Headers headers, int padding)
/*     */         throws Http2Exception
/*     */       {
/* 109 */         Http2InboundFrameLogger.this.logger.logPushPromise(Http2FrameLogger.Direction.INBOUND, streamId, promisedStreamId, headers, padding);
/* 110 */         listener.onPushPromiseRead(ctx, streamId, promisedStreamId, headers, padding);
/*     */       }
/*     */       
/*     */       public void onGoAwayRead(ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData)
/*     */         throws Http2Exception
/*     */       {
/* 116 */         Http2InboundFrameLogger.this.logger.logGoAway(Http2FrameLogger.Direction.INBOUND, lastStreamId, errorCode, debugData);
/* 117 */         listener.onGoAwayRead(ctx, lastStreamId, errorCode, debugData);
/*     */       }
/*     */       
/*     */       public void onWindowUpdateRead(ChannelHandlerContext ctx, int streamId, int windowSizeIncrement)
/*     */         throws Http2Exception
/*     */       {
/* 123 */         Http2InboundFrameLogger.this.logger.logWindowsUpdate(Http2FrameLogger.Direction.INBOUND, streamId, windowSizeIncrement);
/* 124 */         listener.onWindowUpdateRead(ctx, streamId, windowSizeIncrement);
/*     */       }
/*     */       
/*     */ 
/*     */       public void onUnknownFrame(ChannelHandlerContext ctx, byte frameType, int streamId, Http2Flags flags, ByteBuf payload)
/*     */       {
/* 130 */         Http2InboundFrameLogger.this.logger.logUnknownFrame(Http2FrameLogger.Direction.INBOUND, frameType, streamId, flags, payload);
/* 131 */         listener.onUnknownFrame(ctx, frameType, streamId, flags, payload);
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   public void close()
/*     */   {
/* 138 */     this.reader.close();
/*     */   }
/*     */   
/*     */   public Http2FrameReader.Configuration configuration()
/*     */   {
/* 143 */     return this.reader.configuration();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\Http2InboundFrameLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */