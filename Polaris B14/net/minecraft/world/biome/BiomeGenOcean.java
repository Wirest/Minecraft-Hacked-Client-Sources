/*    */ package net.minecraft.world.biome;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraft.world.chunk.ChunkPrimer;
/*    */ 
/*    */ public class BiomeGenOcean extends BiomeGenBase
/*    */ {
/*    */   public BiomeGenOcean(int p_i1985_1_)
/*    */   {
/* 11 */     super(p_i1985_1_);
/* 12 */     this.spawnableCreatureList.clear();
/*    */   }
/*    */   
/*    */   public BiomeGenBase.TempCategory getTempCategory()
/*    */   {
/* 17 */     return BiomeGenBase.TempCategory.OCEAN;
/*    */   }
/*    */   
/*    */   public void genTerrainBlocks(World worldIn, Random rand, ChunkPrimer chunkPrimerIn, int p_180622_4_, int p_180622_5_, double p_180622_6_)
/*    */   {
/* 22 */     super.genTerrainBlocks(worldIn, rand, chunkPrimerIn, p_180622_4_, p_180622_5_, p_180622_6_);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\biome\BiomeGenOcean.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */