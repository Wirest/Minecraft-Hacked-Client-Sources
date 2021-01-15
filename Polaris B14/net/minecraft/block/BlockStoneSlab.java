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
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public abstract class BlockStoneSlab extends BlockSlab
/*     */ {
/*  22 */   public static final PropertyBool SEAMLESS = PropertyBool.create("seamless");
/*  23 */   public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
/*     */   
/*     */   public BlockStoneSlab()
/*     */   {
/*  27 */     super(Material.rock);
/*  28 */     IBlockState iblockstate = this.blockState.getBaseState();
/*     */     
/*  30 */     if (isDouble())
/*     */     {
/*  32 */       iblockstate = iblockstate.withProperty(SEAMLESS, Boolean.valueOf(false));
/*     */     }
/*     */     else
/*     */     {
/*  36 */       iblockstate = iblockstate.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
/*     */     }
/*     */     
/*  39 */     setDefaultState(iblockstate.withProperty(VARIANT, EnumType.STONE));
/*  40 */     setCreativeTab(CreativeTabs.tabBlock);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*     */   {
/*  48 */     return Item.getItemFromBlock(Blocks.stone_slab);
/*     */   }
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos)
/*     */   {
/*  53 */     return Item.getItemFromBlock(Blocks.stone_slab);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getUnlocalizedName(int meta)
/*     */   {
/*  61 */     return super.getUnlocalizedName() + "." + EnumType.byMetadata(meta).getUnlocalizedName();
/*     */   }
/*     */   
/*     */   public IProperty<?> getVariantProperty()
/*     */   {
/*  66 */     return VARIANT;
/*     */   }
/*     */   
/*     */   public Object getVariant(ItemStack stack)
/*     */   {
/*  71 */     return EnumType.byMetadata(stack.getMetadata() & 0x7);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
/*     */   {
/*  79 */     if (itemIn != Item.getItemFromBlock(Blocks.double_stone_slab)) {
/*     */       EnumType[] arrayOfEnumType;
/*  81 */       int j = (arrayOfEnumType = EnumType.values()).length; for (int i = 0; i < j; i++) { EnumType blockstoneslab$enumtype = arrayOfEnumType[i];
/*     */         
/*  83 */         if (blockstoneslab$enumtype != EnumType.WOOD)
/*     */         {
/*  85 */           list.add(new ItemStack(itemIn, 1, blockstoneslab$enumtype.getMetadata()));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/*  96 */     IBlockState iblockstate = getDefaultState().withProperty(VARIANT, EnumType.byMetadata(meta & 0x7));
/*     */     
/*  98 */     if (isDouble())
/*     */     {
/* 100 */       iblockstate = iblockstate.withProperty(SEAMLESS, Boolean.valueOf((meta & 0x8) != 0));
/*     */     }
/*     */     else
/*     */     {
/* 104 */       iblockstate = iblockstate.withProperty(HALF, (meta & 0x8) == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
/*     */     }
/*     */     
/* 107 */     return iblockstate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 115 */     int i = 0;
/* 116 */     i |= ((EnumType)state.getValue(VARIANT)).getMetadata();
/*     */     
/* 118 */     if (isDouble())
/*     */     {
/* 120 */       if (((Boolean)state.getValue(SEAMLESS)).booleanValue())
/*     */       {
/* 122 */         i |= 0x8;
/*     */       }
/*     */     }
/* 125 */     else if (state.getValue(HALF) == BlockSlab.EnumBlockHalf.TOP)
/*     */     {
/* 127 */       i |= 0x8;
/*     */     }
/*     */     
/* 130 */     return i;
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 135 */     return isDouble() ? new BlockState(this, new IProperty[] { SEAMLESS, VARIANT }) : new BlockState(this, new IProperty[] { HALF, VARIANT });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int damageDropped(IBlockState state)
/*     */   {
/* 144 */     return ((EnumType)state.getValue(VARIANT)).getMetadata();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MapColor getMapColor(IBlockState state)
/*     */   {
/* 152 */     return ((EnumType)state.getValue(VARIANT)).func_181074_c();
/*     */   }
/*     */   
/*     */   public static enum EnumType implements IStringSerializable
/*     */   {
/* 157 */     STONE(0, MapColor.stoneColor, "stone"), 
/* 158 */     SAND(1, MapColor.sandColor, "sandstone", "sand"), 
/* 159 */     WOOD(2, MapColor.woodColor, "wood_old", "wood"), 
/* 160 */     COBBLESTONE(3, MapColor.stoneColor, "cobblestone", "cobble"), 
/* 161 */     BRICK(4, MapColor.redColor, "brick"), 
/* 162 */     SMOOTHBRICK(5, MapColor.stoneColor, "stone_brick", "smoothStoneBrick"), 
/* 163 */     NETHERBRICK(6, MapColor.netherrackColor, "nether_brick", "netherBrick"), 
/* 164 */     QUARTZ(7, MapColor.quartzColor, "quartz");
/*     */     
/*     */     private static final EnumType[] META_LOOKUP;
/*     */     private final int meta;
/*     */     private final MapColor field_181075_k;
/*     */     private final String name;
/*     */     private final String unlocalizedName;
/*     */     
/*     */     private EnumType(int p_i46381_3_, MapColor p_i46381_4_, String p_i46381_5_)
/*     */     {
/* 174 */       this(p_i46381_3_, p_i46381_4_, p_i46381_5_, p_i46381_5_);
/*     */     }
/*     */     
/*     */     private EnumType(int p_i46382_3_, MapColor p_i46382_4_, String p_i46382_5_, String p_i46382_6_)
/*     */     {
/* 179 */       this.meta = p_i46382_3_;
/* 180 */       this.field_181075_k = p_i46382_4_;
/* 181 */       this.name = p_i46382_5_;
/* 182 */       this.unlocalizedName = p_i46382_6_;
/*     */     }
/*     */     
/*     */     public int getMetadata()
/*     */     {
/* 187 */       return this.meta;
/*     */     }
/*     */     
/*     */     public MapColor func_181074_c()
/*     */     {
/* 192 */       return this.field_181075_k;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 197 */       return this.name;
/*     */     }
/*     */     
/*     */     public static EnumType byMetadata(int meta)
/*     */     {
/* 202 */       if ((meta < 0) || (meta >= META_LOOKUP.length))
/*     */       {
/* 204 */         meta = 0;
/*     */       }
/*     */       
/* 207 */       return META_LOOKUP[meta];
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 212 */       return this.name;
/*     */     }
/*     */     
/*     */     public String getUnlocalizedName()
/*     */     {
/* 217 */       return this.unlocalizedName;
/*     */     }
/*     */     
/*     */     static
/*     */     {
/* 166 */       META_LOOKUP = new EnumType[values().length];
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
/*     */ 
/*     */ 
/*     */ 
/* 221 */       int j = (arrayOfEnumType = values()).length; for (int i = 0; i < j; i++) { EnumType blockstoneslab$enumtype = arrayOfEnumType[i];
/*     */         
/* 223 */         META_LOOKUP[blockstoneslab$enumtype.getMetadata()] = blockstoneslab$enumtype;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockStoneSlab.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */