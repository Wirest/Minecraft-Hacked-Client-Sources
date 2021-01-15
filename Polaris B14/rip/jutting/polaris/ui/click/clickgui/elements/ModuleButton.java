/*     */ package rip.jutting.polaris.ui.click.clickgui.elements;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.module.Module;
/*     */ import rip.jutting.polaris.ui.click.clickgui.ClickGUI;
/*     */ import rip.jutting.polaris.ui.click.clickgui.Panel;
/*     */ import rip.jutting.polaris.ui.click.clickgui.elements.menu.ElementCheckBox;
/*     */ import rip.jutting.polaris.ui.click.clickgui.elements.menu.ElementComboBox;
/*     */ import rip.jutting.polaris.ui.click.clickgui.elements.menu.ElementSlider;
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
/*     */ public class ModuleButton
/*     */ {
/*     */   public Module mod;
/*     */   public ArrayList<Element> menuelements;
/*     */   public Panel parent;
/*     */   public double x;
/*     */   public double y;
/*     */   public double width;
/*     */   public double height;
/*  37 */   public boolean extended = false;
/*  38 */   public boolean listening = false;
/*     */   
/*     */   protected double hoverTransition;
/*     */   
/*     */ 
/*     */   public ModuleButton(Module imod, Panel pl)
/*     */   {
/*  45 */     CFontRenderer font = FontLoaders.vardana12;
/*  46 */     this.mod = imod;
/*  47 */     this.height = (font.getHeight() + 6.5D);
/*  48 */     this.parent = pl;
/*  49 */     this.menuelements = new ArrayList();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  58 */     if (Polaris.instance.settingsManager.getSettingsByMod(imod) != null) {
/*  59 */       for (Setting s : Polaris.instance.settingsManager.getSettingsByMod(imod)) {
/*  60 */         if (s.isCheck()) {
/*  61 */           this.menuelements.add(new ElementCheckBox(this, s));
/*  62 */         } else if (s.isSlider()) {
/*  63 */           this.menuelements.add(new ElementSlider(this, s));
/*  64 */         } else if (s.isCombo()) {
/*  65 */           this.menuelements.add(new ElementComboBox(this, s));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*     */   {
/*  75 */     CFontRenderer font = FontLoaders.vardana12;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  81 */     int textcolor = -5263441;
/*  82 */     if (this.mod.isToggled()) {
/*  83 */       Gui.drawRect(this.x - 2.0D, this.y, this.x + this.width + 2.0D, this.y + this.height + 1.0D, new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/*  84 */         (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/*  85 */         (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble(), 70).darker().getRGB());
/*  86 */       textcolor = -1052689;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  92 */     if (isHovered(mouseX, mouseY)) {
/*  93 */       this.hoverTransition += (2.0D - this.hoverTransition) / 5.0D;
/*  94 */       Gui.drawRect(this.x - 2.0D + this.hoverTransition, this.y + this.hoverTransition, this.x + this.width + 2.0D - this.hoverTransition, this.y + this.height + 1.0D - this.hoverTransition, new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/*  95 */         (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/*  96 */         (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble(), 100).brighter().getRGB());
/*  97 */     } else if (!isHovered(mouseX, mouseY)) {
/*  98 */       this.hoverTransition += (0.0D - this.hoverTransition) / 5.0D;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 104 */     font.drawCenteredString(this.mod.getName(), (float)this.x + (float)this.width / 2.0F, (float)this.y - 2.0F + (float)this.height / 2.0F, textcolor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean mouseClicked(int mouseX, int mouseY, int mouseButton)
/*     */   {
/* 112 */     if (!isHovered(mouseX, mouseY)) {
/* 113 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 118 */     if (mouseButton == 0) {
/* 119 */       this.mod.toggle();
/*     */       
/* 121 */       if (Polaris.instance.settingsManager.getSettingByName("Sound").getValBoolean())
/* 122 */         Minecraft.getMinecraft().thePlayer.playSound("random.click", 0.5F, 0.5F);
/* 123 */     } else if (mouseButton == 1)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 128 */       if ((this.menuelements != null) && (this.menuelements.size() > 0)) {
/* 129 */         boolean b = !this.extended;
/* 130 */         Polaris.instance.clickGui.closeAllSettings();
/* 131 */         this.extended = b;
/*     */         
/* 133 */         if (Polaris.instance.settingsManager.getSettingByName("Sound").getValBoolean())
/* 134 */           if (this.extended) Minecraft.getMinecraft().thePlayer.playSound("tile.piston.out", 1.0F, 1.0F); else Minecraft.getMinecraft().thePlayer.playSound("tile.piston.in", 1.0F, 1.0F);
/*     */       }
/* 136 */     } else if (mouseButton == 2)
/*     */     {
/*     */ 
/*     */ 
/* 140 */       this.listening = true;
/*     */     }
/* 142 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean keyTyped(char typedChar, int keyCode)
/*     */     throws IOException
/*     */   {
/* 150 */     if (this.listening) {
/* 151 */       if (keyCode != 1)
/*     */       {
/* 153 */         this.mod.setKey(keyCode);
/*     */       }
/*     */       else {
/* 156 */         this.mod.setKey(0);
/*     */       }
/* 158 */       this.listening = false;
/* 159 */       return true;
/*     */     }
/* 161 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isHovered(int mouseX, int mouseY) {
/* 165 */     return (mouseX >= this.x) && (mouseX <= this.x + this.width) && (mouseY >= this.y) && (mouseY <= this.y + this.height);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\click\clickgui\elements\ModuleButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */