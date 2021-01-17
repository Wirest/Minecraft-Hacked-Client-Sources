// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.text.translate;

import java.util.Locale;
import java.io.StringWriter;
import java.io.IOException;
import java.io.Writer;

public abstract class CharSequenceTranslator
{
    public abstract int translate(final CharSequence p0, final int p1, final Writer p2) throws IOException;
    
    public final String translate(final CharSequence input) {
        if (input == null) {
            return null;
        }
        try {
            final StringWriter writer = new StringWriter(input.length() * 2);
            this.translate(input, writer);
            return writer.toString();
        }
        catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    
    public final void translate(final CharSequence input, final Writer out) throws IOException {
        if (out == null) {
            throw new IllegalArgumentException("The Writer must not be null");
        }
        if (input == null) {
            return;
        }
        char[] c;
        for (int pos = 0, len = input.length(); pos < len; pos += c.length) {
            final int consumed = this.translate(input, pos, out);
            if (consumed == 0) {
                c = Character.toChars(Character.codePointAt(input, pos));
                out.write(c);
            }
            else {
                for (int pt = 0; pt < consumed; ++pt) {
                    pos += Character.charCount(Character.codePointAt(input, pos));
                }
            }
        }
    }
    
    public final CharSequenceTranslator with(final CharSequenceTranslator... translators) {
        final CharSequenceTranslator[] newArray = new CharSequenceTranslator[translators.length + 1];
        newArray[0] = this;
        System.arraycopy(translators, 0, newArray, 1, translators.length);
        return new AggregateTranslator(newArray);
    }
    
    public static String hex(final int codepoint) {
        return Integer.toHexString(codepoint).toUpperCase(Locale.ENGLISH);
    }
}
