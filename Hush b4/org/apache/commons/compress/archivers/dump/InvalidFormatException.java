// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.dump;

public class InvalidFormatException extends DumpArchiveException
{
    private static final long serialVersionUID = 1L;
    protected long offset;
    
    public InvalidFormatException() {
        super("there was an error decoding a tape segment");
    }
    
    public InvalidFormatException(final long offset) {
        super("there was an error decoding a tape segment header at offset " + offset + ".");
        this.offset = offset;
    }
    
    public long getOffset() {
        return this.offset;
    }
}
