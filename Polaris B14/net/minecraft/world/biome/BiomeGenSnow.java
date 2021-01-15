/*    */ package net.minecraft.world.biome;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraft.world.gen.feature.WorldGenAbstractTree;
/*    */ import net.minecraft.world.gen.feature.WorldGenIcePath;
/*    */ import net.minecraft.world.gen.feature.WorldGenIceSpike;
/*    */ 
/*    */ public class BiomeGenSnow extends BiomeGenBase
/*    */ {
/*    */   private boolean field_150615_aC;
/* 15 */   private WorldGenIceSpike field_150616_aD = new WorldGenIceSpike();
/* 16 */   private WorldGenIcePath field_150617_aE = new WorldGenIcePath(4);
/*    */   
/*    */   public BiomeGenSnow(int p_i45378_1_, boolean p_i45378_2_)
/*    */   {
/* 20 */     super(p_i45378_1_);
/* 21 */     this.field_150615_aC = p_i45378_2_;
/*    */     
/* 23 */     if (p_i45378_2_)
/*    */     {
/* 25 */       this.topBlock = net.minecraft.init.Blocks.snow.getDefaultState();
/*    */     }
/*    */     
/* 28 */     this.spawnableCreatureList.clear();
/*    */   }
/*    */   
/*    */   public void decorate(World worldIn, Random rand, BlockPos pos)
/*    */   {
/* 33 */     if (this.field_150615_aC)
/*    */     {
/* 35 */       for (int i = 0; i < 3; i++)
/*    */       {
/* 37 */         int j = rand.nextInt(16) + 8;
/* 38 */         int k = rand.nextInt(16) + 8;
/* 39 */         this.field_150616_aD.generate(worldIn, rand, worldIn.getHeight(pos.add(j, 0, k)));
/*    */       }
/*    */       
/* 42 */       for (int l = 0; l < 2; l++)
/*    */       {
/* 44 */         int i1 = rand.nextInt(16) + 8;
/* 45 */         int j1 = rand.nextInt(16) + 8;
/* 46 */         this.field_150617_aE.generate(worldIn, rand, worldIn.getHeight(pos.add(i1, 0, j1)));
/*    */       }
/*    */     }
/*    */     
/* 50 */     super.decorate(worldIn, rand, pos);
/*    */   }
/*    */   
/*    */   public WorldGenAbstractTree genBigTreeChance(Random rand)
/*    */   {
/* 55 */     return new net.minecraft.world.gen.feature.WorldGenTaiga2(false);
/*    */   }
/*    */   
/*    */   protected BiomeGenBase createMutatedBiome(int p_180277_1_)
/*    */   {
/* 60 */     BiomeGenBase biomegenbase = new BiomeGenSnow(p_180277_1_, true).func_150557_a(13828095, true).setBiomeName(this.biomeName + " Spikes").setEnableSnow().setTemperatureRainfall(0.0F, 0.5F).setHeight(new BiomeGenBase.Height(this.minHeight + 0.1F, this.maxHeight + 0.1F));
/* 61 */     biomegenbase.minHeight = (this.minHeight + 0.3F);
/* 62 */     biomegenbase.maxHeight = (this.maxHeight + 0.4F);
/* 63 */     return biomegenbase;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\biome\BiomeGenSnow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */