// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.audio;

import paulscode.sound.Source;
import paulscode.sound.SoundSystem;
import net.minecraft.entity.player.EntityPlayer;
import java.net.MalformedURLException;
import net.minecraft.client.Minecraft;
import java.io.InputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.URL;
import net.minecraft.util.ResourceLocation;
import java.util.Random;
import net.minecraft.util.MathHelper;
import io.netty.util.internal.ThreadLocalRandom;
import java.util.Iterator;
import paulscode.sound.SoundSystemLogger;
import paulscode.sound.SoundSystemException;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;
import com.google.common.collect.Lists;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.MarkerManager;
import java.util.List;
import com.google.common.collect.Multimap;
import java.util.Map;
import net.minecraft.client.settings.GameSettings;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;

public class SoundManager
{
    private static final Marker LOG_MARKER;
    private static final Logger logger;
    private final SoundHandler sndHandler;
    private final GameSettings options;
    private SoundSystemStarterThread sndSystem;
    private boolean loaded;
    private int playTime;
    private final Map<String, ISound> playingSounds;
    private final Map<ISound, String> invPlayingSounds;
    private Map<ISound, SoundPoolEntry> playingSoundPoolEntries;
    private final Multimap<SoundCategory, String> categorySounds;
    private final List<ITickableSound> tickableSounds;
    private final Map<ISound, Integer> delayedSounds;
    private final Map<String, Integer> playingSoundsStopTime;
    
    static {
        LOG_MARKER = MarkerManager.getMarker("SOUNDS");
        logger = LogManager.getLogger();
    }
    
    public SoundManager(final SoundHandler p_i45119_1_, final GameSettings p_i45119_2_) {
        this.playTime = 0;
        this.playingSounds = (Map<String, ISound>)HashBiMap.create();
        this.invPlayingSounds = (Map<ISound, String>)((BiMap)this.playingSounds).inverse();
        this.playingSoundPoolEntries = (Map<ISound, SoundPoolEntry>)Maps.newHashMap();
        this.categorySounds = (Multimap<SoundCategory, String>)HashMultimap.create();
        this.tickableSounds = (List<ITickableSound>)Lists.newArrayList();
        this.delayedSounds = (Map<ISound, Integer>)Maps.newHashMap();
        this.playingSoundsStopTime = (Map<String, Integer>)Maps.newHashMap();
        this.sndHandler = p_i45119_1_;
        this.options = p_i45119_2_;
        try {
            SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
            SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
        }
        catch (SoundSystemException soundsystemexception) {
            SoundManager.logger.error(SoundManager.LOG_MARKER, "Error linking with the LibraryJavaSound plug-in", soundsystemexception);
        }
    }
    
    public void reloadSoundSystem() {
        this.unloadSoundSystem();
        this.loadSoundSystem();
    }
    
    private synchronized void loadSoundSystem() {
        if (!this.loaded) {
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SoundSystemConfig.setLogger(new SoundSystemLogger() {
                            @Override
                            public void message(final String p_message_1_, final int p_message_2_) {
                                if (!p_message_1_.isEmpty()) {
                                    SoundManager.logger.info(p_message_1_);
                                }
                            }
                            
                            @Override
                            public void importantMessage(final String p_importantMessage_1_, final int p_importantMessage_2_) {
                                if (!p_importantMessage_1_.isEmpty()) {
                                    SoundManager.logger.warn(p_importantMessage_1_);
                                }
                            }
                            
                            @Override
                            public void errorMessage(final String p_errorMessage_1_, final String p_errorMessage_2_, final int p_errorMessage_3_) {
                                if (!p_errorMessage_2_.isEmpty()) {
                                    SoundManager.logger.error("Error in class '" + p_errorMessage_1_ + "'");
                                    SoundManager.logger.error(p_errorMessage_2_);
                                }
                            }
                        });
                        SoundManager.access$1(SoundManager.this, new SoundSystemStarterThread((SoundSystemStarterThread)null));
                        SoundManager.access$2(SoundManager.this, true);
                        SoundManager.this.sndSystem.setMasterVolume(SoundManager.this.options.getSoundLevel(SoundCategory.MASTER));
                        SoundManager.logger.info(SoundManager.LOG_MARKER, "Sound engine started");
                    }
                }, "Sound Library Loader").start();
            }
            catch (RuntimeException runtimeexception) {
                SoundManager.logger.error(SoundManager.LOG_MARKER, "Error starting SoundSystem. Turning off sounds & music", runtimeexception);
                this.options.setSoundLevel(SoundCategory.MASTER, 0.0f);
                this.options.saveOptions();
            }
        }
    }
    
    private float getSoundCategoryVolume(final SoundCategory category) {
        return (category != null && category != SoundCategory.MASTER) ? this.options.getSoundLevel(category) : 1.0f;
    }
    
    public void setSoundCategoryVolume(final SoundCategory category, final float volume) {
        if (this.loaded) {
            if (category == SoundCategory.MASTER) {
                this.sndSystem.setMasterVolume(volume);
            }
            else {
                for (final String s : this.categorySounds.get(category)) {
                    final ISound isound = this.playingSounds.get(s);
                    final float f = this.getNormalizedVolume(isound, this.playingSoundPoolEntries.get(isound), category);
                    if (f <= 0.0f) {
                        this.stopSound(isound);
                    }
                    else {
                        this.sndSystem.setVolume(s, f);
                    }
                }
            }
        }
    }
    
    public void unloadSoundSystem() {
        if (this.loaded) {
            this.stopAllSounds();
            this.sndSystem.cleanup();
            this.loaded = false;
        }
    }
    
    public void stopAllSounds() {
        if (this.loaded) {
            for (final String s : this.playingSounds.keySet()) {
                this.sndSystem.stop(s);
            }
            this.playingSounds.clear();
            this.delayedSounds.clear();
            this.tickableSounds.clear();
            this.categorySounds.clear();
            this.playingSoundPoolEntries.clear();
            this.playingSoundsStopTime.clear();
        }
    }
    
    public void updateAllSounds() {
        ++this.playTime;
        for (final ITickableSound itickablesound : this.tickableSounds) {
            itickablesound.update();
            if (itickablesound.isDonePlaying()) {
                this.stopSound(itickablesound);
            }
            else {
                final String s = this.invPlayingSounds.get(itickablesound);
                this.sndSystem.setVolume(s, this.getNormalizedVolume(itickablesound, this.playingSoundPoolEntries.get(itickablesound), this.sndHandler.getSound(itickablesound.getSoundLocation()).getSoundCategory()));
                this.sndSystem.setPitch(s, this.getNormalizedPitch(itickablesound, this.playingSoundPoolEntries.get(itickablesound)));
                this.sndSystem.setPosition(s, itickablesound.getXPosF(), itickablesound.getYPosF(), itickablesound.getZPosF());
            }
        }
        final Iterator<Map.Entry<String, ISound>> iterator = this.playingSounds.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<String, ISound> entry = iterator.next();
            final String s2 = entry.getKey();
            final ISound isound = entry.getValue();
            if (!this.sndSystem.playing(s2)) {
                final int i = this.playingSoundsStopTime.get(s2);
                if (i > this.playTime) {
                    continue;
                }
                final int j = isound.getRepeatDelay();
                if (isound.canRepeat() && j > 0) {
                    this.delayedSounds.put(isound, this.playTime + j);
                }
                iterator.remove();
                SoundManager.logger.debug(SoundManager.LOG_MARKER, "Removed channel {} because it's not playing anymore", s2);
                this.sndSystem.removeSource(s2);
                this.playingSoundsStopTime.remove(s2);
                this.playingSoundPoolEntries.remove(isound);
                try {
                    this.categorySounds.remove(this.sndHandler.getSound(isound.getSoundLocation()).getSoundCategory(), s2);
                }
                catch (RuntimeException ex) {}
                if (!(isound instanceof ITickableSound)) {
                    continue;
                }
                this.tickableSounds.remove(isound);
            }
        }
        final Iterator<Map.Entry<ISound, Integer>> iterator2 = this.delayedSounds.entrySet().iterator();
        while (iterator2.hasNext()) {
            final Map.Entry<ISound, Integer> entry2 = iterator2.next();
            if (this.playTime >= entry2.getValue()) {
                final ISound isound2 = entry2.getKey();
                if (isound2 instanceof ITickableSound) {
                    ((ITickableSound)isound2).update();
                }
                this.playSound(isound2);
                iterator2.remove();
            }
        }
    }
    
    public boolean isSoundPlaying(final ISound sound) {
        if (!this.loaded) {
            return false;
        }
        final String s = this.invPlayingSounds.get(sound);
        return s != null && (this.sndSystem.playing(s) || (this.playingSoundsStopTime.containsKey(s) && this.playingSoundsStopTime.get(s) <= this.playTime));
    }
    
    public void stopSound(final ISound sound) {
        if (this.loaded) {
            final String s = this.invPlayingSounds.get(sound);
            if (s != null) {
                this.sndSystem.stop(s);
            }
        }
    }
    
    public void playSound(final ISound sound) {
        if (this.loaded) {
            if (this.sndSystem.getMasterVolume() <= 0.0f) {
                SoundManager.logger.debug(SoundManager.LOG_MARKER, "Skipped playing soundEvent: {}, master volume was zero", sound.getSoundLocation());
            }
            else {
                final SoundEventAccessorComposite soundeventaccessorcomposite = this.sndHandler.getSound(sound.getSoundLocation());
                if (soundeventaccessorcomposite == null) {
                    SoundManager.logger.warn(SoundManager.LOG_MARKER, "Unable to play unknown soundEvent: {}", sound.getSoundLocation());
                }
                else {
                    final SoundPoolEntry soundpoolentry = soundeventaccessorcomposite.cloneEntry();
                    if (soundpoolentry == SoundHandler.missing_sound) {
                        SoundManager.logger.warn(SoundManager.LOG_MARKER, "Unable to play empty soundEvent: {}", soundeventaccessorcomposite.getSoundEventLocation());
                    }
                    else {
                        final float f = sound.getVolume();
                        float f2 = 16.0f;
                        if (f > 1.0f) {
                            f2 *= f;
                        }
                        final SoundCategory soundcategory = soundeventaccessorcomposite.getSoundCategory();
                        final float f3 = this.getNormalizedVolume(sound, soundpoolentry, soundcategory);
                        final double d0 = this.getNormalizedPitch(sound, soundpoolentry);
                        final ResourceLocation resourcelocation = soundpoolentry.getSoundPoolEntryLocation();
                        if (f3 == 0.0f) {
                            SoundManager.logger.debug(SoundManager.LOG_MARKER, "Skipped playing sound {}, volume was zero.", resourcelocation);
                        }
                        else {
                            final boolean flag = sound.canRepeat() && sound.getRepeatDelay() == 0;
                            final String s = MathHelper.getRandomUuid(ThreadLocalRandom.current()).toString();
                            if (soundpoolentry.isStreamingSound()) {
                                this.sndSystem.newStreamingSource(false, s, getURLForSoundResource(resourcelocation), resourcelocation.toString(), flag, sound.getXPosF(), sound.getYPosF(), sound.getZPosF(), sound.getAttenuationType().getTypeInt(), f2);
                            }
                            else {
                                this.sndSystem.newSource(false, s, getURLForSoundResource(resourcelocation), resourcelocation.toString(), flag, sound.getXPosF(), sound.getYPosF(), sound.getZPosF(), sound.getAttenuationType().getTypeInt(), f2);
                            }
                            SoundManager.logger.debug(SoundManager.LOG_MARKER, "Playing sound {} for event {} as channel {}", soundpoolentry.getSoundPoolEntryLocation(), soundeventaccessorcomposite.getSoundEventLocation(), s);
                            this.sndSystem.setPitch(s, (float)d0);
                            this.sndSystem.setVolume(s, f3);
                            this.sndSystem.play(s);
                            this.playingSoundsStopTime.put(s, this.playTime + 20);
                            this.playingSounds.put(s, sound);
                            this.playingSoundPoolEntries.put(sound, soundpoolentry);
                            if (soundcategory != SoundCategory.MASTER) {
                                this.categorySounds.put(soundcategory, s);
                            }
                            if (sound instanceof ITickableSound) {
                                this.tickableSounds.add((ITickableSound)sound);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private float getNormalizedPitch(final ISound sound, final SoundPoolEntry entry) {
        return (float)MathHelper.clamp_double(sound.getPitch() * entry.getPitch(), 0.5, 2.0);
    }
    
    private float getNormalizedVolume(final ISound sound, final SoundPoolEntry entry, final SoundCategory category) {
        return (float)MathHelper.clamp_double(sound.getVolume() * entry.getVolume(), 0.0, 1.0) * this.getSoundCategoryVolume(category);
    }
    
    public void pauseAllSounds() {
        for (final String s : this.playingSounds.keySet()) {
            SoundManager.logger.debug(SoundManager.LOG_MARKER, "Pausing channel {}", s);
            this.sndSystem.pause(s);
        }
    }
    
    public void resumeAllSounds() {
        for (final String s : this.playingSounds.keySet()) {
            SoundManager.logger.debug(SoundManager.LOG_MARKER, "Resuming channel {}", s);
            this.sndSystem.play(s);
        }
    }
    
    public void playDelayedSound(final ISound sound, final int delay) {
        this.delayedSounds.put(sound, this.playTime + delay);
    }
    
    private static URL getURLForSoundResource(final ResourceLocation p_148612_0_) {
        final String s = String.format("%s:%s:%s", "mcsounddomain", p_148612_0_.getResourceDomain(), p_148612_0_.getResourcePath());
        final URLStreamHandler urlstreamhandler = new URLStreamHandler() {
            @Override
            protected URLConnection openConnection(final URL p_openConnection_1_) {
                return new URLConnection(p_openConnection_1_) {
                    @Override
                    public void connect() throws IOException {
                    }
                    
                    @Override
                    public InputStream getInputStream() throws IOException {
                        return Minecraft.getMinecraft().getResourceManager().getResource(p_148612_0_).getInputStream();
                    }
                };
            }
        };
        try {
            return new URL(null, s, urlstreamhandler);
        }
        catch (MalformedURLException var4) {
            throw new Error("TODO: Sanely handle url exception! :D");
        }
    }
    
    public void setListener(final EntityPlayer player, final float p_148615_2_) {
        if (this.loaded && player != null) {
            final float f = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * p_148615_2_;
            final float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * p_148615_2_;
            final double d0 = player.prevPosX + (player.posX - player.prevPosX) * p_148615_2_;
            final double d2 = player.prevPosY + (player.posY - player.prevPosY) * p_148615_2_ + player.getEyeHeight();
            final double d3 = player.prevPosZ + (player.posZ - player.prevPosZ) * p_148615_2_;
            final float f3 = MathHelper.cos((f2 + 90.0f) * 0.017453292f);
            final float f4 = MathHelper.sin((f2 + 90.0f) * 0.017453292f);
            final float f5 = MathHelper.cos(-f * 0.017453292f);
            final float f6 = MathHelper.sin(-f * 0.017453292f);
            final float f7 = MathHelper.cos((-f + 90.0f) * 0.017453292f);
            final float f8 = MathHelper.sin((-f + 90.0f) * 0.017453292f);
            final float f9 = f3 * f5;
            final float f10 = f4 * f5;
            final float f11 = f3 * f7;
            final float f12 = f4 * f7;
            this.sndSystem.setListenerPosition((float)d0, (float)d2, (float)d3);
            this.sndSystem.setListenerOrientation(f9, f6, f10, f11, f8, f12);
        }
    }
    
    static /* synthetic */ void access$1(final SoundManager soundManager, final SoundSystemStarterThread sndSystem) {
        soundManager.sndSystem = sndSystem;
    }
    
    static /* synthetic */ void access$2(final SoundManager soundManager, final boolean loaded) {
        soundManager.loaded = loaded;
    }
    
    class SoundSystemStarterThread extends SoundSystem
    {
        private SoundSystemStarterThread() {
        }
        
        @Override
        public boolean playing(final String p_playing_1_) {
            synchronized (SoundSystemConfig.THREAD_SYNC) {
                if (this.soundLibrary == null) {
                    // monitorexit(SoundSystemConfig.THREAD_SYNC)
                    return false;
                }
                final Source source = this.soundLibrary.getSources().get(p_playing_1_);
                // monitorexit(SoundSystemConfig.THREAD_SYNC)
                return source != null && (source.playing() || source.paused() || source.preLoad);
            }
        }
    }
}
