// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.lookup;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.LogEvent;
import java.util.Iterator;
import org.apache.logging.log4j.core.config.plugins.PluginType;
import org.apache.logging.log4j.core.config.plugins.PluginManager;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.Logger;

public class Interpolator implements StrLookup
{
    private static final Logger LOGGER;
    private static final char PREFIX_SEPARATOR = ':';
    private final Map<String, StrLookup> lookups;
    private final StrLookup defaultLookup;
    
    public Interpolator(final StrLookup defaultLookup) {
        this.lookups = new HashMap<String, StrLookup>();
        this.defaultLookup = ((defaultLookup == null) ? new MapLookup(new HashMap<String, String>()) : defaultLookup);
        final PluginManager manager = new PluginManager("Lookup");
        manager.collectPlugins();
        final Map<String, PluginType<?>> plugins = manager.getPlugins();
        for (final Map.Entry<String, PluginType<?>> entry : plugins.entrySet()) {
            final Class<? extends StrLookup> clazz = (Class<? extends StrLookup>)entry.getValue().getPluginClass();
            try {
                this.lookups.put(entry.getKey(), (StrLookup)clazz.newInstance());
            }
            catch (Exception ex) {
                Interpolator.LOGGER.error("Unable to create Lookup for " + entry.getKey(), ex);
            }
        }
    }
    
    public Interpolator() {
        this.lookups = new HashMap<String, StrLookup>();
        this.defaultLookup = new MapLookup(new HashMap<String, String>());
        this.lookups.put("sys", new SystemPropertiesLookup());
        this.lookups.put("env", new EnvironmentLookup());
        this.lookups.put("jndi", new JndiLookup());
        try {
            if (Class.forName("javax.servlet.ServletContext") != null) {
                this.lookups.put("web", new WebLookup());
            }
        }
        catch (ClassNotFoundException ex2) {
            Interpolator.LOGGER.debug("ServletContext not present - WebLookup not added");
        }
        catch (Exception ex) {
            Interpolator.LOGGER.error("Unable to locate ServletContext", ex);
        }
    }
    
    @Override
    public String lookup(final String var) {
        return this.lookup(null, var);
    }
    
    @Override
    public String lookup(final LogEvent event, String var) {
        if (var == null) {
            return null;
        }
        final int prefixPos = var.indexOf(58);
        if (prefixPos >= 0) {
            final String prefix = var.substring(0, prefixPos);
            final String name = var.substring(prefixPos + 1);
            final StrLookup lookup = this.lookups.get(prefix);
            String value = null;
            if (lookup != null) {
                value = ((event == null) ? lookup.lookup(name) : lookup.lookup(event, name));
            }
            if (value != null) {
                return value;
            }
            var = var.substring(prefixPos + 1);
        }
        if (this.defaultLookup != null) {
            return (event == null) ? this.defaultLookup.lookup(var) : this.defaultLookup.lookup(event, var);
        }
        return null;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (final String name : this.lookups.keySet()) {
            if (sb.length() == 0) {
                sb.append("{");
            }
            else {
                sb.append(", ");
            }
            sb.append(name);
        }
        if (sb.length() > 0) {
            sb.append("}");
        }
        return sb.toString();
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
