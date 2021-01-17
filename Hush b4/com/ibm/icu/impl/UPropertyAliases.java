// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.util.MissingResourceException;
import com.ibm.icu.util.BytesTrie;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.io.InputStream;

public final class UPropertyAliases
{
    private static final int IX_VALUE_MAPS_OFFSET = 0;
    private static final int IX_BYTE_TRIES_OFFSET = 1;
    private static final int IX_NAME_GROUPS_OFFSET = 2;
    private static final int IX_RESERVED3_OFFSET = 3;
    private int[] valueMaps;
    private byte[] bytesTries;
    private String nameGroups;
    private static final IsAcceptable IS_ACCEPTABLE;
    private static final byte[] DATA_FORMAT;
    public static final UPropertyAliases INSTANCE;
    
    private void load(final InputStream data) throws IOException {
        final BufferedInputStream bis = new BufferedInputStream(data);
        ICUBinary.readHeader(bis, UPropertyAliases.DATA_FORMAT, UPropertyAliases.IS_ACCEPTABLE);
        final DataInputStream ds = new DataInputStream(bis);
        final int indexesLength = ds.readInt() / 4;
        if (indexesLength < 8) {
            throw new IOException("pnames.icu: not enough indexes");
        }
        final int[] inIndexes = new int[indexesLength];
        inIndexes[0] = indexesLength * 4;
        for (int i = 1; i < indexesLength; ++i) {
            inIndexes[i] = ds.readInt();
        }
        int offset = inIndexes[0];
        int nextOffset = inIndexes[1];
        final int numInts = (nextOffset - offset) / 4;
        this.valueMaps = new int[numInts];
        for (int j = 0; j < numInts; ++j) {
            this.valueMaps[j] = ds.readInt();
        }
        offset = nextOffset;
        nextOffset = inIndexes[2];
        int numBytes = nextOffset - offset;
        ds.readFully(this.bytesTries = new byte[numBytes]);
        offset = nextOffset;
        nextOffset = inIndexes[3];
        numBytes = nextOffset - offset;
        final StringBuilder sb = new StringBuilder(numBytes);
        for (int k = 0; k < numBytes; ++k) {
            sb.append((char)ds.readByte());
        }
        this.nameGroups = sb.toString();
        data.close();
    }
    
    private UPropertyAliases() throws IOException {
        this.load(ICUData.getRequiredStream("data/icudt51b/pnames.icu"));
    }
    
    private int findProperty(final int property) {
        int i = 1;
        for (int numRanges = this.valueMaps[0]; numRanges > 0; --numRanges) {
            final int start = this.valueMaps[i];
            final int limit = this.valueMaps[i + 1];
            i += 2;
            if (property < start) {
                break;
            }
            if (property < limit) {
                return i + (property - start) * 2;
            }
            i += (limit - start) * 2;
        }
        return 0;
    }
    
    private int findPropertyValueNameGroup(int valueMapIndex, final int value) {
        if (valueMapIndex == 0) {
            return 0;
        }
        ++valueMapIndex;
        int numRanges = this.valueMaps[valueMapIndex++];
        if (numRanges < 16) {
            while (numRanges > 0) {
                final int start = this.valueMaps[valueMapIndex];
                final int limit = this.valueMaps[valueMapIndex + 1];
                valueMapIndex += 2;
                if (value < start) {
                    break;
                }
                if (value < limit) {
                    return this.valueMaps[valueMapIndex + value - start];
                }
                valueMapIndex += limit - start;
                --numRanges;
            }
        }
        else {
            final int valuesStart = valueMapIndex;
            final int nameGroupOffsetsStart = valueMapIndex + numRanges - 16;
            do {
                final int v = this.valueMaps[valueMapIndex];
                if (value < v) {
                    break;
                }
                if (value == v) {
                    return this.valueMaps[nameGroupOffsetsStart + valueMapIndex - valuesStart];
                }
            } while (++valueMapIndex < nameGroupOffsetsStart);
        }
        return 0;
    }
    
    private String getName(int nameGroupsIndex, int nameIndex) {
        final int numNames = this.nameGroups.charAt(nameGroupsIndex++);
        if (nameIndex < 0 || numNames <= nameIndex) {
            throw new IllegalIcuArgumentException("Invalid property (value) name choice");
        }
        while (nameIndex > 0) {
            while ('\0' != this.nameGroups.charAt(nameGroupsIndex++)) {}
            --nameIndex;
        }
        final int nameStart = nameGroupsIndex;
        while ('\0' != this.nameGroups.charAt(nameGroupsIndex)) {
            ++nameGroupsIndex;
        }
        if (nameStart == nameGroupsIndex) {
            return null;
        }
        return this.nameGroups.substring(nameStart, nameGroupsIndex);
    }
    
    private static int asciiToLowercase(final int c) {
        return (65 <= c && c <= 90) ? (c + 32) : c;
    }
    
    private boolean containsName(final BytesTrie trie, final CharSequence name) {
        BytesTrie.Result result = BytesTrie.Result.NO_VALUE;
        for (int i = 0; i < name.length(); ++i) {
            int c = name.charAt(i);
            if (c != 45 && c != 95 && c != 32) {
                if (9 > c || c > 13) {
                    if (!result.hasNext()) {
                        return false;
                    }
                    c = asciiToLowercase(c);
                    result = trie.next(c);
                }
            }
        }
        return result.hasValue();
    }
    
    public String getPropertyName(final int property, final int nameChoice) {
        final int valueMapIndex = this.findProperty(property);
        if (valueMapIndex == 0) {
            throw new IllegalArgumentException("Invalid property enum " + property + " (0x" + Integer.toHexString(property) + ")");
        }
        return this.getName(this.valueMaps[valueMapIndex], nameChoice);
    }
    
    public String getPropertyValueName(final int property, final int value, final int nameChoice) {
        final int valueMapIndex = this.findProperty(property);
        if (valueMapIndex == 0) {
            throw new IllegalArgumentException("Invalid property enum " + property + " (0x" + Integer.toHexString(property) + ")");
        }
        final int nameGroupOffset = this.findPropertyValueNameGroup(this.valueMaps[valueMapIndex + 1], value);
        if (nameGroupOffset == 0) {
            throw new IllegalArgumentException("Property " + property + " (0x" + Integer.toHexString(property) + ") does not have named values");
        }
        return this.getName(nameGroupOffset, nameChoice);
    }
    
    private int getPropertyOrValueEnum(final int bytesTrieOffset, final CharSequence alias) {
        final BytesTrie trie = new BytesTrie(this.bytesTries, bytesTrieOffset);
        if (this.containsName(trie, alias)) {
            return trie.getValue();
        }
        return -1;
    }
    
    public int getPropertyEnum(final CharSequence alias) {
        return this.getPropertyOrValueEnum(0, alias);
    }
    
    public int getPropertyValueEnum(final int property, final CharSequence alias) {
        int valueMapIndex = this.findProperty(property);
        if (valueMapIndex == 0) {
            throw new IllegalArgumentException("Invalid property enum " + property + " (0x" + Integer.toHexString(property) + ")");
        }
        valueMapIndex = this.valueMaps[valueMapIndex + 1];
        if (valueMapIndex == 0) {
            throw new IllegalArgumentException("Property " + property + " (0x" + Integer.toHexString(property) + ") does not have named values");
        }
        return this.getPropertyOrValueEnum(this.valueMaps[valueMapIndex], alias);
    }
    
    public static int compare(final String stra, final String strb) {
        int istra = 0;
        int istrb = 0;
        int cstra = 0;
        int cstrb = 0;
        while (true) {
            if (istra < stra.length()) {
                cstra = stra.charAt(istra);
                switch (cstra) {
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 32:
                    case 45:
                    case 95: {
                        ++istra;
                        continue;
                    }
                }
            }
        Label_0209:
            while (istrb < strb.length()) {
                cstrb = strb.charAt(istrb);
                switch (cstrb) {
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 32:
                    case 45:
                    case 95: {
                        ++istrb;
                        continue;
                    }
                    default: {
                        break Label_0209;
                    }
                }
            }
            final boolean endstra = istra == stra.length();
            final boolean endstrb = istrb == strb.length();
            if (endstra) {
                if (endstrb) {
                    return 0;
                }
                cstra = 0;
            }
            else if (endstrb) {
                cstrb = 0;
            }
            final int rc = asciiToLowercase(cstra) - asciiToLowercase(cstrb);
            if (rc != 0) {
                return rc;
            }
            ++istra;
            ++istrb;
        }
    }
    
    static {
        IS_ACCEPTABLE = new IsAcceptable();
        DATA_FORMAT = new byte[] { 112, 110, 97, 109 };
        try {
            INSTANCE = new UPropertyAliases();
        }
        catch (IOException e) {
            final MissingResourceException mre = new MissingResourceException("Could not construct UPropertyAliases. Missing pnames.icu", "", "");
            mre.initCause(e);
            throw mre;
        }
    }
    
    private static final class IsAcceptable implements ICUBinary.Authenticate
    {
        public boolean isDataVersionAcceptable(final byte[] version) {
            return version[0] == 2;
        }
    }
}
