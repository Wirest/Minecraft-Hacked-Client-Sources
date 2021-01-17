// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;

public class EntityAIOwnerHurtTarget extends EntityAITarget
{
    EntityTameable theEntityTameable;
    EntityLivingBase theTarget;
    private int field_142050_e;
    
    public EntityAIOwnerHurtTarget(final EntityTameable theEntityTameableIn) {
        super(theEntityTameableIn, false);
        this.theEntityTameable = theEntityTameableIn;
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        if (!this.theEntityTameable.isTamed()) {
            return false;
        }
        final EntityLivingBase entitylivingbase = this.theEntityTameable.getOwner();
        if (entitylivingbase == null) {
            return false;
        }
        this.theTarget = entitylivingbase.getLastAttacker();
        final int i = entitylivingbase.getLastAttackerTime();
        return i != this.field_142050_e && this.isSuitableTarget(this.theTarget, false) && this.theEntityTameable.shouldAttackEntity(this.theTarget, entitylivingbase);
    }
    
    @Override
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.theTarget);
        final EntityLivingBase entitylivingbase = this.theEntityTameable.getOwner();
        if (entitylivingbase != null) {
            this.field_142050_e = entitylivingbase.getLastAttackerTime();
        }
        super.startExecuting();
    }
}
