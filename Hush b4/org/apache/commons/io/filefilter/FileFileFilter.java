// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

public class FileFileFilter extends AbstractFileFilter implements Serializable
{
    public static final IOFileFilter FILE;
    
    protected FileFileFilter() {
    }
    
    @Override
    public boolean accept(final File file) {
        return file.isFile();
    }
    
    static {
        FILE = new FileFileFilter();
    }
}
