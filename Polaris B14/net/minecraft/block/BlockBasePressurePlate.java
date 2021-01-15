/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public abstract class BlockBasePressurePlate extends Block
/*     */ {
/*     */   protected BlockBasePressurePlate(Material materialIn)
/*     */   {
/*  19 */     this(materialIn, materialIn.getMaterialMapColor());
/*     */   }
/*     */   
/*     */   protected BlockBasePressurePlate(Material p_i46401_1_, MapColor p_i46401_2_)
/*     */   {
/*  24 */     super(p_i46401_1_, p_i46401_2_);
/*  25 */     setCreativeTab(CreativeTabs.tabRedstone);
/*  26 */     setTickRandomly(true);
/*     */   }
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/*  31 */     setBlockBoundsBasedOnState0(worldIn.getBlockState(pos));
/*     */   }
/*     */   
/*     */   protected void setBlockBoundsBasedOnState0(IBlockState state)
/*     */   {
/*  36 */     boolean flag = getRedstoneStrength(state) > 0;
/*  37 */     float f = 0.0625F;
/*     */     
/*  39 */     if (flag)
/*     */     {
/*  41 */       setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.03125F, 0.9375F);
/*     */     }
/*     */     else
/*     */     {
/*  45 */       setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.0625F, 0.9375F);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int tickRate(World worldIn)
/*     */   {
/*  54 */     return 20;
/*     */   }
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  59 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpaqueCube()
/*     */   {
/*  67 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isFullCube()
/*     */   {
/*  72 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/*  77 */     return true;
/*     */   }
/*     */   
/*     */   public boolean func_181623_g()
/*     */   {
/*  82 */     return true;
/*     */   }
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
/*     */   {
/*  87 */     return canBePlacedOn(worldIn, pos.down());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*     */   {
/*  95 */     if (!canBePlacedOn(worldIn, pos.down()))
/*     */     {
/*  97 */       dropBlockAsItem(worldIn, pos, state, 0);
/*  98 */       worldIn.setBlockToAir(pos);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean canBePlacedOn(World worldIn, BlockPos pos)
/*     */   {
/* 104 */     return (World.doesBlockHaveSolidTopSurface(worldIn, pos)) || ((worldIn.getBlockState(pos).getBlock() instanceof BlockFence));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*     */   {
/* 116 */     if (!worldIn.isRemote)
/*     */     {
/* 118 */       int i = getRedstoneStrength(state);
/*     */       
/* 120 */       if (i > 0)
/*     */       {
/* 122 */         updateState(worldIn, pos, state, i);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
/*     */   {
/* 132 */     if (!worldIn.isRemote)
/*     */     {
/* 134 */       int i = getRedstoneStrength(state);
/*     */       
/* 136 */       if (i == 0)
/*     */       {
/* 138 */         updateState(worldIn, pos, state, i);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void updateState(World worldIn, BlockPos pos, IBlockState state, int oldRedstoneStrength)
/*     */   {
/* 148 */     int i = computeRedstoneStrength(worldIn, pos);
/* 149 */     boolean flag = oldRedstoneStrength > 0;
/* 150 */     boolean flag1 = i > 0;
/*     */     
/* 152 */     if (oldRedstoneStrength != i)
/*     */     {
/* 154 */       state = setRedstoneStrength(state, i);
/* 155 */       worldIn.setBlockState(pos, state, 2);
/* 156 */       updateNeighbors(worldIn, pos);
/* 157 */       worldIn.markBlockRangeForRenderUpdate(pos, pos);
/*     */     }
/*     */     
/* 160 */     if ((!flag1) && (flag))
/*     */     {
/* 162 */       worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D, "random.click", 0.3F, 0.5F);
/*     */     }
/* 164 */     else if ((flag1) && (!flag))
/*     */     {
/* 166 */       worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.1D, pos.getZ() + 0.5D, "random.click", 0.3F, 0.6F);
/*     */     }
/*     */     
/* 169 */     if (flag1)
/*     */     {
/* 171 */       worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AxisAlignedBB getSensitiveAABB(BlockPos pos)
/*     */   {
/* 180 */     float f = 0.125F;
/* 181 */     return new AxisAlignedBB(pos.getX() + 0.125F, pos.getY(), pos.getZ() + 0.125F, pos.getX() + 1 - 0.125F, pos.getY() + 0.25D, pos.getZ() + 1 - 0.125F);
/*     */   }
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 186 */     if (getRedstoneStrength(state) > 0)
/*     */     {
/* 188 */       updateNeighbors(worldIn, pos);
/*     */     }
/*     */     
/* 191 */     super.breakBlock(worldIn, pos, state);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void updateNeighbors(World worldIn, BlockPos pos)
/*     */   {
/* 199 */     worldIn.notifyNeighborsOfStateChange(pos, this);
/* 200 */     worldIn.notifyNeighborsOfStateChange(pos.down(), this);
/*     */   }
/*     */   
/*     */   public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
/*     */   {
/* 205 */     return getRedstoneStrength(state);
/*     */   }
/*     */   
/*     */   public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
/*     */   {
/* 210 */     return side == EnumFacing.UP ? getRedstoneStrength(state) : 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canProvidePower()
/*     */   {
/* 218 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBlockBoundsForItemRender()
/*     */   {
/* 226 */     float f = 0.5F;
/* 227 */     float f1 = 0.125F;
/* 228 */     float f2 = 0.5F;
/* 229 */     setBlockBounds(0.0F, 0.375F, 0.0F, 1.0F, 0.625F, 1.0F);
/*     */   }
/*     */   
/*     */   public int getMobilityFlag()
/*     */   {
/* 234 */     return 1;
/*     */   }
/*     */   
/*     */   protected abstract int computeRedstoneStrength(World paramWorld, BlockPos paramBlockPos);
/*     */   
/*     */   protected abstract int getRedstoneStrength(IBlockState paramIBlockState);
/*     */   
/*     */   protected abstract IBlockState setRedstoneStrength(IBlockState paramIBlockState, int paramInt);
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockBasePressurePlate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */