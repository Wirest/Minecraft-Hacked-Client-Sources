package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class Slot {
   private final int slotIndex;
   public final IInventory inventory;
   public int slotNumber;
   public int xDisplayPosition;
   public int yDisplayPosition;
   private static final String __OBFID = "CL_00001762";

   public Slot(IInventory p_i1824_1_, int p_i1824_2_, int p_i1824_3_, int p_i1824_4_) {
      this.inventory = p_i1824_1_;
      this.slotIndex = p_i1824_2_;
      this.xDisplayPosition = p_i1824_3_;
      this.yDisplayPosition = p_i1824_4_;
   }

   public void onSlotChange(ItemStack p_75220_1_, ItemStack p_75220_2_) {
      if (p_75220_1_ != null && p_75220_2_ != null && p_75220_1_.getItem() == p_75220_2_.getItem()) {
         int var3 = p_75220_2_.stackSize - p_75220_1_.stackSize;
         if (var3 > 0) {
            this.onCrafting(p_75220_1_, var3);
         }
      }

   }

   protected void onCrafting(ItemStack p_75210_1_, int p_75210_2_) {
   }

   protected void onCrafting(ItemStack p_75208_1_) {
   }

   public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
      this.onSlotChanged();
   }

   public boolean isItemValid(ItemStack stack) {
      return true;
   }

   public ItemStack getStack() {
      return this.inventory.getStackInSlot(this.slotIndex);
   }

   public boolean getHasStack() {
      return this.getStack() != null;
   }

   public void putStack(ItemStack p_75215_1_) {
      this.inventory.setInventorySlotContents(this.slotIndex, p_75215_1_);
      this.onSlotChanged();
   }

   public void onSlotChanged() {
      this.inventory.markDirty();
   }

   public int getSlotStackLimit() {
      return this.inventory.getInventoryStackLimit();
   }

   public int func_178170_b(ItemStack p_178170_1_) {
      return this.getSlotStackLimit();
   }

   public String func_178171_c() {
      return null;
   }

   public ItemStack decrStackSize(int p_75209_1_) {
      return this.inventory.decrStackSize(this.slotIndex, p_75209_1_);
   }

   public boolean isHere(IInventory p_75217_1_, int p_75217_2_) {
      return p_75217_1_ == this.inventory && p_75217_2_ == this.slotIndex;
   }

   public boolean canTakeStack(EntityPlayer p_82869_1_) {
      return true;
   }

   public boolean canBeHovered() {
      return true;
   }
}
