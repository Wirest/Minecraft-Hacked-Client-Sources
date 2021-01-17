// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.ar;

import java.io.EOFException;
import org.apache.commons.compress.archivers.ArchiveEntry;
import java.io.IOException;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.apache.commons.compress.utils.IOUtils;
import java.io.InputStream;
import org.apache.commons.compress.archivers.ArchiveInputStream;

public class ArArchiveInputStream extends ArchiveInputStream
{
    private final InputStream input;
    private long offset;
    private boolean closed;
    private ArArchiveEntry currentEntry;
    private byte[] namebuffer;
    private long entryOffset;
    private final byte[] NAME_BUF;
    private final byte[] LAST_MODIFIED_BUF;
    private final byte[] ID_BUF;
    private final byte[] FILE_MODE_BUF;
    private final byte[] LENGTH_BUF;
    static final String BSD_LONGNAME_PREFIX = "#1/";
    private static final int BSD_LONGNAME_PREFIX_LEN;
    private static final String BSD_LONGNAME_PATTERN = "^#1/\\d+";
    private static final String GNU_STRING_TABLE_NAME = "//";
    private static final String GNU_LONGNAME_PATTERN = "^/\\d+";
    
    public ArArchiveInputStream(final InputStream pInput) {
        this.offset = 0L;
        this.currentEntry = null;
        this.namebuffer = null;
        this.entryOffset = -1L;
        this.NAME_BUF = new byte[16];
        this.LAST_MODIFIED_BUF = new byte[12];
        this.ID_BUF = new byte[6];
        this.FILE_MODE_BUF = new byte[8];
        this.LENGTH_BUF = new byte[10];
        this.input = pInput;
        this.closed = false;
    }
    
    public ArArchiveEntry getNextArEntry() throws IOException {
        if (this.currentEntry != null) {
            final long entryEnd = this.entryOffset + this.currentEntry.getLength();
            IOUtils.skip(this, entryEnd - this.offset);
            this.currentEntry = null;
        }
        if (this.offset == 0L) {
            final byte[] expected = ArchiveUtils.toAsciiBytes("!<arch>\n");
            final byte[] realized = new byte[expected.length];
            final int read = IOUtils.readFully(this, realized);
            if (read != expected.length) {
                throw new IOException("failed to read header. Occured at byte: " + this.getBytesRead());
            }
            for (int i = 0; i < expected.length; ++i) {
                if (expected[i] != realized[i]) {
                    throw new IOException("invalid header " + ArchiveUtils.toAsciiString(realized));
                }
            }
        }
        if (this.offset % 2L != 0L && this.read() < 0) {
            return null;
        }
        if (this.input.available() == 0) {
            return null;
        }
        IOUtils.readFully(this, this.NAME_BUF);
        IOUtils.readFully(this, this.LAST_MODIFIED_BUF);
        IOUtils.readFully(this, this.ID_BUF);
        final int userId = this.asInt(this.ID_BUF, true);
        IOUtils.readFully(this, this.ID_BUF);
        IOUtils.readFully(this, this.FILE_MODE_BUF);
        IOUtils.readFully(this, this.LENGTH_BUF);
        final byte[] expected2 = ArchiveUtils.toAsciiBytes("`\n");
        final byte[] realized2 = new byte[expected2.length];
        final int read2 = IOUtils.readFully(this, realized2);
        if (read2 != expected2.length) {
            throw new IOException("failed to read entry trailer. Occured at byte: " + this.getBytesRead());
        }
        for (int j = 0; j < expected2.length; ++j) {
            if (expected2[j] != realized2[j]) {
                throw new IOException("invalid entry trailer. not read the content? Occured at byte: " + this.getBytesRead());
            }
        }
        this.entryOffset = this.offset;
        String temp = ArchiveUtils.toAsciiString(this.NAME_BUF).trim();
        if (isGNUStringTable(temp)) {
            this.currentEntry = this.readGNUStringTable(this.LENGTH_BUF);
            return this.getNextArEntry();
        }
        long len = this.asLong(this.LENGTH_BUF);
        if (temp.endsWith("/")) {
            temp = temp.substring(0, temp.length() - 1);
        }
        else if (this.isGNULongName(temp)) {
            final int off = Integer.parseInt(temp.substring(1));
            temp = this.getExtendedName(off);
        }
        else if (isBSDLongName(temp)) {
            temp = this.getBSDLongName(temp);
            final int nameLen = temp.length();
            len -= nameLen;
            this.entryOffset += nameLen;
        }
        return this.currentEntry = new ArArchiveEntry(temp, len, userId, this.asInt(this.ID_BUF, true), this.asInt(this.FILE_MODE_BUF, 8), this.asLong(this.LAST_MODIFIED_BUF));
    }
    
    private String getExtendedName(final int offset) throws IOException {
        if (this.namebuffer == null) {
            throw new IOException("Cannot process GNU long filename as no // record was found");
        }
        for (int i = offset; i < this.namebuffer.length; ++i) {
            if (this.namebuffer[i] == 10) {
                if (this.namebuffer[i - 1] == 47) {
                    --i;
                }
                return ArchiveUtils.toAsciiString(this.namebuffer, offset, i - offset);
            }
        }
        throw new IOException("Failed to read entry: " + offset);
    }
    
    private long asLong(final byte[] input) {
        return Long.parseLong(ArchiveUtils.toAsciiString(input).trim());
    }
    
    private int asInt(final byte[] input) {
        return this.asInt(input, 10, false);
    }
    
    private int asInt(final byte[] input, final boolean treatBlankAsZero) {
        return this.asInt(input, 10, treatBlankAsZero);
    }
    
    private int asInt(final byte[] input, final int base) {
        return this.asInt(input, base, false);
    }
    
    private int asInt(final byte[] input, final int base, final boolean treatBlankAsZero) {
        final String string = ArchiveUtils.toAsciiString(input).trim();
        if (string.length() == 0 && treatBlankAsZero) {
            return 0;
        }
        return Integer.parseInt(string, base);
    }
    
    @Override
    public ArchiveEntry getNextEntry() throws IOException {
        return this.getNextArEntry();
    }
    
    @Override
    public void close() throws IOException {
        if (!this.closed) {
            this.closed = true;
            this.input.close();
        }
        this.currentEntry = null;
    }
    
    @Override
    public int read(final byte[] b, final int off, final int len) throws IOException {
        int toRead = len;
        if (this.currentEntry != null) {
            final long entryEnd = this.entryOffset + this.currentEntry.getLength();
            if (len <= 0 || entryEnd <= this.offset) {
                return -1;
            }
            toRead = (int)Math.min(len, entryEnd - this.offset);
        }
        final int ret = this.input.read(b, off, toRead);
        this.count(ret);
        this.offset += ((ret > 0) ? ret : 0L);
        return ret;
    }
    
    public static boolean matches(final byte[] signature, final int length) {
        return length >= 8 && signature[0] == 33 && signature[1] == 60 && signature[2] == 97 && signature[3] == 114 && signature[4] == 99 && signature[5] == 104 && signature[6] == 62 && signature[7] == 10;
    }
    
    private static boolean isBSDLongName(final String name) {
        return name != null && name.matches("^#1/\\d+");
    }
    
    private String getBSDLongName(final String bsdLongName) throws IOException {
        final int nameLen = Integer.parseInt(bsdLongName.substring(ArArchiveInputStream.BSD_LONGNAME_PREFIX_LEN));
        final byte[] name = new byte[nameLen];
        final int read = IOUtils.readFully(this.input, name);
        this.count(read);
        if (read != nameLen) {
            throw new EOFException();
        }
        return ArchiveUtils.toAsciiString(name);
    }
    
    private static boolean isGNUStringTable(final String name) {
        return "//".equals(name);
    }
    
    private ArArchiveEntry readGNUStringTable(final byte[] length) throws IOException {
        final int bufflen = this.asInt(length);
        this.namebuffer = new byte[bufflen];
        final int read = IOUtils.readFully(this, this.namebuffer, 0, bufflen);
        if (read != bufflen) {
            throw new IOException("Failed to read complete // record: expected=" + bufflen + " read=" + read);
        }
        return new ArArchiveEntry("//", bufflen);
    }
    
    private boolean isGNULongName(final String name) {
        return name != null && name.matches("^/\\d+");
    }
    
    static {
        BSD_LONGNAME_PREFIX_LEN = "#1/".length();
    }
}
