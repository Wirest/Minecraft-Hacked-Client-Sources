// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentWaterWorker extends Enchantment
{
    public EnchantmentWaterWorker(final int p_i45761_1_, final ResourceLocation p_i45761_2_, final int p_i45761_3_) {
        super(p_i45761_1_, p_i45761_2_, p_i45761_3_, EnumEnchantmentType.ARMOR_HEAD);
        this.setName("waterWorker");
    }
    
    @Override
    public int getMinEnchantability(final int enchantmentLevel) {
        return 1;
    }
    
    @Override
    public int getMaxEnchantability(final int enchantmentLevel) {
        return this.getMinEnchantability(enchantmentLevel) + 40;
    }
    
    @Override
    public int getMaxLevel() {
        return 1;
    }
}
