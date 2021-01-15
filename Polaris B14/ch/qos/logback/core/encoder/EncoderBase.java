/*    */ package ch.qos.logback.core.encoder;
/*    */ 
/*    */ import ch.qos.logback.core.spi.ContextAwareBase;
/*    */ import java.io.IOException;
/*    */ import java.io.OutputStream;
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
/*    */ public abstract class EncoderBase<E>
/*    */   extends ContextAwareBase
/*    */   implements Encoder<E>
/*    */ {
/*    */   protected boolean started;
/*    */   protected OutputStream outputStream;
/*    */   
/*    */   public void init(OutputStream os)
/*    */     throws IOException
/*    */   {
/* 28 */     this.outputStream = os;
/*    */   }
/*    */   
/*    */   public boolean isStarted() {
/* 32 */     return this.started;
/*    */   }
/*    */   
/*    */   public void start() {
/* 36 */     this.started = true;
/*    */   }
/*    */   
/*    */   public void stop() {
/* 40 */     this.started = false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\encoder\EncoderBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */