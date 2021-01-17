// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.ThresholdFilter;
import org.apache.logging.log4j.core.layout.HTMLLayout;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.net.SMTPManager;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "SMTP", category = "Core", elementType = "appender", printObject = true)
public final class SMTPAppender extends AbstractAppender
{
    private static final int DEFAULT_BUFFER_SIZE = 512;
    protected final SMTPManager manager;
    
    private SMTPAppender(final String name, final Filter filter, final Layout<? extends Serializable> layout, final SMTPManager manager, final boolean ignoreExceptions) {
        super(name, filter, layout, ignoreExceptions);
        this.manager = manager;
    }
    
    @PluginFactory
    public static SMTPAppender createAppender(@PluginAttribute("name") final String name, @PluginAttribute("to") final String to, @PluginAttribute("cc") final String cc, @PluginAttribute("bcc") final String bcc, @PluginAttribute("from") final String from, @PluginAttribute("replyTo") final String replyTo, @PluginAttribute("subject") final String subject, @PluginAttribute("smtpProtocol") final String smtpProtocol, @PluginAttribute("smtpHost") final String smtpHost, @PluginAttribute("smtpPort") final String smtpPortStr, @PluginAttribute("smtpUsername") final String smtpUsername, @PluginAttribute("smtpPassword") final String smtpPassword, @PluginAttribute("smtpDebug") final String smtpDebug, @PluginAttribute("bufferSize") final String bufferSizeStr, @PluginElement("Layout") Layout<? extends Serializable> layout, @PluginElement("Filter") Filter filter, @PluginAttribute("ignoreExceptions") final String ignore) {
        if (name == null) {
            SMTPAppender.LOGGER.error("No name provided for SMTPAppender");
            return null;
        }
        final boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        final int smtpPort = AbstractAppender.parseInt(smtpPortStr, 0);
        final boolean isSmtpDebug = Boolean.parseBoolean(smtpDebug);
        final int bufferSize = (bufferSizeStr == null) ? 512 : Integer.parseInt(bufferSizeStr);
        if (layout == null) {
            layout = HTMLLayout.createLayout(null, null, null, null, null, null);
        }
        if (filter == null) {
            filter = ThresholdFilter.createFilter(null, null, null);
        }
        final SMTPManager manager = SMTPManager.getSMTPManager(to, cc, bcc, from, replyTo, subject, smtpProtocol, smtpHost, smtpPort, smtpUsername, smtpPassword, isSmtpDebug, filter.toString(), bufferSize);
        if (manager == null) {
            return null;
        }
        return new SMTPAppender(name, filter, layout, manager, ignoreExceptions);
    }
    
    @Override
    public boolean isFiltered(final LogEvent event) {
        final boolean filtered = super.isFiltered(event);
        if (filtered) {
            this.manager.add(event);
        }
        return filtered;
    }
    
    @Override
    public void append(final LogEvent event) {
        this.manager.sendEvents(this.getLayout(), event);
    }
}
