// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.DataInputStream;

public final class StringPrepDataReader implements ICUBinary.Authenticate
{
    private static final boolean debug;
    private DataInputStream dataInputStream;
    private byte[] unicodeVersion;
    private static final byte[] DATA_FORMAT_ID;
    private static final byte[] DATA_FORMAT_VERSION;
    
    public StringPrepDataReader(final InputStream inputStream) throws IOException {
        if (StringPrepDataReader.debug) {
            System.out.println("Bytes in inputStream " + inputStream.available());
        }
        this.unicodeVersion = ICUBinary.readHeader(inputStream, StringPrepDataReader.DATA_FORMAT_ID, this);
        if (StringPrepDataReader.debug) {
            System.out.println("Bytes left in inputStream " + inputStream.available());
        }
        this.dataInputStream = new DataInputStream(inputStream);
        if (StringPrepDataReader.debug) {
            System.out.println("Bytes left in dataInputStream " + this.dataInputStream.available());
        }
    }
    
    public void read(final byte[] idnaBytes, final char[] mappingTable) throws IOException {
        this.dataInputStream.readFully(idnaBytes);
        for (int i = 0; i < mappingTable.length; ++i) {
            mappingTable[i] = this.dataInputStream.readChar();
        }
    }
    
    public byte[] getDataFormatVersion() {
        return StringPrepDataReader.DATA_FORMAT_VERSION;
    }
    
    public boolean isDataVersionAcceptable(final byte[] version) {
        return version[0] == StringPrepDataReader.DATA_FORMAT_VERSION[0] && version[2] == StringPrepDataReader.DATA_FORMAT_VERSION[2] && version[3] == StringPrepDataReader.DATA_FORMAT_VERSION[3];
    }
    
    public int[] readIndexes(final int length) throws IOException {
        final int[] indexes = new int[length];
        for (int i = 0; i < length; ++i) {
            indexes[i] = this.dataInputStream.readInt();
        }
        return indexes;
    }
    
    public byte[] getUnicodeVersion() {
        return this.unicodeVersion;
    }
    
    static {
        debug = ICUDebug.enabled("NormalizerDataReader");
        DATA_FORMAT_ID = new byte[] { 83, 80, 82, 80 };
        DATA_FORMAT_VERSION = new byte[] { 3, 2, 5, 2 };
    }
}
