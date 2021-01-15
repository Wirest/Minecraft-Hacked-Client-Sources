/*    */ package io.netty.handler.codec.serialization;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.ByteBufInputStream;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
/*    */ import java.io.ObjectInputStream;
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
/*    */ public class ObjectDecoder
/*    */   extends LengthFieldBasedFrameDecoder
/*    */ {
/*    */   private final ClassResolver classResolver;
/*    */   
/*    */   public ObjectDecoder(ClassResolver classResolver)
/*    */   {
/* 49 */     this(1048576, classResolver);
/*    */   }
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
/*    */   public ObjectDecoder(int maxObjectSize, ClassResolver classResolver)
/*    */   {
/* 63 */     super(maxObjectSize, 0, 4, 0, 4);
/* 64 */     this.classResolver = classResolver;
/*    */   }
/*    */   
/*    */   protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception
/*    */   {
/* 69 */     ByteBuf frame = (ByteBuf)super.decode(ctx, in);
/* 70 */     if (frame == null) {
/* 71 */       return null;
/*    */     }
/*    */     
/* 74 */     ObjectInputStream is = new CompactObjectInputStream(new ByteBufInputStream(frame), this.classResolver);
/* 75 */     Object result = is.readObject();
/* 76 */     is.close();
/* 77 */     return result;
/*    */   }
/*    */   
/*    */   protected ByteBuf extractFrame(ChannelHandlerContext ctx, ByteBuf buffer, int index, int length)
/*    */   {
/* 82 */     return buffer.slice(index, length);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\serialization\ObjectDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */