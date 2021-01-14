package net.minecraft.pathfinding;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.pathfinder.SwimNodeProcessor;

public class PathNavigateSwimmer extends PathNavigate {
    private static final String __OBFID = "CL_00002244";

    public PathNavigateSwimmer(EntityLiving p_i45873_1_, World worldIn) {
        super(p_i45873_1_, worldIn);
    }

    @Override
    protected PathFinder func_179679_a() {
        return new PathFinder(new SwimNodeProcessor());
    }

    /**
     * If on ground or swimming and can swim
     */
    @Override
    protected boolean canNavigate() {
        return isInLiquid();
    }

    @Override
    protected Vec3 getEntityPosition() {
        return new Vec3(theEntity.posX, theEntity.posY + theEntity.height * 0.5D, theEntity.posZ);
    }

    @Override
    protected void pathFollow() {
        Vec3 var1 = getEntityPosition();
        float var2 = theEntity.width * theEntity.width;
        byte var3 = 6;

        if (var1.squareDistanceTo(currentPath.getVectorFromIndex(theEntity, currentPath.getCurrentPathIndex())) < var2) {
            currentPath.incrementPathIndex();
        }

        for (int var4 = Math.min(currentPath.getCurrentPathIndex() + var3, currentPath.getCurrentPathLength() - 1); var4 > currentPath.getCurrentPathIndex(); --var4) {
            Vec3 var5 = currentPath.getVectorFromIndex(theEntity, var4);

            if (var5.squareDistanceTo(var1) <= 36.0D && isDirectPathBetweenPoints(var1, var5, 0, 0, 0)) {
                currentPath.setCurrentPathIndex(var4);
                break;
            }
        }

        func_179677_a(var1);
    }

    /**
     * Trims path data from the end to the first sun covered block
     */
    @Override
    protected void removeSunnyPath() {
        super.removeSunnyPath();
    }

    /**
     * Returns true when an entity of specified size could safely walk in a
     * straight line between the two points. Args: pos1, pos2, entityXSize,
     * entityYSize, entityZSize
     */
    @Override
    protected boolean isDirectPathBetweenPoints(Vec3 p_75493_1_, Vec3 p_75493_2_, int p_75493_3_, int p_75493_4_, int p_75493_5_) {
        MovingObjectPosition var6 = worldObj.rayTraceBlocks(p_75493_1_, new Vec3(p_75493_2_.xCoord, p_75493_2_.yCoord + theEntity.height * 0.5D, p_75493_2_.zCoord), false, true, false);
        return var6 == null || var6.typeOfHit == MovingObjectPosition.MovingObjectType.MISS;
    }
}
