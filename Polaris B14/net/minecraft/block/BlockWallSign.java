/*    */ package net.minecraft.block;
/*    */ 
/*    */ import net.minecraft.block.properties.PropertyDirection;
/*    */ import net.minecraft.block.state.BlockState;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.util.EnumFacing.Plane;
/*    */ import net.minecraft.world.IBlockAccess;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class BlockWallSign extends BlockSign
/*    */ {
/* 14 */   public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
/*    */   
/*    */   public BlockWallSign()
/*    */   {
/* 18 */     setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
/*    */   }
/*    */   
/*    */ 
/*    */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
/*    */   {
/* 24 */     EnumFacing enumfacing = (EnumFacing)worldIn.getBlockState(pos).getValue(FACING);
/* 25 */     float f = 0.28125F;
/* 26 */     float f1 = 0.78125F;
/* 27 */     float f2 = 0.0F;
/* 28 */     float f3 = 1.0F;
/* 29 */     float f4 = 0.125F;
/* 30 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*    */     
/* 32 */     switch (enumfacing)
/*    */     {
/*    */     case NORTH: 
/* 35 */       setBlockBounds(f2, f, 1.0F - f4, f3, f1, 1.0F);
/* 36 */       break;
/*    */     
/*    */     case SOUTH: 
/* 39 */       setBlockBounds(f2, f, 0.0F, f3, f1, f4);
/* 40 */       break;
/*    */     
/*    */     case UP: 
/* 43 */       setBlockBounds(1.0F - f4, f, f2, 1.0F, f1, f3);
/* 44 */       break;
/*    */     
/*    */     case WEST: 
/* 47 */       setBlockBounds(0.0F, f, f2, f4, f1, f3);
/*    */     }
/*    */     
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*    */   {
/* 56 */     EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
/*    */     
/* 58 */     if (!worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getBlock().getMaterial().isSolid())
/*    */     {
/* 60 */       dropBlockAsItem(worldIn, pos, state, 0);
/* 61 */       worldIn.setBlockToAir(pos);
/*    */     }
/*    */     
/* 64 */     super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public IBlockState getStateFromMeta(int meta)
/*    */   {
/* 72 */     EnumFacing enumfacing = EnumFacing.getFront(meta);
/*    */     
/* 74 */     if (enumfacing.getAxis() == net.minecraft.util.EnumFacing.Axis.Y)
/*    */     {
/* 76 */       enumfacing = EnumFacing.NORTH;
/*    */     }
/*    */     
/* 79 */     return getDefaultState().withProperty(FACING, enumfacing);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getMetaFromState(IBlockState state)
/*    */   {
/* 87 */     return ((EnumFacing)state.getValue(FACING)).getIndex();
/*    */   }
/*    */   
/*    */   protected BlockState createBlockState()
/*    */   {
/* 92 */     return new BlockState(this, new net.minecraft.block.properties.IProperty[] { FACING });
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockWallSign.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */