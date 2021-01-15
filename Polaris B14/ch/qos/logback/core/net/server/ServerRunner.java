package ch.qos.logback.core.net.server;

import ch.qos.logback.core.spi.ContextAware;
import java.io.IOException;

public abstract interface ServerRunner<T extends Client>
  extends ContextAware, Runnable
{
  public abstract boolean isRunning();
  
  public abstract void stop()
    throws IOException;
  
  public abstract void accept(ClientVisitor<T> paramClientVisitor);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\net\server\ServerRunner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */