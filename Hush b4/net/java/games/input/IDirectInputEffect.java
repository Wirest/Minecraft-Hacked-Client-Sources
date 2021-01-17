// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.io.IOException;

final class IDirectInputEffect implements Rumbler
{
    private final long address;
    private final DIEffectInfo info;
    private boolean released;
    
    public IDirectInputEffect(final long address, final DIEffectInfo info) {
        this.address = address;
        this.info = info;
    }
    
    public final synchronized void rumble(final float intensity) {
        try {
            this.checkReleased();
            if (intensity > 0.0f) {
                final int int_gain = Math.round(intensity * 10000.0f);
                this.setGain(int_gain);
                this.start(1, 0);
            }
            else {
                this.stop();
            }
        }
        catch (IOException e) {
            ControllerEnvironment.logln("Failed to set rumbler gain: " + e.getMessage());
        }
    }
    
    public final Component.Identifier getAxisIdentifier() {
        return null;
    }
    
    public final String getAxisName() {
        return null;
    }
    
    public final synchronized void release() {
        if (!this.released) {
            this.released = true;
            nRelease(this.address);
        }
    }
    
    private static final native void nRelease(final long p0);
    
    private final void checkReleased() throws IOException {
        if (this.released) {
            throw new IOException();
        }
    }
    
    private final void setGain(final int gain) throws IOException {
        final int res = nSetGain(this.address, gain);
        if (res != 3 && res != 4 && res != 0 && res != 8 && res != 12) {
            throw new IOException("Failed to set effect gain (0x" + Integer.toHexString(res) + ")");
        }
    }
    
    private static final native int nSetGain(final long p0, final int p1);
    
    private final void start(final int iterations, final int flags) throws IOException {
        final int res = nStart(this.address, iterations, flags);
        if (res != 0) {
            throw new IOException("Failed to start effect (0x" + Integer.toHexString(res) + ")");
        }
    }
    
    private static final native int nStart(final long p0, final int p1, final int p2);
    
    private final void stop() throws IOException {
        final int res = nStop(this.address);
        if (res != 0) {
            throw new IOException("Failed to stop effect (0x" + Integer.toHexString(res) + ")");
        }
    }
    
    private static final native int nStop(final long p0);
    
    protected void finalize() {
        this.release();
    }
}
