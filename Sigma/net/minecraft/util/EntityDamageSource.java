package net.minecraft.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class EntityDamageSource extends DamageSource {
    protected Entity damageSourceEntity;
    private boolean field_180140_r = false;
    private static final String __OBFID = "CL_00001522";

    public EntityDamageSource(String p_i1567_1_, Entity p_i1567_2_) {
        super(p_i1567_1_);
        damageSourceEntity = p_i1567_2_;
    }

    public EntityDamageSource func_180138_v() {
        field_180140_r = true;
        return this;
    }

    public boolean func_180139_w() {
        return field_180140_r;
    }

    @Override
    public Entity getEntity() {
        return damageSourceEntity;
    }

    /**
     * Gets the death message that is displayed when the player dies
     */
    @Override
    public IChatComponent getDeathMessage(EntityLivingBase p_151519_1_) {
        ItemStack var2 = damageSourceEntity instanceof EntityLivingBase ? ((EntityLivingBase) damageSourceEntity).getHeldItem() : null;
        String var3 = "death.attack." + damageType;
        String var4 = var3 + ".item";
        return var2 != null && var2.hasDisplayName() && StatCollector.canTranslate(var4) ? new ChatComponentTranslation(var4, new Object[]{p_151519_1_.getDisplayName(), damageSourceEntity.getDisplayName(), var2.getChatComponent()}) : new ChatComponentTranslation(var3, new Object[]{p_151519_1_.getDisplayName(), damageSourceEntity.getDisplayName()});
    }

    /**
     * Return whether this damage source will have its damage amount scaled
     * based on the current difficulty.
     */
    @Override
    public boolean isDifficultyScaled() {
        return damageSourceEntity != null && damageSourceEntity instanceof EntityLivingBase && !(damageSourceEntity instanceof EntityPlayer);
    }
}
