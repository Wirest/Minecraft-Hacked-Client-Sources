package io.netty.channel;

import io.netty.buffer.ByteBufAllocator;
import io.netty.util.AttributeMap;
import io.netty.util.concurrent.EventExecutor;

import java.net.SocketAddress;

public abstract interface ChannelHandlerContext
        extends AttributeMap {
    public abstract Channel channel();

    public abstract EventExecutor executor();

    public abstract String name();

    public abstract ChannelHandler handler();

    public abstract boolean isRemoved();

    public abstract ChannelHandlerContext fireChannelRegistered();

    public abstract ChannelHandlerContext fireChannelUnregistered();

    public abstract ChannelHandlerContext fireChannelActive();

    public abstract ChannelHandlerContext fireChannelInactive();

    public abstract ChannelHandlerContext fireExceptionCaught(Throwable paramThrowable);

    public abstract ChannelHandlerContext fireUserEventTriggered(Object paramObject);

    public abstract ChannelHandlerContext fireChannelRead(Object paramObject);

    public abstract ChannelHandlerContext fireChannelReadComplete();

    public abstract ChannelHandlerContext fireChannelWritabilityChanged();

    public abstract ChannelFuture bind(SocketAddress paramSocketAddress);

    public abstract ChannelFuture connect(SocketAddress paramSocketAddress);

    public abstract ChannelFuture connect(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2);

    public abstract ChannelFuture disconnect();

    public abstract ChannelFuture close();

    public abstract ChannelFuture deregister();

    public abstract ChannelFuture bind(SocketAddress paramSocketAddress, ChannelPromise paramChannelPromise);

    public abstract ChannelFuture connect(SocketAddress paramSocketAddress, ChannelPromise paramChannelPromise);

    public abstract ChannelFuture connect(SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2, ChannelPromise paramChannelPromise);

    public abstract ChannelFuture disconnect(ChannelPromise paramChannelPromise);

    public abstract ChannelFuture close(ChannelPromise paramChannelPromise);

    public abstract ChannelFuture deregister(ChannelPromise paramChannelPromise);

    public abstract ChannelHandlerContext read();

    public abstract ChannelFuture write(Object paramObject);

    public abstract ChannelFuture write(Object paramObject, ChannelPromise paramChannelPromise);

    public abstract ChannelHandlerContext flush();

    public abstract ChannelFuture writeAndFlush(Object paramObject, ChannelPromise paramChannelPromise);

    public abstract ChannelFuture writeAndFlush(Object paramObject);

    public abstract ChannelPipeline pipeline();

    public abstract ByteBufAllocator alloc();

    public abstract ChannelPromise newPromise();

    public abstract ChannelProgressivePromise newProgressivePromise();

    public abstract ChannelFuture newSucceededFuture();

    public abstract ChannelFuture newFailedFuture(Throwable paramThrowable);

    public abstract ChannelPromise voidPromise();
}




