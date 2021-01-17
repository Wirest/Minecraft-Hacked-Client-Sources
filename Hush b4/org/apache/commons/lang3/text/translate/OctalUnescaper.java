// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;

public class OctalUnescaper extends CharSequenceTranslator
{
    @Override
    public int translate(final CharSequence input, final int index, final Writer out) throws IOException {
        final int remaining = input.length() - index - 1;
        final StringBuilder builder = new StringBuilder();
        if (input.charAt(index) == '\\' && remaining > 0 && this.isOctalDigit(input.charAt(index + 1))) {
            final int next = index + 1;
            final int next2 = index + 2;
            final int next3 = index + 3;
            builder.append(input.charAt(next));
            if (remaining > 1 && this.isOctalDigit(input.charAt(next2))) {
                builder.append(input.charAt(next2));
                if (remaining > 2 && this.isZeroToThree(input.charAt(next)) && this.isOctalDigit(input.charAt(next3))) {
                    builder.append(input.charAt(next3));
                }
            }
            out.write(Integer.parseInt(builder.toString(), 8));
            return 1 + builder.length();
        }
        return 0;
    }
    
    private boolean isOctalDigit(final char ch) {
        return ch >= '0' && ch <= '7';
    }
    
    private boolean isZeroToThree(final char ch) {
        return ch >= '0' && ch <= '3';
    }
}
