package io.netty.util;

import java.util.concurrent.ConcurrentMap;

public final class AttributeKey<T>
        extends UniqueName {
    private static final ConcurrentMap<String, Boolean> names = ;

    @Deprecated
    public AttributeKey(String paramString) {
        super(names, paramString, new Object[0]);
    }

    public static <T> AttributeKey<T> valueOf(String paramString) {
        return new AttributeKey(paramString);
    }
}




