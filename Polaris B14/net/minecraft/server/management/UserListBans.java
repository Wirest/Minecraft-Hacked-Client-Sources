/*    */ package net.minecraft.server.management;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.io.File;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class UserListBans extends UserList<GameProfile, UserListBansEntry>
/*    */ {
/*    */   public UserListBans(File bansFile)
/*    */   {
/* 11 */     super(bansFile);
/*    */   }
/*    */   
/*    */   protected UserListEntry<GameProfile> createEntry(com.google.gson.JsonObject entryData)
/*    */   {
/* 16 */     return new UserListBansEntry(entryData);
/*    */   }
/*    */   
/*    */   public boolean isBanned(GameProfile profile)
/*    */   {
/* 21 */     return hasEntry(profile);
/*    */   }
/*    */   
/*    */   public String[] getKeys()
/*    */   {
/* 26 */     String[] astring = new String[getValues().size()];
/* 27 */     int i = 0;
/*    */     
/* 29 */     for (UserListBansEntry userlistbansentry : getValues().values())
/*    */     {
/* 31 */       astring[(i++)] = ((GameProfile)userlistbansentry.getValue()).getName();
/*    */     }
/*    */     
/* 34 */     return astring;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected String getObjectKey(GameProfile obj)
/*    */   {
/* 42 */     return obj.getId().toString();
/*    */   }
/*    */   
/*    */   public GameProfile isUsernameBanned(String username)
/*    */   {
/* 47 */     for (UserListBansEntry userlistbansentry : getValues().values())
/*    */     {
/* 49 */       if (username.equalsIgnoreCase(((GameProfile)userlistbansentry.getValue()).getName()))
/*    */       {
/* 51 */         return (GameProfile)userlistbansentry.getValue();
/*    */       }
/*    */     }
/*    */     
/* 55 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\server\management\UserListBans.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */