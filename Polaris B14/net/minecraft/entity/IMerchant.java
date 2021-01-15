package net.minecraft.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public abstract interface IMerchant
{
  public abstract void setCustomer(EntityPlayer paramEntityPlayer);
  
  public abstract EntityPlayer getCustomer();
  
  public abstract MerchantRecipeList getRecipes(EntityPlayer paramEntityPlayer);
  
  public abstract void setRecipes(MerchantRecipeList paramMerchantRecipeList);
  
  public abstract void useRecipe(MerchantRecipe paramMerchantRecipe);
  
  public abstract void verifySellingItem(ItemStack paramItemStack);
  
  public abstract IChatComponent getDisplayName();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\IMerchant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */