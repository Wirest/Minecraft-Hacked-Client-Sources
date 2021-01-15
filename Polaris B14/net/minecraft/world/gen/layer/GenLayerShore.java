/*     */ package net.minecraft.world.gen.layer;
/*     */ 
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.biome.BiomeGenJungle;
/*     */ import net.minecraft.world.biome.BiomeGenMesa;
/*     */ 
/*     */ public class GenLayerShore extends GenLayer
/*     */ {
/*     */   public GenLayerShore(long p_i2130_1_, GenLayer p_i2130_3_)
/*     */   {
/*  11 */     super(p_i2130_1_);
/*  12 */     this.parent = p_i2130_3_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
/*     */   {
/*  21 */     int[] aint = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
/*  22 */     int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);
/*     */     
/*  24 */     for (int i = 0; i < areaHeight; i++)
/*     */     {
/*  26 */       for (int j = 0; j < areaWidth; j++)
/*     */       {
/*  28 */         initChunkSeed(j + areaX, i + areaY);
/*  29 */         int k = aint[(j + 1 + (i + 1) * (areaWidth + 2))];
/*  30 */         BiomeGenBase biomegenbase = BiomeGenBase.getBiome(k);
/*     */         
/*  32 */         if (k == BiomeGenBase.mushroomIsland.biomeID)
/*     */         {
/*  34 */           int j2 = aint[(j + 1 + (i + 1 - 1) * (areaWidth + 2))];
/*  35 */           int i3 = aint[(j + 1 + 1 + (i + 1) * (areaWidth + 2))];
/*  36 */           int l3 = aint[(j + 1 - 1 + (i + 1) * (areaWidth + 2))];
/*  37 */           int k4 = aint[(j + 1 + (i + 1 + 1) * (areaWidth + 2))];
/*     */           
/*  39 */           if ((j2 != BiomeGenBase.ocean.biomeID) && (i3 != BiomeGenBase.ocean.biomeID) && (l3 != BiomeGenBase.ocean.biomeID) && (k4 != BiomeGenBase.ocean.biomeID))
/*     */           {
/*  41 */             aint1[(j + i * areaWidth)] = k;
/*     */           }
/*     */           else
/*     */           {
/*  45 */             aint1[(j + i * areaWidth)] = BiomeGenBase.mushroomIslandShore.biomeID;
/*     */           }
/*     */         }
/*  48 */         else if ((biomegenbase != null) && (biomegenbase.getBiomeClass() == BiomeGenJungle.class))
/*     */         {
/*  50 */           int i2 = aint[(j + 1 + (i + 1 - 1) * (areaWidth + 2))];
/*  51 */           int l2 = aint[(j + 1 + 1 + (i + 1) * (areaWidth + 2))];
/*  52 */           int k3 = aint[(j + 1 - 1 + (i + 1) * (areaWidth + 2))];
/*  53 */           int j4 = aint[(j + 1 + (i + 1 + 1) * (areaWidth + 2))];
/*     */           
/*  55 */           if ((func_151631_c(i2)) && (func_151631_c(l2)) && (func_151631_c(k3)) && (func_151631_c(j4)))
/*     */           {
/*  57 */             if ((!isBiomeOceanic(i2)) && (!isBiomeOceanic(l2)) && (!isBiomeOceanic(k3)) && (!isBiomeOceanic(j4)))
/*     */             {
/*  59 */               aint1[(j + i * areaWidth)] = k;
/*     */             }
/*     */             else
/*     */             {
/*  63 */               aint1[(j + i * areaWidth)] = BiomeGenBase.beach.biomeID;
/*     */             }
/*     */             
/*     */           }
/*     */           else {
/*  68 */             aint1[(j + i * areaWidth)] = BiomeGenBase.jungleEdge.biomeID;
/*     */           }
/*     */         }
/*  71 */         else if ((k != BiomeGenBase.extremeHills.biomeID) && (k != BiomeGenBase.extremeHillsPlus.biomeID) && (k != BiomeGenBase.extremeHillsEdge.biomeID))
/*     */         {
/*  73 */           if ((biomegenbase != null) && (biomegenbase.isSnowyBiome()))
/*     */           {
/*  75 */             func_151632_a(aint, aint1, j, i, areaWidth, k, BiomeGenBase.coldBeach.biomeID);
/*     */           }
/*  77 */           else if ((k != BiomeGenBase.mesa.biomeID) && (k != BiomeGenBase.mesaPlateau_F.biomeID))
/*     */           {
/*  79 */             if ((k != BiomeGenBase.ocean.biomeID) && (k != BiomeGenBase.deepOcean.biomeID) && (k != BiomeGenBase.river.biomeID) && (k != BiomeGenBase.swampland.biomeID))
/*     */             {
/*  81 */               int l1 = aint[(j + 1 + (i + 1 - 1) * (areaWidth + 2))];
/*  82 */               int k2 = aint[(j + 1 + 1 + (i + 1) * (areaWidth + 2))];
/*  83 */               int j3 = aint[(j + 1 - 1 + (i + 1) * (areaWidth + 2))];
/*  84 */               int i4 = aint[(j + 1 + (i + 1 + 1) * (areaWidth + 2))];
/*     */               
/*  86 */               if ((!isBiomeOceanic(l1)) && (!isBiomeOceanic(k2)) && (!isBiomeOceanic(j3)) && (!isBiomeOceanic(i4)))
/*     */               {
/*  88 */                 aint1[(j + i * areaWidth)] = k;
/*     */               }
/*     */               else
/*     */               {
/*  92 */                 aint1[(j + i * areaWidth)] = BiomeGenBase.beach.biomeID;
/*     */               }
/*     */             }
/*     */             else
/*     */             {
/*  97 */               aint1[(j + i * areaWidth)] = k;
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 102 */             int l = aint[(j + 1 + (i + 1 - 1) * (areaWidth + 2))];
/* 103 */             int i1 = aint[(j + 1 + 1 + (i + 1) * (areaWidth + 2))];
/* 104 */             int j1 = aint[(j + 1 - 1 + (i + 1) * (areaWidth + 2))];
/* 105 */             int k1 = aint[(j + 1 + (i + 1 + 1) * (areaWidth + 2))];
/*     */             
/* 107 */             if ((!isBiomeOceanic(l)) && (!isBiomeOceanic(i1)) && (!isBiomeOceanic(j1)) && (!isBiomeOceanic(k1)))
/*     */             {
/* 109 */               if ((func_151633_d(l)) && (func_151633_d(i1)) && (func_151633_d(j1)) && (func_151633_d(k1)))
/*     */               {
/* 111 */                 aint1[(j + i * areaWidth)] = k;
/*     */               }
/*     */               else
/*     */               {
/* 115 */                 aint1[(j + i * areaWidth)] = BiomeGenBase.desert.biomeID;
/*     */               }
/*     */               
/*     */             }
/*     */             else {
/* 120 */               aint1[(j + i * areaWidth)] = k;
/*     */             }
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 126 */           func_151632_a(aint, aint1, j, i, areaWidth, k, BiomeGenBase.stoneBeach.biomeID);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 131 */     return aint1;
/*     */   }
/*     */   
/*     */   private void func_151632_a(int[] p_151632_1_, int[] p_151632_2_, int p_151632_3_, int p_151632_4_, int p_151632_5_, int p_151632_6_, int p_151632_7_)
/*     */   {
/* 136 */     if (isBiomeOceanic(p_151632_6_))
/*     */     {
/* 138 */       p_151632_2_[(p_151632_3_ + p_151632_4_ * p_151632_5_)] = p_151632_6_;
/*     */     }
/*     */     else
/*     */     {
/* 142 */       int i = p_151632_1_[(p_151632_3_ + 1 + (p_151632_4_ + 1 - 1) * (p_151632_5_ + 2))];
/* 143 */       int j = p_151632_1_[(p_151632_3_ + 1 + 1 + (p_151632_4_ + 1) * (p_151632_5_ + 2))];
/* 144 */       int k = p_151632_1_[(p_151632_3_ + 1 - 1 + (p_151632_4_ + 1) * (p_151632_5_ + 2))];
/* 145 */       int l = p_151632_1_[(p_151632_3_ + 1 + (p_151632_4_ + 1 + 1) * (p_151632_5_ + 2))];
/*     */       
/* 147 */       if ((!isBiomeOceanic(i)) && (!isBiomeOceanic(j)) && (!isBiomeOceanic(k)) && (!isBiomeOceanic(l)))
/*     */       {
/* 149 */         p_151632_2_[(p_151632_3_ + p_151632_4_ * p_151632_5_)] = p_151632_6_;
/*     */       }
/*     */       else
/*     */       {
/* 153 */         p_151632_2_[(p_151632_3_ + p_151632_4_ * p_151632_5_)] = p_151632_7_;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean func_151631_c(int p_151631_1_)
/*     */   {
/* 160 */     return (BiomeGenBase.getBiome(p_151631_1_) != null) && (BiomeGenBase.getBiome(p_151631_1_).getBiomeClass() == BiomeGenJungle.class);
/*     */   }
/*     */   
/*     */   private boolean func_151633_d(int p_151633_1_)
/*     */   {
/* 165 */     return BiomeGenBase.getBiome(p_151633_1_) instanceof BiomeGenMesa;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\layer\GenLayerShore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */