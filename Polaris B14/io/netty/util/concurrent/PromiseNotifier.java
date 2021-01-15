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
/*    */ 
/*    */ 
/*    */ public class PromiseNotifier<V, F extends Future<V>>
/*    */   implements GenericFutureListener<F>
/*    */ {
/*    */   private final Promise<? super V>[] promises;
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
/*    */   @SafeVarargs
/*    */   public PromiseNotifier(Promise<? super V>... promises)
/*    */   {
/* 36 */     if (promises == null) {
/* 37 */       throw new NullPointerException("promises");
/*    */     }
/* 39 */     for (Promise<? super V> promise : promises) {
/* 40 */       if (promise == null) {
/* 41 */         throw new IllegalArgumentException("promises contains null Promise");
/*    */       }
/*    */     }
/* 44 */     this.promises = ((Promise[])promises.clone());
/*    */   }
/*    */   
/*    */   public void operationComplete(F future) throws Exception
/*    */   {
/* 49 */     if (future.isSuccess()) {
/* 50 */       V result = future.get();
/* 51 */       for (Promise<? super V> p : this.promises) {
/* 52 */         p.setSuccess(result);
/*    */       }
/* 54 */       return;
/*    */     }
/*    */     
/* 57 */     Throwable cause = future.cause();
/* 58 */     for (Promise<? super V> p : this.promises) {
/* 59 */       p.setFailure(cause);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\concurrent\PromiseNotifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */