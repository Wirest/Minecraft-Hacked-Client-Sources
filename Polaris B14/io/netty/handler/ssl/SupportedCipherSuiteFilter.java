/*    */ package io.netty.handler.ssl;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Set;
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
/*    */ public final class SupportedCipherSuiteFilter
/*    */   implements CipherSuiteFilter
/*    */ {
/* 27 */   public static final SupportedCipherSuiteFilter INSTANCE = new SupportedCipherSuiteFilter();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String[] filterCipherSuites(Iterable<String> ciphers, List<String> defaultCiphers, Set<String> supportedCiphers)
/*    */   {
/* 34 */     if (defaultCiphers == null) {
/* 35 */       throw new NullPointerException("defaultCiphers");
/*    */     }
/* 37 */     if (supportedCiphers == null) {
/* 38 */       throw new NullPointerException("supportedCiphers");
/*    */     }
/*    */     
/*    */     List<String> newCiphers;
/* 42 */     if (ciphers == null) {
/* 43 */       List<String> newCiphers = new ArrayList(defaultCiphers.size());
/* 44 */       ciphers = defaultCiphers;
/*    */     } else {
/* 46 */       newCiphers = new ArrayList(supportedCiphers.size());
/*    */     }
/* 48 */     for (String c : ciphers) {
/* 49 */       if (c == null) {
/*    */         break;
/*    */       }
/* 52 */       if (supportedCiphers.contains(c)) {
/* 53 */         newCiphers.add(c);
/*    */       }
/*    */     }
/* 56 */     return (String[])newCiphers.toArray(new String[newCiphers.size()]);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\SupportedCipherSuiteFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */