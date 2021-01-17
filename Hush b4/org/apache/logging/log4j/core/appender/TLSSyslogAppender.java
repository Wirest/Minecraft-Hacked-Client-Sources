// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.net.TLSSocketManager;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.SyslogLayout;
import org.apache.logging.log4j.core.layout.RFC5424Layout;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.layout.LoggerFields;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.net.ssl.SSLConfiguration;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.net.AbstractSocketManager;
import org.apache.logging.log4j.core.Filter;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "TLSSyslog", category = "Core", elementType = "appender", printObject = true)
public final class TLSSyslogAppender extends SyslogAppender
{
    protected TLSSyslogAppender(final String name, final Layout<? extends Serializable> layout, final Filter filter, final boolean ignoreExceptions, final boolean immediateFlush, final AbstractSocketManager manager, final Advertiser advertiser) {
        super(name, layout, filter, ignoreExceptions, immediateFlush, manager, advertiser);
    }
    
    @PluginFactory
    public static TLSSyslogAppender createAppender(@PluginAttribute("host") final String host, @PluginAttribute("port") final String portNum, @PluginElement("ssl") final SSLConfiguration sslConfig, @PluginAttribute("reconnectionDelay") final String delay, @PluginAttribute("immediateFail") final String immediateFail, @PluginAttribute("name") final String name, @PluginAttribute("immediateFlush") final String immediateFlush, @PluginAttribute("ignoreExceptions") final String ignore, @PluginAttribute("facility") final String facility, @PluginAttribute("id") final String id, @PluginAttribute("enterpriseNumber") final String ein, @PluginAttribute("includeMDC") final String includeMDC, @PluginAttribute("mdcId") final String mdcId, @PluginAttribute("mdcPrefix") final String mdcPrefix, @PluginAttribute("eventPrefix") final String eventPrefix, @PluginAttribute("newLine") final String includeNL, @PluginAttribute("newLineEscape") final String escapeNL, @PluginAttribute("appName") final String appName, @PluginAttribute("messageId") final String msgId, @PluginAttribute("mdcExcludes") final String excludes, @PluginAttribute("mdcIncludes") final String includes, @PluginAttribute("mdcRequired") final String required, @PluginAttribute("format") final String format, @PluginElement("filters") final Filter filter, @PluginConfiguration final Configuration config, @PluginAttribute("charset") final String charsetName, @PluginAttribute("exceptionPattern") final String exceptionPattern, @PluginElement("LoggerFields") final LoggerFields[] loggerFields, @PluginAttribute("advertise") final String advertise) {
        final boolean isFlush = Booleans.parseBoolean(immediateFlush, true);
        final boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        final int reconnectDelay = AbstractAppender.parseInt(delay, 0);
        final boolean fail = Booleans.parseBoolean(immediateFail, true);
        final int port = AbstractAppender.parseInt(portNum, 0);
        final boolean isAdvertise = Boolean.parseBoolean(advertise);
        final Layout<? extends Serializable> layout = "RFC5424".equalsIgnoreCase(format) ? RFC5424Layout.createLayout(facility, id, ein, includeMDC, mdcId, mdcPrefix, eventPrefix, includeNL, escapeNL, appName, msgId, excludes, includes, required, exceptionPattern, "true", loggerFields, config) : SyslogLayout.createLayout(facility, includeNL, escapeNL, charsetName);
        if (name == null) {
            TLSSyslogAppender.LOGGER.error("No name provided for TLSSyslogAppender");
            return null;
        }
        final AbstractSocketManager manager = createSocketManager(sslConfig, host, port, reconnectDelay, fail, layout);
        if (manager == null) {
            return null;
        }
        return new TLSSyslogAppender(name, layout, filter, ignoreExceptions, isFlush, manager, isAdvertise ? config.getAdvertiser() : null);
    }
    
    public static AbstractSocketManager createSocketManager(final SSLConfiguration sslConf, final String host, final int port, final int reconnectDelay, final boolean fail, final Layout<? extends Serializable> layout) {
        return TLSSocketManager.getSocketManager(sslConf, host, port, reconnectDelay, fail, layout);
    }
}
