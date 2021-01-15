/*    */ package io.netty.handler.ssl;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import javax.net.ssl.SSLEngine;
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
/*    */ final class JdkDefaultApplicationProtocolNegotiator
/*    */   implements JdkApplicationProtocolNegotiator
/*    */ {
/* 28 */   public static final JdkDefaultApplicationProtocolNegotiator INSTANCE = new JdkDefaultApplicationProtocolNegotiator();
/*    */   
/* 30 */   private static final JdkApplicationProtocolNegotiator.SslEngineWrapperFactory DEFAULT_SSL_ENGINE_WRAPPER_FACTORY = new JdkApplicationProtocolNegotiator.SslEngineWrapperFactory()
/*    */   {
/*    */     public SSLEngine wrapSslEngine(SSLEngine engine, JdkApplicationProtocolNegotiator applicationNegotiator, boolean isServer)
/*    */     {
/* 34 */       return engine;
/*    */     }
/*    */   };
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public JdkApplicationProtocolNegotiator.SslEngineWrapperFactory wrapperFactory()
/*    */   {
/* 43 */     return DEFAULT_SSL_ENGINE_WRAPPER_FACTORY;
/*    */   }
/*    */   
/*    */   public JdkApplicationProtocolNegotiator.ProtocolSelectorFactory protocolSelectorFactory()
/*    */   {
/* 48 */     throw new UnsupportedOperationException("Application protocol negotiation unsupported");
/*    */   }
/*    */   
/*    */   public JdkApplicationProtocolNegotiator.ProtocolSelectionListenerFactory protocolListenerFactory()
/*    */   {
/* 53 */     throw new UnsupportedOperationException("Application protocol negotiation unsupported");
/*    */   }
/*    */   
/*    */   public List<String> protocols()
/*    */   {
/* 58 */     return Collections.emptyList();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\JdkDefaultApplicationProtocolNegotiator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */