/*    */ package com.jagrosh.discordipc.entities;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum DiscordBuild
/*    */ {
/* 24 */   CANARY(
/*    */   
/*    */ 
/* 27 */     "//canary.discordapp.com/api"), 
/*    */   
/* 29 */   PTB(
/*    */   
/*    */ 
/* 32 */     "//ptb.discordapp.com/api"), 
/*    */   
/* 34 */   STABLE(
/*    */   
/*    */ 
/* 37 */     "//discordapp.com/api"), 
/*    */   
/* 39 */   ANY;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private final String endpoint;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private DiscordBuild(String endpoint)
/*    */   {
/* 52 */     this.endpoint = endpoint;
/*    */   }
/*    */   
/*    */   private DiscordBuild()
/*    */   {
/* 57 */     this(null);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static DiscordBuild from(String endpoint)
/*    */   {
/*    */     DiscordBuild[] arrayOfDiscordBuild;
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 72 */     int j = (arrayOfDiscordBuild = values()).length; for (int i = 0; i < j; i++) { DiscordBuild value = arrayOfDiscordBuild[i];
/*    */       
/* 74 */       if ((value.endpoint != null) && (value.endpoint.equals(endpoint)))
/*    */       {
/* 76 */         return value;
/*    */       }
/*    */     }
/* 79 */     return ANY;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\com\jagrosh\discordipc\entities\DiscordBuild.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */