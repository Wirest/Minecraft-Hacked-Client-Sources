// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.dump;

import java.io.IOException;

public class DumpArchiveException extends IOException
{
    private static final long serialVersionUID = 1L;
    
    public DumpArchiveException() {
    }
    
    public DumpArchiveException(final String msg) {
        super(msg);
    }
    
    public DumpArchiveException(final Throwable cause) {
        this.initCause(cause);
    }
    
    public DumpArchiveException(final String msg, final Throwable cause) {
        super(msg);
        this.initCause(cause);
    }
}
