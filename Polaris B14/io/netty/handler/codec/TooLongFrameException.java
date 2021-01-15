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
/*    */ public class TooLongFrameException
/*    */   extends DecoderException
/*    */ {
/*    */   private static final long serialVersionUID = -1995801950698951640L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public TooLongFrameException() {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public TooLongFrameException(String message, Throwable cause)
/*    */   {
/* 36 */     super(message, cause);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public TooLongFrameException(String message)
/*    */   {
/* 43 */     super(message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public TooLongFrameException(Throwable cause)
/*    */   {
/* 50 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\TooLongFrameException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */