/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ import net.minecraft.realms.RealmsBridge;
/*    */ import rip.jutting.polaris.ui.newconfig.GuiAltManager;
/*    */ 
/*    */ public class GuiIngameMenu extends GuiScreen
/*    */ {
/*    */   private int field_146445_a;
/*    */   private int field_146444_f;
/*    */   
/*    */   public void initGui()
/*    */   {
/* 17 */     this.field_146445_a = 0;
/* 18 */     this.buttonList.clear();
/* 19 */     int i = -16;
/* 20 */     int j = 98;
/* 21 */     this.buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + i, I18n.format("menu.returnToMenu", new Object[0])));
/* 22 */     if (!this.mc.isIntegratedServerRunning()) {
/* 23 */       ((GuiButton)this.buttonList.get(0)).displayString = I18n.format("menu.disconnect", new Object[0]);
/*    */     }
/*    */     
/* 26 */     this.buttonList.add(new GuiButton(4, width / 2 - 100, height / 4 + 24 + i, I18n.format("menu.returnToGame", new Object[0])));
/* 27 */     this.buttonList.add(new GuiButton(69, width / 2 - 100, height / 4 + 72 + i, "Configs"));
/* 28 */     this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + i, 98, 20, I18n.format("menu.options", new Object[0])));
/*    */     GuiButton guibutton;
/* 30 */     this.buttonList.add(guibutton = new GuiButton(7, width / 2 + 2, height / 4 + 96 + i, 98, 20, I18n.format("menu.shareToLan", new Object[0])));
/* 31 */     this.buttonList.add(new GuiButton(5, width / 2 - 100, height / 4 + 48 + i, 98, 20, I18n.format("gui.achievements", new Object[0])));
/* 32 */     this.buttonList.add(new GuiButton(6, width / 2 + 2, height / 4 + 48 + i, 98, 20, I18n.format("gui.stats", new Object[0])));
/* 33 */     guibutton.enabled = ((this.mc.isSingleplayer()) && (!this.mc.getIntegratedServer().getPublic()));
/*    */   }
/*    */   
/*    */   protected void actionPerformed(GuiButton button) throws java.io.IOException {
/* 37 */     switch (button.id) {
/*    */     case 0: 
/* 39 */       this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
/* 40 */       break;
/*    */     case 1: 
/* 42 */       boolean flag = this.mc.isIntegratedServerRunning();
/* 43 */       boolean flag1 = this.mc.func_181540_al();
/* 44 */       button.enabled = false;
/* 45 */       this.mc.theWorld.sendQuittingDisconnectingPacket();
/* 46 */       this.mc.loadWorld(null);
/* 47 */       if (flag) {
/* 48 */         this.mc.displayGuiScreen(new GuiMainMenu());
/* 49 */       } else if (flag1) {
/* 50 */         RealmsBridge realmsbridge = new RealmsBridge();
/* 51 */         realmsbridge.switchToRealms(new GuiMainMenu());
/*    */       } else {
/* 53 */         this.mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
/*    */       }
/*    */       break;
/*    */     case 2: case 3: 
/*    */     default: 
/*    */       break;
/*    */     case 4: 
/* 60 */       this.mc.displayGuiScreen(null);
/* 61 */       this.mc.setIngameFocus();
/* 62 */       break;
/*    */     case 5: 
/* 64 */       this.mc.displayGuiScreen(new net.minecraft.client.gui.achievement.GuiAchievements(this, this.mc.thePlayer.getStatFileWriter()));
/* 65 */       break;
/*    */     case 6: 
/* 67 */       this.mc.displayGuiScreen(new net.minecraft.client.gui.achievement.GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
/* 68 */       break;
/*    */     case 7: 
/* 70 */       this.mc.displayGuiScreen(new GuiShareToLan(this));
/* 71 */       break;
/*    */     case 69: 
/* 73 */       this.mc.displayGuiScreen(new GuiAltManager());
/*    */     }
/*    */   }
/*    */   
/*    */   public void updateScreen() {
/* 78 */     super.updateScreen();
/* 79 */     this.field_146444_f += 1;
/*    */   }
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 83 */     drawDefaultBackground();
/* 84 */     drawCenteredString(this.fontRendererObj, I18n.format("menu.game", new Object[0]), width / 2, 40, 16777215);
/* 85 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiIngameMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */