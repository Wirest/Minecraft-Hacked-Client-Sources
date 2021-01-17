// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentWaterWalker extends Enchantment
{
    public EnchantmentWaterWalker(final int p_i45762_1_, final ResourceLocation p_i45762_2_, final int p_i45762_3_) {
        super(p_i45762_1_, p_i45762_2_, p_i45762_3_, EnumEnchantmentType.ARMOR_FEET);
        this.setName("waterWalker");
    }
    
    @Override
    public int getMinEnchantability(final int enchantmentLevel) {
        return enchantmentLevel * 10;
    }
    
    @Override
    public int getMaxEnchantability(final int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 15;
    }
    
    @Override
    public int getMaxLevel() {
        return 3;
    }
}
