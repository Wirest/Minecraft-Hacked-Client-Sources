/*    */ package io.netty.handler.codec.memcache;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractMemcacheObject
/*    */   implements MemcacheObject
/*    */ {
/* 25 */   private DecoderResult decoderResult = DecoderResult.SUCCESS;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public DecoderResult decoderResult()
/*    */   {
/* 33 */     return this.decoderResult;
/*    */   }
/*    */   
/*    */   public void setDecoderResult(DecoderResult result)
/*    */   {
/* 38 */     if (result == null) {
/* 39 */       throw new NullPointerException("DecoderResult should not be null.");
/*    */     }
/*    */     
/* 42 */     this.decoderResult = result;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\memcache\AbstractMemcacheObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */