// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.IOException;
import com.ibm.icu.text.UTF16;

public final class CharsTrie implements Cloneable, Iterable<Entry>
{
    private static BytesTrie.Result[] valueResults_;
    static final int kMaxBranchLinearSubNodeLength = 5;
    static final int kMinLinearMatch = 48;
    static final int kMaxLinearMatchLength = 16;
    static final int kMinValueLead = 64;
    static final int kNodeTypeMask = 63;
    static final int kValueIsFinal = 32768;
    static final int kMaxOneUnitValue = 16383;
    static final int kMinTwoUnitValueLead = 16384;
    static final int kThreeUnitValueLead = 32767;
    static final int kMaxTwoUnitValue = 1073676287;
    static final int kMaxOneUnitNodeValue = 255;
    static final int kMinTwoUnitNodeValueLead = 16448;
    static final int kThreeUnitNodeValueLead = 32704;
    static final int kMaxTwoUnitNodeValue = 16646143;
    static final int kMaxOneUnitDelta = 64511;
    static final int kMinTwoUnitDeltaLead = 64512;
    static final int kThreeUnitDeltaLead = 65535;
    static final int kMaxTwoUnitDelta = 67043327;
    private CharSequence chars_;
    private int root_;
    private int pos_;
    private int remainingMatchLength_;
    
