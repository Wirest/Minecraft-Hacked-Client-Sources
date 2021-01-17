// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.pathfinder;

import net.minecraft.block.BlockWall;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;

public class WalkNodeProcessor extends NodeProcessor
{
    private boolean canEnterDoors;
    private boolean canBreakDoors;
    private boolean avoidsWater;
    private boolean canSwim;
    private boolean shouldAvoidWater;
    
    @Override
    public void initProcessor(final IBlockAccess iblockaccessIn, final Entity entityIn) {
        super.initProcessor(iblockaccessIn, entityIn);
        this.shouldAvoidWater = this.avoidsWater;
    }
    
    @Override
    public void postProcess() {
        super.postProcess();
        this.avoidsWater = this.shouldAvoidWater;
    }
    
    @Override
    public PathPoint getPathPointTo(final Entity entityIn) {
        int i;
        if (this.canSwim && entityIn.isInWater()) {
            i = (int)entityIn.getEntityBoundingBox().minY;
            final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(MathHelper.floor_double(entityIn.posX), i, MathHelper.floor_double(entityIn.posZ));
            for (Block block = this.blockaccess.getBlockState(blockpos$mutableblockpos).getBlock(); block == Blocks.flowing_water || block == Blocks.water; block = this.blockaccess.getBlockState(blockpos$mutableblockpos).getBlock()) {
                ++i;
                blockpos$mutableblockpos.func_181079_c(MathHelper.floor_double(entityIn.posX), i, MathHelper.floor_double(entityIn.posZ));
            }
            this.avoidsWater = false;
        }
        else {
            i = MathHelper.floor_double(entityIn.getEntityBoundingBox().minY + 0.5);
        }
        return this.openPoint(MathHelper.floor_double(entityIn.getEntityBoundingBox().minX), i, MathHelper.floor_double(entityIn.getEntityBoundingBox().minZ));
    }
    
    @Override
    public PathPoint getPathPointToCoords(final Entity entityIn, final double x, final double y, final double target) {
        return this.openPoint(MathHelper.floor_double(x - entityIn.width / 2.0f), MathHelper.floor_double(y), MathHelper.floor_double(target - entityIn.width / 2.0f));
    }
    
    @Override
    public int findPathOptions(final PathPoint[] pathOptions, final Entity entityIn, final PathPoint currentPoint, final PathPoint targetPoint, final float maxDistance) {
        int i = 0;
        int j = 0;
        if (this.getVerticalOffset(entityIn, currentPoint.xCoord, currentPoint.yCoord + 1, currentPoint.zCoord) == 1) {
            j = 1;
        }
        final PathPoint pathpoint = this.getSafePoint(entityIn, currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord + 1, j);
        final PathPoint pathpoint2 = this.getSafePoint(entityIn, currentPoint.xCoord - 1, currentPoint.yCoord, currentPoint.zCoord, j);
        final PathPoint pathpoint3 = this.getSafePoint(entityIn, currentPoint.xCoord + 1, currentPoint.yCoord, currentPoint.zCoord, j);
        final PathPoint pathpoint4 = this.getSafePoint(entityIn, currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord - 1, j);
        if (pathpoint != null && !pathpoint.visited && pathpoint.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i++] = pathpoint;
        }
        if (pathpoint2 != null && !pathpoint2.visited && pathpoint2.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i++] = pathpoint2;
        }
        if (pathpoint3 != null && !pathpoint3.visited && pathpoint3.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i++] = pathpoint3;
        }
        if (pathpoint4 != null && !pathpoint4.visited && pathpoint4.distanceTo(targetPoint) < maxDistance) {
            pathOptions[i++] = pathpoint4;
        }
        return i;
    }
    
    private PathPoint getSafePoint(final Entity entityIn, final int x, int y, final int z, final int p_176171_5_) {
        PathPoint pathpoint = null;
        final int i = this.getVerticalOffset(entityIn, x, y, z);
        if (i == 2) {
            return this.openPoint(x, y, z);
        }
        if (i == 1) {
            pathpoint = this.openPoint(x, y, z);
        }
        if (pathpoint == null && p_176171_5_ > 0 && i != -3 && i != -4 && this.getVerticalOffset(entityIn, x, y + p_176171_5_, z) == 1) {
            pathpoint = this.openPoint(x, y + p_176171_5_, z);
            y += p_176171_5_;
        }
        if (pathpoint != null) {
            int j = 0;
            int k = 0;
            while (y > 0) {
                k = this.getVerticalOffset(entityIn, x, y - 1, z);
                if (this.avoidsWater && k == -1) {
                    return null;
                }
                if (k != 1) {
                    break;
                }
                if (j++ >= entityIn.getMaxFallHeight()) {
                    return null;
                }
                if (--y <= 0) {
                    return null;
                }
                pathpoint = this.openPoint(x, y, z);
            }
            if (k == -2) {
                return null;
            }
        }
        return pathpoint;
    }
    
    private int getVerticalOffset(final Entity entityIn, final int x, final int y, final int z) {
        return func_176170_a(this.blockaccess, entityIn, x, y, z, this.entitySizeX, this.entitySizeY, this.entitySizeZ, this.avoidsWater, this.canBreakDoors, this.canEnterDoors);
    }
    
    public static int func_176170_a(final IBlockAccess blockaccessIn, final Entity entityIn, final int x, final int y, final int z, final int sizeX, final int sizeY, final int sizeZ, final boolean avoidWater, final boolean breakDoors, final boolean enterDoors) {
        boolean flag = false;
        final BlockPos blockpos = new BlockPos(entityIn);
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int i = x; i < x + sizeX; ++i) {
            for (int j = y; j < y + sizeY; ++j) {
                for (int k = z; k < z + sizeZ; ++k) {
                    blockpos$mutableblockpos.func_181079_c(i, j, k);
                    final Block block = blockaccessIn.getBlockState(blockpos$mutableblockpos).getBlock();
                    if (block.getMaterial() != Material.air) {
                        if (block != Blocks.trapdoor && block != Blocks.iron_trapdoor) {
                            if (block != Blocks.flowing_water && block != Blocks.water) {
                                if (!enterDoors && block instanceof BlockDoor && block.getMaterial() == Material.wood) {
                                    return 0;
                                }
                            }
                            else {
                                if (avoidWater) {
                                    return -1;
                                }
                                flag = true;
                            }
                        }
                        else {
                            flag = true;
                        }
                        if (entityIn.worldObj.getBlockState(blockpos$mutableblockpos).getBlock() instanceof BlockRailBase) {
                            if (!(entityIn.worldObj.getBlockState(blockpos).getBlock() instanceof BlockRailBase) && !(entityIn.worldObj.getBlockState(blockpos.down()).getBlock() instanceof BlockRailBase)) {
                                return -3;
                            }
                        }
                        else if (!block.isPassable(blockaccessIn, blockpos$mutableblockpos) && (!breakDoors || !(block instanceof BlockDoor) || block.getMaterial() != Material.wood)) {
                            if (block instanceof BlockFence || block instanceof BlockFenceGate || block instanceof BlockWall) {
                                return -3;
                            }
                            if (block == Blocks.trapdoor || block == Blocks.iron_trapdoor) {
                                return -4;
                            }
                            final Material material = block.getMaterial();
                            if (material != Material.lava) {
                                return 0;
                            }
                            if (!entityIn.isInLava()) {
                                return -2;
                            }
                        }
                    }
                }
            }
        }
        return flag ? 2 : 1;
    }
    
    public void setEnterDoors(final boolean canEnterDoorsIn) {
        this.canEnterDoors = canEnterDoorsIn;
    }
    
    public void setBreakDoors(final boolean canBreakDoorsIn) {
        this.canBreakDoors = canBreakDoorsIn;
    }
    
    public void setAvoidsWater(final boolean avoidsWaterIn) {
        this.avoidsWater = avoidsWaterIn;
    }
    
    public void setCanSwim(final boolean canSwimIn) {
        this.canSwim = canSwimIn;
    }
    
    public boolean getEnterDoors() {
        return this.canEnterDoors;
    }
    
    public boolean getCanSwim() {
        return this.canSwim;
    }
    
    public boolean getAvoidsWater() {
        return this.avoidsWater;
    }
}
