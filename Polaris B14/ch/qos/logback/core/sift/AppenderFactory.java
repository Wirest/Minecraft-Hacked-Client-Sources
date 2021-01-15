package ch.qos.logback.core.sift;

import ch.qos.logback.core.Appender;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.joran.spi.JoranException;

public abstract interface AppenderFactory<E>
{
  public abstract Appender<E> buildAppender(Context paramContext, String paramString)
    throws JoranException;
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\sift\AppenderFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */