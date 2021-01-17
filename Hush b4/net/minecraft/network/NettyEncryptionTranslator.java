// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network;

import javax.crypto.ShortBufferException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;
import javax.crypto.Cipher;

public class NettyEncryptionTranslator
{
    private final Cipher cipher;
    private byte[] field_150505_b;
    private byte[] field_150506_c;
    
    protected NettyEncryptionTranslator(final Cipher cipherIn) {
        this.field_150505_b = new byte[0];
        this.field_150506_c = new byte[0];
        this.cipher = cipherIn;
    }
    
    private byte[] func_150502_a(final ByteBuf p_150502_1_) {
        final int i = p_150502_1_.readableBytes();
        if (this.field_150505_b.length < i) {
            this.field_150505_b = new byte[i];
        }
        p_150502_1_.readBytes(this.field_150505_b, 0, i);
        return this.field_150505_b;
    }
    
    protected ByteBuf decipher(final ChannelHandlerContext ctx, final ByteBuf buffer) throws ShortBufferException {
        final int i = buffer.readableBytes();
        final byte[] abyte = this.func_150502_a(buffer);
        final ByteBuf bytebuf = ctx.alloc().heapBuffer(this.cipher.getOutputSize(i));
        bytebuf.writerIndex(this.cipher.update(abyte, 0, i, bytebuf.array(), bytebuf.arrayOffset()));
        return bytebuf;
    }
    
    protected void cipher(final ByteBuf p_150504_1_, final ByteBuf p_150504_2_) throws ShortBufferException {
        final int i = p_150504_1_.readableBytes();
        final byte[] abyte = this.func_150502_a(p_150504_1_);
        final int j = this.cipher.getOutputSize(i);
        if (this.field_150506_c.length < j) {
            this.field_150506_c = new byte[j];
        }
        p_150504_2_.writeBytes(this.field_150506_c, 0, this.cipher.update(abyte, 0, i, this.field_150506_c));
    }
}
