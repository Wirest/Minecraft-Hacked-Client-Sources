/*    */ package net.minecraft.world.gen.layer;
/*    */ 
/*    */ public class GenLayerAddSnow extends GenLayer
/*    */ {
/*    */   public GenLayerAddSnow(long p_i2121_1_, GenLayer p_i2121_3_)
/*    */   {
/*  7 */     super(p_i2121_1_);
/*  8 */     this.parent = p_i2121_3_;
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
/* 28 */         int k1 = aint[(j1 + 1 + (i1 + 1) * k)];
/* 29 */         initChunkSeed(j1 + areaX, i1 + areaY);
/*    */         
/* 31 */         if (k1 == 0)
/*    */         {
/* 33 */           aint1[(j1 + i1 * areaWidth)] = 0;
/*    */         }
/*    */         else
/*    */         {
/* 37 */           int l1 = nextInt(6);
/*    */           
/* 39 */           if (l1 == 0)
/*    */           {
/* 41 */             l1 = 4;
/*    */           }
/* 43 */           else if (l1 <= 1)
/*    */           {
/* 45 */             l1 = 3;
/*    */           }
/*    */           else
/*    */           {
/* 49 */             l1 = 1;
/*    */           }
/*    */           
/* 52 */           aint1[(j1 + i1 * areaWidth)] = l1;
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 57 */     return aint1;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\layer\GenLayerAddSnow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */