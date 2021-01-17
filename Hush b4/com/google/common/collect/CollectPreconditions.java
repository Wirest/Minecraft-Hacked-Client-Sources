// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
final class CollectPreconditions
{
    static void checkEntryNotNull(final Object key, final Object value) {
        if (key == null) {
            throw new NullPointerException("null key in entry: null=" + value);
        }
        if (value == null) {
            throw new NullPointerException("null value in entry: " + key + "=null");
        }
    }
    
    static int checkNonnegative(final int value, final String name) {
        if (value < 0) {
            throw new IllegalArgumentException(name + " cannot be negative but was: " + value);
        }
        return value;
    }
    
    static void checkRemove(final boolean canRemove) {
        Preconditions.checkState(canRemove, (Object)"no calls to next() since the last call to remove()");
    }
}
