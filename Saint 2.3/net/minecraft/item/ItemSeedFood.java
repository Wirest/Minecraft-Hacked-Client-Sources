package net.minecraft.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemSeedFood extends ItemFood {
   private Block field_150908_b;
   private Block soilId;
   private static final String __OBFID = "CL_00000060";

   public ItemSeedFood(int p_i45351_1_, float p_i45351_2_, Block p_i45351_3_, Block p_i45351_4_) {
      super(p_i45351_1_, p_i45351_2_, false);
      this.field_150908_b = p_i45351_3_;
      this.soilId = p_i45351_4_;
   }

   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
      if (side != EnumFacing.UP) {
         return false;
      } else if (!playerIn.func_175151_a(pos.offset(side), side, stack)) {
         return false;
      } else if (worldIn.getBlockState(pos).getBlock() == this.soilId && worldIn.isAirBlock(pos.offsetUp())) {
         worldIn.setBlockState(pos.offsetUp(), this.field_150908_b.getDefaultState());
         --stack.stackSize;
         return true;
      } else {
         return false;
      }
   }
}
