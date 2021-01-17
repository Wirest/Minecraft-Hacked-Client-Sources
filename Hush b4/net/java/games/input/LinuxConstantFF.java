// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.io.IOException;

final class LinuxConstantFF extends LinuxForceFeedbackEffect
{
    public LinuxConstantFF(final LinuxEventDevice device) throws IOException {
        super(device);
    }
    
    protected final int upload(final int id, final float intensity) throws IOException {
        final int scaled_intensity = Math.round(intensity * 32767.0f);
        return this.getDevice().uploadConstantEffect(id, 0, 0, 0, 0, 0, scaled_intensity, 0, 0, 0, 0);
    }
}
