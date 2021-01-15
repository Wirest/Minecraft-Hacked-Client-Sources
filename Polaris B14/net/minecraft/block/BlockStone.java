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
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ import net.minecraft.util.StatCollector;
/*     */ 
/*     */ public class BlockStone extends Block
/*     */ {
/*  20 */   public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
/*     */   
/*     */   public BlockStone()
/*     */   {
/*  24 */     super(Material.rock);
/*  25 */     setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumType.STONE));
/*  26 */     setCreativeTab(CreativeTabs.tabBlock);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getLocalizedName()
/*     */   {
/*  34 */     return StatCollector.translateToLocal(getUnlocalizedName() + "." + EnumType.STONE.getUnlocalizedName() + ".name");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MapColor getMapColor(IBlockState state)
/*     */   {
/*  42 */     return ((EnumType)state.getValue(VARIANT)).func_181072_c();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*     */   {
/*  50 */     return state.getValue(VARIANT) == EnumType.STONE ? Item.getItemFromBlock(Blocks.cobblestone) : Item.getItemFromBlock(Blocks.stone);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int damageDropped(IBlockState state)
/*     */   {
/*  59 */     return ((EnumType)state.getValue(VARIANT)).getMetadata();
/*     */   }
/*     */   
/*     */ 
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
/*     */   {
/*     */     EnumType[] arrayOfEnumType;
/*     */     
/*  67 */     int j = (arrayOfEnumType = EnumType.values()).length; for (int i = 0; i < j; i++) { EnumType blockstone$enumtype = arrayOfEnumType[i];
/*     */       
/*  69 */       list.add(new ItemStack(itemIn, 1, blockstone$enumtype.getMetadata()));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/*  78 */     return getDefaultState().withProperty(VARIANT, EnumType.byMetadata(meta));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/*  86 */     return ((EnumType)state.getValue(VARIANT)).getMetadata();
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/*  91 */     return new BlockState(this, new IProperty[] { VARIANT });
/*     */   }
/*     */   
/*     */   public static enum EnumType implements IStringSerializable
/*     */   {
/*  96 */     STONE(0, MapColor.stoneColor, "stone"), 
/*  97 */     GRANITE(1, MapColor.dirtColor, "granite"), 
/*  98 */     GRANITE_SMOOTH(2, MapColor.dirtColor, "smooth_granite", "graniteSmooth"), 
/*  99 */     DIORITE(3, MapColor.quartzColor, "diorite"), 
/* 100 */     DIORITE_SMOOTH(4, MapColor.quartzColor, "smooth_diorite", "dioriteSmooth"), 
/* 101 */     ANDESITE(5, MapColor.stoneColor, "andesite"), 
/* 102 */     ANDESITE_SMOOTH(6, MapColor.stoneColor, "smooth_andesite", "andesiteSmooth");
/*     */     
/*     */     private static final EnumType[] META_LOOKUP;
/*     */     private final int meta;
/*     */     private final String name;
/*     */     private final String unlocalizedName;
/*     */     private final MapColor field_181073_l;
/*     */     
/*     */     private EnumType(int p_i46383_3_, MapColor p_i46383_4_, String p_i46383_5_)
/*     */     {
/* 112 */       this(p_i46383_3_, p_i46383_4_, p_i46383_5_, p_i46383_5_);
/*     */     }
/*     */     
/*     */     private EnumType(int p_i46384_3_, MapColor p_i46384_4_, String p_i46384_5_, String p_i46384_6_)
/*     */     {
/* 117 */       this.meta = p_i46384_3_;
/* 118 */       this.name = p_i46384_5_;
/* 119 */       this.unlocalizedName = p_i46384_6_;
/* 120 */       this.field_181073_l = p_i46384_4_;
/*     */     }
/*     */     
/*     */     public int getMetadata()
/*     */     {
/* 125 */       return this.meta;
/*     */     }
/*     */     
/*     */     public MapColor func_181072_c()
/*     */     {
/* 130 */       return this.field_181073_l;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 135 */       return this.name;
/*     */     }
/*     */     
/*     */     public static EnumType byMetadata(int meta)
/*     */     {
/* 140 */       if ((meta < 0) || (meta >= META_LOOKUP.length))
/*     */       {
/* 142 */         meta = 0;
/*     */       }
/*     */       
/* 145 */       return META_LOOKUP[meta];
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 150 */       return this.name;
/*     */     }
/*     */     
/*     */     public String getUnlocalizedName()
/*     */     {
/* 155 */       return this.unlocalizedName;
/*     */     }
/*     */     
/*     */     static
/*     */     {
/* 104 */       META_LOOKUP = new EnumType[values().length];
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
/* 159 */       int j = (arrayOfEnumType = values()).length; for (int i = 0; i < j; i++) { EnumType blockstone$enumtype = arrayOfEnumType[i];
/*     */         
/* 161 */         META_LOOKUP[blockstone$enumtype.getMetadata()] = blockstone$enumtype;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockStone.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */