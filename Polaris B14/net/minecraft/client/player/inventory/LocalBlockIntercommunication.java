/*    */ package net.minecraft.client.player.inventory;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.InventoryPlayer;
/*    */ import net.minecraft.inventory.Container;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ import net.minecraft.world.IInteractionObject;
/*    */ 
/*    */ public class LocalBlockIntercommunication implements IInteractionObject
/*    */ {
/*    */   private String guiID;
/*    */   private IChatComponent displayName;
/*    */   
/*    */   public LocalBlockIntercommunication(String guiIdIn, IChatComponent displayNameIn)
/*    */   {
/* 16 */     this.guiID = guiIdIn;
/* 17 */     this.displayName = displayNameIn;
/*    */   }
/*    */   
/*    */   public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn)
/*    */   {
/* 22 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getName()
/*    */   {
/* 30 */     return this.displayName.getUnformattedText();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean hasCustomName()
/*    */   {
/* 38 */     return true;
/*    */   }
/*    */   
/*    */   public String getGuiID()
/*    */   {
/* 43 */     return this.guiID;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public IChatComponent getDisplayName()
/*    */   {
/* 51 */     return this.displayName;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\player\inventory\LocalBlockIntercommunication.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */