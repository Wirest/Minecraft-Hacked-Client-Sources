package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import javax.crypto.Cipher;
import javax.crypto.ShortBufferException;

public class NettyEncryptingEncoder extends MessageToByteEncoder {
    private final NettyEncryptionTranslator encryptionCodec;
    private static final String __OBFID = "CL_00001239";

    public NettyEncryptingEncoder(Cipher cipher) {
        this.encryptionCodec = new NettyEncryptionTranslator(cipher);
    }

    protected void encode(ChannelHandlerContext p_encode_1_, ByteBuf p_encode_2_, ByteBuf p_encode_3_) throws ShortBufferException {
        this.encryptionCodec.cipher(p_encode_2_, p_encode_3_);
    }

    protected void encode(ChannelHandlerContext p_encode_1_, Object p_encode_2_, ByteBuf p_encode_3_) throws ShortBufferException {
        this.encode(p_encode_1_, (ByteBuf) p_encode_2_, p_encode_3_);
    }
}
