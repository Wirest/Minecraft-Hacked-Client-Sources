// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

final class RawKeyboardEvent
{
    private long millis;
    private int make_code;
    private int flags;
    private int vkey;
    private int message;
    private long extra_information;
    
    public final void set(final long millis, final int make_code, final int flags, final int vkey, final int message, final long extra_information) {
        this.millis = millis;
        this.make_code = make_code;
        this.flags = flags;
        this.vkey = vkey;
        this.message = message;
        this.extra_information = extra_information;
    }
    
    public final void set(final RawKeyboardEvent event) {
        this.set(event.millis, event.make_code, event.flags, event.vkey, event.message, event.extra_information);
    }
    
    public final int getVKey() {
        return this.vkey;
    }
    
    public final int getMessage() {
        return this.message;
    }
    
    public final long getNanos() {
        return this.millis * 1000000L;
    }
}
