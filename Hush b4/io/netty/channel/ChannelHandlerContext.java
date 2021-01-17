// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import io.netty.buffer.ByteBufAllocator;
import java.net.SocketAddress;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.AttributeMap;

public interface ChannelHandlerContext extends AttributeMap
{
    Channel channel();
    
    EventExecutor executor();
    
    String name();
    
    ChannelHandler handler();
    
    boolean isRemoved();
    
    ChannelHandlerContext fireChannelRegistered();
    
    ChannelHandlerContext fireChannelUnregistered();
    
    ChannelHandlerContext fireChannelActive();
    
    ChannelHandlerContext fireChannelInactive();
    
    ChannelHandlerContext fireExceptionCaught(final Throwable p0);
    
    ChannelHandlerContext fireUserEventTriggered(final Object p0);
    
    ChannelHandlerContext fireChannelRead(final Object p0);
    
    ChannelHandlerContext fireChannelReadComplete();
    
    ChannelHandlerContext fireChannelWritabilityChanged();
    
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
    
    ChannelHandlerContext read();
    
    ChannelFuture write(final Object p0);
    
    ChannelFuture write(final Object p0, final ChannelPromise p1);
    
    ChannelHandlerContext flush();
    
    ChannelFuture writeAndFlush(final Object p0, final ChannelPromise p1);
    
    ChannelFuture writeAndFlush(final Object p0);
    
    ChannelPipeline pipeline();
    
    ByteBufAllocator alloc();
    
    ChannelPromise newPromise();
    
    ChannelProgressivePromise newProgressivePromise();
    
    ChannelFuture newSucceededFuture();
    
    ChannelFuture newFailedFuture(final Throwable p0);
    
    ChannelPromise voidPromise();
}
