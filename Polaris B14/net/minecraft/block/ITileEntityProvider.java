package net.minecraft.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract interface ITileEntityProvider
{
  public abstract TileEntity createNewTileEntity(World paramWorld, int paramInt);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\ITileEntityProvider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */