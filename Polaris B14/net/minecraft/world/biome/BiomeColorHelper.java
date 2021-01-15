/*    */ package net.minecraft.world.biome;
/*    */ 
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.IBlockAccess;
/*    */ 
/*    */ public class BiomeColorHelper
/*    */ {
/*  8 */   private static final ColorResolver field_180291_a = new ColorResolver()
/*    */   {
/*    */     public int getColorAtPos(BiomeGenBase p_180283_1_, BlockPos blockPosition)
/*    */     {
/* 12 */       return p_180283_1_.getGrassColorAtPos(blockPosition);
/*    */     }
/*    */   };
/* 15 */   private static final ColorResolver field_180289_b = new ColorResolver()
/*    */   {
/*    */     public int getColorAtPos(BiomeGenBase p_180283_1_, BlockPos blockPosition)
/*    */     {
/* 19 */       return p_180283_1_.getFoliageColorAtPos(blockPosition);
/*    */     }
/*    */   };
/* 22 */   private static final ColorResolver field_180290_c = new ColorResolver()
/*    */   {
/*    */     public int getColorAtPos(BiomeGenBase p_180283_1_, BlockPos blockPosition)
/*    */     {
/* 26 */       return p_180283_1_.waterColorMultiplier;
/*    */     }
/*    */   };
/*    */   
/*    */   private static int func_180285_a(IBlockAccess p_180285_0_, BlockPos p_180285_1_, ColorResolver p_180285_2_)
/*    */   {
/* 32 */     int i = 0;
/* 33 */     int j = 0;
/* 34 */     int k = 0;
/*    */     
/* 36 */     for (net.minecraft.util.BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(p_180285_1_.add(-1, 0, -1), p_180285_1_.add(1, 0, 1)))
/*    */     {
/* 38 */       int l = p_180285_2_.getColorAtPos(p_180285_0_.getBiomeGenForCoords(blockpos$mutableblockpos), blockpos$mutableblockpos);
/* 39 */       i += ((l & 0xFF0000) >> 16);
/* 40 */       j += ((l & 0xFF00) >> 8);
/* 41 */       k += (l & 0xFF);
/*    */     }
/*    */     
/* 44 */     return (i / 9 & 0xFF) << 16 | (j / 9 & 0xFF) << 8 | k / 9 & 0xFF;
/*    */   }
/*    */   
/*    */   public static int getGrassColorAtPos(IBlockAccess p_180286_0_, BlockPos p_180286_1_)
/*    */   {
/* 49 */     return func_180285_a(p_180286_0_, p_180286_1_, field_180291_a);
/*    */   }
/*    */   
/*    */   public static int getFoliageColorAtPos(IBlockAccess p_180287_0_, BlockPos p_180287_1_)
/*    */   {
/* 54 */     return func_180285_a(p_180287_0_, p_180287_1_, field_180289_b);
/*    */   }
/*    */   
/*    */   public static int getWaterColorAtPos(IBlockAccess p_180288_0_, BlockPos p_180288_1_)
/*    */   {
/* 59 */     return func_180285_a(p_180288_0_, p_180288_1_, field_180290_c);
/*    */   }
/*    */   
/*    */   static abstract interface ColorResolver
/*    */   {
/*    */     public abstract int getColorAtPos(BiomeGenBase paramBiomeGenBase, BlockPos paramBlockPos);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\biome\BiomeColorHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */