/*     */ package net.minecraft.world.biome;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockFlower;
/*     */ import net.minecraft.block.BlockFlower.EnumFlowerType;
/*     */ import net.minecraft.block.BlockStone;
/*     */ import net.minecraft.block.BlockStone.EnumType;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.gen.ChunkProviderSettings;
/*     */ import net.minecraft.world.gen.ChunkProviderSettings.Factory;
/*     */ import net.minecraft.world.gen.GeneratorBushFeature;
/*     */ import net.minecraft.world.gen.feature.WorldGenAbstractTree;
/*     */ import net.minecraft.world.gen.feature.WorldGenBigMushroom;
/*     */ import net.minecraft.world.gen.feature.WorldGenCactus;
/*     */ import net.minecraft.world.gen.feature.WorldGenClay;
/*     */ import net.minecraft.world.gen.feature.WorldGenDeadBush;
/*     */ import net.minecraft.world.gen.feature.WorldGenFlowers;
/*     */ import net.minecraft.world.gen.feature.WorldGenLiquids;
/*     */ import net.minecraft.world.gen.feature.WorldGenMinable;
/*     */ import net.minecraft.world.gen.feature.WorldGenPumpkin;
/*     */ import net.minecraft.world.gen.feature.WorldGenReed;
/*     */ import net.minecraft.world.gen.feature.WorldGenSand;
/*     */ import net.minecraft.world.gen.feature.WorldGenerator;
/*     */ import net.minecraft.world.storage.WorldInfo;
/*     */ 
/*     */ public class BiomeDecorator
/*     */ {
/*     */   protected World currentWorld;
/*     */   protected Random randomGenerator;
/*     */   protected BlockPos field_180294_c;
/*     */   protected ChunkProviderSettings chunkProviderSettings;
/*  37 */   protected WorldGenerator clayGen = new WorldGenClay(4);
/*     */   
/*     */ 
/*  40 */   protected WorldGenerator sandGen = new WorldGenSand(Blocks.sand, 7);
/*     */   
/*     */ 
/*  43 */   protected WorldGenerator gravelAsSandGen = new WorldGenSand(Blocks.gravel, 6);
/*     */   
/*     */   protected WorldGenerator dirtGen;
/*     */   
/*     */   protected WorldGenerator gravelGen;
/*     */   
/*     */   protected WorldGenerator graniteGen;
/*     */   
/*     */   protected WorldGenerator dioriteGen;
/*     */   
/*     */   protected WorldGenerator andesiteGen;
/*     */   
/*     */   protected WorldGenerator coalGen;
/*     */   protected WorldGenerator ironGen;
/*     */   protected WorldGenerator goldGen;
/*     */   protected WorldGenerator redstoneGen;
/*     */   protected WorldGenerator diamondGen;
/*     */   protected WorldGenerator lapisGen;
/*  61 */   protected WorldGenFlowers yellowFlowerGen = new WorldGenFlowers(Blocks.yellow_flower, BlockFlower.EnumFlowerType.DANDELION);
/*     */   
/*     */ 
/*  64 */   protected WorldGenerator mushroomBrownGen = new GeneratorBushFeature(Blocks.brown_mushroom);
/*     */   
/*     */ 
/*  67 */   protected WorldGenerator mushroomRedGen = new GeneratorBushFeature(Blocks.red_mushroom);
/*     */   
/*     */ 
/*  70 */   protected WorldGenerator bigMushroomGen = new WorldGenBigMushroom();
/*     */   
/*     */ 
/*  73 */   protected WorldGenerator reedGen = new WorldGenReed();
/*     */   
/*     */ 
/*  76 */   protected WorldGenerator cactusGen = new WorldGenCactus();
/*     */   
/*     */ 
/*  79 */   protected WorldGenerator waterlilyGen = new net.minecraft.world.gen.feature.WorldGenWaterlily();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int waterlilyPerChunk;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int treesPerChunk;
/*     */   
/*     */ 
/*     */ 
/*  93 */   protected int flowersPerChunk = 2;
/*     */   
/*     */ 
/*  96 */   protected int grassPerChunk = 1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int deadBushPerChunk;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int mushroomsPerChunk;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int reedsPerChunk;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int cactiPerChunk;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 122 */   protected int sandPerChunk = 1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 128 */   protected int sandPerChunk2 = 3;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 133 */   protected int clayPerChunk = 1;
/*     */   
/*     */ 
/*     */   protected int bigMushroomsPerChunk;
/*     */   
/*     */ 
/* 139 */   public boolean generateLakes = true;
/*     */   
/*     */   public void decorate(World worldIn, Random random, BiomeGenBase p_180292_3_, BlockPos p_180292_4_)
/*     */   {
/* 143 */     if (this.currentWorld != null)
/*     */     {
/* 145 */       throw new RuntimeException("Already decorating");
/*     */     }
/*     */     
/*     */ 
/* 149 */     this.currentWorld = worldIn;
/* 150 */     String s = worldIn.getWorldInfo().getGeneratorOptions();
/*     */     
/* 152 */     if (s != null)
/*     */     {
/* 154 */       this.chunkProviderSettings = ChunkProviderSettings.Factory.jsonToFactory(s).func_177864_b();
/*     */     }
/*     */     else
/*     */     {
/* 158 */       this.chunkProviderSettings = ChunkProviderSettings.Factory.jsonToFactory("").func_177864_b();
/*     */     }
/*     */     
/* 161 */     this.randomGenerator = random;
/* 162 */     this.field_180294_c = p_180292_4_;
/* 163 */     this.dirtGen = new WorldGenMinable(Blocks.dirt.getDefaultState(), this.chunkProviderSettings.dirtSize);
/* 164 */     this.gravelGen = new WorldGenMinable(Blocks.gravel.getDefaultState(), this.chunkProviderSettings.gravelSize);
/* 165 */     this.graniteGen = new WorldGenMinable(Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), this.chunkProviderSettings.graniteSize);
/* 166 */     this.dioriteGen = new WorldGenMinable(Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE), this.chunkProviderSettings.dioriteSize);
/* 167 */     this.andesiteGen = new WorldGenMinable(Blocks.stone.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE), this.chunkProviderSettings.andesiteSize);
/* 168 */     this.coalGen = new WorldGenMinable(Blocks.coal_ore.getDefaultState(), this.chunkProviderSettings.coalSize);
/* 169 */     this.ironGen = new WorldGenMinable(Blocks.iron_ore.getDefaultState(), this.chunkProviderSettings.ironSize);
/* 170 */     this.goldGen = new WorldGenMinable(Blocks.gold_ore.getDefaultState(), this.chunkProviderSettings.goldSize);
/* 171 */     this.redstoneGen = new WorldGenMinable(Blocks.redstone_ore.getDefaultState(), this.chunkProviderSettings.redstoneSize);
/* 172 */     this.diamondGen = new WorldGenMinable(Blocks.diamond_ore.getDefaultState(), this.chunkProviderSettings.diamondSize);
/* 173 */     this.lapisGen = new WorldGenMinable(Blocks.lapis_ore.getDefaultState(), this.chunkProviderSettings.lapisSize);
/* 174 */     genDecorations(p_180292_3_);
/* 175 */     this.currentWorld = null;
/* 176 */     this.randomGenerator = null;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void genDecorations(BiomeGenBase biomeGenBaseIn)
/*     */   {
/* 182 */     generateOres();
/*     */     
/* 184 */     for (int i = 0; i < this.sandPerChunk2; i++)
/*     */     {
/* 186 */       int j = this.randomGenerator.nextInt(16) + 8;
/* 187 */       int k = this.randomGenerator.nextInt(16) + 8;
/* 188 */       this.sandGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getTopSolidOrLiquidBlock(this.field_180294_c.add(j, 0, k)));
/*     */     }
/*     */     
/* 191 */     for (int i1 = 0; i1 < this.clayPerChunk; i1++)
/*     */     {
/* 193 */       int l1 = this.randomGenerator.nextInt(16) + 8;
/* 194 */       int i6 = this.randomGenerator.nextInt(16) + 8;
/* 195 */       this.clayGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getTopSolidOrLiquidBlock(this.field_180294_c.add(l1, 0, i6)));
/*     */     }
/*     */     
/* 198 */     for (int j1 = 0; j1 < this.sandPerChunk; j1++)
/*     */     {
/* 200 */       int i2 = this.randomGenerator.nextInt(16) + 8;
/* 201 */       int j6 = this.randomGenerator.nextInt(16) + 8;
/* 202 */       this.gravelAsSandGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getTopSolidOrLiquidBlock(this.field_180294_c.add(i2, 0, j6)));
/*     */     }
/*     */     
/* 205 */     int k1 = this.treesPerChunk;
/*     */     
/* 207 */     if (this.randomGenerator.nextInt(10) == 0)
/*     */     {
/* 209 */       k1++;
/*     */     }
/*     */     
/* 212 */     for (int j2 = 0; j2 < k1; j2++)
/*     */     {
/* 214 */       int k6 = this.randomGenerator.nextInt(16) + 8;
/* 215 */       int l = this.randomGenerator.nextInt(16) + 8;
/* 216 */       WorldGenAbstractTree worldgenabstracttree = biomeGenBaseIn.genBigTreeChance(this.randomGenerator);
/* 217 */       worldgenabstracttree.func_175904_e();
/* 218 */       BlockPos blockpos = this.currentWorld.getHeight(this.field_180294_c.add(k6, 0, l));
/*     */       
/* 220 */       if (worldgenabstracttree.generate(this.currentWorld, this.randomGenerator, blockpos))
/*     */       {
/* 222 */         worldgenabstracttree.func_180711_a(this.currentWorld, this.randomGenerator, blockpos);
/*     */       }
/*     */     }
/*     */     
/* 226 */     for (int k2 = 0; k2 < this.bigMushroomsPerChunk; k2++)
/*     */     {
/* 228 */       int l6 = this.randomGenerator.nextInt(16) + 8;
/* 229 */       int k10 = this.randomGenerator.nextInt(16) + 8;
/* 230 */       this.bigMushroomGen.generate(this.currentWorld, this.randomGenerator, this.currentWorld.getHeight(this.field_180294_c.add(l6, 0, k10)));
/*     */     }
/*     */     
/* 233 */     for (int l2 = 0; l2 < this.flowersPerChunk; l2++)
/*     */     {
/* 235 */       int i7 = this.randomGenerator.nextInt(16) + 8;
/* 236 */       int l10 = this.randomGenerator.nextInt(16) + 8;
/* 237 */       int j14 = this.currentWorld.getHeight(this.field_180294_c.add(i7, 0, l10)).getY() + 32;
/*     */       
/* 239 */       if (j14 > 0)
/*     */       {
/* 241 */         int k17 = this.randomGenerator.nextInt(j14);
/* 242 */         BlockPos blockpos1 = this.field_180294_c.add(i7, k17, l10);
/* 243 */         BlockFlower.EnumFlowerType blockflower$enumflowertype = biomeGenBaseIn.pickRandomFlower(this.randomGenerator, blockpos1);
/* 244 */         BlockFlower blockflower = blockflower$enumflowertype.getBlockType().getBlock();
/*     */         
/* 246 */         if (blockflower.getMaterial() != Material.air)
/*     */         {
/* 248 */           this.yellowFlowerGen.setGeneratedBlock(blockflower, blockflower$enumflowertype);
/* 249 */           this.yellowFlowerGen.generate(this.currentWorld, this.randomGenerator, blockpos1);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 254 */     for (int i3 = 0; i3 < this.grassPerChunk; i3++)
/*     */     {
/* 256 */       int j7 = this.randomGenerator.nextInt(16) + 8;
/* 257 */       int i11 = this.randomGenerator.nextInt(16) + 8;
/* 258 */       int k14 = this.currentWorld.getHeight(this.field_180294_c.add(j7, 0, i11)).getY() * 2;
/*     */       
/* 260 */       if (k14 > 0)
/*     */       {
/* 262 */         int l17 = this.randomGenerator.nextInt(k14);
/* 263 */         biomeGenBaseIn.getRandomWorldGenForGrass(this.randomGenerator).generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(j7, l17, i11));
/*     */       }
/*     */     }
/*     */     
/* 267 */     for (int j3 = 0; j3 < this.deadBushPerChunk; j3++)
/*     */     {
/* 269 */       int k7 = this.randomGenerator.nextInt(16) + 8;
/* 270 */       int j11 = this.randomGenerator.nextInt(16) + 8;
/* 271 */       int l14 = this.currentWorld.getHeight(this.field_180294_c.add(k7, 0, j11)).getY() * 2;
/*     */       
/* 273 */       if (l14 > 0)
/*     */       {
/* 275 */         int i18 = this.randomGenerator.nextInt(l14);
/* 276 */         new WorldGenDeadBush().generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(k7, i18, j11));
/*     */       }
/*     */     }
/*     */     
/* 280 */     for (int k3 = 0; k3 < this.waterlilyPerChunk; k3++)
/*     */     {
/* 282 */       int l7 = this.randomGenerator.nextInt(16) + 8;
/* 283 */       int k11 = this.randomGenerator.nextInt(16) + 8;
/* 284 */       int i15 = this.currentWorld.getHeight(this.field_180294_c.add(l7, 0, k11)).getY() * 2;
/*     */       
/* 286 */       if (i15 > 0)
/*     */       {
/* 288 */         int j18 = this.randomGenerator.nextInt(i15);
/*     */         
/*     */         BlockPos blockpos7;
/*     */         
/* 292 */         for (BlockPos blockpos4 = this.field_180294_c.add(l7, j18, k11); blockpos4.getY() > 0; blockpos4 = blockpos7)
/*     */         {
/* 294 */           blockpos7 = blockpos4.down();
/*     */           
/* 296 */           if (!this.currentWorld.isAirBlock(blockpos7)) {
/*     */             break;
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 302 */         this.waterlilyGen.generate(this.currentWorld, this.randomGenerator, blockpos4);
/*     */       }
/*     */     }
/*     */     
/* 306 */     for (int l3 = 0; l3 < this.mushroomsPerChunk; l3++)
/*     */     {
/* 308 */       if (this.randomGenerator.nextInt(4) == 0)
/*     */       {
/* 310 */         int i8 = this.randomGenerator.nextInt(16) + 8;
/* 311 */         int l11 = this.randomGenerator.nextInt(16) + 8;
/* 312 */         BlockPos blockpos2 = this.currentWorld.getHeight(this.field_180294_c.add(i8, 0, l11));
/* 313 */         this.mushroomBrownGen.generate(this.currentWorld, this.randomGenerator, blockpos2);
/*     */       }
/*     */       
/* 316 */       if (this.randomGenerator.nextInt(8) == 0)
/*     */       {
/* 318 */         int j8 = this.randomGenerator.nextInt(16) + 8;
/* 319 */         int i12 = this.randomGenerator.nextInt(16) + 8;
/* 320 */         int j15 = this.currentWorld.getHeight(this.field_180294_c.add(j8, 0, i12)).getY() * 2;
/*     */         
/* 322 */         if (j15 > 0)
/*     */         {
/* 324 */           int k18 = this.randomGenerator.nextInt(j15);
/* 325 */           BlockPos blockpos5 = this.field_180294_c.add(j8, k18, i12);
/* 326 */           this.mushroomRedGen.generate(this.currentWorld, this.randomGenerator, blockpos5);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 331 */     if (this.randomGenerator.nextInt(4) == 0)
/*     */     {
/* 333 */       int i4 = this.randomGenerator.nextInt(16) + 8;
/* 334 */       int k8 = this.randomGenerator.nextInt(16) + 8;
/* 335 */       int j12 = this.currentWorld.getHeight(this.field_180294_c.add(i4, 0, k8)).getY() * 2;
/*     */       
/* 337 */       if (j12 > 0)
/*     */       {
/* 339 */         int k15 = this.randomGenerator.nextInt(j12);
/* 340 */         this.mushroomBrownGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(i4, k15, k8));
/*     */       }
/*     */     }
/*     */     
/* 344 */     if (this.randomGenerator.nextInt(8) == 0)
/*     */     {
/* 346 */       int j4 = this.randomGenerator.nextInt(16) + 8;
/* 347 */       int l8 = this.randomGenerator.nextInt(16) + 8;
/* 348 */       int k12 = this.currentWorld.getHeight(this.field_180294_c.add(j4, 0, l8)).getY() * 2;
/*     */       
/* 350 */       if (k12 > 0)
/*     */       {
/* 352 */         int l15 = this.randomGenerator.nextInt(k12);
/* 353 */         this.mushroomRedGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(j4, l15, l8));
/*     */       }
/*     */     }
/*     */     
/* 357 */     for (int k4 = 0; k4 < this.reedsPerChunk; k4++)
/*     */     {
/* 359 */       int i9 = this.randomGenerator.nextInt(16) + 8;
/* 360 */       int l12 = this.randomGenerator.nextInt(16) + 8;
/* 361 */       int i16 = this.currentWorld.getHeight(this.field_180294_c.add(i9, 0, l12)).getY() * 2;
/*     */       
/* 363 */       if (i16 > 0)
/*     */       {
/* 365 */         int l18 = this.randomGenerator.nextInt(i16);
/* 366 */         this.reedGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(i9, l18, l12));
/*     */       }
/*     */     }
/*     */     
/* 370 */     for (int l4 = 0; l4 < 10; l4++)
/*     */     {
/* 372 */       int j9 = this.randomGenerator.nextInt(16) + 8;
/* 373 */       int i13 = this.randomGenerator.nextInt(16) + 8;
/* 374 */       int j16 = this.currentWorld.getHeight(this.field_180294_c.add(j9, 0, i13)).getY() * 2;
/*     */       
/* 376 */       if (j16 > 0)
/*     */       {
/* 378 */         int i19 = this.randomGenerator.nextInt(j16);
/* 379 */         this.reedGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(j9, i19, i13));
/*     */       }
/*     */     }
/*     */     
/* 383 */     if (this.randomGenerator.nextInt(32) == 0)
/*     */     {
/* 385 */       int i5 = this.randomGenerator.nextInt(16) + 8;
/* 386 */       int k9 = this.randomGenerator.nextInt(16) + 8;
/* 387 */       int j13 = this.currentWorld.getHeight(this.field_180294_c.add(i5, 0, k9)).getY() * 2;
/*     */       
/* 389 */       if (j13 > 0)
/*     */       {
/* 391 */         int k16 = this.randomGenerator.nextInt(j13);
/* 392 */         new WorldGenPumpkin().generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(i5, k16, k9));
/*     */       }
/*     */     }
/*     */     
/* 396 */     for (int j5 = 0; j5 < this.cactiPerChunk; j5++)
/*     */     {
/* 398 */       int l9 = this.randomGenerator.nextInt(16) + 8;
/* 399 */       int k13 = this.randomGenerator.nextInt(16) + 8;
/* 400 */       int l16 = this.currentWorld.getHeight(this.field_180294_c.add(l9, 0, k13)).getY() * 2;
/*     */       
/* 402 */       if (l16 > 0)
/*     */       {
/* 404 */         int j19 = this.randomGenerator.nextInt(l16);
/* 405 */         this.cactusGen.generate(this.currentWorld, this.randomGenerator, this.field_180294_c.add(l9, j19, k13));
/*     */       }
/*     */     }
/*     */     
/* 409 */     if (this.generateLakes)
/*     */     {
/* 411 */       for (int k5 = 0; k5 < 50; k5++)
/*     */       {
/* 413 */         int i10 = this.randomGenerator.nextInt(16) + 8;
/* 414 */         int l13 = this.randomGenerator.nextInt(16) + 8;
/* 415 */         int i17 = this.randomGenerator.nextInt(248) + 8;
/*     */         
/* 417 */         if (i17 > 0)
/*     */         {
/* 419 */           int k19 = this.randomGenerator.nextInt(i17);
/* 420 */           BlockPos blockpos6 = this.field_180294_c.add(i10, k19, l13);
/* 421 */           new WorldGenLiquids(Blocks.flowing_water).generate(this.currentWorld, this.randomGenerator, blockpos6);
/*     */         }
/*     */       }
/*     */       
/* 425 */       for (int l5 = 0; l5 < 20; l5++)
/*     */       {
/* 427 */         int j10 = this.randomGenerator.nextInt(16) + 8;
/* 428 */         int i14 = this.randomGenerator.nextInt(16) + 8;
/* 429 */         int j17 = this.randomGenerator.nextInt(this.randomGenerator.nextInt(this.randomGenerator.nextInt(240) + 8) + 8);
/* 430 */         BlockPos blockpos3 = this.field_180294_c.add(j10, j17, i14);
/* 431 */         new WorldGenLiquids(Blocks.flowing_lava).generate(this.currentWorld, this.randomGenerator, blockpos3);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void genStandardOre1(int blockCount, WorldGenerator generator, int minHeight, int maxHeight)
/*     */   {
/* 441 */     if (maxHeight < minHeight)
/*     */     {
/* 443 */       int i = minHeight;
/* 444 */       minHeight = maxHeight;
/* 445 */       maxHeight = i;
/*     */     }
/* 447 */     else if (maxHeight == minHeight)
/*     */     {
/* 449 */       if (minHeight < 255)
/*     */       {
/* 451 */         maxHeight++;
/*     */       }
/*     */       else
/*     */       {
/* 455 */         minHeight--;
/*     */       }
/*     */     }
/*     */     
/* 459 */     for (int j = 0; j < blockCount; j++)
/*     */     {
/* 461 */       BlockPos blockpos = this.field_180294_c.add(this.randomGenerator.nextInt(16), this.randomGenerator.nextInt(maxHeight - minHeight) + minHeight, this.randomGenerator.nextInt(16));
/* 462 */       generator.generate(this.currentWorld, this.randomGenerator, blockpos);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void genStandardOre2(int blockCount, WorldGenerator generator, int centerHeight, int spread)
/*     */   {
/* 471 */     for (int i = 0; i < blockCount; i++)
/*     */     {
/* 473 */       BlockPos blockpos = this.field_180294_c.add(this.randomGenerator.nextInt(16), this.randomGenerator.nextInt(spread) + this.randomGenerator.nextInt(spread) + centerHeight - spread, this.randomGenerator.nextInt(16));
/* 474 */       generator.generate(this.currentWorld, this.randomGenerator, blockpos);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void generateOres()
/*     */   {
/* 483 */     genStandardOre1(this.chunkProviderSettings.dirtCount, this.dirtGen, this.chunkProviderSettings.dirtMinHeight, this.chunkProviderSettings.dirtMaxHeight);
/* 484 */     genStandardOre1(this.chunkProviderSettings.gravelCount, this.gravelGen, this.chunkProviderSettings.gravelMinHeight, this.chunkProviderSettings.gravelMaxHeight);
/* 485 */     genStandardOre1(this.chunkProviderSettings.dioriteCount, this.dioriteGen, this.chunkProviderSettings.dioriteMinHeight, this.chunkProviderSettings.dioriteMaxHeight);
/* 486 */     genStandardOre1(this.chunkProviderSettings.graniteCount, this.graniteGen, this.chunkProviderSettings.graniteMinHeight, this.chunkProviderSettings.graniteMaxHeight);
/* 487 */     genStandardOre1(this.chunkProviderSettings.andesiteCount, this.andesiteGen, this.chunkProviderSettings.andesiteMinHeight, this.chunkProviderSettings.andesiteMaxHeight);
/* 488 */     genStandardOre1(this.chunkProviderSettings.coalCount, this.coalGen, this.chunkProviderSettings.coalMinHeight, this.chunkProviderSettings.coalMaxHeight);
/* 489 */     genStandardOre1(this.chunkProviderSettings.ironCount, this.ironGen, this.chunkProviderSettings.ironMinHeight, this.chunkProviderSettings.ironMaxHeight);
/* 490 */     genStandardOre1(this.chunkProviderSettings.goldCount, this.goldGen, this.chunkProviderSettings.goldMinHeight, this.chunkProviderSettings.goldMaxHeight);
/* 491 */     genStandardOre1(this.chunkProviderSettings.redstoneCount, this.redstoneGen, this.chunkProviderSettings.redstoneMinHeight, this.chunkProviderSettings.redstoneMaxHeight);
/* 492 */     genStandardOre1(this.chunkProviderSettings.diamondCount, this.diamondGen, this.chunkProviderSettings.diamondMinHeight, this.chunkProviderSettings.diamondMaxHeight);
/* 493 */     genStandardOre2(this.chunkProviderSettings.lapisCount, this.lapisGen, this.chunkProviderSettings.lapisCenterHeight, this.chunkProviderSettings.lapisSpread);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\biome\BiomeDecorator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */