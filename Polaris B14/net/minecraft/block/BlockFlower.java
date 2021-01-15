/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.collect.Collections2;
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ 
/*     */ public abstract class BlockFlower extends BlockBush
/*     */ {
/*     */   protected PropertyEnum<EnumFlowerType> type;
/*     */   
/*     */   protected BlockFlower()
/*     */   {
/*  24 */     setDefaultState(this.blockState.getBaseState().withProperty(getTypeProperty(), getBlockType() == EnumFlowerColor.RED ? EnumFlowerType.POPPY : EnumFlowerType.DANDELION));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int damageDropped(IBlockState state)
/*     */   {
/*  33 */     return ((EnumFlowerType)state.getValue(getTypeProperty())).getMeta();
/*     */   }
/*     */   
/*     */ 
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
/*     */   {
/*     */     EnumFlowerType[] arrayOfEnumFlowerType;
/*     */     
/*  41 */     int j = (arrayOfEnumFlowerType = EnumFlowerType.getTypes(getBlockType())).length; for (int i = 0; i < j; i++) { EnumFlowerType blockflower$enumflowertype = arrayOfEnumFlowerType[i];
/*     */       
/*  43 */       list.add(new ItemStack(itemIn, 1, blockflower$enumflowertype.getMeta()));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/*  52 */     return getDefaultState().withProperty(getTypeProperty(), EnumFlowerType.getType(getBlockType(), meta));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract EnumFlowerColor getBlockType();
/*     */   
/*     */ 
/*     */   public IProperty<EnumFlowerType> getTypeProperty()
/*     */   {
/*  62 */     if (this.type == null)
/*     */     {
/*  64 */       this.type = PropertyEnum.create("type", EnumFlowerType.class, new Predicate()
/*     */       {
/*     */         public boolean apply(BlockFlower.EnumFlowerType p_apply_1_)
/*     */         {
/*  68 */           return p_apply_1_.getBlockType() == BlockFlower.this.getBlockType();
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*  73 */     return this.type;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/*  81 */     return ((EnumFlowerType)state.getValue(getTypeProperty())).getMeta();
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/*  86 */     return new BlockState(this, new IProperty[] { getTypeProperty() });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Block.EnumOffsetType getOffsetType()
/*     */   {
/*  94 */     return Block.EnumOffsetType.XZ;
/*     */   }
/*     */   
/*     */   public static enum EnumFlowerColor
/*     */   {
/*  99 */     YELLOW, 
/* 100 */     RED;
/*     */     
/*     */     public BlockFlower getBlock()
/*     */     {
/* 104 */       return this == YELLOW ? Blocks.yellow_flower : Blocks.red_flower;
/*     */     }
/*     */   }
/*     */   
/*     */   public static enum EnumFlowerType implements IStringSerializable
/*     */   {
/* 110 */     DANDELION(BlockFlower.EnumFlowerColor.YELLOW, 0, "dandelion"), 
/* 111 */     POPPY(BlockFlower.EnumFlowerColor.RED, 0, "poppy"), 
/* 112 */     BLUE_ORCHID(BlockFlower.EnumFlowerColor.RED, 1, "blue_orchid", "blueOrchid"), 
/* 113 */     ALLIUM(BlockFlower.EnumFlowerColor.RED, 2, "allium"), 
/* 114 */     HOUSTONIA(BlockFlower.EnumFlowerColor.RED, 3, "houstonia"), 
/* 115 */     RED_TULIP(BlockFlower.EnumFlowerColor.RED, 4, "red_tulip", "tulipRed"), 
/* 116 */     ORANGE_TULIP(BlockFlower.EnumFlowerColor.RED, 5, "orange_tulip", "tulipOrange"), 
/* 117 */     WHITE_TULIP(BlockFlower.EnumFlowerColor.RED, 6, "white_tulip", "tulipWhite"), 
/* 118 */     PINK_TULIP(BlockFlower.EnumFlowerColor.RED, 7, "pink_tulip", "tulipPink"), 
/* 119 */     OXEYE_DAISY(BlockFlower.EnumFlowerColor.RED, 8, "oxeye_daisy", "oxeyeDaisy");
/*     */     
/*     */     private static final EnumFlowerType[][] TYPES_FOR_BLOCK;
/*     */     private final BlockFlower.EnumFlowerColor blockType;
/*     */     private final int meta;
/*     */     private final String name;
/*     */     private final String unlocalizedName;
/*     */     
/*     */     private EnumFlowerType(BlockFlower.EnumFlowerColor blockType, int meta, String name)
/*     */     {
/* 129 */       this(blockType, meta, name, name);
/*     */     }
/*     */     
/*     */     private EnumFlowerType(BlockFlower.EnumFlowerColor blockType, int meta, String name, String unlocalizedName)
/*     */     {
/* 134 */       this.blockType = blockType;
/* 135 */       this.meta = meta;
/* 136 */       this.name = name;
/* 137 */       this.unlocalizedName = unlocalizedName;
/*     */     }
/*     */     
/*     */     public BlockFlower.EnumFlowerColor getBlockType()
/*     */     {
/* 142 */       return this.blockType;
/*     */     }
/*     */     
/*     */     public int getMeta()
/*     */     {
/* 147 */       return this.meta;
/*     */     }
/*     */     
/*     */     public static EnumFlowerType getType(BlockFlower.EnumFlowerColor blockType, int meta)
/*     */     {
/* 152 */       EnumFlowerType[] ablockflower$enumflowertype = TYPES_FOR_BLOCK[blockType.ordinal()];
/*     */       
/* 154 */       if ((meta < 0) || (meta >= ablockflower$enumflowertype.length))
/*     */       {
/* 156 */         meta = 0;
/*     */       }
/*     */       
/* 159 */       return ablockflower$enumflowertype[meta];
/*     */     }
/*     */     
/*     */     public static EnumFlowerType[] getTypes(BlockFlower.EnumFlowerColor flowerColor)
/*     */     {
/* 164 */       return TYPES_FOR_BLOCK[flowerColor.ordinal()];
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 169 */       return this.name;
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 174 */       return this.name;
/*     */     }
/*     */     
/*     */     public String getUnlocalizedName()
/*     */     {
/* 179 */       return this.unlocalizedName;
/*     */     }
/*     */     
/*     */     static
/*     */     {
/* 121 */       TYPES_FOR_BLOCK = new EnumFlowerType[BlockFlower.EnumFlowerColor.values().length][];
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
/*     */ 
/*     */ 
/*     */ 
/*     */       BlockFlower.EnumFlowerColor[] arrayOfEnumFlowerColor;
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
/*     */ 
/*     */ 
/*     */ 
/* 183 */       int j = (arrayOfEnumFlowerColor = BlockFlower.EnumFlowerColor.values()).length; for (int i = 0; i < j; i++) { BlockFlower.EnumFlowerColor blockflower$enumflowercolor = arrayOfEnumFlowerColor[i];
/*     */         
/* 185 */         Collection<EnumFlowerType> collection = Collections2.filter(Lists.newArrayList(values()), new Predicate()
/*     */         {
/*     */           public boolean apply(BlockFlower.EnumFlowerType p_apply_1_)
/*     */           {
/* 189 */             return p_apply_1_.getBlockType() == BlockFlower.EnumFlowerType.this;
/*     */           }
/* 191 */         });
/* 192 */         TYPES_FOR_BLOCK[blockflower$enumflowercolor.ordinal()] = ((EnumFlowerType[])collection.toArray(new EnumFlowerType[collection.size()]));
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockFlower.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */