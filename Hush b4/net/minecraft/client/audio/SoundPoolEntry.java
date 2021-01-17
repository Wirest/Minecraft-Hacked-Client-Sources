// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;

public class SoundPoolEntry
{
    private final ResourceLocation location;
    private final boolean streamingSound;
    private double pitch;
    private double volume;
    
    public SoundPoolEntry(final ResourceLocation locationIn, final double pitchIn, final double volumeIn, final boolean streamingSoundIn) {
        this.location = locationIn;
        this.pitch = pitchIn;
        this.volume = volumeIn;
        this.streamingSound = streamingSoundIn;
    }
    
    public SoundPoolEntry(final SoundPoolEntry locationIn) {
        this.location = locationIn.location;
        this.pitch = locationIn.pitch;
        this.volume = locationIn.volume;
        this.streamingSound = locationIn.streamingSound;
    }
    
    public ResourceLocation getSoundPoolEntryLocation() {
        return this.location;
    }
    
    public double getPitch() {
        return this.pitch;
    }
    
    public void setPitch(final double pitchIn) {
        this.pitch = pitchIn;
    }
    
    public double getVolume() {
        return this.volume;
    }
    
    public void setVolume(final double volumeIn) {
        this.volume = volumeIn;
    }
    
    public boolean isStreamingSound() {
        return this.streamingSound;
    }
}
