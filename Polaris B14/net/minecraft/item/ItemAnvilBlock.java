/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ 
/*    */ public class ItemAnvilBlock extends ItemMultiTexture
/*    */ {
/*    */   public ItemAnvilBlock(Block block)
/*    */   {
/*  9 */     super(block, block, new String[] { "intact", "slightlyDamaged", "veryDamaged" });
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getMetadata(int damage)
/*    */   {
/* 18 */     return damage << 2;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemAnvilBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */