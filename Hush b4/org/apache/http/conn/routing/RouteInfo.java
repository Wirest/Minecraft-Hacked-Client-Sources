// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn.routing;

import java.net.InetAddress;
import org.apache.http.HttpHost;

public interface RouteInfo
{
    HttpHost getTargetHost();
    
    InetAddress getLocalAddress();
    
    int getHopCount();
    
    HttpHost getHopTarget(final int p0);
    
    HttpHost getProxyHost();
    
    TunnelType getTunnelType();
    
    boolean isTunnelled();
    
    LayerType getLayerType();
    
    boolean isLayered();
    
    boolean isSecure();
    
    public enum TunnelType
    {
        PLAIN, 
        TUNNELLED;
    }
    
    public enum LayerType
    {
        PLAIN, 
        LAYERED;
    }
}
