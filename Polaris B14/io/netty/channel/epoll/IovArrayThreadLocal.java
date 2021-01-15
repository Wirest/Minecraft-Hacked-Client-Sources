/*    */ package io.netty.channel.epoll;
/*    */ 
/*    */ import io.netty.buffer.CompositeByteBuf;
/*    */ import io.netty.channel.ChannelOutboundBuffer;
/*    */ import io.netty.util.concurrent.FastThreadLocal;
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
/*    */ final class IovArrayThreadLocal
/*    */ {
/* 27 */   private static final FastThreadLocal<IovArray> ARRAY = new FastThreadLocal()
/*    */   {
/*    */     protected IovArray initialValue() throws Exception {
/* 30 */       return new IovArray();
/*    */     }
/*    */     
/*    */     protected void onRemoval(IovArray value)
/*    */       throws Exception
/*    */     {
/* 36 */       value.release();
/*    */     }
/*    */   };
/*    */   
/*    */ 
/*    */   static IovArray get(ChannelOutboundBuffer buffer)
/*    */     throws Exception
/*    */   {
/* 44 */     IovArray array = (IovArray)ARRAY.get();
/* 45 */     array.clear();
/* 46 */     buffer.forEachFlushedMessage(array);
/* 47 */     return array;
/*    */   }
/*    */   
/*    */ 
/*    */   static IovArray get(CompositeByteBuf buf)
/*    */     throws Exception
/*    */   {
/* 54 */     IovArray array = (IovArray)ARRAY.get();
/* 55 */     array.clear();
/* 56 */     array.add(buf);
/* 57 */     return array;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\epoll\IovArrayThreadLocal.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */