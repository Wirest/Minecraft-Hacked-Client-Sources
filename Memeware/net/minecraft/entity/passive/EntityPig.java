package net.minecraft.entity.passive;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIControlledByPlayer;
import net.minecraft.entity.ai.EntityAIFollowParent;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class EntityPig extends EntityAnimal {
    /**
     * AI task for player control.
     */
    private final EntityAIControlledByPlayer aiControlledByPlayer;
    private static final String __OBFID = "CL_00001647";

    public EntityPig(World worldIn) {
        super(worldIn);
        this.setSize(0.9F, 0.9F);
        ((PathNavigateGround) this.getNavigator()).func_179690_a(true);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIPanic(this, 1.25D));
        this.tasks.addTask(2, this.aiControlledByPlayer = new EntityAIControlledByPlayer(this, 0.3F));
        this.tasks.addTask(3, new EntityAIMate(this, 1.0D));
        this.tasks.addTask(4, new EntityAITempt(this, 1.2D, Items.carrot_on_a_stick, false));
        this.tasks.addTask(4, new EntityAITempt(this, 1.2D, Items.carrot, false));
        this.tasks.addTask(5, new EntityAIFollowParent(this, 1.1D));
        this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
    }

    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
    }

    /**
     * returns true if all the conditions for steering the entity are met. For pigs, this is true if it is being ridden
     * by a player and the player is holding a carrot-on-a-stick
     */
    public boolean canBeSteered() {
        ItemStack var1 = ((EntityPlayer) this.riddenByEntity).getHeldItem();
        return var1 != null && var1.getItem() == Items.carrot_on_a_stick;
    }

    protected void entityInit() {
        super.entityInit();
        this.dataWatcher.addObject(16, Byte.valueOf((byte) 0));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound tagCompound) {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setBoolean("Saddle", this.getSaddled());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound tagCompund) {
        super.readEntityFromNBT(tagCompund);
        this.setSaddled(tagCompund.getBoolean("Saddle"));
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    protected String getLivingSound() {
        return "mob.pig.say";
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    protected String getHurtSound() {
        return "mob.pig.say";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    protected String getDeathSound() {
        return "mob.pig.death";
    }

    protected void func_180429_a(BlockPos p_180429_1_, Block p_180429_2_) {
        this.playSound("mob.pig.step", 0.15F, 1.0F);
    }

    /**
     * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
     */
    public boolean interact(EntityPlayer p_70085_1_) {
        if (super.interact(p_70085_1_)) {
            return true;
        } else if (this.getSaddled() && !this.worldObj.isRemote && (this.riddenByEntity == null || this.riddenByEntity == p_70085_1_)) {
            p_70085_1_.mountEntity(this);
            return true;
        } else {
            return false;
        }
    }

    protected Item getDropItem() {
        return this.isBurning() ? Items.cooked_porkchop : Items.porkchop;
    }

    /**
     * Drop 0-2 items of this living's type
     */
    protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {
        int var3 = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + p_70628_2_);

        for (int var4 = 0; var4 < var3; ++var4) {
            if (this.isBurning()) {
                this.dropItem(Items.cooked_porkchop, 1);
            } else {
                this.dropItem(Items.porkchop, 1);
            }
        }

        if (this.getSaddled()) {
            this.dropItem(Items.saddle, 1);
        }
    }

    /**
     * Returns true if the pig is saddled.
     */
    public boolean getSaddled() {
        return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    /**
     * Set or remove the saddle of the pig.
     */
    public void setSaddled(boolean p_70900_1_) {
        if (p_70900_1_) {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) 1));
        } else {
            this.dataWatcher.updateObject(16, Byte.valueOf((byte) 0));
        }
    }

    /**
     * Called when a lightning bolt hits the entity.
     */
    public void onStruckByLightning(EntityLightningBolt lightningBolt) {
        if (!this.worldObj.isRemote) {
            EntityPigZombie var2 = new EntityPigZombie(this.worldObj);
            var2.setCurrentItemOrArmor(0, new ItemStack(Items.golden_sword));
            var2.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            this.worldObj.spawnEntityInWorld(var2);
            this.setDead();
        }
    }

    public void fall(float distance, float damageMultiplier) {
        super.fall(distance, damageMultiplier);

        if (distance > 5.0F && this.riddenByEntity instanceof EntityPlayer) {
            ((EntityPlayer) this.riddenByEntity).triggerAchievement(AchievementList.flyPig);
        }
    }

    public EntityPig createChild(EntityAgeable p_90011_1_) {
        return new EntityPig(this.worldObj);
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    public boolean isBreedingItem(ItemStack p_70877_1_) {
        return p_70877_1_ != null && p_70877_1_.getItem() == Items.carrot;
    }

    /**
     * Return the AI task for player control.
     */
    public EntityAIControlledByPlayer getAIControlledByPlayer() {
        return this.aiControlledByPlayer;
    }


}
