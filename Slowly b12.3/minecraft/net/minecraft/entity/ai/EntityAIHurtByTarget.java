package net.minecraft.entity.ai;

import java.util.Iterator;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.AxisAlignedBB;

public class EntityAIHurtByTarget extends EntityAITarget
{
    private boolean entityCallsForHelp;

    /** Store the previous revengeTimer value */
    private int revengeTimerOld;
    private final Class[] targetClasses;

    public EntityAIHurtByTarget(EntityCreature creatureIn, boolean entityCallsForHelpIn, Class... targetClassesIn)
    {
        super(creatureIn, false);
        this.entityCallsForHelp = entityCallsForHelpIn;
        this.targetClasses = targetClassesIn;
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        int i = this.taskOwner.getRevengeTimer();
        return i != this.revengeTimerOld && this.isSuitableTarget(this.taskOwner.getAITarget(), false);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.taskOwner.setAttackTarget(this.taskOwner.getAITarget());
        this.revengeTimerOld = this.taskOwner.getRevengeTimer();
        if (this.entityCallsForHelp) {
           double d0 = this.getTargetDistance();
           Iterator var4 = this.taskOwner.worldObj.getEntitiesWithinAABB(this.taskOwner.getClass(), (new AxisAlignedBB(this.taskOwner.posX, this.taskOwner.posY, this.taskOwner.posZ, this.taskOwner.posX + 1.0D, this.taskOwner.posY + 1.0D, this.taskOwner.posZ + 1.0D)).expand(d0, 10.0D, d0)).iterator();

           label42:
           while(true) {
              EntityCreature entitycreature;
              do {
                 do {
                    do {
                       if (!var4.hasNext()) {
                          break label42;
                       }

                       entitycreature = (EntityCreature)var4.next();
                    } while(this.taskOwner == entitycreature);
                 } while(entitycreature.getAttackTarget() != null);
              } while(entitycreature.isOnSameTeam(this.taskOwner.getAITarget()));

              boolean flag = false;
              Class[] var9 = this.targetClasses;
              int var8 = this.targetClasses.length;

              for(int var7 = 0; var7 < var8; ++var7) {
                 Class oclass = var9[var7];
                 if (entitycreature.getClass() == oclass) {
                    flag = true;
                    break;
                 }
              }

              if (!flag) {
                 this.setEntityAttackTarget(entitycreature, this.taskOwner.getAITarget());
              }
           }
        }
    }
    protected void setEntityAttackTarget(EntityCreature creatureIn, EntityLivingBase entityLivingBaseIn)
    {
        creatureIn.setAttackTarget(entityLivingBaseIn);
    }
}
