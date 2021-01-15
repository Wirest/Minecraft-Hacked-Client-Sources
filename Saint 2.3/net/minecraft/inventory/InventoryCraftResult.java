package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class InventoryCraftResult implements IInventory {
   private ItemStack[] stackResult = new ItemStack[1];
   private static final String __OBFID = "CL_00001760";

   public int getSizeInventory() {
      return 1;
   }

   public ItemStack getStackInSlot(int slotIn) {
      return this.stackResult[0];
   }

   public String getName() {
      return "Result";
   }

   public boolean hasCustomName() {
      return false;
   }

   public IChatComponent getDisplayName() {
      return (IChatComponent)(this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]));
   }

   public ItemStack decrStackSize(int index, int count) {
      if (this.stackResult[0] != null) {
         ItemStack var3 = this.stackResult[0];
         this.stackResult[0] = null;
         return var3;
      } else {
         return null;
      }
   }

   public ItemStack getStackInSlotOnClosing(int index) {
      if (this.stackResult[0] != null) {
         ItemStack var2 = this.stackResult[0];
         this.stackResult[0] = null;
         return var2;
      } else {
         return null;
      }
   }

   public void setInventorySlotContents(int index, ItemStack stack) {
      this.stackResult[0] = stack;
   }

   public int getInventoryStackLimit() {
      return 64;
   }

   public void markDirty() {
   }

   public boolean isUseableByPlayer(EntityPlayer playerIn) {
      return true;
   }

   public void openInventory(EntityPlayer playerIn) {
   }

   public void closeInventory(EntityPlayer playerIn) {
   }

   public boolean isItemValidForSlot(int index, ItemStack stack) {
      return true;
   }

   public int getField(int id) {
      return 0;
   }

   public void setField(int id, int value) {
   }

   public int getFieldCount() {
      return 0;
   }

   public void clearInventory() {
      for(int var1 = 0; var1 < this.stackResult.length; ++var1) {
         this.stackResult[var1] = null;
      }

   }
}
