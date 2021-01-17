// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

public class EntityAIWatchClosest extends EntityAIBase
{
    protected EntityLiving theWatcher;
    protected Entity closestEntity;
    protected float maxDistanceForPlayer;
    private int lookTime;
    private float chance;
    protected Class<? extends Entity> watchedClass;
    
    public EntityAIWatchClosest(final EntityLiving entitylivingIn, final Class<? extends Entity> watchTargetClass, final float maxDistance) {
        this.theWatcher = entitylivingIn;
        this.watchedClass = watchTargetClass;
        this.maxDistanceForPlayer = maxDistance;
        this.chance = 0.02f;
        this.setMutexBits(2);
    }
    
    public EntityAIWatchClosest(final EntityLiving entitylivingIn, final Class<? extends Entity> watchTargetClass, final float maxDistance, final float chanceIn) {
        this.theWatcher = entitylivingIn;
        this.watchedClass = watchTargetClass;
        this.maxDistanceForPlayer = maxDistance;
        this.chance = chanceIn;
        this.setMutexBits(2);
    }
    
    @Override
    public boolean shouldExecute() {
        if (this.theWatcher.getRNG().nextFloat() >= this.chance) {
            return false;
        }
        if (this.theWatcher.getAttackTarget() != null) {
            this.closestEntity = this.theWatcher.getAttackTarget();
        }
        if (this.watchedClass == EntityPlayer.class) {
            this.closestEntity = this.theWatcher.worldObj.getClosestPlayerToEntity(this.theWatcher, this.maxDistanceForPlayer);
        }
        else {
            this.closestEntity = this.theWatcher.worldObj.findNearestEntityWithinAABB((Class<? extends EntityLiving>)this.watchedClass, this.theWatcher.getEntityBoundingBox().expand(this.maxDistanceForPlayer, 3.0, this.maxDistanceForPlayer), this.theWatcher);
        }
        return this.closestEntity != null;
    }
    
    @Override
    public boolean continueExecuting() {
        return this.closestEntity.isEntityAlive() && this.theWatcher.getDistanceSqToEntity(this.closestEntity) <= this.maxDistanceForPlayer * this.maxDistanceForPlayer && this.lookTime > 0;
    }
    
    @Override
    public void startExecuting() {
        this.lookTime = 40 + this.theWatcher.getRNG().nextInt(40);
    }
    
    @Override
    public void resetTask() {
        this.closestEntity = null;
    }
    
    @Override
    public void updateTask() {
        this.theWatcher.getLookHelper().setLookPosition(this.closestEntity.posX, this.closestEntity.posY + this.closestEntity.getEyeHeight(), this.closestEntity.posZ, 10.0f, (float)this.theWatcher.getVerticalFaceSpeed());
        --this.lookTime;
    }
}
