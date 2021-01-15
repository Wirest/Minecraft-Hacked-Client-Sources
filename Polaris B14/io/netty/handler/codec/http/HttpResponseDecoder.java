/*     */ package io.netty.handler.codec.http;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HttpResponseDecoder
/*     */   extends HttpObjectDecoder
/*     */ {
/*  86 */   private static final HttpResponseStatus UNKNOWN_STATUS = new HttpResponseStatus(999, "Unknown");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpResponseDecoder() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HttpResponseDecoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize)
/*     */   {
/* 101 */     super(maxInitialLineLength, maxHeaderSize, maxChunkSize, true);
/*     */   }
/*     */   
/*     */   public HttpResponseDecoder(int maxInitialLineLength, int maxHeaderSize, int maxChunkSize, boolean validateHeaders)
/*     */   {
/* 106 */     super(maxInitialLineLength, maxHeaderSize, maxChunkSize, true, validateHeaders);
/*     */   }
/*     */   
/*     */   protected HttpMessage createMessage(String[] initialLine)
/*     */   {
/* 111 */     return new DefaultHttpResponse(HttpVersion.valueOf(initialLine[0]), new HttpResponseStatus(Integer.parseInt(initialLine[1]), initialLine[2]), this.validateHeaders);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected HttpMessage createInvalidMessage()
/*     */   {
/* 118 */     return new DefaultFullHttpResponse(HttpVersion.HTTP_1_0, UNKNOWN_STATUS, this.validateHeaders);
/*     */   }
/*     */   
/*     */   protected boolean isDecodingRequest()
/*     */   {
/* 123 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\HttpResponseDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */