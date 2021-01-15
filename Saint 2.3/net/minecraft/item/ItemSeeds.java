package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemSeeds extends Item {
   private Block field_150925_a;
   private Block soilBlockID;
   private static final String __OBFID = "CL_00000061";

   public ItemSeeds(Block p_i45352_1_, Block p_i45352_2_) {
      this.field_150925_a = p_i45352_1_;
      this.soilBlockID = p_i45352_2_;
      this.setCreativeTab(CreativeTabs.tabMaterials);
   }

   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (side != EnumFacing.UP) {
         return false;
      } else if (!playerIn.func_175151_a(pos.offset(side), side, stack)) {
         return false;
      } else if (worldIn.getBlockState(pos).getBlock() == this.soilBlockID && worldIn.isAirBlock(pos.offsetUp())) {
         worldIn.setBlockState(pos.offsetUp(), this.field_150925_a.getDefaultState());
         --stack.stackSize;
         return true;
      } else {
         return false;
      }
   }
}
