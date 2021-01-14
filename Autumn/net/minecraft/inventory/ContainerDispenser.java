package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ContainerDispenser extends Container {
   private IInventory dispenserInventory;

   public ContainerDispenser(IInventory playerInventory, IInventory dispenserInventoryIn) {
      this.dispenserInventory = dispenserInventoryIn;

      int l;
      int i1;
      for(l = 0; l < 3; ++l) {
         for(i1 = 0; i1 < 3; ++i1) {
            this.addSlotToContainer(new Slot(dispenserInventoryIn, i1 + l * 3, 62 + i1 * 18, 17 + l * 18));
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

   }

   public boolean canInteractWith(EntityPlayer playerIn) {
      return this.dispenserInventory.isUseableByPlayer(playerIn);
   }

   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
      ItemStack itemstack = null;
      Slot slot = (Slot)this.inventorySlots.get(index);
      if (slot != null && slot.getHasStack()) {
         ItemStack itemstack1 = slot.getStack();
         itemstack = itemstack1.copy();
         if (index < 9) {
            if (!this.mergeItemStack(itemstack1, 9, 45, true)) {
               return null;
            }
         } else if (!this.mergeItemStack(itemstack1, 0, 9, false)) {
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
}
