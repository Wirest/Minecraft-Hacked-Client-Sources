/*     */ package rip.jutting.polaris.ui.click.clickgui.elements.menu;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.ui.click.clickgui.ClickGUI;
/*     */ import rip.jutting.polaris.ui.click.clickgui.elements.Element;
/*     */ import rip.jutting.polaris.ui.click.clickgui.elements.ModuleButton;
/*     */ import rip.jutting.polaris.ui.click.settings.Setting;
/*     */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*     */ import rip.jutting.polaris.ui.font.FontManager;
/*     */ import rip.jutting.polaris.ui.font.MinecraftFontRenderer;
/*     */ import rip.jutting.polaris.ui.fonth.CFontRenderer;
/*     */ import rip.jutting.polaris.ui.fonth.FontLoaders;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ElementComboBox
/*     */   extends Element
/*     */ {
/*     */   public ElementComboBox(ModuleButton iparent, Setting iset)
/*     */   {
/*  26 */     this.parent = iparent;
/*  27 */     this.set = iset;
/*  28 */     super.setup();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*     */   {
/*  36 */     CFontRenderer font = FontLoaders.vardana12;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  42 */     Gui.drawRect(this.x, this.y, this.x + this.width + 1.0D, this.y + this.height, new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/*  43 */       (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/*  44 */       (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble(), 150).getRGB());
/*  45 */     Gui.drawRect(this.x, this.y, this.x + this.width + 1.0D, this.y + this.height, new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/*  46 */       (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/*  47 */       (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble(), 150).getRGB());
/*     */     
/*  49 */     Polaris.instance.fontManager.arraylist.drawCenteredStringWithShadow(this.setstrg, (float)this.x + (float)this.width / 2.0F, (float)this.y + 5.0F, -1);
/*  50 */     int clr1 = new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/*  51 */       (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/*  52 */       (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble()).getRGB();
/*  53 */     int clr2 = new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/*  54 */       (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/*  55 */       (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble()).getRGB();
/*  56 */     if (this.comboextended) {
/*  57 */       Gui.drawRect(this.x, this.y + 15.0D, this.x + this.width, this.y + this.height, new Color(0, 0, 0, 100).getRGB());
/*  58 */       Gui.drawRect(this.x, this.y + 15.0D, this.x + this.width, this.y + this.height, new Color(0, 0, 0, 100).getRGB());
/*  59 */       double ay = this.y + 15.0D;
/*  60 */       for (String sld : this.set.getOptions()) {
/*  61 */         String elementtitle = sld.substring(0, 1).toUpperCase() + sld.substring(1, sld.length());
/*  62 */         Polaris.instance.fontManager.arraylist.drawCenteredString(elementtitle, (float)this.x + (float)this.width / 2.0F, (float)ay + 2.0F, -1);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  68 */         if (sld.equalsIgnoreCase(this.set.getValString())) {
/*  69 */           Gui.drawRect(this.x, ay, this.x + 1.5D, ay + font.getHeight() + 2.0D, new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/*  70 */             (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/*  71 */             (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble()).brighter().getRGB());
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*  77 */         if ((mouseX >= this.x) && (mouseX <= this.x + this.width) && (mouseY >= ay) && (mouseY < ay + font.getHeight() + 2.0D)) {
/*  78 */           Gui.drawRect(this.x + this.width - 1.2D, ay, this.x + this.width, ay + font.getHeight() + 2.0D, new Color(0, 0, 0, 100).getRGB());
/*     */         }
/*  80 */         ay += font.getHeight() + 2;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean mouseClicked(int mouseX, int mouseY, int mouseButton)
/*     */   {
/*  90 */     CFontRenderer font = FontLoaders.vardana12;
/*  91 */     if (mouseButton == 0) {
/*  92 */       if (isButtonHovered(mouseX, mouseY)) {
/*  93 */         this.comboextended = (!this.comboextended);
/*     */         
/*  95 */         return true;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 103 */       if (!this.comboextended) return false;
/* 104 */       double ay = this.y + 15.0D;
/* 105 */       for (String slcd : this.set.getOptions()) {
/* 106 */         if ((mouseX >= this.x) && (mouseX <= this.x + this.width) && (mouseY >= ay) && (mouseY <= ay + font.getHeight() + 2.0D)) {
/* 107 */           if (Polaris.instance.settingsManager.getSettingByName("Sound").getValBoolean()) {
/* 108 */             Minecraft.getMinecraft().thePlayer.playSound("tile.piston.in", 20.0F, 20.0F);
/*     */           }
/* 110 */           if ((this.clickgui != null) && (this.clickgui.setmgr != null))
/* 111 */             this.clickgui.setmgr.getSettingByName(this.set.getName()).setValString(slcd.toLowerCase());
/* 112 */           return true;
/*     */         }
/* 114 */         ay += font.getHeight() + 2;
/*     */       }
/*     */     }
/*     */     
/* 118 */     return super.mouseClicked(mouseX, mouseY, mouseButton);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isButtonHovered(int mouseX, int mouseY)
/*     */   {
/* 125 */     return (mouseX >= this.x) && (mouseX <= this.x + this.width) && (mouseY >= this.y) && (mouseY <= this.y + 15.0D);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\click\clickgui\elements\menu\ElementComboBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */