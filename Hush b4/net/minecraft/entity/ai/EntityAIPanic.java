// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.util.Vec3;
import net.minecraft.entity.EntityCreature;

public class EntityAIPanic extends EntityAIBase
{
    private EntityCreature theEntityCreature;
    protected double speed;
    private double randPosX;
    private double randPosY;
    private double randPosZ;
    
    public EntityAIPanic(final EntityCreature creature, final double speedIn) {
        this.theEntityCreature = creature;
        this.speed = speedIn;
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        if (this.theEntityCreature.getAITarget() == null && !this.theEntityCreature.isBurning()) {
            return false;
        }
        final Vec3 vec3 = RandomPositionGenerator.findRandomTarget(this.theEntityCreature, 5, 4);
        if (vec3 == null) {
            return false;
        }
        this.randPosX = vec3.xCoord;
        this.randPosY = vec3.yCoord;
        this.randPosZ = vec3.zCoord;
        return true;
    }
    
    @Override
    public void startExecuting() {
        this.theEntityCreature.getNavigator().tryMoveToXYZ(this.randPosX, this.randPosY, this.randPosZ, this.speed);
    }
    
    @Override
    public boolean continueExecuting() {
        return !this.theEntityCreature.getNavigator().noPath();
    }
}
