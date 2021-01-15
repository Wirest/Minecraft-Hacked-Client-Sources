/*    */ package io.netty.handler.codec.compression;
/*    */ 
/*    */ import io.netty.handler.codec.EncoderException;
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
/*    */ public class CompressionException
/*    */   extends EncoderException
/*    */ {
/*    */   private static final long serialVersionUID = 5603413481274811897L;
/*    */   
/*    */   public CompressionException() {}
/*    */   
/*    */   public CompressionException(String message, Throwable cause)
/*    */   {
/* 37 */     super(message, cause);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public CompressionException(String message)
/*    */   {
/* 44 */     super(message);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public CompressionException(Throwable cause)
/*    */   {
/* 51 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\compression\CompressionException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */