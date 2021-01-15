/*     */ package io.netty.handler.ssl.util;
/*     */ 
/*     */ import io.netty.buffer.ByteBufUtil;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import io.netty.util.concurrent.FastThreadLocal;
/*     */ import io.netty.util.internal.EmptyArrays;
/*     */ import java.security.KeyStore;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.cert.CertificateEncodingException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.net.ssl.ManagerFactoryParameters;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.X509TrustManager;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FingerprintTrustManagerFactory
/*     */   extends SimpleTrustManagerFactory
/*     */ {
/*  66 */   private static final Pattern FINGERPRINT_PATTERN = Pattern.compile("^[0-9a-fA-F:]+$");
/*  67 */   private static final Pattern FINGERPRINT_STRIP_PATTERN = Pattern.compile(":");
/*     */   
/*     */   private static final int SHA1_BYTE_LEN = 20;
/*     */   private static final int SHA1_HEX_LEN = 40;
/*  71 */   private static final FastThreadLocal<MessageDigest> tlmd = new FastThreadLocal()
/*     */   {
/*     */     protected MessageDigest initialValue() {
/*     */       try {
/*  75 */         return MessageDigest.getInstance("SHA1");
/*     */       }
/*     */       catch (NoSuchAlgorithmException e) {
/*  78 */         throw new Error(e);
/*     */       }
/*     */     }
/*     */   };
/*     */   
/*  83 */   private final TrustManager tm = new X509TrustManager()
/*     */   {
/*     */     public void checkClientTrusted(X509Certificate[] chain, String s) throws CertificateException
/*     */     {
/*  87 */       checkTrusted("client", chain);
/*     */     }
/*     */     
/*     */     public void checkServerTrusted(X509Certificate[] chain, String s) throws CertificateException
/*     */     {
/*  92 */       checkTrusted("server", chain);
/*     */     }
/*     */     
/*     */     private void checkTrusted(String type, X509Certificate[] chain) throws CertificateException {
/*  96 */       X509Certificate cert = chain[0];
/*  97 */       byte[] fingerprint = fingerprint(cert);
/*  98 */       boolean found = false;
/*  99 */       for (byte[] allowedFingerprint : FingerprintTrustManagerFactory.this.fingerprints) {
/* 100 */         if (Arrays.equals(fingerprint, allowedFingerprint)) {
/* 101 */           found = true;
/* 102 */           break;
/*     */         }
/*     */       }
/*     */       
/* 106 */       if (!found) {
/* 107 */         throw new CertificateException(type + " certificate with unknown fingerprint: " + cert.getSubjectDN());
/*     */       }
/*     */     }
/*     */     
/*     */     private byte[] fingerprint(X509Certificate cert) throws CertificateEncodingException
/*     */     {
/* 113 */       MessageDigest md = (MessageDigest)FingerprintTrustManagerFactory.tlmd.get();
/* 114 */       md.reset();
/* 115 */       return md.digest(cert.getEncoded());
/*     */     }
/*     */     
/*     */     public X509Certificate[] getAcceptedIssuers()
/*     */     {
/* 120 */       return EmptyArrays.EMPTY_X509_CERTIFICATES;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */   private final byte[][] fingerprints;
/*     */   
/*     */ 
/*     */ 
/*     */   public FingerprintTrustManagerFactory(Iterable<String> fingerprints)
/*     */   {
/* 132 */     this(toFingerprintArray(fingerprints));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FingerprintTrustManagerFactory(String... fingerprints)
/*     */   {
/* 141 */     this(toFingerprintArray(Arrays.asList(fingerprints)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FingerprintTrustManagerFactory(byte[]... fingerprints)
/*     */   {
/* 150 */     if (fingerprints == null) {
/* 151 */       throw new NullPointerException("fingerprints");
/*     */     }
/*     */     
/* 154 */     List<byte[]> list = new ArrayList();
/* 155 */     for (byte[] f : fingerprints) {
/* 156 */       if (f == null) {
/*     */         break;
/*     */       }
/* 159 */       if (f.length != 20) {
/* 160 */         throw new IllegalArgumentException("malformed fingerprint: " + ByteBufUtil.hexDump(Unpooled.wrappedBuffer(f)) + " (expected: SHA1)");
/*     */       }
/*     */       
/* 163 */       list.add(f.clone());
/*     */     }
/*     */     
/* 166 */     this.fingerprints = ((byte[][])list.toArray(new byte[list.size()][]));
/*     */   }
/*     */   
/*     */   private static byte[][] toFingerprintArray(Iterable<String> fingerprints) {
/* 170 */     if (fingerprints == null) {
/* 171 */       throw new NullPointerException("fingerprints");
/*     */     }
/*     */     
/* 174 */     List<byte[]> list = new ArrayList();
/* 175 */     for (String f : fingerprints) {
/* 176 */       if (f == null) {
/*     */         break;
/*     */       }
/*     */       
/* 180 */       if (!FINGERPRINT_PATTERN.matcher(f).matches()) {
/* 181 */         throw new IllegalArgumentException("malformed fingerprint: " + f);
/*     */       }
/* 183 */       f = FINGERPRINT_STRIP_PATTERN.matcher(f).replaceAll("");
/* 184 */       if (f.length() != 40) {
/* 185 */         throw new IllegalArgumentException("malformed fingerprint: " + f + " (expected: SHA1)");
/*     */       }
/*     */       
/* 188 */       byte[] farr = new byte[20];
/* 189 */       for (int i = 0; i < farr.length; i++) {
/* 190 */         int strIdx = i << 1;
/* 191 */         farr[i] = ((byte)Integer.parseInt(f.substring(strIdx, strIdx + 2), 16));
/*     */       }
/* 193 */       list.add(farr);
/*     */     }
/*     */     
/* 196 */     return (byte[][])list.toArray(new byte[list.size()][]);
/*     */   }
/*     */   
/*     */   protected void engineInit(KeyStore keyStore) throws Exception
/*     */   {}
/*     */   
/*     */   protected void engineInit(ManagerFactoryParameters managerFactoryParameters) throws Exception
/*     */   {}
/*     */   
/*     */   protected TrustManager[] engineGetTrustManagers()
/*     */   {
/* 207 */     return new TrustManager[] { this.tm };
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\util\FingerprintTrustManagerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */