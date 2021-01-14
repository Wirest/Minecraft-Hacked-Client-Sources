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
        this.damageSourceEntity = p_i1567_2_;
    }

    public EntityDamageSource func_180138_v() {
        this.field_180140_r = true;
        return this;
    }

    public boolean func_180139_w() {
        return this.field_180140_r;
    }

    public Entity getEntity() {
        return this.damageSourceEntity;
    }

    /**
     * Gets the death message that is displayed when the player dies
     */
    public IChatComponent getDeathMessage(EntityLivingBase p_151519_1_) {
        ItemStack var2 = this.damageSourceEntity instanceof EntityLivingBase ? ((EntityLivingBase) this.damageSourceEntity).getHeldItem() : null;
        String var3 = "death.attack." + this.damageType;
        String var4 = var3 + ".item";
        return var2 != null && var2.hasDisplayName() && StatCollector.canTranslate(var4) ? new ChatComponentTranslation(var4, new Object[]{p_151519_1_.getDisplayName(), this.damageSourceEntity.getDisplayName(), var2.getChatComponent()}) : new ChatComponentTranslation(var3, new Object[]{p_151519_1_.getDisplayName(), this.damageSourceEntity.getDisplayName()});
    }

    /**
     * Return whether this damage source will have its damage amount scaled based on the current difficulty.
     */
    public boolean isDifficultyScaled() {
        return this.damageSourceEntity != null && this.damageSourceEntity instanceof EntityLivingBase && !(this.damageSourceEntity instanceof EntityPlayer);
    }
}
