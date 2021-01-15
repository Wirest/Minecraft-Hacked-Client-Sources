/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.properties.PropertyInteger;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumFacing.Axis;
/*     */ import net.minecraft.util.EnumFacing.Plane;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockRedstoneWire extends Block
/*     */ {
/*  30 */   public static final PropertyEnum<EnumAttachPosition> NORTH = PropertyEnum.create("north", EnumAttachPosition.class);
/*  31 */   public static final PropertyEnum<EnumAttachPosition> EAST = PropertyEnum.create("east", EnumAttachPosition.class);
/*  32 */   public static final PropertyEnum<EnumAttachPosition> SOUTH = PropertyEnum.create("south", EnumAttachPosition.class);
/*  33 */   public static final PropertyEnum<EnumAttachPosition> WEST = PropertyEnum.create("west", EnumAttachPosition.class);
/*  34 */   public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
/*  35 */   private boolean canProvidePower = true;
/*  36 */   private final Set<BlockPos> blocksNeedingUpdate = Sets.newHashSet();
/*     */   
/*     */   public BlockRedstoneWire()
/*     */   {
/*  40 */     super(Material.circuits);
/*  41 */     setDefaultState(this.blockState.getBaseState().withProperty(NORTH, EnumAttachPosition.NONE).withProperty(EAST, EnumAttachPosition.NONE).withProperty(SOUTH, EnumAttachPosition.NONE).withProperty(WEST, EnumAttachPosition.NONE).withProperty(POWER, Integer.valueOf(0)));
/*  42 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
/*     */   {
/*  51 */     state = state.withProperty(WEST, getAttachPosition(worldIn, pos, EnumFacing.WEST));
/*  52 */     state = state.withProperty(EAST, getAttachPosition(worldIn, pos, EnumFacing.EAST));
/*  53 */     state = state.withProperty(NORTH, getAttachPosition(worldIn, pos, EnumFacing.NORTH));
/*  54 */     state = state.withProperty(SOUTH, getAttachPosition(worldIn, pos, EnumFacing.SOUTH));
/*  55 */     return state;
/*     */   }
/*     */   
/*     */   private EnumAttachPosition getAttachPosition(IBlockAccess worldIn, BlockPos pos, EnumFacing direction)
/*     */   {
/*  60 */     BlockPos blockpos = pos.offset(direction);
/*  61 */     Block block = worldIn.getBlockState(pos.offset(direction)).getBlock();
/*     */     
/*  63 */     if ((!canConnectTo(worldIn.getBlockState(blockpos), direction)) && ((block.isBlockNormalCube()) || (!canConnectUpwardsTo(worldIn.getBlockState(blockpos.down())))))
/*     */     {
/*  65 */       Block block1 = worldIn.getBlockState(pos.up()).getBlock();
/*  66 */       return (!block1.isBlockNormalCube()) && (block.isBlockNormalCube()) && (canConnectUpwardsTo(worldIn.getBlockState(blockpos.up()))) ? EnumAttachPosition.UP : EnumAttachPosition.NONE;
/*     */     }
/*     */     
/*     */ 
/*  70 */     return EnumAttachPosition.SIDE;
/*     */   }
/*     */   
/*     */ 
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  76 */     return null;
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
/*     */   public int colorMultiplier(IBlockAccess worldIn, BlockPos pos, int renderPass)
/*     */   {
/*  94 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*  95 */     return iblockstate.getBlock() != this ? super.colorMultiplier(worldIn, pos, renderPass) : colorMultiplier(((Integer)iblockstate.getValue(POWER)).intValue());
/*     */   }
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
/*     */   {
/* 100 */     return (World.doesBlockHaveSolidTopSurface(worldIn, pos.down())) || (worldIn.getBlockState(pos.down()).getBlock() == Blocks.glowstone);
/*     */   }
/*     */   
/*     */   private IBlockState updateSurroundingRedstone(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 105 */     state = calculateCurrentChanges(worldIn, pos, pos, state);
/* 106 */     java.util.List<BlockPos> list = com.google.common.collect.Lists.newArrayList(this.blocksNeedingUpdate);
/* 107 */     this.blocksNeedingUpdate.clear();
/*     */     
/* 109 */     for (BlockPos blockpos : list)
/*     */     {
/* 111 */       worldIn.notifyNeighborsOfStateChange(blockpos, this);
/*     */     }
/*     */     
/* 114 */     return state;
/*     */   }
/*     */   
/*     */   private IBlockState calculateCurrentChanges(World worldIn, BlockPos pos1, BlockPos pos2, IBlockState state)
/*     */   {
/* 119 */     IBlockState iblockstate = state;
/* 120 */     int i = ((Integer)state.getValue(POWER)).intValue();
/* 121 */     int j = 0;
/* 122 */     j = getMaxCurrentStrength(worldIn, pos2, j);
/* 123 */     this.canProvidePower = false;
/* 124 */     int k = worldIn.isBlockIndirectlyGettingPowered(pos1);
/* 125 */     this.canProvidePower = true;
/*     */     
/* 127 */     if ((k > 0) && (k > j - 1))
/*     */     {
/* 129 */       j = k;
/*     */     }
/*     */     
/* 132 */     int l = 0;
/*     */     EnumFacing enumfacing;
/* 134 */     BlockPos blockpos; for (Object enumfacing0 : EnumFacing.Plane.HORIZONTAL)
/*     */     {
/* 136 */       enumfacing = (EnumFacing)enumfacing0;
/* 137 */       blockpos = pos1.offset(enumfacing);
/* 138 */       boolean flag = (blockpos.getX() != pos2.getX()) || (blockpos.getZ() != pos2.getZ());
/*     */       
/* 140 */       if (flag)
/*     */       {
/* 142 */         l = getMaxCurrentStrength(worldIn, blockpos, l);
/*     */       }
/*     */       
/* 145 */       if ((worldIn.getBlockState(blockpos).getBlock().isNormalCube()) && (!worldIn.getBlockState(pos1.up()).getBlock().isNormalCube()))
/*     */       {
/* 147 */         if ((flag) && (pos1.getY() >= pos2.getY()))
/*     */         {
/* 149 */           l = getMaxCurrentStrength(worldIn, blockpos.up(), l);
/*     */         }
/*     */       }
/* 152 */       else if ((!worldIn.getBlockState(blockpos).getBlock().isNormalCube()) && (flag) && (pos1.getY() <= pos2.getY()))
/*     */       {
/* 154 */         l = getMaxCurrentStrength(worldIn, blockpos.down(), l);
/*     */       }
/*     */     }
/*     */     
/* 158 */     if (l > j)
/*     */     {
/* 160 */       j = l - 1;
/*     */     }
/* 162 */     else if (j > 0)
/*     */     {
/* 164 */       j--;
/*     */     }
/*     */     else
/*     */     {
/* 168 */       j = 0;
/*     */     }
/*     */     
/* 171 */     if (k > j - 1)
/*     */     {
/* 173 */       j = k;
/*     */     }
/*     */     
/* 176 */     if (i != j)
/*     */     {
/* 178 */       state = state.withProperty(POWER, Integer.valueOf(j));
/*     */       
/* 180 */       if (worldIn.getBlockState(pos1) == iblockstate)
/*     */       {
/* 182 */         worldIn.setBlockState(pos1, state, 2);
/*     */       }
/*     */       
/* 185 */       this.blocksNeedingUpdate.add(pos1);
/*     */       
/* 187 */       enumfacing = (blockpos = EnumFacing.values()).length; for (EnumFacing localEnumFacing1 = 0; localEnumFacing1 < enumfacing; localEnumFacing1++) { EnumFacing enumfacing1 = blockpos[localEnumFacing1];
/*     */         
/* 189 */         this.blocksNeedingUpdate.add(pos1.offset(enumfacing1));
/*     */       }
/*     */     }
/*     */     
/* 193 */     return state;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void notifyWireNeighborsOfStateChange(World worldIn, BlockPos pos)
/*     */   {
/* 202 */     if (worldIn.getBlockState(pos).getBlock() == this)
/*     */     {
/* 204 */       worldIn.notifyNeighborsOfStateChange(pos, this);
/*     */       EnumFacing[] arrayOfEnumFacing;
/* 206 */       int j = (arrayOfEnumFacing = EnumFacing.values()).length; for (int i = 0; i < j; i++) { EnumFacing enumfacing = arrayOfEnumFacing[i];
/*     */         
/* 208 */         worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 215 */     if (!worldIn.isRemote)
/*     */     {
/* 217 */       updateSurroundingRedstone(worldIn, pos, state);
/*     */       
/* 219 */       for (Object enumfacing : EnumFacing.Plane.VERTICAL)
/*     */       {
/* 221 */         worldIn.notifyNeighborsOfStateChange(pos.offset((EnumFacing)enumfacing), this);
/*     */       }
/*     */       
/* 224 */       for (Object enumfacing10 : EnumFacing.Plane.HORIZONTAL)
/*     */       {
/* 226 */         EnumFacing enumfacing1 = (EnumFacing)enumfacing10;
/* 227 */         notifyWireNeighborsOfStateChange(worldIn, pos.offset(enumfacing1));
/*     */       }
/*     */       
/* 230 */       for (Object enumfacing20 : EnumFacing.Plane.HORIZONTAL)
/*     */       {
/* 232 */         EnumFacing enumfacing2 = (EnumFacing)enumfacing20;
/* 233 */         BlockPos blockpos = pos.offset(enumfacing2);
/*     */         
/* 235 */         if (worldIn.getBlockState(blockpos).getBlock().isNormalCube())
/*     */         {
/* 237 */           notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
/*     */         }
/*     */         else
/*     */         {
/* 241 */           notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 249 */     super.breakBlock(worldIn, pos, state);
/*     */     
/* 251 */     if (!worldIn.isRemote) {
/*     */       EnumFacing[] arrayOfEnumFacing;
/* 253 */       int j = (arrayOfEnumFacing = EnumFacing.values()).length; for (int i = 0; i < j; i++) { EnumFacing enumfacing = arrayOfEnumFacing[i];
/*     */         
/* 255 */         worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
/*     */       }
/*     */       
/* 258 */       updateSurroundingRedstone(worldIn, pos, state);
/*     */       
/* 260 */       for (Object enumfacing1 : EnumFacing.Plane.HORIZONTAL)
/*     */       {
/* 262 */         notifyWireNeighborsOfStateChange(worldIn, pos.offset((EnumFacing)enumfacing1));
/*     */       }
/*     */       
/* 265 */       for (Object enumfacing2 : EnumFacing.Plane.HORIZONTAL)
/*     */       {
/* 267 */         BlockPos blockpos = pos.offset((EnumFacing)enumfacing2);
/*     */         
/* 269 */         if (worldIn.getBlockState(blockpos).getBlock().isNormalCube())
/*     */         {
/* 271 */           notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
/*     */         }
/*     */         else
/*     */         {
/* 275 */           notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private int getMaxCurrentStrength(World worldIn, BlockPos pos, int strength)
/*     */   {
/* 283 */     if (worldIn.getBlockState(pos).getBlock() != this)
/*     */     {
/* 285 */       return strength;
/*     */     }
/*     */     
/*     */ 
/* 289 */     int i = ((Integer)worldIn.getBlockState(pos).getValue(POWER)).intValue();
/* 290 */     return i > strength ? i : strength;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*     */   {
/* 299 */     if (!worldIn.isRemote)
/*     */     {
/* 301 */       if (canPlaceBlockAt(worldIn, pos))
/*     */       {
/* 303 */         updateSurroundingRedstone(worldIn, pos, state);
/*     */       }
/*     */       else
/*     */       {
/* 307 */         dropBlockAsItem(worldIn, pos, state, 0);
/* 308 */         worldIn.setBlockToAir(pos);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*     */   {
/* 318 */     return Items.redstone;
/*     */   }
/*     */   
/*     */   public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
/*     */   {
/* 323 */     return !this.canProvidePower ? 0 : getWeakPower(worldIn, pos, state, side);
/*     */   }
/*     */   
/*     */   public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
/*     */   {
/* 328 */     if (!this.canProvidePower)
/*     */     {
/* 330 */       return 0;
/*     */     }
/*     */     
/*     */ 
/* 334 */     int i = ((Integer)state.getValue(POWER)).intValue();
/*     */     
/* 336 */     if (i == 0)
/*     */     {
/* 338 */       return 0;
/*     */     }
/* 340 */     if (side == EnumFacing.UP)
/*     */     {
/* 342 */       return i;
/*     */     }
/*     */     
/*     */ 
/* 346 */     EnumSet<EnumFacing> enumset = EnumSet.noneOf(EnumFacing.class);
/*     */     
/* 348 */     for (Object enumfacing0 : EnumFacing.Plane.HORIZONTAL)
/*     */     {
/* 350 */       EnumFacing enumfacing = (EnumFacing)enumfacing0;
/*     */       
/* 352 */       if (func_176339_d(worldIn, pos, enumfacing))
/*     */       {
/* 354 */         enumset.add(enumfacing);
/*     */       }
/*     */     }
/*     */     
/* 358 */     if ((side.getAxis().isHorizontal()) && (enumset.isEmpty()))
/*     */     {
/* 360 */       return i;
/*     */     }
/* 362 */     if ((enumset.contains(side)) && (!enumset.contains(side.rotateYCCW())) && (!enumset.contains(side.rotateY())))
/*     */     {
/* 364 */       return i;
/*     */     }
/*     */     
/*     */ 
/* 368 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean func_176339_d(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
/*     */   {
/* 376 */     BlockPos blockpos = pos.offset(side);
/* 377 */     IBlockState iblockstate = worldIn.getBlockState(blockpos);
/* 378 */     Block block = iblockstate.getBlock();
/* 379 */     boolean flag = block.isNormalCube();
/* 380 */     boolean flag1 = worldIn.getBlockState(pos.up()).getBlock().isNormalCube();
/* 381 */     return (!flag1) && (flag) && (canConnectUpwardsTo(worldIn, blockpos.up()));
/*     */   }
/*     */   
/*     */   protected static boolean canConnectUpwardsTo(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/* 386 */     return canConnectUpwardsTo(worldIn.getBlockState(pos));
/*     */   }
/*     */   
/*     */   protected static boolean canConnectUpwardsTo(IBlockState state)
/*     */   {
/* 391 */     return canConnectTo(state, null);
/*     */   }
/*     */   
/*     */   protected static boolean canConnectTo(IBlockState blockState, EnumFacing side)
/*     */   {
/* 396 */     Block block = blockState.getBlock();
/*     */     
/* 398 */     if (block == Blocks.redstone_wire)
/*     */     {
/* 400 */       return true;
/*     */     }
/* 402 */     if (Blocks.unpowered_repeater.isAssociated(block))
/*     */     {
/* 404 */       EnumFacing enumfacing = (EnumFacing)blockState.getValue(BlockRedstoneRepeater.FACING);
/* 405 */       return (enumfacing == side) || (enumfacing.getOpposite() == side);
/*     */     }
/*     */     
/*     */ 
/* 409 */     return (block.canProvidePower()) && (side != null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canProvidePower()
/*     */   {
/* 418 */     return this.canProvidePower;
/*     */   }
/*     */   
/*     */   private int colorMultiplier(int powerLevel)
/*     */   {
/* 423 */     float f = powerLevel / 15.0F;
/* 424 */     float f1 = f * 0.6F + 0.4F;
/*     */     
/* 426 */     if (powerLevel == 0)
/*     */     {
/* 428 */       f1 = 0.3F;
/*     */     }
/*     */     
/* 431 */     float f2 = f * f * 0.7F - 0.5F;
/* 432 */     float f3 = f * f * 0.6F - 0.7F;
/*     */     
/* 434 */     if (f2 < 0.0F)
/*     */     {
/* 436 */       f2 = 0.0F;
/*     */     }
/*     */     
/* 439 */     if (f3 < 0.0F)
/*     */     {
/* 441 */       f3 = 0.0F;
/*     */     }
/*     */     
/* 444 */     int i = MathHelper.clamp_int((int)(f1 * 255.0F), 0, 255);
/* 445 */     int j = MathHelper.clamp_int((int)(f2 * 255.0F), 0, 255);
/* 446 */     int k = MathHelper.clamp_int((int)(f3 * 255.0F), 0, 255);
/* 447 */     return 0xFF000000 | i << 16 | j << 8 | k;
/*     */   }
/*     */   
/*     */   public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*     */   {
/* 452 */     int i = ((Integer)state.getValue(POWER)).intValue();
/*     */     
/* 454 */     if (i != 0)
/*     */     {
/* 456 */       double d0 = pos.getX() + 0.5D + (rand.nextFloat() - 0.5D) * 0.2D;
/* 457 */       double d1 = pos.getY() + 0.0625F;
/* 458 */       double d2 = pos.getZ() + 0.5D + (rand.nextFloat() - 0.5D) * 0.2D;
/* 459 */       float f = i / 15.0F;
/* 460 */       float f1 = f * 0.6F + 0.4F;
/* 461 */       float f2 = Math.max(0.0F, f * f * 0.7F - 0.5F);
/* 462 */       float f3 = Math.max(0.0F, f * f * 0.6F - 0.7F);
/* 463 */       worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0, d1, d2, f1, f2, f3, new int[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos)
/*     */   {
/* 469 */     return Items.redstone;
/*     */   }
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer()
/*     */   {
/* 474 */     return EnumWorldBlockLayer.CUTOUT;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 482 */     return getDefaultState().withProperty(POWER, Integer.valueOf(meta));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 490 */     return ((Integer)state.getValue(POWER)).intValue();
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 495 */     return new BlockState(this, new IProperty[] { NORTH, EAST, SOUTH, WEST, POWER });
/*     */   }
/*     */   
/*     */   static enum EnumAttachPosition implements IStringSerializable
/*     */   {
/* 500 */     UP("up"), 
/* 501 */     SIDE("side"), 
/* 502 */     NONE("none");
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private EnumAttachPosition(String name)
/*     */     {
/* 508 */       this.name = name;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 513 */       return getName();
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 518 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockRedstoneWire.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */