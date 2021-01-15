/*    */ package ch.qos.logback.core.net.ssl;
/*    */ 
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import java.security.NoSuchProviderException;
/*    */ import java.security.SecureRandom;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SecureRandomFactoryBean
/*    */ {
/*    */   private String algorithm;
/*    */   private String provider;
/*    */   
/*    */   public SecureRandom createSecureRandom()
/*    */     throws NoSuchProviderException, NoSuchAlgorithmException
/*    */   {
/*    */     try
/*    */     {
/* 47 */       return getProvider() != null ? SecureRandom.getInstance(getAlgorithm(), getProvider()) : SecureRandom.getInstance(getAlgorithm());
/*    */ 
/*    */     }
/*    */     catch (NoSuchProviderException ex)
/*    */     {
/* 52 */       throw new NoSuchProviderException("no such secure random provider: " + getProvider());
/*    */     }
/*    */     catch (NoSuchAlgorithmException ex)
/*    */     {
/* 56 */       throw new NoSuchAlgorithmException("no such secure random algorithm: " + getAlgorithm());
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getAlgorithm()
/*    */   {
/* 68 */     if (this.algorithm == null) {
/* 69 */       return "SHA1PRNG";
/*    */     }
/* 71 */     return this.algorithm;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setAlgorithm(String algorithm)
/*    */   {
/* 81 */     this.algorithm = algorithm;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getProvider()
/*    */   {
/* 89 */     return this.provider;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setProvider(String provider)
/*    */   {
/* 98 */     this.provider = provider;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\net\ssl\SecureRandomFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */