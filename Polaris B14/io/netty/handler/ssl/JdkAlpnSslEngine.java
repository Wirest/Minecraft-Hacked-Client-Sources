/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLException;
/*     */ import org.eclipse.jetty.alpn.ALPN;
/*     */ import org.eclipse.jetty.alpn.ALPN.ClientProvider;
/*     */ import org.eclipse.jetty.alpn.ALPN.ServerProvider;
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
/*     */ final class JdkAlpnSslEngine
/*     */   extends JdkSslEngine
/*     */ {
/*     */   private static boolean available;
/*     */   
/*     */   static boolean isAvailable()
/*     */   {
/*  37 */     updateAvailability();
/*  38 */     return available;
/*     */   }
/*     */   
/*     */   private static void updateAvailability() {
/*  42 */     if (available) {
/*  43 */       return;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/*  48 */       ClassLoader bootloader = ClassLoader.getSystemClassLoader().getParent();
/*  49 */       if (bootloader == null)
/*     */       {
/*     */ 
/*  52 */         bootloader = ClassLoader.getSystemClassLoader();
/*     */       }
/*  54 */       Class.forName("sun.security.ssl.ALPNExtension", true, bootloader);
/*  55 */       available = true;
/*     */     }
/*     */     catch (Exception ignore) {}
/*     */   }
/*     */   
/*     */   JdkAlpnSslEngine(SSLEngine engine, final JdkApplicationProtocolNegotiator applicationNegotiator, boolean server)
/*     */   {
/*  62 */     super(engine);
/*  63 */     ObjectUtil.checkNotNull(applicationNegotiator, "applicationNegotiator");
/*     */     
/*  65 */     if (server) {
/*  66 */       final JdkApplicationProtocolNegotiator.ProtocolSelector protocolSelector = (JdkApplicationProtocolNegotiator.ProtocolSelector)ObjectUtil.checkNotNull(applicationNegotiator.protocolSelectorFactory().newSelector(this, new HashSet(applicationNegotiator.protocols())), "protocolSelector");
/*     */       
/*  68 */       ALPN.put(engine, new ALPN.ServerProvider()
/*     */       {
/*     */         public String select(List<String> protocols) {
/*     */           try {
/*  72 */             return protocolSelector.select(protocols);
/*     */           } catch (Throwable t) {
/*  74 */             PlatformDependent.throwException(t); }
/*  75 */           return null;
/*     */         }
/*     */         
/*     */ 
/*     */         public void unsupported()
/*     */         {
/*  81 */           protocolSelector.unsupported();
/*     */         }
/*     */       });
/*     */     } else {
/*  85 */       final JdkApplicationProtocolNegotiator.ProtocolSelectionListener protocolListener = (JdkApplicationProtocolNegotiator.ProtocolSelectionListener)ObjectUtil.checkNotNull(applicationNegotiator.protocolListenerFactory().newListener(this, applicationNegotiator.protocols()), "protocolListener");
/*     */       
/*     */ 
/*  88 */       ALPN.put(engine, new ALPN.ClientProvider()
/*     */       {
/*     */         public List<String> protocols() {
/*  91 */           return applicationNegotiator.protocols();
/*     */         }
/*     */         
/*     */         public void selected(String protocol)
/*     */         {
/*     */           try {
/*  97 */             protocolListener.selected(protocol);
/*     */           } catch (Throwable t) {
/*  99 */             PlatformDependent.throwException(t);
/*     */           }
/*     */         }
/*     */         
/*     */         public void unsupported()
/*     */         {
/* 105 */           protocolListener.unsupported();
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */   public void closeInbound() throws SSLException
/*     */   {
/* 113 */     ALPN.remove(getWrappedEngine());
/* 114 */     super.closeInbound();
/*     */   }
/*     */   
/*     */   public void closeOutbound()
/*     */   {
/* 119 */     ALPN.remove(getWrappedEngine());
/* 120 */     super.closeOutbound();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\JdkAlpnSslEngine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */