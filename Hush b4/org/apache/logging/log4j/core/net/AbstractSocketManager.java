// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.net;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;
import org.apache.logging.log4j.core.Layout;
import java.io.OutputStream;
import java.net.InetAddress;
import org.apache.logging.log4j.core.appender.OutputStreamManager;

public abstract class AbstractSocketManager extends OutputStreamManager
{
    protected final InetAddress address;
    protected final String host;
    protected final int port;
    
    public AbstractSocketManager(final String name, final OutputStream os, final InetAddress addr, final String host, final int port, final Layout<? extends Serializable> layout) {
        super(os, name, layout);
        this.address = addr;
        this.host = host;
        this.port = port;
    }
    
    @Override
    public Map<String, String> getContentFormat() {
        final Map<String, String> result = new HashMap<String, String>(super.getContentFormat());
        result.put("port", Integer.toString(this.port));
        result.put("address", this.address.getHostAddress());
        return result;
    }
}
