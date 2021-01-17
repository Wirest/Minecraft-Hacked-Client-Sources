// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

public class ChannelInboundHandlerAdapter extends ChannelHandlerAdapter implements ChannelInboundHandler
{
    @Override
    public void channelRegistered(final ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelRegistered();
    }
    
    @Override
    public void channelUnregistered(final ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelUnregistered();
    }
    
    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
    }
    
    @Override
    public void channelInactive(final ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelInactive();
    }
    
    @Override
    public void channelRead(final ChannelHandlerContext ctx, final Object msg) throws Exception {
        ctx.fireChannelRead(msg);
    }
    
    @Override
    public void channelReadComplete(final ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelReadComplete();
    }
    
    @Override
    public void userEventTriggered(final ChannelHandlerContext ctx, final Object evt) throws Exception {
        ctx.fireUserEventTriggered(evt);
    }
    
    @Override
    public void channelWritabilityChanged(final ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelWritabilityChanged();
    }
    
    @Override
    public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) throws Exception {
        ctx.fireExceptionCaught(cause);
    }
}
