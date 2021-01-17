// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAIOwnerHurtByTarget extends EntityAITarget
{
    EntityTameable theDefendingTameable;
    EntityLivingBase theOwnerAttacker;
    private int field_142051_e;
    
    public EntityAIOwnerHurtByTarget(final EntityTameable theDefendingTameableIn) {
        super(theDefendingTameableIn, false);
        this.theDefendingTameable = theDefendingTameableIn;
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        if (!this.theDefendingTameable.isTamed()) {
            return false;
        }
        final EntityLivingBase entitylivingbase = this.theDefendingTameable.getOwner();
        if (entitylivingbase == null) {
            return false;
        }
        this.theOwnerAttacker = entitylivingbase.getAITarget();
        final int i = entitylivingbase.getRevengeTimer();
        return i != this.field_142051_e && this.isSuitableTarget(this.theOwnerAttacker, false) && this.theDefendingTameable.shouldAttackEntity(this.theOwnerAttacker, entitylivingbase);
    }
    
    @Override
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.theOwnerAttacker);
        final EntityLivingBase entitylivingbase = this.theDefendingTameable.getOwner();
        if (entitylivingbase != null) {
            this.field_142051_e = entitylivingbase.getRevengeTimer();
        }
        super.startExecuting();
    }
}
