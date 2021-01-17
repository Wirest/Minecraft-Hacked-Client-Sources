// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.compressors.gzip;

public class GzipParameters
{
    private int compressionLevel;
    private long modificationTime;
    private String filename;
    private String comment;
    private int operatingSystem;
    
    public GzipParameters() {
        this.compressionLevel = -1;
        this.operatingSystem = 255;
    }
    
    public int getCompressionLevel() {
        return this.compressionLevel;
    }
    
    public void setCompressionLevel(final int compressionLevel) {
        if (compressionLevel < -1 || compressionLevel > 9) {
            throw new IllegalArgumentException("Invalid gzip compression level: " + compressionLevel);
        }
        this.compressionLevel = compressionLevel;
    }
    
    public long getModificationTime() {
        return this.modificationTime;
    }
    
    public void setModificationTime(final long modificationTime) {
        this.modificationTime = modificationTime;
    }
    
    public String getFilename() {
        return this.filename;
    }
    
    public void setFilename(final String filename) {
        this.filename = filename;
    }
    
    public String getComment() {
        return this.comment;
    }
    
    public void setComment(final String comment) {
        this.comment = comment;
    }
    
    public int getOperatingSystem() {
        return this.operatingSystem;
    }
    
    public void setOperatingSystem(final int operatingSystem) {
        this.operatingSystem = operatingSystem;
    }
}
