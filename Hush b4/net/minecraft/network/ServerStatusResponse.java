// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.network;

import com.google.gson.JsonArray;
import java.util.UUID;
import com.mojang.authlib.GameProfile;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import net.minecraft.util.IChatComponent;

public class ServerStatusResponse
{
    private IChatComponent serverMotd;
    private PlayerCountData playerCount;
    private MinecraftProtocolVersionIdentifier protocolVersion;
    private String favicon;
    
    public IChatComponent getServerDescription() {
        return this.serverMotd;
    }
    
    public void setServerDescription(final IChatComponent motd) {
        this.serverMotd = motd;
    }
    
    public PlayerCountData getPlayerCountData() {
        return this.playerCount;
    }
    
    public void setPlayerCountData(final PlayerCountData countData) {
        this.playerCount = countData;
    }
    
    public MinecraftProtocolVersionIdentifier getProtocolVersionInfo() {
        return this.protocolVersion;
    }
    
    public void setProtocolVersionInfo(final MinecraftProtocolVersionIdentifier protocolVersionData) {
        this.protocolVersion = protocolVersionData;
    }
    
    public void setFavicon(final String faviconBlob) {
        this.favicon = faviconBlob;
    }
    
    public String getFavicon() {
        return this.favicon;
    }
    
    public static class Serializer implements JsonDeserializer<ServerStatusResponse>, JsonSerializer<ServerStatusResponse>
    {
        @Override
        public ServerStatusResponse deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            final JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "status");
            final ServerStatusResponse serverstatusresponse = new ServerStatusResponse();
            if (jsonobject.has("description")) {
                serverstatusresponse.setServerDescription(p_deserialize_3_.deserialize(jsonobject.get("description"), IChatComponent.class));
            }
            if (jsonobject.has("players")) {
                serverstatusresponse.setPlayerCountData(p_deserialize_3_.deserialize(jsonobject.get("players"), PlayerCountData.class));
            }
            if (jsonobject.has("version")) {
                serverstatusresponse.setProtocolVersionInfo(p_deserialize_3_.deserialize(jsonobject.get("version"), MinecraftProtocolVersionIdentifier.class));
            }
            if (jsonobject.has("favicon")) {
                serverstatusresponse.setFavicon(JsonUtils.getString(jsonobject, "favicon"));
            }
            return serverstatusresponse;
        }
        
        @Override
        public JsonElement serialize(final ServerStatusResponse p_serialize_1_, final Type p_serialize_2_, final JsonSerializationContext p_serialize_3_) {
            final JsonObject jsonobject = new JsonObject();
            if (p_serialize_1_.getServerDescription() != null) {
                jsonobject.add("description", p_serialize_3_.serialize(p_serialize_1_.getServerDescription()));
            }
            if (p_serialize_1_.getPlayerCountData() != null) {
                jsonobject.add("players", p_serialize_3_.serialize(p_serialize_1_.getPlayerCountData()));
            }
            if (p_serialize_1_.getProtocolVersionInfo() != null) {
                jsonobject.add("version", p_serialize_3_.serialize(p_serialize_1_.getProtocolVersionInfo()));
            }
            if (p_serialize_1_.getFavicon() != null) {
                jsonobject.addProperty("favicon", p_serialize_1_.getFavicon());
            }
            return jsonobject;
        }
    }
    
    public static class MinecraftProtocolVersionIdentifier
    {
        private final String name;
        private final int protocol;
        
        public MinecraftProtocolVersionIdentifier(final String nameIn, final int protocolIn) {
            this.name = nameIn;
            this.protocol = protocolIn;
        }
        
        public String getName() {
            return this.name;
        }
        
        public int getProtocol() {
            return this.protocol;
        }
        
        public static class Serializer implements JsonDeserializer<MinecraftProtocolVersionIdentifier>, JsonSerializer<MinecraftProtocolVersionIdentifier>
        {
            @Override
            public MinecraftProtocolVersionIdentifier deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
                final JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "version");
                return new MinecraftProtocolVersionIdentifier(JsonUtils.getString(jsonobject, "name"), JsonUtils.getInt(jsonobject, "protocol"));
            }
            
            @Override
            public JsonElement serialize(final MinecraftProtocolVersionIdentifier p_serialize_1_, final Type p_serialize_2_, final JsonSerializationContext p_serialize_3_) {
                final JsonObject jsonobject = new JsonObject();
                jsonobject.addProperty("name", p_serialize_1_.getName());
                jsonobject.addProperty("protocol", p_serialize_1_.getProtocol());
                return jsonobject;
            }
        }
    }
    
    public static class PlayerCountData
    {
        private final int maxPlayers;
        private final int onlinePlayerCount;
        private GameProfile[] players;
        
        public PlayerCountData(final int maxOnlinePlayers, final int onlinePlayers) {
            this.maxPlayers = maxOnlinePlayers;
            this.onlinePlayerCount = onlinePlayers;
        }
        
        public int getMaxPlayers() {
            return this.maxPlayers;
        }
        
        public int getOnlinePlayerCount() {
            return this.onlinePlayerCount;
        }
        
        public GameProfile[] getPlayers() {
            return this.players;
        }
        
        public void setPlayers(final GameProfile[] playersIn) {
            this.players = playersIn;
        }
        
        public static class Serializer implements JsonDeserializer<PlayerCountData>, JsonSerializer<PlayerCountData>
        {
            @Override
            public PlayerCountData deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
                final JsonObject jsonobject = JsonUtils.getJsonObject(p_deserialize_1_, "players");
                final PlayerCountData serverstatusresponse$playercountdata = new PlayerCountData(JsonUtils.getInt(jsonobject, "max"), JsonUtils.getInt(jsonobject, "online"));
                if (JsonUtils.isJsonArray(jsonobject, "sample")) {
                    final JsonArray jsonarray = JsonUtils.getJsonArray(jsonobject, "sample");
                    if (jsonarray.size() > 0) {
                        final GameProfile[] agameprofile = new GameProfile[jsonarray.size()];
                        for (int i = 0; i < agameprofile.length; ++i) {
                            final JsonObject jsonobject2 = JsonUtils.getJsonObject(jsonarray.get(i), "player[" + i + "]");
                            final String s = JsonUtils.getString(jsonobject2, "id");
                            agameprofile[i] = new GameProfile(UUID.fromString(s), JsonUtils.getString(jsonobject2, "name"));
                        }
                        serverstatusresponse$playercountdata.setPlayers(agameprofile);
                    }
                }
                return serverstatusresponse$playercountdata;
            }
            
            @Override
            public JsonElement serialize(final PlayerCountData p_serialize_1_, final Type p_serialize_2_, final JsonSerializationContext p_serialize_3_) {
                final JsonObject jsonobject = new JsonObject();
                jsonobject.addProperty("max", p_serialize_1_.getMaxPlayers());
                jsonobject.addProperty("online", p_serialize_1_.getOnlinePlayerCount());
                if (p_serialize_1_.getPlayers() != null && p_serialize_1_.getPlayers().length > 0) {
                    final JsonArray jsonarray = new JsonArray();
                    for (int i = 0; i < p_serialize_1_.getPlayers().length; ++i) {
                        final JsonObject jsonobject2 = new JsonObject();
                        final UUID uuid = p_serialize_1_.getPlayers()[i].getId();
                        jsonobject2.addProperty("id", (uuid == null) ? "" : uuid.toString());
                        jsonobject2.addProperty("name", p_serialize_1_.getPlayers()[i].getName());
                        jsonarray.add(jsonobject2);
                    }
                    jsonobject.add("sample", jsonarray);
                }
                return jsonobject;
            }
        }
    }
}
