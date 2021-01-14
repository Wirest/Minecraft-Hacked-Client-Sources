package net.minecraft.inventory;

import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ContainerHorseInventory extends Container {
   private IInventory horseInventory;
   private EntityHorse theHorse;

   public ContainerHorseInventory(IInventory playerInventory, IInventory horseInventoryIn, final EntityHorse horse, EntityPlayer player) {
      this.horseInventory = horseInventoryIn;
      this.theHorse = horse;
      int i = 3;
      horseInventoryIn.openInventory(player);
      int j = (i - 4) * 18;
      this.addSlotToContainer(new Slot(horseInventoryIn, 0, 8, 18) {
         public boolean isItemValid(ItemStack stack) {
            return super.isItemValid(stack) && stack.getItem() == Items.saddle && !this.getHasStack();
         }
      });
      this.addSlotToContainer(new Slot(horseInventoryIn, 1, 8, 36) {
         public boolean isItemValid(ItemStack stack) {
            return super.isItemValid(stack) && horse.canWearArmor() && EntityHorse.isArmorItem(stack.getItem());
         }

         public boolean canBeHovered() {
            return horse.canWearArmor();
         }
      });
      int j1;
      int k1;
      if (horse.isChested()) {
         for(j1 = 0; j1 < i; ++j1) {
            for(k1 = 0; k1 < 5; ++k1) {
               this.addSlotToContainer(new Slot(horseInventoryIn, 2 + k1 + j1 * 5, 80 + k1 * 18, 18 + j1 * 18));
            }
         }
      }

      for(j1 = 0; j1 < 3; ++j1) {
         for(k1 = 0; k1 < 9; ++k1) {
            this.addSlotToContainer(new Slot(playerInventory, k1 + j1 * 9 + 9, 8 + k1 * 18, 102 + j1 * 18 + j));
         }
      }

      for(j1 = 0; j1 < 9; ++j1) {
         this.addSlotToContainer(new Slot(playerInventory, j1, 8 + j1 * 18, 160 + j));
      }

   }

   public boolean canInteractWith(EntityPlayer playerIn) {
      return this.horseInventory.isUseableByPlayer(playerIn) && this.theHorse.isEntityAlive() && this.theHorse.getDistanceToEntity(playerIn) < 8.0F;
   }

   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
      ItemStack itemstack = null;
      Slot slot = (Slot)this.inventorySlots.get(index);
      if (slot != null && slot.getHasStack()) {
         ItemStack itemstack1 = slot.getStack();
         itemstack = itemstack1.copy();
         if (index < this.horseInventory.getSizeInventory()) {
            if (!this.mergeItemStack(itemstack1, this.horseInventory.getSizeInventory(), this.inventorySlots.size(), true)) {
               return null;
            }
         } else if (this.getSlot(1).isItemValid(itemstack1) && !this.getSlot(1).getHasStack()) {
            if (!this.mergeItemStack(itemstack1, 1, 2, false)) {
               return null;
            }
         } else if (this.getSlot(0).isItemValid(itemstack1)) {
            if (!this.mergeItemStack(itemstack1, 0, 1, false)) {
               return null;
            }
         } else if (this.horseInventory.getSizeInventory() <= 2 || !this.mergeItemStack(itemstack1, 2, this.horseInventory.getSizeInventory(), false)) {
            return null;
         }

         if (itemstack1.stackSize == 0) {
            slot.putStack((ItemStack)null);
         } else {
            slot.onSlotChanged();
         }
      }

      return itemstack;
   }

   public void onContainerClosed(EntityPlayer playerIn) {
      super.onContainerClosed(playerIn);
      this.horseInventory.closeInventory(playerIn);
   }
}
