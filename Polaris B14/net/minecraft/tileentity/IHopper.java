package net.minecraft.tileentity;

import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;

public abstract interface IHopper
  extends IInventory
{
  public abstract World getWorld();
  
  public abstract double getXPos();
  
  public abstract double getYPos();
  
  public abstract double getZPos();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\tileentity\IHopper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */