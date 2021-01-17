/*    */ package me.slowly.client.irc;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ public class IRCChat
/*    */ {
/*    */   public SocketClient client;
/*    */   public String serverAddr;
/*    */   public int port;
/*    */   public String username;
/*    */   public Thread clientThread;
/*    */   public ArrayList<IRCProfile> userList;
/*    */   
/*    */   private void setAALName() {}
/*    */   
/*    */   private void setDebugName() {}
/*    */   
/*    */   public void sendMessage(String msg, String user) {}
/*    */   
/*    */   public void sendPacket(String type, String content) {}
/*    */   
/*    */   private void append(String msg) {}
/*    */   
/*    */   public void logIn() {}
/*    */   
/*    */   public boolean isClientUser(String mcName)
/*    */   {
/* 87 */     return false;
/*    */   }
/*    */   
/*    */   public void connect() {}
/*    */ }