/*    */ package net.minecraft.world.gen.layer;
/*    */ 
/*    */ import net.minecraft.world.biome.BiomeGenBase;
/*    */ 
/*    */ public class GenLayerRiverMix extends GenLayer
/*    */ {
/*    */   private GenLayer biomePatternGeneratorChain;
/*    */   private GenLayer riverPatternGeneratorChain;
/*    */   
/*    */   public GenLayerRiverMix(long p_i2129_1_, GenLayer p_i2129_3_, GenLayer p_i2129_4_)
/*    */   {
/* 12 */     super(p_i2129_1_);
/* 13 */     this.biomePatternGeneratorChain = p_i2129_3_;
/* 14 */     this.riverPatternGeneratorChain = p_i2129_4_;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void initWorldGenSeed(long seed)
/*    */   {
/* 23 */     this.biomePatternGeneratorChain.initWorldGenSeed(seed);
/* 24 */     this.riverPatternGeneratorChain.initWorldGenSeed(seed);
/* 25 */     super.initWorldGenSeed(seed);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
/*    */   {
/* 34 */     int[] aint = this.biomePatternGeneratorChain.getInts(areaX, areaY, areaWidth, areaHeight);
/* 35 */     int[] aint1 = this.riverPatternGeneratorChain.getInts(areaX, areaY, areaWidth, areaHeight);
/* 36 */     int[] aint2 = IntCache.getIntCache(areaWidth * areaHeight);
/*    */     
/* 38 */     for (int i = 0; i < areaWidth * areaHeight; i++)
/*    */     {
/* 40 */       if ((aint[i] != BiomeGenBase.ocean.biomeID) && (aint[i] != BiomeGenBase.deepOcean.biomeID))
/*    */       {
/* 42 */         if (aint1[i] == BiomeGenBase.river.biomeID)
/*    */         {
/* 44 */           if (aint[i] == BiomeGenBase.icePlains.biomeID)
/*    */           {
/* 46 */             aint2[i] = BiomeGenBase.frozenRiver.biomeID;
/*    */           }
/* 48 */           else if ((aint[i] != BiomeGenBase.mushroomIsland.biomeID) && (aint[i] != BiomeGenBase.mushroomIslandShore.biomeID))
/*    */           {
/* 50 */             aint1[i] &= 0xFF;
/*    */           }
/*    */           else
/*    */           {
/* 54 */             aint2[i] = BiomeGenBase.mushroomIslandShore.biomeID;
/*    */           }
/*    */           
/*    */         }
/*    */         else {
/* 59 */           aint2[i] = aint[i];
/*    */         }
/*    */         
/*    */       }
/*    */       else {
/* 64 */         aint2[i] = aint[i];
/*    */       }
/*    */     }
/*    */     
/* 68 */     return aint2;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\layer\GenLayerRiverMix.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */