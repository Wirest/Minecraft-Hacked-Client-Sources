/*    */ package net.minecraft.world.biome;
/*    */ 
/*    */ import net.minecraft.block.BlockSand;
/*    */ 
/*    */ public class BiomeGenBeach extends BiomeGenBase
/*    */ {
/*    */   public BiomeGenBeach(int p_i1969_1_)
/*    */   {
/*  9 */     super(p_i1969_1_);
/* 10 */     this.spawnableCreatureList.clear();
/* 11 */     this.topBlock = net.minecraft.init.Blocks.sand.getDefaultState();
/* 12 */     this.fillerBlock = net.minecraft.init.Blocks.sand.getDefaultState();
/* 13 */     this.theBiomeDecorator.treesPerChunk = 64537;
/* 14 */     this.theBiomeDecorator.deadBushPerChunk = 0;
/* 15 */     this.theBiomeDecorator.reedsPerChunk = 0;
/* 16 */     this.theBiomeDecorator.cactiPerChunk = 0;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\biome\BiomeGenBeach.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */