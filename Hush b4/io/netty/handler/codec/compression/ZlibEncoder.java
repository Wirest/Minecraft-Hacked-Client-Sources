// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.compression;

import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelFuture;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToByteEncoder;

public abstract class ZlibEncoder extends MessageToByteEncoder<ByteBuf>
{
    protected ZlibEncoder() {
        super(false);
    }
    
    public abstract boolean isClosed();
    
    public abstract ChannelFuture close();
    
    public abstract ChannelFuture close(final ChannelPromise p0);
}
