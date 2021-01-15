/*     */ package io.netty.handler.codec.http2;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.util.internal.ObjectUtil;
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
/*     */ public class DefaultHttp2ConnectionDecoder
/*     */   implements Http2ConnectionDecoder
/*     */ {
/*  39 */   private final Http2FrameListener internalFrameListener = new FrameReadListener(null);
/*     */   
/*     */   private final Http2Connection connection;
/*     */   private final Http2LifecycleManager lifecycleManager;
/*     */   private final Http2ConnectionEncoder encoder;
/*     */   private final Http2FrameReader frameReader;
/*     */   private final Http2FrameListener listener;
/*     */   private boolean prefaceReceived;
/*     */   
/*     */   public static class Builder
/*     */     implements Http2ConnectionDecoder.Builder
/*     */   {
/*     */     private Http2Connection connection;
/*     */     private Http2LifecycleManager lifecycleManager;
/*     */     private Http2ConnectionEncoder encoder;
/*     */     private Http2FrameReader frameReader;
/*     */     private Http2FrameListener listener;
/*     */     
/*     */     public Builder connection(Http2Connection connection)
/*     */     {
/*  59 */       this.connection = connection;
/*  60 */       return this;
/*     */     }
/*     */     
/*     */     public Builder lifecycleManager(Http2LifecycleManager lifecycleManager)
/*     */     {
/*  65 */       this.lifecycleManager = lifecycleManager;
/*  66 */       return this;
/*     */     }
/*     */     
/*     */     public Http2LifecycleManager lifecycleManager()
/*     */     {
/*  71 */       return this.lifecycleManager;
/*     */     }
/*     */     
/*     */     public Builder frameReader(Http2FrameReader frameReader)
/*     */     {
/*  76 */       this.frameReader = frameReader;
/*  77 */       return this;
/*     */     }
/*     */     
/*     */     public Builder listener(Http2FrameListener listener)
/*     */     {
/*  82 */       this.listener = listener;
/*  83 */       return this;
/*     */     }
/*     */     
/*     */     public Builder encoder(Http2ConnectionEncoder encoder)
/*     */     {
/*  88 */       this.encoder = encoder;
/*  89 */       return this;
/*     */     }
/*     */     
/*     */     public Http2ConnectionDecoder build()
/*     */     {
/*  94 */       return new DefaultHttp2ConnectionDecoder(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Builder newBuilder() {
/*  99 */     return new Builder();
/*     */   }
/*     */   
/*     */   protected DefaultHttp2ConnectionDecoder(Builder builder) {
/* 103 */     this.connection = ((Http2Connection)ObjectUtil.checkNotNull(builder.connection, "connection"));
/* 104 */     this.frameReader = ((Http2FrameReader)ObjectUtil.checkNotNull(builder.frameReader, "frameReader"));
/* 105 */     this.lifecycleManager = ((Http2LifecycleManager)ObjectUtil.checkNotNull(builder.lifecycleManager, "lifecycleManager"));
/* 106 */     this.encoder = ((Http2ConnectionEncoder)ObjectUtil.checkNotNull(builder.encoder, "encoder"));
/* 107 */     this.listener = ((Http2FrameListener)ObjectUtil.checkNotNull(builder.listener, "listener"));
/* 108 */     if (this.connection.local().flowController() == null) {
/* 109 */       this.connection.local().flowController(new DefaultHttp2LocalFlowController(this.connection, this.encoder.frameWriter()));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public Http2Connection connection()
/*     */   {
/* 116 */     return this.connection;
/*     */   }
/*     */   
/*     */   public final Http2LocalFlowController flowController()
/*     */   {
/* 121 */     return (Http2LocalFlowController)this.connection.local().flowController();
/*     */   }
/*     */   
/*     */   public Http2FrameListener listener()
/*     */   {
/* 126 */     return this.listener;
/*     */   }
/*     */   
/*     */   public boolean prefaceReceived()
/*     */   {
/* 131 */     return this.prefaceReceived;
/*     */   }
/*     */   
/*     */   public void decodeFrame(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Http2Exception
/*     */   {
/* 136 */     this.frameReader.readFrame(ctx, in, this.internalFrameListener);
/*     */   }
/*     */   
/*     */   public Http2Settings localSettings()
/*     */   {
/* 141 */     Http2Settings settings = new Http2Settings();
/* 142 */     Http2FrameReader.Configuration config = this.frameReader.configuration();
/* 143 */     Http2HeaderTable headerTable = config.headerTable();
/* 144 */     Http2FrameSizePolicy frameSizePolicy = config.frameSizePolicy();
/* 145 */     settings.initialWindowSize(flowController().initialWindowSize());
/* 146 */     settings.maxConcurrentStreams(this.connection.remote().maxStreams());
/* 147 */     settings.headerTableSize(headerTable.maxHeaderTableSize());
/* 148 */     settings.maxFrameSize(frameSizePolicy.maxFrameSize());
/* 149 */     settings.maxHeaderListSize(headerTable.maxHeaderListSize());
/* 150 */     if (!this.connection.isServer())
/*     */     {
/* 152 */       settings.pushEnabled(this.connection.local().allowPushTo());
/*     */     }
/* 154 */     return settings;
/*     */   }
/*     */   
/*     */   public void localSettings(Http2Settings settings) throws Http2Exception
/*     */   {
/* 159 */     Boolean pushEnabled = settings.pushEnabled();
/* 160 */     Http2FrameReader.Configuration config = this.frameReader.configuration();
/* 161 */     Http2HeaderTable inboundHeaderTable = config.headerTable();
/* 162 */     Http2FrameSizePolicy inboundFrameSizePolicy = config.frameSizePolicy();
/* 163 */     if (pushEnabled != null) {
/* 164 */       if (this.connection.isServer()) {
/* 165 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Server sending SETTINGS frame with ENABLE_PUSH specified", new Object[0]);
/*     */       }
/* 167 */       this.connection.local().allowPushTo(pushEnabled.booleanValue());
/*     */     }
/*     */     
/* 170 */     Long maxConcurrentStreams = settings.maxConcurrentStreams();
/* 171 */     if (maxConcurrentStreams != null) {
/* 172 */       int value = (int)Math.min(maxConcurrentStreams.longValue(), 2147483647L);
/* 173 */       this.connection.remote().maxStreams(value);
/*     */     }
/*     */     
/* 176 */     Long headerTableSize = settings.headerTableSize();
/* 177 */     if (headerTableSize != null) {
/* 178 */       inboundHeaderTable.maxHeaderTableSize((int)Math.min(headerTableSize.longValue(), 2147483647L));
/*     */     }
/*     */     
/* 181 */     Integer maxHeaderListSize = settings.maxHeaderListSize();
/* 182 */     if (maxHeaderListSize != null) {
/* 183 */       inboundHeaderTable.maxHeaderListSize(maxHeaderListSize.intValue());
/*     */     }
/*     */     
/* 186 */     Integer maxFrameSize = settings.maxFrameSize();
/* 187 */     if (maxFrameSize != null) {
/* 188 */       inboundFrameSizePolicy.maxFrameSize(maxFrameSize.intValue());
/*     */     }
/*     */     
/* 191 */     Integer initialWindowSize = settings.initialWindowSize();
/* 192 */     if (initialWindowSize != null) {
/* 193 */       flowController().initialWindowSize(initialWindowSize.intValue());
/*     */     }
/*     */   }
/*     */   
/*     */   public void close()
/*     */   {
/* 199 */     this.frameReader.close();
/*     */   }
/*     */   
/*     */   private int unconsumedBytes(Http2Stream stream) {
/* 203 */     return flowController().unconsumedBytes(stream);
/*     */   }
/*     */   
/*     */   private final class FrameReadListener
/*     */     implements Http2FrameListener
/*     */   {
/*     */     private FrameReadListener() {}
/*     */     
/*     */     public int onDataRead(ChannelHandlerContext ctx, int streamId, ByteBuf data, int padding, boolean endOfStream)
/*     */       throws Http2Exception
/*     */     {
/* 214 */       verifyPrefaceReceived();
/*     */       
/*     */ 
/* 217 */       Http2Stream stream = DefaultHttp2ConnectionDecoder.this.connection.requireStream(streamId);
/*     */       
/* 219 */       verifyGoAwayNotReceived();
/*     */       
/*     */ 
/*     */ 
/* 223 */       boolean shouldIgnore = shouldIgnoreFrame(stream, false);
/* 224 */       Http2Exception error = null;
/* 225 */       switch (DefaultHttp2ConnectionDecoder.1.$SwitchMap$io$netty$handler$codec$http2$Http2Stream$State[stream.state().ordinal()])
/*     */       {
/*     */       case 1: 
/*     */       case 2: 
/*     */         break;
/*     */       case 3: 
/* 231 */         error = Http2Exception.streamError(stream.id(), Http2Error.STREAM_CLOSED, "Stream %d in unexpected state: %s", new Object[] { Integer.valueOf(stream.id()), stream.state() });
/*     */         
/* 233 */         break;
/*     */       case 4: 
/* 235 */         if (!shouldIgnore) {
/* 236 */           error = Http2Exception.streamError(stream.id(), Http2Error.STREAM_CLOSED, "Stream %d in unexpected state: %s", new Object[] { Integer.valueOf(stream.id()), stream.state() });
/*     */         }
/*     */         
/*     */         break;
/*     */       default: 
/* 241 */         if (!shouldIgnore) {
/* 242 */           error = Http2Exception.streamError(stream.id(), Http2Error.PROTOCOL_ERROR, "Stream %d in unexpected state: %s", new Object[] { Integer.valueOf(stream.id()), stream.state() });
/*     */         }
/*     */         
/*     */         break;
/*     */       }
/*     */       
/* 248 */       int bytesToReturn = data.readableBytes() + padding;
/* 249 */       int unconsumedBytes = DefaultHttp2ConnectionDecoder.this.unconsumedBytes(stream);
/* 250 */       Http2LocalFlowController flowController = DefaultHttp2ConnectionDecoder.this.flowController();
/*     */       try
/*     */       {
/* 253 */         flowController.receiveFlowControlledFrame(ctx, stream, data, padding, endOfStream);
/*     */         
/* 255 */         unconsumedBytes = DefaultHttp2ConnectionDecoder.this.unconsumedBytes(stream);
/*     */         
/*     */         int i;
/* 258 */         if (shouldIgnore) {
/* 259 */           return bytesToReturn;
/*     */         }
/*     */         
/*     */ 
/* 263 */         if (error != null) {
/* 264 */           throw error;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 269 */         bytesToReturn = DefaultHttp2ConnectionDecoder.this.listener.onDataRead(ctx, streamId, data, padding, endOfStream);
/* 270 */         return bytesToReturn;
/*     */ 
/*     */       }
/*     */       catch (Http2Exception e)
/*     */       {
/* 275 */         int delta = unconsumedBytes - DefaultHttp2ConnectionDecoder.this.unconsumedBytes(stream);
/* 276 */         bytesToReturn -= delta;
/* 277 */         throw e;
/*     */ 
/*     */       }
/*     */       catch (RuntimeException e)
/*     */       {
/* 282 */         int delta = unconsumedBytes - DefaultHttp2ConnectionDecoder.this.unconsumedBytes(stream);
/* 283 */         bytesToReturn -= delta;
/* 284 */         throw e;
/*     */       }
/*     */       finally {
/* 287 */         if (bytesToReturn > 0) {
/* 288 */           flowController.consumeBytes(ctx, stream, bytesToReturn);
/*     */         }
/*     */         
/* 291 */         if (endOfStream) {
/* 292 */           DefaultHttp2ConnectionDecoder.this.lifecycleManager.closeRemoteSide(stream, ctx.newSucceededFuture());
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     private void verifyPrefaceReceived()
/*     */       throws Http2Exception
/*     */     {
/* 301 */       if (!DefaultHttp2ConnectionDecoder.this.prefaceReceived) {
/* 302 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Received non-SETTINGS as first frame.", new Object[0]);
/*     */       }
/*     */     }
/*     */     
/*     */     public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int padding, boolean endOfStream)
/*     */       throws Http2Exception
/*     */     {
/* 309 */       onHeadersRead(ctx, streamId, headers, 0, (short)16, false, padding, endOfStream);
/*     */     }
/*     */     
/*     */     public void onHeadersRead(ChannelHandlerContext ctx, int streamId, Http2Headers headers, int streamDependency, short weight, boolean exclusive, int padding, boolean endOfStream)
/*     */       throws Http2Exception
/*     */     {
/* 315 */       verifyPrefaceReceived();
/*     */       
/* 317 */       Http2Stream stream = DefaultHttp2ConnectionDecoder.this.connection.stream(streamId);
/* 318 */       verifyGoAwayNotReceived();
/* 319 */       if (shouldIgnoreFrame(stream, false))
/*     */       {
/* 321 */         return;
/*     */       }
/*     */       
/* 324 */       if (stream == null) {
/* 325 */         stream = DefaultHttp2ConnectionDecoder.this.connection.createRemoteStream(streamId).open(endOfStream);
/*     */       } else {
/* 327 */         switch (DefaultHttp2ConnectionDecoder.1.$SwitchMap$io$netty$handler$codec$http2$Http2Stream$State[stream.state().ordinal()]) {
/*     */         case 5: 
/*     */         case 6: 
/* 330 */           stream.open(endOfStream);
/* 331 */           break;
/*     */         
/*     */         case 1: 
/*     */         case 2: 
/*     */           break;
/*     */         
/*     */         case 3: 
/*     */         case 4: 
/* 339 */           throw Http2Exception.streamError(stream.id(), Http2Error.STREAM_CLOSED, "Stream %d in unexpected state: %s", new Object[] { Integer.valueOf(stream.id()), stream.state() });
/*     */         
/*     */ 
/*     */         default: 
/* 343 */           throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Stream %d in unexpected state: %s", new Object[] { Integer.valueOf(stream.id()), stream.state() });
/*     */         }
/*     */         
/*     */       }
/*     */       
/* 348 */       DefaultHttp2ConnectionDecoder.this.listener.onHeadersRead(ctx, streamId, headers, streamDependency, weight, exclusive, padding, endOfStream);
/*     */       
/*     */ 
/* 351 */       stream.setPriority(streamDependency, weight, exclusive);
/*     */       
/*     */ 
/* 354 */       if (endOfStream) {
/* 355 */         DefaultHttp2ConnectionDecoder.this.lifecycleManager.closeRemoteSide(stream, ctx.newSucceededFuture());
/*     */       }
/*     */     }
/*     */     
/*     */     public void onPriorityRead(ChannelHandlerContext ctx, int streamId, int streamDependency, short weight, boolean exclusive)
/*     */       throws Http2Exception
/*     */     {
/* 362 */       verifyPrefaceReceived();
/*     */       
/* 364 */       Http2Stream stream = DefaultHttp2ConnectionDecoder.this.connection.stream(streamId);
/* 365 */       verifyGoAwayNotReceived();
/* 366 */       if (shouldIgnoreFrame(stream, true))
/*     */       {
/* 368 */         return;
/*     */       }
/*     */       
/* 371 */       if (stream == null)
/*     */       {
/*     */ 
/* 374 */         stream = DefaultHttp2ConnectionDecoder.this.connection.createRemoteStream(streamId);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 379 */       stream.setPriority(streamDependency, weight, exclusive);
/*     */       
/* 381 */       DefaultHttp2ConnectionDecoder.this.listener.onPriorityRead(ctx, streamId, streamDependency, weight, exclusive);
/*     */     }
/*     */     
/*     */     public void onRstStreamRead(ChannelHandlerContext ctx, int streamId, long errorCode) throws Http2Exception
/*     */     {
/* 386 */       verifyPrefaceReceived();
/*     */       
/* 388 */       Http2Stream stream = DefaultHttp2ConnectionDecoder.this.connection.requireStream(streamId);
/* 389 */       if (stream.state() == Http2Stream.State.CLOSED)
/*     */       {
/* 391 */         return;
/*     */       }
/*     */       
/* 394 */       DefaultHttp2ConnectionDecoder.this.listener.onRstStreamRead(ctx, streamId, errorCode);
/*     */       
/* 396 */       DefaultHttp2ConnectionDecoder.this.lifecycleManager.closeStream(stream, ctx.newSucceededFuture());
/*     */     }
/*     */     
/*     */     public void onSettingsAckRead(ChannelHandlerContext ctx) throws Http2Exception
/*     */     {
/* 401 */       verifyPrefaceReceived();
/*     */       
/*     */ 
/* 404 */       Http2Settings settings = DefaultHttp2ConnectionDecoder.this.encoder.pollSentSettings();
/*     */       
/* 406 */       if (settings != null) {
/* 407 */         applyLocalSettings(settings);
/*     */       }
/*     */       
/* 410 */       DefaultHttp2ConnectionDecoder.this.listener.onSettingsAckRead(ctx);
/*     */     }
/*     */     
/*     */ 
/*     */     private void applyLocalSettings(Http2Settings settings)
/*     */       throws Http2Exception
/*     */     {
/* 417 */       Boolean pushEnabled = settings.pushEnabled();
/* 418 */       Http2FrameReader.Configuration config = DefaultHttp2ConnectionDecoder.this.frameReader.configuration();
/* 419 */       Http2HeaderTable headerTable = config.headerTable();
/* 420 */       Http2FrameSizePolicy frameSizePolicy = config.frameSizePolicy();
/* 421 */       if (pushEnabled != null) {
/* 422 */         if (DefaultHttp2ConnectionDecoder.this.connection.isServer()) {
/* 423 */           throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Server sending SETTINGS frame with ENABLE_PUSH specified", new Object[0]);
/*     */         }
/* 425 */         DefaultHttp2ConnectionDecoder.this.connection.local().allowPushTo(pushEnabled.booleanValue());
/*     */       }
/*     */       
/* 428 */       Long maxConcurrentStreams = settings.maxConcurrentStreams();
/* 429 */       if (maxConcurrentStreams != null) {
/* 430 */         int value = (int)Math.min(maxConcurrentStreams.longValue(), 2147483647L);
/* 431 */         DefaultHttp2ConnectionDecoder.this.connection.remote().maxStreams(value);
/*     */       }
/*     */       
/* 434 */       Long headerTableSize = settings.headerTableSize();
/* 435 */       if (headerTableSize != null) {
/* 436 */         headerTable.maxHeaderTableSize((int)Math.min(headerTableSize.longValue(), 2147483647L));
/*     */       }
/*     */       
/* 439 */       Integer maxHeaderListSize = settings.maxHeaderListSize();
/* 440 */       if (maxHeaderListSize != null) {
/* 441 */         headerTable.maxHeaderListSize(maxHeaderListSize.intValue());
/*     */       }
/*     */       
/* 444 */       Integer maxFrameSize = settings.maxFrameSize();
/* 445 */       if (maxFrameSize != null) {
/* 446 */         frameSizePolicy.maxFrameSize(maxFrameSize.intValue());
/*     */       }
/*     */       
/* 449 */       Integer initialWindowSize = settings.initialWindowSize();
/* 450 */       if (initialWindowSize != null) {
/* 451 */         DefaultHttp2ConnectionDecoder.this.flowController().initialWindowSize(initialWindowSize.intValue());
/*     */       }
/*     */     }
/*     */     
/*     */     public void onSettingsRead(ChannelHandlerContext ctx, Http2Settings settings) throws Http2Exception
/*     */     {
/* 457 */       DefaultHttp2ConnectionDecoder.this.encoder.remoteSettings(settings);
/*     */       
/*     */ 
/* 460 */       DefaultHttp2ConnectionDecoder.this.encoder.writeSettingsAck(ctx, ctx.newPromise());
/*     */       
/*     */ 
/* 463 */       DefaultHttp2ConnectionDecoder.this.prefaceReceived = true;
/*     */       
/* 465 */       DefaultHttp2ConnectionDecoder.this.listener.onSettingsRead(ctx, settings);
/*     */     }
/*     */     
/*     */     public void onPingRead(ChannelHandlerContext ctx, ByteBuf data) throws Http2Exception
/*     */     {
/* 470 */       verifyPrefaceReceived();
/*     */       
/*     */ 
/*     */ 
/* 474 */       DefaultHttp2ConnectionDecoder.this.encoder.writePing(ctx, true, data.retain(), ctx.newPromise());
/* 475 */       ctx.flush();
/*     */       
/* 477 */       DefaultHttp2ConnectionDecoder.this.listener.onPingRead(ctx, data);
/*     */     }
/*     */     
/*     */     public void onPingAckRead(ChannelHandlerContext ctx, ByteBuf data) throws Http2Exception
/*     */     {
/* 482 */       verifyPrefaceReceived();
/*     */       
/* 484 */       DefaultHttp2ConnectionDecoder.this.listener.onPingAckRead(ctx, data);
/*     */     }
/*     */     
/*     */     public void onPushPromiseRead(ChannelHandlerContext ctx, int streamId, int promisedStreamId, Http2Headers headers, int padding)
/*     */       throws Http2Exception
/*     */     {
/* 490 */       verifyPrefaceReceived();
/*     */       
/* 492 */       Http2Stream parentStream = DefaultHttp2ConnectionDecoder.this.connection.requireStream(streamId);
/* 493 */       verifyGoAwayNotReceived();
/* 494 */       if (shouldIgnoreFrame(parentStream, false))
/*     */       {
/* 496 */         return;
/*     */       }
/*     */       
/* 499 */       switch (DefaultHttp2ConnectionDecoder.1.$SwitchMap$io$netty$handler$codec$http2$Http2Stream$State[parentStream.state().ordinal()])
/*     */       {
/*     */       case 1: 
/*     */       case 2: 
/*     */         break;
/*     */       
/*     */       default: 
/* 506 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Stream %d in unexpected state for receiving push promise: %s", new Object[] { Integer.valueOf(parentStream.id()), parentStream.state() });
/*     */       }
/*     */       
/*     */       
/*     */ 
/*     */ 
/* 512 */       DefaultHttp2ConnectionDecoder.this.connection.remote().reservePushStream(promisedStreamId, parentStream);
/*     */       
/* 514 */       DefaultHttp2ConnectionDecoder.this.listener.onPushPromiseRead(ctx, streamId, promisedStreamId, headers, padding);
/*     */     }
/*     */     
/*     */ 
/*     */     public void onGoAwayRead(ChannelHandlerContext ctx, int lastStreamId, long errorCode, ByteBuf debugData)
/*     */       throws Http2Exception
/*     */     {
/* 521 */       DefaultHttp2ConnectionDecoder.this.connection.goAwayReceived(lastStreamId);
/*     */       
/* 523 */       DefaultHttp2ConnectionDecoder.this.listener.onGoAwayRead(ctx, lastStreamId, errorCode, debugData);
/*     */     }
/*     */     
/*     */     public void onWindowUpdateRead(ChannelHandlerContext ctx, int streamId, int windowSizeIncrement)
/*     */       throws Http2Exception
/*     */     {
/* 529 */       verifyPrefaceReceived();
/*     */       
/* 531 */       Http2Stream stream = DefaultHttp2ConnectionDecoder.this.connection.requireStream(streamId);
/* 532 */       verifyGoAwayNotReceived();
/* 533 */       if ((stream.state() == Http2Stream.State.CLOSED) || (shouldIgnoreFrame(stream, false)))
/*     */       {
/* 535 */         return;
/*     */       }
/*     */       
/*     */ 
/* 539 */       DefaultHttp2ConnectionDecoder.this.encoder.flowController().incrementWindowSize(ctx, stream, windowSizeIncrement);
/*     */       
/* 541 */       DefaultHttp2ConnectionDecoder.this.listener.onWindowUpdateRead(ctx, streamId, windowSizeIncrement);
/*     */     }
/*     */     
/*     */ 
/*     */     public void onUnknownFrame(ChannelHandlerContext ctx, byte frameType, int streamId, Http2Flags flags, ByteBuf payload)
/*     */     {
/* 547 */       DefaultHttp2ConnectionDecoder.this.listener.onUnknownFrame(ctx, frameType, streamId, flags, payload);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private boolean shouldIgnoreFrame(Http2Stream stream, boolean allowResetSent)
/*     */     {
/* 555 */       if ((DefaultHttp2ConnectionDecoder.this.connection.goAwaySent()) && ((stream == null) || (DefaultHttp2ConnectionDecoder.this.connection.remote().lastStreamCreated() <= stream.id())))
/*     */       {
/*     */ 
/*     */ 
/* 559 */         return true;
/*     */       }
/*     */       
/*     */ 
/* 563 */       return (stream != null) && (!allowResetSent) && (stream.isResetSent());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private void verifyGoAwayNotReceived()
/*     */       throws Http2Exception
/*     */     {
/* 571 */       if (DefaultHttp2ConnectionDecoder.this.connection.goAwayReceived())
/*     */       {
/* 573 */         throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Received frames after receiving GO_AWAY", new Object[0]);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\DefaultHttp2ConnectionDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */