// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import com.google.common.annotations.Beta;
import javax.annotation.CheckReturnValue;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public final class Ascii
{
    public static final byte NUL = 0;
    public static final byte SOH = 1;
    public static final byte STX = 2;
    public static final byte ETX = 3;
    public static final byte EOT = 4;
    public static final byte ENQ = 5;
    public static final byte ACK = 6;
    public static final byte BEL = 7;
    public static final byte BS = 8;
    public static final byte HT = 9;
    public static final byte LF = 10;
    public static final byte NL = 10;
    public static final byte VT = 11;
    public static final byte FF = 12;
    public static final byte CR = 13;
    public static final byte SO = 14;
    public static final byte SI = 15;
    public static final byte DLE = 16;
    public static final byte DC1 = 17;
    public static final byte XON = 17;
    public static final byte DC2 = 18;
    public static final byte DC3 = 19;
    public static final byte XOFF = 19;
    public static final byte DC4 = 20;
    public static final byte NAK = 21;
    public static final byte SYN = 22;
    public static final byte ETB = 23;
    public static final byte CAN = 24;
    public static final byte EM = 25;
    public static final byte SUB = 26;
    public static final byte ESC = 27;
    public static final byte FS = 28;
    public static final byte GS = 29;
    public static final byte RS = 30;
    public static final byte US = 31;
    public static final byte SP = 32;
    public static final byte SPACE = 32;
    public static final byte DEL = Byte.MAX_VALUE;
    public static final char MIN = '\0';
    public static final char MAX = '\u007f';
    
    private Ascii() {
    }
    
    public static String toLowerCase(final String string) {
        for (int length = string.length(), i = 0; i < length; ++i) {
            if (isUpperCase(string.charAt(i))) {
                final char[] chars = string.toCharArray();
                while (i < length) {
                    final char c = chars[i];
                    if (isUpperCase(c)) {
                        chars[i] = (char)(c ^ ' ');
                    }
                    ++i;
                }
                return String.valueOf(chars);
            }
        }
        return string;
    }
    
    public static String toLowerCase(final CharSequence chars) {
        if (chars instanceof String) {
            return toLowerCase((String)chars);
        }
        final int length = chars.length();
        final StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            builder.append(toLowerCase(chars.charAt(i)));
        }
        return builder.toString();
    }
    
    public static char toLowerCase(final char c) {
        return isUpperCase(c) ? ((char)(c ^ ' ')) : c;
    }
    
    public static String toUpperCase(final String string) {
        for (int length = string.length(), i = 0; i < length; ++i) {
            if (isLowerCase(string.charAt(i))) {
                final char[] chars = string.toCharArray();
                while (i < length) {
                    final char c = chars[i];
                    if (isLowerCase(c)) {
                        chars[i] = (char)(c & '_');
                    }
                    ++i;
                }
                return String.valueOf(chars);
            }
        }
        return string;
    }
    
    public static String toUpperCase(final CharSequence chars) {
        if (chars instanceof String) {
            return toUpperCase((String)chars);
        }
        final int length = chars.length();
        final StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            builder.append(toUpperCase(chars.charAt(i)));
        }
        return builder.toString();
    }
    
    public static char toUpperCase(final char c) {
        return isLowerCase(c) ? ((char)(c & '_')) : c;
    }
    
    public static boolean isLowerCase(final char c) {
        return c >= 'a' && c <= 'z';
    }
    
    public static boolean isUpperCase(final char c) {
        return c >= 'A' && c <= 'Z';
    }
    
    @CheckReturnValue
    @Beta
    public static String truncate(CharSequence seq, final int maxLength, final String truncationIndicator) {
        Preconditions.checkNotNull(seq);
        final int truncationLength = maxLength - truncationIndicator.length();
        Preconditions.checkArgument(truncationLength >= 0, "maxLength (%s) must be >= length of the truncation indicator (%s)", maxLength, truncationIndicator.length());
        if (seq.length() <= maxLength) {
            final String string = seq.toString();
            if (string.length() <= maxLength) {
                return string;
            }
            seq = string;
        }
        return new StringBuilder(maxLength).append(seq, 0, truncationLength).append(truncationIndicator).toString();
    }
    
    @Beta
    public static boolean equalsIgnoreCase(final CharSequence s1, final CharSequence s2) {
        final int length = s1.length();
        if (s1 == s2) {
            return true;
        }
        if (length != s2.length()) {
            return false;
        }
        for (int i = 0; i < length; ++i) {
            final char c1 = s1.charAt(i);
            final char c2 = s2.charAt(i);
            if (c1 != c2) {
                final int alphaIndex = getAlphaIndex(c1);
                if (alphaIndex >= 26 || alphaIndex != getAlphaIndex(c2)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private static int getAlphaIndex(final char c) {
        return (char)((c | ' ') - 97);
    }
}
