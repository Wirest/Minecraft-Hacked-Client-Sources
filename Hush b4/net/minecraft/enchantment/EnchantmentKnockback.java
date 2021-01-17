// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentKnockback extends Enchantment
{
    protected EnchantmentKnockback(final int p_i45768_1_, final ResourceLocation p_i45768_2_, final int p_i45768_3_) {
        super(p_i45768_1_, p_i45768_2_, p_i45768_3_, EnumEnchantmentType.WEAPON);
        this.setName("knockback");
    }
    
    @Override
    public int getMinEnchantability(final int enchantmentLevel) {
        return 5 + 20 * (enchantmentLevel - 1);
    }
    
    @Override
    public int getMaxEnchantability(final int enchantmentLevel) {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 2;
    }
}
