// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jmx;

import java.util.Map;
import java.io.Reader;
import java.io.Closeable;
import org.apache.logging.log4j.core.helpers.Closer;
import java.io.InputStreamReader;
import java.io.InputStream;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import java.io.ByteArrayInputStream;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.Charset;
import org.apache.logging.log4j.core.helpers.Charsets;
import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URI;
import java.io.File;
import org.apache.logging.log4j.core.config.Configuration;
import javax.management.Notification;
import org.apache.logging.log4j.core.helpers.Assert;
import javax.management.MBeanNotificationInfo;
import java.util.concurrent.Executor;
import org.apache.logging.log4j.core.LoggerContext;
import javax.management.ObjectName;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.status.StatusLogger;
import java.beans.PropertyChangeListener;
import javax.management.NotificationBroadcasterSupport;

public class LoggerContextAdmin extends NotificationBroadcasterSupport implements LoggerContextAdminMBean, PropertyChangeListener
{
    private static final int PAGE = 4096;
    private static final int TEXT_BUFFER = 65536;
    private static final int BUFFER_SIZE = 2048;
    private static final StatusLogger LOGGER;
    private final AtomicLong sequenceNo;
    private final ObjectName objectName;
    private final LoggerContext loggerContext;
    private String customConfigText;
    
    public LoggerContextAdmin(final LoggerContext loggerContext, final Executor executor) {
        super(executor, new MBeanNotificationInfo[] { createNotificationInfo() });
        this.sequenceNo = new AtomicLong();
        this.loggerContext = Assert.isNotNull(loggerContext, "loggerContext");
        try {
            final String ctxName = Server.escape(loggerContext.getName());
            final String name = String.format("org.apache.logging.log4j2:type=LoggerContext,ctx=%s", ctxName);
            this.objectName = new ObjectName(name);
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
        loggerContext.addPropertyChangeListener(this);
    }
    
    private static MBeanNotificationInfo createNotificationInfo() {
        final String[] notifTypes = { "com.apache.logging.log4j.core.jmx.config.reconfigured" };
        final String name = Notification.class.getName();
        final String description = "Configuration reconfigured";
        return new MBeanNotificationInfo(notifTypes, name, "Configuration reconfigured");
    }
    
    @Override
    public String getStatus() {
        return this.loggerContext.getStatus().toString();
    }
    
    @Override
    public String getName() {
        return this.loggerContext.getName();
    }
    
    private Configuration getConfig() {
        return this.loggerContext.getConfiguration();
    }
    
    @Override
    public String getConfigLocationURI() {
        if (this.loggerContext.getConfigLocation() != null) {
            return String.valueOf(this.loggerContext.getConfigLocation());
        }
        if (this.getConfigName() != null) {
            return String.valueOf(new File(this.getConfigName()).toURI());
        }
        return "";
    }
    
    @Override
    public void setConfigLocationURI(final String configLocation) throws URISyntaxException, IOException {
        LoggerContextAdmin.LOGGER.debug("---------");
        LoggerContextAdmin.LOGGER.debug("Remote request to reconfigure using location " + configLocation);
        final URI uri = new URI(configLocation);
        uri.toURL().openStream().close();
        this.loggerContext.setConfigLocation(uri);
        LoggerContextAdmin.LOGGER.debug("Completed remote request to reconfigure.");
    }
    
    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        if (!"config".equals(evt.getPropertyName())) {
            return;
        }
        if (this.loggerContext.getConfiguration().getName() != null) {
            this.customConfigText = null;
        }
        final Notification notif = new Notification("com.apache.logging.log4j.core.jmx.config.reconfigured", this.getObjectName(), this.nextSeqNo(), this.now(), null);
        this.sendNotification(notif);
    }
    
    @Override
    public String getConfigText() throws IOException {
        return this.getConfigText(Charsets.UTF_8.name());
    }
    
    @Override
    public String getConfigText(final String charsetName) throws IOException {
        if (this.customConfigText != null) {
            return this.customConfigText;
        }
        try {
            final Charset charset = Charset.forName(charsetName);
            return this.readContents(new URI(this.getConfigLocationURI()), charset);
        }
        catch (Exception ex) {
            final StringWriter sw = new StringWriter(2048);
            ex.printStackTrace(new PrintWriter(sw));
            return sw.toString();
        }
    }
    
    @Override
    public void setConfigText(final String configText, final String charsetName) {
        final String old = this.customConfigText;
        this.customConfigText = Assert.isNotNull(configText, "configText");
        LoggerContextAdmin.LOGGER.debug("---------");
        LoggerContextAdmin.LOGGER.debug("Remote request to reconfigure from config text.");
        try {
            final InputStream in = new ByteArrayInputStream(configText.getBytes(charsetName));
            final ConfigurationFactory.ConfigurationSource source = new ConfigurationFactory.ConfigurationSource(in);
            final Configuration updated = ConfigurationFactory.getInstance().getConfiguration(source);
            this.loggerContext.start(updated);
            LoggerContextAdmin.LOGGER.debug("Completed remote request to reconfigure from config text.");
        }
        catch (Exception ex) {
            this.customConfigText = old;
            final String msg = "Could not reconfigure from config text";
            LoggerContextAdmin.LOGGER.error("Could not reconfigure from config text", ex);
            throw new IllegalArgumentException("Could not reconfigure from config text", ex);
        }
    }
    
    private String readContents(final URI uri, final Charset charset) throws IOException {
        InputStream in = null;
        Reader reader = null;
        try {
            in = uri.toURL().openStream();
            reader = new InputStreamReader(in, charset);
            final StringBuilder result = new StringBuilder(65536);
            final char[] buff = new char[4096];
            int count = -1;
            while ((count = reader.read(buff)) >= 0) {
                result.append(buff, 0, count);
            }
            return result.toString();
        }
        finally {
            Closer.closeSilent(in);
            Closer.closeSilent(reader);
        }
    }
    
    @Override
    public String getConfigName() {
        return this.getConfig().getName();
    }
    
    @Override
    public String getConfigClassName() {
        return this.getConfig().getClass().getName();
    }
    
    @Override
    public String getConfigFilter() {
        return String.valueOf(this.getConfig().getFilter());
    }
    
    @Override
    public String getConfigMonitorClassName() {
        return this.getConfig().getConfigurationMonitor().getClass().getName();
    }
    
    @Override
    public Map<String, String> getConfigProperties() {
        return this.getConfig().getProperties();
    }
    
    public ObjectName getObjectName() {
        return this.objectName;
    }
    
    private long nextSeqNo() {
        return this.sequenceNo.getAndIncrement();
    }
    
    private long now() {
        return System.currentTimeMillis();
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
    }
}
