// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util;

import io.netty.util.internal.PlatformDependent;
import java.util.concurrent.ConcurrentMap;

public final class Signal extends Error
{
    private static final long serialVersionUID = -221145131122459977L;
    private static final ConcurrentMap<String, Boolean> map;
    private final UniqueName uname;
    
    public static Signal valueOf(final String name) {
        return new Signal(name);
    }
    
    @Deprecated
    public Signal(final String name) {
        super(name);
        this.uname = new UniqueName(Signal.map, name, new Object[0]);
    }
    
    public void expect(final Signal signal) {
        if (this != signal) {
            throw new IllegalStateException("unexpected signal: " + signal);
        }
    }
    
    @Override
    public Throwable initCause(final Throwable cause) {
        return this;
    }
    
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
    
    @Override
    public String toString() {
        return this.uname.name();
    }
    
    static {
        map = PlatformDependent.newConcurrentHashMap();
    }
}
