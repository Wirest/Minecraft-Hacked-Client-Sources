package net.minecraft.world.pathfinder;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

public class SwimNodeProcessor extends NodeProcessor
{

    @Override
	public void initProcessor(IBlockAccess iblockaccessIn, Entity entityIn)
    {
        super.initProcessor(iblockaccessIn, entityIn);
    }

    /**
     * This method is called when all nodes have been processed and PathEntity is created.
     *  {@link net.minecraft.world.pathfinder.WalkNodeProcessor WalkNodeProcessor} uses this to change its field {@link
     * net.minecraft.world.pathfinder.WalkNodeProcessor#avoidsWater avoidsWater}
     */
    @Override
	public void postProcess()
    {
        super.postProcess();
    }

    /**
     * Returns given entity's position as PathPoint
     */
    @Override
	public PathPoint getPathPointTo(Entity entityIn)
    {
        return this.openPoint(MathHelper.floor_double(entityIn.getEntityBoundingBox().minX), MathHelper.floor_double(entityIn.getEntityBoundingBox().minY + 0.5D), MathHelper.floor_double(entityIn.getEntityBoundingBox().minZ));
    }

    /**
     * Returns PathPoint for given coordinates
     *  
     * @param entityIn entity which size will be used to center position
     * @param x target x coordinate
     * @param y target y coordinate
     * @param target z coordinate
     */
    @Override
	public PathPoint getPathPointToCoords(Entity entityIn, double x, double y, double target)
    {
        return this.openPoint(MathHelper.floor_double(x - entityIn.width / 2.0F), MathHelper.floor_double(y + 0.5D), MathHelper.floor_double(target - entityIn.width / 2.0F));
    }

    @Override
	public int findPathOptions(PathPoint[] pathOptions, Entity entityIn, PathPoint currentPoint, PathPoint targetPoint, float maxDistance)
    {
        int var6 = 0;
        EnumFacing[] var7 = EnumFacing.values();
        int var8 = var7.length;

        for (int var9 = 0; var9 < var8; ++var9)
        {
            EnumFacing var10 = var7[var9];
            PathPoint var11 = this.getSafePoint(entityIn, currentPoint.xCoord + var10.getFrontOffsetX(), currentPoint.yCoord + var10.getFrontOffsetY(), currentPoint.zCoord + var10.getFrontOffsetZ());

            if (var11 != null && !var11.visited && var11.distanceTo(targetPoint) < maxDistance)
            {
                pathOptions[var6++] = var11;
            }
        }

        return var6;
    }

    /**
     * Returns a point that the entity can safely move to
     */
    private PathPoint getSafePoint(Entity entityIn, int x, int y, int z)
    {
        int var5 = this.func_176186_b(entityIn, x, y, z);
        return var5 == -1 ? this.openPoint(x, y, z) : null;
    }

    private int func_176186_b(Entity entityIn, int x, int y, int z)
    {
        for (int var5 = x; var5 < x + this.entitySizeX; ++var5)
        {
            for (int var6 = y; var6 < y + this.entitySizeY; ++var6)
            {
                for (int var7 = z; var7 < z + this.entitySizeZ; ++var7)
                {
                    BlockPos var8 = new BlockPos(var5, var6, var7);
                    Block var9 = this.blockaccess.getBlockState(var8).getBlock();

                    if (var9.getMaterial() != Material.water)
                    {
                        return 0;
                    }
                }
            }
        }

        return -1;
    }
}
