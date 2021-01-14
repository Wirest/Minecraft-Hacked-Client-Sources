package net.minecraft.entity.ai;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.pathfinder.WalkNodeProcessor;

public class EntityAIControlledByPlayer extends EntityAIBase {
    private final EntityLiving thisEntity;
    private final float maxSpeed;
    private float currentSpeed;

    /**
     * Whether the entity's speed is boosted.
     */
    private boolean speedBoosted;

    /**
     * Counter for speed boosting, upon reaching maxSpeedBoostTime the speed boost will be disabled
     */
    private int speedBoostTime;

    /**
     * Maximum time the entity's speed should be boosted for.
     */
    private int maxSpeedBoostTime;
    private static final String __OBFID = "CL_00001580";

    public EntityAIControlledByPlayer(EntityLiving p_i1620_1_, float p_i1620_2_) {
        this.thisEntity = p_i1620_1_;
        this.maxSpeed = p_i1620_2_;
        this.setMutexBits(7);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.currentSpeed = 0.0F;
    }

    /**
     * Resets the task
     */
    public void resetTask() {
        this.speedBoosted = false;
        this.currentSpeed = 0.0F;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        return this.thisEntity.isEntityAlive() && this.thisEntity.riddenByEntity != null && this.thisEntity.riddenByEntity instanceof EntityPlayer && (this.speedBoosted || this.thisEntity.canBeSteered());
    }

    /**
     * Updates the task
     */
    public void updateTask() {
        EntityPlayer var1 = (EntityPlayer) this.thisEntity.riddenByEntity;
        EntityCreature var2 = (EntityCreature) this.thisEntity;
        float var3 = MathHelper.wrapAngleTo180_float(var1.rotationYaw - this.thisEntity.rotationYaw) * 0.5F;

        if (var3 > 5.0F) {
            var3 = 5.0F;
        }

        if (var3 < -5.0F) {
            var3 = -5.0F;
        }

        this.thisEntity.rotationYaw = MathHelper.wrapAngleTo180_float(this.thisEntity.rotationYaw + var3);

        if (this.currentSpeed < this.maxSpeed) {
            this.currentSpeed += (this.maxSpeed - this.currentSpeed) * 0.01F;
        }

        if (this.currentSpeed > this.maxSpeed) {
            this.currentSpeed = this.maxSpeed;
        }

        int var4 = MathHelper.floor_double(this.thisEntity.posX);
        int var5 = MathHelper.floor_double(this.thisEntity.posY);
        int var6 = MathHelper.floor_double(this.thisEntity.posZ);
        float var7 = this.currentSpeed;

        if (this.speedBoosted) {
            if (this.speedBoostTime++ > this.maxSpeedBoostTime) {
                this.speedBoosted = false;
            }

            var7 += var7 * 1.15F * MathHelper.sin((float) this.speedBoostTime / (float) this.maxSpeedBoostTime * (float) Math.PI);
        }

        float var8 = 0.91F;

        if (this.thisEntity.onGround) {
            var8 = this.thisEntity.worldObj.getBlockState(new BlockPos(MathHelper.floor_float((float) var4), MathHelper.floor_float((float) var5) - 1, MathHelper.floor_float((float) var6))).getBlock().slipperiness * 0.91F;
        }

        float var9 = 0.16277136F / (var8 * var8 * var8);
        float var10 = MathHelper.sin(var2.rotationYaw * (float) Math.PI / 180.0F);
        float var11 = MathHelper.cos(var2.rotationYaw * (float) Math.PI / 180.0F);
        float var12 = var2.getAIMoveSpeed() * var9;
        float var13 = Math.max(var7, 1.0F);
        var13 = var12 / var13;
        float var14 = var7 * var13;
        float var15 = -(var14 * var10);
        float var16 = var14 * var11;

        if (MathHelper.abs(var15) > MathHelper.abs(var16)) {
            if (var15 < 0.0F) {
                var15 -= this.thisEntity.width / 2.0F;
            }

            if (var15 > 0.0F) {
                var15 += this.thisEntity.width / 2.0F;
            }

            var16 = 0.0F;
        } else {
            var15 = 0.0F;

            if (var16 < 0.0F) {
                var16 -= this.thisEntity.width / 2.0F;
            }

            if (var16 > 0.0F) {
                var16 += this.thisEntity.width / 2.0F;
            }
        }

        int var17 = MathHelper.floor_double(this.thisEntity.posX + (double) var15);
        int var18 = MathHelper.floor_double(this.thisEntity.posZ + (double) var16);
        int var19 = MathHelper.floor_float(this.thisEntity.width + 1.0F);
        int var20 = MathHelper.floor_float(this.thisEntity.height + var1.height + 1.0F);
        int var21 = MathHelper.floor_float(this.thisEntity.width + 1.0F);

        if (var4 != var17 || var6 != var18) {
            Block var22 = this.thisEntity.worldObj.getBlockState(new BlockPos(var4, var5, var6)).getBlock();
            boolean var23 = !this.isStairOrSlab(var22) && (var22.getMaterial() != Material.air || !this.isStairOrSlab(this.thisEntity.worldObj.getBlockState(new BlockPos(var4, var5 - 1, var6)).getBlock()));

            if (var23 && 0 == WalkNodeProcessor.func_176170_a(this.thisEntity.worldObj, this.thisEntity, var17, var5, var18, var19, var20, var21, false, false, true) && 1 == WalkNodeProcessor.func_176170_a(this.thisEntity.worldObj, this.thisEntity, var4, var5 + 1, var6, var19, var20, var21, false, false, true) && 1 == WalkNodeProcessor.func_176170_a(this.thisEntity.worldObj, this.thisEntity, var17, var5 + 1, var18, var19, var20, var21, false, false, true)) {
                var2.getJumpHelper().setJumping();
            }
        }

        if (!var1.capabilities.isCreativeMode && this.currentSpeed >= this.maxSpeed * 0.5F && this.thisEntity.getRNG().nextFloat() < 0.006F && !this.speedBoosted) {
            ItemStack var24 = var1.getHeldItem();

            if (var24 != null && var24.getItem() == Items.carrot_on_a_stick) {
                var24.damageItem(1, var1);

                if (var24.stackSize == 0) {
                    ItemStack var25 = new ItemStack(Items.fishing_rod);
                    var25.setTagCompound(var24.getTagCompound());
                    var1.inventory.mainInventory[var1.inventory.currentItem] = var25;
                }
            }
        }

        this.thisEntity.moveEntityWithHeading(0.0F, var7);
    }

    /**
     * True if the block is a stair block or a slab block
     */
    private boolean isStairOrSlab(Block p_151498_1_) {
        return p_151498_1_ instanceof BlockStairs || p_151498_1_ instanceof BlockSlab;
    }

    /**
     * Return whether the entity's speed is boosted.
     */
    public boolean isSpeedBoosted() {
        return this.speedBoosted;
    }

    /**
     * Boost the entity's movement speed.
     */
    public void boostSpeed() {
        this.speedBoosted = true;
        this.speedBoostTime = 0;
        this.maxSpeedBoostTime = this.thisEntity.getRNG().nextInt(841) + 140;
    }

    /**
     * Return whether the entity is being controlled by a player.
     */
    public boolean isControlledByPlayer() {
        return !this.isSpeedBoosted() && this.currentSpeed > this.maxSpeed * 0.3F;
    }
}
