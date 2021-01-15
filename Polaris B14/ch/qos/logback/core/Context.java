package ch.qos.logback.core;

import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.spi.PropertyContainer;
import ch.qos.logback.core.status.StatusManager;
import java.util.Map;
import java.util.concurrent.ExecutorService;

public abstract interface Context
  extends PropertyContainer
{
  public abstract StatusManager getStatusManager();
  
  public abstract Object getObject(String paramString);
  
  public abstract void putObject(String paramString, Object paramObject);
  
  public abstract String getProperty(String paramString);
  
  public abstract void putProperty(String paramString1, String paramString2);
  
  public abstract Map<String, String> getCopyOfPropertyMap();
  
  public abstract String getName();
  
  public abstract void setName(String paramString);
  
  public abstract long getBirthTime();
  
  public abstract Object getConfigurationLock();
  
  public abstract ExecutorService getExecutorService();
  
  public abstract void register(LifeCycle paramLifeCycle);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\Context.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */