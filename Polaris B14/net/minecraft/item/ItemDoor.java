/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.BlockDoor;
/*    */ import net.minecraft.block.BlockDoor.EnumHingePosition;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemDoor extends Item
/*    */ {
/*    */   private Block block;
/*    */   
/*    */   public ItemDoor(Block block)
/*    */   {
/* 18 */     this.block = block;
/* 19 */     setCreativeTab(net.minecraft.creativetab.CreativeTabs.tabRedstone);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
/*    */   {
/* 27 */     if (side != EnumFacing.UP)
/*    */     {
/* 29 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 33 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/* 34 */     Block block = iblockstate.getBlock();
/*    */     
/* 36 */     if (!block.isReplaceable(worldIn, pos))
/*    */     {
/* 38 */       pos = pos.offset(side);
/*    */     }
/*    */     
/* 41 */     if (!playerIn.canPlayerEdit(pos, side, stack))
/*    */     {
/* 43 */       return false;
/*    */     }
/* 45 */     if (!this.block.canPlaceBlockAt(worldIn, pos))
/*    */     {
/* 47 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 51 */     placeDoor(worldIn, pos, EnumFacing.fromAngle(playerIn.rotationYaw), this.block);
/* 52 */     stack.stackSize -= 1;
/* 53 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static void placeDoor(World worldIn, BlockPos pos, EnumFacing facing, Block door)
/*    */   {
/* 60 */     BlockPos blockpos = pos.offset(facing.rotateY());
/* 61 */     BlockPos blockpos1 = pos.offset(facing.rotateYCCW());
/* 62 */     int i = (worldIn.getBlockState(blockpos1).getBlock().isNormalCube() ? 1 : 0) + (worldIn.getBlockState(blockpos1.up()).getBlock().isNormalCube() ? 1 : 0);
/* 63 */     int j = (worldIn.getBlockState(blockpos).getBlock().isNormalCube() ? 1 : 0) + (worldIn.getBlockState(blockpos.up()).getBlock().isNormalCube() ? 1 : 0);
/* 64 */     boolean flag = (worldIn.getBlockState(blockpos1).getBlock() == door) || (worldIn.getBlockState(blockpos1.up()).getBlock() == door);
/* 65 */     boolean flag1 = (worldIn.getBlockState(blockpos).getBlock() == door) || (worldIn.getBlockState(blockpos.up()).getBlock() == door);
/* 66 */     boolean flag2 = false;
/*    */     
/* 68 */     if (((flag) && (!flag1)) || (j > i))
/*    */     {
/* 70 */       flag2 = true;
/*    */     }
/*    */     
/* 73 */     BlockPos blockpos2 = pos.up();
/* 74 */     IBlockState iblockstate = door.getDefaultState().withProperty(BlockDoor.FACING, facing).withProperty(BlockDoor.HINGE, flag2 ? BlockDoor.EnumHingePosition.RIGHT : BlockDoor.EnumHingePosition.LEFT);
/* 75 */     worldIn.setBlockState(pos, iblockstate.withProperty(BlockDoor.HALF, net.minecraft.block.BlockDoor.EnumDoorHalf.LOWER), 2);
/* 76 */     worldIn.setBlockState(blockpos2, iblockstate.withProperty(BlockDoor.HALF, net.minecraft.block.BlockDoor.EnumDoorHalf.UPPER), 2);
/* 77 */     worldIn.notifyNeighborsOfStateChange(pos, door);
/* 78 */     worldIn.notifyNeighborsOfStateChange(blockpos2, door);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemDoor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */