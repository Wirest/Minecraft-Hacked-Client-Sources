// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.util.EnglishEnums;
import org.apache.logging.log4j.core.net.Protocol;
import org.apache.logging.log4j.core.layout.SyslogLayout;
import org.apache.logging.log4j.core.layout.RFC5424Layout;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.layout.LoggerFields;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.net.AbstractSocketManager;
import org.apache.logging.log4j.core.Filter;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "Syslog", category = "Core", elementType = "appender", printObject = true)
public class SyslogAppender extends SocketAppender
{
    protected static final String RFC5424 = "RFC5424";
    
    protected SyslogAppender(final String name, final Layout<? extends Serializable> layout, final Filter filter, final boolean ignoreExceptions, final boolean immediateFlush, final AbstractSocketManager manager, final Advertiser advertiser) {
        super(name, layout, filter, manager, ignoreExceptions, immediateFlush, advertiser);
    }
    
    @PluginFactory
    public static SyslogAppender createAppender(@PluginAttribute("host") final String host, @PluginAttribute("port") final String portNum, @PluginAttribute("protocol") final String protocol, @PluginAttribute("reconnectionDelay") final String delay, @PluginAttribute("immediateFail") final String immediateFail, @PluginAttribute("name") final String name, @PluginAttribute("immediateFlush") final String immediateFlush, @PluginAttribute("ignoreExceptions") final String ignore, @PluginAttribute("facility") final String facility, @PluginAttribute("id") final String id, @PluginAttribute("enterpriseNumber") final String ein, @PluginAttribute("includeMDC") final String includeMDC, @PluginAttribute("mdcId") final String mdcId, @PluginAttribute("mdcPrefix") final String mdcPrefix, @PluginAttribute("eventPrefix") final String eventPrefix, @PluginAttribute("newLine") final String includeNL, @PluginAttribute("newLineEscape") final String escapeNL, @PluginAttribute("appName") final String appName, @PluginAttribute("messageId") final String msgId, @PluginAttribute("mdcExcludes") final String excludes, @PluginAttribute("mdcIncludes") final String includes, @PluginAttribute("mdcRequired") final String required, @PluginAttribute("format") final String format, @PluginElement("Filters") final Filter filter, @PluginConfiguration final Configuration config, @PluginAttribute("charset") final String charsetName, @PluginAttribute("exceptionPattern") final String exceptionPattern, @PluginElement("LoggerFields") final LoggerFields[] loggerFields, @PluginAttribute("advertise") final String advertise) {
        final boolean isFlush = Booleans.parseBoolean(immediateFlush, true);
        final boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        final int reconnectDelay = AbstractAppender.parseInt(delay, 0);
        final boolean fail = Booleans.parseBoolean(immediateFail, true);
        final int port = AbstractAppender.parseInt(portNum, 0);
        final boolean isAdvertise = Boolean.parseBoolean(advertise);
        final Layout<? extends Serializable> layout = "RFC5424".equalsIgnoreCase(format) ? RFC5424Layout.createLayout(facility, id, ein, includeMDC, mdcId, mdcPrefix, eventPrefix, includeNL, escapeNL, appName, msgId, excludes, includes, required, exceptionPattern, "false", loggerFields, config) : SyslogLayout.createLayout(facility, includeNL, escapeNL, charsetName);
        if (name == null) {
            SyslogAppender.LOGGER.error("No name provided for SyslogAppender");
            return null;
        }
        final Protocol p = EnglishEnums.valueOf(Protocol.class, protocol);
        final AbstractSocketManager manager = SocketAppender.createSocketManager(p, host, port, reconnectDelay, fail, layout);
        if (manager == null) {
            return null;
        }
        return new SyslogAppender(name, layout, filter, ignoreExceptions, isFlush, manager, isAdvertise ? config.getAdvertiser() : null);
    }
}
