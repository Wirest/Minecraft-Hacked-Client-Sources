/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class GuiErrorScreen extends GuiScreen
/*    */ {
/*    */   private String field_146313_a;
/*    */   private String field_146312_f;
/*    */   
/*    */   public GuiErrorScreen(String p_i46319_1_, String p_i46319_2_) {
/* 11 */     this.field_146313_a = p_i46319_1_;
/* 12 */     this.field_146312_f = p_i46319_2_;
/*    */   }
/*    */   
/*    */   public void initGui() {
/* 16 */     super.initGui();
/* 17 */     this.buttonList.add(new GuiButton(0, width / 2 - 100, 140, net.minecraft.client.resources.I18n.format("gui.cancel", new Object[0])));
/*    */   }
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 21 */     drawGradientRect(0, 0, width, height, -12574688, -11530224);
/* 22 */     drawCenteredString(this.fontRendererObj, this.field_146313_a, width / 2, 90, 16777215);
/* 23 */     drawCenteredString(this.fontRendererObj, this.field_146312_f, width / 2, 110, 16777215);
/* 24 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*    */   }
/*    */   
/*    */   protected void keyTyped(char typedChar, int keyCode) throws IOException
/*    */   {}
/*    */   
/*    */   protected void actionPerformed(GuiButton button) throws IOException {
/* 31 */     this.mc.displayGuiScreen(null);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiErrorScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */