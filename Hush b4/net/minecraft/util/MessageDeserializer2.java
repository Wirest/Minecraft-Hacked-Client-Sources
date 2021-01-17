// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import io.netty.handler.codec.CorruptedFrameException;
import net.minecraft.network.PacketBuffer;
import io.netty.buffer.Unpooled;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class MessageDeserializer2 extends ByteToMessageDecoder
{
    @Override
    protected void decode(final ChannelHandlerContext p_decode_1_, final ByteBuf p_decode_2_, final List<Object> p_decode_3_) throws Exception {
        p_decode_2_.markReaderIndex();
        final byte[] abyte = new byte[3];
        for (int i = 0; i < abyte.length; ++i) {
            if (!p_decode_2_.isReadable()) {
                p_decode_2_.resetReaderIndex();
                return;
            }
            abyte[i] = p_decode_2_.readByte();
            if (abyte[i] >= 0) {
                final PacketBuffer packetbuffer = new PacketBuffer(Unpooled.wrappedBuffer(abyte));
                try {
                    final int j = packetbuffer.readVarIntFromBuffer();
                    if (p_decode_2_.readableBytes() >= j) {
                        p_decode_3_.add(p_decode_2_.readBytes(j));
                        return;
                    }
                    p_decode_2_.resetReaderIndex();
                }
                finally {
                    packetbuffer.release();
                }
                packetbuffer.release();
                return;
            }
        }
        throw new CorruptedFrameException("length wider than 21-bit");
    }
}
