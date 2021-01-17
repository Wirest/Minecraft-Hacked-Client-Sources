// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.zip;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.compressors.z._internal_.InternalLZWInputStream;

class UnshrinkingInputStream extends InternalLZWInputStream
{
    private static final int MAX_CODE_SIZE = 13;
    private static final int MAX_TABLE_SIZE = 8192;
    private final boolean[] isUsed;
    
    public UnshrinkingInputStream(final InputStream inputStream) throws IOException {
        super(inputStream);
        this.setClearCode(this.codeSize);
        this.initializeTables(13);
        this.isUsed = new boolean[this.prefixes.length];
        for (int i = 0; i < 256; ++i) {
            this.isUsed[i] = true;
        }
        this.tableSize = this.clearCode + 1;
    }
    
    @Override
    protected int addEntry(final int previousCode, final byte character) throws IOException {
        while (this.tableSize < 8192 && this.isUsed[this.tableSize]) {
            ++this.tableSize;
        }
        final int idx = this.addEntry(previousCode, character, 8192);
        if (idx >= 0) {
            this.isUsed[idx] = true;
        }
        return idx;
    }
    
    private void partialClear() {
        final boolean[] isParent = new boolean[8192];
        for (int i = 0; i < this.isUsed.length; ++i) {
            if (this.isUsed[i] && this.prefixes[i] != -1) {
                isParent[this.prefixes[i]] = true;
            }
        }
        for (int i = this.clearCode + 1; i < isParent.length; ++i) {
            if (!isParent[i]) {
                this.isUsed[i] = false;
                this.prefixes[i] = -1;
            }
        }
    }
    
    @Override
    protected int decompressNextSymbol() throws IOException {
        final int code = this.readNextCode();
        if (code < 0) {
            return -1;
        }
        if (code != this.clearCode) {
            boolean addedUnfinishedEntry = false;
            int effectiveCode = code;
            if (!this.isUsed[code]) {
                effectiveCode = this.addRepeatOfPreviousCode();
                addedUnfinishedEntry = true;
            }
            return this.expandCodeToOutputStack(effectiveCode, addedUnfinishedEntry);
        }
        final int subCode = this.readNextCode();
        if (subCode < 0) {
            throw new IOException("Unexpected EOF;");
        }
        if (subCode == 1) {
            if (this.codeSize >= 13) {
                throw new IOException("Attempt to increase code size beyond maximum");
            }
            ++this.codeSize;
        }
        else {
            if (subCode != 2) {
                throw new IOException("Invalid clear code subcode " + subCode);
            }
            this.partialClear();
            this.tableSize = this.clearCode + 1;
        }
        return 0;
    }
}
