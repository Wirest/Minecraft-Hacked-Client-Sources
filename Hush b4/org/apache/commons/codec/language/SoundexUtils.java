// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import java.util.Locale;

final class SoundexUtils
{
    static String clean(final String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        final int len = str.length();
        final char[] chars = new char[len];
        int count = 0;
        for (int i = 0; i < len; ++i) {
            if (Character.isLetter(str.charAt(i))) {
                chars[count++] = str.charAt(i);
            }
        }
        if (count == len) {
            return str.toUpperCase(Locale.ENGLISH);
        }
        return new String(chars, 0, count).toUpperCase(Locale.ENGLISH);
    }
    
    static int difference(final StringEncoder encoder, final String s1, final String s2) throws EncoderException {
        return differenceEncoded(encoder.encode(s1), encoder.encode(s2));
    }
    
    static int differenceEncoded(final String es1, final String es2) {
        if (es1 == null || es2 == null) {
            return 0;
        }
        final int lengthToMatch = Math.min(es1.length(), es2.length());
        int diff = 0;
        for (int i = 0; i < lengthToMatch; ++i) {
            if (es1.charAt(i) == es2.charAt(i)) {
                ++diff;
            }
        }
        return diff;
    }
}
