/*     */ package net.minecraft.world.gen;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.EnumCreatureType;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IProgressUpdate;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.biome.BiomeGenBase.SpawnListEntry;
/*     */ import net.minecraft.world.biome.WorldChunkManager;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ import net.minecraft.world.chunk.ChunkPrimer;
/*     */ import net.minecraft.world.chunk.IChunkProvider;
/*     */ 
/*     */ public class ChunkProviderEnd implements IChunkProvider
/*     */ {
/*     */   private Random endRNG;
/*     */   private NoiseGeneratorOctaves noiseGen1;
/*     */   private NoiseGeneratorOctaves noiseGen2;
/*     */   private NoiseGeneratorOctaves noiseGen3;
/*     */   public NoiseGeneratorOctaves noiseGen4;
/*     */   public NoiseGeneratorOctaves noiseGen5;
/*     */   private World endWorld;
/*     */   private double[] densities;
/*     */   private BiomeGenBase[] biomesForGeneration;
/*     */   double[] noiseData1;
/*     */   double[] noiseData2;
/*     */   double[] noiseData3;
/*     */   double[] noiseData4;
/*     */   double[] noiseData5;
/*     */   
/*     */   public ChunkProviderEnd(World worldIn, long p_i2007_2_)
/*     */   {
/*  40 */     this.endWorld = worldIn;
/*  41 */     this.endRNG = new Random(p_i2007_2_);
/*  42 */     this.noiseGen1 = new NoiseGeneratorOctaves(this.endRNG, 16);
/*  43 */     this.noiseGen2 = new NoiseGeneratorOctaves(this.endRNG, 16);
/*  44 */     this.noiseGen3 = new NoiseGeneratorOctaves(this.endRNG, 8);
/*  45 */     this.noiseGen4 = new NoiseGeneratorOctaves(this.endRNG, 10);
/*  46 */     this.noiseGen5 = new NoiseGeneratorOctaves(this.endRNG, 16);
/*     */   }
/*     */   
/*     */   public void func_180520_a(int p_180520_1_, int p_180520_2_, ChunkPrimer p_180520_3_)
/*     */   {
/*  51 */     int i = 2;
/*  52 */     int j = i + 1;
/*  53 */     int k = 33;
/*  54 */     int l = i + 1;
/*  55 */     this.densities = initializeNoiseField(this.densities, p_180520_1_ * i, 0, p_180520_2_ * i, j, k, l);
/*     */     
/*  57 */     for (int i1 = 0; i1 < i; i1++)
/*     */     {
/*  59 */       for (int j1 = 0; j1 < i; j1++)
/*     */       {
/*  61 */         for (int k1 = 0; k1 < 32; k1++)
/*     */         {
/*  63 */           double d0 = 0.25D;
/*  64 */           double d1 = this.densities[(((i1 + 0) * l + j1 + 0) * k + k1 + 0)];
/*  65 */           double d2 = this.densities[(((i1 + 0) * l + j1 + 1) * k + k1 + 0)];
/*  66 */           double d3 = this.densities[(((i1 + 1) * l + j1 + 0) * k + k1 + 0)];
/*  67 */           double d4 = this.densities[(((i1 + 1) * l + j1 + 1) * k + k1 + 0)];
/*  68 */           double d5 = (this.densities[(((i1 + 0) * l + j1 + 0) * k + k1 + 1)] - d1) * d0;
/*  69 */           double d6 = (this.densities[(((i1 + 0) * l + j1 + 1) * k + k1 + 1)] - d2) * d0;
/*  70 */           double d7 = (this.densities[(((i1 + 1) * l + j1 + 0) * k + k1 + 1)] - d3) * d0;
/*  71 */           double d8 = (this.densities[(((i1 + 1) * l + j1 + 1) * k + k1 + 1)] - d4) * d0;
/*     */           
/*  73 */           for (int l1 = 0; l1 < 4; l1++)
/*     */           {
/*  75 */             double d9 = 0.125D;
/*  76 */             double d10 = d1;
/*  77 */             double d11 = d2;
/*  78 */             double d12 = (d3 - d1) * d9;
/*  79 */             double d13 = (d4 - d2) * d9;
/*     */             
/*  81 */             for (int i2 = 0; i2 < 8; i2++)
/*     */             {
/*  83 */               double d14 = 0.125D;
/*  84 */               double d15 = d10;
/*  85 */               double d16 = (d11 - d10) * d14;
/*     */               
/*  87 */               for (int j2 = 0; j2 < 8; j2++)
/*     */               {
/*  89 */                 IBlockState iblockstate = null;
/*     */                 
/*  91 */                 if (d15 > 0.0D)
/*     */                 {
/*  93 */                   iblockstate = Blocks.end_stone.getDefaultState();
/*     */                 }
/*     */                 
/*  96 */                 int k2 = i2 + i1 * 8;
/*  97 */                 int l2 = l1 + k1 * 4;
/*  98 */                 int i3 = j2 + j1 * 8;
/*  99 */                 p_180520_3_.setBlockState(k2, l2, i3, iblockstate);
/* 100 */                 d15 += d16;
/*     */               }
/*     */               
/* 103 */               d10 += d12;
/* 104 */               d11 += d13;
/*     */             }
/*     */             
/* 107 */             d1 += d5;
/* 108 */             d2 += d6;
/* 109 */             d3 += d7;
/* 110 */             d4 += d8;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_180519_a(ChunkPrimer p_180519_1_)
/*     */   {
/* 119 */     for (int i = 0; i < 16; i++)
/*     */     {
/* 121 */       for (int j = 0; j < 16; j++)
/*     */       {
/* 123 */         int k = 1;
/* 124 */         int l = -1;
/* 125 */         IBlockState iblockstate = Blocks.end_stone.getDefaultState();
/* 126 */         IBlockState iblockstate1 = Blocks.end_stone.getDefaultState();
/*     */         
/* 128 */         for (int i1 = 127; i1 >= 0; i1--)
/*     */         {
/* 130 */           IBlockState iblockstate2 = p_180519_1_.getBlockState(i, i1, j);
/*     */           
/* 132 */           if (iblockstate2.getBlock().getMaterial() == Material.air)
/*     */           {
/* 134 */             l = -1;
/*     */           }
/* 136 */           else if (iblockstate2.getBlock() == Blocks.stone)
/*     */           {
/* 138 */             if (l == -1)
/*     */             {
/* 140 */               if (k <= 0)
/*     */               {
/* 142 */                 iblockstate = Blocks.air.getDefaultState();
/* 143 */                 iblockstate1 = Blocks.end_stone.getDefaultState();
/*     */               }
/*     */               
/* 146 */               l = k;
/*     */               
/* 148 */               if (i1 >= 0)
/*     */               {
/* 150 */                 p_180519_1_.setBlockState(i, i1, j, iblockstate);
/*     */               }
/*     */               else
/*     */               {
/* 154 */                 p_180519_1_.setBlockState(i, i1, j, iblockstate1);
/*     */               }
/*     */             }
/* 157 */             else if (l > 0)
/*     */             {
/* 159 */               l--;
/* 160 */               p_180519_1_.setBlockState(i, i1, j, iblockstate1);
/*     */             }
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
/* 174 */     this.endRNG.setSeed(x * 341873128712L + z * 132897987541L);
/* 175 */     ChunkPrimer chunkprimer = new ChunkPrimer();
/* 176 */     this.biomesForGeneration = this.endWorld.getWorldChunkManager().loadBlockGeneratorData(this.biomesForGeneration, x * 16, z * 16, 16, 16);
/* 177 */     func_180520_a(x, z, chunkprimer);
/* 178 */     func_180519_a(chunkprimer);
/* 179 */     Chunk chunk = new Chunk(this.endWorld, chunkprimer, x, z);
/* 180 */     byte[] abyte = chunk.getBiomeArray();
/*     */     
/* 182 */     for (int i = 0; i < abyte.length; i++)
/*     */     {
/* 184 */       abyte[i] = ((byte)this.biomesForGeneration[i].biomeID);
/*     */     }
/*     */     
/* 187 */     chunk.generateSkylightMap();
/* 188 */     return chunk;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private double[] initializeNoiseField(double[] p_73187_1_, int p_73187_2_, int p_73187_3_, int p_73187_4_, int p_73187_5_, int p_73187_6_, int p_73187_7_)
/*     */   {
/* 197 */     if (p_73187_1_ == null)
/*     */     {
/* 199 */       p_73187_1_ = new double[p_73187_5_ * p_73187_6_ * p_73187_7_];
/*     */     }
/*     */     
/* 202 */     double d0 = 684.412D;
/* 203 */     double d1 = 684.412D;
/* 204 */     this.noiseData4 = this.noiseGen4.generateNoiseOctaves(this.noiseData4, p_73187_2_, p_73187_4_, p_73187_5_, p_73187_7_, 1.121D, 1.121D, 0.5D);
/* 205 */     this.noiseData5 = this.noiseGen5.generateNoiseOctaves(this.noiseData5, p_73187_2_, p_73187_4_, p_73187_5_, p_73187_7_, 200.0D, 200.0D, 0.5D);
/* 206 */     d0 *= 2.0D;
/* 207 */     this.noiseData1 = this.noiseGen3.generateNoiseOctaves(this.noiseData1, p_73187_2_, p_73187_3_, p_73187_4_, p_73187_5_, p_73187_6_, p_73187_7_, d0 / 80.0D, d1 / 160.0D, d0 / 80.0D);
/* 208 */     this.noiseData2 = this.noiseGen1.generateNoiseOctaves(this.noiseData2, p_73187_2_, p_73187_3_, p_73187_4_, p_73187_5_, p_73187_6_, p_73187_7_, d0, d1, d0);
/* 209 */     this.noiseData3 = this.noiseGen2.generateNoiseOctaves(this.noiseData3, p_73187_2_, p_73187_3_, p_73187_4_, p_73187_5_, p_73187_6_, p_73187_7_, d0, d1, d0);
/* 210 */     int i = 0;
/*     */     
/* 212 */     for (int j = 0; j < p_73187_5_; j++)
/*     */     {
/* 214 */       for (int k = 0; k < p_73187_7_; k++)
/*     */       {
/* 216 */         float f = (j + p_73187_2_) / 1.0F;
/* 217 */         float f1 = (k + p_73187_4_) / 1.0F;
/* 218 */         float f2 = 100.0F - MathHelper.sqrt_float(f * f + f1 * f1) * 8.0F;
/*     */         
/* 220 */         if (f2 > 80.0F)
/*     */         {
/* 222 */           f2 = 80.0F;
/*     */         }
/*     */         
/* 225 */         if (f2 < -100.0F)
/*     */         {
/* 227 */           f2 = -100.0F;
/*     */         }
/*     */         
/* 230 */         for (int l = 0; l < p_73187_6_; l++)
/*     */         {
/* 232 */           double d2 = 0.0D;
/* 233 */           double d3 = this.noiseData2[i] / 512.0D;
/* 234 */           double d4 = this.noiseData3[i] / 512.0D;
/* 235 */           double d5 = (this.noiseData1[i] / 10.0D + 1.0D) / 2.0D;
/*     */           
/* 237 */           if (d5 < 0.0D)
/*     */           {
/* 239 */             d2 = d3;
/*     */           }
/* 241 */           else if (d5 > 1.0D)
/*     */           {
/* 243 */             d2 = d4;
/*     */           }
/*     */           else
/*     */           {
/* 247 */             d2 = d3 + (d4 - d3) * d5;
/*     */           }
/*     */           
/* 250 */           d2 -= 8.0D;
/* 251 */           d2 += f2;
/* 252 */           int i1 = 2;
/*     */           
/* 254 */           if (l > p_73187_6_ / 2 - i1)
/*     */           {
/* 256 */             double d6 = (l - (p_73187_6_ / 2 - i1)) / 64.0F;
/* 257 */             d6 = MathHelper.clamp_double(d6, 0.0D, 1.0D);
/* 258 */             d2 = d2 * (1.0D - d6) + -3000.0D * d6;
/*     */           }
/*     */           
/* 261 */           i1 = 8;
/*     */           
/* 263 */           if (l < i1)
/*     */           {
/* 265 */             double d7 = (i1 - l) / (i1 - 1.0F);
/* 266 */             d2 = d2 * (1.0D - d7) + -30.0D * d7;
/*     */           }
/*     */           
/* 269 */           p_73187_1_[i] = d2;
/* 270 */           i++;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 275 */     return p_73187_1_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean chunkExists(int x, int z)
/*     */   {
/* 283 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void populate(IChunkProvider p_73153_1_, int p_73153_2_, int p_73153_3_)
/*     */   {
/* 291 */     net.minecraft.block.BlockFalling.fallInstantly = true;
/* 292 */     BlockPos blockpos = new BlockPos(p_73153_2_ * 16, 0, p_73153_3_ * 16);
/* 293 */     this.endWorld.getBiomeGenForCoords(blockpos.add(16, 0, 16)).decorate(this.endWorld, this.endWorld.rand, blockpos);
/* 294 */     net.minecraft.block.BlockFalling.fallInstantly = false;
/*     */   }
/*     */   
/*     */   public boolean func_177460_a(IChunkProvider p_177460_1_, Chunk p_177460_2_, int p_177460_3_, int p_177460_4_)
/*     */   {
/* 299 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean saveChunks(boolean p_73151_1_, IProgressUpdate progressCallback)
/*     */   {
/* 308 */     return true;
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
/* 324 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canSave()
/*     */   {
/* 332 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String makeString()
/*     */   {
/* 340 */     return "RandomLevelSource";
/*     */   }
/*     */   
/*     */   public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
/*     */   {
/* 345 */     return this.endWorld.getBiomeGenForCoords(pos).getSpawnableList(creatureType);
/*     */   }
/*     */   
/*     */   public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position)
/*     */   {
/* 350 */     return null;
/*     */   }
/*     */   
/*     */   public int getLoadedChunkCount()
/*     */   {
/* 355 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public void recreateStructures(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_) {}
/*     */   
/*     */ 
/*     */   public Chunk provideChunk(BlockPos blockPosIn)
/*     */   {
/* 364 */     return provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\ChunkProviderEnd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */