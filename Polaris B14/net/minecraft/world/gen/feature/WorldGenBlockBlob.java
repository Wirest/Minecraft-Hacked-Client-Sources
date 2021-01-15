/*    */ package net.minecraft.world.gen.feature;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class WorldGenBlockBlob extends WorldGenerator
/*    */ {
/*    */   private final Block field_150545_a;
/*    */   private final int field_150544_b;
/*    */   
/*    */   public WorldGenBlockBlob(Block p_i45450_1_, int p_i45450_2_)
/*    */   {
/* 16 */     super(false);
/* 17 */     this.field_150545_a = p_i45450_1_;
/* 18 */     this.field_150544_b = p_i45450_2_;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean generate(World worldIn, Random rand, BlockPos position)
/*    */   {
/*    */     for (;;)
/*    */     {
/* 27 */       if (position.getY() > 3)
/*    */       {
/* 29 */         if (!worldIn.isAirBlock(position.down()))
/*    */         {
/*    */ 
/*    */ 
/*    */ 
/* 34 */           Block block = worldIn.getBlockState(position.down()).getBlock();
/*    */           
/* 36 */           if ((block != Blocks.grass) && (block != Blocks.dirt) && (block != Blocks.stone)) {}
/*    */         }
/*    */         
/*    */       }
/*    */       else
/*    */       {
/* 42 */         if (position.getY() <= 3)
/*    */         {
/* 44 */           return false;
/*    */         }
/*    */         
/* 47 */         int i1 = this.field_150544_b;
/*    */         
/* 49 */         for (int i = 0; (i1 >= 0) && (i < 3); i++)
/*    */         {
/* 51 */           int j = i1 + rand.nextInt(2);
/* 52 */           int k = i1 + rand.nextInt(2);
/* 53 */           int l = i1 + rand.nextInt(2);
/* 54 */           float f = (j + k + l) * 0.333F + 0.5F;
/*    */           
/* 56 */           for (BlockPos blockpos : BlockPos.getAllInBox(position.add(-j, -k, -l), position.add(j, k, l)))
/*    */           {
/* 58 */             if (blockpos.distanceSq(position) <= f * f)
/*    */             {
/* 60 */               worldIn.setBlockState(blockpos, this.field_150545_a.getDefaultState(), 4);
/*    */             }
/*    */           }
/*    */           
/* 64 */           position = position.add(-(i1 + 1) + rand.nextInt(2 + i1 * 2), 0 - rand.nextInt(2), -(i1 + 1) + rand.nextInt(2 + i1 * 2));
/*    */         }
/*    */         
/* 67 */         return true;
/*    */       }
/* 69 */       position = position.down();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\feature\WorldGenBlockBlob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */