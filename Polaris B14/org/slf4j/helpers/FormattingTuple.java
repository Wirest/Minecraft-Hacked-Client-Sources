/*    */ package org.slf4j.helpers;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FormattingTuple
/*    */ {
/* 35 */   public static FormattingTuple NULL = new FormattingTuple(null);
/*    */   private String message;
/*    */   private Throwable throwable;
/*    */   private Object[] argArray;
/*    */   
/*    */   public FormattingTuple(String message)
/*    */   {
/* 42 */     this(message, null, null);
/*    */   }
/*    */   
/*    */   public FormattingTuple(String message, Object[] argArray, Throwable throwable) {
/* 46 */     this.message = message;
/* 47 */     this.throwable = throwable;
/* 48 */     if (throwable == null) {
/* 49 */       this.argArray = argArray;
/*    */     } else {
/* 51 */       this.argArray = trimmedCopy(argArray);
/*    */     }
/*    */   }
/*    */   
/*    */   static Object[] trimmedCopy(Object[] argArray) {
/* 56 */     if ((argArray == null) || (argArray.length == 0)) {
/* 57 */       throw new IllegalStateException("non-sensical empty or null argument array");
/*    */     }
/* 59 */     int trimemdLen = argArray.length - 1;
/* 60 */     Object[] trimmed = new Object[trimemdLen];
/* 61 */     System.arraycopy(argArray, 0, trimmed, 0, trimemdLen);
/* 62 */     return trimmed;
/*    */   }
/*    */   
/*    */   public String getMessage() {
/* 66 */     return this.message;
/*    */   }
/*    */   
/*    */   public Object[] getArgArray() {
/* 70 */     return this.argArray;
/*    */   }
/*    */   
/*    */   public Throwable getThrowable() {
/* 74 */     return this.throwable;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\slf4j\helpers\FormattingTuple.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */