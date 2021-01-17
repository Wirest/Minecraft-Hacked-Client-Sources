// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.village.MerchantRecipeList;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.IMerchant;

public class InventoryMerchant implements IInventory
{
    private final IMerchant theMerchant;
    private ItemStack[] theInventory;
    private final EntityPlayer thePlayer;
    private MerchantRecipe currentRecipe;
    private int currentRecipeIndex;
    
    public InventoryMerchant(final EntityPlayer thePlayerIn, final IMerchant theMerchantIn) {
        this.theInventory = new ItemStack[3];
        this.thePlayer = thePlayerIn;
        this.theMerchant = theMerchantIn;
    }
    
    @Override
    public int getSizeInventory() {
        return this.theInventory.length;
    }
    
    @Override
    public ItemStack getStackInSlot(final int index) {
        return this.theInventory[index];
    }
    
    @Override
    public ItemStack decrStackSize(final int index, final int count) {
        if (this.theInventory[index] == null) {
            return null;
        }
        if (index == 2) {
            final ItemStack itemstack2 = this.theInventory[index];
            this.theInventory[index] = null;
            return itemstack2;
        }
        if (this.theInventory[index].stackSize <= count) {
            final ItemStack itemstack3 = this.theInventory[index];
            this.theInventory[index] = null;
            if (this.inventoryResetNeededOnSlotChange(index)) {
                this.resetRecipeAndSlots();
            }
            return itemstack3;
        }
        final ItemStack itemstack4 = this.theInventory[index].splitStack(count);
        if (this.theInventory[index].stackSize == 0) {
            this.theInventory[index] = null;
        }
        if (this.inventoryResetNeededOnSlotChange(index)) {
            this.resetRecipeAndSlots();
        }
        return itemstack4;
    }
    
    private boolean inventoryResetNeededOnSlotChange(final int p_70469_1_) {
        return p_70469_1_ == 0 || p_70469_1_ == 1;
    }
    
    @Override
    public ItemStack removeStackFromSlot(final int index) {
        if (this.theInventory[index] != null) {
            final ItemStack itemstack = this.theInventory[index];
            this.theInventory[index] = null;
            return itemstack;
        }
        return null;
    }
    
    @Override
    public void setInventorySlotContents(final int index, final ItemStack stack) {
        this.theInventory[index] = stack;
        if (stack != null && stack.stackSize > this.getInventoryStackLimit()) {
            stack.stackSize = this.getInventoryStackLimit();
        }
        if (this.inventoryResetNeededOnSlotChange(index)) {
            this.resetRecipeAndSlots();
        }
    }
    
    @Override
    public String getName() {
        return "mob.villager";
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
    public int getInventoryStackLimit() {
        return 64;
    }
    
    @Override
    public boolean isUseableByPlayer(final EntityPlayer player) {
        return this.theMerchant.getCustomer() == player;
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
    public void markDirty() {
        this.resetRecipeAndSlots();
    }
    
    public void resetRecipeAndSlots() {
        this.currentRecipe = null;
        ItemStack itemstack = this.theInventory[0];
        ItemStack itemstack2 = this.theInventory[1];
        if (itemstack == null) {
            itemstack = itemstack2;
            itemstack2 = null;
        }
        if (itemstack == null) {
            this.setInventorySlotContents(2, null);
        }
        else {
            final MerchantRecipeList merchantrecipelist = this.theMerchant.getRecipes(this.thePlayer);
            if (merchantrecipelist != null) {
                MerchantRecipe merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack, itemstack2, this.currentRecipeIndex);
                if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {
                    this.currentRecipe = merchantrecipe;
                    this.setInventorySlotContents(2, merchantrecipe.getItemToSell().copy());
                }
                else if (itemstack2 != null) {
                    merchantrecipe = merchantrecipelist.canRecipeBeUsed(itemstack2, itemstack, this.currentRecipeIndex);
                    if (merchantrecipe != null && !merchantrecipe.isRecipeDisabled()) {
                        this.currentRecipe = merchantrecipe;
                        this.setInventorySlotContents(2, merchantrecipe.getItemToSell().copy());
                    }
                    else {
                        this.setInventorySlotContents(2, null);
                    }
                }
                else {
                    this.setInventorySlotContents(2, null);
                }
            }
        }
        this.theMerchant.verifySellingItem(this.getStackInSlot(2));
    }
    
    public MerchantRecipe getCurrentRecipe() {
        return this.currentRecipe;
    }
    
    public void setCurrentRecipeIndex(final int currentRecipeIndexIn) {
        this.currentRecipeIndex = currentRecipeIndexIn;
        this.resetRecipeAndSlots();
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
        for (int i = 0; i < this.theInventory.length; ++i) {
            this.theInventory[i] = null;
        }
    }
}
