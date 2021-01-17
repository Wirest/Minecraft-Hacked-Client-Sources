// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.server.management;

import java.text.ParseException;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonDeserializer;
import java.util.ArrayList;
import com.google.common.collect.Iterators;
import java.io.BufferedWriter;
import java.io.Writer;
import java.io.IOException;
import java.util.Iterator;
import java.io.BufferedReader;
import org.apache.commons.io.IOUtils;
import com.google.gson.JsonParseException;
import java.io.FileNotFoundException;
import java.io.Reader;
import com.google.common.io.Files;
import com.google.common.base.Charsets;
import java.util.Locale;
import java.util.Calendar;
import java.util.Date;
import net.minecraft.entity.player.EntityPlayer;
import com.mojang.authlib.Agent;
import com.mojang.authlib.ProfileLookupCallback;
import com.google.gson.GsonBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.io.File;
import com.google.gson.Gson;
import net.minecraft.server.MinecraftServer;
import com.mojang.authlib.GameProfile;
import java.util.LinkedList;
import java.util.UUID;
import java.util.Map;
import java.text.SimpleDateFormat;

public class PlayerProfileCache
{
    public static final SimpleDateFormat dateFormat;
    private final Map<String, ProfileEntry> usernameToProfileEntryMap;
    private final Map<UUID, ProfileEntry> uuidToProfileEntryMap;
    private final LinkedList<GameProfile> gameProfiles;
    private final MinecraftServer mcServer;
    protected final Gson gson;
    private final File usercacheFile;
    private static final ParameterizedType TYPE;
    
    static {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        TYPE = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] { ProfileEntry.class };
            }
            
            @Override
            public Type getRawType() {
                return List.class;
            }
            
            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }
    
    public PlayerProfileCache(final MinecraftServer server, final File cacheFile) {
        this.usernameToProfileEntryMap = (Map<String, ProfileEntry>)Maps.newHashMap();
        this.uuidToProfileEntryMap = (Map<UUID, ProfileEntry>)Maps.newHashMap();
        this.gameProfiles = Lists.newLinkedList();
        this.mcServer = server;
        this.usercacheFile = cacheFile;
        final GsonBuilder gsonbuilder = new GsonBuilder();
        gsonbuilder.registerTypeHierarchyAdapter(ProfileEntry.class, new Serializer((Serializer)null));
        this.gson = gsonbuilder.create();
        this.load();
    }
    
    private static GameProfile getGameProfile(final MinecraftServer server, final String username) {
        final GameProfile[] agameprofile = { null };
        final ProfileLookupCallback profilelookupcallback = new ProfileLookupCallback() {
            @Override
            public void onProfileLookupSucceeded(final GameProfile p_onProfileLookupSucceeded_1_) {
                agameprofile[0] = p_onProfileLookupSucceeded_1_;
            }
            
            @Override
            public void onProfileLookupFailed(final GameProfile p_onProfileLookupFailed_1_, final Exception p_onProfileLookupFailed_2_) {
                agameprofile[0] = null;
            }
        };
        server.getGameProfileRepository().findProfilesByNames(new String[] { username }, Agent.MINECRAFT, profilelookupcallback);
        if (!server.isServerInOnlineMode() && agameprofile[0] == null) {
            final UUID uuid = EntityPlayer.getUUID(new GameProfile(null, username));
            final GameProfile gameprofile = new GameProfile(uuid, username);
            profilelookupcallback.onProfileLookupSucceeded(gameprofile);
        }
        return agameprofile[0];
    }
    
    public void addEntry(final GameProfile gameProfile) {
        this.addEntry(gameProfile, null);
    }
    
    private void addEntry(final GameProfile gameProfile, Date expirationDate) {
        final UUID uuid = gameProfile.getId();
        if (expirationDate == null) {
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(2, 1);
            expirationDate = calendar.getTime();
        }
        final String s = gameProfile.getName().toLowerCase(Locale.ROOT);
        final ProfileEntry playerprofilecache$profileentry = new ProfileEntry(gameProfile, expirationDate, (ProfileEntry)null);
        if (this.uuidToProfileEntryMap.containsKey(uuid)) {
            final ProfileEntry playerprofilecache$profileentry2 = this.uuidToProfileEntryMap.get(uuid);
            this.usernameToProfileEntryMap.remove(playerprofilecache$profileentry2.getGameProfile().getName().toLowerCase(Locale.ROOT));
            this.gameProfiles.remove(gameProfile);
        }
        this.usernameToProfileEntryMap.put(gameProfile.getName().toLowerCase(Locale.ROOT), playerprofilecache$profileentry);
        this.uuidToProfileEntryMap.put(uuid, playerprofilecache$profileentry);
        this.gameProfiles.addFirst(gameProfile);
        this.save();
    }
    
    public GameProfile getGameProfileForUsername(final String username) {
        final String s = username.toLowerCase(Locale.ROOT);
        ProfileEntry playerprofilecache$profileentry = this.usernameToProfileEntryMap.get(s);
        if (playerprofilecache$profileentry != null && new Date().getTime() >= playerprofilecache$profileentry.expirationDate.getTime()) {
            this.uuidToProfileEntryMap.remove(playerprofilecache$profileentry.getGameProfile().getId());
            this.usernameToProfileEntryMap.remove(playerprofilecache$profileentry.getGameProfile().getName().toLowerCase(Locale.ROOT));
            this.gameProfiles.remove(playerprofilecache$profileentry.getGameProfile());
            playerprofilecache$profileentry = null;
        }
        if (playerprofilecache$profileentry != null) {
            final GameProfile gameprofile = playerprofilecache$profileentry.getGameProfile();
            this.gameProfiles.remove(gameprofile);
            this.gameProfiles.addFirst(gameprofile);
        }
        else {
            final GameProfile gameprofile2 = getGameProfile(this.mcServer, s);
            if (gameprofile2 != null) {
                this.addEntry(gameprofile2);
                playerprofilecache$profileentry = this.usernameToProfileEntryMap.get(s);
            }
        }
        this.save();
        return (playerprofilecache$profileentry == null) ? null : playerprofilecache$profileentry.getGameProfile();
    }
    
    public String[] getUsernames() {
        final List<String> list = (List<String>)Lists.newArrayList((Iterable<?>)this.usernameToProfileEntryMap.keySet());
        return list.toArray(new String[list.size()]);
    }
    
    public GameProfile getProfileByUUID(final UUID uuid) {
        final ProfileEntry playerprofilecache$profileentry = this.uuidToProfileEntryMap.get(uuid);
        return (playerprofilecache$profileentry == null) ? null : playerprofilecache$profileentry.getGameProfile();
    }
    
    private ProfileEntry getByUUID(final UUID uuid) {
        final ProfileEntry playerprofilecache$profileentry = this.uuidToProfileEntryMap.get(uuid);
        if (playerprofilecache$profileentry != null) {
            final GameProfile gameprofile = playerprofilecache$profileentry.getGameProfile();
            this.gameProfiles.remove(gameprofile);
            this.gameProfiles.addFirst(gameprofile);
        }
        return playerprofilecache$profileentry;
    }
    
    public void load() {
        BufferedReader bufferedreader = null;
        try {
            bufferedreader = Files.newReader(this.usercacheFile, Charsets.UTF_8);
            final List<ProfileEntry> list = this.gson.fromJson(bufferedreader, PlayerProfileCache.TYPE);
            this.usernameToProfileEntryMap.clear();
            this.uuidToProfileEntryMap.clear();
            this.gameProfiles.clear();
            for (final ProfileEntry playerprofilecache$profileentry : Lists.reverse(list)) {
                if (playerprofilecache$profileentry != null) {
                    this.addEntry(playerprofilecache$profileentry.getGameProfile(), playerprofilecache$profileentry.getExpirationDate());
                }
            }
        }
        catch (FileNotFoundException ex) {}
        catch (JsonParseException ex2) {}
        finally {
            IOUtils.closeQuietly(bufferedreader);
        }
        IOUtils.closeQuietly(bufferedreader);
    }
    
    public void save() {
        final String s = this.gson.toJson(this.getEntriesWithLimit(1000));
        BufferedWriter bufferedwriter = null;
        try {
            bufferedwriter = Files.newWriter(this.usercacheFile, Charsets.UTF_8);
            bufferedwriter.write(s);
        }
        catch (FileNotFoundException ex) {}
        catch (IOException var9) {}
        finally {
            IOUtils.closeQuietly(bufferedwriter);
        }
    }
    
    private List<ProfileEntry> getEntriesWithLimit(final int limitSize) {
        final ArrayList<ProfileEntry> arraylist = Lists.newArrayList();
        for (final GameProfile gameprofile : Lists.newArrayList((Iterator<? extends GameProfile>)Iterators.limit(this.gameProfiles.iterator(), limitSize))) {
            final ProfileEntry playerprofilecache$profileentry = this.getByUUID(gameprofile.getId());
            if (playerprofilecache$profileentry != null) {
                arraylist.add(playerprofilecache$profileentry);
            }
        }
        return arraylist;
    }
    
    class Serializer implements JsonDeserializer<ProfileEntry>, JsonSerializer<ProfileEntry>
    {
        private Serializer() {
        }
        
        @Override
        public JsonElement serialize(final ProfileEntry p_serialize_1_, final Type p_serialize_2_, final JsonSerializationContext p_serialize_3_) {
            final JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("name", p_serialize_1_.getGameProfile().getName());
            final UUID uuid = p_serialize_1_.getGameProfile().getId();
            jsonobject.addProperty("uuid", (uuid == null) ? "" : uuid.toString());
            jsonobject.addProperty("expiresOn", PlayerProfileCache.dateFormat.format(p_serialize_1_.getExpirationDate()));
            return jsonobject;
        }
        
        @Override
        public ProfileEntry deserialize(final JsonElement p_deserialize_1_, final Type p_deserialize_2_, final JsonDeserializationContext p_deserialize_3_) throws JsonParseException {
            if (!p_deserialize_1_.isJsonObject()) {
                return null;
            }
            final JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
            final JsonElement jsonelement = jsonobject.get("name");
            final JsonElement jsonelement2 = jsonobject.get("uuid");
            final JsonElement jsonelement3 = jsonobject.get("expiresOn");
            if (jsonelement == null || jsonelement2 == null) {
                return null;
            }
            final String s = jsonelement2.getAsString();
            final String s2 = jsonelement.getAsString();
            Date date = null;
            if (jsonelement3 != null) {
                try {
                    date = PlayerProfileCache.dateFormat.parse(jsonelement3.getAsString());
                }
                catch (ParseException var14) {
                    date = null;
                }
            }
            if (s2 != null && s != null) {
                UUID uuid;
                try {
                    uuid = UUID.fromString(s);
                }
                catch (Throwable var15) {
                    return null;
                }
                final PlayerProfileCache this$0 = PlayerProfileCache.this;
                this$0.getClass();
                final ProfileEntry playerprofilecache$profileentry = new ProfileEntry(new GameProfile(uuid, s2), date, (ProfileEntry)null);
                return playerprofilecache$profileentry;
            }
            return null;
        }
    }
    
    class ProfileEntry
    {
        private final GameProfile gameProfile;
        private final Date expirationDate;
        
        private ProfileEntry(final GameProfile gameProfileIn, final Date expirationDateIn) {
            this.gameProfile = gameProfileIn;
            this.expirationDate = expirationDateIn;
        }
        
        public GameProfile getGameProfile() {
            return this.gameProfile;
        }
        
        public Date getExpirationDate() {
            return this.expirationDate;
        }
    }
}
