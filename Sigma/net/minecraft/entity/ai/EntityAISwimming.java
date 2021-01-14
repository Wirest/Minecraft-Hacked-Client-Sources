package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathNavigateGround;

public class EntityAISwimming extends EntityAIBase {
    private EntityLiving theEntity;
    private static final String __OBFID = "CL_00001584";

    public EntityAISwimming(EntityLiving p_i1624_1_) {
        this.theEntity = p_i1624_1_;
        this.setMutexBits(4);
        ((PathNavigateGround) p_i1624_1_.getNavigator()).func_179693_d(true);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        return this.theEntity.isInWater() || this.theEntity.func_180799_ab();
    }

    /**
     * Updates the task
     */
    public void updateTask() {
        if (this.theEntity.getRNG().nextFloat() < 0.8F) {
            this.theEntity.getJumpHelper().setJumping();
        }
    }
}
