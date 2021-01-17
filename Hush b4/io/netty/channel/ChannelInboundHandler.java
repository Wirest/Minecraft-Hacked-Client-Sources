// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

public interface ChannelInboundHandler extends ChannelHandler
{
    void channelRegistered(final ChannelHandlerContext p0) throws Exception;
    
    void channelUnregistered(final ChannelHandlerContext p0) throws Exception;
    
    void channelActive(final ChannelHandlerContext p0) throws Exception;
    
    void channelInactive(final ChannelHandlerContext p0) throws Exception;
    
    void channelRead(final ChannelHandlerContext p0, final Object p1) throws Exception;
    
    void channelReadComplete(final ChannelHandlerContext p0) throws Exception;
    
    void userEventTriggered(final ChannelHandlerContext p0, final Object p1) throws Exception;
    
    void channelWritabilityChanged(final ChannelHandlerContext p0) throws Exception;
    
    void exceptionCaught(final ChannelHandlerContext p0, final Throwable p1) throws Exception;
}
