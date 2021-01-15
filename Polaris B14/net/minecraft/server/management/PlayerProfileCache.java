/*     */ package net.minecraft.server.management;
/*     */ 
/*     */ import com.google.common.base.Charsets;
/*     */ import com.google.common.collect.Iterators;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.io.Files;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.google.gson.JsonDeserializationContext;
/*     */ import com.google.gson.JsonDeserializer;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParseException;
/*     */ import com.google.gson.JsonSerializationContext;
/*     */ import com.google.gson.JsonSerializer;
/*     */ import com.mojang.authlib.Agent;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.GameProfileRepository;
/*     */ import com.mojang.authlib.ProfileLookupCallback;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlayerProfileCache
/*     */ {
/*  45 */   public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
/*  46 */   private final Map<String, ProfileEntry> usernameToProfileEntryMap = Maps.newHashMap();
/*  47 */   private final Map<UUID, ProfileEntry> uuidToProfileEntryMap = Maps.newHashMap();
/*  48 */   private final LinkedList<GameProfile> gameProfiles = Lists.newLinkedList();
/*     */   private final MinecraftServer mcServer;
/*     */   protected final Gson gson;
/*     */   private final File usercacheFile;
/*  52 */   private static final ParameterizedType TYPE = new ParameterizedType()
/*     */   {
/*     */     public Type[] getActualTypeArguments()
/*     */     {
/*  56 */       return new Type[] { PlayerProfileCache.ProfileEntry.class };
/*     */     }
/*     */     
/*     */     public Type getRawType() {
/*  60 */       return List.class;
/*     */     }
/*     */     
/*     */     public Type getOwnerType() {
/*  64 */       return null;
/*     */     }
/*     */   };
/*     */   
/*     */   public PlayerProfileCache(MinecraftServer server, File cacheFile)
/*     */   {
/*  70 */     this.mcServer = server;
/*  71 */     this.usercacheFile = cacheFile;
/*  72 */     GsonBuilder gsonbuilder = new GsonBuilder();
/*  73 */     gsonbuilder.registerTypeHierarchyAdapter(ProfileEntry.class, new Serializer(null));
/*  74 */     this.gson = gsonbuilder.create();
/*  75 */     load();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static GameProfile getGameProfile(MinecraftServer server, String username)
/*     */   {
/*  86 */     GameProfile[] agameprofile = new GameProfile[1];
/*  87 */     ProfileLookupCallback profilelookupcallback = new ProfileLookupCallback()
/*     */     {
/*     */       public void onProfileLookupSucceeded(GameProfile p_onProfileLookupSucceeded_1_)
/*     */       {
/*  91 */         PlayerProfileCache.this[0] = p_onProfileLookupSucceeded_1_;
/*     */       }
/*     */       
/*     */       public void onProfileLookupFailed(GameProfile p_onProfileLookupFailed_1_, Exception p_onProfileLookupFailed_2_) {
/*  95 */         PlayerProfileCache.this[0] = null;
/*     */       }
/*  97 */     };
/*  98 */     server.getGameProfileRepository().findProfilesByNames(new String[] { username }, Agent.MINECRAFT, profilelookupcallback);
/*     */     
/* 100 */     if ((!server.isServerInOnlineMode()) && (agameprofile[0] == null))
/*     */     {
/* 102 */       UUID uuid = EntityPlayer.getUUID(new GameProfile(null, username));
/* 103 */       GameProfile gameprofile = new GameProfile(uuid, username);
/* 104 */       profilelookupcallback.onProfileLookupSucceeded(gameprofile);
/*     */     }
/*     */     
/* 107 */     return agameprofile[0];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addEntry(GameProfile gameProfile)
/*     */   {
/* 115 */     addEntry(gameProfile, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void addEntry(GameProfile gameProfile, Date expirationDate)
/*     */   {
/* 123 */     UUID uuid = gameProfile.getId();
/*     */     
/* 125 */     if (expirationDate == null)
/*     */     {
/* 127 */       Calendar calendar = Calendar.getInstance();
/* 128 */       calendar.setTime(new Date());
/* 129 */       calendar.add(2, 1);
/* 130 */       expirationDate = calendar.getTime();
/*     */     }
/*     */     
/* 133 */     String s = gameProfile.getName().toLowerCase(Locale.ROOT);
/* 134 */     ProfileEntry playerprofilecache$profileentry = new ProfileEntry(gameProfile, expirationDate, null);
/*     */     
/* 136 */     if (this.uuidToProfileEntryMap.containsKey(uuid))
/*     */     {
/* 138 */       ProfileEntry playerprofilecache$profileentry1 = (ProfileEntry)this.uuidToProfileEntryMap.get(uuid);
/* 139 */       this.usernameToProfileEntryMap.remove(playerprofilecache$profileentry1.getGameProfile().getName().toLowerCase(Locale.ROOT));
/* 140 */       this.gameProfiles.remove(gameProfile);
/*     */     }
/*     */     
/* 143 */     this.usernameToProfileEntryMap.put(gameProfile.getName().toLowerCase(Locale.ROOT), playerprofilecache$profileentry);
/* 144 */     this.uuidToProfileEntryMap.put(uuid, playerprofilecache$profileentry);
/* 145 */     this.gameProfiles.addFirst(gameProfile);
/* 146 */     save();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GameProfile getGameProfileForUsername(String username)
/*     */   {
/* 155 */     String s = username.toLowerCase(Locale.ROOT);
/* 156 */     ProfileEntry playerprofilecache$profileentry = (ProfileEntry)this.usernameToProfileEntryMap.get(s);
/*     */     
/* 158 */     if ((playerprofilecache$profileentry != null) && (new Date().getTime() >= playerprofilecache$profileentry.expirationDate.getTime()))
/*     */     {
/* 160 */       this.uuidToProfileEntryMap.remove(playerprofilecache$profileentry.getGameProfile().getId());
/* 161 */       this.usernameToProfileEntryMap.remove(playerprofilecache$profileentry.getGameProfile().getName().toLowerCase(Locale.ROOT));
/* 162 */       this.gameProfiles.remove(playerprofilecache$profileentry.getGameProfile());
/* 163 */       playerprofilecache$profileentry = null;
/*     */     }
/*     */     
/* 166 */     if (playerprofilecache$profileentry != null)
/*     */     {
/* 168 */       GameProfile gameprofile = playerprofilecache$profileentry.getGameProfile();
/* 169 */       this.gameProfiles.remove(gameprofile);
/* 170 */       this.gameProfiles.addFirst(gameprofile);
/*     */     }
/*     */     else
/*     */     {
/* 174 */       GameProfile gameprofile1 = getGameProfile(this.mcServer, s);
/*     */       
/* 176 */       if (gameprofile1 != null)
/*     */       {
/* 178 */         addEntry(gameprofile1);
/* 179 */         playerprofilecache$profileentry = (ProfileEntry)this.usernameToProfileEntryMap.get(s);
/*     */       }
/*     */     }
/*     */     
/* 183 */     save();
/* 184 */     return playerprofilecache$profileentry == null ? null : playerprofilecache$profileentry.getGameProfile();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getUsernames()
/*     */   {
/* 192 */     List<String> list = Lists.newArrayList(this.usernameToProfileEntryMap.keySet());
/* 193 */     return (String[])list.toArray(new String[list.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public GameProfile getProfileByUUID(UUID uuid)
/*     */   {
/* 201 */     ProfileEntry playerprofilecache$profileentry = (ProfileEntry)this.uuidToProfileEntryMap.get(uuid);
/* 202 */     return playerprofilecache$profileentry == null ? null : playerprofilecache$profileentry.getGameProfile();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private ProfileEntry getByUUID(UUID uuid)
/*     */   {
/* 210 */     ProfileEntry playerprofilecache$profileentry = (ProfileEntry)this.uuidToProfileEntryMap.get(uuid);
/*     */     
/* 212 */     if (playerprofilecache$profileentry != null)
/*     */     {
/* 214 */       GameProfile gameprofile = playerprofilecache$profileentry.getGameProfile();
/* 215 */       this.gameProfiles.remove(gameprofile);
/* 216 */       this.gameProfiles.addFirst(gameprofile);
/*     */     }
/*     */     
/* 219 */     return playerprofilecache$profileentry;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void load()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aconst_null
/*     */     //   1: astore_1
/*     */     //   2: aload_0
/*     */     //   3: getfield 73	net/minecraft/server/management/PlayerProfileCache:usercacheFile	Ljava/io/File;
/*     */     //   6: getstatic 296	com/google/common/base/Charsets:UTF_8	Ljava/nio/charset/Charset;
/*     */     //   9: invokestatic 302	com/google/common/io/Files:newReader	(Ljava/io/File;Ljava/nio/charset/Charset;)Ljava/io/BufferedReader;
/*     */     //   12: astore_1
/*     */     //   13: aload_0
/*     */     //   14: getfield 89	net/minecraft/server/management/PlayerProfileCache:gson	Lcom/google/gson/Gson;
/*     */     //   17: aload_1
/*     */     //   18: getstatic 49	net/minecraft/server/management/PlayerProfileCache:TYPE	Ljava/lang/reflect/ParameterizedType;
/*     */     //   21: invokevirtual 308	com/google/gson/Gson:fromJson	(Ljava/io/Reader;Ljava/lang/reflect/Type;)Ljava/lang/Object;
/*     */     //   24: checkcast 269	java/util/List
/*     */     //   27: astore_2
/*     */     //   28: aload_0
/*     */     //   29: getfield 59	net/minecraft/server/management/PlayerProfileCache:usernameToProfileEntryMap	Ljava/util/Map;
/*     */     //   32: invokeinterface 311 1 0
/*     */     //   37: aload_0
/*     */     //   38: getfield 61	net/minecraft/server/management/PlayerProfileCache:uuidToProfileEntryMap	Ljava/util/Map;
/*     */     //   41: invokeinterface 311 1 0
/*     */     //   46: aload_0
/*     */     //   47: getfield 69	net/minecraft/server/management/PlayerProfileCache:gameProfiles	Ljava/util/LinkedList;
/*     */     //   50: invokevirtual 312	java/util/LinkedList:clear	()V
/*     */     //   53: aload_2
/*     */     //   54: invokestatic 316	com/google/common/collect/Lists:reverse	(Ljava/util/List;)Ljava/util/List;
/*     */     //   57: invokeinterface 320 1 0
/*     */     //   62: astore 4
/*     */     //   64: goto +30 -> 94
/*     */     //   67: aload 4
/*     */     //   69: invokeinterface 328 1 0
/*     */     //   74: checkcast 11	net/minecraft/server/management/PlayerProfileCache$ProfileEntry
/*     */     //   77: astore_3
/*     */     //   78: aload_3
/*     */     //   79: ifnull +15 -> 94
/*     */     //   82: aload_0
/*     */     //   83: aload_3
/*     */     //   84: invokevirtual 217	net/minecraft/server/management/PlayerProfileCache$ProfileEntry:getGameProfile	()Lcom/mojang/authlib/GameProfile;
/*     */     //   87: aload_3
/*     */     //   88: invokevirtual 331	net/minecraft/server/management/PlayerProfileCache$ProfileEntry:getExpirationDate	()Ljava/util/Date;
/*     */     //   91: invokespecial 159	net/minecraft/server/management/PlayerProfileCache:addEntry	(Lcom/mojang/authlib/GameProfile;Ljava/util/Date;)V
/*     */     //   94: aload 4
/*     */     //   96: invokeinterface 334 1 0
/*     */     //   101: ifne -34 -> 67
/*     */     //   104: goto +28 -> 132
/*     */     //   107: astore_2
/*     */     //   108: aload_1
/*     */     //   109: invokestatic 340	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Reader;)V
/*     */     //   112: goto +24 -> 136
/*     */     //   115: astore_2
/*     */     //   116: aload_1
/*     */     //   117: invokestatic 340	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Reader;)V
/*     */     //   120: goto +16 -> 136
/*     */     //   123: astore 5
/*     */     //   125: aload_1
/*     */     //   126: invokestatic 340	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Reader;)V
/*     */     //   129: aload 5
/*     */     //   131: athrow
/*     */     //   132: aload_1
/*     */     //   133: invokestatic 340	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Reader;)V
/*     */     //   136: return
/*     */     // Line number table:
/*     */     //   Java source line #227	-> byte code offset #0
/*     */     //   Java source line #231	-> byte code offset #2
/*     */     //   Java source line #232	-> byte code offset #13
/*     */     //   Java source line #233	-> byte code offset #28
/*     */     //   Java source line #234	-> byte code offset #37
/*     */     //   Java source line #235	-> byte code offset #46
/*     */     //   Java source line #237	-> byte code offset #53
/*     */     //   Java source line #239	-> byte code offset #78
/*     */     //   Java source line #241	-> byte code offset #82
/*     */     //   Java source line #237	-> byte code offset #94
/*     */     //   Java source line #244	-> byte code offset #104
/*     */     //   Java source line #245	-> byte code offset #107
/*     */     //   Java source line #255	-> byte code offset #108
/*     */     //   Java source line #249	-> byte code offset #115
/*     */     //   Java source line #255	-> byte code offset #116
/*     */     //   Java source line #254	-> byte code offset #123
/*     */     //   Java source line #255	-> byte code offset #125
/*     */     //   Java source line #256	-> byte code offset #129
/*     */     //   Java source line #255	-> byte code offset #132
/*     */     //   Java source line #257	-> byte code offset #136
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	137	0	this	PlayerProfileCache
/*     */     //   1	132	1	bufferedreader	java.io.BufferedReader
/*     */     //   27	27	2	list	List<ProfileEntry>
/*     */     //   107	1	2	localFileNotFoundException	FileNotFoundException
/*     */     //   115	1	2	localJsonParseException	JsonParseException
/*     */     //   77	11	3	playerprofilecache$profileentry	ProfileEntry
/*     */     //   62	33	4	localIterator	java.util.Iterator
/*     */     //   123	7	5	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   2	104	107	java/io/FileNotFoundException
/*     */     //   2	104	115	com/google/gson/JsonParseException
/*     */     //   2	108	123	finally
/*     */     //   115	116	123	finally
/*     */   }
/*     */   
/*     */   public void save()
/*     */   {
/* 264 */     String s = this.gson.toJson(getEntriesWithLimit(1000));
/* 265 */     BufferedWriter bufferedwriter = null;
/*     */     
/*     */     try
/*     */     {
/* 269 */       bufferedwriter = Files.newWriter(this.usercacheFile, Charsets.UTF_8);
/* 270 */       bufferedwriter.write(s);
/* 271 */       return;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (FileNotFoundException localFileNotFoundException) {}catch (IOException var9) {}finally
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 283 */       IOUtils.closeQuietly(bufferedwriter);
/*     */     }
/*     */   }
/*     */   
/*     */   private List<ProfileEntry> getEntriesWithLimit(int limitSize)
/*     */   {
/* 289 */     ArrayList<ProfileEntry> arraylist = Lists.newArrayList();
/*     */     
/* 291 */     for (GameProfile gameprofile : Lists.newArrayList(Iterators.limit(this.gameProfiles.iterator(), limitSize)))
/*     */     {
/* 293 */       ProfileEntry playerprofilecache$profileentry = getByUUID(gameprofile.getId());
/*     */       
/* 295 */       if (playerprofilecache$profileentry != null)
/*     */       {
/* 297 */         arraylist.add(playerprofilecache$profileentry);
/*     */       }
/*     */     }
/*     */     
/* 301 */     return arraylist;
/*     */   }
/*     */   
/*     */   class ProfileEntry
/*     */   {
/*     */     private final GameProfile gameProfile;
/*     */     private final Date expirationDate;
/*     */     
/*     */     private ProfileEntry(GameProfile gameProfileIn, Date expirationDateIn)
/*     */     {
/* 311 */       this.gameProfile = gameProfileIn;
/* 312 */       this.expirationDate = expirationDateIn;
/*     */     }
/*     */     
/*     */     public GameProfile getGameProfile()
/*     */     {
/* 317 */       return this.gameProfile;
/*     */     }
/*     */     
/*     */     public Date getExpirationDate()
/*     */     {
/* 322 */       return this.expirationDate;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   class Serializer
/*     */     implements JsonDeserializer<PlayerProfileCache.ProfileEntry>, JsonSerializer<PlayerProfileCache.ProfileEntry>
/*     */   {
/*     */     private Serializer() {}
/*     */     
/*     */     public JsonElement serialize(PlayerProfileCache.ProfileEntry p_serialize_1_, Type p_serialize_2_, JsonSerializationContext p_serialize_3_)
/*     */     {
/* 334 */       JsonObject jsonobject = new JsonObject();
/* 335 */       jsonobject.addProperty("name", p_serialize_1_.getGameProfile().getName());
/* 336 */       UUID uuid = p_serialize_1_.getGameProfile().getId();
/* 337 */       jsonobject.addProperty("uuid", uuid == null ? "" : uuid.toString());
/* 338 */       jsonobject.addProperty("expiresOn", PlayerProfileCache.dateFormat.format(p_serialize_1_.getExpirationDate()));
/* 339 */       return jsonobject;
/*     */     }
/*     */     
/*     */     public PlayerProfileCache.ProfileEntry deserialize(JsonElement p_deserialize_1_, Type p_deserialize_2_, JsonDeserializationContext p_deserialize_3_) throws JsonParseException
/*     */     {
/* 344 */       if (p_deserialize_1_.isJsonObject())
/*     */       {
/* 346 */         JsonObject jsonobject = p_deserialize_1_.getAsJsonObject();
/* 347 */         JsonElement jsonelement = jsonobject.get("name");
/* 348 */         JsonElement jsonelement1 = jsonobject.get("uuid");
/* 349 */         JsonElement jsonelement2 = jsonobject.get("expiresOn");
/*     */         
/* 351 */         if ((jsonelement != null) && (jsonelement1 != null))
/*     */         {
/* 353 */           String s = jsonelement1.getAsString();
/* 354 */           String s1 = jsonelement.getAsString();
/* 355 */           Date date = null;
/*     */           
/* 357 */           if (jsonelement2 != null)
/*     */           {
/*     */             try
/*     */             {
/* 361 */               date = PlayerProfileCache.dateFormat.parse(jsonelement2.getAsString());
/*     */             }
/*     */             catch (ParseException var14)
/*     */             {
/* 365 */               date = null;
/*     */             }
/*     */           }
/*     */           
/* 369 */           if ((s1 != null) && (s != null))
/*     */           {
/*     */ 
/*     */             try
/*     */             {
/*     */ 
/* 375 */               uuid = UUID.fromString(s);
/*     */             }
/*     */             catch (Throwable var13) {
/*     */               UUID uuid;
/* 379 */               return null;
/*     */             }
/*     */             UUID uuid;
/* 382 */             PlayerProfileCache tmp125_122 = PlayerProfileCache.this;tmp125_122.getClass();PlayerProfileCache.ProfileEntry playerprofilecache$profileentry = new PlayerProfileCache.ProfileEntry(tmp125_122, new GameProfile(uuid, s1), date, null);
/* 383 */             return playerprofilecache$profileentry;
/*     */           }
/*     */           
/*     */ 
/* 387 */           return null;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 392 */         return null;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 397 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\server\management\PlayerProfileCache.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */