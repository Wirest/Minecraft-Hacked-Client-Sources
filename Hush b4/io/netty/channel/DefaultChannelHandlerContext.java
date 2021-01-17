// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import io.netty.util.concurrent.EventExecutorGroup;

final class DefaultChannelHandlerContext extends AbstractChannelHandlerContext
{
    private final ChannelHandler handler;
    
    DefaultChannelHandlerContext(final DefaultChannelPipeline pipeline, final EventExecutorGroup group, final String name, final ChannelHandler handler) {
        super(pipeline, group, name, isInbound(handler), isOutbound(handler));
        if (handler == null) {
            throw new NullPointerException("handler");
        }
        this.handler = handler;
    }
    
    @Override
    public ChannelHandler handler() {
        return this.handler;
    }
    
    private static boolean isInbound(final ChannelHandler handler) {
        return handler instanceof ChannelInboundHandler;
    }
    
    private static boolean isOutbound(final ChannelHandler handler) {
        return handler instanceof ChannelOutboundHandler;
    }
}
