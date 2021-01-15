/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.properties.PropertyDirection;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.InventoryHelper;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityHopper;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockHopper extends BlockContainer
/*     */ {
/*  31 */   public static final PropertyDirection FACING = PropertyDirection.create("facing", new Predicate()
/*     */   {
/*     */     public boolean apply(EnumFacing p_apply_1_)
/*     */     {
/*  35 */       return p_apply_1_ != EnumFacing.UP;
/*     */     }
/*  31 */   });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  38 */   public static final PropertyBool ENABLED = PropertyBool.create("enabled");
/*     */   
/*     */   public BlockHopper()
/*     */   {
/*  42 */     super(Material.iron, MapColor.stoneColor);
/*  43 */     setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.DOWN).withProperty(ENABLED, Boolean.valueOf(true)));
/*  44 */     setCreativeTab(CreativeTabs.tabRedstone);
/*  45 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/*  50 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity)
/*     */   {
/*  58 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.625F, 1.0F);
/*  59 */     super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*  60 */     float f = 0.125F;
/*  61 */     setBlockBounds(0.0F, 0.0F, 0.0F, f, 1.0F, 1.0F);
/*  62 */     super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*  63 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, f);
/*  64 */     super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*  65 */     setBlockBounds(1.0F - f, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*  66 */     super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*  67 */     setBlockBounds(0.0F, 0.0F, 1.0F - f, 1.0F, 1.0F, 1.0F);
/*  68 */     super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
/*  69 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
/*     */   {
/*  78 */     EnumFacing enumfacing = facing.getOpposite();
/*     */     
/*  80 */     if (enumfacing == EnumFacing.UP)
/*     */     {
/*  82 */       enumfacing = EnumFacing.DOWN;
/*     */     }
/*     */     
/*  85 */     return getDefaultState().withProperty(FACING, enumfacing).withProperty(ENABLED, Boolean.valueOf(true));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TileEntity createNewTileEntity(World worldIn, int meta)
/*     */   {
/*  93 */     return new TileEntityHopper();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
/*     */   {
/* 101 */     super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
/*     */     
/* 103 */     if (stack.hasDisplayName())
/*     */     {
/* 105 */       TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */       
/* 107 */       if ((tileentity instanceof TileEntityHopper))
/*     */       {
/* 109 */         ((TileEntityHopper)tileentity).setCustomName(stack.getDisplayName());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 116 */     updateState(worldIn, pos, state);
/*     */   }
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
/*     */   {
/* 121 */     if (worldIn.isRemote)
/*     */     {
/* 123 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 127 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/* 129 */     if ((tileentity instanceof TileEntityHopper))
/*     */     {
/* 131 */       playerIn.displayGUIChest((TileEntityHopper)tileentity);
/* 132 */       playerIn.triggerAchievement(StatList.field_181732_P);
/*     */     }
/*     */     
/* 135 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*     */   {
/* 144 */     updateState(worldIn, pos, state);
/*     */   }
/*     */   
/*     */   private void updateState(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 149 */     boolean flag = !worldIn.isBlockPowered(pos);
/*     */     
/* 151 */     if (flag != ((Boolean)state.getValue(ENABLED)).booleanValue())
/*     */     {
/* 153 */       worldIn.setBlockState(pos, state.withProperty(ENABLED, Boolean.valueOf(flag)), 4);
/*     */     }
/*     */   }
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 159 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/* 161 */     if ((tileentity instanceof TileEntityHopper))
/*     */     {
/* 163 */       InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityHopper)tileentity);
/* 164 */       worldIn.updateComparatorOutputLevel(pos, this);
/*     */     }
/*     */     
/* 167 */     super.breakBlock(worldIn, pos, state);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRenderType()
/*     */   {
/* 175 */     return 3;
/*     */   }
/*     */   
/*     */   public boolean isFullCube()
/*     */   {
/* 180 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpaqueCube()
/*     */   {
/* 188 */     return false;
/*     */   }
/*     */   
/*     */   public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
/*     */   {
/* 193 */     return true;
/*     */   }
/*     */   
/*     */   public static EnumFacing getFacing(int meta)
/*     */   {
/* 198 */     return EnumFacing.getFront(meta & 0x7);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isEnabled(int meta)
/*     */   {
/* 207 */     return (meta & 0x8) != 8;
/*     */   }
/*     */   
/*     */   public boolean hasComparatorInputOverride()
/*     */   {
/* 212 */     return true;
/*     */   }
/*     */   
/*     */   public int getComparatorInputOverride(World worldIn, BlockPos pos)
/*     */   {
/* 217 */     return Container.calcRedstone(worldIn.getTileEntity(pos));
/*     */   }
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer()
/*     */   {
/* 222 */     return EnumWorldBlockLayer.CUTOUT_MIPPED;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 230 */     return getDefaultState().withProperty(FACING, getFacing(meta)).withProperty(ENABLED, Boolean.valueOf(isEnabled(meta)));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 238 */     int i = 0;
/* 239 */     i |= ((EnumFacing)state.getValue(FACING)).getIndex();
/*     */     
/* 241 */     if (!((Boolean)state.getValue(ENABLED)).booleanValue())
/*     */     {
/* 243 */       i |= 0x8;
/*     */     }
/*     */     
/* 246 */     return i;
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 251 */     return new BlockState(this, new IProperty[] { FACING, ENABLED });
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockHopper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */