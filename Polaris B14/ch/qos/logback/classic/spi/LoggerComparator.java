/*    */ package ch.qos.logback.classic.spi;
/*    */ 
/*    */ import ch.qos.logback.classic.Logger;
/*    */ import java.util.Comparator;
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
/*    */ public class LoggerComparator
/*    */   implements Comparator<Logger>
/*    */ {
/*    */   public int compare(Logger l1, Logger l2)
/*    */   {
/* 23 */     if (l1.getName().equals(l2.getName())) {
/* 24 */       return 0;
/*    */     }
/* 26 */     if (l1.getName().equals("ROOT")) {
/* 27 */       return -1;
/*    */     }
/* 29 */     if (l2.getName().equals("ROOT")) {
/* 30 */       return 1;
/*    */     }
/* 32 */     return l1.getName().compareTo(l2.getName());
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\spi\LoggerComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */