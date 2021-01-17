// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.oio;

import java.net.ConnectException;
import io.netty.channel.ChannelPromise;
import java.net.SocketAddress;
import io.netty.channel.ThreadPerChannelEventLoop;
import io.netty.channel.EventLoop;
import io.netty.channel.Channel;
import io.netty.channel.AbstractChannel;

public abstract class AbstractOioChannel extends AbstractChannel
{
    protected static final int SO_TIMEOUT = 1000;
    private volatile boolean readPending;
    private final Runnable readTask;
    
    protected AbstractOioChannel(final Channel parent) {
        super(parent);
        this.readTask = new Runnable() {
            @Override
            public void run() {
                if (!AbstractOioChannel.this.isReadPending() && !AbstractOioChannel.this.config().isAutoRead()) {
                    return;
                }
                AbstractOioChannel.this.setReadPending(false);
                AbstractOioChannel.this.doRead();
            }
        };
    }
    
    @Override
    protected AbstractUnsafe newUnsafe() {
        return new DefaultOioUnsafe();
    }
    
    @Override
    protected boolean isCompatible(final EventLoop loop) {
        return loop instanceof ThreadPerChannelEventLoop;
    }
    
    protected abstract void doConnect(final SocketAddress p0, final SocketAddress p1) throws Exception;
    
    @Override
    protected void doBeginRead() throws Exception {
        if (this.isReadPending()) {
            return;
        }
        this.setReadPending(true);
        this.eventLoop().execute(this.readTask);
    }
    
    protected abstract void doRead();
    
    protected boolean isReadPending() {
        return this.readPending;
    }
    
    protected void setReadPending(final boolean readPending) {
        this.readPending = readPending;
    }
    
    private final class DefaultOioUnsafe extends AbstractUnsafe
    {
        @Override
        public void connect(final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) {
            if (!promise.setUncancellable() || !this.ensureOpen(promise)) {
                return;
            }
            try {
                final boolean wasActive = AbstractOioChannel.this.isActive();
                AbstractOioChannel.this.doConnect(remoteAddress, localAddress);
                this.safeSetSuccess(promise);
                if (!wasActive && AbstractOioChannel.this.isActive()) {
                    AbstractOioChannel.this.pipeline().fireChannelActive();
                }
            }
            catch (Throwable t) {
                if (t instanceof ConnectException) {
                    final Throwable newT = new ConnectException(t.getMessage() + ": " + remoteAddress);
                    newT.setStackTrace(t.getStackTrace());
                    t = newT;
                }
                this.safeSetFailure(promise, t);
                this.closeIfClosed();
            }
        }
    }
}
