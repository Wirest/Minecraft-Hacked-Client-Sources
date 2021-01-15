/*    */ package io.netty.handler.codec;
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
/*    */ public class UnsupportedMessageTypeException
/*    */   extends CodecException
/*    */ {
/*    */   private static final long serialVersionUID = 2799598826487038726L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public UnsupportedMessageTypeException(Object message, Class<?>... expectedTypes)
/*    */   {
/* 27 */     super(message(message == null ? "null" : message.getClass().getName(), expectedTypes));
/*    */   }
/*    */   
/*    */   public UnsupportedMessageTypeException() {}
/*    */   
/*    */   public UnsupportedMessageTypeException(String message, Throwable cause)
/*    */   {
/* 34 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public UnsupportedMessageTypeException(String s) {
/* 38 */     super(s);
/*    */   }
/*    */   
/*    */   public UnsupportedMessageTypeException(Throwable cause) {
/* 42 */     super(cause);
/*    */   }
/*    */   
/*    */   private static String message(String actualType, Class<?>... expectedTypes)
/*    */   {
/* 47 */     StringBuilder buf = new StringBuilder(actualType);
/*    */     
/* 49 */     if ((expectedTypes != null) && (expectedTypes.length > 0)) {
/* 50 */       buf.append(" (expected: ").append(expectedTypes[0].getName());
/* 51 */       for (int i = 1; i < expectedTypes.length; i++) {
/* 52 */         Class<?> t = expectedTypes[i];
/* 53 */         if (t == null) {
/*    */           break;
/*    */         }
/* 56 */         buf.append(", ").append(t.getName());
/*    */       }
/* 58 */       buf.append(')');
/*    */     }
/*    */     
/* 61 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\UnsupportedMessageTypeException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */