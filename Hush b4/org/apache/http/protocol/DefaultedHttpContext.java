// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.protocol;

import org.apache.http.util.Args;

@Deprecated
public final class DefaultedHttpContext implements HttpContext
{
    private final HttpContext local;
    private final HttpContext defaults;
    
    public DefaultedHttpContext(final HttpContext local, final HttpContext defaults) {
        this.local = Args.notNull(local, "HTTP context");
        this.defaults = defaults;
    }
    
    public Object getAttribute(final String id) {
        final Object obj = this.local.getAttribute(id);
        if (obj == null) {
            return this.defaults.getAttribute(id);
        }
        return obj;
    }
    
    public Object removeAttribute(final String id) {
        return this.local.removeAttribute(id);
    }
    
    public void setAttribute(final String id, final Object obj) {
        this.local.setAttribute(id, obj);
    }
    
    public HttpContext getDefaults() {
        return this.defaults;
    }
    
    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        buf.append("[local: ").append(this.local);
        buf.append("defaults: ").append(this.defaults);
        buf.append("]");
        return buf.toString();
    }
}
