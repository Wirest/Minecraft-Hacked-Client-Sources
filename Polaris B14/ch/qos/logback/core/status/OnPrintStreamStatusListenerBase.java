/*    */ package ch.qos.logback.core.status;
/*    */ 
/*    */ import ch.qos.logback.core.Context;
/*    */ import ch.qos.logback.core.spi.ContextAwareBase;
/*    */ import ch.qos.logback.core.spi.LifeCycle;
/*    */ import ch.qos.logback.core.util.StatusPrinter;
/*    */ import java.io.PrintStream;
/*    */ import java.util.List;
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
/*    */ abstract class OnPrintStreamStatusListenerBase
/*    */   extends ContextAwareBase
/*    */   implements StatusListener, LifeCycle
/*    */ {
/* 29 */   boolean isStarted = false;
/*    */   
/*    */   static final long DEFAULT_RETROSPECTIVE = 300L;
/* 32 */   long retrospective = 300L;
/*    */   
/*    */ 
/*    */ 
/*    */   protected abstract PrintStream getPrintStream();
/*    */   
/*    */ 
/*    */ 
/*    */   private void print(Status status)
/*    */   {
/* 42 */     StringBuilder sb = new StringBuilder();
/* 43 */     StatusPrinter.buildStr(sb, "", status);
/* 44 */     getPrintStream().print(sb);
/*    */   }
/*    */   
/*    */   public void addStatusEvent(Status status) {
/* 48 */     if (!this.isStarted)
/* 49 */       return;
/* 50 */     print(status);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   private void retrospectivePrint()
/*    */   {
/* 57 */     if (this.context == null)
/* 58 */       return;
/* 59 */     long now = System.currentTimeMillis();
/* 60 */     StatusManager sm = this.context.getStatusManager();
/* 61 */     List<Status> statusList = sm.getCopyOfStatusList();
/* 62 */     for (Status status : statusList) {
/* 63 */       long timestamp = status.getDate().longValue();
/* 64 */       if (now - timestamp < this.retrospective) {
/* 65 */         print(status);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public void start() {
/* 71 */     this.isStarted = true;
/* 72 */     if (this.retrospective > 0L) {
/* 73 */       retrospectivePrint();
/*    */     }
/*    */   }
/*    */   
/*    */   public void setRetrospective(long retrospective) {
/* 78 */     this.retrospective = retrospective;
/*    */   }
/*    */   
/*    */   public long getRetrospective() {
/* 82 */     return this.retrospective;
/*    */   }
/*    */   
/*    */   public void stop() {
/* 86 */     this.isStarted = false;
/*    */   }
/*    */   
/*    */   public boolean isStarted() {
/* 90 */     return this.isStarted;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\status\OnPrintStreamStatusListenerBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */