/*    */ package io.netty.handler.codec.http;
/*    */ 
/*    */ import io.netty.handler.codec.AsciiString;
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
/*    */ public enum HttpStatusClass
/*    */ {
/* 28 */   INFORMATIONAL(100, 200, "Informational"), 
/*    */   
/*    */ 
/*    */ 
/* 32 */   SUCCESS(200, 300, "Success"), 
/*    */   
/*    */ 
/*    */ 
/* 36 */   REDIRECTION(300, 400, "Redirection"), 
/*    */   
/*    */ 
/*    */ 
/* 40 */   CLIENT_ERROR(400, 500, "Client Error"), 
/*    */   
/*    */ 
/*    */ 
/* 44 */   SERVER_ERROR(500, 600, "Server Error"), 
/*    */   
/*    */ 
/*    */ 
/* 48 */   UNKNOWN(0, 0, "Unknown Status");
/*    */   
/*    */ 
/*    */   private final int min;
/*    */   
/*    */   private final int max;
/*    */   
/*    */   private final AsciiString defaultReasonPhrase;
/*    */   
/*    */   public static HttpStatusClass valueOf(int code)
/*    */   {
/* 59 */     if (INFORMATIONAL.contains(code)) {
/* 60 */       return INFORMATIONAL;
/*    */     }
/* 62 */     if (SUCCESS.contains(code)) {
/* 63 */       return SUCCESS;
/*    */     }
/* 65 */     if (REDIRECTION.contains(code)) {
/* 66 */       return REDIRECTION;
/*    */     }
/* 68 */     if (CLIENT_ERROR.contains(code)) {
/* 69 */       return CLIENT_ERROR;
/*    */     }
/* 71 */     if (SERVER_ERROR.contains(code)) {
/* 72 */       return SERVER_ERROR;
/*    */     }
/* 74 */     return UNKNOWN;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private HttpStatusClass(int min, int max, String defaultReasonPhrase)
/*    */   {
/* 82 */     this.min = min;
/* 83 */     this.max = max;
/* 84 */     this.defaultReasonPhrase = new AsciiString(defaultReasonPhrase);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean contains(int code)
/*    */   {
/* 91 */     return (code >= this.min) && (code < this.max);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   AsciiString defaultReasonPhrase()
/*    */   {
/* 98 */     return this.defaultReasonPhrase;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\HttpStatusClass.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */