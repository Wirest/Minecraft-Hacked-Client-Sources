// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.cpio;

import org.apache.commons.compress.archivers.ArchiveEntry;
import java.io.EOFException;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.ArchiveUtils;
import java.io.IOException;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import java.io.InputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;

public class CpioArchiveInputStream extends ArchiveInputStream implements CpioConstants
{
    private boolean closed;
    private CpioArchiveEntry entry;
    private long entryBytesRead;
    private boolean entryEOF;
    private final byte[] tmpbuf;
    private long crc;
    private final InputStream in;
    private final byte[] TWO_BYTES_BUF;
    private final byte[] FOUR_BYTES_BUF;
    private final byte[] SIX_BYTES_BUF;
    private final int blockSize;
    private final ZipEncoding encoding;
    
    public CpioArchiveInputStream(final InputStream in) {
        this(in, 512, "US-ASCII");
    }
    
    public CpioArchiveInputStream(final InputStream in, final String encoding) {
        this(in, 512, encoding);
    }
    
    public CpioArchiveInputStream(final InputStream in, final int blockSize) {
        this(in, blockSize, "US-ASCII");
    }
    
    public CpioArchiveInputStream(final InputStream in, final int blockSize, final String encoding) {
        this.closed = false;
        this.entryBytesRead = 0L;
        this.entryEOF = false;
        this.tmpbuf = new byte[4096];
        this.crc = 0L;
        this.TWO_BYTES_BUF = new byte[2];
        this.FOUR_BYTES_BUF = new byte[4];
        this.SIX_BYTES_BUF = new byte[6];
        this.in = in;
        this.blockSize = blockSize;
        this.encoding = ZipEncodingHelper.getZipEncoding(encoding);
    }
    
    @Override
    public int available() throws IOException {
        this.ensureOpen();
        if (this.entryEOF) {
            return 0;
        }
        return 1;
    }
    
    @Override
    public void close() throws IOException {
        if (!this.closed) {
            this.in.close();
            this.closed = true;
        }
    }
    
    private void closeEntry() throws IOException {
        while (this.skip(2147483647L) == 2147483647L) {}
    }
    
    private void ensureOpen() throws IOException {
        if (this.closed) {
            throw new IOException("Stream closed");
        }
    }
    
    public CpioArchiveEntry getNextCPIOEntry() throws IOException {
        this.ensureOpen();
        if (this.entry != null) {
            this.closeEntry();
        }
        this.readFully(this.TWO_BYTES_BUF, 0, this.TWO_BYTES_BUF.length);
        if (CpioUtil.byteArray2long(this.TWO_BYTES_BUF, false) == 29127L) {
            this.entry = this.readOldBinaryEntry(false);
        }
        else if (CpioUtil.byteArray2long(this.TWO_BYTES_BUF, true) == 29127L) {
            this.entry = this.readOldBinaryEntry(true);
        }
        else {
            System.arraycopy(this.TWO_BYTES_BUF, 0, this.SIX_BYTES_BUF, 0, this.TWO_BYTES_BUF.length);
            this.readFully(this.SIX_BYTES_BUF, this.TWO_BYTES_BUF.length, this.FOUR_BYTES_BUF.length);
            final String magicString = ArchiveUtils.toAsciiString(this.SIX_BYTES_BUF);
            if (magicString.equals("070701")) {
                this.entry = this.readNewEntry(false);
            }
            else if (magicString.equals("070702")) {
                this.entry = this.readNewEntry(true);
            }
            else {
                if (!magicString.equals("070707")) {
                    throw new IOException("Unknown magic [" + magicString + "]. Occured at byte: " + this.getBytesRead());
                }
                this.entry = this.readOldAsciiEntry();
            }
        }
        this.entryBytesRead = 0L;
        this.entryEOF = false;
        this.crc = 0L;
        if (this.entry.getName().equals("TRAILER!!!")) {
            this.entryEOF = true;
            this.skipRemainderOfLastBlock();
            return null;
        }
        return this.entry;
    }
    
    private void skip(final int bytes) throws IOException {
        if (bytes > 0) {
            this.readFully(this.FOUR_BYTES_BUF, 0, bytes);
        }
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        this.ensureOpen();
        if (off < 0 || len < 0 || off > b.length - len) {
            throw new IndexOutOfBoundsException();
        }
        if (len == 0) {
            return 0;
        }
        if (this.entry == null || this.entryEOF) {
            return -1;
        }
        if (this.entryBytesRead == this.entry.getSize()) {
            this.skip(this.entry.getDataPadCount());
            this.entryEOF = true;
            if (this.entry.getFormat() == 2 && this.crc != this.entry.getChksum()) {
                throw new IOException("CRC Error. Occured at byte: " + this.getBytesRead());
            }
            return -1;
        }
        else {
            final int tmplength = (int)Math.min(len, this.entry.getSize() - this.entryBytesRead);
            if (tmplength < 0) {
                return -1;
            }
            final int tmpread = this.readFully(b, off, tmplength);
            if (this.entry.getFormat() == 2) {
                for (int pos = 0; pos < tmpread; ++pos) {
                    this.crc += (b[pos] & 0xFF);
                }
            }
            this.entryBytesRead += tmpread;
            return tmpread;
        }
    }
    
    private final int readFully(final byte[] b, final int off, final int len) throws IOException {
        final int count = IOUtils.readFully(this.in, b, off, len);
        this.count(count);
        if (count < len) {
            throw new EOFException();
        }
        return count;
    }
    
    private long readBinaryLong(final int length, final boolean swapHalfWord) throws IOException {
        final byte[] tmp = new byte[length];
        this.readFully(tmp, 0, tmp.length);
        return CpioUtil.byteArray2long(tmp, swapHalfWord);
    }
    
    private long readAsciiLong(final int length, final int radix) throws IOException {
        final byte[] tmpBuffer = new byte[length];
        this.readFully(tmpBuffer, 0, tmpBuffer.length);
        return Long.parseLong(ArchiveUtils.toAsciiString(tmpBuffer), radix);
    }
    
    private CpioArchiveEntry readNewEntry(final boolean hasCrc) throws IOException {
        CpioArchiveEntry ret;
        if (hasCrc) {
            ret = new CpioArchiveEntry((short)2);
        }
        else {
            ret = new CpioArchiveEntry((short)1);
        }
        ret.setInode(this.readAsciiLong(8, 16));
        final long mode = this.readAsciiLong(8, 16);
        if (CpioUtil.fileType(mode) != 0L) {
            ret.setMode(mode);
        }
        ret.setUID(this.readAsciiLong(8, 16));
        ret.setGID(this.readAsciiLong(8, 16));
        ret.setNumberOfLinks(this.readAsciiLong(8, 16));
        ret.setTime(this.readAsciiLong(8, 16));
        ret.setSize(this.readAsciiLong(8, 16));
        ret.setDeviceMaj(this.readAsciiLong(8, 16));
        ret.setDeviceMin(this.readAsciiLong(8, 16));
        ret.setRemoteDeviceMaj(this.readAsciiLong(8, 16));
        ret.setRemoteDeviceMin(this.readAsciiLong(8, 16));
        final long namesize = this.readAsciiLong(8, 16);
        ret.setChksum(this.readAsciiLong(8, 16));
        final String name = this.readCString((int)namesize);
        ret.setName(name);
        if (CpioUtil.fileType(mode) == 0L && !name.equals("TRAILER!!!")) {
            throw new IOException("Mode 0 only allowed in the trailer. Found entry name: " + name + " Occured at byte: " + this.getBytesRead());
        }
        this.skip(ret.getHeaderPadCount());
        return ret;
    }
    
    private CpioArchiveEntry readOldAsciiEntry() throws IOException {
        final CpioArchiveEntry ret = new CpioArchiveEntry((short)4);
        ret.setDevice(this.readAsciiLong(6, 8));
        ret.setInode(this.readAsciiLong(6, 8));
        final long mode = this.readAsciiLong(6, 8);
        if (CpioUtil.fileType(mode) != 0L) {
            ret.setMode(mode);
        }
        ret.setUID(this.readAsciiLong(6, 8));
        ret.setGID(this.readAsciiLong(6, 8));
        ret.setNumberOfLinks(this.readAsciiLong(6, 8));
        ret.setRemoteDevice(this.readAsciiLong(6, 8));
        ret.setTime(this.readAsciiLong(11, 8));
        final long namesize = this.readAsciiLong(6, 8);
        ret.setSize(this.readAsciiLong(11, 8));
        final String name = this.readCString((int)namesize);
        ret.setName(name);
        if (CpioUtil.fileType(mode) == 0L && !name.equals("TRAILER!!!")) {
            throw new IOException("Mode 0 only allowed in the trailer. Found entry: " + name + " Occured at byte: " + this.getBytesRead());
        }
        return ret;
    }
    
    private CpioArchiveEntry readOldBinaryEntry(final boolean swapHalfWord) throws IOException {
        final CpioArchiveEntry ret = new CpioArchiveEntry((short)8);
        ret.setDevice(this.readBinaryLong(2, swapHalfWord));
        ret.setInode(this.readBinaryLong(2, swapHalfWord));
        final long mode = this.readBinaryLong(2, swapHalfWord);
        if (CpioUtil.fileType(mode) != 0L) {
            ret.setMode(mode);
        }
        ret.setUID(this.readBinaryLong(2, swapHalfWord));
        ret.setGID(this.readBinaryLong(2, swapHalfWord));
        ret.setNumberOfLinks(this.readBinaryLong(2, swapHalfWord));
        ret.setRemoteDevice(this.readBinaryLong(2, swapHalfWord));
        ret.setTime(this.readBinaryLong(4, swapHalfWord));
        final long namesize = this.readBinaryLong(2, swapHalfWord);
        ret.setSize(this.readBinaryLong(4, swapHalfWord));
        final String name = this.readCString((int)namesize);
        ret.setName(name);
        if (CpioUtil.fileType(mode) == 0L && !name.equals("TRAILER!!!")) {
            throw new IOException("Mode 0 only allowed in the trailer. Found entry: " + name + "Occured at byte: " + this.getBytesRead());
        }
        this.skip(ret.getHeaderPadCount());
        return ret;
    }
    
    private String readCString(final int length) throws IOException {
        final byte[] tmpBuffer = new byte[length - 1];
        this.readFully(tmpBuffer, 0, tmpBuffer.length);
        this.in.read();
        return this.encoding.decode(tmpBuffer);
    }
    
    @Override
    public long skip(final long n) throws IOException {
        if (n < 0L) {
            throw new IllegalArgumentException("negative skip length");
        }
        this.ensureOpen();
        int max;
        int total;
        int len;
        for (max = (int)Math.min(n, 2147483647L), total = 0; total < max; total += len) {
            len = max - total;
            if (len > this.tmpbuf.length) {
                len = this.tmpbuf.length;
            }
            len = this.read(this.tmpbuf, 0, len);
            if (len == -1) {
                this.entryEOF = true;
                break;
            }
        }
        return total;
    }
    
    @Override
    public ArchiveEntry getNextEntry() throws IOException {
        return this.getNextCPIOEntry();
    }
    
    private void skipRemainderOfLastBlock() throws IOException {
        final long readFromLastBlock = this.getBytesRead() % this.blockSize;
        long skipped;
        for (long remainingBytes = (readFromLastBlock == 0L) ? 0L : (this.blockSize - readFromLastBlock); remainingBytes > 0L; remainingBytes -= skipped) {
            skipped = this.skip(this.blockSize - readFromLastBlock);
            if (skipped <= 0L) {
                break;
            }
        }
    }
    
    public static boolean matches(final byte[] signature, final int length) {
        return length >= 6 && ((signature[0] == 113 && (signature[1] & 0xFF) == 0xC7) || (signature[1] == 113 && (signature[0] & 0xFF) == 0xC7) || (signature[0] == 48 && signature[1] == 55 && signature[2] == 48 && signature[3] == 55 && signature[4] == 48 && (signature[5] == 49 || signature[5] == 50 || signature[5] == 55)));
    }
}
