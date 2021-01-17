// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.entity.Entity;
import com.google.common.base.Predicate;

public final class EntitySelectors
{
    public static final Predicate<Entity> selectAnything;
    public static final Predicate<Entity> IS_STANDALONE;
    public static final Predicate<Entity> selectInventories;
    public static final Predicate<Entity> NOT_SPECTATING;
    
    static {
        selectAnything = new Predicate<Entity>() {
            @Override
            public boolean apply(final Entity p_apply_1_) {
                return p_apply_1_.isEntityAlive();
            }
        };
        IS_STANDALONE = new Predicate<Entity>() {
            @Override
            public boolean apply(final Entity p_apply_1_) {
                return p_apply_1_.isEntityAlive() && p_apply_1_.riddenByEntity == null && p_apply_1_.ridingEntity == null;
            }
        };
        selectInventories = new Predicate<Entity>() {
            @Override
            public boolean apply(final Entity p_apply_1_) {
                return p_apply_1_ instanceof IInventory && p_apply_1_.isEntityAlive();
            }
        };
        NOT_SPECTATING = new Predicate<Entity>() {
            @Override
            public boolean apply(final Entity p_apply_1_) {
                return !(p_apply_1_ instanceof EntityPlayer) || !((EntityPlayer)p_apply_1_).isSpectator();
            }
        };
    }
    
    public static class ArmoredMob implements Predicate<Entity>
    {
        private final ItemStack armor;
        
        public ArmoredMob(final ItemStack armor) {
            this.armor = armor;
        }
        
        @Override
        public boolean apply(final Entity p_apply_1_) {
            if (!p_apply_1_.isEntityAlive()) {
                return false;
            }
            if (!(p_apply_1_ instanceof EntityLivingBase)) {
                return false;
            }
            final EntityLivingBase entitylivingbase = (EntityLivingBase)p_apply_1_;
            return entitylivingbase.getEquipmentInSlot(EntityLiving.getArmorPosition(this.armor)) == null && ((entitylivingbase instanceof EntityLiving) ? ((EntityLiving)entitylivingbase).canPickUpLoot() : (entitylivingbase instanceof EntityArmorStand || entitylivingbase instanceof EntityPlayer));
        }
    }
}
