package ch.qos.logback.core.spi;

import java.util.Collection;
import java.util.Set;

public abstract interface ComponentTracker<C>
{
  public static final int DEFAULT_TIMEOUT = 1800000;
  public static final int DEFAULT_MAX_COMPONENTS = Integer.MAX_VALUE;
  
  public abstract int getComponentCount();
  
  public abstract C find(String paramString);
  
  public abstract C getOrCreate(String paramString, long paramLong);
  
  public abstract void removeStaleComponents(long paramLong);
  
  public abstract void endOfLife(String paramString);
  
  public abstract Collection<C> allComponents();
  
  public abstract Set<String> allKeys();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\spi\ComponentTracker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */