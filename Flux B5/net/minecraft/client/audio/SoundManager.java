package net.minecraft.client.audio;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import io.netty.util.internal.ThreadLocalRandom;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import paulscode.sound.SoundSystem;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;
import paulscode.sound.SoundSystemLogger;
import paulscode.sound.Source;
import paulscode.sound.codecs.CodecJOrbis;
import paulscode.sound.libraries.LibraryLWJGLOpenAL;

public class SoundManager
{
    /** The marker used for logging */
    private static final Marker LOG_MARKER = MarkerManager.getMarker("SOUNDS");
    private static final Logger logger = LogManager.getLogger();

    /** A reference to the sound handler. */
    private final SoundHandler sndHandler;

    /** Reference to the GameSettings object. */
    private final GameSettings options;

    /** A reference to the sound system. */
    private SoundManager.SoundSystemStarterThread sndSystem;

    /** Set to true when the SoundManager has been initialised. */
    private boolean loaded;

    /** A counter for how long the sound manager has been running */
    private int playTime = 0;

    /**
     * Identifiers of all currently playing sounds. Type: HashBiMap<String, ISound>
     */
    private final Map playingSounds = HashBiMap.create();

    /**
     * Inverse map of currently playing sounds, automatically mirroring changes in original map
     */
    private final Map invPlayingSounds;

    /** A HashMap<String, SoundPoolEntry> of the playing sounds. */
    private Map playingSoundPoolEntries;

    /**
     * Contains sounds mapped by category. Type: Multimap<SoundCategory, String>
     */
    private final Multimap categorySounds;

    /** A subset of playingSounds, this contains only ITickableSounds */
    private final List tickableSounds;

    /** Contains sounds to play in n ticks.  Type: HashMap<ISound, Integer> */
    private final Map delayedSounds;

    /**
     * The future time in which to stop this sound.  Type: HashMap<String, Integer>
     */
    private final Map playingSoundsStopTime;
    private static final String __OBFID = "CL_00001141";

    public SoundManager(SoundHandler p_i45119_1_, GameSettings p_i45119_2_)
    {
        this.invPlayingSounds = ((BiMap)this.playingSounds).inverse();
        this.playingSoundPoolEntries = Maps.newHashMap();
        this.categorySounds = HashMultimap.create();
        this.tickableSounds = Lists.newArrayList();
        this.delayedSounds = Maps.newHashMap();
        this.playingSoundsStopTime = Maps.newHashMap();
        this.sndHandler = p_i45119_1_;
        this.options = p_i45119_2_;

        try
        {
            SoundSystemConfig.addLibrary(LibraryLWJGLOpenAL.class);
            SoundSystemConfig.setCodec("ogg", CodecJOrbis.class);
        }
        catch (SoundSystemException var4)
        {
            logger.error(LOG_MARKER, "Error linking with the LibraryJavaSound plug-in", var4);
        }
    }

    public void reloadSoundSystem()
    {
        this.unloadSoundSystem();
        this.loadSoundSystem();
    }

    /**
     * Tries to add the paulscode library and the relevant codecs. If it fails, the master volume  will be set to zero.
     */
    private synchronized void loadSoundSystem()
    {
        if (!this.loaded)
        {
            try
            {
                (new Thread(new Runnable()
                {
                    private static final String __OBFID = "CL_00001142";
                    public void run()
                    {
                        SoundSystemConfig.setLogger(new SoundSystemLogger()
                        {
                            private static final String __OBFID = "CL_00002378";
                            public void message(String p_message_1_, int p_message_2_)
                            {
                                if (!p_message_1_.isEmpty())
                                {
                                    SoundManager.logger.info(p_message_1_);
                                }
                            }
                            public void importantMessage(String p_importantMessage_1_, int p_importantMessage_2_)
                            {
                                if (!p_importantMessage_1_.isEmpty())
                                {
                                    SoundManager.logger.warn(p_importantMessage_1_);
                                }
                            }
                            public void errorMessage(String p_errorMessage_1_, String p_errorMessage_2_, int p_errorMessage_3_)
                            {
                                if (!p_errorMessage_2_.isEmpty())
                                {
                                    SoundManager.logger.error("Error in class \'" + p_errorMessage_1_ + "\'");
                                    SoundManager.logger.error(p_errorMessage_2_);
                                }
                            }
                        });
                        SoundManager.this.sndSystem = SoundManager.this.new SoundSystemStarterThread(null);
                        SoundManager.this.loaded = true;
                        SoundManager.this.sndSystem.setMasterVolume(SoundManager.this.options.getSoundLevel(SoundCategory.MASTER));
                        SoundManager.logger.info(SoundManager.LOG_MARKER, "Sound engine started");
                    }
                }, "Sound Library Loader")).start();
            }
            catch (RuntimeException var2)
            {
                logger.error(LOG_MARKER, "Error starting SoundSystem. Turning off sounds & music", var2);
                this.options.setSoundLevel(SoundCategory.MASTER, 0.0F);
                this.options.saveOptions();
            }
        }
    }

    /**
     * Returns the sound level (between 0.0 and 1.0) for a category, but 1.0 for the master sound category
     */
    private float getSoundCategoryVolume(SoundCategory p_148595_1_)
    {
        return p_148595_1_ != null && p_148595_1_ != SoundCategory.MASTER ? this.options.getSoundLevel(p_148595_1_) : 1.0F;
    }

    /**
     * Adjusts volume for currently playing sounds in this category
     */
    public void setSoundCategoryVolume(SoundCategory p_148601_1_, float p_148601_2_)
    {
        if (this.loaded)
        {
            if (p_148601_1_ == SoundCategory.MASTER)
            {
                this.sndSystem.setMasterVolume(p_148601_2_);
            }
            else
            {
                Iterator var3 = this.categorySounds.get(p_148601_1_).iterator();

                while (var3.hasNext())
                {
                    String var4 = (String)var3.next();
                    ISound var5 = (ISound)this.playingSounds.get(var4);
                    float var6 = this.getNormalizedVolume(var5, (SoundPoolEntry)this.playingSoundPoolEntries.get(var5), p_148601_1_);

                    if (var6 <= 0.0F)
                    {
                        this.stopSound(var5);
                    }
                    else
                    {
                        this.sndSystem.setVolume(var4, var6);
                    }
                }
            }
        }
    }

    /**
     * Cleans up the Sound System
     */
    public void unloadSoundSystem()
    {
        if (this.loaded)
        {
            this.stopAllSounds();
            this.sndSystem.cleanup();
            this.loaded = false;
        }
    }

    /**
     * Stops all currently playing sounds
     */
    public void stopAllSounds()
    {
        if (this.loaded)
        {
            Iterator var1 = this.playingSounds.keySet().iterator();

            while (var1.hasNext())
            {
                String var2 = (String)var1.next();
                this.sndSystem.stop(var2);
            }

            this.playingSounds.clear();
            this.delayedSounds.clear();
            this.tickableSounds.clear();
            this.categorySounds.clear();
            this.playingSoundPoolEntries.clear();
            this.playingSoundsStopTime.clear();
        }
    }

