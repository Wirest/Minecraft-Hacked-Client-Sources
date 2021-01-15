package ch.qos.logback.core.rolling;

import ch.qos.logback.core.rolling.helper.ArchiveRemover;
import ch.qos.logback.core.spi.ContextAware;

public abstract interface TimeBasedFileNamingAndTriggeringPolicy<E>
  extends TriggeringPolicy<E>, ContextAware
{
  public abstract void setTimeBasedRollingPolicy(TimeBasedRollingPolicy<E> paramTimeBasedRollingPolicy);
  
  public abstract String getElapsedPeriodsFileName();
  
  public abstract String getCurrentPeriodsFileNameWithoutCompressionSuffix();
  
  public abstract ArchiveRemover getArchiveRemover();
  
  public abstract long getCurrentTime();
  
  public abstract void setCurrentTime(long paramLong);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\rolling\TimeBasedFileNamingAndTriggeringPolicy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */