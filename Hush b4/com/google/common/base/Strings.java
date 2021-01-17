// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import com.google.common.annotations.VisibleForTesting;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public final class Strings
{
    private Strings() {
    }
    
    public static String nullToEmpty(@Nullable final String string) {
        return (string == null) ? "" : string;
    }
    
    @Nullable
    public static String emptyToNull(@Nullable final String string) {
        return isNullOrEmpty(string) ? null : string;
    }
    
    public static boolean isNullOrEmpty(@Nullable final String string) {
        return string == null || string.length() == 0;
    }
    
    public static String padStart(final String string, final int minLength, final char padChar) {
        Preconditions.checkNotNull(string);
        if (string.length() >= minLength) {
            return string;
        }
        final StringBuilder sb = new StringBuilder(minLength);
        for (int i = string.length(); i < minLength; ++i) {
            sb.append(padChar);
        }
        sb.append(string);
        return sb.toString();
    }
    
    public static String padEnd(final String string, final int minLength, final char padChar) {
        Preconditions.checkNotNull(string);
        if (string.length() >= minLength) {
            return string;
        }
        final StringBuilder sb = new StringBuilder(minLength);
        sb.append(string);
        for (int i = string.length(); i < minLength; ++i) {
            sb.append(padChar);
        }
        return sb.toString();
    }
    
    public static String repeat(final String string, final int count) {
        Preconditions.checkNotNull(string);
        if (count <= 1) {
            Preconditions.checkArgument(count >= 0, "invalid count: %s", count);
            return (count == 0) ? "" : string;
        }
        final int len = string.length();
        final long longSize = len * (long)count;
        final int size = (int)longSize;
        if (size != longSize) {
            throw new ArrayIndexOutOfBoundsException("Required array size too large: " + longSize);
        }
        final char[] array = new char[size];
        string.getChars(0, len, array, 0);
        int n;
        for (n = len; n < size - n; n <<= 1) {
            System.arraycopy(array, 0, array, n, n);
        }
        System.arraycopy(array, 0, array, n, size - n);
        return new String(array);
    }
    
    public static String commonPrefix(final CharSequence a, final CharSequence b) {
        Preconditions.checkNotNull(a);
        Preconditions.checkNotNull(b);
        int maxPrefixLength;
        int p;
        for (maxPrefixLength = Math.min(a.length(), b.length()), p = 0; p < maxPrefixLength && a.charAt(p) == b.charAt(p); ++p) {}
        if (validSurrogatePairAt(a, p - 1) || validSurrogatePairAt(b, p - 1)) {
            --p;
        }
        return a.subSequence(0, p).toString();
    }
    
    public static String commonSuffix(final CharSequence a, final CharSequence b) {
        Preconditions.checkNotNull(a);
        Preconditions.checkNotNull(b);
        int maxSuffixLength;
        int s;
        for (maxSuffixLength = Math.min(a.length(), b.length()), s = 0; s < maxSuffixLength && a.charAt(a.length() - s - 1) == b.charAt(b.length() - s - 1); ++s) {}
        if (validSurrogatePairAt(a, a.length() - s - 1) || validSurrogatePairAt(b, b.length() - s - 1)) {
            --s;
        }
        return a.subSequence(a.length() - s, a.length()).toString();
    }
    
    @VisibleForTesting
    static boolean validSurrogatePairAt(final CharSequence string, final int index) {
        return index >= 0 && index <= string.length() - 2 && Character.isHighSurrogate(string.charAt(index)) && Character.isLowSurrogate(string.charAt(index + 1));
    }
}
