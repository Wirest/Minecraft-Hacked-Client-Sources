// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerPlayer extends Container
{
    public InventoryCrafting craftMatrix;
    public IInventory craftResult;
    public boolean isLocalWorld;
    private final EntityPlayer thePlayer;
    
    public ContainerPlayer(final InventoryPlayer playerInventory, final boolean localWorld, final EntityPlayer player) {
        this.craftMatrix = new InventoryCrafting(this, 2, 2);
        this.craftResult = new InventoryCraftResult();
        this.isLocalWorld = localWorld;
        this.thePlayer = player;
        this.addSlotToContainer(new SlotCrafting(playerInventory.player, this.craftMatrix, this.craftResult, 0, 144, 36));
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                this.addSlotToContainer(new Slot(this.craftMatrix, j + i * 2, 88 + j * 18, 26 + i * 18));
            }
        }
        for (int k = 0; k < 4; ++k) {
            final int k_f = k;
            this.addSlotToContainer(new Slot(playerInventory, playerInventory.getSizeInventory() - 1 - k, 8, 8 + k * 18) {
                @Override
                public int getSlotStackLimit() {
                    return 1;
                }
                
                @Override
                public boolean isItemValid(final ItemStack stack) {
                    return stack != null && ((stack.getItem() instanceof ItemArmor) ? (((ItemArmor)stack.getItem()).armorType == k_f) : ((stack.getItem() == Item.getItemFromBlock(Blocks.pumpkin) || stack.getItem() == Items.skull) && k_f == 0));
                }
                
                @Override
                public String getSlotTexture() {
                    return ItemArmor.EMPTY_SLOT_NAMES[k_f];
                }
            });
        }
        for (int l = 0; l < 3; ++l) {
            for (int j2 = 0; j2 < 9; ++j2) {
                this.addSlotToContainer(new Slot(playerInventory, j2 + (l + 1) * 9, 8 + j2 * 18, 84 + l * 18));
            }
        }
        for (int i2 = 0; i2 < 9; ++i2) {
            this.addSlotToContainer(new Slot(playerInventory, i2, 8 + i2 * 18, 142));
        }
        this.onCraftMatrixChanged(this.craftMatrix);
    }
    
    @Override
    public void onCraftMatrixChanged(final IInventory inventoryIn) {
        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.thePlayer.worldObj));
    }
    
    @Override
    public void onContainerClosed(final EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        for (int i = 0; i < 4; ++i) {
            final ItemStack itemstack = this.craftMatrix.removeStackFromSlot(i);
            if (itemstack != null) {
                playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
            }
        }
        this.craftResult.setInventorySlotContents(0, null);
    }
    
    @Override
    public boolean canInteractWith(final EntityPlayer playerIn) {
        return true;
    }
    
    @Override
    public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
        ItemStack itemstack = null;
        final Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            final ItemStack itemstack2 = slot.getStack();
            itemstack = itemstack2.copy();
            if (index == 0) {
                if (!this.mergeItemStack(itemstack2, 9, 45, true)) {
                    return null;
                }
                slot.onSlotChange(itemstack2, itemstack);
            }
            else if (index >= 1 && index < 5) {
                if (!this.mergeItemStack(itemstack2, 9, 45, false)) {
                    return null;
                }
            }
            else if (index >= 5 && index < 9) {
                if (!this.mergeItemStack(itemstack2, 9, 45, false)) {
                    return null;
                }
            }
            else if (itemstack.getItem() instanceof ItemArmor && !this.inventorySlots.get(5 + ((ItemArmor)itemstack.getItem()).armorType).getHasStack()) {
                final int i = 5 + ((ItemArmor)itemstack.getItem()).armorType;
                if (!this.mergeItemStack(itemstack2, i, i + 1, false)) {
                    return null;
                }
            }
            else if (index >= 9 && index < 36) {
                if (!this.mergeItemStack(itemstack2, 36, 45, false)) {
                    return null;
                }
            }
            else if (index >= 36 && index < 45) {
                if (!this.mergeItemStack(itemstack2, 9, 36, false)) {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack2, 9, 45, false)) {
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
    public boolean canMergeSlot(final ItemStack stack, final Slot p_94530_2_) {
        return p_94530_2_.inventory != this.craftResult && super.canMergeSlot(stack, p_94530_2_);
    }
}
