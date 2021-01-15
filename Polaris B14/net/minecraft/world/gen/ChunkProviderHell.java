/*     */ package net.minecraft.world.gen;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockStaticLiquid;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.block.state.pattern.BlockHelper;
/*     */ import net.minecraft.entity.EnumCreatureType;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IProgressUpdate;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.ChunkCoordIntPair;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
/*     */ import net.minecraft.world.biome.WorldChunkManager;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.minecraft.world.chunk.ChunkPrimer;
/*     */ import net.minecraft.world.chunk.IChunkProvider;
/*     */ import net.minecraft.world.gen.feature.WorldGenFire;
/*     */ import net.minecraft.world.gen.feature.WorldGenGlowStone1;
/*     */ import net.minecraft.world.gen.feature.WorldGenGlowStone2;
/*     */ import net.minecraft.world.gen.feature.WorldGenHellLava;
/*     */ import net.minecraft.world.gen.feature.WorldGenMinable;
/*     */ import net.minecraft.world.gen.feature.WorldGenerator;
/*     */ import net.minecraft.world.gen.structure.MapGenNetherBridge;
/*     */ 
/*     */ 
/*     */ public class ChunkProviderHell
/*     */   implements IChunkProvider
/*     */ {
/*     */   private final World worldObj;
/*     */   private final boolean field_177466_i;
/*     */   private final Random hellRNG;
/*  38 */   private double[] slowsandNoise = new double['Ā'];
/*  39 */   private double[] gravelNoise = new double['Ā'];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  44 */   private double[] netherrackExclusivityNoise = new double['Ā'];
/*     */   
/*     */   private double[] noiseField;
/*     */   
/*     */   private final NoiseGeneratorOctaves netherNoiseGen1;
/*     */   
/*     */   private final NoiseGeneratorOctaves netherNoiseGen2;
/*     */   
/*     */   private final NoiseGeneratorOctaves netherNoiseGen3;
/*     */   
/*     */   private final NoiseGeneratorOctaves slowsandGravelNoiseGen;
/*     */   
/*     */   private final NoiseGeneratorOctaves netherrackExculsivityNoiseGen;
/*     */   
/*     */   public final NoiseGeneratorOctaves netherNoiseGen6;
/*     */   
/*     */   public final NoiseGeneratorOctaves netherNoiseGen7;
/*  61 */   private final WorldGenFire field_177470_t = new WorldGenFire();
/*  62 */   private final WorldGenGlowStone1 field_177469_u = new WorldGenGlowStone1();
/*  63 */   private final WorldGenGlowStone2 field_177468_v = new WorldGenGlowStone2();
/*  64 */   private final WorldGenerator field_177467_w = new WorldGenMinable(Blocks.quartz_ore.getDefaultState(), 14, BlockHelper.forBlock(Blocks.netherrack));
/*  65 */   private final WorldGenHellLava field_177473_x = new WorldGenHellLava(Blocks.flowing_lava, true);
/*  66 */   private final WorldGenHellLava field_177472_y = new WorldGenHellLava(Blocks.flowing_lava, false);
/*  67 */   private final GeneratorBushFeature field_177471_z = new GeneratorBushFeature(Blocks.brown_mushroom);
/*  68 */   private final GeneratorBushFeature field_177465_A = new GeneratorBushFeature(Blocks.red_mushroom);
/*  69 */   private final MapGenNetherBridge genNetherBridge = new MapGenNetherBridge();
/*  70 */   private final MapGenBase netherCaveGenerator = new MapGenCavesHell();
/*     */   double[] noiseData1;
/*     */   double[] noiseData2;
/*     */   double[] noiseData3;
/*     */   double[] noiseData4;
/*     */   double[] noiseData5;
/*     */   
/*     */   public ChunkProviderHell(World worldIn, boolean p_i45637_2_, long p_i45637_3_)
/*     */   {
/*  79 */     this.worldObj = worldIn;
/*  80 */     this.field_177466_i = p_i45637_2_;
/*  81 */     this.hellRNG = new Random(p_i45637_3_);
/*  82 */     this.netherNoiseGen1 = new NoiseGeneratorOctaves(this.hellRNG, 16);
/*  83 */     this.netherNoiseGen2 = new NoiseGeneratorOctaves(this.hellRNG, 16);
/*  84 */     this.netherNoiseGen3 = new NoiseGeneratorOctaves(this.hellRNG, 8);
/*  85 */     this.slowsandGravelNoiseGen = new NoiseGeneratorOctaves(this.hellRNG, 4);
/*  86 */     this.netherrackExculsivityNoiseGen = new NoiseGeneratorOctaves(this.hellRNG, 4);
/*  87 */     this.netherNoiseGen6 = new NoiseGeneratorOctaves(this.hellRNG, 10);
/*  88 */     this.netherNoiseGen7 = new NoiseGeneratorOctaves(this.hellRNG, 16);
/*  89 */     worldIn.func_181544_b(63);
/*     */   }
/*     */   
/*     */   public void func_180515_a(int p_180515_1_, int p_180515_2_, ChunkPrimer p_180515_3_)
/*     */   {
/*  94 */     int i = 4;
/*  95 */     int j = this.worldObj.func_181545_F() / 2 + 1;
/*  96 */     int k = i + 1;
/*  97 */     int l = 17;
/*  98 */     int i1 = i + 1;
/*  99 */     this.noiseField = initializeNoiseField(this.noiseField, p_180515_1_ * i, 0, p_180515_2_ * i, k, l, i1);
/*     */     
/* 101 */     for (int j1 = 0; j1 < i; j1++)
/*     */     {
/* 103 */       for (int k1 = 0; k1 < i; k1++)
/*     */       {
/* 105 */         for (int l1 = 0; l1 < 16; l1++)
/*     */         {
/* 107 */           double d0 = 0.125D;
/* 108 */           double d1 = this.noiseField[(((j1 + 0) * i1 + k1 + 0) * l + l1 + 0)];
/* 109 */           double d2 = this.noiseField[(((j1 + 0) * i1 + k1 + 1) * l + l1 + 0)];
/* 110 */           double d3 = this.noiseField[(((j1 + 1) * i1 + k1 + 0) * l + l1 + 0)];
/* 111 */           double d4 = this.noiseField[(((j1 + 1) * i1 + k1 + 1) * l + l1 + 0)];
/* 112 */           double d5 = (this.noiseField[(((j1 + 0) * i1 + k1 + 0) * l + l1 + 1)] - d1) * d0;
/* 113 */           double d6 = (this.noiseField[(((j1 + 0) * i1 + k1 + 1) * l + l1 + 1)] - d2) * d0;
/* 114 */           double d7 = (this.noiseField[(((j1 + 1) * i1 + k1 + 0) * l + l1 + 1)] - d3) * d0;
/* 115 */           double d8 = (this.noiseField[(((j1 + 1) * i1 + k1 + 1) * l + l1 + 1)] - d4) * d0;
/*     */           
/* 117 */           for (int i2 = 0; i2 < 8; i2++)
/*     */           {
/* 119 */             double d9 = 0.25D;
/* 120 */             double d10 = d1;
/* 121 */             double d11 = d2;
/* 122 */             double d12 = (d3 - d1) * d9;
/* 123 */             double d13 = (d4 - d2) * d9;
/*     */             
/* 125 */             for (int j2 = 0; j2 < 4; j2++)
/*     */             {
/* 127 */               double d14 = 0.25D;
/* 128 */               double d15 = d10;
/* 129 */               double d16 = (d11 - d10) * d14;
/*     */               
/* 131 */               for (int k2 = 0; k2 < 4; k2++)
/*     */               {
/* 133 */                 IBlockState iblockstate = null;
/*     */                 
/* 135 */                 if (l1 * 8 + i2 < j)
/*     */                 {
/* 137 */                   iblockstate = Blocks.lava.getDefaultState();
/*     */                 }
/*     */                 
/* 140 */                 if (d15 > 0.0D)
/*     */                 {
/* 142 */                   iblockstate = Blocks.netherrack.getDefaultState();
/*     */                 }
/*     */                 
/* 145 */                 int l2 = j2 + j1 * 4;
/* 146 */                 int i3 = i2 + l1 * 8;
/* 147 */                 int j3 = k2 + k1 * 4;
/* 148 */                 p_180515_3_.setBlockState(l2, i3, j3, iblockstate);
/* 149 */                 d15 += d16;
/*     */               }
/*     */               
/* 152 */               d10 += d12;
/* 153 */               d11 += d13;
/*     */             }
/*     */             
/* 156 */             d1 += d5;
/* 157 */             d2 += d6;
/* 158 */             d3 += d7;
/* 159 */             d4 += d8;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_180516_b(int p_180516_1_, int p_180516_2_, ChunkPrimer p_180516_3_)
/*     */   {
/* 168 */     int i = this.worldObj.func_181545_F() + 1;
/* 169 */     double d0 = 0.03125D;
/* 170 */     this.slowsandNoise = this.slowsandGravelNoiseGen.generateNoiseOctaves(this.slowsandNoise, p_180516_1_ * 16, p_180516_2_ * 16, 0, 16, 16, 1, d0, d0, 1.0D);
/* 171 */     this.gravelNoise = this.slowsandGravelNoiseGen.generateNoiseOctaves(this.gravelNoise, p_180516_1_ * 16, 109, p_180516_2_ * 16, 16, 1, 16, d0, 1.0D, d0);
/* 172 */     this.netherrackExclusivityNoise = this.netherrackExculsivityNoiseGen.generateNoiseOctaves(this.netherrackExclusivityNoise, p_180516_1_ * 16, p_180516_2_ * 16, 0, 16, 16, 1, d0 * 2.0D, d0 * 2.0D, d0 * 2.0D);
/*     */     
/* 174 */     for (int j = 0; j < 16; j++)
/*     */     {
/* 176 */       for (int k = 0; k < 16; k++)
/*     */       {
/* 178 */         boolean flag = this.slowsandNoise[(j + k * 16)] + this.hellRNG.nextDouble() * 0.2D > 0.0D;
/* 179 */         boolean flag1 = this.gravelNoise[(j + k * 16)] + this.hellRNG.nextDouble() * 0.2D > 0.0D;
/* 180 */         int l = (int)(this.netherrackExclusivityNoise[(j + k * 16)] / 3.0D + 3.0D + this.hellRNG.nextDouble() * 0.25D);
/* 181 */         int i1 = -1;
/* 182 */         IBlockState iblockstate = Blocks.netherrack.getDefaultState();
/* 183 */         IBlockState iblockstate1 = Blocks.netherrack.getDefaultState();
/*     */         
/* 185 */         for (int j1 = 127; j1 >= 0; j1--)
/*     */         {
/* 187 */           if ((j1 < 127 - this.hellRNG.nextInt(5)) && (j1 > this.hellRNG.nextInt(5)))
/*     */           {
/* 189 */             IBlockState iblockstate2 = p_180516_3_.getBlockState(k, j1, j);
/*     */             
/* 191 */             if ((iblockstate2.getBlock() != null) && (iblockstate2.getBlock().getMaterial() != Material.air))
/*     */             {
/* 193 */               if (iblockstate2.getBlock() == Blocks.netherrack)
/*     */               {
/* 195 */                 if (i1 == -1)
/*     */                 {
/* 197 */                   if (l <= 0)
/*     */                   {
/* 199 */                     iblockstate = null;
/* 200 */                     iblockstate1 = Blocks.netherrack.getDefaultState();
/*     */                   }
/* 202 */                   else if ((j1 >= i - 4) && (j1 <= i + 1))
/*     */                   {
/* 204 */                     iblockstate = Blocks.netherrack.getDefaultState();
/* 205 */                     iblockstate1 = Blocks.netherrack.getDefaultState();
/*     */                     
/* 207 */                     if (flag1)
/*     */                     {
/* 209 */                       iblockstate = Blocks.gravel.getDefaultState();
/* 210 */                       iblockstate1 = Blocks.netherrack.getDefaultState();
/*     */                     }
/*     */                     
/* 213 */                     if (flag)
/*     */                     {
/* 215 */                       iblockstate = Blocks.soul_sand.getDefaultState();
/* 216 */                       iblockstate1 = Blocks.soul_sand.getDefaultState();
/*     */                     }
/*     */                   }
/*     */                   
/* 220 */                   if ((j1 < i) && ((iblockstate == null) || (iblockstate.getBlock().getMaterial() == Material.air)))
/*     */                   {
/* 222 */                     iblockstate = Blocks.lava.getDefaultState();
/*     */                   }
/*     */                   
/* 225 */                   i1 = l;
/*     */                   
/* 227 */                   if (j1 >= i - 1)
/*     */                   {
/* 229 */                     p_180516_3_.setBlockState(k, j1, j, iblockstate);
/*     */                   }
/*     */                   else
/*     */                   {
/* 233 */                     p_180516_3_.setBlockState(k, j1, j, iblockstate1);
/*     */                   }
/*     */                 }
/* 236 */                 else if (i1 > 0)
/*     */                 {
/* 238 */                   i1--;
/* 239 */                   p_180516_3_.setBlockState(k, j1, j, iblockstate1);
/*     */                 }
/*     */                 
/*     */               }
/*     */             }
/*     */             else {
/* 245 */               i1 = -1;
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 250 */             p_180516_3_.setBlockState(k, j1, j, Blocks.bedrock.getDefaultState());
/*     */           }
/*     */         }
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
/* 263 */     this.hellRNG.setSeed(x * 341873128712L + z * 132897987541L);
/* 264 */     ChunkPrimer chunkprimer = new ChunkPrimer();
/* 265 */     func_180515_a(x, z, chunkprimer);
/* 266 */     func_180516_b(x, z, chunkprimer);
/* 267 */     this.netherCaveGenerator.generate(this, this.worldObj, x, z, chunkprimer);
/*     */     
/* 269 */     if (this.field_177466_i)
/*     */     {
/* 271 */       this.genNetherBridge.generate(this, this.worldObj, x, z, chunkprimer);
/*     */     }
/*     */     
/* 274 */     Chunk chunk = new Chunk(this.worldObj, chunkprimer, x, z);
/* 275 */     BiomeGenBase[] abiomegenbase = this.worldObj.getWorldChunkManager().loadBlockGeneratorData(null, x * 16, z * 16, 16, 16);
/* 276 */     byte[] abyte = chunk.getBiomeArray();
/*     */     
/* 278 */     for (int i = 0; i < abyte.length; i++)
/*     */     {
/* 280 */       abyte[i] = ((byte)abiomegenbase[i].biomeID);
/*     */     }
/*     */     
/* 283 */     chunk.resetRelightChecks();
/* 284 */     return chunk;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private double[] initializeNoiseField(double[] p_73164_1_, int p_73164_2_, int p_73164_3_, int p_73164_4_, int p_73164_5_, int p_73164_6_, int p_73164_7_)
/*     */   {
/* 293 */     if (p_73164_1_ == null)
/*     */     {
/* 295 */       p_73164_1_ = new double[p_73164_5_ * p_73164_6_ * p_73164_7_];
/*     */     }
/*     */     
/* 298 */     double d0 = 684.412D;
/* 299 */     double d1 = 2053.236D;
/* 300 */     this.noiseData4 = this.netherNoiseGen6.generateNoiseOctaves(this.noiseData4, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, 1, p_73164_7_, 1.0D, 0.0D, 1.0D);
/* 301 */     this.noiseData5 = this.netherNoiseGen7.generateNoiseOctaves(this.noiseData5, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, 1, p_73164_7_, 100.0D, 0.0D, 100.0D);
/* 302 */     this.noiseData1 = this.netherNoiseGen3.generateNoiseOctaves(this.noiseData1, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, p_73164_6_, p_73164_7_, d0 / 80.0D, d1 / 60.0D, d0 / 80.0D);
/* 303 */     this.noiseData2 = this.netherNoiseGen1.generateNoiseOctaves(this.noiseData2, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, p_73164_6_, p_73164_7_, d0, d1, d0);
/* 304 */     this.noiseData3 = this.netherNoiseGen2.generateNoiseOctaves(this.noiseData3, p_73164_2_, p_73164_3_, p_73164_4_, p_73164_5_, p_73164_6_, p_73164_7_, d0, d1, d0);
/* 305 */     int i = 0;
/* 306 */     double[] adouble = new double[p_73164_6_];
/*     */     
/* 308 */     for (int j = 0; j < p_73164_6_; j++)
/*     */     {
/* 310 */       adouble[j] = (Math.cos(j * 3.141592653589793D * 6.0D / p_73164_6_) * 2.0D);
/* 311 */       double d2 = j;
/*     */       
/* 313 */       if (j > p_73164_6_ / 2)
/*     */       {
/* 315 */         d2 = p_73164_6_ - 1 - j;
/*     */       }
/*     */       
/* 318 */       if (d2 < 4.0D)
/*     */       {
/* 320 */         d2 = 4.0D - d2;
/* 321 */         adouble[j] -= d2 * d2 * d2 * 10.0D;
/*     */       }
/*     */     }
/*     */     
/* 325 */     for (int l = 0; l < p_73164_5_; l++)
/*     */     {
/* 327 */       for (int i1 = 0; i1 < p_73164_7_; i1++)
/*     */       {
/* 329 */         double d3 = 0.0D;
/*     */         
/* 331 */         for (int k = 0; k < p_73164_6_; k++)
/*     */         {
/* 333 */           double d4 = 0.0D;
/* 334 */           double d5 = adouble[k];
/* 335 */           double d6 = this.noiseData2[i] / 512.0D;
/* 336 */           double d7 = this.noiseData3[i] / 512.0D;
/* 337 */           double d8 = (this.noiseData1[i] / 10.0D + 1.0D) / 2.0D;
/*     */           
/* 339 */           if (d8 < 0.0D)
/*     */           {
/* 341 */             d4 = d6;
/*     */           }
/* 343 */           else if (d8 > 1.0D)
/*     */           {
/* 345 */             d4 = d7;
/*     */           }
/*     */           else
/*     */           {
/* 349 */             d4 = d6 + (d7 - d6) * d8;
/*     */           }
/*     */           
/* 352 */           d4 -= d5;
/*     */           
/* 354 */           if (k > p_73164_6_ - 4)
/*     */           {
/* 356 */             double d9 = (k - (p_73164_6_ - 4)) / 3.0F;
/* 357 */             d4 = d4 * (1.0D - d9) + -10.0D * d9;
/*     */           }
/*     */           
/* 360 */           if (k < d3)
/*     */           {
/* 362 */             double d10 = (d3 - k) / 4.0D;
/* 363 */             d10 = MathHelper.clamp_double(d10, 0.0D, 1.0D);
/* 364 */             d4 = d4 * (1.0D - d10) + -10.0D * d10;
/*     */           }
/*     */           
/* 367 */           p_73164_1_[i] = d4;
/* 368 */           i++;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 373 */     return p_73164_1_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean chunkExists(int x, int z)
/*     */   {
/* 381 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_)
/*     */   {
/* 389 */     net.minecraft.block.BlockFalling.fallInstantly = true;
/* 390 */     BlockPos blockpos = new BlockPos(p_73153_2_ * 16, 0, p_73153_3_ * 16);
/* 391 */     ChunkCoordIntPair chunkcoordintpair = new ChunkCoordIntPair(p_73153_2_, p_73153_3_);
/* 392 */     this.genNetherBridge.generateStructure(this.worldObj, this.hellRNG, chunkcoordintpair);
/*     */     
/* 394 */     for (int i = 0; i < 8; i++)
/*     */     {
/* 396 */       this.field_177472_y.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(120) + 4, this.hellRNG.nextInt(16) + 8));
/*     */     }
/*     */     
/* 399 */     for (int j = 0; j < this.hellRNG.nextInt(this.hellRNG.nextInt(10) + 1) + 1; j++)
/*     */     {
/* 401 */       this.field_177470_t.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(120) + 4, this.hellRNG.nextInt(16) + 8));
/*     */     }
/*     */     
/* 404 */     for (int k = 0; k < this.hellRNG.nextInt(this.hellRNG.nextInt(10) + 1); k++)
/*     */     {
/* 406 */       this.field_177469_u.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(120) + 4, this.hellRNG.nextInt(16) + 8));
/*     */     }
/*     */     
/* 409 */     for (int l = 0; l < 10; l++)
/*     */     {
/* 411 */       this.field_177468_v.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(128), this.hellRNG.nextInt(16) + 8));
/*     */     }
/*     */     
/* 414 */     if (this.hellRNG.nextBoolean())
/*     */     {
/* 416 */       this.field_177471_z.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(128), this.hellRNG.nextInt(16) + 8));
/*     */     }
/*     */     
/* 419 */     if (this.hellRNG.nextBoolean())
/*     */     {
/* 421 */       this.field_177465_A.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16) + 8, this.hellRNG.nextInt(128), this.hellRNG.nextInt(16) + 8));
/*     */     }
/*     */     
/* 424 */     for (int i1 = 0; i1 < 16; i1++)
/*     */     {
/* 426 */       this.field_177467_w.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16), this.hellRNG.nextInt(108) + 10, this.hellRNG.nextInt(16)));
/*     */     }
/*     */     
/* 429 */     for (int j1 = 0; j1 < 16; j1++)
/*     */     {
/* 431 */       this.field_177473_x.generate(this.worldObj, this.hellRNG, blockpos.add(this.hellRNG.nextInt(16), this.hellRNG.nextInt(108) + 10, this.hellRNG.nextInt(16)));
/*     */     }
/*     */     
/* 434 */     net.minecraft.block.BlockFalling.fallInstantly = false;
/*     */   }
/*     */   
/*     */   public boolean func_177460_a(IChunkProvider p_177460_1_, Chunk p_177460_2_, int p_177460_3_, int p_177460_4_)
/*     */   {
/* 439 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean saveChunks(boolean p_73151_1_, IProgressUpdate progressCallback)
/*     */   {
/* 448 */     return true;
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
/* 464 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canSave()
/*     */   {
/* 472 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String makeString()
/*     */   {
/* 480 */     return "HellRandomLevelSource";
/*     */   }
/*     */   
/*     */   public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
/*     */   {
/* 485 */     if (creatureType == EnumCreatureType.MONSTER)
/*     */     {
/* 487 */       if (this.genNetherBridge.func_175795_b(pos))
/*     */       {
/* 489 */         return this.genNetherBridge.getSpawnList();
/*     */       }
/*     */       
/* 492 */       if ((this.genNetherBridge.func_175796_a(this.worldObj, pos)) && (this.worldObj.getBlockState(pos.down()).getBlock() == Blocks.nether_brick))
/*     */       {
/* 494 */         return this.genNetherBridge.getSpawnList();
/*     */       }
/*     */     }
/*     */     
/* 498 */     BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(pos);
/* 499 */     return biomegenbase.getSpawnableList(creatureType);
/*     */   }
/*     */   
/*     */   public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position)
/*     */   {
/* 504 */     return null;
/*     */   }
/*     */   
/*     */   public int getLoadedChunkCount()
/*     */   {
/* 509 */     return 0;
/*     */   }
/*     */   
/*     */   public void recreateStructures(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_)
/*     */   {
/* 514 */     this.genNetherBridge.generate(this, this.worldObj, p_180514_2_, p_180514_3_, null);
/*     */   }
/*     */   
/*     */   public Chunk provideChunk(BlockPos blockPosIn)
/*     */   {
/* 519 */     return provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\ChunkProviderHell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */