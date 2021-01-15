/*     */ package net.minecraft.world.biome;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.BlockDoublePlant.EnumPlantType;
/*     */ import net.minecraft.block.BlockFlower.EnumFlowerType;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.gen.feature.WorldGenDoublePlant;
/*     */ 
/*     */ public class BiomeGenPlains extends BiomeGenBase
/*     */ {
/*     */   protected boolean field_150628_aC;
/*     */   
/*     */   protected BiomeGenPlains(int p_i1986_1_)
/*     */   {
/*  16 */     super(p_i1986_1_);
/*  17 */     setTemperatureRainfall(0.8F, 0.4F);
/*  18 */     setHeight(height_LowPlains);
/*  19 */     this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(net.minecraft.entity.passive.EntityHorse.class, 5, 2, 6));
/*  20 */     this.theBiomeDecorator.treesPerChunk = 64537;
/*  21 */     this.theBiomeDecorator.flowersPerChunk = 4;
/*  22 */     this.theBiomeDecorator.grassPerChunk = 10;
/*     */   }
/*     */   
/*     */   public BlockFlower.EnumFlowerType pickRandomFlower(Random rand, BlockPos pos)
/*     */   {
/*  27 */     double d0 = GRASS_COLOR_NOISE.func_151601_a(pos.getX() / 200.0D, pos.getZ() / 200.0D);
/*     */     
/*  29 */     if (d0 < -0.8D)
/*     */     {
/*  31 */       int j = rand.nextInt(4);
/*     */       
/*  33 */       switch (j)
/*     */       {
/*     */       case 0: 
/*  36 */         return BlockFlower.EnumFlowerType.ORANGE_TULIP;
/*     */       
/*     */       case 1: 
/*  39 */         return BlockFlower.EnumFlowerType.RED_TULIP;
/*     */       
/*     */       case 2: 
/*  42 */         return BlockFlower.EnumFlowerType.PINK_TULIP;
/*     */       }
/*     */       
/*     */       
/*  46 */       return BlockFlower.EnumFlowerType.WHITE_TULIP;
/*     */     }
/*     */     
/*  49 */     if (rand.nextInt(3) > 0)
/*     */     {
/*  51 */       int i = rand.nextInt(3);
/*  52 */       return i == 1 ? BlockFlower.EnumFlowerType.HOUSTONIA : i == 0 ? BlockFlower.EnumFlowerType.POPPY : BlockFlower.EnumFlowerType.OXEYE_DAISY;
/*     */     }
/*     */     
/*     */ 
/*  56 */     return BlockFlower.EnumFlowerType.DANDELION;
/*     */   }
/*     */   
/*     */ 
/*     */   public void decorate(World worldIn, Random rand, BlockPos pos)
/*     */   {
/*  62 */     double d0 = GRASS_COLOR_NOISE.func_151601_a((pos.getX() + 8) / 200.0D, (pos.getZ() + 8) / 200.0D);
/*     */     
/*  64 */     if (d0 < -0.8D)
/*     */     {
/*  66 */       this.theBiomeDecorator.flowersPerChunk = 15;
/*  67 */       this.theBiomeDecorator.grassPerChunk = 5;
/*     */     }
/*     */     else
/*     */     {
/*  71 */       this.theBiomeDecorator.flowersPerChunk = 4;
/*  72 */       this.theBiomeDecorator.grassPerChunk = 10;
/*  73 */       DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.GRASS);
/*     */       
/*  75 */       for (int i = 0; i < 7; i++)
/*     */       {
/*  77 */         int j = rand.nextInt(16) + 8;
/*  78 */         int k = rand.nextInt(16) + 8;
/*  79 */         int l = rand.nextInt(worldIn.getHeight(pos.add(j, 0, k)).getY() + 32);
/*  80 */         DOUBLE_PLANT_GENERATOR.generate(worldIn, rand, pos.add(j, l, k));
/*     */       }
/*     */     }
/*     */     
/*  84 */     if (this.field_150628_aC)
/*     */     {
/*  86 */       DOUBLE_PLANT_GENERATOR.setPlantType(BlockDoublePlant.EnumPlantType.SUNFLOWER);
/*     */       
/*  88 */       for (int i1 = 0; i1 < 10; i1++)
/*     */       {
/*  90 */         int j1 = rand.nextInt(16) + 8;
/*  91 */         int k1 = rand.nextInt(16) + 8;
/*  92 */         int l1 = rand.nextInt(worldIn.getHeight(pos.add(j1, 0, k1)).getY() + 32);
/*  93 */         DOUBLE_PLANT_GENERATOR.generate(worldIn, rand, pos.add(j1, l1, k1));
/*     */       }
/*     */     }
/*     */     
/*  97 */     super.decorate(worldIn, rand, pos);
/*     */   }
/*     */   
/*     */   protected BiomeGenBase createMutatedBiome(int p_180277_1_)
/*     */   {
/* 102 */     BiomeGenPlains biomegenplains = new BiomeGenPlains(p_180277_1_);
/* 103 */     biomegenplains.setBiomeName("Sunflower Plains");
/* 104 */     biomegenplains.field_150628_aC = true;
/* 105 */     biomegenplains.setColor(9286496);
/* 106 */     biomegenplains.field_150609_ah = 14273354;
/* 107 */     return biomegenplains;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\biome\BiomeGenPlains.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */