// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.pattern.RegexReplacement;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.LogEvent;
import java.util.Map;
import java.util.HashMap;
import org.apache.logging.log4j.core.Filter;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "RandomAccessFile", category = "Core", elementType = "appender", printObject = true)
public final class RandomAccessFileAppender extends AbstractOutputStreamAppender
{
    private final String fileName;
    private Object advertisement;
    private final Advertiser advertiser;
    
    private RandomAccessFileAppender(final String name, final Layout<? extends Serializable> layout, final Filter filter, final RandomAccessFileManager manager, final String filename, final boolean ignoreExceptions, final boolean immediateFlush, final Advertiser advertiser) {
        super(name, layout, filter, ignoreExceptions, immediateFlush, manager);
        if (advertiser != null) {
            final Map<String, String> configuration = new HashMap<String, String>(layout.getContentFormat());
            configuration.putAll(manager.getContentFormat());
            configuration.put("contentType", layout.getContentType());
            configuration.put("name", name);
            this.advertisement = advertiser.advertise(configuration);
        }
        this.fileName = filename;
        this.advertiser = advertiser;
    }
    
    @Override
    public void stop() {
        super.stop();
        if (this.advertiser != null) {
            this.advertiser.unadvertise(this.advertisement);
        }
    }
    
    @Override
    public void append(final LogEvent event) {
        ((RandomAccessFileManager)this.getManager()).setEndOfBatch(event.isEndOfBatch());
        super.append(event);
    }
    
    public String getFileName() {
        return this.fileName;
    }
    
    @PluginFactory
    public static RandomAccessFileAppender createAppender(@PluginAttribute("fileName") final String fileName, @PluginAttribute("append") final String append, @PluginAttribute("name") final String name, @PluginAttribute("immediateFlush") final String immediateFlush, @PluginAttribute("ignoreExceptions") final String ignore, @PluginElement("Layout") Layout<? extends Serializable> layout, @PluginElement("Filters") final Filter filter, @PluginAttribute("advertise") final String advertise, @PluginAttribute("advertiseURI") final String advertiseURI, @PluginConfiguration final Configuration config) {
        final boolean isAppend = Booleans.parseBoolean(append, true);
        final boolean isFlush = Booleans.parseBoolean(immediateFlush, true);
        final boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        final boolean isAdvertise = Boolean.parseBoolean(advertise);
        if (name == null) {
            RandomAccessFileAppender.LOGGER.error("No name provided for FileAppender");
            return null;
        }
        if (fileName == null) {
            RandomAccessFileAppender.LOGGER.error("No filename provided for FileAppender with name " + name);
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createLayout(null, null, null, null, null);
        }
        final RandomAccessFileManager manager = RandomAccessFileManager.getFileManager(fileName, isAppend, isFlush, advertiseURI, layout);
        if (manager == null) {
            return null;
        }
        return new RandomAccessFileAppender(name, layout, filter, manager, fileName, ignoreExceptions, isFlush, isAdvertise ? config.getAdvertiser() : null);
    }
}
