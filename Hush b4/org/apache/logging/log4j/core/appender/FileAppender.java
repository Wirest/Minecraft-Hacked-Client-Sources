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
import java.util.Map;
import java.util.HashMap;
import org.apache.logging.log4j.core.Filter;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "File", category = "Core", elementType = "appender", printObject = true)
public final class FileAppender extends AbstractOutputStreamAppender
{
    private final String fileName;
    private final Advertiser advertiser;
    private Object advertisement;
    
    private FileAppender(final String name, final Layout<? extends Serializable> layout, final Filter filter, final FileManager manager, final String filename, final boolean ignoreExceptions, final boolean immediateFlush, final Advertiser advertiser) {
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
    
    public String getFileName() {
        return this.fileName;
    }
    
    @PluginFactory
    public static FileAppender createAppender(@PluginAttribute("fileName") final String fileName, @PluginAttribute("append") final String append, @PluginAttribute("locking") final String locking, @PluginAttribute("name") final String name, @PluginAttribute("immediateFlush") final String immediateFlush, @PluginAttribute("ignoreExceptions") final String ignore, @PluginAttribute("bufferedIO") final String bufferedIO, @PluginElement("Layout") Layout<? extends Serializable> layout, @PluginElement("Filters") final Filter filter, @PluginAttribute("advertise") final String advertise, @PluginAttribute("advertiseURI") final String advertiseURI, @PluginConfiguration final Configuration config) {
        final boolean isAppend = Booleans.parseBoolean(append, true);
        final boolean isLocking = Boolean.parseBoolean(locking);
        boolean isBuffered = Booleans.parseBoolean(bufferedIO, true);
        final boolean isAdvertise = Boolean.parseBoolean(advertise);
        if (isLocking && isBuffered) {
            if (bufferedIO != null) {
                FileAppender.LOGGER.warn("Locking and buffering are mutually exclusive. No buffering will occur for " + fileName);
            }
            isBuffered = false;
        }
        final boolean isFlush = Booleans.parseBoolean(immediateFlush, true);
        final boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        if (name == null) {
            FileAppender.LOGGER.error("No name provided for FileAppender");
            return null;
        }
        if (fileName == null) {
            FileAppender.LOGGER.error("No filename provided for FileAppender with name " + name);
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createLayout(null, null, null, null, null);
        }
        final FileManager manager = FileManager.getFileManager(fileName, isAppend, isLocking, isBuffered, advertiseURI, layout);
        if (manager == null) {
            return null;
        }
        return new FileAppender(name, layout, filter, manager, fileName, ignoreExceptions, isFlush, isAdvertise ? config.getAdvertiser() : null);
    }
}
