// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3;

public class CharSetUtils
{
    public static String squeeze(final String str, final String... set) {
        if (StringUtils.isEmpty(str) || deepEmpty(set)) {
            return str;
        }
        final CharSet chars = CharSet.getInstance(set);
        final StringBuilder buffer = new StringBuilder(str.length());
        final char[] chrs = str.toCharArray();
        final int sz = chrs.length;
        char lastChar = ' ';
        char ch = ' ';
        for (int i = 0; i < sz; ++i) {
            ch = chrs[i];
            if (ch != lastChar || i == 0 || !chars.contains(ch)) {
                buffer.append(ch);
                lastChar = ch;
            }
        }
        return buffer.toString();
    }
    
    public static boolean containsAny(final String str, final String... set) {
        if (StringUtils.isEmpty(str) || deepEmpty(set)) {
            return false;
        }
        final CharSet chars = CharSet.getInstance(set);
        for (final char c : str.toCharArray()) {
            if (chars.contains(c)) {
                return true;
            }
        }
        return false;
    }
    
    public static int count(final String str, final String... set) {
        if (StringUtils.isEmpty(str) || deepEmpty(set)) {
            return 0;
        }
        final CharSet chars = CharSet.getInstance(set);
        int count = 0;
        for (final char c : str.toCharArray()) {
            if (chars.contains(c)) {
                ++count;
            }
        }
        return count;
    }
    
    public static String keep(final String str, final String... set) {
        if (str == null) {
            return null;
        }
        if (str.isEmpty() || deepEmpty(set)) {
            return "";
        }
        return modify(str, set, true);
    }
    
    public static String delete(final String str, final String... set) {
        if (StringUtils.isEmpty(str) || deepEmpty(set)) {
            return str;
        }
        return modify(str, set, false);
    }
    
    private static String modify(final String str, final String[] set, final boolean expect) {
        final CharSet chars = CharSet.getInstance(set);
        final StringBuilder buffer = new StringBuilder(str.length());
        final char[] chrs = str.toCharArray();
        for (int sz = chrs.length, i = 0; i < sz; ++i) {
            if (chars.contains(chrs[i]) == expect) {
                buffer.append(chrs[i]);
            }
        }
        return buffer.toString();
    }
    
    private static boolean deepEmpty(final String[] strings) {
        if (strings != null) {
            for (final String s : strings) {
                if (StringUtils.isNotEmpty(s)) {
                    return false;
                }
            }
        }
        return true;
    }
}
