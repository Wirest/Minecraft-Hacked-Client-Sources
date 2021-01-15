/*    */ package io.netty.util.internal.logging;
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
/*    */ class FormattingTuple
/*    */ {
/* 47 */   static final FormattingTuple NULL = new FormattingTuple(null);
/*    */   private final String message;
/*    */   private final Throwable throwable;
/*    */   private final Object[] argArray;
/*    */   
/*    */   FormattingTuple(String message)
/*    */   {
/* 54 */     this(message, null, null);
/*    */   }
/*    */   
/*    */   FormattingTuple(String message, Object[] argArray, Throwable throwable) {
/* 58 */     this.message = message;
/* 59 */     this.throwable = throwable;
/* 60 */     if (throwable == null) {
/* 61 */       this.argArray = argArray;
/*    */     } else {
/* 63 */       this.argArray = trimmedCopy(argArray);
/*    */     }
/*    */   }
/*    */   
/*    */   static Object[] trimmedCopy(Object[] argArray) {
/* 68 */     if ((argArray == null) || (argArray.length == 0)) {
/* 69 */       throw new IllegalStateException("non-sensical empty or null argument array");
/*    */     }
/* 71 */     int trimemdLen = argArray.length - 1;
/* 72 */     Object[] trimmed = new Object[trimemdLen];
/* 73 */     System.arraycopy(argArray, 0, trimmed, 0, trimemdLen);
/* 74 */     return trimmed;
/*    */   }
/*    */   
/*    */   public String getMessage() {
/* 78 */     return this.message;
/*    */   }
/*    */   
/*    */   public Object[] getArgArray() {
/* 82 */     return this.argArray;
/*    */   }
/*    */   
/*    */   public Throwable getThrowable() {
/* 86 */     return this.throwable;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\logging\FormattingTuple.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */