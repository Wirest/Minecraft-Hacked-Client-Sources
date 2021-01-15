package io.netty.handler.ssl;

import java.util.List;
import java.util.Set;
import javax.net.ssl.SSLEngine;

public abstract interface JdkApplicationProtocolNegotiator
  extends ApplicationProtocolNegotiator
{
  public abstract SslEngineWrapperFactory wrapperFactory();
  
  public abstract ProtocolSelectorFactory protocolSelectorFactory();
  
  public abstract ProtocolSelectionListenerFactory protocolListenerFactory();
  
  public static abstract interface ProtocolSelectionListenerFactory
  {
    public abstract JdkApplicationProtocolNegotiator.ProtocolSelectionListener newListener(SSLEngine paramSSLEngine, List<String> paramList);
  }
  
  public static abstract interface ProtocolSelectorFactory
  {
    public abstract JdkApplicationProtocolNegotiator.ProtocolSelector newSelector(SSLEngine paramSSLEngine, Set<String> paramSet);
  }
  
  public static abstract interface ProtocolSelectionListener
  {
    public abstract void unsupported();
    
    public abstract void selected(String paramString)
      throws Exception;
  }
  
  public static abstract interface ProtocolSelector
  {
    public abstract void unsupported();
    
    public abstract String select(List<String> paramList)
      throws Exception;
  }
  
  public static abstract interface SslEngineWrapperFactory
  {
    public abstract SSLEngine wrapSslEngine(SSLEngine paramSSLEngine, JdkApplicationProtocolNegotiator paramJdkApplicationProtocolNegotiator, boolean paramBoolean);
  }
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\ssl\JdkApplicationProtocolNegotiator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */