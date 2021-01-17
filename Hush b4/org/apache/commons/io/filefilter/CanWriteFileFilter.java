// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

public class CanWriteFileFilter extends AbstractFileFilter implements Serializable
{
    public static final IOFileFilter CAN_WRITE;
    public static final IOFileFilter CANNOT_WRITE;
    
    protected CanWriteFileFilter() {
    }
    
    @Override
    public boolean accept(final File file) {
        return file.canWrite();
    }
    
    static {
        CAN_WRITE = new CanWriteFileFilter();
        CANNOT_WRITE = new NotFileFilter(CanWriteFileFilter.CAN_WRITE);
    }
}
