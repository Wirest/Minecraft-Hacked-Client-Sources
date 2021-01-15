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
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public abstract class BlockWoodSlab extends BlockSlab
/*     */ {
/*  20 */   public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class);
/*     */   
/*     */   public BlockWoodSlab()
/*     */   {
/*  24 */     super(Material.wood);
/*  25 */     IBlockState iblockstate = this.blockState.getBaseState();
/*     */     
/*  27 */     if (!isDouble())
/*     */     {
/*  29 */       iblockstate = iblockstate.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
/*     */     }
/*     */     
/*  32 */     setDefaultState(iblockstate.withProperty(VARIANT, BlockPlanks.EnumType.OAK));
/*  33 */     setCreativeTab(CreativeTabs.tabBlock);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MapColor getMapColor(IBlockState state)
/*     */   {
/*  41 */     return ((BlockPlanks.EnumType)state.getValue(VARIANT)).func_181070_c();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*     */   {
/*  49 */     return Item.getItemFromBlock(Blocks.wooden_slab);
/*     */   }
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos)
/*     */   {
/*  54 */     return Item.getItemFromBlock(Blocks.wooden_slab);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getUnlocalizedName(int meta)
/*     */   {
/*  62 */     return super.getUnlocalizedName() + "." + BlockPlanks.EnumType.byMetadata(meta).getUnlocalizedName();
/*     */   }
/*     */   
/*     */   public IProperty<?> getVariantProperty()
/*     */   {
/*  67 */     return VARIANT;
/*     */   }
/*     */   
/*     */   public Object getVariant(ItemStack stack)
/*     */   {
/*  72 */     return BlockPlanks.EnumType.byMetadata(stack.getMetadata() & 0x7);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
/*     */   {
/*  80 */     if (itemIn != Item.getItemFromBlock(Blocks.double_wooden_slab)) {
/*     */       BlockPlanks.EnumType[] arrayOfEnumType;
/*  82 */       int j = (arrayOfEnumType = BlockPlanks.EnumType.values()).length; for (int i = 0; i < j; i++) { BlockPlanks.EnumType blockplanks$enumtype = arrayOfEnumType[i];
/*     */         
/*  84 */         list.add(new ItemStack(itemIn, 1, blockplanks$enumtype.getMetadata()));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/*  94 */     IBlockState iblockstate = getDefaultState().withProperty(VARIANT, BlockPlanks.EnumType.byMetadata(meta & 0x7));
/*     */     
/*  96 */     if (!isDouble())
/*     */     {
/*  98 */       iblockstate = iblockstate.withProperty(HALF, (meta & 0x8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
/*     */     }
/*     */     
/* 101 */     return iblockstate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 109 */     int i = 0;
/* 110 */     i |= ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata();
/*     */     
/* 112 */     if ((!isDouble()) && (state.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP))
/*     */     {
/* 114 */       i |= 0x8;
/*     */     }
/*     */     
/* 117 */     return i;
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 122 */     return isDouble() ? new BlockState(this, new IProperty[] { VARIANT }) : new BlockState(this, new IProperty[] { HALF, VARIANT });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int damageDropped(IBlockState state)
/*     */   {
/* 131 */     return ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockWoodSlab.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */