package net.minecraft.pathfinding;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.pathfinder.SwimNodeProcessor;

public class PathNavigateSwimmer extends PathNavigate
{

    public PathNavigateSwimmer(EntityLiving entitylivingIn, World worldIn)
    {
        super(entitylivingIn, worldIn);
    }

    @Override
	protected PathFinder getPathFinder()
    {
        return new PathFinder(new SwimNodeProcessor());
    }

    /**
     * If on ground or swimming and can swim
     */
    @Override
	protected boolean canNavigate()
    {
        return this.isInLiquid();
    }

    @Override
	protected Vec3 getEntityPosition()
    {
        return new Vec3(this.theEntity.posX, this.theEntity.posY + this.theEntity.height * 0.5D, this.theEntity.posZ);
    }

    @Override
	protected void pathFollow()
    {
        Vec3 var1 = this.getEntityPosition();
        float var2 = this.theEntity.width * this.theEntity.width;
        byte var3 = 6;

        if (var1.squareDistanceTo(this.currentPath.getVectorFromIndex(this.theEntity, this.currentPath.getCurrentPathIndex())) < var2)
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

        this.checkForStuck(var1);
    }

    /**
     * Trims path data from the end to the first sun covered block
     */
    @Override
	protected void removeSunnyPath()
    {
        super.removeSunnyPath();
    }

    /**
     * Returns true when an entity of specified size could safely walk in a straight line between the two points. Args:
     * pos1, pos2, entityXSize, entityYSize, entityZSize
     */
    @Override
	protected boolean isDirectPathBetweenPoints(Vec3 posVec31, Vec3 posVec32, int sizeX, int sizeY, int sizeZ)
    {
        MovingObjectPosition var6 = this.worldObj.rayTraceBlocks(posVec31, new Vec3(posVec32.xCoord, posVec32.yCoord + this.theEntity.height * 0.5D, posVec32.zCoord), false, true, false);
        return var6 == null || var6.typeOfHit == MovingObjectPosition.MovingObjectType.MISS;
    }
}
