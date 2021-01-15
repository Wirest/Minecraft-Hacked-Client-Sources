/*    */ package net.minecraft.world.biome;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.BlockLeaves;
/*    */ import net.minecraft.block.BlockOldLeaf;
/*    */ import net.minecraft.block.BlockOldLog;
/*    */ import net.minecraft.block.BlockPlanks.EnumType;
/*    */ import net.minecraft.block.BlockTallGrass.EnumType;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.entity.passive.EntityChicken;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraft.world.gen.feature.WorldGenAbstractTree;
/*    */ import net.minecraft.world.gen.feature.WorldGenMelon;
/*    */ import net.minecraft.world.gen.feature.WorldGenShrub;
/*    */ import net.minecraft.world.gen.feature.WorldGenTallGrass;
/*    */ import net.minecraft.world.gen.feature.WorldGenTrees;
/*    */ import net.minecraft.world.gen.feature.WorldGenVines;
/*    */ import net.minecraft.world.gen.feature.WorldGenerator;
/*    */ 
/*    */ public class BiomeGenJungle extends BiomeGenBase
/*    */ {
/*    */   private boolean field_150614_aC;
/* 27 */   private static final IBlockState field_181620_aE = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
/* 28 */   private static final IBlockState field_181621_aF = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
/* 29 */   private static final IBlockState field_181622_aG = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
/*    */   
/*    */   public BiomeGenJungle(int p_i45379_1_, boolean p_i45379_2_)
/*    */   {
/* 33 */     super(p_i45379_1_);
/* 34 */     this.field_150614_aC = p_i45379_2_;
/*    */     
/* 36 */     if (p_i45379_2_)
/*    */     {
/* 38 */       this.theBiomeDecorator.treesPerChunk = 2;
/*    */     }
/*    */     else
/*    */     {
/* 42 */       this.theBiomeDecorator.treesPerChunk = 50;
/*    */     }
/*    */     
/* 45 */     this.theBiomeDecorator.grassPerChunk = 25;
/* 46 */     this.theBiomeDecorator.flowersPerChunk = 4;
/*    */     
/* 48 */     if (!p_i45379_2_)
/*    */     {
/* 50 */       this.spawnableMonsterList.add(new BiomeGenBase.SpawnListEntry(net.minecraft.entity.passive.EntityOcelot.class, 2, 1, 1));
/*    */     }
/*    */     
/* 53 */     this.spawnableCreatureList.add(new BiomeGenBase.SpawnListEntry(EntityChicken.class, 10, 4, 4));
/*    */   }
/*    */   
/*    */   public WorldGenAbstractTree genBigTreeChance(Random rand)
/*    */   {
/* 58 */     return (!this.field_150614_aC) && (rand.nextInt(3) == 0) ? new net.minecraft.world.gen.feature.WorldGenMegaJungle(false, 10, 20, field_181620_aE, field_181621_aF) : rand.nextInt(2) == 0 ? new WorldGenShrub(field_181620_aE, field_181622_aG) : rand.nextInt(10) == 0 ? this.worldGeneratorBigTree : new WorldGenTrees(false, 4 + rand.nextInt(7), field_181620_aE, field_181621_aF, true);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public WorldGenerator getRandomWorldGenForGrass(Random rand)
/*    */   {
/* 66 */     return rand.nextInt(4) == 0 ? new WorldGenTallGrass(BlockTallGrass.EnumType.FERN) : new WorldGenTallGrass(BlockTallGrass.EnumType.GRASS);
/*    */   }
/*    */   
/*    */   public void decorate(World worldIn, Random rand, BlockPos pos)
/*    */   {
/* 71 */     super.decorate(worldIn, rand, pos);
/* 72 */     int i = rand.nextInt(16) + 8;
/* 73 */     int j = rand.nextInt(16) + 8;
/* 74 */     int k = rand.nextInt(worldIn.getHeight(pos.add(i, 0, j)).getY() * 2);
/* 75 */     new WorldGenMelon().generate(worldIn, rand, pos.add(i, k, j));
/* 76 */     WorldGenVines worldgenvines = new WorldGenVines();
/*    */     
/* 78 */     for (j = 0; j < 50; j++)
/*    */     {
/* 80 */       k = rand.nextInt(16) + 8;
/* 81 */       int l = 128;
/* 82 */       int i1 = rand.nextInt(16) + 8;
/* 83 */       worldgenvines.generate(worldIn, rand, pos.add(k, 128, i1));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\biome\BiomeGenJungle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */