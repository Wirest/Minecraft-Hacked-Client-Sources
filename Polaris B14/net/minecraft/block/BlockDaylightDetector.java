/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyInteger;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityDaylightDetector;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldProvider;
/*     */ 
/*     */ public class BlockDaylightDetector extends BlockContainer
/*     */ {
/*  26 */   public static final PropertyInteger POWER = PropertyInteger.create("power", 0, 15);
/*     */   private final boolean inverted;
/*     */   
/*     */   public BlockDaylightDetector(boolean inverted)
/*     */   {
/*  31 */     super(Material.wood);
/*  32 */     this.inverted = inverted;
/*  33 */     setDefaultState(this.blockState.getBaseState().withProperty(POWER, Integer.valueOf(0)));
/*  34 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.375F, 1.0F);
/*  35 */     setCreativeTab(CreativeTabs.tabRedstone);
/*  36 */     setHardness(0.2F);
/*  37 */     setStepSound(soundTypeWood);
/*  38 */     setUnlocalizedName("daylightDetector");
/*     */   }
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/*  43 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.375F, 1.0F);
/*     */   }
/*     */   
/*     */   public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side)
/*     */   {
/*  48 */     return ((Integer)state.getValue(POWER)).intValue();
/*     */   }
/*     */   
/*     */   public void updatePower(World worldIn, BlockPos pos)
/*     */   {
/*  53 */     if (!worldIn.provider.getHasNoSky())
/*     */     {
/*  55 */       IBlockState iblockstate = worldIn.getBlockState(pos);
/*  56 */       int i = worldIn.getLightFor(net.minecraft.world.EnumSkyBlock.SKY, pos) - worldIn.getSkylightSubtracted();
/*  57 */       float f = worldIn.getCelestialAngleRadians(1.0F);
/*  58 */       float f1 = f < 3.1415927F ? 0.0F : 6.2831855F;
/*  59 */       f += (f1 - f) * 0.2F;
/*  60 */       i = Math.round(i * MathHelper.cos(f));
/*  61 */       i = MathHelper.clamp_int(i, 0, 15);
/*     */       
/*  63 */       if (this.inverted)
/*     */       {
/*  65 */         i = 15 - i;
/*     */       }
/*     */       
/*  68 */       if (((Integer)iblockstate.getValue(POWER)).intValue() != i)
/*     */       {
/*  70 */         worldIn.setBlockState(pos, iblockstate.withProperty(POWER, Integer.valueOf(i)), 3);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
/*     */   {
/*  77 */     if (playerIn.isAllowEdit())
/*     */     {
/*  79 */       if (worldIn.isRemote)
/*     */       {
/*  81 */         return true;
/*     */       }
/*     */       
/*     */ 
/*  85 */       if (this.inverted)
/*     */       {
/*  87 */         worldIn.setBlockState(pos, Blocks.daylight_detector.getDefaultState().withProperty(POWER, (Integer)state.getValue(POWER)), 4);
/*  88 */         Blocks.daylight_detector.updatePower(worldIn, pos);
/*     */       }
/*     */       else
/*     */       {
/*  92 */         worldIn.setBlockState(pos, Blocks.daylight_detector_inverted.getDefaultState().withProperty(POWER, (Integer)state.getValue(POWER)), 4);
/*  93 */         Blocks.daylight_detector_inverted.updatePower(worldIn, pos);
/*     */       }
/*     */       
/*  96 */       return true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 101 */     return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*     */   {
/* 110 */     return Item.getItemFromBlock(Blocks.daylight_detector);
/*     */   }
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos)
/*     */   {
/* 115 */     return Item.getItemFromBlock(Blocks.daylight_detector);
/*     */   }
/*     */   
/*     */   public boolean isFullCube()
/*     */   {
/* 120 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpaqueCube()
/*     */   {
/* 128 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRenderType()
/*     */   {
/* 136 */     return 3;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canProvidePower()
/*     */   {
/* 144 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TileEntity createNewTileEntity(World worldIn, int meta)
/*     */   {
/* 152 */     return new TileEntityDaylightDetector();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 160 */     return getDefaultState().withProperty(POWER, Integer.valueOf(meta));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 168 */     return ((Integer)state.getValue(POWER)).intValue();
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 173 */     return new BlockState(this, new IProperty[] { POWER });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
/*     */   {
/* 181 */     if (!this.inverted)
/*     */     {
/* 183 */       super.getSubBlocks(itemIn, tab, list);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockDaylightDetector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */