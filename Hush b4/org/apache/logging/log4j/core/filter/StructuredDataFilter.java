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
import org.apache.logging.log4j.message.StructuredDataMessage;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.Filter;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "StructuredDataFilter", category = "Core", elementType = "filter", printObject = true)
public final class StructuredDataFilter extends MapFilter
{
    private StructuredDataFilter(final Map<String, List<String>> map, final boolean oper, final Filter.Result onMatch, final Filter.Result onMismatch) {
        super(map, oper, onMatch, onMismatch);
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final Message msg, final Throwable t) {
        if (msg instanceof StructuredDataMessage) {
            return this.filter((StructuredDataMessage)msg);
        }
        return Filter.Result.NEUTRAL;
    }
    
    @Override
    public Filter.Result filter(final LogEvent event) {
        final Message msg = event.getMessage();
        if (msg instanceof StructuredDataMessage) {
            return this.filter((StructuredDataMessage)msg);
        }
        return super.filter(event);
    }
    
    protected Filter.Result filter(final StructuredDataMessage message) {
        boolean match = false;
        for (final Map.Entry<String, List<String>> entry : this.getMap().entrySet()) {
            final String toMatch = this.getValue(message, entry.getKey());
            match = (toMatch != null && entry.getValue().contains(toMatch));
            if (!this.isAnd() && match) {
                break;
            }
            if (this.isAnd() && !match) {
                break;
            }
        }
        return match ? this.onMatch : this.onMismatch;
    }
    
    private String getValue(final StructuredDataMessage data, final String key) {
        if (key.equalsIgnoreCase("id")) {
            return data.getId().toString();
        }
        if (key.equalsIgnoreCase("id.name")) {
            return data.getId().getName();
        }
        if (key.equalsIgnoreCase("type")) {
            return data.getType();
        }
        if (key.equalsIgnoreCase("message")) {
            return data.getFormattedMessage();
        }
        return data.getData().get(key);
    }
    
    @PluginFactory
    public static StructuredDataFilter createFilter(@PluginElement("Pairs") final KeyValuePair[] pairs, @PluginAttribute("operator") final String oper, @PluginAttribute("onMatch") final String match, @PluginAttribute("onMismatch") final String mismatch) {
        if (pairs == null || pairs.length == 0) {
            StructuredDataFilter.LOGGER.error("keys and values must be specified for the StructuredDataFilter");
            return null;
        }
        final Map<String, List<String>> map = new HashMap<String, List<String>>();
        for (final KeyValuePair pair : pairs) {
            final String key = pair.getKey();
            if (key == null) {
                StructuredDataFilter.LOGGER.error("A null key is not valid in MapFilter");
            }
            else {
                final String value = pair.getValue();
                if (value == null) {
                    StructuredDataFilter.LOGGER.error("A null value for key " + key + " is not allowed in MapFilter");
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
            StructuredDataFilter.LOGGER.error("StructuredDataFilter is not configured with any valid key value pairs");
            return null;
        }
        final boolean isAnd = oper == null || !oper.equalsIgnoreCase("or");
        final Filter.Result onMatch = Filter.Result.toResult(match);
        final Filter.Result onMismatch = Filter.Result.toResult(mismatch);
        return new StructuredDataFilter(map, isAnd, onMatch, onMismatch);
    }
}
