// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

public class FalseFileFilter implements IOFileFilter, Serializable
{
    public static final IOFileFilter FALSE;
    public static final IOFileFilter INSTANCE;
    
    protected FalseFileFilter() {
    }
    
    @Override
    public boolean accept(final File file) {
        return false;
    }
    
    @Override
    public boolean accept(final File dir, final String name) {
        return false;
    }
    
    static {
        FALSE = new FalseFileFilter();
        INSTANCE = FalseFileFilter.FALSE;
    }
}
