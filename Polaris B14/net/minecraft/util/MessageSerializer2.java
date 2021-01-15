/*    */ package net.minecraft.util;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToByteEncoder;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ 
/*    */ public class MessageSerializer2 extends MessageToByteEncoder<ByteBuf>
/*    */ {
/*    */   protected void encode(ChannelHandlerContext p_encode_1_, ByteBuf p_encode_2_, ByteBuf p_encode_3_) throws Exception
/*    */   {
/* 12 */     int i = p_encode_2_.readableBytes();
/* 13 */     int j = PacketBuffer.getVarIntSize(i);
/*    */     
/* 15 */     if (j > 3)
/*    */     {
/* 17 */       throw new IllegalArgumentException("unable to fit " + i + " into " + 3);
/*    */     }
/*    */     
/*    */ 
/* 21 */     PacketBuffer packetbuffer = new PacketBuffer(p_encode_3_);
/* 22 */     packetbuffer.ensureWritable(j + i);
/* 23 */     packetbuffer.writeVarIntToBuffer(i);
/* 24 */     packetbuffer.writeBytes(p_encode_2_, p_encode_2_.readerIndex(), i);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\MessageSerializer2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */