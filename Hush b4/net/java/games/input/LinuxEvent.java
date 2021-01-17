// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

final class LinuxEvent
{
    private long nanos;
    private final LinuxAxisDescriptor descriptor;
    private int value;
    
    LinuxEvent() {
        this.descriptor = new LinuxAxisDescriptor();
    }
    
    public final void set(final long seconds, final long microseconds, final int type, final int code, final int value) {
        this.nanos = (seconds * 1000000L + microseconds) * 1000L;
        this.descriptor.set(type, code);
        this.value = value;
    }
    
    public final int getValue() {
        return this.value;
    }
    
    public final LinuxAxisDescriptor getDescriptor() {
        return this.descriptor;
    }
    
    public final long getNanos() {
        return this.nanos;
    }
}
