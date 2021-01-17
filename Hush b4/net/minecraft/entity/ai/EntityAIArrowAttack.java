// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.EntityLiving;

public class EntityAIArrowAttack extends EntityAIBase
{
    private final EntityLiving entityHost;
    private final IRangedAttackMob rangedAttackEntityHost;
    private EntityLivingBase attackTarget;
    private int rangedAttackTime;
    private double entityMoveSpeed;
    private int field_75318_f;
    private int field_96561_g;
    private int maxRangedAttackTime;
    private float field_96562_i;
    private float maxAttackDistance;
    
    public EntityAIArrowAttack(final IRangedAttackMob attacker, final double movespeed, final int p_i1649_4_, final float p_i1649_5_) {
        this(attacker, movespeed, p_i1649_4_, p_i1649_4_, p_i1649_5_);
    }
    
    public EntityAIArrowAttack(final IRangedAttackMob attacker, final double movespeed, final int p_i1650_4_, final int maxAttackTime, final float maxAttackDistanceIn) {
        this.rangedAttackTime = -1;
        if (!(attacker instanceof EntityLivingBase)) {
            throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
        }
        this.rangedAttackEntityHost = attacker;
        this.entityHost = (EntityLiving)attacker;
        this.entityMoveSpeed = movespeed;
        this.field_96561_g = p_i1650_4_;
        this.maxRangedAttackTime = maxAttackTime;
        this.field_96562_i = maxAttackDistanceIn;
        this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
        this.setMutexBits(3);
    }
    
    @Override
    public boolean shouldExecute() {
        final EntityLivingBase entitylivingbase = this.entityHost.getAttackTarget();
        if (entitylivingbase == null) {
            return false;
        }
        this.attackTarget = entitylivingbase;
        return true;
    }
    
    @Override
    public boolean continueExecuting() {
        return this.shouldExecute() || !this.entityHost.getNavigator().noPath();
    }
    
    @Override
    public void resetTask() {
        this.attackTarget = null;
        this.field_75318_f = 0;
        this.rangedAttackTime = -1;
    }
    
    @Override
    public void updateTask() {
        final double d0 = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.getEntityBoundingBox().minY, this.attackTarget.posZ);
        final boolean flag = this.entityHost.getEntitySenses().canSee(this.attackTarget);
        if (flag) {
            ++this.field_75318_f;
        }
        else {
            this.field_75318_f = 0;
        }
        if (d0 <= this.maxAttackDistance && this.field_75318_f >= 20) {
            this.entityHost.getNavigator().clearPathEntity();
        }
        else {
            this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
        }
        this.entityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0f, 30.0f);
        final int rangedAttackTime = this.rangedAttackTime - 1;
        this.rangedAttackTime = rangedAttackTime;
        if (rangedAttackTime == 0) {
            if (d0 > this.maxAttackDistance || !flag) {
                return;
            }
            final float f = MathHelper.sqrt_double(d0) / this.field_96562_i;
            final float lvt_5_1_ = MathHelper.clamp_float(f, 0.1f, 1.0f);
            this.rangedAttackEntityHost.attackEntityWithRangedAttack(this.attackTarget, lvt_5_1_);
            this.rangedAttackTime = MathHelper.floor_float(f * (this.maxRangedAttackTime - this.field_96561_g) + this.field_96561_g);
        }
        else if (this.rangedAttackTime < 0) {
            final float f2 = MathHelper.sqrt_double(d0) / this.field_96562_i;
            this.rangedAttackTime = MathHelper.floor_float(f2 * (this.maxRangedAttackTime - this.field_96561_g) + this.field_96561_g);
        }
    }
}
