package ch.qos.logback.core;

import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.spi.FilterAttachable;
import ch.qos.logback.core.spi.LifeCycle;

public abstract interface Appender<E>
  extends LifeCycle, ContextAware, FilterAttachable<E>
{
  public abstract String getName();
  
  public abstract void doAppend(E paramE)
    throws LogbackException;
  
  public abstract void setName(String paramString);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\Appender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */