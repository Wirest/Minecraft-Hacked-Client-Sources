// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerHopper extends Container
{
    private final IInventory hopperInventory;
    
    public ContainerHopper(final InventoryPlayer playerInventory, final IInventory hopperInventoryIn, final EntityPlayer player) {
        (this.hopperInventory = hopperInventoryIn).openInventory(player);
        final int i = 51;
        for (int j = 0; j < hopperInventoryIn.getSizeInventory(); ++j) {
            this.addSlotToContainer(new Slot(hopperInventoryIn, j, 44 + j * 18, 20));
        }
        for (int l = 0; l < 3; ++l) {
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(playerInventory, k + l * 9 + 9, 8 + k * 18, l * 18 + i));
            }
        }
        for (int i2 = 0; i2 < 9; ++i2) {
            this.addSlotToContainer(new Slot(playerInventory, i2, 8 + i2 * 18, 58 + i));
        }
    }
    
    @Override
    public boolean canInteractWith(final EntityPlayer playerIn) {
        return this.hopperInventory.isUseableByPlayer(playerIn);
    }
    
    @Override
    public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
        ItemStack itemstack = null;
        final Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            final ItemStack itemstack2 = slot.getStack();
            itemstack = itemstack2.copy();
            if (index < this.hopperInventory.getSizeInventory()) {
                if (!this.mergeItemStack(itemstack2, this.hopperInventory.getSizeInventory(), this.inventorySlots.size(), true)) {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack2, 0, this.hopperInventory.getSizeInventory(), false)) {
                return null;
            }
            if (itemstack2.stackSize == 0) {
                slot.putStack(null);
            }
            else {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }
    
    @Override
    public void onContainerClosed(final EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        this.hopperInventory.closeInventory(playerIn);
    }
}
