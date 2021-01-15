package ch.qos.logback.core.net.ssl;

public abstract interface SSLConfigurable
{
  public abstract String[] getDefaultProtocols();
  
  public abstract String[] getSupportedProtocols();
  
  public abstract void setEnabledProtocols(String[] paramArrayOfString);
  
  public abstract String[] getDefaultCipherSuites();
  
  public abstract String[] getSupportedCipherSuites();
  
  public abstract void setEnabledCipherSuites(String[] paramArrayOfString);
  
  public abstract void setNeedClientAuth(boolean paramBoolean);
  
  public abstract void setWantClientAuth(boolean paramBoolean);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\net\ssl\SSLConfigurable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */