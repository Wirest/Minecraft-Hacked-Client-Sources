// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.conn;

import java.util.Iterator;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpConnection;
import java.util.Map;
import org.apache.commons.logging.Log;

@Deprecated
public class IdleConnectionHandler
{
    private final Log log;
    private final Map<HttpConnection, TimeValues> connectionToTimes;
    
    public IdleConnectionHandler() {
        this.log = LogFactory.getLog(this.getClass());
        this.connectionToTimes = new HashMap<HttpConnection, TimeValues>();
    }
    
    public void add(final HttpConnection connection, final long validDuration, final TimeUnit unit) {
        final long timeAdded = System.currentTimeMillis();
        if (this.log.isDebugEnabled()) {
            this.log.debug("Adding connection at: " + timeAdded);
        }
        this.connectionToTimes.put(connection, new TimeValues(timeAdded, validDuration, unit));
    }
    
    public boolean remove(final HttpConnection connection) {
        final TimeValues times = this.connectionToTimes.remove(connection);
        if (times == null) {
            this.log.warn("Removing a connection that never existed!");
            return true;
        }
        return System.currentTimeMillis() <= times.timeExpires;
    }
    
    public void removeAll() {
        this.connectionToTimes.clear();
    }
    
    public void closeIdleConnections(final long idleTime) {
        final long idleTimeout = System.currentTimeMillis() - idleTime;
        if (this.log.isDebugEnabled()) {
            this.log.debug("Checking for connections, idle timeout: " + idleTimeout);
        }
        for (final Map.Entry<HttpConnection, TimeValues> entry : this.connectionToTimes.entrySet()) {
            final HttpConnection conn = entry.getKey();
            final TimeValues times = entry.getValue();
            final long connectionTime = times.timeAdded;
            if (connectionTime <= idleTimeout) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Closing idle connection, connection time: " + connectionTime);
                }
                try {
                    conn.close();
                }
                catch (IOException ex) {
                    this.log.debug("I/O error closing connection", ex);
                }
            }
        }
    }
    
    public void closeExpiredConnections() {
        final long now = System.currentTimeMillis();
        if (this.log.isDebugEnabled()) {
            this.log.debug("Checking for expired connections, now: " + now);
        }
        for (final Map.Entry<HttpConnection, TimeValues> entry : this.connectionToTimes.entrySet()) {
            final HttpConnection conn = entry.getKey();
            final TimeValues times = entry.getValue();
            if (times.timeExpires <= now) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug("Closing connection, expired @: " + times.timeExpires);
                }
                try {
                    conn.close();
                }
                catch (IOException ex) {
                    this.log.debug("I/O error closing connection", ex);
                }
            }
        }
    }
    
    private static class TimeValues
    {
        private final long timeAdded;
        private final long timeExpires;
        
        TimeValues(final long now, final long validDuration, final TimeUnit validUnit) {
            this.timeAdded = now;
            if (validDuration > 0L) {
                this.timeExpires = now + validUnit.toMillis(validDuration);
            }
            else {
                this.timeExpires = Long.MAX_VALUE;
            }
        }
    }
}
