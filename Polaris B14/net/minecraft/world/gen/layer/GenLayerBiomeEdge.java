/*     */ package net.minecraft.world.gen.layer;
/*     */ 
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ 
/*     */ public class GenLayerBiomeEdge extends GenLayer
/*     */ {
/*     */   public GenLayerBiomeEdge(long p_i45475_1_, GenLayer p_i45475_3_)
/*     */   {
/*   9 */     super(p_i45475_1_);
/*  10 */     this.parent = p_i45475_3_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
/*     */   {
/*  19 */     int[] aint = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
/*  20 */     int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);
/*     */     
/*  22 */     for (int i = 0; i < areaHeight; i++)
/*     */     {
/*  24 */       for (int j = 0; j < areaWidth; j++)
/*     */       {
/*  26 */         initChunkSeed(j + areaX, i + areaY);
/*  27 */         int k = aint[(j + 1 + (i + 1) * (areaWidth + 2))];
/*     */         
/*  29 */         if ((!replaceBiomeEdgeIfNecessary(aint, aint1, j, i, areaWidth, k, BiomeGenBase.extremeHills.biomeID, BiomeGenBase.extremeHillsEdge.biomeID)) && (!replaceBiomeEdge(aint, aint1, j, i, areaWidth, k, BiomeGenBase.mesaPlateau_F.biomeID, BiomeGenBase.mesa.biomeID)) && (!replaceBiomeEdge(aint, aint1, j, i, areaWidth, k, BiomeGenBase.mesaPlateau.biomeID, BiomeGenBase.mesa.biomeID)) && (!replaceBiomeEdge(aint, aint1, j, i, areaWidth, k, BiomeGenBase.megaTaiga.biomeID, BiomeGenBase.taiga.biomeID)))
/*     */         {
/*  31 */           if (k == BiomeGenBase.desert.biomeID)
/*     */           {
/*  33 */             int l1 = aint[(j + 1 + (i + 1 - 1) * (areaWidth + 2))];
/*  34 */             int i2 = aint[(j + 1 + 1 + (i + 1) * (areaWidth + 2))];
/*  35 */             int j2 = aint[(j + 1 - 1 + (i + 1) * (areaWidth + 2))];
/*  36 */             int k2 = aint[(j + 1 + (i + 1 + 1) * (areaWidth + 2))];
/*     */             
/*  38 */             if ((l1 != BiomeGenBase.icePlains.biomeID) && (i2 != BiomeGenBase.icePlains.biomeID) && (j2 != BiomeGenBase.icePlains.biomeID) && (k2 != BiomeGenBase.icePlains.biomeID))
/*     */             {
/*  40 */               aint1[(j + i * areaWidth)] = k;
/*     */             }
/*     */             else
/*     */             {
/*  44 */               aint1[(j + i * areaWidth)] = BiomeGenBase.extremeHillsPlus.biomeID;
/*     */             }
/*     */           }
/*  47 */           else if (k == BiomeGenBase.swampland.biomeID)
/*     */           {
/*  49 */             int l = aint[(j + 1 + (i + 1 - 1) * (areaWidth + 2))];
/*  50 */             int i1 = aint[(j + 1 + 1 + (i + 1) * (areaWidth + 2))];
/*  51 */             int j1 = aint[(j + 1 - 1 + (i + 1) * (areaWidth + 2))];
/*  52 */             int k1 = aint[(j + 1 + (i + 1 + 1) * (areaWidth + 2))];
/*     */             
/*  54 */             if ((l != BiomeGenBase.desert.biomeID) && (i1 != BiomeGenBase.desert.biomeID) && (j1 != BiomeGenBase.desert.biomeID) && (k1 != BiomeGenBase.desert.biomeID) && (l != BiomeGenBase.coldTaiga.biomeID) && (i1 != BiomeGenBase.coldTaiga.biomeID) && (j1 != BiomeGenBase.coldTaiga.biomeID) && (k1 != BiomeGenBase.coldTaiga.biomeID) && (l != BiomeGenBase.icePlains.biomeID) && (i1 != BiomeGenBase.icePlains.biomeID) && (j1 != BiomeGenBase.icePlains.biomeID) && (k1 != BiomeGenBase.icePlains.biomeID))
/*     */             {
/*  56 */               if ((l != BiomeGenBase.jungle.biomeID) && (k1 != BiomeGenBase.jungle.biomeID) && (i1 != BiomeGenBase.jungle.biomeID) && (j1 != BiomeGenBase.jungle.biomeID))
/*     */               {
/*  58 */                 aint1[(j + i * areaWidth)] = k;
/*     */               }
/*     */               else
/*     */               {
/*  62 */                 aint1[(j + i * areaWidth)] = BiomeGenBase.jungleEdge.biomeID;
/*     */               }
/*     */               
/*     */             }
/*     */             else {
/*  67 */               aint1[(j + i * areaWidth)] = BiomeGenBase.plains.biomeID;
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/*  72 */             aint1[(j + i * areaWidth)] = k;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  78 */     return aint1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean replaceBiomeEdgeIfNecessary(int[] p_151636_1_, int[] p_151636_2_, int p_151636_3_, int p_151636_4_, int p_151636_5_, int p_151636_6_, int p_151636_7_, int p_151636_8_)
/*     */   {
/*  86 */     if (!biomesEqualOrMesaPlateau(p_151636_6_, p_151636_7_))
/*     */     {
/*  88 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  92 */     int i = p_151636_1_[(p_151636_3_ + 1 + (p_151636_4_ + 1 - 1) * (p_151636_5_ + 2))];
/*  93 */     int j = p_151636_1_[(p_151636_3_ + 1 + 1 + (p_151636_4_ + 1) * (p_151636_5_ + 2))];
/*  94 */     int k = p_151636_1_[(p_151636_3_ + 1 - 1 + (p_151636_4_ + 1) * (p_151636_5_ + 2))];
/*  95 */     int l = p_151636_1_[(p_151636_3_ + 1 + (p_151636_4_ + 1 + 1) * (p_151636_5_ + 2))];
/*     */     
/*  97 */     if ((canBiomesBeNeighbors(i, p_151636_7_)) && (canBiomesBeNeighbors(j, p_151636_7_)) && (canBiomesBeNeighbors(k, p_151636_7_)) && (canBiomesBeNeighbors(l, p_151636_7_)))
/*     */     {
/*  99 */       p_151636_2_[(p_151636_3_ + p_151636_4_ * p_151636_5_)] = p_151636_6_;
/*     */     }
/*     */     else
/*     */     {
/* 103 */       p_151636_2_[(p_151636_3_ + p_151636_4_ * p_151636_5_)] = p_151636_8_;
/*     */     }
/*     */     
/* 106 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean replaceBiomeEdge(int[] p_151635_1_, int[] p_151635_2_, int p_151635_3_, int p_151635_4_, int p_151635_5_, int p_151635_6_, int p_151635_7_, int p_151635_8_)
/*     */   {
/* 115 */     if (p_151635_6_ != p_151635_7_)
/*     */     {
/* 117 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 121 */     int i = p_151635_1_[(p_151635_3_ + 1 + (p_151635_4_ + 1 - 1) * (p_151635_5_ + 2))];
/* 122 */     int j = p_151635_1_[(p_151635_3_ + 1 + 1 + (p_151635_4_ + 1) * (p_151635_5_ + 2))];
/* 123 */     int k = p_151635_1_[(p_151635_3_ + 1 - 1 + (p_151635_4_ + 1) * (p_151635_5_ + 2))];
/* 124 */     int l = p_151635_1_[(p_151635_3_ + 1 + (p_151635_4_ + 1 + 1) * (p_151635_5_ + 2))];
/*     */     
/* 126 */     if ((biomesEqualOrMesaPlateau(i, p_151635_7_)) && (biomesEqualOrMesaPlateau(j, p_151635_7_)) && (biomesEqualOrMesaPlateau(k, p_151635_7_)) && (biomesEqualOrMesaPlateau(l, p_151635_7_)))
/*     */     {
/* 128 */       p_151635_2_[(p_151635_3_ + p_151635_4_ * p_151635_5_)] = p_151635_6_;
/*     */     }
/*     */     else
/*     */     {
/* 132 */       p_151635_2_[(p_151635_3_ + p_151635_4_ * p_151635_5_)] = p_151635_8_;
/*     */     }
/*     */     
/* 135 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean canBiomesBeNeighbors(int p_151634_1_, int p_151634_2_)
/*     */   {
/* 145 */     if (biomesEqualOrMesaPlateau(p_151634_1_, p_151634_2_))
/*     */     {
/* 147 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 151 */     BiomeGenBase biomegenbase = BiomeGenBase.getBiome(p_151634_1_);
/* 152 */     BiomeGenBase biomegenbase1 = BiomeGenBase.getBiome(p_151634_2_);
/*     */     
/* 154 */     if ((biomegenbase != null) && (biomegenbase1 != null))
/*     */     {
/* 156 */       net.minecraft.world.biome.BiomeGenBase.TempCategory biomegenbase$tempcategory = biomegenbase.getTempCategory();
/* 157 */       net.minecraft.world.biome.BiomeGenBase.TempCategory biomegenbase$tempcategory1 = biomegenbase1.getTempCategory();
/* 158 */       return (biomegenbase$tempcategory == biomegenbase$tempcategory1) || (biomegenbase$tempcategory == net.minecraft.world.biome.BiomeGenBase.TempCategory.MEDIUM) || (biomegenbase$tempcategory1 == net.minecraft.world.biome.BiomeGenBase.TempCategory.MEDIUM);
/*     */     }
/*     */     
/*     */ 
/* 162 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\layer\GenLayerBiomeEdge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */