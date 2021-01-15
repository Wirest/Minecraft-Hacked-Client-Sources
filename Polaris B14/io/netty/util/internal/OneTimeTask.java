/*    */ package io.netty.util.internal;
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
/*    */ public abstract class OneTimeTask
/*    */   extends MpscLinkedQueueNode<Runnable>
/*    */   implements Runnable
/*    */ {
/*    */   public Runnable value()
/*    */   {
/* 30 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\OneTimeTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */