// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

final class DIDeviceObjectData
{
    private int format_offset;
    private int data;
    private int millis;
    private int sequence;
    
    public final void set(final int format_offset, final int data, final int millis, final int sequence) {
        this.format_offset = format_offset;
        this.data = data;
        this.millis = millis;
        this.sequence = sequence;
    }
    
    public final void set(final DIDeviceObjectData other) {
        this.set(other.format_offset, other.data, other.millis, other.sequence);
    }
    
    public final int getData() {
        return this.data;
    }
    
    public final int getFormatOffset() {
        return this.format_offset;
    }
    
    public final long getNanos() {
        return this.millis * 1000000L;
    }
}
