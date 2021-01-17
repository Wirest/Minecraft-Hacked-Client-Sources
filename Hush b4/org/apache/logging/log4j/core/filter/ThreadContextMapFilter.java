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
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;
import java.util.Iterator;
import org.apache.logging.log4j.core.Filter;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "ThreadContextMapFilter", category = "Core", elementType = "filter", printObject = true)
public class ThreadContextMapFilter extends MapFilter
{
    private final String key;
    private final String value;
    private final boolean useMap;
    
    public ThreadContextMapFilter(final Map<String, List<String>> pairs, final boolean oper, final Filter.Result onMatch, final Filter.Result onMismatch) {
        super(pairs, oper, onMatch, onMismatch);
        if (pairs.size() == 1) {
            final Iterator<Map.Entry<String, List<String>>> iter = pairs.entrySet().iterator();
            final Map.Entry<String, List<String>> entry = iter.next();
            if (entry.getValue().size() == 1) {
                this.key = entry.getKey();
                this.value = entry.getValue().get(0);
                this.useMap = false;
            }
            else {
                this.key = null;
                this.value = null;
                this.useMap = true;
            }
        }
        else {
            this.key = null;
            this.value = null;
            this.useMap = true;
        }
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final String msg, final Object... params) {
        return this.filter();
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final Object msg, final Throwable t) {
        return this.filter();
    }
    
    @Override
    public Filter.Result filter(final Logger logger, final Level level, final Marker marker, final Message msg, final Throwable t) {
        return this.filter();
    }
    
    private Filter.Result filter() {
        boolean match = false;
        if (this.useMap) {
            for (final Map.Entry<String, List<String>> entry : this.getMap().entrySet()) {
                final String toMatch = ThreadContext.get(entry.getKey());
                match = (toMatch != null && entry.getValue().contains(toMatch));
                if (!this.isAnd() && match) {
                    break;
                }
                if (this.isAnd() && !match) {
                    break;
                }
            }
        }
        else {
            match = this.value.equals(ThreadContext.get(this.key));
        }
        return match ? this.onMatch : this.onMismatch;
    }
    
    @Override
    public Filter.Result filter(final LogEvent event) {
        return super.filter(event.getContextMap()) ? this.onMatch : this.onMismatch;
    }
    
    @PluginFactory
    public static ThreadContextMapFilter createFilter(@PluginElement("Pairs") final KeyValuePair[] pairs, @PluginAttribute("operator") final String oper, @PluginAttribute("onMatch") final String match, @PluginAttribute("onMismatch") final String mismatch) {
        if (pairs == null || pairs.length == 0) {
            ThreadContextMapFilter.LOGGER.error("key and value pairs must be specified for the ThreadContextMapFilter");
            return null;
        }
        final Map<String, List<String>> map = new HashMap<String, List<String>>();
        for (final KeyValuePair pair : pairs) {
            final String key = pair.getKey();
            if (key == null) {
                ThreadContextMapFilter.LOGGER.error("A null key is not valid in MapFilter");
            }
            else {
                final String value = pair.getValue();
                if (value == null) {
                    ThreadContextMapFilter.LOGGER.error("A null value for key " + key + " is not allowed in MapFilter");
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
            ThreadContextMapFilter.LOGGER.error("ThreadContextMapFilter is not configured with any valid key value pairs");
            return null;
        }
        final boolean isAnd = oper == null || !oper.equalsIgnoreCase("or");
        final Filter.Result onMatch = Filter.Result.toResult(match);
        final Filter.Result onMismatch = Filter.Result.toResult(mismatch);
        return new ThreadContextMapFilter(map, isAnd, onMatch, onMismatch);
    }
}
