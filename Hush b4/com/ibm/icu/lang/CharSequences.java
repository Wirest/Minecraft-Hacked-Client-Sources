// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.lang;

public class CharSequences
{
    @Deprecated
    public static int matchAfter(final CharSequence a, final CharSequence b, final int aIndex, final int bIndex) {
        int i = aIndex;
        int j = bIndex;
        for (int alen = a.length(), blen = b.length(); i < alen && j < blen; ++i, ++j) {
            final char ca = a.charAt(i);
            final char cb = b.charAt(j);
            if (ca != cb) {
                break;
            }
        }
        int result = i - aIndex;
        if (result != 0 && !onCharacterBoundary(a, i) && !onCharacterBoundary(b, j)) {
            --result;
        }
        return result;
    }
    
    @Deprecated
    public int codePointLength(final CharSequence s) {
        return Character.codePointCount(s, 0, s.length());
    }
    
    @Deprecated
    public static final boolean equals(final int codepoint, final CharSequence other) {
        if (other == null) {
            return false;
        }
        switch (other.length()) {
            case 1: {
                return codepoint == other.charAt(0);
            }
            case 2: {
                return codepoint > 65535 && codepoint == Character.codePointAt(other, 0);
            }
            default: {
                return false;
            }
        }
    }
    
    @Deprecated
    public static final boolean equals(final CharSequence other, final int codepoint) {
        return equals(codepoint, other);
    }
    
    @Deprecated
    public static int compare(final CharSequence string, final int codePoint) {
        if (codePoint < 0 || codePoint > 1114111) {
            throw new IllegalArgumentException();
        }
        final int stringLength = string.length();
        if (stringLength == 0) {
            return -1;
        }
        final char firstChar = string.charAt(0);
        final int offset = codePoint - 65536;
        if (offset < 0) {
            final int result = firstChar - codePoint;
            if (result != 0) {
                return result;
            }
            return stringLength - 1;
        }
        else {
            final char lead = (char)((offset >>> 10) + 55296);
            int result2 = firstChar - lead;
            if (result2 != 0) {
                return result2;
            }
            if (stringLength > 1) {
                final char trail = (char)((offset & 0x3FF) + 56320);
                result2 = string.charAt(1) - trail;
                if (result2 != 0) {
                    return result2;
                }
            }
            return stringLength - 2;
        }
    }
    
    @Deprecated
    public static int compare(final int codepoint, final CharSequence a) {
        return -compare(a, codepoint);
    }
    
    @Deprecated
    public static int getSingleCodePoint(final CharSequence s) {
        final int length = s.length();
        if (length < 1 || length > 2) {
            return Integer.MAX_VALUE;
        }
        final int result = Character.codePointAt(s, 0);
        return (result < 65536 == (length == 1)) ? result : Integer.MAX_VALUE;
    }
    
    @Deprecated
    public static final <T> boolean equals(final T a, final T b) {
        return (a == null) ? (b == null) : (b != null && a.equals(b));
    }
    
    @Deprecated
    public static int compare(final CharSequence a, final CharSequence b) {
        final int alength = a.length();
        final int blength = b.length();
        for (int min = (alength <= blength) ? alength : blength, i = 0; i < min; ++i) {
            final int diff = a.charAt(i) - b.charAt(i);
            if (diff != 0) {
                return diff;
            }
        }
        return alength - blength;
    }
    
    @Deprecated
    public static boolean equalsChars(final CharSequence a, final CharSequence b) {
        return a.length() == b.length() && compare(a, b) == 0;
    }
    
    @Deprecated
    public static boolean onCharacterBoundary(final CharSequence s, final int i) {
        return i <= 0 || i >= s.length() || !Character.isHighSurrogate(s.charAt(i - 1)) || !Character.isLowSurrogate(s.charAt(i));
    }
    
    @Deprecated
    public static int indexOf(final CharSequence s, final int codePoint) {
        int cp;
        for (int i = 0; i < s.length(); i += Character.charCount(cp)) {
            cp = Character.codePointAt(s, i);
            if (cp == codePoint) {
                return i;
            }
        }
        return -1;
    }
    
    @Deprecated
    public static int[] codePoints(final CharSequence s) {
        final int[] result = new int[s.length()];
        int j = 0;
        for (int i = 0; i < s.length(); ++i) {
            final char cp = s.charAt(i);
            if (cp >= '\udc00' && cp <= '\udfff' && i != 0) {
                final char last = (char)result[j - 1];
                if (last >= '\ud800' && last <= '\udbff') {
                    result[j - 1] = Character.toCodePoint(last, cp);
                    continue;
                }
            }
            result[j++] = cp;
        }
        if (j == result.length) {
            return result;
        }
        final int[] shortResult = new int[j];
        System.arraycopy(result, 0, shortResult, 0, j);
        return shortResult;
    }
    
    private CharSequences() {
    }
}
