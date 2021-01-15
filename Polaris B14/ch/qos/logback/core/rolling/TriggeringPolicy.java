package ch.qos.logback.core.rolling;

import ch.qos.logback.core.spi.LifeCycle;
import java.io.File;

public abstract interface TriggeringPolicy<E>
  extends LifeCycle
{
  public abstract boolean isTriggeringEvent(File paramFile, E paramE);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\rolling\TriggeringPolicy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */