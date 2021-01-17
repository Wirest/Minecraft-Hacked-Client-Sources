// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public interface ISidedInventory extends IInventory
{
    int[] getSlotsForFace(final EnumFacing p0);
    
    boolean canInsertItem(final int p0, final ItemStack p1, final EnumFacing p2);
    
    boolean canExtractItem(final int p0, final ItemStack p1, final EnumFacing p2);
}
