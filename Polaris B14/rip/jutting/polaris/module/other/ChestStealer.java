/*    */ package rip.jutting.polaris.module.other;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.inventory.GuiChest;
/*    */ 
/*    */ public class ChestStealer extends rip.jutting.polaris.module.Module
/*    */ {
/*    */   public static net.minecraft.network.play.server.S30PacketWindowItems packet;
/*    */   int delay;
/*    */   
/*    */   public ChestStealer()
/*    */   {
/* 13 */     super("ChestStealer", 0, rip.jutting.polaris.module.Category.OTHER);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   @rip.jutting.polaris.event.EventTarget
/*    */   public void onUpdate(rip.jutting.polaris.event.events.EventUpdate event)
/*    */   {
/* 21 */     this.delay += 1;
/* 22 */     if ((mc.currentScreen instanceof GuiChest)) {
/* 23 */       GuiChest chest = (GuiChest)mc.currentScreen;
/* 24 */       if (isChestEmpty(chest)) {
/* 25 */         mc.thePlayer.closeScreen();
/* 26 */         packet = null;
/*    */       }
/* 28 */       for (int index = 0; index < chest.lowerChestInventory.getSizeInventory(); index++) {
/* 29 */         net.minecraft.item.ItemStack stack = chest.lowerChestInventory.getStackInSlot(index);
/* 30 */         if ((stack != null) && (this.delay > 2) && (!stack.hasDisplayName())) {
/* 31 */           mc.playerController.windowClick(chest.inventorySlots.windowId, index, 0, 1, mc.thePlayer);
/* 32 */           this.delay = 0;
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   private boolean isChestEmpty(GuiChest chest) {
/* 39 */     for (int index = 0; index <= chest.lowerChestInventory.getSizeInventory(); index++) {
/* 40 */       net.minecraft.item.ItemStack stack = chest.lowerChestInventory.getStackInSlot(index);
/* 41 */       if (stack != null) {
/* 42 */         return false;
/*    */       }
/*    */     }
/* 45 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\other\ChestStealer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */