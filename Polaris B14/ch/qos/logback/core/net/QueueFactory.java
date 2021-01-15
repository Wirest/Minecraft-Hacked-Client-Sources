/*    */ package ch.qos.logback.core.net;
/*    */ 
/*    */ import java.util.concurrent.LinkedBlockingDeque;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class QueueFactory
/*    */ {
/*    */   public <E> LinkedBlockingDeque<E> newLinkedBlockingDeque(int capacity)
/*    */   {
/* 36 */     int actualCapacity = capacity < 1 ? 1 : capacity;
/* 37 */     return new LinkedBlockingDeque(actualCapacity);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\net\QueueFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */