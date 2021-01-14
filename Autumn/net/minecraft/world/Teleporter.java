package net.minecraft.world;

import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.MathHelper;

public class Teleporter {
   private final WorldServer worldServerInstance;
   private final Random random;
   private final LongHashMap destinationCoordinateCache = new LongHashMap();
   private final List destinationCoordinateKeys = Lists.newArrayList();

   public Teleporter(WorldServer worldIn) {
      this.worldServerInstance = worldIn;
      this.random = new Random(worldIn.getSeed());
   }

   public void placeInPortal(Entity entityIn, float rotationYaw) {
      if (this.worldServerInstance.provider.getDimensionId() != 1) {
         if (!this.placeInExistingPortal(entityIn, rotationYaw)) {
            this.makePortal(entityIn);
            this.placeInExistingPortal(entityIn, rotationYaw);
         }
      } else {
         int i = MathHelper.floor_double(entityIn.posX);
         int j = MathHelper.floor_double(entityIn.posY) - 1;
         int k = MathHelper.floor_double(entityIn.posZ);
         int l = 1;
         int i1 = 0;

         for(int j1 = -2; j1 <= 2; ++j1) {
            for(int k1 = -2; k1 <= 2; ++k1) {
               for(int l1 = -1; l1 < 3; ++l1) {
                  int i2 = i + k1 * l + j1 * i1;
                  int j2 = j + l1;
                  int k2 = k + k1 * i1 - j1 * l;
                  boolean flag = l1 < 0;
                  this.worldServerInstance.setBlockState(new BlockPos(i2, j2, k2), flag ? Blocks.obsidian.getDefaultState() : Blocks.air.getDefaultState());
               }
            }
         }

         entityIn.setLocationAndAngles((double)i, (double)j, (double)k, entityIn.rotationYaw, 0.0F);
         entityIn.motionX = entityIn.motionY = entityIn.motionZ = 0.0D;
      }

   }

   public boolean placeInExistingPortal(Entity entityIn, float rotationYaw) {
      int i = true;
      double d0 = -1.0D;
      int j = MathHelper.floor_double(entityIn.posX);
      int k = MathHelper.floor_double(entityIn.posZ);
      boolean flag = true;
      BlockPos blockpos = BlockPos.ORIGIN;
      long l = ChunkCoordIntPair.chunkXZ2Int(j, k);
      if (this.destinationCoordinateCache.containsItem(l)) {
         Teleporter.PortalPosition teleporter$portalposition = (Teleporter.PortalPosition)this.destinationCoordinateCache.getValueByKey(l);
         d0 = 0.0D;
         blockpos = teleporter$portalposition;
         teleporter$portalposition.lastUpdateTime = this.worldServerInstance.getTotalWorldTime();
         flag = false;
      } else {
         BlockPos blockpos3 = new BlockPos(entityIn);

         for(int i1 = -128; i1 <= 128; ++i1) {
            BlockPos blockpos2;
            for(int j1 = -128; j1 <= 128; ++j1) {
               for(BlockPos blockpos1 = blockpos3.add(i1, this.worldServerInstance.getActualHeight() - 1 - blockpos3.getY(), j1); blockpos1.getY() >= 0; blockpos1 = blockpos2) {
                  blockpos2 = blockpos1.down();
                  if (this.worldServerInstance.getBlockState(blockpos1).getBlock() == Blocks.portal) {
                     while(this.worldServerInstance.getBlockState(blockpos2 = blockpos1.down()).getBlock() == Blocks.portal) {
                        blockpos1 = blockpos2;
                     }

                     double d1 = blockpos1.distanceSq(blockpos3);
                     if (d0 < 0.0D || d1 < d0) {
                        d0 = d1;
                        blockpos = blockpos1;
                     }
                  }
               }
            }
         }
      }

      if (d0 >= 0.0D) {
         if (flag) {
            this.destinationCoordinateCache.add(l, new Teleporter.PortalPosition((BlockPos)blockpos, this.worldServerInstance.getTotalWorldTime()));
            this.destinationCoordinateKeys.add(l);
         }

         double d5 = (double)((BlockPos)blockpos).getX() + 0.5D;
         double d6 = (double)((BlockPos)blockpos).getY() + 0.5D;
         double d7 = (double)((BlockPos)blockpos).getZ() + 0.5D;
         BlockPattern.PatternHelper blockpattern$patternhelper = Blocks.portal.func_181089_f(this.worldServerInstance, (BlockPos)blockpos);
         boolean flag1 = blockpattern$patternhelper.getFinger().rotateY().getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE;
         double d2 = blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X ? (double)blockpattern$patternhelper.func_181117_a().getZ() : (double)blockpattern$patternhelper.func_181117_a().getX();
         d6 = (double)(blockpattern$patternhelper.func_181117_a().getY() + 1) - entityIn.func_181014_aG().yCoord * (double)blockpattern$patternhelper.func_181119_e();
         if (flag1) {
            ++d2;
         }

         if (blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X) {
            d7 = d2 + (1.0D - entityIn.func_181014_aG().xCoord) * (double)blockpattern$patternhelper.func_181118_d() * (double)blockpattern$patternhelper.getFinger().rotateY().getAxisDirection().getOffset();
         } else {
            d5 = d2 + (1.0D - entityIn.func_181014_aG().xCoord) * (double)blockpattern$patternhelper.func_181118_d() * (double)blockpattern$patternhelper.getFinger().rotateY().getAxisDirection().getOffset();
         }

         float f = 0.0F;
         float f1 = 0.0F;
         float f2 = 0.0F;
         float f3 = 0.0F;
         if (blockpattern$patternhelper.getFinger().getOpposite() == entityIn.func_181012_aH()) {
            f = 1.0F;
            f1 = 1.0F;
         } else if (blockpattern$patternhelper.getFinger().getOpposite() == entityIn.func_181012_aH().getOpposite()) {
            f = -1.0F;
            f1 = -1.0F;
         } else if (blockpattern$patternhelper.getFinger().getOpposite() == entityIn.func_181012_aH().rotateY()) {
            f2 = 1.0F;
            f3 = -1.0F;
         } else {
            f2 = -1.0F;
            f3 = 1.0F;
         }

         double d3 = entityIn.motionX;
         double d4 = entityIn.motionZ;
         entityIn.motionX = d3 * (double)f + d4 * (double)f3;
         entityIn.motionZ = d3 * (double)f2 + d4 * (double)f1;
         entityIn.rotationYaw = rotationYaw - (float)(entityIn.func_181012_aH().getOpposite().getHorizontalIndex() * 90) + (float)(blockpattern$patternhelper.getFinger().getHorizontalIndex() * 90);
         entityIn.setLocationAndAngles(d5, d6, d7, entityIn.rotationYaw, entityIn.rotationPitch);
         return true;
      } else {
         return false;
      }
   }

