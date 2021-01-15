/*    */ package io.netty.util.concurrent;
/*    */ 
/*    */ import io.netty.util.internal.InternalThreadLocalMap;
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
/*    */ public class FastThreadLocalThread
/*    */   extends Thread
/*    */   implements FastThreadLocalAccess
/*    */ {
/*    */   private InternalThreadLocalMap threadLocalMap;
/*    */   
/*    */   public FastThreadLocalThread() {}
/*    */   
/*    */   public FastThreadLocalThread(Runnable target)
/*    */   {
/* 30 */     super(target);
/*    */   }
/*    */   
/*    */   public FastThreadLocalThread(ThreadGroup group, Runnable target) {
/* 34 */     super(group, target);
/*    */   }
/*    */   
/*    */   public FastThreadLocalThread(String name) {
/* 38 */     super(name);
/*    */   }
/*    */   
/*    */   public FastThreadLocalThread(ThreadGroup group, String name) {
/* 42 */     super(group, name);
/*    */   }
/*    */   
/*    */   public FastThreadLocalThread(Runnable target, String name) {
/* 46 */     super(target, name);
/*    */   }
/*    */   
/*    */   public FastThreadLocalThread(ThreadGroup group, Runnable target, String name) {
/* 50 */     super(group, target, name);
/*    */   }
/*    */   
/*    */   public FastThreadLocalThread(ThreadGroup group, Runnable target, String name, long stackSize) {
/* 54 */     super(group, target, name, stackSize);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public final InternalThreadLocalMap threadLocalMap()
/*    */   {
/* 63 */     return this.threadLocalMap;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public final void setThreadLocalMap(InternalThreadLocalMap threadLocalMap)
/*    */   {
/* 72 */     this.threadLocalMap = threadLocalMap;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\concurrent\FastThreadLocalThread.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */