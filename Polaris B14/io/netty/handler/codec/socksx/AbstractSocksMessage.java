/*    */ package io.netty.handler.codec.socksx;
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
/*    */ 
/*    */ public abstract class AbstractSocksMessage
/*    */   implements SocksMessage
/*    */ {
/* 26 */   private DecoderResult decoderResult = DecoderResult.SUCCESS;
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
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\AbstractSocksMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */