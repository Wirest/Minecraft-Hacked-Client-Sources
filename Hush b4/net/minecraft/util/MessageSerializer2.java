// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import net.minecraft.network.PacketBuffer;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageSerializer2 extends MessageToByteEncoder<ByteBuf>
{
    @Override
    protected void encode(final ChannelHandlerContext p_encode_1_, final ByteBuf p_encode_2_, final ByteBuf p_encode_3_) throws Exception {
        final int i = p_encode_2_.readableBytes();
        final int j = PacketBuffer.getVarIntSize(i);
        if (j > 3) {
            throw new IllegalArgumentException("unable to fit " + i + " into " + 3);
        }
        final PacketBuffer packetbuffer = new PacketBuffer(p_encode_3_);
        packetbuffer.ensureWritable(j + i);
        packetbuffer.writeVarIntToBuffer(i);
        packetbuffer.writeBytes(p_encode_2_, p_encode_2_.readerIndex(), i);
    }
}