   public boolean makePortal(Entity p_85188_1_) {
      int i = 16;
      double d0 = -1.0D;
      int j = MathHelper.floor_double(p_85188_1_.posX);
      int k = MathHelper.floor_double(p_85188_1_.posY);
      int l = MathHelper.floor_double(p_85188_1_.posZ);
      int i1 = j;
      int j1 = k;
      int k1 = l;
      int l1 = 0;
      int i2 = this.random.nextInt(4);
      BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

      int l5;
      double d3;
      int j6;
      double d4;
      int i7;
      int k7;
      int j8;
      int j9;
      int j10;
      int j11;
      int j12;
      int i13;
      int j13;
      double d6;
      double d8;
      for(l5 = j - i; l5 <= j + i; ++l5) {
         d3 = (double)l5 + 0.5D - p_85188_1_.posX;

         for(j6 = l - i; j6 <= l + i; ++j6) {
            d4 = (double)j6 + 0.5D - p_85188_1_.posZ;

            label291:
            for(i7 = this.worldServerInstance.getActualHeight() - 1; i7 >= 0; --i7) {
               if (this.worldServerInstance.isAirBlock(blockpos$mutableblockpos.func_181079_c(l5, i7, j6))) {
                  while(i7 > 0 && this.worldServerInstance.isAirBlock(blockpos$mutableblockpos.func_181079_c(l5, i7 - 1, j6))) {
                     --i7;
                  }

                  for(k7 = i2; k7 < i2 + 4; ++k7) {
                     j8 = k7 % 2;
                     j9 = 1 - j8;
                     if (k7 % 4 >= 2) {
                        j8 = -j8;
                        j9 = -j9;
                     }

                     for(j10 = 0; j10 < 3; ++j10) {
                        for(j11 = 0; j11 < 4; ++j11) {
                           for(j12 = -1; j12 < 4; ++j12) {
                              i13 = l5 + (j11 - 1) * j8 + j10 * j9;
                              j13 = i7 + j12;
                              int k5 = j6 + (j11 - 1) * j9 - j10 * j8;
                              blockpos$mutableblockpos.func_181079_c(i13, j13, k5);
                              if (j12 < 0 && !this.worldServerInstance.getBlockState(blockpos$mutableblockpos).getBlock().getMaterial().isSolid() || j12 >= 0 && !this.worldServerInstance.isAirBlock(blockpos$mutableblockpos)) {
                                 continue label291;
                              }
                           }
                        }
                     }

                     d6 = (double)i7 + 0.5D - p_85188_1_.posY;
                     d8 = d3 * d3 + d6 * d6 + d4 * d4;
                     if (d0 < 0.0D || d8 < d0) {
                        d0 = d8;
                        i1 = l5;
                        j1 = i7;
                        k1 = j6;
                        l1 = k7 % 4;
                     }
                  }
               }
            }
         }
      }

      if (d0 < 0.0D) {
         for(l5 = j - i; l5 <= j + i; ++l5) {
            d3 = (double)l5 + 0.5D - p_85188_1_.posX;

            for(j6 = l - i; j6 <= l + i; ++j6) {
               d4 = (double)j6 + 0.5D - p_85188_1_.posZ;

               label229:
               for(i7 = this.worldServerInstance.getActualHeight() - 1; i7 >= 0; --i7) {
                  if (this.worldServerInstance.isAirBlock(blockpos$mutableblockpos.func_181079_c(l5, i7, j6))) {
                     while(i7 > 0 && this.worldServerInstance.isAirBlock(blockpos$mutableblockpos.func_181079_c(l5, i7 - 1, j6))) {
                        --i7;
                     }

                     for(k7 = i2; k7 < i2 + 2; ++k7) {
                        j8 = k7 % 2;
                        j9 = 1 - j8;

                        for(j10 = 0; j10 < 4; ++j10) {
                           for(j11 = -1; j11 < 4; ++j11) {
                              j12 = l5 + (j10 - 1) * j8;
                              i13 = i7 + j11;
                              j13 = j6 + (j10 - 1) * j9;
                              blockpos$mutableblockpos.func_181079_c(j12, i13, j13);
                              if (j11 < 0 && !this.worldServerInstance.getBlockState(blockpos$mutableblockpos).getBlock().getMaterial().isSolid() || j11 >= 0 && !this.worldServerInstance.isAirBlock(blockpos$mutableblockpos)) {
                                 continue label229;
                              }
                           }
                        }

                        d6 = (double)i7 + 0.5D - p_85188_1_.posY;
                        d8 = d3 * d3 + d6 * d6 + d4 * d4;
                        if (d0 < 0.0D || d8 < d0) {
                           d0 = d8;
                           i1 = l5;
                           j1 = i7;
                           k1 = j6;
                           l1 = k7 % 2;
                        }
                     }
                  }
               }
            }
         }
      }

      l5 = i1;
      int k2 = j1;
      int k6 = k1;
      j6 = l1 % 2;
      int i3 = 1 - j6;
      if (l1 % 4 >= 2) {
         j6 = -j6;
         i3 = -i3;
      }

      if (d0 < 0.0D) {
         j1 = MathHelper.clamp_int(j1, 70, this.worldServerInstance.getActualHeight() - 10);
         k2 = j1;

         for(int j7 = -1; j7 <= 1; ++j7) {
            for(i7 = 1; i7 < 3; ++i7) {
               for(k7 = -1; k7 < 3; ++k7) {
                  j8 = l5 + (i7 - 1) * j6 + j7 * i3;
                  j9 = k2 + k7;
                  j10 = k6 + (i7 - 1) * i3 - j7 * j6;
                  boolean flag = k7 < 0;
                  this.worldServerInstance.setBlockState(new BlockPos(j8, j9, j10), flag ? Blocks.obsidian.getDefaultState() : Blocks.air.getDefaultState());
               }
            }
         }
      }

      IBlockState iblockstate = Blocks.portal.getDefaultState().withProperty(BlockPortal.AXIS, j6 != 0 ? EnumFacing.Axis.X : EnumFacing.Axis.Z);

      for(i7 = 0; i7 < 4; ++i7) {
         for(k7 = 0; k7 < 4; ++k7) {
            for(j8 = -1; j8 < 4; ++j8) {
               j9 = l5 + (k7 - 1) * j6;
               j10 = k2 + j8;
               j11 = k6 + (k7 - 1) * i3;
               boolean flag1 = k7 == 0 || k7 == 3 || j8 == -1 || j8 == 3;
               this.worldServerInstance.setBlockState(new BlockPos(j9, j10, j11), flag1 ? Blocks.obsidian.getDefaultState() : iblockstate, 2);
            }
         }

         for(k7 = 0; k7 < 4; ++k7) {
            for(j8 = -1; j8 < 4; ++j8) {
               j9 = l5 + (k7 - 1) * j6;
               j10 = k2 + j8;
               j11 = k6 + (k7 - 1) * i3;
               BlockPos blockpos = new BlockPos(j9, j10, j11);
               this.worldServerInstance.notifyNeighborsOfStateChange(blockpos, this.worldServerInstance.getBlockState(blockpos).getBlock());
            }
         }
      }

      return true;
   }

   public void removeStalePortalLocations(long worldTime) {
      if (worldTime % 100L == 0L) {
         Iterator iterator = this.destinationCoordinateKeys.iterator();
         long i = worldTime - 300L;

         while(true) {
            Long olong;
            Teleporter.PortalPosition teleporter$portalposition;
            do {
               if (!iterator.hasNext()) {
                  return;
               }

               olong = (Long)iterator.next();
               teleporter$portalposition = (Teleporter.PortalPosition)this.destinationCoordinateCache.getValueByKey(olong);
            } while(teleporter$portalposition != null && teleporter$portalposition.lastUpdateTime >= i);

            iterator.remove();
            this.destinationCoordinateCache.remove(olong);
         }
      }
   }

   public class PortalPosition extends BlockPos {
      public long lastUpdateTime;

      public PortalPosition(BlockPos pos, long lastUpdate) {
         super(pos.getX(), pos.getY(), pos.getZ());
         this.lastUpdateTime = lastUpdate;
      }
   }
}
