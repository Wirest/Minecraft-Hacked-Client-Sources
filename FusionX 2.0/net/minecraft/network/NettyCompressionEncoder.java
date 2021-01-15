package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.util.zip.Deflater;

public class NettyCompressionEncoder extends MessageToByteEncoder
{
    private final byte[] buffer = new byte[8192];
    private final Deflater deflater;
    private int treshold;
    private static final String __OBFID = "CL_00002313";

    public NettyCompressionEncoder(int treshold)
    {
        this.treshold = treshold;
        this.deflater = new Deflater();
    }

    protected void compress(ChannelHandlerContext ctx, ByteBuf input, ByteBuf output)
    {
        int var4 = input.readableBytes();
        PacketBuffer var5 = new PacketBuffer(output);

        if (var4 < this.treshold)
        {
            var5.writeVarIntToBuffer(0);
            var5.writeBytes(input);
        }
        else
        {
            byte[] var6 = new byte[var4];
            input.readBytes(var6);
            var5.writeVarIntToBuffer(var6.length);
            this.deflater.setInput(var6, 0, var4);
            this.deflater.finish();

            while (!this.deflater.finished())
            {
                int var7 = this.deflater.deflate(this.buffer);
                var5.writeBytes(this.buffer, 0, var7);
            }

            this.deflater.reset();
        }
    }

    public void setCompressionTreshold(int treshold)
    {
        this.treshold = treshold;
    }

    protected void encode(ChannelHandlerContext p_encode_1_, Object p_encode_2_, ByteBuf p_encode_3_)
    {
        this.compress(p_encode_1_, (ByteBuf)p_encode_2_, p_encode_3_);
    }
}
