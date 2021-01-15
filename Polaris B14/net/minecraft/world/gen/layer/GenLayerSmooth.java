/*    */ package net.minecraft.world.gen.layer;
/*    */ 
/*    */ public class GenLayerSmooth extends GenLayer
/*    */ {
/*    */   public GenLayerSmooth(long p_i2131_1_, GenLayer p_i2131_3_)
/*    */   {
/*  7 */     super(p_i2131_1_);
/*  8 */     this.parent = p_i2131_3_;
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
/* 28 */         int k1 = aint[(j1 + 0 + (i1 + 1) * k)];
/* 29 */         int l1 = aint[(j1 + 2 + (i1 + 1) * k)];
/* 30 */         int i2 = aint[(j1 + 1 + (i1 + 0) * k)];
/* 31 */         int j2 = aint[(j1 + 1 + (i1 + 2) * k)];
/* 32 */         int k2 = aint[(j1 + 1 + (i1 + 1) * k)];
/*    */         
/* 34 */         if ((k1 == l1) && (i2 == j2))
/*    */         {
/* 36 */           initChunkSeed(j1 + areaX, i1 + areaY);
/*    */           
/* 38 */           if (nextInt(2) == 0)
/*    */           {
/* 40 */             k2 = k1;
/*    */           }
/*    */           else
/*    */           {
/* 44 */             k2 = i2;
/*    */           }
/*    */         }
/*    */         else
/*    */         {
/* 49 */           if (k1 == l1)
/*    */           {
/* 51 */             k2 = k1;
/*    */           }
/*    */           
/* 54 */           if (i2 == j2)
/*    */           {
/* 56 */             k2 = i2;
/*    */           }
/*    */         }
/*    */         
/* 60 */         aint1[(j1 + i1 * areaWidth)] = k2;
/*    */       }
/*    */     }
/*    */     
/* 64 */     return aint1;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\layer\GenLayerSmooth.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */