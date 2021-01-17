/*     */ package me.slowly.client.irc;
/*     */ 
/*     */ import com.socket.Message;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.net.Socket;
/*     */ import java.util.ArrayList;
/*     */ import me.slowly.client.irc.ui.UICheckBox;
/*     */ import me.slowly.client.irc.ui.UICheckBoxes;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ 
/*     */ public class SocketClient implements Runnable
/*     */ {
/*     */   public int port;
/*     */   public String serverAddr;
/*     */   public Socket socket;
/*     */   public IRCChat ui;
/*     */   public ObjectInputStream In;
/*     */   public ObjectOutputStream Out;
/*     */   
/*     */   public SocketClient(IRCChat frame) throws IOException
/*     */   {
/*  25 */     this.ui = frame;
/*  26 */     this.serverAddr = this.ui.serverAddr;
/*  27 */     this.port = this.ui.port;
/*  28 */     this.socket = new Socket(java.net.InetAddress.getByName(this.serverAddr), this.port);
/*     */     
/*  30 */     this.Out = new ObjectOutputStream(this.socket.getOutputStream());
/*  31 */     this.Out.flush();
/*  32 */     this.In = new ObjectInputStream(this.socket.getInputStream());
/*     */   }
/*     */   
/*     */   private void append(String msg) {
/*  36 */     msg = msg.replace("\n", "");
/*  37 */     String prefix = "¡ì8[¡ì4IRC¡ì8]¡ìf ";
/*  38 */     if ((Minecraft.getMinecraft().theWorld != null) && (UICheckBoxes.ircChat.isEnabled())) {
/*  39 */       Minecraft.getMinecraft().thePlayer.addChatMessage(new net.minecraft.util.ChatComponentText(prefix + msg));
/*     */     }
/*     */   }
/*     */   
/*     */   public void run() {
/*  44 */     boolean keepRunning = true;
/*  45 */     while (keepRunning) {
/*     */       try {
/*  47 */         Message msg = (Message)this.In.readObject();
/*  48 */         if (msg.type.equals("message")) {
/*  49 */           if (msg.recipient.equals(this.ui.username)) {
/*  50 */             append("[" + msg.sender + " > Me] : " + msg.content + "\n");
/*     */           } else {
/*  52 */             append("[" + msg.sender + " > " + msg.recipient + "] : " + msg.content + "\n");
/*     */           }
/*     */         }
/*  55 */         else if (msg.type.equals("login")) {
/*  56 */           if (msg.content.equals("TRUE")) {
/*  57 */             append("[SERVER > Me] : Login Successful\n");
/*     */           } else {
/*  59 */             append("[SERVER > Me] : Login Failed\n");
/*     */           }
/*  61 */         } else if (msg.type.equals("info")) {
/*  62 */           String[] split = msg.content.split(":");
/*  63 */           if (split.length >= 3) {
/*  64 */             for (int i = 0; i < this.ui.userList.size(); i++) {
/*  65 */               if (((IRCProfile)this.ui.userList.get(i)).getName().equals(split[0])) {
/*  66 */                 ((IRCProfile)this.ui.userList.get(i)).setStatus(split[1]);
/*  67 */                 ((IRCProfile)this.ui.userList.get(i)).setMcName(split[2]);
/*     */               }
/*     */             }
/*     */           }
/*  71 */         } else if (msg.type.equals("newuser")) {
/*  72 */           if (!msg.content.equals(this.ui.username)) {
/*  73 */             boolean exists = false;
/*  74 */             for (int i = 0; i < this.ui.userList.size(); i++) {
/*  75 */               if (((IRCProfile)this.ui.userList.get(i)).getName().equals(msg.content)) {
/*  76 */                 exists = true;
/*  77 */                 break;
/*     */               }
/*     */             }
/*  80 */             if ((!exists) && (!msg.content.equals("")) && (!msg.content.equals(" "))) {
/*  81 */               this.ui.userList.add(new IRCProfile(msg.content, "Unknown", "Unknown"));
/*     */             }
/*     */           }
/*  84 */         } else if (msg.type.equals("signup")) {
/*  85 */           if (msg.content.equals("TRUE")) {
/*  86 */             append("[SERVER > Me] : Singup Successful\n");
/*     */           } else {
/*  88 */             append("[SERVER > Me] : Signup Failed\n");
/*     */           }
/*  90 */         } else if (msg.type.equals("signout")) {
/*  91 */           if (msg.content.equals(this.ui.username))
/*     */           {
/*  93 */             for (int i = 1; i < this.ui.userList.size(); i++) {
/*  94 */               this.ui.userList.remove(i);
/*     */             }
/*     */             
/*  97 */             this.ui.clientThread.stop();
/*     */           } else {
/*  99 */             for (int i = 0; i < this.ui.userList.size(); i++) {
/* 100 */               if (((IRCProfile)this.ui.userList.get(i)).getName().equalsIgnoreCase(msg.content))
/* 101 */                 this.ui.userList.remove(i);
/*     */             }
/*     */           }
/*     */         } else {
/* 105 */           append("[SERVER > Me] : Unknown message type\n");
/*     */         }
/*     */       } catch (Exception ex) {
/* 108 */         keepRunning = false;
/* 109 */         append("[Application > Me] : Connection Failure\n");
/*     */         
/* 111 */         for (int i = 1; i < this.ui.userList.size(); i++) {
/* 112 */           this.ui.userList.remove(i);
/*     */         }
/*     */         
/*     */ 
/* 116 */         ex.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void send(Message msg) {
/*     */     try {
/* 123 */       this.Out.writeObject(msg);
/* 124 */       this.Out.flush();
/*     */     }
/*     */     catch (IOException localIOException) {}
/*     */   }
/*     */   
/*     */   public void closeThread(Thread t)
/*     */   {
/* 131 */     t = null;
/*     */   }
/*     */ }
