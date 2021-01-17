// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.passive;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.AchievementList;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.item.Item;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemStack;
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
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.world.World;
import net.minecraft.entity.ai.EntityAIControlledByPlayer;

public class EntityPig extends EntityAnimal
{
    private final EntityAIControlledByPlayer aiControlledByPlayer;
    
    public EntityPig(final World worldIn) {
        super(worldIn);
        this.setSize(0.9f, 0.9f);
        ((PathNavigateGround)this.getNavigator()).setAvoidsWater(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.25));
        this.tasks.addTask(2, this.aiControlledByPlayer = new EntityAIControlledByPlayer(this, 0.3f));
        this.tasks.addTask(3, new EntityAIMate(this, 1.0));
        this.tasks.addTask(4, new EntityAITempt(this, 1.2, Items.carrot_on_a_stick, false));
        this.tasks.addTask(4, new EntityAITempt(this, 1.2, Items.carrot, false));
        this.tasks.addTask(5, new EntityAIFollowParent(this, 1.1));
        this.tasks.addTask(6, new EntityAIWander(this, 1.0));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0f));
        this.tasks.addTask(8, new EntityAILookIdle(this));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25);
    }
    
    @Override
    public boolean canBeSteered() {
        final ItemStack itemstack = ((EntityPlayer)this.riddenByEntity).getHeldItem();
        return itemstack != null && itemstack.getItem() == Items.carrot_on_a_stick;
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, (Byte)0);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("Saddle", this.getSaddled());
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.setSaddled(tagCompund.getBoolean("Saddle"));
    }
    
    @Override
    protected String getLivingSound() {
        return "mob.pig.say";
    }
    
    @Override
    protected String getHurtSound() {
        return "mob.pig.say";
    }
    
    @Override
    protected String getDeathSound() {
        return "mob.pig.death";
    }
    
    @Override
    protected void playStepSound(final BlockPos pos, final Block blockIn) {
        this.playSound("mob.pig.step", 0.15f, 1.0f);
    }
    
    @Override
    public boolean interact(final EntityPlayer player) {
        if (super.interact(player)) {
            return true;
        }
        if (!this.getSaddled() || this.worldObj.isRemote || (this.riddenByEntity != null && this.riddenByEntity != player)) {
            return false;
        }
        player.mountEntity(this);
        return true;
    }
    
    @Override
    protected Item getDropItem() {
        return this.isBurning() ? Items.cooked_porkchop : Items.porkchop;
    }
    
    @Override
    protected void dropFewItems(final boolean p_70628_1_, final int p_70628_2_) {
        for (int i = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + p_70628_2_), j = 0; j < i; ++j) {
            if (this.isBurning()) {
                this.dropItem(Items.cooked_porkchop, 1);
            }
            else {
                this.dropItem(Items.porkchop, 1);
            }
        }
        if (this.getSaddled()) {
            this.dropItem(Items.saddle, 1);
        }
    }
    
    public boolean getSaddled() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 0x1) != 0x0;
    }
    
    public void setSaddled(final boolean saddled) {
        if (saddled) {
            this.dataWatcher.updateObject(16, (Byte)1);
        }
        else {
            this.dataWatcher.updateObject(16, (Byte)0);
        }
    }
    
    @Override
    public void onStruckByLightning(final EntityLightningBolt lightningBolt) {
        if (!this.worldObj.isRemote && !this.isDead) {
            final EntityPigZombie entitypigzombie = new EntityPigZombie(this.worldObj);
            entitypigzombie.setCurrentItemOrArmor(0, new ItemStack(Items.golden_sword));
            entitypigzombie.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            entitypigzombie.setNoAI(this.isAIDisabled());
            if (this.hasCustomName()) {
                entitypigzombie.setCustomNameTag(this.getCustomNameTag());
                entitypigzombie.setAlwaysRenderNameTag(this.getAlwaysRenderNameTag());
            }
            this.worldObj.spawnEntityInWorld(entitypigzombie);
            this.setDead();
        }
    }
    
    @Override
    public void fall(final float distance, final float damageMultiplier) {
        super.fall(distance, damageMultiplier);
        if (distance > 5.0f && this.riddenByEntity instanceof EntityPlayer) {
            ((EntityPlayer)this.riddenByEntity).triggerAchievement(AchievementList.flyPig);
        }
    }
    
    @Override
    public EntityPig createChild(final EntityAgeable ageable) {
        return new EntityPig(this.worldObj);
    }
    
    @Override
    public boolean isBreedingItem(final ItemStack stack) {
        return stack != null && stack.getItem() == Items.carrot;
    }
    
    public EntityAIControlledByPlayer getAIControlledByPlayer() {
        return this.aiControlledByPlayer;
    }
}
