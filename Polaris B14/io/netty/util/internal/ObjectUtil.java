/*    */ package io.netty.util.internal;
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
/*    */ public final class ObjectUtil
/*    */ {
/*    */   public static <T> T checkNotNull(T arg, String text)
/*    */   {
/* 30 */     if (arg == null) {
/* 31 */       throw new NullPointerException(text);
/*    */     }
/* 33 */     return arg;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\ObjectUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */