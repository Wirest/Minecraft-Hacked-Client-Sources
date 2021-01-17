// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.client;

import org.apache.http.util.Args;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.pool.ConnPoolControl;
import org.apache.http.client.BackoffManager;

public class AIMDBackoffManager implements BackoffManager
{
    private final ConnPoolControl<HttpRoute> connPerRoute;
    private final Clock clock;
    private final Map<HttpRoute, Long> lastRouteProbes;
    private final Map<HttpRoute, Long> lastRouteBackoffs;
    private long coolDown;
    private double backoffFactor;
    private int cap;
    
    public AIMDBackoffManager(final ConnPoolControl<HttpRoute> connPerRoute) {
        this(connPerRoute, new SystemClock());
    }
    
    AIMDBackoffManager(final ConnPoolControl<HttpRoute> connPerRoute, final Clock clock) {
        this.coolDown = 5000L;
        this.backoffFactor = 0.5;
        this.cap = 2;
        this.clock = clock;
        this.connPerRoute = connPerRoute;
        this.lastRouteProbes = new HashMap<HttpRoute, Long>();
        this.lastRouteBackoffs = new HashMap<HttpRoute, Long>();
    }
    
    public void backOff(final HttpRoute route) {
        synchronized (this.connPerRoute) {
            final int curr = this.connPerRoute.getMaxPerRoute(route);
            final Long lastUpdate = this.getLastUpdate(this.lastRouteBackoffs, route);
            final long now = this.clock.getCurrentTime();
            if (now - lastUpdate < this.coolDown) {
                return;
            }
            this.connPerRoute.setMaxPerRoute(route, this.getBackedOffPoolSize(curr));
            this.lastRouteBackoffs.put(route, now);
        }
    }
    
    private int getBackedOffPoolSize(final int curr) {
        if (curr <= 1) {
            return 1;
        }
        return (int)Math.floor(this.backoffFactor * curr);
    }
    
    public void probe(final HttpRoute route) {
        synchronized (this.connPerRoute) {
            final int curr = this.connPerRoute.getMaxPerRoute(route);
            final int max = (curr >= this.cap) ? this.cap : (curr + 1);
            final Long lastProbe = this.getLastUpdate(this.lastRouteProbes, route);
            final Long lastBackoff = this.getLastUpdate(this.lastRouteBackoffs, route);
            final long now = this.clock.getCurrentTime();
            if (now - lastProbe < this.coolDown || now - lastBackoff < this.coolDown) {
                return;
            }
            this.connPerRoute.setMaxPerRoute(route, max);
            this.lastRouteProbes.put(route, now);
        }
    }
    
    private Long getLastUpdate(final Map<HttpRoute, Long> updates, final HttpRoute route) {
        Long lastUpdate = updates.get(route);
        if (lastUpdate == null) {
            lastUpdate = 0L;
        }
        return lastUpdate;
    }
    
    public void setBackoffFactor(final double d) {
        Args.check(d > 0.0 && d < 1.0, "Backoff factor must be 0.0 < f < 1.0");
        this.backoffFactor = d;
    }
    
    public void setCooldownMillis(final long l) {
        Args.positive(this.coolDown, "Cool down");
        this.coolDown = l;
    }
    
    public void setPerHostConnectionCap(final int cap) {
        Args.positive(cap, "Per host connection cap");
        this.cap = cap;
    }
}
