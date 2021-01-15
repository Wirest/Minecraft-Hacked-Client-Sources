/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import java.net.URI;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ import net.minecraft.client.settings.GameSettings;
/*    */ import net.minecraft.client.settings.KeyBinding;
/*    */ import org.apache.logging.log4j.Logger;
/*    */ 
/*    */ public class GuiScreenDemo extends GuiScreen
/*    */ {
/* 13 */   private static final Logger logger = ;
/* 14 */   private static final net.minecraft.util.ResourceLocation field_146348_f = new net.minecraft.util.ResourceLocation("textures/gui/demo_background.png");
/*    */   
/*    */   public void initGui() {
/* 17 */     this.buttonList.clear();
/* 18 */     int i = -16;
/* 19 */     this.buttonList.add(new GuiButton(1, width / 2 - 116, height / 2 + 62 + i, 114, 20, I18n.format("demo.help.buy", new Object[0])));
/* 20 */     this.buttonList.add(new GuiButton(2, width / 2 + 2, height / 2 + 62 + i, 114, 20, I18n.format("demo.help.later", new Object[0])));
/*    */   }
/*    */   
/*    */   protected void actionPerformed(GuiButton button) throws java.io.IOException {
/* 24 */     switch (button.id) {
/*    */     case 1: 
/* 26 */       button.enabled = false;
/*    */       try
/*    */       {
/* 29 */         Class<?> oclass = Class.forName("java.awt.Desktop");
/* 30 */         Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
/* 31 */         oclass.getMethod("browse", new Class[] { URI.class }).invoke(object, new Object[] { new URI("http://www.minecraft.net/store?source=demo") });
/*    */       } catch (Throwable throwable) {
/* 33 */         logger.error("Couldn't open link", throwable);
/*    */       }
/*    */     
/*    */     case 2: 
/* 37 */       this.mc.displayGuiScreen(null);
/* 38 */       this.mc.setIngameFocus();
/*    */     }
/*    */   }
/*    */   
/*    */   public void updateScreen() {
/* 43 */     super.updateScreen();
/*    */   }
/*    */   
/*    */   public void drawDefaultBackground() {
/* 47 */     super.drawDefaultBackground();
/* 48 */     net.minecraft.client.renderer.GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 49 */     this.mc.getTextureManager().bindTexture(field_146348_f);
/* 50 */     int i = (width - 248) / 2;
/* 51 */     int j = (height - 166) / 2;
/* 52 */     drawTexturedModalRect(i, j, 0, 0, 248, 166);
/*    */   }
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 56 */     drawDefaultBackground();
/* 57 */     int i = (width - 248) / 2 + 10;
/* 58 */     int j = (height - 166) / 2 + 8;
/* 59 */     this.fontRendererObj.drawString(I18n.format("demo.help.title", new Object[0]), i, j, 2039583);
/* 60 */     j += 12;
/* 61 */     GameSettings gamesettings = this.mc.gameSettings;
/* 62 */     this.fontRendererObj.drawString(I18n.format("demo.help.movementShort", new Object[] { GameSettings.getKeyDisplayString(gamesettings.keyBindForward.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindLeft.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindBack.getKeyCode()), GameSettings.getKeyDisplayString(gamesettings.keyBindRight.getKeyCode()) }), i, j, 5197647);
/* 63 */     this.fontRendererObj.drawString(I18n.format("demo.help.movementMouse", new Object[0]), i, j + 12, 5197647);
/* 64 */     this.fontRendererObj.drawString(I18n.format("demo.help.jump", new Object[] { GameSettings.getKeyDisplayString(gamesettings.keyBindJump.getKeyCode()) }), i, j + 24, 5197647);
/* 65 */     this.fontRendererObj.drawString(I18n.format("demo.help.inventory", new Object[] { GameSettings.getKeyDisplayString(gamesettings.keyBindInventory.getKeyCode()) }), i, j + 36, 5197647);
/* 66 */     this.fontRendererObj.drawSplitString(I18n.format("demo.help.fullWrapped", new Object[0]), i, j + 68, 218, 2039583);
/* 67 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\gui\GuiScreenDemo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */