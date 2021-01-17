// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.comparator;

import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;
import java.io.File;
import java.util.Comparator;
import java.io.Serializable;

public class ExtensionFileComparator extends AbstractFileComparator implements Serializable
{
    public static final Comparator<File> EXTENSION_COMPARATOR;
    public static final Comparator<File> EXTENSION_REVERSE;
    public static final Comparator<File> EXTENSION_INSENSITIVE_COMPARATOR;
    public static final Comparator<File> EXTENSION_INSENSITIVE_REVERSE;
    public static final Comparator<File> EXTENSION_SYSTEM_COMPARATOR;
    public static final Comparator<File> EXTENSION_SYSTEM_REVERSE;
    private final IOCase caseSensitivity;
    
    public ExtensionFileComparator() {
        this.caseSensitivity = IOCase.SENSITIVE;
    }
    
    public ExtensionFileComparator(final IOCase caseSensitivity) {
        this.caseSensitivity = ((caseSensitivity == null) ? IOCase.SENSITIVE : caseSensitivity);
    }
    
    @Override
    public int compare(final File file1, final File file2) {
        final String suffix1 = FilenameUtils.getExtension(file1.getName());
        final String suffix2 = FilenameUtils.getExtension(file2.getName());
        return this.caseSensitivity.checkCompareTo(suffix1, suffix2);
    }
    
    @Override
    public String toString() {
        return super.toString() + "[caseSensitivity=" + this.caseSensitivity + "]";
    }
    
    static {
        EXTENSION_COMPARATOR = new ExtensionFileComparator();
        EXTENSION_REVERSE = new ReverseComparator(ExtensionFileComparator.EXTENSION_COMPARATOR);
        EXTENSION_INSENSITIVE_COMPARATOR = new ExtensionFileComparator(IOCase.INSENSITIVE);
        EXTENSION_INSENSITIVE_REVERSE = new ReverseComparator(ExtensionFileComparator.EXTENSION_INSENSITIVE_COMPARATOR);
        EXTENSION_SYSTEM_COMPARATOR = new ExtensionFileComparator(IOCase.SYSTEM);
        EXTENSION_SYSTEM_REVERSE = new ReverseComparator(ExtensionFileComparator.EXTENSION_SYSTEM_COMPARATOR);
    }
}
