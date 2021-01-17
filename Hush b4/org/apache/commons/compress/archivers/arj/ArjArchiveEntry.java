// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.arj;

import org.apache.commons.compress.archivers.zip.ZipUtil;
import java.util.Date;
import java.util.regex.Matcher;
import java.io.File;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArjArchiveEntry implements ArchiveEntry
{
    private final LocalFileHeader localFileHeader;
    
    public ArjArchiveEntry() {
        this.localFileHeader = new LocalFileHeader();
    }
    
    ArjArchiveEntry(final LocalFileHeader localFileHeader) {
        this.localFileHeader = localFileHeader;
    }
    
    public String getName() {
        if ((this.localFileHeader.arjFlags & 0x10) != 0x0) {
            return this.localFileHeader.name.replaceAll("/", Matcher.quoteReplacement(File.separator));
        }
        return this.localFileHeader.name;
    }
    
    public long getSize() {
        return this.localFileHeader.originalSize;
    }
    
    public boolean isDirectory() {
        return this.localFileHeader.fileType == 3;
    }
    
    public Date getLastModifiedDate() {
        final long ts = this.isHostOsUnix() ? (this.localFileHeader.dateTimeModified * 1000L) : ZipUtil.dosToJavaTime(0xFFFFFFFFL & (long)this.localFileHeader.dateTimeModified);
        return new Date(ts);
    }
    
    public int getMode() {
        return this.localFileHeader.fileAccessMode;
    }
    
    public int getUnixMode() {
        return this.isHostOsUnix() ? this.getMode() : 0;
    }
    
    public int getHostOs() {
        return this.localFileHeader.hostOS;
    }
    
    public boolean isHostOsUnix() {
        return this.getHostOs() == 2 || this.getHostOs() == 8;
    }
    
    int getMethod() {
        return this.localFileHeader.method;
    }
    
    public static class HostOs
    {
        public static final int DOS = 0;
        public static final int PRIMOS = 1;
        public static final int UNIX = 2;
        public static final int AMIGA = 3;
        public static final int MAC_OS = 4;
        public static final int OS_2 = 5;
        public static final int APPLE_GS = 6;
        public static final int ATARI_ST = 7;
        public static final int NEXT = 8;
        public static final int VAX_VMS = 9;
        public static final int WIN95 = 10;
        public static final int WIN32 = 11;
    }
}
