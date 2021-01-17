// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

public class EmptyFileFilter extends AbstractFileFilter implements Serializable
{
    public static final IOFileFilter EMPTY;
    public static final IOFileFilter NOT_EMPTY;
    
    protected EmptyFileFilter() {
    }
    
    @Override
    public boolean accept(final File file) {
        if (file.isDirectory()) {
            final File[] files = file.listFiles();
            return files == null || files.length == 0;
        }
        return file.length() == 0L;
    }
    
    static {
        EMPTY = new EmptyFileFilter();
        NOT_EMPTY = new NotFileFilter(EmptyFileFilter.EMPTY);
    }
}
