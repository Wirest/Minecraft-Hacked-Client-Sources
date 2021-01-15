/*    */ package io.netty.util.internal;
/*    */ 
/*    */ import io.netty.util.Recycler.Handle;
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
/*    */ public abstract class RecyclableMpscLinkedQueueNode<T>
/*    */   extends MpscLinkedQueueNode<T>
/*    */ {
/*    */   private final Recycler.Handle handle;
/*    */   
/*    */   protected RecyclableMpscLinkedQueueNode(Recycler.Handle<? extends RecyclableMpscLinkedQueueNode<T>> handle)
/*    */   {
/* 30 */     if (handle == null) {
/* 31 */       throw new NullPointerException("handle");
/*    */     }
/* 33 */     this.handle = handle;
/*    */   }
/*    */   
/*    */ 
/*    */   final void unlink()
/*    */   {
/* 39 */     super.unlink();
/* 40 */     this.handle.recycle(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\RecyclableMpscLinkedQueueNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */