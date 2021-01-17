// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;

public class EntityDamageSourceIndirect extends EntityDamageSource
{
    private Entity indirectEntity;
    
    public EntityDamageSourceIndirect(final String p_i1568_1_, final Entity p_i1568_2_, final Entity indirectEntityIn) {
        super(p_i1568_1_, p_i1568_2_);
        this.indirectEntity = indirectEntityIn;
    }
    
    @Override
    public Entity getSourceOfDamage() {
        return this.damageSourceEntity;
    }
    
    @Override
    public Entity getEntity() {
        return this.indirectEntity;
    }
    
    @Override
    public IChatComponent getDeathMessage(final EntityLivingBase p_151519_1_) {
        final IChatComponent ichatcomponent = (this.indirectEntity == null) ? this.damageSourceEntity.getDisplayName() : this.indirectEntity.getDisplayName();
        final ItemStack itemstack = (this.indirectEntity instanceof EntityLivingBase) ? ((EntityLivingBase)this.indirectEntity).getHeldItem() : null;
        final String s = "death.attack." + this.damageType;
        final String s2 = String.valueOf(s) + ".item";
        return (itemstack != null && itemstack.hasDisplayName() && StatCollector.canTranslate(s2)) ? new ChatComponentTranslation(s2, new Object[] { p_151519_1_.getDisplayName(), ichatcomponent, itemstack.getChatComponent() }) : new ChatComponentTranslation(s, new Object[] { p_151519_1_.getDisplayName(), ichatcomponent });
    }
}
