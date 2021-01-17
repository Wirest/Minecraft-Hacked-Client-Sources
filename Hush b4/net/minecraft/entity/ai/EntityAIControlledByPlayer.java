// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.pathfinder.WalkNodeProcessor;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityLiving;

public class EntityAIControlledByPlayer extends EntityAIBase
{
    private final EntityLiving thisEntity;
    private final float maxSpeed;
    private float currentSpeed;
    private boolean speedBoosted;
    private int speedBoostTime;
    private int maxSpeedBoostTime;
    
    public EntityAIControlledByPlayer(final EntityLiving entitylivingIn, final float maxspeed) {
        this.thisEntity = entitylivingIn;
        this.maxSpeed = maxspeed;
        this.setMutexBits(7);
    }
    
    @Override
    public void startExecuting() {
        this.currentSpeed = 0.0f;
    }
    
    @Override
    public void resetTask() {
        this.speedBoosted = false;
        this.currentSpeed = 0.0f;
    }
    
    @Override
    public boolean shouldExecute() {
        return this.thisEntity.isEntityAlive() && this.thisEntity.riddenByEntity != null && this.thisEntity.riddenByEntity instanceof EntityPlayer && (this.speedBoosted || this.thisEntity.canBeSteered());
    }
    
    @Override
    public void updateTask() {
        final EntityPlayer entityplayer = (EntityPlayer)this.thisEntity.riddenByEntity;
        final EntityCreature entitycreature = (EntityCreature)this.thisEntity;
        float f = MathHelper.wrapAngleTo180_float(entityplayer.rotationYaw - this.thisEntity.rotationYaw) * 0.5f;
        if (f > 5.0f) {
            f = 5.0f;
        }
        if (f < -5.0f) {
            f = -5.0f;
        }
        this.thisEntity.rotationYaw = MathHelper.wrapAngleTo180_float(this.thisEntity.rotationYaw + f);
        if (this.currentSpeed < this.maxSpeed) {
            this.currentSpeed += (this.maxSpeed - this.currentSpeed) * 0.01f;
        }
        if (this.currentSpeed > this.maxSpeed) {
            this.currentSpeed = this.maxSpeed;
        }
        final int i = MathHelper.floor_double(this.thisEntity.posX);
        final int j = MathHelper.floor_double(this.thisEntity.posY);
        final int k = MathHelper.floor_double(this.thisEntity.posZ);
        float f2 = this.currentSpeed;
        if (this.speedBoosted) {
            if (this.speedBoostTime++ > this.maxSpeedBoostTime) {
                this.speedBoosted = false;
            }
            f2 += f2 * 1.15f * MathHelper.sin(this.speedBoostTime / (float)this.maxSpeedBoostTime * 3.1415927f);
        }
        float f3 = 0.91f;
        if (this.thisEntity.onGround) {
            f3 = this.thisEntity.worldObj.getBlockState(new BlockPos(MathHelper.floor_float((float)i), MathHelper.floor_float((float)j) - 1, MathHelper.floor_float((float)k))).getBlock().slipperiness * 0.91f;
        }
        final float f4 = 0.16277136f / (f3 * f3 * f3);
        final float f5 = MathHelper.sin(entitycreature.rotationYaw * 3.1415927f / 180.0f);
        final float f6 = MathHelper.cos(entitycreature.rotationYaw * 3.1415927f / 180.0f);
        final float f7 = entitycreature.getAIMoveSpeed() * f4;
        float f8 = Math.max(f2, 1.0f);
        f8 = f7 / f8;
        final float f9 = f2 * f8;
        float f10 = -(f9 * f5);
        float f11 = f9 * f6;
        if (MathHelper.abs(f10) > MathHelper.abs(f11)) {
            if (f10 < 0.0f) {
                f10 -= this.thisEntity.width / 2.0f;
            }
            if (f10 > 0.0f) {
                f10 += this.thisEntity.width / 2.0f;
            }
            f11 = 0.0f;
        }
        else {
            f10 = 0.0f;
            if (f11 < 0.0f) {
                f11 -= this.thisEntity.width / 2.0f;
            }
            if (f11 > 0.0f) {
                f11 += this.thisEntity.width / 2.0f;
            }
        }
        final int l = MathHelper.floor_double(this.thisEntity.posX + f10);
        final int i2 = MathHelper.floor_double(this.thisEntity.posZ + f11);
        final int j2 = MathHelper.floor_float(this.thisEntity.width + 1.0f);
        final int k2 = MathHelper.floor_float(this.thisEntity.height + entityplayer.height + 1.0f);
        final int l2 = MathHelper.floor_float(this.thisEntity.width + 1.0f);
        if (i != l || k != i2) {
            final Block block = this.thisEntity.worldObj.getBlockState(new BlockPos(i, j, k)).getBlock();
            final boolean flag = !this.isStairOrSlab(block) && (block.getMaterial() != Material.air || !this.isStairOrSlab(this.thisEntity.worldObj.getBlockState(new BlockPos(i, j - 1, k)).getBlock()));
            if (flag && WalkNodeProcessor.func_176170_a(this.thisEntity.worldObj, this.thisEntity, l, j, i2, j2, k2, l2, false, false, true) == 0 && 1 == WalkNodeProcessor.func_176170_a(this.thisEntity.worldObj, this.thisEntity, i, j + 1, k, j2, k2, l2, false, false, true) && 1 == WalkNodeProcessor.func_176170_a(this.thisEntity.worldObj, this.thisEntity, l, j + 1, i2, j2, k2, l2, false, false, true)) {
                entitycreature.getJumpHelper().setJumping();
            }
        }
        if (!entityplayer.capabilities.isCreativeMode && this.currentSpeed >= this.maxSpeed * 0.5f && this.thisEntity.getRNG().nextFloat() < 0.006f && !this.speedBoosted) {
            final ItemStack itemstack = entityplayer.getHeldItem();
            if (itemstack != null && itemstack.getItem() == Items.carrot_on_a_stick) {
                itemstack.damageItem(1, entityplayer);
                if (itemstack.stackSize == 0) {
                    final ItemStack itemstack2 = new ItemStack(Items.fishing_rod);
                    itemstack2.setTagCompound(itemstack.getTagCompound());
                    entityplayer.inventory.mainInventory[entityplayer.inventory.currentItem] = itemstack2;
                }
            }
        }
        this.thisEntity.moveEntityWithHeading(0.0f, f2);
    }
    
    private boolean isStairOrSlab(final Block blockIn) {
        return blockIn instanceof BlockStairs || blockIn instanceof BlockSlab;
    }
    
    public boolean isSpeedBoosted() {
        return this.speedBoosted;
    }
    
    public void boostSpeed() {
        this.speedBoosted = true;
        this.speedBoostTime = 0;
        this.maxSpeedBoostTime = this.thisEntity.getRNG().nextInt(841) + 140;
    }
    
    public boolean isControlledByPlayer() {
        return !this.isSpeedBoosted() && this.currentSpeed > this.maxSpeed * 0.3f;
    }
}
