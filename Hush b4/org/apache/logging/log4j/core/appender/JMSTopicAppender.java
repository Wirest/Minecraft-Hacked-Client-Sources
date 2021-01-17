// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.SerializedLayout;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.LogEvent;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.net.JMSTopicManager;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "JMSTopic", category = "Core", elementType = "appender", printObject = true)
public final class JMSTopicAppender extends AbstractAppender
{
    private final JMSTopicManager manager;
    
    private JMSTopicAppender(final String name, final Filter filter, final Layout<? extends Serializable> layout, final JMSTopicManager manager, final boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
        this.manager = manager;
    }
    
    @Override
    public void append(final LogEvent event) {
        try {
            this.manager.send((Serializable)this.getLayout().toSerializable(event));
        }
        catch (Exception ex) {
            throw new AppenderLoggingException(ex);
        }
    }
    
    @PluginFactory
    public static JMSTopicAppender createAppender(@PluginAttribute("name") final String name, @PluginAttribute("factoryName") final String factoryName, @PluginAttribute("providerURL") final String providerURL, @PluginAttribute("urlPkgPrefixes") final String urlPkgPrefixes, @PluginAttribute("securityPrincipalName") final String securityPrincipalName, @PluginAttribute("securityCredentials") final String securityCredentials, @PluginAttribute("factoryBindingName") final String factoryBindingName, @PluginAttribute("topicBindingName") final String topicBindingName, @PluginAttribute("userName") final String userName, @PluginAttribute("password") final String password, @PluginElement("Layout") Layout<? extends Serializable> layout, @PluginElement("Filters") final Filter filter, @PluginAttribute("ignoreExceptions") final String ignore) {
        if (name == null) {
            JMSTopicAppender.LOGGER.error("No name provided for JMSQueueAppender");
            return null;
        }
        final boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        final JMSTopicManager manager = JMSTopicManager.getJMSTopicManager(factoryName, providerURL, urlPkgPrefixes, securityPrincipalName, securityCredentials, factoryBindingName, topicBindingName, userName, password);
        if (manager == null) {
            return null;
        }
        if (layout == null) {
            layout = SerializedLayout.createLayout();
        }
        return new JMSTopicAppender(name, filter, layout, manager, ignoreExceptions);
    }
}
