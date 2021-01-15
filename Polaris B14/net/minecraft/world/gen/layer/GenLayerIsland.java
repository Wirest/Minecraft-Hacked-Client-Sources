/*    */ package net.minecraft.world.gen.layer;
/*    */ 
/*    */ public class GenLayerIsland extends GenLayer
/*    */ {
/*    */   public GenLayerIsland(long p_i2124_1_)
/*    */   {
/*  7 */     super(p_i2124_1_);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
/*    */   {
/* 16 */     int[] aint = IntCache.getIntCache(areaWidth * areaHeight);
/*    */     
/* 18 */     for (int i = 0; i < areaHeight; i++)
/*    */     {
/* 20 */       for (int j = 0; j < areaWidth; j++)
/*    */       {
/* 22 */         initChunkSeed(areaX + j, areaY + i);
/* 23 */         aint[(j + i * areaWidth)] = (nextInt(10) == 0 ? 1 : 0);
/*    */       }
/*    */     }
/*    */     
/* 27 */     if ((areaX > -areaWidth) && (areaX <= 0) && (areaY > -areaHeight) && (areaY <= 0))
/*    */     {
/* 29 */       aint[(-areaX + -areaY * areaWidth)] = 1;
/*    */     }
/*    */     
/* 32 */     return aint;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\layer\GenLayerIsland.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */