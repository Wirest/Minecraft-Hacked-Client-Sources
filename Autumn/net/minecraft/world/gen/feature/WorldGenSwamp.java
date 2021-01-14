package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenSwamp extends WorldGenAbstractTree {
   private static final IBlockState field_181648_a;
   private static final IBlockState field_181649_b;

   public WorldGenSwamp() {
      super(false);
   }

   public boolean generate(World worldIn, Random rand, BlockPos position) {
      int i;
      for(i = rand.nextInt(4) + 5; worldIn.getBlockState(position.down()).getBlock().getMaterial() == Material.water; position = position.down()) {
      }

      boolean flag = true;
      if (position.getY() >= 1 && position.getY() + i + 1 <= 256) {
         int j3;
         int k3;
         for(int j = position.getY(); j <= position.getY() + 1 + i; ++j) {
            int k = 1;
            if (j == position.getY()) {
               k = 0;
            }

            if (j >= position.getY() + 1 + i - 2) {
               k = 3;
            }

            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for(j3 = position.getX() - k; j3 <= position.getX() + k && flag; ++j3) {
               for(k3 = position.getZ() - k; k3 <= position.getZ() + k && flag; ++k3) {
                  if (j >= 0 && j < 256) {
                     Block block = worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(j3, j, k3)).getBlock();
                     if (block.getMaterial() != Material.air && block.getMaterial() != Material.leaves) {
                        if (block != Blocks.water && block != Blocks.flowing_water) {
                           flag = false;
                        } else if (j > position.getY()) {
                           flag = false;
                        }
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
            if ((block1 == Blocks.grass || block1 == Blocks.dirt) && position.getY() < 256 - i - 1) {
               this.func_175921_a(worldIn, position.down());

               int j4;
               BlockPos blockpos4;
               int j2;
               int k2;
               int i4;
               for(j2 = position.getY() - 3 + i; j2 <= position.getY() + i; ++j2) {
                  k2 = j2 - (position.getY() + i);
                  j3 = 2 - k2 / 2;

                  for(k3 = position.getX() - j3; k3 <= position.getX() + j3; ++k3) {
                     i4 = k3 - position.getX();

                     for(j4 = position.getZ() - j3; j4 <= position.getZ() + j3; ++j4) {
                        int k1 = j4 - position.getZ();
                        if (Math.abs(i4) != j3 || Math.abs(k1) != j3 || rand.nextInt(2) != 0 && k2 != 0) {
                           blockpos4 = new BlockPos(k3, j2, j4);
                           if (!worldIn.getBlockState(blockpos4).getBlock().isFullBlock()) {
                              this.setBlockAndNotifyAdequately(worldIn, blockpos4, field_181649_b);
                           }
                        }
                     }
                  }
               }

               for(j2 = 0; j2 < i; ++j2) {
                  Block block2 = worldIn.getBlockState(position.up(j2)).getBlock();
                  if (block2.getMaterial() == Material.air || block2.getMaterial() == Material.leaves || block2 == Blocks.flowing_water || block2 == Blocks.water) {
                     this.setBlockAndNotifyAdequately(worldIn, position.up(j2), field_181648_a);
                  }
               }

               for(j2 = position.getY() - 3 + i; j2 <= position.getY() + i; ++j2) {
                  k2 = j2 - (position.getY() + i);
                  j3 = 2 - k2 / 2;
                  BlockPos.MutableBlockPos blockpos$mutableblockpos1 = new BlockPos.MutableBlockPos();

                  for(i4 = position.getX() - j3; i4 <= position.getX() + j3; ++i4) {
                     for(j4 = position.getZ() - j3; j4 <= position.getZ() + j3; ++j4) {
                        blockpos$mutableblockpos1.func_181079_c(i4, j2, j4);
                        if (worldIn.getBlockState(blockpos$mutableblockpos1).getBlock().getMaterial() == Material.leaves) {
                           BlockPos blockpos3 = blockpos$mutableblockpos1.west();
                           blockpos4 = blockpos$mutableblockpos1.east();
                           BlockPos blockpos1 = blockpos$mutableblockpos1.north();
                           BlockPos blockpos2 = blockpos$mutableblockpos1.south();
                           if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos3).getBlock().getMaterial() == Material.air) {
                              this.func_181647_a(worldIn, blockpos3, BlockVine.EAST);
                           }

                           if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos4).getBlock().getMaterial() == Material.air) {
                              this.func_181647_a(worldIn, blockpos4, BlockVine.WEST);
                           }

                           if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos1).getBlock().getMaterial() == Material.air) {
                              this.func_181647_a(worldIn, blockpos1, BlockVine.SOUTH);
                           }

                           if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos2).getBlock().getMaterial() == Material.air) {
                              this.func_181647_a(worldIn, blockpos2, BlockVine.NORTH);
                           }
                        }
                     }
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

   private void func_181647_a(World p_181647_1_, BlockPos p_181647_2_, PropertyBool p_181647_3_) {
      IBlockState iblockstate = Blocks.vine.getDefaultState().withProperty(p_181647_3_, true);
      this.setBlockAndNotifyAdequately(p_181647_1_, p_181647_2_, iblockstate);
      int i = 4;

      for(p_181647_2_ = p_181647_2_.down(); p_181647_1_.getBlockState(p_181647_2_).getBlock().getMaterial() == Material.air && i > 0; --i) {
         this.setBlockAndNotifyAdequately(p_181647_1_, p_181647_2_, iblockstate);
         p_181647_2_ = p_181647_2_.down();
      }

   }

   static {
      field_181648_a = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);
      field_181649_b = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockOldLeaf.CHECK_DECAY, false);
   }
}
