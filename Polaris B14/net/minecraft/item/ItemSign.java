/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.BlockStandingSign;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.tileentity.TileEntitySign;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemSign extends Item
/*    */ {
/*    */   public ItemSign()
/*    */   {
/* 19 */     this.maxStackSize = 16;
/* 20 */     setCreativeTab(CreativeTabs.tabDecorations);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
/*    */   {
/* 28 */     if (side == EnumFacing.DOWN)
/*    */     {
/* 30 */       return false;
/*    */     }
/* 32 */     if (!worldIn.getBlockState(pos).getBlock().getMaterial().isSolid())
/*    */     {
/* 34 */       return false;
/*    */     }
/*    */     
/*    */ 
/* 38 */     pos = pos.offset(side);
/*    */     
/* 40 */     if (!playerIn.canPlayerEdit(pos, side, stack))
/*    */     {
/* 42 */       return false;
/*    */     }
/* 44 */     if (!Blocks.standing_sign.canPlaceBlockAt(worldIn, pos))
/*    */     {
/* 46 */       return false;
/*    */     }
/* 48 */     if (worldIn.isRemote)
/*    */     {
/* 50 */       return true;
/*    */     }
/*    */     
/*    */ 
/* 54 */     if (side == EnumFacing.UP)
/*    */     {
/* 56 */       int i = MathHelper.floor_double((playerIn.rotationYaw + 180.0F) * 16.0F / 360.0F + 0.5D) & 0xF;
/* 57 */       worldIn.setBlockState(pos, Blocks.standing_sign.getDefaultState().withProperty(BlockStandingSign.ROTATION, Integer.valueOf(i)), 3);
/*    */     }
/*    */     else
/*    */     {
/* 61 */       worldIn.setBlockState(pos, Blocks.wall_sign.getDefaultState().withProperty(net.minecraft.block.BlockWallSign.FACING, side), 3);
/*    */     }
/*    */     
/* 64 */     stack.stackSize -= 1;
/* 65 */     net.minecraft.tileentity.TileEntity tileentity = worldIn.getTileEntity(pos);
/*    */     
/* 67 */     if (((tileentity instanceof TileEntitySign)) && (!ItemBlock.setTileEntityNBT(worldIn, playerIn, pos, stack)))
/*    */     {
/* 69 */       playerIn.openEditSign((TileEntitySign)tileentity);
/*    */     }
/*    */     
/* 72 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemSign.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */