/*    */ package net.minecraft.world.gen.layer;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ 
/*    */ public class IntCache
/*    */ {
/*  8 */   private static int intCacheSize = 256;
/*  9 */   private static List<int[]> freeSmallArrays = Lists.newArrayList();
/* 10 */   private static List<int[]> inUseSmallArrays = Lists.newArrayList();
/* 11 */   private static List<int[]> freeLargeArrays = Lists.newArrayList();
/* 12 */   private static List<int[]> inUseLargeArrays = Lists.newArrayList();
/*    */   
/*    */   public static synchronized int[] getIntCache(int p_76445_0_)
/*    */   {
/* 16 */     if (p_76445_0_ <= 256)
/*    */     {
/* 18 */       if (freeSmallArrays.isEmpty())
/*    */       {
/* 20 */         int[] aint4 = new int['Ā'];
/* 21 */         inUseSmallArrays.add(aint4);
/* 22 */         return aint4;
/*    */       }
/*    */       
/*    */ 
/* 26 */       int[] aint3 = (int[])freeSmallArrays.remove(freeSmallArrays.size() - 1);
/* 27 */       inUseSmallArrays.add(aint3);
/* 28 */       return aint3;
/*    */     }
/*    */     
/* 31 */     if (p_76445_0_ > intCacheSize)
/*    */     {
/* 33 */       intCacheSize = p_76445_0_;
/* 34 */       freeLargeArrays.clear();
/* 35 */       inUseLargeArrays.clear();
/* 36 */       int[] aint2 = new int[intCacheSize];
/* 37 */       inUseLargeArrays.add(aint2);
/* 38 */       return aint2;
/*    */     }
/* 40 */     if (freeLargeArrays.isEmpty())
/*    */     {
/* 42 */       int[] aint1 = new int[intCacheSize];
/* 43 */       inUseLargeArrays.add(aint1);
/* 44 */       return aint1;
/*    */     }
/*    */     
/*    */ 
/* 48 */     int[] aint = (int[])freeLargeArrays.remove(freeLargeArrays.size() - 1);
/* 49 */     inUseLargeArrays.add(aint);
/* 50 */     return aint;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static synchronized void resetIntCache()
/*    */   {
/* 59 */     if (!freeLargeArrays.isEmpty())
/*    */     {
/* 61 */       freeLargeArrays.remove(freeLargeArrays.size() - 1);
/*    */     }
/*    */     
/* 64 */     if (!freeSmallArrays.isEmpty())
/*    */     {
/* 66 */       freeSmallArrays.remove(freeSmallArrays.size() - 1);
/*    */     }
/*    */     
/* 69 */     freeLargeArrays.addAll(inUseLargeArrays);
/* 70 */     freeSmallArrays.addAll(inUseSmallArrays);
/* 71 */     inUseLargeArrays.clear();
/* 72 */     inUseSmallArrays.clear();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static synchronized String getCacheSizes()
/*    */   {
/* 81 */     return "cache: " + freeLargeArrays.size() + ", tcache: " + freeSmallArrays.size() + ", allocated: " + inUseLargeArrays.size() + ", tallocated: " + inUseSmallArrays.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\layer\IntCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */