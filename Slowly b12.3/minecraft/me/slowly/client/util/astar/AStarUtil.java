package me.slowly.client.util.astar;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;

public class AStarUtil {
   protected Minecraft mc = Minecraft.getMinecraft();

   public boolean isWalkableBlock(BlockPos from, BlockPos to) {
      return !this.isFullBlock(to.add(0, 1, 0)) && !this.isFullBlock(to.add(0, 0, 0)) && this.isFullBlock(to.add(0, -1, 0));
   }

   public double getCost(BlockPos from, BlockPos to) {
      return (double)(Math.abs(from.getX() - to.getX()) + Math.abs(from.getY() - to.getY()) + Math.abs(from.getZ() - to.getZ()));
   }

   private boolean isFullBlock(BlockPos pos) {
      Block block = this.mc.theWorld.getBlockState(pos).getBlock();
      return block.isFullBlock();
   }
}
