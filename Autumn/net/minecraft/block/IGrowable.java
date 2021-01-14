package net.minecraft.block;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public interface IGrowable {
   boolean canGrow(World var1, BlockPos var2, IBlockState var3, boolean var4);

   boolean canUseBonemeal(World var1, Random var2, BlockPos var3, IBlockState var4);

   void grow(World var1, Random var2, BlockPos var3, IBlockState var4);
}
