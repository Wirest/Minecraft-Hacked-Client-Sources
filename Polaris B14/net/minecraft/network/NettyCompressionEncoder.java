/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToByteEncoder;
/*    */ import java.util.zip.Deflater;
/*    */ 
/*    */ public class NettyCompressionEncoder extends MessageToByteEncoder<ByteBuf>
/*    */ {
/* 10 */   private final byte[] buffer = new byte[' '];
/*    */   private final Deflater deflater;
/*    */   private int treshold;
/*    */   
/*    */   public NettyCompressionEncoder(int treshold)
/*    */   {
/* 16 */     this.treshold = treshold;
/* 17 */     this.deflater = new Deflater();
/*    */   }
/*    */   
/*    */   protected void encode(ChannelHandlerContext p_encode_1_, ByteBuf p_encode_2_, ByteBuf p_encode_3_) throws Exception
/*    */   {
/* 22 */     int i = p_encode_2_.readableBytes();
/* 23 */     PacketBuffer packetbuffer = new PacketBuffer(p_encode_3_);
/*    */     
/* 25 */     if (i < this.treshold)
/*    */     {
/* 27 */       packetbuffer.writeVarIntToBuffer(0);
/* 28 */       packetbuffer.writeBytes(p_encode_2_);
/*    */     }
/*    */     else
/*    */     {
/* 32 */       byte[] abyte = new byte[i];
/* 33 */       p_encode_2_.readBytes(abyte);
/* 34 */       packetbuffer.writeVarIntToBuffer(abyte.length);
/* 35 */       this.deflater.setInput(abyte, 0, i);
/* 36 */       this.deflater.finish();
/*    */       
/* 38 */       while (!this.deflater.finished())
/*    */       {
/* 40 */         int j = this.deflater.deflate(this.buffer);
/* 41 */         packetbuffer.writeBytes(this.buffer, 0, j);
/*    */       }
/*    */       
/* 44 */       this.deflater.reset();
/*    */     }
/*    */   }
/*    */   
/*    */   public void setCompressionTreshold(int treshold)
/*    */   {
/* 50 */     this.treshold = treshold;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\NettyCompressionEncoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */