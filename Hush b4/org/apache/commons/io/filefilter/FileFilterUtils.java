// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.filefilter;

import java.util.Date;
import java.io.FilenameFilter;
import java.io.FileFilter;
import org.apache.commons.io.IOCase;
import java.util.Iterator;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.io.File;

public class FileFilterUtils
{
    private static final IOFileFilter cvsFilter;
    private static final IOFileFilter svnFilter;
    
    public static File[] filter(final IOFileFilter filter, final File... files) {
        if (filter == null) {
            throw new IllegalArgumentException("file filter is null");
        }
        if (files == null) {
            return new File[0];
        }
        final List<File> acceptedFiles = new ArrayList<File>();
        for (final File file : files) {
            if (file == null) {
                throw new IllegalArgumentException("file array contains null");
            }
            if (filter.accept(file)) {
                acceptedFiles.add(file);
            }
        }
        return acceptedFiles.toArray(new File[acceptedFiles.size()]);
    }
    
    public static File[] filter(final IOFileFilter filter, final Iterable<File> files) {
        final List<File> acceptedFiles = filterList(filter, files);
        return acceptedFiles.toArray(new File[acceptedFiles.size()]);
    }
    
    public static List<File> filterList(final IOFileFilter filter, final Iterable<File> files) {
        return filter(filter, files, new ArrayList<File>());
    }
    
    public static List<File> filterList(final IOFileFilter filter, final File... files) {
        final File[] acceptedFiles = filter(filter, files);
        return Arrays.asList(acceptedFiles);
    }
    
    public static Set<File> filterSet(final IOFileFilter filter, final File... files) {
        final File[] acceptedFiles = filter(filter, files);
        return new HashSet<File>(Arrays.asList(acceptedFiles));
    }
    
    public static Set<File> filterSet(final IOFileFilter filter, final Iterable<File> files) {
        return filter(filter, files, new HashSet<File>());
    }
    
    private static <T extends Collection<File>> T filter(final IOFileFilter filter, final Iterable<File> files, final T acceptedFiles) {
        if (filter == null) {
            throw new IllegalArgumentException("file filter is null");
        }
        if (files != null) {
            for (final File file : files) {
                if (file == null) {
                    throw new IllegalArgumentException("file collection contains null");
                }
                if (!filter.accept(file)) {
                    continue;
                }
                acceptedFiles.add(file);
            }
        }
        return acceptedFiles;
    }
    
    public static IOFileFilter prefixFileFilter(final String prefix) {
        return new PrefixFileFilter(prefix);
    }
    
    public static IOFileFilter prefixFileFilter(final String prefix, final IOCase caseSensitivity) {
        return new PrefixFileFilter(prefix, caseSensitivity);
    }
    
    public static IOFileFilter suffixFileFilter(final String suffix) {
        return new SuffixFileFilter(suffix);
    }
    
    public static IOFileFilter suffixFileFilter(final String suffix, final IOCase caseSensitivity) {
        return new SuffixFileFilter(suffix, caseSensitivity);
    }
    
    public static IOFileFilter nameFileFilter(final String name) {
        return new NameFileFilter(name);
    }
    
    public static IOFileFilter nameFileFilter(final String name, final IOCase caseSensitivity) {
        return new NameFileFilter(name, caseSensitivity);
    }
    
    public static IOFileFilter directoryFileFilter() {
        return DirectoryFileFilter.DIRECTORY;
    }
    
    public static IOFileFilter fileFileFilter() {
        return FileFileFilter.FILE;
    }
    
    @Deprecated
    public static IOFileFilter andFileFilter(final IOFileFilter filter1, final IOFileFilter filter2) {
        return new AndFileFilter(filter1, filter2);
    }
    
    @Deprecated
    public static IOFileFilter orFileFilter(final IOFileFilter filter1, final IOFileFilter filter2) {
        return new OrFileFilter(filter1, filter2);
    }
    
    public static IOFileFilter and(final IOFileFilter... filters) {
        return new AndFileFilter(toList(filters));
    }
    
    public static IOFileFilter or(final IOFileFilter... filters) {
        return new OrFileFilter(toList(filters));
    }
    
    public static List<IOFileFilter> toList(final IOFileFilter... filters) {
        if (filters == null) {
            throw new IllegalArgumentException("The filters must not be null");
        }
        final List<IOFileFilter> list = new ArrayList<IOFileFilter>(filters.length);
        for (int i = 0; i < filters.length; ++i) {
            if (filters[i] == null) {
                throw new IllegalArgumentException("The filter[" + i + "] is null");
            }
            list.add(filters[i]);
        }
        return list;
    }
    
    public static IOFileFilter notFileFilter(final IOFileFilter filter) {
        return new NotFileFilter(filter);
    }
    
    public static IOFileFilter trueFileFilter() {
        return TrueFileFilter.TRUE;
    }
    
    public static IOFileFilter falseFileFilter() {
        return FalseFileFilter.FALSE;
    }
    
    public static IOFileFilter asFileFilter(final FileFilter filter) {
        return new DelegateFileFilter(filter);
    }
    
    public static IOFileFilter asFileFilter(final FilenameFilter filter) {
        return new DelegateFileFilter(filter);
    }
    
    public static IOFileFilter ageFileFilter(final long cutoff) {
        return new AgeFileFilter(cutoff);
    }
    
    public static IOFileFilter ageFileFilter(final long cutoff, final boolean acceptOlder) {
        return new AgeFileFilter(cutoff, acceptOlder);
    }
    
    public static IOFileFilter ageFileFilter(final Date cutoffDate) {
        return new AgeFileFilter(cutoffDate);
    }
    
    public static IOFileFilter ageFileFilter(final Date cutoffDate, final boolean acceptOlder) {
        return new AgeFileFilter(cutoffDate, acceptOlder);
    }
    
    public static IOFileFilter ageFileFilter(final File cutoffReference) {
        return new AgeFileFilter(cutoffReference);
    }
    
    public static IOFileFilter ageFileFilter(final File cutoffReference, final boolean acceptOlder) {
        return new AgeFileFilter(cutoffReference, acceptOlder);
    }
    
    public static IOFileFilter sizeFileFilter(final long threshold) {
        return new SizeFileFilter(threshold);
    }
    
    public static IOFileFilter sizeFileFilter(final long threshold, final boolean acceptLarger) {
        return new SizeFileFilter(threshold, acceptLarger);
    }
    
    public static IOFileFilter sizeRangeFileFilter(final long minSizeInclusive, final long maxSizeInclusive) {
        final IOFileFilter minimumFilter = new SizeFileFilter(minSizeInclusive, true);
        final IOFileFilter maximumFilter = new SizeFileFilter(maxSizeInclusive + 1L, false);
        return new AndFileFilter(minimumFilter, maximumFilter);
    }
    
    public static IOFileFilter magicNumberFileFilter(final String magicNumber) {
        return new MagicNumberFileFilter(magicNumber);
    }
    
    public static IOFileFilter magicNumberFileFilter(final String magicNumber, final long offset) {
        return new MagicNumberFileFilter(magicNumber, offset);
    }
    
    public static IOFileFilter magicNumberFileFilter(final byte[] magicNumber) {
        return new MagicNumberFileFilter(magicNumber);
    }
    
    public static IOFileFilter magicNumberFileFilter(final byte[] magicNumber, final long offset) {
        return new MagicNumberFileFilter(magicNumber, offset);
    }
    
    public static IOFileFilter makeCVSAware(final IOFileFilter filter) {
        if (filter == null) {
            return FileFilterUtils.cvsFilter;
        }
        return and(filter, FileFilterUtils.cvsFilter);
    }
    
    public static IOFileFilter makeSVNAware(final IOFileFilter filter) {
        if (filter == null) {
            return FileFilterUtils.svnFilter;
        }
        return and(filter, FileFilterUtils.svnFilter);
    }
    
    public static IOFileFilter makeDirectoryOnly(final IOFileFilter filter) {
        if (filter == null) {
            return DirectoryFileFilter.DIRECTORY;
        }
        return new AndFileFilter(DirectoryFileFilter.DIRECTORY, filter);
    }
    
    public static IOFileFilter makeFileOnly(final IOFileFilter filter) {
        if (filter == null) {
            return FileFileFilter.FILE;
        }
        return new AndFileFilter(FileFileFilter.FILE, filter);
    }
    
    static {
        cvsFilter = notFileFilter(and(directoryFileFilter(), nameFileFilter("CVS")));
        svnFilter = notFileFilter(and(directoryFileFilter(), nameFileFilter(".svn")));
    }
}
