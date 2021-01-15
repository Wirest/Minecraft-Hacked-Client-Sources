/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyBool;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ import net.minecraft.util.StatCollector;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public abstract class BlockStoneSlabNew extends BlockSlab
/*     */ {
/*  23 */   public static final PropertyBool SEAMLESS = PropertyBool.create("seamless");
/*  24 */   public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
/*     */   
/*     */   public BlockStoneSlabNew()
/*     */   {
/*  28 */     super(Material.rock);
/*  29 */     IBlockState iblockstate = this.blockState.getBaseState();
/*     */     
/*  31 */     if (isDouble())
/*     */     {
/*  33 */       iblockstate = iblockstate.withProperty(SEAMLESS, Boolean.valueOf(false));
/*     */     }
/*     */     else
/*     */     {
/*  37 */       iblockstate = iblockstate.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
/*     */     }
/*     */     
/*  40 */     setDefaultState(iblockstate.withProperty(VARIANT, EnumType.RED_SANDSTONE));
/*  41 */     setCreativeTab(CreativeTabs.tabBlock);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getLocalizedName()
/*     */   {
/*  49 */     return StatCollector.translateToLocal(getUnlocalizedName() + ".red_sandstone.name");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*     */   {
/*  57 */     return Item.getItemFromBlock(Blocks.stone_slab2);
/*     */   }
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos)
/*     */   {
/*  62 */     return Item.getItemFromBlock(Blocks.stone_slab2);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getUnlocalizedName(int meta)
/*     */   {
/*  70 */     return super.getUnlocalizedName() + "." + EnumType.byMetadata(meta).getUnlocalizedName();
/*     */   }
/*     */   
/*     */   public IProperty<?> getVariantProperty()
/*     */   {
/*  75 */     return VARIANT;
/*     */   }
/*     */   
/*     */   public Object getVariant(ItemStack stack)
/*     */   {
/*  80 */     return EnumType.byMetadata(stack.getMetadata() & 0x7);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
/*     */   {
/*  88 */     if (itemIn != Item.getItemFromBlock(Blocks.double_stone_slab2)) {
/*     */       EnumType[] arrayOfEnumType;
/*  90 */       int j = (arrayOfEnumType = EnumType.values()).length; for (int i = 0; i < j; i++) { EnumType blockstoneslabnew$enumtype = arrayOfEnumType[i];
/*     */         
/*  92 */         list.add(new ItemStack(itemIn, 1, blockstoneslabnew$enumtype.getMetadata()));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/* 102 */     IBlockState iblockstate = getDefaultState().withProperty(VARIANT, EnumType.byMetadata(meta & 0x7));
/*     */     
/* 104 */     if (isDouble())
/*     */     {
/* 106 */       iblockstate = iblockstate.withProperty(SEAMLESS, Boolean.valueOf((meta & 0x8) != 0));
/*     */     }
/*     */     else
/*     */     {
/* 110 */       iblockstate = iblockstate.withProperty(HALF, (meta & 0x8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
/*     */     }
/*     */     
/* 113 */     return iblockstate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 121 */     int i = 0;
/* 122 */     i |= ((EnumType)state.getValue(VARIANT)).getMetadata();
/*     */     
/* 124 */     if (isDouble())
/*     */     {
/* 126 */       if (((Boolean)state.getValue(SEAMLESS)).booleanValue())
/*     */       {
/* 128 */         i |= 0x8;
/*     */       }
/*     */     }
/* 131 */     else if (state.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP)
/*     */     {
/* 133 */       i |= 0x8;
/*     */     }
/*     */     
/* 136 */     return i;
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 141 */     return isDouble() ? new BlockState(this, new IProperty[] { SEAMLESS, VARIANT }) : new BlockState(this, new IProperty[] { HALF, VARIANT });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MapColor getMapColor(IBlockState state)
/*     */   {
/* 149 */     return ((EnumType)state.getValue(VARIANT)).func_181068_c();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int damageDropped(IBlockState state)
/*     */   {
/* 158 */     return ((EnumType)state.getValue(VARIANT)).getMetadata();
/*     */   }
/*     */   
/*     */   public static enum EnumType implements IStringSerializable
/*     */   {
/* 163 */     RED_SANDSTONE(0, "red_sandstone", BlockSand.EnumType.RED_SAND.getMapColor());
/*     */     
/*     */     private static final EnumType[] META_LOOKUP;
/*     */     private final int meta;
/*     */     private final String name;
/*     */     private final MapColor field_181069_e;
/*     */     
/*     */     private EnumType(int p_i46391_3_, String p_i46391_4_, MapColor p_i46391_5_)
/*     */     {
/* 172 */       this.meta = p_i46391_3_;
/* 173 */       this.name = p_i46391_4_;
/* 174 */       this.field_181069_e = p_i46391_5_;
/*     */     }
/*     */     
/*     */     public int getMetadata()
/*     */     {
/* 179 */       return this.meta;
/*     */     }
/*     */     
/*     */     public MapColor func_181068_c()
/*     */     {
/* 184 */       return this.field_181069_e;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 189 */       return this.name;
/*     */     }
/*     */     
/*     */     public static EnumType byMetadata(int meta)
/*     */     {
/* 194 */       if ((meta < 0) || (meta >= META_LOOKUP.length))
/*     */       {
/* 196 */         meta = 0;
/*     */       }
/*     */       
/* 199 */       return META_LOOKUP[meta];
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 204 */       return this.name;
/*     */     }
/*     */     
/*     */     public String getUnlocalizedName()
/*     */     {
/* 209 */       return this.name;
/*     */     }
/*     */     
/*     */     static
/*     */     {
/* 165 */       META_LOOKUP = new EnumType[values().length];
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       EnumType[] arrayOfEnumType;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 213 */       int j = (arrayOfEnumType = values()).length; for (int i = 0; i < j; i++) { EnumType blockstoneslabnew$enumtype = arrayOfEnumType[i];
/*     */         
/* 215 */         META_LOOKUP[blockstoneslabnew$enumtype.getMetadata()] = blockstoneslabnew$enumtype;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockStoneSlabNew.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */