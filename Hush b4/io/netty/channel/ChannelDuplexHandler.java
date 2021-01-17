// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import java.net.SocketAddress;

public class ChannelDuplexHandler extends ChannelInboundHandlerAdapter implements ChannelOutboundHandler
{
    @Override
    public void bind(final ChannelHandlerContext ctx, final SocketAddress localAddress, final ChannelPromise future) throws Exception {
        ctx.bind(localAddress, future);
    }
    
    @Override
    public void connect(final ChannelHandlerContext ctx, final SocketAddress remoteAddress, final SocketAddress localAddress, final ChannelPromise future) throws Exception {
        ctx.connect(remoteAddress, localAddress, future);
    }
    
    @Override
    public void disconnect(final ChannelHandlerContext ctx, final ChannelPromise future) throws Exception {
        ctx.disconnect(future);
    }
    
    @Override
    public void close(final ChannelHandlerContext ctx, final ChannelPromise future) throws Exception {
        ctx.close(future);
    }
    
    @Override
    public void deregister(final ChannelHandlerContext ctx, final ChannelPromise future) throws Exception {
        ctx.deregister(future);
    }
    
    @Override
    public void read(final ChannelHandlerContext ctx) throws Exception {
        ctx.read();
    }
    
    @Override
    public void write(final ChannelHandlerContext ctx, final Object msg, final ChannelPromise promise) throws Exception {
        ctx.write(msg, promise);
    }
    
    @Override
    public void flush(final ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
