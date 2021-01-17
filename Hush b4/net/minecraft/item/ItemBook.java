// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.item;

public class ItemBook extends Item
{
    @Override
    public boolean isItemTool(final ItemStack stack) {
        return stack.stackSize == 1;
    }
    
    @Override
    public int getItemEnchantability() {
        return 1;
    }
}
