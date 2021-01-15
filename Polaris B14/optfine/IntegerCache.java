/*    */ package optfine;
/*    */ 
/*    */ public class IntegerCache
/*    */ {
/*    */   private static final int CACHE_SIZE = 4096;
/*  6 */   private static final Integer[] cache = makeCache(4096);
/*    */   
/*    */   private static Integer[] makeCache(int p_makeCache_0_)
/*    */   {
/* 10 */     Integer[] ainteger = new Integer[p_makeCache_0_];
/*    */     
/* 12 */     for (int i = 0; i < p_makeCache_0_; i++)
/*    */     {
/* 14 */       ainteger[i] = new Integer(i);
/*    */     }
/*    */     
/* 17 */     return ainteger;
/*    */   }
/*    */   
/*    */   public static Integer valueOf(int p_valueOf_0_)
/*    */   {
/* 22 */     return (p_valueOf_0_ >= 0) && (p_valueOf_0_ < 4096) ? cache[p_valueOf_0_] : new Integer(p_valueOf_0_);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\optfine\IntegerCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */