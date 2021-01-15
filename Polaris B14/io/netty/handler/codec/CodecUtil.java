/*    */ package io.netty.handler.codec;
/*    */ 
/*    */ import io.netty.channel.ChannelHandlerAdapter;
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
/*    */ final class CodecUtil
/*    */ {
/*    */   static void ensureNotSharable(ChannelHandlerAdapter handler)
/*    */   {
/* 26 */     if (handler.isSharable()) {
/* 27 */       throw new IllegalStateException("@Sharable annotation is not allowed");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\CodecUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */