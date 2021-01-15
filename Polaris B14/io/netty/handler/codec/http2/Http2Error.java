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
/*    */ public enum Http2Error
/*    */ {
/* 22 */   NO_ERROR(0L), 
/* 23 */   PROTOCOL_ERROR(1L), 
/* 24 */   INTERNAL_ERROR(2L), 
/* 25 */   FLOW_CONTROL_ERROR(3L), 
/* 26 */   SETTINGS_TIMEOUT(4L), 
/* 27 */   STREAM_CLOSED(5L), 
/* 28 */   FRAME_SIZE_ERROR(6L), 
/* 29 */   REFUSED_STREAM(7L), 
/* 30 */   CANCEL(8L), 
/* 31 */   COMPRESSION_ERROR(9L), 
/* 32 */   CONNECT_ERROR(10L), 
/* 33 */   ENHANCE_YOUR_CALM(11L), 
/* 34 */   INADEQUATE_SECURITY(12L), 
/* 35 */   HTTP_1_1_REQUIRED(13L);
/*    */   
/*    */   private final long code;
/*    */   
/*    */   private Http2Error(long code) {
/* 40 */     this.code = code;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public long code()
/*    */   {
/* 47 */     return this.code;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\Http2Error.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */