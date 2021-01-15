/*     */ package net.minecraft.block;
/*     */ 
/*     */ import net.minecraft.block.properties.PropertyDirection;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumFacing.Axis;
/*     */ import net.minecraft.util.EnumFacing.Plane;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockLadder extends Block
/*     */ {
/*  19 */   public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
/*     */   
/*     */   protected BlockLadder()
/*     */   {
/*  23 */     super(net.minecraft.block.material.Material.circuits);
/*  24 */     setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
/*  25 */     setCreativeTab(CreativeTabs.tabDecorations);
/*     */   }
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  30 */     setBlockBoundsBasedOnState(worldIn, pos);
/*  31 */     return super.getCollisionBoundingBox(worldIn, pos, state);
/*     */   }
/*     */   
/*     */   public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
/*     */   {
/*  36 */     setBlockBoundsBasedOnState(worldIn, pos);
/*  37 */     return super.getSelectedBoundingBox(worldIn, pos);
/*     */   }
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/*  42 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*     */     
/*  44 */     if (iblockstate.getBlock() == this)
/*     */     {
/*  46 */       float f = 0.125F;
/*     */       
/*  48 */       switch ((EnumFacing)iblockstate.getValue(FACING))
/*     */       {
/*     */       case NORTH: 
/*  51 */         setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
/*  52 */         break;
/*     */       
/*     */       case SOUTH: 
/*  55 */         setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
/*  56 */         break;
/*     */       
/*     */       case UP: 
/*  59 */         setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*  60 */         break;
/*     */       
/*     */       case WEST: 
/*     */       default: 
/*  64 */         setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
/*     */       }
/*     */       
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isOpaqueCube()
/*     */   {
/*  74 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isFullCube()
/*     */   {
/*  79 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
/*     */   {
/*  84 */     return worldIn.getBlockState(pos.north()).getBlock().isNormalCube() ? true : worldIn.getBlockState(pos.east()).getBlock().isNormalCube() ? true : worldIn.getBlockState(pos.west()).getBlock().isNormalCube() ? true : worldIn.getBlockState(pos.south()).getBlock().isNormalCube();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
/*     */   {
/*  93 */     if ((facing.getAxis().isHorizontal()) && (canBlockStay(worldIn, pos, facing)))
/*     */     {
/*  95 */       return getDefaultState().withProperty(FACING, facing);
/*     */     }
/*     */     
/*     */ 
/*  99 */     for (Object enumfacing0 : EnumFacing.Plane.HORIZONTAL)
/*     */     {
/* 101 */       EnumFacing enumfacing = (EnumFacing)enumfacing0;
/*     */       
/* 103 */       if (canBlockStay(worldIn, pos, enumfacing))
/*     */       {
/* 105 */         return getDefaultState().withProperty(FACING, enumfacing);
/*     */       }
/*     */     }
/*     */     
/* 109 */     return getDefaultState();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*     */   {
/* 118 */     EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
/*     */     
/* 120 */     if (!canBlockStay(worldIn, pos, enumfacing))
/*     */     {
/* 122 */       dropBlockAsItem(worldIn, pos, state, 0);
/* 123 */       worldIn.setBlockToAir(pos);
/*     */     }
/*     */     
/* 126 */     super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
/*     */   }
/*     */   
/*     */   protected boolean canBlockStay(World worldIn, BlockPos pos, EnumFacing facing)
/*     */   {
/* 131 */     return worldIn.getBlockState(pos.offset(facing.getOpposite())).getBlock().isNormalCube();
/*     */   }
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer()
/*     */   {
/* 136 */     return EnumWorldBlockLayer.CUTOUT;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 144 */     EnumFacing enumfacing = EnumFacing.getFront(meta);
/*     */     
/* 146 */     if (enumfacing.getAxis() == EnumFacing.Axis.Y)
/*     */     {
/* 148 */       enumfacing = EnumFacing.NORTH;
/*     */     }
/*     */     
/* 151 */     return getDefaultState().withProperty(FACING, enumfacing);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 159 */     return ((EnumFacing)state.getValue(FACING)).getIndex();
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 164 */     return new BlockState(this, new net.minecraft.block.properties.IProperty[] { FACING });
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockLadder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */