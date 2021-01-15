/*    */ package io.netty.handler.ssl;
/*    */ 
/*    */ import java.util.ArrayList;
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
/*    */ final class ApplicationProtocolUtil
/*    */ {
/*    */   private static final int DEFAULT_LIST_SIZE = 2;
/*    */   
/*    */   static List<String> toList(Iterable<String> protocols)
/*    */   {
/* 31 */     return toList(2, protocols);
/*    */   }
/*    */   
/*    */   static List<String> toList(int initialListSize, Iterable<String> protocols) {
/* 35 */     if (protocols == null) {
/* 36 */       return null;
/*    */     }
/*    */     
/* 39 */     List<String> result = new ArrayList(initialListSize);
/* 40 */     for (String p : protocols) {
/* 41 */       if ((p == null) || (p.isEmpty())) {
/* 42 */         throw new IllegalArgumentException("protocol cannot be null or empty");
/*    */       }
/* 44 */       result.add(p);
/*    */     }
/*    */     
/* 47 */     if (result.isEmpty()) {
/* 48 */       throw new IllegalArgumentException("protocols cannot empty");
/*    */     }
/*    */     
/* 51 */     return result;
/*    */   }
/*    */   
/*    */   static List<String> toList(String... protocols) {
/* 55 */     return toList(2, protocols);
/*    */   }
/*    */   
/*    */   static List<String> toList(int initialListSize, String... protocols) {
/* 59 */     if (protocols == null) {
/* 60 */       return null;
/*    */     }
/*    */     
/* 63 */     List<String> result = new ArrayList(initialListSize);
/* 64 */     for (String p : protocols) {
/* 65 */       if ((p == null) || (p.isEmpty())) {
/* 66 */         throw new IllegalArgumentException("protocol cannot be null or empty");
/*    */       }
/* 68 */       result.add(p);
/*    */     }
/*    */     
/* 71 */     if (result.isEmpty()) {
/* 72 */       throw new IllegalArgumentException("protocols cannot empty");
/*    */     }
/*    */     
/* 75 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\ApplicationProtocolUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */