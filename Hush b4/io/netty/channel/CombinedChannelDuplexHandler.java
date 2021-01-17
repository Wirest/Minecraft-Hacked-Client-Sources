// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import java.net.SocketAddress;

public class CombinedChannelDuplexHandler<I extends ChannelInboundHandler, O extends ChannelOutboundHandler> extends ChannelDuplexHandler
{
    private I inboundHandler;
    private O outboundHandler;
    
    protected CombinedChannelDuplexHandler() {
    }
    
    public CombinedChannelDuplexHandler(final I inboundHandler, final O outboundHandler) {
        this.init(inboundHandler, outboundHandler);
    }
    
    protected final void init(final I inboundHandler, final O outboundHandler) {
        this.validate(inboundHandler, outboundHandler);
        this.inboundHandler = inboundHandler;
        this.outboundHandler = outboundHandler;
    }
    
    private void validate(final I inboundHandler, final O outboundHandler) {
        if (this.inboundHandler != null) {
            throw new IllegalStateException("init() can not be invoked if " + CombinedChannelDuplexHandler.class.getSimpleName() + " was constructed with non-default constructor.");
        }
        if (inboundHandler == null) {
            throw new NullPointerException("inboundHandler");
        }
        if (outboundHandler == null) {
            throw new NullPointerException("outboundHandler");
        }
        if (inboundHandler instanceof ChannelOutboundHandler) {
            throw new IllegalArgumentException("inboundHandler must not implement " + ChannelOutboundHandler.class.getSimpleName() + " to get combined.");
        }
        if (outboundHandler instanceof ChannelInboundHandler) {
            throw new IllegalArgumentException("outboundHandler must not implement " + ChannelInboundHandler.class.getSimpleName() + " to get combined.");
        }
    }
    
    protected final I inboundHandler() {
        return this.inboundHandler;
    }
    
    protected final O outboundHandler() {
        return this.outboundHandler;
    }
    
    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) throws Exception {
        if (this.inboundHandler == null) {
            throw new IllegalStateException("init() must be invoked before being added to a " + ChannelPipeline.class.getSimpleName() + " if " + CombinedChannelDuplexHandler.class.getSimpleName() + " was constructed with the default constructor.");
        }
        try {
            this.inboundHandler.handlerAdded(ctx);
        }
        finally {
            this.outboundHandler.handlerAdded(ctx);
        }
    }
    
    @Override
    public void handlerRemoved(final ChannelHandlerContext ctx) throws Exception {
        try {
            this.inboundHandler.handlerRemoved(ctx);
        }
        finally {
            this.outboundHandler.handlerRemoved(ctx);
        }
    }
    
    @Override
    public void channelRegistered(final ChannelHandlerContext ctx) throws Exception {
        this.inboundHandler.channelRegistered(ctx);
    }
    
    @Override
    public void channelUnregistered(final ChannelHandlerContext ctx) throws Exception {
        this.inboundHandler.channelUnregistered(ctx);
    }
    
    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        this.inboundHandler.channelActive(ctx);
    }
    
    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        this.inboundHandler.channelInactive(ctx);
    }
    
    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        this.inboundHandler.exceptionCaught(ctx, cause);
    }
    
    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) throws Exception {
        this.inboundHandler.userEventTriggered(ctx, evt);
    }
    
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        this.inboundHandler.channelRead(ctx, msg);
    }
    
    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) throws Exception {
        this.inboundHandler.channelReadComplete(ctx);
    }
    
    @Override
    public void bind(final ChannelHandlerContext ctx, final SocketAddress localAddress, final ChannelPromise promise) throws Exception {
        this.outboundHandler.bind(ctx, localAddress, promise);
    }
    
    @Override
    public void connect(final ChannelHandlerContext ctx, final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise promise) throws Exception {
        this.outboundHandler.connect(ctx, remoteAddress, localAddress, promise);
    }
    
    @Override
    public void disconnect(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        this.outboundHandler.disconnect(ctx, promise);
    }
    
    @Override
    public void close(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        this.outboundHandler.close(ctx, promise);
    }
    
    @Override
    public void deregister(final ChannelHandlerContext ctx, final ChannelPromise promise) throws Exception {
        this.outboundHandler.deregister(ctx, promise);
    }
    
    @Override
    public void read(final ChannelHandlerContext ctx) throws Exception {
        this.outboundHandler.read(ctx);
    }
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        this.outboundHandler.write(ctx, msg, promise);
    }
    
    @Override
    public void flush(final ChannelHandlerContext ctx) throws Exception {
        this.outboundHandler.flush(ctx);
    }
    
    @Override
    public void channelWritabilityChanged(final ChannelHandlerContext ctx) throws Exception {
        this.inboundHandler.channelWritabilityChanged(ctx);
    }
}
