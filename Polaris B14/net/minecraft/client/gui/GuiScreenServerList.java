/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ 
/*    */ public class GuiScreenServerList extends GuiScreen
/*    */ {
/*    */   private final GuiScreen field_146303_a;
/*    */   private final net.minecraft.client.multiplayer.ServerData field_146301_f;
/*    */   private GuiTextField field_146302_g;
/*    */   
/*    */   public GuiScreenServerList(GuiScreen p_i1031_1_, net.minecraft.client.multiplayer.ServerData p_i1031_2_)
/*    */   {
/* 14 */     this.field_146303_a = p_i1031_1_;
/* 15 */     this.field_146301_f = p_i1031_2_;
/*    */   }
/*    */   
/*    */   public void updateScreen() {
/* 19 */     this.field_146302_g.updateCursorCounter();
/*    */   }
/*    */   
/*    */   public void initGui() {
/* 23 */     org.lwjgl.input.Keyboard.enableRepeatEvents(true);
/* 24 */     this.buttonList.clear();
/* 25 */     this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + 12, I18n.format("selectServer.select", new Object[0])));
/* 26 */     this.buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + 12, I18n.format("gui.cancel", new Object[0])));
/* 27 */     this.field_146302_g = new GuiTextField(2, this.fontRendererObj, width / 2 - 100, 116, 200, 20);
/* 28 */     this.field_146302_g.setMaxStringLength(128);
/* 29 */     this.field_146302_g.setFocused(true);
/* 30 */     this.field_146302_g.setText(this.mc.gameSettings.lastServer);
/* 31 */     ((GuiButton)this.buttonList.get(0)).enabled = ((this.field_146302_g.getText().length() > 0) && (this.field_146302_g.getText().split(":").length > 0));
/*    */   }
/*    */   
/*    */   public void onGuiClosed() {
/* 35 */     org.lwjgl.input.Keyboard.enableRepeatEvents(false);
/* 36 */     this.mc.gameSettings.lastServer = this.field_146302_g.getText();
/* 37 */     this.mc.gameSettings.saveOptions();
/*    */   }
/*    */   
/*    */   protected void actionPerformed(GuiButton button) throws java.io.IOException {
/* 41 */     if (button.enabled) {
/* 42 */       if (button.id == 1) {
/* 43 */         this.field_146303_a.confirmClicked(false, 0);
/* 44 */       } else if (button.id == 0) {
/* 45 */         this.field_146301_f.serverIP = this.field_146302_g.getText();
/* 46 */         this.field_146303_a.confirmClicked(true, 0);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   protected void keyTyped(char typedChar, int keyCode) throws java.io.IOException {
/* 52 */     if (this.field_146302_g.textboxKeyTyped(typedChar, keyCode)) {
/* 53 */       ((GuiButton)this.buttonList.get(0)).enabled = ((this.field_146302_g.getText().length() > 0) && (this.field_146302_g.getText().split(":").length > 0));
/* 54 */     } else if ((keyCode == 28) || (keyCode == 156)) {
/* 55 */       actionPerformed((GuiButton)this.buttonList.get(0));
/*    */     }
/*    */   }
/*    */   
/*    */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws java.io.IOException {
/* 60 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/* 61 */     this.field_146302_g.mouseClicked(mouseX, mouseY, mouseButton);
/*    */   }
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 65 */     drawDefaultBackground();
/* 66 */     drawCenteredString(this.fontRendererObj, I18n.format("selectServer.direct", new Object[0]), width / 2, 20, 16777215);
/* 67 */     drawString(this.fontRendererObj, I18n.format("addServer.enterIp", new Object[0]), width / 2 - 100, 100, 10526880);
/* 68 */     this.field_146302_g.drawTextBox();
/* 69 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiScreenServerList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */