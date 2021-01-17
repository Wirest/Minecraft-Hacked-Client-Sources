// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.text;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;
import java.util.Formatter;
import java.util.Formattable;

public class FormattableUtils
{
    private static final String SIMPLEST_FORMAT = "%s";
    
    public static String toString(final Formattable formattable) {
        return String.format("%s", formattable);
    }
    
    public static Formatter append(final CharSequence seq, final Formatter formatter, final int flags, final int width, final int precision) {
        return append(seq, formatter, flags, width, precision, ' ', null);
    }
    
    public static Formatter append(final CharSequence seq, final Formatter formatter, final int flags, final int width, final int precision, final char padChar) {
        return append(seq, formatter, flags, width, precision, padChar, null);
    }
    
    public static Formatter append(final CharSequence seq, final Formatter formatter, final int flags, final int width, final int precision, final CharSequence ellipsis) {
        return append(seq, formatter, flags, width, precision, ' ', ellipsis);
    }
    
    public static Formatter append(final CharSequence seq, final Formatter formatter, final int flags, final int width, final int precision, final char padChar, final CharSequence ellipsis) {
        Validate.isTrue(ellipsis == null || precision < 0 || ellipsis.length() <= precision, "Specified ellipsis '%1$s' exceeds precision of %2$s", ellipsis, precision);
        final StringBuilder buf = new StringBuilder(seq);
        if (precision >= 0 && precision < seq.length()) {
            final CharSequence _ellipsis = ObjectUtils.defaultIfNull(ellipsis, "");
            buf.replace(precision - _ellipsis.length(), seq.length(), _ellipsis.toString());
        }
        final boolean leftJustify = (flags & 0x1) == 0x1;
        for (int i = buf.length(); i < width; ++i) {
            buf.insert(leftJustify ? i : 0, padChar);
        }
        formatter.format(buf.toString(), new Object[0]);
        return formatter;
    }
}
