package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class ContainerPlayer extends Container {
   public InventoryCrafting craftMatrix = new InventoryCrafting(this, 2, 2);
   public IInventory craftResult = new InventoryCraftResult();
   public boolean isLocalWorld;
   private final EntityPlayer thePlayer;

   public ContainerPlayer(InventoryPlayer playerInventory, boolean localWorld, EntityPlayer player) {
      this.isLocalWorld = localWorld;
      this.thePlayer = player;
      this.addSlotToContainer(new SlotCrafting(playerInventory.player, this.craftMatrix, this.craftResult, 0, 144, 36));

      final int i1;
      int j1;
      for(i1 = 0; i1 < 2; ++i1) {
         for(j1 = 0; j1 < 2; ++j1) {
            this.addSlotToContainer(new Slot(this.craftMatrix, j1 + i1 * 2, 88 + j1 * 18, 26 + i1 * 18));
         }
      }

      for(i1 = 0; i1 < 4; ++i1) {
         this.addSlotToContainer(new Slot(playerInventory, playerInventory.getSizeInventory() - 1 - i1, 8, 8 + i1 * 18) {
            public int getSlotStackLimit() {
               return 1;
            }

            public boolean isItemValid(ItemStack stack) {
               return stack == null ? false : (stack.getItem() instanceof ItemArmor ? ((ItemArmor)stack.getItem()).armorType == i1 : (stack.getItem() != Item.getItemFromBlock(Blocks.pumpkin) && stack.getItem() != Items.skull ? false : i1 == 0));
            }

            public String getSlotTexture() {
               return ItemArmor.EMPTY_SLOT_NAMES[i1];
            }
         });
      }

      for(i1 = 0; i1 < 3; ++i1) {
         for(j1 = 0; j1 < 9; ++j1) {
            this.addSlotToContainer(new Slot(playerInventory, j1 + (i1 + 1) * 9, 8 + j1 * 18, 84 + i1 * 18));
         }
      }

      for(i1 = 0; i1 < 9; ++i1) {
         this.addSlotToContainer(new Slot(playerInventory, i1, 8 + i1 * 18, 142));
      }

      this.onCraftMatrixChanged(this.craftMatrix);
   }

   public void onCraftMatrixChanged(IInventory inventoryIn) {
      this.craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.craftMatrix, this.thePlayer.worldObj));
   }

   public void onContainerClosed(EntityPlayer playerIn) {
      super.onContainerClosed(playerIn);

      for(int i = 0; i < 4; ++i) {
         ItemStack itemstack = this.craftMatrix.removeStackFromSlot(i);
         if (itemstack != null) {
            playerIn.dropPlayerItemWithRandomChoice(itemstack, false);
         }
      }

      this.craftResult.setInventorySlotContents(0, (ItemStack)null);
   }

   public boolean canInteractWith(EntityPlayer playerIn) {
      return true;
   }

   public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
      ItemStack itemstack = null;
      Slot slot = (Slot)this.inventorySlots.get(index);
      if (slot != null && slot.getHasStack()) {
         ItemStack itemstack1 = slot.getStack();
         itemstack = itemstack1.copy();
         if (index == 0) {
            if (!this.mergeItemStack(itemstack1, 9, 45, true)) {
               return null;
            }

            slot.onSlotChange(itemstack1, itemstack);
         } else if (index >= 1 && index < 5) {
            if (!this.mergeItemStack(itemstack1, 9, 45, false)) {
               return null;
            }
         } else if (index >= 5 && index < 9) {
            if (!this.mergeItemStack(itemstack1, 9, 45, false)) {
               return null;
            }
         } else if (itemstack.getItem() instanceof ItemArmor && !((Slot)this.inventorySlots.get(5 + ((ItemArmor)itemstack.getItem()).armorType)).getHasStack()) {
            int i = 5 + ((ItemArmor)itemstack.getItem()).armorType;
            if (!this.mergeItemStack(itemstack1, i, i + 1, false)) {
               return null;
            }
         } else if (index >= 9 && index < 36) {
            if (!this.mergeItemStack(itemstack1, 36, 45, false)) {
               return null;
            }
         } else if (index >= 36 && index < 45) {
            if (!this.mergeItemStack(itemstack1, 9, 36, false)) {
               return null;
            }
         } else if (!this.mergeItemStack(itemstack1, 9, 45, false)) {
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
