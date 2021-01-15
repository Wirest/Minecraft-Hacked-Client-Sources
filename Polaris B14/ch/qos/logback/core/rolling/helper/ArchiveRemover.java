package ch.qos.logback.core.rolling.helper;

import ch.qos.logback.core.spi.ContextAware;
import java.util.Date;

public abstract interface ArchiveRemover
  extends ContextAware
{
  public abstract void clean(Date paramDate);
  
  public abstract void setMaxHistory(int paramInt);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\rolling\helper\ArchiveRemover.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */