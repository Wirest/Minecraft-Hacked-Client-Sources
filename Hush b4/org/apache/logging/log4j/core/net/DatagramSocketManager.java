// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net;

import java.net.UnknownHostException;
import org.apache.logging.log4j.Logger;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.appender.ManagerFactory;
import org.apache.logging.log4j.core.appender.OutputStreamManager;
import org.apache.logging.log4j.core.helpers.Strings;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import java.net.InetAddress;
import java.io.OutputStream;

public class DatagramSocketManager extends AbstractSocketManager
{
    private static final DatagramSocketManagerFactory FACTORY;
    
    protected DatagramSocketManager(final String name, final OutputStream os, final InetAddress address, final String host, final int port, final Layout<? extends Serializable> layout) {
        super(name, os, address, host, port, layout);
    }
    
    public static DatagramSocketManager getSocketManager(final String host, final int port, final Layout<? extends Serializable> layout) {
        if (Strings.isEmpty(host)) {
            throw new IllegalArgumentException("A host name is required");
        }
        if (port <= 0) {
            throw new IllegalArgumentException("A port value is required");
        }
        return (DatagramSocketManager)OutputStreamManager.getManager("UDP:" + host + ":" + port, new FactoryData(host, port, layout), DatagramSocketManager.FACTORY);
    }
    
    @Override
    public Map<String, String> getContentFormat() {
        final Map<String, String> result = new HashMap<String, String>(super.getContentFormat());
        result.put("protocol", "udp");
        result.put("direction", "out");
        return result;
    }
    
    static {
        FACTORY = new DatagramSocketManagerFactory();
    }
    
    private static class FactoryData
    {
        private final String host;
        private final int port;
        private final Layout<? extends Serializable> layout;
        
        public FactoryData(final String host, final int port, final Layout<? extends Serializable> layout) {
            this.host = host;
            this.port = port;
            this.layout = layout;
        }
    }
    
    private static class DatagramSocketManagerFactory implements ManagerFactory<DatagramSocketManager, FactoryData>
    {
        @Override
        public DatagramSocketManager createManager(final String name, final FactoryData data) {
            final OutputStream os = new DatagramOutputStream(data.host, data.port, data.layout.getHeader(), data.layout.getFooter());
            InetAddress address;
            try {
                address = InetAddress.getByName(data.host);
            }
            catch (UnknownHostException ex) {
                DatagramSocketManager.LOGGER.error("Could not find address of " + data.host, ex);
                return null;
            }
            return new DatagramSocketManager(name, os, address, data.host, data.port, data.layout);
        }
    }
}
