// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.io.IOException;

final class OSXHIDDeviceIterator
{
    private final long iterator_address;
    
    public OSXHIDDeviceIterator() throws IOException {
        this.iterator_address = nCreateIterator();
    }
    
    private static final native long nCreateIterator();
    
    public final void close() {
        nReleaseIterator(this.iterator_address);
    }
    
    private static final native void nReleaseIterator(final long p0);
    
    public final OSXHIDDevice next() throws IOException {
        return nNext(this.iterator_address);
    }
    
    private static final native OSXHIDDevice nNext(final long p0) throws IOException;
}
