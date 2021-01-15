package ch.qos.logback.core;

import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.spi.LifeCycle;

public abstract interface Layout<E>
  extends ContextAware, LifeCycle
{
  public abstract String doLayout(E paramE);
  
  public abstract String getFileHeader();
  
  public abstract String getPresentationHeader();
  
  public abstract String getPresentationFooter();
  
  public abstract String getFileFooter();
  
  public abstract String getContentType();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\Layout.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */