/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumFacing.Plane;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public abstract class BlockRailBase extends Block
/*     */ {
/*     */   protected final boolean isPowered;
/*     */   
/*     */   public static boolean isRailBlock(World worldIn, BlockPos pos)
/*     */   {
/*  26 */     return isRailBlock(worldIn.getBlockState(pos));
/*     */   }
/*     */   
/*     */   public static boolean isRailBlock(IBlockState state)
/*     */   {
/*  31 */     Block block = state.getBlock();
/*  32 */     return (block == Blocks.rail) || (block == Blocks.golden_rail) || (block == Blocks.detector_rail) || (block == Blocks.activator_rail);
/*     */   }
/*     */   
/*     */   protected BlockRailBase(boolean isPowered)
/*     */   {
/*  37 */     super(Material.circuits);
/*  38 */     this.isPowered = isPowered;
/*  39 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
/*  40 */     setCreativeTab(CreativeTabs.tabTransport);
/*     */   }
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  45 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpaqueCube()
/*     */   {
/*  53 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public net.minecraft.util.MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end)
/*     */   {
/*  61 */     setBlockBoundsBasedOnState(worldIn, pos);
/*  62 */     return super.collisionRayTrace(worldIn, pos, start, end);
/*     */   }
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/*  67 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*  68 */     EnumRailDirection blockrailbase$enumraildirection = iblockstate.getBlock() == this ? (EnumRailDirection)iblockstate.getValue(getShapeProperty()) : null;
/*     */     
/*  70 */     if ((blockrailbase$enumraildirection != null) && (blockrailbase$enumraildirection.isAscending()))
/*     */     {
/*  72 */       setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
/*     */     }
/*     */     else
/*     */     {
/*  76 */       setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isFullCube()
/*     */   {
/*  82 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
/*     */   {
/*  87 */     return World.doesBlockHaveSolidTopSurface(worldIn, pos.down());
/*     */   }
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  92 */     if (!worldIn.isRemote)
/*     */     {
/*  94 */       state = func_176564_a(worldIn, pos, state, true);
/*     */       
/*  96 */       if (this.isPowered)
/*     */       {
/*  98 */         onNeighborBlockChange(worldIn, pos, state, this);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*     */   {
/* 108 */     if (!worldIn.isRemote)
/*     */     {
/* 110 */       EnumRailDirection blockrailbase$enumraildirection = (EnumRailDirection)state.getValue(getShapeProperty());
/* 111 */       boolean flag = false;
/*     */       
/* 113 */       if (!World.doesBlockHaveSolidTopSurface(worldIn, pos.down()))
/*     */       {
/* 115 */         flag = true;
/*     */       }
/*     */       
/* 118 */       if ((blockrailbase$enumraildirection == EnumRailDirection.ASCENDING_EAST) && (!World.doesBlockHaveSolidTopSurface(worldIn, pos.east())))
/*     */       {
/* 120 */         flag = true;
/*     */       }
/* 122 */       else if ((blockrailbase$enumraildirection == EnumRailDirection.ASCENDING_WEST) && (!World.doesBlockHaveSolidTopSurface(worldIn, pos.west())))
/*     */       {
/* 124 */         flag = true;
/*     */       }
/* 126 */       else if ((blockrailbase$enumraildirection == EnumRailDirection.ASCENDING_NORTH) && (!World.doesBlockHaveSolidTopSurface(worldIn, pos.north())))
/*     */       {
/* 128 */         flag = true;
/*     */       }
/* 130 */       else if ((blockrailbase$enumraildirection == EnumRailDirection.ASCENDING_SOUTH) && (!World.doesBlockHaveSolidTopSurface(worldIn, pos.south())))
/*     */       {
/* 132 */         flag = true;
/*     */       }
/*     */       
/* 135 */       if (flag)
/*     */       {
/* 137 */         dropBlockAsItem(worldIn, pos, state, 0);
/* 138 */         worldIn.setBlockToAir(pos);
/*     */       }
/*     */       else
/*     */       {
/* 142 */         onNeighborChangedInternal(worldIn, pos, state, neighborBlock);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void onNeighborChangedInternal(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {}
/*     */   
/*     */ 
/*     */   protected IBlockState func_176564_a(World worldIn, BlockPos p_176564_2_, IBlockState p_176564_3_, boolean p_176564_4_)
/*     */   {
/* 153 */     return worldIn.isRemote ? p_176564_3_ : new Rail(worldIn, p_176564_2_, p_176564_3_).func_180364_a(worldIn.isBlockPowered(p_176564_2_), p_176564_4_).getBlockState();
/*     */   }
/*     */   
/*     */   public int getMobilityFlag()
/*     */   {
/* 158 */     return 0;
/*     */   }
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer()
/*     */   {
/* 163 */     return EnumWorldBlockLayer.CUTOUT;
/*     */   }
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 168 */     super.breakBlock(worldIn, pos, state);
/*     */     
/* 170 */     if (((EnumRailDirection)state.getValue(getShapeProperty())).isAscending())
/*     */     {
/* 172 */       worldIn.notifyNeighborsOfStateChange(pos.up(), this);
/*     */     }
/*     */     
/* 175 */     if (this.isPowered)
/*     */     {
/* 177 */       worldIn.notifyNeighborsOfStateChange(pos, this);
/* 178 */       worldIn.notifyNeighborsOfStateChange(pos.down(), this);
/*     */     }
/*     */   }
/*     */   
/*     */   public abstract IProperty<EnumRailDirection> getShapeProperty();
/*     */   
/*     */   public static enum EnumRailDirection implements IStringSerializable
/*     */   {
/* 186 */     NORTH_SOUTH(0, "north_south"), 
/* 187 */     EAST_WEST(1, "east_west"), 
/* 188 */     ASCENDING_EAST(2, "ascending_east"), 
/* 189 */     ASCENDING_WEST(3, "ascending_west"), 
/* 190 */     ASCENDING_NORTH(4, "ascending_north"), 
/* 191 */     ASCENDING_SOUTH(5, "ascending_south"), 
/* 192 */     SOUTH_EAST(6, "south_east"), 
/* 193 */     SOUTH_WEST(7, "south_west"), 
/* 194 */     NORTH_WEST(8, "north_west"), 
/* 195 */     NORTH_EAST(9, "north_east");
/*     */     
/*     */     private static final EnumRailDirection[] META_LOOKUP;
/*     */     private final int meta;
/*     */     private final String name;
/*     */     
/*     */     private EnumRailDirection(int meta, String name)
/*     */     {
/* 203 */       this.meta = meta;
/* 204 */       this.name = name;
/*     */     }
/*     */     
/*     */     public int getMetadata()
/*     */     {
/* 209 */       return this.meta;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 214 */       return this.name;
/*     */     }
/*     */     
/*     */     public boolean isAscending()
/*     */     {
/* 219 */       return (this == ASCENDING_NORTH) || (this == ASCENDING_EAST) || (this == ASCENDING_SOUTH) || (this == ASCENDING_WEST);
/*     */     }
/*     */     
/*     */     public static EnumRailDirection byMetadata(int meta)
/*     */     {
/* 224 */       if ((meta < 0) || (meta >= META_LOOKUP.length))
/*     */       {
/* 226 */         meta = 0;
/*     */       }
/*     */       
/* 229 */       return META_LOOKUP[meta];
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 234 */       return this.name;
/*     */     }
/*     */     
/*     */     static
/*     */     {
/* 197 */       META_LOOKUP = new EnumRailDirection[values().length];
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
/*     */       EnumRailDirection[] arrayOfEnumRailDirection;
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
/* 238 */       int j = (arrayOfEnumRailDirection = values()).length; for (int i = 0; i < j; i++) { EnumRailDirection blockrailbase$enumraildirection = arrayOfEnumRailDirection[i];
/*     */         
/* 240 */         META_LOOKUP[blockrailbase$enumraildirection.getMetadata()] = blockrailbase$enumraildirection;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public class Rail
/*     */   {
/*     */     private final World world;
/*     */     private final BlockPos pos;
/*     */     private final BlockRailBase block;
/*     */     private IBlockState state;
/*     */     private final boolean isPowered;
/* 252 */     private final List<BlockPos> field_150657_g = Lists.newArrayList();
/*     */     
/*     */     public Rail(World worldIn, BlockPos pos, IBlockState state)
/*     */     {
/* 256 */       this.world = worldIn;
/* 257 */       this.pos = pos;
/* 258 */       this.state = state;
/* 259 */       this.block = ((BlockRailBase)state.getBlock());
/* 260 */       BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = (BlockRailBase.EnumRailDirection)state.getValue(BlockRailBase.this.getShapeProperty());
/* 261 */       this.isPowered = this.block.isPowered;
/* 262 */       func_180360_a(blockrailbase$enumraildirection);
/*     */     }
/*     */     
/*     */     private void func_180360_a(BlockRailBase.EnumRailDirection p_180360_1_)
/*     */     {
/* 267 */       this.field_150657_g.clear();
/*     */       
/* 269 */       switch (p_180360_1_)
/*     */       {
/*     */       case ASCENDING_EAST: 
/* 272 */         this.field_150657_g.add(this.pos.north());
/* 273 */         this.field_150657_g.add(this.pos.south());
/* 274 */         break;
/*     */       
/*     */       case ASCENDING_NORTH: 
/* 277 */         this.field_150657_g.add(this.pos.west());
/* 278 */         this.field_150657_g.add(this.pos.east());
/* 279 */         break;
/*     */       
/*     */       case ASCENDING_SOUTH: 
/* 282 */         this.field_150657_g.add(this.pos.west());
/* 283 */         this.field_150657_g.add(this.pos.east().up());
/* 284 */         break;
/*     */       
/*     */       case ASCENDING_WEST: 
/* 287 */         this.field_150657_g.add(this.pos.west().up());
/* 288 */         this.field_150657_g.add(this.pos.east());
/* 289 */         break;
/*     */       
/*     */       case EAST_WEST: 
/* 292 */         this.field_150657_g.add(this.pos.north().up());
/* 293 */         this.field_150657_g.add(this.pos.south());
/* 294 */         break;
/*     */       
/*     */       case NORTH_EAST: 
/* 297 */         this.field_150657_g.add(this.pos.north());
/* 298 */         this.field_150657_g.add(this.pos.south().up());
/* 299 */         break;
/*     */       
/*     */       case NORTH_SOUTH: 
/* 302 */         this.field_150657_g.add(this.pos.east());
/* 303 */         this.field_150657_g.add(this.pos.south());
/* 304 */         break;
/*     */       
/*     */       case NORTH_WEST: 
/* 307 */         this.field_150657_g.add(this.pos.west());
/* 308 */         this.field_150657_g.add(this.pos.south());
/* 309 */         break;
/*     */       
/*     */       case SOUTH_EAST: 
/* 312 */         this.field_150657_g.add(this.pos.west());
/* 313 */         this.field_150657_g.add(this.pos.north());
/* 314 */         break;
/*     */       
/*     */       case SOUTH_WEST: 
/* 317 */         this.field_150657_g.add(this.pos.east());
/* 318 */         this.field_150657_g.add(this.pos.north());
/*     */       }
/*     */     }
/*     */     
/*     */     private void func_150651_b()
/*     */     {
/* 324 */       for (int i = 0; i < this.field_150657_g.size(); i++)
/*     */       {
/* 326 */         Rail blockrailbase$rail = findRailAt((BlockPos)this.field_150657_g.get(i));
/*     */         
/* 328 */         if ((blockrailbase$rail != null) && (blockrailbase$rail.func_150653_a(this)))
/*     */         {
/* 330 */           this.field_150657_g.set(i, blockrailbase$rail.pos);
/*     */         }
/*     */         else
/*     */         {
/* 334 */           this.field_150657_g.remove(i--);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     private boolean hasRailAt(BlockPos pos)
/*     */     {
/* 341 */       return (BlockRailBase.isRailBlock(this.world, pos)) || (BlockRailBase.isRailBlock(this.world, pos.up())) || (BlockRailBase.isRailBlock(this.world, pos.down()));
/*     */     }
/*     */     
/*     */     private Rail findRailAt(BlockPos pos)
/*     */     {
/* 346 */       IBlockState iblockstate = this.world.getBlockState(pos);
/*     */       
/* 348 */       if (BlockRailBase.isRailBlock(iblockstate))
/*     */       {
/* 350 */         BlockRailBase tmp24_21 = BlockRailBase.this;tmp24_21.getClass();return new Rail(tmp24_21, this.world, pos, iblockstate);
/*     */       }
/*     */       
/*     */ 
/* 354 */       BlockPos lvt_2_1_ = pos.up();
/* 355 */       iblockstate = this.world.getBlockState(lvt_2_1_);
/*     */       
/* 357 */       if (BlockRailBase.isRailBlock(iblockstate))
/*     */       {
/* 359 */         BlockRailBase tmp68_65 = BlockRailBase.this;tmp68_65.getClass();return new Rail(tmp68_65, this.world, lvt_2_1_, iblockstate);
/*     */       }
/*     */       
/*     */ 
/* 363 */       lvt_2_1_ = pos.down();
/* 364 */       iblockstate = this.world.getBlockState(lvt_2_1_); BlockRailBase 
/* 365 */         tmp112_109 = BlockRailBase.this;tmp112_109.getClass();return BlockRailBase.isRailBlock(iblockstate) ? new Rail(tmp112_109, this.world, lvt_2_1_, iblockstate) : null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private boolean func_150653_a(Rail p_150653_1_)
/*     */     {
/* 372 */       return func_180363_c(p_150653_1_.pos);
/*     */     }
/*     */     
/*     */     private boolean func_180363_c(BlockPos p_180363_1_)
/*     */     {
/* 377 */       for (int i = 0; i < this.field_150657_g.size(); i++)
/*     */       {
/* 379 */         BlockPos blockpos = (BlockPos)this.field_150657_g.get(i);
/*     */         
/* 381 */         if ((blockpos.getX() == p_180363_1_.getX()) && (blockpos.getZ() == p_180363_1_.getZ()))
/*     */         {
/* 383 */           return true;
/*     */         }
/*     */       }
/*     */       
/* 387 */       return false;
/*     */     }
/*     */     
/*     */     protected int countAdjacentRails()
/*     */     {
/* 392 */       int i = 0;
/*     */       
/* 394 */       for (Object enumfacing0 : EnumFacing.Plane.HORIZONTAL)
/*     */       {
/* 396 */         EnumFacing enumfacing = (EnumFacing)enumfacing0;
/*     */         
/* 398 */         if (hasRailAt(this.pos.offset(enumfacing)))
/*     */         {
/* 400 */           i++;
/*     */         }
/*     */       }
/*     */       
/* 404 */       return i;
/*     */     }
/*     */     
/*     */     private boolean func_150649_b(Rail rail)
/*     */     {
/* 409 */       return (func_150653_a(rail)) || (this.field_150657_g.size() != 2);
/*     */     }
/*     */     
/*     */     private void func_150645_c(Rail p_150645_1_)
/*     */     {
/* 414 */       this.field_150657_g.add(p_150645_1_.pos);
/* 415 */       BlockPos blockpos = this.pos.north();
/* 416 */       BlockPos blockpos1 = this.pos.south();
/* 417 */       BlockPos blockpos2 = this.pos.west();
/* 418 */       BlockPos blockpos3 = this.pos.east();
/* 419 */       boolean flag = func_180363_c(blockpos);
/* 420 */       boolean flag1 = func_180363_c(blockpos1);
/* 421 */       boolean flag2 = func_180363_c(blockpos2);
/* 422 */       boolean flag3 = func_180363_c(blockpos3);
/* 423 */       BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = null;
/*     */       
/* 425 */       if ((flag) || (flag1))
/*     */       {
/* 427 */         blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
/*     */       }
/*     */       
/* 430 */       if ((flag2) || (flag3))
/*     */       {
/* 432 */         blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.EAST_WEST;
/*     */       }
/*     */       
/* 435 */       if (!this.isPowered)
/*     */       {
/* 437 */         if ((flag1) && (flag3) && (!flag) && (!flag2))
/*     */         {
/* 439 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_EAST;
/*     */         }
/*     */         
/* 442 */         if ((flag1) && (flag2) && (!flag) && (!flag3))
/*     */         {
/* 444 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_WEST;
/*     */         }
/*     */         
/* 447 */         if ((flag) && (flag2) && (!flag1) && (!flag3))
/*     */         {
/* 449 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_WEST;
/*     */         }
/*     */         
/* 452 */         if ((flag) && (flag3) && (!flag1) && (!flag2))
/*     */         {
/* 454 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_EAST;
/*     */         }
/*     */       }
/*     */       
/* 458 */       if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.NORTH_SOUTH)
/*     */       {
/* 460 */         if (BlockRailBase.isRailBlock(this.world, blockpos.up()))
/*     */         {
/* 462 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_NORTH;
/*     */         }
/*     */         
/* 465 */         if (BlockRailBase.isRailBlock(this.world, blockpos1.up()))
/*     */         {
/* 467 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_SOUTH;
/*     */         }
/*     */       }
/*     */       
/* 471 */       if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.EAST_WEST)
/*     */       {
/* 473 */         if (BlockRailBase.isRailBlock(this.world, blockpos3.up()))
/*     */         {
/* 475 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_EAST;
/*     */         }
/*     */         
/* 478 */         if (BlockRailBase.isRailBlock(this.world, blockpos2.up()))
/*     */         {
/* 480 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_WEST;
/*     */         }
/*     */       }
/*     */       
/* 484 */       if (blockrailbase$enumraildirection == null)
/*     */       {
/* 486 */         blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
/*     */       }
/*     */       
/* 489 */       this.state = this.state.withProperty(this.block.getShapeProperty(), blockrailbase$enumraildirection);
/* 490 */       this.world.setBlockState(this.pos, this.state, 3);
/*     */     }
/*     */     
/*     */     private boolean func_180361_d(BlockPos p_180361_1_)
/*     */     {
/* 495 */       Rail blockrailbase$rail = findRailAt(p_180361_1_);
/*     */       
/* 497 */       if (blockrailbase$rail == null)
/*     */       {
/* 499 */         return false;
/*     */       }
/*     */       
/*     */ 
/* 503 */       blockrailbase$rail.func_150651_b();
/* 504 */       return blockrailbase$rail.func_150649_b(this);
/*     */     }
/*     */     
/*     */ 
/*     */     public Rail func_180364_a(boolean p_180364_1_, boolean p_180364_2_)
/*     */     {
/* 510 */       BlockPos blockpos = this.pos.north();
/* 511 */       BlockPos blockpos1 = this.pos.south();
/* 512 */       BlockPos blockpos2 = this.pos.west();
/* 513 */       BlockPos blockpos3 = this.pos.east();
/* 514 */       boolean flag = func_180361_d(blockpos);
/* 515 */       boolean flag1 = func_180361_d(blockpos1);
/* 516 */       boolean flag2 = func_180361_d(blockpos2);
/* 517 */       boolean flag3 = func_180361_d(blockpos3);
/* 518 */       BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = null;
/*     */       
/* 520 */       if (((flag) || (flag1)) && (!flag2) && (!flag3))
/*     */       {
/* 522 */         blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
/*     */       }
/*     */       
/* 525 */       if (((flag2) || (flag3)) && (!flag) && (!flag1))
/*     */       {
/* 527 */         blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.EAST_WEST;
/*     */       }
/*     */       
/* 530 */       if (!this.isPowered)
/*     */       {
/* 532 */         if ((flag1) && (flag3) && (!flag) && (!flag2))
/*     */         {
/* 534 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_EAST;
/*     */         }
/*     */         
/* 537 */         if ((flag1) && (flag2) && (!flag) && (!flag3))
/*     */         {
/* 539 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_WEST;
/*     */         }
/*     */         
/* 542 */         if ((flag) && (flag2) && (!flag1) && (!flag3))
/*     */         {
/* 544 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_WEST;
/*     */         }
/*     */         
/* 547 */         if ((flag) && (flag3) && (!flag1) && (!flag2))
/*     */         {
/* 549 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_EAST;
/*     */         }
/*     */       }
/*     */       
/* 553 */       if (blockrailbase$enumraildirection == null)
/*     */       {
/* 555 */         if ((flag) || (flag1))
/*     */         {
/* 557 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
/*     */         }
/*     */         
/* 560 */         if ((flag2) || (flag3))
/*     */         {
/* 562 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.EAST_WEST;
/*     */         }
/*     */         
/* 565 */         if (!this.isPowered)
/*     */         {
/* 567 */           if (p_180364_1_)
/*     */           {
/* 569 */             if ((flag1) && (flag3))
/*     */             {
/* 571 */               blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_EAST;
/*     */             }
/*     */             
/* 574 */             if ((flag2) && (flag1))
/*     */             {
/* 576 */               blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_WEST;
/*     */             }
/*     */             
/* 579 */             if ((flag3) && (flag))
/*     */             {
/* 581 */               blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_EAST;
/*     */             }
/*     */             
/* 584 */             if ((flag) && (flag2))
/*     */             {
/* 586 */               blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_WEST;
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 591 */             if ((flag) && (flag2))
/*     */             {
/* 593 */               blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_WEST;
/*     */             }
/*     */             
/* 596 */             if ((flag3) && (flag))
/*     */             {
/* 598 */               blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_EAST;
/*     */             }
/*     */             
/* 601 */             if ((flag2) && (flag1))
/*     */             {
/* 603 */               blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_WEST;
/*     */             }
/*     */             
/* 606 */             if ((flag1) && (flag3))
/*     */             {
/* 608 */               blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.SOUTH_EAST;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 614 */       if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.NORTH_SOUTH)
/*     */       {
/* 616 */         if (BlockRailBase.isRailBlock(this.world, blockpos.up()))
/*     */         {
/* 618 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_NORTH;
/*     */         }
/*     */         
/* 621 */         if (BlockRailBase.isRailBlock(this.world, blockpos1.up()))
/*     */         {
/* 623 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_SOUTH;
/*     */         }
/*     */       }
/*     */       
/* 627 */       if (blockrailbase$enumraildirection == BlockRailBase.EnumRailDirection.EAST_WEST)
/*     */       {
/* 629 */         if (BlockRailBase.isRailBlock(this.world, blockpos3.up()))
/*     */         {
/* 631 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_EAST;
/*     */         }
/*     */         
/* 634 */         if (BlockRailBase.isRailBlock(this.world, blockpos2.up()))
/*     */         {
/* 636 */           blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.ASCENDING_WEST;
/*     */         }
/*     */       }
/*     */       
/* 640 */       if (blockrailbase$enumraildirection == null)
/*     */       {
/* 642 */         blockrailbase$enumraildirection = BlockRailBase.EnumRailDirection.NORTH_SOUTH;
/*     */       }
/*     */       
/* 645 */       func_180360_a(blockrailbase$enumraildirection);
/* 646 */       this.state = this.state.withProperty(this.block.getShapeProperty(), blockrailbase$enumraildirection);
/*     */       
/* 648 */       if ((p_180364_2_) || (this.world.getBlockState(this.pos) != this.state))
/*     */       {
/* 650 */         this.world.setBlockState(this.pos, this.state, 3);
/*     */         
/* 652 */         for (int i = 0; i < this.field_150657_g.size(); i++)
/*     */         {
/* 654 */           Rail blockrailbase$rail = findRailAt((BlockPos)this.field_150657_g.get(i));
/*     */           
/* 656 */           if (blockrailbase$rail != null)
/*     */           {
/* 658 */             blockrailbase$rail.func_150651_b();
/*     */             
/* 660 */             if (blockrailbase$rail.func_150649_b(this))
/*     */             {
/* 662 */               blockrailbase$rail.func_150645_c(this);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 668 */       return this;
/*     */     }
/*     */     
/*     */     public IBlockState getBlockState()
/*     */     {
/* 673 */       return this.state;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockRailBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */