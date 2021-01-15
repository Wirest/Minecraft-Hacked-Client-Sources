/*     */ package net.minecraft.world.gen;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.EnumCreatureType;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IProgressUpdate;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.ChunkCoordIntPair;
/*     */ import net.minecraft.world.SpawnerAnimals;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldType;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
/*     */ import net.minecraft.world.biome.WorldChunkManager;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.minecraft.world.chunk.ChunkPrimer;
/*     */ import net.minecraft.world.chunk.IChunkProvider;
/*     */ import net.minecraft.world.gen.feature.WorldGenDungeons;
/*     */ import net.minecraft.world.gen.feature.WorldGenLakes;
/*     */ import net.minecraft.world.gen.structure.MapGenMineshaft;
/*     */ import net.minecraft.world.gen.structure.MapGenScatteredFeature;
/*     */ import net.minecraft.world.gen.structure.MapGenStronghold;
/*     */ import net.minecraft.world.gen.structure.MapGenVillage;
/*     */ import net.minecraft.world.gen.structure.StructureOceanMonument;
/*     */ import net.minecraft.world.storage.WorldInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChunkProviderGenerate
/*     */   implements IChunkProvider
/*     */ {
/*     */   private Random rand;
/*     */   private NoiseGeneratorOctaves field_147431_j;
/*     */   private NoiseGeneratorOctaves field_147432_k;
/*     */   private NoiseGeneratorOctaves field_147429_l;
/*     */   private NoiseGeneratorPerlin field_147430_m;
/*     */   public NoiseGeneratorOctaves noiseGen5;
/*     */   public NoiseGeneratorOctaves noiseGen6;
/*     */   public NoiseGeneratorOctaves mobSpawnerNoise;
/*     */   private World worldObj;
/*     */   private final boolean mapFeaturesEnabled;
/*     */   private WorldType field_177475_o;
/*     */   private final double[] field_147434_q;
/*     */   private final float[] parabolicField;
/*     */   private ChunkProviderSettings settings;
/*  53 */   private Block field_177476_s = Blocks.water;
/*  54 */   private double[] stoneNoise = new double['Ā'];
/*  55 */   private MapGenBase caveGenerator = new MapGenCaves();
/*     */   
/*     */ 
/*  58 */   private MapGenStronghold strongholdGenerator = new MapGenStronghold();
/*     */   
/*     */ 
/*  61 */   private MapGenVillage villageGenerator = new MapGenVillage();
/*     */   
/*     */ 
/*  64 */   private MapGenMineshaft mineshaftGenerator = new MapGenMineshaft();
/*  65 */   private MapGenScatteredFeature scatteredFeatureGenerator = new MapGenScatteredFeature();
/*     */   
/*     */ 
/*  68 */   private MapGenBase ravineGenerator = new MapGenRavine();
/*  69 */   private StructureOceanMonument oceanMonumentGenerator = new StructureOceanMonument();
/*     */   
/*     */   private BiomeGenBase[] biomesForGeneration;
/*     */   
/*     */   double[] field_147427_d;
/*     */   double[] field_147428_e;
/*     */   double[] field_147425_f;
/*     */   double[] field_147426_g;
/*     */   
/*     */   public ChunkProviderGenerate(World worldIn, long p_i45636_2_, boolean p_i45636_4_, String p_i45636_5_)
/*     */   {
/*  80 */     this.worldObj = worldIn;
/*  81 */     this.mapFeaturesEnabled = p_i45636_4_;
/*  82 */     this.field_177475_o = worldIn.getWorldInfo().getTerrainType();
/*  83 */     this.rand = new Random(p_i45636_2_);
/*  84 */     this.field_147431_j = new NoiseGeneratorOctaves(this.rand, 16);
/*  85 */     this.field_147432_k = new NoiseGeneratorOctaves(this.rand, 16);
/*  86 */     this.field_147429_l = new NoiseGeneratorOctaves(this.rand, 8);
/*  87 */     this.field_147430_m = new NoiseGeneratorPerlin(this.rand, 4);
/*  88 */     this.noiseGen5 = new NoiseGeneratorOctaves(this.rand, 10);
/*  89 */     this.noiseGen6 = new NoiseGeneratorOctaves(this.rand, 16);
/*  90 */     this.mobSpawnerNoise = new NoiseGeneratorOctaves(this.rand, 8);
/*  91 */     this.field_147434_q = new double['̹'];
/*  92 */     this.parabolicField = new float[25];
/*     */     
/*  94 */     for (int i = -2; i <= 2; i++)
/*     */     {
/*  96 */       for (int j = -2; j <= 2; j++)
/*     */       {
/*  98 */         float f = 10.0F / MathHelper.sqrt_float(i * i + j * j + 0.2F);
/*  99 */         this.parabolicField[(i + 2 + (j + 2) * 5)] = f;
/*     */       }
/*     */     }
/*     */     
/* 103 */     if (p_i45636_5_ != null)
/*     */     {
/* 105 */       this.settings = ChunkProviderSettings.Factory.jsonToFactory(p_i45636_5_).func_177864_b();
/* 106 */       this.field_177476_s = (this.settings.useLavaOceans ? Blocks.lava : Blocks.water);
/* 107 */       worldIn.func_181544_b(this.settings.seaLevel);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setBlocksInChunk(int p_180518_1_, int p_180518_2_, ChunkPrimer p_180518_3_)
/*     */   {
/* 113 */     this.biomesForGeneration = this.worldObj.getWorldChunkManager().getBiomesForGeneration(this.biomesForGeneration, p_180518_1_ * 4 - 2, p_180518_2_ * 4 - 2, 10, 10);
/* 114 */     func_147423_a(p_180518_1_ * 4, 0, p_180518_2_ * 4);
/*     */     
/* 116 */     for (int i = 0; i < 4; i++)
/*     */     {
/* 118 */       int j = i * 5;
/* 119 */       int k = (i + 1) * 5;
/*     */       
/* 121 */       for (int l = 0; l < 4; l++)
/*     */       {
/* 123 */         int i1 = (j + l) * 33;
/* 124 */         int j1 = (j + l + 1) * 33;
/* 125 */         int k1 = (k + l) * 33;
/* 126 */         int l1 = (k + l + 1) * 33;
/*     */         
/* 128 */         for (int i2 = 0; i2 < 32; i2++)
/*     */         {
/* 130 */           double d0 = 0.125D;
/* 131 */           double d1 = this.field_147434_q[(i1 + i2)];
/* 132 */           double d2 = this.field_147434_q[(j1 + i2)];
/* 133 */           double d3 = this.field_147434_q[(k1 + i2)];
/* 134 */           double d4 = this.field_147434_q[(l1 + i2)];
/* 135 */           double d5 = (this.field_147434_q[(i1 + i2 + 1)] - d1) * d0;
/* 136 */           double d6 = (this.field_147434_q[(j1 + i2 + 1)] - d2) * d0;
/* 137 */           double d7 = (this.field_147434_q[(k1 + i2 + 1)] - d3) * d0;
/* 138 */           double d8 = (this.field_147434_q[(l1 + i2 + 1)] - d4) * d0;
/*     */           
/* 140 */           for (int j2 = 0; j2 < 8; j2++)
/*     */           {
/* 142 */             double d9 = 0.25D;
/* 143 */             double d10 = d1;
/* 144 */             double d11 = d2;
/* 145 */             double d12 = (d3 - d1) * d9;
/* 146 */             double d13 = (d4 - d2) * d9;
/*     */             
/* 148 */             for (int k2 = 0; k2 < 4; k2++)
/*     */             {
/* 150 */               double d14 = 0.25D;
/* 151 */               double d16 = (d11 - d10) * d14;
/* 152 */               double lvt_45_1_ = d10 - d16;
/*     */               
/* 154 */               for (int l2 = 0; l2 < 4; l2++)
/*     */               {
/* 156 */                 if (lvt_45_1_ += d16 > 0.0D)
/*     */                 {
/* 158 */                   p_180518_3_.setBlockState(i * 4 + k2, i2 * 8 + j2, l * 4 + l2, Blocks.stone.getDefaultState());
/*     */                 }
/* 160 */                 else if (i2 * 8 + j2 < this.settings.seaLevel)
/*     */                 {
/* 162 */                   p_180518_3_.setBlockState(i * 4 + k2, i2 * 8 + j2, l * 4 + l2, this.field_177476_s.getDefaultState());
/*     */                 }
/*     */               }
/*     */               
/* 166 */               d10 += d12;
/* 167 */               d11 += d13;
/*     */             }
/*     */             
/* 170 */             d1 += d5;
/* 171 */             d2 += d6;
/* 172 */             d3 += d7;
/* 173 */             d4 += d8;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void replaceBlocksForBiome(int p_180517_1_, int p_180517_2_, ChunkPrimer p_180517_3_, BiomeGenBase[] p_180517_4_)
/*     */   {
/* 182 */     double d0 = 0.03125D;
/* 183 */     this.stoneNoise = this.field_147430_m.func_151599_a(this.stoneNoise, p_180517_1_ * 16, p_180517_2_ * 16, 16, 16, d0 * 2.0D, d0 * 2.0D, 1.0D);
/*     */     
/* 185 */     for (int i = 0; i < 16; i++)
/*     */     {
/* 187 */       for (int j = 0; j < 16; j++)
/*     */       {
/* 189 */         BiomeGenBase biomegenbase = p_180517_4_[(j + i * 16)];
/* 190 */         biomegenbase.genTerrainBlocks(this.worldObj, this.rand, p_180517_3_, p_180517_1_ * 16 + i, p_180517_2_ * 16 + j, this.stoneNoise[(j + i * 16)]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Chunk provideChunk(int x, int z)
/*     */   {
/* 201 */     this.rand.setSeed(x * 341873128712L + z * 132897987541L);
/* 202 */     ChunkPrimer chunkprimer = new ChunkPrimer();
/* 203 */     setBlocksInChunk(x, z, chunkprimer);
/* 204 */     this.biomesForGeneration = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, x * 16, z * 16, 16, 16);
/* 205 */     replaceBlocksForBiome(x, z, chunkprimer, this.biomesForGeneration);
/*     */     
/* 207 */     if (this.settings.useCaves)
/*     */     {
/* 209 */       this.caveGenerator.generate(this, this.worldObj, x, z, chunkprimer);
/*     */     }
/*     */     
/* 212 */     if (this.settings.useRavines)
/*     */     {
/* 214 */       this.ravineGenerator.generate(this, this.worldObj, x, z, chunkprimer);
/*     */     }
/*     */     
/* 217 */     if ((this.settings.useMineShafts) && (this.mapFeaturesEnabled))
/*     */     {
/* 219 */       this.mineshaftGenerator.generate(this, this.worldObj, x, z, chunkprimer);
/*     */     }
/*     */     
/* 222 */     if ((this.settings.useVillages) && (this.mapFeaturesEnabled))
/*     */     {
/* 224 */       this.villageGenerator.generate(this, this.worldObj, x, z, chunkprimer);
/*     */     }
/*     */     
/* 227 */     if ((this.settings.useStrongholds) && (this.mapFeaturesEnabled))
/*     */     {
/* 229 */       this.strongholdGenerator.generate(this, this.worldObj, x, z, chunkprimer);
/*     */     }
/*     */     
/* 232 */     if ((this.settings.useTemples) && (this.mapFeaturesEnabled))
/*     */     {
/* 234 */       this.scatteredFeatureGenerator.generate(this, this.worldObj, x, z, chunkprimer);
/*     */     }
/*     */     
/* 237 */     if ((this.settings.useMonuments) && (this.mapFeaturesEnabled))
/*     */     {
/* 239 */       this.oceanMonumentGenerator.generate(this, this.worldObj, x, z, chunkprimer);
/*     */     }
/*     */     
/* 242 */     Chunk chunk = new Chunk(this.worldObj, chunkprimer, x, z);
/* 243 */     byte[] abyte = chunk.getBiomeArray();
/*     */     
/* 245 */     for (int i = 0; i < abyte.length; i++)
/*     */     {
/* 247 */       abyte[i] = ((byte)this.biomesForGeneration[i].biomeID);
/*     */     }
/*     */     
/* 250 */     chunk.generateSkylightMap();
/* 251 */     return chunk;
/*     */   }
/*     */   
/*     */   private void func_147423_a(int p_147423_1_, int p_147423_2_, int p_147423_3_)
/*     */   {
/* 256 */     this.field_147426_g = this.noiseGen6.generateNoiseOctaves(this.field_147426_g, p_147423_1_, p_147423_3_, 5, 5, this.settings.depthNoiseScaleX, this.settings.depthNoiseScaleZ, this.settings.depthNoiseScaleExponent);
/* 257 */     float f = this.settings.coordinateScale;
/* 258 */     float f1 = this.settings.heightScale;
/* 259 */     this.field_147427_d = this.field_147429_l.generateNoiseOctaves(this.field_147427_d, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5, f / this.settings.mainNoiseScaleX, f1 / this.settings.mainNoiseScaleY, f / this.settings.mainNoiseScaleZ);
/* 260 */     this.field_147428_e = this.field_147431_j.generateNoiseOctaves(this.field_147428_e, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5, f, f1, f);
/* 261 */     this.field_147425_f = this.field_147432_k.generateNoiseOctaves(this.field_147425_f, p_147423_1_, p_147423_2_, p_147423_3_, 5, 33, 5, f, f1, f);
/* 262 */     p_147423_3_ = 0;
/* 263 */     p_147423_1_ = 0;
/* 264 */     int i = 0;
/* 265 */     int j = 0;
/*     */     
/* 267 */     for (int k = 0; k < 5; k++)
/*     */     {
/* 269 */       for (int l = 0; l < 5; l++)
/*     */       {
/* 271 */         float f2 = 0.0F;
/* 272 */         float f3 = 0.0F;
/* 273 */         float f4 = 0.0F;
/* 274 */         int i1 = 2;
/* 275 */         BiomeGenBase biomegenbase = this.biomesForGeneration[(k + 2 + (l + 2) * 10)];
/*     */         
/* 277 */         for (int j1 = -i1; j1 <= i1; j1++)
/*     */         {
/* 279 */           for (int k1 = -i1; k1 <= i1; k1++)
/*     */           {
/* 281 */             BiomeGenBase biomegenbase1 = this.biomesForGeneration[(k + j1 + 2 + (l + k1 + 2) * 10)];
/* 282 */             float f5 = this.settings.biomeDepthOffSet + biomegenbase1.minHeight * this.settings.biomeDepthWeight;
/* 283 */             float f6 = this.settings.biomeScaleOffset + biomegenbase1.maxHeight * this.settings.biomeScaleWeight;
/*     */             
/* 285 */             if ((this.field_177475_o == WorldType.AMPLIFIED) && (f5 > 0.0F))
/*     */             {
/* 287 */               f5 = 1.0F + f5 * 2.0F;
/* 288 */               f6 = 1.0F + f6 * 4.0F;
/*     */             }
/*     */             
/* 291 */             float f7 = this.parabolicField[(j1 + 2 + (k1 + 2) * 5)] / (f5 + 2.0F);
/*     */             
/* 293 */             if (biomegenbase1.minHeight > biomegenbase.minHeight)
/*     */             {
/* 295 */               f7 /= 2.0F;
/*     */             }
/*     */             
/* 298 */             f2 += f6 * f7;
/* 299 */             f3 += f5 * f7;
/* 300 */             f4 += f7;
/*     */           }
/*     */         }
/*     */         
/* 304 */         f2 /= f4;
/* 305 */         f3 /= f4;
/* 306 */         f2 = f2 * 0.9F + 0.1F;
/* 307 */         f3 = (f3 * 4.0F - 1.0F) / 8.0F;
/* 308 */         double d7 = this.field_147426_g[j] / 8000.0D;
/*     */         
/* 310 */         if (d7 < 0.0D)
/*     */         {
/* 312 */           d7 = -d7 * 0.3D;
/*     */         }
/*     */         
/* 315 */         d7 = d7 * 3.0D - 2.0D;
/*     */         
/* 317 */         if (d7 < 0.0D)
/*     */         {
/* 319 */           d7 /= 2.0D;
/*     */           
/* 321 */           if (d7 < -1.0D)
/*     */           {
/* 323 */             d7 = -1.0D;
/*     */           }
/*     */           
/* 326 */           d7 /= 1.4D;
/* 327 */           d7 /= 2.0D;
/*     */         }
/*     */         else
/*     */         {
/* 331 */           if (d7 > 1.0D)
/*     */           {
/* 333 */             d7 = 1.0D;
/*     */           }
/*     */           
/* 336 */           d7 /= 8.0D;
/*     */         }
/*     */         
/* 339 */         j++;
/* 340 */         double d8 = f3;
/* 341 */         double d9 = f2;
/* 342 */         d8 += d7 * 0.2D;
/* 343 */         d8 = d8 * this.settings.baseSize / 8.0D;
/* 344 */         double d0 = this.settings.baseSize + d8 * 4.0D;
/*     */         
/* 346 */         for (int l1 = 0; l1 < 33; l1++)
/*     */         {
/* 348 */           double d1 = (l1 - d0) * this.settings.stretchY * 128.0D / 256.0D / d9;
/*     */           
/* 350 */           if (d1 < 0.0D)
/*     */           {
/* 352 */             d1 *= 4.0D;
/*     */           }
/*     */           
/* 355 */           double d2 = this.field_147428_e[i] / this.settings.lowerLimitScale;
/* 356 */           double d3 = this.field_147425_f[i] / this.settings.upperLimitScale;
/* 357 */           double d4 = (this.field_147427_d[i] / 10.0D + 1.0D) / 2.0D;
/* 358 */           double d5 = MathHelper.denormalizeClamp(d2, d3, d4) - d1;
/*     */           
/* 360 */           if (l1 > 29)
/*     */           {
/* 362 */             double d6 = (l1 - 29) / 3.0F;
/* 363 */             d5 = d5 * (1.0D - d6) + -10.0D * d6;
/*     */           }
/*     */           
/* 366 */           this.field_147434_q[i] = d5;
/* 367 */           i++;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean chunkExists(int x, int z)
/*     */   {
/* 378 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_)
/*     */   {
/* 386 */     net.minecraft.block.BlockFalling.fallInstantly = true;
/* 387 */     int i = p_73153_2_ * 16;
/* 388 */     int j = p_73153_3_ * 16;
/* 389 */     BlockPos blockpos = new BlockPos(i, 0, j);
/* 390 */     BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(blockpos.add(16, 0, 16));
/* 391 */     this.rand.setSeed(this.worldObj.getSeed());
/* 392 */     long k = this.rand.nextLong() / 2L * 2L + 1L;
/* 393 */     long l = this.rand.nextLong() / 2L * 2L + 1L;
/* 394 */     this.rand.setSeed(p_73153_2_ * k + p_73153_3_ * l ^ this.worldObj.getSeed());
/* 395 */     boolean flag = false;
/* 396 */     ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(p_73153_2_, p_73153_3_);
/*     */     
/* 398 */     if ((this.settings.useMineShafts) && (this.mapFeaturesEnabled))
/*     */     {
/* 400 */       this.mineshaftGenerator.generateStructure(this.worldObj, this.rand, chunkcoordintpair);
/*     */     }
/*     */     
/* 403 */     if ((this.settings.useVillages) && (this.mapFeaturesEnabled))
/*     */     {
/* 405 */       flag = this.villageGenerator.generateStructure(this.worldObj, this.rand, chunkcoordintpair);
/*     */     }
/*     */     
/* 408 */     if ((this.settings.useStrongholds) && (this.mapFeaturesEnabled))
/*     */     {
/* 410 */       this.strongholdGenerator.generateStructure(this.worldObj, this.rand, chunkcoordintpair);
/*     */     }
/*     */     
/* 413 */     if ((this.settings.useTemples) && (this.mapFeaturesEnabled))
/*     */     {
/* 415 */       this.scatteredFeatureGenerator.generateStructure(this.worldObj, this.rand, chunkcoordintpair);
/*     */     }
/*     */     
/* 418 */     if ((this.settings.useMonuments) && (this.mapFeaturesEnabled))
/*     */     {
/* 420 */       this.oceanMonumentGenerator.generateStructure(this.worldObj, this.rand, chunkcoordintpair);
/*     */     }
/*     */     
/* 423 */     if ((biomegenbase != BiomeGenBase.desert) && (biomegenbase != BiomeGenBase.desertHills) && (this.settings.useWaterLakes) && (!flag) && (this.rand.nextInt(this.settings.waterLakeChance) == 0))
/*     */     {
/* 425 */       int i1 = this.rand.nextInt(16) + 8;
/* 426 */       int j1 = this.rand.nextInt(256);
/* 427 */       int k1 = this.rand.nextInt(16) + 8;
/* 428 */       new WorldGenLakes(Blocks.water).generate(this.worldObj, this.rand, blockpos.add(i1, j1, k1));
/*     */     }
/*     */     
/* 431 */     if ((!flag) && (this.rand.nextInt(this.settings.lavaLakeChance / 10) == 0) && (this.settings.useLavaLakes))
/*     */     {
/* 433 */       int i2 = this.rand.nextInt(16) + 8;
/* 434 */       int l2 = this.rand.nextInt(this.rand.nextInt(248) + 8);
/* 435 */       int k3 = this.rand.nextInt(16) + 8;
/*     */       
/* 437 */       if ((l2 < this.worldObj.func_181545_F()) || (this.rand.nextInt(this.settings.lavaLakeChance / 8) == 0))
/*     */       {
/* 439 */         new WorldGenLakes(Blocks.lava).generate(this.worldObj, this.rand, blockpos.add(i2, l2, k3));
/*     */       }
/*     */     }
/*     */     
/* 443 */     if (this.settings.useDungeons)
/*     */     {
/* 445 */       for (int j2 = 0; j2 < this.settings.dungeonChance; j2++)
/*     */       {
/* 447 */         int i3 = this.rand.nextInt(16) + 8;
/* 448 */         int l3 = this.rand.nextInt(256);
/* 449 */         int l1 = this.rand.nextInt(16) + 8;
/* 450 */         new WorldGenDungeons().generate(this.worldObj, this.rand, blockpos.add(i3, l3, l1));
/*     */       }
/*     */     }
/*     */     
/* 454 */     biomegenbase.decorate(this.worldObj, this.rand, new BlockPos(i, 0, j));
/* 455 */     SpawnerAnimals.performWorldGenSpawning(this.worldObj, biomegenbase, i + 8, j + 8, 16, 16, this.rand);
/* 456 */     blockpos = blockpos.add(8, 0, 8);
/*     */     
/* 458 */     for (int k2 = 0; k2 < 16; k2++)
/*     */     {
/* 460 */       for (int j3 = 0; j3 < 16; j3++)
/*     */       {
/* 462 */         BlockPos blockpos1 = this.worldObj.getPrecipitationHeight(blockpos.add(k2, 0, j3));
/* 463 */         BlockPos blockpos2 = blockpos1.down();
/*     */         
/* 465 */         if (this.worldObj.canBlockFreezeWater(blockpos2))
/*     */         {
/* 467 */           this.worldObj.setBlockState(blockpos2, Blocks.ice.getDefaultState(), 2);
/*     */         }
/*     */         
/* 470 */         if (this.worldObj.canSnowAt(blockpos1, true))
/*     */         {
/* 472 */           this.worldObj.setBlockState(blockpos1, Blocks.snow_layer.getDefaultState(), 2);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 477 */     net.minecraft.block.BlockFalling.fallInstantly = false;
/*     */   }
/*     */   
/*     */   public boolean func_177460_a(IChunkProvider p_177460_1_, Chunk p_177460_2_, int p_177460_3_, int p_177460_4_)
/*     */   {
/* 482 */     boolean flag = false;
/*     */     
/* 484 */     if ((this.settings.useMonuments) && (this.mapFeaturesEnabled) && (p_177460_2_.getInhabitedTime() < 3600L))
/*     */     {
/* 486 */       flag |= this.oceanMonumentGenerator.generateStructure(this.worldObj, this.rand, new ChunkCoordIntPair(p_177460_3_, p_177460_4_));
/*     */     }
/*     */     
/* 489 */     return flag;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean saveChunks(boolean p_73151_1_, IProgressUpdate progressCallback)
/*     */   {
/* 498 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void saveExtraData() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean unloadQueuedChunks()
/*     */   {
/* 514 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canSave()
/*     */   {
/* 522 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String makeString()
/*     */   {
/* 530 */     return "RandomLevelSource";
/*     */   }
/*     */   
/*     */   public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
/*     */   {
/* 535 */     BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(pos);
/*     */     
/* 537 */     if (this.mapFeaturesEnabled)
/*     */     {
/* 539 */       if ((creatureType == EnumCreatureType.MONSTER) && (this.scatteredFeatureGenerator.func_175798_a(pos)))
/*     */       {
/* 541 */         return this.scatteredFeatureGenerator.getScatteredFeatureSpawnList();
/*     */       }
/*     */       
/* 544 */       if ((creatureType == EnumCreatureType.MONSTER) && (this.settings.useMonuments) && (this.oceanMonumentGenerator.func_175796_a(this.worldObj, pos)))
/*     */       {
/* 546 */         return this.oceanMonumentGenerator.func_175799_b();
/*     */       }
/*     */     }
/*     */     
/* 550 */     return biomegenbase.getSpawnableList(creatureType);
/*     */   }
/*     */   
/*     */   public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position)
/*     */   {
/* 555 */     return ("Stronghold".equals(structureName)) && (this.strongholdGenerator != null) ? this.strongholdGenerator.getClosestStrongholdPos(worldIn, position) : null;
/*     */   }
/*     */   
/*     */   public int getLoadedChunkCount()
/*     */   {
/* 560 */     return 0;
/*     */   }
/*     */   
/*     */   public void recreateStructures(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_)
/*     */   {
/* 565 */     if ((this.settings.useMineShafts) && (this.mapFeaturesEnabled))
/*     */     {
/* 567 */       this.mineshaftGenerator.generate(this, this.worldObj, p_180514_2_, p_180514_3_, null);
/*     */     }
/*     */     
/* 570 */     if ((this.settings.useVillages) && (this.mapFeaturesEnabled))
/*     */     {
/* 572 */       this.villageGenerator.generate(this, this.worldObj, p_180514_2_, p_180514_3_, null);
/*     */     }
/*     */     
/* 575 */     if ((this.settings.useStrongholds) && (this.mapFeaturesEnabled))
/*     */     {
/* 577 */       this.strongholdGenerator.generate(this, this.worldObj, p_180514_2_, p_180514_3_, null);
/*     */     }
/*     */     
/* 580 */     if ((this.settings.useTemples) && (this.mapFeaturesEnabled))
/*     */     {
/* 582 */       this.scatteredFeatureGenerator.generate(this, this.worldObj, p_180514_2_, p_180514_3_, null);
/*     */     }
/*     */     
/* 585 */     if ((this.settings.useMonuments) && (this.mapFeaturesEnabled))
/*     */     {
/* 587 */       this.oceanMonumentGenerator.generate(this, this.worldObj, p_180514_2_, p_180514_3_, null);
/*     */     }
/*     */   }
/*     */   
/*     */   public Chunk provideChunk(BlockPos blockPosIn)
/*     */   {
/* 593 */     return provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\ChunkProviderGenerate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */