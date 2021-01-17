// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn.routing;

public interface HttpRouteDirector
{
    public static final int UNREACHABLE = -1;
    public static final int COMPLETE = 0;
    public static final int CONNECT_TARGET = 1;
    public static final int CONNECT_PROXY = 2;
    public static final int TUNNEL_TARGET = 3;
    public static final int TUNNEL_PROXY = 4;
    public static final int LAYER_PROTOCOL = 5;
    
    int nextStep(final RouteInfo p0, final RouteInfo p1);
}
