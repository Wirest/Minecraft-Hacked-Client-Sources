// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.routing;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "Routes", category = "Core", printObject = true)
public final class Routes
{
    private static final Logger LOGGER;
    private final String pattern;
    private final Route[] routes;
    
    private Routes(final String pattern, final Route... routes) {
        this.pattern = pattern;
        this.routes = routes;
    }
    
    public String getPattern() {
        return this.pattern;
    }
    
    public Route[] getRoutes() {
        return this.routes;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (final Route route : this.routes) {
            if (!first) {
                sb.append(",");
            }
            first = false;
            sb.append(route.toString());
        }
        sb.append("}");
        return sb.toString();
    }
    
    @PluginFactory
    public static Routes createRoutes(@PluginAttribute("pattern") final String pattern, @PluginElement("Routes") final Route... routes) {
        if (pattern == null) {
            Routes.LOGGER.error("A pattern is required");
            return null;
        }
        if (routes == null || routes.length == 0) {
            Routes.LOGGER.error("No routes configured");
            return null;
        }
        return new Routes(pattern, routes);
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
