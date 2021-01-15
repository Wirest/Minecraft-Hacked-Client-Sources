/*    */ package io.netty.handler.codec.http;
/*    */ 
/*    */ import io.netty.handler.codec.DecoderResult;
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
/*    */ public class DefaultHttpObject
/*    */   implements HttpObject
/*    */ {
/*    */   private static final int HASH_CODE_PRIME = 31;
/* 22 */   private DecoderResult decoderResult = DecoderResult.SUCCESS;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public DecoderResult decoderResult()
/*    */   {
/* 30 */     return this.decoderResult;
/*    */   }
/*    */   
/*    */   public void setDecoderResult(DecoderResult decoderResult)
/*    */   {
/* 35 */     if (decoderResult == null) {
/* 36 */       throw new NullPointerException("decoderResult");
/*    */     }
/* 38 */     this.decoderResult = decoderResult;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 43 */     int result = 1;
/* 44 */     result = 31 * result + this.decoderResult.hashCode();
/* 45 */     return result;
/*    */   }
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 50 */     if (!(o instanceof DefaultHttpObject)) {
/* 51 */       return false;
/*    */     }
/*    */     
/* 54 */     DefaultHttpObject other = (DefaultHttpObject)o;
/*    */     
/* 56 */     return decoderResult().equals(other.decoderResult());
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\DefaultHttpObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */