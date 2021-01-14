package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class BlockAir extends Block {
   protected BlockAir() {
      super(Material.air);
   }

   public int getRenderType() {
      return -1;
   }

   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
      return null;
   }

   public boolean isOpaqueCube() {
      return false;
   }

   public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid) {
      return false;
   }

   public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
   }

   public boolean isReplaceable(World worldIn, BlockPos pos) {
      return true;
   }
}
