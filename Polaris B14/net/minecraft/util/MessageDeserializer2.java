/*    */ package net.minecraft.util;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.Unpooled;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.ByteToMessageDecoder;
/*    */ import io.netty.handler.codec.CorruptedFrameException;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.PacketBuffer;
/*    */ 
/*    */ public class MessageDeserializer2 extends ByteToMessageDecoder
/*    */ {
/*    */   protected void decode(ChannelHandlerContext p_decode_1_, ByteBuf p_decode_2_, List<Object> p_decode_3_) throws Exception
/*    */   {
/* 15 */     p_decode_2_.markReaderIndex();
/* 16 */     byte[] abyte = new byte[3];
/*    */     
/* 18 */     for (int i = 0; i < abyte.length; i++)
/*    */     {
/* 20 */       if (!p_decode_2_.isReadable())
/*    */       {
/* 22 */         p_decode_2_.resetReaderIndex();
/* 23 */         return;
/*    */       }
/*    */       
/* 26 */       abyte[i] = p_decode_2_.readByte();
/*    */       
/* 28 */       if (abyte[i] >= 0)
/*    */       {
/* 30 */         PacketBuffer packetbuffer = new PacketBuffer(Unpooled.wrappedBuffer(abyte));
/*    */         
/*    */         try
/*    */         {
/* 34 */           int j = packetbuffer.readVarIntFromBuffer();
/*    */           
/* 36 */           if (p_decode_2_.readableBytes() >= j)
/*    */           {
/* 38 */             p_decode_3_.add(p_decode_2_.readBytes(j));
/* 39 */             return;
/*    */           }
/*    */           
/*    */ 
/*    */         }
/*    */         finally
/*    */         {
/* 46 */           packetbuffer.release(); } packetbuffer.release();
/*    */         
/*    */ 
/* 49 */         return;
/*    */       }
/*    */     }
/*    */     
/* 53 */     throw new CorruptedFrameException("length wider than 21-bit");
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\MessageDeserializer2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */