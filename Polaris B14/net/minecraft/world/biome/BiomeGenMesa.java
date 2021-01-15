/*     */ package net.minecraft.world.biome;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockColored;
/*     */ import net.minecraft.block.BlockSand;
/*     */ import net.minecraft.block.BlockSand.EnumType;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.chunk.ChunkPrimer;
/*     */ import net.minecraft.world.gen.NoiseGeneratorPerlin;
/*     */ 
/*     */ public class BiomeGenMesa extends BiomeGenBase
/*     */ {
/*     */   private IBlockState[] field_150621_aC;
/*     */   private long field_150622_aD;
/*     */   private NoiseGeneratorPerlin field_150623_aE;
/*     */   private NoiseGeneratorPerlin field_150624_aF;
/*     */   private NoiseGeneratorPerlin field_150625_aG;
/*     */   private boolean field_150626_aH;
/*     */   private boolean field_150620_aI;
/*     */   
/*     */   public BiomeGenMesa(int p_i45380_1_, boolean p_i45380_2_, boolean p_i45380_3_)
/*     */   {
/*  30 */     super(p_i45380_1_);
/*  31 */     this.field_150626_aH = p_i45380_2_;
/*  32 */     this.field_150620_aI = p_i45380_3_;
/*  33 */     setDisableRain();
/*  34 */     setTemperatureRainfall(2.0F, 0.0F);
/*  35 */     this.spawnableCreatureList.clear();
/*  36 */     this.topBlock = Blocks.sand.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND);
/*  37 */     this.fillerBlock = Blocks.stained_hardened_clay.getDefaultState();
/*  38 */     this.theBiomeDecorator.treesPerChunk = 64537;
/*  39 */     this.theBiomeDecorator.deadBushPerChunk = 20;
/*  40 */     this.theBiomeDecorator.reedsPerChunk = 3;
/*  41 */     this.theBiomeDecorator.cactiPerChunk = 5;
/*  42 */     this.theBiomeDecorator.flowersPerChunk = 0;
/*  43 */     this.spawnableCreatureList.clear();
/*     */     
/*  45 */     if (p_i45380_3_)
/*     */     {
/*  47 */       this.theBiomeDecorator.treesPerChunk = 5;
/*     */     }
/*     */   }
/*     */   
/*     */   public net.minecraft.world.gen.feature.WorldGenAbstractTree genBigTreeChance(Random rand)
/*     */   {
/*  53 */     return this.worldGeneratorTrees;
/*     */   }
/*     */   
/*     */   public int getFoliageColorAtPos(BlockPos pos)
/*     */   {
/*  58 */     return 10387789;
/*     */   }
/*     */   
/*     */   public int getGrassColorAtPos(BlockPos pos)
/*     */   {
/*  63 */     return 9470285;
/*     */   }
/*     */   
/*     */   public void decorate(World worldIn, Random rand, BlockPos pos)
/*     */   {
/*  68 */     super.decorate(worldIn, rand, pos);
/*     */   }
/*     */   
/*     */   public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int p_180622_4_, int p_180622_5_, double p_180622_6_)
/*     */   {
/*  73 */     if ((this.field_150621_aC == null) || (this.field_150622_aD != worldIn.getSeed()))
/*     */     {
/*  75 */       func_150619_a(worldIn.getSeed());
/*     */     }
/*     */     
/*  78 */     if ((this.field_150623_aE == null) || (this.field_150624_aF == null) || (this.field_150622_aD != worldIn.getSeed()))
/*     */     {
/*  80 */       Random random = new Random(this.field_150622_aD);
/*  81 */       this.field_150623_aE = new NoiseGeneratorPerlin(random, 4);
/*  82 */       this.field_150624_aF = new NoiseGeneratorPerlin(random, 1);
/*     */     }
/*     */     
/*  85 */     this.field_150622_aD = worldIn.getSeed();
/*  86 */     double d4 = 0.0D;
/*     */     
/*  88 */     if (this.field_150626_aH)
/*     */     {
/*  90 */       int i = (p_180622_4_ & 0xFFFFFFF0) + (p_180622_5_ & 0xF);
/*  91 */       int j = (p_180622_5_ & 0xFFFFFFF0) + (p_180622_4_ & 0xF);
/*  92 */       double d0 = Math.min(Math.abs(p_180622_6_), this.field_150623_aE.func_151601_a(i * 0.25D, j * 0.25D));
/*     */       
/*  94 */       if (d0 > 0.0D)
/*     */       {
/*  96 */         double d1 = 0.001953125D;
/*  97 */         double d2 = Math.abs(this.field_150624_aF.func_151601_a(i * d1, j * d1));
/*  98 */         d4 = d0 * d0 * 2.5D;
/*  99 */         double d3 = Math.ceil(d2 * 50.0D) + 14.0D;
/*     */         
/* 101 */         if (d4 > d3)
/*     */         {
/* 103 */           d4 = d3;
/*     */         }
/*     */         
/* 106 */         d4 += 64.0D;
/*     */       }
/*     */     }
/*     */     
/* 110 */     int j1 = p_180622_4_ & 0xF;
/* 111 */     int k1 = p_180622_5_ & 0xF;
/* 112 */     int l1 = worldIn.func_181545_F();
/* 113 */     IBlockState iblockstate = Blocks.stained_hardened_clay.getDefaultState();
/* 114 */     IBlockState iblockstate3 = this.fillerBlock;
/* 115 */     int k = (int)(p_180622_6_ / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
/* 116 */     boolean flag = Math.cos(p_180622_6_ / 3.0D * 3.141592653589793D) > 0.0D;
/* 117 */     int l = -1;
/* 118 */     boolean flag1 = false;
/*     */     
/* 120 */     for (int i1 = 255; i1 >= 0; i1--)
/*     */     {
/* 122 */       if ((chunkPrimerIn.getBlockState(k1, i1, j1).getBlock().getMaterial() == Material.air) && (i1 < (int)d4))
/*     */       {
/* 124 */         chunkPrimerIn.setBlockState(k1, i1, j1, Blocks.stone.getDefaultState());
/*     */       }
/*     */       
/* 127 */       if (i1 <= rand.nextInt(5))
/*     */       {
/* 129 */         chunkPrimerIn.setBlockState(k1, i1, j1, Blocks.bedrock.getDefaultState());
/*     */       }
/*     */       else
/*     */       {
/* 133 */         IBlockState iblockstate1 = chunkPrimerIn.getBlockState(k1, i1, j1);
/*     */         
/* 135 */         if (iblockstate1.getBlock().getMaterial() == Material.air)
/*     */         {
/* 137 */           l = -1;
/*     */         }
/* 139 */         else if (iblockstate1.getBlock() == Blocks.stone)
/*     */         {
/* 141 */           if (l == -1)
/*     */           {
/* 143 */             flag1 = false;
/*     */             
/* 145 */             if (k <= 0)
/*     */             {
/* 147 */               iblockstate = null;
/* 148 */               iblockstate3 = Blocks.stone.getDefaultState();
/*     */             }
/* 150 */             else if ((i1 >= l1 - 4) && (i1 <= l1 + 1))
/*     */             {
/* 152 */               iblockstate = Blocks.stained_hardened_clay.getDefaultState();
/* 153 */               iblockstate3 = this.fillerBlock;
/*     */             }
/*     */             
/* 156 */             if ((i1 < l1) && ((iblockstate == null) || (iblockstate.getBlock().getMaterial() == Material.air)))
/*     */             {
/* 158 */               iblockstate = Blocks.water.getDefaultState();
/*     */             }
/*     */             
/* 161 */             l = k + Math.max(0, i1 - l1);
/*     */             
/* 163 */             if (i1 < l1 - 1)
/*     */             {
/* 165 */               chunkPrimerIn.setBlockState(k1, i1, j1, iblockstate3);
/*     */               
/* 167 */               if (iblockstate3.getBlock() == Blocks.stained_hardened_clay)
/*     */               {
/* 169 */                 chunkPrimerIn.setBlockState(k1, i1, j1, iblockstate3.getBlock().getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE));
/*     */               }
/*     */             }
/* 172 */             else if ((this.field_150620_aI) && (i1 > 86 + k * 2))
/*     */             {
/* 174 */               if (flag)
/*     */               {
/* 176 */                 chunkPrimerIn.setBlockState(k1, i1, j1, Blocks.dirt.getDefaultState().withProperty(net.minecraft.block.BlockDirt.VARIANT, net.minecraft.block.BlockDirt.DirtType.COARSE_DIRT));
/*     */               }
/*     */               else
/*     */               {
/* 180 */                 chunkPrimerIn.setBlockState(k1, i1, j1, Blocks.grass.getDefaultState());
/*     */               }
/*     */             }
/* 183 */             else if (i1 <= l1 + 3 + k)
/*     */             {
/* 185 */               chunkPrimerIn.setBlockState(k1, i1, j1, this.topBlock);
/* 186 */               flag1 = true;
/*     */             }
/*     */             else
/*     */             {
/*     */               IBlockState iblockstate4;
/*     */               IBlockState iblockstate4;
/* 192 */               if ((i1 >= 64) && (i1 <= 127)) {
/*     */                 IBlockState iblockstate4;
/* 194 */                 if (flag)
/*     */                 {
/* 196 */                   iblockstate4 = Blocks.hardened_clay.getDefaultState();
/*     */                 }
/*     */                 else
/*     */                 {
/* 200 */                   iblockstate4 = func_180629_a(p_180622_4_, i1, p_180622_5_);
/*     */                 }
/*     */               }
/*     */               else
/*     */               {
/* 205 */                 iblockstate4 = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE);
/*     */               }
/*     */               
/* 208 */               chunkPrimerIn.setBlockState(k1, i1, j1, iblockstate4);
/*     */             }
/*     */           }
/* 211 */           else if (l > 0)
/*     */           {
/* 213 */             l--;
/*     */             
/* 215 */             if (flag1)
/*     */             {
/* 217 */               chunkPrimerIn.setBlockState(k1, i1, j1, Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE));
/*     */             }
/*     */             else
/*     */             {
/* 221 */               IBlockState iblockstate2 = func_180629_a(p_180622_4_, i1, p_180622_5_);
/* 222 */               chunkPrimerIn.setBlockState(k1, i1, j1, iblockstate2);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void func_150619_a(long p_150619_1_)
/*     */   {
/* 232 */     this.field_150621_aC = new IBlockState[64];
/* 233 */     java.util.Arrays.fill(this.field_150621_aC, Blocks.hardened_clay.getDefaultState());
/* 234 */     Random random = new Random(p_150619_1_);
/* 235 */     this.field_150625_aG = new NoiseGeneratorPerlin(random, 1);
/*     */     
/* 237 */     for (int l1 = 0; l1 < 64; l1++)
/*     */     {
/* 239 */       l1 += random.nextInt(5) + 1;
/*     */       
/* 241 */       if (l1 < 64)
/*     */       {
/* 243 */         this.field_150621_aC[l1] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE);
/*     */       }
/*     */     }
/*     */     
/* 247 */     int i2 = random.nextInt(4) + 2;
/*     */     
/* 249 */     for (int i = 0; i < i2; i++)
/*     */     {
/* 251 */       int j = random.nextInt(3) + 1;
/* 252 */       int k = random.nextInt(64);
/*     */       
/* 254 */       for (int l = 0; (k + l < 64) && (l < j); l++)
/*     */       {
/* 256 */         this.field_150621_aC[(k + l)] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.YELLOW);
/*     */       }
/*     */     }
/*     */     
/* 260 */     int j2 = random.nextInt(4) + 2;
/*     */     
/* 262 */     for (int k2 = 0; k2 < j2; k2++)
/*     */     {
/* 264 */       int i3 = random.nextInt(3) + 2;
/* 265 */       int l3 = random.nextInt(64);
/*     */       
/* 267 */       for (int i1 = 0; (l3 + i1 < 64) && (i1 < i3); i1++)
/*     */       {
/* 269 */         this.field_150621_aC[(l3 + i1)] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.BROWN);
/*     */       }
/*     */     }
/*     */     
/* 273 */     int l2 = random.nextInt(4) + 2;
/*     */     
/* 275 */     for (int j3 = 0; j3 < l2; j3++)
/*     */     {
/* 277 */       int i4 = random.nextInt(3) + 1;
/* 278 */       int k4 = random.nextInt(64);
/*     */       
/* 280 */       for (int j1 = 0; (k4 + j1 < 64) && (j1 < i4); j1++)
/*     */       {
/* 282 */         this.field_150621_aC[(k4 + j1)] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.RED);
/*     */       }
/*     */     }
/*     */     
/* 286 */     int k3 = random.nextInt(3) + 3;
/* 287 */     int j4 = 0;
/*     */     
/* 289 */     for (int l4 = 0; l4 < k3; l4++)
/*     */     {
/* 291 */       int i5 = 1;
/* 292 */       j4 += random.nextInt(16) + 4;
/*     */       
/* 294 */       for (int k1 = 0; (j4 + k1 < 64) && (k1 < i5); k1++)
/*     */       {
/* 296 */         this.field_150621_aC[(j4 + k1)] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.WHITE);
/*     */         
/* 298 */         if ((j4 + k1 > 1) && (random.nextBoolean()))
/*     */         {
/* 300 */           this.field_150621_aC[(j4 + k1 - 1)] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.SILVER);
/*     */         }
/*     */         
/* 303 */         if ((j4 + k1 < 63) && (random.nextBoolean()))
/*     */         {
/* 305 */           this.field_150621_aC[(j4 + k1 + 1)] = Blocks.stained_hardened_clay.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.SILVER);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private IBlockState func_180629_a(int p_180629_1_, int p_180629_2_, int p_180629_3_)
/*     */   {
/* 313 */     int i = (int)Math.round(this.field_150625_aG.func_151601_a(p_180629_1_ * 1.0D / 512.0D, p_180629_1_ * 1.0D / 512.0D) * 2.0D);
/* 314 */     return this.field_150621_aC[((p_180629_2_ + i + 64) % 64)];
/*     */   }
/*     */   
/*     */   protected BiomeGenBase createMutatedBiome(int p_180277_1_)
/*     */   {
/* 319 */     boolean flag = this.biomeID == BiomeGenBase.mesa.biomeID;
/* 320 */     BiomeGenMesa biomegenmesa = new BiomeGenMesa(p_180277_1_, flag, this.field_150620_aI);
/*     */     
/* 322 */     if (!flag)
/*     */     {
/* 324 */       biomegenmesa.setHeight(height_LowHills);
/* 325 */       biomegenmesa.setBiomeName(this.biomeName + " M");
/*     */     }
/*     */     else
/*     */     {
/* 329 */       biomegenmesa.setBiomeName(this.biomeName + " (Bryce)");
/*     */     }
/*     */     
/* 332 */     biomegenmesa.func_150557_a(this.color, true);
/* 333 */     return biomegenmesa;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\biome\BiomeGenMesa.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */