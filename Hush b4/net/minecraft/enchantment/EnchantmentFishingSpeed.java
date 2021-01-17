// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.util.ResourceLocation;

public class EnchantmentFishingSpeed extends Enchantment
{
    protected EnchantmentFishingSpeed(final int enchID, final ResourceLocation enchName, final int enchWeight, final EnumEnchantmentType enchType) {
        super(enchID, enchName, enchWeight, enchType);
        this.setName("fishingSpeed");
    }
    
    @Override
    public int getMinEnchantability(final int enchantmentLevel) {
        return 15 + (enchantmentLevel - 1) * 9;
    }
    
    @Override
    public int getMaxEnchantability(final int enchantmentLevel) {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 3;
    }
}
