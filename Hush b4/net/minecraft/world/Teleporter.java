// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

import java.util.Iterator;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.util.LongHashMap;
import java.util.Random;

public class Teleporter
{
    private final WorldServer worldServerInstance;
    private final Random random;
    private final LongHashMap destinationCoordinateCache;
    private final List<Long> destinationCoordinateKeys;
    
    public Teleporter(final WorldServer worldIn) {
        this.destinationCoordinateCache = new LongHashMap();
        this.destinationCoordinateKeys = (List<Long>)Lists.newArrayList();
        this.worldServerInstance = worldIn;
        this.random = new Random(worldIn.getSeed());
    }
    
    public void placeInPortal(final Entity entityIn, final float rotationYaw) {
        if (this.worldServerInstance.provider.getDimensionId() != 1) {
            if (!this.placeInExistingPortal(entityIn, rotationYaw)) {
                this.makePortal(entityIn);
                this.placeInExistingPortal(entityIn, rotationYaw);
            }
        }
        else {
            final int i = MathHelper.floor_double(entityIn.posX);
            final int j = MathHelper.floor_double(entityIn.posY) - 1;
            final int k = MathHelper.floor_double(entityIn.posZ);
            final int l = 1;
            final int i2 = 0;
            for (int j2 = -2; j2 <= 2; ++j2) {
                for (int k2 = -2; k2 <= 2; ++k2) {
                    for (int l2 = -1; l2 < 3; ++l2) {
                        final int i3 = i + k2 * l + j2 * i2;
                        final int j3 = j + l2;
                        final int k3 = k + k2 * i2 - j2 * l;
                        final boolean flag = l2 < 0;
                        this.worldServerInstance.setBlockState(new BlockPos(i3, j3, k3), flag ? Blocks.obsidian.getDefaultState() : Blocks.air.getDefaultState());
                    }
                }
            }
            entityIn.setLocationAndAngles(i, j, k, entityIn.rotationYaw, 0.0f);
            final double motionX = 0.0;
            entityIn.motionZ = motionX;
            entityIn.motionY = motionX;
            entityIn.motionX = motionX;
        }
    }
    
    public boolean placeInExistingPortal(final Entity entityIn, final float rotationYaw) {
        final int i = 128;
        double d0 = -1.0;
        final int j = MathHelper.floor_double(entityIn.posX);
        final int k = MathHelper.floor_double(entityIn.posZ);
        boolean flag = true;
        BlockPos blockpos = BlockPos.ORIGIN;
        final long l = ChunkCoordIntPair.chunkXZ2Int(j, k);
        if (this.destinationCoordinateCache.containsItem(l)) {
            final PortalPosition teleporter$portalposition = (PortalPosition)this.destinationCoordinateCache.getValueByKey(l);
            d0 = 0.0;
            blockpos = teleporter$portalposition;
            teleporter$portalposition.lastUpdateTime = this.worldServerInstance.getTotalWorldTime();
            flag = false;
        }
        else {
            final BlockPos blockpos2 = new BlockPos(entityIn);
            for (int i2 = -128; i2 <= 128; ++i2) {
                for (int j2 = -128; j2 <= 128; ++j2) {
                    BlockPos blockpos4;
                    for (BlockPos blockpos3 = blockpos2.add(i2, this.worldServerInstance.getActualHeight() - 1 - blockpos2.getY(), j2); blockpos3.getY() >= 0; blockpos3 = blockpos4) {
                        blockpos4 = blockpos3.down();
                        if (this.worldServerInstance.getBlockState(blockpos3).getBlock() == Blocks.portal) {
                            while (this.worldServerInstance.getBlockState(blockpos4 = blockpos3.down()).getBlock() == Blocks.portal) {
                                blockpos3 = blockpos4;
                            }
                            final double d2 = blockpos3.distanceSq(blockpos2);
                            if (d0 < 0.0 || d2 < d0) {
                                d0 = d2;
                                blockpos = blockpos3;
                            }
                        }
                    }
                }
            }
        }
        if (d0 >= 0.0) {
            if (flag) {
                this.destinationCoordinateCache.add(l, new PortalPosition(blockpos, this.worldServerInstance.getTotalWorldTime()));
                this.destinationCoordinateKeys.add(l);
            }
            double d3 = blockpos.getX() + 0.5;
            double d4 = blockpos.getY() + 0.5;
            double d5 = blockpos.getZ() + 0.5;
            final BlockPattern.PatternHelper blockpattern$patternhelper = Blocks.portal.func_181089_f(this.worldServerInstance, blockpos);
            final boolean flag2 = blockpattern$patternhelper.getFinger().rotateY().getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE;
            double d6 = (blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X) ? blockpattern$patternhelper.func_181117_a().getZ() : ((double)blockpattern$patternhelper.func_181117_a().getX());
            d4 = blockpattern$patternhelper.func_181117_a().getY() + 1 - entityIn.func_181014_aG().yCoord * blockpattern$patternhelper.func_181119_e();
            if (flag2) {
                ++d6;
            }
            if (blockpattern$patternhelper.getFinger().getAxis() == EnumFacing.Axis.X) {
                d5 = d6 + (1.0 - entityIn.func_181014_aG().xCoord) * blockpattern$patternhelper.func_181118_d() * blockpattern$patternhelper.getFinger().rotateY().getAxisDirection().getOffset();
            }
            else {
                d3 = d6 + (1.0 - entityIn.func_181014_aG().xCoord) * blockpattern$patternhelper.func_181118_d() * blockpattern$patternhelper.getFinger().rotateY().getAxisDirection().getOffset();
            }
            float f = 0.0f;
            float f2 = 0.0f;
            float f3 = 0.0f;
            float f4 = 0.0f;
            if (blockpattern$patternhelper.getFinger().getOpposite() == entityIn.func_181012_aH()) {
                f = 1.0f;
                f2 = 1.0f;
            }
            else if (blockpattern$patternhelper.getFinger().getOpposite() == entityIn.func_181012_aH().getOpposite()) {
                f = -1.0f;
                f2 = -1.0f;
            }
            else if (blockpattern$patternhelper.getFinger().getOpposite() == entityIn.func_181012_aH().rotateY()) {
                f3 = 1.0f;
                f4 = -1.0f;
            }
            else {
                f3 = -1.0f;
                f4 = 1.0f;
            }
            final double d7 = entityIn.motionX;
            final double d8 = entityIn.motionZ;
            entityIn.motionX = d7 * f + d8 * f4;
            entityIn.motionZ = d7 * f3 + d8 * f2;
            entityIn.setLocationAndAngles(d3, d4, d5, entityIn.rotationYaw = rotationYaw - entityIn.func_181012_aH().getOpposite().getHorizontalIndex() * 90 + blockpattern$patternhelper.getFinger().getHorizontalIndex() * 90, entityIn.rotationPitch);
            return true;
        }
        return false;
    }
    
    public boolean makePortal(final Entity p_85188_1_) {
        final int i = 16;
        double d0 = -1.0;
        final int j = MathHelper.floor_double(p_85188_1_.posX);
        final int k = MathHelper.floor_double(p_85188_1_.posY);
        final int l = MathHelper.floor_double(p_85188_1_.posZ);
        int i2 = j;
        int j2 = k;
        int k2 = l;
        int l2 = 0;
        final int i3 = this.random.nextInt(4);
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int j3 = j - i; j3 <= j + i; ++j3) {
            final double d2 = j3 + 0.5 - p_85188_1_.posX;
            for (int l3 = l - i; l3 <= l + i; ++l3) {
                final double d3 = l3 + 0.5 - p_85188_1_.posZ;
            Label_0447:
                for (int j4 = this.worldServerInstance.getActualHeight() - 1; j4 >= 0; --j4) {
                    if (this.worldServerInstance.isAirBlock(blockpos$mutableblockpos.func_181079_c(j3, j4, l3))) {
                        while (j4 > 0 && this.worldServerInstance.isAirBlock(blockpos$mutableblockpos.func_181079_c(j3, j4 - 1, l3))) {
                            --j4;
                        }
                        for (int k3 = i3; k3 < i3 + 4; ++k3) {
                            int l4 = k3 % 2;
                            int i4 = 1 - l4;
                            if (k3 % 4 >= 2) {
                                l4 = -l4;
                                i4 = -i4;
                            }
                            for (int j5 = 0; j5 < 3; ++j5) {
                                for (int k4 = 0; k4 < 4; ++k4) {
                                    for (int l5 = -1; l5 < 4; ++l5) {
                                        final int i5 = j3 + (k4 - 1) * l4 + j5 * i4;
                                        final int j6 = j4 + l5;
                                        final int k5 = l3 + (k4 - 1) * i4 - j5 * l4;
                                        blockpos$mutableblockpos.func_181079_c(i5, j6, k5);
                                        if (l5 < 0 && !this.worldServerInstance.getBlockState(blockpos$mutableblockpos).getBlock().getMaterial().isSolid()) {
                                            continue Label_0447;
                                        }
                                        if (l5 >= 0 && !this.worldServerInstance.isAirBlock(blockpos$mutableblockpos)) {
                                            continue Label_0447;
                                        }
                                    }
                                }
                            }
                            final double d4 = j4 + 0.5 - p_85188_1_.posY;
                            final double d5 = d2 * d2 + d4 * d4 + d3 * d3;
                            if (d0 < 0.0 || d5 < d0) {
                                d0 = d5;
                                i2 = j3;
                                j2 = j4;
                                k2 = l3;
                                l2 = k3 % 4;
                            }
                        }
                    }
                }
            }
        }
        if (d0 < 0.0) {
            for (int l6 = j - i; l6 <= j + i; ++l6) {
                final double d6 = l6 + 0.5 - p_85188_1_.posX;
                for (int j7 = l - i; j7 <= l + i; ++j7) {
                    final double d7 = j7 + 0.5 - p_85188_1_.posZ;
                Label_0819:
                    for (int i6 = this.worldServerInstance.getActualHeight() - 1; i6 >= 0; --i6) {
                        if (this.worldServerInstance.isAirBlock(blockpos$mutableblockpos.func_181079_c(l6, i6, j7))) {
                            while (i6 > 0 && this.worldServerInstance.isAirBlock(blockpos$mutableblockpos.func_181079_c(l6, i6 - 1, j7))) {
                                --i6;
                            }
                            for (int k6 = i3; k6 < i3 + 2; ++k6) {
                                final int j8 = k6 % 2;
                                final int j9 = 1 - j8;
                                for (int j10 = 0; j10 < 4; ++j10) {
                                    for (int j11 = -1; j11 < 4; ++j11) {
                                        final int j12 = l6 + (j10 - 1) * j8;
                                        final int i7 = i6 + j11;
                                        final int j13 = j7 + (j10 - 1) * j9;
                                        blockpos$mutableblockpos.func_181079_c(j12, i7, j13);
                                        if (j11 < 0 && !this.worldServerInstance.getBlockState(blockpos$mutableblockpos).getBlock().getMaterial().isSolid()) {
                                            continue Label_0819;
                                        }
                                        if (j11 >= 0 && !this.worldServerInstance.isAirBlock(blockpos$mutableblockpos)) {
                                            continue Label_0819;
                                        }
                                    }
                                }
                                final double d8 = i6 + 0.5 - p_85188_1_.posY;
                                final double d9 = d6 * d6 + d8 * d8 + d7 * d7;
                                if (d0 < 0.0 || d9 < d0) {
                                    d0 = d9;
                                    i2 = l6;
                                    j2 = i6;
                                    k2 = j7;
                                    l2 = k6 % 2;
                                }
                            }
                        }
                    }
                }
            }
        }
        final int i8 = i2;
        int k7 = j2;
        final int k8 = k2;
        int l7 = l2 % 2;
        int i9 = 1 - l7;
        if (l2 % 4 >= 2) {
            l7 = -l7;
            i9 = -i9;
        }
        if (d0 < 0.0) {
            j2 = (k7 = MathHelper.clamp_int(j2, 70, this.worldServerInstance.getActualHeight() - 10));
            for (int j14 = -1; j14 <= 1; ++j14) {
                for (int l8 = 1; l8 < 3; ++l8) {
                    for (int k9 = -1; k9 < 3; ++k9) {
                        final int k10 = i8 + (l8 - 1) * l7 + j14 * i9;
                        final int k11 = k7 + k9;
                        final int k12 = k8 + (l8 - 1) * i9 - j14 * l7;
                        final boolean flag = k9 < 0;
                        this.worldServerInstance.setBlockState(new BlockPos(k10, k11, k12), flag ? Blocks.obsidian.getDefaultState() : Blocks.air.getDefaultState());
                    }
                }
            }
        }
        final IBlockState iblockstate = Blocks.portal.getDefaultState().withProperty(BlockPortal.AXIS, (l7 != 0) ? EnumFacing.Axis.X : EnumFacing.Axis.Z);
        for (int i10 = 0; i10 < 4; ++i10) {
            for (int l9 = 0; l9 < 4; ++l9) {
                for (int l10 = -1; l10 < 4; ++l10) {
                    final int l11 = i8 + (l9 - 1) * l7;
                    final int l12 = k7 + l10;
                    final int k13 = k8 + (l9 - 1) * i9;
                    final boolean flag2 = l9 == 0 || l9 == 3 || l10 == -1 || l10 == 3;
                    this.worldServerInstance.setBlockState(new BlockPos(l11, l12, k13), flag2 ? Blocks.obsidian.getDefaultState() : iblockstate, 2);
                }
            }
            for (int i11 = 0; i11 < 4; ++i11) {
                for (int i12 = -1; i12 < 4; ++i12) {
                    final int i13 = i8 + (i11 - 1) * l7;
                    final int i14 = k7 + i12;
                    final int l13 = k8 + (i11 - 1) * i9;
                    final BlockPos blockpos = new BlockPos(i13, i14, l13);
                    this.worldServerInstance.notifyNeighborsOfStateChange(blockpos, this.worldServerInstance.getBlockState(blockpos).getBlock());
                }
            }
        }
        return true;
    }
    
    public void removeStalePortalLocations(final long worldTime) {
        if (worldTime % 100L == 0L) {
            final Iterator<Long> iterator = this.destinationCoordinateKeys.iterator();
            final long i = worldTime - 300L;
            while (iterator.hasNext()) {
                final Long olong = iterator.next();
                final PortalPosition teleporter$portalposition = (PortalPosition)this.destinationCoordinateCache.getValueByKey(olong);
                if (teleporter$portalposition == null || teleporter$portalposition.lastUpdateTime < i) {
                    iterator.remove();
                    this.destinationCoordinateCache.remove(olong);
                }
            }
        }
    }
    
    public class PortalPosition extends BlockPos
    {
        public long lastUpdateTime;
        
        public PortalPosition(final BlockPos pos, final long lastUpdate) {
            super(pos.getX(), pos.getY(), pos.getZ());
            this.lastUpdateTime = lastUpdate;
        }
    }
}