    public CharsTrie(final CharSequence trieChars, final int offset) {
        this.chars_ = trieChars;
        this.root_ = offset;
        this.pos_ = offset;
        this.remainingMatchLength_ = -1;
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    public CharsTrie reset() {
        this.pos_ = this.root_;
        this.remainingMatchLength_ = -1;
        return this;
    }
    
    public CharsTrie saveState(final State state) {
        state.chars = this.chars_;
        state.root = this.root_;
        state.pos = this.pos_;
        state.remainingMatchLength = this.remainingMatchLength_;
        return this;
    }
    
    public CharsTrie resetToState(final State state) {
        if (this.chars_ == state.chars && this.chars_ != null && this.root_ == state.root) {
            this.pos_ = state.pos;
            this.remainingMatchLength_ = state.remainingMatchLength;
            return this;
        }
        throw new IllegalArgumentException("incompatible trie state");
    }
    
    public BytesTrie.Result current() {
        final int pos = this.pos_;
        if (pos < 0) {
            return BytesTrie.Result.NO_MATCH;
        }
        final int node;
        return (this.remainingMatchLength_ < 0 && (node = this.chars_.charAt(pos)) >= 64) ? CharsTrie.valueResults_[node >> 15] : BytesTrie.Result.NO_VALUE;
    }
    
    public BytesTrie.Result first(final int inUnit) {
        this.remainingMatchLength_ = -1;
        return this.nextImpl(this.root_, inUnit);
    }
    
    public BytesTrie.Result firstForCodePoint(final int cp) {
        return (cp <= 65535) ? this.first(cp) : (this.first(UTF16.getLeadSurrogate(cp)).hasNext() ? this.next(UTF16.getTrailSurrogate(cp)) : BytesTrie.Result.NO_MATCH);
    }
    
    public BytesTrie.Result next(final int inUnit) {
        int pos = this.pos_;
        if (pos < 0) {
            return BytesTrie.Result.NO_MATCH;
        }
        int length = this.remainingMatchLength_;
        if (length < 0) {
            return this.nextImpl(pos, inUnit);
        }
        if (inUnit == this.chars_.charAt(pos++)) {
            this.remainingMatchLength_ = --length;
            this.pos_ = pos;
            final int node;
            return (length < 0 && (node = this.chars_.charAt(pos)) >= 64) ? CharsTrie.valueResults_[node >> 15] : BytesTrie.Result.NO_VALUE;
        }
        this.stop();
        return BytesTrie.Result.NO_MATCH;
    }
    
    public BytesTrie.Result nextForCodePoint(final int cp) {
        return (cp <= 65535) ? this.next(cp) : (this.next(UTF16.getLeadSurrogate(cp)).hasNext() ? this.next(UTF16.getTrailSurrogate(cp)) : BytesTrie.Result.NO_MATCH);
    }
    
    public BytesTrie.Result next(final CharSequence s, int sIndex, final int sLimit) {
        if (sIndex >= sLimit) {
            return this.current();
        }
        int pos = this.pos_;
        if (pos < 0) {
            return BytesTrie.Result.NO_MATCH;
        }
        int length = this.remainingMatchLength_;
        while (sIndex != sLimit) {
            char inUnit = s.charAt(sIndex++);
            if (length < 0) {
                this.remainingMatchLength_ = length;
                int node = this.chars_.charAt(pos++);
                while (true) {
                    if (node < 48) {
                        final BytesTrie.Result result = this.branchNext(pos, node, inUnit);
                        if (result == BytesTrie.Result.NO_MATCH) {
                            return BytesTrie.Result.NO_MATCH;
                        }
                        if (sIndex == sLimit) {
                            return result;
                        }
                        if (result == BytesTrie.Result.FINAL_VALUE) {
                            this.stop();
                            return BytesTrie.Result.NO_MATCH;
                        }
                        inUnit = s.charAt(sIndex++);
                        pos = this.pos_;
                        node = this.chars_.charAt(pos++);
                    }
                    else if (node < 64) {
                        length = node - 48;
                        if (inUnit != this.chars_.charAt(pos)) {
                            this.stop();
                            return BytesTrie.Result.NO_MATCH;
                        }
                        ++pos;
                        --length;
                        break;
                    }
                    else {
                        if ((node & 0x8000) != 0x0) {
                            this.stop();
                            return BytesTrie.Result.NO_MATCH;
                        }
                        pos = skipNodeValue(pos, node);
                        node &= 0x3F;
                    }
                }
            }
            else {
                if (inUnit != this.chars_.charAt(pos)) {
                    this.stop();
                    return BytesTrie.Result.NO_MATCH;
                }
                ++pos;
                --length;
            }
        }
        this.remainingMatchLength_ = length;
        this.pos_ = pos;
        int node;
        return (length < 0 && (node = this.chars_.charAt(pos)) >= 64) ? CharsTrie.valueResults_[node >> 15] : BytesTrie.Result.NO_VALUE;
    }
    
    public int getValue() {
        int pos = this.pos_;
        final int leadUnit = this.chars_.charAt(pos++);
        assert leadUnit >= 64;
        return ((leadUnit & 0x8000) != 0x0) ? readValue(this.chars_, pos, leadUnit & 0x7FFF) : readNodeValue(this.chars_, pos, leadUnit);
    }
    
    public long getUniqueValue() {
        final int pos = this.pos_;
        if (pos < 0) {
            return 0L;
        }
        final long uniqueValue = findUniqueValue(this.chars_, pos + this.remainingMatchLength_ + 1, 0L);
        return uniqueValue << 31 >> 31;
    }
    
    public int getNextChars(final Appendable out) {
        int pos = this.pos_;
        if (pos < 0) {
            return 0;
        }
        if (this.remainingMatchLength_ >= 0) {
            append(out, this.chars_.charAt(pos));
            return 1;
        }
        int node = this.chars_.charAt(pos++);
        if (node >= 64) {
            if ((node & 0x8000) != 0x0) {
                return 0;
            }
            pos = skipNodeValue(pos, node);
            node &= 0x3F;
        }
        if (node < 48) {
            if (node == 0) {
                node = this.chars_.charAt(pos++);
            }
            getNextBranchChars(this.chars_, pos, ++node, out);
            return node;
        }
        append(out, this.chars_.charAt(pos));
        return 1;
    }
    
    public Iterator iterator() {
        return new Iterator(this.chars_, this.pos_, this.remainingMatchLength_, 0);
    }
    
    public Iterator iterator(final int maxStringLength) {
        return new Iterator(this.chars_, this.pos_, this.remainingMatchLength_, maxStringLength);
    }
    
    public static Iterator iterator(final CharSequence trieChars, final int offset, final int maxStringLength) {
        return new Iterator(trieChars, offset, -1, maxStringLength);
    }
    
    private void stop() {
        this.pos_ = -1;
    }
    
    private static int readValue(final CharSequence chars, final int pos, final int leadUnit) {
        int value;
        if (leadUnit < 16384) {
            value = leadUnit;
        }
        else if (leadUnit < 32767) {
            value = (leadUnit - 16384 << 16 | chars.charAt(pos));
        }
        else {
            value = (chars.charAt(pos) << 16 | chars.charAt(pos + 1));
        }
        return value;
    }
    
    private static int skipValue(int pos, final int leadUnit) {
        if (leadUnit >= 16384) {
            if (leadUnit < 32767) {
                ++pos;
            }
            else {
                pos += 2;
            }
        }
        return pos;
    }
    
    private static int skipValue(final CharSequence chars, int pos) {
        final int leadUnit = chars.charAt(pos++);
        return skipValue(pos, leadUnit & 0x7FFF);
    }
    
    private static int readNodeValue(final CharSequence chars, final int pos, final int leadUnit) {
        assert 64 <= leadUnit && leadUnit < 32768;
        int value;
        if (leadUnit < 16448) {
            value = (leadUnit >> 6) - 1;
        }
        else if (leadUnit < 32704) {
            value = ((leadUnit & 0x7FC0) - 16448 << 10 | chars.charAt(pos));
        }
        else {
            value = (chars.charAt(pos) << 16 | chars.charAt(pos + 1));
        }
        return value;
    }
    
    private static int skipNodeValue(int pos, final int leadUnit) {
        assert 64 <= leadUnit && leadUnit < 32768;
        if (leadUnit >= 16448) {
            if (leadUnit < 32704) {
                ++pos;
            }
            else {
                pos += 2;
            }
        }
        return pos;
    }
    
    private static int jumpByDelta(final CharSequence chars, int pos) {
        int delta = chars.charAt(pos++);
        if (delta >= 64512) {
            if (delta == 65535) {
                delta = (chars.charAt(pos) << 16 | chars.charAt(pos + 1));
                pos += 2;
            }
            else {
                delta = (delta - 64512 << 16 | chars.charAt(pos++));
            }
        }
        return pos + delta;
    }
    
    private static int skipDelta(final CharSequence chars, int pos) {
        final int delta = chars.charAt(pos++);
        if (delta >= 64512) {
            if (delta == 65535) {
                pos += 2;
            }
            else {
                ++pos;
            }
        }
        return pos;
    }
    
    private BytesTrie.Result branchNext(int pos, int length, final int inUnit) {
        if (length == 0) {
            length = this.chars_.charAt(pos++);
        }
        ++length;
        while (length > 5) {
            if (inUnit < this.chars_.charAt(pos++)) {
                length >>= 1;
                pos = jumpByDelta(this.chars_, pos);
            }
            else {
                length -= length >> 1;
                pos = skipDelta(this.chars_, pos);
            }
        }
        while (inUnit != this.chars_.charAt(pos++)) {
            --length;
            pos = skipValue(this.chars_, pos);
            if (length <= 1) {
                if (inUnit == this.chars_.charAt(pos++)) {
                    this.pos_ = pos;
                    final int node = this.chars_.charAt(pos);
                    return (node >= 64) ? CharsTrie.valueResults_[node >> 15] : BytesTrie.Result.NO_VALUE;
                }
                this.stop();
                return BytesTrie.Result.NO_MATCH;
            }
        }
        int node2 = this.chars_.charAt(pos);
        BytesTrie.Result result;
        if ((node2 & 0x8000) != 0x0) {
            result = BytesTrie.Result.FINAL_VALUE;
        }
        else {
            ++pos;
            int delta;
            if (node2 < 16384) {
                delta = node2;
            }
            else if (node2 < 32767) {
                delta = (node2 - 16384 << 16 | this.chars_.charAt(pos++));
            }
            else {
                delta = (this.chars_.charAt(pos) << 16 | this.chars_.charAt(pos + 1));
                pos += 2;
            }
            pos += delta;
            node2 = this.chars_.charAt(pos);
            result = ((node2 >= 64) ? CharsTrie.valueResults_[node2 >> 15] : BytesTrie.Result.NO_VALUE);
        }
        this.pos_ = pos;
        return result;
    }
    
    private BytesTrie.Result nextImpl(int pos, final int inUnit) {
        int node = this.chars_.charAt(pos++);
        while (node >= 48) {
            if (node < 64) {
                int length = node - 48;
                if (inUnit == this.chars_.charAt(pos++)) {
                    this.remainingMatchLength_ = --length;
                    this.pos_ = pos;
                    return (length < 0 && (node = this.chars_.charAt(pos)) >= 64) ? CharsTrie.valueResults_[node >> 15] : BytesTrie.Result.NO_VALUE;
                }
            }
            else if ((node & 0x8000) == 0x0) {
                pos = skipNodeValue(pos, node);
                node &= 0x3F;
                continue;
            }
            this.stop();
            return BytesTrie.Result.NO_MATCH;
        }
        return this.branchNext(pos, node, inUnit);
    }
    
    private static long findUniqueValueFromBranch(final CharSequence chars, int pos, int length, long uniqueValue) {
        while (length > 5) {
            ++pos;
            uniqueValue = findUniqueValueFromBranch(chars, jumpByDelta(chars, pos), length >> 1, uniqueValue);
            if (uniqueValue == 0L) {
                return 0L;
            }
            length -= length >> 1;
            pos = skipDelta(chars, pos);
        }
        do {
            ++pos;
            int node = chars.charAt(pos++);
            final boolean isFinal = (node & 0x8000) != 0x0;
            node &= 0x7FFF;
            final int value = readValue(chars, pos, node);
            pos = skipValue(pos, node);
            if (isFinal) {
                if (uniqueValue != 0L) {
                    if (value != (int)(uniqueValue >> 1)) {
                        return 0L;
                    }
                    continue;
                }
                else {
                    uniqueValue = ((long)value << 1 | 0x1L);
                }
            }
            else {
                uniqueValue = findUniqueValue(chars, pos + value, uniqueValue);
                if (uniqueValue == 0L) {
                    return 0L;
                }
                continue;
            }
        } while (--length > 1);
        return (long)(pos + 1) << 33 | (uniqueValue & 0x1FFFFFFFFL);
    }
    
    private static long findUniqueValue(final CharSequence chars, int pos, long uniqueValue) {
        int node = chars.charAt(pos++);
        while (true) {
            if (node < 48) {
                if (node == 0) {
                    node = chars.charAt(pos++);
                }
                uniqueValue = findUniqueValueFromBranch(chars, pos, node + 1, uniqueValue);
                if (uniqueValue == 0L) {
                    return 0L;
                }
                pos = (int)(uniqueValue >>> 33);
                node = chars.charAt(pos++);
            }
            else if (node < 64) {
                pos += node - 48 + 1;
                node = chars.charAt(pos++);
            }
            else {
                final boolean isFinal = (node & 0x8000) != 0x0;
                int value;
                if (isFinal) {
                    value = readValue(chars, pos, node & 0x7FFF);
                }
                else {
                    value = readNodeValue(chars, pos, node);
                }
                if (uniqueValue != 0L) {
                    if (value != (int)(uniqueValue >> 1)) {
                        return 0L;
                    }
                }
                else {
                    uniqueValue = ((long)value << 1 | 0x1L);
                }
                if (isFinal) {
                    return uniqueValue;
                }
                pos = skipNodeValue(pos, node);
                node &= 0x3F;
            }
        }
    }
    
    private static void getNextBranchChars(final CharSequence chars, int pos, int length, final Appendable out) {
        while (length > 5) {
            ++pos;
            getNextBranchChars(chars, jumpByDelta(chars, pos), length >> 1, out);
            length -= length >> 1;
            pos = skipDelta(chars, pos);
        }
        do {
            append(out, chars.charAt(pos++));
            pos = skipValue(chars, pos);
        } while (--length > 1);
        append(out, chars.charAt(pos));
    }
    
    private static void append(final Appendable out, final int c) {
        try {
            out.append((char)c);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    static {
        CharsTrie.valueResults_ = new BytesTrie.Result[] { BytesTrie.Result.INTERMEDIATE_VALUE, BytesTrie.Result.FINAL_VALUE };
    }
    
    public static final class State
    {
        private CharSequence chars;
        private int root;
        private int pos;
        private int remainingMatchLength;
    }
    
    public static final class Entry
    {
        public CharSequence chars;
        public int value;
        
        private Entry() {
        }
    }
    
    public static final class Iterator implements java.util.Iterator<Entry>
    {
        private CharSequence chars_;
        private int pos_;
        private int initialPos_;
        private int remainingMatchLength_;
        private int initialRemainingMatchLength_;
        private boolean skipValue_;
        private StringBuilder str_;
        private int maxLength_;
        private Entry entry_;
        private ArrayList<Long> stack_;
        
        private Iterator(final CharSequence trieChars, final int offset, final int remainingMatchLength, final int maxStringLength) {
            this.str_ = new StringBuilder();
            this.entry_ = new Entry();
            this.stack_ = new ArrayList<Long>();
            this.chars_ = trieChars;
            this.initialPos_ = offset;
            this.pos_ = offset;
            this.initialRemainingMatchLength_ = remainingMatchLength;
            this.remainingMatchLength_ = remainingMatchLength;
            this.maxLength_ = maxStringLength;
            int length = this.remainingMatchLength_;
            if (length >= 0) {
                ++length;
                if (this.maxLength_ > 0 && length > this.maxLength_) {
                    length = this.maxLength_;
                }
                this.str_.append(this.chars_, this.pos_, this.pos_ + length);
                this.pos_ += length;
                this.remainingMatchLength_ -= length;
            }
        }
        
        public Iterator reset() {
            this.pos_ = this.initialPos_;
            this.remainingMatchLength_ = this.initialRemainingMatchLength_;
            this.skipValue_ = false;
            int length = this.remainingMatchLength_ + 1;
            if (this.maxLength_ > 0 && length > this.maxLength_) {
                length = this.maxLength_;
            }
            this.str_.setLength(length);
            this.pos_ += length;
            this.remainingMatchLength_ -= length;
            this.stack_.clear();
            return this;
        }
        
        public boolean hasNext() {
            return this.pos_ >= 0 || !this.stack_.isEmpty();
        }
        
        public Entry next() {
            int pos = this.pos_;
            if (pos < 0) {
                if (this.stack_.isEmpty()) {
                    throw new NoSuchElementException();
                }
                final long top = this.stack_.remove(this.stack_.size() - 1);
                int length = (int)top;
                pos = (int)(top >> 32);
                this.str_.setLength(length & 0xFFFF);
                length >>>= 16;
                if (length > 1) {
                    pos = this.branchNext(pos, length);
                    if (pos < 0) {
                        return this.entry_;
                    }
                }
                else {
                    this.str_.append(this.chars_.charAt(pos++));
                }
            }
            if (this.remainingMatchLength_ >= 0) {
                return this.truncateAndStop();
            }
            while (true) {
                int node = this.chars_.charAt(pos++);
                if (node >= 64) {
                    if (!this.skipValue_) {
                        final boolean isFinal = (node & 0x8000) != 0x0;
                        if (isFinal) {
                            this.entry_.value = readValue(this.chars_, pos, node & 0x7FFF);
                        }
                        else {
                            this.entry_.value = readNodeValue(this.chars_, pos, node);
                        }
                        if (isFinal || (this.maxLength_ > 0 && this.str_.length() == this.maxLength_)) {
                            this.pos_ = -1;
                        }
                        else {
                            this.pos_ = pos - 1;
                            this.skipValue_ = true;
                        }
                        this.entry_.chars = this.str_;
                        return this.entry_;
                    }
                    pos = skipNodeValue(pos, node);
                    node &= 0x3F;
                    this.skipValue_ = false;
                }
                if (this.maxLength_ > 0 && this.str_.length() == this.maxLength_) {
                    return this.truncateAndStop();
                }
                if (node < 48) {
                    if (node == 0) {
                        node = this.chars_.charAt(pos++);
                    }
                    pos = this.branchNext(pos, node + 1);
                    if (pos < 0) {
                        return this.entry_;
                    }
                    continue;
                }
                else {
                    final int length2 = node - 48 + 1;
                    if (this.maxLength_ > 0 && this.str_.length() + length2 > this.maxLength_) {
                        this.str_.append(this.chars_, pos, pos + this.maxLength_ - this.str_.length());
                        return this.truncateAndStop();
                    }
                    this.str_.append(this.chars_, pos, pos + length2);
                    pos += length2;
                }
            }
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
        private Entry truncateAndStop() {
            this.pos_ = -1;
            this.entry_.chars = this.str_;
            this.entry_.value = -1;
            return this.entry_;
        }
        
        private int branchNext(int pos, int length) {
            while (length > 5) {
                ++pos;
                this.stack_.add((long)skipDelta(this.chars_, pos) << 32 | (long)(length - (length >> 1) << 16) | (long)this.str_.length());
                length >>= 1;
                pos = jumpByDelta(this.chars_, pos);
            }
            final char trieUnit = this.chars_.charAt(pos++);
            int node = this.chars_.charAt(pos++);
            final boolean isFinal = (node & 0x8000) != 0x0;
            final int value = readValue(this.chars_, pos, node &= 0x7FFF);
            pos = skipValue(pos, node);
            this.stack_.add((long)pos << 32 | (long)(length - 1 << 16) | (long)this.str_.length());
            this.str_.append(trieUnit);
            if (isFinal) {
                this.pos_ = -1;
                this.entry_.chars = this.str_;
                this.entry_.value = value;
                return -1;
            }
            return pos + value;
        }
    }
}
