/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufInputStream;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.X509Certificate;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.TrustManagerFactory;
/*     */ import javax.net.ssl.X509TrustManager;
/*     */ import javax.security.auth.x500.X500Principal;
/*     */ import org.apache.tomcat.jni.CertificateVerifier;
/*     */ import org.apache.tomcat.jni.SSL;
/*     */ import org.apache.tomcat.jni.SSLContext;
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
/*     */ public final class OpenSslClientContext
/*     */   extends OpenSslContext
/*     */ {
/*  43 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(OpenSslClientContext.class);
/*     */   
/*     */   private final OpenSslSessionContext sessionContext;
/*     */   
/*     */   public OpenSslClientContext()
/*     */     throws SSLException
/*     */   {
/*  50 */     this(null, null, null, null, 0L, 0L);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public OpenSslClientContext(File certChainFile)
/*     */     throws SSLException
/*     */   {
/*  60 */     this(certChainFile, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public OpenSslClientContext(TrustManagerFactory trustManagerFactory)
/*     */     throws SSLException
/*     */   {
/*  71 */     this(null, trustManagerFactory);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public OpenSslClientContext(File certChainFile, TrustManagerFactory trustManagerFactory)
/*     */     throws SSLException
/*     */   {
/*  84 */     this(certChainFile, trustManagerFactory, null, null, 0L, 0L);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public OpenSslClientContext(File certChainFile, TrustManagerFactory trustManagerFactory, Iterable<String> ciphers, ApplicationProtocolConfig apn, long sessionCacheSize, long sessionTimeout)
/*     */     throws SSLException
/*     */   {
/* 105 */     super(ciphers, apn, sessionCacheSize, sessionTimeout, 0);
/* 106 */     boolean success = false;
/*     */     try {
/* 108 */       if ((certChainFile != null) && (!certChainFile.isFile())) {
/* 109 */         throw new IllegalArgumentException("certChainFile is not a file: " + certChainFile);
/*     */       }
/*     */       
/* 112 */       synchronized (OpenSslContext.class) {
/* 113 */         if (certChainFile != null)
/*     */         {
/* 115 */           if (!SSLContext.setCertificateChainFile(this.ctx, certChainFile.getPath(), true)) {
/* 116 */             long error = SSL.getLastErrorNumber();
/* 117 */             if (OpenSsl.isError(error)) {
/* 118 */               throw new SSLException("failed to set certificate chain: " + certChainFile + " (" + SSL.getErrorString(error) + ')');
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 124 */         SSLContext.setVerify(this.ctx, 0, 10);
/*     */         
/*     */         try
/*     */         {
/* 128 */           if (trustManagerFactory == null) {
/* 129 */             trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
/*     */           }
/*     */           
/* 132 */           initTrustManagerFactory(certChainFile, trustManagerFactory);
/* 133 */           final X509TrustManager manager = chooseTrustManager(trustManagerFactory.getTrustManagers());
/*     */           
/* 135 */           SSLContext.setCertVerifyCallback(this.ctx, new CertificateVerifier()
/*     */           {
/*     */             public boolean verify(long ssl, byte[][] chain, String auth) {
/* 138 */               X509Certificate[] peerCerts = OpenSslContext.certificates(chain);
/*     */               try {
/* 140 */                 manager.checkServerTrusted(peerCerts, auth);
/* 141 */                 return true;
/*     */               } catch (Exception e) {
/* 143 */                 OpenSslClientContext.logger.debug("verification of certificate failed", e);
/*     */               }
/* 145 */               return false;
/*     */             }
/*     */           });
/*     */         } catch (Exception e) {
/* 149 */           throw new SSLException("unable to setup trustmanager", e);
/*     */         }
/*     */       }
/* 152 */       this.sessionContext = new OpenSslClientSessionContext(this.ctx, null);
/* 153 */       success = true;
/*     */     } finally {
/* 155 */       if (!success) {
/* 156 */         destroyPools();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static void initTrustManagerFactory(File certChainFile, TrustManagerFactory trustManagerFactory) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException
/*     */   {
/* 163 */     KeyStore ks = KeyStore.getInstance("JKS");
/* 164 */     ks.load(null, null);
/* 165 */     if (certChainFile != null) {
/* 166 */       ByteBuf[] certs = PemReader.readCertificates(certChainFile);
/*     */       try {
/* 168 */         for (ByteBuf buf : certs) {
/* 169 */           X509Certificate cert = (X509Certificate)X509_CERT_FACTORY.generateCertificate(new ByteBufInputStream(buf));
/*     */           
/* 171 */           X500Principal principal = cert.getSubjectX500Principal();
/* 172 */           ks.setCertificateEntry(principal.getName("RFC2253"), cert); } } finally { ByteBuf[] arr$;
/*     */         int len$;
/*     */         int i$;
/* 175 */         ByteBuf buf; for (ByteBuf buf : certs) {
/* 176 */           buf.release();
/*     */         }
/*     */       }
/*     */     }
/* 180 */     trustManagerFactory.init(ks);
/*     */   }
/*     */   
/*     */   public OpenSslSessionContext sessionContext()
/*     */   {
/* 185 */     return this.sessionContext;
/*     */   }
/*     */   
/*     */   private static final class OpenSslClientSessionContext extends OpenSslSessionContext
/*     */   {
/*     */     private OpenSslClientSessionContext(long context) {
/* 191 */       super();
/*     */     }
/*     */     
/*     */     public void setSessionTimeout(int seconds)
/*     */     {
/* 196 */       if (seconds < 0) {
/* 197 */         throw new IllegalArgumentException();
/*     */       }
/*     */     }
/*     */     
/*     */     public int getSessionTimeout()
/*     */     {
/* 203 */       return 0;
/*     */     }
/*     */     
/*     */     public void setSessionCacheSize(int size)
/*     */     {
/* 208 */       if (size < 0) {
/* 209 */         throw new IllegalArgumentException();
/*     */       }
/*     */     }
/*     */     
/*     */     public int getSessionCacheSize()
/*     */     {
/* 215 */       return 0;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void setSessionCacheEnabled(boolean enabled) {}
/*     */     
/*     */ 
/*     */     public boolean isSessionCacheEnabled()
/*     */     {
/* 225 */       return false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\OpenSslClientContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */