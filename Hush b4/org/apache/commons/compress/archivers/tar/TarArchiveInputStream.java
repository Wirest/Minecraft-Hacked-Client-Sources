// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.tar;

import org.apache.commons.compress.archivers.ArchiveEntry;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.compress.utils.ArchiveUtils;
import java.io.ByteArrayOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import java.io.IOException;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import java.io.InputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;

public class TarArchiveInputStream extends ArchiveInputStream
{
    private static final int SMALL_BUFFER_SIZE = 256;
    private final byte[] SMALL_BUF;
    private final int recordSize;
    private final int blockSize;
    private boolean hasHitEOF;
    private long entrySize;
    private long entryOffset;
    private final InputStream is;
    private TarArchiveEntry currEntry;
    private final ZipEncoding encoding;
    
    public TarArchiveInputStream(final InputStream is) {
        this(is, 10240, 512);
    }
    
    public TarArchiveInputStream(final InputStream is, final String encoding) {
        this(is, 10240, 512, encoding);
    }
    
    public TarArchiveInputStream(final InputStream is, final int blockSize) {
        this(is, blockSize, 512);
    }
    
    public TarArchiveInputStream(final InputStream is, final int blockSize, final String encoding) {
        this(is, blockSize, 512, encoding);
    }
    
    public TarArchiveInputStream(final InputStream is, final int blockSize, final int recordSize) {
        this(is, blockSize, recordSize, null);
    }
    
    public TarArchiveInputStream(final InputStream is, final int blockSize, final int recordSize, final String encoding) {
        this.SMALL_BUF = new byte[256];
        this.is = is;
        this.hasHitEOF = false;
        this.encoding = ZipEncodingHelper.getZipEncoding(encoding);
        this.recordSize = recordSize;
        this.blockSize = blockSize;
    }
    
    @Override
    public void close() throws IOException {
        this.is.close();
    }
    
    public int getRecordSize() {
        return this.recordSize;
    }
    
    @Override
    public int available() throws IOException {
        if (this.entrySize - this.entryOffset > 2147483647L) {
            return Integer.MAX_VALUE;
        }
        return (int)(this.entrySize - this.entryOffset);
    }
    
    @Override
    public long skip(final long n) throws IOException {
        if (n <= 0L) {
            return 0L;
        }
        final long available = this.entrySize - this.entryOffset;
        final long skipped = this.is.skip(Math.min(n, available));
        this.count(skipped);
        this.entryOffset += skipped;
        return skipped;
    }
    
    @Override
    public synchronized void reset() {
    }
    
    public TarArchiveEntry getNextTarEntry() throws IOException {
        if (this.hasHitEOF) {
            return null;
        }
        if (this.currEntry != null) {
            IOUtils.skip(this, Long.MAX_VALUE);
            this.skipRecordPadding();
        }
        final byte[] headerBuf = this.getRecord();
        if (headerBuf == null) {
            return this.currEntry = null;
        }
        try {
            this.currEntry = new TarArchiveEntry(headerBuf, this.encoding);
        }
        catch (IllegalArgumentException e) {
            final IOException ioe = new IOException("Error detected parsing the header");
            ioe.initCause(e);
            throw ioe;
        }
        this.entryOffset = 0L;
        this.entrySize = this.currEntry.getSize();
        if (this.currEntry.isGNULongLinkEntry()) {
            final byte[] longLinkData = this.getLongNameData();
            if (longLinkData == null) {
                return null;
            }
            this.currEntry.setLinkName(this.encoding.decode(longLinkData));
        }
        if (this.currEntry.isGNULongNameEntry()) {
            final byte[] longNameData = this.getLongNameData();
            if (longNameData == null) {
                return null;
            }
            this.currEntry.setName(this.encoding.decode(longNameData));
        }
        if (this.currEntry.isPaxHeader()) {
            this.paxHeaders();
        }
        if (this.currEntry.isGNUSparse()) {
            this.readGNUSparse();
        }
        this.entrySize = this.currEntry.getSize();
        return this.currEntry;
    }
    
    private void skipRecordPadding() throws IOException {
        if (this.entrySize > 0L && this.entrySize % this.recordSize != 0L) {
            final long numRecords = this.entrySize / this.recordSize + 1L;
            final long padding = numRecords * this.recordSize - this.entrySize;
            final long skipped = IOUtils.skip(this.is, padding);
            this.count(skipped);
        }
    }
    
    protected byte[] getLongNameData() throws IOException {
        final ByteArrayOutputStream longName = new ByteArrayOutputStream();
        int length = 0;
        while ((length = this.read(this.SMALL_BUF)) >= 0) {
            longName.write(this.SMALL_BUF, 0, length);
        }
        this.getNextEntry();
        if (this.currEntry == null) {
            return null;
        }
        byte[] longNameData;
        for (longNameData = longName.toByteArray(), length = longNameData.length; length > 0 && longNameData[length - 1] == 0; --length) {}
        if (length != longNameData.length) {
            final byte[] l = new byte[length];
            System.arraycopy(longNameData, 0, l, 0, length);
            longNameData = l;
        }
        return longNameData;
    }
    
    private byte[] getRecord() throws IOException {
        byte[] headerBuf = this.readRecord();
        this.hasHitEOF = this.isEOFRecord(headerBuf);
        if (this.hasHitEOF && headerBuf != null) {
            this.tryToConsumeSecondEOFRecord();
            this.consumeRemainderOfLastBlock();
            headerBuf = null;
        }
        return headerBuf;
    }
    
    protected boolean isEOFRecord(final byte[] record) {
        return record == null || ArchiveUtils.isArrayZero(record, this.recordSize);
    }
    
    protected byte[] readRecord() throws IOException {
        final byte[] record = new byte[this.recordSize];
        final int readNow = IOUtils.readFully(this.is, record);
        this.count(readNow);
        if (readNow != this.recordSize) {
            return null;
        }
        return record;
    }
    
    private void paxHeaders() throws IOException {
        final Map<String, String> headers = this.parsePaxHeaders(this);
        this.getNextEntry();
        this.applyPaxHeadersToCurrentEntry(headers);
    }
    
    Map<String, String> parsePaxHeaders(final InputStream i) throws IOException {
        final Map<String, String> headers = new HashMap<String, String>();
        while (true) {
            int len = 0;
            int read = 0;
            int ch;
            while ((ch = i.read()) != -1) {
                ++read;
                if (ch == 32) {
                    final ByteArrayOutputStream coll = new ByteArrayOutputStream();
                    while ((ch = i.read()) != -1) {
                        ++read;
                        if (ch == 61) {
                            final String keyword = coll.toString("UTF-8");
                            final byte[] rest = new byte[len - read];
                            final int got = IOUtils.readFully(i, rest);
                            if (got != len - read) {
                                throw new IOException("Failed to read Paxheader. Expected " + (len - read) + " bytes, read " + got);
                            }
                            final String value = new String(rest, 0, len - read - 1, "UTF-8");
                            headers.put(keyword, value);
                            break;
                        }
                        else {
                            coll.write((byte)ch);
                        }
                    }
                    break;
                }
                len *= 10;
                len += ch - 48;
            }
            if (ch == -1) {
                return headers;
            }
        }
    }
    
    private void applyPaxHeadersToCurrentEntry(final Map<String, String> headers) {
        for (final Map.Entry<String, String> ent : headers.entrySet()) {
            final String key = ent.getKey();
            final String val = ent.getValue();
            if ("path".equals(key)) {
                this.currEntry.setName(val);
            }
            else if ("linkpath".equals(key)) {
                this.currEntry.setLinkName(val);
            }
            else if ("gid".equals(key)) {
                this.currEntry.setGroupId(Integer.parseInt(val));
            }
            else if ("gname".equals(key)) {
                this.currEntry.setGroupName(val);
            }
            else if ("uid".equals(key)) {
                this.currEntry.setUserId(Integer.parseInt(val));
            }
            else if ("uname".equals(key)) {
                this.currEntry.setUserName(val);
            }
            else if ("size".equals(key)) {
                this.currEntry.setSize(Long.parseLong(val));
            }
            else if ("mtime".equals(key)) {
                this.currEntry.setModTime((long)(Double.parseDouble(val) * 1000.0));
            }
            else if ("SCHILY.devminor".equals(key)) {
                this.currEntry.setDevMinor(Integer.parseInt(val));
            }
            else {
                if (!"SCHILY.devmajor".equals(key)) {
                    continue;
                }
                this.currEntry.setDevMajor(Integer.parseInt(val));
            }
        }
    }
    
    private void readGNUSparse() throws IOException {
        if (this.currEntry.isExtended()) {
            TarArchiveSparseEntry entry;
            do {
                final byte[] headerBuf = this.getRecord();
                if (headerBuf == null) {
                    this.currEntry = null;
                    break;
                }
                entry = new TarArchiveSparseEntry(headerBuf);
            } while (entry.isExtended());
        }
    }
    
    @Override
    public ArchiveEntry getNextEntry() throws IOException {
        return this.getNextTarEntry();
    }
    
    private void tryToConsumeSecondEOFRecord() throws IOException {
        boolean shouldReset = true;
        final boolean marked = this.is.markSupported();
        if (marked) {
            this.is.mark(this.recordSize);
        }
        try {
            shouldReset = !this.isEOFRecord(this.readRecord());
        }
        finally {
            if (shouldReset && marked) {
                this.pushedBackBytes(this.recordSize);
                this.is.reset();
            }
        }
    }
    
    @Override
    public int read(final byte[] buf, final int offset, int numToRead) throws IOException {
        int totalRead = 0;
        if (this.hasHitEOF || this.entryOffset >= this.entrySize) {
            return -1;
        }
        if (this.currEntry == null) {
            throw new IllegalStateException("No current tar entry");
        }
        numToRead = Math.min(numToRead, this.available());
        totalRead = this.is.read(buf, offset, numToRead);
        if (totalRead == -1) {
            if (numToRead > 0) {
                throw new IOException("Truncated TAR archive");
            }
            this.hasHitEOF = true;
        }
        else {
            this.count(totalRead);
            this.entryOffset += totalRead;
        }
        return totalRead;
    }
    
    @Override
    public boolean canReadEntryData(final ArchiveEntry ae) {
        if (ae instanceof TarArchiveEntry) {
            final TarArchiveEntry te = (TarArchiveEntry)ae;
            return !te.isGNUSparse();
        }
        return false;
    }
    
    public TarArchiveEntry getCurrentEntry() {
        return this.currEntry;
    }
    
    protected final void setCurrentEntry(final TarArchiveEntry e) {
        this.currEntry = e;
    }
    
    protected final boolean isAtEOF() {
        return this.hasHitEOF;
    }
    
    protected final void setAtEOF(final boolean b) {
        this.hasHitEOF = b;
    }
    
    private void consumeRemainderOfLastBlock() throws IOException {
        final long bytesReadOfLastBlock = this.getBytesRead() % this.blockSize;
        if (bytesReadOfLastBlock > 0L) {
            final long skipped = IOUtils.skip(this.is, this.blockSize - bytesReadOfLastBlock);
            this.count(skipped);
        }
    }
    
    public static boolean matches(final byte[] signature, final int length) {
        return length >= 265 && ((ArchiveUtils.matchAsciiBuffer("ustar\u0000", signature, 257, 6) && ArchiveUtils.matchAsciiBuffer("00", signature, 263, 2)) || (ArchiveUtils.matchAsciiBuffer("ustar ", signature, 257, 6) && (ArchiveUtils.matchAsciiBuffer(" \u0000", signature, 263, 2) || ArchiveUtils.matchAsciiBuffer("0\u0000", signature, 263, 2))) || (ArchiveUtils.matchAsciiBuffer("ustar\u0000", signature, 257, 6) && ArchiveUtils.matchAsciiBuffer("\u0000\u0000", signature, 263, 2)));
    }
}
