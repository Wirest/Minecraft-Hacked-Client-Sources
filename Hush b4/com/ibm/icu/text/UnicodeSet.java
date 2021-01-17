// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import com.ibm.icu.lang.UScript;
import java.util.Collections;
import com.ibm.icu.lang.CharSequences;
import java.util.ArrayList;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.impl.UPropertyAliases;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.impl.UBiDiProps;
import com.ibm.icu.impl.UCaseProps;
import com.ibm.icu.impl.Norm2AllModes;
import com.ibm.icu.impl.UCharacterProperty;
import com.ibm.icu.impl.RuleCharacterIterator;
import com.ibm.icu.impl.SortedSetRelation;
import java.util.Collection;
import java.util.Iterator;
import com.ibm.icu.impl.PatternProps;
import com.ibm.icu.impl.Utility;
import java.util.SortedSet;
import java.text.ParsePosition;
import com.ibm.icu.util.VersionInfo;
import com.ibm.icu.impl.UnicodeSetStringSpan;
import com.ibm.icu.impl.BMPSet;
import java.util.TreeSet;
import com.ibm.icu.util.Freezable;

public class UnicodeSet extends UnicodeFilter implements Iterable<String>, Comparable<UnicodeSet>, Freezable<UnicodeSet>
{
    public static final UnicodeSet EMPTY;
    public static final UnicodeSet ALL_CODE_POINTS;
    private static XSymbolTable XSYMBOL_TABLE;
    private static final int LOW = 0;
    private static final int HIGH = 1114112;
    public static final int MIN_VALUE = 0;
    public static final int MAX_VALUE = 1114111;
    private int len;
    private int[] list;
    private int[] rangeList;
    private int[] buffer;
    TreeSet<String> strings;
    private String pat;
    private static final int START_EXTRA = 16;
    private static final int GROW_EXTRA = 16;
    private static final String ANY_ID = "ANY";
    private static final String ASCII_ID = "ASCII";
    private static final String ASSIGNED = "Assigned";
    private static UnicodeSet[] INCLUSIONS;
    private BMPSet bmpSet;
    private UnicodeSetStringSpan stringSpan;
    private static final VersionInfo NO_VERSION;
    public static final int IGNORE_SPACE = 1;
    public static final int CASE = 2;
    public static final int CASE_INSENSITIVE = 2;
    public static final int ADD_CASE_MAPPINGS = 4;
    
    public UnicodeSet() {
        this.strings = new TreeSet<String>();
        this.pat = null;
        (this.list = new int[17])[this.len++] = 1114112;
    }
    
    public UnicodeSet(final UnicodeSet other) {
        this.strings = new TreeSet<String>();
        this.pat = null;
        this.set(other);
    }
    
    public UnicodeSet(final int start, final int end) {
        this();
        this.complement(start, end);
    }
    
    public UnicodeSet(final int... pairs) {
        this.strings = new TreeSet<String>();
        this.pat = null;
        if ((pairs.length & 0x1) != 0x0) {
            throw new IllegalArgumentException("Must have even number of integers");
        }
        this.list = new int[pairs.length + 1];
        this.len = this.list.length;
        int last = -1;
        int i;
        int end;
        for (i = 0; i < pairs.length; last = (this.list[i++] = end)) {
            final int start = pairs[i];
            if (last >= start) {
                throw new IllegalArgumentException("Must be monotonically increasing.");
            }
            last = (this.list[i++] = start);
            end = pairs[i] + 1;
            if (last >= end) {
                throw new IllegalArgumentException("Must be monotonically increasing.");
            }
        }
        this.list[i] = 1114112;
    }
    
    public UnicodeSet(final String pattern) {
        this();
        this.applyPattern(pattern, null, null, 1);
    }
    
    public UnicodeSet(final String pattern, final boolean ignoreWhitespace) {
        this();
        this.applyPattern(pattern, null, null, ignoreWhitespace ? 1 : 0);
    }
    
    public UnicodeSet(final String pattern, final int options) {
        this();
        this.applyPattern(pattern, null, null, options);
    }
    
    public UnicodeSet(final String pattern, final ParsePosition pos, final SymbolTable symbols) {
        this();
        this.applyPattern(pattern, pos, symbols, 1);
    }
    
    public UnicodeSet(final String pattern, final ParsePosition pos, final SymbolTable symbols, final int options) {
        this();
        this.applyPattern(pattern, pos, symbols, options);
    }
    
    public Object clone() {
        final UnicodeSet result = new UnicodeSet(this);
        result.bmpSet = this.bmpSet;
        result.stringSpan = this.stringSpan;
        return result;
    }
    
    public UnicodeSet set(final int start, final int end) {
        this.checkFrozen();
        this.clear();
        this.complement(start, end);
        return this;
    }
    
    public UnicodeSet set(final UnicodeSet other) {
        this.checkFrozen();
        this.list = other.list.clone();
        this.len = other.len;
        this.pat = other.pat;
        this.strings = new TreeSet<String>(other.strings);
        return this;
    }
    
    public final UnicodeSet applyPattern(final String pattern) {
        this.checkFrozen();
        return this.applyPattern(pattern, null, null, 1);
    }
    
    public UnicodeSet applyPattern(final String pattern, final boolean ignoreWhitespace) {
        this.checkFrozen();
        return this.applyPattern(pattern, null, null, ignoreWhitespace ? 1 : 0);
    }
    
    public UnicodeSet applyPattern(final String pattern, final int options) {
        this.checkFrozen();
        return this.applyPattern(pattern, null, null, options);
    }
    
    public static boolean resemblesPattern(final String pattern, final int pos) {
        return (pos + 1 < pattern.length() && pattern.charAt(pos) == '[') || resemblesPropertyPattern(pattern, pos);
    }
    
    private static void _appendToPat(final StringBuffer buf, final String s, final boolean escapeUnprintable) {
        int cp;
        for (int i = 0; i < s.length(); i += Character.charCount(cp)) {
            cp = s.codePointAt(i);
            _appendToPat(buf, cp, escapeUnprintable);
        }
    }
    
    private static void _appendToPat(final StringBuffer buf, final int c, final boolean escapeUnprintable) {
        if (escapeUnprintable && Utility.isUnprintable(c) && Utility.escapeUnprintable(buf, c)) {
            return;
        }
        switch (c) {
            case 36:
            case 38:
            case 45:
            case 58:
            case 91:
            case 92:
            case 93:
            case 94:
            case 123:
            case 125: {
                buf.append('\\');
                break;
            }
            default: {
                if (PatternProps.isWhiteSpace(c)) {
                    buf.append('\\');
                    break;
                }
                break;
            }
        }
        UTF16.append(buf, c);
    }
    
    public String toPattern(final boolean escapeUnprintable) {
        final StringBuffer result = new StringBuffer();
        return this._toPattern(result, escapeUnprintable).toString();
    }
    
    private StringBuffer _toPattern(final StringBuffer result, final boolean escapeUnprintable) {
        if (this.pat != null) {
            int backslashCount = 0;
            int i = 0;
            while (i < this.pat.length()) {
                final int c = UTF16.charAt(this.pat, i);
                i += UTF16.getCharCount(c);
                if (escapeUnprintable && Utility.isUnprintable(c)) {
                    if (backslashCount % 2 != 0) {
                        result.setLength(result.length() - 1);
                    }
                    Utility.escapeUnprintable(result, c);
                    backslashCount = 0;
                }
                else {
                    UTF16.append(result, c);
                    if (c == 92) {
                        ++backslashCount;
                    }
                    else {
                        backslashCount = 0;
                    }
                }
            }
            return result;
        }
        return this._generatePattern(result, escapeUnprintable, true);
    }
    
    public StringBuffer _generatePattern(final StringBuffer result, final boolean escapeUnprintable) {
        return this._generatePattern(result, escapeUnprintable, true);
    }
    
    public StringBuffer _generatePattern(final StringBuffer result, final boolean escapeUnprintable, final boolean includeStrings) {
        result.append('[');
        final int count = this.getRangeCount();
        if (count > 1 && this.getRangeStart(0) == 0 && this.getRangeEnd(count - 1) == 1114111) {
            result.append('^');
            for (int i = 1; i < count; ++i) {
                final int start = this.getRangeEnd(i - 1) + 1;
                final int end = this.getRangeStart(i) - 1;
                _appendToPat(result, start, escapeUnprintable);
                if (start != end) {
                    if (start + 1 != end) {
                        result.append('-');
                    }
                    _appendToPat(result, end, escapeUnprintable);
                }
            }
        }
        else {
            for (int i = 0; i < count; ++i) {
                final int start = this.getRangeStart(i);
                final int end = this.getRangeEnd(i);
                _appendToPat(result, start, escapeUnprintable);
                if (start != end) {
                    if (start + 1 != end) {
                        result.append('-');
                    }
                    _appendToPat(result, end, escapeUnprintable);
                }
            }
        }
        if (includeStrings && this.strings.size() > 0) {
            for (final String s : this.strings) {
                result.append('{');
                _appendToPat(result, s, escapeUnprintable);
                result.append('}');
            }
        }
        return result.append(']');
    }
    
    public int size() {
        int n = 0;
        for (int count = this.getRangeCount(), i = 0; i < count; ++i) {
            n += this.getRangeEnd(i) - this.getRangeStart(i) + 1;
        }
        return n + this.strings.size();
    }
    
    public boolean isEmpty() {
        return this.len == 1 && this.strings.size() == 0;
    }
    
    public boolean matchesIndexValue(final int v) {
        for (int i = 0; i < this.getRangeCount(); ++i) {
            final int low = this.getRangeStart(i);
            final int high = this.getRangeEnd(i);
            if ((low & 0xFFFFFF00) == (high & 0xFFFFFF00)) {
                if ((low & 0xFF) <= v && v <= (high & 0xFF)) {
                    return true;
                }
            }
            else if ((low & 0xFF) <= v || v <= (high & 0xFF)) {
                return true;
            }
        }
        if (this.strings.size() != 0) {
            for (final String s : this.strings) {
                final int c = UTF16.charAt(s, 0);
                if ((c & 0xFF) == v) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public int matches(final Replaceable text, final int[] offset, final int limit, final boolean incremental) {
        if (offset[0] != limit) {
            if (this.strings.size() != 0) {
                final boolean forward = offset[0] < limit;
                final char firstChar = text.charAt(offset[0]);
                int highWaterLength = 0;
                for (final String trial : this.strings) {
                    final char c = trial.charAt(forward ? 0 : (trial.length() - 1));
                    if (forward && c > firstChar) {
                        break;
                    }
                    if (c != firstChar) {
                        continue;
                    }
                    final int length = matchRest(text, offset[0], limit, trial);
                    if (incremental) {
                        final int maxLen = forward ? (limit - offset[0]) : (offset[0] - limit);
                        if (length == maxLen) {
                            return 1;
                        }
                    }
                    if (length != trial.length()) {
                        continue;
                    }
                    if (length > highWaterLength) {
                        highWaterLength = length;
                    }
                    if (forward && length < highWaterLength) {
                        break;
                    }
                }
                if (highWaterLength != 0) {
                    final int n = 0;
                    offset[n] += (forward ? highWaterLength : (-highWaterLength));
                    return 2;
                }
            }
            return super.matches(text, offset, limit, incremental);
        }
        if (this.contains(65535)) {
            return incremental ? 1 : 2;
        }
        return 0;
    }
    
    private static int matchRest(final Replaceable text, final int start, final int limit, final String s) {
        int slen = s.length();
        int maxLen;
        if (start < limit) {
            maxLen = limit - start;
            if (maxLen > slen) {
                maxLen = slen;
            }
            for (int i = 1; i < maxLen; ++i) {
                if (text.charAt(start + i) != s.charAt(i)) {
                    return 0;
                }
            }
        }
        else {
            maxLen = start - limit;
            if (maxLen > slen) {
                maxLen = slen;
            }
            --slen;
            for (int i = 1; i < maxLen; ++i) {
                if (text.charAt(start - i) != s.charAt(slen - i)) {
                    return 0;
                }
            }
        }
        return maxLen;
    }
    
    @Deprecated
    public int matchesAt(final CharSequence text, final int offset) {
        int lastLen = -1;
        Label_0135: {
            if (this.strings.size() != 0) {
                final char firstChar = text.charAt(offset);
                String trial = null;
                final Iterator<String> it = this.strings.iterator();
                while (it.hasNext()) {
                    trial = it.next();
                    final char firstStringChar = trial.charAt(0);
                    if (firstStringChar < firstChar) {
                        continue;
                    }
                    if (firstStringChar > firstChar) {
                        break Label_0135;
                    }
                }
                while (true) {
                    final int tempLen = matchesAt(text, offset, trial);
                    if (lastLen > tempLen) {
                        break;
                    }
                    lastLen = tempLen;
                    if (!it.hasNext()) {
                        break;
                    }
                    trial = it.next();
                }
            }
        }
        if (lastLen < 2) {
            final int cp = UTF16.charAt(text, offset);
            if (this.contains(cp)) {
                lastLen = UTF16.getCharCount(cp);
            }
        }
        return offset + lastLen;
    }
    
    private static int matchesAt(final CharSequence text, final int offsetInText, final CharSequence substring) {
        final int len = substring.length();
        final int textLength = text.length();
        if (textLength + offsetInText > len) {
            return -1;
        }
        int i = 0;
        for (int j = offsetInText; i < len; ++i, ++j) {
            final char pc = substring.charAt(i);
            final char tc = text.charAt(j);
            if (pc != tc) {
                return -1;
            }
        }
        return i;
    }
    
    public void addMatchSetTo(final UnicodeSet toUnionTo) {
        toUnionTo.addAll(this);
    }
    
    public int indexOf(final int c) {
        if (c < 0 || c > 1114111) {
            throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(c, 6));
        }
        int i = 0;
        int n = 0;
        while (true) {
            final int start = this.list[i++];
            if (c < start) {
                return -1;
            }
            final int limit = this.list[i++];
            if (c < limit) {
                return n + c - start;
            }
            n += limit - start;
        }
    }
    
    public int charAt(int index) {
        if (index >= 0) {
            final int len2 = this.len & 0xFFFFFFFE;
            int i = 0;
            while (i < len2) {
                final int start = this.list[i++];
                final int count = this.list[i++] - start;
                if (index < count) {
                    return start + index;
                }
                index -= count;
            }
        }
        return -1;
    }
    
    public UnicodeSet add(final int start, final int end) {
        this.checkFrozen();
        return this.add_unchecked(start, end);
    }
    
    public UnicodeSet addAll(final int start, final int end) {
        this.checkFrozen();
        return this.add_unchecked(start, end);
    }
    
    private UnicodeSet add_unchecked(final int start, final int end) {
        if (start < 0 || start > 1114111) {
            throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(start, 6));
        }
        if (end < 0 || end > 1114111) {
            throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(end, 6));
        }
        if (start < end) {
            this.add(this.range(start, end), 2, 0);
        }
        else if (start == end) {
            this.add(start);
        }
        return this;
    }
    
    public final UnicodeSet add(final int c) {
        this.checkFrozen();
        return this.add_unchecked(c);
    }
    
    private final UnicodeSet add_unchecked(final int c) {
        if (c < 0 || c > 1114111) {
            throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(c, 6));
        }
        final int i = this.findCodePoint(c);
        if ((i & 0x1) != 0x0) {
            return this;
        }
        if (c == this.list[i] - 1) {
            if ((this.list[i] = c) == 1114111) {
                this.ensureCapacity(this.len + 1);
                this.list[this.len++] = 1114112;
            }
            if (i > 0 && c == this.list[i - 1]) {
                System.arraycopy(this.list, i + 1, this.list, i - 1, this.len - i - 1);
                this.len -= 2;
            }
        }
        else if (i > 0 && c == this.list[i - 1]) {
            final int[] list = this.list;
            final int n = i - 1;
            ++list[n];
        }
        else {
            if (this.len + 2 > this.list.length) {
                final int[] temp = new int[this.len + 2 + 16];
                if (i != 0) {
                    System.arraycopy(this.list, 0, temp, 0, i);
                }
                System.arraycopy(this.list, i, temp, i + 2, this.len - i);
                this.list = temp;
            }
            else {
                System.arraycopy(this.list, i, this.list, i + 2, this.len - i);
            }
            this.list[i] = c;
            this.list[i + 1] = c + 1;
            this.len += 2;
        }
        this.pat = null;
        return this;
    }
    
    public final UnicodeSet add(final CharSequence s) {
        this.checkFrozen();
        final int cp = getSingleCP(s);
        if (cp < 0) {
            this.strings.add(s.toString());
            this.pat = null;
        }
        else {
            this.add_unchecked(cp, cp);
        }
        return this;
    }
    
    private static int getSingleCP(final CharSequence s) {
        if (s.length() < 1) {
            throw new IllegalArgumentException("Can't use zero-length strings in UnicodeSet");
        }
        if (s.length() > 2) {
            return -1;
        }
        if (s.length() == 1) {
            return s.charAt(0);
        }
        final int cp = UTF16.charAt(s, 0);
        if (cp > 65535) {
            return cp;
        }
        return -1;
    }
    
    public final UnicodeSet addAll(final CharSequence s) {
        this.checkFrozen();
        int cp;
        for (int i = 0; i < s.length(); i += UTF16.getCharCount(cp)) {
            cp = UTF16.charAt(s, i);
            this.add_unchecked(cp, cp);
        }
        return this;
    }
    
    public final UnicodeSet retainAll(final String s) {
        return this.retainAll(fromAll(s));
    }
    
    public final UnicodeSet complementAll(final String s) {
        return this.complementAll(fromAll(s));
    }
    
    public final UnicodeSet removeAll(final String s) {
        return this.removeAll(fromAll(s));
    }
    
    public final UnicodeSet removeAllStrings() {
        this.checkFrozen();
        if (this.strings.size() != 0) {
            this.strings.clear();
            this.pat = null;
        }
        return this;
    }
    
    public static UnicodeSet from(final String s) {
        return new UnicodeSet().add(s);
    }
    
    public static UnicodeSet fromAll(final String s) {
        return new UnicodeSet().addAll(s);
    }
    
    public UnicodeSet retain(final int start, final int end) {
        this.checkFrozen();
        if (start < 0 || start > 1114111) {
            throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(start, 6));
        }
        if (end < 0 || end > 1114111) {
            throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(end, 6));
        }
        if (start <= end) {
            this.retain(this.range(start, end), 2, 0);
        }
        else {
            this.clear();
        }
        return this;
    }
    
    public final UnicodeSet retain(final int c) {
        return this.retain(c, c);
    }
    
    public final UnicodeSet retain(final String s) {
        final int cp = getSingleCP(s);
        if (cp < 0) {
            final boolean isIn = this.strings.contains(s);
            if (isIn && this.size() == 1) {
                return this;
            }
            this.clear();
            this.strings.add(s);
            this.pat = null;
        }
        else {
            this.retain(cp, cp);
        }
        return this;
    }
    
    public UnicodeSet remove(final int start, final int end) {
        this.checkFrozen();
        if (start < 0 || start > 1114111) {
            throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(start, 6));
        }
        if (end < 0 || end > 1114111) {
            throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(end, 6));
        }
        if (start <= end) {
            this.retain(this.range(start, end), 2, 2);
        }
        return this;
    }
    
    public final UnicodeSet remove(final int c) {
        return this.remove(c, c);
    }
    
    public final UnicodeSet remove(final String s) {
        final int cp = getSingleCP(s);
        if (cp < 0) {
            this.strings.remove(s);
            this.pat = null;
        }
        else {
            this.remove(cp, cp);
        }
        return this;
    }
    
    public UnicodeSet complement(final int start, final int end) {
        this.checkFrozen();
        if (start < 0 || start > 1114111) {
            throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(start, 6));
        }
        if (end < 0 || end > 1114111) {
            throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(end, 6));
        }
        if (start <= end) {
            this.xor(this.range(start, end), 2, 0);
        }
        this.pat = null;
        return this;
    }
    
    public final UnicodeSet complement(final int c) {
        return this.complement(c, c);
    }
    
    public UnicodeSet complement() {
        this.checkFrozen();
        if (this.list[0] == 0) {
            System.arraycopy(this.list, 1, this.list, 0, this.len - 1);
            --this.len;
        }
        else {
            this.ensureCapacity(this.len + 1);
            System.arraycopy(this.list, 0, this.list, 1, this.len);
            this.list[0] = 0;
            ++this.len;
        }
        this.pat = null;
        return this;
    }
    
    public final UnicodeSet complement(final String s) {
        this.checkFrozen();
        final int cp = getSingleCP(s);
        if (cp < 0) {
            if (this.strings.contains(s)) {
                this.strings.remove(s);
            }
            else {
                this.strings.add(s);
            }
            this.pat = null;
        }
        else {
            this.complement(cp, cp);
        }
        return this;
    }
    
    @Override
    public boolean contains(final int c) {
        if (c < 0 || c > 1114111) {
            throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(c, 6));
        }
        if (this.bmpSet != null) {
            return this.bmpSet.contains(c);
        }
        if (this.stringSpan != null) {
            return this.stringSpan.contains(c);
        }
        final int i = this.findCodePoint(c);
        return (i & 0x1) != 0x0;
    }
    
    private final int findCodePoint(final int c) {
        if (c < this.list[0]) {
            return 0;
        }
        if (this.len >= 2 && c >= this.list[this.len - 2]) {
            return this.len - 1;
        }
        int lo = 0;
        int hi = this.len - 1;
        while (true) {
            final int i = lo + hi >>> 1;
            if (i == lo) {
                break;
            }
            if (c < this.list[i]) {
                hi = i;
            }
            else {
                lo = i;
            }
        }
        return hi;
    }
    
    public boolean contains(final int start, final int end) {
        if (start < 0 || start > 1114111) {
            throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(start, 6));
        }
        if (end < 0 || end > 1114111) {
            throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(end, 6));
        }
        final int i = this.findCodePoint(start);
        return (i & 0x1) != 0x0 && end < this.list[i];
    }
    
    public final boolean contains(final String s) {
        final int cp = getSingleCP(s);
        if (cp < 0) {
            return this.strings.contains(s);
        }
        return this.contains(cp);
    }
    
    public boolean containsAll(final UnicodeSet b) {
        final int[] listB = b.list;
        boolean needA = true;
        boolean needB = true;
        int aPtr = 0;
        int bPtr = 0;
        final int aLen = this.len - 1;
        final int bLen = b.len - 1;
        int startA = 0;
        int startB = 0;
        int limitA = 0;
        int limitB = 0;
        while (true) {
            if (needA) {
                if (aPtr >= aLen) {
                    if (needB && bPtr >= bLen) {
                        break;
                    }
                    return false;
                }
                else {
                    startA = this.list[aPtr++];
                    limitA = this.list[aPtr++];
                }
            }
            if (needB) {
                if (bPtr >= bLen) {
                    break;
                }
                startB = listB[bPtr++];
                limitB = listB[bPtr++];
            }
            if (startB >= limitA) {
                needA = true;
                needB = false;
            }
            else {
                if (startB < startA || limitB > limitA) {
                    return false;
                }
                needA = false;
                needB = true;
            }
        }
        return this.strings.containsAll(b.strings);
    }
    
    public boolean containsAll(final String s) {
        int cp;
        for (int i = 0; i < s.length(); i += UTF16.getCharCount(cp)) {
            cp = UTF16.charAt(s, i);
            if (!this.contains(cp)) {
                return this.strings.size() != 0 && this.containsAll(s, 0);
            }
        }
        return true;
    }
    
    private boolean containsAll(final String s, final int i) {
        if (i >= s.length()) {
            return true;
        }
        final int cp = UTF16.charAt(s, i);
        if (this.contains(cp) && this.containsAll(s, i + UTF16.getCharCount(cp))) {
            return true;
        }
        for (final String setStr : this.strings) {
            if (s.startsWith(setStr, i) && this.containsAll(s, i + setStr.length())) {
                return true;
            }
        }
        return false;
    }
    
    @Deprecated
    public String getRegexEquivalent() {
        if (this.strings.size() == 0) {
            return this.toString();
        }
        final StringBuffer result = new StringBuffer("(?:");
        this._generatePattern(result, true, false);
        for (final String s : this.strings) {
            result.append('|');
            _appendToPat(result, s, true);
        }
        return result.append(")").toString();
    }
    
    public boolean containsNone(final int start, final int end) {
        if (start < 0 || start > 1114111) {
            throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(start, 6));
        }
        if (end < 0 || end > 1114111) {
            throw new IllegalArgumentException("Invalid code point U+" + Utility.hex(end, 6));
        }
        int i = -1;
        while (start >= this.list[++i]) {}
        return (i & 0x1) == 0x0 && end < this.list[i];
    }
    
    public boolean containsNone(final UnicodeSet b) {
        final int[] listB = b.list;
        boolean needA = true;
        boolean needB = true;
        int aPtr = 0;
        int bPtr = 0;
        final int aLen = this.len - 1;
        final int bLen = b.len - 1;
        int startA = 0;
        int startB = 0;
        int limitA = 0;
        int limitB = 0;
        while (true) {
            if (needA) {
                if (aPtr >= aLen) {
                    break;
                }
                startA = this.list[aPtr++];
                limitA = this.list[aPtr++];
            }
            if (needB) {
                if (bPtr >= bLen) {
                    break;
                }
                startB = listB[bPtr++];
                limitB = listB[bPtr++];
            }
            if (startB >= limitA) {
                needA = true;
                needB = false;
            }
            else {
                if (startA < limitB) {
                    return false;
                }
                needA = false;
                needB = true;
            }
        }
        return SortedSetRelation.hasRelation(this.strings, 5, b.strings);
    }
    
    public boolean containsNone(final String s) {
        return this.span(s, SpanCondition.NOT_CONTAINED) == s.length();
    }
    
    public final boolean containsSome(final int start, final int end) {
        return !this.containsNone(start, end);
    }
    
    public final boolean containsSome(final UnicodeSet s) {
        return !this.containsNone(s);
    }
    
    public final boolean containsSome(final String s) {
        return !this.containsNone(s);
    }
    
    public UnicodeSet addAll(final UnicodeSet c) {
        this.checkFrozen();
        this.add(c.list, c.len, 0);
        this.strings.addAll(c.strings);
        return this;
    }
    
    public UnicodeSet retainAll(final UnicodeSet c) {
        this.checkFrozen();
        this.retain(c.list, c.len, 0);
        this.strings.retainAll(c.strings);
        return this;
    }
    
    public UnicodeSet removeAll(final UnicodeSet c) {
        this.checkFrozen();
        this.retain(c.list, c.len, 2);
        this.strings.removeAll(c.strings);
        return this;
    }
    
    public UnicodeSet complementAll(final UnicodeSet c) {
        this.checkFrozen();
        this.xor(c.list, c.len, 0);
        SortedSetRelation.doOperation(this.strings, 5, c.strings);
        return this;
    }
    
    public UnicodeSet clear() {
        this.checkFrozen();
        this.list[0] = 1114112;
        this.len = 1;
        this.pat = null;
        this.strings.clear();
        return this;
    }
    
    public int getRangeCount() {
        return this.len / 2;
    }
    
    public int getRangeStart(final int index) {
        return this.list[index * 2];
    }
    
    public int getRangeEnd(final int index) {
        return this.list[index * 2 + 1] - 1;
    }
    
    public UnicodeSet compact() {
        this.checkFrozen();
        if (this.len != this.list.length) {
            final int[] temp = new int[this.len];
            System.arraycopy(this.list, 0, temp, 0, this.len);
            this.list = temp;
        }
        this.rangeList = null;
        this.buffer = null;
        return this;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        try {
            final UnicodeSet that = (UnicodeSet)o;
            if (this.len != that.len) {
                return false;
            }
            for (int i = 0; i < this.len; ++i) {
                if (this.list[i] != that.list[i]) {
                    return false;
                }
            }
            if (!this.strings.equals(that.strings)) {
                return false;
            }
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        int result = this.len;
        for (int i = 0; i < this.len; ++i) {
            result *= 1000003;
            result += this.list[i];
        }
        return result;
    }
    
    @Override
    public String toString() {
        return this.toPattern(true);
    }
    
    @Deprecated
    public UnicodeSet applyPattern(final String pattern, ParsePosition pos, final SymbolTable symbols, final int options) {
        final boolean parsePositionWasNull = pos == null;
        if (parsePositionWasNull) {
            pos = new ParsePosition(0);
        }
        final StringBuffer rebuiltPat = new StringBuffer();
        final RuleCharacterIterator chars = new RuleCharacterIterator(pattern, symbols, pos);
        this.applyPattern(chars, symbols, rebuiltPat, options);
        if (chars.inVariable()) {
            syntaxError(chars, "Extra chars in variable value");
        }
        this.pat = rebuiltPat.toString();
        if (parsePositionWasNull) {
            int i = pos.getIndex();
            if ((options & 0x1) != 0x0) {
                i = PatternProps.skipWhiteSpace(pattern, i);
            }
            if (i != pattern.length()) {
                throw new IllegalArgumentException("Parse of \"" + pattern + "\" failed at " + i);
            }
        }
        return this;
    }
    
    void applyPattern(final RuleCharacterIterator chars, final SymbolTable symbols, final StringBuffer rebuiltPat, final int options) {
        int opts = 3;
        if ((options & 0x1) != 0x0) {
            opts |= 0x4;
        }
        final StringBuffer patBuf = new StringBuffer();
        StringBuffer buf = null;
        boolean usePat = false;
        UnicodeSet scratch = null;
        Object backup = null;
        int lastItem = 0;
        int lastChar = 0;
        int mode = 0;
        char op = '\0';
        boolean invert = false;
        this.clear();
        while (mode != 2 && !chars.atEnd()) {
            int c = 0;
            boolean literal = false;
            UnicodeSet nested = null;
            int setMode = 0;
            if (resemblesPropertyPattern(chars, opts)) {
                setMode = 2;
            }
            else {
                backup = chars.getPos(backup);
                c = chars.next(opts);
                literal = chars.isEscaped();
                if (c == 91 && !literal) {
                    if (mode == 1) {
                        chars.setPos(backup);
                        setMode = 1;
                    }
                    else {
                        mode = 1;
                        patBuf.append('[');
                        backup = chars.getPos(backup);
                        c = chars.next(opts);
                        literal = chars.isEscaped();
                        if (c == 94 && !literal) {
                            invert = true;
                            patBuf.append('^');
                            backup = chars.getPos(backup);
                            c = chars.next(opts);
                            literal = chars.isEscaped();
                        }
                        if (c != 45) {
                            chars.setPos(backup);
                            continue;
                        }
                        literal = true;
                    }
                }
                else if (symbols != null) {
                    final UnicodeMatcher m = symbols.lookupMatcher(c);
                    if (m != null) {
                        try {
                            nested = (UnicodeSet)m;
                            setMode = 3;
                        }
                        catch (ClassCastException e) {
                            syntaxError(chars, "Syntax error");
                        }
                    }
                }
            }
            if (setMode != 0) {
                if (lastItem == 1) {
                    if (op != '\0') {
                        syntaxError(chars, "Char expected after operator");
                    }
                    this.add_unchecked(lastChar, lastChar);
                    _appendToPat(patBuf, lastChar, false);
                    op = (char)(lastItem = 0);
                }
                if (op == '-' || op == '&') {
                    patBuf.append(op);
                }
                if (nested == null) {
                    if (scratch == null) {
                        scratch = new UnicodeSet();
                    }
                    nested = scratch;
                }
                switch (setMode) {
                    case 1: {
                        nested.applyPattern(chars, symbols, patBuf, options);
                        break;
                    }
                    case 2: {
                        chars.skipIgnored(opts);
                        nested.applyPropertyPattern(chars, patBuf, symbols);
                        break;
                    }
                    case 3: {
                        nested._toPattern(patBuf, false);
                        break;
                    }
                }
                usePat = true;
                if (mode == 0) {
                    this.set(nested);
                    mode = 2;
                    break;
                }
                switch (op) {
                    case '-': {
                        this.removeAll(nested);
                        break;
                    }
                    case '&': {
                        this.retainAll(nested);
                        break;
                    }
                    case '\0': {
                        this.addAll(nested);
                        break;
                    }
                }
                op = '\0';
                lastItem = 2;
            }
            else {
                if (mode == 0) {
                    syntaxError(chars, "Missing '['");
                }
                if (!literal) {
                    switch (c) {
                        case 93: {
                            if (lastItem == 1) {
                                this.add_unchecked(lastChar, lastChar);
                                _appendToPat(patBuf, lastChar, false);
                            }
                            if (op == '-') {
                                this.add_unchecked(op, op);
                                patBuf.append(op);
                            }
                            else if (op == '&') {
                                syntaxError(chars, "Trailing '&'");
                            }
                            patBuf.append(']');
                            mode = 2;
                            continue;
                        }
                        case 45: {
                            if (op == '\0') {
                                if (lastItem != 0) {
                                    op = (char)c;
                                    continue;
                                }
                                this.add_unchecked(c, c);
                                c = chars.next(opts);
                                literal = chars.isEscaped();
                                if (c == 93 && !literal) {
                                    patBuf.append("-]");
                                    mode = 2;
                                    continue;
                                }
                            }
                            syntaxError(chars, "'-' not after char or set");
                            break;
                        }
                        case 38: {
                            if (lastItem == 2 && op == '\0') {
                                op = (char)c;
                                continue;
                            }
                            syntaxError(chars, "'&' not after set");
                            break;
                        }
                        case 94: {
                            syntaxError(chars, "'^' not after '['");
                            break;
                        }
                        case 123: {
                            if (op != '\0') {
                                syntaxError(chars, "Missing operand after operator");
                            }
                            if (lastItem == 1) {
                                this.add_unchecked(lastChar, lastChar);
                                _appendToPat(patBuf, lastChar, false);
                            }
                            lastItem = 0;
                            if (buf == null) {
                                buf = new StringBuffer();
                            }
                            else {
                                buf.setLength(0);
                            }
                            boolean ok = false;
                            while (!chars.atEnd()) {
                                c = chars.next(opts);
                                literal = chars.isEscaped();
                                if (c == 125 && !literal) {
                                    ok = true;
                                    break;
                                }
                                UTF16.append(buf, c);
                            }
                            if (buf.length() < 1 || !ok) {
                                syntaxError(chars, "Invalid multicharacter string");
                            }
                            this.add(buf.toString());
                            patBuf.append('{');
                            _appendToPat(patBuf, buf.toString(), false);
                            patBuf.append('}');
                            continue;
                        }
                        case 36: {
                            backup = chars.getPos(backup);
                            c = chars.next(opts);
                            literal = chars.isEscaped();
                            final boolean anchor = c == 93 && !literal;
                            if (symbols == null && !anchor) {
                                c = 36;
                                chars.setPos(backup);
                                break;
                            }
                            if (anchor && op == '\0') {
                                if (lastItem == 1) {
                                    this.add_unchecked(lastChar, lastChar);
                                    _appendToPat(patBuf, lastChar, false);
                                }
                                this.add_unchecked(65535);
                                usePat = true;
                                patBuf.append('$').append(']');
                                mode = 2;
                                continue;
                            }
                            syntaxError(chars, "Unquoted '$'");
                            break;
                        }
                    }
                }
                switch (lastItem) {
                    case 0: {
                        lastItem = 1;
                        lastChar = c;
                        continue;
                    }
                    case 1: {
                        if (op == '-') {
                            if (lastChar >= c) {
                                syntaxError(chars, "Invalid range");
                            }
                            this.add_unchecked(lastChar, c);
                            _appendToPat(patBuf, lastChar, false);
                            patBuf.append(op);
                            _appendToPat(patBuf, c, false);
                            op = (char)(lastItem = 0);
                            continue;
                        }
                        this.add_unchecked(lastChar, lastChar);
                        _appendToPat(patBuf, lastChar, false);
                        lastChar = c;
                        continue;
                    }
                    case 2: {
                        if (op != '\0') {
                            syntaxError(chars, "Set expected after operator");
                        }
                        lastChar = c;
                        lastItem = 1;
                        continue;
                    }
                }
            }
        }
        if (mode != 2) {
            syntaxError(chars, "Missing ']'");
        }
        chars.skipIgnored(opts);
        if ((options & 0x2) != 0x0) {
            this.closeOver(2);
        }
        if (invert) {
            this.complement();
        }
        if (usePat) {
            rebuiltPat.append(patBuf.toString());
        }
        else {
            this._generatePattern(rebuiltPat, false, true);
        }
    }
    
    private static void syntaxError(final RuleCharacterIterator chars, final String msg) {
        throw new IllegalArgumentException("Error: " + msg + " at \"" + Utility.escape(chars.toString()) + '\"');
    }
    
    public <T extends Collection<String>> T addAllTo(final T target) {
        return addAllTo((Iterable<Object>)this, target);
    }
    
    public String[] addAllTo(final String[] target) {
        return addAllTo(this, target);
    }
    
    public static String[] toArray(final UnicodeSet set) {
        return addAllTo(set, new String[set.size()]);
    }
    
    public UnicodeSet add(final Collection<?> source) {
        return this.addAll(source);
    }
    
    public UnicodeSet addAll(final Collection<?> source) {
        this.checkFrozen();
        for (final Object o : source) {
            this.add(o.toString());
        }
        return this;
    }
    
    private void ensureCapacity(final int newLen) {
        if (newLen <= this.list.length) {
            return;
        }
        final int[] temp = new int[newLen + 16];
        System.arraycopy(this.list, 0, temp, 0, this.len);
        this.list = temp;
    }
    
    private void ensureBufferCapacity(final int newLen) {
        if (this.buffer != null && newLen <= this.buffer.length) {
            return;
        }
        this.buffer = new int[newLen + 16];
    }
    
    private int[] range(final int start, final int end) {
        if (this.rangeList == null) {
            this.rangeList = new int[] { start, end + 1, 1114112 };
        }
        else {
            this.rangeList[0] = start;
            this.rangeList[1] = end + 1;
        }
        return this.rangeList;
    }
    
    private UnicodeSet xor(final int[] other, final int otherLen, final int polarity) {
        this.ensureBufferCapacity(this.len + otherLen);
        int i = 0;
        int j = 0;
        int k = 0;
        int a = this.list[i++];
        int b;
        if (polarity == 1 || polarity == 2) {
            b = 0;
            if (other[j] == 0) {
                ++j;
                b = other[j];
            }
        }
        else {
            b = other[j++];
        }
        while (true) {
            if (a < b) {
                this.buffer[k++] = a;
                a = this.list[i++];
            }
            else if (b < a) {
                this.buffer[k++] = b;
                b = other[j++];
            }
            else {
                if (a == 1114112) {
                    break;
                }
                a = this.list[i++];
                b = other[j++];
            }
        }
        this.buffer[k++] = 1114112;
        this.len = k;
        final int[] temp = this.list;
        this.list = this.buffer;
        this.buffer = temp;
        this.pat = null;
        return this;
    }
    
    private UnicodeSet add(final int[] other, final int otherLen, int polarity) {
        this.ensureBufferCapacity(this.len + otherLen);
        int i = 0;
        int j = 0;
        int k = 0;
        int a = this.list[i++];
        int b = other[j++];
    Label_0620:
        while (true) {
            switch (polarity) {
                case 0: {
                    if (a < b) {
                        if (k > 0 && a <= this.buffer[k - 1]) {
                            a = max(this.list[i], this.buffer[--k]);
                        }
                        else {
                            this.buffer[k++] = a;
                            a = this.list[i];
                        }
                        ++i;
                        polarity ^= 0x1;
                        continue;
                    }
                    if (b < a) {
                        if (k > 0 && b <= this.buffer[k - 1]) {
                            b = max(other[j], this.buffer[--k]);
                        }
                        else {
                            this.buffer[k++] = b;
                            b = other[j];
                        }
                        ++j;
                        polarity ^= 0x2;
                        continue;
                    }
                    if (a == 1114112) {
                        break Label_0620;
                    }
                    if (k > 0 && a <= this.buffer[k - 1]) {
                        a = max(this.list[i], this.buffer[--k]);
                    }
                    else {
                        this.buffer[k++] = a;
                        a = this.list[i];
                    }
                    ++i;
                    polarity ^= 0x1;
                    b = other[j++];
                    polarity ^= 0x2;
                    continue;
                }
                case 3: {
                    if (b <= a) {
                        if (a == 1114112) {
                            break Label_0620;
                        }
                        this.buffer[k++] = a;
                    }
                    else {
                        if (b == 1114112) {
                            break Label_0620;
                        }
                        this.buffer[k++] = b;
                    }
                    a = this.list[i++];
                    polarity ^= 0x1;
                    b = other[j++];
                    polarity ^= 0x2;
                    continue;
                }
                case 1: {
                    if (a < b) {
                        this.buffer[k++] = a;
                        a = this.list[i++];
                        polarity ^= 0x1;
                        continue;
                    }
                    if (b < a) {
                        b = other[j++];
                        polarity ^= 0x2;
                        continue;
                    }
                    if (a == 1114112) {
                        break Label_0620;
                    }
                    a = this.list[i++];
                    polarity ^= 0x1;
                    b = other[j++];
                    polarity ^= 0x2;
                    continue;
                }
                case 2: {
                    if (b < a) {
                        this.buffer[k++] = b;
                        b = other[j++];
                        polarity ^= 0x2;
                        continue;
                    }
                    if (a < b) {
                        a = this.list[i++];
                        polarity ^= 0x1;
                        continue;
                    }
                    if (a == 1114112) {
                        break Label_0620;
                    }
                    a = this.list[i++];
                    polarity ^= 0x1;
                    b = other[j++];
                    polarity ^= 0x2;
                    continue;
                }
            }
        }
        this.buffer[k++] = 1114112;
        this.len = k;
        final int[] temp = this.list;
        this.list = this.buffer;
        this.buffer = temp;
        this.pat = null;
        return this;
    }
    
    private UnicodeSet retain(final int[] other, final int otherLen, int polarity) {
        this.ensureBufferCapacity(this.len + otherLen);
        int i = 0;
        int j = 0;
        int k = 0;
        int a = this.list[i++];
        int b = other[j++];
    Label_0508:
        while (true) {
            switch (polarity) {
                case 0: {
                    if (a < b) {
                        a = this.list[i++];
                        polarity ^= 0x1;
                        continue;
                    }
                    if (b < a) {
                        b = other[j++];
                        polarity ^= 0x2;
                        continue;
                    }
                    if (a == 1114112) {
                        break Label_0508;
                    }
                    this.buffer[k++] = a;
                    a = this.list[i++];
                    polarity ^= 0x1;
                    b = other[j++];
                    polarity ^= 0x2;
                    continue;
                }
                case 3: {
                    if (a < b) {
                        this.buffer[k++] = a;
                        a = this.list[i++];
                        polarity ^= 0x1;
                        continue;
                    }
                    if (b < a) {
                        this.buffer[k++] = b;
                        b = other[j++];
                        polarity ^= 0x2;
                        continue;
                    }
                    if (a == 1114112) {
                        break Label_0508;
                    }
                    this.buffer[k++] = a;
                    a = this.list[i++];
                    polarity ^= 0x1;
                    b = other[j++];
                    polarity ^= 0x2;
                    continue;
                }
                case 1: {
                    if (a < b) {
                        a = this.list[i++];
                        polarity ^= 0x1;
                        continue;
                    }
                    if (b < a) {
                        this.buffer[k++] = b;
                        b = other[j++];
                        polarity ^= 0x2;
                        continue;
                    }
                    if (a == 1114112) {
                        break Label_0508;
                    }
                    a = this.list[i++];
                    polarity ^= 0x1;
                    b = other[j++];
                    polarity ^= 0x2;
                    continue;
                }
                case 2: {
                    if (b < a) {
                        b = other[j++];
                        polarity ^= 0x2;
                        continue;
                    }
                    if (a < b) {
                        this.buffer[k++] = a;
                        a = this.list[i++];
                        polarity ^= 0x1;
                        continue;
                    }
                    if (a == 1114112) {
                        break Label_0508;
                    }
                    a = this.list[i++];
                    polarity ^= 0x1;
                    b = other[j++];
                    polarity ^= 0x2;
                    continue;
                }
            }
        }
        this.buffer[k++] = 1114112;
        this.len = k;
        final int[] temp = this.list;
        this.list = this.buffer;
        this.buffer = temp;
        this.pat = null;
        return this;
    }
    
    private static final int max(final int a, final int b) {
        return (a > b) ? a : b;
    }
    
    private static synchronized UnicodeSet getInclusions(final int src) {
        if (UnicodeSet.INCLUSIONS == null) {
            UnicodeSet.INCLUSIONS = new UnicodeSet[12];
        }
        if (UnicodeSet.INCLUSIONS[src] == null) {
            final UnicodeSet incl = new UnicodeSet();
            switch (src) {
                case 1: {
                    UCharacterProperty.INSTANCE.addPropertyStarts(incl);
                    break;
                }
                case 2: {
                    UCharacterProperty.INSTANCE.upropsvec_addPropertyStarts(incl);
                    break;
                }
                case 6: {
                    UCharacterProperty.INSTANCE.addPropertyStarts(incl);
                    UCharacterProperty.INSTANCE.upropsvec_addPropertyStarts(incl);
                    break;
                }
                case 7: {
                    Norm2AllModes.getNFCInstance().impl.addPropertyStarts(incl);
                    UCaseProps.INSTANCE.addPropertyStarts(incl);
                    break;
                }
                case 8: {
                    Norm2AllModes.getNFCInstance().impl.addPropertyStarts(incl);
                    break;
                }
                case 9: {
                    Norm2AllModes.getNFKCInstance().impl.addPropertyStarts(incl);
                    break;
                }
                case 10: {
                    Norm2AllModes.getNFKC_CFInstance().impl.addPropertyStarts(incl);
                    break;
                }
                case 11: {
                    Norm2AllModes.getNFCInstance().impl.addCanonIterPropertyStarts(incl);
                    break;
                }
                case 4: {
                    UCaseProps.INSTANCE.addPropertyStarts(incl);
                    break;
                }
                case 5: {
                    UBiDiProps.INSTANCE.addPropertyStarts(incl);
                    break;
                }
                default: {
                    throw new IllegalStateException("UnicodeSet.getInclusions(unknown src " + src + ")");
                }
            }
            UnicodeSet.INCLUSIONS[src] = incl;
        }
        return UnicodeSet.INCLUSIONS[src];
    }
    
    private UnicodeSet applyFilter(final Filter filter, final int src) {
        this.clear();
        int startHasProperty = -1;
        final UnicodeSet inclusions = getInclusions(src);
        for (int limitRange = inclusions.getRangeCount(), j = 0; j < limitRange; ++j) {
            final int start = inclusions.getRangeStart(j);
            for (int end = inclusions.getRangeEnd(j), ch = start; ch <= end; ++ch) {
                if (filter.contains(ch)) {
                    if (startHasProperty < 0) {
                        startHasProperty = ch;
                    }
                }
                else if (startHasProperty >= 0) {
                    this.add_unchecked(startHasProperty, ch - 1);
                    startHasProperty = -1;
                }
            }
        }
        if (startHasProperty >= 0) {
            this.add_unchecked(startHasProperty, 1114111);
        }
        return this;
    }
    
    private static String mungeCharName(String source) {
        source = PatternProps.trimWhiteSpace(source);
        StringBuilder buf = null;
        for (int i = 0; i < source.length(); ++i) {
            char ch = source.charAt(i);
            if (PatternProps.isWhiteSpace(ch)) {
                if (buf == null) {
                    buf = new StringBuilder().append(source, 0, i);
                }
                else if (buf.charAt(buf.length() - 1) == ' ') {
                    continue;
                }
                ch = ' ';
            }
            if (buf != null) {
                buf.append(ch);
            }
        }
        return (buf == null) ? source : buf.toString();
    }
    
    public UnicodeSet applyIntPropertyValue(final int prop, final int value) {
        this.checkFrozen();
        if (prop == 8192) {
            this.applyFilter(new GeneralCategoryMaskFilter(value), 1);
        }
        else if (prop == 28672) {
            this.applyFilter(new ScriptExtensionsFilter(value), 2);
        }
        else {
            this.applyFilter(new IntPropertyFilter(prop, value), UCharacterProperty.INSTANCE.getSource(prop));
        }
        return this;
    }
    
    public UnicodeSet applyPropertyAlias(final String propertyAlias, final String valueAlias) {
        return this.applyPropertyAlias(propertyAlias, valueAlias, null);
    }
    
    public UnicodeSet applyPropertyAlias(final String propertyAlias, final String valueAlias, final SymbolTable symbols) {
        this.checkFrozen();
        final boolean mustNotBeEmpty = false;
        boolean invert = false;
        if (symbols != null && symbols instanceof XSymbolTable && ((XSymbolTable)symbols).applyPropertyAlias(propertyAlias, valueAlias, this)) {
            return this;
        }
        if (UnicodeSet.XSYMBOL_TABLE != null && UnicodeSet.XSYMBOL_TABLE.applyPropertyAlias(propertyAlias, valueAlias, this)) {
            return this;
        }
        int p = 0;
        int v = 0;
        Label_0573: {
            if (valueAlias.length() > 0) {
                p = UCharacter.getPropertyEnum(propertyAlias);
                if (p == 4101) {
                    p = 8192;
                }
                if ((p < 0 || p >= 57) && (p < 4096 || p >= 4117)) {
                    if (p < 8192 || p >= 8193) {
                        switch (p) {
                            case 12288: {
                                final double value = Double.parseDouble(PatternProps.trimWhiteSpace(valueAlias));
                                this.applyFilter(new NumericValueFilter(value), 1);
                                return this;
                            }
                            case 16389: {
                                final String buf = mungeCharName(valueAlias);
                                final int ch = UCharacter.getCharFromExtendedName(buf);
                                if (ch == -1) {
                                    throw new IllegalArgumentException("Invalid character name");
                                }
                                this.clear();
                                this.add_unchecked(ch);
                                return this;
                            }
                            case 16395: {
                                throw new IllegalArgumentException("Unicode_1_Name (na1) not supported");
                            }
                            case 16384: {
                                final VersionInfo version = VersionInfo.getInstance(mungeCharName(valueAlias));
                                this.applyFilter(new VersionFilter(version), 2);
                                return this;
                            }
                            case 28672: {
                                v = UCharacter.getPropertyValueEnum(4106, valueAlias);
                                break Label_0573;
                            }
                            default: {
                                throw new IllegalArgumentException("Unsupported property");
                            }
                        }
                    }
                }
                try {
                    v = UCharacter.getPropertyValueEnum(p, valueAlias);
                }
                catch (IllegalArgumentException e) {
                    if (p != 4098 && p != 4112 && p != 4113) {
                        throw e;
                    }
                    v = Integer.parseInt(PatternProps.trimWhiteSpace(valueAlias));
                    if (v < 0 || v > 255) {
                        throw e;
                    }
                }
            }
            else {
                final UPropertyAliases pnames = UPropertyAliases.INSTANCE;
                p = 8192;
                v = pnames.getPropertyValueEnum(p, propertyAlias);
                if (v == -1) {
                    p = 4106;
                    v = pnames.getPropertyValueEnum(p, propertyAlias);
                    if (v == -1) {
                        p = pnames.getPropertyEnum(propertyAlias);
                        if (p == -1) {
                            p = -1;
                        }
                        if (p >= 0 && p < 57) {
                            v = 1;
                        }
                        else {
                            if (p != -1) {
                                throw new IllegalArgumentException("Missing property value");
                            }
                            if (0 == UPropertyAliases.compare("ANY", propertyAlias)) {
                                this.set(0, 1114111);
                                return this;
                            }
                            if (0 == UPropertyAliases.compare("ASCII", propertyAlias)) {
                                this.set(0, 127);
                                return this;
                            }
                            if (0 != UPropertyAliases.compare("Assigned", propertyAlias)) {
                                throw new IllegalArgumentException("Invalid property alias: " + propertyAlias + "=" + valueAlias);
                            }
                            p = 8192;
                            v = 1;
                            invert = true;
                        }
                    }
                }
            }
        }
        this.applyIntPropertyValue(p, v);
        if (invert) {
            this.complement();
        }
        if (mustNotBeEmpty && this.isEmpty()) {
            throw new IllegalArgumentException("Invalid property value");
        }
        return this;
    }
    
    private static boolean resemblesPropertyPattern(final String pattern, final int pos) {
        return pos + 5 <= pattern.length() && (pattern.regionMatches(pos, "[:", 0, 2) || pattern.regionMatches(true, pos, "\\p", 0, 2) || pattern.regionMatches(pos, "\\N", 0, 2));
    }
    
    private static boolean resemblesPropertyPattern(final RuleCharacterIterator chars, int iterOpts) {
        boolean result = false;
        iterOpts &= 0xFFFFFFFD;
        final Object pos = chars.getPos(null);
        final int c = chars.next(iterOpts);
        if (c == 91 || c == 92) {
            final int d = chars.next(iterOpts & 0xFFFFFFFB);
            result = ((c == 91) ? (d == 58) : (d == 78 || d == 112 || d == 80));
        }
        chars.setPos(pos);
        return result;
    }
    
    private UnicodeSet applyPropertyPattern(final String pattern, final ParsePosition ppos, final SymbolTable symbols) {
        int pos = ppos.getIndex();
        if (pos + 5 > pattern.length()) {
            return null;
        }
        boolean posix = false;
        boolean isName = false;
        boolean invert = false;
        if (pattern.regionMatches(pos, "[:", 0, 2)) {
            posix = true;
            pos = PatternProps.skipWhiteSpace(pattern, pos + 2);
            if (pos < pattern.length() && pattern.charAt(pos) == '^') {
                ++pos;
                invert = true;
            }
        }
        else {
            if (!pattern.regionMatches(true, pos, "\\p", 0, 2) && !pattern.regionMatches(pos, "\\N", 0, 2)) {
                return null;
            }
            final char c = pattern.charAt(pos + 1);
            invert = (c == 'P');
            isName = (c == 'N');
            pos = PatternProps.skipWhiteSpace(pattern, pos + 2);
            if (pos == pattern.length() || pattern.charAt(pos++) != '{') {
                return null;
            }
        }
        final int close = pattern.indexOf(posix ? ":]" : "}", pos);
        if (close < 0) {
            return null;
        }
        final int equals = pattern.indexOf(61, pos);
        String propName;
        String valueName;
        if (equals >= 0 && equals < close && !isName) {
            propName = pattern.substring(pos, equals);
            valueName = pattern.substring(equals + 1, close);
        }
        else {
            propName = pattern.substring(pos, close);
            valueName = "";
            if (isName) {
                valueName = propName;
                propName = "na";
            }
        }
        this.applyPropertyAlias(propName, valueName, symbols);
        if (invert) {
            this.complement();
        }
        ppos.setIndex(close + (posix ? 2 : 1));
        return this;
    }
    
    private void applyPropertyPattern(final RuleCharacterIterator chars, final StringBuffer rebuiltPat, final SymbolTable symbols) {
        final String patStr = chars.lookahead();
        final ParsePosition pos = new ParsePosition(0);
        this.applyPropertyPattern(patStr, pos, symbols);
        if (pos.getIndex() == 0) {
            syntaxError(chars, "Invalid property pattern");
        }
        chars.jumpahead(pos.getIndex());
        rebuiltPat.append(patStr.substring(0, pos.getIndex()));
    }
    
    private static final void addCaseMapping(final UnicodeSet set, final int result, final StringBuilder full) {
        if (result >= 0) {
            if (result > 31) {
                set.add(result);
            }
            else {
                set.add(full.toString());
                full.setLength(0);
            }
        }
    }
    
    public UnicodeSet closeOver(final int attribute) {
        this.checkFrozen();
        if ((attribute & 0x6) != 0x0) {
            final UCaseProps csp = UCaseProps.INSTANCE;
            final UnicodeSet foldSet = new UnicodeSet(this);
            final ULocale root = ULocale.ROOT;
            if ((attribute & 0x2) != 0x0) {
                foldSet.strings.clear();
            }
            final int n = this.getRangeCount();
            final StringBuilder full = new StringBuilder();
            final int[] locCache = { 0 };
            for (int i = 0; i < n; ++i) {
                final int start = this.getRangeStart(i);
                final int end = this.getRangeEnd(i);
                if ((attribute & 0x2) != 0x0) {
                    for (int cp = start; cp <= end; ++cp) {
                        csp.addCaseClosure(cp, foldSet);
                    }
                }
                else {
                    for (int cp = start; cp <= end; ++cp) {
                        int result = csp.toFullLower(cp, null, full, root, locCache);
                        addCaseMapping(foldSet, result, full);
                        result = csp.toFullTitle(cp, null, full, root, locCache);
                        addCaseMapping(foldSet, result, full);
                        result = csp.toFullUpper(cp, null, full, root, locCache);
                        addCaseMapping(foldSet, result, full);
                        result = csp.toFullFolding(cp, full, 0);
                        addCaseMapping(foldSet, result, full);
                    }
                }
            }
            if (!this.strings.isEmpty()) {
                if ((attribute & 0x2) != 0x0) {
                    for (final String s : this.strings) {
                        final String str = UCharacter.foldCase(s, 0);
                        if (!csp.addStringCaseClosure(str, foldSet)) {
                            foldSet.add(str);
                        }
                    }
                }
                else {
                    final BreakIterator bi = BreakIterator.getWordInstance(root);
                    for (final String str : this.strings) {
                        foldSet.add(UCharacter.toLowerCase(root, str));
                        foldSet.add(UCharacter.toTitleCase(root, str, bi));
                        foldSet.add(UCharacter.toUpperCase(root, str));
                        foldSet.add(UCharacter.foldCase(str, 0));
                    }
                }
            }
            this.set(foldSet);
        }
        return this;
    }
    
    public boolean isFrozen() {
        return this.bmpSet != null || this.stringSpan != null;
    }
    
    public UnicodeSet freeze() {
        if (!this.isFrozen()) {
            this.buffer = null;
            if (this.list.length > this.len + 16) {
                final int capacity = (this.len == 0) ? 1 : this.len;
                final int[] oldList = this.list;
                this.list = new int[capacity];
                int i = capacity;
                while (i-- > 0) {
                    this.list[i] = oldList[i];
                }
            }
            if (!this.strings.isEmpty()) {
                this.stringSpan = new UnicodeSetStringSpan(this, new ArrayList<String>(this.strings), 63);
                if (!this.stringSpan.needsStringSpanUTF16()) {
                    this.stringSpan = null;
                }
            }
            if (this.stringSpan == null) {
                this.bmpSet = new BMPSet(this.list, this.len);
            }
        }
        return this;
    }
    
    public int span(final CharSequence s, final SpanCondition spanCondition) {
        return this.span(s, 0, spanCondition);
    }
    
    public int span(final CharSequence s, int start, final SpanCondition spanCondition) {
        final int end = s.length();
        if (start < 0) {
            start = 0;
        }
        else if (start >= end) {
            return end;
        }
        if (this.bmpSet != null) {
            return start + this.bmpSet.span(s, start, end, spanCondition);
        }
        final int len = end - start;
        if (this.stringSpan != null) {
            return start + this.stringSpan.span(s, start, len, spanCondition);
        }
        if (!this.strings.isEmpty()) {
            final int which = (spanCondition == SpanCondition.NOT_CONTAINED) ? 41 : 42;
            final UnicodeSetStringSpan strSpan = new UnicodeSetStringSpan(this, new ArrayList<String>(this.strings), which);
            if (strSpan.needsStringSpanUTF16()) {
                return start + strSpan.span(s, start, len, spanCondition);
            }
        }
        final boolean spanContained = spanCondition != SpanCondition.NOT_CONTAINED;
        int next = start;
        do {
            final int c = Character.codePointAt(s, next);
            if (spanContained != this.contains(c)) {
                break;
            }
            next = Character.offsetByCodePoints(s, next, 1);
        } while (next < end);
        return next;
    }
    
    public int spanBack(final CharSequence s, final SpanCondition spanCondition) {
        return this.spanBack(s, s.length(), spanCondition);
    }
    
    public int spanBack(final CharSequence s, int fromIndex, final SpanCondition spanCondition) {
        if (fromIndex <= 0) {
            return 0;
        }
        if (fromIndex > s.length()) {
            fromIndex = s.length();
        }
        if (this.bmpSet != null) {
            return this.bmpSet.spanBack(s, fromIndex, spanCondition);
        }
        if (this.stringSpan != null) {
            return this.stringSpan.spanBack(s, fromIndex, spanCondition);
        }
        if (!this.strings.isEmpty()) {
            final int which = (spanCondition == SpanCondition.NOT_CONTAINED) ? 25 : 26;
            final UnicodeSetStringSpan strSpan = new UnicodeSetStringSpan(this, new ArrayList<String>(this.strings), which);
            if (strSpan.needsStringSpanUTF16()) {
                return strSpan.spanBack(s, fromIndex, spanCondition);
            }
        }
        final boolean spanContained = spanCondition != SpanCondition.NOT_CONTAINED;
        int prev = fromIndex;
        do {
            final int c = Character.codePointBefore(s, prev);
            if (spanContained != this.contains(c)) {
                break;
            }
            prev = Character.offsetByCodePoints(s, prev, -1);
        } while (prev > 0);
        return prev;
    }
    
    public UnicodeSet cloneAsThawed() {
        final UnicodeSet result = (UnicodeSet)this.clone();
        result.bmpSet = null;
        result.stringSpan = null;
        return result;
    }
    
    private void checkFrozen() {
        if (this.isFrozen()) {
            throw new UnsupportedOperationException("Attempt to modify frozen object");
        }
    }
    
    public Iterator<String> iterator() {
        return new UnicodeSetIterator2(this);
    }
    
    public boolean containsAll(final Collection<String> collection) {
        for (final String o : collection) {
            if (!this.contains(o)) {
                return false;
            }
        }
        return true;
    }
    
    public boolean containsNone(final Collection<String> collection) {
        for (final String o : collection) {
            if (this.contains(o)) {
                return false;
            }
        }
        return true;
    }
    
    public final boolean containsSome(final Collection<String> collection) {
        return !this.containsNone(collection);
    }
    
    public UnicodeSet addAll(final String... collection) {
        this.checkFrozen();
        for (final String str : collection) {
            this.add(str);
        }
        return this;
    }
    
    public UnicodeSet removeAll(final Collection<String> collection) {
        this.checkFrozen();
        for (final String o : collection) {
            this.remove(o);
        }
        return this;
    }
    
    public UnicodeSet retainAll(final Collection<String> collection) {
        this.checkFrozen();
        final UnicodeSet toRetain = new UnicodeSet();
        toRetain.addAll(collection);
        this.retainAll(toRetain);
        return this;
    }
    
    public int compareTo(final UnicodeSet o) {
        return this.compareTo(o, ComparisonStyle.SHORTER_FIRST);
    }
    
    public int compareTo(final UnicodeSet o, final ComparisonStyle style) {
        if (style != ComparisonStyle.LEXICOGRAPHIC) {
            final int diff = this.size() - o.size();
            if (diff != 0) {
                return (diff < 0 == (style == ComparisonStyle.SHORTER_FIRST)) ? -1 : 1;
            }
        }
        int i;
        int result;
        for (i = 0; 0 == (result = this.list[i] - o.list[i]); ++i) {
            if (this.list[i] == 1114112) {
                return compare(this.strings, o.strings);
            }
        }
        if (this.list[i] == 1114112) {
            if (this.strings.isEmpty()) {
                return 1;
            }
            final String item = this.strings.first();
            return compare(item, o.list[i]);
        }
        else {
            if (o.list[i] != 1114112) {
                return ((i & 0x1) == 0x0) ? result : (-result);
            }
            if (o.strings.isEmpty()) {
                return -1;
            }
            final String item = o.strings.first();
            return -compare(item, this.list[i]);
        }
    }
    
    public int compareTo(final Iterable<String> other) {
        return compare(this, other);
    }
    
    public static int compare(final String string, final int codePoint) {
        return CharSequences.compare(string, codePoint);
    }
    
    public static int compare(final int codePoint, final String string) {
        return -CharSequences.compare(string, codePoint);
    }
    
    public static <T extends Comparable<T>> int compare(final Iterable<T> collection1, final Iterable<T> collection2) {
        return compare(collection1.iterator(), collection2.iterator());
    }
    
    @Deprecated
    public static <T extends Comparable<T>> int compare(final Iterator<T> first, final Iterator<T> other) {
        while (first.hasNext()) {
            if (!other.hasNext()) {
                return 1;
            }
            final T item1 = first.next();
            final T item2 = other.next();
            final int result = item1.compareTo(item2);
            if (result != 0) {
                return result;
            }
        }
        return other.hasNext() ? -1 : 0;
    }
    
    public static <T extends Comparable<T>> int compare(final Collection<T> collection1, final Collection<T> collection2, final ComparisonStyle style) {
        if (style != ComparisonStyle.LEXICOGRAPHIC) {
            final int diff = collection1.size() - collection2.size();
            if (diff != 0) {
                return (diff < 0 == (style == ComparisonStyle.SHORTER_FIRST)) ? -1 : 1;
            }
        }
        return compare(collection1, collection2);
    }
    
    public static <T, U extends Collection<T>> U addAllTo(final Iterable<T> source, final U target) {
        for (final T item : source) {
            target.add(item);
        }
        return target;
    }
    
    public static <T> T[] addAllTo(final Iterable<T> source, final T[] target) {
        int i = 0;
        for (final T item : source) {
            target[i++] = item;
        }
        return target;
    }
    
    public Iterable<String> strings() {
        return Collections.unmodifiableSortedSet(this.strings);
    }
    
    @Deprecated
    public static int getSingleCodePoint(final CharSequence s) {
        return CharSequences.getSingleCodePoint(s);
    }
    
    @Deprecated
    public UnicodeSet addBridges(final UnicodeSet dontCare) {
        final UnicodeSet notInInput = new UnicodeSet(this).complement();
        final UnicodeSetIterator it = new UnicodeSetIterator(notInInput);
        while (it.nextRange()) {
            if (it.codepoint != 0 && it.codepoint != UnicodeSetIterator.IS_STRING && it.codepointEnd != 1114111 && dontCare.contains(it.codepoint, it.codepointEnd)) {
                this.add(it.codepoint, it.codepointEnd);
            }
        }
        return this;
    }
    
    @Deprecated
    public int findIn(final CharSequence value, int fromIndex, final boolean findNot) {
        while (fromIndex < value.length()) {
            final int cp = UTF16.charAt(value, fromIndex);
            if (this.contains(cp) != findNot) {
                break;
            }
            fromIndex += UTF16.getCharCount(cp);
        }
        return fromIndex;
    }
    
    @Deprecated
    public int findLastIn(final CharSequence value, int fromIndex, final boolean findNot) {
        --fromIndex;
        while (fromIndex >= 0) {
            final int cp = UTF16.charAt(value, fromIndex);
            if (this.contains(cp) != findNot) {
                break;
            }
            fromIndex -= UTF16.getCharCount(cp);
        }
        return (fromIndex < 0) ? -1 : fromIndex;
    }
    
    @Deprecated
    public String stripFrom(final CharSequence source, final boolean matches) {
        final StringBuilder result = new StringBuilder();
        int inside;
        for (int pos = 0; pos < source.length(); pos = this.findIn(source, inside, matches)) {
            inside = this.findIn(source, pos, !matches);
            result.append(source.subSequence(pos, inside));
        }
        return result.toString();
    }
    
    public static XSymbolTable getDefaultXSymbolTable() {
        return UnicodeSet.XSYMBOL_TABLE;
    }
    
    public static void setDefaultXSymbolTable(final XSymbolTable xSymbolTable) {
        UnicodeSet.XSYMBOL_TABLE = xSymbolTable;
    }
    
    static {
        EMPTY = new UnicodeSet().freeze();
        ALL_CODE_POINTS = new UnicodeSet(0, 1114111).freeze();
        UnicodeSet.XSYMBOL_TABLE = null;
        UnicodeSet.INCLUSIONS = null;
        NO_VERSION = VersionInfo.getInstance(0, 0, 0, 0);
    }
    
    private static class NumericValueFilter implements Filter
    {
        double value;
        
        NumericValueFilter(final double value) {
            this.value = value;
        }
        
        public boolean contains(final int ch) {
            return UCharacter.getUnicodeNumericValue(ch) == this.value;
        }
    }
    
    private static class GeneralCategoryMaskFilter implements Filter
    {
        int mask;
        
        GeneralCategoryMaskFilter(final int mask) {
            this.mask = mask;
        }
        
        public boolean contains(final int ch) {
            return (1 << UCharacter.getType(ch) & this.mask) != 0x0;
        }
    }
    
    private static class IntPropertyFilter implements Filter
    {
        int prop;
        int value;
        
        IntPropertyFilter(final int prop, final int value) {
            this.prop = prop;
            this.value = value;
        }
        
        public boolean contains(final int ch) {
            return UCharacter.getIntPropertyValue(ch, this.prop) == this.value;
        }
    }
    
    private static class ScriptExtensionsFilter implements Filter
    {
        int script;
        
        ScriptExtensionsFilter(final int script) {
            this.script = script;
        }
        
        public boolean contains(final int c) {
            return UScript.hasScript(c, this.script);
        }
    }
    
    private static class VersionFilter implements Filter
    {
        VersionInfo version;
        
        VersionFilter(final VersionInfo version) {
            this.version = version;
        }
        
        public boolean contains(final int ch) {
            final VersionInfo v = UCharacter.getAge(ch);
            return v != UnicodeSet.NO_VERSION && v.compareTo(this.version) <= 0;
        }
    }
    
    public abstract static class XSymbolTable implements SymbolTable
    {
        public UnicodeMatcher lookupMatcher(final int i) {
            return null;
        }
        
        public boolean applyPropertyAlias(final String propertyName, final String propertyValue, final UnicodeSet result) {
            return false;
        }
        
        public char[] lookup(final String s) {
            return null;
        }
        
        public String parseReference(final String text, final ParsePosition pos, final int limit) {
            return null;
        }
    }
    
    private static class UnicodeSetIterator2 implements Iterator<String>
    {
        private int[] sourceList;
        private int len;
        private int item;
        private int current;
        private int limit;
        private TreeSet<String> sourceStrings;
        private Iterator<String> stringIterator;
        private char[] buffer;
        
        UnicodeSetIterator2(final UnicodeSet source) {
            this.len = source.len - 1;
            if (this.item >= this.len) {
                this.stringIterator = source.strings.iterator();
                this.sourceList = null;
            }
            else {
                this.sourceStrings = source.strings;
                this.sourceList = source.list;
                this.current = this.sourceList[this.item++];
                this.limit = this.sourceList[this.item++];
            }
        }
        
        public boolean hasNext() {
            return this.sourceList != null || this.stringIterator.hasNext();
        }
        
        public String next() {
            if (this.sourceList == null) {
                return this.stringIterator.next();
            }
            final int codepoint = this.current++;
            if (this.current >= this.limit) {
                if (this.item >= this.len) {
                    this.stringIterator = this.sourceStrings.iterator();
                    this.sourceList = null;
                }
                else {
                    this.current = this.sourceList[this.item++];
                    this.limit = this.sourceList[this.item++];
                }
            }
            if (codepoint <= 65535) {
                return String.valueOf((char)codepoint);
            }
            if (this.buffer == null) {
                this.buffer = new char[2];
            }
            final int offset = codepoint - 65536;
            this.buffer[0] = (char)((offset >>> 10) + 55296);
            this.buffer[1] = (char)((offset & 0x3FF) + 56320);
            return String.valueOf(this.buffer);
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    
    public enum ComparisonStyle
    {
        SHORTER_FIRST, 
        LEXICOGRAPHIC, 
        LONGER_FIRST;
    }
    
    public enum SpanCondition
    {
        NOT_CONTAINED, 
        CONTAINED, 
        SIMPLE, 
        CONDITION_COUNT;
    }
    
    private interface Filter
    {
        boolean contains(final int p0);
    }
}
