// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.world.World;
import net.minecraft.entity.IMerchant;

public class ContainerMerchant extends Container
{
    private IMerchant theMerchant;
    private InventoryMerchant merchantInventory;
    private final World theWorld;
    
    public ContainerMerchant(final InventoryPlayer playerInventory, final IMerchant merchant, final World worldIn) {
        this.theMerchant = merchant;
        this.theWorld = worldIn;
        this.merchantInventory = new InventoryMerchant(playerInventory.player, merchant);
        this.addSlotToContainer(new Slot(this.merchantInventory, 0, 36, 53));
        this.addSlotToContainer(new Slot(this.merchantInventory, 1, 62, 53));
        this.addSlotToContainer(new SlotMerchantResult(playerInventory.player, merchant, this.merchantInventory, 2, 120, 53));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }
    
    public InventoryMerchant getMerchantInventory() {
        return this.merchantInventory;
    }
    
    @Override
    public void onCraftGuiOpened(final ICrafting listener) {
        super.onCraftGuiOpened(listener);
    }
    
    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
    }
    
    @Override
    public void onCraftMatrixChanged(final IInventory inventoryIn) {
        this.merchantInventory.resetRecipeAndSlots();
        super.onCraftMatrixChanged(inventoryIn);
    }
    
    public void setCurrentRecipeIndex(final int currentRecipeIndex) {
        this.merchantInventory.setCurrentRecipeIndex(currentRecipeIndex);
    }
    
    @Override
    public void updateProgressBar(final int id, final int data) {
    }
    
    @Override
    public boolean canInteractWith(final EntityPlayer playerIn) {
        return this.theMerchant.getCustomer() == playerIn;
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
            else if (index != 0 && index != 1) {
                if (index >= 3 && index < 30) {
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
    
    @Override
    public void onContainerClosed(final EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        this.theMerchant.setCustomer(null);
        super.onContainerClosed(playerIn);
        if (!this.theWorld.isRemote) {
            ItemStack itemstack = this.merchantInventory.removeStackFromSlot(0);
            if (itemstack != null) {
                playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
            }
            itemstack = this.merchantInventory.removeStackFromSlot(1);
            if (itemstack != null) {
                playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
            }
        }
    }
}
