/*    */ package rip.jutting.polaris.ui.protection;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.net.URL;
/*    */ import java.util.Scanner;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiTextField;
/*    */ import org.lwjgl.input.Keyboard;
/*    */ import rip.jutting.polaris.Polaris;
/*    */ import rip.jutting.polaris.module.Module;
/*    */ import rip.jutting.polaris.module.ModuleManager;
/*    */ import rip.jutting.polaris.ui.login.PasswordField;
/*    */ 
/*    */ public class AuthLoginThread extends Thread
/*    */ {
/*    */   public static java.awt.event.KeyEvent ke;
/* 17 */   public static int key = 1;
/* 18 */   private final Minecraft mc = Minecraft.getMinecraft();
/*    */   static String keytext;
/*    */   static String status;
/*    */   public static String username;
/*    */   
/*    */   public AuthLoginThread(String username, String password) {
/* 24 */     super("Alt Login Thread");
/* 25 */     keytext = password;
/* 26 */     status = "§7Waiting...";
/*    */   }
/*    */   
/*    */   public String getStatus() {
/* 30 */     return status;
/*    */   }
/*    */   
/*    */   public void run() {
/* 34 */     if ((!GuiAuth.username.equals(null)) && (!GuiAuth.key.getText().equals(null))) {
/* 35 */       status = "§eLogging in...";
/*    */       try
/*    */       {
/* 38 */         if (!auth()) {
/* 39 */           status = "§cLogin failed!";
/*    */         } else {
/* 41 */           status = "§aLogged in!";
/* 42 */           Polaris.valid = true;
/* 43 */           if (Polaris.instance.moduleManager.getModuleByName("Server").isToggled()) {
/* 44 */             rip.jutting.polaris.socket.ServerSocket.start();
/*    */           }
/*    */         }
/*    */       } catch (Exception var2) {
/* 48 */         var2.printStackTrace();
/*    */       }
/*    */     }
/*    */     else {
/* 52 */       status = "§cCant leave the username box empty.";
/*    */     }
/*    */   }
/*    */   
/*    */   public void keyPressed() {
/* 57 */     if (Keyboard.isKeyDown(64)) {
/* 58 */       this.mc.shutdown();
/*    */     }
/*    */   }
/*    */   
/*    */   private boolean auth() throws IOException {
/* 63 */     URL url = new URL("https://pastebin.com/raw/K7M7h2L1");
/* 64 */     Scanner s = new Scanner(url.openStream());
/* 65 */     boolean xd = false;
/* 66 */     if (xd) {
/* 67 */       s.close();
/*    */     }
/* 69 */     while (s.hasNext()) {
/* 70 */       String[] s2 = s.nextLine().split(":");
/* 71 */       if ((GuiAuth.username.getText().equals(s2[0])) && (GuiAuth.key.getText().equals(s2[1]))) {
/* 72 */         return true;
/*    */       }
/*    */     }
/* 75 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\ui\protection\AuthLoginThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */