// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;

public class EntityAIFollowParent extends EntityAIBase
{
    EntityAnimal childAnimal;
    EntityAnimal parentAnimal;
    double moveSpeed;
    private int delayCounter;
    
    public EntityAIFollowParent(final EntityAnimal animal, final double speed) {
        this.childAnimal = animal;
        this.moveSpeed = speed;
    }
    
    @Override
    public boolean shouldExecute() {
        if (this.childAnimal.getGrowingAge() >= 0) {
            return false;
        }
        final List<EntityAnimal> list = this.childAnimal.worldObj.getEntitiesWithinAABB(this.childAnimal.getClass(), this.childAnimal.getEntityBoundingBox().expand(8.0, 4.0, 8.0));
        EntityAnimal entityanimal = null;
        double d0 = Double.MAX_VALUE;
        for (final EntityAnimal entityanimal2 : list) {
            if (entityanimal2.getGrowingAge() >= 0) {
                final double d2 = this.childAnimal.getDistanceSqToEntity(entityanimal2);
                if (d2 > d0) {
                    continue;
                }
                d0 = d2;
                entityanimal = entityanimal2;
            }
        }
        if (entityanimal == null) {
            return false;
        }
        if (d0 < 9.0) {
            return false;
        }
        this.parentAnimal = entityanimal;
        return true;
    }
    
    @Override
    public boolean continueExecuting() {
        if (this.childAnimal.getGrowingAge() >= 0) {
            return false;
        }
        if (!this.parentAnimal.isEntityAlive()) {
            return false;
        }
        final double d0 = this.childAnimal.getDistanceSqToEntity(this.parentAnimal);
        return d0 >= 9.0 && d0 <= 256.0;
    }
    
    @Override
    public void startExecuting() {
        this.delayCounter = 0;
    }
    
    @Override
    public void resetTask() {
        this.parentAnimal = null;
    }
    
    @Override
    public void updateTask() {
        final int delayCounter = this.delayCounter - 1;
        this.delayCounter = delayCounter;
        if (delayCounter <= 0) {
            this.delayCounter = 10;
            this.childAnimal.getNavigator().tryMoveToEntityLiving(this.parentAnimal, this.moveSpeed);
        }
    }
}
