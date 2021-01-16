package net.minecraft.entity.passive;

import javax.annotation.Nullable;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISkeletonRiders;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntitySkeletonHorse extends AbstractHorse
{
    private final EntityAISkeletonRiders skeletonTrapAI = new EntityAISkeletonRiders(this);
    private boolean skeletonTrap;
    private int skeletonTrapTime;

    public EntitySkeletonHorse(World p_i47295_1_)
    {
        super(p_i47295_1_);
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
        this.getEntityAttribute(JUMP_STRENGTH).setBaseValue(this.getModifiedJumpStrength());
    }

    protected SoundEvent getAmbientSound()
    {
        super.getAmbientSound();
        return SoundEvents.ENTITY_SKELETON_HORSE_AMBIENT;
    }

    protected SoundEvent getDeathSound()
    {
        super.getDeathSound();
        return SoundEvents.ENTITY_SKELETON_HORSE_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        super.getHurtSound(p_184601_1_);
        return SoundEvents.ENTITY_SKELETON_HORSE_HURT;
    }

    /**
     * Get this Entity's EnumCreatureAttribute
     */
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }

    /**
     * Returns the Y offset from the entity's position for any entity riding this one.
     */
    public double getMountedYOffset()
    {
        return super.getMountedYOffset() - 0.1875D;
    }

    @Nullable
    protected ResourceLocation getLootTable()
    {
        return LootTableList.ENTITIES_SKELETON_HORSE;
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if (this.func_190690_dh() && this.skeletonTrapTime++ >= 18000)
        {
            this.setDead();
        }
    }

    public static void func_190692_b(DataFixer p_190692_0_)
    {
        AbstractHorse.func_190683_c(p_190692_0_, EntitySkeletonHorse.class);
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setBoolean("SkeletonTrap", this.func_190690_dh());
        compound.setInteger("SkeletonTrapTime", this.skeletonTrapTime);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.func_190691_p(compound.getBoolean("SkeletonTrap"));
        this.skeletonTrapTime = compound.getInteger("SkeletonTrapTime");
    }

    public boolean func_190690_dh()
    {
        return this.skeletonTrap;
    }

    public void func_190691_p(boolean p_190691_1_)
    {
        if (p_190691_1_ != this.skeletonTrap)
        {
            this.skeletonTrap = p_190691_1_;

            if (p_190691_1_)
            {
                this.tasks.addTask(1, this.skeletonTrapAI);
            }
            else
            {
                this.tasks.removeTask(this.skeletonTrapAI);
            }
        }
    }

    public boolean processInteract(EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);
        boolean flag = !itemstack.func_190926_b();

        if (flag && itemstack.getItem() == Items.SPAWN_EGG)
        {
            return super.processInteract(player, hand);
        }
        else if (!this.isTame())
        {
            return false;
        }
        else if (this.isChild())
        {
            return super.processInteract(player, hand);
        }
        else if (player.isSneaking())
        {
            this.openGUI(player);
            return true;
        }
        else if (this.isBeingRidden())
        {
            return super.processInteract(player, hand);
        }
        else
        {
            if (flag)
            {
                if (itemstack.getItem() == Items.SADDLE && !this.isHorseSaddled())
                {
                    this.openGUI(player);
                    return true;
                }

                if (itemstack.interactWithEntity(player, this, hand))
                {
                    return true;
                }
            }

            this.mountTo(player);
            return true;
        }
    }
}
