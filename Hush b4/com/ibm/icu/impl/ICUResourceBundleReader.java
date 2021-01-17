// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.VersionInfo;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

public final class ICUResourceBundleReader implements ICUBinary.Authenticate
{
    private static final byte[] DATA_FORMAT_ID;
    private static final int URES_INDEX_LENGTH = 0;
    private static final int URES_INDEX_KEYS_TOP = 1;
    private static final int URES_INDEX_BUNDLE_TOP = 3;
    private static final int URES_INDEX_ATTRIBUTES = 5;
    private static final int URES_INDEX_16BIT_TOP = 6;
    private static final int URES_INDEX_POOL_CHECKSUM = 7;
    private static final int URES_ATT_NO_FALLBACK = 1;
    private static final int URES_ATT_IS_POOL_BUNDLE = 2;
    private static final int URES_ATT_USES_POOL_BUNDLE = 4;
    private static final boolean DEBUG = false;
    private byte[] dataVersion;
    private String s16BitUnits;
    private byte[] poolBundleKeys;
    private String poolBundleKeysAsString;
    private int rootRes;
    private int localKeyLimit;
    private boolean noFallback;
    private boolean isPoolBundle;
    private boolean usesPoolBundle;
    private int[] indexes;
    private byte[] keyStrings;
    private String keyStringsAsString;
    private byte[] resourceBytes;
    private int resourceBottom;
    private static ReaderCache CACHE;
    private static final ICUResourceBundleReader NULL_READER;
    private static byte[] emptyBytes;
    private static ByteBuffer emptyByteBuffer;
    private static char[] emptyChars;
    private static int[] emptyInts;
    private static String emptyString;
    private static final String ICU_RESOURCE_SUFFIX = ".res";
    
    private ICUResourceBundleReader() {
    }
    
    private ICUResourceBundleReader(final InputStream stream, final String baseName, final String localeID, final ClassLoader loader) {
        final BufferedInputStream bs = new BufferedInputStream(stream);
        try {
            this.dataVersion = ICUBinary.readHeader(bs, ICUResourceBundleReader.DATA_FORMAT_ID, this);
            this.readData(bs);
            stream.close();
        }
        catch (IOException ex) {
            final String fullName = getFullName(baseName, localeID);
            throw new RuntimeException("Data file " + fullName + " is corrupt - " + ex.getMessage());
        }
        if (this.usesPoolBundle) {
            final ICUResourceBundleReader poolBundleReader = getReader(baseName, "pool", loader);
            if (!poolBundleReader.isPoolBundle) {
                throw new IllegalStateException("pool.res is not a pool bundle");
            }
            if (poolBundleReader.indexes[7] != this.indexes[7]) {
                throw new IllegalStateException("pool.res has a different checksum than this bundle");
            }
            this.poolBundleKeys = poolBundleReader.keyStrings;
            this.poolBundleKeysAsString = poolBundleReader.keyStringsAsString;
        }
    }
    
    static ICUResourceBundleReader getReader(final String baseName, final String localeID, final ClassLoader root) {
        final ReaderInfo info = new ReaderInfo(baseName, localeID, root);
        final ICUResourceBundleReader reader = ICUResourceBundleReader.CACHE.getInstance(info, info);
        if (reader == ICUResourceBundleReader.NULL_READER) {
            return null;
        }
        return reader;
    }
    
    private void readData(final InputStream stream) throws IOException {
        final DataInputStream ds = new DataInputStream(stream);
        this.rootRes = ds.readInt();
        final int indexes0 = ds.readInt();
        final int indexLength = indexes0 & 0xFF;
        (this.indexes = new int[indexLength])[0] = indexes0;
        for (int i = 1; i < indexLength; ++i) {
            this.indexes[i] = ds.readInt();
        }
        this.resourceBottom = 1 + indexLength << 2;
        if (indexLength > 5) {
            final int att = this.indexes[5];
            this.noFallback = ((att & 0x1) != 0x0);
            this.isPoolBundle = ((att & 0x2) != 0x0);
            this.usesPoolBundle = ((att & 0x4) != 0x0);
        }
        final int length = this.indexes[3] * 4;
        if (this.indexes[1] > 1 + indexLength) {
            int keysBottom = 1 + indexLength << 2;
            int keysTop = this.indexes[1] << 2;
            this.resourceBottom = keysTop;
            if (this.isPoolBundle) {
                keysTop -= keysBottom;
                keysBottom = 0;
            }
            else {
                this.localKeyLimit = keysTop;
            }
            ds.readFully(this.keyStrings = new byte[keysTop], keysBottom, keysTop - keysBottom);
            if (this.isPoolBundle) {
                while (keysBottom < keysTop && this.keyStrings[keysTop - 1] == -86) {
                    this.keyStrings[--keysTop] = 0;
                }
                this.keyStringsAsString = new String(this.keyStrings, "US-ASCII");
            }
        }
        if (indexLength > 6 && this.indexes[6] > this.indexes[1]) {
            final int num16BitUnits = (this.indexes[6] - this.indexes[1]) * 2;
            final char[] c16BitUnits = new char[num16BitUnits];
            final byte[] c16BitUnitsBytes = new byte[num16BitUnits * 2];
            ds.readFully(c16BitUnitsBytes);
            for (int j = 0; j < num16BitUnits; ++j) {
                c16BitUnits[j] = (char)(c16BitUnitsBytes[j * 2] << 8 | (c16BitUnitsBytes[j * 2 + 1] & 0xFF));
            }
            this.s16BitUnits = new String(c16BitUnits);
            this.resourceBottom = this.indexes[6] << 2;
        }
        else {
            this.s16BitUnits = "\u0000";
        }
        ds.readFully(this.resourceBytes = new byte[length - this.resourceBottom]);
    }
    
    VersionInfo getVersion() {
        return VersionInfo.getInstance(this.dataVersion[0], this.dataVersion[1], this.dataVersion[2], this.dataVersion[3]);
    }
    
    public boolean isDataVersionAcceptable(final byte[] version) {
        return (version[0] == 1 && version[1] >= 1) || version[0] == 2;
    }
    
    int getRootResource() {
        return this.rootRes;
    }
    
    boolean getNoFallback() {
        return this.noFallback;
    }
    
    boolean getUsesPoolBundle() {
        return this.usesPoolBundle;
    }
    
    static int RES_GET_TYPE(final int res) {
        return res >>> 28;
    }
    
    private static int RES_GET_OFFSET(final int res) {
        return res & 0xFFFFFFF;
    }
    
    private int getResourceByteOffset(final int offset) {
        return (offset << 2) - this.resourceBottom;
    }
    
    static int RES_GET_INT(final int res) {
        return res << 4 >> 4;
    }
    
    static int RES_GET_UINT(final int res) {
        return res & 0xFFFFFFF;
    }
    
    static boolean URES_IS_TABLE(final int type) {
        return type == 2 || type == 5 || type == 4;
    }
    
    private char getChar(final int offset) {
        return (char)(this.resourceBytes[offset] << 8 | (this.resourceBytes[offset + 1] & 0xFF));
    }
    
    private char[] getChars(int offset, final int count) {
        final char[] chars = new char[count];
        for (int i = 0; i < count; ++i) {
            chars[i] = (char)(this.resourceBytes[offset] << 8 | (this.resourceBytes[offset + 1] & 0xFF));
            offset += 2;
        }
        return chars;
    }
    
    private int getInt(final int offset) {
        return this.resourceBytes[offset] << 24 | (this.resourceBytes[offset + 1] & 0xFF) << 16 | (this.resourceBytes[offset + 2] & 0xFF) << 8 | (this.resourceBytes[offset + 3] & 0xFF);
    }
    
    private int[] getInts(int offset, final int count) {
        final int[] ints = new int[count];
        for (int i = 0; i < count; ++i) {
            ints[i] = (this.resourceBytes[offset] << 24 | (this.resourceBytes[offset + 1] & 0xFF) << 16 | (this.resourceBytes[offset + 2] & 0xFF) << 8 | (this.resourceBytes[offset + 3] & 0xFF));
            offset += 4;
        }
        return ints;
    }
    
    private char[] getTable16KeyOffsets(int offset) {
        final int length = this.s16BitUnits.charAt(offset++);
        if (length > 0) {
            return this.s16BitUnits.substring(offset, offset + length).toCharArray();
        }
        return ICUResourceBundleReader.emptyChars;
    }
    
    private char[] getTableKeyOffsets(final int offset) {
        final int length = this.getChar(offset);
        if (length > 0) {
            return this.getChars(offset + 2, length);
        }
        return ICUResourceBundleReader.emptyChars;
    }
    
    private int[] getTable32KeyOffsets(final int offset) {
        final int length = this.getInt(offset);
        if (length > 0) {
            return this.getInts(offset + 4, length);
        }
        return ICUResourceBundleReader.emptyInts;
    }
    
    private String makeKeyStringFromBytes(int keyOffset) {
        final StringBuilder sb = new StringBuilder();
        byte b;
        while ((b = this.keyStrings[keyOffset++]) != 0) {
            sb.append((char)b);
        }
        return sb.toString();
    }
    
    private String makeKeyStringFromString(final int keyOffset) {
        int endOffset;
        for (endOffset = keyOffset; this.poolBundleKeysAsString.charAt(endOffset) != '\0'; ++endOffset) {}
        return this.poolBundleKeysAsString.substring(keyOffset, endOffset);
    }
    
    private ByteSequence RES_GET_KEY16(final char keyOffset) {
        if (keyOffset < this.localKeyLimit) {
            return new ByteSequence(this.keyStrings, keyOffset);
        }
        return new ByteSequence(this.poolBundleKeys, keyOffset - this.localKeyLimit);
    }
    
    private String getKey16String(final int keyOffset) {
        if (keyOffset < this.localKeyLimit) {
            return this.makeKeyStringFromBytes(keyOffset);
        }
        return this.makeKeyStringFromString(keyOffset - this.localKeyLimit);
    }
    
    private ByteSequence RES_GET_KEY32(final int keyOffset) {
        if (keyOffset >= 0) {
            return new ByteSequence(this.keyStrings, keyOffset);
        }
        return new ByteSequence(this.poolBundleKeys, keyOffset & Integer.MAX_VALUE);
    }
    
    private String getKey32String(final int keyOffset) {
        if (keyOffset >= 0) {
            return this.makeKeyStringFromBytes(keyOffset);
        }
        return this.makeKeyStringFromString(keyOffset & Integer.MAX_VALUE);
    }
    
    private static int compareKeys(final CharSequence key, final ByteSequence tableKey) {
        int i;
        for (i = 0; i < key.length(); ++i) {
            final int c2 = tableKey.charAt(i);
            if (c2 == 0) {
                return 1;
            }
            final int diff = key.charAt(i) - c2;
            if (diff != 0) {
                return diff;
            }
        }
        return -tableKey.charAt(i);
    }
    
    private int compareKeys(final CharSequence key, final char keyOffset) {
        return compareKeys(key, this.RES_GET_KEY16(keyOffset));
    }
    
    private int compareKeys32(final CharSequence key, final int keyOffset) {
        return compareKeys(key, this.RES_GET_KEY32(keyOffset));
    }
    
    String getString(final int res) {
        int offset = RES_GET_OFFSET(res);
        if (RES_GET_TYPE(res) == 6) {
            final int first = this.s16BitUnits.charAt(offset);
            if ((first & 0xFFFFFC00) == 0xDC00) {
                int length;
                if (first < 57327) {
                    length = (first & 0x3FF);
                    ++offset;
                }
                else if (first < 57343) {
                    length = (first - 57327 << 16 | this.s16BitUnits.charAt(offset + 1));
                    offset += 2;
                }
                else {
                    length = (this.s16BitUnits.charAt(offset + 1) << 16 | this.s16BitUnits.charAt(offset + 2));
                    offset += 3;
                }
                return this.s16BitUnits.substring(offset, offset + length);
            }
            if (first == 0) {
                return ICUResourceBundleReader.emptyString;
            }
            int endOffset;
            for (endOffset = offset + 1; this.s16BitUnits.charAt(endOffset) != '\0'; ++endOffset) {}
            return this.s16BitUnits.substring(offset, endOffset);
        }
        else {
            if (res != offset) {
                return null;
            }
            if (res == 0) {
                return ICUResourceBundleReader.emptyString;
            }
            offset = this.getResourceByteOffset(offset);
            final int length = this.getInt(offset);
            return new String(this.getChars(offset + 4, length));
        }
    }
    
    String getAlias(final int res) {
        int offset = RES_GET_OFFSET(res);
        if (RES_GET_TYPE(res) != 3) {
            return null;
        }
        if (offset == 0) {
            return ICUResourceBundleReader.emptyString;
        }
        offset = this.getResourceByteOffset(offset);
        final int length = this.getInt(offset);
        return new String(this.getChars(offset + 4, length));
    }
    
    byte[] getBinary(final int res, byte[] ba) {
        int offset = RES_GET_OFFSET(res);
        if (RES_GET_TYPE(res) != 1) {
            return null;
        }
        if (offset == 0) {
            return ICUResourceBundleReader.emptyBytes;
        }
        offset = this.getResourceByteOffset(offset);
        final int length = this.getInt(offset);
        if (ba == null || ba.length != length) {
            ba = new byte[length];
        }
        System.arraycopy(this.resourceBytes, offset + 4, ba, 0, length);
        return ba;
    }
    
    ByteBuffer getBinary(final int res) {
        int offset = RES_GET_OFFSET(res);
        if (RES_GET_TYPE(res) != 1) {
            return null;
        }
        if (offset == 0) {
            return ICUResourceBundleReader.emptyByteBuffer.duplicate();
        }
        offset = this.getResourceByteOffset(offset);
        final int length = this.getInt(offset);
        return ByteBuffer.wrap(this.resourceBytes, offset + 4, length).slice().asReadOnlyBuffer();
    }
    
    int[] getIntVector(final int res) {
        int offset = RES_GET_OFFSET(res);
        if (RES_GET_TYPE(res) != 14) {
            return null;
        }
        if (offset == 0) {
            return ICUResourceBundleReader.emptyInts;
        }
        offset = this.getResourceByteOffset(offset);
        final int length = this.getInt(offset);
        return this.getInts(offset + 4, length);
    }
    
    Container getArray(final int res) {
        final int type = RES_GET_TYPE(res);
        final int offset = RES_GET_OFFSET(res);
        switch (type) {
            case 8:
            case 9: {
                if (offset == 0) {
                    return new Container(this);
                }
                switch (type) {
                    case 8: {
                        return new Array(this, offset);
                    }
                    case 9: {
                        return new Array16(this, offset);
                    }
                    default: {
                        return null;
                    }
                }
                break;
            }
            default: {
                return null;
            }
        }
    }
    
    Table getTable(final int res) {
        final int type = RES_GET_TYPE(res);
        final int offset = RES_GET_OFFSET(res);
        switch (type) {
            case 2:
            case 4:
            case 5: {
                if (offset == 0) {
                    return new Table(this);
                }
                switch (type) {
                    case 2: {
                        return new Table1632(this, offset);
                    }
                    case 5: {
                        return new Table16(this, offset);
                    }
                    case 4: {
                        return new Table32(this, offset);
                    }
                    default: {
                        return null;
                    }
                }
                break;
            }
            default: {
                return null;
            }
        }
    }
    
    public static String getFullName(String baseName, String localeName) {
        if (baseName == null || baseName.length() == 0) {
            if (localeName.length() == 0) {
                return localeName = ULocale.getDefault().toString();
            }
            return localeName + ".res";
        }
        else if (baseName.indexOf(46) == -1) {
            if (baseName.charAt(baseName.length() - 1) != '/') {
                return baseName + "/" + localeName + ".res";
            }
            return baseName + localeName + ".res";
        }
        else {
            baseName = baseName.replace('.', '/');
            if (localeName.length() == 0) {
                return baseName + ".res";
            }
            return baseName + "_" + localeName + ".res";
        }
    }
    
    static {
        DATA_FORMAT_ID = new byte[] { 82, 101, 115, 66 };
        ICUResourceBundleReader.CACHE = new ReaderCache();
        NULL_READER = new ICUResourceBundleReader();
        ICUResourceBundleReader.emptyBytes = new byte[0];
        ICUResourceBundleReader.emptyByteBuffer = ByteBuffer.allocate(0).asReadOnlyBuffer();
        ICUResourceBundleReader.emptyChars = new char[0];
        ICUResourceBundleReader.emptyInts = new int[0];
        ICUResourceBundleReader.emptyString = "";
    }
    
    private static class ReaderInfo
    {
        final String baseName;
        final String localeID;
        final ClassLoader loader;
        
        ReaderInfo(final String baseName, final String localeID, final ClassLoader loader) {
            this.baseName = ((baseName == null) ? "" : baseName);
            this.localeID = ((localeID == null) ? "" : localeID);
            this.loader = loader;
        }
        
        @Override
        public boolean equals(final Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ReaderInfo)) {
                return false;
            }
            final ReaderInfo info = (ReaderInfo)obj;
            return this.baseName.equals(info.baseName) && this.localeID.equals(info.localeID) && this.loader.equals(info.loader);
        }
        
        @Override
        public int hashCode() {
            return this.baseName.hashCode() ^ this.localeID.hashCode() ^ this.loader.hashCode();
        }
    }
    
    private static class ReaderCache extends SoftCache<ReaderInfo, ICUResourceBundleReader, ReaderInfo>
    {
        @Override
        protected ICUResourceBundleReader createInstance(final ReaderInfo key, final ReaderInfo data) {
            final String fullName = ICUResourceBundleReader.getFullName(data.baseName, data.localeID);
            final InputStream stream = ICUData.getStream(data.loader, fullName);
            if (stream == null) {
                return ICUResourceBundleReader.NULL_READER;
            }
            return new ICUResourceBundleReader(stream, data.baseName, data.localeID, data.loader, null);
        }
    }
    
    private static final class ByteSequence
    {
        private byte[] bytes;
        private int offset;
        
        public ByteSequence(final byte[] bytes, final int offset) {
            this.bytes = bytes;
            this.offset = offset;
        }
        
        public byte charAt(final int index) {
            return this.bytes[this.offset + index];
        }
    }
    
    static class Container
    {
        protected ICUResourceBundleReader reader;
        protected int size;
        protected int itemsOffset;
        
        int getSize() {
            return this.size;
        }
        
        int getContainerResource(final int index) {
            return -1;
        }
        
        protected int getContainer16Resource(final int index) {
            if (index < 0 || this.size <= index) {
                return -1;
            }
            return 0x60000000 | this.reader.s16BitUnits.charAt(this.itemsOffset + index);
        }
        
        protected int getContainer32Resource(final int index) {
            if (index < 0 || this.size <= index) {
                return -1;
            }
            return this.reader.getInt(this.itemsOffset + 4 * index);
        }
        
        Container(final ICUResourceBundleReader reader) {
            this.reader = reader;
        }
    }
    
    private static final class Array extends Container
    {
        @Override
        int getContainerResource(final int index) {
            return this.getContainer32Resource(index);
        }
        
        Array(final ICUResourceBundleReader reader, int offset) {
            super(reader);
            offset = reader.getResourceByteOffset(offset);
            this.size = reader.getInt(offset);
            this.itemsOffset = offset + 4;
        }
    }
    
    private static final class Array16 extends Container
    {
        @Override
        int getContainerResource(final int index) {
            return this.getContainer16Resource(index);
        }
        
        Array16(final ICUResourceBundleReader reader, final int offset) {
            super(reader);
            this.size = reader.s16BitUnits.charAt(offset);
            this.itemsOffset = offset + 1;
        }
    }
    
    static class Table extends Container
    {
        protected char[] keyOffsets;
        protected int[] key32Offsets;
        private static final int URESDATA_ITEM_NOT_FOUND = -1;
        
        String getKey(final int index) {
            if (index < 0 || this.size <= index) {
                return null;
            }
            return (this.keyOffsets != null) ? this.reader.getKey16String(this.keyOffsets[index]) : this.reader.getKey32String(this.key32Offsets[index]);
        }
        
        int findTableItem(final CharSequence key) {
            int start = 0;
            int limit = this.size;
            while (start < limit) {
                final int mid = start + limit >>> 1;
                int result;
                if (this.keyOffsets != null) {
                    result = this.reader.compareKeys(key, this.keyOffsets[mid]);
                }
                else {
                    result = this.reader.compareKeys32(key, this.key32Offsets[mid]);
                }
                if (result < 0) {
                    limit = mid;
                }
                else {
                    if (result <= 0) {
                        return mid;
                    }
                    start = mid + 1;
                }
            }
            return -1;
        }
        
        int getTableResource(final String resKey) {
            return this.getContainerResource(this.findTableItem(resKey));
        }
        
        Table(final ICUResourceBundleReader reader) {
            super(reader);
        }
    }
    
    private static final class Table1632 extends Table
    {
        @Override
        int getContainerResource(final int index) {
            return this.getContainer32Resource(index);
        }
        
        Table1632(final ICUResourceBundleReader reader, int offset) {
            super(reader);
            offset = reader.getResourceByteOffset(offset);
            this.keyOffsets = reader.getTableKeyOffsets(offset);
            this.size = this.keyOffsets.length;
            this.itemsOffset = offset + 2 * (this.size + 2 & 0xFFFFFFFE);
        }
    }
    
    private static final class Table16 extends Table
    {
        @Override
        int getContainerResource(final int index) {
            return this.getContainer16Resource(index);
        }
        
        Table16(final ICUResourceBundleReader reader, final int offset) {
            super(reader);
            this.keyOffsets = reader.getTable16KeyOffsets(offset);
            this.size = this.keyOffsets.length;
            this.itemsOffset = offset + 1 + this.size;
        }
    }
    
    private static final class Table32 extends Table
    {
        @Override
        int getContainerResource(final int index) {
            return this.getContainer32Resource(index);
        }
        
        Table32(final ICUResourceBundleReader reader, int offset) {
            super(reader);
            offset = reader.getResourceByteOffset(offset);
            this.key32Offsets = reader.getTable32KeyOffsets(offset);
            this.size = this.key32Offsets.length;
            this.itemsOffset = offset + 4 * (1 + this.size);
        }
    }
}
