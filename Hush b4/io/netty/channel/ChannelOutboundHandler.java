// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel;

import java.net.SocketAddress;

public interface ChannelOutboundHandler extends ChannelHandler
{
    void bind(final ChannelHandlerContext p0, final SocketAddress p1, final ChannelPromise p2) throws Exception;
    
    void connect(final ChannelHandlerContext p0, final SocketAddress p1, final SocketAddress p2, final ChannelPromise p3) throws Exception;
    
    void disconnect(final ChannelHandlerContext p0, final ChannelPromise p1) throws Exception;
    
    void close(final ChannelHandlerContext p0, final ChannelPromise p1) throws Exception;
    
    void deregister(final ChannelHandlerContext p0, final ChannelPromise p1) throws Exception;
    
    void read(final ChannelHandlerContext p0) throws Exception;
    
    void write(final ChannelHandlerContext p0, final Object p1, final ChannelPromise p2) throws Exception;
    
    void flush(final ChannelHandlerContext p0) throws Exception;
}
