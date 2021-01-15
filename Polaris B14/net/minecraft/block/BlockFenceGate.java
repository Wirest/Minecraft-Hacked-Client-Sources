/*     */ package net.minecraft.block;
/*     */ 
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumFacing.Axis;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockFenceGate extends BlockDirectional
/*     */ {
/*  20 */   public static final PropertyBool OPEN = PropertyBool.create("open");
/*  21 */   public static final PropertyBool POWERED = PropertyBool.create("powered");
/*  22 */   public static final PropertyBool IN_WALL = PropertyBool.create("in_wall");
/*     */   
/*     */   public BlockFenceGate(BlockPlanks.EnumType p_i46394_1_)
/*     */   {
/*  26 */     super(Material.wood, p_i46394_1_.func_181070_c());
/*  27 */     setDefaultState(this.blockState.getBaseState().withProperty(OPEN, Boolean.valueOf(false)).withProperty(POWERED, Boolean.valueOf(false)).withProperty(IN_WALL, Boolean.valueOf(false)));
/*  28 */     setCreativeTab(CreativeTabs.tabRedstone);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
/*     */   {
/*  37 */     EnumFacing.Axis enumfacing$axis = ((EnumFacing)state.getValue(FACING)).getAxis();
/*     */     
/*  39 */     if (((enumfacing$axis == EnumFacing.Axis.Z) && ((worldIn.getBlockState(pos.west()).getBlock() == Blocks.cobblestone_wall) || (worldIn.getBlockState(pos.east()).getBlock() == Blocks.cobblestone_wall))) || ((enumfacing$axis == EnumFacing.Axis.X) && ((worldIn.getBlockState(pos.north()).getBlock() == Blocks.cobblestone_wall) || (worldIn.getBlockState(pos.south()).getBlock() == Blocks.cobblestone_wall))))
/*     */     {
/*  41 */       state = state.withProperty(IN_WALL, Boolean.valueOf(true));
/*     */     }
/*     */     
/*  44 */     return state;
/*     */   }
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
/*     */   {
/*  49 */     return worldIn.getBlockState(pos.down()).getBlock().getMaterial().isSolid() ? super.canPlaceBlockAt(worldIn, pos) : false;
/*     */   }
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  54 */     if (((Boolean)state.getValue(OPEN)).booleanValue())
/*     */     {
/*  56 */       return null;
/*     */     }
/*     */     
/*     */ 
/*  60 */     EnumFacing.Axis enumfacing$axis = ((EnumFacing)state.getValue(FACING)).getAxis();
/*  61 */     return enumfacing$axis == EnumFacing.Axis.Z ? new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ() + 0.375F, pos.getX() + 1, pos.getY() + 1.5F, pos.getZ() + 0.625F) : new AxisAlignedBB(pos.getX() + 0.375F, pos.getY(), pos.getZ(), pos.getX() + 0.625F, pos.getY() + 1.5F, pos.getZ() + 1);
/*     */   }
/*     */   
/*     */ 
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/*  67 */     EnumFacing.Axis enumfacing$axis = ((EnumFacing)worldIn.getBlockState(pos).getValue(FACING)).getAxis();
/*     */     
/*  69 */     if (enumfacing$axis == EnumFacing.Axis.Z)
/*     */     {
/*  71 */       setBlockBounds(0.0F, 0.0F, 0.375F, 1.0F, 1.0F, 0.625F);
/*     */     }
/*     */     else
/*     */     {
/*  75 */       setBlockBounds(0.375F, 0.0F, 0.0F, 0.625F, 1.0F, 1.0F);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpaqueCube()
/*     */   {
/*  84 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isFullCube()
/*     */   {
/*  89 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/*  94 */     return ((Boolean)worldIn.getBlockState(pos).getValue(OPEN)).booleanValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
/*     */   {
/* 103 */     return getDefaultState().withProperty(FACING, placer.getHorizontalFacing()).withProperty(OPEN, Boolean.valueOf(false)).withProperty(POWERED, Boolean.valueOf(false)).withProperty(IN_WALL, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
/*     */   {
/* 108 */     if (((Boolean)state.getValue(OPEN)).booleanValue())
/*     */     {
/* 110 */       state = state.withProperty(OPEN, Boolean.valueOf(false));
/* 111 */       worldIn.setBlockState(pos, state, 2);
/*     */     }
/*     */     else
/*     */     {
/* 115 */       EnumFacing enumfacing = EnumFacing.fromAngle(playerIn.rotationYaw);
/*     */       
/* 117 */       if (state.getValue(FACING) == enumfacing.getOpposite())
/*     */       {
/* 119 */         state = state.withProperty(FACING, enumfacing);
/*     */       }
/*     */       
/* 122 */       state = state.withProperty(OPEN, Boolean.valueOf(true));
/* 123 */       worldIn.setBlockState(pos, state, 2);
/*     */     }
/*     */     
/* 126 */     worldIn.playAuxSFXAtEntity(playerIn, ((Boolean)state.getValue(OPEN)).booleanValue() ? 1003 : 1006, pos, 0);
/* 127 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*     */   {
/* 135 */     if (!worldIn.isRemote)
/*     */     {
/* 137 */       boolean flag = worldIn.isBlockPowered(pos);
/*     */       
/* 139 */       if ((flag) || (neighborBlock.canProvidePower()))
/*     */       {
/* 141 */         if ((flag) && (!((Boolean)state.getValue(OPEN)).booleanValue()) && (!((Boolean)state.getValue(POWERED)).booleanValue()))
/*     */         {
/* 143 */           worldIn.setBlockState(pos, state.withProperty(OPEN, Boolean.valueOf(true)).withProperty(POWERED, Boolean.valueOf(true)), 2);
/* 144 */           worldIn.playAuxSFXAtEntity(null, 1003, pos, 0);
/*     */         }
/* 146 */         else if ((!flag) && (((Boolean)state.getValue(OPEN)).booleanValue()) && (((Boolean)state.getValue(POWERED)).booleanValue()))
/*     */         {
/* 148 */           worldIn.setBlockState(pos, state.withProperty(OPEN, Boolean.valueOf(false)).withProperty(POWERED, Boolean.valueOf(false)), 2);
/* 149 */           worldIn.playAuxSFXAtEntity(null, 1006, pos, 0);
/*     */         }
/* 151 */         else if (flag != ((Boolean)state.getValue(POWERED)).booleanValue())
/*     */         {
/* 153 */           worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(flag)), 2);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
/*     */   {
/* 161 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 169 */     return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta)).withProperty(OPEN, Boolean.valueOf((meta & 0x4) != 0)).withProperty(POWERED, Boolean.valueOf((meta & 0x8) != 0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 177 */     int i = 0;
/* 178 */     i |= ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
/*     */     
/* 180 */     if (((Boolean)state.getValue(POWERED)).booleanValue())
/*     */     {
/* 182 */       i |= 0x8;
/*     */     }
/*     */     
/* 185 */     if (((Boolean)state.getValue(OPEN)).booleanValue())
/*     */     {
/* 187 */       i |= 0x4;
/*     */     }
/*     */     
/* 190 */     return i;
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 195 */     return new BlockState(this, new net.minecraft.block.properties.IProperty[] { FACING, OPEN, POWERED, IN_WALL });
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockFenceGate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */