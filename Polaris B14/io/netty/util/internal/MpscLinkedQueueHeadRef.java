/*    */ package io.netty.util.internal;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ abstract class MpscLinkedQueueHeadRef<E>
/*    */   extends MpscLinkedQueuePad0<E>
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 8467054865577874285L;
/*    */   private static final AtomicReferenceFieldUpdater<MpscLinkedQueueHeadRef, MpscLinkedQueueNode> UPDATER;
/*    */   private volatile transient MpscLinkedQueueNode<E> headRef;
/*    */   
/*    */   static
/*    */   {
/* 33 */     AtomicReferenceFieldUpdater<MpscLinkedQueueHeadRef, MpscLinkedQueueNode> updater = PlatformDependent.newAtomicReferenceFieldUpdater(MpscLinkedQueueHeadRef.class, "headRef");
/* 34 */     if (updater == null) {
/* 35 */       updater = AtomicReferenceFieldUpdater.newUpdater(MpscLinkedQueueHeadRef.class, MpscLinkedQueueNode.class, "headRef");
/*    */     }
/*    */     
/* 38 */     UPDATER = updater;
/*    */   }
/*    */   
/*    */ 
/*    */   protected final MpscLinkedQueueNode<E> headRef()
/*    */   {
/* 44 */     return this.headRef;
/*    */   }
/*    */   
/*    */   protected final void setHeadRef(MpscLinkedQueueNode<E> headRef) {
/* 48 */     this.headRef = headRef;
/*    */   }
/*    */   
/*    */   protected final void lazySetHeadRef(MpscLinkedQueueNode<E> headRef) {
/* 52 */     UPDATER.lazySet(this, headRef);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\MpscLinkedQueueHeadRef.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */