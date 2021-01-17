// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.entity.EntityCreature;

public class EntityAIRestrictSun extends EntityAIBase
{
    private EntityCreature theEntity;
    
    public EntityAIRestrictSun(final EntityCreature p_i1652_1_) {
        this.theEntity = p_i1652_1_;
    }
    
    @Override
    public boolean shouldExecute() {
        return this.theEntity.worldObj.isDaytime();
    }
    
    @Override
    public void startExecuting() {
        ((PathNavigateGround)this.theEntity.getNavigator()).setAvoidSun(true);
    }
    
    @Override
    public void resetTask() {
        ((PathNavigateGround)this.theEntity.getNavigator()).setAvoidSun(false);
    }
}
