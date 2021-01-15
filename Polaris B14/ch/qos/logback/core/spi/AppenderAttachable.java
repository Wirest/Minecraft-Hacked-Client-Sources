package ch.qos.logback.core.spi;

import ch.qos.logback.core.Appender;
import java.util.Iterator;

public abstract interface AppenderAttachable<E>
{
  public abstract void addAppender(Appender<E> paramAppender);
  
  public abstract Iterator<Appender<E>> iteratorForAppenders();
  
  public abstract Appender<E> getAppender(String paramString);
  
  public abstract boolean isAttached(Appender<E> paramAppender);
  
  public abstract void detachAndStopAllAppenders();
  
  public abstract boolean detachAppender(Appender<E> paramAppender);
  
  public abstract boolean detachAppender(String paramString);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\spi\AppenderAttachable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */