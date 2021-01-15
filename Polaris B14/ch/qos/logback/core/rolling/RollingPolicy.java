package ch.qos.logback.core.rolling;

import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.rolling.helper.CompressionMode;
import ch.qos.logback.core.spi.LifeCycle;

public abstract interface RollingPolicy
  extends LifeCycle
{
  public abstract void rollover()
    throws RolloverFailure;
  
  public abstract String getActiveFileName();
  
  public abstract CompressionMode getCompressionMode();
  
  public abstract void setParent(FileAppender paramFileAppender);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\rolling\RollingPolicy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */