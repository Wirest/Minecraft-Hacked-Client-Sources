/*    */ package net.minecraft.server.management;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.util.Date;
/*    */ import java.util.UUID;
/*    */ 
/*    */ public class UserListBansEntry extends BanEntry<GameProfile>
/*    */ {
/*    */   public UserListBansEntry(GameProfile profile)
/*    */   {
/* 12 */     this(profile, null, null, null, null);
/*    */   }
/*    */   
/*    */   public UserListBansEntry(GameProfile profile, Date startDate, String banner, Date endDate, String banReason)
/*    */   {
/* 17 */     super(profile, endDate, banner, endDate, banReason);
/*    */   }
/*    */   
/*    */   public UserListBansEntry(JsonObject p_i1136_1_)
/*    */   {
/* 22 */     super(func_152648_b(p_i1136_1_), p_i1136_1_);
/*    */   }
/*    */   
/*    */   protected void onSerialization(JsonObject data)
/*    */   {
/* 27 */     if (getValue() != null)
/*    */     {
/* 29 */       data.addProperty("uuid", ((GameProfile)getValue()).getId() == null ? "" : ((GameProfile)getValue()).getId().toString());
/* 30 */       data.addProperty("name", ((GameProfile)getValue()).getName());
/* 31 */       super.onSerialization(data);
/*    */     }
/*    */   }
/*    */   
/*    */   private static GameProfile func_152648_b(JsonObject p_152648_0_)
/*    */   {
/* 37 */     if ((p_152648_0_.has("uuid")) && (p_152648_0_.has("name")))
/*    */     {
/* 39 */       String s = p_152648_0_.get("uuid").getAsString();
/*    */       
/*    */ 
/*    */       try
/*    */       {
/* 44 */         uuid = UUID.fromString(s);
/*    */       }
/*    */       catch (Throwable var4) {
/*    */         UUID uuid;
/* 48 */         return null;
/*    */       }
/*    */       UUID uuid;
/* 51 */       return new GameProfile(uuid, p_152648_0_.get("name").getAsString());
/*    */     }
/*    */     
/*    */ 
/* 55 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\server\management\UserListBansEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */