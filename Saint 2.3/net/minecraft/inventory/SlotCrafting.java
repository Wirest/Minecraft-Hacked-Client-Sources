package net.minecraft.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.stats.AchievementList;

public class SlotCrafting extends Slot {
   private final InventoryCrafting craftMatrix;
   private final EntityPlayer thePlayer;
   private int amountCrafted;
   private static final String __OBFID = "CL_00001761";

   public SlotCrafting(EntityPlayer p_i45790_1_, InventoryCrafting p_i45790_2_, IInventory p_i45790_3_, int p_i45790_4_, int p_i45790_5_, int p_i45790_6_) {
      super(p_i45790_3_, p_i45790_4_, p_i45790_5_, p_i45790_6_);
      this.thePlayer = p_i45790_1_;
      this.craftMatrix = p_i45790_2_;
   }

   public boolean isItemValid(ItemStack stack) {
      return false;
   }

   public ItemStack decrStackSize(int p_75209_1_) {
      if (this.getHasStack()) {
         this.amountCrafted += Math.min(p_75209_1_, this.getStack().stackSize);
      }

      return super.decrStackSize(p_75209_1_);
   }

   protected void onCrafting(ItemStack p_75210_1_, int p_75210_2_) {
      this.amountCrafted += p_75210_2_;
      this.onCrafting(p_75210_1_);
   }

   protected void onCrafting(ItemStack p_75208_1_) {
      if (this.amountCrafted > 0) {
         p_75208_1_.onCrafting(this.thePlayer.worldObj, this.thePlayer, this.amountCrafted);
      }

      this.amountCrafted = 0;
      if (p_75208_1_.getItem() == Item.getItemFromBlock(Blocks.crafting_table)) {
         this.thePlayer.triggerAchievement(AchievementList.buildWorkBench);
      }

      if (p_75208_1_.getItem() instanceof ItemPickaxe) {
         this.thePlayer.triggerAchievement(AchievementList.buildPickaxe);
      }

      if (p_75208_1_.getItem() == Item.getItemFromBlock(Blocks.furnace)) {
         this.thePlayer.triggerAchievement(AchievementList.buildFurnace);
      }

      if (p_75208_1_.getItem() instanceof ItemHoe) {
         this.thePlayer.triggerAchievement(AchievementList.buildHoe);
      }

      if (p_75208_1_.getItem() == Items.bread) {
         this.thePlayer.triggerAchievement(AchievementList.makeBread);
      }

      if (p_75208_1_.getItem() == Items.cake) {
         this.thePlayer.triggerAchievement(AchievementList.bakeCake);
      }

      if (p_75208_1_.getItem() instanceof ItemPickaxe && ((ItemPickaxe)p_75208_1_.getItem()).getToolMaterial() != Item.ToolMaterial.WOOD) {
         this.thePlayer.triggerAchievement(AchievementList.buildBetterPickaxe);
      }

      if (p_75208_1_.getItem() instanceof ItemSword) {
         this.thePlayer.triggerAchievement(AchievementList.buildSword);
      }

      if (p_75208_1_.getItem() == Item.getItemFromBlock(Blocks.enchanting_table)) {
         this.thePlayer.triggerAchievement(AchievementList.enchantments);
      }

      if (p_75208_1_.getItem() == Item.getItemFromBlock(Blocks.bookshelf)) {
         this.thePlayer.triggerAchievement(AchievementList.bookcase);
      }

      if (p_75208_1_.getItem() == Items.golden_apple && p_75208_1_.getMetadata() == 1) {
         this.thePlayer.triggerAchievement(AchievementList.overpowered);
      }

   }

   public void onPickupFromSlot(EntityPlayer playerIn, ItemStack stack) {
      this.onCrafting(stack);
      ItemStack[] var3 = CraftingManager.getInstance().func_180303_b(this.craftMatrix, playerIn.worldObj);

      for(int var4 = 0; var4 < var3.length; ++var4) {
         ItemStack var5 = this.craftMatrix.getStackInSlot(var4);
         ItemStack var6 = var3[var4];
         if (var5 != null) {
            this.craftMatrix.decrStackSize(var4, 1);
         }

         if (var6 != null) {
            if (this.craftMatrix.getStackInSlot(var4) == null) {
               this.craftMatrix.setInventorySlotContents(var4, var6);
            } else if (!this.thePlayer.inventory.addItemStackToInventory(var6)) {
               this.thePlayer.dropPlayerItemWithRandomChoice(var6, false);
            }
         }
      }

   }
}
