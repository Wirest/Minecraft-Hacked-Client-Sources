/*    */ package io.netty.handler.codec.serialization;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.ByteBufOutputStream;
/*    */ import io.netty.channel.ChannelHandler.Sharable;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToByteEncoder;
/*    */ import java.io.ObjectOutputStream;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ChannelHandler.Sharable
/*    */ public class ObjectEncoder
/*    */   extends MessageToByteEncoder<Serializable>
/*    */ {
/* 38 */   private static final byte[] LENGTH_PLACEHOLDER = new byte[4];
/*    */   
/*    */   protected void encode(ChannelHandlerContext ctx, Serializable msg, ByteBuf out) throws Exception
/*    */   {
/* 42 */     int startIdx = out.writerIndex();
/*    */     
/* 44 */     ByteBufOutputStream bout = new ByteBufOutputStream(out);
/* 45 */     bout.write(LENGTH_PLACEHOLDER);
/* 46 */     ObjectOutputStream oout = new CompactObjectOutputStream(bout);
/* 47 */     oout.writeObject(msg);
/* 48 */     oout.flush();
/* 49 */     oout.close();
/*    */     
/* 51 */     int endIdx = out.writerIndex();
/*    */     
/* 53 */     out.setInt(startIdx, endIdx - startIdx - 4);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\serialization\ObjectEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */