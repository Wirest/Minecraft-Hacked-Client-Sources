// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.dump;

import org.apache.commons.compress.archivers.ArchiveEntry;
import java.util.Arrays;
import java.util.Stack;
import java.util.Iterator;
import java.io.EOFException;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.io.IOException;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import java.util.HashMap;
import org.apache.commons.compress.archivers.ArchiveException;
import java.io.InputStream;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import java.util.Queue;
import java.util.Map;
import org.apache.commons.compress.archivers.ArchiveInputStream;

public class DumpArchiveInputStream extends ArchiveInputStream
{
    private DumpArchiveSummary summary;
    private DumpArchiveEntry active;
    private boolean isClosed;
    private boolean hasHitEOF;
    private long entrySize;
    private long entryOffset;
    private int readIdx;
    private final byte[] readBuf;
    private byte[] blockBuffer;
    private int recordOffset;
    private long filepos;
    protected TapeInputStream raw;
    private final Map<Integer, Dirent> names;
    private final Map<Integer, DumpArchiveEntry> pending;
    private Queue<DumpArchiveEntry> queue;
    private final ZipEncoding encoding;
    
    public DumpArchiveInputStream(final InputStream is) throws ArchiveException {
        this(is, null);
    }
    
    public DumpArchiveInputStream(final InputStream is, final String encoding) throws ArchiveException {
        this.readBuf = new byte[1024];
        this.names = new HashMap<Integer, Dirent>();
        this.pending = new HashMap<Integer, DumpArchiveEntry>();
        this.raw = new TapeInputStream(is);
        this.hasHitEOF = false;
        this.encoding = ZipEncodingHelper.getZipEncoding(encoding);
        try {
            final byte[] headerBytes = this.raw.readRecord();
            if (!DumpArchiveUtil.verify(headerBytes)) {
                throw new UnrecognizedFormatException();
            }
            this.summary = new DumpArchiveSummary(headerBytes, this.encoding);
            this.raw.resetBlockSize(this.summary.getNTRec(), this.summary.isCompressed());
            this.blockBuffer = new byte[4096];
            this.readCLRI();
            this.readBITS();
        }
        catch (IOException ex) {
            throw new ArchiveException(ex.getMessage(), ex);
        }
        final Dirent root = new Dirent(2, 2, 4, ".");
        this.names.put(2, root);
        this.queue = new PriorityQueue<DumpArchiveEntry>(10, new Comparator<DumpArchiveEntry>() {
            public int compare(final DumpArchiveEntry p, final DumpArchiveEntry q) {
                if (p.getOriginalName() == null || q.getOriginalName() == null) {
                    return Integer.MAX_VALUE;
                }
                return p.getOriginalName().compareTo(q.getOriginalName());
            }
        });
    }
    
    @Deprecated
    @Override
    public int getCount() {
        return (int)this.getBytesRead();
    }
    
    @Override
    public long getBytesRead() {
        return this.raw.getBytesRead();
    }
    
    public DumpArchiveSummary getSummary() {
        return this.summary;
    }
    
    private void readCLRI() throws IOException {
        final byte[] buffer = this.raw.readRecord();
        if (!DumpArchiveUtil.verify(buffer)) {
            throw new InvalidFormatException();
        }
        this.active = DumpArchiveEntry.parse(buffer);
        if (DumpArchiveConstants.SEGMENT_TYPE.CLRI != this.active.getHeaderType()) {
            throw new InvalidFormatException();
        }
        if (this.raw.skip(1024 * this.active.getHeaderCount()) == -1L) {
            throw new EOFException();
        }
        this.readIdx = this.active.getHeaderCount();
    }
    
    private void readBITS() throws IOException {
        final byte[] buffer = this.raw.readRecord();
        if (!DumpArchiveUtil.verify(buffer)) {
            throw new InvalidFormatException();
        }
        this.active = DumpArchiveEntry.parse(buffer);
        if (DumpArchiveConstants.SEGMENT_TYPE.BITS != this.active.getHeaderType()) {
            throw new InvalidFormatException();
        }
        if (this.raw.skip(1024 * this.active.getHeaderCount()) == -1L) {
            throw new EOFException();
        }
        this.readIdx = this.active.getHeaderCount();
    }
    
    public DumpArchiveEntry getNextDumpEntry() throws IOException {
        return this.getNextEntry();
    }
    
    @Override
    public DumpArchiveEntry getNextEntry() throws IOException {
        DumpArchiveEntry entry = null;
        String path = null;
        if (!this.queue.isEmpty()) {
            return this.queue.remove();
        }
        while (entry == null) {
            if (this.hasHitEOF) {
                return null;
            }
            while (this.readIdx < this.active.getHeaderCount()) {
                if (!this.active.isSparseRecord(this.readIdx++) && this.raw.skip(1024L) == -1L) {
                    throw new EOFException();
                }
            }
            this.readIdx = 0;
            this.filepos = this.raw.getBytesRead();
            byte[] headerBytes = this.raw.readRecord();
            if (!DumpArchiveUtil.verify(headerBytes)) {
                throw new InvalidFormatException();
            }
            this.active = DumpArchiveEntry.parse(headerBytes);
            while (DumpArchiveConstants.SEGMENT_TYPE.ADDR == this.active.getHeaderType()) {
                if (this.raw.skip(1024 * (this.active.getHeaderCount() - this.active.getHeaderHoles())) == -1L) {
                    throw new EOFException();
                }
                this.filepos = this.raw.getBytesRead();
                headerBytes = this.raw.readRecord();
                if (!DumpArchiveUtil.verify(headerBytes)) {
                    throw new InvalidFormatException();
                }
                this.active = DumpArchiveEntry.parse(headerBytes);
            }
            if (DumpArchiveConstants.SEGMENT_TYPE.END == this.active.getHeaderType()) {
                this.hasHitEOF = true;
                return null;
            }
            entry = this.active;
            if (entry.isDirectory()) {
                this.readDirectoryEntry(this.active);
                this.entryOffset = 0L;
                this.entrySize = 0L;
                this.readIdx = this.active.getHeaderCount();
            }
            else {
                this.entryOffset = 0L;
                this.entrySize = this.active.getEntrySize();
                this.readIdx = 0;
            }
            this.recordOffset = this.readBuf.length;
            path = this.getPath(entry);
            if (path != null) {
                continue;
            }
            entry = null;
        }
        entry.setName(path);
        entry.setSimpleName(this.names.get(entry.getIno()).getName());
        entry.setOffset(this.filepos);
        return entry;
    }
    
    private void readDirectoryEntry(DumpArchiveEntry entry) throws IOException {
        long size = entry.getEntrySize();
        byte[] peekBytes;
        for (boolean first = true; first || DumpArchiveConstants.SEGMENT_TYPE.ADDR == entry.getHeaderType(); entry = DumpArchiveEntry.parse(peekBytes), first = false, size -= 1024L) {
            if (!first) {
                this.raw.readRecord();
            }
            if (!this.names.containsKey(entry.getIno()) && DumpArchiveConstants.SEGMENT_TYPE.INODE == entry.getHeaderType()) {
                this.pending.put(entry.getIno(), entry);
            }
            final int datalen = 1024 * entry.getHeaderCount();
            if (this.blockBuffer.length < datalen) {
                this.blockBuffer = new byte[datalen];
            }
            if (this.raw.read(this.blockBuffer, 0, datalen) != datalen) {
                throw new EOFException();
            }
            for (int reclen = 0, i = 0; i < datalen - 8 && i < size - 8L; i += reclen) {
                final int ino = DumpArchiveUtil.convert32(this.blockBuffer, i);
                reclen = DumpArchiveUtil.convert16(this.blockBuffer, i + 4);
                final byte type = this.blockBuffer[i + 6];
                final String name = DumpArchiveUtil.decode(this.encoding, this.blockBuffer, i + 8, this.blockBuffer[i + 7]);
                if (!".".equals(name)) {
                    if (!"..".equals(name)) {
                        final Dirent d = new Dirent(ino, entry.getIno(), type, name);
                        this.names.put(ino, d);
                        for (final Map.Entry<Integer, DumpArchiveEntry> e : this.pending.entrySet()) {
                            final String path = this.getPath(e.getValue());
                            if (path != null) {
                                e.getValue().setName(path);
                                e.getValue().setSimpleName(this.names.get(e.getKey()).getName());
                                this.queue.add(e.getValue());
                            }
                        }
                        for (final DumpArchiveEntry e2 : this.queue) {
                            this.pending.remove(e2.getIno());
                        }
                    }
                }
            }
            peekBytes = this.raw.peek();
            if (!DumpArchiveUtil.verify(peekBytes)) {
                throw new InvalidFormatException();
            }
        }
    }
    
    private String getPath(final DumpArchiveEntry entry) {
        final Stack<String> elements = new Stack<String>();
        Dirent dirent = null;
        int i = entry.getIno();
        while (true) {
            while (this.names.containsKey(i)) {
                dirent = this.names.get(i);
                elements.push(dirent.getName());
                if (dirent.getIno() == dirent.getParentIno()) {
                    if (elements.isEmpty()) {
                        this.pending.put(entry.getIno(), entry);
                        return null;
                    }
                    final StringBuilder sb = new StringBuilder(elements.pop());
                    while (!elements.isEmpty()) {
                        sb.append('/');
                        sb.append(elements.pop());
                    }
                    return sb.toString();
                }
                else {
                    i = dirent.getParentIno();
                }
            }
            elements.clear();
            continue;
        }
    }
    
    @Override
    public int read(final byte[] buf, int off, int len) throws IOException {
        int totalRead = 0;
        if (this.hasHitEOF || this.isClosed || this.entryOffset >= this.entrySize) {
            return -1;
        }
        if (this.active == null) {
            throw new IllegalStateException("No current dump entry");
        }
        if (len + this.entryOffset > this.entrySize) {
            len = (int)(this.entrySize - this.entryOffset);
        }
        while (len > 0) {
            final int sz = (len > this.readBuf.length - this.recordOffset) ? (this.readBuf.length - this.recordOffset) : len;
            if (this.recordOffset + sz <= this.readBuf.length) {
                System.arraycopy(this.readBuf, this.recordOffset, buf, off, sz);
                totalRead += sz;
                this.recordOffset += sz;
                len -= sz;
                off += sz;
            }
            if (len > 0) {
                if (this.readIdx >= 512) {
                    final byte[] headerBytes = this.raw.readRecord();
                    if (!DumpArchiveUtil.verify(headerBytes)) {
                        throw new InvalidFormatException();
                    }
                    this.active = DumpArchiveEntry.parse(headerBytes);
                    this.readIdx = 0;
                }
                if (!this.active.isSparseRecord(this.readIdx++)) {
                    final int r = this.raw.read(this.readBuf, 0, this.readBuf.length);
                    if (r != this.readBuf.length) {
                        throw new EOFException();
                    }
                }
                else {
                    Arrays.fill(this.readBuf, (byte)0);
                }
                this.recordOffset = 0;
            }
        }
        this.entryOffset += totalRead;
        return totalRead;
    }
    
    @Override
    public void close() throws IOException {
        if (!this.isClosed) {
            this.isClosed = true;
            this.raw.close();
        }
    }
    
    public static boolean matches(final byte[] buffer, final int length) {
        if (length < 32) {
            return false;
        }
        if (length >= 1024) {
            return DumpArchiveUtil.verify(buffer);
        }
        return 60012 == DumpArchiveUtil.convert32(buffer, 24);
    }
}
