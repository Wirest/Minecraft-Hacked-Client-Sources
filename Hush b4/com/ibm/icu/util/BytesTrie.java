// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.io.IOException;

public final class BytesTrie implements Cloneable, Iterable<Entry>
{
    private static Result[] valueResults_;
    static final int kMaxBranchLinearSubNodeLength = 5;
    static final int kMinLinearMatch = 16;
    static final int kMaxLinearMatchLength = 16;
    static final int kMinValueLead = 32;
    private static final int kValueIsFinal = 1;
    static final int kMinOneByteValueLead = 16;
    static final int kMaxOneByteValue = 64;
    static final int kMinTwoByteValueLead = 81;
    static final int kMaxTwoByteValue = 6911;
    static final int kMinThreeByteValueLead = 108;
    static final int kFourByteValueLead = 126;
    static final int kMaxThreeByteValue = 1179647;
    static final int kFiveByteValueLead = 127;
    static final int kMaxOneByteDelta = 191;
    static final int kMinTwoByteDeltaLead = 192;
    static final int kMinThreeByteDeltaLead = 240;
    static final int kFourByteDeltaLead = 254;
    static final int kFiveByteDeltaLead = 255;
    static final int kMaxTwoByteDelta = 12287;
    static final int kMaxThreeByteDelta = 917503;
    private byte[] bytes_;
    private int root_;
    private int pos_;
    private int remainingMatchLength_;
    
    public BytesTrie(final byte[] trieBytes, final int offset) {
        this.bytes_ = trieBytes;
        this.root_ = offset;
        this.pos_ = offset;
        this.remainingMatchLength_ = -1;
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    public BytesTrie reset() {
        this.pos_ = this.root_;
        this.remainingMatchLength_ = -1;
        return this;
    }
    
    public BytesTrie saveState(final State state) {
        state.bytes = this.bytes_;
        state.root = this.root_;
        state.pos = this.pos_;
        state.remainingMatchLength = this.remainingMatchLength_;
        return this;
    }
    
    public BytesTrie resetToState(final State state) {
        if (this.bytes_ == state.bytes && this.bytes_ != null && this.root_ == state.root) {
            this.pos_ = state.pos;
            this.remainingMatchLength_ = state.remainingMatchLength;
            return this;
        }
        throw new IllegalArgumentException("incompatible trie state");
    }
    
    public Result current() {
        final int pos = this.pos_;
        if (pos < 0) {
            return Result.NO_MATCH;
        }
        final int node;
        return (this.remainingMatchLength_ < 0 && (node = (this.bytes_[pos] & 0xFF)) >= 32) ? BytesTrie.valueResults_[node & 0x1] : Result.NO_VALUE;
    }
    
    public Result first(int inByte) {
        this.remainingMatchLength_ = -1;
        if (inByte < 0) {
            inByte += 256;
        }
        return this.nextImpl(this.root_, inByte);
    }
    
    public Result next(int inByte) {
        int pos = this.pos_;
        if (pos < 0) {
            return Result.NO_MATCH;
        }
        if (inByte < 0) {
            inByte += 256;
        }
        int length = this.remainingMatchLength_;
        if (length < 0) {
            return this.nextImpl(pos, inByte);
        }
        if (inByte == (this.bytes_[pos++] & 0xFF)) {
            this.remainingMatchLength_ = --length;
            this.pos_ = pos;
            final int node;
            return (length < 0 && (node = (this.bytes_[pos] & 0xFF)) >= 32) ? BytesTrie.valueResults_[node & 0x1] : Result.NO_VALUE;
        }
        this.stop();
        return Result.NO_MATCH;
    }
    
    public Result next(final byte[] s, int sIndex, final int sLimit) {
        if (sIndex >= sLimit) {
            return this.current();
        }
        int pos = this.pos_;
        if (pos < 0) {
            return Result.NO_MATCH;
        }
        int length = this.remainingMatchLength_;
        while (sIndex != sLimit) {
            byte inByte = s[sIndex++];
            if (length < 0) {
                this.remainingMatchLength_ = length;
                while (true) {
                    final int node = this.bytes_[pos++] & 0xFF;
                    if (node < 16) {
                        final Result result = this.branchNext(pos, node, inByte & 0xFF);
                        if (result == Result.NO_MATCH) {
                            return Result.NO_MATCH;
                        }
                        if (sIndex == sLimit) {
                            return result;
                        }
                        if (result == Result.FINAL_VALUE) {
                            this.stop();
                            return Result.NO_MATCH;
                        }
                        inByte = s[sIndex++];
                        pos = this.pos_;
                    }
                    else if (node < 32) {
                        length = node - 16;
                        if (inByte != this.bytes_[pos]) {
                            this.stop();
                            return Result.NO_MATCH;
                        }
                        ++pos;
                        --length;
                        break;
                    }
                    else {
                        if ((node & 0x1) != 0x0) {
                            this.stop();
                            return Result.NO_MATCH;
                        }
                        pos = skipValue(pos, node);
                        assert (this.bytes_[pos] & 0xFF) < 32;
                        continue;
                    }
                }
            }
            else {
                if (inByte != this.bytes_[pos]) {
                    this.stop();
                    return Result.NO_MATCH;
                }
                ++pos;
                --length;
            }
        }
        this.remainingMatchLength_ = length;
        this.pos_ = pos;
        int node;
        return (length < 0 && (node = (this.bytes_[pos] & 0xFF)) >= 32) ? BytesTrie.valueResults_[node & 0x1] : Result.NO_VALUE;
    }
    
    public int getValue() {
        int pos = this.pos_;
        final int leadByte = this.bytes_[pos++] & 0xFF;
        assert leadByte >= 32;
        return readValue(this.bytes_, pos, leadByte >> 1);
    }
    
    public long getUniqueValue() {
        final int pos = this.pos_;
        if (pos < 0) {
            return 0L;
        }
        final long uniqueValue = findUniqueValue(this.bytes_, pos + this.remainingMatchLength_ + 1, 0L);
        return uniqueValue << 31 >> 31;
    }
    
    public int getNextBytes(final Appendable out) {
        int pos = this.pos_;
        if (pos < 0) {
            return 0;
        }
        if (this.remainingMatchLength_ >= 0) {
            append(out, this.bytes_[pos] & 0xFF);
            return 1;
        }
        int node = this.bytes_[pos++] & 0xFF;
        if (node >= 32) {
            if ((node & 0x1) != 0x0) {
                return 0;
            }
            pos = skipValue(pos, node);
            node = (this.bytes_[pos++] & 0xFF);
            assert node < 32;
        }
        if (node < 16) {
            if (node == 0) {
                node = (this.bytes_[pos++] & 0xFF);
            }
            getNextBranchBytes(this.bytes_, pos, ++node, out);
            return node;
        }
        append(out, this.bytes_[pos] & 0xFF);
        return 1;
    }
    
    public Iterator iterator() {
        return new Iterator(this.bytes_, this.pos_, this.remainingMatchLength_, 0);
    }
    
    public Iterator iterator(final int maxStringLength) {
        return new Iterator(this.bytes_, this.pos_, this.remainingMatchLength_, maxStringLength);
    }
    
    public static Iterator iterator(final byte[] trieBytes, final int offset, final int maxStringLength) {
        return new Iterator(trieBytes, offset, -1, maxStringLength);
    }
    
    private void stop() {
        this.pos_ = -1;
    }
    
    private static int readValue(final byte[] bytes, final int pos, final int leadByte) {
        int value;
        if (leadByte < 81) {
            value = leadByte - 16;
        }
        else if (leadByte < 108) {
            value = (leadByte - 81 << 8 | (bytes[pos] & 0xFF));
        }
        else if (leadByte < 126) {
            value = (leadByte - 108 << 16 | (bytes[pos] & 0xFF) << 8 | (bytes[pos + 1] & 0xFF));
        }
        else if (leadByte == 126) {
            value = ((bytes[pos] & 0xFF) << 16 | (bytes[pos + 1] & 0xFF) << 8 | (bytes[pos + 2] & 0xFF));
        }
        else {
            value = (bytes[pos] << 24 | (bytes[pos + 1] & 0xFF) << 16 | (bytes[pos + 2] & 0xFF) << 8 | (bytes[pos + 3] & 0xFF));
        }
        return value;
    }
    
    private static int skipValue(int pos, final int leadByte) {
        assert leadByte >= 32;
        if (leadByte >= 162) {
            if (leadByte < 216) {
                ++pos;
            }
            else if (leadByte < 252) {
                pos += 2;
            }
            else {
                pos += 3 + (leadByte >> 1 & 0x1);
            }
        }
        return pos;
    }
    
    private static int skipValue(final byte[] bytes, int pos) {
        final int leadByte = bytes[pos++] & 0xFF;
        return skipValue(pos, leadByte);
    }
    
    private static int jumpByDelta(final byte[] bytes, int pos) {
        int delta = bytes[pos++] & 0xFF;
        if (delta >= 192) {
            if (delta < 240) {
                delta = (delta - 192 << 8 | (bytes[pos++] & 0xFF));
            }
            else if (delta < 254) {
                delta = (delta - 240 << 16 | (bytes[pos] & 0xFF) << 8 | (bytes[pos + 1] & 0xFF));
                pos += 2;
            }
            else if (delta == 254) {
                delta = ((bytes[pos] & 0xFF) << 16 | (bytes[pos + 1] & 0xFF) << 8 | (bytes[pos + 2] & 0xFF));
                pos += 3;
            }
            else {
                delta = (bytes[pos] << 24 | (bytes[pos + 1] & 0xFF) << 16 | (bytes[pos + 2] & 0xFF) << 8 | (bytes[pos + 3] & 0xFF));
                pos += 4;
            }
        }
        return pos + delta;
    }
    
    private static int skipDelta(final byte[] bytes, int pos) {
        final int delta = bytes[pos++] & 0xFF;
        if (delta >= 192) {
            if (delta < 240) {
                ++pos;
            }
            else if (delta < 254) {
                pos += 2;
            }
            else {
                pos += 3 + (delta & 0x1);
            }
        }
        return pos;
    }
    
    private Result branchNext(int pos, int length, final int inByte) {
        if (length == 0) {
            length = (this.bytes_[pos++] & 0xFF);
        }
        ++length;
        while (length > 5) {
            if (inByte < (this.bytes_[pos++] & 0xFF)) {
                length >>= 1;
                pos = jumpByDelta(this.bytes_, pos);
            }
            else {
                length -= length >> 1;
                pos = skipDelta(this.bytes_, pos);
            }
        }
        while (inByte != (this.bytes_[pos++] & 0xFF)) {
            --length;
            pos = skipValue(this.bytes_, pos);
            if (length <= 1) {
                if (inByte == (this.bytes_[pos++] & 0xFF)) {
                    this.pos_ = pos;
                    final int node = this.bytes_[pos] & 0xFF;
                    return (node >= 32) ? BytesTrie.valueResults_[node & 0x1] : Result.NO_VALUE;
                }
                this.stop();
                return Result.NO_MATCH;
            }
        }
        int node2 = this.bytes_[pos] & 0xFF;
        assert node2 >= 32;
        Result result;
        if ((node2 & 0x1) != 0x0) {
            result = Result.FINAL_VALUE;
        }
        else {
            ++pos;
            node2 >>= 1;
            int delta;
            if (node2 < 81) {
                delta = node2 - 16;
            }
            else if (node2 < 108) {
                delta = (node2 - 81 << 8 | (this.bytes_[pos++] & 0xFF));
            }
            else if (node2 < 126) {
                delta = (node2 - 108 << 16 | (this.bytes_[pos] & 0xFF) << 8 | (this.bytes_[pos + 1] & 0xFF));
                pos += 2;
            }
            else if (node2 == 126) {
                delta = ((this.bytes_[pos] & 0xFF) << 16 | (this.bytes_[pos + 1] & 0xFF) << 8 | (this.bytes_[pos + 2] & 0xFF));
                pos += 3;
            }
            else {
                delta = (this.bytes_[pos] << 24 | (this.bytes_[pos + 1] & 0xFF) << 16 | (this.bytes_[pos + 2] & 0xFF) << 8 | (this.bytes_[pos + 3] & 0xFF));
                pos += 4;
            }
            pos += delta;
            node2 = (this.bytes_[pos] & 0xFF);
            result = ((node2 >= 32) ? BytesTrie.valueResults_[node2 & 0x1] : Result.NO_VALUE);
        }
        this.pos_ = pos;
        return result;
    }
    
    private Result nextImpl(int pos, final int inByte) {
        while (true) {
            int node = this.bytes_[pos++] & 0xFF;
            if (node < 16) {
                return this.branchNext(pos, node, inByte);
            }
            if (node < 32) {
                int length = node - 16;
                if (inByte == (this.bytes_[pos++] & 0xFF)) {
                    this.remainingMatchLength_ = --length;
                    this.pos_ = pos;
                    return (length < 0 && (node = (this.bytes_[pos] & 0xFF)) >= 32) ? BytesTrie.valueResults_[node & 0x1] : Result.NO_VALUE;
                }
                break;
            }
            else {
                if ((node & 0x1) != 0x0) {
                    break;
                }
                pos = skipValue(pos, node);
                assert (this.bytes_[pos] & 0xFF) < 32;
                continue;
            }
        }
        this.stop();
        return Result.NO_MATCH;
    }
    
    private static long findUniqueValueFromBranch(final byte[] bytes, int pos, int length, long uniqueValue) {
        while (length > 5) {
            ++pos;
            uniqueValue = findUniqueValueFromBranch(bytes, jumpByDelta(bytes, pos), length >> 1, uniqueValue);
            if (uniqueValue == 0L) {
                return 0L;
            }
            length -= length >> 1;
            pos = skipDelta(bytes, pos);
        }
        do {
            ++pos;
            final int node = bytes[pos++] & 0xFF;
            final boolean isFinal = (node & 0x1) != 0x0;
            final int value = readValue(bytes, pos, node >> 1);
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
                uniqueValue = findUniqueValue(bytes, pos + value, uniqueValue);
                if (uniqueValue == 0L) {
                    return 0L;
                }
                continue;
            }
        } while (--length > 1);
        return (long)(pos + 1) << 33 | (uniqueValue & 0x1FFFFFFFFL);
    }
    
    private static long findUniqueValue(final byte[] bytes, int pos, long uniqueValue) {
        while (true) {
            int node = bytes[pos++] & 0xFF;
            if (node < 16) {
                if (node == 0) {
                    node = (bytes[pos++] & 0xFF);
                }
                uniqueValue = findUniqueValueFromBranch(bytes, pos, node + 1, uniqueValue);
                if (uniqueValue == 0L) {
                    return 0L;
                }
                pos = (int)(uniqueValue >>> 33);
            }
            else if (node < 32) {
                pos += node - 16 + 1;
            }
            else {
                final boolean isFinal = (node & 0x1) != 0x0;
                final int value = readValue(bytes, pos, node >> 1);
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
                pos = skipValue(pos, node);
            }
        }
    }
    
    private static void getNextBranchBytes(final byte[] bytes, int pos, int length, final Appendable out) {
        while (length > 5) {
            ++pos;
            getNextBranchBytes(bytes, jumpByDelta(bytes, pos), length >> 1, out);
            length -= length >> 1;
            pos = skipDelta(bytes, pos);
        }
        do {
            append(out, bytes[pos++] & 0xFF);
            pos = skipValue(bytes, pos);
        } while (--length > 1);
        append(out, bytes[pos] & 0xFF);
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
        BytesTrie.valueResults_ = new Result[] { Result.INTERMEDIATE_VALUE, Result.FINAL_VALUE };
    }
    
    public static final class State
    {
        private byte[] bytes;
        private int root;
        private int pos;
        private int remainingMatchLength;
    }
    
    public enum Result
    {
        NO_MATCH, 
        NO_VALUE, 
        FINAL_VALUE, 
        INTERMEDIATE_VALUE;
        
        public boolean matches() {
            return this != Result.NO_MATCH;
        }
        
        public boolean hasValue() {
            return this.ordinal() >= 2;
        }
        
        public boolean hasNext() {
            return (this.ordinal() & 0x1) != 0x0;
        }
    }
    
    public static final class Entry
    {
        public int value;
        private byte[] bytes;
        private int length;
        
        private Entry(final int capacity) {
            this.bytes = new byte[capacity];
        }
        
        public int bytesLength() {
            return this.length;
        }
        
        public byte byteAt(final int index) {
            return this.bytes[index];
        }
        
        public void copyBytesTo(final byte[] dest, final int destOffset) {
            System.arraycopy(this.bytes, 0, dest, destOffset, this.length);
        }
        
        public ByteBuffer bytesAsByteBuffer() {
            return ByteBuffer.wrap(this.bytes, 0, this.length).asReadOnlyBuffer();
        }
        
        private void ensureCapacity(final int len) {
            if (this.bytes.length < len) {
                final byte[] newBytes = new byte[Math.min(2 * this.bytes.length, 2 * len)];
                System.arraycopy(this.bytes, 0, newBytes, 0, this.length);
                this.bytes = newBytes;
            }
        }
        
        private void append(final byte b) {
            this.ensureCapacity(this.length + 1);
            this.bytes[this.length++] = b;
        }
        
        private void append(final byte[] b, final int off, final int len) {
            this.ensureCapacity(this.length + len);
            System.arraycopy(b, off, this.bytes, this.length, len);
            this.length += len;
        }
        
