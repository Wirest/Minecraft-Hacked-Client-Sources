/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockCarpet extends Block
/*     */ {
/*  21 */   public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);
/*     */   
/*     */   protected BlockCarpet()
/*     */   {
/*  25 */     super(Material.carpet);
/*  26 */     setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));
/*  27 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
/*  28 */     setTickRandomly(true);
/*  29 */     setCreativeTab(CreativeTabs.tabDecorations);
/*  30 */     setBlockBoundsFromMeta(0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MapColor getMapColor(IBlockState state)
/*     */   {
/*  38 */     return ((EnumDyeColor)state.getValue(COLOR)).getMapColor();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOpaqueCube()
/*     */   {
/*  46 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isFullCube()
/*     */   {
/*  51 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBlockBoundsForItemRender()
/*     */   {
/*  59 */     setBlockBoundsFromMeta(0);
/*     */   }
/*     */   
/*     */   public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos)
/*     */   {
/*  64 */     setBlockBoundsFromMeta(0);
/*     */   }
/*     */   
/*     */   protected void setBlockBoundsFromMeta(int meta)
/*     */   {
/*  69 */     int i = 0;
/*  70 */     float f = 1 * (1 + i) / 16.0F;
/*  71 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
/*     */   }
/*     */   
/*     */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
/*     */   {
/*  76 */     return (super.canPlaceBlockAt(worldIn, pos)) && (canBlockStay(worldIn, pos));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock)
/*     */   {
/*  84 */     checkForDrop(worldIn, pos, state);
/*     */   }
/*     */   
/*     */   private boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  89 */     if (!canBlockStay(worldIn, pos))
/*     */     {
/*  91 */       dropBlockAsItem(worldIn, pos, state, 0);
/*  92 */       worldIn.setBlockToAir(pos);
/*  93 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  97 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean canBlockStay(World worldIn, BlockPos pos)
/*     */   {
/* 103 */     return !worldIn.isAirBlock(pos.down());
/*     */   }
/*     */   
/*     */   public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side)
/*     */   {
/* 108 */     return side == EnumFacing.UP ? true : super.shouldSideBeRendered(worldIn, pos, side);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int damageDropped(IBlockState state)
/*     */   {
/* 117 */     return ((EnumDyeColor)state.getValue(COLOR)).getMetadata();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
/*     */   {
/* 125 */     for (int i = 0; i < 16; i++)
/*     */     {
/* 127 */       list.add(new ItemStack(itemIn, 1, i));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 136 */     return getDefaultState().withProperty(COLOR, EnumDyeColor.byMetadata(meta));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 144 */     return ((EnumDyeColor)state.getValue(COLOR)).getMetadata();
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 149 */     return new BlockState(this, new IProperty[] { COLOR });
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockCarpet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */