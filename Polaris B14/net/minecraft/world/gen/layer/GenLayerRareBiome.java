/*    */ package net.minecraft.world.gen.layer;
/*    */ 
/*    */ import net.minecraft.world.biome.BiomeGenBase;
/*    */ 
/*    */ public class GenLayerRareBiome extends GenLayer
/*    */ {
/*    */   public GenLayerRareBiome(long p_i45478_1_, GenLayer p_i45478_3_)
/*    */   {
/*  9 */     super(p_i45478_1_);
/* 10 */     this.parent = p_i45478_3_;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
/*    */   {
/* 19 */     int[] aint = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
/* 20 */     int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);
/*    */     
/* 22 */     for (int i = 0; i < areaHeight; i++)
/*    */     {
/* 24 */       for (int j = 0; j < areaWidth; j++)
/*    */       {
/* 26 */         initChunkSeed(j + areaX, i + areaY);
/* 27 */         int k = aint[(j + 1 + (i + 1) * (areaWidth + 2))];
/*    */         
/* 29 */         if (nextInt(57) == 0)
/*    */         {
/* 31 */           if (k == BiomeGenBase.plains.biomeID)
/*    */           {
/* 33 */             aint1[(j + i * areaWidth)] = (BiomeGenBase.plains.biomeID + 128);
/*    */           }
/*    */           else
/*    */           {
/* 37 */             aint1[(j + i * areaWidth)] = k;
/*    */           }
/*    */           
/*    */         }
/*    */         else {
/* 42 */           aint1[(j + i * areaWidth)] = k;
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 47 */     return aint1;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\layer\GenLayerRareBiome.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */