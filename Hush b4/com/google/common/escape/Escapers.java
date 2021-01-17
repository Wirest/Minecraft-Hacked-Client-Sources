// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.escape;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtCompatible
public final class Escapers
{
    private static final Escaper NULL_ESCAPER;
    
    private Escapers() {
    }
    
    public static Escaper nullEscaper() {
        return Escapers.NULL_ESCAPER;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    static UnicodeEscaper asUnicodeEscaper(final Escaper escaper) {
        Preconditions.checkNotNull(escaper);
        if (escaper instanceof UnicodeEscaper) {
            return (UnicodeEscaper)escaper;
        }
        if (escaper instanceof CharEscaper) {
            return wrap((CharEscaper)escaper);
        }
        throw new IllegalArgumentException("Cannot create a UnicodeEscaper from: " + escaper.getClass().getName());
    }
    
    public static String computeReplacement(final CharEscaper escaper, final char c) {
        return stringOrNull(escaper.escape(c));
    }
    
    public static String computeReplacement(final UnicodeEscaper escaper, final int cp) {
        return stringOrNull(escaper.escape(cp));
    }
    
    private static String stringOrNull(final char[] in) {
        return (in == null) ? null : new String(in);
    }
    
    private static UnicodeEscaper wrap(final CharEscaper escaper) {
        return new UnicodeEscaper() {
            @Override
            protected char[] escape(final int cp) {
                if (cp < 65536) {
                    return escaper.escape((char)cp);
                }
                final char[] surrogateChars = new char[2];
                Character.toChars(cp, surrogateChars, 0);
                final char[] hiChars = escaper.escape(surrogateChars[0]);
                final char[] loChars = escaper.escape(surrogateChars[1]);
                if (hiChars == null && loChars == null) {
                    return null;
                }
                final int hiCount = (hiChars != null) ? hiChars.length : 1;
                final int loCount = (loChars != null) ? loChars.length : 1;
                final char[] output = new char[hiCount + loCount];
                if (hiChars != null) {
                    for (int n = 0; n < hiChars.length; ++n) {
                        output[n] = hiChars[n];
                    }
                }
                else {
                    output[0] = surrogateChars[0];
                }
                if (loChars != null) {
                    for (int n = 0; n < loChars.length; ++n) {
                        output[hiCount + n] = loChars[n];
                    }
                }
                else {
                    output[hiCount] = surrogateChars[1];
                }
                return output;
            }
        };
    }
    
    static {
        NULL_ESCAPER = new CharEscaper() {
            @Override
            public String escape(final String string) {
                return Preconditions.checkNotNull(string);
            }
            
            @Override
            protected char[] escape(final char c) {
                return null;
            }
        };
    }
    
    @Beta
    public static final class Builder
    {
        private final Map<Character, String> replacementMap;
        private char safeMin;
        private char safeMax;
        private String unsafeReplacement;
        
        private Builder() {
            this.replacementMap = new HashMap<Character, String>();
            this.safeMin = '\0';
            this.safeMax = '\uffff';
            this.unsafeReplacement = null;
        }
        
        public Builder setSafeRange(final char safeMin, final char safeMax) {
            this.safeMin = safeMin;
            this.safeMax = safeMax;
            return this;
        }
        
        public Builder setUnsafeReplacement(@Nullable final String unsafeReplacement) {
            this.unsafeReplacement = unsafeReplacement;
            return this;
        }
        
        public Builder addEscape(final char c, final String replacement) {
            Preconditions.checkNotNull(replacement);
            this.replacementMap.put(c, replacement);
            return this;
        }
        
        public Escaper build() {
            return new ArrayBasedCharEscaper(this.replacementMap, this.safeMin, this.safeMax) {
                private final char[] replacementChars = (Builder.this.unsafeReplacement != null) ? Builder.this.unsafeReplacement.toCharArray() : null;
                
                @Override
                protected char[] escapeUnsafe(final char c) {
                    return this.replacementChars;
                }
            };
        }
    }
}
