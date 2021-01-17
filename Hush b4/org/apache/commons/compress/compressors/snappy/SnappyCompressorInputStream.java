// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.compressors.snappy;

import org.apache.commons.compress.utils.IOUtils;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;

public class SnappyCompressorInputStream extends CompressorInputStream
{
    private static final int TAG_MASK = 3;
    public static final int DEFAULT_BLOCK_SIZE = 32768;
    private final byte[] decompressBuf;
    private int writeIndex;
    private int readIndex;
    private final int blockSize;
    private final InputStream in;
    private final int size;
    private int uncompressedBytesRemaining;
    private final byte[] oneByte;
    private boolean endReached;
    
    public SnappyCompressorInputStream(final InputStream is) throws IOException {
        this(is, 32768);
    }
    
    public SnappyCompressorInputStream(final InputStream is, final int blockSize) throws IOException {
        this.oneByte = new byte[1];
        this.endReached = false;
        this.in = is;
        this.blockSize = blockSize;
        this.decompressBuf = new byte[blockSize * 3];
        final int n = 0;
        this.readIndex = n;
        this.writeIndex = n;
        final int n2 = (int)this.readSize();
        this.size = n2;
        this.uncompressedBytesRemaining = n2;
    }
    
    @Override
    public int read() throws IOException {
        return (this.read(this.oneByte, 0, 1) == -1) ? -1 : (this.oneByte[0] & 0xFF);
    }
    
    @Override
    public void close() throws IOException {
        this.in.close();
    }
    
    @Override
    public int available() {
        return this.writeIndex - this.readIndex;
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        if (this.endReached) {
            return -1;
        }
        final int avail = this.available();
        if (len > avail) {
            this.fill(len - avail);
        }
        final int readable = Math.min(len, this.available());
        System.arraycopy(this.decompressBuf, this.readIndex, b, off, readable);
        this.readIndex += readable;
        if (this.readIndex > this.blockSize) {
            this.slideBuffer();
        }
        return readable;
    }
    
    private void fill(final int len) throws IOException {
        if (this.uncompressedBytesRemaining == 0) {
            this.endReached = true;
        }
        int length;
        for (int readNow = Math.min(len, this.uncompressedBytesRemaining); readNow > 0; readNow -= length, this.uncompressedBytesRemaining -= length) {
            final int b = this.readOneByte();
            length = 0;
            long offset = 0L;
            switch (b & 0x3) {
                case 0: {
                    length = this.readLiteralLength(b);
                    if (this.expandLiteral(length)) {
                        return;
                    }
                    break;
                }
                case 1: {
                    length = 4 + (b >> 2 & 0x7);
                    offset = (b & 0xE0) << 3;
                    offset |= this.readOneByte();
                    if (this.expandCopy(offset, length)) {
                        return;
                    }
                    break;
                }
                case 2: {
                    length = (b >> 2) + 1;
                    offset = this.readOneByte();
                    offset |= this.readOneByte() << 8;
                    if (this.expandCopy(offset, length)) {
                        return;
                    }
                    break;
                }
                case 3: {
                    length = (b >> 2) + 1;
                    offset = this.readOneByte();
                    offset |= this.readOneByte() << 8;
                    offset |= this.readOneByte() << 16;
                    offset |= (long)this.readOneByte() << 24;
                    if (this.expandCopy(offset, length)) {
                        return;
                    }
                    break;
                }
            }
        }
    }
    
    private void slideBuffer() {
        System.arraycopy(this.decompressBuf, this.blockSize, this.decompressBuf, 0, this.blockSize * 2);
        this.writeIndex -= this.blockSize;
        this.readIndex -= this.blockSize;
    }
    
    private int readLiteralLength(final int b) throws IOException {
        int length = 0;
        switch (b >> 2) {
            case 60: {
                length = this.readOneByte();
                break;
            }
            case 61: {
                length = this.readOneByte();
                length |= this.readOneByte() << 8;
                break;
            }
            case 62: {
                length = this.readOneByte();
                length |= this.readOneByte() << 8;
                length |= this.readOneByte() << 16;
                break;
            }
            case 63: {
                length = this.readOneByte();
                length |= this.readOneByte() << 8;
                length |= this.readOneByte() << 16;
                length = (int)((long)length | (long)this.readOneByte() << 24);
                break;
            }
            default: {
                length = b >> 2;
                break;
            }
        }
        return length + 1;
    }
    
    private boolean expandLiteral(final int length) throws IOException {
        final int bytesRead = IOUtils.readFully(this.in, this.decompressBuf, this.writeIndex, length);
        this.count(bytesRead);
        if (length != bytesRead) {
            throw new IOException("Premature end of stream");
        }
        this.writeIndex += length;
        return this.writeIndex >= 2 * this.blockSize;
    }
    
    private boolean expandCopy(final long off, final int length) throws IOException {
        if (off > this.blockSize) {
            throw new IOException("Offset is larger than block size");
        }
        final int offset = (int)off;
        if (offset == 1) {
            final byte lastChar = this.decompressBuf[this.writeIndex - 1];
            for (int i = 0; i < length; ++i) {
                this.decompressBuf[this.writeIndex++] = lastChar;
            }
        }
        else if (length < offset) {
            System.arraycopy(this.decompressBuf, this.writeIndex - offset, this.decompressBuf, this.writeIndex, length);
            this.writeIndex += length;
        }
        else {
            int fullRotations = length / offset;
            final int pad = length - offset * fullRotations;
            while (fullRotations-- != 0) {
                System.arraycopy(this.decompressBuf, this.writeIndex - offset, this.decompressBuf, this.writeIndex, offset);
                this.writeIndex += offset;
            }
            if (pad > 0) {
                System.arraycopy(this.decompressBuf, this.writeIndex - offset, this.decompressBuf, this.writeIndex, pad);
                this.writeIndex += pad;
            }
        }
        return this.writeIndex >= 2 * this.blockSize;
    }
    
    private int readOneByte() throws IOException {
        final int b = this.in.read();
        if (b == -1) {
            throw new IOException("Premature end of stream");
        }
        this.count(1);
        return b & 0xFF;
    }
    
    private long readSize() throws IOException {
        int index = 0;
        long sz = 0L;
        int b = 0;
        do {
            b = this.readOneByte();
            sz |= (b & 0x7F) << index++ * 7;
        } while (0x0 != (b & 0x80));
        return sz;
    }
    
    public int getSize() {
        return this.size;
    }
}
