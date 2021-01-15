/*     */ package net.minecraft.client.audio;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Random;
/*     */ import net.minecraft.client.resources.IResource;
/*     */ import net.minecraft.client.resources.IResourceManager;
/*     */ import net.minecraft.client.resources.IResourceManagerReloadListener;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.ITickable;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SoundHandler
/*     */   implements IResourceManagerReloadListener, ITickable
/*     */ {
/*  31 */   private static final Logger logger = ;
/*  32 */   private static final Gson GSON = new GsonBuilder().registerTypeAdapter(SoundList.class, new SoundListSerializer()).create();
/*  33 */   private static final ParameterizedType TYPE = new ParameterizedType()
/*     */   {
/*     */     public Type[] getActualTypeArguments()
/*     */     {
/*  37 */       return new Type[] { String.class, SoundList.class };
/*     */     }
/*     */     
/*     */     public Type getRawType() {
/*  41 */       return Map.class;
/*     */     }
/*     */     
/*     */     public Type getOwnerType() {
/*  45 */       return null;
/*     */     }
/*     */   };
/*  48 */   public static final SoundPoolEntry missing_sound = new SoundPoolEntry(new ResourceLocation("meta:missing_sound"), 0.0D, 0.0D, false);
/*  49 */   private final SoundRegistry sndRegistry = new SoundRegistry();
/*     */   private final SoundManager sndManager;
/*     */   private final IResourceManager mcResourceManager;
/*     */   
/*     */   public SoundHandler(IResourceManager manager, GameSettings gameSettingsIn)
/*     */   {
/*  55 */     this.mcResourceManager = manager;
/*  56 */     this.sndManager = new SoundManager(this, gameSettingsIn);
/*     */   }
/*     */   
/*     */   public void onResourceManagerReload(IResourceManager resourceManager)
/*     */   {
/*  61 */     this.sndManager.reloadSoundSystem();
/*  62 */     this.sndRegistry.clearMap();
/*     */     
/*  64 */     for (String s : resourceManager.getResourceDomains())
/*     */     {
/*     */       try
/*     */       {
/*  68 */         for (IResource iresource : resourceManager.getAllResources(new ResourceLocation(s, "sounds.json")))
/*     */         {
/*     */           try
/*     */           {
/*  72 */             Map<String, SoundList> map = getSoundMap(iresource.getInputStream());
/*     */             
/*  74 */             for (Map.Entry<String, SoundList> entry : map.entrySet())
/*     */             {
/*  76 */               loadSoundResource(new ResourceLocation(s, (String)entry.getKey()), (SoundList)entry.getValue());
/*     */             }
/*     */           }
/*     */           catch (RuntimeException runtimeexception)
/*     */           {
/*  81 */             logger.warn("Invalid sounds.json", runtimeexception);
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (IOException localIOException) {}
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected Map<String, SoundList> getSoundMap(java.io.InputStream stream)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: getstatic 70	net/minecraft/client/audio/SoundHandler:GSON	Lcom/google/gson/Gson;
/*     */     //   3: new 202	java/io/InputStreamReader
/*     */     //   6: dup
/*     */     //   7: aload_1
/*     */     //   8: invokespecial 205	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;)V
/*     */     //   11: getstatic 73	net/minecraft/client/audio/SoundHandler:TYPE	Ljava/lang/reflect/ParameterizedType;
/*     */     //   14: invokevirtual 211	com/google/gson/Gson:fromJson	(Ljava/io/Reader;Ljava/lang/reflect/Type;)Ljava/lang/Object;
/*     */     //   17: checkcast 13	java/util/Map
/*     */     //   20: astore_2
/*     */     //   21: goto +10 -> 31
/*     */     //   24: astore_3
/*     */     //   25: aload_1
/*     */     //   26: invokestatic 218	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
/*     */     //   29: aload_3
/*     */     //   30: athrow
/*     */     //   31: aload_1
/*     */     //   32: invokestatic 218	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
/*     */     //   35: aload_2
/*     */     //   36: areturn
/*     */     // Line number table:
/*     */     //   Java source line #98	-> byte code offset #0
/*     */     //   Java source line #99	-> byte code offset #21
/*     */     //   Java source line #101	-> byte code offset #24
/*     */     //   Java source line #102	-> byte code offset #25
/*     */     //   Java source line #103	-> byte code offset #29
/*     */     //   Java source line #102	-> byte code offset #31
/*     */     //   Java source line #105	-> byte code offset #35
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	37	0	this	SoundHandler
/*     */     //   0	37	1	stream	java.io.InputStream
/*     */     //   20	2	2	map	Map
/*     */     //   31	5	2	map	Map
/*     */     //   24	6	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	24	24	finally
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   private void loadSoundResource(ResourceLocation location, SoundList sounds)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 94	net/minecraft/client/audio/SoundHandler:sndRegistry	Lnet/minecraft/client/audio/SoundRegistry;
/*     */     //   4: aload_1
/*     */     //   5: invokevirtual 226	net/minecraft/client/audio/SoundRegistry:containsKey	(Ljava/lang/Object;)Z
/*     */     //   8: ifeq +7 -> 15
/*     */     //   11: iconst_0
/*     */     //   12: goto +4 -> 16
/*     */     //   15: iconst_1
/*     */     //   16: istore_3
/*     */     //   17: iload_3
/*     */     //   18: ifne +26 -> 44
/*     */     //   21: aload_2
/*     */     //   22: invokevirtual 229	net/minecraft/client/audio/SoundList:canReplaceExisting	()Z
/*     */     //   25: ifne +19 -> 44
/*     */     //   28: aload_0
/*     */     //   29: getfield 94	net/minecraft/client/audio/SoundHandler:sndRegistry	Lnet/minecraft/client/audio/SoundRegistry;
/*     */     //   32: aload_1
/*     */     //   33: invokevirtual 233	net/minecraft/client/audio/SoundRegistry:getObject	(Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   36: checkcast 235	net/minecraft/client/audio/SoundEventAccessorComposite
/*     */     //   39: astore 4
/*     */     //   41: goto +50 -> 91
/*     */     //   44: iload_3
/*     */     //   45: ifne +21 -> 66
/*     */     //   48: getstatic 52	net/minecraft/client/audio/SoundHandler:logger	Lorg/apache/logging/log4j/Logger;
/*     */     //   51: ldc -19
/*     */     //   53: iconst_1
/*     */     //   54: anewarray 4	java/lang/Object
/*     */     //   57: dup
/*     */     //   58: iconst_0
/*     */     //   59: aload_1
/*     */     //   60: aastore
/*     */     //   61: invokeinterface 241 3 0
/*     */     //   66: new 235	net/minecraft/client/audio/SoundEventAccessorComposite
/*     */     //   69: dup
/*     */     //   70: aload_1
/*     */     //   71: dconst_1
/*     */     //   72: dconst_1
/*     */     //   73: aload_2
/*     */     //   74: invokevirtual 245	net/minecraft/client/audio/SoundList:getSoundCategory	()Lnet/minecraft/client/audio/SoundCategory;
/*     */     //   77: invokespecial 248	net/minecraft/client/audio/SoundEventAccessorComposite:<init>	(Lnet/minecraft/util/ResourceLocation;DDLnet/minecraft/client/audio/SoundCategory;)V
/*     */     //   80: astore 4
/*     */     //   82: aload_0
/*     */     //   83: getfield 94	net/minecraft/client/audio/SoundHandler:sndRegistry	Lnet/minecraft/client/audio/SoundRegistry;
/*     */     //   86: aload 4
/*     */     //   88: invokevirtual 252	net/minecraft/client/audio/SoundRegistry:registerSound	(Lnet/minecraft/client/audio/SoundEventAccessorComposite;)V
/*     */     //   91: aload_2
/*     */     //   92: invokevirtual 256	net/minecraft/client/audio/SoundList:getSoundList	()Ljava/util/List;
/*     */     //   95: invokeinterface 152 1 0
/*     */     //   100: astore 6
/*     */     //   102: goto +334 -> 436
/*     */     //   105: aload 6
/*     */     //   107: invokeinterface 138 1 0
/*     */     //   112: checkcast 20	net/minecraft/client/audio/SoundList$SoundEntry
/*     */     //   115: astore 5
/*     */     //   117: aload 5
/*     */     //   119: invokevirtual 260	net/minecraft/client/audio/SoundList$SoundEntry:getSoundEntryName	()Ljava/lang/String;
/*     */     //   122: astore 7
/*     */     //   124: new 77	net/minecraft/util/ResourceLocation
/*     */     //   127: dup
/*     */     //   128: aload 7
/*     */     //   130: invokespecial 82	net/minecraft/util/ResourceLocation:<init>	(Ljava/lang/String;)V
/*     */     //   133: astore 8
/*     */     //   135: aload 7
/*     */     //   137: ldc_w 262
/*     */     //   140: invokevirtual 266	java/lang/String:contains	(Ljava/lang/CharSequence;)Z
/*     */     //   143: ifeq +11 -> 154
/*     */     //   146: aload 8
/*     */     //   148: invokevirtual 269	net/minecraft/util/ResourceLocation:getResourceDomain	()Ljava/lang/String;
/*     */     //   151: goto +7 -> 158
/*     */     //   154: aload_1
/*     */     //   155: invokevirtual 269	net/minecraft/util/ResourceLocation:getResourceDomain	()Ljava/lang/String;
/*     */     //   158: astore 9
/*     */     //   160: invokestatic 272	net/minecraft/client/audio/SoundHandler:$SWITCH_TABLE$net$minecraft$client$audio$SoundList$SoundEntry$Type	()[I
/*     */     //   163: aload 5
/*     */     //   165: invokevirtual 276	net/minecraft/client/audio/SoundList$SoundEntry:getSoundEntryType	()Lnet/minecraft/client/audio/SoundList$SoundEntry$Type;
/*     */     //   168: invokevirtual 280	net/minecraft/client/audio/SoundList$SoundEntry$Type:ordinal	()I
/*     */     //   171: iaload
/*     */     //   172: tableswitch	default:+243->415, 1:+24->196, 2:+226->398
/*     */     //   196: new 77	net/minecraft/util/ResourceLocation
/*     */     //   199: dup
/*     */     //   200: aload 9
/*     */     //   202: new 282	java/lang/StringBuilder
/*     */     //   205: dup
/*     */     //   206: ldc_w 284
/*     */     //   209: invokespecial 285	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   212: aload 8
/*     */     //   214: invokevirtual 288	net/minecraft/util/ResourceLocation:getResourcePath	()Ljava/lang/String;
/*     */     //   217: invokevirtual 292	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   220: ldc_w 294
/*     */     //   223: invokevirtual 292	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   226: invokevirtual 297	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   229: invokespecial 145	net/minecraft/util/ResourceLocation:<init>	(Ljava/lang/String;Ljava/lang/String;)V
/*     */     //   232: astore 11
/*     */     //   234: aconst_null
/*     */     //   235: astore 12
/*     */     //   237: aload_0
/*     */     //   238: getfield 96	net/minecraft/client/audio/SoundHandler:mcResourceManager	Lnet/minecraft/client/resources/IResourceManager;
/*     */     //   241: aload 11
/*     */     //   243: invokeinterface 301 2 0
/*     */     //   248: invokeinterface 158 1 0
/*     */     //   253: astore 12
/*     */     //   255: goto +95 -> 350
/*     */     //   258: astore 13
/*     */     //   260: getstatic 52	net/minecraft/client/audio/SoundHandler:logger	Lorg/apache/logging/log4j/Logger;
/*     */     //   263: ldc_w 305
/*     */     //   266: iconst_2
/*     */     //   267: anewarray 4	java/lang/Object
/*     */     //   270: dup
/*     */     //   271: iconst_0
/*     */     //   272: aload 11
/*     */     //   274: aastore
/*     */     //   275: dup
/*     */     //   276: iconst_1
/*     */     //   277: aload_1
/*     */     //   278: aastore
/*     */     //   279: invokeinterface 307 3 0
/*     */     //   284: aload 12
/*     */     //   286: invokestatic 218	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
/*     */     //   289: goto +147 -> 436
/*     */     //   292: astore 13
/*     */     //   294: getstatic 52	net/minecraft/client/audio/SoundHandler:logger	Lorg/apache/logging/log4j/Logger;
/*     */     //   297: new 282	java/lang/StringBuilder
/*     */     //   300: dup
/*     */     //   301: ldc_w 309
/*     */     //   304: invokespecial 285	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   307: aload 11
/*     */     //   309: invokevirtual 312	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */     //   312: ldc_w 314
/*     */     //   315: invokevirtual 292	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   318: aload_1
/*     */     //   319: invokevirtual 312	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */     //   322: invokevirtual 297	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   325: aload 13
/*     */     //   327: invokeinterface 187 3 0
/*     */     //   332: aload 12
/*     */     //   334: invokestatic 218	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
/*     */     //   337: goto +99 -> 436
/*     */     //   340: astore 14
/*     */     //   342: aload 12
/*     */     //   344: invokestatic 218	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
/*     */     //   347: aload 14
/*     */     //   349: athrow
/*     */     //   350: aload 12
/*     */     //   352: invokestatic 218	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
/*     */     //   355: new 316	net/minecraft/client/audio/SoundEventAccessor
/*     */     //   358: dup
/*     */     //   359: new 75	net/minecraft/client/audio/SoundPoolEntry
/*     */     //   362: dup
/*     */     //   363: aload 11
/*     */     //   365: aload 5
/*     */     //   367: invokevirtual 320	net/minecraft/client/audio/SoundList$SoundEntry:getSoundEntryPitch	()F
/*     */     //   370: f2d
/*     */     //   371: aload 5
/*     */     //   373: invokevirtual 323	net/minecraft/client/audio/SoundList$SoundEntry:getSoundEntryVolume	()F
/*     */     //   376: f2d
/*     */     //   377: aload 5
/*     */     //   379: invokevirtual 326	net/minecraft/client/audio/SoundList$SoundEntry:isStreaming	()Z
/*     */     //   382: invokespecial 85	net/minecraft/client/audio/SoundPoolEntry:<init>	(Lnet/minecraft/util/ResourceLocation;DDZ)V
/*     */     //   385: aload 5
/*     */     //   387: invokevirtual 329	net/minecraft/client/audio/SoundList$SoundEntry:getSoundEntryWeight	()I
/*     */     //   390: invokespecial 332	net/minecraft/client/audio/SoundEventAccessor:<init>	(Lnet/minecraft/client/audio/SoundPoolEntry;I)V
/*     */     //   393: astore 10
/*     */     //   395: goto +31 -> 426
/*     */     //   398: new 18	net/minecraft/client/audio/SoundHandler$2
/*     */     //   401: dup
/*     */     //   402: aload_0
/*     */     //   403: aload 9
/*     */     //   405: aload 5
/*     */     //   407: invokespecial 335	net/minecraft/client/audio/SoundHandler$2:<init>	(Lnet/minecraft/client/audio/SoundHandler;Ljava/lang/String;Lnet/minecraft/client/audio/SoundList$SoundEntry;)V
/*     */     //   410: astore 10
/*     */     //   412: goto +14 -> 426
/*     */     //   415: new 337	java/lang/IllegalStateException
/*     */     //   418: dup
/*     */     //   419: ldc_w 339
/*     */     //   422: invokespecial 340	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
/*     */     //   425: athrow
/*     */     //   426: aload 4
/*     */     //   428: aload 10
/*     */     //   430: checkcast 342	net/minecraft/client/audio/ISoundEventAccessor
/*     */     //   433: invokevirtual 346	net/minecraft/client/audio/SoundEventAccessorComposite:addSoundToEventPool	(Lnet/minecraft/client/audio/ISoundEventAccessor;)V
/*     */     //   436: aload 6
/*     */     //   438: invokeinterface 179 1 0
/*     */     //   443: ifne -338 -> 105
/*     */     //   446: return
/*     */     // Line number table:
/*     */     //   Java source line #110	-> byte code offset #0
/*     */     //   Java source line #113	-> byte code offset #17
/*     */     //   Java source line #115	-> byte code offset #28
/*     */     //   Java source line #116	-> byte code offset #41
/*     */     //   Java source line #119	-> byte code offset #44
/*     */     //   Java source line #121	-> byte code offset #48
/*     */     //   Java source line #124	-> byte code offset #66
/*     */     //   Java source line #125	-> byte code offset #82
/*     */     //   Java source line #128	-> byte code offset #91
/*     */     //   Java source line #130	-> byte code offset #117
/*     */     //   Java source line #131	-> byte code offset #124
/*     */     //   Java source line #132	-> byte code offset #135
/*     */     //   Java source line #135	-> byte code offset #160
/*     */     //   Java source line #138	-> byte code offset #196
/*     */     //   Java source line #139	-> byte code offset #234
/*     */     //   Java source line #143	-> byte code offset #237
/*     */     //   Java source line #144	-> byte code offset #255
/*     */     //   Java source line #145	-> byte code offset #258
/*     */     //   Java source line #147	-> byte code offset #260
/*     */     //   Java source line #157	-> byte code offset #284
/*     */     //   Java source line #148	-> byte code offset #289
/*     */     //   Java source line #150	-> byte code offset #292
/*     */     //   Java source line #152	-> byte code offset #294
/*     */     //   Java source line #157	-> byte code offset #332
/*     */     //   Java source line #153	-> byte code offset #337
/*     */     //   Java source line #156	-> byte code offset #340
/*     */     //   Java source line #157	-> byte code offset #342
/*     */     //   Java source line #158	-> byte code offset #347
/*     */     //   Java source line #157	-> byte code offset #350
/*     */     //   Java source line #160	-> byte code offset #355
/*     */     //   Java source line #161	-> byte code offset #395
/*     */     //   Java source line #164	-> byte code offset #398
/*     */     //   Java source line #179	-> byte code offset #412
/*     */     //   Java source line #181	-> byte code offset #415
/*     */     //   Java source line #184	-> byte code offset #426
/*     */     //   Java source line #128	-> byte code offset #436
/*     */     //   Java source line #186	-> byte code offset #446
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	447	0	this	SoundHandler
/*     */     //   0	447	1	location	ResourceLocation
/*     */     //   0	447	2	sounds	SoundList
/*     */     //   16	29	3	flag	boolean
/*     */     //   39	3	4	soundeventaccessorcomposite	SoundEventAccessorComposite
/*     */     //   80	347	4	soundeventaccessorcomposite	SoundEventAccessorComposite
/*     */     //   115	291	5	soundlist$soundentry	SoundList.SoundEntry
/*     */     //   100	337	6	localIterator	java.util.Iterator
/*     */     //   122	14	7	s	String
/*     */     //   133	80	8	resourcelocation	ResourceLocation
/*     */     //   158	246	9	s1	String
/*     */     //   393	3	10	lvt_10_1_	Object
/*     */     //   410	3	10	lvt_10_1_	Object
/*     */     //   426	3	10	lvt_10_1_	Object
/*     */     //   232	132	11	resourcelocation1	ResourceLocation
/*     */     //   235	116	12	inputstream	java.io.InputStream
/*     */     //   258	3	13	var18	java.io.FileNotFoundException
/*     */     //   292	34	13	ioexception	IOException
/*     */     //   340	8	14	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   237	255	258	java/io/FileNotFoundException
/*     */     //   237	255	292	java/io/IOException
/*     */     //   237	284	340	finally
/*     */     //   292	332	340	finally
/*     */   }
/*     */   
/*     */   public SoundEventAccessorComposite getSound(ResourceLocation location)
/*     */   {
/* 190 */     return (SoundEventAccessorComposite)this.sndRegistry.getObject(location);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void playSound(ISound sound)
/*     */   {
/* 198 */     this.sndManager.playSound(sound);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void playDelayedSound(ISound sound, int delay)
/*     */   {
/* 206 */     this.sndManager.playDelayedSound(sound, delay);
/*     */   }
/*     */   
/*     */   public void setListener(EntityPlayer player, float p_147691_2_)
/*     */   {
/* 211 */     this.sndManager.setListener(player, p_147691_2_);
/*     */   }
/*     */   
/*     */   public void pauseSounds()
/*     */   {
/* 216 */     this.sndManager.pauseAllSounds();
/*     */   }
/*     */   
/*     */   public void stopSounds()
/*     */   {
/* 221 */     this.sndManager.stopAllSounds();
/*     */   }
/*     */   
/*     */   public void unloadSounds()
/*     */   {
/* 226 */     this.sndManager.unloadSoundSystem();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void update()
/*     */   {
/* 234 */     this.sndManager.updateAllSounds();
/*     */   }
/*     */   
/*     */   public void resumeSounds()
/*     */   {
/* 239 */     this.sndManager.resumeAllSounds();
/*     */   }
/*     */   
/*     */   public void setSoundLevel(SoundCategory category, float volume)
/*     */   {
/* 244 */     if ((category == SoundCategory.MASTER) && (volume <= 0.0F))
/*     */     {
/* 246 */       stopSounds();
/*     */     }
/*     */     
/* 249 */     this.sndManager.setSoundCategoryVolume(category, volume);
/*     */   }
/*     */   
/*     */   public void stopSound(ISound p_147683_1_)
/*     */   {
/* 254 */     this.sndManager.stopSound(p_147683_1_);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SoundEventAccessorComposite getRandomSoundFromCategories(SoundCategory... categories)
/*     */   {
/* 262 */     List<SoundEventAccessorComposite> list = Lists.newArrayList();
/*     */     
/* 264 */     for (ResourceLocation resourcelocation : this.sndRegistry.getKeys())
/*     */     {
/* 266 */       SoundEventAccessorComposite soundeventaccessorcomposite = (SoundEventAccessorComposite)this.sndRegistry.getObject(resourcelocation);
/*     */       
/* 268 */       if (ArrayUtils.contains(categories, soundeventaccessorcomposite.getSoundCategory()))
/*     */       {
/* 270 */         list.add(soundeventaccessorcomposite);
/*     */       }
/*     */     }
/*     */     
/* 274 */     if (list.isEmpty())
/*     */     {
/* 276 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 280 */     return (SoundEventAccessorComposite)list.get(new Random().nextInt(list.size()));
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isSoundPlaying(ISound sound)
/*     */   {
/* 286 */     return this.sndManager.isSoundPlaying(sound);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\audio\SoundHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */