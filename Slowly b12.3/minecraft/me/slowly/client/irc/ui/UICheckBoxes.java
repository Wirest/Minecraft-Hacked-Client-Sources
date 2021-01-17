/*    */ package me.slowly.client.irc.ui;
/*    */ 
/*    */ public class UICheckBoxes
/*    */ {
/*    */   public static UICheckBox ircChat;
/*    */   public static UICheckBox serverIPBox;
/*    */   
/*    */   public UICheckBoxes() {
/*  9 */     ircChat = new UICheckBox("Enable IRC-Chat", "Displays IRC Chat.");
/* 10 */     serverIPBox = new UICheckBox("Hide ServerIP", "Shows @everyone your Server IP.", false);
/*    */   }
/*    */ }