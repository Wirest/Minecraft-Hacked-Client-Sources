// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.protocol;

@Deprecated
public class SyncBasicHttpContext extends BasicHttpContext
{
    public SyncBasicHttpContext(final HttpContext parentContext) {
        super(parentContext);
    }
    
    public SyncBasicHttpContext() {
    }
    
    @Override
    public synchronized Object getAttribute(final String id) {
        return super.getAttribute(id);
    }
    
    @Override
    public synchronized void setAttribute(final String id, final Object obj) {
        super.setAttribute(id, obj);
    }
    
    @Override
    public synchronized Object removeAttribute(final String id) {
        return super.removeAttribute(id);
    }
    
    @Override
    public synchronized void clear() {
        super.clear();
    }
}
