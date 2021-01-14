package optifine;

public class HttpPipelineRequest {
    private HttpRequest httpRequest = null;
    private HttpListener httpListener = null;
    private boolean closed = false;

    public HttpPipelineRequest(HttpRequest paramHttpRequest, HttpListener paramHttpListener) {
        this.httpRequest = paramHttpRequest;
        this.httpListener = paramHttpListener;
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

    public void setClosed(boolean paramBoolean) {
        this.closed = paramBoolean;
    }
}




