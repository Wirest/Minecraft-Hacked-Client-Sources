// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.io.filefilter;

import java.util.Iterator;
import java.io.File;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class OrFileFilter extends AbstractFileFilter implements ConditionalFileFilter, Serializable
{
    private final List<IOFileFilter> fileFilters;
    
    public OrFileFilter() {
        this.fileFilters = new ArrayList<IOFileFilter>();
    }
    
    public OrFileFilter(final List<IOFileFilter> fileFilters) {
        if (fileFilters == null) {
            this.fileFilters = new ArrayList<IOFileFilter>();
        }
        else {
            this.fileFilters = new ArrayList<IOFileFilter>(fileFilters);
        }
    }
    
    public OrFileFilter(final IOFileFilter filter1, final IOFileFilter filter2) {
        if (filter1 == null || filter2 == null) {
            throw new IllegalArgumentException("The filters must not be null");
        }
        this.fileFilters = new ArrayList<IOFileFilter>(2);
        this.addFileFilter(filter1);
        this.addFileFilter(filter2);
    }
    
    @Override
    public void addFileFilter(final IOFileFilter ioFileFilter) {
        this.fileFilters.add(ioFileFilter);
    }
    
    @Override
    public List<IOFileFilter> getFileFilters() {
        return Collections.unmodifiableList((List<? extends IOFileFilter>)this.fileFilters);
    }
    
    @Override
    public boolean removeFileFilter(final IOFileFilter ioFileFilter) {
        return this.fileFilters.remove(ioFileFilter);
    }
    
    @Override
    public void setFileFilters(final List<IOFileFilter> fileFilters) {
        this.fileFilters.clear();
        this.fileFilters.addAll(fileFilters);
    }
    
    @Override
    public boolean accept(final File file) {
        for (final IOFileFilter fileFilter : this.fileFilters) {
            if (fileFilter.accept(file)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean accept(final File file, final String name) {
        for (final IOFileFilter fileFilter : this.fileFilters) {
            if (fileFilter.accept(file, name)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder();
        buffer.append(super.toString());
        buffer.append("(");
        if (this.fileFilters != null) {
            for (int i = 0; i < this.fileFilters.size(); ++i) {
                if (i > 0) {
                    buffer.append(",");
                }
                final Object filter = this.fileFilters.get(i);
                buffer.append((filter == null) ? "null" : filter.toString());
            }
        }
        buffer.append(")");
        return buffer.toString();
    }
}
