package io.netty.channel.local;

import io.netty.channel.*;
import io.netty.channel.AbstractChannel.AbstractUnsafe;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import io.netty.util.internal.InternalThreadLocalMap;

import java.net.SocketAddress;
import java.nio.channels.AlreadyConnectedException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ConnectionPendingException;
import java.nio.channels.NotYetConnectedException;
import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Queue;

public class LocalChannel
        extends AbstractChannel {
    private static final ChannelMetadata METADATA = new ChannelMetadata(false);
    private static final int MAX_READER_STACK_DEPTH = 8;
    private final ChannelConfig config = new DefaultChannelConfig(this);
    private final Queue<Object> inboundBuffer = new ArrayDeque();
    private final Runnable readTask = new Runnable() {
        public void run() {
            ChannelPipeline localChannelPipeline = LocalChannel.this.pipeline();
            for (; ; ) {
                Object localObject = LocalChannel.this.inboundBuffer.poll();
                if (localObject == null) {
                    break;
                }
                localChannelPipeline.fireChannelRead(localObject);
            }
            localChannelPipeline.fireChannelReadComplete();
        }
    };
    private final Runnable shutdownHook = new Runnable() {
        public void run() {
            LocalChannel.this.unsafe().close(LocalChannel.this.unsafe().voidPromise());
        }
    };
    private volatile int state;
    private volatile LocalChannel peer;
    private volatile LocalAddress localAddress;
    private volatile LocalAddress remoteAddress;
    private volatile ChannelPromise connectPromise;
    private volatile boolean readInProgress;
    private volatile boolean registerInProgress;

    public LocalChannel() {
        super(null);
    }

    LocalChannel(LocalServerChannel paramLocalServerChannel, LocalChannel paramLocalChannel) {
        super(paramLocalServerChannel);
        this.peer = paramLocalChannel;
        this.localAddress = paramLocalServerChannel.localAddress();
        this.remoteAddress = paramLocalChannel.localAddress();
    }

    private static void finishPeerRead(LocalChannel paramLocalChannel, ChannelPipeline paramChannelPipeline) {
        if (paramLocalChannel.readInProgress) {
            paramLocalChannel.readInProgress = false;
            for (; ; ) {
                Object localObject = paramLocalChannel.inboundBuffer.poll();
                if (localObject == null) {
                    break;
                }
                paramChannelPipeline.fireChannelRead(localObject);
            }
            paramChannelPipeline.fireChannelReadComplete();
        }
    }

    public ChannelMetadata metadata() {
        return METADATA;
    }

    public ChannelConfig config() {
        return this.config;
    }

    public LocalServerChannel parent() {
        return (LocalServerChannel) super.parent();
    }

    public LocalAddress localAddress() {
        return (LocalAddress) super.localAddress();
    }

    public LocalAddress remoteAddress() {
        return (LocalAddress) super.remoteAddress();
    }

    public boolean isOpen() {
        return this.state < 3;
    }

    public boolean isActive() {
        return this.state == 2;
    }

    protected AbstractChannel.AbstractUnsafe newUnsafe() {
        return new LocalUnsafe(null);
    }

    protected boolean isCompatible(EventLoop paramEventLoop) {
        return paramEventLoop instanceof SingleThreadEventLoop;
    }

    protected SocketAddress localAddress0() {
        return this.localAddress;
    }

    protected SocketAddress remoteAddress0() {
        return this.remoteAddress;
    }

    protected void doRegister()
            throws Exception {
        if ((this.peer != null) && (parent() != null)) {
            final LocalChannel localLocalChannel = this.peer;
            this.registerInProgress = true;
            this.state = 2;
            localLocalChannel.remoteAddress = parent().localAddress();
            localLocalChannel.state = 2;
            localLocalChannel.eventLoop().execute(new Runnable() {
                public void run() {
                    LocalChannel.this.registerInProgress = false;
                    localLocalChannel.pipeline().fireChannelActive();
                    localLocalChannel.connectPromise.setSuccess();
                }
            });
        }
        ((SingleThreadEventExecutor) eventLoop()).addShutdownHook(this.shutdownHook);
    }

    protected void doBind(SocketAddress paramSocketAddress)
            throws Exception {
        this.localAddress = LocalChannelRegistry.register(this, this.localAddress, paramSocketAddress);
        this.state = 1;
    }

    protected void doDisconnect()
            throws Exception {
        doClose();
    }

    protected void doClose()
            throws Exception {
        if (this.state <= 2) {
            if (this.localAddress != null) {
                if (parent() == null) {
                    LocalChannelRegistry.unregister(this.localAddress);
                }
                this.localAddress = null;
            }
            this.state = 3;
        }
        final LocalChannel localLocalChannel = this.peer;
        if ((localLocalChannel != null) && (localLocalChannel.isActive())) {
            EventLoop localEventLoop = localLocalChannel.eventLoop();
            if ((localEventLoop.inEventLoop()) && (!this.registerInProgress)) {
                localLocalChannel.unsafe().close(unsafe().voidPromise());
            } else {
                localLocalChannel.eventLoop().execute(new Runnable() {
                    public void run() {
                        localLocalChannel.unsafe().close(LocalChannel.this.unsafe().voidPromise());
                    }
                });
            }
            this.peer = null;
        }
    }

    protected void doDeregister()
            throws Exception {
        ((SingleThreadEventExecutor) eventLoop()).removeShutdownHook(this.shutdownHook);
    }

    protected void doBeginRead()
            throws Exception {
        if (this.readInProgress) {
            return;
        }
        ChannelPipeline localChannelPipeline = pipeline();
        Queue localQueue = this.inboundBuffer;
        if (localQueue.isEmpty()) {
            this.readInProgress = true;
            return;
        }
        InternalThreadLocalMap localInternalThreadLocalMap = InternalThreadLocalMap.get();
        Integer localInteger = Integer.valueOf(localInternalThreadLocalMap.localChannelReaderStackDepth());
        if (localInteger.intValue() < 8) {
            localInternalThreadLocalMap.setLocalChannelReaderStackDepth(localInteger.intValue() | 0x1);
            try {
                for (; ; ) {
                    Object localObject1 = localQueue.poll();
                    if (localObject1 == null) {
                        break;
                    }
                    localChannelPipeline.fireChannelRead(localObject1);
                }
                localChannelPipeline.fireChannelReadComplete();
            } finally {
                localInternalThreadLocalMap.setLocalChannelReaderStackDepth(localInteger.intValue());
            }
        } else {
            eventLoop().execute(this.readTask);
        }
    }

    protected void doWrite(ChannelOutboundBuffer paramChannelOutboundBuffer)
            throws Exception {
        if (this.state < 2) {
            throw new NotYetConnectedException();
        }
        if (this.state > 2) {
            throw new ClosedChannelException();
        }
        final LocalChannel localLocalChannel = this.peer;
        final ChannelPipeline localChannelPipeline = localLocalChannel.pipeline();
        EventLoop localEventLoop = localLocalChannel.eventLoop();
        final Object localObject;
        if (localEventLoop == eventLoop()) {
            for (; ; ) {
                localObject = paramChannelOutboundBuffer.current();
                if (localObject == null) {
                    break;
                }
                localLocalChannel.inboundBuffer.add(localObject);
                ReferenceCountUtil.retain(localObject);
                paramChannelOutboundBuffer.remove();
            }
            finishPeerRead(localLocalChannel, localChannelPipeline);
        } else {
            localObject = new Object[paramChannelOutboundBuffer.size()];
            for (int i = 0; i < localObject.length; i++) {
                localObject[i] = ReferenceCountUtil.retain(paramChannelOutboundBuffer.current());
                paramChannelOutboundBuffer.remove();
            }
            localEventLoop.execute(new Runnable() {
                public void run() {
                    Collections.addAll(localLocalChannel.inboundBuffer, localObject);
                    LocalChannel.finishPeerRead(localLocalChannel, localChannelPipeline);
                }
            });
        }
    }

    private class LocalUnsafe
            extends AbstractChannel.AbstractUnsafe {
        private LocalUnsafe() {
            super();
        }

        public void connect(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2, ChannelPromise paramChannelPromise) {
            if ((!paramChannelPromise.setUncancellable()) || (!ensureOpen(paramChannelPromise))) {
                return;
            }
            if (LocalChannel.this.state == 2) {
                AlreadyConnectedException localAlreadyConnectedException = new AlreadyConnectedException();
                safeSetFailure(paramChannelPromise, localAlreadyConnectedException);
                LocalChannel.this.pipeline().fireExceptionCaught(localAlreadyConnectedException);
                return;
            }
            if (LocalChannel.this.connectPromise != null) {
                throw new ConnectionPendingException();
            }
            LocalChannel.this.connectPromise = paramChannelPromise;
            if ((LocalChannel.this.state != 1) && (paramSocketAddress2 == null)) {
                paramSocketAddress2 = new LocalAddress(LocalChannel.this);
            }
            if (paramSocketAddress2 != null) {
                try {
                    LocalChannel.this.doBind(paramSocketAddress2);
                } catch (Throwable localThrowable) {
                    safeSetFailure(paramChannelPromise, localThrowable);
                    close(voidPromise());
                    return;
                }
            }
            Channel localChannel = LocalChannelRegistry.get(paramSocketAddress1);
            if (!(localChannel instanceof LocalServerChannel)) {
                localObject = new ChannelException("connection refused");
                safeSetFailure(paramChannelPromise, (Throwable) localObject);
                close(voidPromise());
                return;
            }
            Object localObject = (LocalServerChannel) localChannel;
            LocalChannel.this.peer = ((LocalServerChannel) localObject).serve(LocalChannel.this);
        }
    }
}




