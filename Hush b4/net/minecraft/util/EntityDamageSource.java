// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;

public class EntityDamageSource extends DamageSource
{
    protected Entity damageSourceEntity;
    private boolean isThornsDamage;
    
    public EntityDamageSource(final String p_i1567_1_, final Entity damageSourceEntityIn) {
        super(p_i1567_1_);
        this.isThornsDamage = false;
        this.damageSourceEntity = damageSourceEntityIn;
    }
    
    public EntityDamageSource setIsThornsDamage() {
        this.isThornsDamage = true;
        return this;
    }
    
    public boolean getIsThornsDamage() {
        return this.isThornsDamage;
    }
    
    @Override
    public Entity getEntity() {
        return this.damageSourceEntity;
    }
    
    @Override
    public IChatComponent getDeathMessage(final EntityLivingBase p_151519_1_) {
        final ItemStack itemstack = (this.damageSourceEntity instanceof EntityLivingBase) ? ((EntityLivingBase)this.damageSourceEntity).getHeldItem() : null;
        final String s = "death.attack." + this.damageType;
        final String s2 = String.valueOf(s) + ".item";
        return (itemstack != null && itemstack.hasDisplayName() && StatCollector.canTranslate(s2)) ? new ChatComponentTranslation(s2, new Object[] { p_151519_1_.getDisplayName(), this.damageSourceEntity.getDisplayName(), itemstack.getChatComponent() }) : new ChatComponentTranslation(s, new Object[] { p_151519_1_.getDisplayName(), this.damageSourceEntity.getDisplayName() });
    }
    
    @Override
    public boolean isDifficultyScaled() {
        return this.damageSourceEntity != null && this.damageSourceEntity instanceof EntityLivingBase && !(this.damageSourceEntity instanceof EntityPlayer);
    }
}
