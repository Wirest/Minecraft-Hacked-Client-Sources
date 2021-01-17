// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.epoll;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPromise;
import io.netty.channel.socket.ServerSocketChannelConfig;
import io.netty.channel.ChannelConfig;
import io.netty.channel.AbstractChannel;
import io.netty.channel.ChannelMetadata;
import io.netty.channel.ChannelOutboundBuffer;
import java.net.SocketAddress;
import io.netty.channel.EventLoop;
import java.net.InetSocketAddress;
import io.netty.channel.socket.ServerSocketChannel;

public final class EpollServerSocketChannel extends AbstractEpollChannel implements ServerSocketChannel
{
    private final EpollServerSocketChannelConfig config;
    private volatile InetSocketAddress local;
    
    public EpollServerSocketChannel() {
        super(Native.socketStreamFd(), 4);
        this.config = new EpollServerSocketChannelConfig(this);
    }
    
    @Override
    protected boolean isCompatible(final EventLoop loop) {
        return loop instanceof EpollEventLoop;
    }
    
    @Override
    protected void doBind(final SocketAddress localAddress) throws Exception {
        final InetSocketAddress addr = (InetSocketAddress)localAddress;
        AbstractEpollChannel.checkResolvable(addr);
        Native.bind(this.fd, addr.getAddress(), addr.getPort());
        this.local = Native.localAddress(this.fd);
        Native.listen(this.fd, this.config.getBacklog());
        this.active = true;
    }
    
    @Override
    public EpollServerSocketChannelConfig config() {
        return this.config;
    }
    
    @Override
    protected InetSocketAddress localAddress0() {
        return this.local;
    }
    
    @Override
    protected InetSocketAddress remoteAddress0() {
        return null;
    }
    
    @Override
    protected AbstractEpollUnsafe newUnsafe() {
        return new EpollServerSocketUnsafe();
    }
    
    @Override
    protected void doWrite(final ChannelOutboundBuffer in) throws Exception {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected Object filterOutboundMessage(final Object msg) throws Exception {
        throw new UnsupportedOperationException();
    }
    
    final class EpollServerSocketUnsafe extends AbstractEpollUnsafe
    {
        @Override
        public void connect(final SocketAddress socketAddress, final SocketAddress socketAddress2, final ChannelPromise channelPromise) {
            channelPromise.setFailure((Throwable)new UnsupportedOperationException());
        }
        
        @Override
        void epollInReady() {
            assert EpollServerSocketChannel.this.eventLoop().inEventLoop();
            final ChannelPipeline pipeline = EpollServerSocketChannel.this.pipeline();
            Throwable exception = null;
            try {
                try {
                    while (true) {
                        final int socketFd = Native.accept(EpollServerSocketChannel.this.fd);
                        if (socketFd == -1) {
                            break;
                        }
                        try {
                            this.readPending = false;
                            pipeline.fireChannelRead(new EpollSocketChannel(EpollServerSocketChannel.this, socketFd));
                        }
                        catch (Throwable t) {
                            pipeline.fireChannelReadComplete();
                            pipeline.fireExceptionCaught(t);
                        }
                    }
                }
                catch (Throwable t2) {
                    exception = t2;
                }
                pipeline.fireChannelReadComplete();
                if (exception != null) {
                    pipeline.fireExceptionCaught(exception);
                }
            }
            finally {
                if (!EpollServerSocketChannel.this.config.isAutoRead() && !this.readPending) {
                    this.clearEpollIn0();
                }
            }
        }
    }
}
