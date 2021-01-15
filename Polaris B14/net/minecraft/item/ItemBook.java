/*    */ package net.minecraft.item;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ItemBook
/*    */   extends Item
/*    */ {
/*    */   public boolean isItemTool(ItemStack stack)
/*    */   {
/* 10 */     return stack.stackSize == 1;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getItemEnchantability()
/*    */   {
/* 18 */     return 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\item\ItemBook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */