/*     */ package net.minecraft.world.gen.layer;
/*     */ 
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class GenLayerHills extends GenLayer
/*     */ {
/*   9 */   private static final Logger logger = ;
/*     */   private GenLayer field_151628_d;
/*     */   
/*     */   public GenLayerHills(long p_i45479_1_, GenLayer p_i45479_3_, GenLayer p_i45479_4_)
/*     */   {
/*  14 */     super(p_i45479_1_);
/*  15 */     this.parent = p_i45479_3_;
/*  16 */     this.field_151628_d = p_i45479_4_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
/*     */   {
/*  25 */     int[] aint = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
/*  26 */     int[] aint1 = this.field_151628_d.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
/*  27 */     int[] aint2 = IntCache.getIntCache(areaWidth * areaHeight);
/*     */     
/*  29 */     for (int i = 0; i < areaHeight; i++)
/*     */     {
/*  31 */       for (int j = 0; j < areaWidth; j++)
/*     */       {
/*  33 */         initChunkSeed(j + areaX, i + areaY);
/*  34 */         int k = aint[(j + 1 + (i + 1) * (areaWidth + 2))];
/*  35 */         int l = aint1[(j + 1 + (i + 1) * (areaWidth + 2))];
/*  36 */         boolean flag = (l - 2) % 29 == 0;
/*     */         
/*  38 */         if (k > 255)
/*     */         {
/*  40 */           logger.debug("old! " + k);
/*     */         }
/*     */         
/*  43 */         if ((k != 0) && (l >= 2) && ((l - 2) % 29 == 1) && (k < 128))
/*     */         {
/*  45 */           if (BiomeGenBase.getBiome(k + 128) != null)
/*     */           {
/*  47 */             aint2[(j + i * areaWidth)] = (k + 128);
/*     */           }
/*     */           else
/*     */           {
/*  51 */             aint2[(j + i * areaWidth)] = k;
/*     */           }
/*     */         }
/*  54 */         else if ((nextInt(3) != 0) && (!flag))
/*     */         {
/*  56 */           aint2[(j + i * areaWidth)] = k;
/*     */         }
/*     */         else
/*     */         {
/*  60 */           int i1 = k;
/*     */           
/*  62 */           if (k == BiomeGenBase.desert.biomeID)
/*     */           {
/*  64 */             i1 = BiomeGenBase.desertHills.biomeID;
/*     */           }
/*  66 */           else if (k == BiomeGenBase.forest.biomeID)
/*     */           {
/*  68 */             i1 = BiomeGenBase.forestHills.biomeID;
/*     */           }
/*  70 */           else if (k == BiomeGenBase.birchForest.biomeID)
/*     */           {
/*  72 */             i1 = BiomeGenBase.birchForestHills.biomeID;
/*     */           }
/*  74 */           else if (k == BiomeGenBase.roofedForest.biomeID)
/*     */           {
/*  76 */             i1 = BiomeGenBase.plains.biomeID;
/*     */           }
/*  78 */           else if (k == BiomeGenBase.taiga.biomeID)
/*     */           {
/*  80 */             i1 = BiomeGenBase.taigaHills.biomeID;
/*     */           }
/*  82 */           else if (k == BiomeGenBase.megaTaiga.biomeID)
/*     */           {
/*  84 */             i1 = BiomeGenBase.megaTaigaHills.biomeID;
/*     */           }
/*  86 */           else if (k == BiomeGenBase.coldTaiga.biomeID)
/*     */           {
/*  88 */             i1 = BiomeGenBase.coldTaigaHills.biomeID;
/*     */           }
/*  90 */           else if (k == BiomeGenBase.plains.biomeID)
/*     */           {
/*  92 */             if (nextInt(3) == 0)
/*     */             {
/*  94 */               i1 = BiomeGenBase.forestHills.biomeID;
/*     */             }
/*     */             else
/*     */             {
/*  98 */               i1 = BiomeGenBase.forest.biomeID;
/*     */             }
/*     */           }
/* 101 */           else if (k == BiomeGenBase.icePlains.biomeID)
/*     */           {
/* 103 */             i1 = BiomeGenBase.iceMountains.biomeID;
/*     */           }
/* 105 */           else if (k == BiomeGenBase.jungle.biomeID)
/*     */           {
/* 107 */             i1 = BiomeGenBase.jungleHills.biomeID;
/*     */           }
/* 109 */           else if (k == BiomeGenBase.ocean.biomeID)
/*     */           {
/* 111 */             i1 = BiomeGenBase.deepOcean.biomeID;
/*     */           }
/* 113 */           else if (k == BiomeGenBase.extremeHills.biomeID)
/*     */           {
/* 115 */             i1 = BiomeGenBase.extremeHillsPlus.biomeID;
/*     */           }
/* 117 */           else if (k == BiomeGenBase.savanna.biomeID)
/*     */           {
/* 119 */             i1 = BiomeGenBase.savannaPlateau.biomeID;
/*     */           }
/* 121 */           else if (biomesEqualOrMesaPlateau(k, BiomeGenBase.mesaPlateau_F.biomeID))
/*     */           {
/* 123 */             i1 = BiomeGenBase.mesa.biomeID;
/*     */           }
/* 125 */           else if ((k == BiomeGenBase.deepOcean.biomeID) && (nextInt(3) == 0))
/*     */           {
/* 127 */             int j1 = nextInt(2);
/*     */             
/* 129 */             if (j1 == 0)
/*     */             {
/* 131 */               i1 = BiomeGenBase.plains.biomeID;
/*     */             }
/*     */             else
/*     */             {
/* 135 */               i1 = BiomeGenBase.forest.biomeID;
/*     */             }
/*     */           }
/*     */           
/* 139 */           if ((flag) && (i1 != k))
/*     */           {
/* 141 */             if (BiomeGenBase.getBiome(i1 + 128) != null)
/*     */             {
/* 143 */               i1 += 128;
/*     */             }
/*     */             else
/*     */             {
/* 147 */               i1 = k;
/*     */             }
/*     */           }
/*     */           
/* 151 */           if (i1 == k)
/*     */           {
/* 153 */             aint2[(j + i * areaWidth)] = k;
/*     */           }
/*     */           else
/*     */           {
/* 157 */             int k2 = aint[(j + 1 + (i + 1 - 1) * (areaWidth + 2))];
/* 158 */             int k1 = aint[(j + 1 + 1 + (i + 1) * (areaWidth + 2))];
/* 159 */             int l1 = aint[(j + 1 - 1 + (i + 1) * (areaWidth + 2))];
/* 160 */             int i2 = aint[(j + 1 + (i + 1 + 1) * (areaWidth + 2))];
/* 161 */             int j2 = 0;
/*     */             
/* 163 */             if (biomesEqualOrMesaPlateau(k2, k))
/*     */             {
/* 165 */               j2++;
/*     */             }
/*     */             
/* 168 */             if (biomesEqualOrMesaPlateau(k1, k))
/*     */             {
/* 170 */               j2++;
/*     */             }
/*     */             
/* 173 */             if (biomesEqualOrMesaPlateau(l1, k))
/*     */             {
/* 175 */               j2++;
/*     */             }
/*     */             
/* 178 */             if (biomesEqualOrMesaPlateau(i2, k))
/*     */             {
/* 180 */               j2++;
/*     */             }
/*     */             
/* 183 */             if (j2 >= 3)
/*     */             {
/* 185 */               aint2[(j + i * areaWidth)] = i1;
/*     */             }
/*     */             else
/*     */             {
/* 189 */               aint2[(j + i * areaWidth)] = k;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 196 */     return aint2;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\layer\GenLayerHills.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */