package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;

@GwtCompatible
final class CollectPreconditions {
    static void checkEntryNotNull(Object paramObject1, Object paramObject2) {
        if (paramObject1 == null) {
            throw new NullPointerException("null key in entry: null=" + paramObject2);
        }
        if (paramObject2 == null) {
            throw new NullPointerException("null value in entry: " + paramObject1 + "=null");
        }
    }

    static int checkNonnegative(int paramInt, String paramString) {
        if (paramInt < 0) {
            throw new IllegalArgumentException(paramString + " cannot be negative but was: " + paramInt);
        }
        return paramInt;
    }

    static void checkRemove(boolean paramBoolean) {
        Preconditions.checkState(paramBoolean, "no calls to next() since the last call to remove()");
    }
}




