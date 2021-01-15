/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
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
/*     */ import net.minecraft.util.EnumWorldBlockLayer;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockStainedGlass extends BlockBreakable
/*     */ {
/*  21 */   public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);
/*     */   
/*     */   public BlockStainedGlass(Material materialIn)
/*     */   {
/*  25 */     super(materialIn, false);
/*  26 */     setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));
/*  27 */     setCreativeTab(CreativeTabs.tabBlock);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int damageDropped(IBlockState state)
/*     */   {
/*  36 */     return ((EnumDyeColor)state.getValue(COLOR)).getMetadata();
/*     */   }
/*     */   
/*     */ 
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
/*     */   {
/*     */     EnumDyeColor[] arrayOfEnumDyeColor;
/*     */     
/*  44 */     int j = (arrayOfEnumDyeColor = EnumDyeColor.values()).length; for (int i = 0; i < j; i++) { EnumDyeColor enumdyecolor = arrayOfEnumDyeColor[i];
/*     */       
/*  46 */       list.add(new ItemStack(itemIn, 1, enumdyecolor.getMetadata()));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MapColor getMapColor(IBlockState state)
/*     */   {
/*  55 */     return ((EnumDyeColor)state.getValue(COLOR)).getMapColor();
/*     */   }
/*     */   
/*     */   public EnumWorldBlockLayer getBlockLayer()
/*     */   {
/*  60 */     return EnumWorldBlockLayer.TRANSLUCENT;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int quantityDropped(Random random)
/*     */   {
/*  68 */     return 0;
/*     */   }
/*     */   
/*     */   protected boolean canSilkHarvest()
/*     */   {
/*  73 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isFullCube()
/*     */   {
/*  78 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/*  86 */     return getDefaultState().withProperty(COLOR, EnumDyeColor.byMetadata(meta));
/*     */   }
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  91 */     if (!worldIn.isRemote)
/*     */     {
/*  93 */       BlockBeacon.updateColorAsync(worldIn, pos);
/*     */     }
/*     */   }
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
/*     */   {
/*  99 */     if (!worldIn.isRemote)
/*     */     {
/* 101 */       BlockBeacon.updateColorAsync(worldIn, pos);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 110 */     return ((EnumDyeColor)state.getValue(COLOR)).getMetadata();
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 115 */     return new BlockState(this, new IProperty[] { COLOR });
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockStainedGlass.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */