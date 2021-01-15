/*     */ package io.netty.handler.ssl.util;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.handler.codec.base64.Base64;
/*     */ import io.netty.util.CharsetUtil;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.security.KeyPair;
/*     */ import java.security.KeyPairGenerator;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.cert.CertificateEncodingException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Date;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class SelfSignedCertificate
/*     */ {
/*  57 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(SelfSignedCertificate.class);
/*     */   
/*     */ 
/*  60 */   static final Date NOT_BEFORE = new Date(System.currentTimeMillis() - 31536000000L);
/*     */   
/*  62 */   static final Date NOT_AFTER = new Date(253402300799000L);
/*     */   
/*     */   private final File certificate;
/*     */   
/*     */   private final File privateKey;
/*     */   
/*     */   public SelfSignedCertificate()
/*     */     throws CertificateException
/*     */   {
/*  71 */     this("example.com");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SelfSignedCertificate(String fqdn)
/*     */     throws CertificateException
/*     */   {
/*  82 */     this(fqdn, ThreadLocalInsecureRandom.current(), 1024);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SelfSignedCertificate(String fqdn, SecureRandom random, int bits)
/*     */     throws CertificateException
/*     */   {
/*     */     KeyPair keypair;
/*     */     
/*     */ 
/*     */     try
/*     */     {
/*  96 */       KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
/*  97 */       keyGen.initialize(bits, random);
/*  98 */       keypair = keyGen.generateKeyPair();
/*     */     }
/*     */     catch (NoSuchAlgorithmException e) {
/* 101 */       throw new Error(e);
/*     */     }
/*     */     
/*     */     String[] paths;
/*     */     try
/*     */     {
/* 107 */       paths = OpenJdkSelfSignedCertGenerator.generate(fqdn, keypair, random);
/*     */     } catch (Throwable t) {
/* 109 */       logger.debug("Failed to generate a self-signed X.509 certificate using sun.security.x509:", t);
/*     */       try
/*     */       {
/* 112 */         paths = BouncyCastleSelfSignedCertGenerator.generate(fqdn, keypair, random);
/*     */       } catch (Throwable t2) {
/* 114 */         logger.debug("Failed to generate a self-signed X.509 certificate using Bouncy Castle:", t2);
/* 115 */         throw new CertificateException("No provider succeeded to generate a self-signed certificate. See debug log for the root cause.");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 121 */     this.certificate = new File(paths[0]);
/* 122 */     this.privateKey = new File(paths[1]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public File certificate()
/*     */   {
/* 129 */     return this.certificate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public File privateKey()
/*     */   {
/* 136 */     return this.privateKey;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void delete()
/*     */   {
/* 143 */     safeDelete(this.certificate);
/* 144 */     safeDelete(this.privateKey);
/*     */   }
/*     */   
/*     */ 
/*     */   static String[] newSelfSignedCertificate(String fqdn, PrivateKey key, X509Certificate cert)
/*     */     throws IOException, CertificateEncodingException
/*     */   {
/* 151 */     String keyText = "-----BEGIN PRIVATE KEY-----\n" + Base64.encode(Unpooled.wrappedBuffer(key.getEncoded()), true).toString(CharsetUtil.US_ASCII) + "\n-----END PRIVATE KEY-----\n";
/*     */     
/*     */ 
/*     */ 
/* 155 */     File keyFile = File.createTempFile("keyutil_" + fqdn + '_', ".key");
/* 156 */     keyFile.deleteOnExit();
/*     */     
/* 158 */     OutputStream keyOut = new FileOutputStream(keyFile);
/*     */     try {
/* 160 */       keyOut.write(keyText.getBytes(CharsetUtil.US_ASCII));
/* 161 */       keyOut.close();
/* 162 */       keyOut = null;
/*     */     } finally {
/* 164 */       if (keyOut != null) {
/* 165 */         safeClose(keyFile, keyOut);
/* 166 */         safeDelete(keyFile);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 171 */     String certText = "-----BEGIN CERTIFICATE-----\n" + Base64.encode(Unpooled.wrappedBuffer(cert.getEncoded()), true).toString(CharsetUtil.US_ASCII) + "\n-----END CERTIFICATE-----\n";
/*     */     
/*     */ 
/*     */ 
/* 175 */     File certFile = File.createTempFile("keyutil_" + fqdn + '_', ".crt");
/* 176 */     certFile.deleteOnExit();
/*     */     
/* 178 */     OutputStream certOut = new FileOutputStream(certFile);
/*     */     try {
/* 180 */       certOut.write(certText.getBytes(CharsetUtil.US_ASCII));
/* 181 */       certOut.close();
/* 182 */       certOut = null;
/*     */     } finally {
/* 184 */       if (certOut != null) {
/* 185 */         safeClose(certFile, certOut);
/* 186 */         safeDelete(certFile);
/* 187 */         safeDelete(keyFile);
/*     */       }
/*     */     }
/*     */     
/* 191 */     return new String[] { certFile.getPath(), keyFile.getPath() };
/*     */   }
/*     */   
/*     */   private static void safeDelete(File certFile) {
/* 195 */     if (!certFile.delete()) {
/* 196 */       logger.warn("Failed to delete a file: " + certFile);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void safeClose(File keyFile, OutputStream keyOut) {
/*     */     try {
/* 202 */       keyOut.close();
/*     */     } catch (IOException e) {
/* 204 */       logger.warn("Failed to close a file: " + keyFile, e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\util\SelfSignedCertificate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */