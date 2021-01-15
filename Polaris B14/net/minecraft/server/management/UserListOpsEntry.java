/*    */ package net.minecraft.server.management;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.util.UUID;
/*    */ 
/*    */ public class UserListOpsEntry extends UserListEntry<GameProfile>
/*    */ {
/*    */   private final int field_152645_a;
/*    */   private final boolean field_183025_b;
/*    */   
/*    */   public UserListOpsEntry(GameProfile p_i46492_1_, int p_i46492_2_, boolean p_i46492_3_)
/*    */   {
/* 14 */     super(p_i46492_1_);
/* 15 */     this.field_152645_a = p_i46492_2_;
/* 16 */     this.field_183025_b = p_i46492_3_;
/*    */   }
/*    */   
/*    */   public UserListOpsEntry(JsonObject p_i1150_1_)
/*    */   {
/* 21 */     super(func_152643_b(p_i1150_1_), p_i1150_1_);
/* 22 */     this.field_152645_a = (p_i1150_1_.has("level") ? p_i1150_1_.get("level").getAsInt() : 0);
/* 23 */     this.field_183025_b = ((p_i1150_1_.has("bypassesPlayerLimit")) && (p_i1150_1_.get("bypassesPlayerLimit").getAsBoolean()));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getPermissionLevel()
/*    */   {
/* 31 */     return this.field_152645_a;
/*    */   }
/*    */   
/*    */   public boolean func_183024_b()
/*    */   {
/* 36 */     return this.field_183025_b;
/*    */   }
/*    */   
/*    */   protected void onSerialization(JsonObject data)
/*    */   {
/* 41 */     if (getValue() != null)
/*    */     {
/* 43 */       data.addProperty("uuid", ((GameProfile)getValue()).getId() == null ? "" : ((GameProfile)getValue()).getId().toString());
/* 44 */       data.addProperty("name", ((GameProfile)getValue()).getName());
/* 45 */       super.onSerialization(data);
/* 46 */       data.addProperty("level", Integer.valueOf(this.field_152645_a));
/* 47 */       data.addProperty("bypassesPlayerLimit", Boolean.valueOf(this.field_183025_b));
/*    */     }
/*    */   }
/*    */   
/*    */   private static GameProfile func_152643_b(JsonObject p_152643_0_)
/*    */   {
/* 53 */     if ((p_152643_0_.has("uuid")) && (p_152643_0_.has("name")))
/*    */     {
/* 55 */       String s = p_152643_0_.get("uuid").getAsString();
/*    */       
/*    */ 
/*    */       try
/*    */       {
/* 60 */         uuid = UUID.fromString(s);
/*    */       }
/*    */       catch (Throwable var4) {
/*    */         UUID uuid;
/* 64 */         return null;
/*    */       }
/*    */       UUID uuid;
/* 67 */       return new GameProfile(uuid, p_152643_0_.get("name").getAsString());
/*    */     }
/*    */     
/*    */ 
/* 71 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\server\management\UserListOpsEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */