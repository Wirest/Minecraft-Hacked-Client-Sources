// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

public class HttpPipelineRequest
{
    private HttpRequest httpRequest;
    private HttpListener httpListener;
    private boolean closed;
    
    public HttpPipelineRequest(final HttpRequest p_i58_1_, final HttpListener p_i58_2_) {
        this.httpRequest = null;
        this.httpListener = null;
        this.closed = false;
        this.httpRequest = p_i58_1_;
        this.httpListener = p_i58_2_;
    }
    
    public HttpRequest getHttpRequest() {
        return this.httpRequest;
    }
    
    public HttpListener getHttpListener() {
        return this.httpListener;
    }
    
    public boolean isClosed() {
        return this.closed;
    }
    
    public void setClosed(final boolean p_setClosed_1_) {
        this.closed = p_setClosed_1_;
    }
}
