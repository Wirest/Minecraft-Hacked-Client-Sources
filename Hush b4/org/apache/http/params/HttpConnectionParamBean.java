// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.params;

@Deprecated
public class HttpConnectionParamBean extends HttpAbstractParamBean
{
    public HttpConnectionParamBean(final HttpParams params) {
        super(params);
    }
    
    public void setSoTimeout(final int soTimeout) {
        HttpConnectionParams.setSoTimeout(this.params, soTimeout);
    }
    
    public void setTcpNoDelay(final boolean tcpNoDelay) {
        HttpConnectionParams.setTcpNoDelay(this.params, tcpNoDelay);
    }
    
    public void setSocketBufferSize(final int socketBufferSize) {
        HttpConnectionParams.setSocketBufferSize(this.params, socketBufferSize);
    }
    
    public void setLinger(final int linger) {
        HttpConnectionParams.setLinger(this.params, linger);
    }
    
    public void setConnectionTimeout(final int connectionTimeout) {
        HttpConnectionParams.setConnectionTimeout(this.params, connectionTimeout);
    }
    
    public void setStaleCheckingEnabled(final boolean staleCheckingEnabled) {
        HttpConnectionParams.setStaleCheckingEnabled(this.params, staleCheckingEnabled);
    }
}
