/*    */ package io.netty.channel;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.ByteBufHolder;
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
/*    */ public final class DefaultMessageSizeEstimator
/*    */   implements MessageSizeEstimator
/*    */ {
/*    */   private static final class HandleImpl
/*    */     implements MessageSizeEstimator.Handle
/*    */   {
/*    */     private final int unknownSize;
/*    */     
/*    */     private HandleImpl(int unknownSize)
/*    */     {
/* 31 */       this.unknownSize = unknownSize;
/*    */     }
/*    */     
/*    */     public int size(Object msg)
/*    */     {
/* 36 */       if ((msg instanceof ByteBuf)) {
/* 37 */         return ((ByteBuf)msg).readableBytes();
/*    */       }
/* 39 */       if ((msg instanceof ByteBufHolder)) {
/* 40 */         return ((ByteBufHolder)msg).content().readableBytes();
/*    */       }
/* 42 */       if ((msg instanceof FileRegion)) {
/* 43 */         return 0;
/*    */       }
/* 45 */       return this.unknownSize;
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 52 */   public static final MessageSizeEstimator DEFAULT = new DefaultMessageSizeEstimator(0);
/*    */   
/*    */ 
/*    */ 
/*    */   private final MessageSizeEstimator.Handle handle;
/*    */   
/*    */ 
/*    */ 
/*    */   public DefaultMessageSizeEstimator(int unknownSize)
/*    */   {
/* 62 */     if (unknownSize < 0) {
/* 63 */       throw new IllegalArgumentException("unknownSize: " + unknownSize + " (expected: >= 0)");
/*    */     }
/* 65 */     this.handle = new HandleImpl(unknownSize, null);
/*    */   }
/*    */   
/*    */   public MessageSizeEstimator.Handle newHandle()
/*    */   {
/* 70 */     return this.handle;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\DefaultMessageSizeEstimator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */