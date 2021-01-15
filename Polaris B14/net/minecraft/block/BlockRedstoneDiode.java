/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumFacing.Axis;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public abstract class BlockRedstoneDiode extends BlockDirectional
/*     */ {
/*     */   protected final boolean isRepeaterPowered;
/*     */   
/*     */   protected BlockRedstoneDiode(boolean powered)
/*     */   {
/*  22 */     super(Material.circuits);
/*  23 */     this.isRepeaterPowered = powered;
/*  24 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.125F, 1.0F);
/*     */   }
/*     */   
/*     */   public boolean isFullCube()
/*     */   {
/*  29 */     return false;
/*     */   }
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
/*     */   {
/*  34 */     return World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) ? super.canPlaceBlockAt(worldIn, pos) : false;
/*     */   }
/*     */   
/*     */   public boolean canBlockStay(World worldIn, BlockPos pos)
/*     */   {
/*  39 */     return World.doesBlockHaveSolidTopSurface(worldIn, pos.down());
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
/*  51 */     if (!isLocked(worldIn, pos, state))
/*     */     {
/*  53 */       boolean flag = shouldBePowered(worldIn, pos, state);
/*     */       
/*  55 */       if ((this.isRepeaterPowered) && (!flag))
/*     */       {
/*  57 */         worldIn.setBlockState(pos, getUnpoweredState(state), 2);
/*     */       }
/*  59 */       else if (!this.isRepeaterPowered)
/*     */       {
/*  61 */         worldIn.setBlockState(pos, getPoweredState(state), 2);
/*     */         
/*  63 */         if (!flag)
/*     */         {
/*  65 */           worldIn.updateBlockTick(pos, getPoweredState(state).getBlock(), getTickDelay(state), -1);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
/*     */   {
/*  73 */     return side.getAxis() != EnumFacing.Axis.Y;
/*     */   }
/*     */   
/*     */   protected boolean isPowered(IBlockState state)
/*     */   {
/*  78 */     return this.isRepeaterPowered;
/*     */   }
/*     */   
/*     */   public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
/*     */   {
/*  83 */     return getWeakPower(worldIn, pos, state, side);
/*     */   }
/*     */   
/*     */   public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
/*     */   {
/*  88 */     return state.getValue(FACING) == side ? getActiveSignal(worldIn, pos, state) : !isPowered(state) ? 0 : 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*     */   {
/*  96 */     if (canBlockStay(worldIn, pos))
/*     */     {
/*  98 */       updateState(worldIn, pos, state);
/*     */     }
/*     */     else
/*     */     {
/* 102 */       dropBlockAsItem(worldIn, pos, state, 0);
/* 103 */       worldIn.setBlockToAir(pos);
/*     */       EnumFacing[] arrayOfEnumFacing;
/* 105 */       int j = (arrayOfEnumFacing = EnumFacing.values()).length; for (int i = 0; i < j; i++) { EnumFacing enumfacing = arrayOfEnumFacing[i];
/*     */         
/* 107 */         worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void updateState(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 114 */     if (!isLocked(worldIn, pos, state))
/*     */     {
/* 116 */       boolean flag = shouldBePowered(worldIn, pos, state);
/*     */       
/* 118 */       if (((this.isRepeaterPowered) && (!flag)) || ((!this.isRepeaterPowered) && (flag) && (!worldIn.isBlockTickPending(pos, this))))
/*     */       {
/* 120 */         int i = -1;
/*     */         
/* 122 */         if (isFacingTowardsRepeater(worldIn, pos, state))
/*     */         {
/* 124 */           i = -3;
/*     */         }
/* 126 */         else if (this.isRepeaterPowered)
/*     */         {
/* 128 */           i = -2;
/*     */         }
/*     */         
/* 131 */         worldIn.updateBlockTick(pos, this, getDelay(state), i);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isLocked(IBlockAccess worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 138 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean shouldBePowered(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 143 */     return calculateInputStrength(worldIn, pos, state) > 0;
/*     */   }
/*     */   
/*     */   protected int calculateInputStrength(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 148 */     EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
/* 149 */     BlockPos blockpos = pos.offset(enumfacing);
/* 150 */     int i = worldIn.getRedstonePower(blockpos, enumfacing);
/*     */     
/* 152 */     if (i >= 15)
/*     */     {
/* 154 */       return i;
/*     */     }
/*     */     
/*     */ 
/* 158 */     IBlockState iblockstate = worldIn.getBlockState(blockpos);
/* 159 */     return Math.max(i, iblockstate.getBlock() == Blocks.redstone_wire ? ((Integer)iblockstate.getValue(BlockRedstoneWire.POWER)).intValue() : 0);
/*     */   }
/*     */   
/*     */ 
/*     */   protected int getPowerOnSides(IBlockAccess worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 165 */     EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
/* 166 */     EnumFacing enumfacing1 = enumfacing.rotateY();
/* 167 */     EnumFacing enumfacing2 = enumfacing.rotateYCCW();
/* 168 */     return Math.max(getPowerOnSide(worldIn, pos.offset(enumfacing1), enumfacing1), getPowerOnSide(worldIn, pos.offset(enumfacing2), enumfacing2));
/*     */   }
/*     */   
/*     */   protected int getPowerOnSide(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
/*     */   {
/* 173 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/* 174 */     Block block = iblockstate.getBlock();
/* 175 */     return canPowerSide(block) ? worldIn.getStrongPower(pos, side) : block == Blocks.redstone_wire ? ((Integer)iblockstate.getValue(BlockRedstoneWire.POWER)).intValue() : 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canProvidePower()
/*     */   {
/* 183 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
/*     */   {
/* 192 */     return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
/*     */   {
/* 200 */     if (shouldBePowered(worldIn, pos, state))
/*     */     {
/* 202 */       worldIn.scheduleUpdate(pos, this, 1);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 208 */     notifyNeighbors(worldIn, pos, state);
/*     */   }
/*     */   
/*     */   protected void notifyNeighbors(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 213 */     EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
/* 214 */     BlockPos blockpos = pos.offset(enumfacing.getOpposite());
/* 215 */     worldIn.notifyBlockOfStateChange(blockpos, this);
/* 216 */     worldIn.notifyNeighborsOfStateExcept(blockpos, this, enumfacing);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 224 */     if (this.isRepeaterPowered) {
/*     */       EnumFacing[] arrayOfEnumFacing;
/* 226 */       int j = (arrayOfEnumFacing = EnumFacing.values()).length; for (int i = 0; i < j; i++) { EnumFacing enumfacing = arrayOfEnumFacing[i];
/*     */         
/* 228 */         worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
/*     */       }
/*     */     }
/*     */     
/* 232 */     super.onBlockDestroyedByPlayer(worldIn, pos, state);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpaqueCube()
/*     */   {
/* 240 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean canPowerSide(Block blockIn)
/*     */   {
/* 245 */     return blockIn.canProvidePower();
/*     */   }
/*     */   
/*     */   protected int getActiveSignal(IBlockAccess worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 250 */     return 15;
/*     */   }
/*     */   
/*     */   public static boolean isRedstoneRepeaterBlockID(Block blockIn)
/*     */   {
/* 255 */     return (Blocks.unpowered_repeater.isAssociated(blockIn)) || (Blocks.unpowered_comparator.isAssociated(blockIn));
/*     */   }
/*     */   
/*     */   public boolean isAssociated(Block other)
/*     */   {
/* 260 */     return (other == getPoweredState(getDefaultState()).getBlock()) || (other == getUnpoweredState(getDefaultState()).getBlock());
/*     */   }
/*     */   
/*     */   public boolean isFacingTowardsRepeater(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 265 */     EnumFacing enumfacing = ((EnumFacing)state.getValue(FACING)).getOpposite();
/* 266 */     BlockPos blockpos = pos.offset(enumfacing);
/* 267 */     return worldIn.getBlockState(blockpos).getValue(FACING) != enumfacing;
/*     */   }
/*     */   
/*     */   protected int getTickDelay(IBlockState state)
/*     */   {
/* 272 */     return getDelay(state);
/*     */   }
/*     */   
/*     */   protected abstract int getDelay(IBlockState paramIBlockState);
/*     */   
/*     */   protected abstract IBlockState getPoweredState(IBlockState paramIBlockState);
/*     */   
/*     */   protected abstract IBlockState getUnpoweredState(IBlockState paramIBlockState);
/*     */   
/*     */   public boolean isAssociatedBlock(Block other)
/*     */   {
/* 283 */     return isAssociated(other);
/*     */   }
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer()
/*     */   {
/* 288 */     return EnumWorldBlockLayer.CUTOUT;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockRedstoneDiode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */