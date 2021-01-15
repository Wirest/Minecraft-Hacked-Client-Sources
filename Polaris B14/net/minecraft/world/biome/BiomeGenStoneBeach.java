/*    */ package net.minecraft.world.biome;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ 
/*    */ public class BiomeGenStoneBeach extends BiomeGenBase
/*    */ {
/*    */   public BiomeGenStoneBeach(int p_i45384_1_)
/*    */   {
/*  9 */     super(p_i45384_1_);
/* 10 */     this.spawnableCreatureList.clear();
/* 11 */     this.topBlock = net.minecraft.init.Blocks.stone.getDefaultState();
/* 12 */     this.fillerBlock = net.minecraft.init.Blocks.stone.getDefaultState();
/* 13 */     this.theBiomeDecorator.treesPerChunk = 64537;
/* 14 */     this.theBiomeDecorator.deadBushPerChunk = 0;
/* 15 */     this.theBiomeDecorator.reedsPerChunk = 0;
/* 16 */     this.theBiomeDecorator.cactiPerChunk = 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\biome\BiomeGenStoneBeach.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */