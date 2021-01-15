/*    */ package io.netty.handler.codec.http2;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Http2NoMoreStreamIdsException
/*    */   extends Http2Exception
/*    */ {
/*    */   private static final long serialVersionUID = -7756236161274851110L;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private static final String ERROR_MESSAGE = "No more streams can be created on this connection";
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Http2NoMoreStreamIdsException()
/*    */   {
/* 27 */     super(Http2Error.PROTOCOL_ERROR, "No more streams can be created on this connection");
/*    */   }
/*    */   
/*    */   public Http2NoMoreStreamIdsException(Throwable cause) {
/* 31 */     super(Http2Error.PROTOCOL_ERROR, "No more streams can be created on this connection", cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\Http2NoMoreStreamIdsException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */