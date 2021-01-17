// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerBeacon extends Container
{
    private IInventory tileBeacon;
    private final BeaconSlot beaconSlot;
    
    public ContainerBeacon(final IInventory playerInventory, final IInventory tileBeaconIn) {
        this.tileBeacon = tileBeaconIn;
        this.addSlotToContainer(this.beaconSlot = new BeaconSlot(tileBeaconIn, 0, 136, 110));
        final int i = 36;
        final int j = 137;
        for (int k = 0; k < 3; ++k) {
            for (int l = 0; l < 9; ++l) {
                this.addSlotToContainer(new Slot(playerInventory, l + k * 9 + 9, i + l * 18, j + k * 18));
            }
        }
        for (int i2 = 0; i2 < 9; ++i2) {
            this.addSlotToContainer(new Slot(playerInventory, i2, i + i2 * 18, 58 + j));
        }
    }
    
    @Override
    public void onCraftGuiOpened(final ICrafting listener) {
        super.onCraftGuiOpened(listener);
        listener.func_175173_a(this, this.tileBeacon);
    }
    
    @Override
    public void updateProgressBar(final int id, final int data) {
        this.tileBeacon.setField(id, data);
    }
    
    public IInventory func_180611_e() {
        return this.tileBeacon;
    }
    
    @Override
    public void onContainerClosed(final EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        if (playerIn != null && !playerIn.worldObj.isRemote) {
            final ItemStack itemstack = this.beaconSlot.decrStackSize(this.beaconSlot.getSlotStackLimit());
            if (itemstack != null) {
                playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
            }
        }
    }
    
    @Override
    public boolean canInteractWith(final EntityPlayer playerIn) {
        return this.tileBeacon.isUseableByPlayer(playerIn);
    }
    
    @Override
    public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
        ItemStack itemstack = null;
        final Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            final ItemStack itemstack2 = slot.getStack();
            itemstack = itemstack2.copy();
            if (index == 0) {
                if (!this.mergeItemStack(itemstack2, 1, 37, true)) {
                    return null;
                }
                slot.onSlotChange(itemstack2, itemstack);
            }
            else if (!this.beaconSlot.getHasStack() && this.beaconSlot.isItemValid(itemstack2) && itemstack2.stackSize == 1) {
                if (!this.mergeItemStack(itemstack2, 0, 1, false)) {
                    return null;
                }
            }
            else if (index >= 1 && index < 28) {
                if (!this.mergeItemStack(itemstack2, 28, 37, false)) {
                    return null;
                }
            }
            else if (index >= 28 && index < 37) {
                if (!this.mergeItemStack(itemstack2, 1, 28, false)) {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack2, 1, 37, false)) {
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
    
    class BeaconSlot extends Slot
    {
        public BeaconSlot(final IInventory p_i1801_2_, final int p_i1801_3_, final int p_i1801_4_, final int p_i1801_5_) {
            super(p_i1801_2_, p_i1801_3_, p_i1801_4_, p_i1801_5_);
        }
        
        @Override
        public boolean isItemValid(final ItemStack stack) {
            return stack != null && (stack.getItem() == Items.emerald || stack.getItem() == Items.diamond || stack.getItem() == Items.gold_ingot || stack.getItem() == Items.iron_ingot);
        }
        
        @Override
        public int getSlotStackLimit() {
            return 1;
        }
    }
}
