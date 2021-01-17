// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.zip;

class CircularBuffer
{
    private final int size;
    private final byte[] buffer;
    private int readIndex;
    private int writeIndex;
    
    CircularBuffer(final int size) {
        this.size = size;
        this.buffer = new byte[size];
    }
    
    public boolean available() {
        return this.readIndex != this.writeIndex;
    }
    
    public void put(final int value) {
        this.buffer[this.writeIndex] = (byte)value;
        this.writeIndex = (this.writeIndex + 1) % this.size;
    }
    
    public int get() {
        if (this.available()) {
            final int value = this.buffer[this.readIndex];
            this.readIndex = (this.readIndex + 1) % this.size;
            return value & 0xFF;
        }
        return -1;
    }
    
    public void copy(final int distance, final int length) {
        final int pos1 = this.writeIndex - distance;
        for (int pos2 = pos1 + length, i = pos1; i < pos2; ++i) {
            this.buffer[this.writeIndex] = this.buffer[(i + this.size) % this.size];
            this.writeIndex = (this.writeIndex + 1) % this.size;
        }
    }
}
