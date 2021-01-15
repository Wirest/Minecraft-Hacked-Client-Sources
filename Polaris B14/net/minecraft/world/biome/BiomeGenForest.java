/*     */ package net.minecraft.world.biome;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.BlockDoublePlant.EnumPlantType;
/*     */ import net.minecraft.block.BlockFlower.EnumFlowerType;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.gen.feature.WorldGenAbstractTree;
/*     */ import net.minecraft.world.gen.feature.WorldGenBigMushroom;
/*     */ import net.minecraft.world.gen.feature.WorldGenCanopyTree;
/*     */ import net.minecraft.world.gen.feature.WorldGenDoublePlant;
/*     */ import net.minecraft.world.gen.feature.WorldGenForest;
/*     */ 
/*     */ public class BiomeGenForest extends BiomeGenBase
/*     */ {
/*     */   private int field_150632_aF;
/*  18 */   protected static final WorldGenForest field_150629_aC = new WorldGenForest(false, true);
/*  19 */   protected static final WorldGenForest field_150630_aD = new WorldGenForest(false, false);
/*  20 */   protected static final WorldGenCanopyTree field_150631_aE = new WorldGenCanopyTree(false);
/*     */   
/*     */   public BiomeGenForest(int p_i45377_1_, int p_i45377_2_)
/*     */   {
/*  24 */     super(p_i45377_1_);
/*  25 */     this.field_150632_aF = p_i45377_2_;
/*  26 */     this.theBiomeDecorator.treesPerChunk = 10;
/*  27 */     this.theBiomeDecorator.grassPerChunk = 2;
/*     */     
/*  29 */     if (this.field_150632_aF == 1)
/*     */     {
/*  31 */       this.theBiomeDecorator.treesPerChunk = 6;
/*  32 */       this.theBiomeDecorator.flowersPerChunk = 100;
/*  33 */       this.theBiomeDecorator.grassPerChunk = 1;
/*     */     }
/*     */     
/*  36 */     setFillerBlockMetadata(5159473);
/*  37 */     setTemperatureRainfall(0.7F, 0.8F);
/*     */     
/*  39 */     if (this.field_150632_aF == 2)
/*     */     {
/*  41 */       this.field_150609_ah = 353825;
/*  42 */       this.color = 3175492;
/*  43 */       setTemperatureRainfall(0.6F, 0.6F);
/*     */     }
/*     */     
/*  46 */     if (this.field_150632_aF == 0)
/*     */     {
/*  48 */       this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(net.minecraft.entity.passive.EntityWolf.class, 5, 4, 4));
/*     */     }
/*     */     
/*  51 */     if (this.field_150632_aF == 3)
/*     */     {
/*  53 */       this.theBiomeDecorator.treesPerChunk = 64537;
/*     */     }
/*     */   }
/*     */   
/*     */   protected BiomeGenBase func_150557_a(int p_150557_1_, boolean p_150557_2_)
/*     */   {
/*  59 */     if (this.field_150632_aF == 2)
/*     */     {
/*  61 */       this.field_150609_ah = 353825;
/*  62 */       this.color = p_150557_1_;
/*     */       
/*  64 */       if (p_150557_2_)
/*     */       {
/*  66 */         this.field_150609_ah = ((this.field_150609_ah & 0xFEFEFE) >> 1);
/*     */       }
/*     */       
/*  69 */       return this;
/*     */     }
/*     */     
/*     */ 
/*  73 */     return super.func_150557_a(p_150557_1_, p_150557_2_);
/*     */   }
/*     */   
/*     */ 
/*     */   public WorldGenAbstractTree genBigTreeChance(Random rand)
/*     */   {
/*  79 */     return (this.field_150632_aF != 2) && (rand.nextInt(5) != 0) ? this.worldGeneratorTrees : (this.field_150632_aF == 3) && (rand.nextInt(3) > 0) ? field_150631_aE : field_150630_aD;
/*     */   }
/*     */   
/*     */   public BlockFlower.EnumFlowerType pickRandomFlower(Random rand, BlockPos pos)
/*     */   {
/*  84 */     if (this.field_150632_aF == 1)
/*     */     {
/*  86 */       double d0 = MathHelper.clamp_double((1.0D + GRASS_COLOR_NOISE.func_151601_a(pos.getX() / 48.0D, pos.getZ() / 48.0D)) / 2.0D, 0.0D, 0.9999D);
/*  87 */       BlockFlower.EnumFlowerType blockflower$enumflowertype = BlockFlower.EnumFlowerType.values()[((int)(d0 * BlockFlower.EnumFlowerType.values().length))];
/*  88 */       return blockflower$enumflowertype == BlockFlower.EnumFlowerType.BLUE_ORCHID ? BlockFlower.EnumFlowerType.POPPY : blockflower$enumflowertype;
/*     */     }
/*     */     
/*     */ 
/*  92 */     return super.pickRandomFlower(rand, pos);
/*     */   }
/*     */   
/*     */ 
/*     */   public void decorate(World worldIn, Random rand, BlockPos pos)
/*     */   {
/*  98 */     if (this.field_150632_aF == 3)
/*     */     {
/* 100 */       for (int i = 0; i < 4; i++)
/*     */       {
/* 102 */         for (int j = 0; j < 4; j++)
/*     */         {
/* 104 */           int k = i * 4 + 1 + 8 + rand.nextInt(3);
/* 105 */           int l = j * 4 + 1 + 8 + rand.nextInt(3);
/* 106 */           BlockPos blockpos = worldIn.getHeight(pos.add(k, 0, l));
/*     */           
/* 108 */           if (rand.nextInt(20) == 0)
/*     */           {
/* 110 */             WorldGenBigMushroom worldgenbigmushroom = new WorldGenBigMushroom();
/* 111 */             worldgenbigmushroom.generate(worldIn, rand, blockpos);
/*     */           }
/*     */           else
/*     */           {
/* 115 */             WorldGenAbstractTree worldgenabstracttree = genBigTreeChance(rand);
/* 116 */             worldgenabstracttree.func_175904_e();
/*     */             
/* 118 */             if (worldgenabstracttree.generate(worldIn, rand, blockpos))
/*     */             {
/* 120 */               worldgenabstracttree.func_180711_a(worldIn, rand, blockpos);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 127 */     int j1 = rand.nextInt(5) - 3;
/*     */     
/* 129 */     if (this.field_150632_aF == 1)
/*     */     {
/* 131 */       j1 += 2;
/*     */     }
/*     */     
/* 134 */     for (int k1 = 0; k1 < j1; k1++)
/*     */     {
/* 136 */       int l1 = rand.nextInt(3);
/*     */       
/* 138 */       if (l1 == 0)
/*     */       {
/* 140 */         DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.SYRINGA);
/*     */       }
/* 142 */       else if (l1 == 1)
/*     */       {
/* 144 */         DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.ROSE);
/*     */       }
/* 146 */       else if (l1 == 2)
/*     */       {
/* 148 */         DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.PAEONIA);
/*     */       }
/*     */       
/* 151 */       for (int i2 = 0; i2 < 5; i2++)
/*     */       {
/* 153 */         int j2 = rand.nextInt(16) + 8;
/* 154 */         int k2 = rand.nextInt(16) + 8;
/* 155 */         int i1 = rand.nextInt(worldIn.getHeight(pos.add(j2, 0, k2)).getY() + 32);
/*     */         
/* 157 */         if (DOUBLE_PLANT_GENERATOR.generate(worldIn, rand, new BlockPos(pos.getX() + j2, i1, pos.getZ() + k2))) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 164 */     super.decorate(worldIn, rand, pos);
/*     */   }
/*     */   
/*     */   public int getGrassColorAtPos(BlockPos pos)
/*     */   {
/* 169 */     int i = super.getGrassColorAtPos(pos);
/* 170 */     return this.field_150632_aF == 3 ? (i & 0xFEFEFE) + 2634762 >> 1 : i;
/*     */   }
/*     */   
/*     */   protected BiomeGenBase createMutatedBiome(int p_180277_1_)
/*     */   {
/* 175 */     if (this.biomeID == BiomeGenBase.forest.biomeID)
/*     */     {
/* 177 */       BiomeGenForest biomegenforest = new BiomeGenForest(p_180277_1_, 1);
/* 178 */       biomegenforest.setHeight(new BiomeGenBase.Height(this.minHeight, this.maxHeight + 0.2F));
/* 179 */       biomegenforest.setBiomeName("Flower Forest");
/* 180 */       biomegenforest.func_150557_a(6976549, true);
/* 181 */       biomegenforest.setFillerBlockMetadata(8233509);
/* 182 */       return biomegenforest;
/*     */     }
/*     */     
/*     */ 
/* 186 */     (this.biomeID != BiomeGenBase.birchForest.biomeID) && (this.biomeID != BiomeGenBase.birchForestHills.biomeID) ? new BiomeGenMutated(p_180277_1_, this)
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 192 */       new BiomeGenMutated
/*     */       {
/*     */         public void decorate(World worldIn, Random rand, BlockPos pos)
/*     */         {
/* 190 */           this.baseBiome.decorate(worldIn, rand, pos);
/*     */         }
/* 192 */       } : new BiomeGenMutated(p_180277_1_, this)
/*     */       {
/*     */         public WorldGenAbstractTree genBigTreeChance(Random rand)
/*     */         {
/* 196 */           return rand.nextBoolean() ? BiomeGenForest.field_150629_aC : BiomeGenForest.field_150630_aD;
/*     */         }
/*     */       };
/*     */     }
/*     */   }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\biome\BiomeGenForest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */