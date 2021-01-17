// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.routing;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.LogEvent;
import java.util.Iterator;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import java.util.concurrent.ConcurrentHashMap;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.config.AppenderControl;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.appender.AbstractAppender;

@Plugin(name = "Routing", category = "Core", elementType = "appender", printObject = true)
public final class RoutingAppender extends AbstractAppender
{
    private static final String DEFAULT_KEY = "ROUTING_APPENDER_DEFAULT";
    private final Routes routes;
    private final Route defaultRoute;
    private final Configuration config;
    private final ConcurrentMap<String, AppenderControl> appenders;
    private final RewritePolicy rewritePolicy;
    
    private RoutingAppender(final String name, final Filter filter, final boolean ignoreExceptions, final Routes routes, final RewritePolicy rewritePolicy, final Configuration config) {
        super(name, filter, null, ignoreExceptions);
        this.appenders = new ConcurrentHashMap<String, AppenderControl>();
        this.routes = routes;
        this.config = config;
        this.rewritePolicy = rewritePolicy;
        Route defRoute = null;
        for (final Route route : routes.getRoutes()) {
            if (route.getKey() == null) {
                if (defRoute == null) {
                    defRoute = route;
                }
                else {
                    this.error("Multiple default routes. Route " + route.toString() + " will be ignored");
                }
            }
        }
        this.defaultRoute = defRoute;
    }
    
    @Override
    public void start() {
        final Map<String, Appender> map = this.config.getAppenders();
        for (final Route route : this.routes.getRoutes()) {
            if (route.getAppenderRef() != null) {
                final Appender appender = map.get(route.getAppenderRef());
                if (appender != null) {
                    final String key = (route == this.defaultRoute) ? "ROUTING_APPENDER_DEFAULT" : route.getKey();
                    this.appenders.put(key, new AppenderControl(appender, null, null));
                }
                else {
                    RoutingAppender.LOGGER.error("Appender " + route.getAppenderRef() + " cannot be located. Route ignored");
                }
            }
        }
        super.start();
    }
    
    @Override
    public void stop() {
        super.stop();
        final Map<String, Appender> map = this.config.getAppenders();
        for (final Map.Entry<String, AppenderControl> entry : this.appenders.entrySet()) {
            final String name = entry.getValue().getAppender().getName();
            if (!map.containsKey(name)) {
                entry.getValue().getAppender().stop();
            }
        }
    }
    
    @Override
    public void append(LogEvent event) {
        if (this.rewritePolicy != null) {
            event = this.rewritePolicy.rewrite(event);
        }
        final String key = this.config.getStrSubstitutor().replace(event, this.routes.getPattern());
        final AppenderControl control = this.getControl(key, event);
        if (control != null) {
            control.callAppender(event);
        }
    }
    
    private synchronized AppenderControl getControl(final String key, final LogEvent event) {
        AppenderControl control = this.appenders.get(key);
        if (control != null) {
            return control;
        }
        Route route = null;
        for (final Route r : this.routes.getRoutes()) {
            if (r.getAppenderRef() == null && key.equals(r.getKey())) {
                route = r;
                break;
            }
        }
        if (route == null) {
            route = this.defaultRoute;
            control = this.appenders.get("ROUTING_APPENDER_DEFAULT");
            if (control != null) {
                return control;
            }
        }
        if (route != null) {
            final Appender app = this.createAppender(route, event);
            if (app == null) {
                return null;
            }
            control = new AppenderControl(app, null, null);
            this.appenders.put(key, control);
        }
        return control;
    }
    
    private Appender createAppender(final Route route, final LogEvent event) {
        final Node routeNode = route.getNode();
        for (final Node node : routeNode.getChildren()) {
            if (node.getType().getElementName().equals("appender")) {
                final Node appNode = new Node(node);
                this.config.createConfiguration(appNode, event);
                if (appNode.getObject() instanceof Appender) {
                    final Appender app = (Appender)appNode.getObject();
                    app.start();
                    return app;
                }
                RoutingAppender.LOGGER.error("Unable to create Appender of type " + node.getName());
                return null;
            }
        }
        RoutingAppender.LOGGER.error("No Appender was configured for route " + route.getKey());
        return null;
    }
    
    @PluginFactory
    public static RoutingAppender createAppender(@PluginAttribute("name") final String name, @PluginAttribute("ignoreExceptions") final String ignore, @PluginElement("Routes") final Routes routes, @PluginConfiguration final Configuration config, @PluginElement("RewritePolicy") final RewritePolicy rewritePolicy, @PluginElement("Filters") final Filter filter) {
        final boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        if (name == null) {
            RoutingAppender.LOGGER.error("No name provided for RoutingAppender");
            return null;
        }
        if (routes == null) {
            RoutingAppender.LOGGER.error("No routes defined for RoutingAppender");
            return null;
        }
        return new RoutingAppender(name, filter, ignoreExceptions, routes, rewritePolicy, config);
    }
}
