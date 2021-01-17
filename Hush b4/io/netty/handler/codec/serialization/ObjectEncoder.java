// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.serialization;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelHandler;
import java.io.Serializable;
import io.netty.handler.codec.MessageToByteEncoder;

@ChannelHandler.Sharable
public class ObjectEncoder extends MessageToByteEncoder<Serializable>
{
    private static final byte[] LENGTH_PLACEHOLDER;
    
    @Override
    protected void encode(final ChannelHandlerContext ctx, final Serializable msg, final ByteBuf out) throws Exception {
        final int startIdx = out.writerIndex();
        final ByteBufOutputStream bout = new ByteBufOutputStream(out);
        bout.write(ObjectEncoder.LENGTH_PLACEHOLDER);
        final ObjectOutputStream oout = new CompactObjectOutputStream(bout);
        oout.writeObject(msg);
        oout.flush();
        oout.close();
        final int endIdx = out.writerIndex();
        out.setInt(startIdx, endIdx - startIdx - 4);
    }
    
    static {
        LENGTH_PLACEHOLDER = new byte[4];
    }
}
