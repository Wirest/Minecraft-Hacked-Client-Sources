package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import javax.crypto.Cipher;
import javax.crypto.ShortBufferException;

public class NettyEncryptingEncoder extends MessageToByteEncoder
{
    private final NettyEncryptionTranslator encryptionCodec;
    

    public NettyEncryptingEncoder(Cipher cipher)
    {
        this.encryptionCodec = new NettyEncryptionTranslator(cipher);
    }

    protected void encode(ChannelHandlerContext p_encode_1_, ByteBuf p_encode_2_, ByteBuf p_encode_3_) throws ShortBufferException
    {
        this.encryptionCodec.cipher(p_encode_2_, p_encode_3_);
    }

    protected void encode(ChannelHandlerContext p_encode_1_, Object p_encode_2_, ByteBuf p_encode_3_) throws ShortBufferException
    {
        this.encode(p_encode_1_, (ByteBuf)p_encode_2_, p_encode_3_);
    }
    
    public static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        int i = 0;
        while (i < data.length) {
            int halfbyte = data[i] >>> 4 & 15;
            int two_halfs = 0;
            do {
                if (halfbyte >= 0 && halfbyte <= 9) {
                    buf.append((char)(48 + halfbyte));
                } else {
                    buf.append((char)(97 + (halfbyte - 10)));
                }
                halfbyte = data[i] & 15;
            } while (two_halfs++ < 1);
            ++i;
        }
        return buf.toString();
    }
}
