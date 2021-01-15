/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ 
/*    */ public class GuiDisconnected extends GuiScreen
/*    */ {
/*    */   private String reason;
/*    */   private IChatComponent message;
/*    */   private List<String> multilineMessage;
/*    */   private final GuiScreen parentScreen;
/*    */   private int field_175353_i;
/*    */   
/*    */   public GuiDisconnected(GuiScreen screen, String reasonLocalizationKey, IChatComponent chatComp)
/*    */   {
/* 16 */     this.parentScreen = screen;
/* 17 */     this.reason = net.minecraft.client.resources.I18n.format(reasonLocalizationKey, new Object[0]);
/* 18 */     this.message = chatComp;
/*    */   }
/*    */   
/*    */   protected void keyTyped(char typedChar, int keyCode) throws java.io.IOException
/*    */   {}
/*    */   
/*    */   public void initGui() {
/* 25 */     this.buttonList.clear();
/* 26 */     this.multilineMessage = this.fontRendererObj.listFormattedStringToWidth(this.message.getFormattedText(), width - 50);
/* 27 */     this.field_175353_i = (this.multilineMessage.size() * this.fontRendererObj.FONT_HEIGHT);
/* 28 */     this.buttonList.add(new GuiButton(0, width / 2 - 100, height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT, net.minecraft.client.resources.I18n.format("gui.toMenu", new Object[0])));
/*    */   }
/*    */   
/*    */   protected void actionPerformed(GuiButton button) throws java.io.IOException {
/* 32 */     if (button.id == 0) {
/* 33 */       this.mc.displayGuiScreen(this.parentScreen);
/*    */     }
/*    */   }
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 38 */     drawDefaultBackground();
/* 39 */     drawCenteredString(this.fontRendererObj, this.reason, width / 2, height / 2 - this.field_175353_i / 2 - this.fontRendererObj.FONT_HEIGHT * 2, 11184810);
/* 40 */     int i = height / 2 - this.field_175353_i / 2;
/* 41 */     if (this.multilineMessage != null) {
/* 42 */       for (String s : this.multilineMessage) {
/* 43 */         drawCenteredString(this.fontRendererObj, s, width / 2, i, 16777215);
/* 44 */         i += this.fontRendererObj.FONT_HEIGHT;
/*    */       }
/*    */     }
/*    */     
/* 48 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiDisconnected.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */