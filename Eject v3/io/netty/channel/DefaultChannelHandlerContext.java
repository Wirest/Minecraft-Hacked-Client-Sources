package io.netty.channel;

import io.netty.util.concurrent.EventExecutorGroup;

final class DefaultChannelHandlerContext
        extends AbstractChannelHandlerContext {
    private final ChannelHandler handler;

    DefaultChannelHandlerContext(DefaultChannelPipeline paramDefaultChannelPipeline, EventExecutorGroup paramEventExecutorGroup, String paramString, ChannelHandler paramChannelHandler) {
        super(paramDefaultChannelPipeline, paramEventExecutorGroup, paramString, isInbound(paramChannelHandler), isOutbound(paramChannelHandler));
        if (paramChannelHandler == null) {
            throw new NullPointerException("handler");
        }
        this.handler = paramChannelHandler;
    }

    private static boolean isInbound(ChannelHandler paramChannelHandler) {
        return paramChannelHandler instanceof ChannelInboundHandler;
    }

    private static boolean isOutbound(ChannelHandler paramChannelHandler) {
        return paramChannelHandler instanceof ChannelOutboundHandler;
    }

    public ChannelHandler handler() {
        return this.handler;
    }
}




