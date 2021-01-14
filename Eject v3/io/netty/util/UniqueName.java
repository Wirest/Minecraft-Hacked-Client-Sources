package io.netty.util;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

@Deprecated
public class UniqueName
        implements Comparable<UniqueName> {
    private static final AtomicInteger nextId = new AtomicInteger();
    private final int id;
    private final String name;

    public UniqueName(ConcurrentMap<String, Boolean> paramConcurrentMap, String paramString, Object... paramVarArgs) {
        if (paramConcurrentMap == null) {
            throw new NullPointerException("map");
        }
        if (paramString == null) {
            throw new NullPointerException("name");
        }
        if ((paramVarArgs != null) && (paramVarArgs.length > 0)) {
            validateArgs(paramVarArgs);
        }
        if (paramConcurrentMap.putIfAbsent(paramString, Boolean.TRUE) != null) {
            throw new IllegalArgumentException(String.format("'%s' is already in use", new Object[]{paramString}));
        }
        this.id = nextId.incrementAndGet();
        this.name = paramString;
    }

    protected void validateArgs(Object... paramVarArgs) {
    }

    public final String name() {
        return this.name;
    }

    public final int id() {
        return this.id;
    }

    public final int hashCode() {
        return super.hashCode();
    }

    public final boolean equals(Object paramObject) {
        return super.equals(paramObject);
    }

    public int compareTo(UniqueName paramUniqueName) {
        if (this == paramUniqueName) {
            return 0;
        }
        int i = this.name.compareTo(paramUniqueName.name);
        if (i != 0) {
            return i;
        }
        return Integer.valueOf(this.id).compareTo(Integer.valueOf(paramUniqueName.id));
    }

    public String toString() {
        return name();
    }
}




