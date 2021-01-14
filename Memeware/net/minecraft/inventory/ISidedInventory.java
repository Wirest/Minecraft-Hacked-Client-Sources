package net.minecraft.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public interface ISidedInventory extends IInventory {
    int[] getSlotsForFace(EnumFacing var1);

    /**
     * Returns true if automation can insert the given item in the given slot from the given side. Args: slot, item,
     * side
     */
    boolean canInsertItem(int var1, ItemStack var2, EnumFacing var3);

    /**
     * Returns true if automation can extract the given item in the given slot from the given side. Args: slot, item,
     * side
     */
    boolean canExtractItem(int var1, ItemStack var2, EnumFacing var3);
}
