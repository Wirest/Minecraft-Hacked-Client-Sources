// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;

public class ContainerFurnace extends Container
{
    private final IInventory tileFurnace;
    private int field_178152_f;
    private int field_178153_g;
    private int field_178154_h;
    private int field_178155_i;
    
    public ContainerFurnace(final InventoryPlayer playerInventory, final IInventory furnaceInventory) {
        this.tileFurnace = furnaceInventory;
        this.addSlotToContainer(new Slot(furnaceInventory, 0, 56, 17));
        this.addSlotToContainer(new SlotFurnaceFuel(furnaceInventory, 1, 56, 53));
        this.addSlotToContainer(new SlotFurnaceOutput(playerInventory.player, furnaceInventory, 2, 116, 35));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }
    
    @Override
    public void onCraftGuiOpened(final ICrafting listener) {
        super.onCraftGuiOpened(listener);
        listener.func_175173_a(this, this.tileFurnace);
    }
    
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (int i = 0; i < this.crafters.size(); ++i) {
            final ICrafting icrafting = this.crafters.get(i);
            if (this.field_178152_f != this.tileFurnace.getField(2)) {
                icrafting.sendProgressBarUpdate(this, 2, this.tileFurnace.getField(2));
            }
            if (this.field_178154_h != this.tileFurnace.getField(0)) {
                icrafting.sendProgressBarUpdate(this, 0, this.tileFurnace.getField(0));
            }
            if (this.field_178155_i != this.tileFurnace.getField(1)) {
                icrafting.sendProgressBarUpdate(this, 1, this.tileFurnace.getField(1));
            }
            if (this.field_178153_g != this.tileFurnace.getField(3)) {
                icrafting.sendProgressBarUpdate(this, 3, this.tileFurnace.getField(3));
            }
        }
        this.field_178152_f = this.tileFurnace.getField(2);
        this.field_178154_h = this.tileFurnace.getField(0);
        this.field_178155_i = this.tileFurnace.getField(1);
        this.field_178153_g = this.tileFurnace.getField(3);
    }
    
    @Override
    public void updateProgressBar(final int id, final int data) {
        this.tileFurnace.setField(id, data);
    }
    
    @Override
    public boolean canInteractWith(final EntityPlayer playerIn) {
        return this.tileFurnace.isUseableByPlayer(playerIn);
    }
    
    @Override
    public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
        ItemStack itemstack = null;
        final Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            final ItemStack itemstack2 = slot.getStack();
            itemstack = itemstack2.copy();
            if (index == 2) {
                if (!this.mergeItemStack(itemstack2, 3, 39, true)) {
                    return null;
                }
                slot.onSlotChange(itemstack2, itemstack);
            }
            else if (index != 1 && index != 0) {
                if (FurnaceRecipes.instance().getSmeltingResult(itemstack2) != null) {
                    if (!this.mergeItemStack(itemstack2, 0, 1, false)) {
                        return null;
                    }
                }
                else if (TileEntityFurnace.isItemFuel(itemstack2)) {
                    if (!this.mergeItemStack(itemstack2, 1, 2, false)) {
                        return null;
                    }
                }
                else if (index >= 3 && index < 30) {
                    if (!this.mergeItemStack(itemstack2, 30, 39, false)) {
                        return null;
                    }
                }
                else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack2, 3, 30, false)) {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack2, 3, 39, false)) {
                return null;
            }
            if (itemstack2.stackSize == 0) {
                slot.putStack(null);
            }
            else {
                slot.onSlotChanged();
            }
            if (itemstack2.stackSize == itemstack.stackSize) {
                return null;
            }
            slot.onPickupFromSlot(playerIn, itemstack2);
        }
        return itemstack;
    }
}
