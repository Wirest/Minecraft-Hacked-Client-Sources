// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

public class HiddenFileFilter extends AbstractFileFilter implements Serializable
{
    public static final IOFileFilter HIDDEN;
    public static final IOFileFilter VISIBLE;
    
    protected HiddenFileFilter() {
    }
    
    @Override
    public boolean accept(final File file) {
        return file.isHidden();
    }
    
    static {
        HIDDEN = new HiddenFileFilter();
        VISIBLE = new NotFileFilter(HiddenFileFilter.HIDDEN);
    }
}
