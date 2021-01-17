// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.locale;

public final class AsciiUtil
{
    public static boolean caseIgnoreMatch(final String s1, final String s2) {
        if (s1 == s2) {
            return true;
        }
        final int len = s1.length();
        if (len != s2.length()) {
            return false;
        }
        int i;
        for (i = 0; i < len; ++i) {
            final char c1 = s1.charAt(i);
            final char c2 = s2.charAt(i);
            if (c1 != c2 && toLower(c1) != toLower(c2)) {
                break;
            }
        }
        return i == len;
    }
    
    public static int caseIgnoreCompare(final String s1, final String s2) {
        if (s1 == s2) {
            return 0;
        }
        return toLowerString(s1).compareTo(toLowerString(s2));
    }
    
    public static char toUpper(char c) {
        if (c >= 'a' && c <= 'z') {
            c -= ' ';
        }
        return c;
    }
    
    public static char toLower(char c) {
        if (c >= 'A' && c <= 'Z') {
            c += ' ';
        }
        return c;
    }
    
    public static String toLowerString(final String s) {
        int idx;
        for (idx = 0; idx < s.length(); ++idx) {
            final char c = s.charAt(idx);
            if (c >= 'A' && c <= 'Z') {
                break;
            }
        }
        if (idx == s.length()) {
            return s;
        }
        final StringBuilder buf = new StringBuilder(s.substring(0, idx));
        while (idx < s.length()) {
            buf.append(toLower(s.charAt(idx)));
            ++idx;
        }
        return buf.toString();
    }
    
    public static String toUpperString(final String s) {
        int idx;
        for (idx = 0; idx < s.length(); ++idx) {
            final char c = s.charAt(idx);
            if (c >= 'a' && c <= 'z') {
                break;
            }
        }
        if (idx == s.length()) {
            return s;
        }
        final StringBuilder buf = new StringBuilder(s.substring(0, idx));
        while (idx < s.length()) {
            buf.append(toUpper(s.charAt(idx)));
            ++idx;
        }
        return buf.toString();
    }
    
    public static String toTitleString(final String s) {
        if (s.length() == 0) {
            return s;
        }
        int idx = 0;
        final char c = s.charAt(idx);
        if (c < 'a' || c > 'z') {
            for (idx = 1; idx < s.length(); ++idx) {
                if (c >= 'A' && c <= 'Z') {
                    break;
                }
            }
        }
        if (idx == s.length()) {
            return s;
        }
        final StringBuilder buf = new StringBuilder(s.substring(0, idx));
        if (idx == 0) {
            buf.append(toUpper(s.charAt(idx)));
            ++idx;
        }
        while (idx < s.length()) {
            buf.append(toLower(s.charAt(idx)));
            ++idx;
        }
        return buf.toString();
    }
    
    public static boolean isAlpha(final char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }
    
    public static boolean isAlphaString(final String s) {
        boolean b = true;
        for (int i = 0; i < s.length(); ++i) {
            if (!isAlpha(s.charAt(i))) {
                b = false;
                break;
            }
        }
        return b;
    }
    
    public static boolean isNumeric(final char c) {
        return c >= '0' && c <= '9';
    }
    
    public static boolean isNumericString(final String s) {
        boolean b = true;
        for (int i = 0; i < s.length(); ++i) {
            if (!isNumeric(s.charAt(i))) {
                b = false;
                break;
            }
        }
        return b;
    }
    
    public static boolean isAlphaNumeric(final char c) {
        return isAlpha(c) || isNumeric(c);
    }
    
    public static boolean isAlphaNumericString(final String s) {
        boolean b = true;
        for (int i = 0; i < s.length(); ++i) {
            if (!isAlphaNumeric(s.charAt(i))) {
                b = false;
                break;
            }
        }
        return b;
    }
    
    public static class CaseInsensitiveKey
    {
        private String _key;
        private int _hash;
        
        public CaseInsensitiveKey(final String key) {
            this._key = key;
            this._hash = AsciiUtil.toLowerString(key).hashCode();
        }
        
        @Override
        public boolean equals(final Object o) {
            return this == o || (o instanceof CaseInsensitiveKey && AsciiUtil.caseIgnoreMatch(this._key, ((CaseInsensitiveKey)o)._key));
        }
        
        @Override
        public int hashCode() {
            return this._hash;
        }
    }
}
