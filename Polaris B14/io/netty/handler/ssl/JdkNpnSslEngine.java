/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import javax.net.ssl.SSLEngine;
/*     */ import javax.net.ssl.SSLException;
/*     */ import org.eclipse.jetty.npn.NextProtoNego;
/*     */ import org.eclipse.jetty.npn.NextProtoNego.ClientProvider;
/*     */ import org.eclipse.jetty.npn.NextProtoNego.ServerProvider;
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
/*     */ final class JdkNpnSslEngine
/*     */   extends JdkSslEngine
/*     */ {
/*     */   private static boolean available;
/*     */   
/*     */   static boolean isAvailable()
/*     */   {
/*  38 */     updateAvailability();
/*  39 */     return available;
/*     */   }
/*     */   
/*     */   private static void updateAvailability() {
/*  43 */     if (available) {
/*  44 */       return;
/*     */     }
/*     */     try
/*     */     {
/*  48 */       ClassLoader bootloader = ClassLoader.getSystemClassLoader().getParent();
/*  49 */       if (bootloader == null)
/*     */       {
/*     */ 
/*  52 */         bootloader = ClassLoader.getSystemClassLoader();
/*     */       }
/*  54 */       Class.forName("sun.security.ssl.NextProtoNegoExtension", true, bootloader);
/*  55 */       available = true;
/*     */     }
/*     */     catch (Exception ignore) {}
/*     */   }
/*     */   
/*     */   JdkNpnSslEngine(SSLEngine engine, final JdkApplicationProtocolNegotiator applicationNegotiator, boolean server)
/*     */   {
/*  62 */     super(engine);
/*  63 */     ObjectUtil.checkNotNull(applicationNegotiator, "applicationNegotiator");
/*     */     
/*  65 */     if (server) {
/*  66 */       final JdkApplicationProtocolNegotiator.ProtocolSelectionListener protocolListener = (JdkApplicationProtocolNegotiator.ProtocolSelectionListener)ObjectUtil.checkNotNull(applicationNegotiator.protocolListenerFactory().newListener(this, applicationNegotiator.protocols()), "protocolListener");
/*     */       
/*     */ 
/*  69 */       NextProtoNego.put(engine, new NextProtoNego.ServerProvider()
/*     */       {
/*     */         public void unsupported() {
/*  72 */           protocolListener.unsupported();
/*     */         }
/*     */         
/*     */         public List<String> protocols()
/*     */         {
/*  77 */           return applicationNegotiator.protocols();
/*     */         }
/*     */         
/*     */         public void protocolSelected(String protocol)
/*     */         {
/*     */           try {
/*  83 */             protocolListener.selected(protocol);
/*     */           } catch (Throwable t) {
/*  85 */             PlatformDependent.throwException(t);
/*     */           }
/*     */         }
/*     */       });
/*     */     } else {
/*  90 */       final JdkApplicationProtocolNegotiator.ProtocolSelector protocolSelector = (JdkApplicationProtocolNegotiator.ProtocolSelector)ObjectUtil.checkNotNull(applicationNegotiator.protocolSelectorFactory().newSelector(this, new HashSet(applicationNegotiator.protocols())), "protocolSelector");
/*     */       
/*  92 */       NextProtoNego.put(engine, new NextProtoNego.ClientProvider()
/*     */       {
/*     */         public boolean supports() {
/*  95 */           return true;
/*     */         }
/*     */         
/*     */         public void unsupported()
/*     */         {
/* 100 */           protocolSelector.unsupported();
/*     */         }
/*     */         
/*     */         public String selectProtocol(List<String> protocols)
/*     */         {
/*     */           try {
/* 106 */             return protocolSelector.select(protocols);
/*     */           } catch (Throwable t) {
/* 108 */             PlatformDependent.throwException(t); }
/* 109 */           return null;
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */   public void closeInbound()
/*     */     throws SSLException
/*     */   {
/* 118 */     NextProtoNego.remove(getWrappedEngine());
/* 119 */     super.closeInbound();
/*     */   }
/*     */   
/*     */   public void closeOutbound()
/*     */   {
/* 124 */     NextProtoNego.remove(getWrappedEngine());
/* 125 */     super.closeOutbound();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\JdkNpnSslEngine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */