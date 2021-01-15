/*     */ package rip.jutting.polaris.module.other;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Scanner;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.gui.GuiChat;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.event.EventTarget;
/*     */ import rip.jutting.polaris.event.events.Event2D;
/*     */ import rip.jutting.polaris.module.Category;
/*     */ import rip.jutting.polaris.module.Module;
/*     */ import rip.jutting.polaris.ui.click.settings.Setting;
/*     */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*     */ import rip.jutting.polaris.ui.fonth.CFontRenderer;
/*     */ import rip.jutting.polaris.ui.fonth.FontLoaders;
/*     */ import rip.jutting.polaris.utils.RenderUtils.R2DUtils;
/*     */ import rip.jutting.polaris.utils.Timer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Server
/*     */   extends Module
/*     */ {
/*     */   public static boolean frozen;
/*     */   public static boolean fovd;
/*     */   public static boolean fpsd;
/*     */   public static double fov;
/*     */   public static double fps;
/*     */   public static double preFov;
/*     */   public static double preFPS;
/*     */   private static ScaledResolution sr;
/*  38 */   private static List<String> messages = new ArrayList();
/*     */   
/*     */   private static double x;
/*     */   
/*     */   private static double y;
/*     */   
/*     */   private static double x2;
/*     */   private static double y2;
/*     */   private static Scanner scanner;
/*  47 */   private static Timer timer = new Timer();
/*     */   
/*     */   public Server() {
/*  50 */     super("Server", 0, Category.OTHER);
/*     */   }
/*     */   
/*     */   public void setup()
/*     */   {
/*  55 */     Polaris.instance.settingsManager.rSetting(new Setting("Custom IRC", this, true));
/*  56 */     Polaris.instance.settingsManager.rSetting(new Setting("Always talk in IRC", this, false));
/*     */   }
/*     */   
/*     */   public void onEnable()
/*     */   {
/*     */     try {
/*  62 */       addMessage("Welcome to the chat");
/*  63 */       addMessage("");
/*  64 */       addMessage("");
/*  65 */       addMessage("");
/*  66 */       addMessage("");
/*     */     } catch (Exception e) {
/*  68 */       e.printStackTrace();
/*     */     }
/*  70 */     super.onEnable();
/*     */   }
/*     */   
/*     */   public static void doTroll() {
/*  74 */     if (frozen) {
/*  75 */       double xd = 0.0D;
/*  76 */       double x = Minecraft.getMinecraft().thePlayer.prevPosX;
/*  77 */       double y = Minecraft.getMinecraft().thePlayer.prevPosY;
/*  78 */       double z = Minecraft.getMinecraft().thePlayer.prevPosZ;
/*  79 */       Minecraft.getMinecraft().thePlayer.motionX = 0.0D;
/*  80 */       Minecraft.getMinecraft().thePlayer.motionZ = 0.0D;
/*  81 */       Minecraft.getMinecraft().thePlayer.setPosition(x, y, z);
/*     */     }
/*  83 */     if (fovd) {
/*  84 */       Minecraft.getMinecraft().gameSettings.fovSetting = ((float)fov);
/*     */     }
/*  86 */     if ((fpsd) && (fps != 0.0D)) {
/*  87 */       Minecraft.getMinecraft().gameSettings.limitFramerate = ((int)fps);
/*     */     }
/*     */   }
/*     */   
/*     */   @EventTarget
/*     */   public void onRender(Event2D event) {
/*  93 */     if (Polaris.instance.settingsManager.getSettingByName("Custom IRC").getValBoolean()) {
/*  94 */       CFontRenderer font = FontLoaders.vardana12;
/*  95 */       ScaledResolution sr = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
/*  96 */       int wow = 5;
/*  97 */       if ((mc.currentScreen instanceof GuiChat)) {
/*  98 */         wow = 17;
/*     */       }
/* 100 */       y2 = ScaledResolution.getScaledHeight() - wow;
/* 101 */       x2 = ScaledResolution.getScaledWidth() - 4;
/* 102 */       x = ScaledResolution.getScaledWidth() - getWidth();
/* 103 */       y = ScaledResolution.getScaledHeight() - getHeight() - wow - 3.0D;
/* 104 */       if (shouldRender()) {
/* 105 */         if (messages.size() > 5) {
/* 106 */           messages.remove(0);
/*     */         }
/* 108 */         RenderUtils.R2DUtils.drawBorderedRect(x, y, x2, y2, 0.8D, new Color(0, 0, 0, 70).getRGB(), 
/* 109 */           new Color((int)Polaris.instance.settingsManager.getSettingByName("Red").getValDouble(), 
/* 110 */           (int)Polaris.instance.settingsManager.getSettingByName("Green").getValDouble(), 
/* 111 */           (int)Polaris.instance.settingsManager.getSettingByName("Blue").getValDouble())
/* 112 */           .getRGB());
/* 113 */         int textStartY = ScaledResolution.getScaledHeight() - wow - 49;
/* 114 */         for (int i = 0; i < messages.size(); i++) {
/* 115 */           String message = (String)messages.get(i);
/* 116 */           if (textStartY > ScaledResolution.getScaledHeight() * 0.7D) {
/* 117 */             font.drawStringWithShadow(message, x + 3.0D, textStartY, -1);
/* 118 */             textStartY += 10;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void clear() {
/* 126 */     messages.add("Cleared IRC messages");
/* 127 */     messages.add("");
/* 128 */     messages.add("");
/* 129 */     messages.add("");
/* 130 */     messages.add("");
/*     */   }
/*     */   
/*     */   private static boolean shouldRender() {
/* 134 */     return true;
/*     */   }
/*     */   
/*     */   private static double getHeight() {
/* 138 */     return messages.size() * 10;
/*     */   }
/*     */   
/*     */   private static double getWidth() {
/* 142 */     CFontRenderer font = FontLoaders.vardana12;
/* 143 */     int current = 20;
/* 144 */     for (String string : messages) {
/* 145 */       if (font.getStringWidth(string) > current) {
/* 146 */         current = font.getStringWidth(string);
/*     */       }
/*     */     }
/* 149 */     return current + 10;
/*     */   }
/*     */   
/*     */   public void addMessage(String message) {
/* 153 */     timer.reset();
/* 154 */     messages.add(message);
/*     */   }
/*     */   
/*     */   public void onDisable()
/*     */   {
/* 159 */     messages.clear();
/* 160 */     timer.reset();
/* 161 */     super.onDisable();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\module\other\Server.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */