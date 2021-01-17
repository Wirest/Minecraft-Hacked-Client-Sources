// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.comparator;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.util.Comparator;
import java.io.Serializable;

public class CompositeFileComparator extends AbstractFileComparator implements Serializable
{
    private static final Comparator<?>[] NO_COMPARATORS;
    private final Comparator<File>[] delegates;
    
    public CompositeFileComparator(final Comparator<File>... delegates) {
        if (delegates == null) {
            this.delegates = (Comparator<File>[])CompositeFileComparator.NO_COMPARATORS;
        }
        else {
            System.arraycopy(delegates, 0, this.delegates = (Comparator<File>[])new Comparator[delegates.length], 0, delegates.length);
        }
    }
    
    public CompositeFileComparator(final Iterable<Comparator<File>> delegates) {
        if (delegates == null) {
            this.delegates = (Comparator<File>[])CompositeFileComparator.NO_COMPARATORS;
        }
        else {
            final List<Comparator<File>> list = new ArrayList<Comparator<File>>();
            for (final Comparator<File> comparator : delegates) {
                list.add(comparator);
            }
            this.delegates = list.toArray(new Comparator[list.size()]);
        }
    }
    
    @Override
    public int compare(final File file1, final File file2) {
        int result = 0;
        for (final Comparator<File> delegate : this.delegates) {
            result = delegate.compare(file1, file2);
            if (result != 0) {
                break;
            }
        }
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append('{');
        for (int i = 0; i < this.delegates.length; ++i) {
            if (i > 0) {
                builder.append(',');
            }
            builder.append(this.delegates[i]);
        }
        builder.append('}');
        return builder.toString();
    }
    
    static {
        NO_COMPARATORS = new Comparator[0];
    }
}
