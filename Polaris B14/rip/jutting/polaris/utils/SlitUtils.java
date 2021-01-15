/*    */ package rip.jutting.polaris.utils;
/*    */ 
/*    */ import java.awt.AWTException;
/*    */ import java.awt.Image;
/*    */ import java.awt.SystemTray;
/*    */ import java.awt.Toolkit;
/*    */ import java.awt.TrayIcon;
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import rip.jutting.polaris.event.events.EventPreMotionUpdate;
/*    */ 
/*    */ public class SlitUtils
/*    */ {
/*    */   public static double setRandom(double min, double max)
/*    */   {
/* 20 */     Random random = new Random();
/* 21 */     return min + random.nextDouble() * (max - min);
/*    */   }
/*    */   
/*    */   public static float setRandom(float min, float max) {
/* 25 */     Random random = new Random();
/* 26 */     return min + random.nextFloat() * (max - min);
/*    */   }
/*    */   
/*    */   public static void displayTray(String title, String message) throws AWTException {
/* 30 */     SystemTray tray = SystemTray.getSystemTray();
/* 31 */     Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
/* 32 */     TrayIcon trayIcon = new TrayIcon(image, "lol");
/* 33 */     trayIcon.setImageAutoSize(true);
/* 34 */     trayIcon.setToolTip("xd");
/* 35 */     tray.add(trayIcon);
/* 36 */     trayIcon.displayMessage(title, message, java.awt.TrayIcon.MessageType.INFO);
/*    */   }
/*    */   
/*    */   public static void airwalk() {
/* 40 */     Minecraft.getMinecraft().thePlayer.cameraYaw = 0.099999994F;
/*    */   }
/*    */   
/*    */   public static IBlockState getState(BlockPos pos) {
/* 44 */     return Minecraft.getMinecraft().theWorld.getBlockState(pos);
/*    */   }
/*    */   
/*    */   public static Block getBlock(double x, double y, double z) {
/* 48 */     return getState(new BlockPos(x, y, z)).getBlock();
/*    */   }
/*    */   
/*    */   public static Block getBlock(BlockPos pos) {
/* 52 */     return getState(pos).getBlock();
/*    */   }
/*    */   
/*    */   public static void setMoveSpeed(EventPreMotionUpdate event, double speed) {
/* 56 */     double forward = Minecraft.getMinecraft().thePlayer.moveForward;
/* 57 */     double strafe = Minecraft.getMinecraft().thePlayer.moveStrafing;
/* 58 */     float yaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
/* 59 */     if ((forward == 0.0D) && (strafe == 0.0D)) {
/* 60 */       event.setX(0.0D);
/* 61 */       event.setZ(0.0D);
/*    */     }
/*    */     else {
/* 64 */       if (forward != 0.0D) {
/* 65 */         if (strafe > 0.0D) {
/* 66 */           yaw += (forward > 0.0D ? -45 : 45);
/*    */         }
/* 68 */         else if (strafe < 0.0D) {
/* 69 */           yaw += (forward > 0.0D ? 45 : -45);
/*    */         }
/* 71 */         strafe = 0.0D;
/* 72 */         if (forward > 0.0D) {
/* 73 */           forward = 1.0D;
/*    */         }
/* 75 */         else if (forward < 0.0D) {
/* 76 */           forward = -1.0D;
/*    */         }
/*    */       }
/* 79 */       event.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)));
/* 80 */       event.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)));
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\utils\SlitUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */