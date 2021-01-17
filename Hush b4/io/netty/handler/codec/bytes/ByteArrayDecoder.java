// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.bytes;

import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToMessageDecoder;

public class ByteArrayDecoder extends MessageToMessageDecoder<ByteBuf>
{
    @Override
    protected void decode(final ChannelHandlerContext ctx, final ByteBuf msg, final List<Object> out) throws Exception {
        final byte[] array = new byte[msg.readableBytes()];
        msg.getBytes(0, array);
        out.add(array);
    }
}
