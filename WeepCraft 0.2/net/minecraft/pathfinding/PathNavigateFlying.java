package net.minecraft.pathfinding;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PathNavigateFlying extends PathNavigate
{
    public PathNavigateFlying(EntityLiving p_i47412_1_, World p_i47412_2_)
    {
        super(p_i47412_1_, p_i47412_2_);
    }

    protected PathFinder getPathFinder()
    {
        this.nodeProcessor = new FlyingNodeProcessor();
        this.nodeProcessor.setCanEnterDoors(true);
        return new PathFinder(this.nodeProcessor);
    }

    /**
     * If on ground or swimming and can swim
     */
    protected boolean canNavigate()
    {
        return this.func_192880_g() && this.isInLiquid() || !this.theEntity.isRiding();
    }

    protected Vec3d getEntityPosition()
    {
        return new Vec3d(this.theEntity.posX, this.theEntity.posY, this.theEntity.posZ);
    }

    /**
     * Returns the path to the given EntityLiving. Args : entity
     */
    public Path getPathToEntityLiving(Entity entityIn)
    {
        return this.getPathToPos(new BlockPos(entityIn));
    }

    public void onUpdateNavigation()
    {
        ++this.totalTicks;

        if (this.tryUpdatePath)
        {
            this.updatePath();
        }

        if (!this.noPath())
        {
            if (this.canNavigate())
            {
                this.pathFollow();
            }
            else if (this.currentPath != null && this.currentPath.getCurrentPathIndex() < this.currentPath.getCurrentPathLength())
            {
                Vec3d vec3d = this.currentPath.getVectorFromIndex(this.theEntity, this.currentPath.getCurrentPathIndex());

                if (MathHelper.floor(this.theEntity.posX) == MathHelper.floor(vec3d.xCoord) && MathHelper.floor(this.theEntity.posY) == MathHelper.floor(vec3d.yCoord) && MathHelper.floor(this.theEntity.posZ) == MathHelper.floor(vec3d.zCoord))
                {
                    this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1);
                }
            }

            this.func_192876_m();

            if (!this.noPath())
            {
                Vec3d vec3d1 = this.currentPath.getPosition(this.theEntity);
                this.theEntity.getMoveHelper().setMoveTo(vec3d1.xCoord, vec3d1.yCoord, vec3d1.zCoord, this.speed);
            }
        }
    }

    /**
     * Checks if the specified entity can safely walk to the specified location.
     */
    protected boolean isDirectPathBetweenPoints(Vec3d posVec31, Vec3d posVec32, int sizeX, int sizeY, int sizeZ)
    {
        int i = MathHelper.floor(posVec31.xCoord);
        int j = MathHelper.floor(posVec31.yCoord);
        int k = MathHelper.floor(posVec31.zCoord);
        double d0 = posVec32.xCoord - posVec31.xCoord;
        double d1 = posVec32.yCoord - posVec31.yCoord;
        double d2 = posVec32.zCoord - posVec31.zCoord;
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;

        if (d3 < 1.0E-8D)
        {
            return false;
        }
        else
        {
            double d4 = 1.0D / Math.sqrt(d3);
            d0 = d0 * d4;
            d1 = d1 * d4;
            d2 = d2 * d4;
            double d5 = 1.0D / Math.abs(d0);
            double d6 = 1.0D / Math.abs(d1);
            double d7 = 1.0D / Math.abs(d2);
            double d8 = (double)i - posVec31.xCoord;
            double d9 = (double)j - posVec31.yCoord;
            double d10 = (double)k - posVec31.zCoord;

            if (d0 >= 0.0D)
            {
                ++d8;
            }

            if (d1 >= 0.0D)
            {
                ++d9;
            }

            if (d2 >= 0.0D)
            {
                ++d10;
            }

            d8 = d8 / d0;
            d9 = d9 / d1;
            d10 = d10 / d2;
            int l = d0 < 0.0D ? -1 : 1;
            int i1 = d1 < 0.0D ? -1 : 1;
            int j1 = d2 < 0.0D ? -1 : 1;
            int k1 = MathHelper.floor(posVec32.xCoord);
            int l1 = MathHelper.floor(posVec32.yCoord);
            int i2 = MathHelper.floor(posVec32.zCoord);
            int j2 = k1 - i;
            int k2 = l1 - j;
            int l2 = i2 - k;

            while (j2 * l > 0 || k2 * i1 > 0 || l2 * j1 > 0)
            {
                if (d8 < d10 && d8 <= d9)
                {
                    d8 += d5;
                    i += l;
                    j2 = k1 - i;
                }
                else if (d9 < d8 && d9 <= d10)
                {
                    d9 += d6;
                    j += i1;
                    k2 = l1 - j;
                }
                else
                {
                    d10 += d7;
                    k += j1;
                    l2 = i2 - k;
                }
            }

            return true;
        }
    }

    public void func_192879_a(boolean p_192879_1_)
    {
        this.nodeProcessor.setCanBreakDoors(p_192879_1_);
    }

    public void func_192878_b(boolean p_192878_1_)
    {
        this.nodeProcessor.setCanEnterDoors(p_192878_1_);
    }

    public void func_192877_c(boolean p_192877_1_)
    {
        this.nodeProcessor.setCanSwim(p_192877_1_);
    }

    public boolean func_192880_g()
    {
        return this.nodeProcessor.getCanSwim();
    }

    public boolean canEntityStandOnPos(BlockPos pos)
    {
        return this.worldObj.getBlockState(pos).isFullyOpaque();
    }
}
