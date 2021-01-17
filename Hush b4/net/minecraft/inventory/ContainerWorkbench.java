// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.inventory;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ContainerWorkbench extends Container
{
    public InventoryCrafting craftMatrix;
    public IInventory craftResult;
    private World worldObj;
    private BlockPos pos;
    
    public ContainerWorkbench(final InventoryPlayer playerInventory, final World worldIn, final BlockPos posIn) {
        this.craftMatrix = new InventoryCrafting(this, 3, 3);
        this.craftResult = new InventoryCraftResult();
        this.worldObj = worldIn;
        this.pos = posIn;
        this.addSlotToContainer(new SlotCrafting(playerInventory.player, this.craftMatrix, this.craftResult, 0, 124, 35));
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.addSlotToContainer(new Slot(this.craftMatrix, j + i * 3, 30 + j * 18, 17 + i * 18));
            }
        }
        for (int k = 0; k < 3; ++k) {
            for (int i2 = 0; i2 < 9; ++i2) {
                this.addSlotToContainer(new Slot(playerInventory, i2 + k * 9 + 9, 8 + i2 * 18, 84 + k * 18));
            }
        }
        for (int l = 0; l < 9; ++l) {
            this.addSlotToContainer(new Slot(playerInventory, l, 8 + l * 18, 142));
        }
        this.onCraftMatrixChanged(this.craftMatrix);
    }
    
    @Override
    public void onCraftMatrixChanged(final IInventory inventoryIn) {
        this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.worldObj));
    }
    
    @Override
    public void onContainerClosed(final EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        if (!this.worldObj.isRemote) {
            for (int i = 0; i < 9; ++i) {
                final ItemStack itemstack = this.craftMatrix.removeStackFromSlot(i);
                if (itemstack != null) {
                    playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
                }
            }
        }
    }
    
    @Override
    public boolean canInteractWith(final EntityPlayer playerIn) {
        return this.worldObj.getBlockState(this.pos).getBlock() == Blocks.crafting_table && playerIn.getDistanceSq(this.pos.getX() + 0.5, this.pos.getY() + 0.5, this.pos.getZ() + 0.5) <= 64.0;
    }
    
    @Override
    public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
        ItemStack itemstack = null;
        final Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            final ItemStack itemstack2 = slot.getStack();
            itemstack = itemstack2.copy();
            if (index == 0) {
                if (!this.mergeItemStack(itemstack2, 10, 46, true)) {
                    return null;
                }
                slot.onSlotChange(itemstack2, itemstack);
            }
            else if (index >= 10 && index < 37) {
                if (!this.mergeItemStack(itemstack2, 37, 46, false)) {
                    return null;
                }
            }
            else if (index >= 37 && index < 46) {
                if (!this.mergeItemStack(itemstack2, 10, 37, false)) {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack2, 10, 46, false)) {
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
