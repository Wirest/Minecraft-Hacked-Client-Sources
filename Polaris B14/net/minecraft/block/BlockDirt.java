/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.List;
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
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockDirt extends Block
/*     */ {
/*  22 */   public static final PropertyEnum<DirtType> VARIANT = PropertyEnum.create("variant", DirtType.class);
/*  23 */   public static final PropertyBool SNOWY = PropertyBool.create("snowy");
/*     */   
/*     */   protected BlockDirt()
/*     */   {
/*  27 */     super(Material.ground);
/*  28 */     setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, DirtType.DIRT).withProperty(SNOWY, Boolean.valueOf(false)));
/*  29 */     setCreativeTab(CreativeTabs.tabBlock);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MapColor getMapColor(IBlockState state)
/*     */   {
/*  37 */     return ((DirtType)state.getValue(VARIANT)).func_181066_d();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
/*     */   {
/*  46 */     if (state.getValue(VARIANT) == DirtType.PODZOL)
/*     */     {
/*  48 */       Block block = worldIn.getBlockState(pos.up()).getBlock();
/*  49 */       state = state.withProperty(SNOWY, Boolean.valueOf((block == Blocks.snow) || (block == Blocks.snow_layer)));
/*     */     }
/*     */     
/*  52 */     return state;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
/*     */   {
/*  60 */     list.add(new ItemStack(this, 1, DirtType.DIRT.getMetadata()));
/*  61 */     list.add(new ItemStack(this, 1, DirtType.COARSE_DIRT.getMetadata()));
/*  62 */     list.add(new ItemStack(this, 1, DirtType.PODZOL.getMetadata()));
/*     */   }
/*     */   
/*     */   public int getDamageValue(World worldIn, BlockPos pos)
/*     */   {
/*  67 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*  68 */     return iblockstate.getBlock() != this ? 0 : ((DirtType)iblockstate.getValue(VARIANT)).getMetadata();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/*  76 */     return getDefaultState().withProperty(VARIANT, DirtType.byMetadata(meta));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/*  84 */     return ((DirtType)state.getValue(VARIANT)).getMetadata();
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/*  89 */     return new BlockState(this, new IProperty[] { VARIANT, SNOWY });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int damageDropped(IBlockState state)
/*     */   {
/*  98 */     DirtType blockdirt$dirttype = (DirtType)state.getValue(VARIANT);
/*     */     
/* 100 */     if (blockdirt$dirttype == DirtType.PODZOL)
/*     */     {
/* 102 */       blockdirt$dirttype = DirtType.DIRT;
/*     */     }
/*     */     
/* 105 */     return blockdirt$dirttype.getMetadata();
/*     */   }
/*     */   
/*     */   public static enum DirtType implements IStringSerializable
/*     */   {
/* 110 */     DIRT(0, "dirt", "default", MapColor.dirtColor), 
/* 111 */     COARSE_DIRT(1, "coarse_dirt", "coarse", MapColor.dirtColor), 
/* 112 */     PODZOL(2, "podzol", MapColor.obsidianColor);
/*     */     
/*     */     private static final DirtType[] METADATA_LOOKUP;
/*     */     private final int metadata;
/*     */     private final String name;
/*     */     private final String unlocalizedName;
/*     */     private final MapColor field_181067_h;
/*     */     
/*     */     private DirtType(int p_i46396_3_, String p_i46396_4_, MapColor p_i46396_5_)
/*     */     {
/* 122 */       this(p_i46396_3_, p_i46396_4_, p_i46396_4_, p_i46396_5_);
/*     */     }
/*     */     
/*     */     private DirtType(int p_i46397_3_, String p_i46397_4_, String p_i46397_5_, MapColor p_i46397_6_)
/*     */     {
/* 127 */       this.metadata = p_i46397_3_;
/* 128 */       this.name = p_i46397_4_;
/* 129 */       this.unlocalizedName = p_i46397_5_;
/* 130 */       this.field_181067_h = p_i46397_6_;
/*     */     }
/*     */     
/*     */     public int getMetadata()
/*     */     {
/* 135 */       return this.metadata;
/*     */     }
/*     */     
/*     */     public String getUnlocalizedName()
/*     */     {
/* 140 */       return this.unlocalizedName;
/*     */     }
/*     */     
/*     */     public MapColor func_181066_d()
/*     */     {
/* 145 */       return this.field_181067_h;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 150 */       return this.name;
/*     */     }
/*     */     
/*     */     public static DirtType byMetadata(int metadata)
/*     */     {
/* 155 */       if ((metadata < 0) || (metadata >= METADATA_LOOKUP.length))
/*     */       {
/* 157 */         metadata = 0;
/*     */       }
/*     */       
/* 160 */       return METADATA_LOOKUP[metadata];
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 165 */       return this.name;
/*     */     }
/*     */     
/*     */     static
/*     */     {
/* 114 */       METADATA_LOOKUP = new DirtType[values().length];
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       DirtType[] arrayOfDirtType;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 169 */       int j = (arrayOfDirtType = values()).length; for (int i = 0; i < j; i++) { DirtType blockdirt$dirttype = arrayOfDirtType[i];
/*     */         
/* 171 */         METADATA_LOOKUP[blockdirt$dirttype.getMetadata()] = blockdirt$dirttype;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockDirt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */