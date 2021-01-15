/*    */ package io.netty.handler.codec.http.websocketx;
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
/*    */ public enum WebSocketVersion
/*    */ {
/* 28 */   UNKNOWN, 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 34 */   V00, 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 40 */   V07, 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 46 */   V08, 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 53 */   V13;
/*    */   
/*    */   private WebSocketVersion() {}
/*    */   
/*    */   public String toHttpHeaderValue()
/*    */   {
/* 59 */     if (this == V00) {
/* 60 */       return "0";
/*    */     }
/* 62 */     if (this == V07) {
/* 63 */       return "7";
/*    */     }
/* 65 */     if (this == V08) {
/* 66 */       return "8";
/*    */     }
/* 68 */     if (this == V13) {
/* 69 */       return "13";
/*    */     }
/* 71 */     throw new IllegalStateException("Unknown web socket version: " + this);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\WebSocketVersion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */