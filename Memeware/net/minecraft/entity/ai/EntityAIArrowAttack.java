package net.minecraft.entity.ai;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.util.MathHelper;

public class EntityAIArrowAttack extends EntityAIBase {
    /**
     * The entity the AI instance has been applied to
     */
    private final EntityLiving entityHost;

    /**
     * The entity (as a RangedAttackMob) the AI instance has been applied to.
     */
    private final IRangedAttackMob rangedAttackEntityHost;
    private EntityLivingBase attackTarget;

    /**
     * A decrementing tick that spawns a ranged attack once this value reaches 0. It is then set back to the
     * maxRangedAttackTime.
     */
    private int rangedAttackTime;
    private double entityMoveSpeed;
    private int field_75318_f;
    private int field_96561_g;

    /**
     * The maximum time the AI has to wait before peforming another ranged attack.
     */
    private int maxRangedAttackTime;
    private float field_96562_i;
    private float maxAttackDistance;
    private static final String __OBFID = "CL_00001609";

    public EntityAIArrowAttack(IRangedAttackMob p_i1649_1_, double p_i1649_2_, int p_i1649_4_, float p_i1649_5_) {
        this(p_i1649_1_, p_i1649_2_, p_i1649_4_, p_i1649_4_, p_i1649_5_);
    }

    public EntityAIArrowAttack(IRangedAttackMob p_i1650_1_, double p_i1650_2_, int p_i1650_4_, int p_i1650_5_, float p_i1650_6_) {
        this.rangedAttackTime = -1;

        if (!(p_i1650_1_ instanceof EntityLivingBase)) {
            throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
        } else {
            this.rangedAttackEntityHost = p_i1650_1_;
            this.entityHost = (EntityLiving) p_i1650_1_;
            this.entityMoveSpeed = p_i1650_2_;
            this.field_96561_g = p_i1650_4_;
            this.maxRangedAttackTime = p_i1650_5_;
            this.field_96562_i = p_i1650_6_;
            this.maxAttackDistance = p_i1650_6_ * p_i1650_6_;
            this.setMutexBits(3);
        }
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        EntityLivingBase var1 = this.entityHost.getAttackTarget();

        if (var1 == null) {
            return false;
        } else {
            this.attackTarget = var1;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting() {
        return this.shouldExecute() || !this.entityHost.getNavigator().noPath();
    }

    /**
     * Resets the task
     */
    public void resetTask() {
        this.attackTarget = null;
        this.field_75318_f = 0;
        this.rangedAttackTime = -1;
    }

    /**
     * Updates the task
     */
    public void updateTask() {
        double var1 = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.getEntityBoundingBox().minY, this.attackTarget.posZ);
        boolean var3 = this.entityHost.getEntitySenses().canSee(this.attackTarget);

        if (var3) {
            ++this.field_75318_f;
        } else {
            this.field_75318_f = 0;
        }

        if (var1 <= (double) this.maxAttackDistance && this.field_75318_f >= 20) {
            this.entityHost.getNavigator().clearPathEntity();
        } else {
            this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
        }

        this.entityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);
        float var4;

        if (--this.rangedAttackTime == 0) {
            if (var1 > (double) this.maxAttackDistance || !var3) {
                return;
            }

            var4 = MathHelper.sqrt_double(var1) / this.field_96562_i;
            float var5 = MathHelper.clamp_float(var4, 0.1F, 1.0F);
            this.rangedAttackEntityHost.attackEntityWithRangedAttack(this.attackTarget, var5);
            this.rangedAttackTime = MathHelper.floor_float(var4 * (float) (this.maxRangedAttackTime - this.field_96561_g) + (float) this.field_96561_g);
        } else if (this.rangedAttackTime < 0) {
            var4 = MathHelper.sqrt_double(var1) / this.field_96562_i;
            this.rangedAttackTime = MathHelper.floor_float(var4 * (float) (this.maxRangedAttackTime - this.field_96561_g) + (float) this.field_96561_g);
        }
    }
}
