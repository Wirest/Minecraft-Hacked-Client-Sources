/*     */ package net.minecraft.world.biome;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.chunk.ChunkPrimer;
/*     */ import net.minecraft.world.gen.feature.WorldGenAbstractTree;
/*     */ import net.minecraft.world.gen.feature.WorldGenTaiga2;
/*     */ import net.minecraft.world.gen.feature.WorldGenerator;
/*     */ 
/*     */ public class BiomeGenHills extends BiomeGenBase
/*     */ {
/*  16 */   private WorldGenerator theWorldGenerator = new net.minecraft.world.gen.feature.WorldGenMinable(Blocks.monster_egg.getDefaultState().withProperty(net.minecraft.block.BlockSilverfish.VARIANT, net.minecraft.block.BlockSilverfish.EnumType.STONE), 9);
/*  17 */   private WorldGenTaiga2 field_150634_aD = new WorldGenTaiga2(false);
/*  18 */   private int field_150635_aE = 0;
/*  19 */   private int field_150636_aF = 1;
/*  20 */   private int field_150637_aG = 2;
/*     */   private int field_150638_aH;
/*     */   
/*     */   protected BiomeGenHills(int p_i45373_1_, boolean p_i45373_2_)
/*     */   {
/*  25 */     super(p_i45373_1_);
/*  26 */     this.field_150638_aH = this.field_150635_aE;
/*     */     
/*  28 */     if (p_i45373_2_)
/*     */     {
/*  30 */       this.theBiomeDecorator.treesPerChunk = 3;
/*  31 */       this.field_150638_aH = this.field_150636_aF;
/*     */     }
/*     */   }
/*     */   
/*     */   public WorldGenAbstractTree genBigTreeChance(Random rand)
/*     */   {
/*  37 */     return rand.nextInt(3) > 0 ? this.field_150634_aD : super.genBigTreeChance(rand);
/*     */   }
/*     */   
/*     */   public void decorate(World worldIn, Random rand, BlockPos pos)
/*     */   {
/*  42 */     super.decorate(worldIn, rand, pos);
/*  43 */     int i = 3 + rand.nextInt(6);
/*     */     
/*  45 */     for (int j = 0; j < i; j++)
/*     */     {
/*  47 */       int k = rand.nextInt(16);
/*  48 */       int l = rand.nextInt(28) + 4;
/*  49 */       int i1 = rand.nextInt(16);
/*  50 */       BlockPos blockpos = pos.add(k, l, i1);
/*     */       
/*  52 */       if (worldIn.getBlockState(blockpos).getBlock() == Blocks.stone)
/*     */       {
/*  54 */         worldIn.setBlockState(blockpos, Blocks.emerald_ore.getDefaultState(), 2);
/*     */       }
/*     */     }
/*     */     
/*  58 */     for (i = 0; i < 7; i++)
/*     */     {
/*  60 */       int j1 = rand.nextInt(16);
/*  61 */       int k1 = rand.nextInt(64);
/*  62 */       int l1 = rand.nextInt(16);
/*  63 */       this.theWorldGenerator.generate(worldIn, rand, pos.add(j1, k1, l1));
/*     */     }
/*     */   }
/*     */   
/*     */   public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int p_180622_4_, int p_180622_5_, double p_180622_6_)
/*     */   {
/*  69 */     this.topBlock = Blocks.grass.getDefaultState();
/*  70 */     this.fillerBlock = Blocks.dirt.getDefaultState();
/*     */     
/*  72 */     if (((p_180622_6_ < -1.0D) || (p_180622_6_ > 2.0D)) && (this.field_150638_aH == this.field_150637_aG))
/*     */     {
/*  74 */       this.topBlock = Blocks.gravel.getDefaultState();
/*  75 */       this.fillerBlock = Blocks.gravel.getDefaultState();
/*     */     }
/*  77 */     else if ((p_180622_6_ > 1.0D) && (this.field_150638_aH != this.field_150636_aF))
/*     */     {
/*  79 */       this.topBlock = Blocks.stone.getDefaultState();
/*  80 */       this.fillerBlock = Blocks.stone.getDefaultState();
/*     */     }
/*     */     
/*  83 */     generateBiomeTerrain(worldIn, rand, chunkPrimerIn, p_180622_4_, p_180622_5_, p_180622_6_);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private BiomeGenHills mutateHills(BiomeGenBase p_150633_1_)
/*     */   {
/*  91 */     this.field_150638_aH = this.field_150637_aG;
/*  92 */     func_150557_a(p_150633_1_.color, true);
/*  93 */     setBiomeName(p_150633_1_.biomeName + " M");
/*  94 */     setHeight(new BiomeGenBase.Height(p_150633_1_.minHeight, p_150633_1_.maxHeight));
/*  95 */     setTemperatureRainfall(p_150633_1_.temperature, p_150633_1_.rainfall);
/*  96 */     return this;
/*     */   }
/*     */   
/*     */   protected BiomeGenBase createMutatedBiome(int p_180277_1_)
/*     */   {
/* 101 */     return new BiomeGenHills(p_180277_1_, false).mutateHills(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\biome\BiomeGenHills.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */