// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.ar;

import java.util.Date;
import java.io.File;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArArchiveEntry implements ArchiveEntry
{
    public static final String HEADER = "!<arch>\n";
    public static final String TRAILER = "`\n";
    private final String name;
    private final int userId;
    private final int groupId;
    private final int mode;
    private static final int DEFAULT_MODE = 33188;
    private final long lastModified;
    private final long length;
    
    public ArArchiveEntry(final String name, final long length) {
        this(name, length, 0, 0, 33188, System.currentTimeMillis() / 1000L);
    }
    
    public ArArchiveEntry(final String name, final long length, final int userId, final int groupId, final int mode, final long lastModified) {
        this.name = name;
        this.length = length;
        this.userId = userId;
        this.groupId = groupId;
        this.mode = mode;
        this.lastModified = lastModified;
    }
    
    public ArArchiveEntry(final File inputFile, final String entryName) {
        this(entryName, inputFile.isFile() ? inputFile.length() : 0L, 0, 0, 33188, inputFile.lastModified() / 1000L);
    }
    
    public long getSize() {
        return this.getLength();
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getUserId() {
        return this.userId;
    }
    
    public int getGroupId() {
        return this.groupId;
    }
    
    public int getMode() {
        return this.mode;
    }
    
    public long getLastModified() {
        return this.lastModified;
    }
    
    public Date getLastModifiedDate() {
        return new Date(1000L * this.getLastModified());
    }
    
    public long getLength() {
        return this.length;
    }
    
    public boolean isDirectory() {
        return false;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        final ArArchiveEntry other = (ArArchiveEntry)obj;
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        }
        else if (!this.name.equals(other.name)) {
            return false;
        }
        return true;
    }
}
