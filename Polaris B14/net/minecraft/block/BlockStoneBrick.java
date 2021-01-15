/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
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
/*     */ public class BlockStoneBrick extends Block
/*     */ {
/*  16 */   public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
/*  17 */   public static final int DEFAULT_META = EnumType.DEFAULT.getMetadata();
/*  18 */   public static final int MOSSY_META = EnumType.MOSSY.getMetadata();
/*  19 */   public static final int CRACKED_META = EnumType.CRACKED.getMetadata();
/*  20 */   public static final int CHISELED_META = EnumType.CHISELED.getMetadata();
/*     */   
/*     */   public BlockStoneBrick()
/*     */   {
/*  24 */     super(Material.rock);
/*  25 */     setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumType.DEFAULT));
/*  26 */     setCreativeTab(CreativeTabs.tabBlock);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int damageDropped(IBlockState state)
/*     */   {
/*  35 */     return ((EnumType)state.getValue(VARIANT)).getMetadata();
/*     */   }
/*     */   
/*     */ 
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
/*     */   {
/*     */     EnumType[] arrayOfEnumType;
/*     */     
/*  43 */     int j = (arrayOfEnumType = EnumType.values()).length; for (int i = 0; i < j; i++) { EnumType blockstonebrick$enumtype = arrayOfEnumType[i];
/*     */       
/*  45 */       list.add(new ItemStack(itemIn, 1, blockstonebrick$enumtype.getMetadata()));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/*  54 */     return getDefaultState().withProperty(VARIANT, EnumType.byMetadata(meta));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/*  62 */     return ((EnumType)state.getValue(VARIANT)).getMetadata();
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/*  67 */     return new BlockState(this, new IProperty[] { VARIANT });
/*     */   }
/*     */   
/*     */   public static enum EnumType implements IStringSerializable
/*     */   {
/*  72 */     DEFAULT(0, "stonebrick", "default"), 
/*  73 */     MOSSY(1, "mossy_stonebrick", "mossy"), 
/*  74 */     CRACKED(2, "cracked_stonebrick", "cracked"), 
/*  75 */     CHISELED(3, "chiseled_stonebrick", "chiseled");
/*     */     
/*     */     private static final EnumType[] META_LOOKUP;
/*     */     private final int meta;
/*     */     private final String name;
/*     */     private final String unlocalizedName;
/*     */     
/*     */     private EnumType(int meta, String name, String unlocalizedName)
/*     */     {
/*  84 */       this.meta = meta;
/*  85 */       this.name = name;
/*  86 */       this.unlocalizedName = unlocalizedName;
/*     */     }
/*     */     
/*     */     public int getMetadata()
/*     */     {
/*  91 */       return this.meta;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/*  96 */       return this.name;
/*     */     }
/*     */     
/*     */     public static EnumType byMetadata(int meta)
/*     */     {
/* 101 */       if ((meta < 0) || (meta >= META_LOOKUP.length))
/*     */       {
/* 103 */         meta = 0;
/*     */       }
/*     */       
/* 106 */       return META_LOOKUP[meta];
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 111 */       return this.name;
/*     */     }
/*     */     
/*     */     public String getUnlocalizedName()
/*     */     {
/* 116 */       return this.unlocalizedName;
/*     */     }
/*     */     
/*     */     static
/*     */     {
/*  77 */       META_LOOKUP = new EnumType[values().length];
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
/* 120 */       int j = (arrayOfEnumType = values()).length; for (int i = 0; i < j; i++) { EnumType blockstonebrick$enumtype = arrayOfEnumType[i];
/*     */         
/* 122 */         META_LOOKUP[blockstonebrick$enumtype.getMetadata()] = blockstonebrick$enumtype;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockStoneBrick.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */