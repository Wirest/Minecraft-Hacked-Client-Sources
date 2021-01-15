/*    */ package io.netty.handler.ssl.util;
/*    */ 
/*    */ import java.math.BigInteger;
/*    */ import java.security.KeyPair;
/*    */ import java.security.PrivateKey;
/*    */ import java.security.Provider;
/*    */ import java.security.SecureRandom;
/*    */ import java.security.cert.X509Certificate;
/*    */ import org.bouncycastle.asn1.x500.X500Name;
/*    */ import org.bouncycastle.cert.X509CertificateHolder;
/*    */ import org.bouncycastle.cert.X509v3CertificateBuilder;
/*    */ import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
/*    */ import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
/*    */ import org.bouncycastle.jce.provider.BouncyCastleProvider;
/*    */ import org.bouncycastle.operator.ContentSigner;
/*    */ import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
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
/*    */ 
/*    */ 
/*    */ final class BouncyCastleSelfSignedCertGenerator
/*    */ {
/* 42 */   private static final Provider PROVIDER = new BouncyCastleProvider();
/*    */   
/*    */   static String[] generate(String fqdn, KeyPair keypair, SecureRandom random) throws Exception {
/* 45 */     PrivateKey key = keypair.getPrivate();
/*    */     
/*    */ 
/* 48 */     X500Name owner = new X500Name("CN=" + fqdn);
/* 49 */     X509v3CertificateBuilder builder = new JcaX509v3CertificateBuilder(owner, new BigInteger(64, random), SelfSignedCertificate.NOT_BEFORE, SelfSignedCertificate.NOT_AFTER, owner, keypair.getPublic());
/*    */     
/*    */ 
/* 52 */     ContentSigner signer = new JcaContentSignerBuilder("SHA256WithRSAEncryption").build(key);
/* 53 */     X509CertificateHolder certHolder = builder.build(signer);
/* 54 */     X509Certificate cert = new JcaX509CertificateConverter().setProvider(PROVIDER).getCertificate(certHolder);
/* 55 */     cert.verify(keypair.getPublic());
/*    */     
/* 57 */     return SelfSignedCertificate.newSelfSignedCertificate(fqdn, key, cert);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\util\BouncyCastleSelfSignedCertGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */