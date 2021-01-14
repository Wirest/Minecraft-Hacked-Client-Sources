package net.minecraft.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class EntityDamageSourceIndirect extends EntityDamageSource {
    private Entity indirectEntity;
    private static final String __OBFID = "CL_00001523";

    public EntityDamageSourceIndirect(String p_i1568_1_, Entity p_i1568_2_, Entity p_i1568_3_) {
        super(p_i1568_1_, p_i1568_2_);
        this.indirectEntity = p_i1568_3_;
    }

    public Entity getSourceOfDamage() {
        return this.damageSourceEntity;
    }

    public Entity getEntity() {
        return this.indirectEntity;
    }

    /**
     * Gets the death message that is displayed when the player dies
     */
    public IChatComponent getDeathMessage(EntityLivingBase p_151519_1_) {
        IChatComponent var2 = this.indirectEntity == null ? this.damageSourceEntity.getDisplayName() : this.indirectEntity.getDisplayName();
        ItemStack var3 = this.indirectEntity instanceof EntityLivingBase ? ((EntityLivingBase) this.indirectEntity).getHeldItem() : null;
        String var4 = "death.attack." + this.damageType;
        String var5 = var4 + ".item";
        return var3 != null && var3.hasDisplayName() && StatCollector.canTranslate(var5) ? new ChatComponentTranslation(var5, new Object[]{p_151519_1_.getDisplayName(), var2, var3.getChatComponent()}) : new ChatComponentTranslation(var4, new Object[]{p_151519_1_.getDisplayName(), var2});
    }
}
