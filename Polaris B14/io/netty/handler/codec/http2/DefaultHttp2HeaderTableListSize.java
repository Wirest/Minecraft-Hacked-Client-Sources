/*    */ package io.netty.handler.codec.http2;
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
/*    */ class DefaultHttp2HeaderTableListSize
/*    */ {
/* 24 */   private int maxHeaderListSize = Integer.MAX_VALUE;
/*    */   
/*    */   public void maxHeaderListSize(int max) throws Http2Exception {
/* 27 */     if (max < 0) {
/* 28 */       throw Http2Exception.connectionError(Http2Error.PROTOCOL_ERROR, "Header List Size must be non-negative but was %d", new Object[] { Integer.valueOf(max) });
/*    */     }
/* 30 */     this.maxHeaderListSize = max;
/*    */   }
/*    */   
/*    */   public int maxHeaderListSize() {
/* 34 */     return this.maxHeaderListSize;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\DefaultHttp2HeaderTableListSize.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */