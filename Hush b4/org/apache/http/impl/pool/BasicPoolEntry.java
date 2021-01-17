// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.pool;

import java.io.IOException;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpHost;
import org.apache.http.pool.PoolEntry;

@ThreadSafe
public class BasicPoolEntry extends PoolEntry<HttpHost, HttpClientConnection>
{
    public BasicPoolEntry(final String id, final HttpHost route, final HttpClientConnection conn) {
        super(id, route, conn);
    }
    
    @Override
    public void close() {
        try {
            ((PoolEntry<T, HttpClientConnection>)this).getConnection().close();
        }
        catch (IOException ex) {}
    }
    
    @Override
    public boolean isClosed() {
        return !((PoolEntry<T, HttpClientConnection>)this).getConnection().isOpen();
    }
}
