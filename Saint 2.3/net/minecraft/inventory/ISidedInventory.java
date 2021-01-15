package net.minecraft.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public interface ISidedInventory extends IInventory {
   int[] getSlotsForFace(EnumFacing var1);

   boolean canInsertItem(int var1, ItemStack var2, EnumFacing var3);

   boolean canExtractItem(int var1, ItemStack var2, EnumFacing var3);
}
