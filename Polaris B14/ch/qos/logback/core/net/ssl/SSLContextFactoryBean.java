/*     */ package ch.qos.logback.core.net.ssl;
/*     */ 
/*     */ import ch.qos.logback.core.spi.ContextAware;
/*     */ import java.security.KeyManagementException;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.UnrecoverableKeyException;
/*     */ import java.security.cert.CertificateException;
/*     */ import javax.net.ssl.KeyManager;
/*     */ import javax.net.ssl.KeyManagerFactory;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.TrustManagerFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SSLContextFactoryBean
/*     */ {
/*     */   private static final String JSSE_KEY_STORE_PROPERTY = "javax.net.ssl.keyStore";
/*     */   private static final String JSSE_TRUST_STORE_PROPERTY = "javax.net.ssl.trustStore";
/*     */   private KeyStoreFactoryBean keyStore;
/*     */   private KeyStoreFactoryBean trustStore;
/*     */   private SecureRandomFactoryBean secureRandom;
/*     */   private KeyManagerFactoryFactoryBean keyManagerFactory;
/*     */   private TrustManagerFactoryFactoryBean trustManagerFactory;
/*     */   private String protocol;
/*     */   private String provider;
/*     */   
/*     */   public SSLContext createContext(ContextAware context)
/*     */     throws NoSuchProviderException, NoSuchAlgorithmException, KeyManagementException, UnrecoverableKeyException, KeyStoreException, CertificateException
/*     */   {
/*  78 */     SSLContext sslContext = getProvider() != null ? SSLContext.getInstance(getProtocol(), getProvider()) : SSLContext.getInstance(getProtocol());
/*     */     
/*     */ 
/*     */ 
/*  82 */     context.addInfo("SSL protocol '" + sslContext.getProtocol() + "' provider '" + sslContext.getProvider() + "'");
/*     */     
/*     */ 
/*  85 */     KeyManager[] keyManagers = createKeyManagers(context);
/*  86 */     TrustManager[] trustManagers = createTrustManagers(context);
/*  87 */     SecureRandom secureRandom = createSecureRandom(context);
/*  88 */     sslContext.init(keyManagers, trustManagers, secureRandom);
/*  89 */     return sslContext;
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
/*     */   private KeyManager[] createKeyManagers(ContextAware context)
/*     */     throws NoSuchProviderException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException
/*     */   {
/* 108 */     if (getKeyStore() == null) { return null;
/*     */     }
/* 110 */     KeyStore keyStore = getKeyStore().createKeyStore();
/* 111 */     context.addInfo("key store of type '" + keyStore.getType() + "' provider '" + keyStore.getProvider() + "': " + getKeyStore().getLocation());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 116 */     KeyManagerFactory kmf = getKeyManagerFactory().createKeyManagerFactory();
/* 117 */     context.addInfo("key manager algorithm '" + kmf.getAlgorithm() + "' provider '" + kmf.getProvider() + "'");
/*     */     
/*     */ 
/* 120 */     char[] passphrase = getKeyStore().getPassword().toCharArray();
/* 121 */     kmf.init(keyStore, passphrase);
/* 122 */     return kmf.getKeyManagers();
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
/*     */   private TrustManager[] createTrustManagers(ContextAware context)
/*     */     throws NoSuchProviderException, NoSuchAlgorithmException, KeyStoreException
/*     */   {
/* 142 */     if (getTrustStore() == null) { return null;
/*     */     }
/* 144 */     KeyStore trustStore = getTrustStore().createKeyStore();
/* 145 */     context.addInfo("trust store of type '" + trustStore.getType() + "' provider '" + trustStore.getProvider() + "': " + getTrustStore().getLocation());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 150 */     TrustManagerFactory tmf = getTrustManagerFactory().createTrustManagerFactory();
/*     */     
/* 152 */     context.addInfo("trust manager algorithm '" + tmf.getAlgorithm() + "' provider '" + tmf.getProvider() + "'");
/*     */     
/*     */ 
/* 155 */     tmf.init(trustStore);
/* 156 */     return tmf.getTrustManagers();
/*     */   }
/*     */   
/*     */   private SecureRandom createSecureRandom(ContextAware context)
/*     */     throws NoSuchProviderException, NoSuchAlgorithmException
/*     */   {
/* 162 */     SecureRandom secureRandom = getSecureRandom().createSecureRandom();
/* 163 */     context.addInfo("secure random algorithm '" + secureRandom.getAlgorithm() + "' provider '" + secureRandom.getProvider() + "'");
/*     */     
/*     */ 
/* 166 */     return secureRandom;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public KeyStoreFactoryBean getKeyStore()
/*     */   {
/* 175 */     if (this.keyStore == null) {
/* 176 */       this.keyStore = keyStoreFromSystemProperties("javax.net.ssl.keyStore");
/*     */     }
/* 178 */     return this.keyStore;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setKeyStore(KeyStoreFactoryBean keyStore)
/*     */   {
/* 186 */     this.keyStore = keyStore;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public KeyStoreFactoryBean getTrustStore()
/*     */   {
/* 195 */     if (this.trustStore == null) {
/* 196 */       this.trustStore = keyStoreFromSystemProperties("javax.net.ssl.trustStore");
/*     */     }
/* 198 */     return this.trustStore;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTrustStore(KeyStoreFactoryBean trustStore)
/*     */   {
/* 206 */     this.trustStore = trustStore;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private KeyStoreFactoryBean keyStoreFromSystemProperties(String property)
/*     */   {
/* 216 */     if (System.getProperty(property) == null) return null;
/* 217 */     KeyStoreFactoryBean keyStore = new KeyStoreFactoryBean();
/* 218 */     keyStore.setLocation(locationFromSystemProperty(property));
/* 219 */     keyStore.setProvider(System.getProperty(property + "Provider"));
/* 220 */     keyStore.setPassword(System.getProperty(property + "Password"));
/* 221 */     keyStore.setType(System.getProperty(property + "Type"));
/* 222 */     return keyStore;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String locationFromSystemProperty(String name)
/*     */   {
/* 232 */     String location = System.getProperty(name);
/* 233 */     if ((location != null) && (!location.startsWith("file:"))) {
/* 234 */       location = "file:" + location;
/*     */     }
/* 236 */     return location;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SecureRandomFactoryBean getSecureRandom()
/*     */   {
/* 245 */     if (this.secureRandom == null) {
/* 246 */       return new SecureRandomFactoryBean();
/*     */     }
/* 248 */     return this.secureRandom;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSecureRandom(SecureRandomFactoryBean secureRandom)
/*     */   {
/* 256 */     this.secureRandom = secureRandom;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public KeyManagerFactoryFactoryBean getKeyManagerFactory()
/*     */   {
/* 265 */     if (this.keyManagerFactory == null) {
/* 266 */       return new KeyManagerFactoryFactoryBean();
/*     */     }
/* 268 */     return this.keyManagerFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setKeyManagerFactory(KeyManagerFactoryFactoryBean keyManagerFactory)
/*     */   {
/* 277 */     this.keyManagerFactory = keyManagerFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TrustManagerFactoryFactoryBean getTrustManagerFactory()
/*     */   {
/* 286 */     if (this.trustManagerFactory == null) {
/* 287 */       return new TrustManagerFactoryFactoryBean();
/*     */     }
/* 289 */     return this.trustManagerFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTrustManagerFactory(TrustManagerFactoryFactoryBean trustManagerFactory)
/*     */   {
/* 298 */     this.trustManagerFactory = trustManagerFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getProtocol()
/*     */   {
/* 308 */     if (this.protocol == null) {
/* 309 */       return "SSL";
/*     */     }
/* 311 */     return this.protocol;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setProtocol(String protocol)
/*     */   {
/* 321 */     this.protocol = protocol;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getProvider()
/*     */   {
/* 329 */     return this.provider;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setProvider(String provider)
/*     */   {
/* 338 */     this.provider = provider;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\net\ssl\SSLContextFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */