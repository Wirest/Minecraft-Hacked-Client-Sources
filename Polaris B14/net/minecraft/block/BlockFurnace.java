/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyDirection;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityFurnace;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumFacing.Axis;
/*     */ import net.minecraft.util.EnumFacing.Plane;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockFurnace extends BlockContainer
/*     */ {
/*  26 */   public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
/*     */   private final boolean isBurning;
/*     */   private static boolean keepInventory;
/*     */   
/*     */   protected BlockFurnace(boolean isBurning)
/*     */   {
/*  32 */     super(net.minecraft.block.material.Material.rock);
/*  33 */     setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
/*  34 */     this.isBurning = isBurning;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*     */   {
/*  42 */     return Item.getItemFromBlock(Blocks.furnace);
/*     */   }
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  47 */     setDefaultFacing(worldIn, pos, state);
/*     */   }
/*     */   
/*     */   private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  52 */     if (!worldIn.isRemote)
/*     */     {
/*  54 */       Block block = worldIn.getBlockState(pos.north()).getBlock();
/*  55 */       Block block1 = worldIn.getBlockState(pos.south()).getBlock();
/*  56 */       Block block2 = worldIn.getBlockState(pos.west()).getBlock();
/*  57 */       Block block3 = worldIn.getBlockState(pos.east()).getBlock();
/*  58 */       EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
/*     */       
/*  60 */       if ((enumfacing == EnumFacing.NORTH) && (block.isFullBlock()) && (!block1.isFullBlock()))
/*     */       {
/*  62 */         enumfacing = EnumFacing.SOUTH;
/*     */       }
/*  64 */       else if ((enumfacing == EnumFacing.SOUTH) && (block1.isFullBlock()) && (!block.isFullBlock()))
/*     */       {
/*  66 */         enumfacing = EnumFacing.NORTH;
/*     */       }
/*  68 */       else if ((enumfacing == EnumFacing.WEST) && (block2.isFullBlock()) && (!block3.isFullBlock()))
/*     */       {
/*  70 */         enumfacing = EnumFacing.EAST;
/*     */       }
/*  72 */       else if ((enumfacing == EnumFacing.EAST) && (block3.isFullBlock()) && (!block2.isFullBlock()))
/*     */       {
/*  74 */         enumfacing = EnumFacing.WEST;
/*     */       }
/*     */       
/*  77 */       worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*     */   {
/*  84 */     if (this.isBurning)
/*     */     {
/*  86 */       EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
/*  87 */       double d0 = pos.getX() + 0.5D;
/*  88 */       double d1 = pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
/*  89 */       double d2 = pos.getZ() + 0.5D;
/*  90 */       double d3 = 0.52D;
/*  91 */       double d4 = rand.nextDouble() * 0.6D - 0.3D;
/*     */       
/*  93 */       switch (enumfacing)
/*     */       {
/*     */       case UP: 
/*  96 */         worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
/*  97 */         worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
/*  98 */         break;
/*     */       
/*     */       case WEST: 
/* 101 */         worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
/* 102 */         worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
/* 103 */         break;
/*     */       
/*     */       case NORTH: 
/* 106 */         worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D, new int[0]);
/* 107 */         worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D, new int[0]);
/* 108 */         break;
/*     */       
/*     */       case SOUTH: 
/* 111 */         worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D, new int[0]);
/* 112 */         worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
/*     */   {
/* 119 */     if (worldIn.isRemote)
/*     */     {
/* 121 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 125 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/* 127 */     if ((tileentity instanceof TileEntityFurnace))
/*     */     {
/* 129 */       playerIn.displayGUIChest((TileEntityFurnace)tileentity);
/* 130 */       playerIn.triggerAchievement(StatList.field_181741_Y);
/*     */     }
/*     */     
/* 133 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public static void setState(boolean active, World worldIn, BlockPos pos)
/*     */   {
/* 139 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/* 140 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/* 141 */     keepInventory = true;
/*     */     
/* 143 */     if (active)
/*     */     {
/* 145 */       worldIn.setBlockState(pos, Blocks.lit_furnace.getDefaultState().withProperty(FACING, (EnumFacing)iblockstate.getValue(FACING)), 3);
/* 146 */       worldIn.setBlockState(pos, Blocks.lit_furnace.getDefaultState().withProperty(FACING, (EnumFacing)iblockstate.getValue(FACING)), 3);
/*     */     }
/*     */     else
/*     */     {
/* 150 */       worldIn.setBlockState(pos, Blocks.furnace.getDefaultState().withProperty(FACING, (EnumFacing)iblockstate.getValue(FACING)), 3);
/* 151 */       worldIn.setBlockState(pos, Blocks.furnace.getDefaultState().withProperty(FACING, (EnumFacing)iblockstate.getValue(FACING)), 3);
/*     */     }
/*     */     
/* 154 */     keepInventory = false;
/*     */     
/* 156 */     if (tileentity != null)
/*     */     {
/* 158 */       tileentity.validate();
/* 159 */       worldIn.setTileEntity(pos, tileentity);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TileEntity createNewTileEntity(World worldIn, int meta)
/*     */   {
/* 168 */     return new TileEntityFurnace();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
/*     */   {
/* 177 */     return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
/*     */   {
/* 185 */     worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
/*     */     
/* 187 */     if (stack.hasDisplayName())
/*     */     {
/* 189 */       TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */       
/* 191 */       if ((tileentity instanceof TileEntityFurnace))
/*     */       {
/* 193 */         ((TileEntityFurnace)tileentity).setCustomInventoryName(stack.getDisplayName());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 200 */     if (!keepInventory)
/*     */     {
/* 202 */       TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */       
/* 204 */       if ((tileentity instanceof TileEntityFurnace))
/*     */       {
/* 206 */         net.minecraft.inventory.InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityFurnace)tileentity);
/* 207 */         worldIn.updateComparatorOutputLevel(pos, this);
/*     */       }
/*     */     }
/*     */     
/* 211 */     super.breakBlock(worldIn, pos, state);
/*     */   }
/*     */   
/*     */   public boolean hasComparatorInputOverride()
/*     */   {
/* 216 */     return true;
/*     */   }
/*     */   
/*     */   public int getComparatorInputOverride(World worldIn, BlockPos pos)
/*     */   {
/* 221 */     return Container.calcRedstone(worldIn.getTileEntity(pos));
/*     */   }
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos)
/*     */   {
/* 226 */     return Item.getItemFromBlock(Blocks.furnace);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRenderType()
/*     */   {
/* 234 */     return 3;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateForEntityRender(IBlockState state)
/*     */   {
/* 242 */     return getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 250 */     EnumFacing enumfacing = EnumFacing.getFront(meta);
/*     */     
/* 252 */     if (enumfacing.getAxis() == EnumFacing.Axis.Y)
/*     */     {
/* 254 */       enumfacing = EnumFacing.NORTH;
/*     */     }
/*     */     
/* 257 */     return getDefaultState().withProperty(FACING, enumfacing);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 265 */     return ((EnumFacing)state.getValue(FACING)).getIndex();
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 270 */     return new BlockState(this, new IProperty[] { FACING });
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockFurnace.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */