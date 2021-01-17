// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.appender;

import org.apache.logging.log4j.core.net.DatagramSocketManager;
import org.apache.logging.log4j.core.net.TCPSocketManager;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.util.EnglishEnums;
import org.apache.logging.log4j.core.net.Protocol;
import org.apache.logging.log4j.core.layout.SerializedLayout;
import org.apache.logging.log4j.core.helpers.Booleans;
import org.apache.logging.log4j.core.config.plugins.PluginConfiguration;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import java.util.Map;
import java.util.HashMap;
import org.apache.logging.log4j.core.net.AbstractSocketManager;
import org.apache.logging.log4j.core.Filter;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.net.Advertiser;
import org.apache.logging.log4j.core.config.plugins.Plugin;

@Plugin(name = "Socket", category = "Core", elementType = "appender", printObject = true)
public class SocketAppender extends AbstractOutputStreamAppender
{
    private Object advertisement;
    private final Advertiser advertiser;
    
    protected SocketAppender(final String name, final Layout<? extends Serializable> layout, final Filter filter, final AbstractSocketManager manager, final boolean ignoreExceptions, final boolean immediateFlush, final Advertiser advertiser) {
        super(name, layout, filter, ignoreExceptions, immediateFlush, manager);
        if (advertiser != null) {
            final Map<String, String> configuration = new HashMap<String, String>(layout.getContentFormat());
            configuration.putAll(manager.getContentFormat());
            configuration.put("contentType", layout.getContentType());
            configuration.put("name", name);
            this.advertisement = advertiser.advertise(configuration);
        }
        this.advertiser = advertiser;
    }
    
    @Override
    public void stop() {
        super.stop();
        if (this.advertiser != null) {
            this.advertiser.unadvertise(this.advertisement);
        }
    }
    
    @PluginFactory
    public static SocketAppender createAppender(@PluginAttribute("host") final String host, @PluginAttribute("port") final String portNum, @PluginAttribute("protocol") final String protocol, @PluginAttribute("reconnectionDelay") final String delay, @PluginAttribute("immediateFail") final String immediateFail, @PluginAttribute("name") final String name, @PluginAttribute("immediateFlush") final String immediateFlush, @PluginAttribute("ignoreExceptions") final String ignore, @PluginElement("Layout") Layout<? extends Serializable> layout, @PluginElement("Filters") final Filter filter, @PluginAttribute("advertise") final String advertise, @PluginConfiguration final Configuration config) {
        boolean isFlush = Booleans.parseBoolean(immediateFlush, true);
        final boolean isAdvertise = Boolean.parseBoolean(advertise);
        final boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        final boolean fail = Booleans.parseBoolean(immediateFail, true);
        final int reconnectDelay = AbstractAppender.parseInt(delay, 0);
        final int port = AbstractAppender.parseInt(portNum, 0);
        if (layout == null) {
            layout = SerializedLayout.createLayout();
        }
        if (name == null) {
            SocketAppender.LOGGER.error("No name provided for SocketAppender");
            return null;
        }
        final Protocol p = EnglishEnums.valueOf(Protocol.class, (protocol != null) ? protocol : Protocol.TCP.name());
        if (p.equals(Protocol.UDP)) {
            isFlush = true;
        }
        final AbstractSocketManager manager = createSocketManager(p, host, port, reconnectDelay, fail, layout);
        if (manager == null) {
            return null;
        }
        return new SocketAppender(name, layout, filter, manager, ignoreExceptions, isFlush, isAdvertise ? config.getAdvertiser() : null);
    }
    
    protected static AbstractSocketManager createSocketManager(final Protocol p, final String host, final int port, final int delay, final boolean immediateFail, final Layout<? extends Serializable> layout) {
        switch (p) {
            case TCP: {
                return TCPSocketManager.getSocketManager(host, port, delay, immediateFail, layout);
            }
            case UDP: {
                return DatagramSocketManager.getSocketManager(host, port, layout);
            }
            default: {
                return null;
            }
        }
    }
}
