package net.minecraft.pathfinding;

import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.pathfinder.WalkNodeProcessor;

public class PathNavigateGround extends PathNavigate
{
    protected WalkNodeProcessor nodeProcessor;
    private boolean shouldAvoidSun;

    public PathNavigateGround(EntityLiving entitylivingIn, World worldIn)
    {
        super(entitylivingIn, worldIn);
    }

    @Override
	protected PathFinder getPathFinder()
    {
        this.nodeProcessor = new WalkNodeProcessor();
        this.nodeProcessor.setEnterDoors(true);
        return new PathFinder(this.nodeProcessor);
    }

    /**
     * If on ground or swimming and can swim
     */
    @Override
	protected boolean canNavigate()
    {
        return this.theEntity.onGround || this.getCanSwim() && this.isInLiquid() || this.theEntity.isRiding() && this.theEntity instanceof EntityZombie && this.theEntity.ridingEntity instanceof EntityChicken;
    }

    @Override
	protected Vec3 getEntityPosition()
    {
        return new Vec3(this.theEntity.posX, this.getPathablePosY(), this.theEntity.posZ);
    }

    /**
     * Gets the safe pathing Y position for the entity depending on if it can path swim or not
     */
    private int getPathablePosY()
    {
        if (this.theEntity.isInWater() && this.getCanSwim())
        {
            int var1 = (int)this.theEntity.getEntityBoundingBox().minY;
            Block var2 = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.theEntity.posX), var1, MathHelper.floor_double(this.theEntity.posZ))).getBlock();
            int var3 = 0;

            do
            {
                if (var2 != Blocks.flowing_water && var2 != Blocks.water)
                {
                    return var1;
                }

                ++var1;
                var2 = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.theEntity.posX), var1, MathHelper.floor_double(this.theEntity.posZ))).getBlock();
                ++var3;
            }
            while (var3 <= 16);

            return (int)this.theEntity.getEntityBoundingBox().minY;
        }
        else
        {
            return (int)(this.theEntity.getEntityBoundingBox().minY + 0.5D);
        }
    }

    /**
     * Trims path data from the end to the first sun covered block
     */
    @Override
	protected void removeSunnyPath()
    {
        super.removeSunnyPath();

        if (this.shouldAvoidSun)
        {
            if (this.worldObj.canSeeSky(new BlockPos(MathHelper.floor_double(this.theEntity.posX), (int)(this.theEntity.getEntityBoundingBox().minY + 0.5D), MathHelper.floor_double(this.theEntity.posZ))))
            {
                return;
            }

            for (int var1 = 0; var1 < this.currentPath.getCurrentPathLength(); ++var1)
            {
                PathPoint var2 = this.currentPath.getPathPointFromIndex(var1);

                if (this.worldObj.canSeeSky(new BlockPos(var2.xCoord, var2.yCoord, var2.zCoord)))
                {
                    this.currentPath.setCurrentPathLength(var1 - 1);
                    return;
                }
            }
        }
    }

    /**
     * Returns true when an entity of specified size could safely walk in a straight line between the two points. Args:
     * pos1, pos2, entityXSize, entityYSize, entityZSize
     */
    @Override
	protected boolean isDirectPathBetweenPoints(Vec3 posVec31, Vec3 posVec32, int sizeX, int sizeY, int sizeZ)
    {
        int var6 = MathHelper.floor_double(posVec31.xCoord);
        int var7 = MathHelper.floor_double(posVec31.zCoord);
        double var8 = posVec32.xCoord - posVec31.xCoord;
        double var10 = posVec32.zCoord - posVec31.zCoord;
        double var12 = var8 * var8 + var10 * var10;

        if (var12 < 1.0E-8D)
        {
            return false;
        }
        else
        {
            double var14 = 1.0D / Math.sqrt(var12);
            var8 *= var14;
            var10 *= var14;
            sizeX += 2;
            sizeZ += 2;

            if (!this.isSafeToStandAt(var6, (int)posVec31.yCoord, var7, sizeX, sizeY, sizeZ, posVec31, var8, var10))
            {
                return false;
            }
            else
            {
                sizeX -= 2;
                sizeZ -= 2;
                double var16 = 1.0D / Math.abs(var8);
                double var18 = 1.0D / Math.abs(var10);
                double var20 = var6 * 1 - posVec31.xCoord;
                double var22 = var7 * 1 - posVec31.zCoord;

                if (var8 >= 0.0D)
                {
                    ++var20;
                }

                if (var10 >= 0.0D)
                {
                    ++var22;
                }

                var20 /= var8;
                var22 /= var10;
                int var24 = var8 < 0.0D ? -1 : 1;
                int var25 = var10 < 0.0D ? -1 : 1;
                int var26 = MathHelper.floor_double(posVec32.xCoord);
                int var27 = MathHelper.floor_double(posVec32.zCoord);
                int var28 = var26 - var6;
                int var29 = var27 - var7;

                do
                {
                    if (var28 * var24 <= 0 && var29 * var25 <= 0)
                    {
                        return true;
                    }

                    if (var20 < var22)
                    {
                        var20 += var16;
                        var6 += var24;
                        var28 = var26 - var6;
                    }
                    else
                    {
                        var22 += var18;
                        var7 += var25;
                        var29 = var27 - var7;
                    }
                }
                while (this.isSafeToStandAt(var6, (int)posVec31.yCoord, var7, sizeX, sizeY, sizeZ, posVec31, var8, var10));

                return false;
            }
        }
    }

    /**
     * Returns true when an entity could stand at a position, including solid blocks under the entire entity.
     */
    private boolean isSafeToStandAt(int p_179683_1_, int p_179683_2_, int p_179683_3_, int p_179683_4_, int p_179683_5_, int p_179683_6_, Vec3 p_179683_7_, double p_179683_8_, double p_179683_10_)
    {
        int var12 = p_179683_1_ - p_179683_4_ / 2;
        int var13 = p_179683_3_ - p_179683_6_ / 2;

        if (!this.isPositionClear(var12, p_179683_2_, var13, p_179683_4_, p_179683_5_, p_179683_6_, p_179683_7_, p_179683_8_, p_179683_10_))
        {
            return false;
        }
        else
        {
            for (int var14 = var12; var14 < var12 + p_179683_4_; ++var14)
            {
                for (int var15 = var13; var15 < var13 + p_179683_6_; ++var15)
                {
                    double var16 = var14 + 0.5D - p_179683_7_.xCoord;
                    double var18 = var15 + 0.5D - p_179683_7_.zCoord;

                    if (var16 * p_179683_8_ + var18 * p_179683_10_ >= 0.0D)
                    {
                        Block var20 = this.worldObj.getBlockState(new BlockPos(var14, p_179683_2_ - 1, var15)).getBlock();
                        Material var21 = var20.getMaterial();

                        if (var21 == Material.air)
                        {
                            return false;
                        }

                        if (var21 == Material.water && !this.theEntity.isInWater())
                        {
                            return false;
                        }

                        if (var21 == Material.lava)
                        {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    /**
     * Returns true if an entity does not collide with any solid blocks at the position.
     */
    private boolean isPositionClear(int p_179692_1_, int p_179692_2_, int p_179692_3_, int p_179692_4_, int p_179692_5_, int p_179692_6_, Vec3 p_179692_7_, double p_179692_8_, double p_179692_10_)
    {
        Iterator var12 = BlockPos.getAllInBox(new BlockPos(p_179692_1_, p_179692_2_, p_179692_3_), new BlockPos(p_179692_1_ + p_179692_4_ - 1, p_179692_2_ + p_179692_5_ - 1, p_179692_3_ + p_179692_6_ - 1)).iterator();

        while (var12.hasNext())
        {
            BlockPos var13 = (BlockPos)var12.next();
            double var14 = var13.getX() + 0.5D - p_179692_7_.xCoord;
            double var16 = var13.getZ() + 0.5D - p_179692_7_.zCoord;

            if (var14 * p_179692_8_ + var16 * p_179692_10_ >= 0.0D)
            {
                Block var18 = this.worldObj.getBlockState(var13).getBlock();

                if (!var18.isPassable(this.worldObj, var13))
                {
                    return false;
                }
            }
        }

        return true;
    }

    public void setAvoidsWater(boolean avoidsWater)
    {
        this.nodeProcessor.setAvoidsWater(avoidsWater);
    }

    public boolean getAvoidsWater()
    {
        return this.nodeProcessor.getAvoidsWater();
    }

    public void setBreakDoors(boolean canBreakDoors)
    {
        this.nodeProcessor.setBreakDoors(canBreakDoors);
    }

    public void setEnterDoors(boolean par1)
    {
        this.nodeProcessor.setEnterDoors(par1);
    }

    public boolean getEnterDoors()
    {
        return this.nodeProcessor.getEnterDoors();
    }

    public void setCanSwim(boolean canSwim)
    {
        this.nodeProcessor.setCanSwim(canSwim);
    }

    public boolean getCanSwim()
    {
        return this.nodeProcessor.getCanSwim();
    }

    public void setAvoidSun(boolean par1)
    {
        this.shouldAvoidSun = par1;
    }
}
