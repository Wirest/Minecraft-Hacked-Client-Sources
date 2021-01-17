// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.filter;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.LifeCycle;
import java.util.Iterator;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.Logger;

public abstract class AbstractFilterable implements Filterable
{
    protected static final Logger LOGGER;
    private volatile Filter filter;
    
    protected AbstractFilterable(final Filter filter) {
        this.filter = filter;
    }
    
    protected AbstractFilterable() {
    }
    
    @Override
    public Filter getFilter() {
        return this.filter;
    }
    
    @Override
    public synchronized void addFilter(final Filter filter) {
        if (this.filter == null) {
            this.filter = filter;
        }
        else if (filter instanceof CompositeFilter) {
            this.filter = ((CompositeFilter)this.filter).addFilter(filter);
        }
        else {
            final Filter[] filters = { this.filter, filter };
            this.filter = CompositeFilter.createFilters(filters);
        }
    }
    
    @Override
    public synchronized void removeFilter(final Filter filter) {
        if (this.filter == filter) {
            this.filter = null;
        }
        else if (filter instanceof CompositeFilter) {
            CompositeFilter composite = (CompositeFilter)filter;
            composite = composite.removeFilter(filter);
            if (composite.size() > 1) {
                this.filter = composite;
            }
            else if (composite.size() == 1) {
                final Iterator<Filter> iter = composite.iterator();
                this.filter = iter.next();
            }
            else {
                this.filter = null;
            }
        }
    }
    
    @Override
    public boolean hasFilter() {
        return this.filter != null;
    }
    
    public void startFilter() {
        if (this.filter != null && this.filter instanceof LifeCycle) {
            ((LifeCycle)this.filter).start();
        }
    }
    
    public void stopFilter() {
        if (this.filter != null && this.filter instanceof LifeCycle) {
            ((LifeCycle)this.filter).stop();
        }
    }
    
    @Override
    public boolean isFiltered(final LogEvent event) {
        return this.filter != null && this.filter.filter(event) == Filter.Result.DENY;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
