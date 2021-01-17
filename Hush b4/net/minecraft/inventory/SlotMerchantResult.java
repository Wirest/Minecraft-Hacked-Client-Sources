// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.village.MerchantRecipe;
import net.minecraft.stats.StatList;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.IMerchant;
import net.minecraft.entity.player.EntityPlayer;

public class SlotMerchantResult extends Slot
{
    private final InventoryMerchant theMerchantInventory;
    private EntityPlayer thePlayer;
    private int field_75231_g;
    private final IMerchant theMerchant;
    
    public SlotMerchantResult(final EntityPlayer player, final IMerchant merchant, final InventoryMerchant merchantInventory, final int slotIndex, final int xPosition, final int yPosition) {
        super(merchantInventory, slotIndex, xPosition, yPosition);
        this.thePlayer = player;
        this.theMerchant = merchant;
        this.theMerchantInventory = merchantInventory;
    }
    
    @Override
    public boolean isItemValid(final ItemStack stack) {
        return false;
    }
    
    @Override
    public ItemStack decrStackSize(final int amount) {
        if (this.getHasStack()) {
            this.field_75231_g += Math.min(amount, this.getStack().stackSize);
        }
        return super.decrStackSize(amount);
    }
    
    @Override
    protected void onCrafting(final ItemStack stack, final int amount) {
        this.field_75231_g += amount;
        this.onCrafting(stack);
    }
    
    @Override
    protected void onCrafting(final ItemStack stack) {
        stack.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.field_75231_g);
        this.field_75231_g = 0;
    }
    
    @Override
    public void onPickupFromSlot(final EntityPlayer playerIn, final ItemStack stack) {
        this.onCrafting(stack);
        final MerchantRecipe merchantrecipe = this.theMerchantInventory.getCurrentRecipe();
        if (merchantrecipe != null) {
            ItemStack itemstack = this.theMerchantInventory.getStackInSlot(0);
            ItemStack itemstack2 = this.theMerchantInventory.getStackInSlot(1);
            if (this.doTrade(merchantrecipe, itemstack, itemstack2) || this.doTrade(merchantrecipe, itemstack2, itemstack)) {
                this.theMerchant.useRecipe(merchantrecipe);
                playerIn.triggerAchievement(StatList.timesTradedWithVillagerStat);
                if (itemstack != null && itemstack.stackSize <= 0) {
                    itemstack = null;
                }
                if (itemstack2 != null && itemstack2.stackSize <= 0) {
                    itemstack2 = null;
                }
                this.theMerchantInventory.setInventorySlotContents(0, itemstack);
                this.theMerchantInventory.setInventorySlotContents(1, itemstack2);
            }
        }
    }
    
    private boolean doTrade(final MerchantRecipe trade, final ItemStack firstItem, final ItemStack secondItem) {
        final ItemStack itemstack = trade.getItemToBuy();
        final ItemStack itemstack2 = trade.getSecondItemToBuy();
        if (firstItem != null && firstItem.getItem() == itemstack.getItem()) {
            if (itemstack2 != null && secondItem != null && itemstack2.getItem() == secondItem.getItem()) {
                firstItem.stackSize -= itemstack.stackSize;
                secondItem.stackSize -= itemstack2.stackSize;
                return true;
            }
            if (itemstack2 == null && secondItem == null) {
                firstItem.stackSize -= itemstack.stackSize;
                return true;
            }
        }
        return false;
    }
}
