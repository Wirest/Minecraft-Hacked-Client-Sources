// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.filter;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.helpers.KeyValuePair;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.util.Iterator;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Logger;
import java.util.HashMap;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.Level;
import java.util.Map;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "DynamicThresholdFilter", category = "Core", elementType = "filter", printObject = true)
public final class DynamicThresholdFilter extends AbstractFilter
{
    private Map<String, Level> levelMap;
    private Level defaultThreshold;
    private final String key;
    
    private DynamicThresholdFilter(final String key, final Map<String, Level> pairs, final Level defaultLevel, final Filter.Result onMatch, final Filter.Result onMismatch) {
        super(onMatch, onMismatch);
        this.levelMap = new HashMap<String, Level>();
        this.defaultThreshold = Level.ERROR;
        if (key == null) {
            throw new NullPointerException("key cannot be null");
        }
        this.key = key;
        this.levelMap = pairs;
        this.defaultThreshold = defaultLevel;
    }
    
    public String getKey() {
        return this.key;
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
        final Object value = ThreadContext.get(this.key);
        if (value != null) {
            Level ctxLevel = this.levelMap.get(value);
            if (ctxLevel == null) {
                ctxLevel = this.defaultThreshold;
            }
            return level.isAtLeastAsSpecificAs(ctxLevel) ? this.onMatch : this.onMismatch;
        }
        return Filter.Result.NEUTRAL;
    }
    
    public Map<String, Level> getLevelMap() {
        return this.levelMap;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("key=").append(this.key);
        sb.append(", default=").append(this.defaultThreshold);
        if (this.levelMap.size() > 0) {
            sb.append("{");
            boolean first = true;
            for (final Map.Entry<String, Level> entry : this.levelMap.entrySet()) {
                if (!first) {
                    sb.append(", ");
                    first = false;
                }
                sb.append(entry.getKey()).append("=").append(entry.getValue());
            }
            sb.append("}");
        }
        return sb.toString();
    }
    
    @PluginFactory
    public static DynamicThresholdFilter createFilter(@PluginAttribute("key") final String key, @PluginElement("Pairs") final KeyValuePair[] pairs, @PluginAttribute("defaultThreshold") final String levelName, @PluginAttribute("onMatch") final String match, @PluginAttribute("onMismatch") final String mismatch) {
        final Filter.Result onMatch = Filter.Result.toResult(match);
        final Filter.Result onMismatch = Filter.Result.toResult(mismatch);
        final Map<String, Level> map = new HashMap<String, Level>();
        for (final KeyValuePair pair : pairs) {
            map.put(pair.getKey(), Level.toLevel(pair.getValue()));
        }
        final Level level = Level.toLevel(levelName, Level.ERROR);
        return new DynamicThresholdFilter(key, map, level, onMatch, onMismatch);
    }
}
