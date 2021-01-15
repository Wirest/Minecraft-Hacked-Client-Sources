/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.properties.PropertyEnum;
/*     */ import net.minecraft.block.state.BlockState;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ 
/*     */ public class BlockOldLog extends BlockLog
/*     */ {
/*  16 */   public static final PropertyEnum<BlockPlanks.EnumType> VARIANT = PropertyEnum.create("variant", BlockPlanks.EnumType.class, new Predicate()
/*     */   {
/*     */     public boolean apply(BlockPlanks.EnumType p_apply_1_)
/*     */     {
/*  20 */       return p_apply_1_.getMetadata() < 4;
/*     */     }
/*  16 */   });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BlockOldLog()
/*     */   {
/*  26 */     setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, BlockPlanks.EnumType.OAK).withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public MapColor getMapColor(IBlockState state)
/*     */   {
/*  34 */     BlockPlanks.EnumType blockplanks$enumtype = (BlockPlanks.EnumType)state.getValue(VARIANT);
/*     */     
/*  36 */     switch ((BlockLog.EnumAxis)state.getValue(LOG_AXIS))
/*     */     {
/*     */     case NONE: 
/*     */     case Y: 
/*     */     case Z: 
/*     */     default: 
/*  42 */       switch (blockplanks$enumtype)
/*     */       {
/*     */       case ACACIA: 
/*     */       default: 
/*  46 */         return BlockPlanks.EnumType.SPRUCE.func_181070_c();
/*     */       
/*     */       case BIRCH: 
/*  49 */         return BlockPlanks.EnumType.DARK_OAK.func_181070_c();
/*     */       
/*     */       case DARK_OAK: 
/*  52 */         return MapColor.quartzColor;
/*     */       }
/*     */       
/*  55 */       return BlockPlanks.EnumType.SPRUCE.func_181070_c();
/*     */     }
/*     */     
/*     */     
/*  59 */     return blockplanks$enumtype.func_181070_c();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
/*     */   {
/*  68 */     list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.OAK.getMetadata()));
/*  69 */     list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()));
/*  70 */     list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.BIRCH.getMetadata()));
/*  71 */     list.add(new ItemStack(itemIn, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IBlockState getStateFromMeta(int meta)
/*     */   {
/*  79 */     IBlockState iblockstate = getDefaultState().withProperty(VARIANT, BlockPlanks.EnumType.byMetadata((meta & 0x3) % 4));
/*     */     
/*  81 */     switch (meta & 0xC)
/*     */     {
/*     */     case 0: 
/*  84 */       iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.Y);
/*  85 */       break;
/*     */     
/*     */     case 4: 
/*  88 */       iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.X);
/*  89 */       break;
/*     */     
/*     */     case 8: 
/*  92 */       iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.Z);
/*  93 */       break;
/*     */     
/*     */     default: 
/*  96 */       iblockstate = iblockstate.withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE);
/*     */     }
/*     */     
/*  99 */     return iblockstate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMetaFromState(IBlockState state)
/*     */   {
/* 109 */     int i = 0;
/* 110 */     i |= ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata();
/*     */     
/* 112 */     switch ((BlockLog.EnumAxis)state.getValue(LOG_AXIS))
/*     */     {
/*     */     case NONE: 
/* 115 */       i |= 0x4;
/* 116 */       break;
/*     */     
/*     */     case Y: 
/* 119 */       i |= 0x8;
/* 120 */       break;
/*     */     
/*     */     case Z: 
/* 123 */       i |= 0xC;
/*     */     }
/*     */     
/* 126 */     return i;
/*     */   }
/*     */   
/*     */   protected BlockState createBlockState()
/*     */   {
/* 131 */     return new BlockState(this, new IProperty[] { VARIANT, LOG_AXIS });
/*     */   }
/*     */   
/*     */   protected ItemStack createStackedBlock(IBlockState state)
/*     */   {
/* 136 */     return new ItemStack(Item.getItemFromBlock(this), 1, ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int damageDropped(IBlockState state)
/*     */   {
/* 145 */     return ((BlockPlanks.EnumType)state.getValue(VARIANT)).getMetadata();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockOldLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */