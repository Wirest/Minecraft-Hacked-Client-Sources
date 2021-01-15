package ch.qos.logback.classic.spi;

import ch.qos.logback.classic.Level;
import ch.qos.logback.core.spi.DeferredProcessingAware;
import java.util.Map;
import org.slf4j.Marker;

public abstract interface ILoggingEvent
  extends DeferredProcessingAware
{
  public abstract String getThreadName();
  
  public abstract Level getLevel();
  
  public abstract String getMessage();
  
  public abstract Object[] getArgumentArray();
  
  public abstract String getFormattedMessage();
  
  public abstract String getLoggerName();
  
  public abstract LoggerContextVO getLoggerContextVO();
  
  public abstract IThrowableProxy getThrowableProxy();
  
  public abstract StackTraceElement[] getCallerData();
  
  public abstract boolean hasCallerData();
  
  public abstract Marker getMarker();
  
  public abstract Map<String, String> getMDCPropertyMap();
  
  /**
   * @deprecated
   */
  public abstract Map<String, String> getMdc();
  
  public abstract long getTimeStamp();
  
  public abstract void prepareForDeferredProcessing();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\spi\ILoggingEvent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */