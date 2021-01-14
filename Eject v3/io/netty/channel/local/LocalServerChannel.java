package io.netty.channel.local;

import io.netty.channel.*;
import io.netty.util.concurrent.SingleThreadEventExecutor;

import java.net.SocketAddress;
import java.util.ArrayDeque;
import java.util.Queue;

public class LocalServerChannel
        extends AbstractServerChannel {
    private final ChannelConfig config = new DefaultChannelConfig(this);
    private final Queue<Object> inboundBuffer = new ArrayDeque();
    private final Runnable shutdownHook = new Runnable() {
        public void run() {
            LocalServerChannel.this.unsafe().close(LocalServerChannel.this.unsafe().voidPromise());
        }
    };
    private volatile int state;
    private volatile LocalAddress localAddress;
    private volatile boolean acceptInProgress;

    public ChannelConfig config() {
        return this.config;
    }

    public LocalAddress localAddress() {
        return (LocalAddress) super.localAddress();
    }

    public LocalAddress remoteAddress() {
        return (LocalAddress) super.remoteAddress();
    }

    public boolean isOpen() {
        return this.state < 2;
    }

    public boolean isActive() {
        return this.state == 1;
    }

    protected boolean isCompatible(EventLoop paramEventLoop) {
        return paramEventLoop instanceof SingleThreadEventLoop;
    }

    protected SocketAddress localAddress0() {
        return this.localAddress;
    }

    protected void doRegister()
            throws Exception {
        ((SingleThreadEventExecutor) eventLoop()).addShutdownHook(this.shutdownHook);
    }

    protected void doBind(SocketAddress paramSocketAddress)
            throws Exception {
        this.localAddress = LocalChannelRegistry.register(this, this.localAddress, paramSocketAddress);
        this.state = 1;
    }

    protected void doClose()
            throws Exception {
        if (this.state <= 1) {
            if (this.localAddress != null) {
                LocalChannelRegistry.unregister(this.localAddress);
                this.localAddress = null;
            }
            this.state = 2;
        }
    }

    protected void doDeregister()
            throws Exception {
        ((SingleThreadEventExecutor) eventLoop()).removeShutdownHook(this.shutdownHook);
    }

    protected void doBeginRead()
            throws Exception {
        if (this.acceptInProgress) {
            return;
        }
        Queue localQueue = this.inboundBuffer;
        if (localQueue.isEmpty()) {
            this.acceptInProgress = true;
            return;
        }
        ChannelPipeline localChannelPipeline = pipeline();
        for (; ; ) {
            Object localObject = localQueue.poll();
            if (localObject == null) {
                break;
            }
            localChannelPipeline.fireChannelRead(localObject);
        }
        localChannelPipeline.fireChannelReadComplete();
    }

    LocalChannel serve(LocalChannel paramLocalChannel) {
        final LocalChannel localLocalChannel = new LocalChannel(this, paramLocalChannel);
        if (eventLoop().inEventLoop()) {
            serve0(localLocalChannel);
        } else {
            eventLoop().execute(new Runnable() {
                public void run() {
                    LocalServerChannel.this.serve0(localLocalChannel);
                }
            });
        }
        return localLocalChannel;
    }

    private void serve0(LocalChannel paramLocalChannel) {
        this.inboundBuffer.add(paramLocalChannel);
        if (this.acceptInProgress) {
            this.acceptInProgress = false;
            ChannelPipeline localChannelPipeline = pipeline();
            for (; ; ) {
                Object localObject = this.inboundBuffer.poll();
                if (localObject == null) {
                    break;
                }
                localChannelPipeline.fireChannelRead(localObject);
            }
            localChannelPipeline.fireChannelReadComplete();
        }
    }
}




