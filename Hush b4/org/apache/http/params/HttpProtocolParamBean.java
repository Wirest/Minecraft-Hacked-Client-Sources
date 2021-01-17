// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.params;

import org.apache.http.ProtocolVersion;
import org.apache.http.HttpVersion;

@Deprecated
public class HttpProtocolParamBean extends HttpAbstractParamBean
{
    public HttpProtocolParamBean(final HttpParams params) {
        super(params);
    }
    
    public void setHttpElementCharset(final String httpElementCharset) {
        HttpProtocolParams.setHttpElementCharset(this.params, httpElementCharset);
    }
    
    public void setContentCharset(final String contentCharset) {
        HttpProtocolParams.setContentCharset(this.params, contentCharset);
    }
    
    public void setVersion(final HttpVersion version) {
        HttpProtocolParams.setVersion(this.params, version);
    }
    
    public void setUserAgent(final String userAgent) {
        HttpProtocolParams.setUserAgent(this.params, userAgent);
    }
    
    public void setUseExpectContinue(final boolean useExpectContinue) {
        HttpProtocolParams.setUseExpectContinue(this.params, useExpectContinue);
    }
}
