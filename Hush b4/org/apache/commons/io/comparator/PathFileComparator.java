// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.comparator;

import java.util.List;
import org.apache.commons.io.IOCase;
import java.io.File;
import java.util.Comparator;
import java.io.Serializable;

public class PathFileComparator extends AbstractFileComparator implements Serializable
{
    public static final Comparator<File> PATH_COMPARATOR;
    public static final Comparator<File> PATH_REVERSE;
    public static final Comparator<File> PATH_INSENSITIVE_COMPARATOR;
    public static final Comparator<File> PATH_INSENSITIVE_REVERSE;
    public static final Comparator<File> PATH_SYSTEM_COMPARATOR;
    public static final Comparator<File> PATH_SYSTEM_REVERSE;
    private final IOCase caseSensitivity;
    
    public PathFileComparator() {
        this.caseSensitivity = IOCase.SENSITIVE;
    }
    
    public PathFileComparator(final IOCase caseSensitivity) {
        this.caseSensitivity = ((caseSensitivity == null) ? IOCase.SENSITIVE : caseSensitivity);
    }
    
    @Override
    public int compare(final File file1, final File file2) {
        return this.caseSensitivity.checkCompareTo(file1.getPath(), file2.getPath());
    }
    
    @Override
    public String toString() {
        return super.toString() + "[caseSensitivity=" + this.caseSensitivity + "]";
    }
    
    static {
        PATH_COMPARATOR = new PathFileComparator();
        PATH_REVERSE = new ReverseComparator(PathFileComparator.PATH_COMPARATOR);
        PATH_INSENSITIVE_COMPARATOR = new PathFileComparator(IOCase.INSENSITIVE);
        PATH_INSENSITIVE_REVERSE = new ReverseComparator(PathFileComparator.PATH_INSENSITIVE_COMPARATOR);
        PATH_SYSTEM_COMPARATOR = new PathFileComparator(IOCase.SYSTEM);
        PATH_SYSTEM_REVERSE = new ReverseComparator(PathFileComparator.PATH_SYSTEM_COMPARATOR);
    }
}
