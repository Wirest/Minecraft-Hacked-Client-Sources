// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.ar;

import java.io.File;
import org.apache.commons.compress.archivers.ArchiveEntry;
import java.io.IOException;
import org.apache.commons.compress.utils.ArchiveUtils;
import java.io.OutputStream;
import org.apache.commons.compress.archivers.ArchiveOutputStream;

public class ArArchiveOutputStream extends ArchiveOutputStream
{
    public static final int LONGFILE_ERROR = 0;
    public static final int LONGFILE_BSD = 1;
    private final OutputStream out;
    private long entryOffset;
    private ArArchiveEntry prevEntry;
    private boolean haveUnclosedEntry;
    private int longFileMode;
    private boolean finished;
    
    public ArArchiveOutputStream(final OutputStream pOut) {
        this.entryOffset = 0L;
        this.haveUnclosedEntry = false;
        this.longFileMode = 0;
        this.finished = false;
        this.out = pOut;
    }
    
    public void setLongFileMode(final int longFileMode) {
        this.longFileMode = longFileMode;
    }
    
    private long writeArchiveHeader() throws IOException {
        final byte[] header = ArchiveUtils.toAsciiBytes("!<arch>\n");
        this.out.write(header);
        return header.length;
    }
    
    @Override
    public void closeArchiveEntry() throws IOException {
        if (this.finished) {
            throw new IOException("Stream has already been finished");
        }
        if (this.prevEntry == null || !this.haveUnclosedEntry) {
            throw new IOException("No current entry to close");
        }
        if (this.entryOffset % 2L != 0L) {
            this.out.write(10);
        }
        this.haveUnclosedEntry = false;
    }
    
    @Override
    public void putArchiveEntry(final ArchiveEntry pEntry) throws IOException {
        if (this.finished) {
            throw new IOException("Stream has already been finished");
        }
        final ArArchiveEntry pArEntry = (ArArchiveEntry)pEntry;
        if (this.prevEntry == null) {
            this.writeArchiveHeader();
        }
        else {
            if (this.prevEntry.getLength() != this.entryOffset) {
                throw new IOException("length does not match entry (" + this.prevEntry.getLength() + " != " + this.entryOffset);
            }
            if (this.haveUnclosedEntry) {
                this.closeArchiveEntry();
            }
        }
        this.writeEntryHeader(this.prevEntry = pArEntry);
        this.entryOffset = 0L;
        this.haveUnclosedEntry = true;
    }
    
    private long fill(final long pOffset, final long pNewOffset, final char pFill) throws IOException {
        final long diff = pNewOffset - pOffset;
        if (diff > 0L) {
            for (int i = 0; i < diff; ++i) {
                this.write(pFill);
            }
        }
        return pNewOffset;
    }
    
    private long write(final String data) throws IOException {
        final byte[] bytes = data.getBytes("ascii");
        this.write(bytes);
        return bytes.length;
    }
    
    private long writeEntryHeader(final ArArchiveEntry pEntry) throws IOException {
        long offset = 0L;
        boolean mustAppendName = false;
        final String n = pEntry.getName();
        if (0 == this.longFileMode && n.length() > 16) {
            throw new IOException("filename too long, > 16 chars: " + n);
        }
        if (1 == this.longFileMode && (n.length() > 16 || n.indexOf(" ") > -1)) {
            mustAppendName = true;
            offset += this.write("#1/" + String.valueOf(n.length()));
        }
        else {
            offset += this.write(n);
        }
        offset = this.fill(offset, 16L, ' ');
        final String m = "" + pEntry.getLastModified();
        if (m.length() > 12) {
            throw new IOException("modified too long");
        }
        offset += this.write(m);
        offset = this.fill(offset, 28L, ' ');
        final String u = "" + pEntry.getUserId();
        if (u.length() > 6) {
            throw new IOException("userid too long");
        }
        offset += this.write(u);
        offset = this.fill(offset, 34L, ' ');
        final String g = "" + pEntry.getGroupId();
        if (g.length() > 6) {
            throw new IOException("groupid too long");
        }
        offset += this.write(g);
        offset = this.fill(offset, 40L, ' ');
        final String fm = "" + Integer.toString(pEntry.getMode(), 8);
        if (fm.length() > 8) {
            throw new IOException("filemode too long");
        }
        offset += this.write(fm);
        offset = this.fill(offset, 48L, ' ');
        final String s = String.valueOf(pEntry.getLength() + (mustAppendName ? n.length() : 0));
        if (s.length() > 10) {
            throw new IOException("size too long");
        }
        offset += this.write(s);
        offset = this.fill(offset, 58L, ' ');
        offset += this.write("`\n");
        if (mustAppendName) {
            offset += this.write(n);
        }
        return offset;
    }
    
    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        this.out.write(b, off, len);
        this.count(len);
        this.entryOffset += len;
    }
    
    @Override
    public void close() throws IOException {
        if (!this.finished) {
            this.finish();
        }
        this.out.close();
        this.prevEntry = null;
    }
    
    @Override
    public ArchiveEntry createArchiveEntry(final File inputFile, final String entryName) throws IOException {
        if (this.finished) {
            throw new IOException("Stream has already been finished");
        }
        return new ArArchiveEntry(inputFile, entryName);
    }
    
    @Override
    public void finish() throws IOException {
        if (this.haveUnclosedEntry) {
            throw new IOException("This archive contains unclosed entries.");
        }
        if (this.finished) {
            throw new IOException("This archive has already been finished");
        }
        this.finished = true;
    }
}
