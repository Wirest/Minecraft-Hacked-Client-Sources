/*     */ package rip.jutting.polaris.ui.click.clickgui.elements.menu;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.ui.click.clickgui.elements.Element;
/*     */ import rip.jutting.polaris.ui.click.clickgui.elements.ModuleButton;
/*     */ import rip.jutting.polaris.ui.click.settings.Setting;
/*     */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*     */ import rip.jutting.polaris.ui.fonth.CFontRenderer;
/*     */ import rip.jutting.polaris.ui.fonth.FontLoaders;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ElementSlider
/*     */   extends Element
/*     */ {
/*     */   public boolean dragging;
/*     */   
/*     */   public ElementSlider(ModuleButton iparent, Setting iset)
/*     */   {
/*  28 */     this.parent = iparent;
/*  29 */     this.set = iset;
/*  30 */     this.dragging = false;
/*  31 */     super.setup();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*     */   {
/*  38 */     CFontRenderer font = FontLoaders.vardana12;
/*  39 */     String displayval = Math.round(this.set.getValDouble() * 100.0D) / 100.0D;
/*  40 */     boolean hoveredORdragged = (isSliderHovered(mouseX, mouseY)) || (this.dragging);
/*  41 */     int color = new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/*  42 */       (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/*  43 */       (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble(), hoveredORdragged ? 250 : 200).getRGB();
/*  44 */     int color2 = new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/*  45 */       (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/*  46 */       (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble(), hoveredORdragged ? 255 : 230).getRGB();
/*     */     
/*     */ 
/*  49 */     double percentBar = (this.set.getValDouble() - this.set.getMin()) / (this.set.getMax() - this.set.getMin());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  54 */     Gui.drawRect(this.x, this.y, this.x + this.width + 1.0D, this.y + this.height, -1156246251);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  59 */     font.drawString(this.setstrg, this.x + 1.0D, this.y + 2.0D, -1, false);
/*  60 */     font.drawString(displayval, this.x + this.width - font.getStringWidth(displayval), this.y + 2.0D, -1, false);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  65 */     Gui.drawRect(this.x, this.y + 12.0D, this.x + this.width, this.y + 13.5D, -15724528);
/*  66 */     Gui.drawRect(this.x, this.y + 12.0D, this.x + percentBar * this.width, this.y + 13.5D, color);
/*     */     
/*  68 */     if ((percentBar > 0.0D) && (percentBar < 1.0D)) {
/*  69 */       Gui.drawRect(this.x + percentBar * this.width - 1.0D, this.y + 12.0D, this.x + Math.min(percentBar * this.width, this.width), this.y + 13.5D, color2);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  75 */     if (this.dragging) {
/*  76 */       double diff = this.set.getMax() - this.set.getMin();
/*  77 */       double val = this.set.getMin() + MathHelper.clamp_double((mouseX - this.x) / this.width, 0.0D, 1.0D) * diff;
/*  78 */       this.set.setValDouble(val);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean mouseClicked(int mouseX, int mouseY, int mouseButton)
/*     */   {
/*  88 */     if ((mouseButton == 0) && (isSliderHovered(mouseX, mouseY))) {
/*  89 */       this.dragging = true;
/*  90 */       return true;
/*     */     }
/*     */     
/*  93 */     return super.mouseClicked(mouseX, mouseY, mouseButton);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void mouseReleased(int mouseX, int mouseY, int state)
/*     */   {
/* 100 */     this.dragging = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isSliderHovered(int mouseX, int mouseY)
/*     */   {
/* 107 */     return (mouseX >= this.x) && (mouseX <= this.x + this.width) && (mouseY >= this.y + 11.0D) && (mouseY <= this.y + 14.0D);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\click\clickgui\elements\menu\ElementSlider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */