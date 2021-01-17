// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;

public class UnicodeUnescaper extends CharSequenceTranslator
{
    @Override
    public int translate(final CharSequence input, final int index, final Writer out) throws IOException {
        if (input.charAt(index) != '\\' || index + 1 >= input.length() || input.charAt(index + 1) != 'u') {
            return 0;
        }
        int i;
        for (i = 2; index + i < input.length() && input.charAt(index + i) == 'u'; ++i) {}
        if (index + i < input.length() && input.charAt(index + i) == '+') {
            ++i;
        }
        if (index + i + 4 <= input.length()) {
            final CharSequence unicode = input.subSequence(index + i, index + i + 4);
            try {
                final int value = Integer.parseInt(unicode.toString(), 16);
                out.write((char)value);
            }
            catch (NumberFormatException nfe) {
                throw new IllegalArgumentException("Unable to parse unicode value: " + (Object)unicode, nfe);
            }
            return i + 4;
        }
        throw new IllegalArgumentException("Less than 4 hex digits in unicode value: '" + (Object)input.subSequence(index, input.length()) + "' due to end of CharSequence");
    }
}
