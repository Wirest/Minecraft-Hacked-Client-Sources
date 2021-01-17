// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.audio;

import net.minecraft.util.ResourceLocation;

public abstract class MovingSound extends PositionedSound implements ITickableSound
{
    protected boolean donePlaying;
    
    protected MovingSound(final ResourceLocation location) {
        super(location);
        this.donePlaying = false;
    }
    
    @Override
    public boolean isDonePlaying() {
        return this.donePlaying;
    }
}
