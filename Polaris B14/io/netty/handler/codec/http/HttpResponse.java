package io.netty.handler.codec.http;

public abstract interface HttpResponse
  extends HttpMessage
{
  public abstract HttpResponseStatus status();
  
  public abstract HttpResponse setStatus(HttpResponseStatus paramHttpResponseStatus);
  
  public abstract HttpResponse setProtocolVersion(HttpVersion paramHttpVersion);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\HttpResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */