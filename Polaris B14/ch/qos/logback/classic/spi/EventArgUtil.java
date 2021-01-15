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
/*    */ public class EventArgUtil
/*    */ {
/*    */   public static final Throwable extractThrowable(Object[] argArray)
/*    */   {
/* 20 */     if ((argArray == null) || (argArray.length == 0)) {
/* 21 */       return null;
/*    */     }
/*    */     
/* 24 */     Object lastEntry = argArray[(argArray.length - 1)];
/* 25 */     if ((lastEntry instanceof Throwable)) {
/* 26 */       return (Throwable)lastEntry;
/*    */     }
/* 28 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Object[] trimmedCopy(Object[] argArray)
/*    */   {
/* 38 */     if ((argArray == null) || (argArray.length == 0)) {
/* 39 */       throw new IllegalStateException("non-sensical empty or null argument array");
/*    */     }
/* 41 */     int trimemdLen = argArray.length - 1;
/* 42 */     Object[] trimmed = new Object[trimemdLen];
/* 43 */     System.arraycopy(argArray, 0, trimmed, 0, trimemdLen);
/* 44 */     return trimmed;
/*    */   }
/*    */   
/*    */   public static Object[] arrangeArguments(Object[] argArray) {
/* 48 */     return argArray;
/*    */   }
/*    */   
/*    */   public static boolean successfulExtraction(Throwable throwable) {
/* 52 */     return throwable != null;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\spi\EventArgUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */