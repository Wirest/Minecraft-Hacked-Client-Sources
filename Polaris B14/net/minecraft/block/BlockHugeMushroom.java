/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.IStringSerializable;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockHugeMushroom extends Block
/*     */ {
/*  19 */   public static final PropertyEnum<EnumType> VARIANT = PropertyEnum.create("variant", EnumType.class);
/*     */   private final Block smallBlock;
/*     */   
/*     */   public BlockHugeMushroom(Material p_i46392_1_, MapColor p_i46392_2_, Block p_i46392_3_)
/*     */   {
/*  24 */     super(p_i46392_1_, p_i46392_2_);
/*  25 */     setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, EnumType.ALL_OUTSIDE));
/*  26 */     this.smallBlock = p_i46392_3_;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int quantityDropped(Random random)
/*     */   {
/*  34 */     return Math.max(0, random.nextInt(10) - 7);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MapColor getMapColor(IBlockState state)
/*     */   {
/*  42 */     switch ((EnumType)state.getValue(VARIANT))
/*     */     {
/*     */     case WEST: 
/*  45 */       return MapColor.clothColor;
/*     */     
/*     */     case SOUTH_WEST: 
/*  48 */       return MapColor.sandColor;
/*     */     
/*     */     case SOUTH_EAST: 
/*  51 */       return MapColor.sandColor;
/*     */     }
/*     */     
/*  54 */     return super.getMapColor(state);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*     */   {
/*  63 */     return Item.getItemFromBlock(this.smallBlock);
/*     */   }
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos)
/*     */   {
/*  68 */     return Item.getItemFromBlock(this.smallBlock);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
/*     */   {
/*  77 */     return getDefaultState();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/*  85 */     return getDefaultState().withProperty(VARIANT, EnumType.byMetadata(meta));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/*  93 */     return ((EnumType)state.getValue(VARIANT)).getMetadata();
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/*  98 */     return new BlockState(this, new IProperty[] { VARIANT });
/*     */   }
/*     */   
/*     */   public static enum EnumType implements IStringSerializable
/*     */   {
/* 103 */     NORTH_WEST(1, "north_west"), 
/* 104 */     NORTH(2, "north"), 
/* 105 */     NORTH_EAST(3, "north_east"), 
/* 106 */     WEST(4, "west"), 
/* 107 */     CENTER(5, "center"), 
/* 108 */     EAST(6, "east"), 
/* 109 */     SOUTH_WEST(7, "south_west"), 
/* 110 */     SOUTH(8, "south"), 
/* 111 */     SOUTH_EAST(9, "south_east"), 
/* 112 */     STEM(10, "stem"), 
/* 113 */     ALL_INSIDE(0, "all_inside"), 
/* 114 */     ALL_OUTSIDE(14, "all_outside"), 
/* 115 */     ALL_STEM(15, "all_stem");
/*     */     
/*     */     private static final EnumType[] META_LOOKUP;
/*     */     private final int meta;
/*     */     private final String name;
/*     */     
/*     */     private EnumType(int meta, String name)
/*     */     {
/* 123 */       this.meta = meta;
/* 124 */       this.name = name;
/*     */     }
/*     */     
/*     */     public int getMetadata()
/*     */     {
/* 129 */       return this.meta;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 134 */       return this.name;
/*     */     }
/*     */     
/*     */     public static EnumType byMetadata(int meta)
/*     */     {
/* 139 */       if ((meta < 0) || (meta >= META_LOOKUP.length))
/*     */       {
/* 141 */         meta = 0;
/*     */       }
/*     */       
/* 144 */       EnumType blockhugemushroom$enumtype = META_LOOKUP[meta];
/* 145 */       return blockhugemushroom$enumtype == null ? META_LOOKUP[0] : blockhugemushroom$enumtype;
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/* 150 */       return this.name;
/*     */     }
/*     */     
/*     */     static
/*     */     {
/* 117 */       META_LOOKUP = new EnumType[16];
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/* 154 */       int j = (arrayOfEnumType = values()).length; for (int i = 0; i < j; i++) { EnumType blockhugemushroom$enumtype = arrayOfEnumType[i];
/*     */         
/* 156 */         META_LOOKUP[blockhugemushroom$enumtype.getMetadata()] = blockhugemushroom$enumtype;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockHugeMushroom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */