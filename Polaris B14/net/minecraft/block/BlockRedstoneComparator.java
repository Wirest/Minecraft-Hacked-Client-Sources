/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.item.EntityItemFrame;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityComparator;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockRedstoneComparator extends BlockRedstoneDiode implements ITileEntityProvider
/*     */ {
/*  31 */   public static final PropertyBool POWERED = PropertyBool.create("powered");
/*  32 */   public static final PropertyEnum<Mode> MODE = PropertyEnum.create("mode", Mode.class);
/*     */   
/*     */   public BlockRedstoneComparator(boolean powered)
/*     */   {
/*  36 */     super(powered);
/*  37 */     setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(POWERED, Boolean.valueOf(false)).withProperty(MODE, Mode.COMPARE));
/*  38 */     this.isBlockContainer = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getLocalizedName()
/*     */   {
/*  46 */     return net.minecraft.util.StatCollector.translateToLocal("item.comparator.name");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*     */   {
/*  54 */     return Items.comparator;
/*     */   }
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos)
/*     */   {
/*  59 */     return Items.comparator;
/*     */   }
/*     */   
/*     */   protected int getDelay(IBlockState state)
/*     */   {
/*  64 */     return 2;
/*     */   }
/*     */   
/*     */   protected IBlockState getPoweredState(IBlockState unpoweredState)
/*     */   {
/*  69 */     Boolean obool = (Boolean)unpoweredState.getValue(POWERED);
/*  70 */     Mode blockredstonecomparator$mode = (Mode)unpoweredState.getValue(MODE);
/*  71 */     EnumFacing enumfacing = (EnumFacing)unpoweredState.getValue(FACING);
/*  72 */     return Blocks.powered_comparator.getDefaultState().withProperty(FACING, enumfacing).withProperty(POWERED, obool).withProperty(MODE, blockredstonecomparator$mode);
/*     */   }
/*     */   
/*     */   protected IBlockState getUnpoweredState(IBlockState poweredState)
/*     */   {
/*  77 */     Boolean obool = (Boolean)poweredState.getValue(POWERED);
/*  78 */     Mode blockredstonecomparator$mode = (Mode)poweredState.getValue(MODE);
/*  79 */     EnumFacing enumfacing = (EnumFacing)poweredState.getValue(FACING);
/*  80 */     return Blocks.unpowered_comparator.getDefaultState().withProperty(FACING, enumfacing).withProperty(POWERED, obool).withProperty(MODE, blockredstonecomparator$mode);
/*     */   }
/*     */   
/*     */   protected boolean isPowered(IBlockState state)
/*     */   {
/*  85 */     return (this.isRepeaterPowered) || (((Boolean)state.getValue(POWERED)).booleanValue());
/*     */   }
/*     */   
/*     */   protected int getActiveSignal(IBlockAccess worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  90 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*  91 */     return (tileentity instanceof TileEntityComparator) ? ((TileEntityComparator)tileentity).getOutputSignal() : 0;
/*     */   }
/*     */   
/*     */   private int calculateOutput(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  96 */     return state.getValue(MODE) == Mode.SUBTRACT ? Math.max(calculateInputStrength(worldIn, pos, state) - getPowerOnSides(worldIn, pos, state), 0) : calculateInputStrength(worldIn, pos, state);
/*     */   }
/*     */   
/*     */   protected boolean shouldBePowered(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 101 */     int i = calculateInputStrength(worldIn, pos, state);
/*     */     
/* 103 */     if (i >= 15)
/*     */     {
/* 105 */       return true;
/*     */     }
/* 107 */     if (i == 0)
/*     */     {
/* 109 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 113 */     int j = getPowerOnSides(worldIn, pos, state);
/* 114 */     return j == 0;
/*     */   }
/*     */   
/*     */ 
/*     */   protected int calculateInputStrength(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 120 */     int i = super.calculateInputStrength(worldIn, pos, state);
/* 121 */     EnumFacing enumfacing = (EnumFacing)state.getValue(FACING);
/* 122 */     BlockPos blockpos = pos.offset(enumfacing);
/* 123 */     Block block = worldIn.getBlockState(blockpos).getBlock();
/*     */     
/* 125 */     if (block.hasComparatorInputOverride())
/*     */     {
/* 127 */       i = block.getComparatorInputOverride(worldIn, blockpos);
/*     */     }
/* 129 */     else if ((i < 15) && (block.isNormalCube()))
/*     */     {
/* 131 */       blockpos = blockpos.offset(enumfacing);
/* 132 */       block = worldIn.getBlockState(blockpos).getBlock();
/*     */       
/* 134 */       if (block.hasComparatorInputOverride())
/*     */       {
/* 136 */         i = block.getComparatorInputOverride(worldIn, blockpos);
/*     */       }
/* 138 */       else if (block.getMaterial() == Material.air)
/*     */       {
/* 140 */         EntityItemFrame entityitemframe = findItemFrame(worldIn, enumfacing, blockpos);
/*     */         
/* 142 */         if (entityitemframe != null)
/*     */         {
/* 144 */           i = entityitemframe.func_174866_q();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 149 */     return i;
/*     */   }
/*     */   
/*     */   private EntityItemFrame findItemFrame(World worldIn, final EnumFacing facing, BlockPos pos)
/*     */   {
/* 154 */     List<EntityItemFrame> list = worldIn.getEntitiesWithinAABB(EntityItemFrame.class, new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1), new Predicate()
/*     */     {
/*     */       public boolean apply(Entity p_apply_1_)
/*     */       {
/* 158 */         return (p_apply_1_ != null) && (p_apply_1_.getHorizontalFacing() == facing);
/*     */       }
/* 160 */     });
/* 161 */     return list.size() == 1 ? (EntityItemFrame)list.get(0) : null;
/*     */   }
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
/*     */   {
/* 166 */     if (!playerIn.capabilities.allowEdit)
/*     */     {
/* 168 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 172 */     state = state.cycleProperty(MODE);
/* 173 */     worldIn.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, "random.click", 0.3F, state.getValue(MODE) == Mode.SUBTRACT ? 0.55F : 0.5F);
/* 174 */     worldIn.setBlockState(pos, state, 2);
/* 175 */     onStateChange(worldIn, pos, state);
/* 176 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void updateState(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 182 */     if (!worldIn.isBlockTickPending(pos, this))
/*     */     {
/* 184 */       int i = calculateOutput(worldIn, pos, state);
/* 185 */       TileEntity tileentity = worldIn.getTileEntity(pos);
/* 186 */       int j = (tileentity instanceof TileEntityComparator) ? ((TileEntityComparator)tileentity).getOutputSignal() : 0;
/*     */       
/* 188 */       if ((i != j) || (isPowered(state) != shouldBePowered(worldIn, pos, state)))
/*     */       {
/* 190 */         if (isFacingTowardsRepeater(worldIn, pos, state))
/*     */         {
/* 192 */           worldIn.updateBlockTick(pos, this, 2, -1);
/*     */         }
/*     */         else
/*     */         {
/* 196 */           worldIn.updateBlockTick(pos, this, 2, 0);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void onStateChange(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 204 */     int i = calculateOutput(worldIn, pos, state);
/* 205 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/* 206 */     int j = 0;
/*     */     
/* 208 */     if ((tileentity instanceof TileEntityComparator))
/*     */     {
/* 210 */       TileEntityComparator tileentitycomparator = (TileEntityComparator)tileentity;
/* 211 */       j = tileentitycomparator.getOutputSignal();
/* 212 */       tileentitycomparator.setOutputSignal(i);
/*     */     }
/*     */     
/* 215 */     if ((j != i) || (state.getValue(MODE) == Mode.COMPARE))
/*     */     {
/* 217 */       boolean flag1 = shouldBePowered(worldIn, pos, state);
/* 218 */       boolean flag = isPowered(state);
/*     */       
/* 220 */       if ((flag) && (!flag1))
/*     */       {
/* 222 */         worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(false)), 2);
/*     */       }
/* 224 */       else if ((!flag) && (flag1))
/*     */       {
/* 226 */         worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(true)), 2);
/*     */       }
/*     */       
/* 229 */       notifyNeighbors(worldIn, pos, state);
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
/*     */   {
/* 235 */     if (this.isRepeaterPowered)
/*     */     {
/* 237 */       worldIn.setBlockState(pos, getUnpoweredState(state).withProperty(POWERED, Boolean.valueOf(true)), 4);
/*     */     }
/*     */     
/* 240 */     onStateChange(worldIn, pos, state);
/*     */   }
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 245 */     super.onBlockAdded(worldIn, pos, state);
/* 246 */     worldIn.setTileEntity(pos, createNewTileEntity(worldIn, 0));
/*     */   }
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/* 251 */     super.breakBlock(worldIn, pos, state);
/* 252 */     worldIn.removeTileEntity(pos);
/* 253 */     notifyNeighbors(worldIn, pos, state);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean onBlockEventReceived(World worldIn, BlockPos pos, IBlockState state, int eventID, int eventParam)
/*     */   {
/* 261 */     super.onBlockEventReceived(worldIn, pos, state, eventID, eventParam);
/* 262 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/* 263 */     return tileentity == null ? false : tileentity.receiveClientEvent(eventID, eventParam);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TileEntity createNewTileEntity(World worldIn, int meta)
/*     */   {
/* 271 */     return new TileEntityComparator();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 279 */     return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta)).withProperty(POWERED, Boolean.valueOf((meta & 0x8) > 0)).withProperty(MODE, (meta & 0x4) > 0 ? Mode.SUBTRACT : Mode.COMPARE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 287 */     int i = 0;
/* 288 */     i |= ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
/*     */     
/* 290 */     if (((Boolean)state.getValue(POWERED)).booleanValue())
/*     */     {
/* 292 */       i |= 0x8;
/*     */     }
/*     */     
/* 295 */     if (state.getValue(MODE) == Mode.SUBTRACT)
/*     */     {
/* 297 */       i |= 0x4;
/*     */     }
/*     */     
/* 300 */     return i;
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 305 */     return new BlockState(this, new IProperty[] { FACING, MODE, POWERED });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
/*     */   {
/* 314 */     return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(POWERED, Boolean.valueOf(false)).withProperty(MODE, Mode.COMPARE);
/*     */   }
/*     */   
/*     */   public static enum Mode implements IStringSerializable
/*     */   {
/* 319 */     COMPARE("compare"), 
/* 320 */     SUBTRACT("subtract");
/*     */     
/*     */     private final String name;
/*     */     
/*     */     private Mode(String name)
/*     */     {
/* 326 */       this.name = name;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 331 */       return this.name;
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 336 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockRedstoneComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */