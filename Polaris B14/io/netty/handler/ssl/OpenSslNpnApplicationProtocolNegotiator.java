/*    */ package io.netty.handler.ssl;
/*    */ 
/*    */ import io.netty.util.internal.ObjectUtil;
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
/*    */ 
/*    */ public final class OpenSslNpnApplicationProtocolNegotiator
/*    */   implements OpenSslApplicationProtocolNegotiator
/*    */ {
/*    */   private final List<String> protocols;
/*    */   
/*    */   public OpenSslNpnApplicationProtocolNegotiator(Iterable<String> protocols)
/*    */   {
/* 30 */     this.protocols = ((List)ObjectUtil.checkNotNull(ApplicationProtocolUtil.toList(protocols), "protocols"));
/*    */   }
/*    */   
/*    */   public OpenSslNpnApplicationProtocolNegotiator(String... protocols) {
/* 34 */     this.protocols = ((List)ObjectUtil.checkNotNull(ApplicationProtocolUtil.toList(protocols), "protocols"));
/*    */   }
/*    */   
/*    */   public List<String> protocols()
/*    */   {
/* 39 */     return this.protocols;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\OpenSslNpnApplicationProtocolNegotiator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */