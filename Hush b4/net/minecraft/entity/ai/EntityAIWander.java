// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.util.Vec3;
import net.minecraft.entity.EntityCreature;

public class EntityAIWander extends EntityAIBase
{
    private EntityCreature entity;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private double speed;
    private int executionChance;
    private boolean mustUpdate;
    
    public EntityAIWander(final EntityCreature creatureIn, final double speedIn) {
        this(creatureIn, speedIn, 120);
    }
    
    public EntityAIWander(final EntityCreature creatureIn, final double speedIn, final int chance) {
        this.entity = creatureIn;
        this.speed = speedIn;
        this.executionChance = chance;
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        if (!this.mustUpdate) {
            if (this.entity.getAge() >= 100) {
                return false;
            }
            if (this.entity.getRNG().nextInt(this.executionChance) != 0) {
                return false;
            }
        }
        final Vec3 vec3 = RandomPositionGenerator.findRandomTarget(this.entity, 10, 7);
        if (vec3 == null) {
            return false;
        }
        this.xPosition = vec3.xCoord;
        this.yPosition = vec3.yCoord;
        this.zPosition = vec3.zCoord;
        this.mustUpdate = false;
        return true;
    }
    
    @Override
    public boolean continueExecuting() {
        return !this.entity.getNavigator().noPath();
    }
    
    @Override
    public void startExecuting() {
        this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
    }
    
    public void makeUpdate() {
        this.mustUpdate = true;
    }
    
    public void setExecutionChance(final int newchance) {
        this.executionChance = newchance;
    }
}
