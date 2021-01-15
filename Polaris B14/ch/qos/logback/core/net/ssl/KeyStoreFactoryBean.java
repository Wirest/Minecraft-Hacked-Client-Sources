/*     */ package ch.qos.logback.core.net.ssl;
/*     */ 
/*     */ import ch.qos.logback.core.util.LocationUtil;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
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
/*     */ public class KeyStoreFactoryBean
/*     */ {
/*     */   private String location;
/*     */   private String provider;
/*     */   private String type;
/*     */   private String password;
/*     */   
/*     */   public KeyStore createKeyStore()
/*     */     throws NoSuchProviderException, NoSuchAlgorithmException, KeyStoreException
/*     */   {
/*  58 */     if (getLocation() == null) {
/*  59 */       throw new IllegalArgumentException("location is required");
/*     */     }
/*     */     
/*  62 */     InputStream inputStream = null;
/*     */     try {
/*  64 */       URL url = LocationUtil.urlForResource(getLocation());
/*  65 */       inputStream = url.openStream();
/*  66 */       KeyStore keyStore = newKeyStore();
/*  67 */       keyStore.load(inputStream, getPassword().toCharArray());
/*  68 */       return keyStore;
/*     */     }
/*     */     catch (NoSuchProviderException ex) {
/*  71 */       throw new NoSuchProviderException("no such keystore provider: " + getProvider());
/*     */     }
/*     */     catch (NoSuchAlgorithmException ex)
/*     */     {
/*  75 */       throw new NoSuchAlgorithmException("no such keystore type: " + getType());
/*     */     }
/*     */     catch (FileNotFoundException ex)
/*     */     {
/*  79 */       throw new KeyStoreException(getLocation() + ": file not found");
/*     */     }
/*     */     catch (Exception ex) {
/*  82 */       throw new KeyStoreException(getLocation() + ": " + ex.getMessage(), ex);
/*     */     }
/*     */     finally {
/*     */       try {
/*  86 */         if (inputStream != null) {
/*  87 */           inputStream.close();
/*     */         }
/*     */       }
/*     */       catch (IOException ex) {
/*  91 */         ex.printStackTrace(System.err);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private KeyStore newKeyStore()
/*     */     throws NoSuchAlgorithmException, NoSuchProviderException, KeyStoreException
/*     */   {
/* 103 */     return getProvider() != null ? KeyStore.getInstance(getType(), getProvider()) : KeyStore.getInstance(getType());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getLocation()
/*     */   {
/* 113 */     return this.location;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLocation(String location)
/*     */   {
/* 123 */     this.location = location;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getType()
/*     */   {
/* 132 */     if (this.type == null) {
/* 133 */       return "JKS";
/*     */     }
/* 135 */     return this.type;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setType(String type)
/*     */   {
/* 146 */     this.type = type;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getProvider()
/*     */   {
/* 154 */     return this.provider;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setProvider(String provider)
/*     */   {
/* 163 */     this.provider = provider;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getPassword()
/*     */   {
/* 172 */     if (this.password == null) {
/* 173 */       return "changeit";
/*     */     }
/* 175 */     return this.password;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPassword(String password)
/*     */   {
/* 183 */     this.password = password;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\net\ssl\KeyStoreFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */