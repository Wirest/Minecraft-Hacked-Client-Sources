package net.minecraft.world.gen.feature;

import java.util.Iterator;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockVine;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class WorldGenTrees extends WorldGenAbstractTree {
   private static final IBlockState field_181653_a;
   private static final IBlockState field_181654_b;
   private final int minTreeHeight;
   private final boolean vinesGrow;
   private final IBlockState metaWood;
   private final IBlockState metaLeaves;

   public WorldGenTrees(boolean p_i2027_1_) {
      this(p_i2027_1_, 4, field_181653_a, field_181654_b, false);
   }

   public WorldGenTrees(boolean p_i46446_1_, int p_i46446_2_, IBlockState p_i46446_3_, IBlockState p_i46446_4_, boolean p_i46446_5_) {
      super(p_i46446_1_);
      this.minTreeHeight = p_i46446_2_;
      this.metaWood = p_i46446_3_;
      this.metaLeaves = p_i46446_4_;
      this.vinesGrow = p_i46446_5_;
   }

   public boolean generate(World worldIn, Random rand, BlockPos position) {
      int i = rand.nextInt(3) + this.minTreeHeight;
      boolean flag = true;
      if (position.getY() >= 1 && position.getY() + i + 1 <= 256) {
         byte k;
         int l3;
         int i4;
         for(int j = position.getY(); j <= position.getY() + 1 + i; ++j) {
            k = 1;
            if (j == position.getY()) {
               k = 0;
            }

            if (j >= position.getY() + 1 + i - 2) {
               k = 2;
            }

            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for(l3 = position.getX() - k; l3 <= position.getX() + k && flag; ++l3) {
               for(i4 = position.getZ() - k; i4 <= position.getZ() + k && flag; ++i4) {
                  if (j >= 0 && j < 256) {
                     if (!this.func_150523_a(worldIn.getBlockState(blockpos$mutableblockpos.func_181079_c(l3, j, i4)).getBlock())) {
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
               k = 3;
               int l2 = 0;

               int k4;
               int l4;
               int i5;
               BlockPos blockpos3;
               for(l3 = position.getY() - k + i; l3 <= position.getY() + i; ++l3) {
                  i4 = l3 - (position.getY() + i);
                  k4 = l2 + 1 - i4 / 2;

                  for(int k1 = position.getX() - k4; k1 <= position.getX() + k4; ++k1) {
                     l4 = k1 - position.getX();

                     for(i5 = position.getZ() - k4; i5 <= position.getZ() + k4; ++i5) {
                        int j2 = i5 - position.getZ();
                        if (Math.abs(l4) != k4 || Math.abs(j2) != k4 || rand.nextInt(2) != 0 && i4 != 0) {
                           blockpos3 = new BlockPos(k1, l3, i5);
                           Block block = worldIn.getBlockState(blockpos3).getBlock();
                           if (block.getMaterial() == Material.air || block.getMaterial() == Material.leaves || block.getMaterial() == Material.vine) {
                              this.setBlockAndNotifyAdequately(worldIn, blockpos3, this.metaLeaves);
                           }
                        }
                     }
                  }
               }

               for(l3 = 0; l3 < i; ++l3) {
                  Block block2 = worldIn.getBlockState(position.up(l3)).getBlock();
                  if (block2.getMaterial() == Material.air || block2.getMaterial() == Material.leaves || block2.getMaterial() == Material.vine) {
                     this.setBlockAndNotifyAdequately(worldIn, position.up(l3), this.metaWood);
                     if (this.vinesGrow && l3 > 0) {
                        if (rand.nextInt(3) > 0 && worldIn.isAirBlock(position.add(-1, l3, 0))) {
                           this.func_181651_a(worldIn, position.add(-1, l3, 0), BlockVine.EAST);
                        }

                        if (rand.nextInt(3) > 0 && worldIn.isAirBlock(position.add(1, l3, 0))) {
                           this.func_181651_a(worldIn, position.add(1, l3, 0), BlockVine.WEST);
                        }

                        if (rand.nextInt(3) > 0 && worldIn.isAirBlock(position.add(0, l3, -1))) {
                           this.func_181651_a(worldIn, position.add(0, l3, -1), BlockVine.SOUTH);
                        }

                        if (rand.nextInt(3) > 0 && worldIn.isAirBlock(position.add(0, l3, 1))) {
                           this.func_181651_a(worldIn, position.add(0, l3, 1), BlockVine.NORTH);
                        }
                     }
                  }
               }

               if (this.vinesGrow) {
                  for(l3 = position.getY() - 3 + i; l3 <= position.getY() + i; ++l3) {
                     i4 = l3 - (position.getY() + i);
                     k4 = 2 - i4 / 2;
                     BlockPos.MutableBlockPos blockpos$mutableblockpos1 = new BlockPos.MutableBlockPos();

                     for(l4 = position.getX() - k4; l4 <= position.getX() + k4; ++l4) {
                        for(i5 = position.getZ() - k4; i5 <= position.getZ() + k4; ++i5) {
                           blockpos$mutableblockpos1.func_181079_c(l4, l3, i5);
                           if (worldIn.getBlockState(blockpos$mutableblockpos1).getBlock().getMaterial() == Material.leaves) {
                              BlockPos blockpos2 = blockpos$mutableblockpos1.west();
                              blockpos3 = blockpos$mutableblockpos1.east();
                              BlockPos blockpos4 = blockpos$mutableblockpos1.north();
                              BlockPos blockpos1 = blockpos$mutableblockpos1.south();
                              if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos2).getBlock().getMaterial() == Material.air) {
                                 this.func_181650_b(worldIn, blockpos2, BlockVine.EAST);
                              }

                              if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos3).getBlock().getMaterial() == Material.air) {
                                 this.func_181650_b(worldIn, blockpos3, BlockVine.WEST);
                              }

                              if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos4).getBlock().getMaterial() == Material.air) {
                                 this.func_181650_b(worldIn, blockpos4, BlockVine.SOUTH);
                              }

                              if (rand.nextInt(4) == 0 && worldIn.getBlockState(blockpos1).getBlock().getMaterial() == Material.air) {
                                 this.func_181650_b(worldIn, blockpos1, BlockVine.NORTH);
                              }
                           }
                        }
                     }
                  }

                  if (rand.nextInt(5) == 0 && i > 5) {
                     for(l3 = 0; l3 < 2; ++l3) {
                        Iterator var22 = EnumFacing.Plane.HORIZONTAL.iterator();

                        while(var22.hasNext()) {
                           Object enumfacing0 = var22.next();
                           EnumFacing enumfacing = (EnumFacing)enumfacing0;
                           if (rand.nextInt(4 - l3) == 0) {
                              EnumFacing enumfacing1 = enumfacing.getOpposite();
                              this.func_181652_a(worldIn, rand.nextInt(3), position.add(enumfacing1.getFrontOffsetX(), i - 5 + l3, enumfacing1.getFrontOffsetZ()), enumfacing);
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

   private void func_181652_a(World p_181652_1_, int p_181652_2_, BlockPos p_181652_3_, EnumFacing p_181652_4_) {
      this.setBlockAndNotifyAdequately(p_181652_1_, p_181652_3_, Blocks.cocoa.getDefaultState().withProperty(BlockCocoa.AGE, p_181652_2_).withProperty(BlockCocoa.FACING, p_181652_4_));
   }

   private void func_181651_a(World p_181651_1_, BlockPos p_181651_2_, PropertyBool p_181651_3_) {
      this.setBlockAndNotifyAdequately(p_181651_1_, p_181651_2_, Blocks.vine.getDefaultState().withProperty(p_181651_3_, true));
   }

   private void func_181650_b(World p_181650_1_, BlockPos p_181650_2_, PropertyBool p_181650_3_) {
      this.func_181651_a(p_181650_1_, p_181650_2_, p_181650_3_);
      int i = 4;

      for(p_181650_2_ = p_181650_2_.down(); p_181650_1_.getBlockState(p_181650_2_).getBlock().getMaterial() == Material.air && i > 0; --i) {
         this.func_181651_a(p_181650_1_, p_181650_2_, p_181650_3_);
         p_181650_2_ = p_181650_2_.down();
      }

   }

   static {
      field_181653_a = Blocks.log.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);
      field_181654_b = Blocks.leaves.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockLeaves.CHECK_DECAY, false);
   }
}
