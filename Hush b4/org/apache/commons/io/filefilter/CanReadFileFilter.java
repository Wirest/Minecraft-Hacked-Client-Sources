// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

public class CanReadFileFilter extends AbstractFileFilter implements Serializable
{
    public static final IOFileFilter CAN_READ;
    public static final IOFileFilter CANNOT_READ;
    public static final IOFileFilter READ_ONLY;
    
    protected CanReadFileFilter() {
    }
    
    @Override
    public boolean accept(final File file) {
        return file.canRead();
    }
    
    static {
        CAN_READ = new CanReadFileFilter();
        CANNOT_READ = new NotFileFilter(CanReadFileFilter.CAN_READ);
        READ_ONLY = new AndFileFilter(CanReadFileFilter.CAN_READ, CanWriteFileFilter.CANNOT_WRITE);
    }
}
