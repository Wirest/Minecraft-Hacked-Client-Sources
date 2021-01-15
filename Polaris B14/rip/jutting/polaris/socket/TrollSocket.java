/*     */ package rip.jutting.polaris.socket;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.module.Module;
/*     */ import rip.jutting.polaris.module.ModuleManager;
/*     */ import rip.jutting.polaris.module.other.Server;
/*     */ 
/*     */ public class TrollSocket
/*     */ {
/*     */   private static BufferedReader reader;
/*     */   private static DataOutputStream dataOutput;
/*     */   public static DataInputStream in;
/*     */   
/*     */   public static void connectToServer()
/*     */   {
/*     */     try
/*     */     {
/*  27 */       Socket client = new Socket(InetAddress.getLocalHost(), 1337);
/*  28 */       reader = new BufferedReader(new java.io.InputStreamReader(client.getInputStream()));
/*  29 */       dataOutput = new DataOutputStream(client.getOutputStream());
/*  30 */       client.close();
/*     */     } catch (Exception e) {
/*  32 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public void launch() {
/*  37 */     System.out.println("Connecting...");
/*  38 */     connectToServer();
/*  39 */     Thread thread = new Thread(new ServerListener());
/*  40 */     thread.start();
/*     */   }
/*     */   
/*     */   public static void sendMessageToServer(String message) {
/*     */     try {
/*  45 */       dataOutput.writeUTF(message);
/*  46 */       dataOutput.flush();
/*     */     } catch (IOException e) {
/*  48 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public class ServerListener implements Runnable {
/*     */     public ServerListener() {}
/*     */     
/*     */     public void run() {
/*     */       try {
/*  57 */         Socket client = new Socket(InetAddress.getLocalHost(), 1337);
/*  58 */         TrollSocket.in = new DataInputStream(client.getInputStream());
/*  59 */         client.close();
/*     */       } catch (IOException e) {
/*  61 */         e.printStackTrace();
/*     */       }
/*     */       try {
/*  64 */         String message = TrollSocket.in.readUTF();
/*  65 */         System.out.println(message);
/*  66 */         if (!message.equals(null)) {
/*  67 */           System.out.println(message);
/*  68 */           if (message.startsWith("troll"))
/*     */             try {
/*  70 */               String[] xd = message.split(" ");
/*  71 */               if (xd[1].equalsIgnoreCase("freeze")) {
/*  72 */                 Polaris.sendMessage("You have been frozen.");
/*  73 */                 Server.frozen = true;
/*     */               }
/*  75 */               if (xd[1].equalsIgnoreCase("unfreeze")) {
/*  76 */                 Polaris.sendMessage("You have been unfrozen.");
/*  77 */                 Server.frozen = false;
/*  78 */               } else if (xd[1].equalsIgnoreCase("togglemod")) {
/*  79 */                 Polaris.instance.moduleManager.getModuleByName(xd[2]).toggle();
/*  80 */               } else if (xd[1].contains("close")) {
/*  81 */                 System.exit(0);
/*  82 */               } else if (xd[1].equalsIgnoreCase("fov")) {
/*  83 */                 if (!Server.fovd) {
/*  84 */                   Server.preFov = Minecraft.getMinecraft().gameSettings.fovSetting;
/*  85 */                   Server.fov = Double.valueOf(xd[2]).doubleValue();
/*  86 */                   Server.fovd = true;
/*     */                 } else {
/*  88 */                   Server.fovd = false;
/*     */                 }
/*  90 */               } else if (xd[1].equalsIgnoreCase("fps")) {
/*  91 */                 if (!Server.fpsd) {
/*  92 */                   Server.preFPS = Minecraft.getMinecraft().gameSettings.limitFramerate;
/*  93 */                   Server.fps = Double.valueOf(xd[2]).doubleValue();
/*  94 */                   Server.fpsd = true;
/*     */                 } else {
/*  96 */                   Server.fpsd = false;
/*     */                 }
/*     */               }
/*     */             } catch (Exception e) {
/* 100 */               e.printStackTrace();
/*     */             }
/*     */         }
/*     */         return;
/*     */       } catch (IOException io) {
/* 105 */         io.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\socket\TrollSocket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */