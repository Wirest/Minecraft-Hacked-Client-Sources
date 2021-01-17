// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers;

public class StreamingNotSupportedException extends ArchiveException
{
    private static final long serialVersionUID = 1L;
    private final String format;
    
    public StreamingNotSupportedException(final String format) {
        super("The " + format + " doesn't support streaming.");
        this.format = format;
    }
    
    public String getFormat() {
        return this.format;
    }
}
