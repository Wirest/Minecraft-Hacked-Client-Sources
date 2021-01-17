// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import javax.annotation.CheckReturnValue;
import java.util.Arrays;
import com.google.common.annotations.GwtIncompatible;
import java.util.BitSet;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtCompatible(emulated = true)
public abstract class CharMatcher implements Predicate<Character>
{
    public static final CharMatcher BREAKING_WHITESPACE;
    public static final CharMatcher ASCII;
    private static final String ZEROES = "0\u0660\u06f0\u07c0\u0966\u09e6\u0a66\u0ae6\u0b66\u0be6\u0c66\u0ce6\u0d66\u0e50\u0ed0\u0f20\u1040\u1090\u17e0\u1810\u1946\u19d0\u1b50\u1bb0\u1c40\u1c50\ua620\ua8d0\ua900\uaa50\uff10";
    private static final String NINES;
    public static final CharMatcher DIGIT;
    public static final CharMatcher JAVA_DIGIT;
    public static final CharMatcher JAVA_LETTER;
    public static final CharMatcher JAVA_LETTER_OR_DIGIT;
    public static final CharMatcher JAVA_UPPER_CASE;
    public static final CharMatcher JAVA_LOWER_CASE;
    public static final CharMatcher JAVA_ISO_CONTROL;
    public static final CharMatcher INVISIBLE;
    public static final CharMatcher SINGLE_WIDTH;
    public static final CharMatcher ANY;
    public static final CharMatcher NONE;
    final String description;
    private static final int DISTINCT_CHARS = 65536;
    static final String WHITESPACE_TABLE = "\u2002\u3000\r\u0085\u200a\u2005\u2000\u3000\u2029\u000b\u3000\u2008\u2003\u205f\u3000\u1680\t \u2006\u2001\u202f \f\u2009\u3000\u2004\u3000\u3000\u2028\n\u2007\u3000";
    static final int WHITESPACE_MULTIPLIER = 1682554634;
    static final int WHITESPACE_SHIFT;
    public static final CharMatcher WHITESPACE;
    
    private static String showCharacter(char c) {
        final String hex = "0123456789ABCDEF";
        final char[] tmp = { '\\', 'u', '\0', '\0', '\0', '\0' };
        for (int i = 0; i < 4; ++i) {
            tmp[5 - i] = hex.charAt(c & '\u000f');
            c >>= 4;
        }
        return String.copyValueOf(tmp);
    }
    
    public static CharMatcher is(final char match) {
        final String description = "CharMatcher.is('" + showCharacter(match) + "')";
        return new FastMatcher(description) {
            @Override
            public boolean matches(final char c) {
                return c == match;
            }
            
            @Override
            public String replaceFrom(final CharSequence sequence, final char replacement) {
                return sequence.toString().replace(match, replacement);
            }
            
            @Override
            public CharMatcher and(final CharMatcher other) {
                return other.matches(match) ? this : CharMatcher$9.NONE;
            }
            
            @Override
            public CharMatcher or(final CharMatcher other) {
                return other.matches(match) ? other : super.or(other);
            }
            
            @Override
            public CharMatcher negate() {
                return CharMatcher.isNot(match);
            }
            
            @GwtIncompatible("java.util.BitSet")
            @Override
            void setBits(final BitSet table) {
                table.set(match);
            }
        };
    }
    
    public static CharMatcher isNot(final char match) {
        final String description = "CharMatcher.isNot('" + showCharacter(match) + "')";
        return new FastMatcher(description) {
            @Override
            public boolean matches(final char c) {
                return c != match;
            }
            
            @Override
            public CharMatcher and(final CharMatcher other) {
                return other.matches(match) ? super.and(other) : other;
            }
            
            @Override
            public CharMatcher or(final CharMatcher other) {
                return other.matches(match) ? CharMatcher$10.ANY : this;
            }
            
            @GwtIncompatible("java.util.BitSet")
            @Override
            void setBits(final BitSet table) {
                table.set(0, match);
                table.set(match + '\u0001', 65536);
            }
            
            @Override
            public CharMatcher negate() {
                return CharMatcher.is(match);
            }
        };
    }
    
    public static CharMatcher anyOf(final CharSequence sequence) {
        switch (sequence.length()) {
            case 0: {
                return CharMatcher.NONE;
            }
            case 1: {
                return is(sequence.charAt(0));
            }
            case 2: {
                return isEither(sequence.charAt(0), sequence.charAt(1));
            }
            default: {
                final char[] chars = sequence.toString().toCharArray();
                Arrays.sort(chars);
                final StringBuilder description = new StringBuilder("CharMatcher.anyOf(\"");
                for (final char c : chars) {
                    description.append(showCharacter(c));
                }
                description.append("\")");
                return new CharMatcher(description.toString()) {
                    @Override
                    public boolean matches(final char c) {
                        return Arrays.binarySearch(chars, c) >= 0;
                    }
                    
                    @GwtIncompatible("java.util.BitSet")
                    @Override
                    void setBits(final BitSet table) {
                        for (final char c : chars) {
                            table.set(c);
                        }
                    }
                };
            }
        }
    }
    
    private static CharMatcher isEither(final char match1, final char match2) {
        final String description = "CharMatcher.anyOf(\"" + showCharacter(match1) + showCharacter(match2) + "\")";
        return new FastMatcher(description) {
            @Override
            public boolean matches(final char c) {
                return c == match1 || c == match2;
            }
            
            @GwtIncompatible("java.util.BitSet")
            @Override
            void setBits(final BitSet table) {
                table.set(match1);
                table.set(match2);
            }
        };
    }
    
    public static CharMatcher noneOf(final CharSequence sequence) {
        return anyOf(sequence).negate();
    }
    
    public static CharMatcher inRange(final char startInclusive, final char endInclusive) {
        Preconditions.checkArgument(endInclusive >= startInclusive);
        final String description = "CharMatcher.inRange('" + showCharacter(startInclusive) + "', '" + showCharacter(endInclusive) + "')";
        return inRange(startInclusive, endInclusive, description);
    }
    
    static CharMatcher inRange(final char startInclusive, final char endInclusive, final String description) {
        return new FastMatcher(description) {
            @Override
            public boolean matches(final char c) {
                return startInclusive <= c && c <= endInclusive;
            }
            
            @GwtIncompatible("java.util.BitSet")
            @Override
            void setBits(final BitSet table) {
                table.set(startInclusive, endInclusive + '\u0001');
            }
        };
    }
    
    public static CharMatcher forPredicate(final Predicate<? super Character> predicate) {
        Preconditions.checkNotNull(predicate);
        if (predicate instanceof CharMatcher) {
            return (CharMatcher)predicate;
        }
        final String description = "CharMatcher.forPredicate(" + predicate + ")";
        return new CharMatcher(description) {
            @Override
            public boolean matches(final char c) {
                return predicate.apply(c);
            }
            
            @Override
            public boolean apply(final Character character) {
                return predicate.apply(Preconditions.checkNotNull(character));
            }
        };
    }
    
    CharMatcher(final String description) {
        this.description = description;
    }
    
    protected CharMatcher() {
        this.description = super.toString();
    }
    
    public abstract boolean matches(final char p0);
    
    public CharMatcher negate() {
        return new NegatedMatcher(this);
    }
    
    public CharMatcher and(final CharMatcher other) {
        return new And(this, Preconditions.checkNotNull(other));
    }
    
    public CharMatcher or(final CharMatcher other) {
        return new Or(this, Preconditions.checkNotNull(other));
    }
    
    public CharMatcher precomputed() {
        return Platform.precomputeCharMatcher(this);
    }
    
    CharMatcher withToString(final String description) {
        throw new UnsupportedOperationException();
    }
    
    @GwtIncompatible("java.util.BitSet")
    CharMatcher precomputedInternal() {
        final BitSet table = new BitSet();
        this.setBits(table);
        final int totalCharacters = table.cardinality();
        if (totalCharacters * 2 <= 65536) {
            return precomputedPositive(totalCharacters, table, this.description);
        }
        table.flip(0, 65536);
        final int negatedCharacters = 65536 - totalCharacters;
        final String suffix = ".negate()";
        final String negatedDescription = this.description.endsWith(suffix) ? this.description.substring(0, this.description.length() - suffix.length()) : (this.description + suffix);
        return new NegatedFastMatcher(this.toString(), precomputedPositive(negatedCharacters, table, negatedDescription));
    }
    
    @GwtIncompatible("java.util.BitSet")
    private static CharMatcher precomputedPositive(final int totalCharacters, final BitSet table, final String description) {
        switch (totalCharacters) {
            case 0: {
                return CharMatcher.NONE;
            }
            case 1: {
                return is((char)table.nextSetBit(0));
            }
            case 2: {
                final char c1 = (char)table.nextSetBit(0);
                final char c2 = (char)table.nextSetBit(c1 + '\u0001');
                return isEither(c1, c2);
            }
            default: {
                return isSmall(totalCharacters, table.length()) ? SmallCharMatcher.from(table, description) : new BitSetMatcher(table, description);
            }
        }
    }
    
    @GwtIncompatible("SmallCharMatcher")
    private static boolean isSmall(final int totalCharacters, final int tableLength) {
        return totalCharacters <= 1023 && tableLength > totalCharacters * 4 * 16;
    }
    
    @GwtIncompatible("java.util.BitSet")
    void setBits(final BitSet table) {
        for (int c = 65535; c >= 0; --c) {
            if (this.matches((char)c)) {
                table.set(c);
            }
        }
    }
    
    public boolean matchesAnyOf(final CharSequence sequence) {
        return !this.matchesNoneOf(sequence);
    }
    
    public boolean matchesAllOf(final CharSequence sequence) {
        for (int i = sequence.length() - 1; i >= 0; --i) {
            if (!this.matches(sequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    
    public boolean matchesNoneOf(final CharSequence sequence) {
        return this.indexIn(sequence) == -1;
    }
    
    public int indexIn(final CharSequence sequence) {
        for (int length = sequence.length(), i = 0; i < length; ++i) {
            if (this.matches(sequence.charAt(i))) {
                return i;
            }
        }
        return -1;
    }
    
    public int indexIn(final CharSequence sequence, final int start) {
        final int length = sequence.length();
        Preconditions.checkPositionIndex(start, length);
        for (int i = start; i < length; ++i) {
            if (this.matches(sequence.charAt(i))) {
                return i;
            }
        }
        return -1;
    }
    
    public int lastIndexIn(final CharSequence sequence) {
        for (int i = sequence.length() - 1; i >= 0; --i) {
            if (this.matches(sequence.charAt(i))) {
                return i;
            }
        }
        return -1;
    }
    
    public int countIn(final CharSequence sequence) {
        int count = 0;
        for (int i = 0; i < sequence.length(); ++i) {
            if (this.matches(sequence.charAt(i))) {
                ++count;
            }
        }
        return count;
    }
    
    @CheckReturnValue
    public String removeFrom(final CharSequence sequence) {
        final String string = sequence.toString();
        int pos = this.indexIn(string);
        if (pos == -1) {
            return string;
        }
        final char[] chars = string.toCharArray();
        int spread = 1;
    Label_0029:
        while (true) {
            ++pos;
            while (pos != chars.length) {
                if (this.matches(chars[pos])) {
                    ++spread;
                    continue Label_0029;
                }
                chars[pos - spread] = chars[pos];
                ++pos;
            }
            break;
        }
        return new String(chars, 0, pos - spread);
    }
    
    @CheckReturnValue
    public String retainFrom(final CharSequence sequence) {
        return this.negate().removeFrom(sequence);
    }
    
    @CheckReturnValue
    public String replaceFrom(final CharSequence sequence, final char replacement) {
        final String string = sequence.toString();
        final int pos = this.indexIn(string);
        if (pos == -1) {
            return string;
        }
        final char[] chars = string.toCharArray();
        chars[pos] = replacement;
        for (int i = pos + 1; i < chars.length; ++i) {
            if (this.matches(chars[i])) {
                chars[i] = replacement;
            }
        }
        return new String(chars);
    }
    
    @CheckReturnValue
    public String replaceFrom(final CharSequence sequence, final CharSequence replacement) {
        final int replacementLen = replacement.length();
        if (replacementLen == 0) {
            return this.removeFrom(sequence);
        }
        if (replacementLen == 1) {
            return this.replaceFrom(sequence, replacement.charAt(0));
        }
        final String string = sequence.toString();
        int pos = this.indexIn(string);
        if (pos == -1) {
            return string;
        }
        final int len = string.length();
        final StringBuilder buf = new StringBuilder(len * 3 / 2 + 16);
        int oldpos = 0;
        do {
            buf.append(string, oldpos, pos);
            buf.append(replacement);
            oldpos = pos + 1;
            pos = this.indexIn(string, oldpos);
        } while (pos != -1);
        buf.append(string, oldpos, len);
        return buf.toString();
    }
    
    @CheckReturnValue
    public String trimFrom(final CharSequence sequence) {
        int len;
        int first;
        for (len = sequence.length(), first = 0; first < len && this.matches(sequence.charAt(first)); ++first) {}
        int last;
        for (last = len - 1; last > first && this.matches(sequence.charAt(last)); --last) {}
        return sequence.subSequence(first, last + 1).toString();
    }
    
    @CheckReturnValue
    public String trimLeadingFrom(final CharSequence sequence) {
        for (int len = sequence.length(), first = 0; first < len; ++first) {
            if (!this.matches(sequence.charAt(first))) {
                return sequence.subSequence(first, len).toString();
            }
        }
        return "";
    }
    
    @CheckReturnValue
    public String trimTrailingFrom(final CharSequence sequence) {
        final int len = sequence.length();
        for (int last = len - 1; last >= 0; --last) {
            if (!this.matches(sequence.charAt(last))) {
                return sequence.subSequence(0, last + 1).toString();
            }
        }
        return "";
    }
    
    @CheckReturnValue
    public String collapseFrom(final CharSequence sequence, final char replacement) {
        for (int len = sequence.length(), i = 0; i < len; ++i) {
            final char c = sequence.charAt(i);
            if (this.matches(c)) {
                if (c != replacement || (i != len - 1 && this.matches(sequence.charAt(i + 1)))) {
                    final StringBuilder builder = new StringBuilder(len).append(sequence.subSequence(0, i)).append(replacement);
                    return this.finishCollapseFrom(sequence, i + 1, len, replacement, builder, true);
                }
                ++i;
            }
        }
        return sequence.toString();
    }
    
    @CheckReturnValue
    public String trimAndCollapseFrom(final CharSequence sequence, final char replacement) {
        int len;
        int first;
        for (len = sequence.length(), first = 0; first < len && this.matches(sequence.charAt(first)); ++first) {}
        int last;
        for (last = len - 1; last > first && this.matches(sequence.charAt(last)); --last) {}
        return (first == 0 && last == len - 1) ? this.collapseFrom(sequence, replacement) : this.finishCollapseFrom(sequence, first, last + 1, replacement, new StringBuilder(last + 1 - first), false);
    }
    
    private String finishCollapseFrom(final CharSequence sequence, final int start, final int end, final char replacement, final StringBuilder builder, boolean inMatchingGroup) {
        for (int i = start; i < end; ++i) {
            final char c = sequence.charAt(i);
            if (this.matches(c)) {
                if (!inMatchingGroup) {
                    builder.append(replacement);
                    inMatchingGroup = true;
                }
            }
            else {
                builder.append(c);
                inMatchingGroup = false;
            }
        }
        return builder.toString();
    }
    
    @Deprecated
    @Override
    public boolean apply(final Character character) {
        return this.matches(character);
    }
    
    @Override
    public String toString() {
        return this.description;
    }
    
    static {
        BREAKING_WHITESPACE = new CharMatcher() {
            @Override
            public boolean matches(final char c) {
                switch (c) {
                    case '\t':
                    case '\n':
                    case '\u000b':
                    case '\f':
                    case '\r':
                    case ' ':
                    case '\u0085':
                    case '\u1680':
                    case '\u2028':
                    case '\u2029':
                    case '\u205f':
                    case '\u3000': {
                        return true;
                    }
                    case '\u2007': {
                        return false;
                    }
                    default: {
                        return c >= '\u2000' && c <= '\u200a';
                    }
                }
            }
            
            @Override
            public String toString() {
                return "CharMatcher.BREAKING_WHITESPACE";
            }
        };
        ASCII = inRange('\0', '\u007f', "CharMatcher.ASCII");
        final StringBuilder builder = new StringBuilder("0\u0660\u06f0\u07c0\u0966\u09e6\u0a66\u0ae6\u0b66\u0be6\u0c66\u0ce6\u0d66\u0e50\u0ed0\u0f20\u1040\u1090\u17e0\u1810\u1946\u19d0\u1b50\u1bb0\u1c40\u1c50\ua620\ua8d0\ua900\uaa50\uff10".length());
        for (int i = 0; i < "0\u0660\u06f0\u07c0\u0966\u09e6\u0a66\u0ae6\u0b66\u0be6\u0c66\u0ce6\u0d66\u0e50\u0ed0\u0f20\u1040\u1090\u17e0\u1810\u1946\u19d0\u1b50\u1bb0\u1c40\u1c50\ua620\ua8d0\ua900\uaa50\uff10".length(); ++i) {
            builder.append((char)("0\u0660\u06f0\u07c0\u0966\u09e6\u0a66\u0ae6\u0b66\u0be6\u0c66\u0ce6\u0d66\u0e50\u0ed0\u0f20\u1040\u1090\u17e0\u1810\u1946\u19d0\u1b50\u1bb0\u1c40\u1c50\ua620\ua8d0\ua900\uaa50\uff10".charAt(i) + '\t'));
        }
        NINES = builder.toString();
        DIGIT = new RangesMatcher("CharMatcher.DIGIT", "0\u0660\u06f0\u07c0\u0966\u09e6\u0a66\u0ae6\u0b66\u0be6\u0c66\u0ce6\u0d66\u0e50\u0ed0\u0f20\u1040\u1090\u17e0\u1810\u1946\u19d0\u1b50\u1bb0\u1c40\u1c50\ua620\ua8d0\ua900\uaa50\uff10".toCharArray(), CharMatcher.NINES.toCharArray());
        JAVA_DIGIT = new CharMatcher("CharMatcher.JAVA_DIGIT") {
            @Override
            public boolean matches(final char c) {
                return Character.isDigit(c);
            }
        };
        JAVA_LETTER = new CharMatcher("CharMatcher.JAVA_LETTER") {
            @Override
            public boolean matches(final char c) {
                return Character.isLetter(c);
            }
        };
        JAVA_LETTER_OR_DIGIT = new CharMatcher("CharMatcher.JAVA_LETTER_OR_DIGIT") {
            @Override
            public boolean matches(final char c) {
                return Character.isLetterOrDigit(c);
            }
        };
        JAVA_UPPER_CASE = new CharMatcher("CharMatcher.JAVA_UPPER_CASE") {
            @Override
            public boolean matches(final char c) {
                return Character.isUpperCase(c);
            }
        };
        JAVA_LOWER_CASE = new CharMatcher("CharMatcher.JAVA_LOWER_CASE") {
            @Override
            public boolean matches(final char c) {
                return Character.isLowerCase(c);
            }
        };
        JAVA_ISO_CONTROL = inRange('\0', '\u001f').or(inRange('\u007f', '\u009f')).withToString("CharMatcher.JAVA_ISO_CONTROL");
        INVISIBLE = new RangesMatcher("CharMatcher.INVISIBLE", "\u0000\u007f\u00ad\u0600\u061c\u06dd\u070f\u1680\u180e\u2000\u2028\u205f\u2066\u2067\u2068\u2069\u206a\u3000\ud800\ufeff\ufff9\ufffa".toCharArray(), "  \u00ad\u0604\u061c\u06dd\u070f\u1680\u180e\u200f\u202f\u2064\u2066\u2067\u2068\u2069\u206f\u3000\uf8ff\ufeff\ufff9\ufffb".toCharArray());
        SINGLE_WIDTH = new RangesMatcher("CharMatcher.SINGLE_WIDTH", "\u0000\u05be\u05d0\u05f3\u0600\u0750\u0e00\u1e00\u2100\ufb50\ufe70\uff61".toCharArray(), "\u04f9\u05be\u05ea\u05f4\u06ff\u077f\u0e7f\u20af\u213a\ufdff\ufeff\uffdc".toCharArray());
        ANY = new FastMatcher("CharMatcher.ANY") {
            @Override
            public boolean matches(final char c) {
                return true;
            }
            
            @Override
            public int indexIn(final CharSequence sequence) {
                return (sequence.length() == 0) ? -1 : 0;
            }
            
            @Override
            public int indexIn(final CharSequence sequence, final int start) {
                final int length = sequence.length();
                Preconditions.checkPositionIndex(start, length);
                return (start == length) ? -1 : start;
            }
            
            @Override
            public int lastIndexIn(final CharSequence sequence) {
                return sequence.length() - 1;
            }
            
            @Override
            public boolean matchesAllOf(final CharSequence sequence) {
                Preconditions.checkNotNull(sequence);
                return true;
            }
            
            @Override
            public boolean matchesNoneOf(final CharSequence sequence) {
                return sequence.length() == 0;
            }
            
            @Override
            public String removeFrom(final CharSequence sequence) {
                Preconditions.checkNotNull(sequence);
                return "";
            }
            
            @Override
            public String replaceFrom(final CharSequence sequence, final char replacement) {
                final char[] array = new char[sequence.length()];
                Arrays.fill(array, replacement);
                return new String(array);
            }
            
            @Override
            public String replaceFrom(final CharSequence sequence, final CharSequence replacement) {
                final StringBuilder retval = new StringBuilder(sequence.length() * replacement.length());
                for (int i = 0; i < sequence.length(); ++i) {
                    retval.append(replacement);
                }
                return retval.toString();
            }
            
            @Override
            public String collapseFrom(final CharSequence sequence, final char replacement) {
                return (sequence.length() == 0) ? "" : String.valueOf(replacement);
            }
            
            @Override
            public String trimFrom(final CharSequence sequence) {
                Preconditions.checkNotNull(sequence);
                return "";
            }
            
            @Override
            public int countIn(final CharSequence sequence) {
                return sequence.length();
            }
            
            @Override
            public CharMatcher and(final CharMatcher other) {
                return Preconditions.checkNotNull(other);
            }
            
            @Override
            public CharMatcher or(final CharMatcher other) {
                Preconditions.checkNotNull(other);
                return this;
            }
            
            @Override
            public CharMatcher negate() {
                return CharMatcher$7.NONE;
            }
        };
        NONE = new FastMatcher("CharMatcher.NONE") {
            @Override
            public boolean matches(final char c) {
                return false;
            }
            
            @Override
            public int indexIn(final CharSequence sequence) {
                Preconditions.checkNotNull(sequence);
                return -1;
            }
            
            @Override
            public int indexIn(final CharSequence sequence, final int start) {
                final int length = sequence.length();
                Preconditions.checkPositionIndex(start, length);
                return -1;
            }
            
            @Override
            public int lastIndexIn(final CharSequence sequence) {
                Preconditions.checkNotNull(sequence);
                return -1;
            }
            
            @Override
            public boolean matchesAllOf(final CharSequence sequence) {
                return sequence.length() == 0;
            }
            
            @Override
            public boolean matchesNoneOf(final CharSequence sequence) {
                Preconditions.checkNotNull(sequence);
                return true;
            }
            
            @Override
            public String removeFrom(final CharSequence sequence) {
                return sequence.toString();
            }
            
            @Override
            public String replaceFrom(final CharSequence sequence, final char replacement) {
                return sequence.toString();
            }
            
            @Override
            public String replaceFrom(final CharSequence sequence, final CharSequence replacement) {
                Preconditions.checkNotNull(replacement);
                return sequence.toString();
            }
            
            @Override
            public String collapseFrom(final CharSequence sequence, final char replacement) {
                return sequence.toString();
            }
            
            @Override
            public String trimFrom(final CharSequence sequence) {
                return sequence.toString();
            }
            
            @Override
            public String trimLeadingFrom(final CharSequence sequence) {
                return sequence.toString();
            }
            
            @Override
            public String trimTrailingFrom(final CharSequence sequence) {
                return sequence.toString();
            }
            
            @Override
            public int countIn(final CharSequence sequence) {
                Preconditions.checkNotNull(sequence);
                return 0;
            }
            
            @Override
            public CharMatcher and(final CharMatcher other) {
                Preconditions.checkNotNull(other);
                return this;
            }
            
            @Override
            public CharMatcher or(final CharMatcher other) {
                return Preconditions.checkNotNull(other);
            }
            
            @Override
            public CharMatcher negate() {
                return CharMatcher$8.ANY;
            }
        };
        WHITESPACE_SHIFT = Integer.numberOfLeadingZeros("\u2002\u3000\r\u0085\u200a\u2005\u2000\u3000\u2029\u000b\u3000\u2008\u2003\u205f\u3000\u1680\t \u2006\u2001\u202f \f\u2009\u3000\u2004\u3000\u3000\u2028\n\u2007\u3000".length() - 1);
        WHITESPACE = new FastMatcher("WHITESPACE") {
            @Override
            public boolean matches(final char c) {
                return "\u2002\u3000\r\u0085\u200a\u2005\u2000\u3000\u2029\u000b\u3000\u2008\u2003\u205f\u3000\u1680\t \u2006\u2001\u202f \f\u2009\u3000\u2004\u3000\u3000\u2028\n\u2007\u3000".charAt(1682554634 * c >>> CharMatcher$15.WHITESPACE_SHIFT) == c;
            }
            
            @GwtIncompatible("java.util.BitSet")
            @Override
            void setBits(final BitSet table) {
                for (int i = 0; i < "\u2002\u3000\r\u0085\u200a\u2005\u2000\u3000\u2029\u000b\u3000\u2008\u2003\u205f\u3000\u1680\t \u2006\u2001\u202f \f\u2009\u3000\u2004\u3000\u3000\u2028\n\u2007\u3000".length(); ++i) {
                    table.set("\u2002\u3000\r\u0085\u200a\u2005\u2000\u3000\u2029\u000b\u3000\u2008\u2003\u205f\u3000\u1680\t \u2006\u2001\u202f \f\u2009\u3000\u2004\u3000\u3000\u2028\n\u2007\u3000".charAt(i));
                }
            }
        };
    }
    
    private static class RangesMatcher extends CharMatcher
    {
        private final char[] rangeStarts;
        private final char[] rangeEnds;
        
        RangesMatcher(final String description, final char[] rangeStarts, final char[] rangeEnds) {
            super(description);
            this.rangeStarts = rangeStarts;
            this.rangeEnds = rangeEnds;
            Preconditions.checkArgument(rangeStarts.length == rangeEnds.length);
            for (int i = 0; i < rangeStarts.length; ++i) {
                Preconditions.checkArgument(rangeStarts[i] <= rangeEnds[i]);
                if (i + 1 < rangeStarts.length) {
                    Preconditions.checkArgument(rangeEnds[i] < rangeStarts[i + 1]);
                }
            }
        }
        
        @Override
        public boolean matches(final char c) {
            int index = Arrays.binarySearch(this.rangeStarts, c);
            if (index >= 0) {
                return true;
            }
            index = ~index - 1;
            return index >= 0 && c <= this.rangeEnds[index];
        }
    }
    
    private static class NegatedMatcher extends CharMatcher
    {
        final CharMatcher original;
        
        NegatedMatcher(final String toString, final CharMatcher original) {
            super(toString);
            this.original = original;
        }
        
        NegatedMatcher(final CharMatcher original) {
            this(original + ".negate()", original);
        }
        
        @Override
        public boolean matches(final char c) {
            return !this.original.matches(c);
        }
        
        @Override
        public boolean matchesAllOf(final CharSequence sequence) {
            return this.original.matchesNoneOf(sequence);
        }
        
        @Override
        public boolean matchesNoneOf(final CharSequence sequence) {
            return this.original.matchesAllOf(sequence);
        }
        
        @Override
        public int countIn(final CharSequence sequence) {
            return sequence.length() - this.original.countIn(sequence);
        }
        
        @GwtIncompatible("java.util.BitSet")
        @Override
        void setBits(final BitSet table) {
            final BitSet tmp = new BitSet();
            this.original.setBits(tmp);
            tmp.flip(0, 65536);
            table.or(tmp);
        }
        
        @Override
        public CharMatcher negate() {
            return this.original;
        }
        
        @Override
        CharMatcher withToString(final String description) {
            return new NegatedMatcher(description, this.original);
        }
    }
    
    private static class And extends CharMatcher
    {
        final CharMatcher first;
        final CharMatcher second;
        
        And(final CharMatcher a, final CharMatcher b) {
            this(a, b, "CharMatcher.and(" + a + ", " + b + ")");
        }
        
        And(final CharMatcher a, final CharMatcher b, final String description) {
            super(description);
            this.first = Preconditions.checkNotNull(a);
            this.second = Preconditions.checkNotNull(b);
        }
        
        @Override
        public boolean matches(final char c) {
            return this.first.matches(c) && this.second.matches(c);
        }
        
        @GwtIncompatible("java.util.BitSet")
        @Override
        void setBits(final BitSet table) {
            final BitSet tmp1 = new BitSet();
            this.first.setBits(tmp1);
            final BitSet tmp2 = new BitSet();
            this.second.setBits(tmp2);
            tmp1.and(tmp2);
            table.or(tmp1);
        }
        
        @Override
        CharMatcher withToString(final String description) {
            return new And(this.first, this.second, description);
        }
    }
    
    private static class Or extends CharMatcher
    {
        final CharMatcher first;
        final CharMatcher second;
        
        Or(final CharMatcher a, final CharMatcher b, final String description) {
            super(description);
            this.first = Preconditions.checkNotNull(a);
            this.second = Preconditions.checkNotNull(b);
        }
        
        Or(final CharMatcher a, final CharMatcher b) {
            this(a, b, "CharMatcher.or(" + a + ", " + b + ")");
        }
        
        @GwtIncompatible("java.util.BitSet")
        @Override
        void setBits(final BitSet table) {
            this.first.setBits(table);
            this.second.setBits(table);
        }
        
        @Override
        public boolean matches(final char c) {
            return this.first.matches(c) || this.second.matches(c);
        }
        
        @Override
        CharMatcher withToString(final String description) {
            return new Or(this.first, this.second, description);
        }
    }
    
    abstract static class FastMatcher extends CharMatcher
    {
        FastMatcher() {
        }
        
        FastMatcher(final String description) {
            super(description);
        }
        
        @Override
        public final CharMatcher precomputed() {
            return this;
        }
        
        @Override
        public CharMatcher negate() {
            return new NegatedFastMatcher(this);
        }
    }
    
    static final class NegatedFastMatcher extends NegatedMatcher
    {
        NegatedFastMatcher(final CharMatcher original) {
            super(original);
        }
        
        NegatedFastMatcher(final String toString, final CharMatcher original) {
            super(toString, original);
        }
        
        @Override
        public final CharMatcher precomputed() {
            return this;
        }
        
        @Override
        CharMatcher withToString(final String description) {
            return new NegatedFastMatcher(description, this.original);
        }
    }
    
    @GwtIncompatible("java.util.BitSet")
    private static class BitSetMatcher extends FastMatcher
    {
        private final BitSet table;
        
        private BitSetMatcher(BitSet table, final String description) {
            super(description);
            if (table.length() + 64 < table.size()) {
                table = (BitSet)table.clone();
            }
            this.table = table;
        }
        
        @Override
        public boolean matches(final char c) {
            return this.table.get(c);
        }
        
        @Override
        void setBits(final BitSet bitSet) {
            bitSet.or(this.table);
        }
    }
}
