// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;

public class PositionedSoundRecord extends PositionedSound
{
    public static PositionedSoundRecord create(final ResourceLocation soundResource, final float pitch) {
        return new PositionedSoundRecord(soundResource, 0.25f, pitch, false, 0, ISound.AttenuationType.NONE, 0.0f, 0.0f, 0.0f);
    }
    
    public static PositionedSoundRecord create(final ResourceLocation soundResource) {
        return new PositionedSoundRecord(soundResource, 1.0f, 1.0f, false, 0, ISound.AttenuationType.NONE, 0.0f, 0.0f, 0.0f);
    }
    
    public static PositionedSoundRecord create(final ResourceLocation soundResource, final float xPosition, final float yPosition, final float zPosition) {
        return new PositionedSoundRecord(soundResource, 4.0f, 1.0f, false, 0, ISound.AttenuationType.LINEAR, xPosition, yPosition, zPosition);
    }
    
    public PositionedSoundRecord(final ResourceLocation soundResource, final float volume, final float pitch, final float xPosition, final float yPosition, final float zPosition) {
        this(soundResource, volume, pitch, false, 0, ISound.AttenuationType.LINEAR, xPosition, yPosition, zPosition);
    }
    
    private PositionedSoundRecord(final ResourceLocation soundResource, final float volume, final float pitch, final boolean repeat, final int repeatDelay, final ISound.AttenuationType attenuationType, final float xPosition, final float yPosition, final float zPosition) {
        super(soundResource);
        this.volume = volume;
        this.pitch = pitch;
        this.xPosF = xPosition;
        this.yPosF = yPosition;
        this.zPosF = zPosition;
        this.repeat = repeat;
        this.repeatDelay = repeatDelay;
        this.attenuationType = attenuationType;
    }
}
