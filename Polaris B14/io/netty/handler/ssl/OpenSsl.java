/*     */ package io.netty.handler.ssl;
/*     */ 
/*     */ import io.netty.util.internal.NativeLibraryLoader;
/*     */ import io.netty.util.internal.logging.InternalLogger;
/*     */ import io.netty.util.internal.logging.InternalLoggerFactory;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.apache.tomcat.jni.Library;
/*     */ import org.apache.tomcat.jni.Pool;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class OpenSsl
/*     */ {
/*  37 */   private static final InternalLogger logger = InternalLoggerFactory.getInstance(OpenSsl.class);
/*     */   private static final Throwable UNAVAILABILITY_CAUSE;
/*     */   private static final Set<String> AVAILABLE_CIPHER_SUITES;
/*     */   
/*     */   static
/*     */   {
/*  43 */     Throwable cause = null;
/*     */     
/*     */     try
/*     */     {
/*  47 */       Class.forName("org.apache.tomcat.jni.SSL", false, OpenSsl.class.getClassLoader());
/*     */     } catch (ClassNotFoundException t) {
/*  49 */       cause = t;
/*  50 */       logger.debug("netty-tcnative not in the classpath; " + OpenSslEngine.class.getSimpleName() + " will be unavailable.");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  56 */     if (cause == null) {
/*     */       try {
/*  58 */         NativeLibraryLoader.load("netty-tcnative", SSL.class.getClassLoader());
/*  59 */         Library.initialize("provided");
/*  60 */         SSL.initialize(null);
/*     */       } catch (Throwable t) {
/*  62 */         cause = t;
/*  63 */         logger.debug("Failed to load netty-tcnative; " + OpenSslEngine.class.getSimpleName() + " will be unavailable. " + "See http://netty.io/wiki/forked-tomcat-native.html for more information.", t);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  70 */     UNAVAILABILITY_CAUSE = cause;
/*     */     
/*  72 */     if (cause == null) {
/*  73 */       Set<String> availableCipherSuites = new LinkedHashSet(128);
/*  74 */       long aprPool = Pool.create(0L);
/*     */       try {
/*  76 */         long sslCtx = SSLContext.make(aprPool, 28, 1);
/*     */         try {
/*  78 */           SSLContext.setOptions(sslCtx, 4095);
/*  79 */           SSLContext.setCipherSuite(sslCtx, "ALL");
/*  80 */           long ssl = SSL.newSSL(sslCtx, true);
/*     */           try {
/*  82 */             for (String c : SSL.getCiphers(ssl))
/*     */             {
/*  84 */               if ((c != null) && (c.length() != 0) && (!availableCipherSuites.contains(c)))
/*     */               {
/*     */ 
/*  87 */                 availableCipherSuites.add(c);
/*     */               }
/*     */             }
/*     */           }
/*     */           finally {}
/*     */         } finally {
/*  93 */           SSLContext.free(sslCtx);
/*     */         }
/*     */       } catch (Exception e) {
/*  96 */         logger.warn("Failed to get the list of available OpenSSL cipher suites.", e);
/*     */       } finally {
/*  98 */         Pool.destroy(aprPool);
/*     */       }
/*     */       
/* 101 */       AVAILABLE_CIPHER_SUITES = Collections.unmodifiableSet(availableCipherSuites);
/*     */     } else {
/* 103 */       AVAILABLE_CIPHER_SUITES = Collections.emptySet();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isAvailable()
/*     */   {
/* 113 */     return UNAVAILABILITY_CAUSE == null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void ensureAvailability()
/*     */   {
/* 123 */     if (UNAVAILABILITY_CAUSE != null) {
/* 124 */       throw ((Error)new UnsatisfiedLinkError("failed to load the required native library").initCause(UNAVAILABILITY_CAUSE));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Throwable unavailabilityCause()
/*     */   {
/* 136 */     return UNAVAILABILITY_CAUSE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Set<String> availableCipherSuites()
/*     */   {
/* 144 */     return AVAILABLE_CIPHER_SUITES;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isCipherSuiteAvailable(String cipherSuite)
/*     */   {
/* 152 */     String converted = CipherSuiteConverter.toOpenSsl(cipherSuite);
/* 153 */     if (converted != null) {
/* 154 */       cipherSuite = converted;
/*     */     }
/* 156 */     return AVAILABLE_CIPHER_SUITES.contains(cipherSuite);
/*     */   }
/*     */   
/*     */   static boolean isError(long errorCode) {
/* 160 */     return errorCode != 0L;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\OpenSsl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */