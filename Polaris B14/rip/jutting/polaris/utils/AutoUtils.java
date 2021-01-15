/*    */ package rip.jutting.polaris.utils;
/*    */ 
/*    */ import java.awt.AWTException;
/*    */ import java.awt.Image;
/*    */ import java.awt.SystemTray;
/*    */ import java.awt.Toolkit;
/*    */ import java.awt.TrayIcon;
/*    */ import java.util.Random;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*    */ 
/*    */ public class AutoUtils
/*    */ {
/*    */   public static double setRandom(double min, double max)
/*    */   {
/* 17 */     Random random = new Random();
/* 18 */     return min + random.nextDouble() * (max - min);
/*    */   }
/*    */   
/*    */   public static float setRandom(float min, float max) {
/* 22 */     Random random = new Random();
/* 23 */     return min + random.nextFloat() * (max - min);
/*    */   }
/*    */   
/*    */   public static void displayTray(String title, String message) throws AWTException {
/* 27 */     SystemTray tray = SystemTray.getSystemTray();
/* 28 */     Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
/* 29 */     TrayIcon trayIcon = new TrayIcon(image, "lol");
/* 30 */     trayIcon.setImageAutoSize(true);
/* 31 */     trayIcon.setToolTip("xd");
/* 32 */     tray.add(trayIcon);
/*    */     
/* 34 */     trayIcon.displayMessage(title, message, java.awt.TrayIcon.MessageType.INFO);
/*    */   }
/*    */   
/*    */   public static void airwalk() {
/* 38 */     Minecraft.getMinecraft().thePlayer.cameraYaw = (0.090909086F * (float)Polaris.instance.settingsManager.getSettingByName("Bobbing").getValDouble());
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\utils\AutoUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */