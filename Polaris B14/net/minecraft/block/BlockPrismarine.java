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
/*     */ import net.minecraft.util.StatCollector;
/*     */ 
/*     */ public class BlockPrismarine extends Block
/*     */ {
/*  18 */   public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
/*  19 */   public static final int ROUGH_META = EnumType.ROUGH.getMetadata();
/*  20 */   public static final int BRICKS_META = EnumType.BRICKS.getMetadata();
/*  21 */   public static final int DARK_META = EnumType.DARK.getMetadata();
/*     */   
/*     */   public BlockPrismarine()
/*     */   {
/*  25 */     super(Material.rock);
/*  26 */     setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumType.ROUGH));
/*  27 */     setCreativeTab(CreativeTabs.tabBlock);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getLocalizedName()
/*     */   {
/*  35 */     return StatCollector.translateToLocal(getUnlocalizedName() + "." + EnumType.ROUGH.getUnlocalizedName() + ".name");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MapColor getMapColor(IBlockState state)
/*     */   {
/*  43 */     return state.getValue(VARIANT) == EnumType.ROUGH ? MapColor.cyanColor : MapColor.diamondColor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int damageDropped(IBlockState state)
/*     */   {
/*  52 */     return ((EnumType)state.getValue(VARIANT)).getMetadata();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/*  60 */     return ((EnumType)state.getValue(VARIANT)).getMetadata();
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/*  65 */     return new BlockState(this, new IProperty[] { VARIANT });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/*  73 */     return getDefaultState().withProperty(VARIANT, EnumType.byMetadata(meta));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
/*     */   {
/*  81 */     list.add(new ItemStack(itemIn, 1, ROUGH_META));
/*  82 */     list.add(new ItemStack(itemIn, 1, BRICKS_META));
/*  83 */     list.add(new ItemStack(itemIn, 1, DARK_META));
/*     */   }
/*     */   
/*     */   public static enum EnumType implements IStringSerializable
/*     */   {
/*  88 */     ROUGH(0, "prismarine", "rough"), 
/*  89 */     BRICKS(1, "prismarine_bricks", "bricks"), 
/*  90 */     DARK(2, "dark_prismarine", "dark");
/*     */     
/*     */     private static final EnumType[] META_LOOKUP;
/*     */     private final int meta;
/*     */     private final String name;
/*     */     private final String unlocalizedName;
/*     */     
/*     */     private EnumType(int meta, String name, String unlocalizedName)
/*     */     {
/*  99 */       this.meta = meta;
/* 100 */       this.name = name;
/* 101 */       this.unlocalizedName = unlocalizedName;
/*     */     }
/*     */     
/*     */     public int getMetadata()
/*     */     {
/* 106 */       return this.meta;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 111 */       return this.name;
/*     */     }
/*     */     
/*     */     public static EnumType byMetadata(int meta)
/*     */     {
/* 116 */       if ((meta < 0) || (meta >= META_LOOKUP.length))
/*     */       {
/* 118 */         meta = 0;
/*     */       }
/*     */       
/* 121 */       return META_LOOKUP[meta];
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 126 */       return this.name;
/*     */     }
/*     */     
/*     */     public String getUnlocalizedName()
/*     */     {
/* 131 */       return this.unlocalizedName;
/*     */     }
/*     */     
/*     */     static
/*     */     {
/*  92 */       META_LOOKUP = new EnumType[values().length];
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
/* 135 */       int j = (arrayOfEnumType = values()).length; for (int i = 0; i < j; i++) { EnumType blockprismarine$enumtype = arrayOfEnumType[i];
/*     */         
/* 137 */         META_LOOKUP[blockprismarine$enumtype.getMetadata()] = blockprismarine$enumtype;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockPrismarine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */