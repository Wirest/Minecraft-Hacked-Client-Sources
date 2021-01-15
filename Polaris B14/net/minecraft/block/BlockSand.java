/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ 
/*     */ public class BlockSand extends BlockFalling
/*     */ {
/*  16 */   public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
/*     */   
/*     */   public BlockSand()
/*     */   {
/*  20 */     setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumType.SAND));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int damageDropped(IBlockState state)
/*     */   {
/*  29 */     return ((EnumType)state.getValue(VARIANT)).getMetadata();
/*     */   }
/*     */   
/*     */ 
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
/*     */   {
/*     */     EnumType[] arrayOfEnumType;
/*     */     
/*  37 */     int j = (arrayOfEnumType = EnumType.values()).length; for (int i = 0; i < j; i++) { EnumType blocksand$enumtype = arrayOfEnumType[i];
/*     */       
/*  39 */       list.add(new ItemStack(itemIn, 1, blocksand$enumtype.getMetadata()));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MapColor getMapColor(IBlockState state)
/*     */   {
/*  48 */     return ((EnumType)state.getValue(VARIANT)).getMapColor();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/*  56 */     return getDefaultState().withProperty(VARIANT, EnumType.byMetadata(meta));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/*  64 */     return ((EnumType)state.getValue(VARIANT)).getMetadata();
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/*  69 */     return new BlockState(this, new IProperty[] { VARIANT });
/*     */   }
/*     */   
/*     */   public static enum EnumType implements IStringSerializable
/*     */   {
/*  74 */     SAND(0, "sand", "default", MapColor.sandColor), 
/*  75 */     RED_SAND(1, "red_sand", "red", MapColor.adobeColor);
/*     */     
/*     */     private static final EnumType[] META_LOOKUP;
/*     */     private final int meta;
/*     */     private final String name;
/*     */     private final MapColor mapColor;
/*     */     private final String unlocalizedName;
/*     */     
/*     */     private EnumType(int meta, String name, String unlocalizedName, MapColor mapColor)
/*     */     {
/*  85 */       this.meta = meta;
/*  86 */       this.name = name;
/*  87 */       this.mapColor = mapColor;
/*  88 */       this.unlocalizedName = unlocalizedName;
/*     */     }
/*     */     
/*     */     public int getMetadata()
/*     */     {
/*  93 */       return this.meta;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/*  98 */       return this.name;
/*     */     }
/*     */     
/*     */     public MapColor getMapColor()
/*     */     {
/* 103 */       return this.mapColor;
/*     */     }
/*     */     
/*     */     public static EnumType byMetadata(int meta)
/*     */     {
/* 108 */       if ((meta < 0) || (meta >= META_LOOKUP.length))
/*     */       {
/* 110 */         meta = 0;
/*     */       }
/*     */       
/* 113 */       return META_LOOKUP[meta];
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 118 */       return this.name;
/*     */     }
/*     */     
/*     */     public String getUnlocalizedName()
/*     */     {
/* 123 */       return this.unlocalizedName;
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
/* 127 */       int j = (arrayOfEnumType = values()).length; for (int i = 0; i < j; i++) { EnumType blocksand$enumtype = arrayOfEnumType[i];
/*     */         
/* 129 */         META_LOOKUP[blocksand$enumtype.getMetadata()] = blocksand$enumtype;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockSand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */