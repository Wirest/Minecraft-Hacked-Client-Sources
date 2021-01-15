/*     */ package net.minecraft.world.gen.layer;
/*     */ 
/*     */ import net.minecraft.world.WorldType;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.gen.ChunkProviderSettings;
/*     */ 
/*     */ public class GenLayerBiome extends GenLayer
/*     */ {
/*   9 */   private BiomeGenBase[] field_151623_c = { BiomeGenBase.desert, BiomeGenBase.desert, BiomeGenBase.desert, BiomeGenBase.savanna, BiomeGenBase.savanna, BiomeGenBase.plains };
/*  10 */   private BiomeGenBase[] field_151621_d = { BiomeGenBase.forest, BiomeGenBase.roofedForest, BiomeGenBase.extremeHills, BiomeGenBase.plains, BiomeGenBase.birchForest, BiomeGenBase.swampland };
/*  11 */   private BiomeGenBase[] field_151622_e = { BiomeGenBase.forest, BiomeGenBase.extremeHills, BiomeGenBase.taiga, BiomeGenBase.plains };
/*  12 */   private BiomeGenBase[] field_151620_f = { BiomeGenBase.icePlains, BiomeGenBase.icePlains, BiomeGenBase.icePlains, BiomeGenBase.coldTaiga };
/*     */   private final ChunkProviderSettings field_175973_g;
/*     */   
/*     */   public GenLayerBiome(long p_i45560_1_, GenLayer p_i45560_3_, WorldType p_i45560_4_, String p_i45560_5_)
/*     */   {
/*  17 */     super(p_i45560_1_);
/*  18 */     this.parent = p_i45560_3_;
/*     */     
/*  20 */     if (p_i45560_4_ == WorldType.DEFAULT_1_1)
/*     */     {
/*  22 */       this.field_151623_c = new BiomeGenBase[] { BiomeGenBase.desert, BiomeGenBase.forest, BiomeGenBase.extremeHills, BiomeGenBase.swampland, BiomeGenBase.plains, BiomeGenBase.taiga };
/*  23 */       this.field_175973_g = null;
/*     */     }
/*  25 */     else if (p_i45560_4_ == WorldType.CUSTOMIZED)
/*     */     {
/*  27 */       this.field_175973_g = net.minecraft.world.gen.ChunkProviderSettings.Factory.jsonToFactory(p_i45560_5_).func_177864_b();
/*     */     }
/*     */     else
/*     */     {
/*  31 */       this.field_175973_g = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
/*     */   {
/*  41 */     int[] aint = this.parent.getInts(areaX, areaY, areaWidth, areaHeight);
/*  42 */     int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);
/*     */     
/*  44 */     for (int i = 0; i < areaHeight; i++)
/*     */     {
/*  46 */       for (int j = 0; j < areaWidth; j++)
/*     */       {
/*  48 */         initChunkSeed(j + areaX, i + areaY);
/*  49 */         int k = aint[(j + i * areaWidth)];
/*  50 */         int l = (k & 0xF00) >> 8;
/*  51 */         k &= 0xF0FF;
/*     */         
/*  53 */         if ((this.field_175973_g != null) && (this.field_175973_g.fixedBiome >= 0))
/*     */         {
/*  55 */           aint1[(j + i * areaWidth)] = this.field_175973_g.fixedBiome;
/*     */         }
/*  57 */         else if (isBiomeOceanic(k))
/*     */         {
/*  59 */           aint1[(j + i * areaWidth)] = k;
/*     */         }
/*  61 */         else if (k == BiomeGenBase.mushroomIsland.biomeID)
/*     */         {
/*  63 */           aint1[(j + i * areaWidth)] = k;
/*     */         }
/*  65 */         else if (k == 1)
/*     */         {
/*  67 */           if (l > 0)
/*     */           {
/*  69 */             if (nextInt(3) == 0)
/*     */             {
/*  71 */               aint1[(j + i * areaWidth)] = BiomeGenBase.mesaPlateau.biomeID;
/*     */             }
/*     */             else
/*     */             {
/*  75 */               aint1[(j + i * areaWidth)] = BiomeGenBase.mesaPlateau_F.biomeID;
/*     */             }
/*     */             
/*     */           }
/*     */           else {
/*  80 */             aint1[(j + i * areaWidth)] = this.field_151623_c[nextInt(this.field_151623_c.length)].biomeID;
/*     */           }
/*     */         }
/*  83 */         else if (k == 2)
/*     */         {
/*  85 */           if (l > 0)
/*     */           {
/*  87 */             aint1[(j + i * areaWidth)] = BiomeGenBase.jungle.biomeID;
/*     */           }
/*     */           else
/*     */           {
/*  91 */             aint1[(j + i * areaWidth)] = this.field_151621_d[nextInt(this.field_151621_d.length)].biomeID;
/*     */           }
/*     */         }
/*  94 */         else if (k == 3)
/*     */         {
/*  96 */           if (l > 0)
/*     */           {
/*  98 */             aint1[(j + i * areaWidth)] = BiomeGenBase.megaTaiga.biomeID;
/*     */           }
/*     */           else
/*     */           {
/* 102 */             aint1[(j + i * areaWidth)] = this.field_151622_e[nextInt(this.field_151622_e.length)].biomeID;
/*     */           }
/*     */         }
/* 105 */         else if (k == 4)
/*     */         {
/* 107 */           aint1[(j + i * areaWidth)] = this.field_151620_f[nextInt(this.field_151620_f.length)].biomeID;
/*     */         }
/*     */         else
/*     */         {
/* 111 */           aint1[(j + i * areaWidth)] = BiomeGenBase.mushroomIsland.biomeID;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 116 */     return aint1;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\layer\GenLayerBiome.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */