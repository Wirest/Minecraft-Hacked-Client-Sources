package net.minecraft.world;

import net.minecraft.inventory.IInventory;

public abstract interface ILockableContainer
  extends IInventory, IInteractionObject
{
  public abstract boolean isLocked();
  
  public abstract void setLockCode(LockCode paramLockCode);
  
  public abstract LockCode getLockCode();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\ILockableContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */