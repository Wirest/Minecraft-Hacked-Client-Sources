package io.netty.channel;

import io.netty.util.concurrent.EventExecutorGroup;

import java.net.SocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract interface ChannelPipeline
        extends Iterable<Map.Entry<String, ChannelHandler>> {
    public abstract ChannelPipeline addFirst(String paramString, ChannelHandler paramChannelHandler);

    public abstract ChannelPipeline addFirst(EventExecutorGroup paramEventExecutorGroup, String paramString, ChannelHandler paramChannelHandler);

    public abstract ChannelPipeline addLast(String paramString, ChannelHandler paramChannelHandler);

    public abstract ChannelPipeline addLast(EventExecutorGroup paramEventExecutorGroup, String paramString, ChannelHandler paramChannelHandler);

    public abstract ChannelPipeline addBefore(String paramString1, String paramString2, ChannelHandler paramChannelHandler);

    public abstract ChannelPipeline addBefore(EventExecutorGroup paramEventExecutorGroup, String paramString1, String paramString2, ChannelHandler paramChannelHandler);

    public abstract ChannelPipeline addAfter(String paramString1, String paramString2, ChannelHandler paramChannelHandler);

    public abstract ChannelPipeline addAfter(EventExecutorGroup paramEventExecutorGroup, String paramString1, String paramString2, ChannelHandler paramChannelHandler);

    public abstract ChannelPipeline addFirst(ChannelHandler... paramVarArgs);

    public abstract ChannelPipeline addFirst(EventExecutorGroup paramEventExecutorGroup, ChannelHandler... paramVarArgs);

    public abstract ChannelPipeline addLast(ChannelHandler... paramVarArgs);

    public abstract ChannelPipeline addLast(EventExecutorGroup paramEventExecutorGroup, ChannelHandler... paramVarArgs);

    public abstract ChannelPipeline remove(ChannelHandler paramChannelHandler);

    public abstract ChannelHandler remove(String paramString);

    public abstract <T extends ChannelHandler> T remove(Class<T> paramClass);

    public abstract ChannelHandler removeFirst();

    public abstract ChannelHandler removeLast();

    public abstract ChannelPipeline replace(ChannelHandler paramChannelHandler1, String paramString, ChannelHandler paramChannelHandler2);

    public abstract ChannelHandler replace(String paramString1, String paramString2, ChannelHandler paramChannelHandler);

    public abstract <T extends ChannelHandler> T replace(Class<T> paramClass, String paramString, ChannelHandler paramChannelHandler);

    public abstract ChannelHandler first();

    public abstract ChannelHandlerContext firstContext();

    public abstract ChannelHandler last();

    public abstract ChannelHandlerContext lastContext();

    public abstract ChannelHandler get(String paramString);

    public abstract <T extends ChannelHandler> T get(Class<T> paramClass);

    public abstract ChannelHandlerContext context(ChannelHandler paramChannelHandler);

    public abstract ChannelHandlerContext context(String paramString);

    public abstract ChannelHandlerContext context(Class<? extends ChannelHandler> paramClass);

    public abstract Channel channel();

    public abstract List<String> names();

    public abstract Map<String, ChannelHandler> toMap();

    public abstract ChannelPipeline fireChannelRegistered();

    public abstract ChannelPipeline fireChannelUnregistered();

    public abstract ChannelPipeline fireChannelActive();

    public abstract ChannelPipeline fireChannelInactive();

    public abstract ChannelPipeline fireExceptionCaught(Throwable paramThrowable);

    public abstract ChannelPipeline fireUserEventTriggered(Object paramObject);

    public abstract ChannelPipeline fireChannelRead(Object paramObject);

    public abstract ChannelPipeline fireChannelReadComplete();

    public abstract ChannelPipeline fireChannelWritabilityChanged();

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

    public abstract ChannelPipeline read();

    public abstract ChannelFuture write(Object paramObject);

    public abstract ChannelFuture write(Object paramObject, ChannelPromise paramChannelPromise);

    public abstract ChannelPipeline flush();

    public abstract ChannelFuture writeAndFlush(Object paramObject, ChannelPromise paramChannelPromise);

    public abstract ChannelFuture writeAndFlush(Object paramObject);
}




