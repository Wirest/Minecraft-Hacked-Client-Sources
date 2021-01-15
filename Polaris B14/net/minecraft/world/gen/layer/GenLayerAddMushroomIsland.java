/*    */ package net.minecraft.world.gen.layer;
/*    */ 
/*    */ import net.minecraft.world.biome.BiomeGenBase;
/*    */ 
/*    */ public class GenLayerAddMushroomIsland extends GenLayer
/*    */ {
/*    */   public GenLayerAddMushroomIsland(long p_i2120_1_, GenLayer p_i2120_3_)
/*    */   {
/*  9 */     super(p_i2120_1_);
/* 10 */     this.parent = p_i2120_3_;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
/*    */   {
/* 19 */     int i = areaX - 1;
/* 20 */     int j = areaY - 1;
/* 21 */     int k = areaWidth + 2;
/* 22 */     int l = areaHeight + 2;
/* 23 */     int[] aint = this.parent.getInts(i, j, k, l);
/* 24 */     int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);
/*    */     
/* 26 */     for (int i1 = 0; i1 < areaHeight; i1++)
/*    */     {
/* 28 */       for (int j1 = 0; j1 < areaWidth; j1++)
/*    */       {
/* 30 */         int k1 = aint[(j1 + 0 + (i1 + 0) * k)];
/* 31 */         int l1 = aint[(j1 + 2 + (i1 + 0) * k)];
/* 32 */         int i2 = aint[(j1 + 0 + (i1 + 2) * k)];
/* 33 */         int j2 = aint[(j1 + 2 + (i1 + 2) * k)];
/* 34 */         int k2 = aint[(j1 + 1 + (i1 + 1) * k)];
/* 35 */         initChunkSeed(j1 + areaX, i1 + areaY);
/*    */         
/* 37 */         if ((k2 == 0) && (k1 == 0) && (l1 == 0) && (i2 == 0) && (j2 == 0) && (nextInt(100) == 0))
/*    */         {
/* 39 */           aint1[(j1 + i1 * areaWidth)] = BiomeGenBase.mushroomIsland.biomeID;
/*    */         }
/*    */         else
/*    */         {
/* 43 */           aint1[(j1 + i1 * areaWidth)] = k2;
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 48 */     return aint1;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\layer\GenLayerAddMushroomIsland.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */