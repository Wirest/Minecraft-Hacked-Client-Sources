/*    */ package io.netty.util.concurrent;
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
/*    */ public final class SucceededFuture<V>
/*    */   extends CompleteFuture<V>
/*    */ {
/*    */   private final V result;
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
/*    */   public SucceededFuture(EventExecutor executor, V result)
/*    */   {
/* 32 */     super(executor);
/* 33 */     this.result = result;
/*    */   }
/*    */   
/*    */   public Throwable cause()
/*    */   {
/* 38 */     return null;
/*    */   }
/*    */   
/*    */   public boolean isSuccess()
/*    */   {
/* 43 */     return true;
/*    */   }
/*    */   
/*    */   public V getNow()
/*    */   {
/* 48 */     return (V)this.result;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\concurrent\SucceededFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */