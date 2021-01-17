// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io;

import java.io.IOException;
import java.util.Collection;
import java.io.File;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import java.io.FileFilter;

public abstract class DirectoryWalker<T>
{
    private final FileFilter filter;
    private final int depthLimit;
    
    protected DirectoryWalker() {
        this(null, -1);
    }
    
    protected DirectoryWalker(final FileFilter filter, final int depthLimit) {
        this.filter = filter;
        this.depthLimit = depthLimit;
    }
    
    protected DirectoryWalker(IOFileFilter directoryFilter, IOFileFilter fileFilter, final int depthLimit) {
        if (directoryFilter == null && fileFilter == null) {
            this.filter = null;
        }
        else {
            directoryFilter = ((directoryFilter != null) ? directoryFilter : TrueFileFilter.TRUE);
            fileFilter = ((fileFilter != null) ? fileFilter : TrueFileFilter.TRUE);
            directoryFilter = FileFilterUtils.makeDirectoryOnly(directoryFilter);
            fileFilter = FileFilterUtils.makeFileOnly(fileFilter);
            this.filter = FileFilterUtils.or(directoryFilter, fileFilter);
        }
        this.depthLimit = depthLimit;
    }
    
    protected final void walk(final File startDirectory, final Collection<T> results) throws IOException {
        if (startDirectory == null) {
            throw new NullPointerException("Start Directory is null");
        }
        try {
            this.handleStart(startDirectory, results);
            this.walk(startDirectory, 0, results);
            this.handleEnd(results);
        }
        catch (CancelException cancel) {
            this.handleCancelled(startDirectory, results, cancel);
        }
    }
    
    private void walk(final File directory, final int depth, final Collection<T> results) throws IOException {
        this.checkIfCancelled(directory, depth, results);
        if (this.handleDirectory(directory, depth, results)) {
            this.handleDirectoryStart(directory, depth, results);
            final int childDepth = depth + 1;
            if (this.depthLimit < 0 || childDepth <= this.depthLimit) {
                this.checkIfCancelled(directory, depth, results);
                File[] childFiles = (this.filter == null) ? directory.listFiles() : directory.listFiles(this.filter);
                childFiles = this.filterDirectoryContents(directory, depth, childFiles);
                if (childFiles == null) {
                    this.handleRestricted(directory, childDepth, results);
                }
                else {
                    for (final File childFile : childFiles) {
                        if (childFile.isDirectory()) {
                            this.walk(childFile, childDepth, results);
                        }
                        else {
                            this.checkIfCancelled(childFile, childDepth, results);
                            this.handleFile(childFile, childDepth, results);
                            this.checkIfCancelled(childFile, childDepth, results);
                        }
                    }
                }
            }
            this.handleDirectoryEnd(directory, depth, results);
        }
        this.checkIfCancelled(directory, depth, results);
    }
    
    protected final void checkIfCancelled(final File file, final int depth, final Collection<T> results) throws IOException {
        if (this.handleIsCancelled(file, depth, results)) {
            throw new CancelException(file, depth);
        }
    }
    
    protected boolean handleIsCancelled(final File file, final int depth, final Collection<T> results) throws IOException {
        return false;
    }
    
    protected void handleCancelled(final File startDirectory, final Collection<T> results, final CancelException cancel) throws IOException {
        throw cancel;
    }
    
    protected void handleStart(final File startDirectory, final Collection<T> results) throws IOException {
    }
    
    protected boolean handleDirectory(final File directory, final int depth, final Collection<T> results) throws IOException {
        return true;
    }
    
    protected void handleDirectoryStart(final File directory, final int depth, final Collection<T> results) throws IOException {
    }
    
    protected File[] filterDirectoryContents(final File directory, final int depth, final File[] files) throws IOException {
        return files;
    }
    
    protected void handleFile(final File file, final int depth, final Collection<T> results) throws IOException {
    }
    
    protected void handleRestricted(final File directory, final int depth, final Collection<T> results) throws IOException {
    }
    
    protected void handleDirectoryEnd(final File directory, final int depth, final Collection<T> results) throws IOException {
    }
    
    protected void handleEnd(final Collection<T> results) throws IOException {
    }
    
    public static class CancelException extends IOException
    {
        private static final long serialVersionUID = 1347339620135041008L;
        private final File file;
        private final int depth;
        
        public CancelException(final File file, final int depth) {
            this("Operation Cancelled", file, depth);
        }
        
        public CancelException(final String message, final File file, final int depth) {
            super(message);
            this.file = file;
            this.depth = depth;
        }
        
        public File getFile() {
            return this.file;
        }
        
        public int getDepth() {
            return this.depth;
        }
    }
}
