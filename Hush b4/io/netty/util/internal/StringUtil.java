// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal;

import java.util.Formatter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public final class StringUtil
{
    public static final String NEWLINE;
    private static final String[] BYTE2HEX_PAD;
    private static final String[] BYTE2HEX_NOPAD;
    private static final String EMPTY_STRING = "";
    
    public static String[] split(final String value, final char delim) {
        final int end = value.length();
        final List<String> res = new ArrayList<String>();
        int start = 0;
        for (int i = 0; i < end; ++i) {
            if (value.charAt(i) == delim) {
                if (start == i) {
                    res.add("");
                }
                else {
                    res.add(value.substring(start, i));
                }
                start = i + 1;
            }
        }
        if (start == 0) {
            res.add(value);
        }
        else if (start != end) {
            res.add(value.substring(start, end));
        }
        else {
            for (int i = res.size() - 1; i >= 0 && res.get(i).isEmpty(); --i) {
                res.remove(i);
            }
        }
        return res.toArray(new String[res.size()]);
    }
    
    public static String byteToHexStringPadded(final int value) {
        return StringUtil.BYTE2HEX_PAD[value & 0xFF];
    }
    
    public static <T extends Appendable> T byteToHexStringPadded(final T buf, final int value) {
        try {
            buf.append(byteToHexStringPadded(value));
        }
        catch (IOException e) {
            PlatformDependent.throwException(e);
        }
        return buf;
    }
    
    public static String toHexStringPadded(final byte[] src) {
        return toHexStringPadded(src, 0, src.length);
    }
    
    public static String toHexStringPadded(final byte[] src, final int offset, final int length) {
        return toHexStringPadded(new StringBuilder(length << 1), src, offset, length).toString();
    }
    
    public static <T extends Appendable> T toHexStringPadded(final T dst, final byte[] src) {
        return toHexStringPadded(dst, src, 0, src.length);
    }
    
    public static <T extends Appendable> T toHexStringPadded(final T dst, final byte[] src, final int offset, final int length) {
        for (int end = offset + length, i = offset; i < end; ++i) {
            byteToHexStringPadded(dst, src[i]);
        }
        return dst;
    }
    
    public static String byteToHexString(final int value) {
        return StringUtil.BYTE2HEX_NOPAD[value & 0xFF];
    }
    
    public static <T extends Appendable> T byteToHexString(final T buf, final int value) {
        try {
            buf.append(byteToHexString(value));
        }
        catch (IOException e) {
            PlatformDependent.throwException(e);
        }
        return buf;
    }
    
    public static String toHexString(final byte[] src) {
        return toHexString(src, 0, src.length);
    }
    
    public static String toHexString(final byte[] src, final int offset, final int length) {
        return toHexString(new StringBuilder(length << 1), src, offset, length).toString();
    }
    
    public static <T extends Appendable> T toHexString(final T dst, final byte[] src) {
        return toHexString(dst, src, 0, src.length);
    }
    
    public static <T extends Appendable> T toHexString(final T dst, final byte[] src, final int offset, final int length) {
        assert length >= 0;
        if (length == 0) {
            return dst;
        }
        final int end = offset + length;
        int endMinusOne;
        int i;
        for (endMinusOne = end - 1, i = offset; i < endMinusOne && src[i] == 0; ++i) {}
        byteToHexString(dst, src[i++]);
        final int remaining = end - i;
        toHexStringPadded((Appendable)dst, src, i, remaining);
        return dst;
    }
    
    public static String simpleClassName(final Object o) {
        if (o == null) {
            return "null_object";
        }
        return simpleClassName(o.getClass());
    }
    
    public static String simpleClassName(final Class<?> clazz) {
        if (clazz == null) {
            return "null_class";
        }
        final Package pkg = clazz.getPackage();
        if (pkg != null) {
            return clazz.getName().substring(pkg.getName().length() + 1);
        }
        return clazz.getName();
    }
    
    private StringUtil() {
    }
    
    static {
        BYTE2HEX_PAD = new String[256];
        BYTE2HEX_NOPAD = new String[256];
        String newLine;
        try {
            newLine = new Formatter().format("%n", new Object[0]).toString();
        }
        catch (Exception e) {
            newLine = "\n";
        }
        NEWLINE = newLine;
        int i;
        for (i = 0; i < 10; ++i) {
            final StringBuilder buf = new StringBuilder(2);
            buf.append('0');
            buf.append(i);
            StringUtil.BYTE2HEX_PAD[i] = buf.toString();
            StringUtil.BYTE2HEX_NOPAD[i] = String.valueOf(i);
        }
        while (i < 16) {
            final StringBuilder buf = new StringBuilder(2);
            final char c = (char)(97 + i - 10);
            buf.append('0');
            buf.append(c);
            StringUtil.BYTE2HEX_PAD[i] = buf.toString();
            StringUtil.BYTE2HEX_NOPAD[i] = String.valueOf(c);
            ++i;
        }
        while (i < StringUtil.BYTE2HEX_PAD.length) {
            final StringBuilder buf = new StringBuilder(2);
            buf.append(Integer.toHexString(i));
            final String str = buf.toString();
            StringUtil.BYTE2HEX_PAD[i] = str;
            StringUtil.BYTE2HEX_NOPAD[i] = str;
            ++i;
        }
    }
}
