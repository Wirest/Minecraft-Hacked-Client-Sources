/*    */ package io.netty.handler.codec.http.multipart;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Comparator;
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
/*    */ final class CaseIgnoringComparator
/*    */   implements Comparator<CharSequence>, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 4582133183775373862L;
/* 25 */   static final CaseIgnoringComparator INSTANCE = new CaseIgnoringComparator();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int compare(CharSequence o1, CharSequence o2)
/*    */   {
/* 32 */     int o1Length = o1.length();
/* 33 */     int o2Length = o2.length();
/* 34 */     int min = Math.min(o1Length, o2Length);
/* 35 */     for (int i = 0; i < min; i++) {
/* 36 */       char c1 = o1.charAt(i);
/* 37 */       char c2 = o2.charAt(i);
/* 38 */       if (c1 != c2) {
/* 39 */         c1 = Character.toUpperCase(c1);
/* 40 */         c2 = Character.toUpperCase(c2);
/* 41 */         if (c1 != c2) {
/* 42 */           c1 = Character.toLowerCase(c1);
/* 43 */           c2 = Character.toLowerCase(c2);
/* 44 */           if (c1 != c2) {
/* 45 */             return c1 - c2;
/*    */           }
/*    */         }
/*    */       }
/*    */     }
/* 50 */     return o1Length - o2Length;
/*    */   }
/*    */   
/*    */   private Object readResolve() {
/* 54 */     return INSTANCE;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\multipart\CaseIgnoringComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */