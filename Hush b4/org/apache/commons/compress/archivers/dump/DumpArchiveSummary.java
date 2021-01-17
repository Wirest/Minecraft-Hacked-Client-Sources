// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.dump;

import java.util.Date;
import java.io.IOException;
import org.apache.commons.compress.archivers.zip.ZipEncoding;

public class DumpArchiveSummary
{
    private long dumpDate;
    private long previousDumpDate;
    private int volume;
    private String label;
    private int level;
    private String filesys;
    private String devname;
    private String hostname;
    private int flags;
    private int firstrec;
    private int ntrec;
    
    DumpArchiveSummary(final byte[] buffer, final ZipEncoding encoding) throws IOException {
        this.dumpDate = 1000L * DumpArchiveUtil.convert32(buffer, 4);
        this.previousDumpDate = 1000L * DumpArchiveUtil.convert32(buffer, 8);
        this.volume = DumpArchiveUtil.convert32(buffer, 12);
        this.label = DumpArchiveUtil.decode(encoding, buffer, 676, 16).trim();
        this.level = DumpArchiveUtil.convert32(buffer, 692);
        this.filesys = DumpArchiveUtil.decode(encoding, buffer, 696, 64).trim();
        this.devname = DumpArchiveUtil.decode(encoding, buffer, 760, 64).trim();
        this.hostname = DumpArchiveUtil.decode(encoding, buffer, 824, 64).trim();
        this.flags = DumpArchiveUtil.convert32(buffer, 888);
        this.firstrec = DumpArchiveUtil.convert32(buffer, 892);
        this.ntrec = DumpArchiveUtil.convert32(buffer, 896);
    }
    
    public Date getDumpDate() {
        return new Date(this.dumpDate);
    }
    
    public void setDumpDate(final Date dumpDate) {
        this.dumpDate = dumpDate.getTime();
    }
    
    public Date getPreviousDumpDate() {
        return new Date(this.previousDumpDate);
    }
    
    public void setPreviousDumpDate(final Date previousDumpDate) {
        this.previousDumpDate = previousDumpDate.getTime();
    }
    
    public int getVolume() {
        return this.volume;
    }
    
    public void setVolume(final int volume) {
        this.volume = volume;
    }
    
    public int getLevel() {
        return this.level;
    }
    
    public void setLevel(final int level) {
        this.level = level;
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public void setLabel(final String label) {
        this.label = label;
    }
    
    public String getFilesystem() {
        return this.filesys;
    }
    
    public void setFilesystem(final String filesystem) {
        this.filesys = filesystem;
    }
    
    public String getDevname() {
        return this.devname;
    }
    
    public void setDevname(final String devname) {
        this.devname = devname;
    }
    
    public String getHostname() {
        return this.hostname;
    }
    
    public void setHostname(final String hostname) {
        this.hostname = hostname;
    }
    
    public int getFlags() {
        return this.flags;
    }
    
    public void setFlags(final int flags) {
        this.flags = flags;
    }
    
    public int getFirstRecord() {
        return this.firstrec;
    }
    
    public void setFirstRecord(final int firstrec) {
        this.firstrec = firstrec;
    }
    
    public int getNTRec() {
        return this.ntrec;
    }
    
    public void setNTRec(final int ntrec) {
        this.ntrec = ntrec;
    }
    
    public boolean isNewHeader() {
        return (this.flags & 0x1) == 0x1;
    }
    
    public boolean isNewInode() {
        return (this.flags & 0x2) == 0x2;
    }
    
    public boolean isCompressed() {
        return (this.flags & 0x80) == 0x80;
    }
    
    public boolean isMetaDataOnly() {
        return (this.flags & 0x100) == 0x100;
    }
    
    public boolean isExtendedAttributes() {
        return (this.flags & 0x8000) == 0x8000;
    }
    
    @Override
    public int hashCode() {
        int hash = 17;
        if (this.label != null) {
            hash = this.label.hashCode();
        }
        hash += (int)(31L * this.dumpDate);
        if (this.hostname != null) {
            hash = 31 * this.hostname.hashCode() + 17;
        }
        if (this.devname != null) {
            hash = 31 * this.devname.hashCode() + 17;
        }
        return hash;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !o.getClass().equals(this.getClass())) {
            return false;
        }
        final DumpArchiveSummary rhs = (DumpArchiveSummary)o;
        return this.dumpDate == rhs.dumpDate && this.getHostname() != null && this.getHostname().equals(rhs.getHostname()) && this.getDevname() != null && this.getDevname().equals(rhs.getDevname());
    }
}
