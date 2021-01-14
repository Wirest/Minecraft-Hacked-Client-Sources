package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;

public abstract class MovingSound extends PositionedSound implements ITickableSound {
    protected boolean donePlaying = false;
    private static final String __OBFID = "CL_00001117";

    protected MovingSound(ResourceLocation location) {
        super(location);
    }

    public boolean isDonePlaying() {
        return this.donePlaying;
    }
}
