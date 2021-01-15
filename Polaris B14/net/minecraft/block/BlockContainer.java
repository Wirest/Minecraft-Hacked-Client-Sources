/*    */ package net.minecraft.block;
/*    */ 
/*    */ import net.minecraft.block.material.MapColor;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.tileentity.TileEntity;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public abstract class BlockContainer extends Block implements ITileEntityProvider
/*    */ {
/*    */   protected BlockContainer(Material materialIn)
/*    */   {
/* 15 */     this(materialIn, materialIn.getMaterialMapColor());
/*    */   }
/*    */   
/*    */   protected BlockContainer(Material p_i46402_1_, MapColor p_i46402_2_)
/*    */   {
/* 20 */     super(p_i46402_1_, p_i46402_2_);
/* 21 */     this.isBlockContainer = true;
/*    */   }
/*    */   
/*    */   protected boolean func_181086_a(World p_181086_1_, BlockPos p_181086_2_, EnumFacing p_181086_3_)
/*    */   {
/* 26 */     return p_181086_1_.getBlockState(p_181086_2_.offset(p_181086_3_)).getBlock().getMaterial() == Material.cactus;
/*    */   }
/*    */   
/*    */   protected boolean func_181087_e(World p_181087_1_, BlockPos p_181087_2_)
/*    */   {
/* 31 */     return (func_181086_a(p_181087_1_, p_181087_2_, EnumFacing.NORTH)) || (func_181086_a(p_181087_1_, p_181087_2_, EnumFacing.SOUTH)) || (func_181086_a(p_181087_1_, p_181087_2_, EnumFacing.WEST)) || (func_181086_a(p_181087_1_, p_181087_2_, EnumFacing.EAST));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getRenderType()
/*    */   {
/* 39 */     return -1;
/*    */   }
/*    */   
/*    */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
/*    */   {
/* 44 */     super.breakBlock(worldIn, pos, state);
/* 45 */     worldIn.removeTileEntity(pos);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam)
/*    */   {
/* 53 */     super.onBlockEventReceived(worldIn, pos, state, eventID, eventParam);
/* 54 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/* 55 */     return tileentity == null ? false : tileentity.receiveClientEvent(eventID, eventParam);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockContainer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */