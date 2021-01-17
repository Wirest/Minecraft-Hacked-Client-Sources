// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.audio;

import net.minecraft.util.RegistrySimple;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang3.ArrayUtils;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import java.io.FileNotFoundException;
import org.apache.commons.io.IOUtils;
import java.io.Reader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.Iterator;
import java.io.IOException;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import java.util.Map;
import java.lang.reflect.Type;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.resources.IResourceManager;
import java.lang.reflect.ParameterizedType;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;
import net.minecraft.util.ITickable;
import net.minecraft.client.resources.IResourceManagerReloadListener;

public class SoundHandler implements IResourceManagerReloadListener, ITickable
{
    private static final Logger logger;
    private static final Gson GSON;
    private static final ParameterizedType TYPE;
    public static final SoundPoolEntry missing_sound;
    private final SoundRegistry sndRegistry;
    private final SoundManager sndManager;
    private final IResourceManager mcResourceManager;
    
    static {
        logger = LogManager.getLogger();
        GSON = new GsonBuilder().registerTypeAdapter(SoundList.class, new SoundListSerializer()).create();
        TYPE = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] { String.class, SoundList.class };
            }
            
            @Override
            public Type getRawType() {
                return Map.class;
            }
            
            @Override
            public Type getOwnerType() {
                return null;
            }
        };
        missing_sound = new SoundPoolEntry(new ResourceLocation("meta:missing_sound"), 0.0, 0.0, false);
    }
    
    public SoundHandler(final IResourceManager manager, final GameSettings gameSettingsIn) {
        this.sndRegistry = new SoundRegistry();
        this.mcResourceManager = manager;
        this.sndManager = new SoundManager(this, gameSettingsIn);
    }
    
    @Override
    public void onResourceManagerReload(final IResourceManager resourceManager) {
        this.sndManager.reloadSoundSystem();
        this.sndRegistry.clearMap();
        for (final String s : resourceManager.getResourceDomains()) {
            try {
                for (final IResource iresource : resourceManager.getAllResources(new ResourceLocation(s, "sounds.json"))) {
                    try {
                        final Map<String, SoundList> map = this.getSoundMap(iresource.getInputStream());
                        for (final Map.Entry<String, SoundList> entry : map.entrySet()) {
                            this.loadSoundResource(new ResourceLocation(s, entry.getKey()), entry.getValue());
                        }
                    }
                    catch (RuntimeException runtimeexception) {
                        SoundHandler.logger.warn("Invalid sounds.json", runtimeexception);
                    }
                }
            }
            catch (IOException ex) {}
        }
    }
    
    protected Map<String, SoundList> getSoundMap(final InputStream stream) {
        Map map;
        try {
            map = SoundHandler.GSON.fromJson(new InputStreamReader(stream), SoundHandler.TYPE);
        }
        finally {
            IOUtils.closeQuietly(stream);
        }
        IOUtils.closeQuietly(stream);
        return (Map<String, SoundList>)map;
    }
    
    private void loadSoundResource(final ResourceLocation location, final SoundList sounds) {
        final boolean flag = !((RegistrySimple<ResourceLocation, V>)this.sndRegistry).containsKey(location);
        SoundEventAccessorComposite soundeventaccessorcomposite;
        if (!flag && !sounds.canReplaceExisting()) {
            soundeventaccessorcomposite = this.sndRegistry.getObject(location);
        }
        else {
            if (!flag) {
                SoundHandler.logger.debug("Replaced sound event location {}", location);
            }
            soundeventaccessorcomposite = new SoundEventAccessorComposite(location, 1.0, 1.0, sounds.getSoundCategory());
            this.sndRegistry.registerSound(soundeventaccessorcomposite);
        }
        for (final SoundList.SoundEntry soundlist$soundentry : sounds.getSoundList()) {
            final String s = soundlist$soundentry.getSoundEntryName();
            final ResourceLocation resourcelocation = new ResourceLocation(s);
            final String s2 = s.contains(":") ? resourcelocation.getResourceDomain() : location.getResourceDomain();
            Object lvt_10_1_ = null;
            switch (soundlist$soundentry.getSoundEntryType()) {
                case FILE: {
                    final ResourceLocation resourcelocation2 = new ResourceLocation(s2, "sounds/" + resourcelocation.getResourcePath() + ".ogg");
                    InputStream inputstream = null;
                    try {
                        inputstream = this.mcResourceManager.getResource(resourcelocation2).getInputStream();
                    }
                    catch (FileNotFoundException var18) {
                        SoundHandler.logger.warn("File {} does not exist, cannot add it to event {}", resourcelocation2, location);
                    }
                    catch (IOException ioexception) {
                        SoundHandler.logger.warn("Could not load sound file " + resourcelocation2 + ", cannot add it to event " + location, ioexception);
                    }
                    finally {
                        IOUtils.closeQuietly(inputstream);
                    }
                    IOUtils.closeQuietly(inputstream);
                    lvt_10_1_ = new SoundEventAccessor(new SoundPoolEntry(resourcelocation2, soundlist$soundentry.getSoundEntryPitch(), soundlist$soundentry.getSoundEntryVolume(), soundlist$soundentry.isStreaming()), soundlist$soundentry.getSoundEntryWeight());
                    break;
                }
                case SOUND_EVENT: {
                    lvt_10_1_ = new ISoundEventAccessor<SoundPoolEntry>(s2, soundlist$soundentry) {
                        final ResourceLocation field_148726_a = new ResourceLocation(resourceDomainIn, soundEntry.getSoundEntryName());
                        
                        @Override
                        public int getWeight() {
                            final SoundEventAccessorComposite soundeventaccessorcomposite1 = SoundHandler.this.sndRegistry.getObject(this.field_148726_a);
                            return (soundeventaccessorcomposite1 == null) ? 0 : soundeventaccessorcomposite1.getWeight();
                        }
                        
                        @Override
                        public SoundPoolEntry cloneEntry() {
                            final SoundEventAccessorComposite soundeventaccessorcomposite1 = SoundHandler.this.sndRegistry.getObject(this.field_148726_a);
                            return (soundeventaccessorcomposite1 == null) ? SoundHandler.missing_sound : soundeventaccessorcomposite1.cloneEntry();
                        }
                    };
                    break;
                }
                default: {
                    throw new IllegalStateException("IN YOU FACE");
                }
            }
            soundeventaccessorcomposite.addSoundToEventPool((ISoundEventAccessor<SoundPoolEntry>)lvt_10_1_);
        }
    }
    
    public SoundEventAccessorComposite getSound(final ResourceLocation location) {
        return this.sndRegistry.getObject(location);
    }
    
    public void playSound(final ISound sound) {
        this.sndManager.playSound(sound);
    }
    
    public void playDelayedSound(final ISound sound, final int delay) {
        this.sndManager.playDelayedSound(sound, delay);
    }
    
    public void setListener(final EntityPlayer player, final float p_147691_2_) {
        this.sndManager.setListener(player, p_147691_2_);
    }
    
    public void pauseSounds() {
        this.sndManager.pauseAllSounds();
    }
    
    public void stopSounds() {
        this.sndManager.stopAllSounds();
    }
    
    public void unloadSounds() {
        this.sndManager.unloadSoundSystem();
    }
    
    @Override
    public void update() {
        this.sndManager.updateAllSounds();
    }
    
    public void resumeSounds() {
        this.sndManager.resumeAllSounds();
    }
    
    public void setSoundLevel(final SoundCategory category, final float volume) {
        if (category == SoundCategory.MASTER && volume <= 0.0f) {
            this.stopSounds();
        }
        this.sndManager.setSoundCategoryVolume(category, volume);
    }
    
    public void stopSound(final ISound p_147683_1_) {
        this.sndManager.stopSound(p_147683_1_);
    }
    
    public SoundEventAccessorComposite getRandomSoundFromCategories(final SoundCategory... categories) {
        final List<SoundEventAccessorComposite> list = (List<SoundEventAccessorComposite>)Lists.newArrayList();
        for (final ResourceLocation resourcelocation : ((RegistrySimple<ResourceLocation, V>)this.sndRegistry).getKeys()) {
            final SoundEventAccessorComposite soundeventaccessorcomposite = this.sndRegistry.getObject(resourcelocation);
            if (ArrayUtils.contains(categories, soundeventaccessorcomposite.getSoundCategory())) {
                list.add(soundeventaccessorcomposite);
            }
        }
        if (list.isEmpty()) {
            return null;
        }
        return list.get(new Random().nextInt(list.size()));
    }
    
    public boolean isSoundPlaying(final ISound sound) {
        return this.sndManager.isSoundPlaying(sound);
    }
}
