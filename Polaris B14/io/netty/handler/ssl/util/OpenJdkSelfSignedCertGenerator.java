/*    */ package io.netty.handler.ssl.util;
/*    */ 
/*    */ import java.math.BigInteger;
/*    */ import java.security.KeyPair;
/*    */ import java.security.PrivateKey;
/*    */ import java.security.SecureRandom;
/*    */ import java.security.cert.CertificateException;
/*    */ import sun.security.x509.AlgorithmId;
/*    */ import sun.security.x509.CertificateAlgorithmId;
/*    */ import sun.security.x509.CertificateIssuerName;
/*    */ import sun.security.x509.CertificateSerialNumber;
/*    */ import sun.security.x509.CertificateSubjectName;
/*    */ import sun.security.x509.CertificateValidity;
/*    */ import sun.security.x509.CertificateVersion;
/*    */ import sun.security.x509.CertificateX509Key;
/*    */ import sun.security.x509.X500Name;
/*    */ import sun.security.x509.X509CertImpl;
/*    */ import sun.security.x509.X509CertInfo;
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
/*    */ 
/*    */ 
/*    */ final class OpenJdkSelfSignedCertGenerator
/*    */ {
/*    */   static String[] generate(String fqdn, KeyPair keypair, SecureRandom random)
/*    */     throws Exception
/*    */   {
/* 45 */     PrivateKey key = keypair.getPrivate();
/*    */     
/*    */ 
/* 48 */     X509CertInfo info = new X509CertInfo();
/* 49 */     X500Name owner = new X500Name("CN=" + fqdn);
/* 50 */     info.set("version", new CertificateVersion(2));
/* 51 */     info.set("serialNumber", new CertificateSerialNumber(new BigInteger(64, random)));
/*    */     try {
/* 53 */       info.set("subject", new CertificateSubjectName(owner));
/*    */     } catch (CertificateException ignore) {
/* 55 */       info.set("subject", owner);
/*    */     }
/*    */     try {
/* 58 */       info.set("issuer", new CertificateIssuerName(owner));
/*    */     } catch (CertificateException ignore) {
/* 60 */       info.set("issuer", owner);
/*    */     }
/* 62 */     info.set("validity", new CertificateValidity(SelfSignedCertificate.NOT_BEFORE, SelfSignedCertificate.NOT_AFTER));
/* 63 */     info.set("key", new CertificateX509Key(keypair.getPublic()));
/* 64 */     info.set("algorithmID", new CertificateAlgorithmId(new AlgorithmId(AlgorithmId.sha1WithRSAEncryption_oid)));
/*    */     
/*    */ 
/*    */ 
/* 68 */     X509CertImpl cert = new X509CertImpl(info);
/* 69 */     cert.sign(key, "SHA1withRSA");
/*    */     
/*    */ 
/* 72 */     info.set("algorithmID.algorithm", cert.get("x509.algorithm"));
/* 73 */     cert = new X509CertImpl(info);
/* 74 */     cert.sign(key, "SHA1withRSA");
/* 75 */     cert.verify(keypair.getPublic());
/*    */     
/* 77 */     return SelfSignedCertificate.newSelfSignedCertificate(fqdn, key, cert);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\util\OpenJdkSelfSignedCertGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */