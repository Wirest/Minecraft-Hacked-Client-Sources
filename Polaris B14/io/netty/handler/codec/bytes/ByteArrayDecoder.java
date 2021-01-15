/*    */ package io.netty.handler.codec.bytes;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToMessageDecoder;
/*    */ import java.util.List;
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
/*    */ public class ByteArrayDecoder
/*    */   extends MessageToMessageDecoder<ByteBuf>
/*    */ {
/*    */   protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out)
/*    */     throws Exception
/*    */   {
/* 55 */     byte[] array = new byte[msg.readableBytes()];
/* 56 */     msg.getBytes(0, array);
/*    */     
/* 58 */     out.add(array);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\bytes\ByteArrayDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */