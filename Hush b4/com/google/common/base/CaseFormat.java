// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import javax.annotation.Nullable;
import java.io.Serializable;
import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public enum CaseFormat
{
    LOWER_HYPHEN(CharMatcher.is('-'), "-") {
        @Override
        String normalizeWord(final String word) {
            return Ascii.toLowerCase(word);
        }
        
        @Override
        String convert(final CaseFormat format, final String s) {
            if (format == CaseFormat$1.LOWER_UNDERSCORE) {
                return s.replace('-', '_');
            }
            if (format == CaseFormat$1.UPPER_UNDERSCORE) {
                return Ascii.toUpperCase(s.replace('-', '_'));
            }
            return super.convert(format, s);
        }
    }, 
    LOWER_UNDERSCORE(CharMatcher.is('_'), "_") {
        @Override
        String normalizeWord(final String word) {
            return Ascii.toLowerCase(word);
        }
        
        @Override
        String convert(final CaseFormat format, final String s) {
            if (format == CaseFormat$2.LOWER_HYPHEN) {
                return s.replace('_', '-');
            }
            if (format == CaseFormat$2.UPPER_UNDERSCORE) {
                return Ascii.toUpperCase(s);
            }
            return super.convert(format, s);
        }
    }, 
    LOWER_CAMEL(CharMatcher.inRange('A', 'Z'), "") {
        @Override
        String normalizeWord(final String word) {
            return firstCharOnlyToUpper(word);
        }
    }, 
    UPPER_CAMEL(CharMatcher.inRange('A', 'Z'), "") {
        @Override
        String normalizeWord(final String word) {
            return firstCharOnlyToUpper(word);
        }
    }, 
    UPPER_UNDERSCORE(CharMatcher.is('_'), "_") {
        @Override
        String normalizeWord(final String word) {
            return Ascii.toUpperCase(word);
        }
        
        @Override
        String convert(final CaseFormat format, final String s) {
            if (format == CaseFormat$5.LOWER_HYPHEN) {
                return Ascii.toLowerCase(s.replace('_', '-'));
            }
            if (format == CaseFormat$5.LOWER_UNDERSCORE) {
                return Ascii.toLowerCase(s);
            }
            return super.convert(format, s);
        }
    };
    
    private final CharMatcher wordBoundary;
    private final String wordSeparator;
    
    private CaseFormat(final CharMatcher wordBoundary, final String wordSeparator) {
        this.wordBoundary = wordBoundary;
        this.wordSeparator = wordSeparator;
    }
    
    public final String to(final CaseFormat format, final String str) {
        Preconditions.checkNotNull(format);
        Preconditions.checkNotNull(str);
        return (format == this) ? str : this.convert(format, str);
    }
    
    String convert(final CaseFormat format, final String s) {
        StringBuilder out = null;
        int i = 0;
        int j = -1;
        while ((j = this.wordBoundary.indexIn(s, ++j)) != -1) {
            if (i == 0) {
                out = new StringBuilder(s.length() + 4 * this.wordSeparator.length());
                out.append(format.normalizeFirstWord(s.substring(i, j)));
            }
            else {
                out.append(format.normalizeWord(s.substring(i, j)));
            }
            out.append(format.wordSeparator);
            i = j + this.wordSeparator.length();
        }
        return (i == 0) ? format.normalizeFirstWord(s) : out.append(format.normalizeWord(s.substring(i))).toString();
    }
    
    @Beta
    public Converter<String, String> converterTo(final CaseFormat targetFormat) {
        return new StringConverter(this, targetFormat);
    }
    
    abstract String normalizeWord(final String p0);
    
    private String normalizeFirstWord(final String word) {
        return (this == CaseFormat.LOWER_CAMEL) ? Ascii.toLowerCase(word) : this.normalizeWord(word);
    }
    
    private static String firstCharOnlyToUpper(final String word) {
        return word.isEmpty() ? word : new StringBuilder(word.length()).append(Ascii.toUpperCase(word.charAt(0))).append(Ascii.toLowerCase(word.substring(1))).toString();
    }
    
    private static final class StringConverter extends Converter<String, String> implements Serializable
    {
        private final CaseFormat sourceFormat;
        private final CaseFormat targetFormat;
        private static final long serialVersionUID = 0L;
        
        StringConverter(final CaseFormat sourceFormat, final CaseFormat targetFormat) {
            this.sourceFormat = Preconditions.checkNotNull(sourceFormat);
            this.targetFormat = Preconditions.checkNotNull(targetFormat);
        }
        
        @Override
        protected String doForward(final String s) {
            return (s == null) ? null : this.sourceFormat.to(this.targetFormat, s);
        }
        
        @Override
        protected String doBackward(final String s) {
            return (s == null) ? null : this.targetFormat.to(this.sourceFormat, s);
        }
        
        @Override
        public boolean equals(@Nullable final Object object) {
            if (object instanceof StringConverter) {
                final StringConverter that = (StringConverter)object;
                return this.sourceFormat.equals(that.sourceFormat) && this.targetFormat.equals(that.targetFormat);
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.sourceFormat.hashCode() ^ this.targetFormat.hashCode();
        }
        
        @Override
        public String toString() {
            return this.sourceFormat + ".converterTo(" + this.targetFormat + ")";
        }
    }
}
