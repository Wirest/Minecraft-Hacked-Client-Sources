// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.util.MathHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

public class EntityLookHelper
{
    private EntityLiving entity;
    private float deltaLookYaw;
    private float deltaLookPitch;
    private boolean isLooking;
    private double posX;
    private double posY;
    private double posZ;
    
    public EntityLookHelper(final EntityLiving entitylivingIn) {
        this.entity = entitylivingIn;
    }
    
    public void setLookPositionWithEntity(final Entity entityIn, final float deltaYaw, final float deltaPitch) {
        this.posX = entityIn.posX;
        if (entityIn instanceof EntityLivingBase) {
            this.posY = entityIn.posY + entityIn.getEyeHeight();
        }
        else {
            this.posY = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0;
        }
        this.posZ = entityIn.posZ;
        this.deltaLookYaw = deltaYaw;
        this.deltaLookPitch = deltaPitch;
        this.isLooking = true;
    }
    
    public void setLookPosition(final double x, final double y, final double z, final float deltaYaw, final float deltaPitch) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.deltaLookYaw = deltaYaw;
        this.deltaLookPitch = deltaPitch;
        this.isLooking = true;
    }
    
    public void onUpdateLook() {
        this.entity.rotationPitch = 0.0f;
        if (this.isLooking) {
            this.isLooking = false;
            final double d0 = this.posX - this.entity.posX;
            final double d2 = this.posY - (this.entity.posY + this.entity.getEyeHeight());
            final double d3 = this.posZ - this.entity.posZ;
            final double d4 = MathHelper.sqrt_double(d0 * d0 + d3 * d3);
            final float f = (float)(MathHelper.func_181159_b(d3, d0) * 180.0 / 3.141592653589793) - 90.0f;
            final float f2 = (float)(-(MathHelper.func_181159_b(d2, d4) * 180.0 / 3.141592653589793));
            this.entity.rotationPitch = this.updateRotation(this.entity.rotationPitch, f2, this.deltaLookPitch);
            this.entity.rotationYawHead = this.updateRotation(this.entity.rotationYawHead, f, this.deltaLookYaw);
        }
        else {
            this.entity.rotationYawHead = this.updateRotation(this.entity.rotationYawHead, this.entity.renderYawOffset, 10.0f);
        }
        final float f3 = MathHelper.wrapAngleTo180_float(this.entity.rotationYawHead - this.entity.renderYawOffset);
        if (!this.entity.getNavigator().noPath()) {
            if (f3 < -75.0f) {
                this.entity.rotationYawHead = this.entity.renderYawOffset - 75.0f;
            }
            if (f3 > 75.0f) {
                this.entity.rotationYawHead = this.entity.renderYawOffset + 75.0f;
            }
        }
    }
    
    private float updateRotation(final float p_75652_1_, final float p_75652_2_, final float p_75652_3_) {
        float f = MathHelper.wrapAngleTo180_float(p_75652_2_ - p_75652_1_);
        if (f > p_75652_3_) {
            f = p_75652_3_;
        }
        if (f < -p_75652_3_) {
            f = -p_75652_3_;
        }
        return p_75652_1_ + f;
    }
    
    public boolean getIsLooking() {
        return this.isLooking;
    }
    
    public double getLookPosX() {
        return this.posX;
    }
    
    public double getLookPosY() {
        return this.posY;
    }
    
    public double getLookPosZ() {
        return this.posZ;
    }
}
