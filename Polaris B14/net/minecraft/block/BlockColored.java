/*    */ package net.minecraft.block;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.block.material.MapColor;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.properties.IProperty;
/*    */ import net.minecraft.block.properties.PropertyEnum;
/*    */ import net.minecraft.block.state.BlockState;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.item.EnumDyeColor;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ 
/*    */ public class BlockColored extends Block
/*    */ {
/* 17 */   public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.create("color", EnumDyeColor.class);
/*    */   
/*    */   public BlockColored(Material materialIn)
/*    */   {
/* 21 */     super(materialIn);
/* 22 */     setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));
/* 23 */     setCreativeTab(CreativeTabs.tabBlock);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int damageDropped(IBlockState state)
/*    */   {
/* 32 */     return ((EnumDyeColor)state.getValue(COLOR)).getMetadata();
/*    */   }
/*    */   
/*    */ 
/*    */   public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list)
/*    */   {
/*    */     EnumDyeColor[] arrayOfEnumDyeColor;
/*    */     
/* 40 */     int j = (arrayOfEnumDyeColor = EnumDyeColor.values()).length; for (int i = 0; i < j; i++) { EnumDyeColor enumdyecolor = arrayOfEnumDyeColor[i];
/*    */       
/* 42 */       list.add(new ItemStack(itemIn, 1, enumdyecolor.getMetadata()));
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public MapColor getMapColor(IBlockState state)
/*    */   {
/* 51 */     return ((EnumDyeColor)state.getValue(COLOR)).getMapColor();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public IBlockState getStateFromMeta(int meta)
/*    */   {
/* 59 */     return getDefaultState().withProperty(COLOR, EnumDyeColor.byMetadata(meta));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getMetaFromState(IBlockState state)
/*    */   {
/* 67 */     return ((EnumDyeColor)state.getValue(COLOR)).getMetadata();
/*    */   }
/*    */   
/*    */   protected BlockState createBlockState()
/*    */   {
/* 72 */     return new BlockState(this, new IProperty[] { COLOR });
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockColored.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */