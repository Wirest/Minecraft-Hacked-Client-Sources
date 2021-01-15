/*    */ package net.minecraft.world.biome;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import net.minecraft.util.BlockPos;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WorldChunkManagerHell
/*    */   extends WorldChunkManager
/*    */ {
/*    */   private BiomeGenBase biomeGenerator;
/*    */   private float rainfall;
/*    */   
/*    */   public WorldChunkManagerHell(BiomeGenBase p_i45374_1_, float p_i45374_2_)
/*    */   {
/* 18 */     this.biomeGenerator = p_i45374_1_;
/* 19 */     this.rainfall = p_i45374_2_;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public BiomeGenBase getBiomeGenerator(BlockPos pos)
/*    */   {
/* 27 */     return this.biomeGenerator;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public BiomeGenBase[] getBiomesForGeneration(BiomeGenBase[] biomes, int x, int z, int width, int height)
/*    */   {
/* 35 */     if ((biomes == null) || (biomes.length < width * height))
/*    */     {
/* 37 */       biomes = new BiomeGenBase[width * height];
/*    */     }
/*    */     
/* 40 */     Arrays.fill(biomes, 0, width * height, this.biomeGenerator);
/* 41 */     return biomes;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public float[] getRainfall(float[] listToReuse, int x, int z, int width, int length)
/*    */   {
/* 49 */     if ((listToReuse == null) || (listToReuse.length < width * length))
/*    */     {
/* 51 */       listToReuse = new float[width * length];
/*    */     }
/*    */     
/* 54 */     Arrays.fill(listToReuse, 0, width * length, this.rainfall);
/* 55 */     return listToReuse;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public BiomeGenBase[] loadBlockGeneratorData(BiomeGenBase[] oldBiomeList, int x, int z, int width, int depth)
/*    */   {
/* 64 */     if ((oldBiomeList == null) || (oldBiomeList.length < width * depth))
/*    */     {
/* 66 */       oldBiomeList = new BiomeGenBase[width * depth];
/*    */     }
/*    */     
/* 69 */     Arrays.fill(oldBiomeList, 0, width * depth, this.biomeGenerator);
/* 70 */     return oldBiomeList;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public BiomeGenBase[] getBiomeGenAt(BiomeGenBase[] listToReuse, int x, int z, int width, int length, boolean cacheFlag)
/*    */   {
/* 79 */     return loadBlockGeneratorData(listToReuse, x, z, width, length);
/*    */   }
/*    */   
/*    */   public BlockPos findBiomePosition(int x, int z, int range, List<BiomeGenBase> biomes, Random random)
/*    */   {
/* 84 */     return biomes.contains(this.biomeGenerator) ? new BlockPos(x - range + random.nextInt(range * 2 + 1), 0, z - range + random.nextInt(range * 2 + 1)) : null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean areBiomesViable(int p_76940_1_, int p_76940_2_, int p_76940_3_, List<BiomeGenBase> p_76940_4_)
/*    */   {
/* 92 */     return p_76940_4_.contains(this.biomeGenerator);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\biome\WorldChunkManagerHell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */