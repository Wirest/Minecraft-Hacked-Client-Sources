// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang3.ArrayUtils;

public class AggregateTranslator extends CharSequenceTranslator
{
    private final CharSequenceTranslator[] translators;
    
    public AggregateTranslator(final CharSequenceTranslator... translators) {
        this.translators = ArrayUtils.clone(translators);
    }
    
    @Override
    public int translate(final CharSequence input, final int index, final Writer out) throws IOException {
        for (final CharSequenceTranslator translator : this.translators) {
            final int consumed = translator.translate(input, index, out);
            if (consumed != 0) {
                return consumed;
            }
        }
        return 0;
    }
}
