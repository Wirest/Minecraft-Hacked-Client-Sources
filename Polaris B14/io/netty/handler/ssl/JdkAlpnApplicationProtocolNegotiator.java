/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import javax.net.ssl.SSLEngine;
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
/*     */ public final class JdkAlpnApplicationProtocolNegotiator
/*     */   extends JdkBaseApplicationProtocolNegotiator
/*     */ {
/*  24 */   private static final JdkApplicationProtocolNegotiator.SslEngineWrapperFactory ALPN_WRAPPER = new JdkApplicationProtocolNegotiator.SslEngineWrapperFactory()
/*     */   {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public SSLEngine wrapSslEngine(SSLEngine engine, JdkApplicationProtocolNegotiator applicationNegotiator, boolean isServer)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*  35 */       return new JdkAlpnSslEngine(engine, applicationNegotiator, isServer);
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JdkAlpnApplicationProtocolNegotiator(Iterable<String> protocols)
/*     */   {
/*  44 */     this(false, protocols);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JdkAlpnApplicationProtocolNegotiator(String... protocols)
/*     */   {
/*  52 */     this(false, protocols);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JdkAlpnApplicationProtocolNegotiator(boolean failIfNoCommonProtocols, Iterable<String> protocols)
/*     */   {
/*  61 */     this(failIfNoCommonProtocols, failIfNoCommonProtocols, protocols);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JdkAlpnApplicationProtocolNegotiator(boolean failIfNoCommonProtocols, String... protocols)
/*     */   {
/*  70 */     this(failIfNoCommonProtocols, failIfNoCommonProtocols, protocols);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JdkAlpnApplicationProtocolNegotiator(boolean clientFailIfNoCommonProtocols, boolean serverFailIfNoCommonProtocols, Iterable<String> protocols)
/*     */   {
/*  81 */     this(serverFailIfNoCommonProtocols ? FAIL_SELECTOR_FACTORY : NO_FAIL_SELECTOR_FACTORY, clientFailIfNoCommonProtocols ? FAIL_SELECTION_LISTENER_FACTORY : NO_FAIL_SELECTION_LISTENER_FACTORY, protocols);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JdkAlpnApplicationProtocolNegotiator(boolean clientFailIfNoCommonProtocols, boolean serverFailIfNoCommonProtocols, String... protocols)
/*     */   {
/*  94 */     this(serverFailIfNoCommonProtocols ? FAIL_SELECTOR_FACTORY : NO_FAIL_SELECTOR_FACTORY, clientFailIfNoCommonProtocols ? FAIL_SELECTION_LISTENER_FACTORY : NO_FAIL_SELECTION_LISTENER_FACTORY, protocols);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JdkAlpnApplicationProtocolNegotiator(JdkApplicationProtocolNegotiator.ProtocolSelectorFactory selectorFactory, JdkApplicationProtocolNegotiator.ProtocolSelectionListenerFactory listenerFactory, Iterable<String> protocols)
/*     */   {
/* 107 */     super(ALPN_WRAPPER, selectorFactory, listenerFactory, protocols);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JdkAlpnApplicationProtocolNegotiator(JdkApplicationProtocolNegotiator.ProtocolSelectorFactory selectorFactory, JdkApplicationProtocolNegotiator.ProtocolSelectionListenerFactory listenerFactory, String... protocols)
/*     */   {
/* 118 */     super(ALPN_WRAPPER, selectorFactory, listenerFactory, protocols);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\JdkAlpnApplicationProtocolNegotiator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */