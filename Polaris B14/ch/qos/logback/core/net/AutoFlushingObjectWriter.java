/*    */ package ch.qos.logback.core.net;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectOutputStream;
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
/*    */ public class AutoFlushingObjectWriter
/*    */   implements ObjectWriter
/*    */ {
/*    */   private final ObjectOutputStream objectOutputStream;
/*    */   private final int resetFrequency;
/* 29 */   private int writeCounter = 0;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public AutoFlushingObjectWriter(ObjectOutputStream objectOutputStream, int resetFrequency)
/*    */   {
/* 39 */     this.objectOutputStream = objectOutputStream;
/* 40 */     this.resetFrequency = resetFrequency;
/*    */   }
/*    */   
/*    */   public void write(Object object) throws IOException
/*    */   {
/* 45 */     this.objectOutputStream.writeObject(object);
/* 46 */     this.objectOutputStream.flush();
/* 47 */     preventMemoryLeak();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   private void preventMemoryLeak()
/*    */     throws IOException
/*    */   {
/* 55 */     if (++this.writeCounter >= this.resetFrequency) {
/* 56 */       this.objectOutputStream.reset();
/* 57 */       this.writeCounter = 0;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\net\AutoFlushingObjectWriter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */