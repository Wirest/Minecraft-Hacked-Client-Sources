package net.minecraft.network;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.authlib.GameProfile;
import java.lang.reflect.Type;
import java.util.UUID;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.JsonUtils;

public class ServerStatusResponse
{
    private IChatComponent serverMotd;
    private ServerStatusResponse.PlayerCountData playerCount;
    private ServerStatusResponse.MinecraftProtocolVersionIdentifier protocolVersion;
    private String favicon;
    private static final String __OBFID = "CL_00001385";

    public IChatComponent getServerDescription()
    {
        return this.serverMotd;
    }

    public void setServerDescription(IChatComponent motd)
    {
        this.serverMotd = motd;
    }

    public ServerStatusResponse.PlayerCountData getPlayerCountData()
    {
        return this.playerCount;
    }

    public void setPlayerCountData(ServerStatusResponse.PlayerCountData countData)
    {
        this.playerCount = countData;
    }

    public ServerStatusResponse.MinecraftProtocolVersionIdentifier getProtocolVersionInfo()
    {
        return this.protocolVersion;
    }

    public void setProtocolVersionInfo(ServerStatusResponse.MinecraftProtocolVersionIdentifier protocolVersionData)
    {
        this.protocolVersion = protocolVersionData;
    }

    public void setFavicon(String faviconBlob)
    {
        this.favicon = faviconBlob;
    }

    public String getFavicon()
    {
        return this.favicon;
    }

    public static class MinecraftProtocolVersionIdentifier
    {
        private final String name;
        private final int protocol;
        private static final String __OBFID = "CL_00001389";

        public MinecraftProtocolVersionIdentifier(String nameIn, int protocolIn)
        {
            this.name = nameIn;
            this.protocol = protocolIn;
        }

        public String getName()
        {
            return this.name;
        }

        public int getProtocol()
        {
            return this.protocol;
        }

        public static class Serializer implements JsonDeserializer, JsonSerializer
        {
            private static final String __OBFID = "CL_00001390";

            public ServerStatusResponse.MinecraftProtocolVersionIdentifier deserialize1(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
            {
                JsonObject var4 = JsonUtils.getElementAsJsonObject(p_deserialize_1_, "version");
                return new ServerStatusResponse.MinecraftProtocolVersionIdentifier(JsonUtils.getJsonObjectStringFieldValue(var4, "name"), JsonUtils.getJsonObjectIntegerFieldValue(var4, "protocol"));
            }

            public JsonElement serialize(ServerStatusResponse.MinecraftProtocolVersionIdentifier p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
            {
                JsonObject var4 = new JsonObject();
                var4.addProperty("name", p_serialize_1_.getName());
                var4.addProperty("protocol", Integer.valueOf(p_serialize_1_.getProtocol()));
                return var4;
            }

            public JsonElement serialize(Object p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
            {
                return this.serialize((ServerStatusResponse.MinecraftProtocolVersionIdentifier)p_serialize_1_, p_serialize_2_, p_serialize_3_);
            }

            public Object deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
            {
                return this.deserialize1(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
            }
        }
    }

    public static class PlayerCountData
    {
        private final int maxPlayers;
        private final int onlinePlayerCount;
        private GameProfile[] players;
        private static final String __OBFID = "CL_00001386";

        public PlayerCountData(int p_i45274_1_, int p_i45274_2_)
        {
            this.maxPlayers = p_i45274_1_;
            this.onlinePlayerCount = p_i45274_2_;
        }

        public int getMaxPlayers()
        {
            return this.maxPlayers;
        }

        public int getOnlinePlayerCount()
        {
            return this.onlinePlayerCount;
        }

        public GameProfile[] getPlayers()
        {
            return this.players;
        }

        public void setPlayers(GameProfile[] playersIn)
        {
            this.players = playersIn;
        }

        public static class Serializer implements JsonDeserializer, JsonSerializer
        {
            private static final String __OBFID = "CL_00001387";

            public ServerStatusResponse.PlayerCountData deserialize1(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
            {
                JsonObject var4 = JsonUtils.getElementAsJsonObject(p_deserialize_1_, "players");
                ServerStatusResponse.PlayerCountData var5 = new ServerStatusResponse.PlayerCountData(JsonUtils.getJsonObjectIntegerFieldValue(var4, "max"), JsonUtils.getJsonObjectIntegerFieldValue(var4, "online"));

                if (JsonUtils.jsonObjectFieldTypeIsArray(var4, "sample"))
                {
                    JsonArray var6 = JsonUtils.getJsonObjectJsonArrayField(var4, "sample");

                    if (var6.size() > 0)
                    {
                        GameProfile[] var7 = new GameProfile[var6.size()];

                        for (int var8 = 0; var8 < var7.length; ++var8)
                        {
                            JsonObject var9 = JsonUtils.getElementAsJsonObject(var6.get(var8), "player[" + var8 + "]");
                            String var10 = JsonUtils.getJsonObjectStringFieldValue(var9, "id");
                            var7[var8] = new GameProfile(UUID.fromString(var10), JsonUtils.getJsonObjectStringFieldValue(var9, "name"));
                        }

                        var5.setPlayers(var7);
                    }
                }

                return var5;
            }

            public JsonElement serialize(ServerStatusResponse.PlayerCountData p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
            {
                JsonObject var4 = new JsonObject();
                var4.addProperty("max", Integer.valueOf(p_serialize_1_.getMaxPlayers()));
                var4.addProperty("online", Integer.valueOf(p_serialize_1_.getOnlinePlayerCount()));

                if (p_serialize_1_.getPlayers() != null && p_serialize_1_.getPlayers().length > 0)
                {
                    JsonArray var5 = new JsonArray();

                    for (int var6 = 0; var6 < p_serialize_1_.getPlayers().length; ++var6)
                    {
                        JsonObject var7 = new JsonObject();
                        UUID var8 = p_serialize_1_.getPlayers()[var6].getId();
                        var7.addProperty("id", var8 == null ? "" : var8.toString());
                        var7.addProperty("name", p_serialize_1_.getPlayers()[var6].getName());
                        var5.add(var7);
                    }

                    var4.add("sample", var5);
                }

                return var4;
            }

            public JsonElement serialize(Object p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
            {
                return this.serialize((ServerStatusResponse.PlayerCountData)p_serialize_1_, p_serialize_2_, p_serialize_3_);
            }

            public Object deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
            {
                return this.deserialize1(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
            }
        }
    }

    public static class Serializer implements JsonDeserializer, JsonSerializer
    {
        private static final String __OBFID = "CL_00001388";

        public ServerStatusResponse deserialize1(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
        {
            JsonObject var4 = JsonUtils.getElementAsJsonObject(p_deserialize_1_, "status");
            ServerStatusResponse var5 = new ServerStatusResponse();

            if (var4.has("description"))
            {
                var5.setServerDescription((IChatComponent)p_deserialize_3_.deserialize(var4.get("description"), IChatComponent.class));
            }

            if (var4.has("players"))
            {
                var5.setPlayerCountData((ServerStatusResponse.PlayerCountData)p_deserialize_3_.deserialize(var4.get("players"), ServerStatusResponse.PlayerCountData.class));
            }

            if (var4.has("version"))
            {
                var5.setProtocolVersionInfo((ServerStatusResponse.MinecraftProtocolVersionIdentifier)p_deserialize_3_.deserialize(var4.get("version"), ServerStatusResponse.MinecraftProtocolVersionIdentifier.class));
            }

            if (var4.has("favicon"))
            {
                var5.setFavicon(JsonUtils.getJsonObjectStringFieldValue(var4, "favicon"));
            }

            return var5;
        }

        public JsonElement serialize(ServerStatusResponse p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
        {
            JsonObject var4 = new JsonObject();

            if (p_serialize_1_.getServerDescription() != null)
            {
                var4.add("description", p_serialize_3_.serialize(p_serialize_1_.getServerDescription()));
            }

            if (p_serialize_1_.getPlayerCountData() != null)
            {
                var4.add("players", p_serialize_3_.serialize(p_serialize_1_.getPlayerCountData()));
            }

            if (p_serialize_1_.getProtocolVersionInfo() != null)
            {
                var4.add("version", p_serialize_3_.serialize(p_serialize_1_.getProtocolVersionInfo()));
            }

            if (p_serialize_1_.getFavicon() != null)
            {
                var4.addProperty("favicon", p_serialize_1_.getFavicon());
            }

            return var4;
        }

        public JsonElement serialize(Object p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
        {
            return this.serialize((ServerStatusResponse)p_serialize_1_, p_serialize_2_, p_serialize_3_);
        }

        public Object deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
        {
            return this.deserialize1(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
        }
    }
}