    public void updateAllSounds()
    {
        ++this.playTime;
        Iterator var1 = this.tickableSounds.iterator();
        String var3;

        while (var1.hasNext())
        {
            ITickableSound var2 = (ITickableSound)var1.next();
            var2.update();

            if (var2.isDonePlaying())
            {
                this.stopSound(var2);
            }
            else
            {
                var3 = (String)this.invPlayingSounds.get(var2);
                this.sndSystem.setVolume(var3, this.getNormalizedVolume(var2, (SoundPoolEntry)this.playingSoundPoolEntries.get(var2), this.sndHandler.getSound(var2.getSoundLocation()).getSoundCategory()));
                this.sndSystem.setPitch(var3, this.getNormalizedPitch(var2, (SoundPoolEntry)this.playingSoundPoolEntries.get(var2)));
                this.sndSystem.setPosition(var3, var2.getXPosF(), var2.getYPosF(), var2.getZPosF());
            }
        }

        var1 = this.playingSounds.entrySet().iterator();
        ISound var4;

        while (var1.hasNext())
        {
            Entry var9 = (Entry)var1.next();
            var3 = (String)var9.getKey();
            var4 = (ISound)var9.getValue();

            if (!this.sndSystem.playing(var3))
            {
                int var5 = ((Integer)this.playingSoundsStopTime.get(var3)).intValue();

                if (var5 <= this.playTime)
                {
                    int var6 = var4.getRepeatDelay();

                    if (var4.canRepeat() && var6 > 0)
                    {
                        this.delayedSounds.put(var4, Integer.valueOf(this.playTime + var6));
                    }

                    var1.remove();
                    logger.debug(LOG_MARKER, "Removed channel {} because it\'s not playing anymore", new Object[] {var3});
                    this.sndSystem.removeSource(var3);
                    this.playingSoundsStopTime.remove(var3);
                    this.playingSoundPoolEntries.remove(var4);

                    try
                    {
                        this.categorySounds.remove(this.sndHandler.getSound(var4.getSoundLocation()).getSoundCategory(), var3);
                    }
                    catch (RuntimeException var8)
                    {
                        ;
                    }

                    if (var4 instanceof ITickableSound)
                    {
                        this.tickableSounds.remove(var4);
                    }
                }
            }
        }

        Iterator var10 = this.delayedSounds.entrySet().iterator();

        while (var10.hasNext())
        {
            Entry var11 = (Entry)var10.next();

            if (this.playTime >= ((Integer)var11.getValue()).intValue())
            {
                var4 = (ISound)var11.getKey();

                if (var4 instanceof ITickableSound)
                {
                    ((ITickableSound)var4).update();
                }

                this.playSound(var4);
                var10.remove();
            }
        }
    }

    /**
     * Returns true if the sound is playing or still within time
     */
    public boolean isSoundPlaying(ISound p_148597_1_)
    {
        if (!this.loaded)
        {
            return false;
        }
        else
        {
            String var2 = (String)this.invPlayingSounds.get(p_148597_1_);
            return var2 == null ? false : this.sndSystem.playing(var2) || this.playingSoundsStopTime.containsKey(var2) && ((Integer)this.playingSoundsStopTime.get(var2)).intValue() <= this.playTime;
        }
    }

    public void stopSound(ISound p_148602_1_)
    {
        if (this.loaded)
        {
            String var2 = (String)this.invPlayingSounds.get(p_148602_1_);

            if (var2 != null)
            {
                this.sndSystem.stop(var2);
            }
        }
    }

