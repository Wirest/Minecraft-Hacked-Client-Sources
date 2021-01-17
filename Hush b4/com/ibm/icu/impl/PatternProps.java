// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

public final class PatternProps
{
    private static final byte[] latin1;
    private static final byte[] index2000;
    private static final int[] syntax2000;
    private static final int[] syntaxOrWhiteSpace2000;
    
    public static boolean isSyntax(final int c) {
        if (c < 0) {
            return false;
        }
        if (c <= 255) {
            return PatternProps.latin1[c] == 3;
        }
        if (c < 8208) {
            return false;
        }
        if (c <= 12336) {
            final int bits = PatternProps.syntax2000[PatternProps.index2000[c - 8192 >> 5]];
            return (bits >> (c & 0x1F) & 0x1) != 0x0;
        }
        return 64830 <= c && c <= 65094 && (c <= 64831 || 65093 <= c);
    }
    
    public static boolean isSyntaxOrWhiteSpace(final int c) {
        if (c < 0) {
            return false;
        }
        if (c <= 255) {
            return PatternProps.latin1[c] != 0;
        }
        if (c < 8206) {
            return false;
        }
        if (c <= 12336) {
            final int bits = PatternProps.syntaxOrWhiteSpace2000[PatternProps.index2000[c - 8192 >> 5]];
            return (bits >> (c & 0x1F) & 0x1) != 0x0;
        }
        return 64830 <= c && c <= 65094 && (c <= 64831 || 65093 <= c);
    }
    
    public static boolean isWhiteSpace(final int c) {
        if (c < 0) {
            return false;
        }
        if (c <= 255) {
            return PatternProps.latin1[c] == 5;
        }
        return 8206 <= c && c <= 8233 && (c <= 8207 || 8232 <= c);
    }
    
    public static int skipWhiteSpace(final CharSequence s, int i) {
        while (i < s.length() && isWhiteSpace(s.charAt(i))) {
            ++i;
        }
        return i;
    }
    
    public static String trimWhiteSpace(final String s) {
        if (s.length() == 0 || (!isWhiteSpace(s.charAt(0)) && !isWhiteSpace(s.charAt(s.length() - 1)))) {
            return s;
        }
        int start;
        int limit;
        for (start = 0, limit = s.length(); start < limit && isWhiteSpace(s.charAt(start)); ++start) {}
        if (start < limit) {
            while (isWhiteSpace(s.charAt(limit - 1))) {
                --limit;
            }
        }
        return s.substring(start, limit);
    }
    
    public static boolean isIdentifier(final CharSequence s) {
        final int limit = s.length();
        if (limit == 0) {
            return false;
        }
        int start = 0;
        while (!isSyntaxOrWhiteSpace(s.charAt(start++))) {
            if (start >= limit) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isIdentifier(final CharSequence s, int start, final int limit) {
        if (start >= limit) {
            return false;
        }
        while (!isSyntaxOrWhiteSpace(s.charAt(start++))) {
            if (start >= limit) {
                return true;
            }
        }
        return false;
    }
    
    public static int skipIdentifier(final CharSequence s, int i) {
        while (i < s.length() && !isSyntaxOrWhiteSpace(s.charAt(i))) {
            ++i;
        }
        return i;
    }
    
    static {
        latin1 = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 5, 5, 5, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 3, 3, 3, 0, 3, 0, 3, 3, 0, 3, 0, 3, 3, 0, 0, 0, 0, 3, 0, 0, 0, 0, 3, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0 };
        index2000 = new byte[] { 2, 3, 4, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 6, 7, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 9 };
        syntax2000 = new int[] { 0, -1, -65536, 2147418367, 2146435070, -65536, 4194303, -1048576, -242, 65537 };
        syntaxOrWhiteSpace2000 = new int[] { 0, -1, -16384, 2147419135, 2146435070, -65536, 4194303, -1048576, -242, 65537 };
    }
}
