/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ 
/*    */ 
/*    */ public class GuiMemoryErrorScreen
/*    */   extends GuiScreen
/*    */ {
/*    */   public void initGui()
/*    */   {
/* 14 */     this.buttonList.clear();
/* 15 */     this.buttonList.add(new GuiOptionButton(0, width / 2 - 155, height / 4 + 120 + 12, I18n.format("gui.toTitle", new Object[0])));
/* 16 */     this.buttonList.add(new GuiOptionButton(1, width / 2 - 155 + 160, height / 4 + 120 + 12, I18n.format("menu.quit", new Object[0])));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected void actionPerformed(GuiButton button)
/*    */     throws IOException
/*    */   {
/* 24 */     if (button.id == 0)
/*    */     {
/* 26 */       this.mc.displayGuiScreen(new GuiMainMenu());
/*    */     }
/* 28 */     else if (button.id == 1)
/*    */     {
/* 30 */       this.mc.shutdown();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void keyTyped(char typedChar, int keyCode)
/*    */     throws IOException
/*    */   {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*    */   {
/* 47 */     drawDefaultBackground();
/* 48 */     drawCenteredString(this.fontRendererObj, "Out of memory!", width / 2, height / 4 - 60 + 20, 16777215);
/* 49 */     drawString(this.fontRendererObj, "Minecraft has run out of memory.", width / 2 - 140, height / 4 - 60 + 60 + 0, 10526880);
/* 50 */     drawString(this.fontRendererObj, "This could be caused by a bug in the game or by the", width / 2 - 140, height / 4 - 60 + 60 + 18, 10526880);
/* 51 */     drawString(this.fontRendererObj, "Java Virtual Machine not being allocated enough", width / 2 - 140, height / 4 - 60 + 60 + 27, 10526880);
/* 52 */     drawString(this.fontRendererObj, "memory.", width / 2 - 140, height / 4 - 60 + 60 + 36, 10526880);
/* 53 */     drawString(this.fontRendererObj, "To prevent level corruption, the current game has quit.", width / 2 - 140, height / 4 - 60 + 60 + 54, 10526880);
/* 54 */     drawString(this.fontRendererObj, "We've tried to free up enough memory to let you go back to", width / 2 - 140, height / 4 - 60 + 60 + 63, 10526880);
/* 55 */     drawString(this.fontRendererObj, "the main menu and back to playing, but this may not have worked.", width / 2 - 140, height / 4 - 60 + 60 + 72, 10526880);
/* 56 */     drawString(this.fontRendererObj, "Please restart the game if you see this message again.", width / 2 - 140, height / 4 - 60 + 60 + 81, 10526880);
/* 57 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiMemoryErrorScreen.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */