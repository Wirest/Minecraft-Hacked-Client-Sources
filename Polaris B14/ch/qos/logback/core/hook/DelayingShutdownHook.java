/*    */ package ch.qos.logback.core.hook;
/*    */ 
/*    */ import ch.qos.logback.core.util.Duration;
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
/*    */ 
/*    */ public class DelayingShutdownHook
/*    */   extends ShutdownHookBase
/*    */ {
/* 30 */   public static final Duration DEFAULT_DELAY = Duration.buildByMilliseconds(0.0D);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 35 */   private Duration delay = DEFAULT_DELAY;
/*    */   
/*    */ 
/*    */ 
/*    */   public Duration getDelay()
/*    */   {
/* 41 */     return this.delay;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setDelay(Duration delay)
/*    */   {
/* 50 */     this.delay = delay;
/*    */   }
/*    */   
/*    */   public void run() {
/*    */     try {
/* 55 */       Thread.sleep(this.delay.getMilliseconds());
/*    */     }
/*    */     catch (InterruptedException e) {}
/* 58 */     super.stop();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\hook\DelayingShutdownHook.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */