package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class ContainerWorkbench extends Container {
   public InventoryCrafting craftMatrix = new InventoryCrafting(this, 3, 3);
   public IInventory craftResult = new InventoryCraftResult();
   private World worldObj;
   private BlockPos pos;

   public ContainerWorkbench(InventoryPlayer playerInventory, World worldIn, BlockPos posIn) {
      this.worldObj = worldIn;
      this.pos = posIn;
      this.addSlotToContainer(new SlotCrafting(playerInventory.player, this.craftMatrix, this.craftResult, 0, 124, 35));

      int l;
      int i1;
      for(l = 0; l < 3; ++l) {
         for(i1 = 0; i1 < 3; ++i1) {
            this.addSlotToContainer(new Slot(this.craftMatrix, i1 + l * 3, 30 + i1 * 18, 17 + l * 18));
         }
      }

      for(l = 0; l < 3; ++l) {
         for(i1 = 0; i1 < 9; ++i1) {
            this.addSlotToContainer(new Slot(playerInventory, i1 + l * 9 + 9, 8 + i1 * 18, 84 + l * 18));
         }
      }

      for(l = 0; l < 9; ++l) {
         this.addSlotToContainer(new Slot(playerInventory, l, 8 + l * 18, 142));
      }

      this.onCraftMatrixChanged(this.craftMatrix);
   }

   public void onCraftMatrixChanged(IInventory inventoryIn) {
      this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.worldObj));
   }

   public void onContainerClosed(EntityPlayer playerIn) {
      super.onContainerClosed(playerIn);
      if (!this.worldObj.isRemote) {
         for(int i = 0; i < 9; ++i) {
            ItemStack itemstack = this.craftMatrix.removeStackFromSlot(i);
            if (itemstack != null) {
               playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
            }
         }
      }

   }

   public boolean canInteractWith(EntityPlayer playerIn) {
      return this.worldObj.getBlockState(this.pos).getBlock() != Blocks.crafting_table ? false : playerIn.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
   }

   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
      ItemStack itemstack = null;
      Slot slot = (Slot)this.inventorySlots.get(index);
      if (slot != null && slot.getHasStack()) {
         ItemStack itemstack1 = slot.getStack();
         itemstack = itemstack1.copy();
         if (index == 0) {
            if (!this.mergeItemStack(itemstack1, 10, 46, true)) {
               return null;
            }

            slot.onSlotChange(itemstack1, itemstack);
         } else if (index >= 10 && index < 37) {
            if (!this.mergeItemStack(itemstack1, 37, 46, false)) {
               return null;
            }
         } else if (index >= 37 && index < 46) {
            if (!this.mergeItemStack(itemstack1, 10, 37, false)) {
               return null;
            }
         } else if (!this.mergeItemStack(itemstack1, 10, 46, false)) {
            return null;
         }

         if (itemstack1.stackSize == 0) {
            slot.putStack((ItemStack)null);
         } else {
            slot.onSlotChanged();
         }

         if (itemstack1.stackSize == itemstack.stackSize) {
            return null;
         }

         slot.onPickupFromSlot(playerIn, itemstack1);
      }

      return itemstack;
   }

   public boolean canMergeSlot(ItemStack stack, Slot p_94530_2_) {
      return p_94530_2_.inventory != this.craftResult && super.canMergeSlot(stack, p_94530_2_);
   }
}
