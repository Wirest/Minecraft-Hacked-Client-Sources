package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.LockCode;

public class InventoryLargeChest implements ILockableContainer {
   private String name;
   private ILockableContainer upperChest;
   private ILockableContainer lowerChest;
   private static final String __OBFID = "CL_00001507";

   public InventoryLargeChest(String p_i45905_1_, ILockableContainer p_i45905_2_, ILockableContainer p_i45905_3_) {
      this.name = p_i45905_1_;
      if (p_i45905_2_ == null) {
         p_i45905_2_ = p_i45905_3_;
      }

      if (p_i45905_3_ == null) {
         p_i45905_3_ = p_i45905_2_;
      }

      this.upperChest = p_i45905_2_;
      this.lowerChest = p_i45905_3_;
      if (p_i45905_2_.isLocked()) {
         p_i45905_3_.setLockCode(p_i45905_2_.getLockCode());
      } else if (p_i45905_3_.isLocked()) {
         p_i45905_2_.setLockCode(p_i45905_3_.getLockCode());
      }

   }

   public int getSizeInventory() {
      return this.upperChest.getSizeInventory() + this.lowerChest.getSizeInventory();
   }

   public boolean isPartOfLargeChest(IInventory p_90010_1_) {
      return this.upperChest == p_90010_1_ || this.lowerChest == p_90010_1_;
   }

   public String getName() {
      return this.upperChest.hasCustomName() ? this.upperChest.getName() : (this.lowerChest.hasCustomName() ? this.lowerChest.getName() : this.name);
   }

   public boolean hasCustomName() {
      return this.upperChest.hasCustomName() || this.lowerChest.hasCustomName();
   }

   public IChatComponent getDisplayName() {
      return (IChatComponent)(this.hasCustomName() ? new ChatComponentText(this.getName()) : new ChatComponentTranslation(this.getName(), new Object[0]));
   }

   public ItemStack getStackInSlot(int slotIn) {
      return slotIn >= this.upperChest.getSizeInventory() ? this.lowerChest.getStackInSlot(slotIn - this.upperChest.getSizeInventory()) : this.upperChest.getStackInSlot(slotIn);
   }

   public ItemStack decrStackSize(int index, int count) {
      return index >= this.upperChest.getSizeInventory() ? this.lowerChest.decrStackSize(index - this.upperChest.getSizeInventory(), count) : this.upperChest.decrStackSize(index, count);
   }

   public ItemStack getStackInSlotOnClosing(int index) {
      return index >= this.upperChest.getSizeInventory() ? this.lowerChest.getStackInSlotOnClosing(index - this.upperChest.getSizeInventory()) : this.upperChest.getStackInSlotOnClosing(index);
   }

   public void setInventorySlotContents(int index, ItemStack stack) {
      if (index >= this.upperChest.getSizeInventory()) {
         this.lowerChest.setInventorySlotContents(index - this.upperChest.getSizeInventory(), stack);
      } else {
         this.upperChest.setInventorySlotContents(index, stack);
      }

   }

   public int getInventoryStackLimit() {
      return this.upperChest.getInventoryStackLimit();
   }

   public void markDirty() {
      this.upperChest.markDirty();
      this.lowerChest.markDirty();
   }

   public boolean isUseableByPlayer(EntityPlayer playerIn) {
      return this.upperChest.isUseableByPlayer(playerIn) && this.lowerChest.isUseableByPlayer(playerIn);
   }

   public void openInventory(EntityPlayer playerIn) {
      this.upperChest.openInventory(playerIn);
      this.lowerChest.openInventory(playerIn);
   }

   public void closeInventory(EntityPlayer playerIn) {
      this.upperChest.closeInventory(playerIn);
      this.lowerChest.closeInventory(playerIn);
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

   public boolean isLocked() {
      return this.upperChest.isLocked() || this.lowerChest.isLocked();
   }

   public void setLockCode(LockCode code) {
      this.upperChest.setLockCode(code);
      this.lowerChest.setLockCode(code);
   }

   public LockCode getLockCode() {
      return this.upperChest.getLockCode();
   }

   public String getGuiID() {
      return this.upperChest.getGuiID();
   }

   public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
      return new ContainerChest(playerInventory, this, playerIn);
   }

   public void clearInventory() {
      this.upperChest.clearInventory();
      this.lowerChest.clearInventory();
   }
}
