package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigateGround;

public class EntityAISwimming extends EntityAIBase
{
    private final EntityLiving theEntity;

    public EntityAISwimming(EntityLiving entitylivingIn)
    {
        this.theEntity = entitylivingIn;
        this.setMutexBits(4);

        if (entitylivingIn.getNavigator() instanceof PathNavigateGround)
        {
            ((PathNavigateGround)entitylivingIn.getNavigator()).setCanSwim(true);
        }
        else if (entitylivingIn.getNavigator() instanceof PathNavigateFlying)
        {
            ((PathNavigateFlying)entitylivingIn.getNavigator()).func_192877_c(true);
        }
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        return this.theEntity.isInWater() || this.theEntity.isInLava();
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        if (this.theEntity.getRNG().nextFloat() < 0.8F)
        {
            this.theEntity.getJumpHelper().setJumping();
        }
    }
}
