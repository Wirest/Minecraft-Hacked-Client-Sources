// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.dispenser;

import net.minecraft.item.ItemStack;

public interface IBehaviorDispenseItem
{
    public static final IBehaviorDispenseItem itemDispenseBehaviorProvider = new IBehaviorDispenseItem() {
        @Override
        public ItemStack dispense(final IBlockSource source, final ItemStack stack) {
            return stack;
        }
    };
    
    ItemStack dispense(final IBlockSource p0, final ItemStack p1);
}
