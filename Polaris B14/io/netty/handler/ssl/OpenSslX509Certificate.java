/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.math.BigInteger;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.Principal;
/*     */ import java.security.PublicKey;
/*     */ import java.security.SignatureException;
/*     */ import java.security.cert.CertificateEncodingException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.CertificateExpiredException;
/*     */ import java.security.cert.CertificateFactory;
/*     */ import java.security.cert.CertificateNotYetValidException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Date;
/*     */ import java.util.Set;
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
/*     */ final class OpenSslX509Certificate
/*     */   extends X509Certificate
/*     */ {
/*     */   private final byte[] bytes;
/*     */   private X509Certificate wrapped;
/*     */   
/*     */   public OpenSslX509Certificate(byte[] bytes)
/*     */   {
/*  40 */     this.bytes = bytes;
/*     */   }
/*     */   
/*     */   public void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException
/*     */   {
/*  45 */     unwrap().checkValidity();
/*     */   }
/*     */   
/*     */   public void checkValidity(Date date) throws CertificateExpiredException, CertificateNotYetValidException
/*     */   {
/*  50 */     unwrap().checkValidity(date);
/*     */   }
/*     */   
/*     */   public int getVersion()
/*     */   {
/*  55 */     return unwrap().getVersion();
/*     */   }
/*     */   
/*     */   public BigInteger getSerialNumber()
/*     */   {
/*  60 */     return unwrap().getSerialNumber();
/*     */   }
/*     */   
/*     */   public Principal getIssuerDN()
/*     */   {
/*  65 */     return unwrap().getIssuerDN();
/*     */   }
/*     */   
/*     */   public Principal getSubjectDN()
/*     */   {
/*  70 */     return unwrap().getSubjectDN();
/*     */   }
/*     */   
/*     */   public Date getNotBefore()
/*     */   {
/*  75 */     return unwrap().getNotBefore();
/*     */   }
/*     */   
/*     */   public Date getNotAfter()
/*     */   {
/*  80 */     return unwrap().getNotAfter();
/*     */   }
/*     */   
/*     */   public byte[] getTBSCertificate() throws CertificateEncodingException
/*     */   {
/*  85 */     return unwrap().getTBSCertificate();
/*     */   }
/*     */   
/*     */   public byte[] getSignature()
/*     */   {
/*  90 */     return unwrap().getSignature();
/*     */   }
/*     */   
/*     */   public String getSigAlgName()
/*     */   {
/*  95 */     return unwrap().getSigAlgName();
/*     */   }
/*     */   
/*     */   public String getSigAlgOID()
/*     */   {
/* 100 */     return unwrap().getSigAlgOID();
/*     */   }
/*     */   
/*     */   public byte[] getSigAlgParams()
/*     */   {
/* 105 */     return unwrap().getSigAlgParams();
/*     */   }
/*     */   
/*     */   public boolean[] getIssuerUniqueID()
/*     */   {
/* 110 */     return unwrap().getIssuerUniqueID();
/*     */   }
/*     */   
/*     */   public boolean[] getSubjectUniqueID()
/*     */   {
/* 115 */     return unwrap().getSubjectUniqueID();
/*     */   }
/*     */   
/*     */   public boolean[] getKeyUsage()
/*     */   {
/* 120 */     return unwrap().getKeyUsage();
/*     */   }
/*     */   
/*     */   public int getBasicConstraints()
/*     */   {
/* 125 */     return unwrap().getBasicConstraints();
/*     */   }
/*     */   
/*     */   public byte[] getEncoded()
/*     */   {
/* 130 */     return (byte[])this.bytes.clone();
/*     */   }
/*     */   
/*     */ 
/*     */   public void verify(PublicKey key)
/*     */     throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException
/*     */   {
/* 137 */     unwrap().verify(key);
/*     */   }
/*     */   
/*     */ 
/*     */   public void verify(PublicKey key, String sigProvider)
/*     */     throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException
/*     */   {
/* 144 */     unwrap().verify(key, sigProvider);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 149 */     return unwrap().toString();
/*     */   }
/*     */   
/*     */   public PublicKey getPublicKey()
/*     */   {
/* 154 */     return unwrap().getPublicKey();
/*     */   }
/*     */   
/*     */   public boolean hasUnsupportedCriticalExtension()
/*     */   {
/* 159 */     return unwrap().hasUnsupportedCriticalExtension();
/*     */   }
/*     */   
/*     */   public Set<String> getCriticalExtensionOIDs()
/*     */   {
/* 164 */     return unwrap().getCriticalExtensionOIDs();
/*     */   }
/*     */   
/*     */   public Set<String> getNonCriticalExtensionOIDs()
/*     */   {
/* 169 */     return unwrap().getNonCriticalExtensionOIDs();
/*     */   }
/*     */   
/*     */   public byte[] getExtensionValue(String oid)
/*     */   {
/* 174 */     return unwrap().getExtensionValue(oid);
/*     */   }
/*     */   
/*     */   private X509Certificate unwrap() {
/* 178 */     X509Certificate wrapped = this.wrapped;
/* 179 */     if (wrapped == null) {
/*     */       try {
/* 181 */         wrapped = this.wrapped = (X509Certificate)SslContext.X509_CERT_FACTORY.generateCertificate(new ByteArrayInputStream(this.bytes));
/*     */       }
/*     */       catch (CertificateException e) {
/* 184 */         throw new IllegalStateException(e);
/*     */       }
/*     */     }
/* 187 */     return wrapped;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\OpenSslX509Certificate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */