// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@Deprecated
public class UniqueName implements Comparable<UniqueName>
{
    private static final AtomicInteger nextId;
    private final int id;
    private final String name;
    
    public UniqueName(final ConcurrentMap<String, Boolean> map, final String name, final Object... args) {
        if (map == null) {
            throw new NullPointerException("map");
        }
        if (name == null) {
            throw new NullPointerException("name");
        }
        if (args != null && args.length > 0) {
            this.validateArgs(args);
        }
        if (map.putIfAbsent(name, Boolean.TRUE) != null) {
            throw new IllegalArgumentException(String.format("'%s' is already in use", name));
        }
        this.id = UniqueName.nextId.incrementAndGet();
        this.name = name;
    }
    
    protected void validateArgs(final Object... args) {
    }
    
    public final String name() {
        return this.name;
    }
    
    public final int id() {
        return this.id;
    }
    
    @Override
    public final int hashCode() {
        return super.hashCode();
    }
    
    @Override
    public final boolean equals(final Object o) {
        return super.equals(o);
    }
    
    @Override
    public int compareTo(final UniqueName other) {
        if (this == other) {
            return 0;
        }
        final int returnCode = this.name.compareTo(other.name);
        if (returnCode != 0) {
            return returnCode;
        }
        return Integer.valueOf(this.id).compareTo(other.id);
    }
    
    @Override
    public String toString() {
        return this.name();
    }
    
    static {
        nextId = new AtomicInteger();
    }
}
