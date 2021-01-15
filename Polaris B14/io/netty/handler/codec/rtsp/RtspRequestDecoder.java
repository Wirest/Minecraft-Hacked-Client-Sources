/*    */ package io.netty.handler.codec.rtsp;
/*    */ 
/*    */ import io.netty.handler.codec.http.DefaultFullHttpRequest;
/*    */ import io.netty.handler.codec.http.DefaultHttpRequest;
/*    */ import io.netty.handler.codec.http.HttpMessage;
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
/*    */ public class RtspRequestDecoder
/*    */   extends RtspObjectDecoder
/*    */ {
/*    */   public RtspRequestDecoder() {}
/*    */   
/*    */   public RtspRequestDecoder(int maxInitialLineLength, int maxHeaderSize, int maxContentLength)
/*    */   {
/* 66 */     super(maxInitialLineLength, maxHeaderSize, maxContentLength);
/*    */   }
/*    */   
/*    */   public RtspRequestDecoder(int maxInitialLineLength, int maxHeaderSize, int maxContentLength, boolean validateHeaders)
/*    */   {
/* 71 */     super(maxInitialLineLength, maxHeaderSize, maxContentLength, validateHeaders);
/*    */   }
/*    */   
/*    */   protected HttpMessage createMessage(String[] initialLine) throws Exception
/*    */   {
/* 76 */     return new DefaultHttpRequest(RtspVersions.valueOf(initialLine[2]), RtspMethods.valueOf(initialLine[0]), initialLine[1], this.validateHeaders);
/*    */   }
/*    */   
/*    */ 
/*    */   protected HttpMessage createInvalidMessage()
/*    */   {
/* 82 */     return new DefaultFullHttpRequest(RtspVersions.RTSP_1_0, RtspMethods.OPTIONS, "/bad-request", this.validateHeaders);
/*    */   }
/*    */   
/*    */   protected boolean isDecodingRequest()
/*    */   {
/* 87 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\rtsp\RtspRequestDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */