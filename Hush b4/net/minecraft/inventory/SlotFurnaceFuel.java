// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.init.Items;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.item.ItemStack;

public class SlotFurnaceFuel extends Slot
{
    public SlotFurnaceFuel(final IInventory inventoryIn, final int slotIndex, final int xPosition, final int yPosition) {
        super(inventoryIn, slotIndex, xPosition, yPosition);
    }
    
    @Override
    public boolean isItemValid(final ItemStack stack) {
        return TileEntityFurnace.isItemFuel(stack) || isBucket(stack);
    }
    
    @Override
    public int getItemStackLimit(final ItemStack stack) {
        return isBucket(stack) ? 1 : super.getItemStackLimit(stack);
    }
    
    public static boolean isBucket(final ItemStack stack) {
        return stack != null && stack.getItem() != null && stack.getItem() == Items.bucket;
    }
}
