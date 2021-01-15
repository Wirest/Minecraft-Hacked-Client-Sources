/*     */ package net.minecraft.world.biome;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockDirt;
/*     */ import net.minecraft.block.BlockDirt.DirtType;
/*     */ import net.minecraft.block.BlockTallGrass.EnumType;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.chunk.ChunkPrimer;
/*     */ import net.minecraft.world.gen.feature.WorldGenBlockBlob;
/*     */ import net.minecraft.world.gen.feature.WorldGenDoublePlant;
/*     */ import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
/*     */ import net.minecraft.world.gen.feature.WorldGenTaiga1;
/*     */ import net.minecraft.world.gen.feature.WorldGenTaiga2;
/*     */ import net.minecraft.world.gen.feature.WorldGenTallGrass;
/*     */ 
/*     */ public class BiomeGenTaiga extends BiomeGenBase
/*     */ {
/*  22 */   private static final WorldGenTaiga1 field_150639_aC = new WorldGenTaiga1();
/*  23 */   private static final WorldGenTaiga2 field_150640_aD = new WorldGenTaiga2(false);
/*  24 */   private static final WorldGenMegaPineTree field_150641_aE = new WorldGenMegaPineTree(false, false);
/*  25 */   private static final WorldGenMegaPineTree field_150642_aF = new WorldGenMegaPineTree(false, true);
/*  26 */   private static final WorldGenBlockBlob field_150643_aG = new WorldGenBlockBlob(Blocks.mossy_cobblestone, 0);
/*     */   private int field_150644_aH;
/*     */   
/*     */   public BiomeGenTaiga(int p_i45385_1_, int p_i45385_2_)
/*     */   {
/*  31 */     super(p_i45385_1_);
/*  32 */     this.field_150644_aH = p_i45385_2_;
/*  33 */     this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(net.minecraft.entity.passive.EntityWolf.class, 8, 4, 4));
/*  34 */     this.theBiomeDecorator.treesPerChunk = 10;
/*     */     
/*  36 */     if ((p_i45385_2_ != 1) && (p_i45385_2_ != 2))
/*     */     {
/*  38 */       this.theBiomeDecorator.grassPerChunk = 1;
/*  39 */       this.theBiomeDecorator.mushroomsPerChunk = 1;
/*     */     }
/*     */     else
/*     */     {
/*  43 */       this.theBiomeDecorator.grassPerChunk = 7;
/*  44 */       this.theBiomeDecorator.deadBushPerChunk = 1;
/*  45 */       this.theBiomeDecorator.mushroomsPerChunk = 3;
/*     */     }
/*     */   }
/*     */   
/*     */   public net.minecraft.world.gen.feature.WorldGenAbstractTree genBigTreeChance(Random rand)
/*     */   {
/*  51 */     return rand.nextInt(3) == 0 ? field_150639_aC : ((this.field_150644_aH == 1) || (this.field_150644_aH == 2)) && (rand.nextInt(3) == 0) ? field_150642_aF : (this.field_150644_aH != 2) && (rand.nextInt(13) != 0) ? field_150641_aE : field_150640_aD;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public net.minecraft.world.gen.feature.WorldGenerator getRandomWorldGenForGrass(Random rand)
/*     */   {
/*  59 */     return rand.nextInt(5) > 0 ? new WorldGenTallGrass(BlockTallGrass.EnumType.FERN) : new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
/*     */   }
/*     */   
/*     */   public void decorate(World worldIn, Random rand, BlockPos pos)
/*     */   {
/*  64 */     if ((this.field_150644_aH == 1) || (this.field_150644_aH == 2))
/*     */     {
/*  66 */       int i = rand.nextInt(3);
/*     */       
/*  68 */       for (int j = 0; j < i; j++)
/*     */       {
/*  70 */         int k = rand.nextInt(16) + 8;
/*  71 */         int l = rand.nextInt(16) + 8;
/*  72 */         BlockPos blockpos = worldIn.getHeight(pos.add(k, 0, l));
/*  73 */         field_150643_aG.generate(worldIn, rand, blockpos);
/*     */       }
/*     */     }
/*     */     
/*  77 */     DOUBLE_PLANT_GENERATOR.setPlantType(net.minecraft.block.BlockDoublePlant.EnumPlantType.FERN);
/*     */     
/*  79 */     for (int i1 = 0; i1 < 7; i1++)
/*     */     {
/*  81 */       int j1 = rand.nextInt(16) + 8;
/*  82 */       int k1 = rand.nextInt(16) + 8;
/*  83 */       int l1 = rand.nextInt(worldIn.getHeight(pos.add(j1, 0, k1)).getY() + 32);
/*  84 */       DOUBLE_PLANT_GENERATOR.generate(worldIn, rand, pos.add(j1, l1, k1));
/*     */     }
/*     */     
/*  87 */     super.decorate(worldIn, rand, pos);
/*     */   }
/*     */   
/*     */   public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int p_180622_4_, int p_180622_5_, double p_180622_6_)
/*     */   {
/*  92 */     if ((this.field_150644_aH == 1) || (this.field_150644_aH == 2))
/*     */     {
/*  94 */       this.topBlock = Blocks.grass.getDefaultState();
/*  95 */       this.fillerBlock = Blocks.dirt.getDefaultState();
/*     */       
/*  97 */       if (p_180622_6_ > 1.75D)
/*     */       {
/*  99 */         this.topBlock = Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.COARSE_DIRT);
/*     */       }
/* 101 */       else if (p_180622_6_ > -0.95D)
/*     */       {
/* 103 */         this.topBlock = Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.PODZOL);
/*     */       }
/*     */     }
/*     */     
/* 107 */     generateBiomeTerrain(worldIn, rand, chunkPrimerIn, p_180622_4_, p_180622_5_, p_180622_6_);
/*     */   }
/*     */   
/*     */   protected BiomeGenBase createMutatedBiome(int p_180277_1_)
/*     */   {
/* 112 */     return this.biomeID == BiomeGenBase.megaTaiga.biomeID ? new BiomeGenTaiga(p_180277_1_, 2).func_150557_a(5858897, true).setBiomeName("Mega Spruce Taiga").setFillerBlockMetadata(5159473).setTemperatureRainfall(0.25F, 0.8F).setHeight(new BiomeGenBase.Height(this.minHeight, this.maxHeight)) : super.createMutatedBiome(p_180277_1_);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\biome\BiomeGenTaiga.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */