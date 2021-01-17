// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.conn.routing;

import org.apache.http.util.LangUtils;
import org.apache.http.util.Asserts;
import org.apache.http.util.Args;
import java.net.InetAddress;
import org.apache.http.HttpHost;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public final class RouteTracker implements RouteInfo, Cloneable
{
    private final HttpHost targetHost;
    private final InetAddress localAddress;
    private boolean connected;
    private HttpHost[] proxyChain;
    private TunnelType tunnelled;
    private LayerType layered;
    private boolean secure;
    
    public RouteTracker(final HttpHost target, final InetAddress local) {
        Args.notNull(target, "Target host");
        this.targetHost = target;
        this.localAddress = local;
        this.tunnelled = TunnelType.PLAIN;
        this.layered = LayerType.PLAIN;
    }
    
    public void reset() {
        this.connected = false;
        this.proxyChain = null;
        this.tunnelled = TunnelType.PLAIN;
        this.layered = LayerType.PLAIN;
        this.secure = false;
    }
    
    public RouteTracker(final HttpRoute route) {
        this(route.getTargetHost(), route.getLocalAddress());
    }
    
    public final void connectTarget(final boolean secure) {
        Asserts.check(!this.connected, "Already connected");
        this.connected = true;
        this.secure = secure;
    }
    
    public final void connectProxy(final HttpHost proxy, final boolean secure) {
        Args.notNull(proxy, "Proxy host");
        Asserts.check(!this.connected, "Already connected");
        this.connected = true;
        this.proxyChain = new HttpHost[] { proxy };
        this.secure = secure;
    }
    
    public final void tunnelTarget(final boolean secure) {
        Asserts.check(this.connected, "No tunnel unless connected");
        Asserts.notNull(this.proxyChain, "No tunnel without proxy");
        this.tunnelled = TunnelType.TUNNELLED;
        this.secure = secure;
    }
    
    public final void tunnelProxy(final HttpHost proxy, final boolean secure) {
        Args.notNull(proxy, "Proxy host");
        Asserts.check(this.connected, "No tunnel unless connected");
        Asserts.notNull(this.proxyChain, "No tunnel without proxy");
        final HttpHost[] proxies = new HttpHost[this.proxyChain.length + 1];
        System.arraycopy(this.proxyChain, 0, proxies, 0, this.proxyChain.length);
        proxies[proxies.length - 1] = proxy;
        this.proxyChain = proxies;
        this.secure = secure;
    }
    
    public final void layerProtocol(final boolean secure) {
        Asserts.check(this.connected, "No layered protocol unless connected");
        this.layered = LayerType.LAYERED;
        this.secure = secure;
    }
    
    public final HttpHost getTargetHost() {
        return this.targetHost;
    }
    
    public final InetAddress getLocalAddress() {
        return this.localAddress;
    }
    
    public final int getHopCount() {
        int hops = 0;
        if (this.connected) {
            if (this.proxyChain == null) {
                hops = 1;
            }
            else {
                hops = this.proxyChain.length + 1;
            }
        }
        return hops;
    }
    
    public final HttpHost getHopTarget(final int hop) {
        Args.notNegative(hop, "Hop index");
        final int hopcount = this.getHopCount();
        Args.check(hop < hopcount, "Hop index exceeds tracked route length");
        HttpHost result = null;
        if (hop < hopcount - 1) {
            result = this.proxyChain[hop];
        }
        else {
            result = this.targetHost;
        }
        return result;
    }
    
    public final HttpHost getProxyHost() {
        return (this.proxyChain == null) ? null : this.proxyChain[0];
    }
    
    public final boolean isConnected() {
        return this.connected;
    }
    
    public final TunnelType getTunnelType() {
        return this.tunnelled;
    }
    
    public final boolean isTunnelled() {
        return this.tunnelled == TunnelType.TUNNELLED;
    }
    
    public final LayerType getLayerType() {
        return this.layered;
    }
    
    public final boolean isLayered() {
        return this.layered == LayerType.LAYERED;
    }
    
    public final boolean isSecure() {
        return this.secure;
    }
    
    public final HttpRoute toRoute() {
        return this.connected ? new HttpRoute(this.targetHost, this.localAddress, this.proxyChain, this.secure, this.tunnelled, this.layered) : null;
    }
    
    @Override
    public final boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RouteTracker)) {
            return false;
        }
        final RouteTracker that = (RouteTracker)o;
        return this.connected == that.connected && this.secure == that.secure && this.tunnelled == that.tunnelled && this.layered == that.layered && LangUtils.equals(this.targetHost, that.targetHost) && LangUtils.equals(this.localAddress, that.localAddress) && LangUtils.equals(this.proxyChain, that.proxyChain);
    }
    
    @Override
    public final int hashCode() {
        int hash = 17;
        hash = LangUtils.hashCode(hash, this.targetHost);
        hash = LangUtils.hashCode(hash, this.localAddress);
        if (this.proxyChain != null) {
            for (final HttpHost element : this.proxyChain) {
                hash = LangUtils.hashCode(hash, element);
            }
        }
        hash = LangUtils.hashCode(hash, this.connected);
        hash = LangUtils.hashCode(hash, this.secure);
        hash = LangUtils.hashCode(hash, this.tunnelled);
        hash = LangUtils.hashCode(hash, this.layered);
        return hash;
    }
    
    @Override
    public final String toString() {
        final StringBuilder cab = new StringBuilder(50 + this.getHopCount() * 30);
        cab.append("RouteTracker[");
        if (this.localAddress != null) {
            cab.append(this.localAddress);
            cab.append("->");
        }
        cab.append('{');
        if (this.connected) {
            cab.append('c');
        }
        if (this.tunnelled == TunnelType.TUNNELLED) {
            cab.append('t');
        }
        if (this.layered == LayerType.LAYERED) {
            cab.append('l');
        }
        if (this.secure) {
            cab.append('s');
        }
        cab.append("}->");
        if (this.proxyChain != null) {
            for (final HttpHost element : this.proxyChain) {
                cab.append(element);
                cab.append("->");
            }
        }
        cab.append(this.targetHost);
        cab.append(']');
        return cab.toString();
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
