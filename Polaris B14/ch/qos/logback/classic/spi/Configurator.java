package ch.qos.logback.classic.spi;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.spi.ContextAware;

public abstract interface Configurator
  extends ContextAware
{
  public abstract void configure(LoggerContext paramLoggerContext);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\spi\Configurator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */