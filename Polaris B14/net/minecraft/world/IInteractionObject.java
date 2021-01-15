package net.minecraft.world;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public abstract interface IInteractionObject
  extends IWorldNameable
{
  public abstract Container createContainer(InventoryPlayer paramInventoryPlayer, EntityPlayer paramEntityPlayer);
  
  public abstract String getGuiID();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\IInteractionObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */