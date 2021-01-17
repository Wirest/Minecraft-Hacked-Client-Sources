// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io;

import java.io.File;
import java.io.IOException;

public class FileExistsException extends IOException
{
    private static final long serialVersionUID = 1L;
    
    public FileExistsException() {
    }
    
    public FileExistsException(final String message) {
        super(message);
    }
    
    public FileExistsException(final File file) {
        super("File " + file + " exists");
    }
}
