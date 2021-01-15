/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import net.minecraft.client.network.NetHandlerPlayClient;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ 
/*    */ public class GuiDownloadTerrain extends GuiScreen
/*    */ {
/*    */   private NetHandlerPlayClient netHandlerPlayClient;
/*    */   private int progress;
/*    */   
/*    */   public GuiDownloadTerrain(NetHandlerPlayClient netHandler)
/*    */   {
/* 13 */     this.netHandlerPlayClient = netHandler;
/*    */   }
/*    */   
/*    */   protected void keyTyped(char typedChar, int keyCode) throws java.io.IOException
/*    */   {}
/*    */   
/*    */   public void initGui() {
/* 20 */     this.buttonList.clear();
/*    */   }
/*    */   
/*    */   public void updateScreen() {
/* 24 */     this.progress += 1;
/* 25 */     if (this.progress % 20 == 0) {
/* 26 */       this.netHandlerPlayClient.addToSendQueue(new net.minecraft.network.play.client.C00PacketKeepAlive());
/*    */     }
/*    */   }
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 31 */     drawBackground(0);
/* 32 */     drawCenteredString(this.fontRendererObj, I18n.format("multiplayer.downloadingTerrain", new Object[0]), width / 2, height / 2 - 50, 16777215);
/* 33 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*    */   }
/*    */   
/*    */   public boolean doesGuiPauseGame() {
/* 37 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiDownloadTerrain.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */