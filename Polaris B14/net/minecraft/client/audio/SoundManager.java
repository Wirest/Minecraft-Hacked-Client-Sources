/*     */ package net.minecraft.client.audio;
/*     */ 
/*     */ import com.google.common.collect.BiMap;
/*     */ import com.google.common.collect.HashBiMap;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Multimap;
/*     */ import io.netty.util.internal.ThreadLocalRandom;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.net.URLStreamHandler;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.resources.IResourceManager;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.apache.logging.log4j.Marker;
/*     */ import org.apache.logging.log4j.MarkerManager;
/*     */ import paulscode.sound.SoundSystem;
/*     */ import paulscode.sound.SoundSystemConfig;
/*     */ import paulscode.sound.SoundSystemException;
/*     */ import paulscode.sound.SoundSystemLogger;
/*     */ import paulscode.sound.Source;
/*     */ import paulscode.sound.libraries.LibraryLWJGLOpenAL;
/*     */ 
/*     */ public class SoundManager
/*     */ {
/*  40 */   private static final Marker LOG_MARKER = MarkerManager.getMarker("SOUNDS");
/*  41 */   private static final Logger logger = LogManager.getLogger();
/*     */   
/*     */ 
/*     */   private final SoundHandler sndHandler;
/*     */   
/*     */ 
/*     */   private final GameSettings options;
/*     */   
/*     */ 
/*     */   private SoundSystemStarterThread sndSystem;
/*     */   
/*     */ 
/*     */   private boolean loaded;
/*     */   
/*     */ 
/*  56 */   private int playTime = 0;
/*  57 */   private final Map<String, ISound> playingSounds = HashBiMap.create();
/*     */   private final Map<ISound, String> invPlayingSounds;
/*     */   private Map<ISound, SoundPoolEntry> playingSoundPoolEntries;
/*     */   private final Multimap<SoundCategory, String> categorySounds;
/*     */   private final List<ITickableSound> tickableSounds;
/*     */   private final Map<ISound, Integer> delayedSounds;
/*     */   private final Map<String, Integer> playingSoundsStopTime;
/*     */   
/*     */   public SoundManager(SoundHandler p_i45119_1_, GameSettings p_i45119_2_)
/*     */   {
/*  67 */     this.invPlayingSounds = ((BiMap)this.playingSounds).inverse();
/*  68 */     this.playingSoundPoolEntries = Maps.newHashMap();
/*  69 */     this.categorySounds = com.google.common.collect.HashMultimap.create();
/*  70 */     this.tickableSounds = com.google.common.collect.Lists.newArrayList();
/*  71 */     this.delayedSounds = Maps.newHashMap();
/*  72 */     this.playingSoundsStopTime = Maps.newHashMap();
/*  73 */     this.sndHandler = p_i45119_1_;
/*  74 */     this.options = p_i45119_2_;
/*     */     
/*     */     try
/*     */     {
/*  78 */       SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
/*  79 */       SoundSystemConfig.setCodec("ogg", paulscode.sound.codecs.CodecJOrbis.class);
/*     */     }
/*     */     catch (SoundSystemException soundsystemexception)
/*     */     {
/*  83 */       logger.error(LOG_MARKER, "Error linking with the LibraryJavaSound plug-in", soundsystemexception);
/*     */     }
/*     */   }
/*     */   
/*     */   public void reloadSoundSystem()
/*     */   {
/*  89 */     unloadSoundSystem();
/*  90 */     loadSoundSystem();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private synchronized void loadSoundSystem()
/*     */   {
/*  98 */     if (!this.loaded)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       try
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 136 */         new Thread(new Runnable()
/*     */         {
/*     */           public void run()
/*     */           {
/* 106 */             SoundSystemConfig.setLogger(new SoundSystemLogger()
/*     */             {
/*     */               public void message(String p_message_1_, int p_message_2_)
/*     */               {
/* 110 */                 if (!p_message_1_.isEmpty())
/*     */                 {
/* 112 */                   SoundManager.logger.info(p_message_1_);
/*     */                 }
/*     */               }
/*     */               
/*     */               public void importantMessage(String p_importantMessage_1_, int p_importantMessage_2_) {
/* 117 */                 if (!p_importantMessage_1_.isEmpty())
/*     */                 {
/* 119 */                   SoundManager.logger.warn(p_importantMessage_1_);
/*     */                 }
/*     */               }
/*     */               
/*     */               public void errorMessage(String p_errorMessage_1_, String p_errorMessage_2_, int p_errorMessage_3_) {
/* 124 */                 if (!p_errorMessage_2_.isEmpty())
/*     */                 {
/* 126 */                   SoundManager.logger.error("Error in class '" + p_errorMessage_1_ + "'");
/* 127 */                   SoundManager.logger.error(p_errorMessage_2_);
/*     */                 }
/*     */                 
/*     */               }
/* 131 */             }); SoundManager tmp23_20 = SoundManager.this;tmp23_20.getClass();SoundManager.this.sndSystem = new SoundManager.SoundSystemStarterThread(tmp23_20, null);
/* 132 */             SoundManager.this.loaded = true;
/* 133 */             SoundManager.this.sndSystem.setMasterVolume(SoundManager.this.options.getSoundLevel(SoundCategory.MASTER));
/* 134 */             SoundManager.logger.info(SoundManager.LOG_MARKER, "Sound engine started");
/*     */           }
/* 136 */         }, "Sound Library Loader").start();
/*     */       }
/*     */       catch (RuntimeException runtimeexception)
/*     */       {
/* 140 */         logger.error(LOG_MARKER, "Error starting SoundSystem. Turning off sounds & music", runtimeexception);
/* 141 */         this.options.setSoundLevel(SoundCategory.MASTER, 0.0F);
/* 142 */         this.options.saveOptions();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private float getSoundCategoryVolume(SoundCategory category)
/*     */   {
/* 152 */     return (category != null) && (category != SoundCategory.MASTER) ? this.options.getSoundLevel(category) : 1.0F;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSoundCategoryVolume(SoundCategory category, float volume)
/*     */   {
/* 160 */     if (this.loaded)
/*     */     {
/* 162 */       if (category == SoundCategory.MASTER)
/*     */       {
/* 164 */         this.sndSystem.setMasterVolume(volume);
/*     */       }
/*     */       else
/*     */       {
/* 168 */         for (String s : this.categorySounds.get(category))
/*     */         {
/* 170 */           ISound isound = (ISound)this.playingSounds.get(s);
/* 171 */           float f = getNormalizedVolume(isound, (SoundPoolEntry)this.playingSoundPoolEntries.get(isound), category);
/*     */           
/* 173 */           if (f <= 0.0F)
/*     */           {
/* 175 */             stopSound(isound);
/*     */           }
/*     */           else
/*     */           {
/* 179 */             this.sndSystem.setVolume(s, f);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void unloadSoundSystem()
/*     */   {
/* 191 */     if (this.loaded)
/*     */     {
/* 193 */       stopAllSounds();
/* 194 */       this.sndSystem.cleanup();
/* 195 */       this.loaded = false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void stopAllSounds()
/*     */   {
/* 204 */     if (this.loaded)
/*     */     {
/* 206 */       for (String s : this.playingSounds.keySet())
/*     */       {
/* 208 */         this.sndSystem.stop(s);
/*     */       }
/*     */       
/* 211 */       this.playingSounds.clear();
/* 212 */       this.delayedSounds.clear();
/* 213 */       this.tickableSounds.clear();
/* 214 */       this.categorySounds.clear();
/* 215 */       this.playingSoundPoolEntries.clear();
/* 216 */       this.playingSoundsStopTime.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateAllSounds()
/*     */   {
/* 222 */     this.playTime += 1;
/*     */     
/* 224 */     for (ITickableSound itickablesound : this.tickableSounds)
/*     */     {
/* 226 */       itickablesound.update();
/*     */       
/* 228 */       if (itickablesound.isDonePlaying())
/*     */       {
/* 230 */         stopSound(itickablesound);
/*     */       }
/*     */       else
/*     */       {
/* 234 */         String s = (String)this.invPlayingSounds.get(itickablesound);
/* 235 */         this.sndSystem.setVolume(s, getNormalizedVolume(itickablesound, (SoundPoolEntry)this.playingSoundPoolEntries.get(itickablesound), this.sndHandler.getSound(itickablesound.getSoundLocation()).getSoundCategory()));
/* 236 */         this.sndSystem.setPitch(s, getNormalizedPitch(itickablesound, (SoundPoolEntry)this.playingSoundPoolEntries.get(itickablesound)));
/* 237 */         this.sndSystem.setPosition(s, itickablesound.getXPosF(), itickablesound.getYPosF(), itickablesound.getZPosF());
/*     */       }
/*     */     }
/*     */     
/* 241 */     Iterator<Map.Entry<String, ISound>> iterator = this.playingSounds.entrySet().iterator();
/*     */     
/* 243 */     while (iterator.hasNext())
/*     */     {
/* 245 */       Object entry = (Map.Entry)iterator.next();
/* 246 */       String s1 = (String)((Map.Entry)entry).getKey();
/* 247 */       ISound isound = (ISound)((Map.Entry)entry).getValue();
/*     */       
/* 249 */       if (!this.sndSystem.playing(s1))
/*     */       {
/* 251 */         int i = ((Integer)this.playingSoundsStopTime.get(s1)).intValue();
/*     */         
/* 253 */         if (i <= this.playTime)
/*     */         {
/* 255 */           int j = isound.getRepeatDelay();
/*     */           
/* 257 */           if ((isound.canRepeat()) && (j > 0))
/*     */           {
/* 259 */             this.delayedSounds.put(isound, Integer.valueOf(this.playTime + j));
/*     */           }
/*     */           
/* 262 */           iterator.remove();
/* 263 */           logger.debug(LOG_MARKER, "Removed channel {} because it's not playing anymore", new Object[] { s1 });
/* 264 */           this.sndSystem.removeSource(s1);
/* 265 */           this.playingSoundsStopTime.remove(s1);
/* 266 */           this.playingSoundPoolEntries.remove(isound);
/*     */           
/*     */           try
/*     */           {
/* 270 */             this.categorySounds.remove(this.sndHandler.getSound(isound.getSoundLocation()).getSoundCategory(), s1);
/*     */           }
/*     */           catch (RuntimeException localRuntimeException) {}
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 277 */           if ((isound instanceof ITickableSound))
/*     */           {
/* 279 */             this.tickableSounds.remove(isound);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 285 */     Object iterator1 = this.delayedSounds.entrySet().iterator();
/*     */     
/* 287 */     while (((Iterator)iterator1).hasNext())
/*     */     {
/* 289 */       Map.Entry<ISound, Integer> entry1 = (Map.Entry)((Iterator)iterator1).next();
/*     */       
/* 291 */       if (this.playTime >= ((Integer)entry1.getValue()).intValue())
/*     */       {
/* 293 */         ISound isound1 = (ISound)entry1.getKey();
/*     */         
/* 295 */         if ((isound1 instanceof ITickableSound))
/*     */         {
/* 297 */           ((ITickableSound)isound1).update();
/*     */         }
/*     */         
/* 300 */         playSound(isound1);
/* 301 */         ((Iterator)iterator1).remove();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isSoundPlaying(ISound sound)
/*     */   {
/* 311 */     if (!this.loaded)
/*     */     {
/* 313 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 317 */     String s = (String)this.invPlayingSounds.get(sound);
/* 318 */     return s != null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void stopSound(ISound sound)
/*     */   {
/* 324 */     if (this.loaded)
/*     */     {
/* 326 */       String s = (String)this.invPlayingSounds.get(sound);
/*     */       
/* 328 */       if (s != null)
/*     */       {
/* 330 */         this.sndSystem.stop(s);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void playSound(ISound sound)
/*     */   {
/* 337 */     if (this.loaded)
/*     */     {
/* 339 */       if (this.sndSystem.getMasterVolume() <= 0.0F)
/*     */       {
/* 341 */         logger.debug(LOG_MARKER, "Skipped playing soundEvent: {}, master volume was zero", new Object[] { sound.getSoundLocation() });
/*     */       }
/*     */       else
/*     */       {
/* 345 */         SoundEventAccessorComposite soundeventaccessorcomposite = this.sndHandler.getSound(sound.getSoundLocation());
/*     */         
/* 347 */         if (soundeventaccessorcomposite == null)
/*     */         {
/* 349 */           logger.warn(LOG_MARKER, "Unable to play unknown soundEvent: {}", new Object[] { sound.getSoundLocation() });
/*     */         }
/*     */         else
/*     */         {
/* 353 */           SoundPoolEntry soundpoolentry = soundeventaccessorcomposite.cloneEntry();
/*     */           
/* 355 */           if (soundpoolentry == SoundHandler.missing_sound)
/*     */           {
/* 357 */             logger.warn(LOG_MARKER, "Unable to play empty soundEvent: {}", new Object[] { soundeventaccessorcomposite.getSoundEventLocation() });
/*     */           }
/*     */           else
/*     */           {
/* 361 */             float f = sound.getVolume();
/* 362 */             float f1 = 16.0F;
/*     */             
/* 364 */             if (f > 1.0F)
/*     */             {
/* 366 */               f1 *= f;
/*     */             }
/*     */             
/* 369 */             SoundCategory soundcategory = soundeventaccessorcomposite.getSoundCategory();
/* 370 */             float f2 = getNormalizedVolume(sound, soundpoolentry, soundcategory);
/* 371 */             double d0 = getNormalizedPitch(sound, soundpoolentry);
/* 372 */             ResourceLocation resourcelocation = soundpoolentry.getSoundPoolEntryLocation();
/*     */             
/* 374 */             if (f2 == 0.0F)
/*     */             {
/* 376 */               logger.debug(LOG_MARKER, "Skipped playing sound {}, volume was zero.", new Object[] { resourcelocation });
/*     */             }
/*     */             else
/*     */             {
/* 380 */               boolean flag = (sound.canRepeat()) && (sound.getRepeatDelay() == 0);
/* 381 */               String s = MathHelper.getRandomUuid(ThreadLocalRandom.current()).toString();
/*     */               
/* 383 */               if (soundpoolentry.isStreamingSound())
/*     */               {
/* 385 */                 this.sndSystem.newStreamingSource(false, s, getURLForSoundResource(resourcelocation), resourcelocation.toString(), flag, sound.getXPosF(), sound.getYPosF(), sound.getZPosF(), sound.getAttenuationType().getTypeInt(), f1);
/*     */               }
/*     */               else
/*     */               {
/* 389 */                 this.sndSystem.newSource(false, s, getURLForSoundResource(resourcelocation), resourcelocation.toString(), flag, sound.getXPosF(), sound.getYPosF(), sound.getZPosF(), sound.getAttenuationType().getTypeInt(), f1);
/*     */               }
/*     */               
/* 392 */               logger.debug(LOG_MARKER, "Playing sound {} for event {} as channel {}", new Object[] { soundpoolentry.getSoundPoolEntryLocation(), soundeventaccessorcomposite.getSoundEventLocation(), s });
/* 393 */               this.sndSystem.setPitch(s, (float)d0);
/* 394 */               this.sndSystem.setVolume(s, f2);
/* 395 */               this.sndSystem.play(s);
/* 396 */               this.playingSoundsStopTime.put(s, Integer.valueOf(this.playTime + 20));
/* 397 */               this.playingSounds.put(s, sound);
/* 398 */               this.playingSoundPoolEntries.put(sound, soundpoolentry);
/*     */               
/* 400 */               if (soundcategory != SoundCategory.MASTER)
/*     */               {
/* 402 */                 this.categorySounds.put(soundcategory, s);
/*     */               }
/*     */               
/* 405 */               if ((sound instanceof ITickableSound))
/*     */               {
/* 407 */                 this.tickableSounds.add((ITickableSound)sound);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private float getNormalizedPitch(ISound sound, SoundPoolEntry entry)
/*     */   {
/* 421 */     return (float)MathHelper.clamp_double(sound.getPitch() * entry.getPitch(), 0.5D, 2.0D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private float getNormalizedVolume(ISound sound, SoundPoolEntry entry, SoundCategory category)
/*     */   {
/* 429 */     return (float)MathHelper.clamp_double(sound.getVolume() * entry.getVolume(), 0.0D, 1.0D) * getSoundCategoryVolume(category);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void pauseAllSounds()
/*     */   {
/* 437 */     for (String s : this.playingSounds.keySet())
/*     */     {
/* 439 */       logger.debug(LOG_MARKER, "Pausing channel {}", new Object[] { s });
/* 440 */       this.sndSystem.pause(s);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resumeAllSounds()
/*     */   {
/* 449 */     for (String s : this.playingSounds.keySet())
/*     */     {
/* 451 */       logger.debug(LOG_MARKER, "Resuming channel {}", new Object[] { s });
/* 452 */       this.sndSystem.play(s);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void playDelayedSound(ISound sound, int delay)
/*     */   {
/* 461 */     this.delayedSounds.put(sound, Integer.valueOf(this.playTime + delay));
/*     */   }
/*     */   
/*     */   private static URL getURLForSoundResource(ResourceLocation p_148612_0_)
/*     */   {
/* 466 */     String s = String.format("%s:%s:%s", new Object[] { "mcsounddomain", p_148612_0_.getResourceDomain(), p_148612_0_.getResourcePath() });
/* 467 */     URLStreamHandler urlstreamhandler = new URLStreamHandler()
/*     */     {
/*     */       protected URLConnection openConnection(URL p_openConnection_1_)
/*     */       {
/* 471 */         new URLConnection(p_openConnection_1_)
/*     */         {
/*     */           public void connect() throws IOException
/*     */           {}
/*     */           
/*     */           public InputStream getInputStream() throws IOException
/*     */           {
/* 478 */             return Minecraft.getMinecraft().getResourceManager().getResource(this.val$p_148612_0_).getInputStream();
/*     */           }
/*     */         };
/*     */       }
/*     */     };
/*     */     
/*     */     try
/*     */     {
/* 486 */       return new URL(null, s, urlstreamhandler);
/*     */     }
/*     */     catch (MalformedURLException var4)
/*     */     {
/* 490 */       throw new Error("TODO: Sanely handle url exception! :D");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setListener(EntityPlayer player, float p_148615_2_)
/*     */   {
/* 499 */     if ((this.loaded) && (player != null))
/*     */     {
/* 501 */       float f = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * p_148615_2_;
/* 502 */       float f1 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * p_148615_2_;
/* 503 */       double d0 = player.prevPosX + (player.posX - player.prevPosX) * p_148615_2_;
/* 504 */       double d1 = player.prevPosY + (player.posY - player.prevPosY) * p_148615_2_ + player.getEyeHeight();
/* 505 */       double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * p_148615_2_;
/* 506 */       float f2 = MathHelper.cos((f1 + 90.0F) * 0.017453292F);
/* 507 */       float f3 = MathHelper.sin((f1 + 90.0F) * 0.017453292F);
/* 508 */       float f4 = MathHelper.cos(-f * 0.017453292F);
/* 509 */       float f5 = MathHelper.sin(-f * 0.017453292F);
/* 510 */       float f6 = MathHelper.cos((-f + 90.0F) * 0.017453292F);
/* 511 */       float f7 = MathHelper.sin((-f + 90.0F) * 0.017453292F);
/* 512 */       float f8 = f2 * f4;
/* 513 */       float f9 = f3 * f4;
/* 514 */       float f10 = f2 * f6;
/* 515 */       float f11 = f3 * f6;
/* 516 */       this.sndSystem.setListenerPosition((float)d0, (float)d1, (float)d2);
/* 517 */       this.sndSystem.setListenerOrientation(f8, f5, f9, f10, f7, f11);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   class SoundSystemStarterThread
/*     */     extends SoundSystem
/*     */   {
/*     */     private SoundSystemStarterThread() {}
/*     */     
/*     */     public boolean playing(String p_playing_1_)
/*     */     {
/* 529 */       synchronized (SoundSystemConfig.THREAD_SYNC)
/*     */       {
/* 531 */         if (this.soundLibrary == null)
/*     */         {
/* 533 */           return false;
/*     */         }
/*     */         
/*     */ 
/* 537 */         Source source = (Source)this.soundLibrary.getSources().get(p_playing_1_);
/* 538 */         return source != null;
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\client\audio\SoundManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */