// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentFireAspect extends Enchantment
{
    protected EnchantmentFireAspect(final int enchID, final ResourceLocation enchName, final int enchWeight) {
        super(enchID, enchName, enchWeight, EnumEnchantmentType.WEAPON);
        this.setName("fire");
    }
    
    @Override
    public int getMinEnchantability(final int enchantmentLevel) {
        return 10 + 20 * (enchantmentLevel - 1);
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
