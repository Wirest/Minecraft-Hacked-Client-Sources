// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.item.ItemArmor;
import java.util.Random;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class EnchantmentDurability extends Enchantment
{
    protected EnchantmentDurability(final int enchID, final ResourceLocation enchName, final int enchWeight) {
        super(enchID, enchName, enchWeight, EnumEnchantmentType.BREAKABLE);
        this.setName("durability");
    }
    
    @Override
    public int getMinEnchantability(final int enchantmentLevel) {
        return 5 + (enchantmentLevel - 1) * 8;
    }
    
    @Override
    public int getMaxEnchantability(final int enchantmentLevel) {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 3;
    }
    
    @Override
    public boolean canApply(final ItemStack stack) {
        return stack.isItemStackDamageable() || super.canApply(stack);
    }
    
    public static boolean negateDamage(final ItemStack p_92097_0_, final int p_92097_1_, final Random p_92097_2_) {
        return (!(p_92097_0_.getItem() instanceof ItemArmor) || p_92097_2_.nextFloat() >= 0.6f) && p_92097_2_.nextInt(p_92097_1_ + 1) > 0;
    }
}
