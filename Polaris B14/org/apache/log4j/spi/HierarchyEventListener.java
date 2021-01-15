package org.apache.log4j.spi;

import org.apache.log4j.Appender;
import org.apache.log4j.Category;

public abstract interface HierarchyEventListener
{
  public abstract void addAppenderEvent(Category paramCategory, Appender paramAppender);
  
  public abstract void removeAppenderEvent(Category paramCategory, Appender paramAppender);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\apache\log4j\spi\HierarchyEventListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */