// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.filefilter;

import java.io.File;

public abstract class AbstractFileFilter implements IOFileFilter
{
    @Override
    public boolean accept(final File file) {
        return this.accept(file.getParentFile(), file.getName());
    }
    
    @Override
    public boolean accept(final File dir, final String name) {
        return this.accept(new File(dir, name));
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
