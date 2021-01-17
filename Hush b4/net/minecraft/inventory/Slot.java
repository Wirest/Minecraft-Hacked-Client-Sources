// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class Slot
{
    private final int slotIndex;
    public final IInventory inventory;
    public int slotNumber;
    public int xDisplayPosition;
    public int yDisplayPosition;
    
    public Slot(final IInventory inventoryIn, final int index, final int xPosition, final int yPosition) {
        this.inventory = inventoryIn;
        this.slotIndex = index;
        this.xDisplayPosition = xPosition;
        this.yDisplayPosition = yPosition;
    }
    
    public void onSlotChange(final ItemStack p_75220_1_, final ItemStack p_75220_2_) {
        if (p_75220_1_ != null && p_75220_2_ != null && p_75220_1_.getItem() == p_75220_2_.getItem()) {
            final int i = p_75220_2_.stackSize - p_75220_1_.stackSize;
            if (i > 0) {
                this.onCrafting(p_75220_1_, i);
            }
        }
    }
    
    protected void onCrafting(final ItemStack stack, final int amount) {
    }
    
    protected void onCrafting(final ItemStack stack) {
    }
    
    public void onPickupFromSlot(final EntityPlayer playerIn, final ItemStack stack) {
        this.onSlotChanged();
    }
    
    public boolean isItemValid(final ItemStack stack) {
        return true;
    }
    
    public ItemStack getStack() {
        return this.inventory.getStackInSlot(this.slotIndex);
    }
    
    public boolean getHasStack() {
        return this.getStack() != null;
    }
    
    public void putStack(final ItemStack stack) {
        this.inventory.setInventorySlotContents(this.slotIndex, stack);
        this.onSlotChanged();
    }
    
    public void onSlotChanged() {
        this.inventory.markDirty();
    }
    
    public int getSlotStackLimit() {
        return this.inventory.getInventoryStackLimit();
    }
    
    public int getItemStackLimit(final ItemStack stack) {
        return this.getSlotStackLimit();
    }
    
    public String getSlotTexture() {
        return null;
    }
    
    public ItemStack decrStackSize(final int amount) {
        return this.inventory.decrStackSize(this.slotIndex, amount);
    }
    
    public boolean isHere(final IInventory inv, final int slotIn) {
        return inv == this.inventory && slotIn == this.slotIndex;
    }
    
    public boolean canTakeStack(final EntityPlayer playerIn) {
        return true;
    }
    
    public boolean canBeHovered() {
        return true;
    }
}
