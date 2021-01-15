/*    */ package net.minecraft.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.material.MapColor;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.Item;
/*    */ 
/*    */ public class BlockMelon extends Block
/*    */ {
/*    */   protected BlockMelon()
/*    */   {
/* 15 */     super(Material.gourd, MapColor.limeColor);
/* 16 */     setCreativeTab(CreativeTabs.tabBlock);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*    */   {
/* 24 */     return Items.melon;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int quantityDropped(Random random)
/*    */   {
/* 32 */     return 3 + random.nextInt(5);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int quantityDroppedWithBonus(int fortune, Random random)
/*    */   {
/* 40 */     return Math.min(9, quantityDropped(random) + random.nextInt(1 + fortune));
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockMelon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */