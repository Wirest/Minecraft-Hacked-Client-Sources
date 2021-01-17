// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.util.NoSuchElementException;
import java.io.DataOutputStream;
import java.util.Iterator;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.InputStream;

public abstract class Trie2 implements Iterable<Range>
{
    private static ValueMapper defaultValueMapper;
    UTrie2Header header;
    char[] index;
    int data16;
    int[] data32;
    int indexLength;
    int dataLength;
    int index2NullOffset;
    int initialValue;
    int errorValue;
    int highStart;
    int highValueIndex;
    int dataNullOffset;
    int fHash;
    static final int UTRIE2_OPTIONS_VALUE_BITS_MASK = 15;
    static final int UTRIE2_SHIFT_1 = 11;
    static final int UTRIE2_SHIFT_2 = 5;
    static final int UTRIE2_SHIFT_1_2 = 6;
    static final int UTRIE2_OMITTED_BMP_INDEX_1_LENGTH = 32;
    static final int UTRIE2_CP_PER_INDEX_1_ENTRY = 2048;
    static final int UTRIE2_INDEX_2_BLOCK_LENGTH = 64;
    static final int UTRIE2_INDEX_2_MASK = 63;
    static final int UTRIE2_DATA_BLOCK_LENGTH = 32;
    static final int UTRIE2_DATA_MASK = 31;
    static final int UTRIE2_INDEX_SHIFT = 2;
    static final int UTRIE2_DATA_GRANULARITY = 4;
    static final int UTRIE2_INDEX_2_OFFSET = 0;
    static final int UTRIE2_LSCP_INDEX_2_OFFSET = 2048;
    static final int UTRIE2_LSCP_INDEX_2_LENGTH = 32;
    static final int UTRIE2_INDEX_2_BMP_LENGTH = 2080;
    static final int UTRIE2_UTF8_2B_INDEX_2_OFFSET = 2080;
    static final int UTRIE2_UTF8_2B_INDEX_2_LENGTH = 32;
    static final int UTRIE2_INDEX_1_OFFSET = 2112;
    static final int UTRIE2_MAX_INDEX_1_LENGTH = 512;
    static final int UTRIE2_BAD_UTF8_DATA_OFFSET = 128;
    static final int UTRIE2_DATA_START_OFFSET = 192;
    static final int UNEWTRIE2_INDEX_GAP_OFFSET = 2080;
    static final int UNEWTRIE2_INDEX_GAP_LENGTH = 576;
    static final int UNEWTRIE2_MAX_INDEX_2_LENGTH = 35488;
    static final int UNEWTRIE2_INDEX_1_LENGTH = 544;
    static final int UNEWTRIE2_MAX_DATA_LENGTH = 1115264;
    
    public static Trie2 createFromSerialized(final InputStream is) throws IOException {
        final DataInputStream dis = new DataInputStream(is);
        boolean needByteSwap = false;
        final UTrie2Header header = new UTrie2Header();
        switch (header.signature = dis.readInt()) {
            case 1416784178: {
                needByteSwap = false;
                break;
            }
            case 845771348: {
                needByteSwap = true;
                header.signature = Integer.reverseBytes(header.signature);
                break;
            }
            default: {
                throw new IllegalArgumentException("Stream does not contain a serialized UTrie2");
            }
        }
        header.options = swapShort(needByteSwap, dis.readUnsignedShort());
        header.indexLength = swapShort(needByteSwap, dis.readUnsignedShort());
        header.shiftedDataLength = swapShort(needByteSwap, dis.readUnsignedShort());
        header.index2NullOffset = swapShort(needByteSwap, dis.readUnsignedShort());
        header.dataNullOffset = swapShort(needByteSwap, dis.readUnsignedShort());
        header.shiftedHighStart = swapShort(needByteSwap, dis.readUnsignedShort());
        if ((header.options & 0xF) > 1) {
            throw new IllegalArgumentException("UTrie2 serialized format error.");
        }
        ValueWidth width;
        Trie2 This;
        if ((header.options & 0xF) == 0x0) {
            width = ValueWidth.BITS_16;
            This = new Trie2_16();
        }
        else {
            width = ValueWidth.BITS_32;
            This = new Trie2_32();
        }
        This.header = header;
        This.indexLength = header.indexLength;
        This.dataLength = header.shiftedDataLength << 2;
        This.index2NullOffset = header.index2NullOffset;
        This.dataNullOffset = header.dataNullOffset;
        This.highStart = header.shiftedHighStart << 11;
        This.highValueIndex = This.dataLength - 4;
        if (width == ValueWidth.BITS_16) {
            final Trie2 trie2 = This;
            trie2.highValueIndex += This.indexLength;
        }
        int indexArraySize = This.indexLength;
        if (width == ValueWidth.BITS_16) {
            indexArraySize += This.dataLength;
        }
        This.index = new char[indexArraySize];
        for (int i = 0; i < This.indexLength; ++i) {
            This.index[i] = swapChar(needByteSwap, dis.readChar());
        }
        if (width == ValueWidth.BITS_16) {
            This.data16 = This.indexLength;
            for (int i = 0; i < This.dataLength; ++i) {
                This.index[This.data16 + i] = swapChar(needByteSwap, dis.readChar());
            }
        }
        else {
            This.data32 = new int[This.dataLength];
            for (int i = 0; i < This.dataLength; ++i) {
                This.data32[i] = swapInt(needByteSwap, dis.readInt());
            }
        }
        switch (width) {
            case BITS_16: {
                This.data32 = null;
                This.initialValue = This.index[This.dataNullOffset];
                This.errorValue = This.index[This.data16 + 128];
                break;
            }
            case BITS_32: {
                This.data16 = 0;
                This.initialValue = This.data32[This.dataNullOffset];
                This.errorValue = This.data32[128];
                break;
            }
            default: {
                throw new IllegalArgumentException("UTrie2 serialized format error.");
            }
        }
        return This;
    }
    
    private static int swapShort(final boolean needSwap, final int value) {
        return needSwap ? (Short.reverseBytes((short)value) & 0xFFFF) : value;
    }
    
    private static char swapChar(final boolean needSwap, final char value) {
        return needSwap ? ((char)Short.reverseBytes((short)value)) : value;
    }
    
    private static int swapInt(final boolean needSwap, final int value) {
        return needSwap ? Integer.reverseBytes(value) : value;
    }
    
    public static int getVersion(final InputStream is, final boolean littleEndianOk) throws IOException {
        if (!is.markSupported()) {
            throw new IllegalArgumentException("Input stream must support mark().");
        }
        is.mark(4);
        final byte[] sig = new byte[4];
        final int read = is.read(sig);
        is.reset();
        if (read != sig.length) {
            return 0;
        }
        if (sig[0] == 84 && sig[1] == 114 && sig[2] == 105 && sig[3] == 101) {
            return 1;
        }
        if (sig[0] == 84 && sig[1] == 114 && sig[2] == 105 && sig[3] == 50) {
            return 2;
        }
        if (littleEndianOk) {
            if (sig[0] == 101 && sig[1] == 105 && sig[2] == 114 && sig[3] == 84) {
                return 1;
            }
            if (sig[0] == 50 && sig[1] == 105 && sig[2] == 114 && sig[3] == 84) {
                return 2;
            }
        }
        return 0;
    }
    
    public abstract int get(final int p0);
    
    public abstract int getFromU16SingleLead(final char p0);
    
    @Override
    public final boolean equals(final Object other) {
        if (!(other instanceof Trie2)) {
            return false;
        }
        final Trie2 OtherTrie = (Trie2)other;
        final Iterator<Range> otherIter = OtherTrie.iterator();
        for (final Range rangeFromThis : this) {
            if (!otherIter.hasNext()) {
                return false;
            }
            final Range rangeFromOther = otherIter.next();
            if (!rangeFromThis.equals(rangeFromOther)) {
                return false;
            }
        }
        return !otherIter.hasNext() && this.errorValue == OtherTrie.errorValue && this.initialValue == OtherTrie.initialValue;
    }
    
    @Override
    public int hashCode() {
        if (this.fHash == 0) {
            int hash = initHash();
            for (final Range r : this) {
                hash = hashInt(hash, r.hashCode());
            }
            if (hash == 0) {
                hash = 1;
            }
            this.fHash = hash;
        }
        return this.fHash;
    }
    
    public Iterator<Range> iterator() {
        return this.iterator(Trie2.defaultValueMapper);
    }
    
    public Iterator<Range> iterator(final ValueMapper mapper) {
        return new Trie2Iterator(mapper);
    }
    
    public Iterator<Range> iteratorForLeadSurrogate(final char lead, final ValueMapper mapper) {
        return new Trie2Iterator(lead, mapper);
    }
    
    public Iterator<Range> iteratorForLeadSurrogate(final char lead) {
        return new Trie2Iterator(lead, Trie2.defaultValueMapper);
    }
    
    protected int serializeHeader(final DataOutputStream dos) throws IOException {
        int bytesWritten = 0;
        dos.writeInt(this.header.signature);
        dos.writeShort(this.header.options);
        dos.writeShort(this.header.indexLength);
        dos.writeShort(this.header.shiftedDataLength);
        dos.writeShort(this.header.index2NullOffset);
        dos.writeShort(this.header.dataNullOffset);
        dos.writeShort(this.header.shiftedHighStart);
        bytesWritten += 16;
        for (int i = 0; i < this.header.indexLength; ++i) {
            dos.writeChar(this.index[i]);
        }
        bytesWritten += this.header.indexLength;
        return bytesWritten;
    }
    
    public CharSequenceIterator charSequenceIterator(final CharSequence text, final int index) {
        return new CharSequenceIterator(text, index);
    }
    
    int rangeEnd(final int start, final int limitp, final int val) {
        int limit;
        int c;
        for (limit = Math.min(this.highStart, limitp), c = start + 1; c < limit && this.get(c) == val; ++c) {}
        if (c >= this.highStart) {
            c = limitp;
        }
        return c - 1;
    }
    
    private static int initHash() {
        return -2128831035;
    }
    
    private static int hashByte(int h, final int b) {
        h *= 16777619;
        h ^= b;
        return h;
    }
    
    private static int hashUChar32(int h, final int c) {
        h = hashByte(h, c & 0xFF);
        h = hashByte(h, c >> 8 & 0xFF);
        h = hashByte(h, c >> 16);
        return h;
    }
    
    private static int hashInt(int h, final int i) {
        h = hashByte(h, i & 0xFF);
        h = hashByte(h, i >> 8 & 0xFF);
        h = hashByte(h, i >> 16 & 0xFF);
        h = hashByte(h, i >> 24 & 0xFF);
        return h;
    }
    
    static {
        Trie2.defaultValueMapper = new ValueMapper() {
            public int map(final int in) {
                return in;
            }
        };
    }
    
    public static class Range
    {
        public int startCodePoint;
        public int endCodePoint;
        public int value;
        public boolean leadSurrogate;
        
        @Override
        public boolean equals(final Object other) {
            if (other == null || !other.getClass().equals(this.getClass())) {
                return false;
            }
            final Range tother = (Range)other;
            return this.startCodePoint == tother.startCodePoint && this.endCodePoint == tother.endCodePoint && this.value == tother.value && this.leadSurrogate == tother.leadSurrogate;
        }
        
        @Override
        public int hashCode() {
            int h = initHash();
            h = hashUChar32(h, this.startCodePoint);
            h = hashUChar32(h, this.endCodePoint);
            h = hashInt(h, this.value);
            h = hashByte(h, this.leadSurrogate ? 1 : 0);
            return h;
        }
    }
    
    public static class CharSequenceValues
    {
        public int index;
        public int codePoint;
        public int value;
    }
    
    public class CharSequenceIterator implements Iterator<CharSequenceValues>
    {
        private CharSequence text;
        private int textLength;
        private int index;
        private CharSequenceValues fResults;
        
        CharSequenceIterator(final CharSequence t, final int index) {
            this.fResults = new CharSequenceValues();
            this.text = t;
            this.textLength = this.text.length();
            this.set(index);
        }
        
        public void set(final int i) {
            if (i < 0 || i > this.textLength) {
                throw new IndexOutOfBoundsException();
            }
            this.index = i;
        }
        
        public final boolean hasNext() {
            return this.index < this.textLength;
        }
        
        public final boolean hasPrevious() {
            return this.index > 0;
        }
        
        public CharSequenceValues next() {
            final int c = Character.codePointAt(this.text, this.index);
            final int val = Trie2.this.get(c);
            this.fResults.index = this.index;
            this.fResults.codePoint = c;
            this.fResults.value = val;
            ++this.index;
            if (c >= 65536) {
                ++this.index;
            }
            return this.fResults;
        }
        
        public CharSequenceValues previous() {
            final int c = Character.codePointBefore(this.text, this.index);
            final int val = Trie2.this.get(c);
            --this.index;
            if (c >= 65536) {
                --this.index;
            }
            this.fResults.index = this.index;
            this.fResults.codePoint = c;
            this.fResults.value = val;
            return this.fResults;
        }
        
        public void remove() {
            throw new UnsupportedOperationException("Trie2.CharSequenceIterator does not support remove().");
        }
    }
    
    enum ValueWidth
    {
        BITS_16, 
        BITS_32;
    }
    
    static class UTrie2Header
    {
        int signature;
        int options;
        int indexLength;
        int shiftedDataLength;
        int index2NullOffset;
        int dataNullOffset;
        int shiftedHighStart;
    }
    
    class Trie2Iterator implements Iterator<Range>
    {
        private ValueMapper mapper;
        private Range returnValue;
        private int nextStart;
        private int limitCP;
        private boolean doingCodePoints;
        private boolean doLeadSurrogates;
        
        Trie2Iterator(final ValueMapper vm) {
            this.returnValue = new Range();
            this.doingCodePoints = true;
            this.doLeadSurrogates = true;
            this.mapper = vm;
            this.nextStart = 0;
            this.limitCP = 1114112;
            this.doLeadSurrogates = true;
        }
        
        Trie2Iterator(final char leadSurrogate, final ValueMapper vm) {
            this.returnValue = new Range();
            this.doingCodePoints = true;
            this.doLeadSurrogates = true;
            if (leadSurrogate < '\ud800' || leadSurrogate > '\udbff') {
                throw new IllegalArgumentException("Bad lead surrogate value.");
            }
            this.mapper = vm;
            this.nextStart = leadSurrogate - '\ud7c0' << 10;
            this.limitCP = this.nextStart + 1024;
            this.doLeadSurrogates = false;
        }
        
        public Range next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            if (this.nextStart >= this.limitCP) {
                this.doingCodePoints = false;
                this.nextStart = 55296;
            }
            int endOfRange = 0;
            int val = 0;
            int mappedVal = 0;
            if (this.doingCodePoints) {
                val = Trie2.this.get(this.nextStart);
                mappedVal = this.mapper.map(val);
                for (endOfRange = Trie2.this.rangeEnd(this.nextStart, this.limitCP, val); endOfRange < this.limitCP - 1; endOfRange = Trie2.this.rangeEnd(endOfRange + 1, this.limitCP, val)) {
                    val = Trie2.this.get(endOfRange + 1);
                    if (this.mapper.map(val) != mappedVal) {
                        break;
                    }
                }
            }
            else {
                val = Trie2.this.getFromU16SingleLead((char)this.nextStart);
                mappedVal = this.mapper.map(val);
                for (endOfRange = this.rangeEndLS((char)this.nextStart); endOfRange < 56319; endOfRange = this.rangeEndLS((char)(endOfRange + 1))) {
                    val = Trie2.this.getFromU16SingleLead((char)(endOfRange + 1));
                    if (this.mapper.map(val) != mappedVal) {
                        break;
                    }
                }
            }
            this.returnValue.startCodePoint = this.nextStart;
            this.returnValue.endCodePoint = endOfRange;
            this.returnValue.value = mappedVal;
            this.returnValue.leadSurrogate = !this.doingCodePoints;
            this.nextStart = endOfRange + 1;
            return this.returnValue;
        }
        
        public boolean hasNext() {
            return (this.doingCodePoints && (this.doLeadSurrogates || this.nextStart < this.limitCP)) || this.nextStart < 56320;
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
        private int rangeEndLS(final char startingLS) {
            if (startingLS >= '\udbff') {
                return 56319;
            }
            int val;
            int c;
            for (val = Trie2.this.getFromU16SingleLead(startingLS), c = startingLS + '\u0001'; c <= 56319 && Trie2.this.getFromU16SingleLead((char)c) == val; ++c) {}
            return c - 1;
        }
    }
    
    public interface ValueMapper
    {
        int map(final int p0);
    }
}
