// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.zip;

import java.io.IOException;
import java.io.InputStream;

class BitStream
{
    private final InputStream in;
    private long bitCache;
    private int bitCacheSize;
    private static final int[] MASKS;
    
    BitStream(final InputStream in) {
        this.in = in;
    }
    
    private boolean fillCache() throws IOException {
        boolean filled = false;
        while (this.bitCacheSize <= 56) {
            final long nextByte = this.in.read();
            if (nextByte == -1L) {
                break;
            }
            filled = true;
            this.bitCache |= nextByte << this.bitCacheSize;
            this.bitCacheSize += 8;
        }
        return filled;
    }
    
    int nextBit() throws IOException {
        if (this.bitCacheSize == 0 && !this.fillCache()) {
            return -1;
        }
        final int bit = (int)(this.bitCache & 0x1L);
        this.bitCache >>>= 1;
        --this.bitCacheSize;
        return bit;
    }
    
    int nextBits(final int n) throws IOException {
        if (this.bitCacheSize < n && !this.fillCache()) {
            return -1;
        }
        final int bits = (int)(this.bitCache & (long)BitStream.MASKS[n]);
        this.bitCache >>>= n;
        this.bitCacheSize -= n;
        return bits;
    }
    
    int nextByte() throws IOException {
        return this.nextBits(8);
    }
    
    static {
        MASKS = new int[] { 0, 1, 3, 7, 15, 31, 63, 127, 255 };
    }
}
