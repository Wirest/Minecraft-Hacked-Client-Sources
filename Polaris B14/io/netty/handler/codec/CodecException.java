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
/*    */ public class CodecException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = -1464830400709348473L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public CodecException() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public CodecException(String message, Throwable cause)
/*    */   {
/* 35 */     super(message, cause);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public CodecException(String message)
/*    */   {
/* 42 */     super(message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public CodecException(Throwable cause)
/*    */   {
/* 49 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\CodecException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */