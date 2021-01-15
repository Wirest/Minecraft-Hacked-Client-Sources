/*    */ package net.minecraft.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.material.MapColor;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.Item;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BlockGravel
/*    */   extends BlockFalling
/*    */ {
/*    */   public Item getItemDropped(IBlockState state, Random rand, int fortune)
/*    */   {
/* 16 */     if (fortune > 3)
/*    */     {
/* 18 */       fortune = 3;
/*    */     }
/*    */     
/* 21 */     return rand.nextInt(10 - fortune * 3) == 0 ? Items.flint : Item.getItemFromBlock(this);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public MapColor getMapColor(IBlockState state)
/*    */   {
/* 29 */     return MapColor.stoneColor;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\BlockGravel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */