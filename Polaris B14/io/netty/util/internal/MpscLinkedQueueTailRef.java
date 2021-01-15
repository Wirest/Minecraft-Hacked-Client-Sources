/*    */ package io.netty.util.internal;
/*    */ 
/*    */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
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
/*    */ abstract class MpscLinkedQueueTailRef<E>
/*    */   extends MpscLinkedQueuePad1<E>
/*    */ {
/*    */   private static final long serialVersionUID = 8717072462993327429L;
/*    */   private static final AtomicReferenceFieldUpdater<MpscLinkedQueueTailRef, MpscLinkedQueueNode> UPDATER;
/*    */   private volatile transient MpscLinkedQueueNode<E> tailRef;
/*    */   
/*    */   static
/*    */   {
/* 31 */     AtomicReferenceFieldUpdater<MpscLinkedQueueTailRef, MpscLinkedQueueNode> updater = PlatformDependent.newAtomicReferenceFieldUpdater(MpscLinkedQueueTailRef.class, "tailRef");
/* 32 */     if (updater == null) {
/* 33 */       updater = AtomicReferenceFieldUpdater.newUpdater(MpscLinkedQueueTailRef.class, MpscLinkedQueueNode.class, "tailRef");
/*    */     }
/*    */     
/* 36 */     UPDATER = updater;
/*    */   }
/*    */   
/*    */ 
/*    */   protected final MpscLinkedQueueNode<E> tailRef()
/*    */   {
/* 42 */     return this.tailRef;
/*    */   }
/*    */   
/*    */   protected final void setTailRef(MpscLinkedQueueNode<E> tailRef) {
/* 46 */     this.tailRef = tailRef;
/*    */   }
/*    */   
/*    */ 
/*    */   protected final MpscLinkedQueueNode<E> getAndSetTailRef(MpscLinkedQueueNode<E> tailRef)
/*    */   {
/* 52 */     return (MpscLinkedQueueNode)UPDATER.getAndSet(this, tailRef);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\MpscLinkedQueueTailRef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */