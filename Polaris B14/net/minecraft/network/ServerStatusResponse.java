/*     */ package net.minecraft.network;
/*     */ 
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonDeserializer;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import com.google.gson.JsonSerializer;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.JsonUtils;
/*     */ 
/*     */ public class ServerStatusResponse
/*     */ {
/*     */   private IChatComponent serverMotd;
/*     */   private PlayerCountData playerCount;
/*     */   private MinecraftProtocolVersionIdentifier protocolVersion;
/*     */   private String favicon;
/*     */   
/*     */   public IChatComponent getServerDescription()
/*     */   {
/*  26 */     return this.serverMotd;
/*     */   }
/*     */   
/*     */   public void setServerDescription(IChatComponent motd)
/*     */   {
/*  31 */     this.serverMotd = motd;
/*     */   }
/*     */   
/*     */   public PlayerCountData getPlayerCountData()
/*     */   {
/*  36 */     return this.playerCount;
/*     */   }
/*     */   
/*     */   public void setPlayerCountData(PlayerCountData countData)
/*     */   {
/*  41 */     this.playerCount = countData;
/*     */   }
/*     */   
/*     */   public MinecraftProtocolVersionIdentifier getProtocolVersionInfo()
/*     */   {
/*  46 */     return this.protocolVersion;
/*     */   }
/*     */   
/*     */   public void setProtocolVersionInfo(MinecraftProtocolVersionIdentifier protocolVersionData)
/*     */   {
/*  51 */     this.protocolVersion = protocolVersionData;
/*     */   }
/*     */   
/*     */   public void setFavicon(String faviconBlob)
/*     */   {
/*  56 */     this.favicon = faviconBlob;
/*     */   }
/*     */   
/*     */   public String getFavicon()
/*     */   {
/*  61 */     return this.favicon;
/*     */   }
/*     */   
/*     */   public static class MinecraftProtocolVersionIdentifier
/*     */   {
/*     */     private final String name;
/*     */     private final int protocol;
/*     */     
/*     */     public MinecraftProtocolVersionIdentifier(String nameIn, int protocolIn)
/*     */     {
/*  71 */       this.name = nameIn;
/*  72 */       this.protocol = protocolIn;
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/*  77 */       return this.name;
/*     */     }
/*     */     
/*     */     public int getProtocol()
/*     */     {
/*  82 */       return this.protocol;
/*     */     }
/*     */     
/*     */     public static class Serializer implements JsonDeserializer<ServerStatusResponse.MinecraftProtocolVersionIdentifier>, JsonSerializer<ServerStatusResponse.MinecraftProtocolVersionIdentifier>
/*     */     {
/*     */       public ServerStatusResponse.MinecraftProtocolVersionIdentifier deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
/*     */       {
/*  89 */         JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "version");
/*  90 */         return new ServerStatusResponse.MinecraftProtocolVersionIdentifier(JsonUtils.getString(jsonobject, "name"), JsonUtils.getInt(jsonobject, "protocol"));
/*     */       }
/*     */       
/*     */       public JsonElement serialize(ServerStatusResponse.MinecraftProtocolVersionIdentifier p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
/*     */       {
/*  95 */         JsonObject jsonobject = new JsonObject();
/*  96 */         jsonobject.addProperty("name", p_serialize_1_.getName());
/*  97 */         jsonobject.addProperty("protocol", Integer.valueOf(p_serialize_1_.getProtocol()));
/*  98 */         return jsonobject;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static class PlayerCountData
/*     */   {
/*     */     private final int maxPlayers;
/*     */     private final int onlinePlayerCount;
/*     */     private GameProfile[] players;
/*     */     
/*     */     public PlayerCountData(int maxOnlinePlayers, int onlinePlayers)
/*     */     {
/* 111 */       this.maxPlayers = maxOnlinePlayers;
/* 112 */       this.onlinePlayerCount = onlinePlayers;
/*     */     }
/*     */     
/*     */     public int getMaxPlayers()
/*     */     {
/* 117 */       return this.maxPlayers;
/*     */     }
/*     */     
/*     */     public int getOnlinePlayerCount()
/*     */     {
/* 122 */       return this.onlinePlayerCount;
/*     */     }
/*     */     
/*     */     public GameProfile[] getPlayers()
/*     */     {
/* 127 */       return this.players;
/*     */     }
/*     */     
/*     */     public void setPlayers(GameProfile[] playersIn)
/*     */     {
/* 132 */       this.players = playersIn;
/*     */     }
/*     */     
/*     */     public static class Serializer implements JsonDeserializer<ServerStatusResponse.PlayerCountData>, JsonSerializer<ServerStatusResponse.PlayerCountData>
/*     */     {
/*     */       public ServerStatusResponse.PlayerCountData deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
/*     */       {
/* 139 */         JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "players");
/* 140 */         ServerStatusResponse.PlayerCountData serverstatusresponse$playercountdata = new ServerStatusResponse.PlayerCountData(JsonUtils.getInt(jsonobject, "max"), JsonUtils.getInt(jsonobject, "online"));
/*     */         
/* 142 */         if (JsonUtils.isJsonArray(jsonobject, "sample"))
/*     */         {
/* 144 */           JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "sample");
/*     */           
/* 146 */           if (jsonarray.size() > 0)
/*     */           {
/* 148 */             GameProfile[] agameprofile = new GameProfile[jsonarray.size()];
/*     */             
/* 150 */             for (int i = 0; i < agameprofile.length; i++)
/*     */             {
/* 152 */               JsonObject jsonobject1 = JsonUtils.getJsonObject(jsonarray.get(i), "player[" + i + "]");
/* 153 */               String s = JsonUtils.getString(jsonobject1, "id");
/* 154 */               agameprofile[i] = new GameProfile(UUID.fromString(s), JsonUtils.getString(jsonobject1, "name"));
/*     */             }
/*     */             
/* 157 */             serverstatusresponse$playercountdata.setPlayers(agameprofile);
/*     */           }
/*     */         }
/*     */         
/* 161 */         return serverstatusresponse$playercountdata;
/*     */       }
/*     */       
/*     */       public JsonElement serialize(ServerStatusResponse.PlayerCountData p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
/*     */       {
/* 166 */         JsonObject jsonobject = new JsonObject();
/* 167 */         jsonobject.addProperty("max", Integer.valueOf(p_serialize_1_.getMaxPlayers()));
/* 168 */         jsonobject.addProperty("online", Integer.valueOf(p_serialize_1_.getOnlinePlayerCount()));
/*     */         
/* 170 */         if ((p_serialize_1_.getPlayers() != null) && (p_serialize_1_.getPlayers().length > 0))
/*     */         {
/* 172 */           JsonArray jsonarray = new JsonArray();
/*     */           
/* 174 */           for (int i = 0; i < p_serialize_1_.getPlayers().length; i++)
/*     */           {
/* 176 */             JsonObject jsonobject1 = new JsonObject();
/* 177 */             UUID uuid = p_serialize_1_.getPlayers()[i].getId();
/* 178 */             jsonobject1.addProperty("id", uuid == null ? "" : uuid.toString());
/* 179 */             jsonobject1.addProperty("name", p_serialize_1_.getPlayers()[i].getName());
/* 180 */             jsonarray.add(jsonobject1);
/*     */           }
/*     */           
/* 183 */           jsonobject.add("sample", jsonarray);
/*     */         }
/*     */         
/* 186 */         return jsonobject;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Serializer implements JsonDeserializer<ServerStatusResponse>, JsonSerializer<ServerStatusResponse>
/*     */   {
/*     */     public ServerStatusResponse deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
/*     */     {
/* 195 */       JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "status");
/* 196 */       ServerStatusResponse serverstatusresponse = new ServerStatusResponse();
/*     */       
/* 198 */       if (jsonobject.has("description"))
/*     */       {
/* 200 */         serverstatusresponse.setServerDescription((IChatComponent)p_deserialize_3_.deserialize(jsonobject.get("description"), IChatComponent.class));
/*     */       }
/*     */       
/* 203 */       if (jsonobject.has("players"))
/*     */       {
/* 205 */         serverstatusresponse.setPlayerCountData((ServerStatusResponse.PlayerCountData)p_deserialize_3_.deserialize(jsonobject.get("players"), ServerStatusResponse.PlayerCountData.class));
/*     */       }
/*     */       
/* 208 */       if (jsonobject.has("version"))
/*     */       {
/* 210 */         serverstatusresponse.setProtocolVersionInfo((ServerStatusResponse.MinecraftProtocolVersionIdentifier)p_deserialize_3_.deserialize(jsonobject.get("version"), ServerStatusResponse.MinecraftProtocolVersionIdentifier.class));
/*     */       }
/*     */       
/* 213 */       if (jsonobject.has("favicon"))
/*     */       {
/* 215 */         serverstatusresponse.setFavicon(JsonUtils.getString(jsonobject, "favicon"));
/*     */       }
/*     */       
/* 218 */       return serverstatusresponse;
/*     */     }
/*     */     
/*     */     public JsonElement serialize(ServerStatusResponse p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
/*     */     {
/* 223 */       JsonObject jsonobject = new JsonObject();
/*     */       
/* 225 */       if (p_serialize_1_.getServerDescription() != null)
/*     */       {
/* 227 */         jsonobject.add("description", p_serialize_3_.serialize(p_serialize_1_.getServerDescription()));
/*     */       }
/*     */       
/* 230 */       if (p_serialize_1_.getPlayerCountData() != null)
/*     */       {
/* 232 */         jsonobject.add("players", p_serialize_3_.serialize(p_serialize_1_.getPlayerCountData()));
/*     */       }
/*     */       
/* 235 */       if (p_serialize_1_.getProtocolVersionInfo() != null)
/*     */       {
/* 237 */         jsonobject.add("version", p_serialize_3_.serialize(p_serialize_1_.getProtocolVersionInfo()));
/*     */       }
/*     */       
/* 240 */       if (p_serialize_1_.getFavicon() != null)
/*     */       {
/* 242 */         jsonobject.addProperty("favicon", p_serialize_1_.getFavicon());
/*     */       }
/*     */       
/* 245 */       return jsonobject;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\network\ServerStatusResponse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */