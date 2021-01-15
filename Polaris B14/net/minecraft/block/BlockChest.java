/*     */ package net.minecraft.block;
/*     */ 
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyDirection;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.passive.EntityOcelot;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.inventory.InventoryLargeChest;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityChest;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumFacing.Axis;
/*     */ import net.minecraft.util.EnumFacing.Plane;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.ILockableContainer;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockChest extends BlockContainer
/*     */ {
/*  31 */   public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
/*     */   
/*     */   public final int chestType;
/*     */   
/*     */ 
/*     */   protected BlockChest(int type)
/*     */   {
/*  38 */     super(Material.wood);
/*  39 */     setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
/*  40 */     this.chestType = type;
/*  41 */     setCreativeTab(net.minecraft.creativetab.CreativeTabs.tabDecorations);
/*  42 */     setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpaqueCube()
/*     */   {
/*  50 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isFullCube()
/*     */   {
/*  55 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRenderType()
/*     */   {
/*  63 */     return 2;
/*     */   }
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/*  68 */     if (worldIn.getBlockState(pos.north()).getBlock() == this)
/*     */     {
/*  70 */       setBlockBounds(0.0625F, 0.0F, 0.0F, 0.9375F, 0.875F, 0.9375F);
/*     */     }
/*  72 */     else if (worldIn.getBlockState(pos.south()).getBlock() == this)
/*     */     {
/*  74 */       setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 1.0F);
/*     */     }
/*  76 */     else if (worldIn.getBlockState(pos.west()).getBlock() == this)
/*     */     {
/*  78 */       setBlockBounds(0.0F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
/*     */     }
/*  80 */     else if (worldIn.getBlockState(pos.east()).getBlock() == this)
/*     */     {
/*  82 */       setBlockBounds(0.0625F, 0.0F, 0.0625F, 1.0F, 0.875F, 0.9375F);
/*     */     }
/*     */     else
/*     */     {
/*  86 */       setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  92 */     checkForSurroundingChests(worldIn, pos, state);
/*     */     
/*  94 */     for (Object enumfacing0 : EnumFacing.Plane.HORIZONTAL)
/*     */     {
/*  96 */       EnumFacing enumfacing = (EnumFacing)enumfacing0;
/*  97 */       BlockPos blockpos = pos.offset(enumfacing);
/*  98 */       IBlockState iblockstate = worldIn.getBlockState(blockpos);
/*     */       
/* 100 */       if (iblockstate.getBlock() == this)
/*     */       {
/* 102 */         checkForSurroundingChests(worldIn, blockpos, iblockstate);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
/*     */   {
/* 113 */     return getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
/*     */   {
/* 121 */     EnumFacing enumfacing = EnumFacing.getHorizontal(MathHelper.floor_double(placer.rotationYaw * 4.0F / 360.0F + 0.5D) & 0x3).getOpposite();
/* 122 */     state = state.withProperty(FACING, enumfacing);
/* 123 */     BlockPos blockpos = pos.north();
/* 124 */     BlockPos blockpos1 = pos.south();
/* 125 */     BlockPos blockpos2 = pos.west();
/* 126 */     BlockPos blockpos3 = pos.east();
/* 127 */     boolean flag = this == worldIn.getBlockState(blockpos).getBlock();
/* 128 */     boolean flag1 = this == worldIn.getBlockState(blockpos1).getBlock();
/* 129 */     boolean flag2 = this == worldIn.getBlockState(blockpos2).getBlock();
/* 130 */     boolean flag3 = this == worldIn.getBlockState(blockpos3).getBlock();
/*     */     
/* 132 */     if ((!flag) && (!flag1) && (!flag2) && (!flag3))
/*     */     {
/* 134 */       worldIn.setBlockState(pos, state, 3);
/*     */     }
/* 136 */     else if ((enumfacing.getAxis() != EnumFacing.Axis.X) || ((!flag) && (!flag1)))
/*     */     {
/* 138 */       if ((enumfacing.getAxis() == EnumFacing.Axis.Z) && ((flag2) || (flag3)))
/*     */       {
/* 140 */         if (flag2)
/*     */         {
/* 142 */           worldIn.setBlockState(blockpos2, state, 3);
/*     */         }
/*     */         else
/*     */         {
/* 146 */           worldIn.setBlockState(blockpos3, state, 3);
/*     */         }
/*     */         
/* 149 */         worldIn.setBlockState(pos, state, 3);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 154 */       if (flag)
/*     */       {
/* 156 */         worldIn.setBlockState(blockpos, state, 3);
/*     */       }
/*     */       else
/*     */       {
/* 160 */         worldIn.setBlockState(blockpos1, state, 3);
/*     */       }
/*     */       
/* 163 */       worldIn.setBlockState(pos, state, 3);
/*     */     }
/*     */     
/* 166 */     if (stack.hasDisplayName())
/*     */     {
/* 168 */       TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */       
/* 170 */       if ((tileentity instanceof TileEntityChest))
/*     */       {
/* 172 */         ((TileEntityChest)tileentity).setCustomName(stack.getDisplayName());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public IBlockState checkForSurroundingChests(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 179 */     if (worldIn.isRemote)
/*     */     {
/* 181 */       return state;
/*     */     }
/*     */     
/*     */ 
/* 185 */     IBlockState iblockstate = worldIn.getBlockState(pos.north());
/* 186 */     IBlockState iblockstate1 = worldIn.getBlockState(pos.south());
/* 187 */     IBlockState iblockstate2 = worldIn.getBlockState(pos.west());
/* 188 */     IBlockState iblockstate3 = worldIn.getBlockState(pos.east());
/* 189 */     EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
/* 190 */     Block block = iblockstate.getBlock();
/* 191 */     Block block1 = iblockstate1.getBlock();
/* 192 */     Block block2 = iblockstate2.getBlock();
/* 193 */     Block block3 = iblockstate3.getBlock();
/*     */     
/* 195 */     if ((block != this) && (block1 != this))
/*     */     {
/* 197 */       boolean flag = block.isFullBlock();
/* 198 */       boolean flag1 = block1.isFullBlock();
/*     */       
/* 200 */       if ((block2 == this) || (block3 == this))
/*     */       {
/* 202 */         BlockPos blockpos1 = block2 == this ? pos.west() : pos.east();
/* 203 */         IBlockState iblockstate6 = worldIn.getBlockState(blockpos1.north());
/* 204 */         IBlockState iblockstate7 = worldIn.getBlockState(blockpos1.south());
/* 205 */         enumfacing = EnumFacing.SOUTH;
/*     */         EnumFacing enumfacing2;
/*     */         EnumFacing enumfacing2;
/* 208 */         if (block2 == this)
/*     */         {
/* 210 */           enumfacing2 = (EnumFacing)iblockstate2.getValue(FACING);
/*     */         }
/*     */         else
/*     */         {
/* 214 */           enumfacing2 = (EnumFacing)iblockstate3.getValue(FACING);
/*     */         }
/*     */         
/* 217 */         if (enumfacing2 == EnumFacing.NORTH)
/*     */         {
/* 219 */           enumfacing = EnumFacing.NORTH;
/*     */         }
/*     */         
/* 222 */         Block block6 = iblockstate6.getBlock();
/* 223 */         Block block7 = iblockstate7.getBlock();
/*     */         
/* 225 */         if (((flag) || (block6.isFullBlock())) && (!flag1) && (!block7.isFullBlock()))
/*     */         {
/* 227 */           enumfacing = EnumFacing.SOUTH;
/*     */         }
/*     */         
/* 230 */         if (((flag1) || (block7.isFullBlock())) && (!flag) && (!block6.isFullBlock()))
/*     */         {
/* 232 */           enumfacing = EnumFacing.NORTH;
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 238 */       BlockPos blockpos = block == this ? pos.north() : pos.south();
/* 239 */       IBlockState iblockstate4 = worldIn.getBlockState(blockpos.west());
/* 240 */       IBlockState iblockstate5 = worldIn.getBlockState(blockpos.east());
/* 241 */       enumfacing = EnumFacing.EAST;
/*     */       EnumFacing enumfacing1;
/*     */       EnumFacing enumfacing1;
/* 244 */       if (block == this)
/*     */       {
/* 246 */         enumfacing1 = (EnumFacing)iblockstate.getValue(FACING);
/*     */       }
/*     */       else
/*     */       {
/* 250 */         enumfacing1 = (EnumFacing)iblockstate1.getValue(FACING);
/*     */       }
/*     */       
/* 253 */       if (enumfacing1 == EnumFacing.WEST)
/*     */       {
/* 255 */         enumfacing = EnumFacing.WEST;
/*     */       }
/*     */       
/* 258 */       Block block4 = iblockstate4.getBlock();
/* 259 */       Block block5 = iblockstate5.getBlock();
/*     */       
/* 261 */       if (((block2.isFullBlock()) || (block4.isFullBlock())) && (!block3.isFullBlock()) && (!block5.isFullBlock()))
/*     */       {
/* 263 */         enumfacing = EnumFacing.EAST;
/*     */       }
/*     */       
/* 266 */       if (((block3.isFullBlock()) || (block5.isFullBlock())) && (!block2.isFullBlock()) && (!block4.isFullBlock()))
/*     */       {
/* 268 */         enumfacing = EnumFacing.WEST;
/*     */       }
/*     */     }
/*     */     
/* 272 */     state = state.withProperty(FACING, enumfacing);
/* 273 */     worldIn.setBlockState(pos, state, 3);
/* 274 */     return state;
/*     */   }
/*     */   
/*     */ 
/*     */   public IBlockState correctFacing(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 280 */     EnumFacing enumfacing = null;
/*     */     
/* 282 */     for (Object enumfacing10 : EnumFacing.Plane.HORIZONTAL)
/*     */     {
/* 284 */       EnumFacing enumfacing1 = (EnumFacing)enumfacing10;
/* 285 */       IBlockState iblockstate = worldIn.getBlockState(pos.offset(enumfacing1));
/*     */       
/* 287 */       if (iblockstate.getBlock() == this)
/*     */       {
/* 289 */         return state;
/*     */       }
/*     */       
/* 292 */       if (iblockstate.getBlock().isFullBlock())
/*     */       {
/* 294 */         if (enumfacing != null)
/*     */         {
/* 296 */           enumfacing = null;
/* 297 */           break;
/*     */         }
/*     */         
/* 300 */         enumfacing = enumfacing1;
/*     */       }
/*     */     }
/*     */     
/* 304 */     if (enumfacing != null)
/*     */     {
/* 306 */       return state.withProperty(FACING, enumfacing.getOpposite());
/*     */     }
/*     */     
/*     */ 
/* 310 */     EnumFacing enumfacing2 = (EnumFacing)state.getValue(FACING);
/*     */     
/* 312 */     if (worldIn.getBlockState(pos.offset(enumfacing2)).getBlock().isFullBlock())
/*     */     {
/* 314 */       enumfacing2 = enumfacing2.getOpposite();
/*     */     }
/*     */     
/* 317 */     if (worldIn.getBlockState(pos.offset(enumfacing2)).getBlock().isFullBlock())
/*     */     {
/* 319 */       enumfacing2 = enumfacing2.rotateY();
/*     */     }
/*     */     
/* 322 */     if (worldIn.getBlockState(pos.offset(enumfacing2)).getBlock().isFullBlock())
/*     */     {
/* 324 */       enumfacing2 = enumfacing2.getOpposite();
/*     */     }
/*     */     
/* 327 */     return state.withProperty(FACING, enumfacing2);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
/*     */   {
/* 333 */     int i = 0;
/* 334 */     BlockPos blockpos = pos.west();
/* 335 */     BlockPos blockpos1 = pos.east();
/* 336 */     BlockPos blockpos2 = pos.north();
/* 337 */     BlockPos blockpos3 = pos.south();
/*     */     
/* 339 */     if (worldIn.getBlockState(blockpos).getBlock() == this)
/*     */     {
/* 341 */       if (isDoubleChest(worldIn, blockpos))
/*     */       {
/* 343 */         return false;
/*     */       }
/*     */       
/* 346 */       i++;
/*     */     }
/*     */     
/* 349 */     if (worldIn.getBlockState(blockpos1).getBlock() == this)
/*     */     {
/* 351 */       if (isDoubleChest(worldIn, blockpos1))
/*     */       {
/* 353 */         return false;
/*     */       }
/*     */       
/* 356 */       i++;
/*     */     }
/*     */     
/* 359 */     if (worldIn.getBlockState(blockpos2).getBlock() == this)
/*     */     {
/* 361 */       if (isDoubleChest(worldIn, blockpos2))
/*     */       {
/* 363 */         return false;
/*     */       }
/*     */       
/* 366 */       i++;
/*     */     }
/*     */     
/* 369 */     if (worldIn.getBlockState(blockpos3).getBlock() == this)
/*     */     {
/* 371 */       if (isDoubleChest(worldIn, blockpos3))
/*     */       {
/* 373 */         return false;
/*     */       }
/*     */       
/* 376 */       i++;
/*     */     }
/*     */     
/* 379 */     return i <= 1;
/*     */   }
/*     */   
/*     */   private boolean isDoubleChest(World worldIn, BlockPos pos)
/*     */   {
/* 384 */     if (worldIn.getBlockState(pos).getBlock() != this)
/*     */     {
/* 386 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 390 */     for (Object enumfacing0 : EnumFacing.Plane.HORIZONTAL)
/*     */     {
/* 392 */       EnumFacing enumfacing = (EnumFacing)enumfacing0;
/*     */       
/* 394 */       if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() == this)
/*     */       {
/* 396 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 400 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*     */   {
/* 409 */     super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
/* 410 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/* 412 */     if ((tileentity instanceof TileEntityChest))
/*     */     {
/* 414 */       tileentity.updateContainingBlockInfo();
/*     */     }
/*     */   }
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 420 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/* 422 */     if ((tileentity instanceof IInventory))
/*     */     {
/* 424 */       net.minecraft.inventory.InventoryHelper.dropInventoryItems(worldIn, pos, (IInventory)tileentity);
/* 425 */       worldIn.updateComparatorOutputLevel(pos, this);
/*     */     }
/*     */     
/* 428 */     super.breakBlock(worldIn, pos, state);
/*     */   }
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
/*     */   {
/* 433 */     if (worldIn.isRemote)
/*     */     {
/* 435 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 439 */     ILockableContainer ilockablecontainer = getLockableContainer(worldIn, pos);
/*     */     
/* 441 */     if (ilockablecontainer != null)
/*     */     {
/* 443 */       playerIn.displayGUIChest(ilockablecontainer);
/*     */       
/* 445 */       if (this.chestType == 0)
/*     */       {
/* 447 */         playerIn.triggerAchievement(StatList.field_181723_aa);
/*     */       }
/* 449 */       else if (this.chestType == 1)
/*     */       {
/* 451 */         playerIn.triggerAchievement(StatList.field_181737_U);
/*     */       }
/*     */     }
/*     */     
/* 455 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public ILockableContainer getLockableContainer(World worldIn, BlockPos pos)
/*     */   {
/* 461 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/* 463 */     if (!(tileentity instanceof TileEntityChest))
/*     */     {
/* 465 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 469 */     ILockableContainer ilockablecontainer = (TileEntityChest)tileentity;
/*     */     
/* 471 */     if (isBlocked(worldIn, pos))
/*     */     {
/* 473 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 477 */     for (Object enumfacing0 : EnumFacing.Plane.HORIZONTAL)
/*     */     {
/* 479 */       EnumFacing enumfacing = (EnumFacing)enumfacing0;
/* 480 */       BlockPos blockpos = pos.offset(enumfacing);
/* 481 */       Block block = worldIn.getBlockState(blockpos).getBlock();
/*     */       
/* 483 */       if (block == this)
/*     */       {
/* 485 */         if (isBlocked(worldIn, blockpos))
/*     */         {
/* 487 */           return null;
/*     */         }
/*     */         
/* 490 */         TileEntity tileentity1 = worldIn.getTileEntity(blockpos);
/*     */         
/* 492 */         if ((tileentity1 instanceof TileEntityChest))
/*     */         {
/* 494 */           if ((enumfacing != EnumFacing.WEST) && (enumfacing != EnumFacing.NORTH))
/*     */           {
/* 496 */             ilockablecontainer = new InventoryLargeChest("container.chestDouble", ilockablecontainer, (TileEntityChest)tileentity1);
/*     */           }
/*     */           else
/*     */           {
/* 500 */             ilockablecontainer = new InventoryLargeChest("container.chestDouble", (TileEntityChest)tileentity1, ilockablecontainer);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 506 */     return ilockablecontainer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TileEntity createNewTileEntity(World worldIn, int meta)
/*     */   {
/* 516 */     return new TileEntityChest();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canProvidePower()
/*     */   {
/* 524 */     return this.chestType == 1;
/*     */   }
/*     */   
/*     */   public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
/*     */   {
/* 529 */     if (!canProvidePower())
/*     */     {
/* 531 */       return 0;
/*     */     }
/*     */     
/*     */ 
/* 535 */     int i = 0;
/* 536 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/* 538 */     if ((tileentity instanceof TileEntityChest))
/*     */     {
/* 540 */       i = ((TileEntityChest)tileentity).numPlayersUsing;
/*     */     }
/*     */     
/* 543 */     return MathHelper.clamp_int(i, 0, 15);
/*     */   }
/*     */   
/*     */ 
/*     */   public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
/*     */   {
/* 549 */     return side == EnumFacing.UP ? getWeakPower(worldIn, pos, state, side) : 0;
/*     */   }
/*     */   
/*     */   private boolean isBlocked(World worldIn, BlockPos pos)
/*     */   {
/* 554 */     return (isBelowSolidBlock(worldIn, pos)) || (isOcelotSittingOnChest(worldIn, pos));
/*     */   }
/*     */   
/*     */   private boolean isBelowSolidBlock(World worldIn, BlockPos pos)
/*     */   {
/* 559 */     return worldIn.getBlockState(pos.up()).getBlock().isNormalCube();
/*     */   }
/*     */   
/*     */   private boolean isOcelotSittingOnChest(World worldIn, BlockPos pos)
/*     */   {
/* 564 */     for (Entity entity : worldIn.getEntitiesWithinAABB(EntityOcelot.class, new AxisAlignedBB(pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1)))
/*     */     {
/* 566 */       EntityOcelot entityocelot = (EntityOcelot)entity;
/*     */       
/* 568 */       if (entityocelot.isSitting())
/*     */       {
/* 570 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 574 */     return false;
/*     */   }
/*     */   
/*     */   public boolean hasComparatorInputOverride()
/*     */   {
/* 579 */     return true;
/*     */   }
/*     */   
/*     */   public int getComparatorInputOverride(World worldIn, BlockPos pos)
/*     */   {
/* 584 */     return Container.calcRedstoneFromInventory(getLockableContainer(worldIn, pos));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 592 */     EnumFacing enumfacing = EnumFacing.getFront(meta);
/*     */     
/* 594 */     if (enumfacing.getAxis() == EnumFacing.Axis.Y)
/*     */     {
/* 596 */       enumfacing = EnumFacing.NORTH;
/*     */     }
/*     */     
/* 599 */     return getDefaultState().withProperty(FACING, enumfacing);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 607 */     return ((EnumFacing)state.getValue(FACING)).getIndex();
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 612 */     return new BlockState(this, new IProperty[] { FACING });
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockChest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */