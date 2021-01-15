/*    */ package ch.qos.logback.core.spi;
/*    */ 
/*    */ import ch.qos.logback.core.Context;
/*    */ import ch.qos.logback.core.status.ErrorStatus;
/*    */ import ch.qos.logback.core.status.InfoStatus;
/*    */ import ch.qos.logback.core.status.Status;
/*    */ import ch.qos.logback.core.status.StatusManager;
/*    */ import ch.qos.logback.core.status.WarnStatus;
/*    */ import java.io.PrintStream;
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
/*    */ public class ContextAwareImpl
/*    */   implements ContextAware
/*    */ {
/* 32 */   private int noContextWarning = 0;
/*    */   protected Context context;
/*    */   final Object origin;
/*    */   
/*    */   public ContextAwareImpl(Context context, Object origin) {
/* 37 */     this.context = context;
/* 38 */     this.origin = origin;
/*    */   }
/*    */   
/*    */   protected Object getOrigin()
/*    */   {
/* 43 */     return this.origin;
/*    */   }
/*    */   
/*    */   public void setContext(Context context) {
/* 47 */     if (this.context == null) {
/* 48 */       this.context = context;
/* 49 */     } else if (this.context != context) {
/* 50 */       throw new IllegalStateException("Context has been already set");
/*    */     }
/*    */   }
/*    */   
/*    */   public Context getContext() {
/* 55 */     return this.context;
/*    */   }
/*    */   
/*    */   public StatusManager getStatusManager() {
/* 59 */     if (this.context == null) {
/* 60 */       return null;
/*    */     }
/* 62 */     return this.context.getStatusManager();
/*    */   }
/*    */   
/*    */   public void addStatus(Status status) {
/* 66 */     if (this.context == null) {
/* 67 */       if (this.noContextWarning++ == 0) {
/* 68 */         System.out.println("LOGBACK: No context given for " + this);
/*    */       }
/* 70 */       return;
/*    */     }
/* 72 */     StatusManager sm = this.context.getStatusManager();
/* 73 */     if (sm != null) {
/* 74 */       sm.add(status);
/*    */     }
/*    */   }
/*    */   
/*    */   public void addInfo(String msg) {
/* 79 */     addStatus(new InfoStatus(msg, getOrigin()));
/*    */   }
/*    */   
/*    */   public void addInfo(String msg, Throwable ex) {
/* 83 */     addStatus(new InfoStatus(msg, getOrigin(), ex));
/*    */   }
/*    */   
/*    */   public void addWarn(String msg) {
/* 87 */     addStatus(new WarnStatus(msg, getOrigin()));
/*    */   }
/*    */   
/*    */   public void addWarn(String msg, Throwable ex) {
/* 91 */     addStatus(new WarnStatus(msg, getOrigin(), ex));
/*    */   }
/*    */   
/*    */   public void addError(String msg) {
/* 95 */     addStatus(new ErrorStatus(msg, getOrigin()));
/*    */   }
/*    */   
/*    */   public void addError(String msg, Throwable ex) {
/* 99 */     addStatus(new ErrorStatus(msg, getOrigin(), ex));
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\spi\ContextAwareImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */