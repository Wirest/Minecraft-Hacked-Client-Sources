package net.minecraft.entity.ai;

import com.google.common.base.Predicates;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntitySelectors;

public class EntityAIWatchClosest extends EntityAIBase
{
    protected EntityLiving theWatcher;

    /** The closest entity which is being watched by this one. */
    protected Entity closestEntity;

    /** This is the Maximum distance that the AI will look for the Entity */
    protected float maxDistanceForPlayer;
    private int lookTime;
    private final float chance;
    protected Class <? extends Entity > watchedClass;

    public EntityAIWatchClosest(EntityLiving entitylivingIn, Class <? extends Entity > watchTargetClass, float maxDistance)
    {
        this.theWatcher = entitylivingIn;
        this.watchedClass = watchTargetClass;
        this.maxDistanceForPlayer = maxDistance;
        this.chance = 0.02F;
        this.setMutexBits(2);
    }

    public EntityAIWatchClosest(EntityLiving entitylivingIn, Class <? extends Entity > watchTargetClass, float maxDistance, float chanceIn)
    {
        this.theWatcher = entitylivingIn;
        this.watchedClass = watchTargetClass;
        this.maxDistanceForPlayer = maxDistance;
        this.chance = chanceIn;
        this.setMutexBits(2);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (this.theWatcher.getRNG().nextFloat() >= this.chance)
        {
            return false;
        }
        else
        {
            if (this.theWatcher.getAttackTarget() != null)
            {
                this.closestEntity = this.theWatcher.getAttackTarget();
            }

            if (this.watchedClass == EntityPlayer.class)
            {
                this.closestEntity = this.theWatcher.world.func_190525_a(this.theWatcher.posX, this.theWatcher.posY, this.theWatcher.posZ, (double)this.maxDistanceForPlayer, Predicates.and(EntitySelectors.NOT_SPECTATING, EntitySelectors.func_191324_b(this.theWatcher)));
            }
            else
            {
                this.closestEntity = this.theWatcher.world.findNearestEntityWithinAABB(this.watchedClass, this.theWatcher.getEntityBoundingBox().expand((double)this.maxDistanceForPlayer, 3.0D, (double)this.maxDistanceForPlayer), this.theWatcher);
            }

            return this.closestEntity != null;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        if (!this.closestEntity.isEntityAlive())
        {
            return false;
        }
        else if (this.theWatcher.getDistanceSqToEntity(this.closestEntity) > (double)(this.maxDistanceForPlayer * this.maxDistanceForPlayer))
        {
            return false;
        }
        else
        {
            return this.lookTime > 0;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.lookTime = 40 + this.theWatcher.getRNG().nextInt(40);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.closestEntity = null;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        this.theWatcher.getLookHelper().setLookPosition(this.closestEntity.posX, this.closestEntity.posY + (double)this.closestEntity.getEyeHeight(), this.closestEntity.posZ, (float)this.theWatcher.getHorizontalFaceSpeed(), (float)this.theWatcher.getVerticalFaceSpeed());
        --this.lookTime;
    }
}
