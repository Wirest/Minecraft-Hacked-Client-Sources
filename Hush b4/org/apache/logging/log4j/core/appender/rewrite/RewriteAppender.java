// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender.rewrite;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.util.Iterator;
import org.apache.logging.log4j.core.LogEvent;
import java.util.Map;
import org.apache.logging.log4j.core.Appender;
import java.util.concurrent.ConcurrentHashMap;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.AppenderControl;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.appender.AbstractAppender;

@Plugin(name = "Rewrite", category = "Core", elementType = "appender", printObject = true)
public final class RewriteAppender extends AbstractAppender
{
    private final Configuration config;
    private final ConcurrentMap<String, AppenderControl> appenders;
    private final RewritePolicy rewritePolicy;
    private final AppenderRef[] appenderRefs;
    
    private RewriteAppender(final String name, final Filter filter, final boolean ignoreExceptions, final AppenderRef[] appenderRefs, final RewritePolicy rewritePolicy, final Configuration config) {
        super(name, filter, null, ignoreExceptions);
        this.appenders = new ConcurrentHashMap<String, AppenderControl>();
        this.config = config;
        this.rewritePolicy = rewritePolicy;
        this.appenderRefs = appenderRefs;
    }
    
    @Override
    public void start() {
        final Map<String, Appender> map = this.config.getAppenders();
        for (final AppenderRef ref : this.appenderRefs) {
            final String name = ref.getRef();
            final Appender appender = map.get(name);
            if (appender != null) {
                final Filter filter = (appender instanceof AbstractAppender) ? ((AbstractAppender)appender).getFilter() : null;
                this.appenders.put(name, new AppenderControl(appender, ref.getLevel(), filter));
            }
            else {
                RewriteAppender.LOGGER.error("Appender " + ref + " cannot be located. Reference ignored");
            }
        }
        super.start();
    }
    
    @Override
    public void stop() {
        super.stop();
    }
    
    @Override
    public void append(LogEvent event) {
        if (this.rewritePolicy != null) {
            event = this.rewritePolicy.rewrite(event);
        }
        for (final AppenderControl control : this.appenders.values()) {
            control.callAppender(event);
        }
    }
    
    @PluginFactory
    public static RewriteAppender createAppender(@PluginAttribute("name") final String name, @PluginAttribute("ignoreExceptions") final String ignore, @PluginElement("AppenderRef") final AppenderRef[] appenderRefs, @PluginConfiguration final Configuration config, @PluginElement("RewritePolicy") final RewritePolicy rewritePolicy, @PluginElement("Filter") final Filter filter) {
        final boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        if (name == null) {
            RewriteAppender.LOGGER.error("No name provided for RewriteAppender");
            return null;
        }
        if (appenderRefs == null) {
            RewriteAppender.LOGGER.error("No appender references defined for RewriteAppender");
            return null;
        }
        return new RewriteAppender(name, filter, ignoreExceptions, appenderRefs, rewritePolicy, config);
    }
}
