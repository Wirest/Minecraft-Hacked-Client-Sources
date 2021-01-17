// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.io.IOException;

final class LinuxRumbleFF extends LinuxForceFeedbackEffect
{
    public LinuxRumbleFF(final LinuxEventDevice device) throws IOException {
        super(device);
    }
    
    protected final int upload(final int id, final float intensity) throws IOException {
        int strong_magnitude;
        int weak_magnitude;
        if (intensity > 0.666666f) {
            strong_magnitude = (int)(32768.0f * intensity);
            weak_magnitude = (int)(49152.0f * intensity);
        }
        else if (intensity > 0.3333333f) {
            strong_magnitude = (int)(32768.0f * intensity);
            weak_magnitude = 0;
        }
        else {
            strong_magnitude = 0;
            weak_magnitude = (int)(49152.0f * intensity);
        }
        return this.getDevice().uploadRumbleEffect(id, 0, 0, 0, -1, 0, strong_magnitude, weak_magnitude);
    }
}
