/*    */ package io.netty.handler.codec.http2;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
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
/*    */ public final class Http2SecurityUtil
/*    */ {
/*    */   public static final List<String> CIPHERS;
/* 37 */   private static final List<String> CIPHERS_JAVA_MOZILLA_INCREASED_SECURITY = Collections.unmodifiableList(Arrays.asList(new String[] { "TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384", "TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256", "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384", "TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256", "TLS_DHE_RSA_WITH_AES_128_GCM_SHA256", "TLS_DHE_DSS_WITH_AES_128_GCM_SHA256" }));
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
/* 49 */   private static final List<String> CIPHERS_JAVA_NO_MOZILLA_INCREASED_SECURITY = Collections.unmodifiableList(Arrays.asList(new String[] { "TLS_DHE_RSA_WITH_AES_256_GCM_SHA384", "TLS_DHE_DSS_WITH_AES_256_GCM_SHA384" }));
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   static
/*    */   {
/* 56 */     List<String> ciphers = new ArrayList(CIPHERS_JAVA_MOZILLA_INCREASED_SECURITY.size() + CIPHERS_JAVA_NO_MOZILLA_INCREASED_SECURITY.size());
/*    */     
/* 58 */     ciphers.addAll(CIPHERS_JAVA_MOZILLA_INCREASED_SECURITY);
/* 59 */     ciphers.addAll(CIPHERS_JAVA_NO_MOZILLA_INCREASED_SECURITY);
/* 60 */     CIPHERS = Collections.unmodifiableList(ciphers);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\Http2SecurityUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */