package net.minecraft.item.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract interface IRecipe
{
  public abstract boolean matches(InventoryCrafting paramInventoryCrafting, World paramWorld);
  
  public abstract ItemStack getCraftingResult(InventoryCrafting paramInventoryCrafting);
  
  public abstract int getRecipeSize();
  
  public abstract ItemStack getRecipeOutput();
  
  public abstract ItemStack[] getRemainingItems(InventoryCrafting paramInventoryCrafting);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\crafting\IRecipe.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */