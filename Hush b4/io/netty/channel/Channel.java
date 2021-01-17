// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import io.netty.buffer.ByteBufAllocator;
import java.net.SocketAddress;
import io.netty.util.AttributeMap;

public interface Channel extends AttributeMap, Comparable<Channel>
{
    EventLoop eventLoop();
    
    Channel parent();
    
    ChannelConfig config();
    
    boolean isOpen();
    
    boolean isRegistered();
    
    boolean isActive();
    
    ChannelMetadata metadata();
    
    SocketAddress localAddress();
    
    SocketAddress remoteAddress();
    
    ChannelFuture closeFuture();
    
    boolean isWritable();
    
    Unsafe unsafe();
    
    ChannelPipeline pipeline();
    
    ByteBufAllocator alloc();
    
    ChannelPromise newPromise();
    
    ChannelProgressivePromise newProgressivePromise();
    
    ChannelFuture newSucceededFuture();
    
    ChannelFuture newFailedFuture(final Throwable p0);
    
    ChannelPromise voidPromise();
    
    ChannelFuture bind(final SocketAddress p0);
    
    ChannelFuture connect(final SocketAddress p0);
    
    ChannelFuture connect(final SocketAddress p0, final SocketAddress p1);
    
    ChannelFuture disconnect();
    
    ChannelFuture close();
    
    ChannelFuture deregister();
    
    ChannelFuture bind(final SocketAddress p0, final ChannelPromise p1);
    
    ChannelFuture connect(final SocketAddress p0, final ChannelPromise p1);
    
    ChannelFuture connect(final SocketAddress p0, final SocketAddress p1, final ChannelPromise p2);
    
    ChannelFuture disconnect(final ChannelPromise p0);
    
    ChannelFuture close(final ChannelPromise p0);
    
    ChannelFuture deregister(final ChannelPromise p0);
    
    Channel read();
    
    ChannelFuture write(final Object p0);
    
    ChannelFuture write(final Object p0, final ChannelPromise p1);
    
    Channel flush();
    
    ChannelFuture writeAndFlush(final Object p0, final ChannelPromise p1);
    
    ChannelFuture writeAndFlush(final Object p0);
    
    public interface Unsafe
    {
        SocketAddress localAddress();
        
        SocketAddress remoteAddress();
        
        void register(final EventLoop p0, final ChannelPromise p1);
        
        void bind(final SocketAddress p0, final ChannelPromise p1);
        
        void connect(final SocketAddress p0, final SocketAddress p1, final ChannelPromise p2);
        
        void disconnect(final ChannelPromise p0);
        
        void close(final ChannelPromise p0);
        
        void closeForcibly();
        
        void deregister(final ChannelPromise p0);
        
        void beginRead();
        
        void write(final Object p0, final ChannelPromise p1);
        
        void flush();
        
        ChannelPromise voidPromise();
        
        ChannelOutboundBuffer outboundBuffer();
    }
}
