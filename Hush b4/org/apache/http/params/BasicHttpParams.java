// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.params;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import org.apache.http.annotation.ThreadSafe;
import java.io.Serializable;

@Deprecated
@ThreadSafe
public class BasicHttpParams extends AbstractHttpParams implements Serializable, Cloneable
{
    private static final long serialVersionUID = -7086398485908701455L;
    private final Map<String, Object> parameters;
    
    public BasicHttpParams() {
        this.parameters = new ConcurrentHashMap<String, Object>();
    }
    
    public Object getParameter(final String name) {
        return this.parameters.get(name);
    }
    
    public HttpParams setParameter(final String name, final Object value) {
        if (name == null) {
            return this;
        }
        if (value != null) {
            this.parameters.put(name, value);
        }
        else {
            this.parameters.remove(name);
        }
        return this;
    }
    
    public boolean removeParameter(final String name) {
        if (this.parameters.containsKey(name)) {
            this.parameters.remove(name);
            return true;
        }
        return false;
    }
    
    public void setParameters(final String[] names, final Object value) {
        for (final String name : names) {
            this.setParameter(name, value);
        }
    }
    
    public boolean isParameterSet(final String name) {
        return this.getParameter(name) != null;
    }
    
    public boolean isParameterSetLocally(final String name) {
        return this.parameters.get(name) != null;
    }
    
    public void clear() {
        this.parameters.clear();
    }
    
    public HttpParams copy() {
        try {
            return (HttpParams)this.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new UnsupportedOperationException("Cloning not supported");
        }
    }
    
    public Object clone() throws CloneNotSupportedException {
        final BasicHttpParams clone = (BasicHttpParams)super.clone();
        this.copyParams(clone);
        return clone;
    }
    
    public void copyParams(final HttpParams target) {
        for (final Map.Entry<String, Object> me : this.parameters.entrySet()) {
            target.setParameter(me.getKey(), me.getValue());
        }
    }
    
    @Override
    public Set<String> getNames() {
        return new HashSet<String>(this.parameters.keySet());
    }
}
