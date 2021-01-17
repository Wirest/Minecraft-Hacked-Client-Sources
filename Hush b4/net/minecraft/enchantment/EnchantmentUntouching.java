// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.enchantment;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class EnchantmentUntouching extends Enchantment
{
    protected EnchantmentUntouching(final int p_i45763_1_, final ResourceLocation p_i45763_2_, final int p_i45763_3_) {
        super(p_i45763_1_, p_i45763_2_, p_i45763_3_, EnumEnchantmentType.DIGGER);
        this.setName("untouching");
    }
    
    @Override
    public int getMinEnchantability(final int enchantmentLevel) {
        return 15;
    }
    
    @Override
    public int getMaxEnchantability(final int enchantmentLevel) {
        return super.getMinEnchantability(enchantmentLevel) + 50;
    }
    
    @Override
    public int getMaxLevel() {
        return 1;
    }
    
    @Override
    public boolean canApplyTogether(final Enchantment ench) {
        return super.canApplyTogether(ench) && ench.effectId != EnchantmentUntouching.fortune.effectId;
    }
    
    @Override
    public boolean canApply(final ItemStack stack) {
        return stack.getItem() == Items.shears || super.canApply(stack);
    }
}
