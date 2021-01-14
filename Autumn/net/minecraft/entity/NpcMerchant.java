package net.minecraft.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryMerchant;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class NpcMerchant implements IMerchant {
   private InventoryMerchant theMerchantInventory;
   private EntityPlayer customer;
   private MerchantRecipeList recipeList;
   private IChatComponent field_175548_d;

   public NpcMerchant(EntityPlayer p_i45817_1_, IChatComponent p_i45817_2_) {
      this.customer = p_i45817_1_;
      this.field_175548_d = p_i45817_2_;
      this.theMerchantInventory = new InventoryMerchant(p_i45817_1_, this);
   }

   public EntityPlayer getCustomer() {
      return this.customer;
   }

   public void setCustomer(EntityPlayer p_70932_1_) {
   }

   public MerchantRecipeList getRecipes(EntityPlayer p_70934_1_) {
      return this.recipeList;
   }

   public void setRecipes(MerchantRecipeList recipeList) {
      this.recipeList = recipeList;
   }

   public void useRecipe(MerchantRecipe recipe) {
      recipe.incrementToolUses();
   }

   public void verifySellingItem(ItemStack stack) {
   }

   public IChatComponent getDisplayName() {
      return (IChatComponent)(this.field_175548_d != null ? this.field_175548_d : new ChatComponentTranslation("entity.Villager.name", new Object[0]));
   }
}
