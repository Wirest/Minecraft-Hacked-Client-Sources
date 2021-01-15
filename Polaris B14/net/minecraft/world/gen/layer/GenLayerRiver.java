/*    */ package net.minecraft.world.gen.layer;
/*    */ 
/*    */ import net.minecraft.world.biome.BiomeGenBase;
/*    */ 
/*    */ public class GenLayerRiver extends GenLayer
/*    */ {
/*    */   public GenLayerRiver(long p_i2128_1_, GenLayer p_i2128_3_)
/*    */   {
/*  9 */     super(p_i2128_1_);
/* 10 */     this.parent = p_i2128_3_;
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
/* 30 */         int k1 = func_151630_c(aint[(j1 + 0 + (i1 + 1) * k)]);
/* 31 */         int l1 = func_151630_c(aint[(j1 + 2 + (i1 + 1) * k)]);
/* 32 */         int i2 = func_151630_c(aint[(j1 + 1 + (i1 + 0) * k)]);
/* 33 */         int j2 = func_151630_c(aint[(j1 + 1 + (i1 + 2) * k)]);
/* 34 */         int k2 = func_151630_c(aint[(j1 + 1 + (i1 + 1) * k)]);
/*    */         
/* 36 */         if ((k2 == k1) && (k2 == i2) && (k2 == l1) && (k2 == j2))
/*    */         {
/* 38 */           aint1[(j1 + i1 * areaWidth)] = -1;
/*    */         }
/*    */         else
/*    */         {
/* 42 */           aint1[(j1 + i1 * areaWidth)] = BiomeGenBase.river.biomeID;
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 47 */     return aint1;
/*    */   }
/*    */   
/*    */   private int func_151630_c(int p_151630_1_)
/*    */   {
/* 52 */     return p_151630_1_ >= 2 ? 2 + (p_151630_1_ & 0x1) : p_151630_1_;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\layer\GenLayerRiver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */