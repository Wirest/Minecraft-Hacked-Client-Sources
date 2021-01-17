// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.dump;

import org.apache.commons.compress.utils.IOUtils;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.Arrays;
import java.io.IOException;
import java.io.InputStream;
import java.io.FilterInputStream;

class TapeInputStream extends FilterInputStream
{
    private byte[] blockBuffer;
    private int currBlkIdx;
    private int blockSize;
    private static final int recordSize = 1024;
    private int readOffset;
    private boolean isCompressed;
    private long bytesRead;
    
    public TapeInputStream(final InputStream in) {
        super(in);
        this.blockBuffer = new byte[1024];
        this.currBlkIdx = -1;
        this.blockSize = 1024;
        this.readOffset = 1024;
        this.isCompressed = false;
        this.bytesRead = 0L;
    }
    
    public void resetBlockSize(final int recsPerBlock, final boolean isCompressed) throws IOException {
        this.isCompressed = isCompressed;
        this.blockSize = 1024 * recsPerBlock;
        final byte[] oldBuffer = this.blockBuffer;
        System.arraycopy(oldBuffer, 0, this.blockBuffer = new byte[this.blockSize], 0, 1024);
        this.readFully(this.blockBuffer, 1024, this.blockSize - 1024);
        this.currBlkIdx = 0;
        this.readOffset = 1024;
    }
    
    @Override
    public int available() throws IOException {
        if (this.readOffset < this.blockSize) {
            return this.blockSize - this.readOffset;
        }
        return this.in.available();
    }
    
    @Override
    public int read() throws IOException {
        throw new IllegalArgumentException("all reads must be multiple of record size (1024 bytes.");
    }
    
    @Override
    public int read(final byte[] b, int off, final int len) throws IOException {
        if (len % 1024 != 0) {
            throw new IllegalArgumentException("all reads must be multiple of record size (1024 bytes.");
        }
        int bytes;
        int n;
        for (bytes = 0; bytes < len; bytes += n, off += n) {
            if (this.readOffset == this.blockSize && !this.readBlock(true)) {
                return -1;
            }
            n = 0;
            if (this.readOffset + (len - bytes) <= this.blockSize) {
                n = len - bytes;
            }
            else {
                n = this.blockSize - this.readOffset;
            }
            System.arraycopy(this.blockBuffer, this.readOffset, b, off, n);
            this.readOffset += n;
        }
        return bytes;
    }
    
    @Override
    public long skip(final long len) throws IOException {
        if (len % 1024L != 0L) {
            throw new IllegalArgumentException("all reads must be multiple of record size (1024 bytes.");
        }
        long bytes;
        long n;
        for (bytes = 0L; bytes < len; bytes += n) {
            if (this.readOffset == this.blockSize && !this.readBlock(len - bytes < this.blockSize)) {
                return -1L;
            }
            n = 0L;
            if (this.readOffset + (len - bytes) <= this.blockSize) {
                n = len - bytes;
            }
            else {
                n = this.blockSize - this.readOffset;
            }
            this.readOffset += (int)n;
        }
        return bytes;
    }
    
    @Override
    public void close() throws IOException {
        if (this.in != null && this.in != System.in) {
            this.in.close();
        }
    }
    
    public byte[] peek() throws IOException {
        if (this.readOffset == this.blockSize && !this.readBlock(true)) {
            return null;
        }
        final byte[] b = new byte[1024];
        System.arraycopy(this.blockBuffer, this.readOffset, b, 0, b.length);
        return b;
    }
    
    public byte[] readRecord() throws IOException {
        final byte[] result = new byte[1024];
        if (-1 == this.read(result, 0, result.length)) {
            throw new ShortFileException();
        }
        return result;
    }
    
    private boolean readBlock(final boolean decompress) throws IOException {
        boolean success = true;
        if (this.in == null) {
            throw new IOException("input buffer is closed");
        }
        if (!this.isCompressed || this.currBlkIdx == -1) {
            success = this.readFully(this.blockBuffer, 0, this.blockSize);
            this.bytesRead += this.blockSize;
        }
        else {
            if (!this.readFully(this.blockBuffer, 0, 4)) {
                return false;
            }
            this.bytesRead += 4L;
            final int h = DumpArchiveUtil.convert32(this.blockBuffer, 0);
            final boolean compressed = (h & 0x1) == 0x1;
            if (!compressed) {
                success = this.readFully(this.blockBuffer, 0, this.blockSize);
                this.bytesRead += this.blockSize;
            }
            else {
                final int flags = h >> 1 & 0x7;
                int length = h >> 4 & 0xFFFFFFF;
                final byte[] compBuffer = new byte[length];
                success = this.readFully(compBuffer, 0, length);
                this.bytesRead += length;
                if (!decompress) {
                    Arrays.fill(this.blockBuffer, (byte)0);
                }
                else {
                    switch (DumpArchiveConstants.COMPRESSION_TYPE.find(flags & 0x3)) {
                        case ZLIB: {
                            try {
                                final Inflater inflator = new Inflater();
                                inflator.setInput(compBuffer, 0, compBuffer.length);
                                length = inflator.inflate(this.blockBuffer);
                                if (length != this.blockSize) {
                                    throw new ShortFileException();
                                }
                                inflator.end();
                                break;
                            }
                            catch (DataFormatException e) {
                                throw new DumpArchiveException("bad data", e);
                            }
                        }
                        case BZLIB: {
                            throw new UnsupportedCompressionAlgorithmException("BZLIB2");
                        }
                        case LZO: {
                            throw new UnsupportedCompressionAlgorithmException("LZO");
                        }
                        default: {
                            throw new UnsupportedCompressionAlgorithmException();
                        }
                    }
                }
            }
        }
        ++this.currBlkIdx;
        this.readOffset = 0;
        return success;
    }
    
    private boolean readFully(final byte[] b, final int off, final int len) throws IOException {
        final int count = IOUtils.readFully(this.in, b, off, len);
        if (count < len) {
            throw new ShortFileException();
        }
        return true;
    }
    
    public long getBytesRead() {
        return this.bytesRead;
    }
}
