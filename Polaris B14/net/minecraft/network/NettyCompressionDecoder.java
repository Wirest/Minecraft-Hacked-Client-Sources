/*    */ package net.minecraft.network;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.Unpooled;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.ByteToMessageDecoder;
/*    */ import io.netty.handler.codec.DecoderException;
/*    */ import java.util.List;
/*    */ import java.util.zip.DataFormatException;
/*    */ import java.util.zip.Inflater;
/*    */ 
/*    */ public class NettyCompressionDecoder extends ByteToMessageDecoder
/*    */ {
/*    */   private final Inflater inflater;
/*    */   private int treshold;
/*    */   
/*    */   public NettyCompressionDecoder(int treshold)
/*    */   {
/* 19 */     this.treshold = treshold;
/* 20 */     this.inflater = new Inflater();
/*    */   }
/*    */   
/*    */   protected void decode(ChannelHandlerContext p_decode_1_, ByteBuf p_decode_2_, List<Object> p_decode_3_) throws DataFormatException, Exception
/*    */   {
/* 25 */     if (p_decode_2_.readableBytes() != 0)
/*    */     {
/* 27 */       PacketBuffer packetbuffer = new PacketBuffer(p_decode_2_);
/* 28 */       int i = packetbuffer.readVarIntFromBuffer();
/*    */       
/* 30 */       if (i == 0)
/*    */       {
/* 32 */         p_decode_3_.add(packetbuffer.readBytes(packetbuffer.readableBytes()));
/*    */       }
/*    */       else
/*    */       {
/* 36 */         if (i < this.treshold)
/*    */         {
/* 38 */           throw new DecoderException("Badly compressed packet - size of " + i + " is below server threshold of " + this.treshold);
/*    */         }
/*    */         
/* 41 */         if (i > 2097152)
/*    */         {
/* 43 */           throw new DecoderException("Badly compressed packet - size of " + i + " is larger than protocol maximum of " + 2097152);
/*    */         }
/*    */         
/* 46 */         byte[] abyte = new byte[packetbuffer.readableBytes()];
/* 47 */         packetbuffer.readBytes(abyte);
/* 48 */         this.inflater.setInput(abyte);
/* 49 */         byte[] abyte1 = new byte[i];
/* 50 */         this.inflater.inflate(abyte1);
/* 51 */         p_decode_3_.add(Unpooled.wrappedBuffer(abyte1));
/* 52 */         this.inflater.reset();
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public void setCompressionTreshold(int treshold)
/*    */   {
/* 59 */     this.treshold = treshold;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\NettyCompressionDecoder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */