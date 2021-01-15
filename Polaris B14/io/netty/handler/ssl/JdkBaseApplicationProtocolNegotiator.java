/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLHandshakeException;
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
/*     */ class JdkBaseApplicationProtocolNegotiator
/*     */   implements JdkApplicationProtocolNegotiator
/*     */ {
/*     */   private final List<String> protocols;
/*     */   private final JdkApplicationProtocolNegotiator.ProtocolSelectorFactory selectorFactory;
/*     */   private final JdkApplicationProtocolNegotiator.ProtocolSelectionListenerFactory listenerFactory;
/*     */   private final JdkApplicationProtocolNegotiator.SslEngineWrapperFactory wrapperFactory;
/*     */   
/*     */   protected JdkBaseApplicationProtocolNegotiator(JdkApplicationProtocolNegotiator.SslEngineWrapperFactory wrapperFactory, JdkApplicationProtocolNegotiator.ProtocolSelectorFactory selectorFactory, JdkApplicationProtocolNegotiator.ProtocolSelectionListenerFactory listenerFactory, Iterable<String> protocols)
/*     */   {
/*  47 */     this(wrapperFactory, selectorFactory, listenerFactory, ApplicationProtocolUtil.toList(protocols));
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
/*     */   protected JdkBaseApplicationProtocolNegotiator(JdkApplicationProtocolNegotiator.SslEngineWrapperFactory wrapperFactory, JdkApplicationProtocolNegotiator.ProtocolSelectorFactory selectorFactory, JdkApplicationProtocolNegotiator.ProtocolSelectionListenerFactory listenerFactory, String... protocols)
/*     */   {
/*  60 */     this(wrapperFactory, selectorFactory, listenerFactory, ApplicationProtocolUtil.toList(protocols));
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
/*     */   private JdkBaseApplicationProtocolNegotiator(JdkApplicationProtocolNegotiator.SslEngineWrapperFactory wrapperFactory, JdkApplicationProtocolNegotiator.ProtocolSelectorFactory selectorFactory, JdkApplicationProtocolNegotiator.ProtocolSelectionListenerFactory listenerFactory, List<String> protocols)
/*     */   {
/*  73 */     this.wrapperFactory = ((JdkApplicationProtocolNegotiator.SslEngineWrapperFactory)ObjectUtil.checkNotNull(wrapperFactory, "wrapperFactory"));
/*  74 */     this.selectorFactory = ((JdkApplicationProtocolNegotiator.ProtocolSelectorFactory)ObjectUtil.checkNotNull(selectorFactory, "selectorFactory"));
/*  75 */     this.listenerFactory = ((JdkApplicationProtocolNegotiator.ProtocolSelectionListenerFactory)ObjectUtil.checkNotNull(listenerFactory, "listenerFactory"));
/*  76 */     this.protocols = Collections.unmodifiableList((List)ObjectUtil.checkNotNull(protocols, "protocols"));
/*     */   }
/*     */   
/*     */   public List<String> protocols()
/*     */   {
/*  81 */     return this.protocols;
/*     */   }
/*     */   
/*     */   public JdkApplicationProtocolNegotiator.ProtocolSelectorFactory protocolSelectorFactory()
/*     */   {
/*  86 */     return this.selectorFactory;
/*     */   }
/*     */   
/*     */   public JdkApplicationProtocolNegotiator.ProtocolSelectionListenerFactory protocolListenerFactory()
/*     */   {
/*  91 */     return this.listenerFactory;
/*     */   }
/*     */   
/*     */   public JdkApplicationProtocolNegotiator.SslEngineWrapperFactory wrapperFactory()
/*     */   {
/*  96 */     return this.wrapperFactory;
/*     */   }
/*     */   
/*  99 */   static final JdkApplicationProtocolNegotiator.ProtocolSelectorFactory FAIL_SELECTOR_FACTORY = new JdkApplicationProtocolNegotiator.ProtocolSelectorFactory()
/*     */   {
/*     */     public JdkApplicationProtocolNegotiator.ProtocolSelector newSelector(SSLEngine engine, Set<String> supportedProtocols) {
/* 102 */       return new JdkBaseApplicationProtocolNegotiator.FailProtocolSelector((JdkSslEngine)engine, supportedProtocols);
/*     */     }
/*     */   };
/*     */   
/* 106 */   static final JdkApplicationProtocolNegotiator.ProtocolSelectorFactory NO_FAIL_SELECTOR_FACTORY = new JdkApplicationProtocolNegotiator.ProtocolSelectorFactory()
/*     */   {
/*     */     public JdkApplicationProtocolNegotiator.ProtocolSelector newSelector(SSLEngine engine, Set<String> supportedProtocols) {
/* 109 */       return new JdkBaseApplicationProtocolNegotiator.NoFailProtocolSelector((JdkSslEngine)engine, supportedProtocols);
/*     */     }
/*     */   };
/*     */   
/* 113 */   static final JdkApplicationProtocolNegotiator.ProtocolSelectionListenerFactory FAIL_SELECTION_LISTENER_FACTORY = new JdkApplicationProtocolNegotiator.ProtocolSelectionListenerFactory()
/*     */   {
/*     */     public JdkApplicationProtocolNegotiator.ProtocolSelectionListener newListener(SSLEngine engine, List<String> supportedProtocols)
/*     */     {
/* 117 */       return new JdkBaseApplicationProtocolNegotiator.FailProtocolSelectionListener((JdkSslEngine)engine, supportedProtocols);
/*     */     }
/*     */   };
/*     */   
/* 121 */   static final JdkApplicationProtocolNegotiator.ProtocolSelectionListenerFactory NO_FAIL_SELECTION_LISTENER_FACTORY = new JdkApplicationProtocolNegotiator.ProtocolSelectionListenerFactory()
/*     */   {
/*     */     public JdkApplicationProtocolNegotiator.ProtocolSelectionListener newListener(SSLEngine engine, List<String> supportedProtocols)
/*     */     {
/* 125 */       return new JdkBaseApplicationProtocolNegotiator.NoFailProtocolSelectionListener((JdkSslEngine)engine, supportedProtocols);
/*     */     }
/*     */   };
/*     */   
/*     */   protected static class NoFailProtocolSelector implements JdkApplicationProtocolNegotiator.ProtocolSelector {
/*     */     private final JdkSslEngine jettyWrapper;
/*     */     private final Set<String> supportedProtocols;
/*     */     
/*     */     public NoFailProtocolSelector(JdkSslEngine jettyWrapper, Set<String> supportedProtocols) {
/* 134 */       this.jettyWrapper = jettyWrapper;
/* 135 */       this.supportedProtocols = supportedProtocols;
/*     */     }
/*     */     
/*     */     public void unsupported()
/*     */     {
/* 140 */       this.jettyWrapper.getSession().setApplicationProtocol(null);
/*     */     }
/*     */     
/*     */     public String select(List<String> protocols) throws Exception
/*     */     {
/* 145 */       for (int i = 0; i < protocols.size(); i++) {
/* 146 */         String p = (String)protocols.get(i);
/* 147 */         if (this.supportedProtocols.contains(p)) {
/* 148 */           this.jettyWrapper.getSession().setApplicationProtocol(p);
/* 149 */           return p;
/*     */         }
/*     */       }
/* 152 */       return noSelectMatchFound();
/*     */     }
/*     */     
/*     */     public String noSelectMatchFound() throws Exception {
/* 156 */       this.jettyWrapper.getSession().setApplicationProtocol(null);
/* 157 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   protected static final class FailProtocolSelector extends JdkBaseApplicationProtocolNegotiator.NoFailProtocolSelector {
/*     */     public FailProtocolSelector(JdkSslEngine jettyWrapper, Set<String> supportedProtocols) {
/* 163 */       super(supportedProtocols);
/*     */     }
/*     */     
/*     */     public String noSelectMatchFound() throws Exception
/*     */     {
/* 168 */       throw new SSLHandshakeException("Selected protocol is not supported");
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class NoFailProtocolSelectionListener implements JdkApplicationProtocolNegotiator.ProtocolSelectionListener {
/*     */     private final JdkSslEngine jettyWrapper;
/*     */     private final List<String> supportedProtocols;
/*     */     
/*     */     public NoFailProtocolSelectionListener(JdkSslEngine jettyWrapper, List<String> supportedProtocols) {
/* 177 */       this.jettyWrapper = jettyWrapper;
/* 178 */       this.supportedProtocols = supportedProtocols;
/*     */     }
/*     */     
/*     */     public void unsupported()
/*     */     {
/* 183 */       this.jettyWrapper.getSession().setApplicationProtocol(null);
/*     */     }
/*     */     
/*     */     public void selected(String protocol) throws Exception
/*     */     {
/* 188 */       if (this.supportedProtocols.contains(protocol)) {
/* 189 */         this.jettyWrapper.getSession().setApplicationProtocol(protocol);
/*     */       } else {
/* 191 */         noSelectedMatchFound(protocol);
/*     */       }
/*     */     }
/*     */     
/*     */     public void noSelectedMatchFound(String protocol) throws Exception
/*     */     {}
/*     */   }
/*     */   
/*     */   protected static final class FailProtocolSelectionListener extends JdkBaseApplicationProtocolNegotiator.NoFailProtocolSelectionListener {
/*     */     public FailProtocolSelectionListener(JdkSslEngine jettyWrapper, List<String> supportedProtocols) {
/* 201 */       super(supportedProtocols);
/*     */     }
/*     */     
/*     */     public void noSelectedMatchFound(String protocol) throws Exception
/*     */     {
/* 206 */       throw new SSLHandshakeException("No compatible protocols found");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\JdkBaseApplicationProtocolNegotiator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */