// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.filter;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "MarkerFilter", category = "Core", elementType = "filter", printObject = true)
public final class MarkerFilter extends AbstractFilter
{
    private final String name;
    
    private MarkerFilter(final String name, final Filter.Result onMatch, final Filter.Result onMismatch) {
        super(onMatch, onMismatch);
        this.name = name;
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object... params) {
        return this.filter(marker);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final Object msg, final Throwable t) {
        return this.filter(marker);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final Message msg, final Throwable t) {
        return this.filter(marker);
    }
    
    @Override
    public Filter.Result filter(final LogEvent event) {
        return this.filter(event.getMarker());
    }
    
    private Filter.Result filter(final Marker marker) {
        return (marker != null && marker.isInstanceOf(this.name)) ? this.onMatch : this.onMismatch;
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    @PluginFactory
    public static MarkerFilter createFilter(@PluginAttribute("marker") final String marker, @PluginAttribute("onMatch") final String match, @PluginAttribute("onMismatch") final String mismatch) {
        if (marker == null) {
            MarkerFilter.LOGGER.error("A marker must be provided for MarkerFilter");
            return null;
        }
        final Filter.Result onMatch = Filter.Result.toResult(match);
        final Filter.Result onMismatch = Filter.Result.toResult(mismatch);
        return new MarkerFilter(marker, onMatch, onMismatch);
    }
}
