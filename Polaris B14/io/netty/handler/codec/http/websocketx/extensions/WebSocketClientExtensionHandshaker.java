package io.netty.handler.codec.http.websocketx.extensions;

public abstract interface WebSocketClientExtensionHandshaker
{
  public abstract WebSocketExtensionData newRequestData();
  
  public abstract WebSocketClientExtension handshakeExtension(WebSocketExtensionData paramWebSocketExtensionData);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\websocketx\extensions\WebSocketClientExtensionHandshaker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */