// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.local;

import io.netty.channel.ChannelException;
import java.nio.channels.ConnectionPendingException;
import java.nio.channels.AlreadyConnectedException;
import java.util.Collection;
import java.util.Collections;
import io.netty.util.ReferenceCountUtil;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NotYetConnectedException;
import io.netty.channel.ChannelOutboundBuffer;
import io.netty.util.internal.InternalThreadLocalMap;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import java.net.SocketAddress;
import io.netty.channel.SingleThreadEventLoop;
import io.netty.channel.EventLoop;
import io.netty.channel.ChannelPipeline;
import java.util.ArrayDeque;
import io.netty.channel.DefaultChannelConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPromise;
import java.util.Queue;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.AbstractChannel;

public class LocalChannel extends AbstractChannel
{
    private static final ChannelMetadata METADATA;
    private static final int MAX_READER_STACK_DEPTH = 8;
    private final ChannelConfig config;
    private final Queue<Object> inboundBuffer;
    private final Runnable readTask;
    private final Runnable shutdownHook;
    private volatile int state;
    private volatile LocalChannel peer;
    private volatile LocalAddress localAddress;
    private volatile LocalAddress remoteAddress;
    private volatile ChannelPromise connectPromise;
    private volatile boolean readInProgress;
    private volatile boolean registerInProgress;
    
    public LocalChannel() {
        super(null);
        this.config = new DefaultChannelConfig(this);
        this.inboundBuffer = new ArrayDeque<Object>();
        this.readTask = new Runnable() {
            @Override
            public void run() {
                final ChannelPipeline pipeline = LocalChannel.this.pipeline();
                while (true) {
                    final Object m = LocalChannel.this.inboundBuffer.poll();
                    if (m == null) {
                        break;
                    }
                    pipeline.fireChannelRead(m);
                }
                pipeline.fireChannelReadComplete();
            }
        };
        this.shutdownHook = new Runnable() {
            @Override
            public void run() {
                LocalChannel.this.unsafe().close(LocalChannel.this.unsafe().voidPromise());
            }
        };
    }
    
    LocalChannel(final LocalServerChannel parent, final LocalChannel peer) {
        super(parent);
        this.config = new DefaultChannelConfig(this);
        this.inboundBuffer = new ArrayDeque<Object>();
        this.readTask = new Runnable() {
            @Override
            public void run() {
                final ChannelPipeline pipeline = LocalChannel.this.pipeline();
                while (true) {
                    final Object m = LocalChannel.this.inboundBuffer.poll();
                    if (m == null) {
                        break;
                    }
                    pipeline.fireChannelRead(m);
                }
                pipeline.fireChannelReadComplete();
            }
        };
        this.shutdownHook = new Runnable() {
            @Override
            public void run() {
                LocalChannel.this.unsafe().close(LocalChannel.this.unsafe().voidPromise());
            }
        };
        this.peer = peer;
        this.localAddress = parent.localAddress();
        this.remoteAddress = peer.localAddress();
    }
    
    @Override
    public ChannelMetadata metadata() {
        return LocalChannel.METADATA;
    }
    
    @Override
    public ChannelConfig config() {
        return this.config;
    }
    
    @Override
    public LocalServerChannel parent() {
        return (LocalServerChannel)super.parent();
    }
    
    @Override
    public LocalAddress localAddress() {
        return (LocalAddress)super.localAddress();
    }
    
    @Override
    public LocalAddress remoteAddress() {
        return (LocalAddress)super.remoteAddress();
    }
    
    @Override
    public boolean isOpen() {
        return this.state < 3;
    }
    
    @Override
    public boolean isActive() {
        return this.state == 2;
    }
    
    @Override
    protected AbstractUnsafe newUnsafe() {
        return new LocalUnsafe();
    }
    
    @Override
    protected boolean isCompatible(final EventLoop loop) {
        return loop instanceof SingleThreadEventLoop;
    }
    
    @Override
    protected SocketAddress localAddress0() {
        return this.localAddress;
    }
    
    @Override
    protected SocketAddress remoteAddress0() {
        return this.remoteAddress;
    }
    
    @Override
    protected void doRegister() throws Exception {
        if (this.peer != null && this.parent() != null) {
            final LocalChannel peer = this.peer;
            this.registerInProgress = true;
            this.state = 2;
            peer.remoteAddress = this.parent().localAddress();
            peer.state = 2;
            peer.eventLoop().execute(new Runnable() {
                @Override
                public void run() {
                    LocalChannel.this.registerInProgress = false;
                    peer.pipeline().fireChannelActive();
                    peer.connectPromise.setSuccess();
                }
            });
        }
        ((SingleThreadEventExecutor)this.eventLoop()).addShutdownHook(this.shutdownHook);
    }
    
    @Override
    protected void doBind(final SocketAddress localAddress) throws Exception {
        this.localAddress = LocalChannelRegistry.register(this, this.localAddress, localAddress);
        this.state = 1;
    }
    
    @Override
    protected void doDisconnect() throws Exception {
        this.doClose();
    }
    
    @Override
    protected void doClose() throws Exception {
        if (this.state <= 2) {
            if (this.localAddress != null) {
                if (this.parent() == null) {
                    LocalChannelRegistry.unregister(this.localAddress);
                }
                this.localAddress = null;
            }
            this.state = 3;
        }
        final LocalChannel peer = this.peer;
        if (peer != null && peer.isActive()) {
            final EventLoop eventLoop = peer.eventLoop();
            if (eventLoop.inEventLoop() && !this.registerInProgress) {
                peer.unsafe().close(this.unsafe().voidPromise());
            }
            else {
                peer.eventLoop().execute(new Runnable() {
                    @Override
                    public void run() {
                        peer.unsafe().close(LocalChannel.this.unsafe().voidPromise());
                    }
                });
            }
            this.peer = null;
        }
    }
    
    @Override
    protected void doDeregister() throws Exception {
        ((SingleThreadEventExecutor)this.eventLoop()).removeShutdownHook(this.shutdownHook);
    }
    
    @Override
    protected void doBeginRead() throws Exception {
        if (this.readInProgress) {
            return;
        }
        final ChannelPipeline pipeline = this.pipeline();
        final Queue<Object> inboundBuffer = this.inboundBuffer;
        if (inboundBuffer.isEmpty()) {
            this.readInProgress = true;
            return;
        }
        final InternalThreadLocalMap threadLocals = InternalThreadLocalMap.get();
        final Integer stackDepth = threadLocals.localChannelReaderStackDepth();
        if (stackDepth < 8) {
            threadLocals.setLocalChannelReaderStackDepth(stackDepth + 1);
            try {
                while (true) {
                    final Object received = inboundBuffer.poll();
                    if (received == null) {
                        break;
                    }
                    pipeline.fireChannelRead(received);
                }
                pipeline.fireChannelReadComplete();
            }
            finally {
                threadLocals.setLocalChannelReaderStackDepth(stackDepth);
            }
        }
        else {
            this.eventLoop().execute(this.readTask);
        }
    }
    
    @Override
    protected void doWrite(final ChannelOutboundBuffer in) throws Exception {
        if (this.state < 2) {
            throw new NotYetConnectedException();
        }
        if (this.state > 2) {
            throw new ClosedChannelException();
        }
        final LocalChannel peer = this.peer;
        final ChannelPipeline peerPipeline = peer.pipeline();
        final EventLoop peerLoop = peer.eventLoop();
        if (peerLoop == this.eventLoop()) {
            while (true) {
                final Object msg = in.current();
                if (msg == null) {
                    break;
                }
                peer.inboundBuffer.add(msg);
                ReferenceCountUtil.retain(msg);
                in.remove();
            }
            finishPeerRead(peer, peerPipeline);
        }
        else {
            final Object[] msgsCopy = new Object[in.size()];
            for (int i = 0; i < msgsCopy.length; ++i) {
                msgsCopy[i] = ReferenceCountUtil.retain(in.current());
                in.remove();
            }
            peerLoop.execute(new Runnable() {
                @Override
                public void run() {
                    Collections.addAll(peer.inboundBuffer, msgsCopy);
                    finishPeerRead(peer, peerPipeline);
                }
            });
        }
    }
    
    private static void finishPeerRead(final LocalChannel peer, final ChannelPipeline peerPipeline) {
        if (peer.readInProgress) {
            peer.readInProgress = false;
            while (true) {
                final Object received = peer.inboundBuffer.poll();
                if (received == null) {
                    break;
                }
                peerPipeline.fireChannelRead(received);
            }
            peerPipeline.fireChannelReadComplete();
        }
    }
    
    static {
        METADATA = new ChannelMetadata(false);
    }
    
    private class LocalUnsafe extends AbstractUnsafe
    {
        @Override
        public void connect(final SocketAddress remoteAddress, SocketAddress localAddress, final ChannelPromise promise) {
            if (!promise.setUncancellable() || !this.ensureOpen(promise)) {
                return;
            }
            if (LocalChannel.this.state == 2) {
                final Exception cause = new AlreadyConnectedException();
                this.safeSetFailure(promise, cause);
                LocalChannel.this.pipeline().fireExceptionCaught(cause);
                return;
            }
            if (LocalChannel.this.connectPromise != null) {
                throw new ConnectionPendingException();
            }
            LocalChannel.this.connectPromise = promise;
            if (LocalChannel.this.state != 1 && localAddress == null) {
                localAddress = new LocalAddress(LocalChannel.this);
            }
            if (localAddress != null) {
                try {
                    LocalChannel.this.doBind(localAddress);
                }
                catch (Throwable t) {
                    this.safeSetFailure(promise, t);
                    this.close(this.voidPromise());
                    return;
                }
            }
            final Channel boundChannel = LocalChannelRegistry.get(remoteAddress);
            if (!(boundChannel instanceof LocalServerChannel)) {
                final Exception cause2 = new ChannelException("connection refused");
                this.safeSetFailure(promise, cause2);
                this.close(this.voidPromise());
                return;
            }
            final LocalServerChannel serverChannel = (LocalServerChannel)boundChannel;
            LocalChannel.this.peer = serverChannel.serve(LocalChannel.this);
        }
    }
}
