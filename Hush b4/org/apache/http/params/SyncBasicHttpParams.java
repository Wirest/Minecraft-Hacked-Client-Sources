// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.params;

import org.apache.http.annotation.ThreadSafe;

@Deprecated
@ThreadSafe
public class SyncBasicHttpParams extends BasicHttpParams
{
    private static final long serialVersionUID = 5387834869062660642L;
    
    @Override
    public synchronized boolean removeParameter(final String name) {
        return super.removeParameter(name);
    }
    
    @Override
    public synchronized HttpParams setParameter(final String name, final Object value) {
        return super.setParameter(name, value);
    }
    
    @Override
    public synchronized Object getParameter(final String name) {
        return super.getParameter(name);
    }
    
    @Override
    public synchronized boolean isParameterSet(final String name) {
        return super.isParameterSet(name);
    }
    
    @Override
    public synchronized boolean isParameterSetLocally(final String name) {
        return super.isParameterSetLocally(name);
    }
    
    @Override
    public synchronized void setParameters(final String[] names, final Object value) {
        super.setParameters(names, value);
    }
    
    @Override
    public synchronized void clear() {
        super.clear();
    }
    
    @Override
    public synchronized Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
