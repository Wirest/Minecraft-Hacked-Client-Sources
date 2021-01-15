/*    */ package net.minecraft.network;
/*    */ 
/*    */ public final class ThreadQuickExitException extends RuntimeException
/*    */ {
/*  5 */   public static final ThreadQuickExitException field_179886_a = new ThreadQuickExitException();
/*    */   
/*    */   private ThreadQuickExitException()
/*    */   {
/*  9 */     setStackTrace(new StackTraceElement[0]);
/*    */   }
/*    */   
/*    */   public synchronized Throwable fillInStackTrace()
/*    */   {
/* 14 */     setStackTrace(new StackTraceElement[0]);
/* 15 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\ThreadQuickExitException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */