// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.PluginAliases;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "AppenderRef", category = "Core", printObject = true)
@PluginAliases({ "appender-ref" })
public final class AppenderRef
{
    private static final Logger LOGGER;
    private final String ref;
    private final Level level;
    private final Filter filter;
    
    private AppenderRef(final String ref, final Level level, final Filter filter) {
        this.ref = ref;
        this.level = level;
        this.filter = filter;
    }
    
    public String getRef() {
        return this.ref;
    }
    
    public Level getLevel() {
        return this.level;
    }
    
    public Filter getFilter() {
        return this.filter;
    }
    
    @Override
    public String toString() {
        return this.ref;
    }
    
    @PluginFactory
    public static AppenderRef createAppenderRef(@PluginAttribute("ref") final String ref, @PluginAttribute("level") final String levelName, @PluginElement("Filters") final Filter filter) {
        if (ref == null) {
            AppenderRef.LOGGER.error("Appender references must contain a reference");
            return null;
        }
        Level level = null;
        if (levelName != null) {
            level = Level.toLevel(levelName, null);
            if (level == null) {
                AppenderRef.LOGGER.error("Invalid level " + levelName + " on Appender reference " + ref);
            }
        }
        return new AppenderRef(ref, level, filter);
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
