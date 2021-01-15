/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyInteger;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumFacing.Axis;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockCocoa extends BlockDirectional implements IGrowable
/*     */ {
/*  24 */   public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 2);
/*     */   
/*     */   public BlockCocoa()
/*     */   {
/*  28 */     super(net.minecraft.block.material.Material.plants);
/*  29 */     setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(AGE, Integer.valueOf(0)));
/*  30 */     setTickRandomly(true);
/*     */   }
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*     */   {
/*  35 */     if (!canBlockStay(worldIn, pos, state))
/*     */     {
/*  37 */       dropBlock(worldIn, pos, state);
/*     */     }
/*  39 */     else if (worldIn.rand.nextInt(5) == 0)
/*     */     {
/*  41 */       int i = ((Integer)state.getValue(AGE)).intValue();
/*     */       
/*  43 */       if (i < 2)
/*     */       {
/*  45 */         worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(i + 1)), 2);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  52 */     pos = pos.offset((EnumFacing)state.getValue(FACING));
/*  53 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*  54 */     return (iblockstate.getBlock() == Blocks.log) && (iblockstate.getValue(BlockPlanks.VARIANT) == BlockPlanks.EnumType.JUNGLE);
/*     */   }
/*     */   
/*     */   public boolean isFullCube()
/*     */   {
/*  59 */     return false;
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
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  72 */     setBlockBoundsBasedOnState(worldIn, pos);
/*  73 */     return super.getCollisionBoundingBox(worldIn, pos, state);
/*     */   }
/*     */   
/*     */   public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos)
/*     */   {
/*  78 */     setBlockBoundsBasedOnState(worldIn, pos);
/*  79 */     return super.getSelectedBoundingBox(worldIn, pos);
/*     */   }
/*     */   
/*     */ 
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/*  85 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*  86 */     EnumFacing enumfacing = (EnumFacing)iblockstate.getValue(FACING);
/*  87 */     int i = ((Integer)iblockstate.getValue(AGE)).intValue();
/*  88 */     int j = 4 + i * 2;
/*  89 */     int k = 5 + i * 2;
/*  90 */     float f = j / 2.0F;
/*     */     
/*  92 */     switch (enumfacing)
/*     */     {
/*     */     case SOUTH: 
/*  95 */       setBlockBounds((8.0F - f) / 16.0F, (12.0F - k) / 16.0F, (15.0F - j) / 16.0F, (8.0F + f) / 16.0F, 0.75F, 0.9375F);
/*  96 */       break;
/*     */     
/*     */     case NORTH: 
/*  99 */       setBlockBounds((8.0F - f) / 16.0F, (12.0F - k) / 16.0F, 0.0625F, (8.0F + f) / 16.0F, 0.75F, (1.0F + j) / 16.0F);
/* 100 */       break;
/*     */     
/*     */     case UP: 
/* 103 */       setBlockBounds(0.0625F, (12.0F - k) / 16.0F, (8.0F - f) / 16.0F, (1.0F + j) / 16.0F, 0.75F, (8.0F + f) / 16.0F);
/* 104 */       break;
/*     */     
/*     */     case WEST: 
/* 107 */       setBlockBounds((15.0F - j) / 16.0F, (12.0F - k) / 16.0F, (8.0F - f) / 16.0F, 0.9375F, 0.75F, (8.0F + f) / 16.0F);
/*     */     }
/*     */     
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
/*     */   {
/* 116 */     EnumFacing enumfacing = EnumFacing.fromAngle(placer.rotationYaw);
/* 117 */     worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
/*     */   {
/* 126 */     if (!facing.getAxis().isHorizontal())
/*     */     {
/* 128 */       facing = EnumFacing.NORTH;
/*     */     }
/*     */     
/* 131 */     return getDefaultState().withProperty(FACING, facing.getOpposite()).withProperty(AGE, Integer.valueOf(0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*     */   {
/* 139 */     if (!canBlockStay(worldIn, pos, state))
/*     */     {
/* 141 */       dropBlock(worldIn, pos, state);
/*     */     }
/*     */   }
/*     */   
/*     */   private void dropBlock(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 147 */     worldIn.setBlockState(pos, Blocks.air.getDefaultState(), 3);
/* 148 */     dropBlockAsItem(worldIn, pos, state, 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
/*     */   {
/* 156 */     int i = ((Integer)state.getValue(AGE)).intValue();
/* 157 */     int j = 1;
/*     */     
/* 159 */     if (i >= 2)
/*     */     {
/* 161 */       j = 3;
/*     */     }
/*     */     
/* 164 */     for (int k = 0; k < j; k++)
/*     */     {
/* 166 */       spawnAsEntity(worldIn, pos, new ItemStack(Items.dye, 1, EnumDyeColor.BROWN.getDyeDamage()));
/*     */     }
/*     */   }
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos)
/*     */   {
/* 172 */     return Items.dye;
/*     */   }
/*     */   
/*     */   public int getDamageValue(World worldIn, BlockPos pos)
/*     */   {
/* 177 */     return EnumDyeColor.BROWN.getDyeDamage();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
/*     */   {
/* 185 */     return ((Integer)state.getValue(AGE)).intValue() < 2;
/*     */   }
/*     */   
/*     */   public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
/*     */   {
/* 190 */     return true;
/*     */   }
/*     */   
/*     */   public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
/*     */   {
/* 195 */     worldIn.setBlockState(pos, state.withProperty(AGE, Integer.valueOf(((Integer)state.getValue(AGE)).intValue() + 1)), 2);
/*     */   }
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer()
/*     */   {
/* 200 */     return EnumWorldBlockLayer.CUTOUT;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 208 */     return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta)).withProperty(AGE, Integer.valueOf((meta & 0xF) >> 2));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 216 */     int i = 0;
/* 217 */     i |= ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
/* 218 */     i |= ((Integer)state.getValue(AGE)).intValue() << 2;
/* 219 */     return i;
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 224 */     return new BlockState(this, new IProperty[] { FACING, AGE });
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockCocoa.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */