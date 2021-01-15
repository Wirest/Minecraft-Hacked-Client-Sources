/*    */ package rip.jutting.polaris.ui.click.clickgui.elements.menu;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import net.minecraft.client.gui.Gui;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.ui.click.clickgui.elements.Element;
/*    */ import rip.jutting.polaris.ui.click.clickgui.elements.ModuleButton;
/*    */ import rip.jutting.polaris.ui.click.settings.Setting;
/*    */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*    */ import rip.jutting.polaris.ui.fonth.CFontRenderer;
/*    */ import rip.jutting.polaris.ui.fonth.FontLoaders;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ElementCheckBox
/*    */   extends Element
/*    */ {
/*    */   public ElementCheckBox(ModuleButton iparent, Setting iset)
/*    */   {
/* 25 */     this.parent = iparent;
/* 26 */     this.set = iset;
/* 27 */     super.setup();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*    */   {
/* 34 */     CFontRenderer font = FontLoaders.vardana12;
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 39 */     Gui.drawRect(this.x, this.y, this.x + this.width + 1.0D, this.y + this.height, -1156246251);
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 44 */     font.drawString(this.setstrg, this.x + this.width - font.getStringWidth(this.setstrg), this.y + font.getHeight() / 1.3D - 0.5D, -1, false);
/* 45 */     Gui.drawRect(this.x + 1.0D, this.y + 2.0D, this.x + 12.0D, this.y + 13.0D, this.set.getValBoolean() ? new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 46 */       (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 47 */       (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble()).getRGB() : new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 48 */       (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 49 */       (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble(), 50).brighter().getRGB());
/* 50 */     if (isCheckHovered(mouseX, mouseY)) {
/* 51 */       Gui.drawRect(this.x + 1.0D, this.y + 2.0D, this.x + 12.0D, this.y + 13.0D, new Color(0, 0, 0, 100).getRGB());
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean mouseClicked(int mouseX, int mouseY, int mouseButton)
/*    */   {
/* 60 */     if ((mouseButton == 0) && (isCheckHovered(mouseX, mouseY))) {
/* 61 */       this.set.setValBoolean(!this.set.getValBoolean());
/* 62 */       return true;
/*    */     }
/*    */     
/* 65 */     return super.mouseClicked(mouseX, mouseY, mouseButton);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean isCheckHovered(int mouseX, int mouseY)
/*    */   {
/* 72 */     return (mouseX >= this.x + 1.0D) && (mouseX <= this.x + 12.0D) && (mouseY >= this.y + 2.0D) && (mouseY <= this.y + 13.0D);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\click\clickgui\elements\menu\ElementCheckBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */