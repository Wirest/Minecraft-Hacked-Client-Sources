/*     */ package rip.jutting.polaris.ui.click.clickgui;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.ui.click.clickgui.elements.ModuleButton;
/*     */ import rip.jutting.polaris.ui.click.settings.Setting;
/*     */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*     */ import rip.jutting.polaris.ui.font.FontManager;
/*     */ import rip.jutting.polaris.ui.font.MinecraftFontRenderer;
/*     */ import rip.jutting.polaris.ui.fonth.CFontRenderer;
/*     */ import rip.jutting.polaris.ui.fonth.FontLoaders;
/*     */ import rip.jutting.polaris.ui.particles.Particle.ParticleGen;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Panel
/*     */ {
/*     */   public String title;
/*     */   public double x;
/*     */   public double y;
/*     */   private double x2;
/*     */   private double y2;
/*     */   public double width;
/*     */   public double height;
/*     */   public boolean dragging;
/*     */   public boolean extended;
/*     */   public boolean visible;
/*     */   protected float hoverTransition;
/*  33 */   public ArrayList<ModuleButton> Elements = new ArrayList();
/*     */   public ClickGUI clickgui;
/*  35 */   ParticleGen pgen = new ParticleGen(30);
/*     */   
/*     */ 
/*     */ 
/*     */   public Panel(String ititle, double ix, double iy, double iwidth, double iheight, boolean iextended, ClickGUI parent)
/*     */   {
/*  41 */     this.title = ititle;
/*  42 */     this.x = ix;
/*  43 */     this.y = iy;
/*  44 */     this.width = iwidth;
/*  45 */     this.height = iheight;
/*  46 */     this.extended = iextended;
/*  47 */     this.dragging = false;
/*  48 */     this.visible = true;
/*  49 */     this.clickgui = parent;
/*  50 */     setup();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setup() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*     */   {
/*  63 */     if (!this.visible) {
/*  64 */       return;
/*     */     }
/*  66 */     if (this.dragging) {
/*  67 */       this.x = (this.x2 + mouseX);
/*  68 */       this.y = (this.y2 + mouseY);
/*     */     }
/*  70 */     CFontRenderer font = FontLoaders.vardana12;
/*  71 */     Gui.drawRect(this.x - 1.0D, this.y - 2.0D, this.x + 1.0D + this.width, this.y + this.height, this.dragging ? new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/*  72 */       (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/*  73 */       (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble(), 150).darker().getRGB() : new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/*  74 */       (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/*  75 */       (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble(), 200).getRGB());
/*  76 */     if (Polaris.instance.settingsManager.getSettingByName("Design").getValString().equalsIgnoreCase("New")) {
/*  77 */       Gui.drawRect(this.x - 2.0D, this.y, this.x, this.y + this.height, new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/*  78 */         (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/*  79 */         (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble()).getRGB());
/*  80 */       Polaris.instance.fontManager.arraylist.drawStringWithShadow(this.title, this.x + 2.0D, this.y + this.height / 2.0D - font.getHeight() / 2, -1052689);
/*  81 */     } else if (Polaris.instance.settingsManager.getSettingByName("Design").getValString().equalsIgnoreCase("Meme"))
/*     */     {
/*     */ 
/*  84 */       Polaris.instance.fontManager.arraylist.drawCenteredStringWithShadow(this.title, (float)this.x + (float)this.width / 2.0F, (float)this.y - 2.0F + (float)this.height / 3.0F, -1052689);
/*     */     }
/*     */     
/*  87 */     if ((this.extended) && (!this.Elements.isEmpty())) {
/*  88 */       double startY = this.y + this.height;
/*  89 */       int epanelcolor = Polaris.instance.settingsManager.getSettingByName("Design").getValString().equalsIgnoreCase("Meme") ? -1156246251 : Polaris.instance.settingsManager.getSettingByName("Design").getValString().equalsIgnoreCase("New") ? -14474461 : 0;
/*  90 */       for (ModuleButton et : this.Elements) {
/*  91 */         if (Polaris.instance.settingsManager.getSettingByName("Design").getValString().equalsIgnoreCase("New")) {
/*  92 */           Gui.drawRect(this.x - 2.0D, startY, this.x + this.width, startY + et.height + 1.0D, 
/*  93 */             new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/*  94 */             (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/*  95 */             (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble()).getRGB());
/*     */         }
/*  97 */         if (this.hoverTransition <= et.height) {
/*  98 */           this.hoverTransition = ((float)(this.hoverTransition + startY / 4000.0D));
/*  99 */           System.out.println(this.hoverTransition);
/* 100 */           System.out.println(et.height);
/*     */         } else {
/* 102 */           this.hoverTransition = ((float)et.height + 1.0F);
/*     */         }
/* 104 */         Gui.drawRect(this.x, startY, this.x + this.width, startY + et.height + 1.0D, epanelcolor);
/* 105 */         et.x = (this.x + 2.0D);
/* 106 */         et.y = startY;
/* 107 */         et.width = (this.width - 4.0D);
/* 108 */         et.drawScreen(mouseX, mouseY, partialTicks);
/* 109 */         startY += et.height + 1.0D;
/*     */       }
/* 111 */       Gui.drawRect(this.x, startY + 1.0D, this.x + this.width, startY + 1.0D, epanelcolor);
/*     */     }
/*     */     else {
/* 114 */       this.hoverTransition += (0.0F - this.hoverTransition) / 1.0F;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean mouseClicked(int mouseX, int mouseY, int mouseButton)
/*     */   {
/* 123 */     if (!this.visible) {
/* 124 */       return false;
/*     */     }
/* 126 */     if ((mouseButton == 0) && (isHovered(mouseX, mouseY))) {
/* 127 */       this.x2 = (this.x - mouseX);
/* 128 */       this.y2 = (this.y - mouseY);
/* 129 */       this.dragging = true;
/* 130 */       return true; }
/* 131 */     if ((mouseButton == 1) && (isHovered(mouseX, mouseY))) {
/* 132 */       this.extended = (!this.extended);
/* 133 */       return true; }
/* 134 */     if (this.extended) {
/* 135 */       for (ModuleButton et : this.Elements) {
/* 136 */         if (et.mouseClicked(mouseX, mouseY, mouseButton)) {
/* 137 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 141 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void mouseReleased(int mouseX, int mouseY, int state)
/*     */   {
/* 148 */     if (!this.visible) {
/* 149 */       return;
/*     */     }
/* 151 */     if (state == 0) {
/* 152 */       this.dragging = false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isHovered(int mouseX, int mouseY)
/*     */   {
/* 160 */     return (mouseX >= this.x) && (mouseX <= this.x + this.width) && (mouseY >= this.y) && (mouseY <= this.y + this.height);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\click\clickgui\Panel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */