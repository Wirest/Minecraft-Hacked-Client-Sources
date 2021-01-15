/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyDirection;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityPiston;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockPistonMoving extends BlockContainer
/*     */ {
/*  25 */   public static final PropertyDirection FACING = BlockPistonExtension.FACING;
/*  26 */   public static final PropertyEnum<BlockPistonExtension.EnumPistonType> TYPE = BlockPistonExtension.TYPE;
/*     */   
/*     */   public BlockPistonMoving()
/*     */   {
/*  30 */     super(Material.piston);
/*  31 */     setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(TYPE, BlockPistonExtension.EnumPistonType.DEFAULT));
/*  32 */     setHardness(-1.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TileEntity createNewTileEntity(World worldIn, int meta)
/*     */   {
/*  40 */     return null;
/*     */   }
/*     */   
/*     */   public static TileEntity newTileEntity(IBlockState state, EnumFacing facing, boolean extending, boolean renderHead)
/*     */   {
/*  45 */     return new TileEntityPiston(state, facing, extending, renderHead);
/*     */   }
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  50 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/*  52 */     if ((tileentity instanceof TileEntityPiston))
/*     */     {
/*  54 */       ((TileEntityPiston)tileentity).clearPistonTileEntity();
/*     */     }
/*     */     else
/*     */     {
/*  58 */       super.breakBlock(worldIn, pos, state);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
/*     */   {
/*  64 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
/*     */   {
/*  72 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  80 */     BlockPos blockpos = pos.offset(((EnumFacing)state.getValue(FACING)).getOpposite());
/*  81 */     IBlockState iblockstate = worldIn.getBlockState(blockpos);
/*     */     
/*  83 */     if (((iblockstate.getBlock() instanceof BlockPistonBase)) && (((Boolean)iblockstate.getValue(BlockPistonBase.EXTENDED)).booleanValue()))
/*     */     {
/*  85 */       worldIn.setBlockToAir(blockpos);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpaqueCube()
/*     */   {
/*  94 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isFullCube()
/*     */   {
/*  99 */     return false;
/*     */   }
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
/*     */   {
/* 104 */     if ((!worldIn.isRemote) && (worldIn.getTileEntity(pos) == null))
/*     */     {
/* 106 */       worldIn.setBlockToAir(pos);
/* 107 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 111 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*     */   {
/* 120 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
/*     */   {
/* 128 */     if (!worldIn.isRemote)
/*     */     {
/* 130 */       TileEntityPiston tileentitypiston = getTileEntity(worldIn, pos);
/*     */       
/* 132 */       if (tileentitypiston != null)
/*     */       {
/* 134 */         IBlockState iblockstate = tileentitypiston.getPistonState();
/* 135 */         iblockstate.getBlock().dropBlockAsItem(worldIn, pos, iblockstate, 0);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end)
/*     */   {
/* 145 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*     */   {
/* 153 */     if (!worldIn.isRemote)
/*     */     {
/* 155 */       worldIn.getTileEntity(pos);
/*     */     }
/*     */   }
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 161 */     TileEntityPiston tileentitypiston = getTileEntity(worldIn, pos);
/*     */     
/* 163 */     if (tileentitypiston == null)
/*     */     {
/* 165 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 169 */     float f = tileentitypiston.getProgress(0.0F);
/*     */     
/* 171 */     if (tileentitypiston.isExtending())
/*     */     {
/* 173 */       f = 1.0F - f;
/*     */     }
/*     */     
/* 176 */     return getBoundingBox(worldIn, pos, tileentitypiston.getPistonState(), f, tileentitypiston.getFacing());
/*     */   }
/*     */   
/*     */ 
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/* 182 */     TileEntityPiston tileentitypiston = getTileEntity(worldIn, pos);
/*     */     
/* 184 */     if (tileentitypiston != null)
/*     */     {
/* 186 */       IBlockState iblockstate = tileentitypiston.getPistonState();
/* 187 */       Block block = iblockstate.getBlock();
/*     */       
/* 189 */       if ((block == this) || (block.getMaterial() == Material.air))
/*     */       {
/* 191 */         return;
/*     */       }
/*     */       
/* 194 */       float f = tileentitypiston.getProgress(0.0F);
/*     */       
/* 196 */       if (tileentitypiston.isExtending())
/*     */       {
/* 198 */         f = 1.0F - f;
/*     */       }
/*     */       
/* 201 */       block.setBlockBoundsBasedOnState(worldIn, pos);
/*     */       
/* 203 */       if ((block == Blocks.piston) || (block == Blocks.sticky_piston))
/*     */       {
/* 205 */         f = 0.0F;
/*     */       }
/*     */       
/* 208 */       EnumFacing enumfacing = tileentitypiston.getFacing();
/* 209 */       this.minX = (block.getBlockBoundsMinX() - enumfacing.getFrontOffsetX() * f);
/* 210 */       this.minY = (block.getBlockBoundsMinY() - enumfacing.getFrontOffsetY() * f);
/* 211 */       this.minZ = (block.getBlockBoundsMinZ() - enumfacing.getFrontOffsetZ() * f);
/* 212 */       this.maxX = (block.getBlockBoundsMaxX() - enumfacing.getFrontOffsetX() * f);
/* 213 */       this.maxY = (block.getBlockBoundsMaxY() - enumfacing.getFrontOffsetY() * f);
/* 214 */       this.maxZ = (block.getBlockBoundsMaxZ() - enumfacing.getFrontOffsetZ() * f);
/*     */     }
/*     */   }
/*     */   
/*     */   public AxisAlignedBB getBoundingBox(World worldIn, BlockPos pos, IBlockState extendingBlock, float progress, EnumFacing direction)
/*     */   {
/* 220 */     if ((extendingBlock.getBlock() != this) && (extendingBlock.getBlock().getMaterial() != Material.air))
/*     */     {
/* 222 */       AxisAlignedBB axisalignedbb = extendingBlock.getBlock().getCollisionBoundingBox(worldIn, pos, extendingBlock);
/*     */       
/* 224 */       if (axisalignedbb == null)
/*     */       {
/* 226 */         return null;
/*     */       }
/*     */       
/*     */ 
/* 230 */       double d0 = axisalignedbb.minX;
/* 231 */       double d1 = axisalignedbb.minY;
/* 232 */       double d2 = axisalignedbb.minZ;
/* 233 */       double d3 = axisalignedbb.maxX;
/* 234 */       double d4 = axisalignedbb.maxY;
/* 235 */       double d5 = axisalignedbb.maxZ;
/*     */       
/* 237 */       if (direction.getFrontOffsetX() < 0)
/*     */       {
/* 239 */         d0 -= direction.getFrontOffsetX() * progress;
/*     */       }
/*     */       else
/*     */       {
/* 243 */         d3 -= direction.getFrontOffsetX() * progress;
/*     */       }
/*     */       
/* 246 */       if (direction.getFrontOffsetY() < 0)
/*     */       {
/* 248 */         d1 -= direction.getFrontOffsetY() * progress;
/*     */       }
/*     */       else
/*     */       {
/* 252 */         d4 -= direction.getFrontOffsetY() * progress;
/*     */       }
/*     */       
/* 255 */       if (direction.getFrontOffsetZ() < 0)
/*     */       {
/* 257 */         d2 -= direction.getFrontOffsetZ() * progress;
/*     */       }
/*     */       else
/*     */       {
/* 261 */         d5 -= direction.getFrontOffsetZ() * progress;
/*     */       }
/*     */       
/* 264 */       return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 269 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   private TileEntityPiston getTileEntity(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/* 275 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/* 276 */     return (tileentity instanceof TileEntityPiston) ? (TileEntityPiston)tileentity : null;
/*     */   }
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos)
/*     */   {
/* 281 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 289 */     return getDefaultState().withProperty(FACING, BlockPistonExtension.getFacing(meta)).withProperty(TYPE, (meta & 0x8) > 0 ? BlockPistonExtension.EnumPistonType.STICKY : BlockPistonExtension.EnumPistonType.DEFAULT);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 297 */     int i = 0;
/* 298 */     i |= ((EnumFacing)state.getValue(FACING)).getIndex();
/*     */     
/* 300 */     if (state.getValue(TYPE) == BlockPistonExtension.EnumPistonType.STICKY)
/*     */     {
/* 302 */       i |= 0x8;
/*     */     }
/*     */     
/* 305 */     return i;
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 310 */     return new BlockState(this, new IProperty[] { FACING, TYPE });
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockPistonMoving.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */