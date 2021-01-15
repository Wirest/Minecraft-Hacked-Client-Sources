package net.minecraft.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import javax.crypto.Cipher;
import javax.crypto.ShortBufferException;

public class NettyEncryptionTranslator
{
    private final Cipher cipher;
    private byte[] field_150505_b = new byte[0];
    private byte[] field_150506_c = new byte[0];
    private static final String __OBFID = "CL_00001237";

    protected NettyEncryptionTranslator(Cipher cipherIn)
    {
        this.cipher = cipherIn;
    }

    private byte[] func_150502_a(ByteBuf p_150502_1_)
    {
        int var2 = p_150502_1_.readableBytes();

        if (this.field_150505_b.length < var2)
        {
            this.field_150505_b = new byte[var2];
        }

        p_150502_1_.readBytes(this.field_150505_b, 0, var2);
        return this.field_150505_b;
    }

    protected ByteBuf decipher(ChannelHandlerContext ctx, ByteBuf buffer) throws ShortBufferException
    {
        int var3 = buffer.readableBytes();
        byte[] var4 = this.func_150502_a(buffer);
        ByteBuf var5 = ctx.alloc().heapBuffer(this.cipher.getOutputSize(var3));
        var5.writerIndex(this.cipher.update(var4, 0, var3, var5.array(), var5.arrayOffset()));
        return var5;
    }

    protected void cipher(ByteBuf p_150504_1_, ByteBuf p_150504_2_) throws ShortBufferException
    {
        int var3 = p_150504_1_.readableBytes();
        byte[] var4 = this.func_150502_a(p_150504_1_);
        int var5 = this.cipher.getOutputSize(var3);

        if (this.field_150506_c.length < var5)
        {
            this.field_150506_c = new byte[var5];
        }

        p_150504_2_.writeBytes(this.field_150506_c, 0, this.cipher.update(var4, 0, var3, this.field_150506_c));
    }
}
