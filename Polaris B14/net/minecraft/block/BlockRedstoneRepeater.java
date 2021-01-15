/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.properties.PropertyInteger;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.StatCollector;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockRedstoneRepeater extends BlockRedstoneDiode
/*     */ {
/*  22 */   public static final PropertyBool LOCKED = PropertyBool.create("locked");
/*  23 */   public static final PropertyInteger DELAY = PropertyInteger.create("delay", 1, 4);
/*     */   
/*     */   protected BlockRedstoneRepeater(boolean powered)
/*     */   {
/*  27 */     super(powered);
/*  28 */     setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(DELAY, Integer.valueOf(1)).withProperty(LOCKED, Boolean.valueOf(false)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getLocalizedName()
/*     */   {
/*  36 */     return StatCollector.translateToLocal("item.diode.name");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
/*     */   {
/*  45 */     return state.withProperty(LOCKED, Boolean.valueOf(isLocked(worldIn, pos, state)));
/*     */   }
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
/*     */   {
/*  50 */     if (!playerIn.capabilities.allowEdit)
/*     */     {
/*  52 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  56 */     worldIn.setBlockState(pos, state.cycleProperty(DELAY), 3);
/*  57 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   protected int getDelay(IBlockState state)
/*     */   {
/*  63 */     return ((Integer)state.getValue(DELAY)).intValue() * 2;
/*     */   }
/*     */   
/*     */   protected IBlockState getPoweredState(IBlockState unpoweredState)
/*     */   {
/*  68 */     Integer integer = (Integer)unpoweredState.getValue(DELAY);
/*  69 */     Boolean obool = (Boolean)unpoweredState.getValue(LOCKED);
/*  70 */     EnumFacing enumfacing = (EnumFacing)unpoweredState.getValue(FACING);
/*  71 */     return Blocks.powered_repeater.getDefaultState().withProperty(FACING, enumfacing).withProperty(DELAY, integer).withProperty(LOCKED, obool);
/*     */   }
/*     */   
/*     */   protected IBlockState getUnpoweredState(IBlockState poweredState)
/*     */   {
/*  76 */     Integer integer = (Integer)poweredState.getValue(DELAY);
/*  77 */     Boolean obool = (Boolean)poweredState.getValue(LOCKED);
/*  78 */     EnumFacing enumfacing = (EnumFacing)poweredState.getValue(FACING);
/*  79 */     return Blocks.unpowered_repeater.getDefaultState().withProperty(FACING, enumfacing).withProperty(DELAY, integer).withProperty(LOCKED, obool);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*     */   {
/*  87 */     return Items.repeater;
/*     */   }
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos)
/*     */   {
/*  92 */     return Items.repeater;
/*     */   }
/*     */   
/*     */   public boolean isLocked(IBlockAccess worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  97 */     return getPowerOnSides(worldIn, pos, state) > 0;
/*     */   }
/*     */   
/*     */   protected boolean canPowerSide(Block blockIn)
/*     */   {
/* 102 */     return isRedstoneRepeaterBlockID(blockIn);
/*     */   }
/*     */   
/*     */   public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*     */   {
/* 107 */     if (this.isRepeaterPowered)
/*     */     {
/* 109 */       EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
/* 110 */       double d0 = pos.getX() + 0.5F + (rand.nextFloat() - 0.5F) * 0.2D;
/* 111 */       double d1 = pos.getY() + 0.4F + (rand.nextFloat() - 0.5F) * 0.2D;
/* 112 */       double d2 = pos.getZ() + 0.5F + (rand.nextFloat() - 0.5F) * 0.2D;
/* 113 */       float f = -5.0F;
/*     */       
/* 115 */       if (rand.nextBoolean())
/*     */       {
/* 117 */         f = ((Integer)state.getValue(DELAY)).intValue() * 2 - 1;
/*     */       }
/*     */       
/* 120 */       f /= 16.0F;
/* 121 */       double d3 = f * enumfacing.getFrontOffsetX();
/* 122 */       double d4 = f * enumfacing.getFrontOffsetZ();
/* 123 */       worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 129 */     super.breakBlock(worldIn, pos, state);
/* 130 */     notifyNeighbors(worldIn, pos, state);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 138 */     return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta)).withProperty(LOCKED, Boolean.valueOf(false)).withProperty(DELAY, Integer.valueOf(1 + (meta >> 2)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 146 */     int i = 0;
/* 147 */     i |= ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
/* 148 */     i |= ((Integer)state.getValue(DELAY)).intValue() - 1 << 2;
/* 149 */     return i;
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 154 */     return new BlockState(this, new net.minecraft.block.properties.IProperty[] { FACING, DELAY, LOCKED });
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockRedstoneRepeater.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */