package net.minecraft.pathfinding;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.pathfinder.SwimNodeProcessor;

public class PathNavigateSwimmer extends PathNavigate
{
    private static final String __OBFID = "CL_00002244";

    public PathNavigateSwimmer(EntityLiving p_i45873_1_, World worldIn)
    {
        super(p_i45873_1_, worldIn);
    }

    protected PathFinder func_179679_a()
    {
        return new PathFinder(new SwimNodeProcessor());
    }

    /**
     * If on ground or swimming and can swim
     */
    protected boolean canNavigate()
    {
        return this.isInLiquid();
    }

    protected Vec3 getEntityPosition()
    {
        return new Vec3(this.theEntity.posX, this.theEntity.posY + (double)this.theEntity.height * 0.5D, this.theEntity.posZ);
    }

    protected void pathFollow()
    {
        Vec3 var1 = this.getEntityPosition();
        float var2 = this.theEntity.width * this.theEntity.width;
        byte var3 = 6;

        if (var1.squareDistanceTo(this.currentPath.getVectorFromIndex(this.theEntity, this.currentPath.getCurrentPathIndex())) < (double)var2)
        {
            this.currentPath.incrementPathIndex();
        }

        for (int var4 = Math.min(this.currentPath.getCurrentPathIndex() + var3, this.currentPath.getCurrentPathLength() - 1); var4 > this.currentPath.getCurrentPathIndex(); --var4)
        {
            Vec3 var5 = this.currentPath.getVectorFromIndex(this.theEntity, var4);

            if (var5.squareDistanceTo(var1) <= 36.0D && this.isDirectPathBetweenPoints(var1, var5, 0, 0, 0))
            {
                this.currentPath.setCurrentPathIndex(var4);
                break;
            }
        }

        this.func_179677_a(var1);
    }

    /**
     * Trims path data from the end to the first sun covered block
     */
    protected void removeSunnyPath()
    {
        super.removeSunnyPath();
    }

    /**
     * Returns true when an entity of specified size could safely walk in a straight line between the two points. Args:
     * pos1, pos2, entityXSize, entityYSize, entityZSize
     */
    protected boolean isDirectPathBetweenPoints(Vec3 p_75493_1_, Vec3 p_75493_2_, int p_75493_3_, int p_75493_4_, int p_75493_5_)
    {
        MovingObjectPosition var6 = this.worldObj.rayTraceBlocks(p_75493_1_, new Vec3(p_75493_2_.xCoord, p_75493_2_.yCoord + (double)this.theEntity.height * 0.5D, p_75493_2_.zCoord), false, true, false);
        return var6 == null || var6.typeOfHit == MovingObjectPosition.MovingObjectType.MISS;
    }
}
