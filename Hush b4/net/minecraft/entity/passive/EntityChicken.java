// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.item.Item;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.init.Items;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.world.World;

public class EntityChicken extends EntityAnimal
{
    public float wingRotation;
    public float destPos;
    public float field_70884_g;
    public float field_70888_h;
    public float wingRotDelta;
    public int timeUntilNextEgg;
    public boolean chickenJockey;
    
    public EntityChicken(final World worldIn) {
        super(worldIn);
        this.wingRotDelta = 1.0f;
        this.setSize(0.4f, 0.7f);
        this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.4));
        this.tasks.addTask(2, new EntityAIMate(this, 1.0));
        this.tasks.addTask(3, new EntityAITempt(this, 1.0, Items.wheat_seeds, false));
        this.tasks.addTask(4, new EntityAIFollowParent(this, 1.1));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0f));
        this.tasks.addTask(7, new EntityAILookIdle(this));
    }
    
    @Override
    public float getEyeHeight() {
        return this.height;
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(4.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25);
    }
    
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        this.field_70888_h = this.wingRotation;
        this.field_70884_g = this.destPos;
        this.destPos += (float)((this.onGround ? -1 : 4) * 0.3);
        this.destPos = MathHelper.clamp_float(this.destPos, 0.0f, 1.0f);
        if (!this.onGround && this.wingRotDelta < 1.0f) {
            this.wingRotDelta = 1.0f;
        }
        this.wingRotDelta *= (float)0.9;
        if (!this.onGround && this.motionY < 0.0) {
            this.motionY *= 0.6;
        }
        this.wingRotation += this.wingRotDelta * 2.0f;
        if (!this.worldObj.isRemote && !this.isChild() && !this.isChickenJockey() && --this.timeUntilNextEgg <= 0) {
            this.playSound("mob.chicken.plop", 1.0f, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 1.0f);
            this.dropItem(Items.egg, 1);
            this.timeUntilNextEgg = this.rand.nextInt(6000) + 6000;
        }
    }
    
    @Override
    public void fall(final float distance, final float damageMultiplier) {
    }
    
    @Override
    protected String getLivingSound() {
        return "mob.chicken.say";
    }
    
    @Override
    protected String getHurtSound() {
        return "mob.chicken.hurt";
    }
    
    @Override
    protected String getDeathSound() {
        return "mob.chicken.hurt";
    }
    
    @Override
    protected void playStepSound(final BlockPos pos, final Block blockIn) {
        this.playSound("mob.chicken.step", 0.15f, 1.0f);
    }
    
    @Override
    protected Item getDropItem() {
        return Items.feather;
    }
    
    @Override
    protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
        for (int i = this.rand.nextInt(3) + this.rand.nextInt(1 + p_70628_2_), j = 0; j < i; ++j) {
            this.dropItem(Items.feather, 1);
        }
        if (this.isBurning()) {
            this.dropItem(Items.cooked_chicken, 1);
        }
        else {
            this.dropItem(Items.chicken, 1);
        }
    }
    
    @Override
    public EntityChicken createChild(final EntityAgeable ageable) {
        return new EntityChicken(this.worldObj);
    }
    
    @Override
    public boolean isBreedingItem(final ItemStack stack) {
        return stack != null && stack.getItem() == Items.wheat_seeds;
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.chickenJockey = tagCompund.getBoolean("IsChickenJockey");
        if (tagCompund.hasKey("EggLayTime")) {
            this.timeUntilNextEgg = tagCompund.getInteger("EggLayTime");
        }
    }
    
    @Override
    protected int getExperiencePoints(final EntityPlayer player) {
        return this.isChickenJockey() ? 10 : super.getExperiencePoints(player);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("IsChickenJockey", this.chickenJockey);
        tagCompound.setInteger("EggLayTime", this.timeUntilNextEgg);
    }
    
    @Override
    protected boolean canDespawn() {
        return this.isChickenJockey() && this.riddenByEntity == null;
    }
    
    @Override
    public void updateRiderPosition() {
        super.updateRiderPosition();
        final float f = MathHelper.sin(this.renderYawOffset * 3.1415927f / 180.0f);
        final float f2 = MathHelper.cos(this.renderYawOffset * 3.1415927f / 180.0f);
        final float f3 = 0.1f;
        final float f4 = 0.0f;
        this.riddenByEntity.setPosition(this.posX + f3 * f, this.posY + this.height * 0.5f + this.riddenByEntity.getYOffset() + f4, this.posZ - f3 * f2);
        if (this.riddenByEntity instanceof EntityLivingBase) {
            ((EntityLivingBase)this.riddenByEntity).renderYawOffset = this.renderYawOffset;
        }
    }
    
    public boolean isChickenJockey() {
        return this.chickenJockey;
    }
    
    public void setChickenJockey(final boolean jockey) {
        this.chickenJockey = jockey;
    }
}
