/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufInputStream;
/*     */ import io.netty.util.internal.ObjectUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.File;
/*     */ import java.security.KeyFactory;
/*     */ import java.security.KeyStore;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.cert.Certificate;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.security.spec.InvalidKeySpecException;
/*     */ import java.security.spec.PKCS8EncodedKeySpec;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.net.ssl.SSLException;
/*     */ import javax.net.ssl.TrustManagerFactory;
/*     */ import javax.net.ssl.X509TrustManager;
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
/*     */ 
/*     */ public final class OpenSslServerContext
/*     */   extends OpenSslContext
/*     */ {
/*  47 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(OpenSslServerContext.class);
/*     */   
/*     */ 
/*     */ 
/*     */   private final OpenSslServerSessionContext sessionContext;
/*     */   
/*     */ 
/*     */ 
/*     */   public OpenSslServerContext(File certChainFile, File keyFile)
/*     */     throws SSLException
/*     */   {
/*  58 */     this(certChainFile, keyFile, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public OpenSslServerContext(File certChainFile, File keyFile, String keyPassword)
/*     */     throws SSLException
/*     */   {
/*  70 */     this(certChainFile, keyFile, keyPassword, null, null, OpenSslDefaultApplicationProtocolNegotiator.INSTANCE, 0L, 0L);
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
/*     */ 
/*     */ 
/*     */   public OpenSslServerContext(File certChainFile, File keyFile, String keyPassword, Iterable<String> ciphers, ApplicationProtocolConfig apn, long sessionCacheSize, long sessionTimeout)
/*     */     throws SSLException
/*     */   {
/*  93 */     this(certChainFile, keyFile, keyPassword, null, ciphers, toNegotiator(apn, false), sessionCacheSize, sessionTimeout);
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
/*     */ 
/*     */ 
/*     */   public OpenSslServerContext(File certChainFile, File keyFile, String keyPassword, TrustManagerFactory trustManagerFactory, Iterable<String> ciphers, ApplicationProtocolConfig config, long sessionCacheSize, long sessionTimeout)
/*     */     throws SSLException
/*     */   {
/* 116 */     this(certChainFile, keyFile, keyPassword, trustManagerFactory, ciphers, toNegotiator(config, true), sessionCacheSize, sessionTimeout);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public OpenSslServerContext(File certChainFile, File keyFile, String keyPassword, TrustManagerFactory trustManagerFactory, Iterable<String> ciphers, OpenSslApplicationProtocolNegotiator apn, long sessionCacheSize, long sessionTimeout)
/*     */     throws SSLException
/*     */   {
/* 140 */     super(ciphers, apn, sessionCacheSize, sessionTimeout, 1);
/* 141 */     OpenSsl.ensureAvailability();
/*     */     
/* 143 */     ObjectUtil.checkNotNull(certChainFile, "certChainFile");
/* 144 */     if (!certChainFile.isFile()) {
/* 145 */       throw new IllegalArgumentException("certChainFile is not a file: " + certChainFile);
/*     */     }
/* 147 */     ObjectUtil.checkNotNull(keyFile, "keyFile");
/* 148 */     if (!keyFile.isFile()) {
/* 149 */       throw new IllegalArgumentException("keyPath is not a file: " + keyFile);
/*     */     }
/* 151 */     if (keyPassword == null) {
/* 152 */       keyPassword = "";
/*     */     }
/*     */     
/*     */ 
/* 156 */     boolean success = false;
/*     */     try {
/* 158 */       synchronized (OpenSslContext.class)
/*     */       {
/* 160 */         SSLContext.setVerify(this.ctx, 0, 10);
/*     */         
/*     */ 
/* 163 */         if (!SSLContext.setCertificateChainFile(this.ctx, certChainFile.getPath(), true)) {
/* 164 */           long error = SSL.getLastErrorNumber();
/* 165 */           if (OpenSsl.isError(error)) {
/* 166 */             String err = SSL.getErrorString(error);
/* 167 */             throw new SSLException("failed to set certificate chain: " + certChainFile + " (" + err + ')');
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */         try
/*     */         {
/* 174 */           if (!SSLContext.setCertificate(this.ctx, certChainFile.getPath(), keyFile.getPath(), keyPassword, 0))
/*     */           {
/* 176 */             long error = SSL.getLastErrorNumber();
/* 177 */             if (OpenSsl.isError(error)) {
/* 178 */               String err = SSL.getErrorString(error);
/* 179 */               throw new SSLException("failed to set certificate: " + certChainFile + " and " + keyFile + " (" + err + ')');
/*     */             }
/*     */           }
/*     */         }
/*     */         catch (SSLException e) {
/* 184 */           throw e;
/*     */         } catch (Exception e) {
/* 186 */           throw new SSLException("failed to set certificate: " + certChainFile + " and " + keyFile, e);
/*     */         }
/*     */         try {
/* 189 */           KeyStore ks = KeyStore.getInstance("JKS");
/* 190 */           ks.load(null, null);
/* 191 */           CertificateFactory cf = CertificateFactory.getInstance("X.509");
/* 192 */           KeyFactory rsaKF = KeyFactory.getInstance("RSA");
/* 193 */           KeyFactory dsaKF = KeyFactory.getInstance("DSA");
/*     */           
/* 195 */           ByteBuf encodedKeyBuf = PemReader.readPrivateKey(keyFile);
/* 196 */           byte[] encodedKey = new byte[encodedKeyBuf.readableBytes()];
/* 197 */           encodedKeyBuf.readBytes(encodedKey).release();
/*     */           
/* 199 */           char[] keyPasswordChars = keyPassword.toCharArray();
/* 200 */           PKCS8EncodedKeySpec encodedKeySpec = generateKeySpec(keyPasswordChars, encodedKey);
/*     */           PrivateKey key;
/*     */           try
/*     */           {
/* 204 */             key = rsaKF.generatePrivate(encodedKeySpec);
/*     */           } catch (InvalidKeySpecException ignore) {
/* 206 */             key = dsaKF.generatePrivate(encodedKeySpec);
/*     */           }
/*     */           
/* 209 */           List<Certificate> certChain = new ArrayList();
/* 210 */           ByteBuf[] certs = PemReader.readCertificates(certChainFile);
/*     */           try {
/* 212 */             for (ByteBuf buf : certs)
/* 213 */               certChain.add(cf.generateCertificate(new ByteBufInputStream(buf))); } finally { ByteBuf[] arr$;
/*     */             int len$;
/*     */             int i$;
/* 216 */             ByteBuf buf; for (ByteBuf buf : certs) {
/* 217 */               buf.release();
/*     */             }
/*     */           }
/*     */           
/* 221 */           ks.setKeyEntry("key", key, keyPasswordChars, (Certificate[])certChain.toArray(new Certificate[certChain.size()]));
/*     */           
/* 223 */           if (trustManagerFactory == null)
/*     */           {
/* 225 */             trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
/*     */             
/* 227 */             trustManagerFactory.init((KeyStore)null);
/*     */           } else {
/* 229 */             trustManagerFactory.init(ks);
/*     */           }
/*     */           
/* 232 */           final X509TrustManager manager = chooseTrustManager(trustManagerFactory.getTrustManagers());
/* 233 */           SSLContext.setCertVerifyCallback(this.ctx, new CertificateVerifier()
/*     */           {
/*     */             public boolean verify(long ssl, byte[][] chain, String auth) {
/* 236 */               X509Certificate[] peerCerts = OpenSslContext.certificates(chain);
/*     */               try {
/* 238 */                 manager.checkClientTrusted(peerCerts, auth);
/* 239 */                 return true;
/*     */               } catch (Exception e) {
/* 241 */                 OpenSslServerContext.logger.debug("verification of certificate failed", e);
/*     */               }
/* 243 */               return false;
/*     */             }
/*     */           });
/*     */         } catch (Exception e) {
/* 247 */           throw new SSLException("unable to setup trustmanager", e);
/*     */         }
/*     */       }
/* 250 */       this.sessionContext = new OpenSslServerSessionContext(this.ctx);
/* 251 */       success = true;
/*     */     } finally {
/* 253 */       if (!success) {
/* 254 */         destroyPools();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public OpenSslServerSessionContext sessionContext()
/*     */   {
/* 261 */     return this.sessionContext;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\OpenSslServerContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */