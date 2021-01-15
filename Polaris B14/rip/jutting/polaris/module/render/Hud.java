/*     */ package rip.jutting.polaris.module.render;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.gui.GuiChat;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.command.commands.NameCommand;
/*     */ import rip.jutting.polaris.event.EventTarget;
/*     */ import rip.jutting.polaris.event.events.Event2D;
/*     */ import rip.jutting.polaris.event.events.EventKey;
/*     */ import rip.jutting.polaris.event.events.EventUpdate;
/*     */ import rip.jutting.polaris.module.Category;
/*     */ import rip.jutting.polaris.module.Module;
/*     */ import rip.jutting.polaris.module.ModuleManager;
/*     */ import rip.jutting.polaris.ui.click.settings.Setting;
/*     */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*     */ import rip.jutting.polaris.ui.fonth.CFontRenderer;
/*     */ import rip.jutting.polaris.ui.fonth.FontLoaders;
/*     */ import rip.jutting.polaris.utils.RenderUtils.R2DUtils;
/*     */ import rip.jutting.polaris.utils.ScaleUtils;
/*     */ 
/*     */ public class Hud extends Module
/*     */ {
/*  38 */   public static int selectedCat = 0;
/*  39 */   public static int selectedMod = 0;
/*  40 */   public static int catTargetY = 0;
/*  41 */   public static int modTargetY = 0;
/*  42 */   public static int modSelectorX = 0; public static int modSelectorY = 0;
/*     */   public static int x;
/*  44 */   public static int y; public static int width; public static int height; public static int catSelectorX = 0; public static int catSelectorY = 0;
/*     */   public static Category selectedCategory;
/*  46 */   public static boolean collapsed = false;
/*  47 */   public static ArrayList<Module> modules = new ArrayList();
/*  48 */   public static int animf = 1;
/*  49 */   public static int animb = 1;
/*  50 */   public static int animr = 1;
/*  51 */   public static int animl = 1;
/*  52 */   public static int animspace = 1;
/*     */   private static int currentColor;
/*     */   private static int fadeState;
/*     */   private static boolean goingUp;
/*     */   
/*     */   public Hud()
/*     */   {
/*  59 */     super("Hud", 0, Category.RENDER);
/*     */   }
/*     */   
/*     */   public void setup()
/*     */   {
/*  64 */     Polaris.instance.settingsManager.rSetting(new Setting("Info", this, false));
/*  65 */     Polaris.instance.settingsManager.rSetting(new Setting("German", this, true));
/*  66 */     Polaris.instance.settingsManager.rSetting(new Setting("Status Effect", this, true));
/*  67 */     Polaris.instance.settingsManager.rSetting(new Setting("TabGui", this, true));
/*  68 */     Polaris.instance.settingsManager.rSetting(new Setting("Rainbow", this, false));
/*  69 */     Polaris.instance.settingsManager.rSetting(new Setting("Fade", this, true));
/*  70 */     Polaris.instance.settingsManager.rSetting(new Setting("TB", this, false));
/*  71 */     Polaris.instance.settingsManager.rSetting(new Setting("TTF Chat", this, true));
/*     */   }
/*     */   
/*     */ 
/*     */   @EventTarget
/*     */   public void onUpdate(EventUpdate event) {}
/*     */   
/*     */   @EventTarget
/*     */   public void onRender(Event2D event)
/*     */   {
/*  81 */     CFontRenderer font = FontLoaders.vardana12;
/*  82 */     setDisplayName("Hud");
/*  83 */     if ((Polaris.instance.settingsManager.getSettingByName("TabGui").getValBoolean()) && 
/*  84 */       (!Polaris.instance.settingsManager.getSettingByName("TB").getValBoolean())) {
/*  85 */       TabGUI();
/*     */     }
/*  87 */     if ((Polaris.instance.settingsManager.getSettingByName("TabGui").getValBoolean()) && 
/*  88 */       (Polaris.instance.settingsManager.getSettingByName("TB").getValBoolean())) {
/*  89 */       TabGUI1();
/*     */     }
/*  91 */     if ((Polaris.instance.settingsManager.getSettingByName("Info").getValBoolean()) && 
/*  92 */       (!Polaris.instance.settingsManager.getSettingByName("TB").getValBoolean())) {
/*  93 */       ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
/*  94 */       if (!(mc.currentScreen instanceof GuiChat)) {
/*  95 */         font.drawStringWithShadow("FPS §7: " + Minecraft.debugFPS, 2.0D, ScaledResolution.getScaledHeight() - 20, 
/*  96 */           new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/*  97 */           (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/*  98 */           (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble())
/*  99 */           .getRGB());
/*     */       }
/* 101 */       if ((mc.currentScreen instanceof GuiChat)) {
/* 102 */         font.drawStringWithShadow("XYZ§7 : " + 
/* 103 */           (int)mc.thePlayer.posX + ", " + (int)mc.thePlayer.posY + ", " + (int)mc.thePlayer.posZ, 2.0D, 
/* 104 */           ScaledResolution.getScaledHeight() - 22, 
/* 105 */           new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 106 */           (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 107 */           (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble())
/* 108 */           .getRGB());
/*     */       } else {
/* 110 */         font.drawStringWithShadow("XYZ§7 : " + 
/* 111 */           (int)mc.thePlayer.posX + ", " + (int)mc.thePlayer.posY + ", " + (int)mc.thePlayer.posZ, 2.0D, 
/* 112 */           ScaledResolution.getScaledHeight() - 10, 
/* 113 */           new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 114 */           (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 115 */           (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble())
/* 116 */           .getRGB());
/*     */       }
/*     */     }
/* 119 */     if ((Polaris.instance.settingsManager.getSettingByName("Info").getValBoolean()) && 
/* 120 */       (Polaris.instance.settingsManager.getSettingByName("TB").getValBoolean())) {
/* 121 */       ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
/* 122 */       if (!(mc.currentScreen instanceof GuiChat)) {
/* 123 */         mc.fontRendererObj.drawStringWithShadow("FPS §7: " + Minecraft.debugFPS, 2.0F, ScaledResolution.getScaledHeight() - 20, 
/* 124 */           new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 125 */           (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 126 */           (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble())
/* 127 */           .getRGB());
/*     */       }
/* 129 */       if ((mc.currentScreen instanceof GuiChat)) {
/* 130 */         mc.fontRendererObj.drawStringWithShadow("XYZ§7 : " + 
/* 131 */           (int)mc.thePlayer.posX + ", " + (int)mc.thePlayer.posY + ", " + (int)mc.thePlayer.posZ, 2.0F, 
/* 132 */           ScaledResolution.getScaledHeight() - 22, 
/* 133 */           new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 134 */           (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 135 */           (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble())
/* 136 */           .getRGB());
/*     */       } else {
/* 138 */         mc.fontRendererObj.drawStringWithShadow("XYZ§7 : " + 
/* 139 */           (int)mc.thePlayer.posX + ", " + (int)mc.thePlayer.posY + ", " + (int)mc.thePlayer.posZ, 2.0F, 
/* 140 */           ScaledResolution.getScaledHeight() - 10, 
/* 141 */           new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 142 */           (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 143 */           (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble())
/* 144 */           .getRGB());
/*     */       }
/*     */     }
/* 147 */     if (Polaris.instance.settingsManager.getSettingByName("TB").getValBoolean()) {
/* 148 */       mc.fontRendererObj.drawStringWithShadow(NameCommand.name + " §7" + Polaris.instance.version, 2.0F, 3.0F, 
/* 149 */         new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 150 */         (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 151 */         (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble(), 100)
/* 152 */         .getRGB());
/*     */     } else {
/* 154 */       CFontRenderer hudxd = FontLoaders.hud;
/* 155 */       hudxd.drawStringWithShadow("§l" + NameCommand.name + " §7", 2.0D, -1.0D, 
/* 156 */         new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 157 */         (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 158 */         (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble()).getRGB());
/*     */     }
/*     */     
/* 161 */     if (Polaris.instance.settingsManager.getSettingByName("Status Effect").getValBoolean()) {
/* 162 */       renderPotionStatus();
/*     */     }
/*     */     
/* 165 */     int y = 6;
/* 166 */     int y2 = 2;
/*     */     
/* 168 */     List<Module> mods = new ArrayList();
/* 169 */     List<Module> modz = new ArrayList();
/*     */     
/* 171 */     for (Module module : Polaris.instance.moduleManager.getModules()) {
/* 172 */       mods.add(module);
/*     */     }
/* 174 */     for (Module module : Polaris.instance.moduleManager.getModules()) {
/* 175 */       modz.add(module);
/*     */     }
/*     */     
/* 178 */     int[] counter = { 1 };
/*     */     
/* 180 */     Collections.sort(modz, new rip.jutting.polaris.module.ModuleComparator2());
/* 181 */     int width; for (Module module : modz) {
/* 182 */       if ((module.isToggled()) && (Polaris.instance.settingsManager.getSettingByName("TB").getValBoolean())) {
/* 183 */         if (Polaris.instance.settingsManager.getSettingByName("Rainbow").getValBoolean()) {
/* 184 */           int width = mc.fontRendererObj.getStringWidth(module.getDisplayName() + " ");
/* 185 */           mc.fontRendererObj.drawStringWithShadow(module.getDisplayName(), ScaleUtils.getWidth() - width + 1, 
/* 186 */             y2, rainbow(counter[0] * 300));
/* 187 */           y2 += mc.fontRendererObj.getHeight() + 2;
/* 188 */           int[] array = counter;
/* 189 */           int n = 0;
/* 190 */           array[0] += 1;
/*     */         } else {
/* 192 */           width = mc.fontRendererObj.getStringWidth(module.getDisplayName() + " ");
/* 193 */           mc.fontRendererObj.drawStringWithShadow(module.getDisplayName(), ScaleUtils.getWidth() - width + 1, 
/* 194 */             y2, module.getColor());
/* 195 */           y2 += mc.fontRendererObj.getHeight() + 2;
/*     */         }
/*     */       }
/*     */     }
/* 199 */     int i = 0;
/* 200 */     Collections.sort(mods, new rip.jutting.polaris.module.ModuleComparator());
/* 201 */     if (Polaris.instance.settingsManager.getSettingByName("Rainbow").getValBoolean()) {
/* 202 */       for (Module module : mods) {
/* 203 */         if ((module.isToggled()) && (module.anim != -1) && 
/* 204 */           (!Polaris.instance.settingsManager.getSettingByName("TB").getValBoolean())) {
/* 205 */           String s = String.valueOf(module.getDisplayName() + " ");
/* 206 */           int width = font.getStringWidth(module.getDisplayName() + " ");
/* 207 */           if (Polaris.instance.settingsManager.getSettingByName("German").getValBoolean()) {
/* 208 */             int mwidth = ScaledResolution.getScaledWidth() - module.anim;
/* 209 */             int mheight = 12 * i + 5;
/* 210 */             Gui.drawRect(ScaledResolution.getScaledWidth() - 4 - module.anim, y - 5, 
/* 211 */               ScaledResolution.getScaledWidth() - 1, y + 7, new Color(0, 0, 0, 70).getRGB());
/* 212 */             Gui.drawRect(mwidth - 2.2D, mheight - 4, mwidth - 4, mheight + 8, rainbow(counter[0] * 200));
/* 213 */             i++;
/*     */           }
/* 215 */           font.drawStringWithShadow(module.getDisplayName(), ScaleUtils.getWidth() - module.anim, y - 1, 
/* 216 */             rainbow(counter[0] * 200));
/*     */           
/* 218 */           int[] array = counter;
/* 219 */           int n = 0;
/* 220 */           array[0] += 1;
/* 221 */           y += font.getHeight() + 6;
/* 222 */           if ((module.anim < font.getStringWidth(s) + 1) && (module.isToggled())) {
/* 223 */             module.anim += 1;
/*     */           }
/* 225 */           if ((module.anim < font.getStringWidth(s) + 1) && (module.isToggled())) {
/* 226 */             module.anim += 1;
/*     */           }
/* 228 */           if ((module.anim < font.getStringWidth(s) + 1) && (module.isToggled())) {
/* 229 */             module.anim += 1;
/*     */           }
/* 231 */           if ((module.anim < font.getStringWidth(s) + 1) && (module.isToggled())) {
/* 232 */             module.anim += 1;
/*     */           }
/* 234 */           if ((module.anim < font.getStringWidth(s) + 1) && (module.isToggled())) {
/* 235 */             module.anim += 1;
/*     */           }
/* 237 */           if ((module.anim > font.getStringWidth(s) + 1) && (module.isToggled())) {
/* 238 */             module.anim = font.getStringWidth(s);
/*     */           }
/* 240 */           if ((module.anim != -1) && (!module.isToggled())) {
/* 241 */             module.anim -= 1;
/*     */           }
/* 243 */           if ((module.anim != -1) && (!module.isToggled())) {
/* 244 */             module.anim -= 1;
/*     */           }
/* 246 */           if ((module.anim != -1) && (!module.isToggled())) {
/* 247 */             module.anim -= 1;
/*     */           }
/* 249 */           if ((module.anim != -1) && (!module.isToggled())) {
/* 250 */             module.anim -= 1;
/*     */           }
/* 252 */           if ((module.anim < -1) && (!module.isToggled()))
/*     */           {
/*     */ 
/* 255 */             module.anim = 0; }
/*     */         }
/*     */       }
/* 258 */     } else if (Polaris.instance.settingsManager.getSettingByName("Fade").getValBoolean()) {
/* 259 */       for (Module module : mods) {
/* 260 */         if ((module.isToggled()) && (module.anim != -1) && 
/* 261 */           (!Polaris.instance.settingsManager.getSettingByName("TB").getValBoolean())) {
/* 262 */           String s = String.valueOf(module.getDisplayName() + " ");
/* 263 */           int width = font.getStringWidth(module.getDisplayName() + " ");
/* 264 */           if (Polaris.instance.settingsManager.getSettingByName("German").getValBoolean()) {
/* 265 */             int mwidth = ScaledResolution.getScaledWidth() - module.anim;
/* 266 */             int mheight = 12 * i + 5;
/* 267 */             Gui.drawRect(ScaledResolution.getScaledWidth() - 4 - module.anim, y - 5, 
/* 268 */               ScaledResolution.getScaledWidth() - 1, y + 7, new Color(0, 0, 0, 70).getRGB());
/* 269 */             Gui.drawRect(mwidth - 2.2D, mheight - 4, mwidth - 4, mheight + 8, currentColor);
/* 270 */             i++;
/*     */           }
/* 272 */           font.drawStringWithShadow(module.getDisplayName(), ScaleUtils.getWidth() - module.anim, y - 1, 
/* 273 */             currentColor);
/*     */           
/* 275 */           y += font.getHeight() + 6;
/* 276 */           if ((module.anim < font.getStringWidth(s) + 1) && (module.isToggled())) {
/* 277 */             module.anim += 1;
/*     */           }
/* 279 */           if ((module.anim < font.getStringWidth(s) + 1) && (module.isToggled())) {
/* 280 */             module.anim += 1;
/*     */           }
/* 282 */           if ((module.anim < font.getStringWidth(s) + 1) && (module.isToggled())) {
/* 283 */             module.anim += 1;
/*     */           }
/* 285 */           if ((module.anim < font.getStringWidth(s) + 1) && (module.isToggled())) {
/* 286 */             module.anim += 1;
/*     */           }
/* 288 */           if ((module.anim < font.getStringWidth(s) + 1) && (module.isToggled())) {
/* 289 */             module.anim += 1;
/*     */           }
/* 291 */           if ((module.anim > font.getStringWidth(s) + 1) && (module.isToggled())) {
/* 292 */             module.anim = font.getStringWidth(s);
/*     */           }
/* 294 */           if ((module.anim != -1) && (!module.isToggled())) {
/* 295 */             module.anim -= 1;
/*     */           }
/* 297 */           if ((module.anim != -1) && (!module.isToggled())) {
/* 298 */             module.anim -= 1;
/*     */           }
/* 300 */           if ((module.anim != -1) && (!module.isToggled())) {
/* 301 */             module.anim -= 1;
/*     */           }
/* 303 */           if ((module.anim != -1) && (!module.isToggled())) {
/* 304 */             module.anim -= 1;
/*     */           }
/* 306 */           if ((module.anim < -1) && (!module.isToggled()))
/*     */           {
/*     */ 
/* 309 */             module.anim = 0; }
/*     */         }
/*     */       }
/*     */     } else {
/* 313 */       for (Module module : mods) {
/* 314 */         if ((module.isToggled()) && (module.anim != -1) && 
/* 315 */           (!Polaris.instance.settingsManager.getSettingByName("TB").getValBoolean())) {
/* 316 */           String s = String.valueOf(module.getDisplayName() + " ");
/* 317 */           int width = font.getStringWidth(module.getDisplayName() + " ");
/* 318 */           if (Polaris.instance.settingsManager.getSettingByName("German").getValBoolean()) {
/* 319 */             int mwidth = ScaledResolution.getScaledWidth() - module.anim;
/* 320 */             int mheight = 12 * i + 5;
/* 321 */             Gui.drawRect(ScaledResolution.getScaledWidth() - 4 - module.anim, y - 5, 
/* 322 */               ScaledResolution.getScaledWidth() - 1, y + 7, new Color(0, 0, 0, 70).getRGB());
/* 323 */             Gui.drawRect(mwidth - 2.2D, mheight - 4, mwidth - 4, mheight + 8, module.getColor());
/* 324 */             i++;
/*     */           }
/* 326 */           font.drawStringWithShadow(module.getDisplayName(), ScaleUtils.getWidth() - module.anim, y - 1, 
/* 327 */             module.getColor());
/*     */           
/* 329 */           y += font.getHeight() + 6;
/* 330 */           if ((module.anim < font.getStringWidth(s) + 1) && (module.isToggled())) {
/* 331 */             module.anim += 1;
/*     */           }
/* 333 */           if ((module.anim < font.getStringWidth(s) + 1) && (module.isToggled())) {
/* 334 */             module.anim += 1;
/*     */           }
/* 336 */           if ((module.anim < font.getStringWidth(s) + 1) && (module.isToggled())) {
/* 337 */             module.anim += 1;
/*     */           }
/* 339 */           if ((module.anim < font.getStringWidth(s) + 1) && (module.isToggled())) {
/* 340 */             module.anim += 1;
/*     */           }
/* 342 */           if ((module.anim < font.getStringWidth(s) + 1) && (module.isToggled())) {
/* 343 */             module.anim += 1;
/*     */           }
/* 345 */           if ((module.anim > font.getStringWidth(s) + 1) && (module.isToggled())) {
/* 346 */             module.anim = font.getStringWidth(s);
/*     */           }
/* 348 */           if ((module.anim != -1) && (!module.isToggled())) {
/* 349 */             module.anim -= 1;
/*     */           }
/* 351 */           if ((module.anim != -1) && (!module.isToggled())) {
/* 352 */             module.anim -= 1;
/*     */           }
/* 354 */           if ((module.anim != -1) && (!module.isToggled())) {
/* 355 */             module.anim -= 1;
/*     */           }
/* 357 */           if ((module.anim != -1) && (!module.isToggled())) {
/* 358 */             module.anim -= 1;
/*     */           }
/* 360 */           if ((module.anim < -1) && (!module.isToggled()))
/*     */           {
/*     */ 
/* 363 */             module.anim = 0; }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void updateFade() {
/* 370 */     if ((fadeState >= 20) || (fadeState <= 0)) {
/* 371 */       goingUp = !goingUp;
/*     */     }
/* 373 */     if (goingUp) {
/* 374 */       fadeState += 1;
/*     */     } else {
/* 376 */       fadeState -= 1;
/*     */     }
/* 378 */     double ratio = fadeState / 20.0D;
/* 379 */     currentColor = getFadeHex(
/* 380 */       new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 381 */       (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 382 */       (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble()).darker()
/* 383 */       .getRGB() + 
/* 384 */       10, 
/* 385 */       new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 386 */       (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 387 */       (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble()).brighter()
/* 388 */       .getRGB(), 
/* 389 */       ratio);
/*     */   }
/*     */   
/*     */   private static int getFadeHex(int hex1, int hex2, double ratio) {
/* 393 */     int r = hex1 >> 16;
/* 394 */     int g = hex1 >> 8 & 0xFF;
/* 395 */     int b = hex1 & 0xFF;
/* 396 */     r += (int)(((hex2 >> 16) - r) * ratio);
/* 397 */     g += (int)(((hex2 >> 8 & 0xFF) - g) * ratio);
/* 398 */     b += (int)(((hex2 & 0xFF) - b) * ratio);
/* 399 */     return r << 16 | g << 8 | b;
/*     */   }
/*     */   
/*     */   public static int rainbow(int delay) {
/* 403 */     double rainbow = Math.ceil((System.currentTimeMillis() + delay) / 10.0D);
/* 404 */     rainbow %= 360.0D;
/* 405 */     return Color.getHSBColor((float)(rainbow / 360.0D), 0.5F, 1.0F).getRGB();
/*     */   }
/*     */   
/*     */   private String getDirection() {
/* 409 */     return mc.getRenderViewEntity().getHorizontalFacing().name();
/*     */   }
/*     */   
/*     */   private void drawFPS1() {
/* 413 */     ScaledResolution scaledResolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
/* 414 */     int x = 4;
/* 415 */     int y = ScaledResolution.getScaledHeight() - 2;
/* 416 */     y -= mc.fontRendererObj.getHeight() + 1;
/* 417 */     if ((mc.currentScreen instanceof GuiChat)) {
/* 418 */       y -= 13;
/*     */     }
/* 420 */     StringBuilder sb = new StringBuilder("FPS:§f ");
/* 421 */     GL11.glPopMatrix();
/* 422 */     GL11.glPushMatrix();
/* 423 */     mc.fontRendererObj.drawStringWithShadow(Minecraft.debugFPS, 3.0F, 129.0F, 
/* 424 */       new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 425 */       (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 426 */       (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble()).getRGB());
/* 427 */     GL11.glPopMatrix();
/* 428 */     GL11.glPushMatrix();
/*     */   }
/*     */   
/*     */   private void drawCoordinates1() {
/* 432 */     mc.fontRendererObj.drawStringWithShadow("X: §f" + MathHelper.floor_double(mc.thePlayer.posX), 3.0F, 98.0F, 
/* 433 */       new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 434 */       (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 435 */       (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble()).getRGB());
/*     */     
/* 437 */     mc.fontRendererObj.drawStringWithShadow("Y: §f" + MathHelper.floor_double(mc.thePlayer.posY), 3.0F, 108.0F, 
/* 438 */       new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 439 */       (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 440 */       (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble()).getRGB());
/*     */     
/* 442 */     mc.fontRendererObj.drawStringWithShadow("Z: §f" + MathHelper.floor_double(mc.thePlayer.posZ), 3.0F, 118.0F, 
/* 443 */       new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 444 */       (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 445 */       (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble()).getRGB());
/*     */   }
/*     */   
/*     */   private void drawFPS() {
/* 449 */     CFontRenderer font = FontLoaders.vardana12;
/* 450 */     ScaledResolution scaledResolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
/* 451 */     int x = 4;
/* 452 */     int y = ScaledResolution.getScaledHeight() - 2;
/* 453 */     y -= font.getHeight() + 1;
/* 454 */     if ((mc.currentScreen instanceof GuiChat)) {
/* 455 */       y -= 13;
/*     */     }
/* 457 */     CFontRenderer clientFont = font;
/* 458 */     StringBuilder sb = new StringBuilder("FPS:§f ");
/* 459 */     GL11.glPopMatrix();
/* 460 */     GL11.glPushMatrix();
/* 461 */     clientFont.drawStringWithShadow(Minecraft.debugFPS, 3.0D, 129.0D, 
/* 462 */       new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 463 */       (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 464 */       (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble()).getRGB());
/* 465 */     GL11.glPopMatrix();
/* 466 */     GL11.glPushMatrix();
/*     */   }
/*     */   
/*     */   private void drawCoordinates() {
/* 470 */     CFontRenderer font = FontLoaders.vardana12;
/* 471 */     font.drawStringWithShadow("X: §f" + MathHelper.floor_double(mc.thePlayer.posX), 3.0D, 98.0D, 
/* 472 */       new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 473 */       (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 474 */       (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble()).getRGB());
/*     */     
/* 476 */     font.drawStringWithShadow("Y: §f" + MathHelper.floor_double(mc.thePlayer.posY), 3.0D, 108.0D, 
/* 477 */       new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 478 */       (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 479 */       (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble()).getRGB());
/*     */     
/* 481 */     font.drawStringWithShadow("Z: §f" + MathHelper.floor_double(mc.thePlayer.posZ), 3.0D, 118.0D, 
/* 482 */       new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 483 */       (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 484 */       (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble()).getRGB());
/*     */   }
/*     */   
/*     */   public void TabGUI() {
/* 488 */     CFontRenderer font = FontLoaders.vardana12;
/* 489 */     x = 3;
/* 490 */     y = 18;
/* 491 */     width = 65;
/* 492 */     height = 14;
/* 493 */     catSelectorX = x + 1;
/*     */     
/* 495 */     int modX = x + width + 2;
/*     */     
/* 497 */     int catCount = 0;
/* 498 */     Category[] arrayOfCategory; int j = (arrayOfCategory = Category.values()).length; for (int i = 0; i < j; i++) { Category c = arrayOfCategory[i];
/* 499 */       catCount++;
/*     */     }
/*     */     
/* 502 */     if (selectedCat > catCount - 1) {
/* 503 */       selectedCat = 0;
/*     */     }
/*     */     
/* 506 */     if (selectedCat < 0) {
/* 507 */       selectedCat = catCount - 1;
/*     */     }
/*     */     
/* 510 */     selectedCategory = Category.values()[selectedCat];
/*     */     
/* 512 */     catTargetY = y + selectedCat * height;
/*     */     
/* 514 */     if (catSelectorY == 64870)
/* 515 */       catSelectorY = catTargetY;
/* 516 */     if (catSelectorY < catTargetY) {
/* 517 */       catSelectorY += 1;
/*     */     }
/* 519 */     if (catSelectorY > catTargetY) {
/* 520 */       catSelectorY -= 1;
/*     */     }
/* 522 */     if (catSelectorY < catTargetY) {
/* 523 */       catSelectorY += 1;
/*     */     }
/* 525 */     if (catSelectorY > catTargetY) {
/* 526 */       catSelectorY -= 1;
/*     */     }
/* 528 */     if (catSelectorY < catTargetY) {
/* 529 */       catSelectorY += 1;
/*     */     }
/* 531 */     if (catSelectorY > catTargetY) {
/* 532 */       catSelectorY -= 1;
/*     */     }
/* 534 */     if (catSelectorY < catTargetY) {
/* 535 */       catSelectorY += 1;
/*     */     }
/* 537 */     if (catSelectorY > catTargetY) {
/* 538 */       catSelectorY -= 1;
/*     */     }
/*     */     
/* 541 */     int count1 = 0;
/* 542 */     RenderUtils.R2DUtils.drawBorderedRect(x, y + count1 * height * 5, x + width, y + (count1 + 1) * height * 5, 
/* 543 */       new Color(0, 0, 0, 70).getRGB(), new Color(0, 0, 0, 100).getRGB());
/*     */     
/* 545 */     Gui.drawRect(catSelectorX, catSelectorY + 1, width + 2, catSelectorY + (height - 1), 
/* 546 */       new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 547 */       (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 548 */       (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble(), 200).getRGB());
/*     */     
/* 550 */     int count2 = 0;
/* 551 */     Object localObject; int m = (localObject = Category.values()).length; for (int k = 0; k < m; k++) { Category c = localObject[k];
/* 552 */       if ((!c.name().equalsIgnoreCase("CONFIG")) && (!c.name().equalsIgnoreCase("GUI")) && 
/* 553 */         (!c.name().equalsIgnoreCase("DEV"))) {
/* 554 */         String name = c.name().substring(0, 1).toUpperCase() + c.name().substring(1).toLowerCase();
/* 555 */         font.drawStringWithShadow(name, 
/* 556 */           x + (Category.values()[selectedCat].name().equalsIgnoreCase(c.name()) ? 7 : 4), 
/* 557 */           y + count2 * height + 4, 16777215);
/* 558 */         count2++;
/*     */       }
/*     */     }
/*     */     
/* 562 */     if (collapsed) {
/* 563 */       int modCount = 0;
/* 564 */       int maxWidth = 0;
/*     */       
/* 566 */       modules.clear();
/* 567 */       for (localObject = Polaris.instance.moduleManager.getModules().iterator(); ((Iterator)localObject).hasNext();) { Module m = (Module)((Iterator)localObject).next();
/* 568 */         if (m.getCategory() == selectedCategory) {
/* 569 */           modules.add(m);
/*     */         }
/*     */       }
/*     */       
/* 573 */       for (localObject = modules.iterator(); ((Iterator)localObject).hasNext();) { Module m = (Module)((Iterator)localObject).next();
/* 574 */         if (m.getCategory() == selectedCategory) {
/* 575 */           if (font.getStringWidth(m.getName()) > maxWidth) {
/* 576 */             maxWidth = font.getStringWidth(m.getName());
/*     */           }
/* 578 */           modCount++;
/*     */         }
/*     */       }
/*     */       
/* 582 */       maxWidth += 10;
/*     */       
/* 584 */       if (selectedMod > modCount - 1) {
/* 585 */         selectedMod = 0;
/*     */       }
/*     */       
/* 588 */       if (selectedMod < 0) {
/* 589 */         selectedMod = modCount - 1;
/*     */       }
/*     */       
/* 592 */       int selectedCatY = selectedCat * height;
/* 593 */       modTargetY = y + selectedCatY + selectedMod * height;
/*     */       
/* 595 */       if (modSelectorY < modTargetY) {
/* 596 */         modSelectorY += 1;
/*     */       }
/* 598 */       if (modSelectorY > modTargetY) {
/* 599 */         modSelectorY -= 1;
/*     */       }
/* 601 */       if (modSelectorY < modTargetY) {
/* 602 */         modSelectorY += 1;
/*     */       }
/* 604 */       if (modSelectorY > modTargetY) {
/* 605 */         modSelectorY -= 1;
/*     */       }
/* 607 */       if (modSelectorY < modTargetY) {
/* 608 */         modSelectorY += 1;
/*     */       }
/* 610 */       if (modSelectorY > modTargetY) {
/* 611 */         modSelectorY -= 1;
/*     */       }
/* 613 */       if (modSelectorY < modTargetY) {
/* 614 */         modSelectorY += 1;
/*     */       }
/* 616 */       if (modSelectorY > modTargetY) {
/* 617 */         modSelectorY -= 1;
/*     */       }
/*     */       
/* 620 */       int count3 = 0;
/* 621 */       for (Module m : modules) {
/* 622 */         if (m.getCategory() == selectedCategory) {
/* 623 */           Gui.drawRect(modX, y + selectedCatY + count3 * height, modX + maxWidth + 1, 
/* 624 */             y + selectedCatY + (count3 + 1) * height, new Color(0, 0, 0, 70).getRGB());
/* 625 */           count3++;
/*     */         }
/*     */       }
/* 628 */       Gui.drawRect(modX + 1 + modSelectorX, modSelectorY + 1, modX + maxWidth, modSelectorY + (height - 1), 
/* 629 */         new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 630 */         (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 631 */         (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble()).getRGB());
/*     */       
/* 633 */       int count4 = 0;
/* 634 */       for (Module m : modules) {
/* 635 */         if (m.getCategory() == selectedCategory) {
/* 636 */           font.drawStringWithShadow((m.isToggled() ? "§f" : "§7") + m.getName(), 
/* 637 */             modX + (((Module)modules.get(selectedMod)).getName().equalsIgnoreCase(m.getName()) ? 7 : 4), 
/* 638 */             y + selectedCatY + count4 * height + 4, 16777215);
/* 639 */           count4++;
/*     */         }
/*     */       }
/* 642 */       if (tog) {
/* 643 */         tog = false;
/* 644 */         ((Module)modules.get(selectedMod)).toggle();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void TabGUI1() {
/* 650 */     x = 2;
/* 651 */     y = 17;
/* 652 */     width = 76;
/* 653 */     height = 14;
/* 654 */     catSelectorX = x;
/*     */     
/* 656 */     int modX = x + width + 1;
/*     */     
/* 658 */     int catCount = 0;
/* 659 */     Category[] arrayOfCategory1; int j = (arrayOfCategory1 = Category.values()).length; for (int i = 0; i < j; i++) { Category c = arrayOfCategory1[i];
/* 660 */       catCount++;
/*     */     }
/*     */     
/* 663 */     if (selectedCat > catCount - 1) {
/* 664 */       selectedCat = 0;
/*     */     }
/*     */     
/* 667 */     if (selectedCat < 0) {
/* 668 */       selectedCat = catCount - 1;
/*     */     }
/*     */     
/* 671 */     selectedCategory = Category.values()[selectedCat];
/*     */     
/* 673 */     catTargetY = y + selectedCat * height;
/*     */     
/* 675 */     if (catSelectorY == 64870)
/* 676 */       catSelectorY = catTargetY;
/* 677 */     if (catSelectorY < catTargetY) {
/* 678 */       catSelectorY += 1;
/*     */     }
/* 680 */     if (catSelectorY > catTargetY) {
/* 681 */       catSelectorY -= 1;
/*     */     }
/* 683 */     if (catSelectorY < catTargetY) {
/* 684 */       catSelectorY += 1;
/*     */     }
/* 686 */     if (catSelectorY > catTargetY) {
/* 687 */       catSelectorY -= 1;
/*     */     }
/*     */     
/* 690 */     int count1 = 0;
/* 691 */     Category[] arrayOfCategory2; int k = (arrayOfCategory2 = Category.values()).length; for (j = 0; j < k; j++) { Category c = arrayOfCategory2[j];
/* 692 */       Gui.drawRect(x, y + count1 * height, x + width, y + (count1 + 1) * height, 
/* 693 */         new Color(0, 0, 0, 70).getRGB());
/* 694 */       count1++;
/*     */     }
/*     */     
/* 697 */     RenderUtils.R2DUtils.drawBorderedRect(catSelectorX, catSelectorY, width + 2, catSelectorY + 1 + (height - 1), 
/* 698 */       new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 699 */       (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 700 */       (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble(), 160).getRGB(), 
/* 701 */       new Color(0, 0, 0, 90).getRGB());
/*     */     
/* 703 */     int count2 = 0;
/* 704 */     Object localObject; int m = (localObject = Category.values()).length; for (k = 0; k < m; k++) { Category c = localObject[k];
/* 705 */       if ((!c.name().equalsIgnoreCase("CONFIG")) && (!c.name().equalsIgnoreCase("GUI")) && 
/* 706 */         (!c.name().equalsIgnoreCase("DEV"))) {
/* 707 */         String name = c.name().substring(0, 1).toUpperCase() + c.name().substring(1).toLowerCase();
/* 708 */         mc.fontRendererObj.drawStringWithShadow(name, 
/* 709 */           x + (Category.values()[selectedCat].name().equalsIgnoreCase(c.name()) ? 7 : 4), 
/* 710 */           y + count2 * height + 3, 16777215);
/* 711 */         count2++;
/*     */       }
/*     */     }
/*     */     
/* 715 */     if (collapsed) {
/* 716 */       int modCount = 0;
/* 717 */       int maxWidth = 0;
/*     */       
/* 719 */       modules.clear();
/* 720 */       for (localObject = Polaris.instance.moduleManager.getModules().iterator(); ((Iterator)localObject).hasNext();) { Module m = (Module)((Iterator)localObject).next();
/* 721 */         if (m.getCategory() == selectedCategory) {
/* 722 */           modules.add(m);
/*     */         }
/*     */       }
/*     */       
/* 726 */       for (localObject = modules.iterator(); ((Iterator)localObject).hasNext();) { Module m = (Module)((Iterator)localObject).next();
/* 727 */         if (m.getCategory() == selectedCategory) {
/* 728 */           if (mc.fontRendererObj.getStringWidth(m.getName()) > maxWidth) {
/* 729 */             maxWidth = mc.fontRendererObj.getStringWidth(m.getName());
/*     */           }
/* 731 */           modCount++;
/*     */         }
/*     */       }
/*     */       
/* 735 */       maxWidth += 10;
/*     */       
/* 737 */       if (selectedMod > modCount - 1) {
/* 738 */         selectedMod = 0;
/*     */       }
/*     */       
/* 741 */       if (selectedMod < 0) {
/* 742 */         selectedMod = modCount - 1;
/*     */       }
/*     */       
/* 745 */       int selectedCatY = selectedCat * height;
/* 746 */       modTargetY = y + selectedCatY + selectedMod * height;
/*     */       
/* 748 */       if (modSelectorY < modTargetY) {
/* 749 */         modSelectorY += 1;
/*     */       }
/* 751 */       if (modSelectorY > modTargetY) {
/* 752 */         modSelectorY -= 1;
/*     */       }
/* 754 */       if (modSelectorY < modTargetY) {
/* 755 */         modSelectorY += 1;
/*     */       }
/* 757 */       if (modSelectorY > modTargetY) {
/* 758 */         modSelectorY -= 1;
/*     */       }
/* 760 */       if (modSelectorY < modTargetY) {
/* 761 */         modSelectorY += 1;
/*     */       }
/* 763 */       if (modSelectorY > modTargetY) {
/* 764 */         modSelectorY -= 1;
/*     */       }
/* 766 */       if (modSelectorY < modTargetY) {
/* 767 */         modSelectorY += 1;
/*     */       }
/* 769 */       if (modSelectorY > modTargetY) {
/* 770 */         modSelectorY -= 1;
/*     */       }
/*     */       
/* 773 */       int count3 = 0;
/* 774 */       for (Module m : modules) {
/* 775 */         if (m.getCategory() == selectedCategory) {
/* 776 */           Gui.drawRect(modX, y + selectedCatY + count3 * height, modX + maxWidth + 1, 
/* 777 */             y + selectedCatY + (count3 + 1) * height, new Color(0, 0, 0, 70).getRGB());
/* 778 */           count3++;
/*     */         }
/*     */       }
/* 781 */       RenderUtils.R2DUtils.drawBorderedRect(modX + modSelectorX, modSelectorY, modX + maxWidth + 1, 
/* 782 */         modSelectorY + 1 + (height - 1), 
/* 783 */         new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 784 */         (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 785 */         (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble(), 160)
/* 786 */         .getRGB(), 
/* 787 */         new Color(0, 0, 0, 90).getRGB());
/*     */       
/* 789 */       int count4 = 0;
/* 790 */       for (Module m : modules) {
/* 791 */         if (m.getCategory() == selectedCategory) {
/* 792 */           mc.fontRendererObj.drawStringWithShadow((m.isToggled() ? "§f" : "§7") + m.getName(), 
/* 793 */             modX + (((Module)modules.get(selectedMod)).getName().equalsIgnoreCase(m.getName()) ? 7 : 4), 
/* 794 */             y + selectedCatY + count4 * height + 3, 16777215);
/* 795 */           count4++;
/*     */         }
/*     */       }
/* 798 */       if (tog) {
/* 799 */         tog = false;
/* 800 */         ((Module)modules.get(selectedMod)).toggle();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onKeypress(EventKey event) {
/* 807 */     key(event.getKey());
/*     */   }
/*     */   
/* 810 */   static boolean tog = false;
/* 811 */   static boolean c = false;
/* 812 */   static boolean c2 = false;
/*     */   
/*     */   public static void key(int key) {
/* 815 */     if (!collapsed) {
/* 816 */       if (key == 200) {
/* 817 */         selectedCat -= 1;
/*     */       }
/* 819 */       if (key == 208) {
/* 820 */         selectedCat += 1;
/*     */       }
/* 822 */       if ((key == 28) || (key == 205)) {
/* 823 */         collapsed = true;
/* 824 */         modSelectorY = y + selectedCat * height + selectedMod * height;
/*     */       }
/*     */     } else {
/* 827 */       if (key == 200) {
/* 828 */         selectedMod -= 1;
/*     */       }
/* 830 */       if (key == 208) {
/* 831 */         selectedMod += 1;
/*     */       }
/* 833 */       if ((key == 28) || (key == 205)) {
/* 834 */         tog = true;
/*     */       }
/* 836 */       if (key == 203) {
/* 837 */         collapsed = false;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void renderPotionStatus() {
/* 843 */     CFontRenderer font = FontLoaders.vardana12;
/* 844 */     ScaledResolution scaledRes = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
/* 845 */     int x = 4;
/* 846 */     x = 0;
/*     */     
/* 848 */     for (PotionEffect effect : mc.thePlayer.getActivePotionEffects())
/*     */     {
/* 850 */       Potion potion = Potion.potionTypes[effect.getPotionID()];
/* 851 */       String PType = net.minecraft.client.resources.I18n.format(potion.getName(), new Object[0]);String d = "";
/* 852 */       switch (effect.getAmplifier()) {
/*     */       case 1: 
/* 854 */         PType = PType + " II";
/* 855 */         break;
/*     */       case 2: 
/* 857 */         PType = PType + " III";
/* 858 */         break;
/*     */       case 3: 
/* 860 */         PType = PType + " IV";
/* 861 */         break;
/*     */       }
/*     */       
/*     */       
/* 865 */       if ((effect.getDuration() < 600) && (effect.getDuration() > 300)) {
/* 866 */         d = Potion.getDurationString(effect);
/* 867 */       } else if (effect.getDuration() < 300) {
/* 868 */         d = Potion.getDurationString(effect);
/* 869 */       } else if (effect.getDuration() > 600) {
/* 870 */         d = Potion.getDurationString(effect);
/*     */       }
/* 872 */       int x1 = (int)((ScaledResolution.getScaledWidth() - 6) * 1.33D);
/* 873 */       int y1 = (int)((ScaledResolution.getScaledHeight() - 52 - font.getHeight() + x + 5) * 1.33F);
/*     */       
/* 875 */       int y = x + 95;
/* 876 */       int m = 1;
/* 877 */       font.drawStringWithShadow(PType + " §7: §7" + d, 2.0D, y, 
/* 878 */         new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 879 */         (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 880 */         (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble()).getRGB());
/* 881 */       x += 10;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\render\Hud.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */