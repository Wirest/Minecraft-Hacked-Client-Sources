// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.util.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityLiving;

public class EntityAILeapAtTarget extends EntityAIBase
{
    EntityLiving leaper;
    EntityLivingBase leapTarget;
    float leapMotionY;
    
    public EntityAILeapAtTarget(final EntityLiving leapingEntity, final float leapMotionYIn) {
        this.leaper = leapingEntity;
        this.leapMotionY = leapMotionYIn;
        this.setMutexBits(5);
    }
    
    @Override
    public boolean shouldExecute() {
        this.leapTarget = this.leaper.getAttackTarget();
        if (this.leapTarget == null) {
            return false;
        }
        final double d0 = this.leaper.getDistanceSqToEntity(this.leapTarget);
        return d0 >= 4.0 && d0 <= 16.0 && this.leaper.onGround && this.leaper.getRNG().nextInt(5) == 0;
    }
    
    @Override
    public boolean continueExecuting() {
        return !this.leaper.onGround;
    }
    
    @Override
    public void startExecuting() {
        final double d0 = this.leapTarget.posX - this.leaper.posX;
        final double d2 = this.leapTarget.posZ - this.leaper.posZ;
        final float f = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
        final EntityLiving leaper = this.leaper;
        leaper.motionX += d0 / f * 0.5 * 0.800000011920929 + this.leaper.motionX * 0.20000000298023224;
        final EntityLiving leaper2 = this.leaper;
        leaper2.motionZ += d2 / f * 0.5 * 0.800000011920929 + this.leaper.motionZ * 0.20000000298023224;
        this.leaper.motionY = this.leapMotionY;
    }
}
