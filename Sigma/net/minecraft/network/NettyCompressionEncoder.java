package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.zip.Deflater;

public class NettyCompressionEncoder extends MessageToByteEncoder {
    private final byte[] buffer = new byte[8192];
    private final Deflater deflater;
    private int treshold;
    private static final String __OBFID = "CL_00002313";

    public NettyCompressionEncoder(int treshold) {
        this.treshold = treshold;
        deflater = new Deflater();
    }

    protected void compress(ChannelHandlerContext ctx, ByteBuf input, ByteBuf output) {
        int var4 = input.readableBytes();
        PacketBuffer var5 = new PacketBuffer(output);

        if (var4 < treshold) {
            var5.writeVarIntToBuffer(0);
            var5.writeBytes(input);
        } else {
            byte[] var6 = new byte[var4];
            input.readBytes(var6);
            var5.writeVarIntToBuffer(var6.length);
            deflater.setInput(var6, 0, var4);
            deflater.finish();

            while (!deflater.finished()) {
                int var7 = deflater.deflate(buffer);
                var5.writeBytes(buffer, 0, var7);
            }

            deflater.reset();
        }
    }

    public void setCompressionTreshold(int treshold) {
        this.treshold = treshold;
    }

    @Override
    protected void encode(ChannelHandlerContext p_encode_1_, Object p_encode_2_, ByteBuf p_encode_3_) {
        compress(p_encode_1_, (ByteBuf) p_encode_2_, p_encode_3_);
    }
}
