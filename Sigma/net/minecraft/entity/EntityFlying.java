package net.minecraft.entity;

import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public abstract class EntityFlying extends EntityLiving {
    private static final String __OBFID = "CL_00001545";

    public EntityFlying(World worldIn) {
        super(worldIn);
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected void func_180433_a(double p_180433_1_, boolean p_180433_3_, Block p_180433_4_, BlockPos p_180433_5_) {
    }

    /**
     * Moves the entity based on the specified heading. Args: strafe, forward
     */
    @Override
    public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_) {
        if (isInWater()) {
            moveFlying(p_70612_1_, p_70612_2_, 0.02F);
            moveEntity(motionX, motionY, motionZ);
            motionX *= 0.800000011920929D;
            motionY *= 0.800000011920929D;
            motionZ *= 0.800000011920929D;
        } else if (func_180799_ab()) {
            moveFlying(p_70612_1_, p_70612_2_, 0.02F);
            moveEntity(motionX, motionY, motionZ);
            motionX *= 0.5D;
            motionY *= 0.5D;
            motionZ *= 0.5D;
        } else {
            float var3 = 0.91F;

            if (onGround) {
                var3 = worldObj.getBlockState(new BlockPos(MathHelper.floor_double(posX), MathHelper.floor_double(getEntityBoundingBox().minY) - 1, MathHelper.floor_double(posZ))).getBlock().slipperiness * 0.91F;
            }

            float var4 = 0.16277136F / (var3 * var3 * var3);
            moveFlying(p_70612_1_, p_70612_2_, onGround ? 0.1F * var4 : 0.02F);
            var3 = 0.91F;

            if (onGround) {
                var3 = worldObj.getBlockState(new BlockPos(MathHelper.floor_double(posX), MathHelper.floor_double(getEntityBoundingBox().minY) - 1, MathHelper.floor_double(posZ))).getBlock().slipperiness * 0.91F;
            }

            moveEntity(motionX, motionY, motionZ);
            motionX *= var3;
            motionY *= var3;
            motionZ *= var3;
        }

        prevLimbSwingAmount = limbSwingAmount;
        double var8 = posX - prevPosX;
        double var5 = posZ - prevPosZ;
        float var7 = MathHelper.sqrt_double(var8 * var8 + var5 * var5) * 4.0F;

        if (var7 > 1.0F) {
            var7 = 1.0F;
        }

        limbSwingAmount += (var7 - limbSwingAmount) * 0.4F;
        limbSwing += limbSwingAmount;
    }

    /**
     * returns true if this entity is by a ladder, false otherwise
     */
    @Override
    public boolean isOnLadder() {
        return false;
    }
}
