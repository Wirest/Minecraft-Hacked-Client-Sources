// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network;

import javax.crypto.ShortBufferException;
import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import javax.crypto.Cipher;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToMessageDecoder;

public class NettyEncryptingDecoder extends MessageToMessageDecoder<ByteBuf>
{
    private final NettyEncryptionTranslator decryptionCodec;
    
    public NettyEncryptingDecoder(final Cipher cipher) {
        this.decryptionCodec = new NettyEncryptionTranslator(cipher);
    }
    
    @Override
    protected void decode(final ChannelHandlerContext p_decode_1_, final ByteBuf p_decode_2_, final List<Object> p_decode_3_) throws ShortBufferException, Exception {
        p_decode_3_.add(this.decryptionCodec.decipher(p_decode_1_, p_decode_2_));
    }
}
