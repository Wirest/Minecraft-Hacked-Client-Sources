package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IWorldNameable;

public interface IInventory extends IWorldNameable {
   int getSizeInventory();

   ItemStack getStackInSlot(int var1);

   ItemStack decrStackSize(int var1, int var2);

   ItemStack removeStackFromSlot(int var1);

   void setInventorySlotContents(int var1, ItemStack var2);

   int getInventoryStackLimit();

   void markDirty();

   boolean isUseableByPlayer(EntityPlayer var1);

   void openInventory(EntityPlayer var1);

   void closeInventory(EntityPlayer var1);

   boolean isItemValidForSlot(int var1, ItemStack var2);

   int getField(int var1);

   void setField(int var1, int var2);

   int getFieldCount();

   void clear();
}
