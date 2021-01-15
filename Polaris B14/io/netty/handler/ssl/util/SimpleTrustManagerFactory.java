/*     */ package io.netty.handler.ssl.util;
/*     */ 
/*     */ import io.netty.util.concurrent.FastThreadLocal;
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.Provider;
/*     */ import javax.net.ssl.ManagerFactoryParameters;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.TrustManagerFactory;
/*     */ import javax.net.ssl.TrustManagerFactorySpi;
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
/*     */ public abstract class SimpleTrustManagerFactory
/*     */   extends TrustManagerFactory
/*     */ {
/*  35 */   private static final Provider PROVIDER = new Provider("", 0.0D, "")
/*     */   {
/*     */     private static final long serialVersionUID = -2680540247105807895L;
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  47 */   private static final FastThreadLocal<SimpleTrustManagerFactorySpi> CURRENT_SPI = new FastThreadLocal()
/*     */   {
/*     */     protected SimpleTrustManagerFactory.SimpleTrustManagerFactorySpi initialValue()
/*     */     {
/*  51 */       return new SimpleTrustManagerFactory.SimpleTrustManagerFactorySpi();
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */   protected SimpleTrustManagerFactory()
/*     */   {
/*  59 */     this("");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SimpleTrustManagerFactory(String name)
/*     */   {
/*  68 */     super((TrustManagerFactorySpi)CURRENT_SPI.get(), PROVIDER, name);
/*  69 */     ((SimpleTrustManagerFactorySpi)CURRENT_SPI.get()).init(this);
/*  70 */     CURRENT_SPI.remove();
/*     */     
/*  72 */     if (name == null) {
/*  73 */       throw new NullPointerException("name");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void engineInit(KeyStore paramKeyStore)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract void engineInit(ManagerFactoryParameters paramManagerFactoryParameters)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */   protected abstract TrustManager[] engineGetTrustManagers();
/*     */   
/*     */ 
/*     */ 
/*     */   static final class SimpleTrustManagerFactorySpi
/*     */     extends TrustManagerFactorySpi
/*     */   {
/*     */     private SimpleTrustManagerFactory parent;
/*     */     
/*     */ 
/*     */ 
/*     */     void init(SimpleTrustManagerFactory parent)
/*     */     {
/* 103 */       this.parent = parent;
/*     */     }
/*     */     
/*     */     protected void engineInit(KeyStore keyStore) throws KeyStoreException
/*     */     {
/*     */       try {
/* 109 */         this.parent.engineInit(keyStore);
/*     */       } catch (KeyStoreException e) {
/* 111 */         throw e;
/*     */       } catch (Exception e) {
/* 113 */         throw new KeyStoreException(e);
/*     */       }
/*     */     }
/*     */     
/*     */     protected void engineInit(ManagerFactoryParameters managerFactoryParameters) throws InvalidAlgorithmParameterException
/*     */     {
/*     */       try
/*     */       {
/* 121 */         this.parent.engineInit(managerFactoryParameters);
/*     */       } catch (InvalidAlgorithmParameterException e) {
/* 123 */         throw e;
/*     */       } catch (Exception e) {
/* 125 */         throw new InvalidAlgorithmParameterException(e);
/*     */       }
/*     */     }
/*     */     
/*     */     protected TrustManager[] engineGetTrustManagers()
/*     */     {
/* 131 */       return this.parent.engineGetTrustManagers();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\util\SimpleTrustManagerFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */