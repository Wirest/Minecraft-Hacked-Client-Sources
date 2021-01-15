/*    */ package net.minecraft.server.management;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.io.File;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class UserListOps extends UserList<GameProfile, UserListOpsEntry>
/*    */ {
/*    */   public UserListOps(File saveFile)
/*    */   {
/* 11 */     super(saveFile);
/*    */   }
/*    */   
/*    */   protected UserListEntry<GameProfile> createEntry(com.google.gson.JsonObject entryData)
/*    */   {
/* 16 */     return new UserListOpsEntry(entryData);
/*    */   }
/*    */   
/*    */   public String[] getKeys()
/*    */   {
/* 21 */     String[] astring = new String[getValues().size()];
/* 22 */     int i = 0;
/*    */     
/* 24 */     for (UserListOpsEntry userlistopsentry : getValues().values())
/*    */     {
/* 26 */       astring[(i++)] = ((GameProfile)userlistopsentry.getValue()).getName();
/*    */     }
/*    */     
/* 29 */     return astring;
/*    */   }
/*    */   
/*    */   public boolean func_183026_b(GameProfile p_183026_1_)
/*    */   {
/* 34 */     UserListOpsEntry userlistopsentry = (UserListOpsEntry)getEntry(p_183026_1_);
/* 35 */     return userlistopsentry != null ? userlistopsentry.func_183024_b() : false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected String getObjectKey(GameProfile obj)
/*    */   {
/* 43 */     return obj.getId().toString();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public GameProfile getGameProfileFromName(String username)
/*    */   {
/* 51 */     for (UserListOpsEntry userlistopsentry : getValues().values())
/*    */     {
/* 53 */       if (username.equalsIgnoreCase(((GameProfile)userlistopsentry.getValue()).getName()))
/*    */       {
/* 55 */         return (GameProfile)userlistopsentry.getValue();
/*    */       }
/*    */     }
/*    */     
/* 59 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\server\management\UserListOps.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */