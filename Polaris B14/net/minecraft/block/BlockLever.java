/*     */ package net.minecraft.block;
/*     */ 
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumFacing.Plane;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockLever extends Block
/*     */ {
/*  21 */   public static final PropertyEnum<EnumOrientation> FACING = PropertyEnum.create("facing", EnumOrientation.class);
/*  22 */   public static final PropertyBool POWERED = PropertyBool.create("powered");
/*     */   
/*     */   protected BlockLever()
/*     */   {
/*  26 */     super(Material.circuits);
/*  27 */     setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumOrientation.NORTH).withProperty(POWERED, Boolean.valueOf(false)));
/*  28 */     setCreativeTab(net.minecraft.creativetab.CreativeTabs.tabRedstone);
/*     */   }
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  33 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpaqueCube()
/*     */   {
/*  41 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isFullCube()
/*     */   {
/*  46 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
/*     */   {
/*  54 */     return func_181090_a(worldIn, pos, side.getOpposite());
/*     */   }
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
/*     */     EnumFacing[] arrayOfEnumFacing;
/*  59 */     int j = (arrayOfEnumFacing = EnumFacing.values()).length; for (int i = 0; i < j; i++) { EnumFacing enumfacing = arrayOfEnumFacing[i];
/*     */       
/*  61 */       if (func_181090_a(worldIn, pos, enumfacing))
/*     */       {
/*  63 */         return true;
/*     */       }
/*     */     }
/*     */     
/*  67 */     return false;
/*     */   }
/*     */   
/*     */   protected static boolean func_181090_a(World p_181090_0_, BlockPos p_181090_1_, EnumFacing p_181090_2_)
/*     */   {
/*  72 */     return BlockButton.func_181088_a(p_181090_0_, p_181090_1_, p_181090_2_);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
/*     */   {
/*  81 */     IBlockState iblockstate = getDefaultState().withProperty(POWERED, Boolean.valueOf(false));
/*     */     
/*  83 */     if (func_181090_a(worldIn, pos, facing.getOpposite()))
/*     */     {
/*  85 */       return iblockstate.withProperty(FACING, EnumOrientation.forFacings(facing, placer.getHorizontalFacing()));
/*     */     }
/*     */     
/*     */ 
/*  89 */     for (Object enumfacing0 : EnumFacing.Plane.HORIZONTAL)
/*     */     {
/*  91 */       EnumFacing enumfacing = (EnumFacing)enumfacing0;
/*     */       
/*  93 */       if ((enumfacing != facing) && (func_181090_a(worldIn, pos, enumfacing.getOpposite())))
/*     */       {
/*  95 */         return iblockstate.withProperty(FACING, EnumOrientation.forFacings(enumfacing, placer.getHorizontalFacing()));
/*     */       }
/*     */     }
/*     */     
/*  99 */     if (World.doesBlockHaveSolidTopSurface(worldIn, pos.down()))
/*     */     {
/* 101 */       return iblockstate.withProperty(FACING, EnumOrientation.forFacings(EnumFacing.UP, placer.getHorizontalFacing()));
/*     */     }
/*     */     
/*     */ 
/* 105 */     return iblockstate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static int getMetadataForFacing(EnumFacing facing)
/*     */   {
/* 112 */     switch (facing)
/*     */     {
/*     */     case DOWN: 
/* 115 */       return 0;
/*     */     
/*     */     case EAST: 
/* 118 */       return 5;
/*     */     
/*     */     case NORTH: 
/* 121 */       return 4;
/*     */     
/*     */     case SOUTH: 
/* 124 */       return 3;
/*     */     
/*     */     case UP: 
/* 127 */       return 2;
/*     */     
/*     */     case WEST: 
/* 130 */       return 1;
/*     */     }
/*     */     
/* 133 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*     */   {
/* 142 */     if ((func_181091_e(worldIn, pos, state)) && (!func_181090_a(worldIn, pos, ((EnumOrientation)state.getValue(FACING)).getFacing().getOpposite())))
/*     */     {
/* 144 */       dropBlockAsItem(worldIn, pos, state, 0);
/* 145 */       worldIn.setBlockToAir(pos);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean func_181091_e(World p_181091_1_, BlockPos p_181091_2_, IBlockState p_181091_3_)
/*     */   {
/* 151 */     if (canPlaceBlockAt(p_181091_1_, p_181091_2_))
/*     */     {
/* 153 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 157 */     dropBlockAsItem(p_181091_1_, p_181091_2_, p_181091_3_, 0);
/* 158 */     p_181091_1_.setBlockToAir(p_181091_2_);
/* 159 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/* 165 */     float f = 0.1875F;
/*     */     
/* 167 */     switch ((EnumOrientation)worldIn.getBlockState(pos).getValue(FACING))
/*     */     {
/*     */     case DOWN_Z: 
/* 170 */       setBlockBounds(0.0F, 0.2F, 0.5F - f, f * 2.0F, 0.8F, 0.5F + f);
/* 171 */       break;
/*     */     
/*     */     case EAST: 
/* 174 */       setBlockBounds(1.0F - f * 2.0F, 0.2F, 0.5F - f, 1.0F, 0.8F, 0.5F + f);
/* 175 */       break;
/*     */     
/*     */     case NORTH: 
/* 178 */       setBlockBounds(0.5F - f, 0.2F, 0.0F, 0.5F + f, 0.8F, f * 2.0F);
/* 179 */       break;
/*     */     
/*     */     case SOUTH: 
/* 182 */       setBlockBounds(0.5F - f, 0.2F, 1.0F - f * 2.0F, 0.5F + f, 0.8F, 1.0F);
/* 183 */       break;
/*     */     
/*     */     case UP_X: 
/*     */     case UP_Z: 
/* 187 */       f = 0.25F;
/* 188 */       setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.6F, 0.5F + f);
/* 189 */       break;
/*     */     
/*     */     case DOWN_X: 
/*     */     case WEST: 
/* 193 */       f = 0.25F;
/* 194 */       setBlockBounds(0.5F - f, 0.4F, 0.5F - f, 0.5F + f, 1.0F, 0.5F + f);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
/*     */   {
/* 200 */     if (worldIn.isRemote)
/*     */     {
/* 202 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 206 */     state = state.cycleProperty(POWERED);
/* 207 */     worldIn.setBlockState(pos, state, 3);
/* 208 */     worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "random.click", 0.3F, ((Boolean)state.getValue(POWERED)).booleanValue() ? 0.6F : 0.5F);
/* 209 */     worldIn.notifyNeighborsOfStateChange(pos, this);
/* 210 */     EnumFacing enumfacing = ((EnumOrientation)state.getValue(FACING)).getFacing();
/* 211 */     worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing.getOpposite()), this);
/* 212 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 218 */     if (((Boolean)state.getValue(POWERED)).booleanValue())
/*     */     {
/* 220 */       worldIn.notifyNeighborsOfStateChange(pos, this);
/* 221 */       EnumFacing enumfacing = ((EnumOrientation)state.getValue(FACING)).getFacing();
/* 222 */       worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing.getOpposite()), this);
/*     */     }
/*     */     
/* 225 */     super.breakBlock(worldIn, pos, state);
/*     */   }
/*     */   
/*     */   public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
/*     */   {
/* 230 */     return ((Boolean)state.getValue(POWERED)).booleanValue() ? 15 : 0;
/*     */   }
/*     */   
/*     */   public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
/*     */   {
/* 235 */     return ((EnumOrientation)state.getValue(FACING)).getFacing() == side ? 15 : !((Boolean)state.getValue(POWERED)).booleanValue() ? 0 : 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canProvidePower()
/*     */   {
/* 243 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 251 */     return getDefaultState().withProperty(FACING, EnumOrientation.byMetadata(meta & 0x7)).withProperty(POWERED, Boolean.valueOf((meta & 0x8) > 0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 259 */     int i = 0;
/* 260 */     i |= ((EnumOrientation)state.getValue(FACING)).getMetadata();
/*     */     
/* 262 */     if (((Boolean)state.getValue(POWERED)).booleanValue())
/*     */     {
/* 264 */       i |= 0x8;
/*     */     }
/*     */     
/* 267 */     return i;
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 272 */     return new BlockState(this, new IProperty[] { FACING, POWERED });
/*     */   }
/*     */   
/*     */   public static enum EnumOrientation implements IStringSerializable
/*     */   {
/* 277 */     DOWN_X(0, "down_x", EnumFacing.DOWN), 
/* 278 */     EAST(1, "east", EnumFacing.EAST), 
/* 279 */     WEST(2, "west", EnumFacing.WEST), 
/* 280 */     SOUTH(3, "south", EnumFacing.SOUTH), 
/* 281 */     NORTH(4, "north", EnumFacing.NORTH), 
/* 282 */     UP_Z(5, "up_z", EnumFacing.UP), 
/* 283 */     UP_X(6, "up_x", EnumFacing.UP), 
/* 284 */     DOWN_Z(7, "down_z", EnumFacing.DOWN);
/*     */     
/*     */     private static final EnumOrientation[] META_LOOKUP;
/*     */     private final int meta;
/*     */     private final String name;
/*     */     private final EnumFacing facing;
/*     */     
/*     */     private EnumOrientation(int meta, String name, EnumFacing facing)
/*     */     {
/* 293 */       this.meta = meta;
/* 294 */       this.name = name;
/* 295 */       this.facing = facing;
/*     */     }
/*     */     
/*     */     public int getMetadata()
/*     */     {
/* 300 */       return this.meta;
/*     */     }
/*     */     
/*     */     public EnumFacing getFacing()
/*     */     {
/* 305 */       return this.facing;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 310 */       return this.name;
/*     */     }
/*     */     
/*     */     public static EnumOrientation byMetadata(int meta)
/*     */     {
/* 315 */       if ((meta < 0) || (meta >= META_LOOKUP.length))
/*     */       {
/* 317 */         meta = 0;
/*     */       }
/*     */       
/* 320 */       return META_LOOKUP[meta];
/*     */     }
/*     */     
/*     */     public static EnumOrientation forFacings(EnumFacing clickedSide, EnumFacing entityFacing)
/*     */     {
/* 325 */       switch (clickedSide)
/*     */       {
/*     */       case DOWN: 
/* 328 */         switch (entityFacing.getAxis())
/*     */         {
/*     */         case X: 
/* 331 */           return DOWN_X;
/*     */         
/*     */         case Z: 
/* 334 */           return DOWN_Z;
/*     */         }
/*     */         
/* 337 */         throw new IllegalArgumentException("Invalid entityFacing " + entityFacing + " for facing " + clickedSide);
/*     */       
/*     */ 
/*     */       case EAST: 
/* 341 */         switch (entityFacing.getAxis())
/*     */         {
/*     */         case X: 
/* 344 */           return UP_X;
/*     */         
/*     */         case Z: 
/* 347 */           return UP_Z;
/*     */         }
/*     */         
/* 350 */         throw new IllegalArgumentException("Invalid entityFacing " + entityFacing + " for facing " + clickedSide);
/*     */       
/*     */ 
/*     */       case NORTH: 
/* 354 */         return NORTH;
/*     */       
/*     */       case SOUTH: 
/* 357 */         return SOUTH;
/*     */       
/*     */       case UP: 
/* 360 */         return WEST;
/*     */       
/*     */       case WEST: 
/* 363 */         return EAST;
/*     */       }
/*     */       
/* 366 */       throw new IllegalArgumentException("Invalid facing: " + clickedSide);
/*     */     }
/*     */     
/*     */ 
/*     */     public String getName()
/*     */     {
/* 372 */       return this.name;
/*     */     }
/*     */     
/*     */     static
/*     */     {
/* 286 */       META_LOOKUP = new EnumOrientation[values().length];
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       EnumOrientation[] arrayOfEnumOrientation;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 376 */       int j = (arrayOfEnumOrientation = values()).length; for (int i = 0; i < j; i++) { EnumOrientation blocklever$enumorientation = arrayOfEnumOrientation[i];
/*     */         
/* 378 */         META_LOOKUP[blocklever$enumorientation.getMetadata()] = blocklever$enumorientation;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockLever.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */