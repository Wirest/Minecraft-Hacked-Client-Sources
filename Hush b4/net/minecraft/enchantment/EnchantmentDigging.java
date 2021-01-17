// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class EnchantmentDigging extends Enchantment
{
    protected EnchantmentDigging(final int enchID, final ResourceLocation enchName, final int enchWeight) {
        super(enchID, enchName, enchWeight, EnumEnchantmentType.DIGGER);
        this.setName("digging");
    }
    
    @Override
    public int getMinEnchantability(final int enchantmentLevel) {
        return 1 + 10 * (enchantmentLevel - 1);
    }
    
    @Override
    public int getMaxEnchantability(final int enchantmentLevel) {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 5;
    }
    
    @Override
    public boolean canApply(final ItemStack stack) {
        return stack.getItem() == Items.shears || super.canApply(stack);
    }
}
