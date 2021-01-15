/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyInteger;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.BlockPos.MutableBlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockFarmland extends Block
/*     */ {
/*  22 */   public static final PropertyInteger MOISTURE = PropertyInteger.create("moisture", 0, 7);
/*     */   
/*     */   protected BlockFarmland()
/*     */   {
/*  26 */     super(Material.ground);
/*  27 */     setDefaultState(this.blockState.getBaseState().withProperty(MOISTURE, Integer.valueOf(0)));
/*  28 */     setTickRandomly(true);
/*  29 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);
/*  30 */     setLightOpacity(255);
/*     */   }
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  35 */     return new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpaqueCube()
/*     */   {
/*  43 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isFullCube()
/*     */   {
/*  48 */     return false;
/*     */   }
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*     */   {
/*  53 */     int i = ((Integer)state.getValue(MOISTURE)).intValue();
/*     */     
/*  55 */     if ((!hasWater(worldIn, pos)) && (!worldIn.canLightningStrike(pos.up())))
/*     */     {
/*  57 */       if (i > 0)
/*     */       {
/*  59 */         worldIn.setBlockState(pos, state.withProperty(MOISTURE, Integer.valueOf(i - 1)), 2);
/*     */       }
/*  61 */       else if (!hasCrops(worldIn, pos))
/*     */       {
/*  63 */         worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
/*     */       }
/*     */     }
/*  66 */     else if (i < 7)
/*     */     {
/*  68 */       worldIn.setBlockState(pos, state.withProperty(MOISTURE, Integer.valueOf(7)), 2);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
/*     */   {
/*  77 */     if ((entityIn instanceof net.minecraft.entity.EntityLivingBase))
/*     */     {
/*  79 */       if ((!worldIn.isRemote) && (worldIn.rand.nextFloat() < fallDistance - 0.5F))
/*     */       {
/*  81 */         if ((!(entityIn instanceof EntityPlayer)) && (!worldIn.getGameRules().getBoolean("mobGriefing")))
/*     */         {
/*  83 */           return;
/*     */         }
/*     */         
/*  86 */         worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
/*     */       }
/*     */       
/*  89 */       super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean hasCrops(World worldIn, BlockPos pos)
/*     */   {
/*  95 */     Block block = worldIn.getBlockState(pos.up()).getBlock();
/*  96 */     return ((block instanceof BlockCrops)) || ((block instanceof BlockStem));
/*     */   }
/*     */   
/*     */   private boolean hasWater(World worldIn, BlockPos pos)
/*     */   {
/* 101 */     for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(pos.add(-4, 0, -4), pos.add(4, 1, 4)))
/*     */     {
/* 103 */       if (worldIn.getBlockState(blockpos$mutableblockpos).getBlock().getMaterial() == Material.water)
/*     */       {
/* 105 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 109 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*     */   {
/* 117 */     super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
/*     */     
/* 119 */     if (worldIn.getBlockState(pos.up()).getBlock().getMaterial().isSolid())
/*     */     {
/* 121 */       worldIn.setBlockState(pos, Blocks.dirt.getDefaultState());
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
/*     */   {
/* 127 */     switch (side)
/*     */     {
/*     */     case EAST: 
/* 130 */       return true;
/*     */     
/*     */     case NORTH: 
/*     */     case SOUTH: 
/*     */     case UP: 
/*     */     case WEST: 
/* 136 */       Block block = worldIn.getBlockState(pos).getBlock();
/* 137 */       return (!block.isOpaqueCube()) && (block != Blocks.farmland);
/*     */     }
/*     */     
/* 140 */     return super.shouldSideBeRendered(worldIn, pos, side);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*     */   {
/* 149 */     return Blocks.dirt.getItemDropped(Blocks.dirt.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT), rand, fortune);
/*     */   }
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos)
/*     */   {
/* 154 */     return Item.getItemFromBlock(Blocks.dirt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 162 */     return getDefaultState().withProperty(MOISTURE, Integer.valueOf(meta & 0x7));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 170 */     return ((Integer)state.getValue(MOISTURE)).intValue();
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 175 */     return new BlockState(this, new IProperty[] { MOISTURE });
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockFarmland.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */