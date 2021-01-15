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
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ 
/*     */ public class BlockPlanks extends Block
/*     */ {
/*  17 */   public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
/*     */   
/*     */   public BlockPlanks()
/*     */   {
/*  21 */     super(Material.wood);
/*  22 */     setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumType.OAK));
/*  23 */     setCreativeTab(CreativeTabs.tabBlock);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int damageDropped(IBlockState state)
/*     */   {
/*  32 */     return ((EnumType)state.getValue(VARIANT)).getMetadata();
/*     */   }
/*     */   
/*     */ 
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
/*     */   {
/*     */     EnumType[] arrayOfEnumType;
/*     */     
/*  40 */     int j = (arrayOfEnumType = EnumType.values()).length; for (int i = 0; i < j; i++) { EnumType blockplanks$enumtype = arrayOfEnumType[i];
/*     */       
/*  42 */       list.add(new ItemStack(itemIn, 1, blockplanks$enumtype.getMetadata()));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/*  51 */     return getDefaultState().withProperty(VARIANT, EnumType.byMetadata(meta));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MapColor getMapColor(IBlockState state)
/*     */   {
/*  59 */     return ((EnumType)state.getValue(VARIANT)).func_181070_c();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/*  67 */     return ((EnumType)state.getValue(VARIANT)).getMetadata();
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/*  72 */     return new BlockState(this, new IProperty[] { VARIANT });
/*     */   }
/*     */   
/*     */   public static enum EnumType implements IStringSerializable
/*     */   {
/*  77 */     OAK(0, "oak", MapColor.woodColor), 
/*  78 */     SPRUCE(1, "spruce", MapColor.obsidianColor), 
/*  79 */     BIRCH(2, "birch", MapColor.sandColor), 
/*  80 */     JUNGLE(3, "jungle", MapColor.dirtColor), 
/*  81 */     ACACIA(4, "acacia", MapColor.adobeColor), 
/*  82 */     DARK_OAK(5, "dark_oak", "big_oak", MapColor.brownColor);
/*     */     
/*     */     private static final EnumType[] META_LOOKUP;
/*     */     private final int meta;
/*     */     private final String name;
/*     */     private final String unlocalizedName;
/*     */     private final MapColor field_181071_k;
/*     */     
/*     */     private EnumType(int p_i46388_3_, String p_i46388_4_, MapColor p_i46388_5_)
/*     */     {
/*  92 */       this(p_i46388_3_, p_i46388_4_, p_i46388_4_, p_i46388_5_);
/*     */     }
/*     */     
/*     */     private EnumType(int p_i46389_3_, String p_i46389_4_, String p_i46389_5_, MapColor p_i46389_6_)
/*     */     {
/*  97 */       this.meta = p_i46389_3_;
/*  98 */       this.name = p_i46389_4_;
/*  99 */       this.unlocalizedName = p_i46389_5_;
/* 100 */       this.field_181071_k = p_i46389_6_;
/*     */     }
/*     */     
/*     */     public int getMetadata()
/*     */     {
/* 105 */       return this.meta;
/*     */     }
/*     */     
/*     */     public MapColor func_181070_c()
/*     */     {
/* 110 */       return this.field_181071_k;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 115 */       return this.name;
/*     */     }
/*     */     
/*     */     public static EnumType byMetadata(int meta)
/*     */     {
/* 120 */       if ((meta < 0) || (meta >= META_LOOKUP.length))
/*     */       {
/* 122 */         meta = 0;
/*     */       }
/*     */       
/* 125 */       return META_LOOKUP[meta];
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 130 */       return this.name;
/*     */     }
/*     */     
/*     */     public String getUnlocalizedName()
/*     */     {
/* 135 */       return this.unlocalizedName;
/*     */     }
/*     */     
/*     */     static
/*     */     {
/*  84 */       META_LOOKUP = new EnumType[values().length];
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
/* 139 */       int j = (arrayOfEnumType = values()).length; for (int i = 0; i < j; i++) { EnumType blockplanks$enumtype = arrayOfEnumType[i];
/*     */         
/* 141 */         META_LOOKUP[blockplanks$enumtype.getMetadata()] = blockplanks$enumtype;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockPlanks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */