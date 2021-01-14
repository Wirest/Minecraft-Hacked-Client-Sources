package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenTaiga2 extends WorldGenAbstractTree {
   private static final IBlockState field_181645_a;
   private static final IBlockState field_181646_b;

   public WorldGenTaiga2(boolean p_i2025_1_) {
      super(p_i2025_1_);
   }

   public boolean generate(World worldIn, Random rand, BlockPos position) {
      int i = rand.nextInt(4) + 6;
      int j = 1 + rand.nextInt(2);
      int k = i - j;
      int l = 2 + rand.nextInt(2);
      boolean flag = true;
      if (position.getY() >= 1 && position.getY() + i + 1 <= 256) {
         int l1;
         int i3;
         for(int i1 = position.getY(); i1 <= position.getY() + 1 + i && flag; ++i1) {
            int j1 = true;
            if (i1 - position.getY() < j) {
               i3 = 0;
            } else {
               i3 = l;
            }

            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for(int k1 = position.getX() - i3; k1 <= position.getX() + i3 && flag; ++k1) {
               for(l1 = position.getZ() - i3; l1 <= position.getZ() + i3 && flag; ++l1) {
                  if (i1 >= 0 && i1 < 256) {
                     Block block = worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(k1, i1, l1)).getBlock();
                     if (block.getMaterial() != Material.air && block.getMaterial() != Material.leaves) {
                        flag = false;
                     }
                  } else {
                     flag = false;
                  }
               }
            }
         }

         if (!flag) {
            return false;
         } else {
            Block block1 = worldIn.getBlockState(position.down()).getBlock();
            if ((block1 == Blocks.grass || block1 == Blocks.dirt || block1 == Blocks.farmland) && position.getY() < 256 - i - 1) {
               this.func_175921_a(worldIn, position.down());
               i3 = rand.nextInt(2);
               int j3 = 1;
               int k3 = 0;

               int j4;
               for(l1 = 0; l1 <= k; ++l1) {
                  j4 = position.getY() + i - l1;

                  for(int i2 = position.getX() - i3; i2 <= position.getX() + i3; ++i2) {
                     int j2 = i2 - position.getX();

                     for(int k2 = position.getZ() - i3; k2 <= position.getZ() + i3; ++k2) {
                        int l2 = k2 - position.getZ();
                        if (Math.abs(j2) != i3 || Math.abs(l2) != i3 || i3 <= 0) {
                           BlockPos blockpos = new BlockPos(i2, j4, k2);
                           if (!worldIn.getBlockState(blockpos).getBlock().isFullBlock()) {
                              this.setBlockAndNotifyAdequately(worldIn, blockpos, field_181646_b);
                           }
                        }
                     }
                  }

                  if (i3 >= j3) {
                     i3 = k3;
                     k3 = 1;
                     ++j3;
                     if (j3 > l) {
                        j3 = l;
                     }
                  } else {
                     ++i3;
                  }
               }

               l1 = rand.nextInt(3);

               for(j4 = 0; j4 < i - l1; ++j4) {
                  Block block2 = worldIn.getBlockState(position.up(j4)).getBlock();
                  if (block2.getMaterial() == Material.air || block2.getMaterial() == Material.leaves) {
                     this.setBlockAndNotifyAdequately(worldIn, position.up(j4), field_181645_a);
                  }
               }

               return true;
            } else {
               return false;
            }
         }
      } else {
         return false;
      }
   }

   static {
      field_181645_a = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
      field_181646_b = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLeaves.CHECK_DECAY, false);
   }
}
