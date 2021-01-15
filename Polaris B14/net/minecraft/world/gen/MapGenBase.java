/*    */ package net.minecraft.world.gen;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraft.world.chunk.ChunkPrimer;
/*    */ import net.minecraft.world.chunk.IChunkProvider;
/*    */ 
/*    */ 
/*    */ public class MapGenBase
/*    */ {
/* 11 */   protected int range = 8;
/*    */   
/*    */ 
/* 14 */   protected Random rand = new Random();
/*    */   
/*    */   protected World worldObj;
/*    */   
/*    */ 
/*    */   public void generate(IChunkProvider chunkProviderIn, World worldIn, int x, int z, ChunkPrimer chunkPrimerIn)
/*    */   {
/* 21 */     int i = this.range;
/* 22 */     this.worldObj = worldIn;
/* 23 */     this.rand.setSeed(worldIn.getSeed());
/* 24 */     long j = this.rand.nextLong();
/* 25 */     long k = this.rand.nextLong();
/*    */     
/* 27 */     for (int l = x - i; l <= x + i; l++)
/*    */     {
/* 29 */       for (int i1 = z - i; i1 <= z + i; i1++)
/*    */       {
/* 31 */         long j1 = l * j;
/* 32 */         long k1 = i1 * k;
/* 33 */         this.rand.setSeed(j1 ^ k1 ^ worldIn.getSeed());
/* 34 */         recursiveGenerate(worldIn, l, i1, x, z, chunkPrimerIn);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   protected void recursiveGenerate(World worldIn, int chunkX, int chunkZ, int p_180701_4_, int p_180701_5_, ChunkPrimer chunkPrimerIn) {}
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\MapGenBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */