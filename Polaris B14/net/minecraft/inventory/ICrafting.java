package net.minecraft.inventory;

import java.util.List;
import net.minecraft.item.ItemStack;

public abstract interface ICrafting
{
  public abstract void updateCraftingInventory(Container paramContainer, List<ItemStack> paramList);
  
  public abstract void sendSlotContents(Container paramContainer, int paramInt, ItemStack paramItemStack);
  
  public abstract void sendProgressBarUpdate(Container paramContainer, int paramInt1, int paramInt2);
  
  public abstract void func_175173_a(Container paramContainer, IInventory paramIInventory);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\inventory\ICrafting.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */