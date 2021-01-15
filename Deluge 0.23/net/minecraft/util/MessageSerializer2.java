package net.minecraft.util;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.minecraft.network.PacketBuffer;

public class MessageSerializer2 extends MessageToByteEncoder
{
    private static final String __OBFID = "CL_00001256";

    protected void encode(ChannelHandlerContext p_encode_1_, ByteBuf p_encode_2_, ByteBuf p_encode_3_)
    {
        int var4 = p_encode_2_.readableBytes();
        int var5 = PacketBuffer.getVarIntSize(var4);

        if (var5 > 3)
        {
            throw new IllegalArgumentException("unable to fit " + var4 + " into " + 3);
        }
        else
        {
            PacketBuffer var6 = new PacketBuffer(p_encode_3_);
            var6.ensureWritable(var5 + var4);
            var6.writeVarIntToBuffer(var4);
            var6.writeBytes(p_encode_2_, p_encode_2_.readerIndex(), var4);
        }
    }

    protected void encode(ChannelHandlerContext p_encode_1_, Object p_encode_2_, ByteBuf p_encode_3_)
    {
        this.encode(p_encode_1_, (ByteBuf)p_encode_2_, p_encode_3_);
    }
}
