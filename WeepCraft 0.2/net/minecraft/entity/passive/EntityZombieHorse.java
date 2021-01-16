package net.minecraft.entity.passive;

import javax.annotation.Nullable;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;

public class EntityZombieHorse extends AbstractHorse
{
    public EntityZombieHorse(World p_i47293_1_)
    {
        super(p_i47293_1_);
    }

    public static void func_190693_b(DataFixer p_190693_0_)
    {
        AbstractHorse.func_190683_c(p_190693_0_, EntityZombieHorse.class);
    }

    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224D);
        this.getEntityAttribute(JUMP_STRENGTH).setBaseValue(this.getModifiedJumpStrength());
    }

    /**
     * Get this Entity's EnumCreatureAttribute
     */
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEAD;
    }

    protected SoundEvent getAmbientSound()
    {
        super.getAmbientSound();
        return SoundEvents.ENTITY_ZOMBIE_HORSE_AMBIENT;
    }

    protected SoundEvent getDeathSound()
    {
        super.getDeathSound();
        return SoundEvents.ENTITY_ZOMBIE_HORSE_DEATH;
    }

    protected SoundEvent getHurtSound(DamageSource p_184601_1_)
    {
        super.getHurtSound(p_184601_1_);
        return SoundEvents.ENTITY_ZOMBIE_HORSE_HURT;
    }

    @Nullable
    protected ResourceLocation getLootTable()
    {
        return LootTableList.ENTITIES_ZOMBIE_HORSE;
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
                if (!this.isHorseSaddled() && itemstack.getItem() == Items.SADDLE)
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
