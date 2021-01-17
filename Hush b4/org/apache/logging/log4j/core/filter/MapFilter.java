// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.filter;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.helpers.KeyValuePair;
import java.util.Iterator;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.message.MapMessage;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.Filter;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "MapFilter", category = "Core", elementType = "filter", printObject = true)
public class MapFilter extends AbstractFilter
{
    private final Map<String, List<String>> map;
    private final boolean isAnd;
    
    protected MapFilter(final Map<String, List<String>> map, final boolean oper, final Filter.Result onMatch, final Filter.Result onMismatch) {
        super(onMatch, onMismatch);
        if (map == null) {
            throw new NullPointerException("key cannot be null");
        }
        this.isAnd = oper;
        this.map = map;
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final Message msg, final Throwable t) {
        if (msg instanceof MapMessage) {
            return this.filter(((MapMessage)msg).getData()) ? this.onMatch : this.onMismatch;
        }
        return Filter.Result.NEUTRAL;
    }
    
    @Override
    public Filter.Result filter(final LogEvent event) {
        final Message msg = event.getMessage();
        if (msg instanceof MapMessage) {
            return this.filter(((MapMessage)msg).getData()) ? this.onMatch : this.onMismatch;
        }
        return Filter.Result.NEUTRAL;
    }
    
    protected boolean filter(final Map<String, String> data) {
        boolean match = false;
        for (final Map.Entry<String, List<String>> entry : this.map.entrySet()) {
            final String toMatch = data.get(entry.getKey());
            match = (toMatch != null && entry.getValue().contains(toMatch));
            if (!this.isAnd && match) {
                break;
            }
            if (this.isAnd && !match) {
                break;
            }
        }
        return match;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("isAnd=").append(this.isAnd);
        if (this.map.size() > 0) {
            sb.append(", {");
            boolean first = true;
            for (final Map.Entry<String, List<String>> entry : this.map.entrySet()) {
                if (!first) {
                    sb.append(", ");
                }
                first = false;
                final List<String> list = entry.getValue();
                final String value = (list.size() > 1) ? list.get(0) : list.toString();
                sb.append(entry.getKey()).append("=").append(value);
            }
            sb.append("}");
        }
        return sb.toString();
    }
    
    protected boolean isAnd() {
        return this.isAnd;
    }
    
    protected Map<String, List<String>> getMap() {
        return this.map;
    }
    
    @PluginFactory
    public static MapFilter createFilter(@PluginElement("Pairs") final KeyValuePair[] pairs, @PluginAttribute("operator") final String oper, @PluginAttribute("onMatch") final String match, @PluginAttribute("onMismatch") final String mismatch) {
        if (pairs == null || pairs.length == 0) {
            MapFilter.LOGGER.error("keys and values must be specified for the MapFilter");
            return null;
        }
        final Map<String, List<String>> map = new HashMap<String, List<String>>();
        for (final KeyValuePair pair : pairs) {
            final String key = pair.getKey();
            if (key == null) {
                MapFilter.LOGGER.error("A null key is not valid in MapFilter");
            }
            else {
                final String value = pair.getValue();
                if (value == null) {
                    MapFilter.LOGGER.error("A null value for key " + key + " is not allowed in MapFilter");
                }
                else {
                    List<String> list = map.get(pair.getKey());
                    if (list != null) {
                        list.add(value);
                    }
                    else {
                        list = new ArrayList<String>();
                        list.add(value);
                        map.put(pair.getKey(), list);
                    }
                }
            }
        }
        if (map.size() == 0) {
            MapFilter.LOGGER.error("MapFilter is not configured with any valid key value pairs");
            return null;
        }
        final boolean isAnd = oper == null || !oper.equalsIgnoreCase("or");
        final Filter.Result onMatch = Filter.Result.toResult(match);
        final Filter.Result onMismatch = Filter.Result.toResult(mismatch);
        return new MapFilter(map, isAnd, onMatch, onMismatch);
    }
}
