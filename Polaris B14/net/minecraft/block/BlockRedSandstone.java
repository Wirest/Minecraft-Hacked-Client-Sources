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
/*     */ public class BlockRedSandstone extends Block
/*     */ {
/*  16 */   public static final PropertyEnum<EnumType> TYPE = PropertyEnum.create("type", EnumType.class);
/*     */   
/*     */   public BlockRedSandstone()
/*     */   {
/*  20 */     super(Material.rock, BlockSand.EnumType.RED_SAND.getMapColor());
/*  21 */     setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumType.DEFAULT));
/*  22 */     setCreativeTab(CreativeTabs.tabBlock);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int damageDropped(IBlockState state)
/*     */   {
/*  31 */     return ((EnumType)state.getValue(TYPE)).getMetadata();
/*     */   }
/*     */   
/*     */ 
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
/*     */   {
/*     */     EnumType[] arrayOfEnumType;
/*     */     
/*  39 */     int j = (arrayOfEnumType = EnumType.values()).length; for (int i = 0; i < j; i++) { EnumType blockredsandstone$enumtype = arrayOfEnumType[i];
/*     */       
/*  41 */       list.add(new ItemStack(itemIn, 1, blockredsandstone$enumtype.getMetadata()));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/*  50 */     return getDefaultState().withProperty(TYPE, EnumType.byMetadata(meta));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/*  58 */     return ((EnumType)state.getValue(TYPE)).getMetadata();
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/*  63 */     return new BlockState(this, new IProperty[] { TYPE });
/*     */   }
/*     */   
/*     */   public static enum EnumType implements IStringSerializable
/*     */   {
/*  68 */     DEFAULT(0, "red_sandstone", "default"), 
/*  69 */     CHISELED(1, "chiseled_red_sandstone", "chiseled"), 
/*  70 */     SMOOTH(2, "smooth_red_sandstone", "smooth");
/*     */     
/*     */     private static final EnumType[] META_LOOKUP;
/*     */     private final int meta;
/*     */     private final String name;
/*     */     private final String unlocalizedName;
/*     */     
/*     */     private EnumType(int meta, String name, String unlocalizedName)
/*     */     {
/*  79 */       this.meta = meta;
/*  80 */       this.name = name;
/*  81 */       this.unlocalizedName = unlocalizedName;
/*     */     }
/*     */     
/*     */     public int getMetadata()
/*     */     {
/*  86 */       return this.meta;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/*  91 */       return this.name;
/*     */     }
/*     */     
/*     */     public static EnumType byMetadata(int meta)
/*     */     {
/*  96 */       if ((meta < 0) || (meta >= META_LOOKUP.length))
/*     */       {
/*  98 */         meta = 0;
/*     */       }
/*     */       
/* 101 */       return META_LOOKUP[meta];
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 106 */       return this.name;
/*     */     }
/*     */     
/*     */     public String getUnlocalizedName()
/*     */     {
/* 111 */       return this.unlocalizedName;
/*     */     }
/*     */     
/*     */     static
/*     */     {
/*  72 */       META_LOOKUP = new EnumType[values().length];
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
/* 115 */       int j = (arrayOfEnumType = values()).length; for (int i = 0; i < j; i++) { EnumType blockredsandstone$enumtype = arrayOfEnumType[i];
/*     */         
/* 117 */         META_LOOKUP[blockredsandstone$enumtype.getMetadata()] = blockredsandstone$enumtype;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockRedSandstone.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */