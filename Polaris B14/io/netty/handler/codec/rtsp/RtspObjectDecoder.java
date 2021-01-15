/*    */ package io.netty.handler.codec.rtsp;
/*    */ 
/*    */ import io.netty.handler.codec.http.HttpHeaders;
/*    */ import io.netty.handler.codec.http.HttpMessage;
/*    */ import io.netty.handler.codec.http.HttpObjectDecoder;
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
/*    */ public abstract class RtspObjectDecoder
/*    */   extends HttpObjectDecoder
/*    */ {
/*    */   protected RtspObjectDecoder()
/*    */   {
/* 59 */     this(4096, 8192, 8192);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected RtspObjectDecoder(int maxInitialLineLength, int maxHeaderSize, int maxContentLength)
/*    */   {
/* 66 */     super(maxInitialLineLength, maxHeaderSize, maxContentLength * 2, false);
/*    */   }
/*    */   
/*    */   protected RtspObjectDecoder(int maxInitialLineLength, int maxHeaderSize, int maxContentLength, boolean validateHeaders)
/*    */   {
/* 71 */     super(maxInitialLineLength, maxHeaderSize, maxContentLength * 2, false, validateHeaders);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected boolean isContentAlwaysEmpty(HttpMessage msg)
/*    */   {
/* 78 */     boolean empty = super.isContentAlwaysEmpty(msg);
/* 79 */     if (empty) {
/* 80 */       return true;
/*    */     }
/* 82 */     if (!msg.headers().contains(RtspHeaderNames.CONTENT_LENGTH)) {
/* 83 */       return true;
/*    */     }
/* 85 */     return empty;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\rtsp\RtspObjectDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */