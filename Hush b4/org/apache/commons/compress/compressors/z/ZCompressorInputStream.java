// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.compressors.z;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.compressors.z._internal_.InternalLZWInputStream;

public class ZCompressorInputStream extends InternalLZWInputStream
{
    private static final int MAGIC_1 = 31;
    private static final int MAGIC_2 = 157;
    private static final int BLOCK_MODE_MASK = 128;
    private static final int MAX_CODE_SIZE_MASK = 31;
    private final boolean blockMode;
    private final int maxCodeSize;
    private long totalCodesRead;
    
    public ZCompressorInputStream(final InputStream inputStream) throws IOException {
        super(inputStream);
        this.totalCodesRead = 0L;
        final int firstByte = this.in.read();
        final int secondByte = this.in.read();
        final int thirdByte = this.in.read();
        if (firstByte != 31 || secondByte != 157 || thirdByte < 0) {
            throw new IOException("Input is not in .Z format");
        }
        this.blockMode = ((thirdByte & 0x80) != 0x0);
        this.maxCodeSize = (thirdByte & 0x1F);
        if (this.blockMode) {
            this.setClearCode(this.codeSize);
        }
        this.initializeTables(this.maxCodeSize);
        this.clearEntries();
    }
    
    private void clearEntries() {
        this.tableSize = 256;
        if (this.blockMode) {
            ++this.tableSize;
        }
    }
    
    @Override
    protected int readNextCode() throws IOException {
        final int code = super.readNextCode();
        if (code >= 0) {
            ++this.totalCodesRead;
        }
        return code;
    }
    
    private void reAlignReading() throws IOException {
        long codeReadsToThrowAway = 8L - this.totalCodesRead % 8L;
        if (codeReadsToThrowAway == 8L) {
            codeReadsToThrowAway = 0L;
        }
        for (long i = 0L; i < codeReadsToThrowAway; ++i) {
            this.readNextCode();
        }
        this.bitsCached = 0;
        this.bitsCachedSize = 0;
    }
    
    @Override
    protected int addEntry(final int previousCode, final byte character) throws IOException {
        final int maxTableSize = 1 << this.codeSize;
        final int r = this.addEntry(previousCode, character, maxTableSize);
        if (this.tableSize == maxTableSize && this.codeSize < this.maxCodeSize) {
            this.reAlignReading();
            ++this.codeSize;
        }
        return r;
    }
    
    @Override
    protected int decompressNextSymbol() throws IOException {
        final int code = this.readNextCode();
        if (code < 0) {
            return -1;
        }
        if (this.blockMode && code == this.clearCode) {
            this.clearEntries();
            this.reAlignReading();
            this.codeSize = 9;
            this.previousCode = -1;
            return 0;
        }
        boolean addedUnfinishedEntry = false;
        if (code == this.tableSize) {
            this.addRepeatOfPreviousCode();
            addedUnfinishedEntry = true;
        }
        else if (code > this.tableSize) {
            throw new IOException(String.format("Invalid %d bit code 0x%x", this.codeSize, code));
        }
        return this.expandCodeToOutputStack(code, addedUnfinishedEntry);
    }
    
    public static boolean matches(final byte[] signature, final int length) {
        return length > 3 && signature[0] == 31 && signature[1] == -99;
    }
}
