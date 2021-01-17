// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.util.Iterator;
import com.ibm.icu.text.UnicodeSet;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;

public final class UBiDiProps
{
    private int[] indexes;
    private int[] mirrors;
    private byte[] jgArray;
    private Trie2_16 trie;
    private static final String DATA_NAME = "ubidi";
    private static final String DATA_TYPE = "icu";
    private static final String DATA_FILE_NAME = "ubidi.icu";
    private static final byte[] FMT;
    private static final int IX_TRIE_SIZE = 2;
    private static final int IX_MIRROR_LENGTH = 3;
    private static final int IX_JG_START = 4;
    private static final int IX_JG_LIMIT = 5;
    private static final int IX_MAX_VALUES = 15;
    private static final int IX_TOP = 16;
    private static final int JT_SHIFT = 5;
    private static final int JOIN_CONTROL_SHIFT = 10;
    private static final int BIDI_CONTROL_SHIFT = 11;
    private static final int IS_MIRRORED_SHIFT = 12;
    private static final int MIRROR_DELTA_SHIFT = 13;
    private static final int MAX_JG_SHIFT = 16;
    private static final int CLASS_MASK = 31;
    private static final int JT_MASK = 224;
    private static final int MAX_JG_MASK = 16711680;
    private static final int ESC_MIRROR_DELTA = -4;
    private static final int MIRROR_INDEX_SHIFT = 21;
    public static final UBiDiProps INSTANCE;
    
    private UBiDiProps() throws IOException {
        final InputStream is = ICUData.getStream("data/icudt51b/ubidi.icu");
        final BufferedInputStream b = new BufferedInputStream(is, 4096);
        this.readData(b);
        b.close();
        is.close();
    }
    
    private void readData(final InputStream is) throws IOException {
        final DataInputStream inputStream = new DataInputStream(is);
        ICUBinary.readHeader(inputStream, UBiDiProps.FMT, new IsAcceptable());
        int count = inputStream.readInt();
        if (count < 16) {
            throw new IOException("indexes[0] too small in ubidi.icu");
        }
        (this.indexes = new int[count])[0] = count;
        for (int i = 1; i < count; ++i) {
            this.indexes[i] = inputStream.readInt();
        }
        this.trie = Trie2_16.createFromSerialized(inputStream);
        final int expectedTrieLength = this.indexes[2];
        final int trieLength = this.trie.getSerializedLength();
        if (trieLength > expectedTrieLength) {
            throw new IOException("ubidi.icu: not enough bytes for the trie");
        }
        inputStream.skipBytes(expectedTrieLength - trieLength);
        count = this.indexes[3];
        if (count > 0) {
            this.mirrors = new int[count];
            for (int i = 0; i < count; ++i) {
                this.mirrors[i] = inputStream.readInt();
            }
        }
        count = this.indexes[5] - this.indexes[4];
        this.jgArray = new byte[count];
        for (int i = 0; i < count; ++i) {
            this.jgArray[i] = inputStream.readByte();
        }
    }
    
    public final void addPropertyStarts(final UnicodeSet set) {
        final Iterator<Trie2.Range> trieIterator = this.trie.iterator();
        Trie2.Range range;
        while (trieIterator.hasNext() && !(range = trieIterator.next()).leadSurrogate) {
            set.add(range.startCodePoint);
        }
        for (int length = this.indexes[3], i = 0; i < length; ++i) {
            final int c = getMirrorCodePoint(this.mirrors[i]);
            set.add(c, c + 1);
        }
        int start = this.indexes[4];
        final int limit = this.indexes[5];
        final int length = limit - start;
        byte prev = 0;
        for (int i = 0; i < length; ++i) {
            final byte jg = this.jgArray[i];
            if (jg != prev) {
                set.add(start);
                prev = jg;
            }
            ++start;
        }
        if (prev != 0) {
            set.add(limit);
        }
    }
    
    public final int getMaxValue(final int which) {
        final int max = this.indexes[15];
        switch (which) {
            case 4096: {
                return max & 0x1F;
            }
            case 4102: {
                return (max & 0xFF0000) >> 16;
            }
            case 4103: {
                return (max & 0xE0) >> 5;
            }
            default: {
                return -1;
            }
        }
    }
    
    public final int getClass(final int c) {
        return getClassFromProps(this.trie.get(c));
    }
    
    public final boolean isMirrored(final int c) {
        return getFlagFromProps(this.trie.get(c), 12);
    }
    
    public final int getMirror(final int c) {
        final int props = this.trie.get(c);
        final int delta = (short)props >> 13;
        if (delta != -4) {
            return c + delta;
        }
        for (int length = this.indexes[3], i = 0; i < length; ++i) {
            final int m = this.mirrors[i];
            final int c2 = getMirrorCodePoint(m);
            if (c == c2) {
                return getMirrorCodePoint(this.mirrors[getMirrorIndex(m)]);
            }
            if (c < c2) {
                break;
            }
        }
        return c;
    }
    
    public final boolean isBidiControl(final int c) {
        return getFlagFromProps(this.trie.get(c), 11);
    }
    
    public final boolean isJoinControl(final int c) {
        return getFlagFromProps(this.trie.get(c), 10);
    }
    
    public final int getJoiningType(final int c) {
        return (this.trie.get(c) & 0xE0) >> 5;
    }
    
    public final int getJoiningGroup(final int c) {
        final int start = this.indexes[4];
        final int limit = this.indexes[5];
        if (start <= c && c < limit) {
            return this.jgArray[c - start] & 0xFF;
        }
        return 0;
    }
    
    private static final int getClassFromProps(final int props) {
        return props & 0x1F;
    }
    
    private static final boolean getFlagFromProps(final int props, final int shift) {
        return (props >> shift & 0x1) != 0x0;
    }
    
    private static final int getMirrorCodePoint(final int m) {
        return m & 0x1FFFFF;
    }
    
    private static final int getMirrorIndex(final int m) {
        return m >>> 21;
    }
    
    static {
        FMT = new byte[] { 66, 105, 68, 105 };
        try {
            INSTANCE = new UBiDiProps();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private static final class IsAcceptable implements ICUBinary.Authenticate
    {
        public boolean isDataVersionAcceptable(final byte[] version) {
            return version[0] == 2;
        }
    }
}
