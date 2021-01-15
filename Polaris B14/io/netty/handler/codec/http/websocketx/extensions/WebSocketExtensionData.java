/*    */ package io.netty.handler.codec.http.websocketx.extensions;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.Map;
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
/*    */ public final class WebSocketExtensionData
/*    */ {
/*    */   private final String name;
/*    */   private final Map<String, String> parameters;
/*    */   
/*    */   public WebSocketExtensionData(String name, Map<String, String> parameters)
/*    */   {
/* 32 */     if (name == null) {
/* 33 */       throw new NullPointerException("name");
/*    */     }
/* 35 */     if (parameters == null) {
/* 36 */       throw new NullPointerException("parameters");
/*    */     }
/* 38 */     this.name = name;
/* 39 */     this.parameters = Collections.unmodifiableMap(parameters);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String name()
/*    */   {
/* 46 */     return this.name;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public Map<String, String> parameters()
/*    */   {
/* 53 */     return this.parameters;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\extensions\WebSocketExtensionData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */