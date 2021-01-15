/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityMinecartCommandBlock;
/*     */ import net.minecraft.entity.item.EntityMinecart;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EntitySelectors;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockRailDetector extends BlockRailBase
/*     */ {
/*  25 */   public static final PropertyEnum<BlockRailBase.EnumRailDirection> SHAPE = PropertyEnum.create("shape", BlockRailBase.EnumRailDirection.class, new Predicate()
/*     */   {
/*     */     public boolean apply(BlockRailBase.EnumRailDirection p_apply_1_)
/*     */     {
/*  29 */       return (p_apply_1_ != BlockRailBase.EnumRailDirection.NORTH_EAST) && (p_apply_1_ != BlockRailBase.EnumRailDirection.NORTH_WEST) && (p_apply_1_ != BlockRailBase.EnumRailDirection.SOUTH_EAST) && (p_apply_1_ != BlockRailBase.EnumRailDirection.SOUTH_WEST);
/*     */     }
/*  25 */   });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  32 */   public static final PropertyBool POWERED = PropertyBool.create("powered");
/*     */   
/*     */   public BlockRailDetector()
/*     */   {
/*  36 */     super(true);
/*  37 */     setDefaultState(this.blockState.getBaseState().withProperty(POWERED, Boolean.valueOf(false)).withProperty(SHAPE, BlockRailBase.EnumRailDirection.NORTH_SOUTH));
/*  38 */     setTickRandomly(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int tickRate(World worldIn)
/*     */   {
/*  46 */     return 20;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canProvidePower()
/*     */   {
/*  54 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
/*     */   {
/*  62 */     if (!worldIn.isRemote)
/*     */     {
/*  64 */       if (!((Boolean)state.getValue(POWERED)).booleanValue())
/*     */       {
/*  66 */         updatePoweredState(worldIn, pos, state);
/*     */       }
/*     */     }
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
/*  80 */     if ((!worldIn.isRemote) && (((Boolean)state.getValue(POWERED)).booleanValue()))
/*     */     {
/*  82 */       updatePoweredState(worldIn, pos, state);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
/*     */   {
/*  88 */     return ((Boolean)state.getValue(POWERED)).booleanValue() ? 15 : 0;
/*     */   }
/*     */   
/*     */   public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
/*     */   {
/*  93 */     return side == EnumFacing.UP ? 15 : !((Boolean)state.getValue(POWERED)).booleanValue() ? 0 : 0;
/*     */   }
/*     */   
/*     */   private void updatePoweredState(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  98 */     boolean flag = ((Boolean)state.getValue(POWERED)).booleanValue();
/*  99 */     boolean flag1 = false;
/* 100 */     List<EntityMinecart> list = findMinecarts(worldIn, pos, EntityMinecart.class, new Predicate[0]);
/*     */     
/* 102 */     if (!list.isEmpty())
/*     */     {
/* 104 */       flag1 = true;
/*     */     }
/*     */     
/* 107 */     if ((flag1) && (!flag))
/*     */     {
/* 109 */       worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(true)), 3);
/* 110 */       worldIn.notifyNeighborsOfStateChange(pos, this);
/* 111 */       worldIn.notifyNeighborsOfStateChange(pos.down(), this);
/* 112 */       worldIn.markBlockRangeForRenderUpdate(pos, pos);
/*     */     }
/*     */     
/* 115 */     if ((!flag1) && (flag))
/*     */     {
/* 117 */       worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(false)), 3);
/* 118 */       worldIn.notifyNeighborsOfStateChange(pos, this);
/* 119 */       worldIn.notifyNeighborsOfStateChange(pos.down(), this);
/* 120 */       worldIn.markBlockRangeForRenderUpdate(pos, pos);
/*     */     }
/*     */     
/* 123 */     if (flag1)
/*     */     {
/* 125 */       worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
/*     */     }
/*     */     
/* 128 */     worldIn.updateComparatorOutputLevel(pos, this);
/*     */   }
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 133 */     super.onBlockAdded(worldIn, pos, state);
/* 134 */     updatePoweredState(worldIn, pos, state);
/*     */   }
/*     */   
/*     */   public IProperty<BlockRailBase.EnumRailDirection> getShapeProperty()
/*     */   {
/* 139 */     return SHAPE;
/*     */   }
/*     */   
/*     */   public boolean hasComparatorInputOverride()
/*     */   {
/* 144 */     return true;
/*     */   }
/*     */   
/*     */   public int getComparatorInputOverride(World worldIn, BlockPos pos)
/*     */   {
/* 149 */     if (((Boolean)worldIn.getBlockState(pos).getValue(POWERED)).booleanValue())
/*     */     {
/* 151 */       List<EntityMinecartCommandBlock> list = findMinecarts(worldIn, pos, EntityMinecartCommandBlock.class, new Predicate[0]);
/*     */       
/* 153 */       if (!list.isEmpty())
/*     */       {
/* 155 */         return ((EntityMinecartCommandBlock)list.get(0)).getCommandBlockLogic().getSuccessCount();
/*     */       }
/*     */       
/* 158 */       List<EntityMinecart> list1 = findMinecarts(worldIn, pos, EntityMinecart.class, new Predicate[] { EntitySelectors.selectInventories });
/*     */       
/* 160 */       if (!list1.isEmpty())
/*     */       {
/* 162 */         return Container.calcRedstoneFromInventory((IInventory)list1.get(0));
/*     */       }
/*     */     }
/*     */     
/* 166 */     return 0;
/*     */   }
/*     */   
/*     */   protected <T extends EntityMinecart> List<T> findMinecarts(World worldIn, BlockPos pos, Class<T> clazz, Predicate<Entity>... filter)
/*     */   {
/* 171 */     AxisAlignedBB axisalignedbb = getDectectionBox(pos);
/* 172 */     return filter.length != 1 ? worldIn.getEntitiesWithinAABB(clazz, axisalignedbb) : worldIn.getEntitiesWithinAABB(clazz, axisalignedbb, filter[0]);
/*     */   }
/*     */   
/*     */   private AxisAlignedBB getDectectionBox(BlockPos pos)
/*     */   {
/* 177 */     float f = 0.2F;
/* 178 */     return new AxisAlignedBB(pos.getX() + 0.2F, pos.getY(), pos.getZ() + 0.2F, pos.getX() + 1 - 0.2F, pos.getY() + 1 - 0.2F, pos.getZ() + 1 - 0.2F);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 186 */     return getDefaultState().withProperty(SHAPE, BlockRailBase.EnumRailDirection.byMetadata(meta & 0x7)).withProperty(POWERED, Boolean.valueOf((meta & 0x8) > 0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 194 */     int i = 0;
/* 195 */     i |= ((BlockRailBase.EnumRailDirection)state.getValue(SHAPE)).getMetadata();
/*     */     
/* 197 */     if (((Boolean)state.getValue(POWERED)).booleanValue())
/*     */     {
/* 199 */       i |= 0x8;
/*     */     }
/*     */     
/* 202 */     return i;
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 207 */     return new BlockState(this, new IProperty[] { SHAPE, POWERED });
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockRailDetector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */