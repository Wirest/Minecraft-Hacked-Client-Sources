/*    */ package net.minecraft.world.gen.feature;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.BlockPos.MutableBlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class WorldGenSpikes extends WorldGenerator
/*    */ {
/*    */   private Block baseBlockRequired;
/*    */   
/*    */   public WorldGenSpikes(Block p_i45464_1_)
/*    */   {
/* 17 */     this.baseBlockRequired = p_i45464_1_;
/*    */   }
/*    */   
/*    */   public boolean generate(World worldIn, Random rand, BlockPos position)
/*    */   {
/* 22 */     if ((worldIn.isAirBlock(position)) && (worldIn.getBlockState(position.down()).getBlock() == this.baseBlockRequired))
/*    */     {
/* 24 */       int i = rand.nextInt(32) + 6;
/* 25 */       int j = rand.nextInt(4) + 1;
/* 26 */       BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*    */       
/* 28 */       for (int k = position.getX() - j; k <= position.getX() + j; k++)
/*    */       {
/* 30 */         for (int l = position.getZ() - j; l <= position.getZ() + j; l++)
/*    */         {
/* 32 */           int i1 = k - position.getX();
/* 33 */           int j1 = l - position.getZ();
/*    */           
/* 35 */           if ((i1 * i1 + j1 * j1 <= j * j + 1) && (worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(k, position.getY() - 1, l)).getBlock() != this.baseBlockRequired))
/*    */           {
/* 37 */             return false;
/*    */           }
/*    */         }
/*    */       }
/*    */       
/* 42 */       for (int l1 = position.getY(); (l1 < position.getY() + i) && (l1 < 256); l1++)
/*    */       {
/* 44 */         for (int i2 = position.getX() - j; i2 <= position.getX() + j; i2++)
/*    */         {
/* 46 */           for (int j2 = position.getZ() - j; j2 <= position.getZ() + j; j2++)
/*    */           {
/* 48 */             int k2 = i2 - position.getX();
/* 49 */             int k1 = j2 - position.getZ();
/*    */             
/* 51 */             if (k2 * k2 + k1 * k1 <= j * j + 1)
/*    */             {
/* 53 */               worldIn.setBlockState(new BlockPos(i2, l1, j2), net.minecraft.init.Blocks.obsidian.getDefaultState(), 2);
/*    */             }
/*    */           }
/*    */         }
/*    */       }
/*    */       
/* 59 */       Entity entity = new net.minecraft.entity.item.EntityEnderCrystal(worldIn);
/* 60 */       entity.setLocationAndAngles(position.getX() + 0.5F, position.getY() + i, position.getZ() + 0.5F, rand.nextFloat() * 360.0F, 0.0F);
/* 61 */       worldIn.spawnEntityInWorld(entity);
/* 62 */       worldIn.setBlockState(position.up(i), net.minecraft.init.Blocks.bedrock.getDefaultState(), 2);
/* 63 */       return true;
/*    */     }
/*    */     
/*    */ 
/* 67 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\gen\feature\WorldGenSpikes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */