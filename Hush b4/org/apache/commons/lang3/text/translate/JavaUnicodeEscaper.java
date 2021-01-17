// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.text.translate;

public class JavaUnicodeEscaper extends UnicodeEscaper
{
    public static JavaUnicodeEscaper above(final int codepoint) {
        return outsideOf(0, codepoint);
    }
    
    public static JavaUnicodeEscaper below(final int codepoint) {
        return outsideOf(codepoint, Integer.MAX_VALUE);
    }
    
    public static JavaUnicodeEscaper between(final int codepointLow, final int codepointHigh) {
        return new JavaUnicodeEscaper(codepointLow, codepointHigh, true);
    }
    
    public static JavaUnicodeEscaper outsideOf(final int codepointLow, final int codepointHigh) {
        return new JavaUnicodeEscaper(codepointLow, codepointHigh, false);
    }
    
    public JavaUnicodeEscaper(final int below, final int above, final boolean between) {
        super(below, above, between);
    }
    
    @Override
    protected String toUtf16Escape(final int codepoint) {
        final char[] surrogatePair = Character.toChars(codepoint);
        return "\\u" + CharSequenceTranslator.hex(surrogatePair[0]) + "\\u" + CharSequenceTranslator.hex(surrogatePair[1]);
    }
}
