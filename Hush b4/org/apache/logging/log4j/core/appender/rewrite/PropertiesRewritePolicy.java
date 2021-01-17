// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rewrite;

import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import java.util.Arrays;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.LogEvent;
import java.util.Iterator;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Property;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "PropertiesRewritePolicy", category = "Core", elementType = "rewritePolicy", printObject = true)
public final class PropertiesRewritePolicy implements RewritePolicy
{
    protected static final Logger LOGGER;
    private final Map<Property, Boolean> properties;
    private final Configuration config;
    
    private PropertiesRewritePolicy(final Configuration config, final List<Property> props) {
        this.config = config;
        this.properties = new HashMap<Property, Boolean>(props.size());
        for (final Property property : props) {
            final Boolean interpolate = property.getValue().contains("${");
            this.properties.put(property, interpolate);
        }
    }
    
    @Override
    public LogEvent rewrite(final LogEvent source) {
        final Map<String, String> props = new HashMap<String, String>(source.getContextMap());
        for (final Map.Entry<Property, Boolean> entry : this.properties.entrySet()) {
            final Property prop = entry.getKey();
            props.put(prop.getName(), ((boolean)entry.getValue()) ? this.config.getStrSubstitutor().replace(prop.getValue()) : prop.getValue());
        }
        return new Log4jLogEvent(source.getLoggerName(), source.getMarker(), source.getFQCN(), source.getLevel(), source.getMessage(), source.getThrown(), props, source.getContextStack(), source.getThreadName(), source.getSource(), source.getMillis());
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(" {");
        boolean first = true;
        for (final Map.Entry<Property, Boolean> entry : this.properties.entrySet()) {
            if (!first) {
                sb.append(", ");
            }
            final Property prop = entry.getKey();
            sb.append(prop.getName()).append("=").append(prop.getValue());
            first = false;
        }
        sb.append("}");
        return sb.toString();
    }
    
    @PluginFactory
    public static PropertiesRewritePolicy createPolicy(@PluginConfiguration final Configuration config, @PluginElement("Properties") final Property[] props) {
        if (props == null || props.length == 0) {
            PropertiesRewritePolicy.LOGGER.error("Properties must be specified for the PropertiesRewritePolicy");
            return null;
        }
        final List<Property> properties = Arrays.asList(props);
        return new PropertiesRewritePolicy(config, properties);
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
