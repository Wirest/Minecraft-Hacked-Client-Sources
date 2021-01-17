// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.cpio;

import java.io.File;
import java.nio.ByteBuffer;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.compress.archivers.ArchiveEntry;
import java.io.IOException;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import java.io.OutputStream;
import java.util.HashMap;
import org.apache.commons.compress.archivers.ArchiveOutputStream;

public class CpioArchiveOutputStream extends ArchiveOutputStream implements CpioConstants
{
    private CpioArchiveEntry entry;
    private boolean closed;
    private boolean finished;
    private final short entryFormat;
    private final HashMap<String, CpioArchiveEntry> names;
    private long crc;
    private long written;
    private final OutputStream out;
    private final int blockSize;
    private long nextArtificalDeviceAndInode;
    private final ZipEncoding encoding;
    
    public CpioArchiveOutputStream(final OutputStream out, final short format) {
        this(out, format, 512, "US-ASCII");
    }
    
    public CpioArchiveOutputStream(final OutputStream out, final short format, final int blockSize) {
        this(out, format, blockSize, "US-ASCII");
    }
    
    public CpioArchiveOutputStream(final OutputStream out, final short format, final int blockSize, final String encoding) {
        this.closed = false;
        this.names = new HashMap<String, CpioArchiveEntry>();
        this.crc = 0L;
        this.nextArtificalDeviceAndInode = 1L;
        this.out = out;
        switch (format) {
            case 1:
            case 2:
            case 4:
            case 8: {
                this.entryFormat = format;
                this.blockSize = blockSize;
                this.encoding = ZipEncodingHelper.getZipEncoding(encoding);
            }
            default: {
                throw new IllegalArgumentException("Unknown format: " + format);
            }
        }
    }
    
    public CpioArchiveOutputStream(final OutputStream out) {
        this(out, (short)1);
    }
    
    public CpioArchiveOutputStream(final OutputStream out, final String encoding) {
        this(out, (short)1, 512, encoding);
    }
    
    private void ensureOpen() throws IOException {
        if (this.closed) {
            throw new IOException("Stream closed");
        }
    }
    
    @Override
    public void putArchiveEntry(final ArchiveEntry entry) throws IOException {
        if (this.finished) {
            throw new IOException("Stream has already been finished");
        }
        final CpioArchiveEntry e = (CpioArchiveEntry)entry;
        this.ensureOpen();
        if (this.entry != null) {
            this.closeArchiveEntry();
        }
        if (e.getTime() == -1L) {
            e.setTime(System.currentTimeMillis() / 1000L);
        }
        final short format = e.getFormat();
        if (format != this.entryFormat) {
            throw new IOException("Header format: " + format + " does not match existing format: " + this.entryFormat);
        }
        if (this.names.put(e.getName(), e) != null) {
            throw new IOException("duplicate entry: " + e.getName());
        }
        this.writeHeader(e);
        this.entry = e;
        this.written = 0L;
    }
    
    private void writeHeader(final CpioArchiveEntry e) throws IOException {
        switch (e.getFormat()) {
            case 1: {
                this.out.write(ArchiveUtils.toAsciiBytes("070701"));
                this.count(6);
                this.writeNewEntry(e);
                break;
            }
            case 2: {
                this.out.write(ArchiveUtils.toAsciiBytes("070702"));
                this.count(6);
                this.writeNewEntry(e);
                break;
            }
            case 4: {
                this.out.write(ArchiveUtils.toAsciiBytes("070707"));
                this.count(6);
                this.writeOldAsciiEntry(e);
                break;
            }
            case 8: {
                final boolean swapHalfWord = true;
                this.writeBinaryLong(29127L, 2, swapHalfWord);
                this.writeOldBinaryEntry(e, swapHalfWord);
                break;
            }
            default: {
                throw new IOException("unknown format " + e.getFormat());
            }
        }
    }
    
    private void writeNewEntry(final CpioArchiveEntry entry) throws IOException {
        long inode = entry.getInode();
        long devMin = entry.getDeviceMin();
        if ("TRAILER!!!".equals(entry.getName())) {
            devMin = (inode = 0L);
        }
        else if (inode == 0L && devMin == 0L) {
            inode = (this.nextArtificalDeviceAndInode & -1L);
            devMin = (this.nextArtificalDeviceAndInode++ >> 32 & -1L);
        }
        else {
            this.nextArtificalDeviceAndInode = Math.max(this.nextArtificalDeviceAndInode, inode + 4294967296L * devMin) + 1L;
        }
        this.writeAsciiLong(inode, 8, 16);
        this.writeAsciiLong(entry.getMode(), 8, 16);
        this.writeAsciiLong(entry.getUID(), 8, 16);
        this.writeAsciiLong(entry.getGID(), 8, 16);
        this.writeAsciiLong(entry.getNumberOfLinks(), 8, 16);
        this.writeAsciiLong(entry.getTime(), 8, 16);
        this.writeAsciiLong(entry.getSize(), 8, 16);
        this.writeAsciiLong(entry.getDeviceMaj(), 8, 16);
        this.writeAsciiLong(devMin, 8, 16);
        this.writeAsciiLong(entry.getRemoteDeviceMaj(), 8, 16);
        this.writeAsciiLong(entry.getRemoteDeviceMin(), 8, 16);
        this.writeAsciiLong(entry.getName().length() + 1, 8, 16);
        this.writeAsciiLong(entry.getChksum(), 8, 16);
        this.writeCString(entry.getName());
        this.pad(entry.getHeaderPadCount());
    }
    
    private void writeOldAsciiEntry(final CpioArchiveEntry entry) throws IOException {
        long inode = entry.getInode();
        long device = entry.getDevice();
        if ("TRAILER!!!".equals(entry.getName())) {
            device = (inode = 0L);
        }
        else if (inode == 0L && device == 0L) {
            inode = (this.nextArtificalDeviceAndInode & 0x3FFFFL);
            device = (this.nextArtificalDeviceAndInode++ >> 18 & 0x3FFFFL);
        }
        else {
            this.nextArtificalDeviceAndInode = Math.max(this.nextArtificalDeviceAndInode, inode + 262144L * device) + 1L;
        }
        this.writeAsciiLong(device, 6, 8);
        this.writeAsciiLong(inode, 6, 8);
        this.writeAsciiLong(entry.getMode(), 6, 8);
        this.writeAsciiLong(entry.getUID(), 6, 8);
        this.writeAsciiLong(entry.getGID(), 6, 8);
        this.writeAsciiLong(entry.getNumberOfLinks(), 6, 8);
        this.writeAsciiLong(entry.getRemoteDevice(), 6, 8);
        this.writeAsciiLong(entry.getTime(), 11, 8);
        this.writeAsciiLong(entry.getName().length() + 1, 6, 8);
        this.writeAsciiLong(entry.getSize(), 11, 8);
        this.writeCString(entry.getName());
    }
    
    private void writeOldBinaryEntry(final CpioArchiveEntry entry, final boolean swapHalfWord) throws IOException {
        long inode = entry.getInode();
        long device = entry.getDevice();
        if ("TRAILER!!!".equals(entry.getName())) {
            device = (inode = 0L);
        }
        else if (inode == 0L && device == 0L) {
            inode = (this.nextArtificalDeviceAndInode & 0xFFFFL);
            device = (this.nextArtificalDeviceAndInode++ >> 16 & 0xFFFFL);
        }
        else {
            this.nextArtificalDeviceAndInode = Math.max(this.nextArtificalDeviceAndInode, inode + 65536L * device) + 1L;
        }
        this.writeBinaryLong(device, 2, swapHalfWord);
        this.writeBinaryLong(inode, 2, swapHalfWord);
        this.writeBinaryLong(entry.getMode(), 2, swapHalfWord);
        this.writeBinaryLong(entry.getUID(), 2, swapHalfWord);
        this.writeBinaryLong(entry.getGID(), 2, swapHalfWord);
        this.writeBinaryLong(entry.getNumberOfLinks(), 2, swapHalfWord);
        this.writeBinaryLong(entry.getRemoteDevice(), 2, swapHalfWord);
        this.writeBinaryLong(entry.getTime(), 4, swapHalfWord);
        this.writeBinaryLong(entry.getName().length() + 1, 2, swapHalfWord);
        this.writeBinaryLong(entry.getSize(), 4, swapHalfWord);
        this.writeCString(entry.getName());
        this.pad(entry.getHeaderPadCount());
    }
    
    @Override
    public void closeArchiveEntry() throws IOException {
        if (this.finished) {
            throw new IOException("Stream has already been finished");
        }
        this.ensureOpen();
        if (this.entry == null) {
            throw new IOException("Trying to close non-existent entry");
        }
        if (this.entry.getSize() != this.written) {
            throw new IOException("invalid entry size (expected " + this.entry.getSize() + " but got " + this.written + " bytes)");
        }
        this.pad(this.entry.getDataPadCount());
        if (this.entry.getFormat() == 2 && this.crc != this.entry.getChksum()) {
            throw new IOException("CRC Error");
        }
        this.entry = null;
        this.crc = 0L;
        this.written = 0L;
    }
    
    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        this.ensureOpen();
        if (off < 0 || len < 0 || off > b.length - len) {
            throw new IndexOutOfBoundsException();
        }
        if (len == 0) {
            return;
        }
        if (this.entry == null) {
            throw new IOException("no current CPIO entry");
        }
        if (this.written + len > this.entry.getSize()) {
            throw new IOException("attempt to write past end of STORED entry");
        }
        this.out.write(b, off, len);
        this.written += len;
        if (this.entry.getFormat() == 2) {
            for (int pos = 0; pos < len; ++pos) {
                this.crc += (b[pos] & 0xFF);
            }
        }
        this.count(len);
    }
    
    @Override
    public void finish() throws IOException {
        this.ensureOpen();
        if (this.finished) {
            throw new IOException("This archive has already been finished");
        }
        if (this.entry != null) {
            throw new IOException("This archive contains unclosed entries.");
        }
        (this.entry = new CpioArchiveEntry(this.entryFormat)).setName("TRAILER!!!");
        this.entry.setNumberOfLinks(1L);
        this.writeHeader(this.entry);
        this.closeArchiveEntry();
        final int lengthOfLastBlock = (int)(this.getBytesWritten() % this.blockSize);
        if (lengthOfLastBlock != 0) {
            this.pad(this.blockSize - lengthOfLastBlock);
        }
        this.finished = true;
    }
    
    @Override
    public void close() throws IOException {
        if (!this.finished) {
            this.finish();
        }
        if (!this.closed) {
            this.out.close();
            this.closed = true;
        }
    }
    
    private void pad(final int count) throws IOException {
        if (count > 0) {
            final byte[] buff = new byte[count];
            this.out.write(buff);
            this.count(count);
        }
    }
    
    private void writeBinaryLong(final long number, final int length, final boolean swapHalfWord) throws IOException {
        final byte[] tmp = CpioUtil.long2byteArray(number, length, swapHalfWord);
        this.out.write(tmp);
        this.count(tmp.length);
    }
    
    private void writeAsciiLong(final long number, final int length, final int radix) throws IOException {
        final StringBuilder tmp = new StringBuilder();
        if (radix == 16) {
            tmp.append(Long.toHexString(number));
        }
        else if (radix == 8) {
            tmp.append(Long.toOctalString(number));
        }
        else {
            tmp.append(Long.toString(number));
        }
        String tmpStr;
        if (tmp.length() <= length) {
            final long insertLength = length - tmp.length();
            for (int pos = 0; pos < insertLength; ++pos) {
                tmp.insert(0, "0");
            }
            tmpStr = tmp.toString();
        }
        else {
            tmpStr = tmp.substring(tmp.length() - length);
        }
        final byte[] b = ArchiveUtils.toAsciiBytes(tmpStr);
        this.out.write(b);
        this.count(b.length);
    }
    
    private void writeCString(final String str) throws IOException {
        final ByteBuffer buf = this.encoding.encode(str);
        final int len = buf.limit() - buf.position();
        this.out.write(buf.array(), buf.arrayOffset(), len);
        this.out.write(0);
        this.count(len + 1);
    }
    
    @Override
    public ArchiveEntry createArchiveEntry(final File inputFile, final String entryName) throws IOException {
        if (this.finished) {
            throw new IOException("Stream has already been finished");
        }
        return new CpioArchiveEntry(inputFile, entryName);
    }
}
