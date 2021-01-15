package net.minecraft.command;

import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public final class IEntitySelector
{
    public static final Predicate selectAnything = new Predicate()
    {
        public boolean apply(Entity entityIn)
        {
            return entityIn.isEntityAlive();
        }
        @Override
		public boolean apply(Object p_apply_1_)
        {
            return this.apply((Entity)p_apply_1_);
        }
    };

    /**
     * Selects only entities which are neither ridden by anything nor ride on anything
     */
    public static final Predicate IS_STANDALONE = new Predicate()
    {
        public boolean apply(Entity entityIn)
        {
            return entityIn.isEntityAlive() && entityIn.riddenByEntity == null && entityIn.ridingEntity == null;
        }
        @Override
		public boolean apply(Object p_apply_1_)
        {
            return this.apply((Entity)p_apply_1_);
        }
    };
    public static final Predicate selectInventories = new Predicate()
    {
        public boolean apply(Entity entityIn)
        {
            return entityIn instanceof IInventory && entityIn.isEntityAlive();
        }
        @Override
		public boolean apply(Object p_apply_1_)
        {
            return this.apply((Entity)p_apply_1_);
        }
    };

    /**
     * Selects entities which are either not players or players that are not spectating
     */
    public static final Predicate NOT_SPECTATING = new Predicate()
    {
        public boolean apply(Entity entityIn)
        {
            return !(entityIn instanceof EntityPlayer) || !((EntityPlayer)entityIn).isSpectator();
        }
        @Override
		public boolean apply(Object p_apply_1_)
        {
            return this.apply((Entity)p_apply_1_);
        }
    };

    public static class ArmoredMob implements Predicate
    {
        private final ItemStack armor;

        public ArmoredMob(ItemStack armor)
        {
            this.armor = armor;
        }

        public boolean apply(Entity p_180100_1_)
        {
            if (!p_180100_1_.isEntityAlive())
            {
                return false;
            }
            else if (!(p_180100_1_ instanceof EntityLivingBase))
            {
                return false;
            }
            else
            {
                EntityLivingBase var2 = (EntityLivingBase)p_180100_1_;
                return var2.getEquipmentInSlot(EntityLiving.getArmorPosition(this.armor)) != null ? false : (var2 instanceof EntityLiving ? ((EntityLiving)var2).canPickUpLoot() : (var2 instanceof EntityArmorStand ? true : var2 instanceof EntityPlayer));
            }
        }

        @Override
		public boolean apply(Object p_apply_1_)
        {
            return this.apply((Entity)p_apply_1_);
        }
    }
}
