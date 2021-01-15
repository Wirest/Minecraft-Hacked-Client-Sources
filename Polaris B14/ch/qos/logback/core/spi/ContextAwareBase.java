/*     */ package ch.qos.logback.core.spi;
/*     */ 
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.status.ErrorStatus;
/*     */ import ch.qos.logback.core.status.InfoStatus;
/*     */ import ch.qos.logback.core.status.Status;
/*     */ import ch.qos.logback.core.status.StatusManager;
/*     */ import ch.qos.logback.core.status.WarnStatus;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContextAwareBase
/*     */   implements ContextAware
/*     */ {
/*  30 */   private int noContextWarning = 0;
/*     */   protected Context context;
/*     */   final Object declaredOrigin;
/*     */   
/*     */   public ContextAwareBase() {
/*  35 */     this.declaredOrigin = this;
/*     */   }
/*     */   
/*  38 */   public ContextAwareBase(ContextAware declaredOrigin) { this.declaredOrigin = declaredOrigin; }
/*     */   
/*     */   public void setContext(Context context)
/*     */   {
/*  42 */     if (this.context == null) {
/*  43 */       this.context = context;
/*  44 */     } else if (this.context != context) {
/*  45 */       throw new IllegalStateException("Context has been already set");
/*     */     }
/*     */   }
/*     */   
/*     */   public Context getContext() {
/*  50 */     return this.context;
/*     */   }
/*     */   
/*     */   public StatusManager getStatusManager() {
/*  54 */     if (this.context == null) {
/*  55 */       return null;
/*     */     }
/*  57 */     return this.context.getStatusManager();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object getDeclaredOrigin()
/*     */   {
/*  67 */     return this.declaredOrigin;
/*     */   }
/*     */   
/*     */   public void addStatus(Status status) {
/*  71 */     if (this.context == null) {
/*  72 */       if (this.noContextWarning++ == 0) {
/*  73 */         System.out.println("LOGBACK: No context given for " + this);
/*     */       }
/*  75 */       return;
/*     */     }
/*  77 */     StatusManager sm = this.context.getStatusManager();
/*  78 */     if (sm != null) {
/*  79 */       sm.add(status);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addInfo(String msg) {
/*  84 */     addStatus(new InfoStatus(msg, getDeclaredOrigin()));
/*     */   }
/*     */   
/*     */   public void addInfo(String msg, Throwable ex) {
/*  88 */     addStatus(new InfoStatus(msg, getDeclaredOrigin(), ex));
/*     */   }
/*     */   
/*     */   public void addWarn(String msg) {
/*  92 */     addStatus(new WarnStatus(msg, getDeclaredOrigin()));
/*     */   }
/*     */   
/*     */   public void addWarn(String msg, Throwable ex) {
/*  96 */     addStatus(new WarnStatus(msg, getDeclaredOrigin(), ex));
/*     */   }
/*     */   
/*     */   public void addError(String msg) {
/* 100 */     addStatus(new ErrorStatus(msg, getDeclaredOrigin()));
/*     */   }
/*     */   
/*     */   public void addError(String msg, Throwable ex) {
/* 104 */     addStatus(new ErrorStatus(msg, getDeclaredOrigin(), ex));
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\spi\ContextAwareBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */