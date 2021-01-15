package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public abstract interface IGrowable
{
  public abstract boolean canGrow(World paramWorld, BlockPos paramBlockPos, IBlockState paramIBlockState, boolean paramBoolean);
  
  public abstract boolean canUseBonemeal(World paramWorld, Random paramRandom, BlockPos paramBlockPos, IBlockState paramIBlockState);
  
  public abstract void grow(World paramWorld, Random paramRandom, BlockPos paramBlockPos, IBlockState paramIBlockState);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\IGrowable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */