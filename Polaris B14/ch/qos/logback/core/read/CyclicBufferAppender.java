/*    */ package ch.qos.logback.core.read;
/*    */ 
/*    */ import ch.qos.logback.core.AppenderBase;
/*    */ import ch.qos.logback.core.helpers.CyclicBuffer;
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
/*    */ public class CyclicBufferAppender<E>
/*    */   extends AppenderBase<E>
/*    */ {
/*    */   CyclicBuffer<E> cb;
/* 29 */   int maxSize = 512;
/*    */   
/*    */   public void start() {
/* 32 */     this.cb = new CyclicBuffer(this.maxSize);
/* 33 */     super.start();
/*    */   }
/*    */   
/*    */   public void stop() {
/* 37 */     this.cb = null;
/* 38 */     super.stop();
/*    */   }
/*    */   
/*    */   protected void append(E eventObject)
/*    */   {
/* 43 */     if (!isStarted()) {
/* 44 */       return;
/*    */     }
/* 46 */     this.cb.add(eventObject);
/*    */   }
/*    */   
/*    */   public int getLength() {
/* 50 */     if (isStarted()) {
/* 51 */       return this.cb.length();
/*    */     }
/* 53 */     return 0;
/*    */   }
/*    */   
/*    */   public E get(int i)
/*    */   {
/* 58 */     if (isStarted()) {
/* 59 */       return (E)this.cb.get(i);
/*    */     }
/* 61 */     return null;
/*    */   }
/*    */   
/*    */   public void reset()
/*    */   {
/* 66 */     this.cb.clear();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public int getMaxSize()
/*    */   {
/* 73 */     return this.maxSize;
/*    */   }
/*    */   
/*    */   public void setMaxSize(int maxSize) {
/* 77 */     this.maxSize = maxSize;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\read\CyclicBufferAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */