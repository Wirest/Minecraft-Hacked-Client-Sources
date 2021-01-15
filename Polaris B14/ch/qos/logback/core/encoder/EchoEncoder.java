/*    */ package ch.qos.logback.core.encoder;
/*    */ 
/*    */ import ch.qos.logback.core.CoreConstants;
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
/*    */ public class EchoEncoder<E>
/*    */   extends EncoderBase<E>
/*    */ {
/*    */   String fileHeader;
/*    */   String fileFooter;
/*    */   
/*    */   public void doEncode(E event)
/*    */     throws IOException
/*    */   {
/* 27 */     String val = event + CoreConstants.LINE_SEPARATOR;
/* 28 */     this.outputStream.write(val.getBytes());
/*    */     
/* 30 */     this.outputStream.flush();
/*    */   }
/*    */   
/*    */   public void close() throws IOException {
/* 34 */     if (this.fileFooter == null) {
/* 35 */       return;
/*    */     }
/* 37 */     this.outputStream.write(this.fileFooter.getBytes());
/*    */   }
/*    */   
/*    */   public void init(OutputStream os) throws IOException {
/* 41 */     super.init(os);
/* 42 */     if (this.fileHeader == null) {
/* 43 */       return;
/*    */     }
/* 45 */     this.outputStream.write(this.fileHeader.getBytes());
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\encoder\EchoEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */