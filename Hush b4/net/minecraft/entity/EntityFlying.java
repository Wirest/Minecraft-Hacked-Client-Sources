// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity;

import net.minecraft.util.MathHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.world.World;

public abstract class EntityFlying extends EntityLiving
{
    public EntityFlying(final World worldIn) {
        super(worldIn);
    }
    
    @Override
    public void fall(final float distance, final float damageMultiplier) {
    }
    
    @Override
    protected void updateFallState(final double y, final boolean onGroundIn, final Block blockIn, final BlockPos pos) {
    }
    
    @Override
    public void moveEntityWithHeading(final float strafe, final float forward) {
        if (this.isInWater()) {
            this.moveFlying(strafe, forward, 0.02f);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.800000011920929;
            this.motionY *= 0.800000011920929;
            this.motionZ *= 0.800000011920929;
        }
        else if (this.isInLava()) {
            this.moveFlying(strafe, forward, 0.02f);
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5;
            this.motionY *= 0.5;
            this.motionZ *= 0.5;
        }
        else {
            float f = 0.91f;
            if (this.onGround) {
                f = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.91f;
            }
            final float f2 = 0.16277136f / (f * f * f);
            this.moveFlying(strafe, forward, this.onGround ? (0.1f * f2) : 0.02f);
            f = 0.91f;
            if (this.onGround) {
                f = this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.getEntityBoundingBox().minY) - 1, MathHelper.floor_double(this.posZ))).getBlock().slipperiness * 0.91f;
            }
            this.moveEntity(this.motionX, this.motionY, this.motionZ);
            this.motionX *= f;
            this.motionY *= f;
            this.motionZ *= f;
        }
        this.prevLimbSwingAmount = this.limbSwingAmount;
        final double d1 = this.posX - this.prevPosX;
        final double d2 = this.posZ - this.prevPosZ;
        float f3 = MathHelper.sqrt_double(d1 * d1 + d2 * d2) * 4.0f;
        if (f3 > 1.0f) {
            f3 = 1.0f;
        }
        this.limbSwingAmount += (f3 - this.limbSwingAmount) * 0.4f;
        this.limbSwing += this.limbSwingAmount;
    }
    
    @Override
    public boolean isOnLadder() {
        return false;
    }
}
