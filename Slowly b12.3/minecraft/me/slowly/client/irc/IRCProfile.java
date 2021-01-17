/*    */ package me.slowly.client.irc;
/*    */ 
/*    */ public class IRCProfile {
/*    */   private String name;
/*    */   private String mcName;
/*    */   private String status;
/*    */   
/*  8 */   public IRCProfile(String name, String mcName, String status) { this.name = name;
/*  9 */     this.mcName = mcName;
/* 10 */     this.status = status;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 14 */     return this.name;
/*    */   }
/*    */   
/*    */   public String getMcName() {
/* 18 */     return this.mcName;
/*    */   }
/*    */   
/*    */   public String getStatus() {
/* 22 */     return this.status;
/*    */   }
/*    */   
/*    */   public void setMcName(String mcName) {
/* 26 */     this.mcName = mcName;
/*    */   }
/*    */   
/*    */   public void setStatus(String status) {
/* 30 */     this.status = status;
/*    */   }
/*    */ }