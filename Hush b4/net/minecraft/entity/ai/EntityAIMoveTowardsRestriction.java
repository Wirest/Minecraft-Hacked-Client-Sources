// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.entity.EntityCreature;

public class EntityAIMoveTowardsRestriction extends EntityAIBase
{
    private EntityCreature theEntity;
    private double movePosX;
    private double movePosY;
    private double movePosZ;
    private double movementSpeed;
    
    public EntityAIMoveTowardsRestriction(final EntityCreature creatureIn, final double speedIn) {
        this.theEntity = creatureIn;
        this.movementSpeed = speedIn;
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        if (this.theEntity.isWithinHomeDistanceCurrentPosition()) {
            return false;
        }
        final BlockPos blockpos = this.theEntity.getHomePosition();
        final Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.theEntity, 16, 7, new Vec3(blockpos.getX(), blockpos.getY(), blockpos.getZ()));
        if (vec3 == null) {
            return false;
        }
        this.movePosX = vec3.xCoord;
        this.movePosY = vec3.yCoord;
        this.movePosZ = vec3.zCoord;
        return true;
    }
    
    @Override
    public boolean continueExecuting() {
        return !this.theEntity.getNavigator().noPath();
    }
    
    @Override
    public void startExecuting() {
        this.theEntity.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.movementSpeed);
    }
}
