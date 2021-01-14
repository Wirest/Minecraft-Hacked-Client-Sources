package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;

public class PositionedSoundRecord extends PositionedSound {
    private static final String __OBFID = "CL_00001120";

    public static PositionedSoundRecord createPositionedSoundRecord(ResourceLocation soundResource, float pitch) {
        return new PositionedSoundRecord(soundResource, 0.25F, pitch, false, 0, ISound.AttenuationType.NONE, 0.0F, 0.0F, 0.0F);
    }

    public static PositionedSoundRecord createPositionedSoundRecord(ResourceLocation soundResource) {
        return new PositionedSoundRecord(soundResource, 1.0F, 1.0F, false, 0, ISound.AttenuationType.NONE, 0.0F, 0.0F, 0.0F);
    }

    public static PositionedSoundRecord createRecordSoundAtPosition(ResourceLocation soundResource, float xPosition, float yPosition, float zPosition) {
        return new PositionedSoundRecord(soundResource, 4.0F, 1.0F, false, 0, ISound.AttenuationType.LINEAR, xPosition, yPosition, zPosition);
    }

    public PositionedSoundRecord(ResourceLocation soundResource, float volume, float pitch, float xPosition, float yPosition, float zPosition) {
        this(soundResource, volume, pitch, false, 0, ISound.AttenuationType.LINEAR, xPosition, yPosition, zPosition);
    }

    private PositionedSoundRecord(ResourceLocation soundResource, float volume, float pitch, boolean repeat, int repeatDelay, ISound.AttenuationType attenuationType, float xPosition, float yPosition, float zPosition) {
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
