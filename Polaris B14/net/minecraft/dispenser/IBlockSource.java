package net.minecraft.dispenser;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;

public abstract interface IBlockSource
  extends ILocatableSource
{
  public abstract double getX();
  
  public abstract double getY();
  
  public abstract double getZ();
  
  public abstract BlockPos getBlockPos();
  
  public abstract int getBlockMetadata();
  
  public abstract <T extends TileEntity> T getBlockTileEntity();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\dispenser\IBlockSource.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */