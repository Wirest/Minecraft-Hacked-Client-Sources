package net.minecraft.server.management;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.IOUtils;

import com.google.common.base.Charsets;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.ProfileLookupCallback;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class PlayerProfileCache
{
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

    /**
     * A map between player usernames and {@link ProfileEntry profile entries}
     */
    private final Map usernameToProfileEntryMap = Maps.newHashMap();

    /**
     * A map between {@link UUID UUID's} and {@link ProfileEntry ProfileEntries}
     */
    private final Map uuidToProfileEntryMap = Maps.newHashMap();

    /** A list of all the cached {@link GameProfile GameProfiles} */
    private final LinkedList gameProfiles = Lists.newLinkedList();
    private final MinecraftServer mcServer;
    protected final Gson gson;
    private final File usercacheFile;
    private static final ParameterizedType TYPE = new ParameterizedType()
    {
        @Override
		public Type[] getActualTypeArguments()
        {
            return new Type[] {PlayerProfileCache.ProfileEntry.class};
        }
        @Override
		public Type getRawType()
        {
            return List.class;
        }
        @Override
		public Type getOwnerType()
        {
            return null;
        }
    };

    public PlayerProfileCache(MinecraftServer server, File cacheFile)
    {
        this.mcServer = server;
        this.usercacheFile = cacheFile;
        GsonBuilder var3 = new GsonBuilder();
        var3.registerTypeHierarchyAdapter(PlayerProfileCache.ProfileEntry.class, new PlayerProfileCache.Serializer(null));
        this.gson = var3.create();
        this.load();
    }

    /**
     * Get a GameProfile given the MinecraftServer and the player's username.

     *  The UUID of the GameProfile will <b>not</b> be null. If the server is offline, a UUID based on the hash of the
     * username will be used.
     *  
     * @param server The Minecraft Server
     * @param username The player's username
     */
    private static GameProfile getGameProfile(MinecraftServer server, String username)
    {
        final GameProfile[] var2 = new GameProfile[1];
        ProfileLookupCallback var3 = new ProfileLookupCallback()
        {
            @Override
			public void onProfileLookupSucceeded(GameProfile p_onProfileLookupSucceeded_1_)
            {
                var2[0] = p_onProfileLookupSucceeded_1_;
            }
            @Override
			public void onProfileLookupFailed(GameProfile p_onProfileLookupFailed_1_, Exception p_onProfileLookupFailed_2_)
            {
                var2[0] = null;
            }
        };
        server.getGameProfileRepository().findProfilesByNames(new String[] {username}, Agent.MINECRAFT, var3);

        if (!server.isServerInOnlineMode() && var2[0] == null)
        {
            UUID var4 = EntityPlayer.getUUID(new GameProfile((UUID)null, username));
            GameProfile var5 = new GameProfile(var4, username);
            var3.onProfileLookupSucceeded(var5);
        }

        return var2[0];
    }

    /**
     * Add an entry to this cache
     *  
     * @param gameProfile The entry's {@link GameProfile}
     */
    public void addEntry(GameProfile gameProfile)
    {
        this.addEntry(gameProfile, (Date)null);
    }

    /**
     * Add an entry to this cache
     *  
     * @param gameProfile The entry's {@link GameProfile}
     * @param expirationDate The expiration date for this entry. {@code null} is allowed, 1 month will be used in this
     * case.
     */
    private void addEntry(GameProfile gameProfile, Date expirationDate)
    {
        UUID var3 = gameProfile.getId();

        if (expirationDate == null)
        {
            Calendar var4 = Calendar.getInstance();
            var4.setTime(new Date());
            var4.add(2, 1);
            expirationDate = var4.getTime();
        }

        String var7 = gameProfile.getName().toLowerCase(Locale.ROOT);
        PlayerProfileCache.ProfileEntry var5 = new PlayerProfileCache.ProfileEntry(gameProfile, expirationDate, null);

        if (this.uuidToProfileEntryMap.containsKey(var3))
        {
            PlayerProfileCache.ProfileEntry var6 = (PlayerProfileCache.ProfileEntry)this.uuidToProfileEntryMap.get(var3);
            this.usernameToProfileEntryMap.remove(var6.getGameProfile().getName().toLowerCase(Locale.ROOT));
            this.usernameToProfileEntryMap.put(gameProfile.getName().toLowerCase(Locale.ROOT), var5);
            this.gameProfiles.remove(gameProfile);
        }
        else
        {
            this.uuidToProfileEntryMap.put(var3, var5);
            this.usernameToProfileEntryMap.put(var7, var5);
        }

        this.gameProfiles.addFirst(gameProfile);
    }

    /**
     * Get a player's GameProfile given their username. Mojang's server's will be contacted if the entry is not cached
     * locally.
     *  
     * @param username The player's username
     */
    public GameProfile getGameProfileForUsername(String username)
    {
        String var2 = username.toLowerCase(Locale.ROOT);
        PlayerProfileCache.ProfileEntry var3 = (PlayerProfileCache.ProfileEntry)this.usernameToProfileEntryMap.get(var2);

        if (var3 != null && (new Date()).getTime() >= var3.expirationDate.getTime())
        {
            this.uuidToProfileEntryMap.remove(var3.getGameProfile().getId());
            this.usernameToProfileEntryMap.remove(var3.getGameProfile().getName().toLowerCase(Locale.ROOT));
            this.gameProfiles.remove(var3.getGameProfile());
            var3 = null;
        }

        GameProfile var4;

        if (var3 != null)
        {
            var4 = var3.getGameProfile();
            this.gameProfiles.remove(var4);
            this.gameProfiles.addFirst(var4);
        }
        else
        {
            var4 = getGameProfile(this.mcServer, var2);

            if (var4 != null)
            {
                this.addEntry(var4);
                var3 = (PlayerProfileCache.ProfileEntry)this.usernameToProfileEntryMap.get(var2);
            }
        }

        this.save();
        return var3 == null ? null : var3.getGameProfile();
    }

    /**
     * Get an array of the usernames that are cached in this cache
     */
    public String[] getUsernames()
    {
        ArrayList var1 = Lists.newArrayList(this.usernameToProfileEntryMap.keySet());
        return (String[])var1.toArray(new String[var1.size()]);
    }

    /**
     * Get a player's {@link GameProfile} given their UUID
     *  
     * @param uuid The player's UUID
     */
    public GameProfile getProfileByUUID(UUID uuid)
    {
        PlayerProfileCache.ProfileEntry var2 = (PlayerProfileCache.ProfileEntry)this.uuidToProfileEntryMap.get(uuid);
        return var2 == null ? null : var2.getGameProfile();
    }

    /**
     * Get a {@link ProfileEntry} by UUID
     *  
     * @param uuid The UUID
     */
    private PlayerProfileCache.ProfileEntry getByUUID(UUID uuid)
    {
        PlayerProfileCache.ProfileEntry var2 = (PlayerProfileCache.ProfileEntry)this.uuidToProfileEntryMap.get(uuid);

        if (var2 != null)
        {
            GameProfile var3 = var2.getGameProfile();
            this.gameProfiles.remove(var3);
            this.gameProfiles.addFirst(var3);
        }

        return var2;
    }

    /**
     * Load the cached profiles from disk
     */
    public void load()
    {
        List var1 = null;
        BufferedReader var2 = null;
        label64:
        {
            try
            {
                var2 = Files.newReader(this.usercacheFile, Charsets.UTF_8);
                var1 = (List)this.gson.fromJson(var2, TYPE);
                break label64;
            }
            catch (FileNotFoundException var7)
            {
                ;
            }
            finally
            {
                IOUtils.closeQuietly(var2);
            }

            return;
        }

        if (var1 != null)
        {
            this.usernameToProfileEntryMap.clear();
            this.uuidToProfileEntryMap.clear();
            this.gameProfiles.clear();
            var1 = Lists.reverse(var1);
            Iterator var3 = var1.iterator();

            while (var3.hasNext())
            {
                PlayerProfileCache.ProfileEntry var4 = (PlayerProfileCache.ProfileEntry)var3.next();

                if (var4 != null)
                {
                    this.addEntry(var4.getGameProfile(), var4.getExpirationDate());
                }
            }
        }
    }

    /**
     * Save the cached profiles to disk
     */
    public void save()
    {
        String var1 = this.gson.toJson(this.getEntriesWithLimit(1000));
        BufferedWriter var2 = null;

        try
        {
            var2 = Files.newWriter(this.usercacheFile, Charsets.UTF_8);
            var2.write(var1);
            return;
        }
        catch (FileNotFoundException var8)
        {
            ;
        }
        catch (IOException var9)
        {
            return;
        }
        finally
        {
            IOUtils.closeQuietly(var2);
        }
    }

    /**
     * Get the {@link PlayerProfileCache.ProfileEntry entries} of this cache with a given limit
     *  
     * @param limitSize The maximum size of the returned list
     */
    private List getEntriesWithLimit(int limitSize)
    {
        ArrayList var2 = Lists.newArrayList();
        ArrayList var3 = Lists.newArrayList(Iterators.limit(this.gameProfiles.iterator(), limitSize));
        Iterator var4 = var3.iterator();

        while (var4.hasNext())
        {
            GameProfile var5 = (GameProfile)var4.next();
            PlayerProfileCache.ProfileEntry var6 = this.getByUUID(var5.getId());

            if (var6 != null)
            {
                var2.add(var6);
            }
        }

        return var2;
    }

    class ProfileEntry
    {
        private final GameProfile gameProfile;
        private final Date expirationDate;

        private ProfileEntry(GameProfile gameProfileIn, Date expirationDateIn)
        {
            this.gameProfile = gameProfileIn;
            this.expirationDate = expirationDateIn;
        }

        public GameProfile getGameProfile()
        {
            return this.gameProfile;
        }

        public Date getExpirationDate()
        {
            return this.expirationDate;
        }

        ProfileEntry(GameProfile p_i1166_2_, Date p_i1166_3_, Object p_i1166_4_)
        {
            this(p_i1166_2_, p_i1166_3_);
        }
    }

    class Serializer implements JsonDeserializer, JsonSerializer
    {

        private Serializer() {}

        public JsonElement doSerialize(PlayerProfileCache.ProfileEntry profileEntry, Type type, JsonSerializationContext serializationContext)
        {
            JsonObject var4 = new JsonObject();
            var4.addProperty("name", profileEntry.getGameProfile().getName());
            UUID var5 = profileEntry.getGameProfile().getId();
            var4.addProperty("uuid", var5 == null ? "" : var5.toString());
            var4.addProperty("expiresOn", PlayerProfileCache.dateFormat.format(profileEntry.getExpirationDate()));
            return var4;
        }

        public PlayerProfileCache.ProfileEntry doDeserialize(JsonElement jsonElement, Type type, JsonDeserializationContext deserializationContext)
        {
            if (jsonElement.isJsonObject())
            {
                JsonObject var4 = jsonElement.getAsJsonObject();
                JsonElement var5 = var4.get("name");
                JsonElement var6 = var4.get("uuid");
                JsonElement var7 = var4.get("expiresOn");

                if (var5 != null && var6 != null)
                {
                    String var8 = var6.getAsString();
                    String var9 = var5.getAsString();
                    Date var10 = null;

                    if (var7 != null)
                    {
                        try
                        {
                            var10 = PlayerProfileCache.dateFormat.parse(var7.getAsString());
                        }
                        catch (ParseException var14)
                        {
                            var10 = null;
                        }
                    }

                    if (var9 != null && var8 != null)
                    {
                        UUID var11;

                        try
                        {
                            var11 = UUID.fromString(var8);
                        }
                        catch (Throwable var13)
                        {
                            return null;
                        }

                        PlayerProfileCache.ProfileEntry var12 = PlayerProfileCache.this.new ProfileEntry(new GameProfile(var11, var9), var10, null);
                        return var12;
                    }
                    else
                    {
                        return null;
                    }
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }

        @Override
		public JsonElement serialize(Object p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
        {
            return this.doSerialize((PlayerProfileCache.ProfileEntry)p_serialize_1_, p_serialize_2_, p_serialize_3_);
        }

        @Override
		public Object deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_)
        {
            return this.doDeserialize(p_deserialize_1_, p_deserialize_2_, p_deserialize_3_);
        }

        Serializer(Object p_i46332_2_)
        {
            this();
        }
    }
}
