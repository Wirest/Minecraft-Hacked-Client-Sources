// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.auth.params;

import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpAbstractParamBean;

@Deprecated
public class AuthParamBean extends HttpAbstractParamBean
{
    public AuthParamBean(final HttpParams params) {
        super(params);
    }
    
    public void setCredentialCharset(final String charset) {
        AuthParams.setCredentialCharset(this.params, charset);
    }
}
