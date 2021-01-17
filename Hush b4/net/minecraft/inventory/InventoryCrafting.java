// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.item.ItemStack;

public class InventoryCrafting implements IInventory
{
    private final ItemStack[] stackList;
    private final int inventoryWidth;
    private final int inventoryHeight;
    private final Container eventHandler;
    
    public InventoryCrafting(final Container eventHandlerIn, final int width, final int height) {
        final int i = width * height;
        this.stackList = new ItemStack[i];
        this.eventHandler = eventHandlerIn;
        this.inventoryWidth = width;
        this.inventoryHeight = height;
    }
    
    @Override
    public int getSizeInventory() {
        return this.stackList.length;
    }
    
    @Override
    public ItemStack getStackInSlot(final int index) {
        return (index >= this.getSizeInventory()) ? null : this.stackList[index];
    }
    
    public ItemStack getStackInRowAndColumn(final int row, final int column) {
        return (row >= 0 && row < this.inventoryWidth && column >= 0 && column <= this.inventoryHeight) ? this.getStackInSlot(row + column * this.inventoryWidth) : null;
    }
    
    @Override
    public String getName() {
        return "container.crafting";
    }
    
    @Override
    public boolean hasCustomName() {
        return false;
    }
    
    @Override
    public IChatComponent getDisplayName() {
        return this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]);
    }
    
    @Override
    public ItemStack removeStackFromSlot(final int index) {
        if (this.stackList[index] != null) {
            final ItemStack itemstack = this.stackList[index];
            this.stackList[index] = null;
            return itemstack;
        }
        return null;
    }
    
    @Override
    public ItemStack decrStackSize(final int index, final int count) {
        if (this.stackList[index] == null) {
            return null;
        }
        if (this.stackList[index].stackSize <= count) {
            final ItemStack itemstack1 = this.stackList[index];
            this.stackList[index] = null;
            this.eventHandler.onCraftMatrixChanged(this);
            return itemstack1;
        }
        final ItemStack itemstack2 = this.stackList[index].splitStack(count);
        if (this.stackList[index].stackSize == 0) {
            this.stackList[index] = null;
        }
        this.eventHandler.onCraftMatrixChanged(this);
        return itemstack2;
    }
    
    @Override
    public void setInventorySlotContents(final int index, final ItemStack stack) {
        this.stackList[index] = stack;
        this.eventHandler.onCraftMatrixChanged(this);
    }
    
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }
    
    @Override
    public void markDirty() {
    }
    
    @Override
    public boolean isUseableByPlayer(final EntityPlayer player) {
        return true;
    }
    
    @Override
    public void openInventory(final EntityPlayer player) {
    }
    
    @Override
    public void closeInventory(final EntityPlayer player) {
    }
    
    @Override
    public boolean isItemValidForSlot(final int index, final ItemStack stack) {
        return true;
    }
    
    @Override
    public int getField(final int id) {
        return 0;
    }
    
    @Override
    public void setField(final int id, final int value) {
    }
    
    @Override
    public int getFieldCount() {
        return 0;
    }
    
    @Override
    public void clear() {
        for (int i = 0; i < this.stackList.length; ++i) {
            this.stackList[i] = null;
        }
    }
    
    public int getHeight() {
        return this.inventoryHeight;
    }
    
    public int getWidth() {
        return this.inventoryWidth;
    }
}
