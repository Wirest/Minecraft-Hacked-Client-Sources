package ch.qos.logback.classic.spi;

public abstract interface IThrowableProxy
{
  public abstract String getMessage();
  
  public abstract String getClassName();
  
  public abstract StackTraceElementProxy[] getStackTraceElementProxyArray();
  
  public abstract int getCommonFrames();
  
  public abstract IThrowableProxy getCause();
  
  public abstract IThrowableProxy[] getSuppressed();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\spi\IThrowableProxy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */