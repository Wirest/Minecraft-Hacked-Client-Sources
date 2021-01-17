// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.filter;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import java.util.Arrays;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;
import java.util.Iterator;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.LifeCycle;
import org.apache.logging.log4j.core.Filter;

@Plugin(name = "filters", category = "Core", printObject = true)
public final class CompositeFilter implements Iterable<Filter>, Filter, LifeCycle
{
    private final List<Filter> filters;
    private final boolean hasFilters;
    private boolean isStarted;
    
    private CompositeFilter() {
        this.filters = new ArrayList<Filter>();
        this.hasFilters = false;
    }
    
    private CompositeFilter(final List<Filter> filters) {
        if (filters == null) {
            this.filters = Collections.unmodifiableList((List<? extends Filter>)new ArrayList<Filter>());
            this.hasFilters = false;
            return;
        }
        this.filters = Collections.unmodifiableList((List<? extends Filter>)filters);
        this.hasFilters = (this.filters.size() > 0);
    }
    
    public CompositeFilter addFilter(final Filter filter) {
        final List<Filter> filters = new ArrayList<Filter>(this.filters);
        filters.add(filter);
        return new CompositeFilter(Collections.unmodifiableList((List<? extends Filter>)filters));
    }
    
    public CompositeFilter removeFilter(final Filter filter) {
        final List<Filter> filters = new ArrayList<Filter>(this.filters);
        filters.remove(filter);
        return new CompositeFilter(Collections.unmodifiableList((List<? extends Filter>)filters));
    }
    
    @Override
    public Iterator<Filter> iterator() {
        return this.filters.iterator();
    }
    
    public List<Filter> getFilters() {
        return this.filters;
    }
    
    public boolean hasFilters() {
        return this.hasFilters;
    }
    
    public int size() {
        return this.filters.size();
    }
    
    @Override
    public void start() {
        for (final Filter filter : this.filters) {
            if (filter instanceof LifeCycle) {
                ((LifeCycle)filter).start();
            }
        }
        this.isStarted = true;
    }
    
    @Override
    public void stop() {
        for (final Filter filter : this.filters) {
            if (filter instanceof LifeCycle) {
                ((LifeCycle)filter).stop();
            }
        }
        this.isStarted = false;
    }
    
    @Override
    public boolean isStarted() {
        return this.isStarted;
    }
    
    @Override
    public Result getOnMismatch() {
        return Result.NEUTRAL;
    }
    
    @Override
    public Result getOnMatch() {
        return Result.NEUTRAL;
    }
    
    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object... params) {
        Result result = Result.NEUTRAL;
        for (final Filter filter : this.filters) {
            result = filter.filter(logger, level, marker, msg, params);
            if (result == Result.ACCEPT || result == Result.DENY) {
                return result;
            }
        }
        return result;
    }
    
    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final Object msg, final Throwable t) {
        Result result = Result.NEUTRAL;
        for (final Filter filter : this.filters) {
            result = filter.filter(logger, level, marker, msg, t);
            if (result == Result.ACCEPT || result == Result.DENY) {
                return result;
            }
        }
        return result;
    }
    
    @Override
    public Result filter(final Logger logger, final Level level, final Marker marker, final Message msg, final Throwable t) {
        Result result = Result.NEUTRAL;
        for (final Filter filter : this.filters) {
            result = filter.filter(logger, level, marker, msg, t);
            if (result == Result.ACCEPT || result == Result.DENY) {
                return result;
            }
        }
        return result;
    }
    
    @Override
    public Result filter(final LogEvent event) {
        Result result = Result.NEUTRAL;
        for (final Filter filter : this.filters) {
            result = filter.filter(event);
            if (result == Result.ACCEPT || result == Result.DENY) {
                return result;
            }
        }
        return result;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (final Filter filter : this.filters) {
            if (sb.length() == 0) {
                sb.append("{");
            }
            else {
                sb.append(", ");
            }
            sb.append(filter.toString());
        }
        if (sb.length() > 0) {
            sb.append("}");
        }
        return sb.toString();
    }
    
    @PluginFactory
    public static CompositeFilter createFilters(@PluginElement("Filters") final Filter[] filters) {
        final List<Filter> f = (filters == null || filters.length == 0) ? new ArrayList<Filter>() : Arrays.asList(filters);
        return new CompositeFilter(f);
    }
}
