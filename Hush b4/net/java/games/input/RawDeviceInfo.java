// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.io.IOException;

abstract class RawDeviceInfo
{
    public abstract Controller createControllerFromDevice(final RawDevice p0, final SetupAPIDevice p1) throws IOException;
    
    public abstract int getUsage();
    
    public abstract int getUsagePage();
    
    public abstract long getHandle();
    
    public final boolean equals(final Object other) {
        if (!(other instanceof RawDeviceInfo)) {
            return false;
        }
        final RawDeviceInfo other_info = (RawDeviceInfo)other;
        return other_info.getUsage() == this.getUsage() && other_info.getUsagePage() == this.getUsagePage();
    }
    
    public final int hashCode() {
        return this.getUsage() ^ this.getUsagePage();
    }
}
