/*    */ package net.minecraft.inventory;
/*    */ 
/*    */ import net.minecraft.util.IChatComponent;
/*    */ 
/*    */ public class AnimalChest extends InventoryBasic
/*    */ {
/*    */   public AnimalChest(String inventoryName, int slotCount)
/*    */   {
/*  9 */     super(inventoryName, false, slotCount);
/*    */   }
/*    */   
/*    */   public AnimalChest(IChatComponent invTitle, int slotCount)
/*    */   {
/* 14 */     super(invTitle, slotCount);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\inventory\AnimalChest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */