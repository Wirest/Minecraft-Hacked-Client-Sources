// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.client;

import org.apache.http.util.Args;
import org.apache.http.params.HttpParams;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.params.AbstractHttpParams;

@Deprecated
@NotThreadSafe
public class ClientParamsStack extends AbstractHttpParams
{
    protected final HttpParams applicationParams;
    protected final HttpParams clientParams;
    protected final HttpParams requestParams;
    protected final HttpParams overrideParams;
    
    public ClientParamsStack(final HttpParams aparams, final HttpParams cparams, final HttpParams rparams, final HttpParams oparams) {
        this.applicationParams = aparams;
        this.clientParams = cparams;
        this.requestParams = rparams;
        this.overrideParams = oparams;
    }
    
    public ClientParamsStack(final ClientParamsStack stack) {
        this(stack.getApplicationParams(), stack.getClientParams(), stack.getRequestParams(), stack.getOverrideParams());
    }
    
    public ClientParamsStack(final ClientParamsStack stack, final HttpParams aparams, final HttpParams cparams, final HttpParams rparams, final HttpParams oparams) {
        this((aparams != null) ? aparams : stack.getApplicationParams(), (cparams != null) ? cparams : stack.getClientParams(), (rparams != null) ? rparams : stack.getRequestParams(), (oparams != null) ? oparams : stack.getOverrideParams());
    }
    
    public final HttpParams getApplicationParams() {
        return this.applicationParams;
    }
    
    public final HttpParams getClientParams() {
        return this.clientParams;
    }
    
    public final HttpParams getRequestParams() {
        return this.requestParams;
    }
    
    public final HttpParams getOverrideParams() {
        return this.overrideParams;
    }
    
    public Object getParameter(final String name) {
        Args.notNull(name, "Parameter name");
        Object result = null;
        if (this.overrideParams != null) {
            result = this.overrideParams.getParameter(name);
        }
        if (result == null && this.requestParams != null) {
            result = this.requestParams.getParameter(name);
        }
        if (result == null && this.clientParams != null) {
            result = this.clientParams.getParameter(name);
        }
        if (result == null && this.applicationParams != null) {
            result = this.applicationParams.getParameter(name);
        }
        return result;
    }
    
    public HttpParams setParameter(final String name, final Object value) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Setting parameters in a stack is not supported.");
    }
    
    public boolean removeParameter(final String name) {
        throw new UnsupportedOperationException("Removing parameters in a stack is not supported.");
    }
    
    public HttpParams copy() {
        return this;
    }
}
