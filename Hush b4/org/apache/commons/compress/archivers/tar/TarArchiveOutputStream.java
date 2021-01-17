// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.tar;

import java.nio.ByteBuffer;
import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.io.StringWriter;
import java.util.Map;
import java.util.HashMap;
import org.apache.commons.compress.archivers.ArchiveEntry;
import java.io.IOException;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;

public class TarArchiveOutputStream extends ArchiveOutputStream
{
    public static final int LONGFILE_ERROR = 0;
    public static final int LONGFILE_TRUNCATE = 1;
    public static final int LONGFILE_GNU = 2;
    public static final int LONGFILE_POSIX = 3;
    public static final int BIGNUMBER_ERROR = 0;
    public static final int BIGNUMBER_STAR = 1;
    public static final int BIGNUMBER_POSIX = 2;
    private long currSize;
    private String currName;
    private long currBytes;
    private final byte[] recordBuf;
    private int assemLen;
    private final byte[] assemBuf;
    private int longFileMode;
    private int bigNumberMode;
    private int recordsWritten;
    private final int recordsPerBlock;
    private final int recordSize;
    private boolean closed;
    private boolean haveUnclosedEntry;
    private boolean finished;
    private final OutputStream out;
    private final ZipEncoding encoding;
    private boolean addPaxHeadersForNonAsciiNames;
    private static final ZipEncoding ASCII;
    
    public TarArchiveOutputStream(final OutputStream os) {
        this(os, 10240, 512);
    }
    
    public TarArchiveOutputStream(final OutputStream os, final String encoding) {
        this(os, 10240, 512, encoding);
    }
    
    public TarArchiveOutputStream(final OutputStream os, final int blockSize) {
        this(os, blockSize, 512);
    }
    
    public TarArchiveOutputStream(final OutputStream os, final int blockSize, final String encoding) {
        this(os, blockSize, 512, encoding);
    }
    
    public TarArchiveOutputStream(final OutputStream os, final int blockSize, final int recordSize) {
        this(os, blockSize, recordSize, null);
    }
    
    public TarArchiveOutputStream(final OutputStream os, final int blockSize, final int recordSize, final String encoding) {
        this.longFileMode = 0;
        this.bigNumberMode = 0;
        this.closed = false;
        this.haveUnclosedEntry = false;
        this.finished = false;
        this.addPaxHeadersForNonAsciiNames = false;
        this.out = new CountingOutputStream(os);
        this.encoding = ZipEncodingHelper.getZipEncoding(encoding);
        this.assemLen = 0;
        this.assemBuf = new byte[recordSize];
        this.recordBuf = new byte[recordSize];
        this.recordSize = recordSize;
        this.recordsPerBlock = blockSize / recordSize;
    }
    
    public void setLongFileMode(final int longFileMode) {
        this.longFileMode = longFileMode;
    }
    
    public void setBigNumberMode(final int bigNumberMode) {
        this.bigNumberMode = bigNumberMode;
    }
    
    public void setAddPaxHeadersForNonAsciiNames(final boolean b) {
        this.addPaxHeadersForNonAsciiNames = b;
    }
    
    @Deprecated
    @Override
    public int getCount() {
        return (int)this.getBytesWritten();
    }
    
    @Override
    public long getBytesWritten() {
        return ((CountingOutputStream)this.out).getBytesWritten();
    }
    
    @Override
    public void finish() throws IOException {
        if (this.finished) {
            throw new IOException("This archive has already been finished");
        }
        if (this.haveUnclosedEntry) {
            throw new IOException("This archives contains unclosed entries.");
        }
        this.writeEOFRecord();
        this.writeEOFRecord();
        this.padAsNeeded();
        this.out.flush();
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
    
    public int getRecordSize() {
        return this.recordSize;
    }
    
    @Override
    public void putArchiveEntry(final ArchiveEntry archiveEntry) throws IOException {
        if (this.finished) {
            throw new IOException("Stream has already been finished");
        }
        final TarArchiveEntry entry = (TarArchiveEntry)archiveEntry;
        final Map<String, String> paxHeaders = new HashMap<String, String>();
        final String entryName = entry.getName();
        final boolean paxHeaderContainsPath = this.handleLongName(entryName, paxHeaders, "path", (byte)76, "file name");
        final String linkName = entry.getLinkName();
        final boolean paxHeaderContainsLinkPath = linkName != null && linkName.length() > 0 && this.handleLongName(linkName, paxHeaders, "linkpath", (byte)75, "link name");
        if (this.bigNumberMode == 2) {
            this.addPaxHeadersForBigNumbers(paxHeaders, entry);
        }
        else if (this.bigNumberMode != 1) {
            this.failForBigNumbers(entry);
        }
        if (this.addPaxHeadersForNonAsciiNames && !paxHeaderContainsPath && !TarArchiveOutputStream.ASCII.canEncode(entryName)) {
            paxHeaders.put("path", entryName);
        }
        if (this.addPaxHeadersForNonAsciiNames && !paxHeaderContainsLinkPath && (entry.isLink() || entry.isSymbolicLink()) && !TarArchiveOutputStream.ASCII.canEncode(linkName)) {
            paxHeaders.put("linkpath", linkName);
        }
        if (paxHeaders.size() > 0) {
            this.writePaxHeaders(entryName, paxHeaders);
        }
        entry.writeEntryHeader(this.recordBuf, this.encoding, this.bigNumberMode == 1);
        this.writeRecord(this.recordBuf);
        this.currBytes = 0L;
        if (entry.isDirectory()) {
            this.currSize = 0L;
        }
        else {
            this.currSize = entry.getSize();
        }
        this.currName = entryName;
        this.haveUnclosedEntry = true;
    }
    
    @Override
    public void closeArchiveEntry() throws IOException {
        if (this.finished) {
            throw new IOException("Stream has already been finished");
        }
        if (!this.haveUnclosedEntry) {
            throw new IOException("No current entry to close");
        }
        if (this.assemLen > 0) {
            for (int i = this.assemLen; i < this.assemBuf.length; ++i) {
                this.assemBuf[i] = 0;
            }
            this.writeRecord(this.assemBuf);
            this.currBytes += this.assemLen;
            this.assemLen = 0;
        }
        if (this.currBytes < this.currSize) {
            throw new IOException("entry '" + this.currName + "' closed at '" + this.currBytes + "' before the '" + this.currSize + "' bytes specified in the header were written");
        }
        this.haveUnclosedEntry = false;
    }
    
    @Override
    public void write(final byte[] wBuf, int wOffset, int numToWrite) throws IOException {
        if (!this.haveUnclosedEntry) {
            throw new IllegalStateException("No current tar entry");
        }
        if (this.currBytes + numToWrite > this.currSize) {
            throw new IOException("request to write '" + numToWrite + "' bytes exceeds size in header of '" + this.currSize + "' bytes for entry '" + this.currName + "'");
        }
        if (this.assemLen > 0) {
            if (this.assemLen + numToWrite >= this.recordBuf.length) {
                final int aLen = this.recordBuf.length - this.assemLen;
                System.arraycopy(this.assemBuf, 0, this.recordBuf, 0, this.assemLen);
                System.arraycopy(wBuf, wOffset, this.recordBuf, this.assemLen, aLen);
                this.writeRecord(this.recordBuf);
                this.currBytes += this.recordBuf.length;
                wOffset += aLen;
                numToWrite -= aLen;
                this.assemLen = 0;
            }
            else {
                System.arraycopy(wBuf, wOffset, this.assemBuf, this.assemLen, numToWrite);
                wOffset += numToWrite;
                this.assemLen += numToWrite;
                numToWrite = 0;
            }
        }
        while (numToWrite > 0) {
            if (numToWrite < this.recordBuf.length) {
                System.arraycopy(wBuf, wOffset, this.assemBuf, this.assemLen, numToWrite);
                this.assemLen += numToWrite;
                break;
            }
            this.writeRecord(wBuf, wOffset);
            final int num = this.recordBuf.length;
            this.currBytes += num;
            numToWrite -= num;
            wOffset += num;
        }
    }
    
    void writePaxHeaders(final String entryName, final Map<String, String> headers) throws IOException {
        String name = "./PaxHeaders.X/" + this.stripTo7Bits(entryName);
        if (name.length() >= 100) {
            name = name.substring(0, 99);
        }
        final TarArchiveEntry pex = new TarArchiveEntry(name, (byte)120);
        final StringWriter w = new StringWriter();
        for (final Map.Entry<String, String> h : headers.entrySet()) {
            final String key = h.getKey();
            final String value = h.getValue();
            int len = key.length() + value.length() + 3 + 2;
            String line = len + " " + key + "=" + value + "\n";
            for (int actualLength = line.getBytes("UTF-8").length; len != actualLength; len = actualLength, line = len + " " + key + "=" + value + "\n", actualLength = line.getBytes("UTF-8").length) {}
            w.write(line);
        }
        final byte[] data = w.toString().getBytes("UTF-8");
        pex.setSize(data.length);
        this.putArchiveEntry(pex);
        this.write(data);
        this.closeArchiveEntry();
    }
    
    private String stripTo7Bits(final String name) {
        final int length = name.length();
        final StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; ++i) {
            final char stripped = (char)(name.charAt(i) & '\u007f');
            if (this.shouldBeReplaced(stripped)) {
                result.append("_");
            }
            else {
                result.append(stripped);
            }
        }
        return result.toString();
    }
    
    private boolean shouldBeReplaced(final char c) {
        return c == '\0' || c == '/' || c == '\\';
    }
    
    private void writeEOFRecord() throws IOException {
        Arrays.fill(this.recordBuf, (byte)0);
        this.writeRecord(this.recordBuf);
    }
    
    @Override
    public void flush() throws IOException {
        this.out.flush();
    }
    
    @Override
    public ArchiveEntry createArchiveEntry(final File inputFile, final String entryName) throws IOException {
        if (this.finished) {
            throw new IOException("Stream has already been finished");
        }
        return new TarArchiveEntry(inputFile, entryName);
    }
    
    private void writeRecord(final byte[] record) throws IOException {
        if (record.length != this.recordSize) {
            throw new IOException("record to write has length '" + record.length + "' which is not the record size of '" + this.recordSize + "'");
        }
        this.out.write(record);
        ++this.recordsWritten;
    }
    
    private void writeRecord(final byte[] buf, final int offset) throws IOException {
        if (offset + this.recordSize > buf.length) {
            throw new IOException("record has length '" + buf.length + "' with offset '" + offset + "' which is less than the record size of '" + this.recordSize + "'");
        }
        this.out.write(buf, offset, this.recordSize);
        ++this.recordsWritten;
    }
    
    private void padAsNeeded() throws IOException {
        final int start = this.recordsWritten % this.recordsPerBlock;
        if (start != 0) {
            for (int i = start; i < this.recordsPerBlock; ++i) {
                this.writeEOFRecord();
            }
        }
    }
    
    private void addPaxHeadersForBigNumbers(final Map<String, String> paxHeaders, final TarArchiveEntry entry) {
        this.addPaxHeaderForBigNumber(paxHeaders, "size", entry.getSize(), 8589934591L);
        this.addPaxHeaderForBigNumber(paxHeaders, "gid", entry.getGroupId(), 2097151L);
        this.addPaxHeaderForBigNumber(paxHeaders, "mtime", entry.getModTime().getTime() / 1000L, 8589934591L);
        this.addPaxHeaderForBigNumber(paxHeaders, "uid", entry.getUserId(), 2097151L);
        this.addPaxHeaderForBigNumber(paxHeaders, "SCHILY.devmajor", entry.getDevMajor(), 2097151L);
        this.addPaxHeaderForBigNumber(paxHeaders, "SCHILY.devminor", entry.getDevMinor(), 2097151L);
        this.failForBigNumber("mode", entry.getMode(), 2097151L);
    }
    
    private void addPaxHeaderForBigNumber(final Map<String, String> paxHeaders, final String header, final long value, final long maxValue) {
        if (value < 0L || value > maxValue) {
            paxHeaders.put(header, String.valueOf(value));
        }
    }
    
    private void failForBigNumbers(final TarArchiveEntry entry) {
        this.failForBigNumber("entry size", entry.getSize(), 8589934591L);
        this.failForBigNumber("group id", entry.getGroupId(), 2097151L);
        this.failForBigNumber("last modification time", entry.getModTime().getTime() / 1000L, 8589934591L);
        this.failForBigNumber("user id", entry.getUserId(), 2097151L);
        this.failForBigNumber("mode", entry.getMode(), 2097151L);
        this.failForBigNumber("major device number", entry.getDevMajor(), 2097151L);
        this.failForBigNumber("minor device number", entry.getDevMinor(), 2097151L);
    }
    
    private void failForBigNumber(final String field, final long value, final long maxValue) {
        if (value < 0L || value > maxValue) {
            throw new RuntimeException(field + " '" + value + "' is too big ( > " + maxValue + " )");
        }
    }
    
    private boolean handleLongName(final String name, final Map<String, String> paxHeaders, final String paxHeaderName, final byte linkType, final String fieldName) throws IOException {
        final ByteBuffer encodedName = this.encoding.encode(name);
        final int len = encodedName.limit() - encodedName.position();
        if (len >= 100) {
            if (this.longFileMode == 3) {
                paxHeaders.put(paxHeaderName, name);
                return true;
            }
            if (this.longFileMode == 2) {
                final TarArchiveEntry longLinkEntry = new TarArchiveEntry("././@LongLink", linkType);
                longLinkEntry.setSize(len + 1);
                this.putArchiveEntry(longLinkEntry);
                this.write(encodedName.array(), encodedName.arrayOffset(), len);
                this.write(0);
                this.closeArchiveEntry();
            }
            else if (this.longFileMode != 1) {
                throw new RuntimeException(fieldName + " '" + name + "' is too long ( > " + 100 + " bytes)");
            }
        }
        return false;
    }
    
    static {
        ASCII = ZipEncodingHelper.getZipEncoding("ASCII");
    }
}
