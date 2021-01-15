/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import javax.crypto.Cipher;
/*    */ import javax.crypto.ShortBufferException;
/*    */ 
/*    */ public class NettyEncryptionTranslator
/*    */ {
/*    */   private final Cipher cipher;
/* 11 */   private byte[] field_150505_b = new byte[0];
/* 12 */   private byte[] field_150506_c = new byte[0];
/*    */   
/*    */   protected NettyEncryptionTranslator(Cipher cipherIn)
/*    */   {
/* 16 */     this.cipher = cipherIn;
/*    */   }
/*    */   
/*    */   private byte[] func_150502_a(ByteBuf p_150502_1_)
/*    */   {
/* 21 */     int i = p_150502_1_.readableBytes();
/*    */     
/* 23 */     if (this.field_150505_b.length < i)
/*    */     {
/* 25 */       this.field_150505_b = new byte[i];
/*    */     }
/*    */     
/* 28 */     p_150502_1_.readBytes(this.field_150505_b, 0, i);
/* 29 */     return this.field_150505_b;
/*    */   }
/*    */   
/*    */   protected ByteBuf decipher(ChannelHandlerContext ctx, ByteBuf buffer) throws ShortBufferException
/*    */   {
/* 34 */     int i = buffer.readableBytes();
/* 35 */     byte[] abyte = func_150502_a(buffer);
/* 36 */     ByteBuf bytebuf = ctx.alloc().heapBuffer(this.cipher.getOutputSize(i));
/* 37 */     bytebuf.writerIndex(this.cipher.update(abyte, 0, i, bytebuf.array(), bytebuf.arrayOffset()));
/* 38 */     return bytebuf;
/*    */   }
/*    */   
/*    */   protected void cipher(ByteBuf p_150504_1_, ByteBuf p_150504_2_) throws ShortBufferException
/*    */   {
/* 43 */     int i = p_150504_1_.readableBytes();
/* 44 */     byte[] abyte = func_150502_a(p_150504_1_);
/* 45 */     int j = this.cipher.getOutputSize(i);
/*    */     
/* 47 */     if (this.field_150506_c.length < j)
/*    */     {
/* 49 */       this.field_150506_c = new byte[j];
/*    */     }
/*    */     
/* 52 */     p_150504_2_.writeBytes(this.field_150506_c, 0, this.cipher.update(abyte, 0, i, this.field_150506_c));
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\NettyEncryptionTranslator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */