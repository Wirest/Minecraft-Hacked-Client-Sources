// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.pathfinding;

import net.minecraft.util.MovingObjectPosition;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import net.minecraft.world.pathfinder.NodeProcessor;
import net.minecraft.world.pathfinder.SwimNodeProcessor;
import net.minecraft.world.World;
import net.minecraft.entity.EntityLiving;

public class PathNavigateSwimmer extends PathNavigate
{
    public PathNavigateSwimmer(final EntityLiving entitylivingIn, final World worldIn) {
        super(entitylivingIn, worldIn);
    }
    
    @Override
    protected PathFinder getPathFinder() {
        return new PathFinder(new SwimNodeProcessor());
    }
    
    @Override
    protected boolean canNavigate() {
        return this.isInLiquid();
    }
    
    @Override
    protected Vec3 getEntityPosition() {
        return new Vec3(this.theEntity.posX, this.theEntity.posY + this.theEntity.height * 0.5, this.theEntity.posZ);
    }
    
    @Override
    protected void pathFollow() {
        final Vec3 vec3 = this.getEntityPosition();
        final float f = this.theEntity.width * this.theEntity.width;
        final int i = 6;
        if (vec3.squareDistanceTo(this.currentPath.getVectorFromIndex(this.theEntity, this.currentPath.getCurrentPathIndex())) < f) {
            this.currentPath.incrementPathIndex();
        }
        for (int j = Math.min(this.currentPath.getCurrentPathIndex() + i, this.currentPath.getCurrentPathLength() - 1); j > this.currentPath.getCurrentPathIndex(); --j) {
            final Vec3 vec4 = this.currentPath.getVectorFromIndex(this.theEntity, j);
            if (vec4.squareDistanceTo(vec3) <= 36.0 && this.isDirectPathBetweenPoints(vec3, vec4, 0, 0, 0)) {
                this.currentPath.setCurrentPathIndex(j);
                break;
            }
        }
        this.checkForStuck(vec3);
    }
    
    @Override
    protected void removeSunnyPath() {
        super.removeSunnyPath();
    }
    
    @Override
    protected boolean isDirectPathBetweenPoints(final Vec3 posVec31, final Vec3 posVec32, final int sizeX, final int sizeY, final int sizeZ) {
        final MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(posVec31, new Vec3(posVec32.xCoord, posVec32.yCoord + this.theEntity.height * 0.5, posVec32.zCoord), false, true, false);
        return movingobjectposition == null || movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.MISS;
    }
}
