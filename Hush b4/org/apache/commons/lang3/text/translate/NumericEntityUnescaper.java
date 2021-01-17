// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Arrays;
import java.util.EnumSet;

public class NumericEntityUnescaper extends CharSequenceTranslator
{
    private final EnumSet<OPTION> options;
    
    public NumericEntityUnescaper(final OPTION... options) {
        if (options.length > 0) {
            this.options = EnumSet.copyOf(Arrays.asList(options));
        }
        else {
            this.options = EnumSet.copyOf(Arrays.asList(OPTION.semiColonRequired));
        }
    }
    
    public boolean isSet(final OPTION option) {
        return this.options != null && this.options.contains(option);
    }
    
    @Override
    public int translate(final CharSequence input, final int index, final Writer out) throws IOException {
        final int seqEnd = input.length();
        if (input.charAt(index) == '&' && index < seqEnd - 2 && input.charAt(index + 1) == '#') {
            int start = index + 2;
            boolean isHex = false;
            final char firstChar = input.charAt(start);
            if (firstChar == 'x' || firstChar == 'X') {
                ++start;
                isHex = true;
                if (start == seqEnd) {
                    return 0;
                }
            }
            int end;
            for (end = start; end < seqEnd && ((input.charAt(end) >= '0' && input.charAt(end) <= '9') || (input.charAt(end) >= 'a' && input.charAt(end) <= 'f') || (input.charAt(end) >= 'A' && input.charAt(end) <= 'F')); ++end) {}
            final boolean semiNext = end != seqEnd && input.charAt(end) == ';';
            if (!semiNext) {
                if (this.isSet(OPTION.semiColonRequired)) {
                    return 0;
                }
                if (this.isSet(OPTION.errorIfNoSemiColon)) {
                    throw new IllegalArgumentException("Semi-colon required at end of numeric entity");
                }
            }
            int entityValue;
            try {
                if (isHex) {
                    entityValue = Integer.parseInt(input.subSequence(start, end).toString(), 16);
                }
                else {
                    entityValue = Integer.parseInt(input.subSequence(start, end).toString(), 10);
                }
            }
            catch (NumberFormatException nfe) {
                return 0;
            }
            if (entityValue > 65535) {
                final char[] chrs = Character.toChars(entityValue);
                out.write(chrs[0]);
                out.write(chrs[1]);
            }
            else {
                out.write(entityValue);
            }
            return 2 + end - start + (isHex ? 1 : 0) + (semiNext ? 1 : 0);
        }
        return 0;
    }
    
    public enum OPTION
    {
        semiColonRequired, 
        semiColonOptional, 
        errorIfNoSemiColon;
    }
}
