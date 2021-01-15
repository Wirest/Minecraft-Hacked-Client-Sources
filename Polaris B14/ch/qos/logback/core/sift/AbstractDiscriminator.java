/*    */ package ch.qos.logback.core.sift;
/*    */ 
/*    */ import ch.qos.logback.core.spi.ContextAwareBase;
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
/*    */ public abstract class AbstractDiscriminator<E>
/*    */   extends ContextAwareBase
/*    */   implements Discriminator<E>
/*    */ {
/*    */   protected boolean started;
/*    */   
/*    */   public void start()
/*    */   {
/* 29 */     this.started = true;
/*    */   }
/*    */   
/*    */   public void stop() {
/* 33 */     this.started = false;
/*    */   }
/*    */   
/*    */   public boolean isStarted() {
/* 37 */     return this.started;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\sift\AbstractDiscriminator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */