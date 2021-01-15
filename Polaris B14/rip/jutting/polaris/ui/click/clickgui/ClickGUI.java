/*     */ package rip.jutting.polaris.ui.click.clickgui;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.EntityRenderer;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.shader.ShaderGroup;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.module.Category;
/*     */ import rip.jutting.polaris.module.Module;
/*     */ import rip.jutting.polaris.module.ModuleManager;
/*     */ import rip.jutting.polaris.ui.click.clickgui.elements.Element;
/*     */ import rip.jutting.polaris.ui.click.clickgui.elements.ModuleButton;
/*     */ import rip.jutting.polaris.ui.click.clickgui.elements.menu.ElementSlider;
/*     */ import rip.jutting.polaris.ui.click.clickgui.util.FontUtil;
/*     */ import rip.jutting.polaris.ui.click.settings.Setting;
/*     */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ClickGUI
/*     */   extends GuiScreen
/*     */ {
/*     */   public static ArrayList<Panel> panels;
/*     */   public static ArrayList<Panel> rpanels;
/*  39 */   private ModuleButton mb = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SettingsManager setmgr;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ClickGUI()
/*     */   {
/*  53 */     this.setmgr = Polaris.instance.settingsManager;
/*     */     
/*  55 */     FontUtil.setupFontUtils();
/*  56 */     panels = new ArrayList();
/*  57 */     double pwidth = 80.0D;
/*  58 */     double pheight = 15.0D;
/*  59 */     double px = 10.0D;
/*  60 */     double py = 10.0D;
/*  61 */     double pyplus = pheight + 10.0D;
/*     */     
/*     */ 
/*     */     Category[] arrayOfCategory;
/*     */     
/*  66 */     int j = (arrayOfCategory = Category.values()).length; for (int i = 0; i < j; i++) { Category c = arrayOfCategory[i];
/*  67 */       String title = Character.toUpperCase(c.name().toLowerCase().charAt(0)) + c.name().toLowerCase().substring(1);
/*  68 */       panels.add(new Panel(title, px, py, pwidth, pheight, false, this)
/*     */       {
/*     */         public void setup() {
/*  71 */           for (Module m : Polaris.instance.moduleManager.getModules()) {
/*  72 */             if (m.getCategory().equals(this.val$c))
/*  73 */               this.Elements.add(new ModuleButton(m, this));
/*     */           }
/*     */         }
/*  76 */       });
/*  77 */       py += pyplus;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  91 */     rpanels = new ArrayList();
/*  92 */     for (Panel p : panels) {
/*  93 */       rpanels.add(p);
/*     */     }
/*  95 */     Collections.reverse(rpanels);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks)
/*     */   {
/* 109 */     for (Panel p : panels) {
/* 110 */       p.drawScreen(mouseX, mouseY, partialTicks);
/*     */     }
/* 112 */     ScaledResolution s = new ScaledResolution(this.mc, Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 121 */     this.mb = null;
/*     */     
/*     */ 
/*     */     Iterator localIterator3;
/*     */     
/*     */     label186:
/*     */     
/* 128 */     for (Iterator localIterator2 = panels.iterator(); localIterator2.hasNext(); 
/*     */         
/*     */ 
/* 131 */         localIterator3.hasNext())
/*     */     {
/* 128 */       Panel p = (Panel)localIterator2.next();
/* 129 */       if ((p == null) || (!p.visible) || (!p.extended) || (p.Elements == null) || 
/* 130 */         (p.Elements.size() <= 0)) break label186;
/* 131 */       localIterator3 = p.Elements.iterator(); continue;ModuleButton e = (ModuleButton)localIterator3.next();
/* 132 */       if (e.listening) {
/* 133 */         this.mb = e;
/* 134 */         break;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     label489:
/*     */     
/*     */ 
/*     */ 
/* 145 */     for (localIterator2 = panels.iterator(); localIterator2.hasNext(); 
/*     */         
/* 147 */         localIterator3.hasNext())
/*     */     {
/* 145 */       Panel panel = (Panel)localIterator2.next();
/* 146 */       if ((!panel.extended) || (!panel.visible) || (panel.Elements == null)) break label489;
/* 147 */       localIterator3 = panel.Elements.iterator(); continue;ModuleButton b = (ModuleButton)localIterator3.next();
/* 148 */       if ((b.extended) && (b.menuelements != null) && (!b.menuelements.isEmpty())) {
/* 149 */         double off = 0.0D;
/*     */         
/* 151 */         for (Element e : b.menuelements) {
/* 152 */           e.offset = off;
/* 153 */           e.update();
/* 154 */           if (Polaris.instance.settingsManager.getSettingByName("Design").getValString().equalsIgnoreCase("New")) {
/* 155 */             Gui.drawRect(e.x, e.y, e.x + e.width + 2.0D, e.y + e.height, new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 156 */               (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 157 */               (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble()).getRGB());
/*     */           }
/* 159 */           e.drawScreen(mouseX, mouseY, partialTicks);
/* 160 */           off += e.height;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 172 */     if (this.mb != null) {
/* 173 */       drawRect(0.0D, 0.0D, width, height, -2012213232);
/* 174 */       GL11.glPushMatrix();
/* 175 */       GL11.glTranslatef(ScaledResolution.getScaledWidth() / 2, ScaledResolution.getScaledHeight() / 2, 0.0F);
/* 176 */       GL11.glScalef(2.0F, 2.0F, 0.0F);
/* 177 */       Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("Press Key", 0.0F, -10.0F, -1);
/* 178 */       GL11.glScalef(0.5F, 0.5F, 0.0F);
/* 179 */       Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("Press 'ESCAPE' to unbind " + this.mb.mod.getName() + (this.mb.mod.getKey() > -1 ? " (" + Keyboard.getKeyName(this.mb.mod.getKey()) + ")" : ""), 0.0F, 15.0F, -1);
/* 180 */       GL11.glScalef(0.25F, 0.25F, 0.0F);
/* 181 */       FontUtil.drawTotalCenteredStringWithShadow("", 0.0D, 20.0D, -1);
/* 182 */       GL11.glPopMatrix();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 190 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void mouseClicked(int mouseX, int mouseY, int mouseButton)
/*     */   {
/* 199 */     if (this.mb != null) {
/*     */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     Iterator localIterator2;
/*     */     
/*     */ 
/*     */ 
/*     */     label145:
/*     */     
/*     */ 
/*     */ 
/* 213 */     for (Iterator localIterator1 = rpanels.iterator(); localIterator1.hasNext(); 
/*     */         
/* 215 */         localIterator2.hasNext())
/*     */     {
/* 213 */       Panel panel = (Panel)localIterator1.next();
/* 214 */       if ((!panel.extended) || (!panel.visible) || (panel.Elements == null)) break label145;
/* 215 */       localIterator2 = panel.Elements.iterator(); continue;ModuleButton b = (ModuleButton)localIterator2.next();
/* 216 */       if (b.extended) {
/* 217 */         for (Element e : b.menuelements) {
/* 218 */           if (e.mouseClicked(mouseX, mouseY, mouseButton)) {
/* 219 */             return;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 230 */     for (Panel p : rpanels) {
/* 231 */       if (p.mouseClicked(mouseX, mouseY, mouseButton)) {
/* 232 */         return;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 239 */       super.mouseClicked(mouseX, mouseY, mouseButton);
/*     */     } catch (IOException e) {
/* 241 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void mouseReleased(int mouseX, int mouseY, int state)
/*     */   {
/* 251 */     if (this.mb != null) {
/*     */       return;
/*     */     }
/*     */     
/*     */     Iterator localIterator2;
/*     */     
/*     */     label141:
/* 258 */     for (Iterator localIterator1 = rpanels.iterator(); localIterator1.hasNext(); 
/*     */         
/* 260 */         localIterator2.hasNext())
/*     */     {
/* 258 */       Panel panel = (Panel)localIterator1.next();
/* 259 */       if ((!panel.extended) || (!panel.visible) || (panel.Elements == null)) break label141;
/* 260 */       localIterator2 = panel.Elements.iterator(); continue;ModuleButton b = (ModuleButton)localIterator2.next();
/* 261 */       if (b.extended) {
/* 262 */         for (Element e : b.menuelements) {
/* 263 */           e.mouseReleased(mouseX, mouseY, state);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 274 */     for (Panel p : rpanels) {
/* 275 */       p.mouseReleased(mouseX, mouseY, state);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 281 */     super.mouseReleased(mouseX, mouseY, state);
/*     */   }
/*     */   
/*     */ 
/*     */   protected void keyTyped(char typedChar, int keyCode)
/*     */   {
/*     */     Iterator localIterator2;
/*     */     label109:
/* 289 */     for (Iterator localIterator1 = rpanels.iterator(); localIterator1.hasNext(); 
/*     */         
/* 291 */         localIterator2.hasNext())
/*     */     {
/* 289 */       Panel p = (Panel)localIterator1.next();
/* 290 */       if ((p == null) || (!p.visible) || (!p.extended) || (p.Elements == null) || (p.Elements.size() <= 0)) break label109;
/* 291 */       localIterator2 = p.Elements.iterator(); continue;ModuleButton e = (ModuleButton)localIterator2.next();
/*     */       try {
/* 293 */         if (e.keyTyped(typedChar, keyCode)) return;
/*     */       } catch (IOException e1) {
/* 295 */         e1.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 306 */       super.keyTyped(typedChar, keyCode);
/*     */     } catch (IOException e2) {
/* 308 */       e2.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initGui()
/*     */   {
/* 317 */     if ((OpenGlHelper.shadersSupported) && ((this.mc.getRenderViewEntity() instanceof EntityPlayer)) && 
/* 318 */       (this.mc.entityRenderer.theShaderGroup != null)) {
/* 319 */       this.mc.entityRenderer.theShaderGroup.deleteShaderGroup();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onGuiClosed()
/*     */   {
/* 330 */     if (this.mc.entityRenderer.theShaderGroup != null) {
/* 331 */       this.mc.entityRenderer.theShaderGroup.deleteShaderGroup();
/* 332 */       this.mc.entityRenderer.theShaderGroup = null;
/*     */     }
/*     */     
/*     */     Iterator localIterator2;
/*     */     label169:
/* 337 */     for (Iterator localIterator1 = rpanels.iterator(); localIterator1.hasNext(); 
/*     */         
/* 339 */         localIterator2.hasNext())
/*     */     {
/* 337 */       Panel panel = (Panel)localIterator1.next();
/* 338 */       if ((!panel.extended) || (!panel.visible) || (panel.Elements == null)) break label169;
/* 339 */       localIterator2 = panel.Elements.iterator(); continue;ModuleButton b = (ModuleButton)localIterator2.next();
/* 340 */       if (b.extended) {
/* 341 */         for (Element e : b.menuelements) {
/* 342 */           if ((e instanceof ElementSlider)) {
/* 343 */             ((ElementSlider)e).dragging = false;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void closeAllSettings() {
/*     */     Iterator localIterator2;
/*     */     label88:
/* 353 */     for (Iterator localIterator1 = rpanels.iterator(); localIterator1.hasNext(); 
/*     */         
/*     */ 
/* 356 */         localIterator2.hasNext())
/*     */     {
/* 353 */       Panel p = (Panel)localIterator1.next();
/* 354 */       if ((p == null) || (!p.visible) || (!p.extended) || (p.Elements == null) || 
/* 355 */         (p.Elements.size() <= 0)) break label88;
/* 356 */       localIterator2 = p.Elements.iterator(); continue;ModuleButton localModuleButton = (ModuleButton)localIterator2.next();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\click\clickgui\ClickGUI.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */