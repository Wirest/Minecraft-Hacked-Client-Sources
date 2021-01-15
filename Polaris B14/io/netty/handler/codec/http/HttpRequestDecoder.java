/*    */ package io.netty.handler.codec.http;
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
/*    */ public class HttpRequestDecoder
/*    */   extends HttpObjectDecoder
/*    */ {
/*    */   public HttpRequestDecoder() {}
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
/*    */   public HttpRequestDecoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize)
/*    */   {
/* 70 */     super(maxInitialLineLength, maxHeaderSize, maxChunkSize, true);
/*    */   }
/*    */   
/*    */   public HttpRequestDecoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean validateHeaders)
/*    */   {
/* 75 */     super(maxInitialLineLength, maxHeaderSize, maxChunkSize, true, validateHeaders);
/*    */   }
/*    */   
/*    */   protected HttpMessage createMessage(String[] initialLine) throws Exception
/*    */   {
/* 80 */     return new DefaultHttpRequest(HttpVersion.valueOf(initialLine[2]), HttpMethod.valueOf(initialLine[0]), initialLine[1], this.validateHeaders);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected HttpMessage createInvalidMessage()
/*    */   {
/* 87 */     return new DefaultFullHttpRequest(HttpVersion.HTTP_1_0, HttpMethod.GET, "/bad-request", this.validateHeaders);
/*    */   }
/*    */   
/*    */   protected boolean isDecodingRequest()
/*    */   {
/* 92 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\HttpRequestDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */