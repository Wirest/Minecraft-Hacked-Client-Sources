// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.item.ItemStack;
import java.util.List;

public interface ICrafting
{
    void updateCraftingInventory(final Container p0, final List<ItemStack> p1);
    
    void sendSlotContents(final Container p0, final int p1, final ItemStack p2);
    
    void sendProgressBarUpdate(final Container p0, final int p1, final int p2);
    
    void func_175173_a(final Container p0, final IInventory p1);
}
