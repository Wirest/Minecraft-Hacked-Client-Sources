package net.minecraft.world.pathfinder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

public class WalkNodeProcessor extends NodeProcessor
{
    private boolean canEnterDoors;
    private boolean canBreakDoors;
    private boolean avoidsWater;
    private boolean canSwim;
    private boolean shouldAvoidWater;

    @Override
	public void initProcessor(IBlockAccess iblockaccessIn, Entity entityIn)
    {
        super.initProcessor(iblockaccessIn, entityIn);
        this.shouldAvoidWater = this.avoidsWater;
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
        this.avoidsWater = this.shouldAvoidWater;
    }

    /**
     * Returns given entity's position as PathPoint
     */
    @Override
	public PathPoint getPathPointTo(Entity entityIn)
    {
        int var2;

        if (this.canSwim && entityIn.isInWater())
        {
            var2 = (int)entityIn.getEntityBoundingBox().minY;

            for (Block var3 = this.blockaccess.getBlockState(new BlockPos(MathHelper.floor_double(entityIn.posX), var2, MathHelper.floor_double(entityIn.posZ))).getBlock(); var3 == Blocks.flowing_water || var3 == Blocks.water; var3 = this.blockaccess.getBlockState(new BlockPos(MathHelper.floor_double(entityIn.posX), var2, MathHelper.floor_double(entityIn.posZ))).getBlock())
            {
                ++var2;
            }

            this.avoidsWater = false;
        }
        else
        {
            var2 = MathHelper.floor_double(entityIn.getEntityBoundingBox().minY + 0.5D);
        }

        return this.openPoint(MathHelper.floor_double(entityIn.getEntityBoundingBox().minX), var2, MathHelper.floor_double(entityIn.getEntityBoundingBox().minZ));
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
        return this.openPoint(MathHelper.floor_double(x - entityIn.width / 2.0F), MathHelper.floor_double(y), MathHelper.floor_double(target - entityIn.width / 2.0F));
    }

    @Override
	public int findPathOptions(PathPoint[] pathOptions, Entity entityIn, PathPoint currentPoint, PathPoint targetPoint, float maxDistance)
    {
        int var6 = 0;
        byte var7 = 0;

        if (this.getVerticalOffset(entityIn, currentPoint.xCoord, currentPoint.yCoord + 1, currentPoint.zCoord) == 1)
        {
            var7 = 1;
        }

        PathPoint var8 = this.getSafePoint(entityIn, currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord + 1, var7);
        PathPoint var9 = this.getSafePoint(entityIn, currentPoint.xCoord - 1, currentPoint.yCoord, currentPoint.zCoord, var7);
        PathPoint var10 = this.getSafePoint(entityIn, currentPoint.xCoord + 1, currentPoint.yCoord, currentPoint.zCoord, var7);
        PathPoint var11 = this.getSafePoint(entityIn, currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord - 1, var7);

        if (var8 != null && !var8.visited && var8.distanceTo(targetPoint) < maxDistance)
        {
            pathOptions[var6++] = var8;
        }

        if (var9 != null && !var9.visited && var9.distanceTo(targetPoint) < maxDistance)
        {
            pathOptions[var6++] = var9;
        }

        if (var10 != null && !var10.visited && var10.distanceTo(targetPoint) < maxDistance)
        {
            pathOptions[var6++] = var10;
        }

        if (var11 != null && !var11.visited && var11.distanceTo(targetPoint) < maxDistance)
        {
            pathOptions[var6++] = var11;
        }

        return var6;
    }

    /**
     * Returns a point that the entity can safely move to
     */
    private PathPoint getSafePoint(Entity entityIn, int x, int y, int z, int p_176171_5_)
    {
        PathPoint var6 = null;
        int var7 = this.getVerticalOffset(entityIn, x, y, z);

        if (var7 == 2)
        {
            return this.openPoint(x, y, z);
        }
        else
        {
            if (var7 == 1)
            {
                var6 = this.openPoint(x, y, z);
            }

            if (var6 == null && p_176171_5_ > 0 && var7 != -3 && var7 != -4 && this.getVerticalOffset(entityIn, x, y + p_176171_5_, z) == 1)
            {
                var6 = this.openPoint(x, y + p_176171_5_, z);
                y += p_176171_5_;
            }

            if (var6 != null)
            {
                int var8 = 0;
                int var9;

                for (var9 = 0; y > 0; var6 = this.openPoint(x, y, z))
                {
                    var9 = this.getVerticalOffset(entityIn, x, y - 1, z);

                    if (this.avoidsWater && var9 == -1)
                    {
                        return null;
                    }

                    if (var9 != 1)
                    {
                        break;
                    }

                    if (var8++ >= entityIn.getMaxFallHeight())
                    {
                        return null;
                    }

                    --y;

                    if (y <= 0)
                    {
                        return null;
                    }
                }

                if (var9 == -2)
                {
                    return null;
                }
            }

            return var6;
        }
    }

    /**
     * Checks if an entity collides with blocks at a position.
     * Returns 1 if clear, 0 for colliding with any solid block, -1 for water(if avoids water),
     * -2 for lava, -3 for fence and wall, -4 for closed trapdoor, 2 if otherwise clear except for open trapdoor or
     * water(if not avoiding)
     */
    private int getVerticalOffset(Entity entityIn, int x, int y, int z)
    {
        return func_176170_a(this.blockaccess, entityIn, x, y, z, this.entitySizeX, this.entitySizeY, this.entitySizeZ, this.avoidsWater, this.canBreakDoors, this.canEnterDoors);
    }

    public static int func_176170_a(IBlockAccess blockaccessIn, Entity entityIn, int x, int y, int z, int sizeX, int sizeY, int sizeZ, boolean avoidWater, boolean breakDoors, boolean enterDoors)
    {
        boolean var11 = false;
        BlockPos var12 = new BlockPos(entityIn);

        for (int var13 = x; var13 < x + sizeX; ++var13)
        {
            for (int var14 = y; var14 < y + sizeY; ++var14)
            {
                for (int var15 = z; var15 < z + sizeZ; ++var15)
                {
                    BlockPos var16 = new BlockPos(var13, var14, var15);
                    Block var17 = blockaccessIn.getBlockState(var16).getBlock();

                    if (var17.getMaterial() != Material.air)
                    {
                        if (var17 != Blocks.trapdoor && var17 != Blocks.iron_trapdoor)
                        {
                            if (var17 != Blocks.flowing_water && var17 != Blocks.water)
                            {
                                if (!enterDoors && var17 instanceof BlockDoor && var17.getMaterial() == Material.wood)
                                {
                                    return 0;
                                }
                            }
                            else
                            {
                                if (avoidWater)
                                {
                                    return -1;
                                }

                                var11 = true;
                            }
                        }
                        else
                        {
                            var11 = true;
                        }

                        if (entityIn.worldObj.getBlockState(var16).getBlock() instanceof BlockRailBase)
                        {
                            if (!(entityIn.worldObj.getBlockState(var12).getBlock() instanceof BlockRailBase) && !(entityIn.worldObj.getBlockState(var12.down()).getBlock() instanceof BlockRailBase))
                            {
                                return -3;
                            }
                        }
                        else if (!var17.isPassable(blockaccessIn, var16) && (!breakDoors || !(var17 instanceof BlockDoor) || var17.getMaterial() != Material.wood))
                        {
                            if (var17 instanceof BlockFence || var17 instanceof BlockFenceGate || var17 instanceof BlockWall)
                            {
                                return -3;
                            }

                            if (var17 == Blocks.trapdoor || var17 == Blocks.iron_trapdoor)
                            {
                                return -4;
                            }

                            Material var18 = var17.getMaterial();

                            if (var18 != Material.lava)
                            {
                                return 0;
                            }

                            if (!entityIn.isInLava())
                            {
                                return -2;
                            }
                        }
                    }
                }
            }
        }

        return var11 ? 2 : 1;
    }

    public void setEnterDoors(boolean canEnterDoorsIn)
    {
        this.canEnterDoors = canEnterDoorsIn;
    }

    public void setBreakDoors(boolean canBreakDoorsIn)
    {
        this.canBreakDoors = canBreakDoorsIn;
    }

    public void setAvoidsWater(boolean avoidsWaterIn)
    {
        this.avoidsWater = avoidsWaterIn;
    }

    public void setCanSwim(boolean canSwimIn)
    {
        this.canSwim = canSwimIn;
    }

    public boolean getEnterDoors()
    {
        return this.canEnterDoors;
    }

    public boolean getCanSwim()
    {
        return this.canSwim;
    }

    public boolean getAvoidsWater()
    {
        return this.avoidsWater;
    }
}
