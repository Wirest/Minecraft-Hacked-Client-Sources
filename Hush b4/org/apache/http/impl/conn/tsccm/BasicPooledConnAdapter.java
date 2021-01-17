// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.conn.tsccm;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.conn.AbstractPoolEntry;
import org.apache.http.impl.conn.AbstractPooledConnAdapter;

@Deprecated
public class BasicPooledConnAdapter extends AbstractPooledConnAdapter
{
    protected BasicPooledConnAdapter(final ThreadSafeClientConnManager tsccm, final AbstractPoolEntry entry) {
        super(tsccm, entry);
        this.markReusable();
    }
    
    @Override
    protected ClientConnectionManager getManager() {
        return super.getManager();
    }
    
    @Override
    protected AbstractPoolEntry getPoolEntry() {
        return super.getPoolEntry();
    }
    
    @Override
    protected void detach() {
        super.detach();
    }
}