    public void playSound(ISound p_148611_1_)
    {
        if (this.loaded)
        {
            if (this.sndSystem.getMasterVolume() <= 0.0F)
            {
                logger.debug(LOG_MARKER, "Skipped playing soundEvent: {}, master volume was zero", new Object[] {p_148611_1_.getSoundLocation()});
            }
            else
            {
                SoundEventAccessorComposite var2 = this.sndHandler.getSound(p_148611_1_.getSoundLocation());

                if (var2 == null)
                {
                    logger.warn(LOG_MARKER, "Unable to play unknown soundEvent: {}", new Object[] {p_148611_1_.getSoundLocation()});
                }
                else
                {
                    SoundPoolEntry var3 = (SoundPoolEntry) var2.cloneEntry();

                    if (var3 == SoundHandler.missing_sound)
                    {
                        logger.warn(LOG_MARKER, "Unable to play empty soundEvent: {}", new Object[] {var2.getSoundEventLocation()});
                    }
                    else
                    {
                        float var4 = p_148611_1_.getVolume();
                        float var5 = 16.0F;

                        if (var4 > 1.0F)
                        {
                            var5 *= var4;
                        }

                        SoundCategory var6 = var2.getSoundCategory();
                        float var7 = this.getNormalizedVolume(p_148611_1_, var3, var6);
                        double var8 = (double)this.getNormalizedPitch(p_148611_1_, var3);
                        ResourceLocation var10 = var3.getSoundPoolEntryLocation();

                        if (var7 == 0.0F)
                        {
                            logger.debug(LOG_MARKER, "Skipped playing sound {}, volume was zero.", new Object[] {var10});
                        }
                        else
                        {
                            boolean var11 = p_148611_1_.canRepeat() && p_148611_1_.getRepeatDelay() == 0;
                            String var12 = MathHelper.func_180182_a(ThreadLocalRandom.current()).toString();

                            if (var3.isStreamingSound())
                            {
                                this.sndSystem.newStreamingSource(false, var12, getURLForSoundResource(var10), var10.toString(), var11, p_148611_1_.getXPosF(), p_148611_1_.getYPosF(), p_148611_1_.getZPosF(), p_148611_1_.getAttenuationType().getTypeInt(), var5);
                            }
                            else
                            {
                                this.sndSystem.newSource(false, var12, getURLForSoundResource(var10), var10.toString(), var11, p_148611_1_.getXPosF(), p_148611_1_.getYPosF(), p_148611_1_.getZPosF(), p_148611_1_.getAttenuationType().getTypeInt(), var5);
                            }

                            logger.debug(LOG_MARKER, "Playing sound {} for event {} as channel {}", new Object[] {var3.getSoundPoolEntryLocation(), var2.getSoundEventLocation(), var12});
                            this.sndSystem.setPitch(var12, (float)var8);
                            this.sndSystem.setVolume(var12, var7);
                            this.sndSystem.play(var12);
                            this.playingSoundsStopTime.put(var12, Integer.valueOf(this.playTime + 20));
                            this.playingSounds.put(var12, p_148611_1_);
                            this.playingSoundPoolEntries.put(p_148611_1_, var3);

                            if (var6 != SoundCategory.MASTER)
                            {
                                this.categorySounds.put(var6, var12);
                            }

                            if (p_148611_1_ instanceof ITickableSound)
                            {
                                this.tickableSounds.add((ITickableSound)p_148611_1_);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Normalizes pitch from parameters and clamps to [0.5, 2.0]
     */
    private float getNormalizedPitch(ISound p_148606_1_, SoundPoolEntry p_148606_2_)
    {
        return (float)MathHelper.clamp_double((double)p_148606_1_.getPitch() * p_148606_2_.getPitch(), 0.5D, 2.0D);
    }

    /**
     * Normalizes volume level from parameters.  Range [0.0, 1.0]
     */
    private float getNormalizedVolume(ISound p_148594_1_, SoundPoolEntry p_148594_2_, SoundCategory p_148594_3_)
    {
        return (float)MathHelper.clamp_double((double)p_148594_1_.getVolume() * p_148594_2_.getVolume(), 0.0D, 1.0D) * this.getSoundCategoryVolume(p_148594_3_);
    }

    /**
     * Pauses all currently playing sounds
     */
    public void pauseAllSounds()
    {
        Iterator var1 = this.playingSounds.keySet().iterator();

        while (var1.hasNext())
        {
            String var2 = (String)var1.next();
            logger.debug(LOG_MARKER, "Pausing channel {}", new Object[] {var2});
            this.sndSystem.pause(var2);
        }
    }

    /**
     * Resumes playing all currently playing sounds (after pauseAllSounds)
     */
    public void resumeAllSounds()
    {
        Iterator var1 = this.playingSounds.keySet().iterator();

        while (var1.hasNext())
        {
            String var2 = (String)var1.next();
            logger.debug(LOG_MARKER, "Resuming channel {}", new Object[] {var2});
            this.sndSystem.play(var2);
        }
    }

    /**
     * Adds a sound to play in n tick
     */
    public void playDelayedSound(ISound p_148599_1_, int p_148599_2_)
    {
        this.delayedSounds.put(p_148599_1_, Integer.valueOf(this.playTime + p_148599_2_));
    }

    private static URL getURLForSoundResource(final ResourceLocation p_148612_0_)
    {
        String var1 = String.format("%s:%s:%s", new Object[] {"mcsounddomain", p_148612_0_.getResourceDomain(), p_148612_0_.getResourcePath()});
        URLStreamHandler var2 = new URLStreamHandler()
        {
            private static final String __OBFID = "CL_00001143";
            protected URLConnection openConnection(final URL p_openConnection_1_)
            {
                return new URLConnection(p_openConnection_1_)
                {
                    private static final String __OBFID = "CL_00001144";
                    public void connect() {}
                    public InputStream getInputStream() throws IOException
                    {
                        return Minecraft.getMinecraft().getResourceManager().getResource(p_148612_0_).getInputStream();
                    }
                };
            }
        };

        try
        {
            return new URL((URL)null, var1, var2);
        }
        catch (MalformedURLException var4)
        {
            throw new Error("TODO: Sanely handle url exception! :D");
        }
    }

    /**
     * Sets the listener of sounds
     */
    public void setListener(EntityPlayer p_148615_1_, float p_148615_2_)
    {
        if (this.loaded && p_148615_1_ != null)
        {
            float var3 = p_148615_1_.prevRotationPitch + (p_148615_1_.rotationPitch - p_148615_1_.prevRotationPitch) * p_148615_2_;
            float var4 = p_148615_1_.prevRotationYaw + (p_148615_1_.rotationYaw - p_148615_1_.prevRotationYaw) * p_148615_2_;
            double var5 = p_148615_1_.prevPosX + (p_148615_1_.posX - p_148615_1_.prevPosX) * (double)p_148615_2_;
            double var7 = p_148615_1_.prevPosY + (p_148615_1_.posY - p_148615_1_.prevPosY) * (double)p_148615_2_ + (double)p_148615_1_.getEyeHeight();
            double var9 = p_148615_1_.prevPosZ + (p_148615_1_.posZ - p_148615_1_.prevPosZ) * (double)p_148615_2_;
            float var11 = MathHelper.cos((var4 + 90.0F) * 0.017453292F);
            float var12 = MathHelper.sin((var4 + 90.0F) * 0.017453292F);
            float var13 = MathHelper.cos(-var3 * 0.017453292F);
            float var14 = MathHelper.sin(-var3 * 0.017453292F);
            float var15 = MathHelper.cos((-var3 + 90.0F) * 0.017453292F);
            float var16 = MathHelper.sin((-var3 + 90.0F) * 0.017453292F);
            float var17 = var11 * var13;
            float var19 = var12 * var13;
            float var20 = var11 * var15;
            float var22 = var12 * var15;
            this.sndSystem.setListenerPosition((float)var5, (float)var7, (float)var9);
            this.sndSystem.setListenerOrientation(var17, var14, var19, var20, var16, var22);
        }
    }

    class SoundSystemStarterThread extends SoundSystem
    {
        private static final String __OBFID = "CL_00001145";

        private SoundSystemStarterThread() {}

        public boolean playing(String p_playing_1_)
        {
            Object var2 = SoundSystemConfig.THREAD_SYNC;

            synchronized (SoundSystemConfig.THREAD_SYNC)
            {
                if (this.soundLibrary == null)
                {
                    return false;
                }
                else
                {
                    Source var3 = (Source)this.soundLibrary.getSources().get(p_playing_1_);
                    return var3 == null ? false : var3.playing() || var3.paused() || var3.preLoad;
                }
            }
        }

        SoundSystemStarterThread(Object p_i45118_2_)
        {
            this();
        }
    }
}
