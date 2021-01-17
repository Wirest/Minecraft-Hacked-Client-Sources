// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.params;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.http.util.Args;

@Deprecated
public final class DefaultedHttpParams extends AbstractHttpParams
{
    private final HttpParams local;
    private final HttpParams defaults;
    
    public DefaultedHttpParams(final HttpParams local, final HttpParams defaults) {
        this.local = Args.notNull(local, "Local HTTP parameters");
        this.defaults = defaults;
    }
    
    public HttpParams copy() {
        final HttpParams clone = this.local.copy();
        return new DefaultedHttpParams(clone, this.defaults);
    }
    
    public Object getParameter(final String name) {
        Object obj = this.local.getParameter(name);
        if (obj == null && this.defaults != null) {
            obj = this.defaults.getParameter(name);
        }
        return obj;
    }
    
    public boolean removeParameter(final String name) {
        return this.local.removeParameter(name);
    }
    
    public HttpParams setParameter(final String name, final Object value) {
        return this.local.setParameter(name, value);
    }
    
    public HttpParams getDefaults() {
        return this.defaults;
    }
    
    @Override
    public Set<String> getNames() {
        final Set<String> combined = new HashSet<String>(this.getNames(this.defaults));
        combined.addAll(this.getNames(this.local));
        return combined;
    }
    
    public Set<String> getDefaultNames() {
        return new HashSet<String>(this.getNames(this.defaults));
    }
    
    public Set<String> getLocalNames() {
        return new HashSet<String>(this.getNames(this.local));
    }
    
    private Set<String> getNames(final HttpParams params) {
        if (params instanceof HttpParamsNames) {
            return ((HttpParamsNames)params).getNames();
        }
        throw new UnsupportedOperationException("HttpParams instance does not implement HttpParamsNames");
    }
}
