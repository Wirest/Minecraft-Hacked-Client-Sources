// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.compressors.snappy;

import java.util.Arrays;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.BoundedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import org.apache.commons.compress.compressors.CompressorInputStream;

public class FramedSnappyCompressorInputStream extends CompressorInputStream
{
    static final long MASK_OFFSET = 2726488792L;
    private static final int STREAM_IDENTIFIER_TYPE = 255;
    private static final int COMPRESSED_CHUNK_TYPE = 0;
    private static final int UNCOMPRESSED_CHUNK_TYPE = 1;
    private static final int PADDING_CHUNK_TYPE = 254;
    private static final int MIN_UNSKIPPABLE_TYPE = 2;
    private static final int MAX_UNSKIPPABLE_TYPE = 127;
    private static final int MAX_SKIPPABLE_TYPE = 253;
    private static final byte[] SZ_SIGNATURE;
    private final PushbackInputStream in;
    private SnappyCompressorInputStream currentCompressedChunk;
    private final byte[] oneByte;
    private boolean endReached;
    private boolean inUncompressedChunk;
    private int uncompressedBytesRemaining;
    private long expectedChecksum;
    private final PureJavaCrc32C checksum;
    
    public FramedSnappyCompressorInputStream(final InputStream in) throws IOException {
        this.oneByte = new byte[1];
        this.expectedChecksum = -1L;
        this.checksum = new PureJavaCrc32C();
        this.in = new PushbackInputStream(in, 1);
        this.readStreamIdentifier();
    }
    
    @Override
    public int read() throws IOException {
        return (this.read(this.oneByte, 0, 1) == -1) ? -1 : (this.oneByte[0] & 0xFF);
    }
    
    @Override
    public void close() throws IOException {
        if (this.currentCompressedChunk != null) {
            this.currentCompressedChunk.close();
            this.currentCompressedChunk = null;
        }
        this.in.close();
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        int read = this.readOnce(b, off, len);
        if (read == -1) {
            this.readNextBlock();
            if (this.endReached) {
                return -1;
            }
            read = this.readOnce(b, off, len);
        }
        return read;
    }
    
    @Override
    public int available() throws IOException {
        if (this.inUncompressedChunk) {
            return Math.min(this.uncompressedBytesRemaining, this.in.available());
        }
        if (this.currentCompressedChunk != null) {
            return this.currentCompressedChunk.available();
        }
        return 0;
    }
    
    private int readOnce(final byte[] b, final int off, final int len) throws IOException {
        int read = -1;
        if (this.inUncompressedChunk) {
            final int amount = Math.min(this.uncompressedBytesRemaining, len);
            if (amount == 0) {
                return -1;
            }
            read = this.in.read(b, off, amount);
            if (read != -1) {
                this.uncompressedBytesRemaining -= read;
                this.count(read);
            }
        }
        else if (this.currentCompressedChunk != null) {
            final long before = this.currentCompressedChunk.getBytesRead();
            read = this.currentCompressedChunk.read(b, off, len);
            if (read == -1) {
                this.currentCompressedChunk.close();
                this.currentCompressedChunk = null;
            }
            else {
                this.count(this.currentCompressedChunk.getBytesRead() - before);
            }
        }
        if (read > 0) {
            this.checksum.update(b, off, read);
        }
        return read;
    }
    
    private void readNextBlock() throws IOException {
        this.verifyLastChecksumAndReset();
        this.inUncompressedChunk = false;
        final int type = this.readOneByte();
        if (type == -1) {
            this.endReached = true;
        }
        else if (type == 255) {
            this.in.unread(type);
            this.pushedBackBytes(1L);
            this.readStreamIdentifier();
            this.readNextBlock();
        }
        else if (type == 254 || (type > 127 && type <= 253)) {
            this.skipBlock();
            this.readNextBlock();
        }
        else {
            if (type >= 2 && type <= 127) {
                throw new IOException("unskippable chunk with type " + type + " (hex " + Integer.toHexString(type) + ")" + " detected.");
            }
            if (type == 1) {
                this.inUncompressedChunk = true;
                this.uncompressedBytesRemaining = this.readSize() - 4;
                this.expectedChecksum = unmask(this.readCrc());
            }
            else {
                if (type != 0) {
                    throw new IOException("unknown chunk type " + type + " detected.");
                }
                final long size = this.readSize() - 4;
                this.expectedChecksum = unmask(this.readCrc());
                this.currentCompressedChunk = new SnappyCompressorInputStream(new BoundedInputStream(this.in, size));
                this.count(this.currentCompressedChunk.getBytesRead());
            }
        }
    }
    
    private long readCrc() throws IOException {
        final byte[] b = new byte[4];
        final int read = IOUtils.readFully(this.in, b);
        this.count(read);
        if (read != 4) {
            throw new IOException("premature end of stream");
        }
        long crc = 0L;
        for (int i = 0; i < 4; ++i) {
            crc |= ((long)b[i] & 0xFFL) << 8 * i;
        }
        return crc;
    }
    
    static long unmask(long x) {
        x -= 2726488792L;
        x &= 0xFFFFFFFFL;
        return (x >> 17 | x << 15) & 0xFFFFFFFFL;
    }
    
    private int readSize() throws IOException {
        int b = 0;
        int sz = 0;
        for (int i = 0; i < 3; ++i) {
            b = this.readOneByte();
            if (b == -1) {
                throw new IOException("premature end of stream");
            }
            sz |= b << i * 8;
        }
        return sz;
    }
    
    private void skipBlock() throws IOException {
        final int size = this.readSize();
        final long read = IOUtils.skip(this.in, size);
        this.count(read);
        if (read != size) {
            throw new IOException("premature end of stream");
        }
    }
    
    private void readStreamIdentifier() throws IOException {
        final byte[] b = new byte[10];
        final int read = IOUtils.readFully(this.in, b);
        this.count(read);
        if (10 != read || !matches(b, 10)) {
            throw new IOException("Not a framed Snappy stream");
        }
    }
    
    private int readOneByte() throws IOException {
        final int b = this.in.read();
        if (b != -1) {
            this.count(1);
            return b & 0xFF;
        }
        return -1;
    }
    
    private void verifyLastChecksumAndReset() throws IOException {
        if (this.expectedChecksum >= 0L && this.expectedChecksum != this.checksum.getValue()) {
            throw new IOException("Checksum verification failed");
        }
        this.expectedChecksum = -1L;
        this.checksum.reset();
    }
    
    public static boolean matches(final byte[] signature, final int length) {
        if (length < FramedSnappyCompressorInputStream.SZ_SIGNATURE.length) {
            return false;
        }
        byte[] shortenedSig = signature;
        if (signature.length > FramedSnappyCompressorInputStream.SZ_SIGNATURE.length) {
            shortenedSig = new byte[FramedSnappyCompressorInputStream.SZ_SIGNATURE.length];
            System.arraycopy(signature, 0, shortenedSig, 0, FramedSnappyCompressorInputStream.SZ_SIGNATURE.length);
        }
        return Arrays.equals(shortenedSig, FramedSnappyCompressorInputStream.SZ_SIGNATURE);
    }
    
    static {
        SZ_SIGNATURE = new byte[] { -1, 6, 0, 0, 115, 78, 97, 80, 112, 89 };
    }
}
