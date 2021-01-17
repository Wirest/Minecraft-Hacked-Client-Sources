// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.pathfinder;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;

public class SwimNodeProcessor extends NodeProcessor
{
    @Override
    public void initProcessor(final IBlockAccess iblockaccessIn, final Entity entityIn) {
        super.initProcessor(iblockaccessIn, entityIn);
    }
    
    @Override
    public void postProcess() {
        super.postProcess();
    }
    
    @Override
    public PathPoint getPathPointTo(final Entity entityIn) {
        return this.openPoint(MathHelper.floor_double(entityIn.getEntityBoundingBox().minX), MathHelper.floor_double(entityIn.getEntityBoundingBox().minY + 0.5), MathHelper.floor_double(entityIn.getEntityBoundingBox().minZ));
    }
    
    @Override
    public PathPoint getPathPointToCoords(final Entity entityIn, final double x, final double y, final double target) {
        return this.openPoint(MathHelper.floor_double(x - entityIn.width / 2.0f), MathHelper.floor_double(y + 0.5), MathHelper.floor_double(target - entityIn.width / 2.0f));
    }
    
    @Override
    public int findPathOptions(final PathPoint[] pathOptions, final Entity entityIn, final PathPoint currentPoint, final PathPoint targetPoint, final float maxDistance) {
        int i = 0;
        EnumFacing[] values;
        for (int length = (values = EnumFacing.values()).length, j = 0; j < length; ++j) {
            final EnumFacing enumfacing = values[j];
            final PathPoint pathpoint = this.getSafePoint(entityIn, currentPoint.xCoord + enumfacing.getFrontOffsetX(), currentPoint.yCoord + enumfacing.getFrontOffsetY(), currentPoint.zCoord + enumfacing.getFrontOffsetZ());
            if (pathpoint != null && !pathpoint.visited && pathpoint.distanceTo(targetPoint) < maxDistance) {
                pathOptions[i++] = pathpoint;
            }
        }
        return i;
    }
    
    private PathPoint getSafePoint(final Entity entityIn, final int x, final int y, final int z) {
        final int i = this.func_176186_b(entityIn, x, y, z);
        return (i == -1) ? this.openPoint(x, y, z) : null;
    }
    
    private int func_176186_b(final Entity entityIn, final int x, final int y, final int z) {
        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (int i = x; i < x + this.entitySizeX; ++i) {
            for (int j = y; j < y + this.entitySizeY; ++j) {
                for (int k = z; k < z + this.entitySizeZ; ++k) {
                    final Block block = this.blockaccess.getBlockState(blockpos$mutableblockpos.func_181079_c(i, j, k)).getBlock();
                    if (block.getMaterial() != Material.water) {
                        return 0;
                    }
                }
            }
        }
        return -1;
    }
}
