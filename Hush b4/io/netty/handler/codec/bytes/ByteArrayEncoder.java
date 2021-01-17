// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.bytes;

import io.netty.buffer.Unpooled;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.MessageToMessageEncoder;

@ChannelHandler.Sharable
public class ByteArrayEncoder extends MessageToMessageEncoder<byte[]>
{
    @Override
    protected void encode(final ChannelHandlerContext ctx, final byte[] msg, final List<Object> out) throws Exception {
        out.add(Unpooled.wrappedBuffer(msg));
    }
}
