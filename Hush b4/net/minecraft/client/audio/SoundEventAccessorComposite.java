// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.audio;

import java.util.Iterator;
import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;
import java.util.Random;
import java.util.List;

public class SoundEventAccessorComposite implements ISoundEventAccessor<SoundPoolEntry>
{
    private final List<ISoundEventAccessor<SoundPoolEntry>> soundPool;
    private final Random rnd;
    private final ResourceLocation soundLocation;
    private final SoundCategory category;
    private double eventPitch;
    private double eventVolume;
    
    public SoundEventAccessorComposite(final ResourceLocation soundLocation, final double pitch, final double volume, final SoundCategory category) {
        this.soundPool = (List<ISoundEventAccessor<SoundPoolEntry>>)Lists.newArrayList();
        this.rnd = new Random();
        this.soundLocation = soundLocation;
        this.eventVolume = volume;
        this.eventPitch = pitch;
        this.category = category;
    }
    
    @Override
    public int getWeight() {
        int i = 0;
        for (final ISoundEventAccessor<SoundPoolEntry> isoundeventaccessor : this.soundPool) {
            i += isoundeventaccessor.getWeight();
        }
        return i;
    }
    
    @Override
    public SoundPoolEntry cloneEntry() {
        final int i = this.getWeight();
        if (!this.soundPool.isEmpty() && i != 0) {
            int j = this.rnd.nextInt(i);
            for (final ISoundEventAccessor<SoundPoolEntry> isoundeventaccessor : this.soundPool) {
                j -= isoundeventaccessor.getWeight();
                if (j < 0) {
                    final SoundPoolEntry soundpoolentry = isoundeventaccessor.cloneEntry();
                    soundpoolentry.setPitch(soundpoolentry.getPitch() * this.eventPitch);
                    soundpoolentry.setVolume(soundpoolentry.getVolume() * this.eventVolume);
                    return soundpoolentry;
                }
            }
            return SoundHandler.missing_sound;
        }
        return SoundHandler.missing_sound;
    }
    
    public void addSoundToEventPool(final ISoundEventAccessor<SoundPoolEntry> sound) {
        this.soundPool.add(sound);
    }
    
    public ResourceLocation getSoundEventLocation() {
        return this.soundLocation;
    }
    
    public SoundCategory getSoundCategory() {
        return this.category;
    }
}
