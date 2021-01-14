package optifine;

public abstract interface HttpListener {
    public abstract void finished(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse);

    public abstract void failed(HttpRequest paramHttpRequest, Exception paramException);
}




