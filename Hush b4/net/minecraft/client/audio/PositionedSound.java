// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;

public abstract class PositionedSound implements ISound
{
    protected final ResourceLocation positionedSoundLocation;
    protected float volume;
    protected float pitch;
    protected float xPosF;
    protected float yPosF;
    protected float zPosF;
    protected boolean repeat;
    protected int repeatDelay;
    protected AttenuationType attenuationType;
    
    protected PositionedSound(final ResourceLocation soundResource) {
        this.volume = 1.0f;
        this.pitch = 1.0f;
        this.repeat = false;
        this.repeatDelay = 0;
        this.attenuationType = AttenuationType.LINEAR;
        this.positionedSoundLocation = soundResource;
    }
    
    @Override
    public ResourceLocation getSoundLocation() {
        return this.positionedSoundLocation;
    }
    
    @Override
    public boolean canRepeat() {
        return this.repeat;
    }
    
    @Override
    public int getRepeatDelay() {
        return this.repeatDelay;
    }
    
    @Override
    public float getVolume() {
        return this.volume;
    }
    
    @Override
    public float getPitch() {
        return this.pitch;
    }
    
    @Override
    public float getXPosF() {
        return this.xPosF;
    }
    
    @Override
    public float getYPosF() {
        return this.yPosF;
    }
    
    @Override
    public float getZPosF() {
        return this.zPosF;
    }
    
    @Override
    public AttenuationType getAttenuationType() {
        return this.attenuationType;
    }
}
