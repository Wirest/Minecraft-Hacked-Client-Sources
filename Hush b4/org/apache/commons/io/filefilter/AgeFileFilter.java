// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.filefilter;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.util.Date;
import java.io.Serializable;

public class AgeFileFilter extends AbstractFileFilter implements Serializable
{
    private final long cutoff;
    private final boolean acceptOlder;
    
    public AgeFileFilter(final long cutoff) {
        this(cutoff, true);
    }
    
    public AgeFileFilter(final long cutoff, final boolean acceptOlder) {
        this.acceptOlder = acceptOlder;
        this.cutoff = cutoff;
    }
    
    public AgeFileFilter(final Date cutoffDate) {
        this(cutoffDate, true);
    }
    
    public AgeFileFilter(final Date cutoffDate, final boolean acceptOlder) {
        this(cutoffDate.getTime(), acceptOlder);
    }
    
    public AgeFileFilter(final File cutoffReference) {
        this(cutoffReference, true);
    }
    
    public AgeFileFilter(final File cutoffReference, final boolean acceptOlder) {
        this(cutoffReference.lastModified(), acceptOlder);
    }
    
    @Override
    public boolean accept(final File file) {
        final boolean newer = FileUtils.isFileNewer(file, this.cutoff);
        return this.acceptOlder ? (!newer) : newer;
    }
    
    @Override
    public String toString() {
        final String condition = this.acceptOlder ? "<=" : ">";
        return super.toString() + "(" + condition + this.cutoff + ")";
    }
}
