package ch.qos.logback.classic.selector;

import ch.qos.logback.classic.LoggerContext;
import java.util.List;

public abstract interface ContextSelector
{
  public abstract LoggerContext getLoggerContext();
  
  public abstract LoggerContext getLoggerContext(String paramString);
  
  public abstract LoggerContext getDefaultLoggerContext();
  
  public abstract LoggerContext detachLoggerContext(String paramString);
  
  public abstract List<String> getContextNames();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\selector\ContextSelector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */