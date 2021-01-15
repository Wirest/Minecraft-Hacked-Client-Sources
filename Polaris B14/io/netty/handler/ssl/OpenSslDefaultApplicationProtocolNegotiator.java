/*    */ package io.netty.handler.ssl;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
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
/*    */ final class OpenSslDefaultApplicationProtocolNegotiator
/*    */   implements OpenSslApplicationProtocolNegotiator
/*    */ {
/* 25 */   static final OpenSslDefaultApplicationProtocolNegotiator INSTANCE = new OpenSslDefaultApplicationProtocolNegotiator();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public List<String> protocols()
/*    */   {
/* 33 */     return Collections.emptyList();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\OpenSslDefaultApplicationProtocolNegotiator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */