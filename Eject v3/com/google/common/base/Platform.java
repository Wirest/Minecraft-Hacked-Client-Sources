package com.google.common.base;

import com.google.common.annotations.GwtCompatible;

import java.lang.ref.WeakReference;

@GwtCompatible(emulated = true)
final class Platform {
    static long systemNanoTime() {
        return System.nanoTime();
    }

    static CharMatcher precomputeCharMatcher(CharMatcher paramCharMatcher) {
        return paramCharMatcher.precomputedInternal();
    }

    static <T extends Enum<T>> Optional<T> getEnumIfPresent(Class<T> paramClass, String paramString) {
        WeakReference localWeakReference = (WeakReference) Enums.getEnumConstants(paramClass).get(paramString);
        return localWeakReference == null ? Optional.absent() : Optional.of(paramClass.cast(localWeakReference.get()));
    }
}




