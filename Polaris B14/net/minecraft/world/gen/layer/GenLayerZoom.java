/*    */ package net.minecraft.world.gen.layer;
/*    */ 
/*    */ public class GenLayerZoom extends GenLayer
/*    */ {
/*    */   public GenLayerZoom(long p_i2134_1_, GenLayer p_i2134_3_)
/*    */   {
/*  7 */     super(p_i2134_1_);
/*  8 */     this.parent = p_i2134_3_;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
/*    */   {
/* 17 */     int i = areaX >> 1;
/* 18 */     int j = areaY >> 1;
/* 19 */     int k = (areaWidth >> 1) + 2;
/* 20 */     int l = (areaHeight >> 1) + 2;
/* 21 */     int[] aint = this.parent.getInts(i, j, k, l);
/* 22 */     int i1 = k - 1 << 1;
/* 23 */     int j1 = l - 1 << 1;
/* 24 */     int[] aint1 = IntCache.getIntCache(i1 * j1);
/*    */     
/* 26 */     for (int k1 = 0; k1 < l - 1; k1++)
/*    */     {
/* 28 */       int l1 = (k1 << 1) * i1;
/* 29 */       int i2 = 0;
/* 30 */       int j2 = aint[(i2 + 0 + (k1 + 0) * k)];
/*    */       
/* 32 */       for (int k2 = aint[(i2 + 0 + (k1 + 1) * k)]; i2 < k - 1; i2++)
/*    */       {
/* 34 */         initChunkSeed(i2 + i << 1, k1 + j << 1);
/* 35 */         int l2 = aint[(i2 + 1 + (k1 + 0) * k)];
/* 36 */         int i3 = aint[(i2 + 1 + (k1 + 1) * k)];
/* 37 */         aint1[l1] = j2;
/* 38 */         aint1[(l1++ + i1)] = selectRandom(new int[] { j2, k2 });
/* 39 */         aint1[l1] = selectRandom(new int[] { j2, l2 });
/* 40 */         aint1[(l1++ + i1)] = selectModeOrRandom(j2, l2, k2, i3);
/* 41 */         j2 = l2;
/* 42 */         k2 = i3;
/*    */       }
/*    */     }
/*    */     
/* 46 */     int[] aint2 = IntCache.getIntCache(areaWidth * areaHeight);
/*    */     
/* 48 */     for (int j3 = 0; j3 < areaHeight; j3++)
/*    */     {
/* 50 */       System.arraycopy(aint1, (j3 + (areaY & 0x1)) * i1 + (areaX & 0x1), aint2, j3 * areaWidth, areaWidth);
/*    */     }
/*    */     
/* 53 */     return aint2;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static GenLayer magnify(long p_75915_0_, GenLayer p_75915_2_, int p_75915_3_)
/*    */   {
/* 61 */     GenLayer genlayer = p_75915_2_;
/*    */     
/* 63 */     for (int i = 0; i < p_75915_3_; i++)
/*    */     {
/* 65 */       genlayer = new GenLayerZoom(p_75915_0_ + i, genlayer);
/*    */     }
/*    */     
/* 68 */     return genlayer;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\layer\GenLayerZoom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */