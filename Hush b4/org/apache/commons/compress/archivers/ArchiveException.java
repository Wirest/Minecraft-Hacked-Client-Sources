// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers;

public class ArchiveException extends Exception
{
    private static final long serialVersionUID = 2772690708123267100L;
    
    public ArchiveException(final String message) {
        super(message);
    }
    
    public ArchiveException(final String message, final Exception cause) {
        super(message);
        this.initCause(cause);
    }
}
