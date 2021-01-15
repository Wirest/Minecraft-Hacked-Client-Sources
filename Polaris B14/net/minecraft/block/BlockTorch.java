/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyDirection;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumFacing.Axis;
/*     */ import net.minecraft.util.EnumFacing.Plane;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockTorch extends Block
/*     */ {
/*  24 */   public static final PropertyDirection FACING = PropertyDirection.create("facing", new Predicate()
/*     */   {
/*     */     public boolean apply(EnumFacing p_apply_1_)
/*     */     {
/*  28 */       return p_apply_1_ != EnumFacing.DOWN;
/*     */     }
/*  24 */   });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BlockTorch()
/*     */   {
/*  34 */     super(Material.circuits);
/*  35 */     setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
/*  36 */     setTickRandomly(true);
/*  37 */     setCreativeTab(net.minecraft.creativetab.CreativeTabs.tabDecorations);
/*     */   }
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  42 */     return null;
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
/*     */   private boolean canPlaceOn(World worldIn, BlockPos pos)
/*     */   {
/*  60 */     if (World.doesBlockHaveSolidTopSurface(worldIn, pos))
/*     */     {
/*  62 */       return true;
/*     */     }
/*     */     
/*     */ 
/*  66 */     Block block = worldIn.getBlockState(pos).getBlock();
/*  67 */     return ((block instanceof BlockFence)) || (block == Blocks.glass) || (block == Blocks.cobblestone_wall) || (block == Blocks.stained_glass);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
/*     */   {
/*  73 */     for (EnumFacing enumfacing : FACING.getAllowedValues())
/*     */     {
/*  75 */       if (canPlaceAt(worldIn, pos, enumfacing))
/*     */       {
/*  77 */         return true;
/*     */       }
/*     */     }
/*     */     
/*  81 */     return false;
/*     */   }
/*     */   
/*     */   private boolean canPlaceAt(World worldIn, BlockPos pos, EnumFacing facing)
/*     */   {
/*  86 */     BlockPos blockpos = pos.offset(facing.getOpposite());
/*  87 */     boolean flag = facing.getAxis().isHorizontal();
/*  88 */     return ((flag) && (worldIn.isBlockNormalCube(blockpos, true))) || ((facing.equals(EnumFacing.UP)) && (canPlaceOn(worldIn, blockpos)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
/*     */   {
/*  97 */     if (canPlaceAt(worldIn, pos, facing))
/*     */     {
/*  99 */       return getDefaultState().withProperty(FACING, facing);
/*     */     }
/*     */     
/*     */ 
/* 103 */     for (Object enumfacing0 : EnumFacing.Plane.HORIZONTAL)
/*     */     {
/* 105 */       EnumFacing enumfacing = (EnumFacing)enumfacing0;
/*     */       
/* 107 */       if (worldIn.isBlockNormalCube(pos.offset(enumfacing.getOpposite()), true))
/*     */       {
/* 109 */         return getDefaultState().withProperty(FACING, enumfacing);
/*     */       }
/*     */     }
/*     */     
/* 113 */     return getDefaultState();
/*     */   }
/*     */   
/*     */ 
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 119 */     checkForDrop(worldIn, pos, state);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*     */   {
/* 127 */     onNeighborChangeInternal(worldIn, pos, state);
/*     */   }
/*     */   
/*     */   protected boolean onNeighborChangeInternal(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 132 */     if (!checkForDrop(worldIn, pos, state))
/*     */     {
/* 134 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 138 */     EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
/* 139 */     EnumFacing.Axis enumfacing$axis = enumfacing.getAxis();
/* 140 */     EnumFacing enumfacing1 = enumfacing.getOpposite();
/* 141 */     boolean flag = false;
/*     */     
/* 143 */     if ((enumfacing$axis.isHorizontal()) && (!worldIn.isBlockNormalCube(pos.offset(enumfacing1), true)))
/*     */     {
/* 145 */       flag = true;
/*     */     }
/* 147 */     else if ((enumfacing$axis.isVertical()) && (!canPlaceOn(worldIn, pos.offset(enumfacing1))))
/*     */     {
/* 149 */       flag = true;
/*     */     }
/*     */     
/* 152 */     if (flag)
/*     */     {
/* 154 */       dropBlockAsItem(worldIn, pos, state, 0);
/* 155 */       worldIn.setBlockToAir(pos);
/* 156 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 160 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 167 */     if ((state.getBlock() == this) && (canPlaceAt(worldIn, pos, (EnumFacing)state.getValue(FACING))))
/*     */     {
/* 169 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 173 */     if (worldIn.getBlockState(pos).getBlock() == this)
/*     */     {
/* 175 */       dropBlockAsItem(worldIn, pos, state, 0);
/* 176 */       worldIn.setBlockToAir(pos);
/*     */     }
/*     */     
/* 179 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public net.minecraft.util.MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end)
/*     */   {
/* 188 */     EnumFacing enumfacing = (EnumFacing)worldIn.getBlockState(pos).getValue(FACING);
/* 189 */     float f = 0.15F;
/*     */     
/* 191 */     if (enumfacing == EnumFacing.EAST)
/*     */     {
/* 193 */       setBlockBounds(0.0F, 0.2F, 0.5F - f, f * 2.0F, 0.8F, 0.5F + f);
/*     */     }
/* 195 */     else if (enumfacing == EnumFacing.WEST)
/*     */     {
/* 197 */       setBlockBounds(1.0F - f * 2.0F, 0.2F, 0.5F - f, 1.0F, 0.8F, 0.5F + f);
/*     */     }
/* 199 */     else if (enumfacing == EnumFacing.SOUTH)
/*     */     {
/* 201 */       setBlockBounds(0.5F - f, 0.2F, 0.0F, 0.5F + f, 0.8F, f * 2.0F);
/*     */     }
/* 203 */     else if (enumfacing == EnumFacing.NORTH)
/*     */     {
/* 205 */       setBlockBounds(0.5F - f, 0.2F, 1.0F - f * 2.0F, 0.5F + f, 0.8F, 1.0F);
/*     */     }
/*     */     else
/*     */     {
/* 209 */       f = 0.1F;
/* 210 */       setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, 0.6F, 0.5F + f);
/*     */     }
/*     */     
/* 213 */     return super.collisionRayTrace(worldIn, pos, start, end);
/*     */   }
/*     */   
/*     */   public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*     */   {
/* 218 */     EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
/* 219 */     double d0 = pos.getX() + 0.5D;
/* 220 */     double d1 = pos.getY() + 0.7D;
/* 221 */     double d2 = pos.getZ() + 0.5D;
/* 222 */     double d3 = 0.22D;
/* 223 */     double d4 = 0.27D;
/*     */     
/* 225 */     if (enumfacing.getAxis().isHorizontal())
/*     */     {
/* 227 */       EnumFacing enumfacing1 = enumfacing.getOpposite();
/* 228 */       worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4 * enumfacing1.getFrontOffsetX(), d1 + d3, d2 + d4 * enumfacing1.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D, new int[0]);
/* 229 */       worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4 * enumfacing1.getFrontOffsetX(), d1 + d3, d2 + d4 * enumfacing1.getFrontOffsetZ(), 0.0D, 0.0D, 0.0D, new int[0]);
/*     */     }
/*     */     else
/*     */     {
/* 233 */       worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
/* 234 */       worldIn.spawnParticle(EnumParticleTypes.FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer()
/*     */   {
/* 240 */     return EnumWorldBlockLayer.CUTOUT;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 248 */     IBlockState iblockstate = getDefaultState();
/*     */     
/* 250 */     switch (meta)
/*     */     {
/*     */     case 1: 
/* 253 */       iblockstate = iblockstate.withProperty(FACING, EnumFacing.EAST);
/* 254 */       break;
/*     */     
/*     */     case 2: 
/* 257 */       iblockstate = iblockstate.withProperty(FACING, EnumFacing.WEST);
/* 258 */       break;
/*     */     
/*     */     case 3: 
/* 261 */       iblockstate = iblockstate.withProperty(FACING, EnumFacing.SOUTH);
/* 262 */       break;
/*     */     
/*     */     case 4: 
/* 265 */       iblockstate = iblockstate.withProperty(FACING, EnumFacing.NORTH);
/* 266 */       break;
/*     */     
/*     */     case 5: 
/*     */     default: 
/* 270 */       iblockstate = iblockstate.withProperty(FACING, EnumFacing.UP);
/*     */     }
/*     */     
/* 273 */     return iblockstate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 281 */     int i = 0;
/*     */     
/* 283 */     switch ((EnumFacing)state.getValue(FACING))
/*     */     {
/*     */     case WEST: 
/* 286 */       i |= 0x1;
/* 287 */       break;
/*     */     
/*     */     case UP: 
/* 290 */       i |= 0x2;
/* 291 */       break;
/*     */     
/*     */     case SOUTH: 
/* 294 */       i |= 0x3;
/* 295 */       break;
/*     */     
/*     */     case NORTH: 
/* 298 */       i |= 0x4;
/* 299 */       break;
/*     */     
/*     */     case DOWN: 
/*     */     case EAST: 
/*     */     default: 
/* 304 */       i |= 0x5;
/*     */     }
/*     */     
/* 307 */     return i;
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 312 */     return new BlockState(this, new IProperty[] { FACING });
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockTorch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */