// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.EntityLiving;

public class EntityMoveHelper
{
    protected EntityLiving entity;
    protected double posX;
    protected double posY;
    protected double posZ;
    protected double speed;
    protected boolean update;
    
    public EntityMoveHelper(final EntityLiving entitylivingIn) {
        this.entity = entitylivingIn;
        this.posX = entitylivingIn.posX;
        this.posY = entitylivingIn.posY;
        this.posZ = entitylivingIn.posZ;
    }
    
    public boolean isUpdating() {
        return this.update;
    }
    
    public double getSpeed() {
        return this.speed;
    }
    
    public void setMoveTo(final double x, final double y, final double z, final double speedIn) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.speed = speedIn;
        this.update = true;
    }
    
    public void onUpdateMoveHelper() {
        this.entity.setMoveForward(0.0f);
        if (this.update) {
            this.update = false;
            final int i = MathHelper.floor_double(this.entity.getEntityBoundingBox().minY + 0.5);
            final double d0 = this.posX - this.entity.posX;
            final double d2 = this.posZ - this.entity.posZ;
            final double d3 = this.posY - i;
            final double d4 = d0 * d0 + d3 * d3 + d2 * d2;
            if (d4 >= 2.500000277905201E-7) {
                final float f = (float)(MathHelper.func_181159_b(d2, d0) * 180.0 / 3.141592653589793) - 90.0f;
                this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, f, 30.0f);
                this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));
                if (d3 > 0.0 && d0 * d0 + d2 * d2 < 1.0) {
                    this.entity.getJumpHelper().setJumping();
                }
            }
        }
    }
    
    protected float limitAngle(final float p_75639_1_, final float p_75639_2_, final float p_75639_3_) {
        float f = MathHelper.wrapAngleTo180_float(p_75639_2_ - p_75639_1_);
        if (f > p_75639_3_) {
            f = p_75639_3_;
        }
        if (f < -p_75639_3_) {
            f = -p_75639_3_;
        }
        float f2 = p_75639_1_ + f;
        if (f2 < 0.0f) {
            f2 += 360.0f;
        }
        else if (f2 > 360.0f) {
            f2 -= 360.0f;
        }
        return f2;
    }
    
    public double getX() {
        return this.posX;
    }
    
    public double getY() {
        return this.posY;
    }
    
    public double getZ() {
        return this.posZ;
    }
}
