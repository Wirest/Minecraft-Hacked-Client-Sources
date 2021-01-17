// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.compressors.z._internal_;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;

public abstract class InternalLZWInputStream extends CompressorInputStream
{
    private final byte[] oneByte;
    protected final InputStream in;
    protected int clearCode;
    protected int codeSize;
    protected int bitsCached;
    protected int bitsCachedSize;
    protected int previousCode;
    protected int tableSize;
    protected int[] prefixes;
    protected byte[] characters;
    private byte[] outputStack;
    private int outputStackLocation;
    
    protected InternalLZWInputStream(final InputStream inputStream) {
        this.oneByte = new byte[1];
        this.clearCode = -1;
        this.codeSize = 9;
        this.bitsCached = 0;
        this.bitsCachedSize = 0;
        this.previousCode = -1;
        this.tableSize = 0;
        this.in = inputStream;
    }
    
    @Override
    public void close() throws IOException {
        this.in.close();
    }
    
    @Override
    public int read() throws IOException {
        final int ret = this.read(this.oneByte);
        if (ret < 0) {
            return ret;
        }
        return 0xFF & this.oneByte[0];
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        int bytesRead = this.readFromStack(b, off, len);
        while (len - bytesRead > 0) {
            final int result = this.decompressNextSymbol();
            if (result < 0) {
                if (bytesRead > 0) {
                    this.count(bytesRead);
                    return bytesRead;
                }
                return result;
            }
            else {
                bytesRead += this.readFromStack(b, off + bytesRead, len - bytesRead);
            }
        }
        this.count(bytesRead);
        return bytesRead;
    }
    
    protected abstract int decompressNextSymbol() throws IOException;
    
    protected abstract int addEntry(final int p0, final byte p1) throws IOException;
    
    protected void setClearCode(final int codeSize) {
        this.clearCode = 1 << codeSize - 1;
    }
    
    protected void initializeTables(final int maxCodeSize) {
        final int maxTableSize = 1 << maxCodeSize;
        this.prefixes = new int[maxTableSize];
        this.characters = new byte[maxTableSize];
        this.outputStack = new byte[maxTableSize];
        this.outputStackLocation = maxTableSize;
        final int max = 256;
        for (int i = 0; i < 256; ++i) {
            this.prefixes[i] = -1;
            this.characters[i] = (byte)i;
        }
    }
    
    protected int readNextCode() throws IOException {
        while (this.bitsCachedSize < this.codeSize) {
            final int nextByte = this.in.read();
            if (nextByte < 0) {
                return nextByte;
            }
            this.bitsCached |= nextByte << this.bitsCachedSize;
            this.bitsCachedSize += 8;
        }
        final int mask = (1 << this.codeSize) - 1;
        final int code = this.bitsCached & mask;
        this.bitsCached >>>= this.codeSize;
        this.bitsCachedSize -= this.codeSize;
        return code;
    }
    
    protected int addEntry(final int previousCode, final byte character, final int maxTableSize) {
        if (this.tableSize < maxTableSize) {
            final int index = this.tableSize;
            this.prefixes[this.tableSize] = previousCode;
            this.characters[this.tableSize] = character;
            ++this.tableSize;
            return index;
        }
        return -1;
    }
    
    protected int addRepeatOfPreviousCode() throws IOException {
        if (this.previousCode == -1) {
            throw new IOException("The first code can't be a reference to its preceding code");
        }
        byte firstCharacter = 0;
        for (int last = this.previousCode; last >= 0; last = this.prefixes[last]) {
            firstCharacter = this.characters[last];
        }
        return this.addEntry(this.previousCode, firstCharacter);
    }
    
    protected int expandCodeToOutputStack(final int code, final boolean addedUnfinishedEntry) throws IOException {
        for (int entry = code; entry >= 0; entry = this.prefixes[entry]) {
            this.outputStack[--this.outputStackLocation] = this.characters[entry];
        }
        if (this.previousCode != -1 && !addedUnfinishedEntry) {
            this.addEntry(this.previousCode, this.outputStack[this.outputStackLocation]);
        }
        this.previousCode = code;
        return this.outputStackLocation;
    }
    
    private int readFromStack(final byte[] b, final int off, final int len) {
        final int remainingInStack = this.outputStack.length - this.outputStackLocation;
        if (remainingInStack > 0) {
            final int maxLength = Math.min(remainingInStack, len);
            System.arraycopy(this.outputStack, this.outputStackLocation, b, off, maxLength);
            this.outputStackLocation += maxLength;
            return maxLength;
        }
        return 0;
    }
}
