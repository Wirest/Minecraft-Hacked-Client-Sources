// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import java.util.Iterator;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.EntityCreature;

public class EntityAIHurtByTarget extends EntityAITarget
{
    private boolean entityCallsForHelp;
    private int revengeTimerOld;
    private final Class[] targetClasses;
    
    public EntityAIHurtByTarget(final EntityCreature creatureIn, final boolean entityCallsForHelpIn, final Class... targetClassesIn) {
        super(creatureIn, false);
        this.entityCallsForHelp = entityCallsForHelpIn;
        this.targetClasses = targetClassesIn;
        this.setMutexBits(1);
    }
    
    @Override
    public boolean shouldExecute() {
        final int i = this.taskOwner.getRevengeTimer();
        return i != this.revengeTimerOld && this.isSuitableTarget(this.taskOwner.getAITarget(), false);
    }
    
    @Override
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.taskOwner.getAITarget());
        this.revengeTimerOld = this.taskOwner.getRevengeTimer();
        if (this.entityCallsForHelp) {
            final double d0 = this.getTargetDistance();
            for (final EntityCreature entitycreature : this.taskOwner.worldObj.getEntitiesWithinAABB(this.taskOwner.getClass(), new AxisAlignedBB(this.taskOwner.posX, this.taskOwner.posY, this.taskOwner.posZ, this.taskOwner.posX + 1.0, this.taskOwner.posY + 1.0, this.taskOwner.posZ + 1.0).expand(d0, 10.0, d0))) {
                if (this.taskOwner != entitycreature && entitycreature.getAttackTarget() == null && !entitycreature.isOnSameTeam(this.taskOwner.getAITarget())) {
                    boolean flag = false;
                    Class[] targetClasses;
                    for (int length = (targetClasses = this.targetClasses).length, i = 0; i < length; ++i) {
                        final Class oclass = targetClasses[i];
                        if (entitycreature.getClass() == oclass) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) {
                        continue;
                    }
                    this.setEntityAttackTarget(entitycreature, this.taskOwner.getAITarget());
                }
            }
        }
        super.startExecuting();
    }
    
    protected void setEntityAttackTarget(final EntityCreature creatureIn, final EntityLivingBase entityLivingBaseIn) {
        creatureIn.setAttackTarget(entityLivingBaseIn);
    }
}
