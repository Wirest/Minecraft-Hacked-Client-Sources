/*    */ package io.netty.handler.codec;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
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
/*    */ public class FixedLengthFrameDecoder
/*    */   extends ByteToMessageDecoder
/*    */ {
/*    */   private final int frameLength;
/*    */   
/*    */   public FixedLengthFrameDecoder(int frameLength)
/*    */   {
/* 49 */     if (frameLength <= 0) {
/* 50 */       throw new IllegalArgumentException("frameLength must be a positive integer: " + frameLength);
/*    */     }
/*    */     
/* 53 */     this.frameLength = frameLength;
/*    */   }
/*    */   
/*    */   protected final void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception
/*    */   {
/* 58 */     Object decoded = decode(ctx, in);
/* 59 */     if (decoded != null) {
/* 60 */       out.add(decoded);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected Object decode(ChannelHandlerContext ctx, ByteBuf in)
/*    */     throws Exception
/*    */   {
/* 74 */     if (in.readableBytes() < this.frameLength) {
/* 75 */       return null;
/*    */     }
/* 77 */     return in.readSlice(this.frameLength).retain();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\FixedLengthFrameDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */