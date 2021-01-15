package net.minecraft.item.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ShapedRecipes implements IRecipe {
   private final int recipeWidth;
   private final int recipeHeight;
   private final ItemStack[] recipeItems;
   private final ItemStack recipeOutput;
   private boolean field_92101_f;
   private static final String __OBFID = "CL_00000093";

   public ShapedRecipes(int p_i1917_1_, int p_i1917_2_, ItemStack[] p_i1917_3_, ItemStack p_i1917_4_) {
      this.recipeWidth = p_i1917_1_;
      this.recipeHeight = p_i1917_2_;
      this.recipeItems = p_i1917_3_;
      this.recipeOutput = p_i1917_4_;
   }

   public ItemStack getRecipeOutput() {
      return this.recipeOutput;
   }

   public ItemStack[] func_179532_b(InventoryCrafting p_179532_1_) {
      ItemStack[] var2 = new ItemStack[p_179532_1_.getSizeInventory()];

      for(int var3 = 0; var3 < var2.length; ++var3) {
         ItemStack var4 = p_179532_1_.getStackInSlot(var3);
         if (var4 != null && var4.getItem().hasContainerItem()) {
            var2[var3] = new ItemStack(var4.getItem().getContainerItem());
         }
      }

      return var2;
   }

   public boolean matches(InventoryCrafting p_77569_1_, World worldIn) {
      for(int var3 = 0; var3 <= 3 - this.recipeWidth; ++var3) {
         for(int var4 = 0; var4 <= 3 - this.recipeHeight; ++var4) {
            if (this.checkMatch(p_77569_1_, var3, var4, true)) {
               return true;
            }

            if (this.checkMatch(p_77569_1_, var3, var4, false)) {
               return true;
            }
         }
      }

      return false;
   }

   private boolean checkMatch(InventoryCrafting p_77573_1_, int p_77573_2_, int p_77573_3_, boolean p_77573_4_) {
      for(int var5 = 0; var5 < 3; ++var5) {
         for(int var6 = 0; var6 < 3; ++var6) {
            int var7 = var5 - p_77573_2_;
            int var8 = var6 - p_77573_3_;
            ItemStack var9 = null;
            if (var7 >= 0 && var8 >= 0 && var7 < this.recipeWidth && var8 < this.recipeHeight) {
               if (p_77573_4_) {
                  var9 = this.recipeItems[this.recipeWidth - var7 - 1 + var8 * this.recipeWidth];
               } else {
                  var9 = this.recipeItems[var7 + var8 * this.recipeWidth];
               }
            }

            ItemStack var10 = p_77573_1_.getStackInRowAndColumn(var5, var6);
            if (var10 != null || var9 != null) {
               if (var10 == null && var9 != null || var10 != null && var9 == null) {
                  return false;
               }

               if (var9.getItem() != var10.getItem()) {
                  return false;
               }

               if (var9.getMetadata() != 32767 && var9.getMetadata() != var10.getMetadata()) {
                  return false;
               }
            }
         }
      }

      return true;
   }

   public ItemStack getCraftingResult(InventoryCrafting p_77572_1_) {
      ItemStack var2 = this.getRecipeOutput().copy();
      if (this.field_92101_f) {
         for(int var3 = 0; var3 < p_77572_1_.getSizeInventory(); ++var3) {
            ItemStack var4 = p_77572_1_.getStackInSlot(var3);
            if (var4 != null && var4.hasTagCompound()) {
               var2.setTagCompound((NBTTagCompound)var4.getTagCompound().copy());
            }
         }
      }

      return var2;
   }

   public int getRecipeSize() {
      return this.recipeWidth * this.recipeHeight;
   }

   public ShapedRecipes func_92100_c() {
      this.field_92101_f = true;
      return this;
   }
}
