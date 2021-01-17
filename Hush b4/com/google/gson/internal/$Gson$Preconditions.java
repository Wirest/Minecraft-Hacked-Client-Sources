// 
// Decompiled by Procyon v0.5.36
// 

package com.google.gson.internal;

public final class $Gson$Preconditions
{
    public static <T> T checkNotNull(final T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }
    
    public static void checkArgument(final boolean condition) {
        if (!condition) {
            throw new IllegalArgumentException();
        }
    }
}
