package io.netty.handler.codec.http;

public abstract interface HttpRequest
  extends HttpMessage
{
  public abstract HttpMethod method();
  
  public abstract HttpRequest setMethod(HttpMethod paramHttpMethod);
  
  public abstract String uri();
  
  public abstract HttpRequest setUri(String paramString);
  
  public abstract HttpRequest setProtocolVersion(HttpVersion paramHttpVersion);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\HttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */