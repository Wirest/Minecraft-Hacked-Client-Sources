// 
// Decompiled by Procyon v0.5.36
// 

package joptsimple.internal;

import java.util.Iterator;
import java.util.List;
import java.util.Arrays;

public final class Strings
{
    public static final String EMPTY = "";
    public static final String SINGLE_QUOTE = "'";
    public static final String LINE_SEPARATOR;
    
    private Strings() {
        throw new UnsupportedOperationException();
    }
    
    public static String repeat(final char ch, final int count) {
        final StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < count; ++i) {
            buffer.append(ch);
        }
        return buffer.toString();
    }
    
    public static boolean isNullOrEmpty(final String target) {
        return target == null || "".equals(target);
    }
    
    public static String surround(final String target, final char begin, final char end) {
        return begin + target + end;
    }
    
    public static String join(final String[] pieces, final String separator) {
        return join(Arrays.asList(pieces), separator);
    }
    
    public static String join(final List<String> pieces, final String separator) {
        final StringBuilder buffer = new StringBuilder();
        final Iterator<String> iter = pieces.iterator();
        while (iter.hasNext()) {
            buffer.append(iter.next());
            if (iter.hasNext()) {
                buffer.append(separator);
            }
        }
        return buffer.toString();
    }
    
    static {
        LINE_SEPARATOR = System.getProperty("line.separator");
    }
}
