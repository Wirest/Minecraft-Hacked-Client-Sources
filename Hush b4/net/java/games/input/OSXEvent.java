// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

class OSXEvent
{
    private long type;
    private long cookie;
    private int value;
    private long nanos;
    
    public void set(final long type, final long cookie, final int value, final long nanos) {
        this.type = type;
        this.cookie = cookie;
        this.value = value;
        this.nanos = nanos;
    }
    
    public long getType() {
        return this.type;
    }
    
    public long getCookie() {
        return this.cookie;
    }
    
    public int getValue() {
        return this.value;
    }
    
    public long getNanos() {
        return this.nanos;
    }
}
