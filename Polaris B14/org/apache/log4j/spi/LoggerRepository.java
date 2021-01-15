package org.apache.log4j.spi;

import java.util.Enumeration;
import org.apache.log4j.Appender;
import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public abstract interface LoggerRepository
{
  public abstract void addHierarchyEventListener(HierarchyEventListener paramHierarchyEventListener);
  
  public abstract boolean isDisabled(int paramInt);
  
  public abstract void setThreshold(Level paramLevel);
  
  public abstract void setThreshold(String paramString);
  
  public abstract void emitNoAppenderWarning(Category paramCategory);
  
  public abstract Level getThreshold();
  
  public abstract Logger getLogger(String paramString);
  
  public abstract Logger getLogger(String paramString, LoggerFactory paramLoggerFactory);
  
  public abstract Logger getRootLogger();
  
  public abstract Logger exists(String paramString);
  
  public abstract void shutdown();
  
  public abstract Enumeration getCurrentLoggers();
  
  public abstract Enumeration getCurrentCategories();
  
  public abstract void fireAddAppenderEvent(Category paramCategory, Appender paramAppender);
  
  public abstract void resetConfiguration();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\apache\log4j\spi\LoggerRepository.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */