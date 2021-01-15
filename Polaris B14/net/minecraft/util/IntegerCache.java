/*    */ package net.minecraft.util;
/*    */ 
/*    */ public class IntegerCache
/*    */ {
/*  5 */   private static final Integer[] field_181757_a = new Integer[65535];
/*    */   
/*    */   public static Integer func_181756_a(int p_181756_0_)
/*    */   {
/*  9 */     return (p_181756_0_ > 0) && (p_181756_0_ < field_181757_a.length) ? field_181757_a[p_181756_0_] : Integer.valueOf(p_181756_0_);
/*    */   }
/*    */   
/*    */   static
/*    */   {
/* 14 */     int i = 0;
/*    */     
/* 16 */     for (int j = field_181757_a.length; i < j; i++)
/*    */     {
/* 18 */       field_181757_a[i] = Integer.valueOf(i);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\IntegerCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */