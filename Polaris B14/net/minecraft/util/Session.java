/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import com.mojang.util.UUIDTypeAdapter;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ 
/*    */ public class Session
/*    */ {
/*    */   private final String username;
/*    */   private final String playerID;
/*    */   private final String token;
/*    */   private final Type sessionType;
/*    */   
/*    */   public Session(String usernameIn, String playerIDIn, String tokenIn, String sessionTypeIn)
/*    */   {
/* 18 */     this.username = usernameIn;
/* 19 */     this.playerID = playerIDIn;
/* 20 */     this.token = tokenIn;
/* 21 */     this.sessionType = Type.setSessionType(sessionTypeIn);
/*    */   }
/*    */   
/*    */   public String getSessionID()
/*    */   {
/* 26 */     return "token:" + this.token + ":" + this.playerID;
/*    */   }
/*    */   
/*    */   public String getPlayerID()
/*    */   {
/* 31 */     return this.playerID;
/*    */   }
/*    */   
/*    */   public String getUsername()
/*    */   {
/* 36 */     return this.username;
/*    */   }
/*    */   
/*    */   public String getToken()
/*    */   {
/* 41 */     return this.token;
/*    */   }
/*    */   
/*    */   public GameProfile getProfile()
/*    */   {
/*    */     try
/*    */     {
/* 48 */       UUID uuid = UUIDTypeAdapter.fromString(getPlayerID());
/* 49 */       return new GameProfile(uuid, getUsername());
/*    */     }
/*    */     catch (IllegalArgumentException var2) {}
/*    */     
/* 53 */     return new GameProfile(null, getUsername());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Type getSessionType()
/*    */   {
/* 62 */     return this.sessionType;
/*    */   }
/*    */   
/*    */   public static enum Type
/*    */   {
/* 67 */     LEGACY("legacy"), 
/* 68 */     MOJANG("mojang");
/*    */     
/*    */     private static final Map<String, Type> SESSION_TYPES;
/*    */     private final String sessionType;
/*    */     
/*    */     private Type(String sessionTypeIn)
/*    */     {
/* 75 */       this.sessionType = sessionTypeIn;
/*    */     }
/*    */     
/*    */     public static Type setSessionType(String sessionTypeIn)
/*    */     {
/* 80 */       return (Type)SESSION_TYPES.get(sessionTypeIn.toLowerCase());
/*    */     }
/*    */     
/*    */     static
/*    */     {
/* 70 */       SESSION_TYPES = Maps.newHashMap();
/*    */       
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       Type[] arrayOfType;
/*    */       
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 84 */       int j = (arrayOfType = values()).length; for (int i = 0; i < j; i++) { Type session$type = arrayOfType[i];
/*    */         
/* 86 */         SESSION_TYPES.put(session$type.sessionType, session$type);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\Session.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */