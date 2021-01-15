/*    */ package net.minecraft.server.management;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.util.UUID;
/*    */ 
/*    */ public class UserListWhitelistEntry extends UserListEntry<GameProfile>
/*    */ {
/*    */   public UserListWhitelistEntry(GameProfile profile)
/*    */   {
/* 11 */     super(profile);
/*    */   }
/*    */   
/*    */   public UserListWhitelistEntry(JsonObject p_i1130_1_)
/*    */   {
/* 16 */     super(gameProfileFromJsonObject(p_i1130_1_), p_i1130_1_);
/*    */   }
/*    */   
/*    */   protected void onSerialization(JsonObject data)
/*    */   {
/* 21 */     if (getValue() != null)
/*    */     {
/* 23 */       data.addProperty("uuid", ((GameProfile)getValue()).getId() == null ? "" : ((GameProfile)getValue()).getId().toString());
/* 24 */       data.addProperty("name", ((GameProfile)getValue()).getName());
/* 25 */       super.onSerialization(data);
/*    */     }
/*    */   }
/*    */   
/*    */   private static GameProfile gameProfileFromJsonObject(JsonObject p_152646_0_)
/*    */   {
/* 31 */     if ((p_152646_0_.has("uuid")) && (p_152646_0_.has("name")))
/*    */     {
/* 33 */       String s = p_152646_0_.get("uuid").getAsString();
/*    */       
/*    */ 
/*    */       try
/*    */       {
/* 38 */         uuid = UUID.fromString(s);
/*    */       }
/*    */       catch (Throwable var4) {
/*    */         UUID uuid;
/* 42 */         return null;
/*    */       }
/*    */       UUID uuid;
/* 45 */       return new GameProfile(uuid, p_152646_0_.get("name").getAsString());
/*    */     }
/*    */     
/*    */ 
/* 49 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\server\management\UserListWhitelistEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */