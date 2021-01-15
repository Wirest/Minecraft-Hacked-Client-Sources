/*   */ package net.minecraft.dispenser;
/*   */ 
/*   */ import net.minecraft.item.ItemStack;
/*   */ 
/*   */ public abstract interface IBehaviorDispenseItem
/*   */ {
/* 7 */   public static final IBehaviorDispenseItem itemDispenseBehaviorProvider = new IBehaviorDispenseItem()
/*   */   {
/*   */     public ItemStack dispense(IBlockSource source, ItemStack stack)
/*   */     {
/* ; */       return stack;
/*   */     }
/*   */   };
/*   */   
/*   */   public abstract ItemStack dispense(IBlockSource paramIBlockSource, ItemStack paramItemStack);
/*   */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\dispenser\IBehaviorDispenseItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */