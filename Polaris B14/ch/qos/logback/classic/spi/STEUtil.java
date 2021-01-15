/*    */ package ch.qos.logback.classic.spi;
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
/*    */ public class STEUtil
/*    */ {
/*    */   static int UNUSED_findNumberOfCommonFrames(StackTraceElement[] steArray, StackTraceElement[] otherSTEArray)
/*    */   {
/* 21 */     if (otherSTEArray == null) {
/* 22 */       return 0;
/*    */     }
/*    */     
/* 25 */     int steIndex = steArray.length - 1;
/* 26 */     int parentIndex = otherSTEArray.length - 1;
/* 27 */     int count = 0;
/* 28 */     while ((steIndex >= 0) && (parentIndex >= 0) && 
/* 29 */       (steArray[steIndex].equals(otherSTEArray[parentIndex]))) {
/* 30 */       count++;
/*    */       
/*    */ 
/*    */ 
/* 34 */       steIndex--;
/* 35 */       parentIndex--;
/*    */     }
/* 37 */     return count;
/*    */   }
/*    */   
/*    */ 
/*    */   static int findNumberOfCommonFrames(StackTraceElement[] steArray, StackTraceElementProxy[] otherSTEPArray)
/*    */   {
/* 43 */     if (otherSTEPArray == null) {
/* 44 */       return 0;
/*    */     }
/*    */     
/* 47 */     int steIndex = steArray.length - 1;
/* 48 */     int parentIndex = otherSTEPArray.length - 1;
/* 49 */     int count = 0;
/* 50 */     while ((steIndex >= 0) && (parentIndex >= 0) && 
/* 51 */       (steArray[steIndex].equals(otherSTEPArray[parentIndex].ste))) {
/* 52 */       count++;
/*    */       
/*    */ 
/*    */ 
/* 56 */       steIndex--;
/* 57 */       parentIndex--;
/*    */     }
/* 59 */     return count;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\spi\STEUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */