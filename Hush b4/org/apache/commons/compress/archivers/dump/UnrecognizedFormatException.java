// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.dump;

public class UnrecognizedFormatException extends DumpArchiveException
{
    private static final long serialVersionUID = 1L;
    
    public UnrecognizedFormatException() {
        super("this is not a recognized format.");
    }
}
