// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.filter;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "ThresholdFilter", category = "Core", elementType = "filter", printObject = true)
public final class ThresholdFilter extends AbstractFilter
{
    private final Level level;
    
    private ThresholdFilter(final Level level, final Filter.Result onMatch, final Filter.Result onMismatch) {
        super(onMatch, onMismatch);
        this.level = level;
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object... params) {
        return this.filter(level);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final Object msg, final Throwable t) {
        return this.filter(level);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final Message msg, final Throwable t) {
        return this.filter(level);
    }
    
    @Override
    public Filter.Result filter(final LogEvent event) {
        return this.filter(event.getLevel());
    }
    
    private Filter.Result filter(final Level level) {
        return level.isAtLeastAsSpecificAs(this.level) ? this.onMatch : this.onMismatch;
    }
    
    @Override
    public String toString() {
        return this.level.toString();
    }
    
    @PluginFactory
    public static ThresholdFilter createFilter(@PluginAttribute("level") final String levelName, @PluginAttribute("onMatch") final String match, @PluginAttribute("onMismatch") final String mismatch) {
        final Level level = Level.toLevel(levelName, Level.ERROR);
        final Filter.Result onMatch = Filter.Result.toResult(match, Filter.Result.NEUTRAL);
        final Filter.Result onMismatch = Filter.Result.toResult(mismatch, Filter.Result.DENY);
        return new ThresholdFilter(level, onMatch, onMismatch);
    }
}