        private void truncateString(final int newLength) {
            this.length = newLength;
        }
    }
    
    public static final class Iterator implements java.util.Iterator<Entry>
    {
        private byte[] bytes_;
        private int pos_;
        private int initialPos_;
        private int remainingMatchLength_;
        private int initialRemainingMatchLength_;
        private int maxLength_;
        private Entry entry_;
        private ArrayList<Long> stack_;
        
        private Iterator(final byte[] trieBytes, final int offset, final int remainingMatchLength, final int maxStringLength) {
            this.stack_ = new ArrayList<Long>();
            this.bytes_ = trieBytes;
            this.initialPos_ = offset;
            this.pos_ = offset;
            this.initialRemainingMatchLength_ = remainingMatchLength;
            this.remainingMatchLength_ = remainingMatchLength;
            this.maxLength_ = maxStringLength;
            this.entry_ = new Entry((this.maxLength_ != 0) ? this.maxLength_ : 32);
            int length = this.remainingMatchLength_;
            if (length >= 0) {
                ++length;
                if (this.maxLength_ > 0 && length > this.maxLength_) {
                    length = this.maxLength_;
                }
                this.entry_.append(this.bytes_, this.pos_, length);
                this.pos_ += length;
                this.remainingMatchLength_ -= length;
            }
        }
        
        public Iterator reset() {
            this.pos_ = this.initialPos_;
            this.remainingMatchLength_ = this.initialRemainingMatchLength_;
            int length = this.remainingMatchLength_ + 1;
            if (this.maxLength_ > 0 && length > this.maxLength_) {
                length = this.maxLength_;
            }
            this.entry_.truncateString(length);
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
                this.entry_.truncateString(length & 0xFFFF);
                length >>>= 16;
                if (length > 1) {
                    pos = this.branchNext(pos, length);
                    if (pos < 0) {
                        return this.entry_;
                    }
                }
                else {
                    this.entry_.append(this.bytes_[pos++]);
                }
            }
            if (this.remainingMatchLength_ >= 0) {
                return this.truncateAndStop();
            }
            while (true) {
                int node = this.bytes_[pos++] & 0xFF;
                if (node >= 32) {
                    final boolean isFinal = (node & 0x1) != 0x0;
                    this.entry_.value = readValue(this.bytes_, pos, node >> 1);
                    if (isFinal || (this.maxLength_ > 0 && this.entry_.length == this.maxLength_)) {
                        this.pos_ = -1;
                    }
                    else {
                        this.pos_ = skipValue(pos, node);
                    }
                    return this.entry_;
                }
                if (this.maxLength_ > 0 && this.entry_.length == this.maxLength_) {
                    return this.truncateAndStop();
                }
                if (node < 16) {
                    if (node == 0) {
                        node = (this.bytes_[pos++] & 0xFF);
                    }
                    pos = this.branchNext(pos, node + 1);
                    if (pos < 0) {
                        return this.entry_;
                    }
                    continue;
                }
                else {
                    final int length2 = node - 16 + 1;
                    if (this.maxLength_ > 0 && this.entry_.length + length2 > this.maxLength_) {
                        this.entry_.append(this.bytes_, pos, this.maxLength_ - this.entry_.length);
                        return this.truncateAndStop();
                    }
                    this.entry_.append(this.bytes_, pos, length2);
                    pos += length2;
                }
            }
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
        private Entry truncateAndStop() {
            this.pos_ = -1;
            this.entry_.value = -1;
            return this.entry_;
        }
        
        private int branchNext(int pos, int length) {
            while (length > 5) {
                ++pos;
                this.stack_.add((long)skipDelta(this.bytes_, pos) << 32 | (long)(length - (length >> 1) << 16) | (long)this.entry_.length);
                length >>= 1;
                pos = jumpByDelta(this.bytes_, pos);
            }
            final byte trieByte = this.bytes_[pos++];
            final int node = this.bytes_[pos++] & 0xFF;
            final boolean isFinal = (node & 0x1) != 0x0;
            final int value = readValue(this.bytes_, pos, node >> 1);
            pos = skipValue(pos, node);
            this.stack_.add((long)pos << 32 | (long)(length - 1 << 16) | (long)this.entry_.length);
            this.entry_.append(trieByte);
            if (isFinal) {
                this.pos_ = -1;
                this.entry_.value = value;
                return -1;
            }
            return pos + value;
        }
    }
}
