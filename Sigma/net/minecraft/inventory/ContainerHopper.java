package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class ContainerHopper extends Container {
    private final IInventory field_94538_a;
    private static final String __OBFID = "CL_00001750";

    public ContainerHopper(InventoryPlayer p_i45792_1_, IInventory p_i45792_2_, EntityPlayer p_i45792_3_) {
        field_94538_a = p_i45792_2_;
        p_i45792_2_.openInventory(p_i45792_3_);
        byte var4 = 51;
        int var5;

        for (var5 = 0; var5 < p_i45792_2_.getSizeInventory(); ++var5) {
            addSlotToContainer(new Slot(p_i45792_2_, var5, 44 + var5 * 18, 20));
        }

        for (var5 = 0; var5 < 3; ++var5) {
            for (int var6 = 0; var6 < 9; ++var6) {
                addSlotToContainer(new Slot(p_i45792_1_, var6 + var5 * 9 + 9, 8 + var6 * 18, var5 * 18 + var4));
            }
        }

        for (var5 = 0; var5 < 9; ++var5) {
            addSlotToContainer(new Slot(p_i45792_1_, var5, 8 + var5 * 18, 58 + var4));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return field_94538_a.isUseableByPlayer(playerIn);
    }

    /**
     * Take a stack from the specified inventory slot.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack var3 = null;
        Slot var4 = (Slot) inventorySlots.get(index);

        if (var4 != null && var4.getHasStack()) {
            ItemStack var5 = var4.getStack();
            var3 = var5.copy();

            if (index < field_94538_a.getSizeInventory()) {
                if (!mergeItemStack(var5, field_94538_a.getSizeInventory(), inventorySlots.size(), true)) {
                    return null;
                }
            } else if (!mergeItemStack(var5, 0, field_94538_a.getSizeInventory(), false)) {
                return null;
            }

            if (var5.stackSize == 0) {
                var4.putStack((ItemStack) null);
            } else {
                var4.onSlotChanged();
            }
        }

        return var3;
    }

    /**
     * Called when the container is closed.
     */
    @Override
    public void onContainerClosed(EntityPlayer p_75134_1_) {
        super.onContainerClosed(p_75134_1_);
        field_94538_a.closeInventory(p_75134_1_);
    }
}
