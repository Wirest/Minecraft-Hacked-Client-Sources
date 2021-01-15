/*    */ package net.minecraft.world.gen.layer;
/*    */ 
/*    */ public class GenLayerRemoveTooMuchOcean extends GenLayer
/*    */ {
/*    */   public GenLayerRemoveTooMuchOcean(long p_i45480_1_, GenLayer p_i45480_3_)
/*    */   {
/*  7 */     super(p_i45480_1_);
/*  8 */     this.parent = p_i45480_3_;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
/*    */   {
/* 17 */     int i = areaX - 1;
/* 18 */     int j = areaY - 1;
/* 19 */     int k = areaWidth + 2;
/* 20 */     int l = areaHeight + 2;
/* 21 */     int[] aint = this.parent.getInts(i, j, k, l);
/* 22 */     int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);
/*    */     
/* 24 */     for (int i1 = 0; i1 < areaHeight; i1++)
/*    */     {
/* 26 */       for (int j1 = 0; j1 < areaWidth; j1++)
/*    */       {
/* 28 */         int k1 = aint[(j1 + 1 + (i1 + 1 - 1) * (areaWidth + 2))];
/* 29 */         int l1 = aint[(j1 + 1 + 1 + (i1 + 1) * (areaWidth + 2))];
/* 30 */         int i2 = aint[(j1 + 1 - 1 + (i1 + 1) * (areaWidth + 2))];
/* 31 */         int j2 = aint[(j1 + 1 + (i1 + 1 + 1) * (areaWidth + 2))];
/* 32 */         int k2 = aint[(j1 + 1 + (i1 + 1) * k)];
/* 33 */         aint1[(j1 + i1 * areaWidth)] = k2;
/* 34 */         initChunkSeed(j1 + areaX, i1 + areaY);
/*    */         
/* 36 */         if ((k2 == 0) && (k1 == 0) && (l1 == 0) && (i2 == 0) && (j2 == 0) && (nextInt(2) == 0))
/*    */         {
/* 38 */           aint1[(j1 + i1 * areaWidth)] = 1;
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 43 */     return aint1;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\layer\GenLayerRemoveTooMuchOcean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */