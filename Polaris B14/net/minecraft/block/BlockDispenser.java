/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.properties.PropertyDirection;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
/*     */ import net.minecraft.dispenser.IBehaviorDispenseItem;
/*     */ import net.minecraft.dispenser.IBlockSource;
/*     */ import net.minecraft.dispenser.IPosition;
/*     */ import net.minecraft.dispenser.PositionImpl;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.InventoryHelper;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityDispenser;
/*     */ import net.minecraft.tileentity.TileEntityDropper;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.RegistryDefaulted;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockDispenser extends BlockContainer
/*     */ {
/*  33 */   public static final PropertyDirection FACING = PropertyDirection.create("facing");
/*  34 */   public static final PropertyBool TRIGGERED = PropertyBool.create("triggered");
/*  35 */   public static final RegistryDefaulted<Item, IBehaviorDispenseItem> dispenseBehaviorRegistry = new RegistryDefaulted(new BehaviorDefaultDispenseItem());
/*  36 */   protected Random rand = new Random();
/*     */   
/*     */   protected BlockDispenser()
/*     */   {
/*  40 */     super(Material.rock);
/*  41 */     setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(TRIGGERED, Boolean.valueOf(false)));
/*  42 */     setCreativeTab(CreativeTabs.tabRedstone);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int tickRate(World worldIn)
/*     */   {
/*  50 */     return 4;
/*     */   }
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  55 */     super.onBlockAdded(worldIn, pos, state);
/*  56 */     setDefaultDirection(worldIn, pos, state);
/*     */   }
/*     */   
/*     */   private void setDefaultDirection(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  61 */     if (!worldIn.isRemote)
/*     */     {
/*  63 */       EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
/*  64 */       boolean flag = worldIn.getBlockState(pos.north()).getBlock().isFullBlock();
/*  65 */       boolean flag1 = worldIn.getBlockState(pos.south()).getBlock().isFullBlock();
/*     */       
/*  67 */       if ((enumfacing == EnumFacing.NORTH) && (flag) && (!flag1))
/*     */       {
/*  69 */         enumfacing = EnumFacing.SOUTH;
/*     */       }
/*  71 */       else if ((enumfacing == EnumFacing.SOUTH) && (flag1) && (!flag))
/*     */       {
/*  73 */         enumfacing = EnumFacing.NORTH;
/*     */       }
/*     */       else
/*     */       {
/*  77 */         boolean flag2 = worldIn.getBlockState(pos.west()).getBlock().isFullBlock();
/*  78 */         boolean flag3 = worldIn.getBlockState(pos.east()).getBlock().isFullBlock();
/*     */         
/*  80 */         if ((enumfacing == EnumFacing.WEST) && (flag2) && (!flag3))
/*     */         {
/*  82 */           enumfacing = EnumFacing.EAST;
/*     */         }
/*  84 */         else if ((enumfacing == EnumFacing.EAST) && (flag3) && (!flag2))
/*     */         {
/*  86 */           enumfacing = EnumFacing.WEST;
/*     */         }
/*     */       }
/*     */       
/*  90 */       worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing).withProperty(TRIGGERED, Boolean.valueOf(false)), 2);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
/*     */   {
/*  96 */     if (worldIn.isRemote)
/*     */     {
/*  98 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 102 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/* 104 */     if ((tileentity instanceof TileEntityDispenser))
/*     */     {
/* 106 */       playerIn.displayGUIChest((TileEntityDispenser)tileentity);
/*     */       
/* 108 */       if ((tileentity instanceof TileEntityDropper))
/*     */       {
/* 110 */         playerIn.triggerAchievement(StatList.field_181731_O);
/*     */       }
/*     */       else
/*     */       {
/* 114 */         playerIn.triggerAchievement(StatList.field_181733_Q);
/*     */       }
/*     */     }
/*     */     
/* 118 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void dispense(World worldIn, BlockPos pos)
/*     */   {
/* 124 */     BlockSourceImpl blocksourceimpl = new BlockSourceImpl(worldIn, pos);
/* 125 */     TileEntityDispenser tileentitydispenser = (TileEntityDispenser)blocksourceimpl.getBlockTileEntity();
/*     */     
/* 127 */     if (tileentitydispenser != null)
/*     */     {
/* 129 */       int i = tileentitydispenser.getDispenseSlot();
/*     */       
/* 131 */       if (i < 0)
/*     */       {
/* 133 */         worldIn.playAuxSFX(1001, pos, 0);
/*     */       }
/*     */       else
/*     */       {
/* 137 */         ItemStack itemstack = tileentitydispenser.getStackInSlot(i);
/* 138 */         IBehaviorDispenseItem ibehaviordispenseitem = getBehavior(itemstack);
/*     */         
/* 140 */         if (ibehaviordispenseitem != IBehaviorDispenseItem.itemDispenseBehaviorProvider)
/*     */         {
/* 142 */           ItemStack itemstack1 = ibehaviordispenseitem.dispense(blocksourceimpl, itemstack);
/* 143 */           tileentitydispenser.setInventorySlotContents(i, itemstack1.stackSize <= 0 ? null : itemstack1);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected IBehaviorDispenseItem getBehavior(ItemStack stack)
/*     */   {
/* 151 */     return (IBehaviorDispenseItem)dispenseBehaviorRegistry.getObject(stack == null ? null : stack.getItem());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*     */   {
/* 159 */     boolean flag = (worldIn.isBlockPowered(pos)) || (worldIn.isBlockPowered(pos.up()));
/* 160 */     boolean flag1 = ((Boolean)state.getValue(TRIGGERED)).booleanValue();
/*     */     
/* 162 */     if ((flag) && (!flag1))
/*     */     {
/* 164 */       worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
/* 165 */       worldIn.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(true)), 4);
/*     */     }
/* 167 */     else if ((!flag) && (flag1))
/*     */     {
/* 169 */       worldIn.setBlockState(pos, state.withProperty(TRIGGERED, Boolean.valueOf(false)), 4);
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*     */   {
/* 175 */     if (!worldIn.isRemote)
/*     */     {
/* 177 */       dispense(worldIn, pos);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TileEntity createNewTileEntity(World worldIn, int meta)
/*     */   {
/* 186 */     return new TileEntityDispenser();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
/*     */   {
/* 195 */     return getDefaultState().withProperty(FACING, BlockPistonBase.getFacingFromEntity(worldIn, pos, placer)).withProperty(TRIGGERED, Boolean.valueOf(false));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
/*     */   {
/* 203 */     worldIn.setBlockState(pos, state.withProperty(FACING, BlockPistonBase.getFacingFromEntity(worldIn, pos, placer)), 2);
/*     */     
/* 205 */     if (stack.hasDisplayName())
/*     */     {
/* 207 */       TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */       
/* 209 */       if ((tileentity instanceof TileEntityDispenser))
/*     */       {
/* 211 */         ((TileEntityDispenser)tileentity).setCustomName(stack.getDisplayName());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 218 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/* 220 */     if ((tileentity instanceof TileEntityDispenser))
/*     */     {
/* 222 */       InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityDispenser)tileentity);
/* 223 */       worldIn.updateComparatorOutputLevel(pos, this);
/*     */     }
/*     */     
/* 226 */     super.breakBlock(worldIn, pos, state);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static IPosition getDispensePosition(IBlockSource coords)
/*     */   {
/* 234 */     EnumFacing enumfacing = getFacing(coords.getBlockMetadata());
/* 235 */     double d0 = coords.getX() + 0.7D * enumfacing.getFrontOffsetX();
/* 236 */     double d1 = coords.getY() + 0.7D * enumfacing.getFrontOffsetY();
/* 237 */     double d2 = coords.getZ() + 0.7D * enumfacing.getFrontOffsetZ();
/* 238 */     return new PositionImpl(d0, d1, d2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static EnumFacing getFacing(int meta)
/*     */   {
/* 246 */     return EnumFacing.getFront(meta & 0x7);
/*     */   }
/*     */   
/*     */   public boolean hasComparatorInputOverride()
/*     */   {
/* 251 */     return true;
/*     */   }
/*     */   
/*     */   public int getComparatorInputOverride(World worldIn, BlockPos pos)
/*     */   {
/* 256 */     return Container.calcRedstone(worldIn.getTileEntity(pos));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRenderType()
/*     */   {
/* 264 */     return 3;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateForEntityRender(IBlockState state)
/*     */   {
/* 272 */     return getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 280 */     return getDefaultState().withProperty(FACING, getFacing(meta)).withProperty(TRIGGERED, Boolean.valueOf((meta & 0x8) > 0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 288 */     int i = 0;
/* 289 */     i |= ((EnumFacing)state.getValue(FACING)).getIndex();
/*     */     
/* 291 */     if (((Boolean)state.getValue(TRIGGERED)).booleanValue())
/*     */     {
/* 293 */       i |= 0x8;
/*     */     }
/*     */     
/* 296 */     return i;
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 301 */     return new BlockState(this, new IProperty[] { FACING, TRIGGERED });
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockDispenser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */