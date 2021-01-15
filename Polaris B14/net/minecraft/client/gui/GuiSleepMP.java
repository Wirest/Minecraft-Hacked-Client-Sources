/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ 
/*    */ public class GuiSleepMP extends GuiChat
/*    */ {
/*    */   public void initGui()
/*    */   {
/* 10 */     super.initGui();
/* 11 */     this.buttonList.add(new GuiButton(1, width / 2 - 100, height - 40, net.minecraft.client.resources.I18n.format("multiplayer.stopSleeping", new Object[0])));
/*    */   }
/*    */   
/*    */   protected void keyTyped(char typedChar, int keyCode) throws java.io.IOException {
/* 15 */     if (keyCode == 1) {
/* 16 */       wakeFromSleep();
/* 17 */     } else if ((keyCode != 28) && (keyCode != 156)) {
/* 18 */       super.keyTyped(typedChar, keyCode);
/*    */     } else {
/* 20 */       String s = this.inputField.getText().trim();
/* 21 */       if (!s.isEmpty()) {
/* 22 */         this.mc.thePlayer.sendChatMessage(s);
/*    */       }
/*    */       
/* 25 */       this.inputField.setText("");
/* 26 */       this.mc.ingameGUI.getChatGUI().resetScroll();
/*    */     }
/*    */   }
/*    */   
/*    */   protected void actionPerformed(GuiButton button) throws java.io.IOException {
/* 31 */     if (button.id == 1) {
/* 32 */       wakeFromSleep();
/*    */     } else {
/* 34 */       super.actionPerformed(button);
/*    */     }
/*    */   }
/*    */   
/*    */   private void wakeFromSleep() {
/* 39 */     net.minecraft.client.network.NetHandlerPlayClient nethandlerplayclient = this.mc.thePlayer.sendQueue;
/* 40 */     nethandlerplayclient.addToSendQueue(new net.minecraft.network.play.client.C0BPacketEntityAction(this.mc.thePlayer, net.minecraft.network.play.client.C0BPacketEntityAction.Action.STOP_SLEEPING));
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiSleepMP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */