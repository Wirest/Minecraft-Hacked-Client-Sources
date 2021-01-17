// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.comparator;

import java.util.List;
import java.io.File;
import java.util.Comparator;
import java.io.Serializable;

public class DefaultFileComparator extends AbstractFileComparator implements Serializable
{
    public static final Comparator<File> DEFAULT_COMPARATOR;
    public static final Comparator<File> DEFAULT_REVERSE;
    
    @Override
    public int compare(final File file1, final File file2) {
        return file1.compareTo(file2);
    }
    
    static {
        DEFAULT_COMPARATOR = new DefaultFileComparator();
        DEFAULT_REVERSE = new ReverseComparator(DefaultFileComparator.DEFAULT_COMPARATOR);
    }
}
