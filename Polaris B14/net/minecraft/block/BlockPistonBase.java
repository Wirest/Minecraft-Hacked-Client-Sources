/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.properties.PropertyDirection;
/*     */ import net.minecraft.block.state.BlockPistonStructureHelper;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityPiston;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.border.WorldBorder;
/*     */ 
/*     */ public class BlockPistonBase extends Block
/*     */ {
/*  27 */   public static final PropertyDirection FACING = PropertyDirection.create("facing");
/*  28 */   public static final PropertyBool EXTENDED = PropertyBool.create("extended");
/*     */   
/*     */   private final boolean isSticky;
/*     */   
/*     */ 
/*     */   public BlockPistonBase(boolean isSticky)
/*     */   {
/*  35 */     super(Material.piston);
/*  36 */     setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(EXTENDED, Boolean.valueOf(false)));
/*  37 */     this.isSticky = isSticky;
/*  38 */     setStepSound(soundTypePiston);
/*  39 */     setHardness(0.5F);
/*  40 */     setCreativeTab(net.minecraft.creativetab.CreativeTabs.tabRedstone);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpaqueCube()
/*     */   {
/*  48 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
/*     */   {
/*  56 */     worldIn.setBlockState(pos, state.withProperty(FACING, getFacingFromEntity(worldIn, pos, placer)), 2);
/*     */     
/*  58 */     if (!worldIn.isRemote)
/*     */     {
/*  60 */       checkForMove(worldIn, pos, state);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*     */   {
/*  69 */     if (!worldIn.isRemote)
/*     */     {
/*  71 */       checkForMove(worldIn, pos, state);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  77 */     if ((!worldIn.isRemote) && (worldIn.getTileEntity(pos) == null))
/*     */     {
/*  79 */       checkForMove(worldIn, pos, state);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
/*     */   {
/*  89 */     return getDefaultState().withProperty(FACING, getFacingFromEntity(worldIn, pos, placer)).withProperty(EXTENDED, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */   private void checkForMove(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  94 */     EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
/*  95 */     boolean flag = shouldBeExtended(worldIn, pos, enumfacing);
/*     */     
/*  97 */     if ((flag) && (!((Boolean)state.getValue(EXTENDED)).booleanValue()))
/*     */     {
/*  99 */       if (new BlockPistonStructureHelper(worldIn, pos, enumfacing, true).canMove())
/*     */       {
/* 101 */         worldIn.addBlockEvent(pos, this, 0, enumfacing.getIndex());
/*     */       }
/*     */     }
/* 104 */     else if ((!flag) && (((Boolean)state.getValue(EXTENDED)).booleanValue()))
/*     */     {
/* 106 */       worldIn.setBlockState(pos, state.withProperty(EXTENDED, Boolean.valueOf(false)), 2);
/* 107 */       worldIn.addBlockEvent(pos, this, 1, enumfacing.getIndex());
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean shouldBeExtended(World worldIn, BlockPos pos, EnumFacing facing) {
/*     */     EnumFacing[] arrayOfEnumFacing1;
/* 113 */     int j = (arrayOfEnumFacing1 = EnumFacing.values()).length; for (int i = 0; i < j; i++) { EnumFacing enumfacing = arrayOfEnumFacing1[i];
/*     */       
/* 115 */       if ((enumfacing != facing) && (worldIn.isSidePowered(pos.offset(enumfacing), enumfacing)))
/*     */       {
/* 117 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 121 */     if (worldIn.isSidePowered(pos, EnumFacing.DOWN))
/*     */     {
/* 123 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 127 */     BlockPos blockpos = pos.up();
/*     */     EnumFacing[] arrayOfEnumFacing2;
/* 129 */     int k = (arrayOfEnumFacing2 = EnumFacing.values()).length; for (j = 0; j < k; j++) { EnumFacing enumfacing1 = arrayOfEnumFacing2[j];
/*     */       
/* 131 */       if ((enumfacing1 != EnumFacing.DOWN) && (worldIn.isSidePowered(blockpos.offset(enumfacing1), enumfacing1)))
/*     */       {
/* 133 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 137 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam)
/*     */   {
/* 146 */     EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
/*     */     
/* 148 */     if (!worldIn.isRemote)
/*     */     {
/* 150 */       boolean flag = shouldBeExtended(worldIn, pos, enumfacing);
/*     */       
/* 152 */       if ((flag) && (eventID == 1))
/*     */       {
/* 154 */         worldIn.setBlockState(pos, state.withProperty(EXTENDED, Boolean.valueOf(true)), 2);
/* 155 */         return false;
/*     */       }
/*     */       
/* 158 */       if ((!flag) && (eventID == 0))
/*     */       {
/* 160 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 164 */     if (eventID == 0)
/*     */     {
/* 166 */       if (!doMove(worldIn, pos, enumfacing, true))
/*     */       {
/* 168 */         return false;
/*     */       }
/*     */       
/* 171 */       worldIn.setBlockState(pos, state.withProperty(EXTENDED, Boolean.valueOf(true)), 2);
/* 172 */       worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "tile.piston.out", 0.5F, worldIn.rand.nextFloat() * 0.25F + 0.6F);
/*     */     }
/* 174 */     else if (eventID == 1)
/*     */     {
/* 176 */       TileEntity tileentity1 = worldIn.getTileEntity(pos.offset(enumfacing));
/*     */       
/* 178 */       if ((tileentity1 instanceof TileEntityPiston))
/*     */       {
/* 180 */         ((TileEntityPiston)tileentity1).clearPistonTileEntity();
/*     */       }
/*     */       
/* 183 */       worldIn.setBlockState(pos, Blocks.piston_extension.getDefaultState().withProperty(BlockPistonMoving.FACING, enumfacing).withProperty(BlockPistonMoving.TYPE, this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT), 3);
/* 184 */       worldIn.setTileEntity(pos, BlockPistonMoving.newTileEntity(getStateFromMeta(eventParam), enumfacing, false, true));
/*     */       
/* 186 */       if (this.isSticky)
/*     */       {
/* 188 */         BlockPos blockpos = pos.add(enumfacing.getFrontOffsetX() * 2, enumfacing.getFrontOffsetY() * 2, enumfacing.getFrontOffsetZ() * 2);
/* 189 */         Block block = worldIn.getBlockState(blockpos).getBlock();
/* 190 */         boolean flag1 = false;
/*     */         
/* 192 */         if (block == Blocks.piston_extension)
/*     */         {
/* 194 */           TileEntity tileentity = worldIn.getTileEntity(blockpos);
/*     */           
/* 196 */           if ((tileentity instanceof TileEntityPiston))
/*     */           {
/* 198 */             TileEntityPiston tileentitypiston = (TileEntityPiston)tileentity;
/*     */             
/* 200 */             if ((tileentitypiston.getFacing() == enumfacing) && (tileentitypiston.isExtending()))
/*     */             {
/* 202 */               tileentitypiston.clearPistonTileEntity();
/* 203 */               flag1 = true;
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 208 */         if ((!flag1) && (block.getMaterial() != Material.air) && (canPush(block, worldIn, blockpos, enumfacing.getOpposite(), false)) && ((block.getMobilityFlag() == 0) || (block == Blocks.piston) || (block == Blocks.sticky_piston)))
/*     */         {
/* 210 */           doMove(worldIn, pos, enumfacing, false);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 215 */         worldIn.setBlockToAir(pos.offset(enumfacing));
/*     */       }
/*     */       
/* 218 */       worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "tile.piston.in", 0.5F, worldIn.rand.nextFloat() * 0.15F + 0.6F);
/*     */     }
/*     */     
/* 221 */     return true;
/*     */   }
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/* 226 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*     */     EnumFacing enumfacing;
/* 228 */     if ((iblockstate.getBlock() == this) && (((Boolean)iblockstate.getValue(EXTENDED)).booleanValue()))
/*     */     {
/* 230 */       float f = 0.25F;
/* 231 */       enumfacing = (EnumFacing)iblockstate.getValue(FACING);
/*     */       
/* 233 */       if (enumfacing == null) {}
/*     */     } else {
/* 235 */       switch (enumfacing)
/*     */       {
/*     */       case DOWN: 
/* 238 */         setBlockBounds(0.0F, 0.25F, 0.0F, 1.0F, 1.0F, 1.0F);
/* 239 */         break;
/*     */       
/*     */       case EAST: 
/* 242 */         setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
/* 243 */         break;
/*     */       
/*     */       case NORTH: 
/* 246 */         setBlockBounds(0.0F, 0.0F, 0.25F, 1.0F, 1.0F, 1.0F);
/* 247 */         break;
/*     */       
/*     */       case SOUTH: 
/* 250 */         setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.75F);
/* 251 */         break;
/*     */       
/*     */       case UP: 
/* 254 */         setBlockBounds(0.25F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/* 255 */         break;
/*     */       
/*     */       case WEST: 
/* 258 */         setBlockBounds(0.0F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */       default: 
/* 264 */         break;setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */       }
/*     */       
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void setBlockBoundsForItemRender()
/*     */   {
/* 273 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
/*     */   {
/* 281 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/* 282 */     super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*     */   }
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 287 */     setBlockBoundsBasedOnState(worldIn, pos);
/* 288 */     return super.getCollisionBoundingBox(worldIn, pos, state);
/*     */   }
/*     */   
/*     */   public boolean isFullCube()
/*     */   {
/* 293 */     return false;
/*     */   }
/*     */   
/*     */   public static EnumFacing getFacing(int meta)
/*     */   {
/* 298 */     int i = meta & 0x7;
/* 299 */     return i > 5 ? null : EnumFacing.getFront(i);
/*     */   }
/*     */   
/*     */   public static EnumFacing getFacingFromEntity(World worldIn, BlockPos clickedBlock, EntityLivingBase entityIn)
/*     */   {
/* 304 */     if ((MathHelper.abs((float)entityIn.posX - clickedBlock.getX()) < 2.0F) && (MathHelper.abs((float)entityIn.posZ - clickedBlock.getZ()) < 2.0F))
/*     */     {
/* 306 */       double d0 = entityIn.posY + entityIn.getEyeHeight();
/*     */       
/* 308 */       if (d0 - clickedBlock.getY() > 2.0D)
/*     */       {
/* 310 */         return EnumFacing.UP;
/*     */       }
/*     */       
/* 313 */       if (clickedBlock.getY() - d0 > 0.0D)
/*     */       {
/* 315 */         return EnumFacing.DOWN;
/*     */       }
/*     */     }
/*     */     
/* 319 */     return entityIn.getHorizontalFacing().getOpposite();
/*     */   }
/*     */   
/*     */   public static boolean canPush(Block blockIn, World worldIn, BlockPos pos, EnumFacing direction, boolean allowDestroy)
/*     */   {
/* 324 */     if (blockIn == Blocks.obsidian)
/*     */     {
/* 326 */       return false;
/*     */     }
/* 328 */     if (!worldIn.getWorldBorder().contains(pos))
/*     */     {
/* 330 */       return false;
/*     */     }
/* 332 */     if ((pos.getY() >= 0) && ((direction != EnumFacing.DOWN) || (pos.getY() != 0)))
/*     */     {
/* 334 */       if ((pos.getY() <= worldIn.getHeight() - 1) && ((direction != EnumFacing.UP) || (pos.getY() != worldIn.getHeight() - 1)))
/*     */       {
/* 336 */         if ((blockIn != Blocks.piston) && (blockIn != Blocks.sticky_piston))
/*     */         {
/* 338 */           if (blockIn.getBlockHardness(worldIn, pos) == -1.0F)
/*     */           {
/* 340 */             return false;
/*     */           }
/*     */           
/* 343 */           if (blockIn.getMobilityFlag() == 2)
/*     */           {
/* 345 */             return false;
/*     */           }
/*     */           
/* 348 */           if (blockIn.getMobilityFlag() == 1)
/*     */           {
/* 350 */             if (!allowDestroy)
/*     */             {
/* 352 */               return false;
/*     */             }
/*     */             
/* 355 */             return true;
/*     */           }
/*     */         }
/* 358 */         else if (((Boolean)worldIn.getBlockState(pos).getValue(EXTENDED)).booleanValue())
/*     */         {
/* 360 */           return false;
/*     */         }
/*     */         
/* 363 */         return !(blockIn instanceof ITileEntityProvider);
/*     */       }
/*     */       
/*     */ 
/* 367 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 372 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean doMove(World worldIn, BlockPos pos, EnumFacing direction, boolean extending)
/*     */   {
/* 378 */     if (!extending)
/*     */     {
/* 380 */       worldIn.setBlockToAir(pos.offset(direction));
/*     */     }
/*     */     
/* 383 */     BlockPistonStructureHelper blockpistonstructurehelper = new BlockPistonStructureHelper(worldIn, pos, direction, extending);
/* 384 */     List<BlockPos> list = blockpistonstructurehelper.getBlocksToMove();
/* 385 */     List<BlockPos> list1 = blockpistonstructurehelper.getBlocksToDestroy();
/*     */     
/* 387 */     if (!blockpistonstructurehelper.canMove())
/*     */     {
/* 389 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 393 */     int i = list.size() + list1.size();
/* 394 */     Block[] ablock = new Block[i];
/* 395 */     EnumFacing enumfacing = extending ? direction : direction.getOpposite();
/*     */     
/* 397 */     for (int j = list1.size() - 1; j >= 0; j--)
/*     */     {
/* 399 */       BlockPos blockpos = (BlockPos)list1.get(j);
/* 400 */       Block block = worldIn.getBlockState(blockpos).getBlock();
/* 401 */       block.dropBlockAsItem(worldIn, blockpos, worldIn.getBlockState(blockpos), 0);
/* 402 */       worldIn.setBlockToAir(blockpos);
/* 403 */       i--;
/* 404 */       ablock[i] = block;
/*     */     }
/*     */     
/* 407 */     for (int k = list.size() - 1; k >= 0; k--)
/*     */     {
/* 409 */       BlockPos blockpos2 = (BlockPos)list.get(k);
/* 410 */       IBlockState iblockstate = worldIn.getBlockState(blockpos2);
/* 411 */       Block block1 = iblockstate.getBlock();
/* 412 */       block1.getMetaFromState(iblockstate);
/* 413 */       worldIn.setBlockToAir(blockpos2);
/* 414 */       blockpos2 = blockpos2.offset(enumfacing);
/* 415 */       worldIn.setBlockState(blockpos2, Blocks.piston_extension.getDefaultState().withProperty(FACING, direction), 4);
/* 416 */       worldIn.setTileEntity(blockpos2, BlockPistonMoving.newTileEntity(iblockstate, direction, extending, false));
/* 417 */       i--;
/* 418 */       ablock[i] = block1;
/*     */     }
/*     */     
/* 421 */     BlockPos blockpos1 = pos.offset(direction);
/*     */     
/* 423 */     if (extending)
/*     */     {
/* 425 */       BlockPistonExtension.EnumPistonType blockpistonextension$enumpistontype = this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT;
/* 426 */       IBlockState iblockstate1 = Blocks.piston_head.getDefaultState().withProperty(BlockPistonExtension.FACING, direction).withProperty(BlockPistonExtension.TYPE, blockpistonextension$enumpistontype);
/* 427 */       IBlockState iblockstate2 = Blocks.piston_extension.getDefaultState().withProperty(BlockPistonMoving.FACING, direction).withProperty(BlockPistonMoving.TYPE, this.isSticky ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
/* 428 */       worldIn.setBlockState(blockpos1, iblockstate2, 4);
/* 429 */       worldIn.setTileEntity(blockpos1, BlockPistonMoving.newTileEntity(iblockstate1, direction, true, false));
/*     */     }
/*     */     
/* 432 */     for (int l = list1.size() - 1; l >= 0; l--)
/*     */     {
/* 434 */       worldIn.notifyNeighborsOfStateChange((BlockPos)list1.get(l), ablock[(i++)]);
/*     */     }
/*     */     
/* 437 */     for (int i1 = list.size() - 1; i1 >= 0; i1--)
/*     */     {
/* 439 */       worldIn.notifyNeighborsOfStateChange((BlockPos)list.get(i1), ablock[(i++)]);
/*     */     }
/*     */     
/* 442 */     if (extending)
/*     */     {
/* 444 */       worldIn.notifyNeighborsOfStateChange(blockpos1, Blocks.piston_head);
/* 445 */       worldIn.notifyNeighborsOfStateChange(pos, this);
/*     */     }
/*     */     
/* 448 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateForEntityRender(IBlockState state)
/*     */   {
/* 457 */     return getDefaultState().withProperty(FACING, EnumFacing.UP);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 465 */     return getDefaultState().withProperty(FACING, getFacing(meta)).withProperty(EXTENDED, Boolean.valueOf((meta & 0x8) > 0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 473 */     int i = 0;
/* 474 */     i |= ((EnumFacing)state.getValue(FACING)).getIndex();
/*     */     
/* 476 */     if (((Boolean)state.getValue(EXTENDED)).booleanValue())
/*     */     {
/* 478 */       i |= 0x8;
/*     */     }
/*     */     
/* 481 */     return i;
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 486 */     return new BlockState(this, new net.minecraft.block.properties.IProperty[] { FACING, EXTENDED });
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockPistonBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */