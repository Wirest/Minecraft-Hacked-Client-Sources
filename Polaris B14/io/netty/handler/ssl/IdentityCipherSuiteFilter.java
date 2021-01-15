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
/*    */ public final class IdentityCipherSuiteFilter
/*    */   implements CipherSuiteFilter
/*    */ {
/* 26 */   public static final IdentityCipherSuiteFilter INSTANCE = new IdentityCipherSuiteFilter();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String[] filterCipherSuites(Iterable<String> ciphers, List<String> defaultCiphers, Set<String> supportedCiphers)
/*    */   {
/* 33 */     if (ciphers == null) {
/* 34 */       return (String[])defaultCiphers.toArray(new String[defaultCiphers.size()]);
/*    */     }
/* 36 */     List<String> newCiphers = new ArrayList(supportedCiphers.size());
/* 37 */     for (String c : ciphers) {
/* 38 */       if (c == null) {
/*    */         break;
/*    */       }
/* 41 */       newCiphers.add(c);
/*    */     }
/* 43 */     return (String[])newCiphers.toArray(new String[newCiphers.size()]);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\IdentityCipherSuiteFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */