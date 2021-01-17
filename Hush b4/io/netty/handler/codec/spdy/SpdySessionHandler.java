// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.spdy;

import io.netty.util.internal.EmptyArrays;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Future;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelPromise;
import java.util.Iterator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelFutureListener;
import java.util.concurrent.atomic.AtomicInteger;
import io.netty.channel.ChannelDuplexHandler;

public class SpdySessionHandler extends ChannelDuplexHandler
{
    private static final SpdyProtocolException PROTOCOL_EXCEPTION;
    private static final SpdyProtocolException STREAM_CLOSED;
    private static final int DEFAULT_WINDOW_SIZE = 65536;
    private int initialSendWindowSize;
    private int initialReceiveWindowSize;
    private volatile int initialSessionReceiveWindowSize;
    private final SpdySession spdySession;
    private int lastGoodStreamId;
    private static final int DEFAULT_MAX_CONCURRENT_STREAMS = Integer.MAX_VALUE;
    private int remoteConcurrentStreams;
    private int localConcurrentStreams;
    private final AtomicInteger pings;
    private boolean sentGoAwayFrame;
    private boolean receivedGoAwayFrame;
    private ChannelFutureListener closeSessionFutureListener;
    private final boolean server;
    private final int minorVersion;
    
    public SpdySessionHandler(final SpdyVersion version, final boolean server) {
        this.initialSendWindowSize = 65536;
        this.initialReceiveWindowSize = 65536;
        this.initialSessionReceiveWindowSize = 65536;
        this.spdySession = new SpdySession(this.initialSendWindowSize, this.initialReceiveWindowSize);
        this.remoteConcurrentStreams = Integer.MAX_VALUE;
        this.localConcurrentStreams = Integer.MAX_VALUE;
        this.pings = new AtomicInteger();
        if (version == null) {
            throw new NullPointerException("version");
        }
        this.server = server;
        this.minorVersion = version.getMinorVersion();
    }
    
    public void setSessionReceiveWindowSize(final int sessionReceiveWindowSize) {
        if (sessionReceiveWindowSize < 0) {
            throw new IllegalArgumentException("sessionReceiveWindowSize");
        }
        this.initialSessionReceiveWindowSize = sessionReceiveWindowSize;
    }
    
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        if (msg instanceof SpdyDataFrame) {
            final SpdyDataFrame spdyDataFrame = (SpdyDataFrame)msg;
            final int streamId = spdyDataFrame.streamId();
            final int deltaWindowSize = -1 * spdyDataFrame.content().readableBytes();
            final int newSessionWindowSize = this.spdySession.updateReceiveWindowSize(0, deltaWindowSize);
            if (newSessionWindowSize < 0) {
                this.issueSessionError(ctx, SpdySessionStatus.PROTOCOL_ERROR);
                return;
            }
            if (newSessionWindowSize <= this.initialSessionReceiveWindowSize / 2) {
                final int sessionDeltaWindowSize = this.initialSessionReceiveWindowSize - newSessionWindowSize;
                this.spdySession.updateReceiveWindowSize(0, sessionDeltaWindowSize);
                final SpdyWindowUpdateFrame spdyWindowUpdateFrame = new DefaultSpdyWindowUpdateFrame(0, sessionDeltaWindowSize);
                ctx.writeAndFlush(spdyWindowUpdateFrame);
            }
            if (!this.spdySession.isActiveStream(streamId)) {
                spdyDataFrame.release();
                if (streamId <= this.lastGoodStreamId) {
                    this.issueStreamError(ctx, streamId, SpdyStreamStatus.PROTOCOL_ERROR);
                }
                else if (!this.sentGoAwayFrame) {
                    this.issueStreamError(ctx, streamId, SpdyStreamStatus.INVALID_STREAM);
                }
                return;
            }
            if (this.spdySession.isRemoteSideClosed(streamId)) {
                spdyDataFrame.release();
                this.issueStreamError(ctx, streamId, SpdyStreamStatus.STREAM_ALREADY_CLOSED);
                return;
            }
            if (!this.isRemoteInitiatedId(streamId) && !this.spdySession.hasReceivedReply(streamId)) {
                spdyDataFrame.release();
                this.issueStreamError(ctx, streamId, SpdyStreamStatus.PROTOCOL_ERROR);
                return;
            }
            final int newWindowSize = this.spdySession.updateReceiveWindowSize(streamId, deltaWindowSize);
            if (newWindowSize < this.spdySession.getReceiveWindowSizeLowerBound(streamId)) {
                spdyDataFrame.release();
                this.issueStreamError(ctx, streamId, SpdyStreamStatus.FLOW_CONTROL_ERROR);
                return;
            }
            if (newWindowSize < 0) {
                while (spdyDataFrame.content().readableBytes() > this.initialReceiveWindowSize) {
                    final SpdyDataFrame partialDataFrame = new DefaultSpdyDataFrame(streamId, spdyDataFrame.content().readSlice(this.initialReceiveWindowSize).retain());
                    ctx.writeAndFlush(partialDataFrame);
                }
            }
            if (newWindowSize <= this.initialReceiveWindowSize / 2 && !spdyDataFrame.isLast()) {
                final int streamDeltaWindowSize = this.initialReceiveWindowSize - newWindowSize;
                this.spdySession.updateReceiveWindowSize(streamId, streamDeltaWindowSize);
                final SpdyWindowUpdateFrame spdyWindowUpdateFrame2 = new DefaultSpdyWindowUpdateFrame(streamId, streamDeltaWindowSize);
                ctx.writeAndFlush(spdyWindowUpdateFrame2);
            }
            if (spdyDataFrame.isLast()) {
                this.halfCloseStream(streamId, true, ctx.newSucceededFuture());
            }
        }
        else if (msg instanceof SpdySynStreamFrame) {
            final SpdySynStreamFrame spdySynStreamFrame = (SpdySynStreamFrame)msg;
            final int streamId = spdySynStreamFrame.streamId();
            if (spdySynStreamFrame.isInvalid() || !this.isRemoteInitiatedId(streamId) || this.spdySession.isActiveStream(streamId)) {
                this.issueStreamError(ctx, streamId, SpdyStreamStatus.PROTOCOL_ERROR);
                return;
            }
            if (streamId <= this.lastGoodStreamId) {
                this.issueSessionError(ctx, SpdySessionStatus.PROTOCOL_ERROR);
                return;
            }
            final byte priority = spdySynStreamFrame.priority();
            final boolean remoteSideClosed = spdySynStreamFrame.isLast();
            final boolean localSideClosed = spdySynStreamFrame.isUnidirectional();
            if (!this.acceptStream(streamId, priority, remoteSideClosed, localSideClosed)) {
                this.issueStreamError(ctx, streamId, SpdyStreamStatus.REFUSED_STREAM);
                return;
            }
        }
        else if (msg instanceof SpdySynReplyFrame) {
            final SpdySynReplyFrame spdySynReplyFrame = (SpdySynReplyFrame)msg;
            final int streamId = spdySynReplyFrame.streamId();
            if (spdySynReplyFrame.isInvalid() || this.isRemoteInitiatedId(streamId) || this.spdySession.isRemoteSideClosed(streamId)) {
                this.issueStreamError(ctx, streamId, SpdyStreamStatus.INVALID_STREAM);
                return;
            }
            if (this.spdySession.hasReceivedReply(streamId)) {
                this.issueStreamError(ctx, streamId, SpdyStreamStatus.STREAM_IN_USE);
                return;
            }
            this.spdySession.receivedReply(streamId);
            if (spdySynReplyFrame.isLast()) {
                this.halfCloseStream(streamId, true, ctx.newSucceededFuture());
            }
        }
        else if (msg instanceof SpdyRstStreamFrame) {
            final SpdyRstStreamFrame spdyRstStreamFrame = (SpdyRstStreamFrame)msg;
            this.removeStream(spdyRstStreamFrame.streamId(), ctx.newSucceededFuture());
        }
        else if (msg instanceof SpdySettingsFrame) {
            final SpdySettingsFrame spdySettingsFrame = (SpdySettingsFrame)msg;
            final int settingsMinorVersion = spdySettingsFrame.getValue(0);
            if (settingsMinorVersion >= 0 && settingsMinorVersion != this.minorVersion) {
                this.issueSessionError(ctx, SpdySessionStatus.PROTOCOL_ERROR);
                return;
            }
            final int newConcurrentStreams = spdySettingsFrame.getValue(4);
            if (newConcurrentStreams >= 0) {
                this.remoteConcurrentStreams = newConcurrentStreams;
            }
            if (spdySettingsFrame.isPersisted(7)) {
                spdySettingsFrame.removeValue(7);
            }
            spdySettingsFrame.setPersistValue(7, false);
            final int newInitialWindowSize = spdySettingsFrame.getValue(7);
            if (newInitialWindowSize >= 0) {
                this.updateInitialSendWindowSize(newInitialWindowSize);
            }
        }
        else if (msg instanceof SpdyPingFrame) {
            final SpdyPingFrame spdyPingFrame = (SpdyPingFrame)msg;
            if (this.isRemoteInitiatedId(spdyPingFrame.id())) {
                ctx.writeAndFlush(spdyPingFrame);
                return;
            }
            if (this.pings.get() == 0) {
                return;
            }
            this.pings.getAndDecrement();
        }
        else if (msg instanceof SpdyGoAwayFrame) {
            this.receivedGoAwayFrame = true;
        }
        else if (msg instanceof SpdyHeadersFrame) {
            final SpdyHeadersFrame spdyHeadersFrame = (SpdyHeadersFrame)msg;
            final int streamId = spdyHeadersFrame.streamId();
            if (spdyHeadersFrame.isInvalid()) {
                this.issueStreamError(ctx, streamId, SpdyStreamStatus.PROTOCOL_ERROR);
                return;
            }
            if (this.spdySession.isRemoteSideClosed(streamId)) {
                this.issueStreamError(ctx, streamId, SpdyStreamStatus.INVALID_STREAM);
                return;
            }
            if (spdyHeadersFrame.isLast()) {
                this.halfCloseStream(streamId, true, ctx.newSucceededFuture());
            }
        }
        else if (msg instanceof SpdyWindowUpdateFrame) {
            final SpdyWindowUpdateFrame spdyWindowUpdateFrame3 = (SpdyWindowUpdateFrame)msg;
            final int streamId = spdyWindowUpdateFrame3.streamId();
            final int deltaWindowSize = spdyWindowUpdateFrame3.deltaWindowSize();
            if (streamId != 0 && this.spdySession.isLocalSideClosed(streamId)) {
                return;
            }
            if (this.spdySession.getSendWindowSize(streamId) > Integer.MAX_VALUE - deltaWindowSize) {
                if (streamId == 0) {
                    this.issueSessionError(ctx, SpdySessionStatus.PROTOCOL_ERROR);
                }
                else {
                    this.issueStreamError(ctx, streamId, SpdyStreamStatus.FLOW_CONTROL_ERROR);
                }
                return;
            }
            this.updateSendWindowSize(ctx, streamId, deltaWindowSize);
        }
        ctx.fireChannelRead(msg);
    }
    
    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        for (final Integer streamId : this.spdySession.activeStreams().keySet()) {
            this.removeStream(streamId, ctx.newSucceededFuture());
        }
        ctx.fireChannelInactive();
    }
    
    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        if (cause instanceof SpdyProtocolException) {
            this.issueSessionError(ctx, SpdySessionStatus.PROTOCOL_ERROR);
        }
        ctx.fireExceptionCaught(cause);
    }
    
    @Override
    public void close(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        this.sendGoAwayFrame(ctx, promise);
    }
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        if (msg instanceof SpdyDataFrame || msg instanceof SpdySynStreamFrame || msg instanceof SpdySynReplyFrame || msg instanceof SpdyRstStreamFrame || msg instanceof SpdySettingsFrame || msg instanceof SpdyPingFrame || msg instanceof SpdyGoAwayFrame || msg instanceof SpdyHeadersFrame || msg instanceof SpdyWindowUpdateFrame) {
            this.handleOutboundMessage(ctx, msg, promise);
        }
        else {
            ctx.write(msg, promise);
        }
    }
    
    private void handleOutboundMessage(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        if (msg instanceof SpdyDataFrame) {
            final SpdyDataFrame spdyDataFrame = (SpdyDataFrame)msg;
            final int streamId = spdyDataFrame.streamId();
            if (this.spdySession.isLocalSideClosed(streamId)) {
                spdyDataFrame.release();
                promise.setFailure((Throwable)SpdySessionHandler.PROTOCOL_EXCEPTION);
                return;
            }
            final int dataLength = spdyDataFrame.content().readableBytes();
            int sendWindowSize = this.spdySession.getSendWindowSize(streamId);
            final int sessionSendWindowSize = this.spdySession.getSendWindowSize(0);
            sendWindowSize = Math.min(sendWindowSize, sessionSendWindowSize);
            if (sendWindowSize <= 0) {
                this.spdySession.putPendingWrite(streamId, new SpdySession.PendingWrite(spdyDataFrame, promise));
                return;
            }
            if (sendWindowSize < dataLength) {
                this.spdySession.updateSendWindowSize(streamId, -1 * sendWindowSize);
                this.spdySession.updateSendWindowSize(0, -1 * sendWindowSize);
                final SpdyDataFrame partialDataFrame = new DefaultSpdyDataFrame(streamId, spdyDataFrame.content().readSlice(sendWindowSize).retain());
                this.spdySession.putPendingWrite(streamId, new SpdySession.PendingWrite(spdyDataFrame, promise));
                final ChannelHandlerContext context = ctx;
                ctx.write(partialDataFrame).addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
                    @Override
                    public void operationComplete(final ChannelFuture future) throws Exception {
                        if (!future.isSuccess()) {
                            SpdySessionHandler.this.issueSessionError(context, SpdySessionStatus.INTERNAL_ERROR);
                        }
                    }
                });
                return;
            }
            this.spdySession.updateSendWindowSize(streamId, -1 * dataLength);
            this.spdySession.updateSendWindowSize(0, -1 * dataLength);
            final ChannelHandlerContext context2 = ctx;
            promise.addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
                @Override
                public void operationComplete(final ChannelFuture future) throws Exception {
                    if (!future.isSuccess()) {
                        SpdySessionHandler.this.issueSessionError(context2, SpdySessionStatus.INTERNAL_ERROR);
                    }
                }
            });
            if (spdyDataFrame.isLast()) {
                this.halfCloseStream(streamId, false, promise);
            }
        }
        else if (msg instanceof SpdySynStreamFrame) {
            final SpdySynStreamFrame spdySynStreamFrame = (SpdySynStreamFrame)msg;
            final int streamId = spdySynStreamFrame.streamId();
            if (this.isRemoteInitiatedId(streamId)) {
                promise.setFailure((Throwable)SpdySessionHandler.PROTOCOL_EXCEPTION);
                return;
            }
            final byte priority = spdySynStreamFrame.priority();
            final boolean remoteSideClosed = spdySynStreamFrame.isUnidirectional();
            final boolean localSideClosed = spdySynStreamFrame.isLast();
            if (!this.acceptStream(streamId, priority, remoteSideClosed, localSideClosed)) {
                promise.setFailure((Throwable)SpdySessionHandler.PROTOCOL_EXCEPTION);
                return;
            }
        }
        else if (msg instanceof SpdySynReplyFrame) {
            final SpdySynReplyFrame spdySynReplyFrame = (SpdySynReplyFrame)msg;
            final int streamId = spdySynReplyFrame.streamId();
            if (!this.isRemoteInitiatedId(streamId) || this.spdySession.isLocalSideClosed(streamId)) {
                promise.setFailure((Throwable)SpdySessionHandler.PROTOCOL_EXCEPTION);
                return;
            }
            if (spdySynReplyFrame.isLast()) {
                this.halfCloseStream(streamId, false, promise);
            }
        }
        else if (msg instanceof SpdyRstStreamFrame) {
            final SpdyRstStreamFrame spdyRstStreamFrame = (SpdyRstStreamFrame)msg;
            this.removeStream(spdyRstStreamFrame.streamId(), promise);
        }
        else if (msg instanceof SpdySettingsFrame) {
            final SpdySettingsFrame spdySettingsFrame = (SpdySettingsFrame)msg;
            final int settingsMinorVersion = spdySettingsFrame.getValue(0);
            if (settingsMinorVersion >= 0 && settingsMinorVersion != this.minorVersion) {
                promise.setFailure((Throwable)SpdySessionHandler.PROTOCOL_EXCEPTION);
                return;
            }
            final int newConcurrentStreams = spdySettingsFrame.getValue(4);
            if (newConcurrentStreams >= 0) {
                this.localConcurrentStreams = newConcurrentStreams;
            }
            if (spdySettingsFrame.isPersisted(7)) {
                spdySettingsFrame.removeValue(7);
            }
            spdySettingsFrame.setPersistValue(7, false);
            final int newInitialWindowSize = spdySettingsFrame.getValue(7);
            if (newInitialWindowSize >= 0) {
                this.updateInitialReceiveWindowSize(newInitialWindowSize);
            }
        }
        else if (msg instanceof SpdyPingFrame) {
            final SpdyPingFrame spdyPingFrame = (SpdyPingFrame)msg;
            if (this.isRemoteInitiatedId(spdyPingFrame.id())) {
                ctx.fireExceptionCaught(new IllegalArgumentException("invalid PING ID: " + spdyPingFrame.id()));
                return;
            }
            this.pings.getAndIncrement();
        }
        else {
            if (msg instanceof SpdyGoAwayFrame) {
                promise.setFailure((Throwable)SpdySessionHandler.PROTOCOL_EXCEPTION);
                return;
            }
            if (msg instanceof SpdyHeadersFrame) {
                final SpdyHeadersFrame spdyHeadersFrame = (SpdyHeadersFrame)msg;
                final int streamId = spdyHeadersFrame.streamId();
                if (this.spdySession.isLocalSideClosed(streamId)) {
                    promise.setFailure((Throwable)SpdySessionHandler.PROTOCOL_EXCEPTION);
                    return;
                }
                if (spdyHeadersFrame.isLast()) {
                    this.halfCloseStream(streamId, false, promise);
                }
            }
            else if (msg instanceof SpdyWindowUpdateFrame) {
                promise.setFailure((Throwable)SpdySessionHandler.PROTOCOL_EXCEPTION);
                return;
            }
        }
        ctx.write(msg, promise);
    }
    
    private void issueSessionError(final ChannelHandlerContext ctx, final SpdySessionStatus status) {
        this.sendGoAwayFrame(ctx, status).addListener((GenericFutureListener<? extends Future<? super Void>>)new ClosingChannelFutureListener(ctx, ctx.newPromise()));
    }
    
    private void issueStreamError(final ChannelHandlerContext ctx, final int streamId, final SpdyStreamStatus status) {
        final boolean fireChannelRead = !this.spdySession.isRemoteSideClosed(streamId);
        final ChannelPromise promise = ctx.newPromise();
        this.removeStream(streamId, promise);
        final SpdyRstStreamFrame spdyRstStreamFrame = new DefaultSpdyRstStreamFrame(streamId, status);
        ctx.writeAndFlush(spdyRstStreamFrame, promise);
        if (fireChannelRead) {
            ctx.fireChannelRead(spdyRstStreamFrame);
        }
    }
    
    private boolean isRemoteInitiatedId(final int id) {
        final boolean serverId = SpdyCodecUtil.isServerId(id);
        return (this.server && !serverId) || (!this.server && serverId);
    }
    
    private synchronized void updateInitialSendWindowSize(final int newInitialWindowSize) {
        final int deltaWindowSize = newInitialWindowSize - this.initialSendWindowSize;
        this.initialSendWindowSize = newInitialWindowSize;
        this.spdySession.updateAllSendWindowSizes(deltaWindowSize);
    }
    
    private synchronized void updateInitialReceiveWindowSize(final int newInitialWindowSize) {
        final int deltaWindowSize = newInitialWindowSize - this.initialReceiveWindowSize;
        this.initialReceiveWindowSize = newInitialWindowSize;
        this.spdySession.updateAllReceiveWindowSizes(deltaWindowSize);
    }
    
    private synchronized boolean acceptStream(final int streamId, final byte priority, final boolean remoteSideClosed, final boolean localSideClosed) {
        if (this.receivedGoAwayFrame || this.sentGoAwayFrame) {
            return false;
        }
        final boolean remote = this.isRemoteInitiatedId(streamId);
        final int maxConcurrentStreams = remote ? this.localConcurrentStreams : this.remoteConcurrentStreams;
        if (this.spdySession.numActiveStreams(remote) >= maxConcurrentStreams) {
            return false;
        }
        this.spdySession.acceptStream(streamId, priority, remoteSideClosed, localSideClosed, this.initialSendWindowSize, this.initialReceiveWindowSize, remote);
        if (remote) {
            this.lastGoodStreamId = streamId;
        }
        return true;
    }
    
    private void halfCloseStream(final int streamId, final boolean remote, final ChannelFuture future) {
        if (remote) {
            this.spdySession.closeRemoteSide(streamId, this.isRemoteInitiatedId(streamId));
        }
        else {
            this.spdySession.closeLocalSide(streamId, this.isRemoteInitiatedId(streamId));
        }
        if (this.closeSessionFutureListener != null && this.spdySession.noActiveStreams()) {
            future.addListener((GenericFutureListener<? extends Future<? super Void>>)this.closeSessionFutureListener);
        }
    }
    
    private void removeStream(final int streamId, final ChannelFuture future) {
        this.spdySession.removeStream(streamId, SpdySessionHandler.STREAM_CLOSED, this.isRemoteInitiatedId(streamId));
        if (this.closeSessionFutureListener != null && this.spdySession.noActiveStreams()) {
            future.addListener((GenericFutureListener<? extends Future<? super Void>>)this.closeSessionFutureListener);
        }
    }
    
    private void updateSendWindowSize(final ChannelHandlerContext ctx, final int streamId, final int deltaWindowSize) {
        this.spdySession.updateSendWindowSize(streamId, deltaWindowSize);
        while (true) {
            final SpdySession.PendingWrite pendingWrite = this.spdySession.getPendingWrite(streamId);
            if (pendingWrite == null) {
                return;
            }
            final SpdyDataFrame spdyDataFrame = pendingWrite.spdyDataFrame;
            final int dataFrameSize = spdyDataFrame.content().readableBytes();
            final int writeStreamId = spdyDataFrame.streamId();
            int sendWindowSize = this.spdySession.getSendWindowSize(writeStreamId);
            final int sessionSendWindowSize = this.spdySession.getSendWindowSize(0);
            sendWindowSize = Math.min(sendWindowSize, sessionSendWindowSize);
            if (sendWindowSize <= 0) {
                return;
            }
            if (sendWindowSize < dataFrameSize) {
                this.spdySession.updateSendWindowSize(writeStreamId, -1 * sendWindowSize);
                this.spdySession.updateSendWindowSize(0, -1 * sendWindowSize);
                final SpdyDataFrame partialDataFrame = new DefaultSpdyDataFrame(writeStreamId, spdyDataFrame.content().readSlice(sendWindowSize).retain());
                ctx.writeAndFlush(partialDataFrame).addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
                    @Override
                    public void operationComplete(final ChannelFuture future) throws Exception {
                        if (!future.isSuccess()) {
                            SpdySessionHandler.this.issueSessionError(ctx, SpdySessionStatus.INTERNAL_ERROR);
                        }
                    }
                });
            }
            else {
                this.spdySession.removePendingWrite(writeStreamId);
                this.spdySession.updateSendWindowSize(writeStreamId, -1 * dataFrameSize);
                this.spdySession.updateSendWindowSize(0, -1 * dataFrameSize);
                if (spdyDataFrame.isLast()) {
                    this.halfCloseStream(writeStreamId, false, pendingWrite.promise);
                }
                ctx.writeAndFlush(spdyDataFrame, pendingWrite.promise).addListener((GenericFutureListener<? extends Future<? super Void>>)new ChannelFutureListener() {
                    @Override
                    public void operationComplete(final ChannelFuture future) throws Exception {
                        if (!future.isSuccess()) {
                            SpdySessionHandler.this.issueSessionError(ctx, SpdySessionStatus.INTERNAL_ERROR);
                        }
                    }
                });
            }
        }
    }
    
    private void sendGoAwayFrame(final ChannelHandlerContext ctx, final ChannelPromise future) {
        if (!ctx.channel().isActive()) {
            ctx.close(future);
            return;
        }
        final ChannelFuture f = this.sendGoAwayFrame(ctx, SpdySessionStatus.OK);
        if (this.spdySession.noActiveStreams()) {
            f.addListener((GenericFutureListener<? extends Future<? super Void>>)new ClosingChannelFutureListener(ctx, future));
        }
        else {
            this.closeSessionFutureListener = new ClosingChannelFutureListener(ctx, future);
        }
    }
    
    private synchronized ChannelFuture sendGoAwayFrame(final ChannelHandlerContext ctx, final SpdySessionStatus status) {
        if (!this.sentGoAwayFrame) {
            this.sentGoAwayFrame = true;
            final SpdyGoAwayFrame spdyGoAwayFrame = new DefaultSpdyGoAwayFrame(this.lastGoodStreamId, status);
            return ctx.writeAndFlush(spdyGoAwayFrame);
        }
        return ctx.newSucceededFuture();
    }
    
    static {
        PROTOCOL_EXCEPTION = new SpdyProtocolException();
        STREAM_CLOSED = new SpdyProtocolException("Stream closed");
        SpdySessionHandler.PROTOCOL_EXCEPTION.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
        SpdySessionHandler.STREAM_CLOSED.setStackTrace(EmptyArrays.EMPTY_STACK_TRACE);
    }
    
    private static final class ClosingChannelFutureListener implements ChannelFutureListener
    {
        private final ChannelHandlerContext ctx;
        private final ChannelPromise promise;
        
        ClosingChannelFutureListener(final ChannelHandlerContext ctx, final ChannelPromise promise) {
            this.ctx = ctx;
            this.promise = promise;
        }
        
        @Override
        public void operationComplete(final ChannelFuture sentGoAwayFuture) throws Exception {
            this.ctx.close(this.promise);
        }
    }
}
