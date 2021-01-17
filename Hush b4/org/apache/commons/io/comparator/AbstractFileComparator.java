// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.comparator;

import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.io.File;
import java.util.Comparator;

abstract class AbstractFileComparator implements Comparator<File>
{
    public File[] sort(final File... files) {
        if (files != null) {
            Arrays.sort(files, this);
        }
        return files;
    }
    
    public List<File> sort(final List<File> files) {
        if (files != null) {
            Collections.sort(files, this);
        }
        return files;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}
