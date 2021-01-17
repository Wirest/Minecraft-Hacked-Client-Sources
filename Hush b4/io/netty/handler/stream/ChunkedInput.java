// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.stream;

import io.netty.channel.ChannelHandlerContext;

public interface ChunkedInput<B>
{
    boolean isEndOfInput() throws Exception;
    
    void close() throws Exception;
    
    B readChunk(final ChannelHandlerContext p0) throws Exception;
}
