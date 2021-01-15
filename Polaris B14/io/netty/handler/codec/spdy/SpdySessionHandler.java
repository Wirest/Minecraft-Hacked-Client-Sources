/*     */ package io.netty.handler.codec.spdy;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandlerAdapter;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelPromise;
/*     */ import io.netty.util.internal.EmptyArrays;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
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
/*     */ public class SpdySessionHandler
/*     */   extends ChannelHandlerAdapter
/*     */ {
/*  34 */   private static final SpdyProtocolException PROTOCOL_EXCEPTION = new SpdyProtocolException();
/*  35 */   private static final SpdyProtocolException STREAM_CLOSED = new SpdyProtocolException("Stream closed");
/*     */   private static final int DEFAULT_WINDOW_SIZE = 65536;
/*     */   
/*  38 */   static { PROTOCOL_EXCEPTION.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
/*  39 */     STREAM_CLOSED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
/*     */   }
/*     */   
/*     */ 
/*  43 */   private int initialSendWindowSize = 65536;
/*  44 */   private int initialReceiveWindowSize = 65536;
/*  45 */   private volatile int initialSessionReceiveWindowSize = 65536;
/*     */   
/*  47 */   private final SpdySession spdySession = new SpdySession(this.initialSendWindowSize, this.initialReceiveWindowSize);
/*     */   
/*     */   private int lastGoodStreamId;
/*     */   private static final int DEFAULT_MAX_CONCURRENT_STREAMS = Integer.MAX_VALUE;
/*  51 */   private int remoteConcurrentStreams = Integer.MAX_VALUE;
/*  52 */   private int localConcurrentStreams = Integer.MAX_VALUE;
/*     */   
/*  54 */   private final AtomicInteger pings = new AtomicInteger();
/*     */   
/*     */ 
/*     */   private boolean sentGoAwayFrame;
/*     */   
/*     */ 
/*     */   private boolean receivedGoAwayFrame;
/*     */   
/*     */ 
/*     */   private ChannelFutureListener closeSessionFutureListener;
/*     */   
/*     */ 
/*     */   private final boolean server;
/*     */   
/*     */ 
/*     */   private final int minorVersion;
/*     */   
/*     */ 
/*     */   public SpdySessionHandler(SpdyVersion version, boolean server)
/*     */   {
/*  74 */     if (version == null) {
/*  75 */       throw new NullPointerException("version");
/*     */     }
/*  77 */     this.server = server;
/*  78 */     this.minorVersion = version.getMinorVersion();
/*     */   }
/*     */   
/*     */   public void setSessionReceiveWindowSize(int sessionReceiveWindowSize) {
/*  82 */     if (sessionReceiveWindowSize < 0) {
/*  83 */       throw new IllegalArgumentException("sessionReceiveWindowSize");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  91 */     this.initialSessionReceiveWindowSize = sessionReceiveWindowSize;
/*     */   }
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
/*     */   {
/*  96 */     if ((msg instanceof SpdyDataFrame))
/*     */     {
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
/* 120 */       SpdyDataFrame spdyDataFrame = (SpdyDataFrame)msg;
/* 121 */       int streamId = spdyDataFrame.streamId();
/*     */       
/* 123 */       int deltaWindowSize = -1 * spdyDataFrame.content().readableBytes();
/* 124 */       int newSessionWindowSize = this.spdySession.updateReceiveWindowSize(0, deltaWindowSize);
/*     */       
/*     */ 
/*     */ 
/* 128 */       if (newSessionWindowSize < 0) {
/* 129 */         issueSessionError(ctx, SpdySessionStatus.PROTOCOL_ERROR);
/* 130 */         return;
/*     */       }
/*     */       
/*     */ 
/* 134 */       if (newSessionWindowSize <= this.initialSessionReceiveWindowSize / 2) {
/* 135 */         int sessionDeltaWindowSize = this.initialSessionReceiveWindowSize - newSessionWindowSize;
/* 136 */         this.spdySession.updateReceiveWindowSize(0, sessionDeltaWindowSize);
/* 137 */         SpdyWindowUpdateFrame spdyWindowUpdateFrame = new DefaultSpdyWindowUpdateFrame(0, sessionDeltaWindowSize);
/*     */         
/* 139 */         ctx.writeAndFlush(spdyWindowUpdateFrame);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 144 */       if (!this.spdySession.isActiveStream(streamId)) {
/* 145 */         spdyDataFrame.release();
/* 146 */         if (streamId <= this.lastGoodStreamId) {
/* 147 */           issueStreamError(ctx, streamId, SpdyStreamStatus.PROTOCOL_ERROR);
/* 148 */         } else if (!this.sentGoAwayFrame) {
/* 149 */           issueStreamError(ctx, streamId, SpdyStreamStatus.INVALID_STREAM);
/*     */         }
/* 151 */         return;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 156 */       if (this.spdySession.isRemoteSideClosed(streamId)) {
/* 157 */         spdyDataFrame.release();
/* 158 */         issueStreamError(ctx, streamId, SpdyStreamStatus.STREAM_ALREADY_CLOSED);
/* 159 */         return;
/*     */       }
/*     */       
/*     */ 
/* 163 */       if ((!isRemoteInitiatedId(streamId)) && (!this.spdySession.hasReceivedReply(streamId))) {
/* 164 */         spdyDataFrame.release();
/* 165 */         issueStreamError(ctx, streamId, SpdyStreamStatus.PROTOCOL_ERROR);
/* 166 */         return;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 176 */       int newWindowSize = this.spdySession.updateReceiveWindowSize(streamId, deltaWindowSize);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 183 */       if (newWindowSize < this.spdySession.getReceiveWindowSizeLowerBound(streamId)) {
/* 184 */         spdyDataFrame.release();
/* 185 */         issueStreamError(ctx, streamId, SpdyStreamStatus.FLOW_CONTROL_ERROR);
/* 186 */         return;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 191 */       if (newWindowSize < 0) {
/* 192 */         while (spdyDataFrame.content().readableBytes() > this.initialReceiveWindowSize) {
/* 193 */           SpdyDataFrame partialDataFrame = new DefaultSpdyDataFrame(streamId, spdyDataFrame.content().readSlice(this.initialReceiveWindowSize).retain());
/*     */           
/* 195 */           ctx.writeAndFlush(partialDataFrame);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 200 */       if ((newWindowSize <= this.initialReceiveWindowSize / 2) && (!spdyDataFrame.isLast())) {
/* 201 */         int streamDeltaWindowSize = this.initialReceiveWindowSize - newWindowSize;
/* 202 */         this.spdySession.updateReceiveWindowSize(streamId, streamDeltaWindowSize);
/* 203 */         SpdyWindowUpdateFrame spdyWindowUpdateFrame = new DefaultSpdyWindowUpdateFrame(streamId, streamDeltaWindowSize);
/*     */         
/* 205 */         ctx.writeAndFlush(spdyWindowUpdateFrame);
/*     */       }
/*     */       
/*     */ 
/* 209 */       if (spdyDataFrame.isLast()) {
/* 210 */         halfCloseStream(streamId, true, ctx.newSucceededFuture());
/*     */       }
/*     */     }
/* 213 */     else if ((msg instanceof SpdySynStreamFrame))
/*     */     {
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
/* 229 */       SpdySynStreamFrame spdySynStreamFrame = (SpdySynStreamFrame)msg;
/* 230 */       int streamId = spdySynStreamFrame.streamId();
/*     */       
/*     */ 
/* 233 */       if ((spdySynStreamFrame.isInvalid()) || (!isRemoteInitiatedId(streamId)) || (this.spdySession.isActiveStream(streamId)))
/*     */       {
/*     */ 
/* 236 */         issueStreamError(ctx, streamId, SpdyStreamStatus.PROTOCOL_ERROR);
/* 237 */         return;
/*     */       }
/*     */       
/*     */ 
/* 241 */       if (streamId <= this.lastGoodStreamId) {
/* 242 */         issueSessionError(ctx, SpdySessionStatus.PROTOCOL_ERROR);
/* 243 */         return;
/*     */       }
/*     */       
/*     */ 
/* 247 */       byte priority = spdySynStreamFrame.priority();
/* 248 */       boolean remoteSideClosed = spdySynStreamFrame.isLast();
/* 249 */       boolean localSideClosed = spdySynStreamFrame.isUnidirectional();
/* 250 */       if (!acceptStream(streamId, priority, remoteSideClosed, localSideClosed)) {
/* 251 */         issueStreamError(ctx, streamId, SpdyStreamStatus.REFUSED_STREAM);
/* 252 */         return;
/*     */       }
/*     */     }
/* 255 */     else if ((msg instanceof SpdySynReplyFrame))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 264 */       SpdySynReplyFrame spdySynReplyFrame = (SpdySynReplyFrame)msg;
/* 265 */       int streamId = spdySynReplyFrame.streamId();
/*     */       
/*     */ 
/* 268 */       if ((spdySynReplyFrame.isInvalid()) || (isRemoteInitiatedId(streamId)) || (this.spdySession.isRemoteSideClosed(streamId)))
/*     */       {
/*     */ 
/* 271 */         issueStreamError(ctx, streamId, SpdyStreamStatus.INVALID_STREAM);
/* 272 */         return;
/*     */       }
/*     */       
/*     */ 
/* 276 */       if (this.spdySession.hasReceivedReply(streamId)) {
/* 277 */         issueStreamError(ctx, streamId, SpdyStreamStatus.STREAM_IN_USE);
/* 278 */         return;
/*     */       }
/*     */       
/* 281 */       this.spdySession.receivedReply(streamId);
/*     */       
/*     */ 
/* 284 */       if (spdySynReplyFrame.isLast()) {
/* 285 */         halfCloseStream(streamId, true, ctx.newSucceededFuture());
/*     */       }
/*     */     }
/* 288 */     else if ((msg instanceof SpdyRstStreamFrame))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 299 */       SpdyRstStreamFrame spdyRstStreamFrame = (SpdyRstStreamFrame)msg;
/* 300 */       removeStream(spdyRstStreamFrame.streamId(), ctx.newSucceededFuture());
/*     */     }
/* 302 */     else if ((msg instanceof SpdySettingsFrame))
/*     */     {
/* 304 */       SpdySettingsFrame spdySettingsFrame = (SpdySettingsFrame)msg;
/*     */       
/* 306 */       int settingsMinorVersion = spdySettingsFrame.getValue(0);
/* 307 */       if ((settingsMinorVersion >= 0) && (settingsMinorVersion != this.minorVersion))
/*     */       {
/* 309 */         issueSessionError(ctx, SpdySessionStatus.PROTOCOL_ERROR);
/* 310 */         return;
/*     */       }
/*     */       
/* 313 */       int newConcurrentStreams = spdySettingsFrame.getValue(4);
/*     */       
/* 315 */       if (newConcurrentStreams >= 0) {
/* 316 */         this.remoteConcurrentStreams = newConcurrentStreams;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 322 */       if (spdySettingsFrame.isPersisted(7)) {
/* 323 */         spdySettingsFrame.removeValue(7);
/*     */       }
/* 325 */       spdySettingsFrame.setPersistValue(7, false);
/*     */       
/* 327 */       int newInitialWindowSize = spdySettingsFrame.getValue(7);
/*     */       
/* 329 */       if (newInitialWindowSize >= 0) {
/* 330 */         updateInitialSendWindowSize(newInitialWindowSize);
/*     */       }
/*     */     }
/* 333 */     else if ((msg instanceof SpdyPingFrame))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 344 */       SpdyPingFrame spdyPingFrame = (SpdyPingFrame)msg;
/*     */       
/* 346 */       if (isRemoteInitiatedId(spdyPingFrame.id())) {
/* 347 */         ctx.writeAndFlush(spdyPingFrame);
/* 348 */         return;
/*     */       }
/*     */       
/*     */ 
/* 352 */       if (this.pings.get() == 0) {
/* 353 */         return;
/*     */       }
/* 355 */       this.pings.getAndDecrement();
/*     */     }
/* 357 */     else if ((msg instanceof SpdyGoAwayFrame))
/*     */     {
/* 359 */       this.receivedGoAwayFrame = true;
/*     */     }
/* 361 */     else if ((msg instanceof SpdyHeadersFrame))
/*     */     {
/* 363 */       SpdyHeadersFrame spdyHeadersFrame = (SpdyHeadersFrame)msg;
/* 364 */       int streamId = spdyHeadersFrame.streamId();
/*     */       
/*     */ 
/* 367 */       if (spdyHeadersFrame.isInvalid()) {
/* 368 */         issueStreamError(ctx, streamId, SpdyStreamStatus.PROTOCOL_ERROR);
/* 369 */         return;
/*     */       }
/*     */       
/* 372 */       if (this.spdySession.isRemoteSideClosed(streamId)) {
/* 373 */         issueStreamError(ctx, streamId, SpdyStreamStatus.INVALID_STREAM);
/* 374 */         return;
/*     */       }
/*     */       
/*     */ 
/* 378 */       if (spdyHeadersFrame.isLast()) {
/* 379 */         halfCloseStream(streamId, true, ctx.newSucceededFuture());
/*     */       }
/*     */     }
/* 382 */     else if ((msg instanceof SpdyWindowUpdateFrame))
/*     */     {
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
/* 394 */       SpdyWindowUpdateFrame spdyWindowUpdateFrame = (SpdyWindowUpdateFrame)msg;
/* 395 */       int streamId = spdyWindowUpdateFrame.streamId();
/* 396 */       int deltaWindowSize = spdyWindowUpdateFrame.deltaWindowSize();
/*     */       
/*     */ 
/* 399 */       if ((streamId != 0) && (this.spdySession.isLocalSideClosed(streamId))) {
/* 400 */         return;
/*     */       }
/*     */       
/*     */ 
/* 404 */       if (this.spdySession.getSendWindowSize(streamId) > Integer.MAX_VALUE - deltaWindowSize) {
/* 405 */         if (streamId == 0) {
/* 406 */           issueSessionError(ctx, SpdySessionStatus.PROTOCOL_ERROR);
/*     */         } else {
/* 408 */           issueStreamError(ctx, streamId, SpdyStreamStatus.FLOW_CONTROL_ERROR);
/*     */         }
/* 410 */         return;
/*     */       }
/*     */       
/* 413 */       updateSendWindowSize(ctx, streamId, deltaWindowSize);
/*     */     }
/*     */     
/* 416 */     ctx.fireChannelRead(msg);
/*     */   }
/*     */   
/*     */   public void channelInactive(ChannelHandlerContext ctx) throws Exception
/*     */   {
/* 421 */     for (Integer streamId : this.spdySession.activeStreams().keySet()) {
/* 422 */       removeStream(streamId.intValue(), ctx.newSucceededFuture());
/*     */     }
/* 424 */     ctx.fireChannelInactive();
/*     */   }
/*     */   
/*     */   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
/*     */   {
/* 429 */     if ((cause instanceof SpdyProtocolException)) {
/* 430 */       issueSessionError(ctx, SpdySessionStatus.PROTOCOL_ERROR);
/*     */     }
/*     */     
/* 433 */     ctx.fireExceptionCaught(cause);
/*     */   }
/*     */   
/*     */   public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception
/*     */   {
/* 438 */     sendGoAwayFrame(ctx, promise);
/*     */   }
/*     */   
/*     */   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception
/*     */   {
/* 443 */     if (((msg instanceof SpdyDataFrame)) || ((msg instanceof SpdySynStreamFrame)) || ((msg instanceof SpdySynReplyFrame)) || ((msg instanceof SpdyRstStreamFrame)) || ((msg instanceof SpdySettingsFrame)) || ((msg instanceof SpdyPingFrame)) || ((msg instanceof SpdyGoAwayFrame)) || ((msg instanceof SpdyHeadersFrame)) || ((msg instanceof SpdyWindowUpdateFrame)))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 453 */       handleOutboundMessage(ctx, msg, promise);
/*     */     } else {
/* 455 */       ctx.write(msg, promise);
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleOutboundMessage(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
/* 460 */     if ((msg instanceof SpdyDataFrame))
/*     */     {
/* 462 */       SpdyDataFrame spdyDataFrame = (SpdyDataFrame)msg;
/* 463 */       int streamId = spdyDataFrame.streamId();
/*     */       
/*     */ 
/* 466 */       if (this.spdySession.isLocalSideClosed(streamId)) {
/* 467 */         spdyDataFrame.release();
/* 468 */         promise.setFailure(PROTOCOL_EXCEPTION);
/* 469 */         return;
/*     */       }
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
/* 485 */       int dataLength = spdyDataFrame.content().readableBytes();
/* 486 */       int sendWindowSize = this.spdySession.getSendWindowSize(streamId);
/* 487 */       int sessionSendWindowSize = this.spdySession.getSendWindowSize(0);
/* 488 */       sendWindowSize = Math.min(sendWindowSize, sessionSendWindowSize);
/*     */       
/* 490 */       if (sendWindowSize <= 0)
/*     */       {
/* 492 */         this.spdySession.putPendingWrite(streamId, new SpdySession.PendingWrite(spdyDataFrame, promise));
/* 493 */         return; }
/* 494 */       if (sendWindowSize < dataLength)
/*     */       {
/* 496 */         this.spdySession.updateSendWindowSize(streamId, -1 * sendWindowSize);
/* 497 */         this.spdySession.updateSendWindowSize(0, -1 * sendWindowSize);
/*     */         
/*     */ 
/* 500 */         SpdyDataFrame partialDataFrame = new DefaultSpdyDataFrame(streamId, spdyDataFrame.content().readSlice(sendWindowSize).retain());
/*     */         
/*     */ 
/*     */ 
/* 504 */         this.spdySession.putPendingWrite(streamId, new SpdySession.PendingWrite(spdyDataFrame, promise));
/*     */         
/*     */ 
/*     */ 
/* 508 */         final ChannelHandlerContext context = ctx;
/* 509 */         ctx.write(partialDataFrame).addListener(new ChannelFutureListener()
/*     */         {
/*     */           public void operationComplete(ChannelFuture future) throws Exception {
/* 512 */             if (!future.isSuccess()) {
/* 513 */               SpdySessionHandler.this.issueSessionError(context, SpdySessionStatus.INTERNAL_ERROR);
/*     */             }
/*     */           }
/* 516 */         });
/* 517 */         return;
/*     */       }
/*     */       
/* 520 */       this.spdySession.updateSendWindowSize(streamId, -1 * dataLength);
/* 521 */       this.spdySession.updateSendWindowSize(0, -1 * dataLength);
/*     */       
/*     */ 
/*     */ 
/* 525 */       final ChannelHandlerContext context = ctx;
/* 526 */       promise.addListener(new ChannelFutureListener()
/*     */       {
/*     */         public void operationComplete(ChannelFuture future) throws Exception {
/* 529 */           if (!future.isSuccess()) {
/* 530 */             SpdySessionHandler.this.issueSessionError(context, SpdySessionStatus.INTERNAL_ERROR);
/*     */           }
/*     */         }
/*     */       });
/*     */       
/*     */ 
/*     */ 
/* 537 */       if (spdyDataFrame.isLast()) {
/* 538 */         halfCloseStream(streamId, false, promise);
/*     */       }
/*     */     }
/* 541 */     else if ((msg instanceof SpdySynStreamFrame))
/*     */     {
/* 543 */       SpdySynStreamFrame spdySynStreamFrame = (SpdySynStreamFrame)msg;
/* 544 */       int streamId = spdySynStreamFrame.streamId();
/*     */       
/* 546 */       if (isRemoteInitiatedId(streamId)) {
/* 547 */         promise.setFailure(PROTOCOL_EXCEPTION);
/* 548 */         return;
/*     */       }
/*     */       
/* 551 */       byte priority = spdySynStreamFrame.priority();
/* 552 */       boolean remoteSideClosed = spdySynStreamFrame.isUnidirectional();
/* 553 */       boolean localSideClosed = spdySynStreamFrame.isLast();
/* 554 */       if (!acceptStream(streamId, priority, remoteSideClosed, localSideClosed)) {
/* 555 */         promise.setFailure(PROTOCOL_EXCEPTION);
/* 556 */         return;
/*     */       }
/*     */     }
/* 559 */     else if ((msg instanceof SpdySynReplyFrame))
/*     */     {
/* 561 */       SpdySynReplyFrame spdySynReplyFrame = (SpdySynReplyFrame)msg;
/* 562 */       int streamId = spdySynReplyFrame.streamId();
/*     */       
/*     */ 
/* 565 */       if ((!isRemoteInitiatedId(streamId)) || (this.spdySession.isLocalSideClosed(streamId))) {
/* 566 */         promise.setFailure(PROTOCOL_EXCEPTION);
/* 567 */         return;
/*     */       }
/*     */       
/*     */ 
/* 571 */       if (spdySynReplyFrame.isLast()) {
/* 572 */         halfCloseStream(streamId, false, promise);
/*     */       }
/*     */     }
/* 575 */     else if ((msg instanceof SpdyRstStreamFrame))
/*     */     {
/* 577 */       SpdyRstStreamFrame spdyRstStreamFrame = (SpdyRstStreamFrame)msg;
/* 578 */       removeStream(spdyRstStreamFrame.streamId(), promise);
/*     */     }
/* 580 */     else if ((msg instanceof SpdySettingsFrame))
/*     */     {
/* 582 */       SpdySettingsFrame spdySettingsFrame = (SpdySettingsFrame)msg;
/*     */       
/* 584 */       int settingsMinorVersion = spdySettingsFrame.getValue(0);
/* 585 */       if ((settingsMinorVersion >= 0) && (settingsMinorVersion != this.minorVersion))
/*     */       {
/* 587 */         promise.setFailure(PROTOCOL_EXCEPTION);
/* 588 */         return;
/*     */       }
/*     */       
/* 591 */       int newConcurrentStreams = spdySettingsFrame.getValue(4);
/*     */       
/* 593 */       if (newConcurrentStreams >= 0) {
/* 594 */         this.localConcurrentStreams = newConcurrentStreams;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 600 */       if (spdySettingsFrame.isPersisted(7)) {
/* 601 */         spdySettingsFrame.removeValue(7);
/*     */       }
/* 603 */       spdySettingsFrame.setPersistValue(7, false);
/*     */       
/* 605 */       int newInitialWindowSize = spdySettingsFrame.getValue(7);
/*     */       
/* 607 */       if (newInitialWindowSize >= 0) {
/* 608 */         updateInitialReceiveWindowSize(newInitialWindowSize);
/*     */       }
/*     */     }
/* 611 */     else if ((msg instanceof SpdyPingFrame))
/*     */     {
/* 613 */       SpdyPingFrame spdyPingFrame = (SpdyPingFrame)msg;
/* 614 */       if (isRemoteInitiatedId(spdyPingFrame.id())) {
/* 615 */         ctx.fireExceptionCaught(new IllegalArgumentException("invalid PING ID: " + spdyPingFrame.id()));
/*     */         
/* 617 */         return;
/*     */       }
/* 619 */       this.pings.getAndIncrement();
/*     */     } else {
/* 621 */       if ((msg instanceof SpdyGoAwayFrame))
/*     */       {
/*     */ 
/*     */ 
/* 625 */         promise.setFailure(PROTOCOL_EXCEPTION);
/* 626 */         return;
/*     */       }
/* 628 */       if ((msg instanceof SpdyHeadersFrame))
/*     */       {
/* 630 */         SpdyHeadersFrame spdyHeadersFrame = (SpdyHeadersFrame)msg;
/* 631 */         int streamId = spdyHeadersFrame.streamId();
/*     */         
/*     */ 
/* 634 */         if (this.spdySession.isLocalSideClosed(streamId)) {
/* 635 */           promise.setFailure(PROTOCOL_EXCEPTION);
/* 636 */           return;
/*     */         }
/*     */         
/*     */ 
/* 640 */         if (spdyHeadersFrame.isLast()) {
/* 641 */           halfCloseStream(streamId, false, promise);
/*     */         }
/*     */       }
/* 644 */       else if ((msg instanceof SpdyWindowUpdateFrame))
/*     */       {
/*     */ 
/* 647 */         promise.setFailure(PROTOCOL_EXCEPTION);
/* 648 */         return;
/*     */       }
/*     */     }
/* 651 */     ctx.write(msg, promise);
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
/*     */   private void issueSessionError(ChannelHandlerContext ctx, SpdySessionStatus status)
/*     */   {
/* 666 */     sendGoAwayFrame(ctx, status).addListener(new ClosingChannelFutureListener(ctx, ctx.newPromise()));
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
/*     */   private void issueStreamError(ChannelHandlerContext ctx, int streamId, SpdyStreamStatus status)
/*     */   {
/* 681 */     boolean fireChannelRead = !this.spdySession.isRemoteSideClosed(streamId);
/* 682 */     ChannelPromise promise = ctx.newPromise();
/* 683 */     removeStream(streamId, promise);
/*     */     
/* 685 */     SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, status);
/* 686 */     ctx.writeAndFlush(spdyRstStreamFrame, promise);
/* 687 */     if (fireChannelRead) {
/* 688 */       ctx.fireChannelRead(spdyRstStreamFrame);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isRemoteInitiatedId(int id)
/*     */   {
/* 697 */     boolean serverId = SpdyCodecUtil.isServerId(id);
/* 698 */     return ((this.server) && (!serverId)) || ((!this.server) && (serverId));
/*     */   }
/*     */   
/*     */   private synchronized void updateInitialSendWindowSize(int newInitialWindowSize)
/*     */   {
/* 703 */     int deltaWindowSize = newInitialWindowSize - this.initialSendWindowSize;
/* 704 */     this.initialSendWindowSize = newInitialWindowSize;
/* 705 */     this.spdySession.updateAllSendWindowSizes(deltaWindowSize);
/*     */   }
/*     */   
/*     */   private synchronized void updateInitialReceiveWindowSize(int newInitialWindowSize)
/*     */   {
/* 710 */     int deltaWindowSize = newInitialWindowSize - this.initialReceiveWindowSize;
/* 711 */     this.initialReceiveWindowSize = newInitialWindowSize;
/* 712 */     this.spdySession.updateAllReceiveWindowSizes(deltaWindowSize);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private synchronized boolean acceptStream(int streamId, byte priority, boolean remoteSideClosed, boolean localSideClosed)
/*     */   {
/* 719 */     if ((this.receivedGoAwayFrame) || (this.sentGoAwayFrame)) {
/* 720 */       return false;
/*     */     }
/*     */     
/* 723 */     boolean remote = isRemoteInitiatedId(streamId);
/* 724 */     int maxConcurrentStreams = remote ? this.localConcurrentStreams : this.remoteConcurrentStreams;
/* 725 */     if (this.spdySession.numActiveStreams(remote) >= maxConcurrentStreams) {
/* 726 */       return false;
/*     */     }
/* 728 */     this.spdySession.acceptStream(streamId, priority, remoteSideClosed, localSideClosed, this.initialSendWindowSize, this.initialReceiveWindowSize, remote);
/*     */     
/*     */ 
/* 731 */     if (remote) {
/* 732 */       this.lastGoodStreamId = streamId;
/*     */     }
/* 734 */     return true;
/*     */   }
/*     */   
/*     */   private void halfCloseStream(int streamId, boolean remote, ChannelFuture future) {
/* 738 */     if (remote) {
/* 739 */       this.spdySession.closeRemoteSide(streamId, isRemoteInitiatedId(streamId));
/*     */     } else {
/* 741 */       this.spdySession.closeLocalSide(streamId, isRemoteInitiatedId(streamId));
/*     */     }
/* 743 */     if ((this.closeSessionFutureListener != null) && (this.spdySession.noActiveStreams())) {
/* 744 */       future.addListener(this.closeSessionFutureListener);
/*     */     }
/*     */   }
/*     */   
/*     */   private void removeStream(int streamId, ChannelFuture future) {
/* 749 */     this.spdySession.removeStream(streamId, STREAM_CLOSED, isRemoteInitiatedId(streamId));
/*     */     
/* 751 */     if ((this.closeSessionFutureListener != null) && (this.spdySession.noActiveStreams())) {
/* 752 */       future.addListener(this.closeSessionFutureListener);
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateSendWindowSize(final ChannelHandlerContext ctx, int streamId, int deltaWindowSize) {
/* 757 */     this.spdySession.updateSendWindowSize(streamId, deltaWindowSize);
/*     */     
/*     */     for (;;)
/*     */     {
/* 761 */       SpdySession.PendingWrite pendingWrite = this.spdySession.getPendingWrite(streamId);
/* 762 */       if (pendingWrite == null) {
/* 763 */         return;
/*     */       }
/*     */       
/* 766 */       SpdyDataFrame spdyDataFrame = pendingWrite.spdyDataFrame;
/* 767 */       int dataFrameSize = spdyDataFrame.content().readableBytes();
/* 768 */       int writeStreamId = spdyDataFrame.streamId();
/* 769 */       int sendWindowSize = this.spdySession.getSendWindowSize(writeStreamId);
/* 770 */       int sessionSendWindowSize = this.spdySession.getSendWindowSize(0);
/* 771 */       sendWindowSize = Math.min(sendWindowSize, sessionSendWindowSize);
/*     */       
/* 773 */       if (sendWindowSize <= 0)
/* 774 */         return;
/* 775 */       if (sendWindowSize < dataFrameSize)
/*     */       {
/* 777 */         this.spdySession.updateSendWindowSize(writeStreamId, -1 * sendWindowSize);
/* 778 */         this.spdySession.updateSendWindowSize(0, -1 * sendWindowSize);
/*     */         
/*     */ 
/* 781 */         SpdyDataFrame partialDataFrame = new DefaultSpdyDataFrame(writeStreamId, spdyDataFrame.content().readSlice(sendWindowSize).retain());
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 786 */         ctx.writeAndFlush(partialDataFrame).addListener(new ChannelFutureListener()
/*     */         {
/*     */           public void operationComplete(ChannelFuture future) throws Exception {
/* 789 */             if (!future.isSuccess()) {
/* 790 */               SpdySessionHandler.this.issueSessionError(ctx, SpdySessionStatus.INTERNAL_ERROR);
/*     */             }
/*     */           }
/*     */         });
/*     */       }
/*     */       else {
/* 796 */         this.spdySession.removePendingWrite(writeStreamId);
/* 797 */         this.spdySession.updateSendWindowSize(writeStreamId, -1 * dataFrameSize);
/* 798 */         this.spdySession.updateSendWindowSize(0, -1 * dataFrameSize);
/*     */         
/*     */ 
/* 801 */         if (spdyDataFrame.isLast()) {
/* 802 */           halfCloseStream(writeStreamId, false, pendingWrite.promise);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 807 */         ctx.writeAndFlush(spdyDataFrame, pendingWrite.promise).addListener(new ChannelFutureListener()
/*     */         {
/*     */           public void operationComplete(ChannelFuture future) throws Exception {
/* 810 */             if (!future.isSuccess()) {
/* 811 */               SpdySessionHandler.this.issueSessionError(ctx, SpdySessionStatus.INTERNAL_ERROR);
/*     */             }
/*     */           }
/*     */         });
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void sendGoAwayFrame(ChannelHandlerContext ctx, ChannelPromise future)
/*     */   {
/* 821 */     if (!ctx.channel().isActive()) {
/* 822 */       ctx.close(future);
/* 823 */       return;
/*     */     }
/*     */     
/* 826 */     ChannelFuture f = sendGoAwayFrame(ctx, SpdySessionStatus.OK);
/* 827 */     if (this.spdySession.noActiveStreams()) {
/* 828 */       f.addListener(new ClosingChannelFutureListener(ctx, future));
/*     */     } else {
/* 830 */       this.closeSessionFutureListener = new ClosingChannelFutureListener(ctx, future);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private synchronized ChannelFuture sendGoAwayFrame(ChannelHandlerContext ctx, SpdySessionStatus status)
/*     */   {
/* 837 */     if (!this.sentGoAwayFrame) {
/* 838 */       this.sentGoAwayFrame = true;
/* 839 */       SpdyGoAwayFrame spdyGoAwayFrame = new DefaultSpdyGoAwayFrame(this.lastGoodStreamId, status);
/* 840 */       return ctx.writeAndFlush(spdyGoAwayFrame);
/*     */     }
/* 842 */     return ctx.newSucceededFuture();
/*     */   }
/*     */   
/*     */   private static final class ClosingChannelFutureListener implements ChannelFutureListener
/*     */   {
/*     */     private final ChannelHandlerContext ctx;
/*     */     private final ChannelPromise promise;
/*     */     
/*     */     ClosingChannelFutureListener(ChannelHandlerContext ctx, ChannelPromise promise) {
/* 851 */       this.ctx = ctx;
/* 852 */       this.promise = promise;
/*     */     }
/*     */     
/*     */     public void operationComplete(ChannelFuture sentGoAwayFuture) throws Exception
/*     */     {
/* 857 */       this.ctx.close(this.promise);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\SpdySessionHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */