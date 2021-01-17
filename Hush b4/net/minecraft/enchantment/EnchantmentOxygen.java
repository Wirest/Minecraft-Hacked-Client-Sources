// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentOxygen extends Enchantment
{
    public EnchantmentOxygen(final int enchID, final ResourceLocation p_i45766_2_, final int p_i45766_3_) {
        super(enchID, p_i45766_2_, p_i45766_3_, EnumEnchantmentType.ARMOR_HEAD);
        this.setName("oxygen");
    }
    
    @Override
    public int getMinEnchantability(final int enchantmentLevel) {
        return 10 * enchantmentLevel;
    }
    
    @Override
    public int getMaxEnchantability(final int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 30;
    }
    
    @Override
    public int getMaxLevel() {
        return 3;
    }
}
