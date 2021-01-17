// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.text;

import java.io.IOException;
import java.io.InputStream;
import com.ibm.icu.impl.Assert;
import java.io.DataInputStream;
import com.ibm.icu.impl.ICUBinary;
import com.ibm.icu.impl.ICUData;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.impl.ICUResourceBundle;

final class DictionaryData
{
    public static final int TRIE_TYPE_BYTES = 0;
    public static final int TRIE_TYPE_UCHARS = 1;
    public static final int TRIE_TYPE_MASK = 7;
    public static final int TRIE_HAS_VALUES = 8;
    public static final int TRANSFORM_NONE = 0;
    public static final int TRANSFORM_TYPE_OFFSET = 16777216;
    public static final int TRANSFORM_TYPE_MASK = 2130706432;
    public static final int TRANSFORM_OFFSET_MASK = 2097151;
    public static final int IX_STRING_TRIE_OFFSET = 0;
    public static final int IX_RESERVED1_OFFSET = 1;
    public static final int IX_RESERVED2_OFFSET = 2;
    public static final int IX_TOTAL_SIZE = 3;
    public static final int IX_TRIE_TYPE = 4;
    public static final int IX_TRANSFORM = 5;
    public static final int IX_RESERVED6 = 6;
    public static final int IX_RESERVED7 = 7;
    public static final int IX_COUNT = 8;
    private static final byte[] DATA_FORMAT_ID;
    
    private DictionaryData() {
    }
    
    public static DictionaryMatcher loadDictionaryFor(final String dictType) throws IOException {
        final ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b/brkitr");
        String dictFileName = rb.getStringWithFallback("dictionaries/" + dictType);
        dictFileName = "data/icudt51b/brkitr/" + dictFileName;
        final InputStream is = ICUData.getStream(dictFileName);
        ICUBinary.readHeader(is, DictionaryData.DATA_FORMAT_ID, null);
        final DataInputStream s = new DataInputStream(is);
        final int[] indexes = new int[8];
        for (int i = 0; i < 8; ++i) {
            indexes[i] = s.readInt();
        }
        final int offset = indexes[0];
        Assert.assrt(offset >= 32);
        if (offset > 32) {
            final int diff = offset - 32;
            s.skipBytes(diff);
        }
        final int trieType = indexes[4] & 0x7;
        final int totalSize = indexes[3] - offset;
        DictionaryMatcher m = null;
        if (trieType == 0) {
            final int transform = indexes[5];
            byte[] data;
            int j;
            for (data = new byte[totalSize], j = 0; j < data.length; ++j) {
                data[j] = s.readByte();
            }
            Assert.assrt(j == totalSize);
            m = new BytesDictionaryMatcher(data, transform);
        }
        else if (trieType == 1) {
            Assert.assrt(totalSize % 2 == 0);
            final int num = totalSize / 2;
            final char[] data2 = new char[totalSize / 2];
            for (int j = 0; j < num; ++j) {
                data2[j] = s.readChar();
            }
            m = new CharsDictionaryMatcher(new String(data2));
        }
        else {
            m = null;
        }
        s.close();
        is.close();
        return m;
    }
    
    static {
        DATA_FORMAT_ID = new byte[] { 68, 105, 99, 116 };
    }
}
